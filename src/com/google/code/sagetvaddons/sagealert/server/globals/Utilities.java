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
package com.google.code.sagetvaddons.sagealert.server.globals;

import org.apache.log4j.Logger;

/**
 * <p>Various utility methods to help with string and number manipulations</p>
 * <p>An instance of this object is available via the global object <b>$utils</b></p>
 * @author dbattams
 * @version $Id$
 */
public class Utilities {
	private static final Logger LOG = Logger.getLogger(Utilities.class);
	
	/**
	 * <p>Append the src to the prefix only if the src is non-null and greater than zero length, otherwise return the empty string.</p>
	 * <p>This is a convenience method to generate optional strings.  A typical use would be to display episode information, only if it exists.</p>
	 * <p>For example, if you wanted to display the title and episode in an alert message you might use this API call:</p>
	 * <code>$0.GetMediaTitle() $utils.concatIfNotEmpty(": ", $2.GetShowEpisode())</code>
	 * @param prefix The prefix to append to the source value
	 * @param src The source string to evaluate
	 * @return If src is non-null and not zero length then return the concatenation of prefix + src, otherwise return the empty string
	 */
	public String concatIfNotEmpty(String prefix, String src) {
		if(src != null && src.length() > 0)
			return prefix + src;
		return "";
	}
	
	/**
	 * <p>Convert a system message level to a human friendly string</p>
	 * @param sysMsgLevel The system message level value
	 * @return The value converted to a string; if the value is unrecognized then UNKNOWN is returned
	 */
	public String sysMsgLevelToString(Integer sysMsgLevel) {
		switch(sysMsgLevel) {
		case 1: return "INFO";
		case 2: return "WARN";
		case 3: return "ERROR";
		default: return "UNKNOWN";
		}
	}
	
	/**
	 * <p>Convert a system message level to a human friendly string</p>
	 * @param sysMsgLevel The system message level value
	 * @return The value converted to a string; if the value is unrecognized then UNKNOWN is returned
	 */
	public String sysMsgLevelToString(Long sysMsgLevel) {
		return sysMsgLevelToString(Integer.parseInt(sysMsgLevel.toString()));
	}
		
	/**
	 * <p>Convert a system message level to a human friendly string</p>
	 * @param sysMsgLevel The system message level value, as a String
	 * @return The value converted to a string; if the value is unrecognized then UNKNOWN is returned
	 */
	public String sysMsgLevelToString(String sysMsgLevel) {
		return sysMsgLevelToString(Integer.parseInt(sysMsgLevel));
	}
	
	/**
	 * <p>Convert a Long to an Integer; be careful that the Long being converted isn't too big to fit in an Integer</p>
	 * <p>Because parsed numbers are always converted to Long in SageAlert, you may need this method to pass an arg as an Integer
	 * @param l The Long to be converted
	 * @return The Long converted to an Integer or null if there was an error with the conversion
	 */
	public Integer long2Int(Long l) {
		try {
			return new Integer(Integer.parseInt(l.toString()));
		} catch(Exception e) {
			LOG.error("Error converting Long to Integer [" + l + "]", e);
			return null;
		}
	}
	
	/**
	 * <p>Convert the long to a Date and then format it given the provided SimpleDateFormat pattern</p>
	 * @param date
	 * @param fmt
	 * @return The date formatted using the given pattern; on error fmt is returned, unmodified
	 * @see java.text.SimpleDateFormat
	 */
	public String fmtDate(long date, String fmt) {
		return new Date(date).format(fmt);
	}
}
