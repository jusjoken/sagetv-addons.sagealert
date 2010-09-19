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

/**
 * Various utility methods to help with string and number manipulations
 * @author dbattams
 * @version $Id$
 */
public class Utilities {
	
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
}
