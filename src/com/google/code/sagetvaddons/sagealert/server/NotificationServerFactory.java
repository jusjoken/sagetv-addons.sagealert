/*
 *      Copyright 2009 Battams, Derek
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

import com.google.code.sagetvaddons.sagealert.client.GrowlServerSettings;
import com.google.code.sagetvaddons.sagealert.client.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.client.TwitterSettings;

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

	/**
	 * Given a settings object, inspect it's concrete type and return a server instance appropriate for it
	 * @param settings
	 * @return
	 */
	static SageEventHandler getInstance(NotificationServerSettings settings) {
		if(settings instanceof GrowlServerSettings)
			return GrowlServer.getServer((GrowlServerSettings)settings);
		if(settings instanceof TwitterSettings)
			return TwitterServer.getServer((TwitterSettings)settings);
		String msg = "Unsupported server settings type: " + settings.getClass().getCanonicalName();
		RuntimeException e = new RuntimeException(msg);
		Logger.getLogger(NotificationServerFactory.class).trace(msg, e);
		throw e;
	}
	
	private NotificationServerFactory() {}
}
