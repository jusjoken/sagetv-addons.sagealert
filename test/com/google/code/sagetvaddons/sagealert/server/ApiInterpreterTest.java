/*
 *      Copyright 2010 Battams, Derek
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Various tests for the SageAlert API interpreter
 * @author dbattams
 * @version $Id$
 */
public class ApiInterpreterTest {

	/**
	 * <p>Testing:</p>
	 * <ul>
	 * 	<li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>Valid method args</li>
	 *  <li>No args</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0001() {
		assertEquals("A simple string.", new ApiInterpreter(new Object[] {new String("A simple string.")}, "$0.toString()").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>No args</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0002() {
		assertEquals("$$.badFunc()", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc()").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>One arg of type String</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0003() {
		assertEquals("$$.badFunc(java.lang.String)", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc(\"foo\")").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>One arg of type boolean (true)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0004() {
		assertEquals("$$.badFunc(java.lang.Boolean)", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc(true)").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>One arg of type boolean (false)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0005() {
		assertEquals("$$.badFunc(java.lang.Boolean)", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc(false)").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>One arg of invalid type</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0006() {
		assertEquals("$0.badFunc(foo)", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc(foo)").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>One arg of type number</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0007() {
		assertEquals("$$.badFunc(java.lang.Long)", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc(3223)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>One arg of type Object (valid reference to $0)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0008() {
		assertEquals("$$.badFunc(java.lang.Object)", new ApiInterpreter(new Object[] {new Object()}, "$0.badFunc($0)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>One arg of type Object (valid reference to $0)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0009() {
		assertEquals("true", new ApiInterpreter(new Object[] {new Object()}, "$0.equals($0)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>One arg of type String</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0010() {
		assertEquals("false", new ApiInterpreter(new Object[] {new Object()}, "$0.equals(\"foo\")").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>One arg of type long</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0011() {
		assertEquals("false", new ApiInterpreter(new Object[] {new Object()}, "$0.equals(500)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>One arg of type boolean</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0012() {
		assertEquals("false", new ApiInterpreter(new Object[] {new Object()}, "$0.equals(true)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>Multiple args</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0013() {
		assertEquals("$$.foo(java.lang.Boolean,java.lang.Boolean,java.lang.Long,java.lang.Boolean,java.lang.String,java.lang.Object)", new ApiInterpreter(new Object[] {new Object()}, "$0.foo(true, false, 423, true, \"jfjs\", $0)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>Multiple args (one is an API call)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0014() {
		assertEquals("$$.foo(java.lang.Boolean,java.lang.Boolean,java.lang.Long,java.lang.String,java.lang.String,java.lang.Object)", new ApiInterpreter(new Object[] {new Object()}, "$0.foo(true, false, 423, $0.toString(), \"jfjs\", $0)").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>Multiple args (multiple are API calls)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0015() {
		assertEquals("$$.foo(java.lang.Boolean,java.lang.Boolean,java.lang.Long,java.lang.String,java.lang.String,java.lang.String)", new ApiInterpreter(new Object[] {new Object()}, "$0.foo(true, false, 423, $0.toString(), \"jfjs\", $0.toString())").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Invalid method name</li>
	 *  <li>Multiple args (multiple are API calls; one API call is invalid)</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0016() {
		assertEquals("$$.foo(java.lang.Boolean,java.lang.Boolean,java.lang.Long,java.lang.String,java.lang.String,java.lang.String)", new ApiInterpreter(new Object[] {new Object()}, "$0.foo(true, false, 423, $0.toString(), \"jfjs\", $0.foo())").interpret());
	}

	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>One arg to invalid obj ref # ($500)</li>
	 * </ul>
	 * <p>This case works because Object.equals() handles null arg fine and a call to $500 will return null</p>
	 */
	@Test
	public void interpretTest0017() {
		assertEquals("false", new ApiInterpreter(new Object[] {new Object()}, "$0.equals($500)").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference (global obj $utils)</li>
	 *  <li>Valid method name</li>
	 *  <li>Two args</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0018() {
		assertEquals("::foobar", new ApiInterpreter(new Object[] {new Object()}, "$utils.concatIfNotEmpty(\"::\", \"foobar\")").interpret());
	}
	
	/**
	 * <p>Testing:</p>
	 * <ul>
	 *  <li>Valid object reference</li>
	 *  <li>Valid method name</li>
	 *  <li>Global $utils as arg</li>
	 * </ul>
	 */
	@Test
	public void interpretTest0019() {
		assertEquals("false", new ApiInterpreter(new Object[] {new Object()}, "$0.equals($utils.concatIfNotEmpty(\"::\", \"foobar\"))").interpret());
	}
	
	/**
	 * Testing $utils.run()
	 */
	@Test
	public void interpreterTest0020() {
		assertEquals("12", new ApiInterpreter(new Object[] {new Object()}, "$utils.run($utils, \"concatIfNotEmpty\", [\"1\", \"2\"])").interpret());
	}

	/**
	 * Testing $utils.run()
	 */
	@Test
	public void interpreterTest0021() {
		assertEquals("12", new ApiInterpreter(new Object[] {new Object()}, "$utils.run($utils.run($utils, \"concatIfNotEmpty\", [\"1\", \"2\"]), \"toString\", [])").interpret());
	}
	
	/**
	 * Testing $utils.run()
	 */
	@Test
	public void interpreterTest0022() {
		assertEquals("java.lang.String", new ApiInterpreter(new Object[] {new String()}, "$utils.run($utils.run($0, \"getClass\", []), \"getName\", [])").interpret());
	}

	/**
	 * Testing $utils.printArray() with constant array arg
	 */
	@Test
	public void interpreterTest0023() {
		assertEquals("[1, true, foobar]", new ApiInterpreter(new Object[0], "$utils.printArray([1,    true,          \"foobar\"])").interpret());
	}
}
