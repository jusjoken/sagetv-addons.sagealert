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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Add support for reading settings from the webserver plugin
 * @author dbattams
 *
 */
final class WebServerSettings {
	static private final Logger LOG = Logger.getLogger(WebServerSettings.class);
	static private final File EXTENDER_PROPS = new File("webserver/extenders.properties");
	static private final String DEFAULT_VALUE = "";
	
	/**
	 * Read alias names from the webserver plugin's extenders.properties file
	 * @param id The extender MAC address (all lower case) or the client IP address
	 * @return The assigned alias name or the empty (non-null) string if no alias is defined
	 */
	static public String lookupExtenderAlias(String id) {
		if(EXTENDER_PROPS.exists()) {
			Properties props = new Properties();
			try {
				props.load(new FileReader(EXTENDER_PROPS));
				return props.getProperty(id, DEFAULT_VALUE);
			} catch (IOException e) {
				LOG.error("IO error", e);
				return DEFAULT_VALUE;
			}
		}
		return DEFAULT_VALUE;
	}
	
	private WebServerSettings() {}
}
