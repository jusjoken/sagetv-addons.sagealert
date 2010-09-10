/*
 *      Copyright 2009-2010 Battams, Derek
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

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.shared.CsvLogFileSettings;
import com.google.code.sagetvaddons.sagealert.shared.EmailSettings;
import com.google.code.sagetvaddons.sagealert.shared.ExeServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.GrowlServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.TwitterSettings;

/**
 * Given a settings object, return the appropriate server
 * 
 * When a new notification system is added to SageAlert then this method MUST be updated to return the appropriate server type; if it's not then the program will
 * crash with unhandled RuntimeExceptions
 * 
 * @author dbattams
 * @version $Id$
 */
final class NotificationServerFactory {
	static private final Logger LOG = Logger.getLogger(NotificationServerFactory.class);

	/**
	 * Given a settings object, inspect it's concrete type and return a server instance appropriate for it
	 * @param settings
	 * @return
	 */
	static SageAlertEventHandler getInstance(NotificationServerSettings settings) {
		if(settings instanceof GrowlServerSettings)
			return GrowlServer.getServer((GrowlServerSettings)settings);
		if(settings instanceof TwitterSettings)
			return TwitterServer.getServer((TwitterSettings)settings);
		if(settings instanceof EmailSettings)
			return new EmailServer((EmailSettings)settings);
		if(settings instanceof CsvLogFileSettings)
			return CsvLogFileServer.get((CsvLogFileSettings)settings);
		if(settings instanceof ExeServerSettings)
			return new ExeServer((ExeServerSettings)settings);
		String msg = "Unsupported server settings type: " + settings.getClass().getCanonicalName();
		RuntimeException e = new RuntimeException(msg);
		LOG.error(msg, e);
		throw e;
	}
	
	private NotificationServerFactory() {}
}
