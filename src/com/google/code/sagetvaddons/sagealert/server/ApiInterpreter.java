/*
 *      Copyright 2010-2011 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sagealert.server;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;

/**
 * @author dbattams
 *
 */
final public class ApiInterpreter {
	static private final Logger LOG = Logger.getLogger(ApiInterpreter.class);
	static private final class ApiMap extends HashMap<String, Object> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ApiMap(String call, Object val) {
			put(call, val);
		}
	}
	static private final class SageAlertStreamTokenizer extends StreamTokenizer {

		private boolean flagged = false;

		private SageAlertStreamTokenizer(Reader r) {
			super(r);
		}

		@Override
		public int nextToken() throws IOException {
			flagged = false;
			return super.nextToken();
		}

		public int flaggedNextToken() throws IOException {
			flagged = true;
			return nextToken();
		}

		public void flaggedPushBack() {
			if(flagged) {
				flagged = false;
				pushBack();
			}
		}

		public void clearFlag() {
			flagged = false;
		}
	}


	private ArrayList<Object> src;
	private SageAlertStreamTokenizer toks;
	private String interpretedMsg;
	private int rightParenthesisCount;
	private boolean pendingComma;
	private String originalMsg;

	public ApiInterpreter(Object[] src, String msg) {
		if(src == null)
			src = new Object[0];
		this.src = new ArrayList<Object>();
		this.src.addAll(Arrays.asList(src));
		interpretedMsg = msg;
		toks = new SageAlertStreamTokenizer(new StringReader(msg));
		rightParenthesisCount = 0;
		pendingComma = false;
		originalMsg = msg;
	}

	private void resetTokenizer() {
		toks.resetSyntax();
		toks.slashSlashComments(false);
		toks.slashStarComments(false);
		toks.eolIsSignificant(false);
		toks.lowerCaseMode(false);
		toks.whitespaceChars(0, 32);		
	}

	private void setForObjCall() {
		resetTokenizer();
		toks.wordChars('$', '$');
		toks.wordChars('\\', '\\');
	}

	private void setForObjId() {
		resetTokenizer();
		toks.wordChars('0', '9');
		toks.wordChars('a', 'z');
	}

	private void setForObjOp() {
		resetTokenizer();
		toks.wordChars('.', '.');
	}

	private void setForMethodName() {
		resetTokenizer();
		toks.wordChars('A', 'Z');
		toks.wordChars('a', 'z');
		toks.wordChars('0', '9');
	}

	private void setForLeftParenthesis() {
		resetTokenizer();
		toks.wordChars('(', '(');
	}

	private void setForArg() {
		resetTokenizer();
		// An arg is either a quoted string, a number, a boolean or an obj call - all covered below
		toks.wordChars('$', '$');
		toks.wordChars('r', 'u'); // Includes r, s, t, u
		toks.wordChars('e', 'f');
		toks.wordChars('a', 'a');
		toks.wordChars('l', 'l');
		toks.wordChars('0', '9');
		toks.wordChars('-', '-');
	}

	private void setForArgPrefix() {
		resetTokenizer();
		toks.quoteChar('"');
		toks.wordChars('$', '$');
		toks.wordChars('-', '-');
		toks.wordChars('0', '9');
		toks.wordChars('a', 'z');
	}

	private void setForArgDelimiter() {
		resetTokenizer();
		toks.wordChars(',', ',');
		toks.wordChars(')', ')');
	}

	static private final boolean isStartOfArgToken(String tok) {
		LOG.trace("Checking tok: " + tok);
		return tok.matches("\\$(?:\\d+|[a-z]+)|[a-z]+|-{0,1}\\d+");
	}
	
	private Object readArg() throws IOException {
		int nextTok;
		String nextWord;
		setForArgPrefix();
		nextTok = toks.nextToken();
		LOG.trace("Read prefix tok: " + nextTok);
		nextWord = toks.ttype == StreamTokenizer.TT_WORD ? toks.sval : "";
		if(nextTok == '"') {
			nextTok = 0;
			nextWord = toks.sval;
		} else if(nextTok == '[') {
			List<Object> array = new ArrayList<Object>();
			Object ele;
			while((ele = readArg()) != null) {
				array.add(ele);
				if(toks.nextToken() != ',') {
					toks.pushBack();
					continue;
				}
			}
			if(toks.nextToken() != ']') {
				LOG.error("Did not find ']' to close array arg!");
				return null;
			}
			LOG.trace("Returning array arg: " + array);
			return new ApiMap("\\[.*\\]", array.toArray());
		} else if(nextTok != StreamTokenizer.TT_WORD || !isStartOfArgToken(String.valueOf(toks.sval))) {
			LOG.trace("Tok is no good! [" + (nextTok < 0 ? toks.sval : (char)nextTok) + "]");
			toks.pushBack();
			return null;
		} else if(nextWord.startsWith("$")) {
			LOG.trace("Tok is an API call...");
			setForObjId();
			if(toks.nextToken() == '.') {
				//toks.pushBack();
				return findApiCall(nextWord.substring(1));
			} else if(toks.ttype == ',' || toks.ttype == ')') { // Naked reference to an object
				toks.pushBack();
				return new ApiMap(Pattern.quote(nextWord), getSrcObj(nextWord.substring(1)));
			} else if(toks.ttype == StreamTokenizer.TT_WORD) {
				int peekTok = toks.nextToken();
				toks.pushBack();
				if(peekTok == ',' || peekTok == ')')
					return new ApiMap(Pattern.quote(nextWord + toks.sval), getSrcObj(nextWord.substring(1) + toks.sval));
				return findApiCall(nextWord.substring(1) + toks.sval);
			} else {
				LOG.error("Invalid API call as arg!");
				return null;
			}
		}
		LOG.trace("Tok is other...");
		try {
			if(toks.ttype != '"' && (nextWord.equals("f") || nextWord.equals("t"))) {
				setForArg();
				toks.nextToken();
				if(toks.ttype != StreamTokenizer.TT_WORD) {
					LOG.error("Rejected rest of token: " + toks.ttype);
					toks.pushBack();
					return null;
				}
			}
		} catch (IOException e) {
			LOG.error("Token IO Error", e);
			return null;
		}
		LOG.trace("nextWord = " + nextWord);
		//String fullTok = (!toks.sval.matches("-{0,1}\\d+") ? nextWord : "") + toks.sval;
		//String fullTok = !toks.sval.matches("-{0,1}\\d+") ? nextWord : "";
		String fullTok = nextWord;
		LOG.trace("fullTok = " + fullTok);

		if(toks.ttype == '"')
			return new String(fullTok);
		else if(toks.ttype == StreamTokenizer.TT_WORD) {
			if(fullTok.matches("-{0,1}\\d+"))
				return Long.parseLong(fullTok);
			else if(fullTok.equals("true"))
				return Boolean.TRUE;
			else if(fullTok.equals("false"))
				return Boolean.FALSE;
			else {
				LOG.error("Invalid boolean constant; skipping API call! [" + fullTok + "]");
				return null;
			}
		} else {
			LOG.error("Returning null! [" + ((char)toks.ttype) + "]");
			return null;
		}
	}

	private ApiMap findApiCall(String objLabel) throws IOException {
		StringBuilder apiCall = new StringBuilder("$");
		StringBuilder apiCallRegex = new StringBuilder("\\$");
		int objId;
		String objName, methodName;
		Collection<Object> args = new ArrayList<Object>();
		setForObjCall();
		LOG.trace("objLabel = " + String.valueOf(objLabel));
		while(objLabel != null || toks.nextToken() != StreamTokenizer.TT_EOF) {
			//LOG.trace("Looking for an API call...");
			try {
				if(objLabel == null && toks.ttype != '$' && (toks.ttype != StreamTokenizer.TT_WORD || toks.sval.length() > 1 || toks.sval.equals("\\"))) {
					LOG.trace("Didn't find '$' [" + (char)toks.ttype + "]");
					continue;
				}
				setForObjId();
				if(objLabel == null && toks.nextToken() != StreamTokenizer.TT_WORD) {
					LOG.error("Didn't find obj id/name");
					continue;
				} else if(objLabel != null)
					toks.sval = objLabel;				
				if(toks.sval.matches("-{0,1}\\d+")) {
					objId = Integer.parseInt(toks.sval);
					objName = "";
					apiCall.append(objId + ".");
					apiCallRegex.append(Pattern.quote(objId + "."));
				}
				else {
					objId = -1;
					objName = toks.sval;
					apiCall.append(objName + ".");
					apiCallRegex.append(Pattern.quote(objName + "."));
				}
				setForObjOp();
				if((objLabel == null && toks.nextToken() != StreamTokenizer.TT_WORD) || (toks.ttype != StreamTokenizer.TT_WORD && toks.ttype != '.')) {
					LOG.error("Didn't find '.' [" + toks.ttype + "]");
					continue;
				}
				objLabel = null;
				setForMethodName();
				if(toks.nextToken() != StreamTokenizer.TT_WORD) {
					LOG.error("Didn't find method name [" + (char)toks.ttype + "]");
					continue;
				}
				methodName = toks.sval;
				apiCall.append(methodName + "(");
				apiCallRegex.append(Pattern.quote(methodName) + "\\s*" + "\\(" + "\\s*");
				setForLeftParenthesis();
				if(toks.nextToken() != StreamTokenizer.TT_WORD) {
					LOG.error("Didn't find left parenthesis");
					continue;
				}
				Object arg = readArg();
				if(arg instanceof ApiMap) {
					apiCall.append(((ApiMap) arg).keySet().toArray()[0].toString());
					apiCallRegex.append("\\s*" + ((ApiMap) arg).keySet().toArray()[0].toString() + "\\s*");
					args.add(((ApiMap) arg).values().toArray()[0]);
					LOG.trace("Read arg 0: " + String.valueOf(((ApiMap)arg).values().toArray()[0]));
				} else if(arg instanceof String) {
					apiCall.append(String.valueOf(arg));
					apiCallRegex.append("\\s*\"" + Pattern.quote(String.valueOf(arg)) + "\"\\s*");
					args.add(arg);
					LOG.trace("Read arg 0: " + arg.toString());
				} else if(arg != null) {
					if(arg instanceof Number)
						arg = ((Number)arg).longValue();
					apiCall.append(String.valueOf(arg));
					apiCallRegex.append("\\s*" + Pattern.quote(String.valueOf(arg)) + "\\s*");
					args.add(arg);
					LOG.trace("Read arg 0: " + arg.toString());
				} else {
					apiCallRegex.append("\\s*");
					LOG.trace("Read arg 0 is null!");
				}
				setForArgDelimiter();
				boolean argListFailed = false;
				int argNum = 1;
				while(!argListFailed && (pendingComma || (toks.flaggedNextToken() == StreamTokenizer.TT_WORD && toks.sval.equals(",")) || toks.ttype == ',')) {
					toks.clearFlag();
					LOG.trace("Looking for arg #" + argNum);
					if(pendingComma) {
						pendingComma = false;
						LOG.trace("Removed pending comma flag!");
					}
					apiCall.append(",");
					apiCallRegex.append("\\s*,\\s*");
					Object arg1 = readArg();
					if(arg1 == null) {
						argListFailed = true;
						LOG.error("Failed to read arg #" + argNum + "!");
					} else if(arg1 instanceof ApiMap) {
						apiCall.append(((ApiMap)arg1).keySet().toArray()[0].toString());
						apiCallRegex.append(((ApiMap) arg1).keySet().toArray()[0].toString());
						args.add(((ApiMap) arg1).values().toArray()[0]);
						LOG.trace("Read arg: " + ((ApiMap)arg1).keySet().toArray()[0].toString());
					} else if(arg1 instanceof String) {
						apiCall.append(String.valueOf(arg1));
						apiCallRegex.append("\\s*\"" + Pattern.quote(String.valueOf(arg1)) + "\"\\s*");
						args.add(arg1);
						LOG.trace("Read arg: " + arg1.toString());
					} else {
						if(arg1 instanceof Number)
							arg1 = ((Number)arg1).longValue();
						apiCall.append(String.valueOf(arg1));
						apiCallRegex.append("\\s*" + Pattern.quote(String.valueOf(arg1)) + "\\s*");
						args.add(arg1);
						LOG.trace("Read arg: " + arg1.toString());
					}
					setForArgDelimiter();
					++argNum;
				}
				toks.flaggedPushBack();

				if(rightParenthesisCount > 0) {
					apiCall.append(")");
					apiCallRegex.append("\\s*\\)");
					--rightParenthesisCount;
					LOG.trace("Reduced right paren count to " + rightParenthesisCount);
				} else if((toks.ttype == StreamTokenizer.TT_WORD && toks.sval.matches("\\)+,{0,1}"))) {
					apiCall.append(")");
					apiCallRegex.append("\\s*\\)");
					for(char c : toks.sval.substring(1).toCharArray())
						if(c == ')') {
							++rightParenthesisCount;
							LOG.trace("Increased right paren count to " + rightParenthesisCount);
						} else {
							pendingComma = true;
							LOG.trace("Set pending comma to true");
						}
				} else if(toks.ttype == ')') {
					apiCall.append(")");
					apiCallRegex.append("\\s*\\)");
				} else if(toks.ttype == ',') { 
					pendingComma = true;
				} else {
					LOG.error("Didn't find right parenthesis [" + String.valueOf(toks.ttype) + " :: " + toks.sval + "]");
					continue;
				}
				LOG.trace("Found API call: " + apiCall.toString());
				return new ApiMap(apiCallRegex.toString(), reflect(getSrcObj(objName, objId), methodName, args));
			} finally {
				setForObjCall();
			}
		}
		LOG.trace("Returned null!");
		return null;
	}

	private Object getSrcObj(String name) {
		if(name == null || name.length() == 0) {
			LOG.error("Invalid object reference! [" + name + "]");
			return null;
		}
		if(name.matches("\\d+"))
			return getSrcObj(null, Integer.parseInt(name));
		return getSrcObj(name, -1);
	}
	
	private Object getSrcObj(String objName, int objId) {
		if(objName != null && objName.length() > 0)
			return getGlobalObj(objName);
		if(src.size() <= objId) {
			LOG.error("Reference to object id " + objId + " is invalid!");
			return null;
		}
		return src.get(objId);
	}

	private Object getGlobalObj(String objName) {
		Object obj = Globals.get().get(objName);
		if(obj == null)
			LOG.error("Reference to object name " + objName + " is invalid!");
		return obj;
	}

	static private Object reflect(Object src, String methodName, Collection<Object> args) {
		StringBuilder argTypesMsg = new StringBuilder("(");
		Collection<Class<?>> argTypes = new ArrayList<Class<?>>();
		for(Object o : args) {
			argTypes.add(o != null ? o.getClass() : Object.class);
			argTypesMsg.append((o != null ? o.getClass().getName() : Object.class.getName()) + ",");
		}
		if(argTypes.size() > 0)
			argTypesMsg.deleteCharAt(argTypesMsg.length() - 1);
		argTypesMsg.append(")");
		try {
			if(src != null) {
				LOG.trace("Attempting to call " + methodName + argTypesMsg.toString() + " on object of type " + src.getClass().getName());
				return MethodUtils.invokeMethod(src, methodName, args.toArray(), argTypes.toArray(new Class<?>[argTypes.size()]));
			} else
				LOG.error("Received null source object!");
		} catch (SecurityException e) {
			LOG.error("SecurityException", e);
		} catch (NoSuchMethodException e) {
			LOG.error("NoSuchMethod", e);
		} catch (IllegalArgumentException e) {
			LOG.error("IllegalArgument", e);
		} catch (IllegalAccessException e) {
			LOG.error("IllegalAccess", e);
		} catch (InvocationTargetException e) {
			LOG.error("InvocationError", e);
		}
		return ("$$." + methodName + argTypesMsg.toString());
	}

	public String interpret() {
		ApiMap map = null;
		do {
			try {
				map = findApiCall(null);
				if(map != null) {
					LOG.debug("Replacing '" + map.keySet().toArray()[0].toString() + "' with '" + map.values().toArray()[0] + "'");
					LOG.debug("Replacing '" + originalMsg + "' with '" + map.values().toArray()[0] + "'");
					interpretedMsg = interpretedMsg.replaceAll(map.keySet().toArray()[0].toString(), map.values().toArray()[0] != null ? map.values().toArray()[0].toString().replace("\\", "\\\\").replace("$", "\\$") : null);
				}
			} catch (IOException e) {
				LOG.fatal("ERROR", e);
				throw new RuntimeException(e);
			}
		} while(map != null);
		return interpretedMsg;
	}
}
