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

import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * All event handlers must implement this interface.
 * @author dbattams
 * @version $Id$
 */
public interface SageEventHandler {
	/**
	 * Called when an event is fired to a handler
	 * @param e The event being fired
	 */
	public void onEvent(SageAlertEvent e);
	
	/**
	 * Return the settings object used to initialize the handler
	 * @return The handler's connection settings
	 */
	public NotificationServerSettings getSettings();
	
	/**
	 * Cleanup any resources associated with the handler
	 */
	public void destroy();
}
