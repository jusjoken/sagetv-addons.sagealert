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
package com.google.code.sagetvaddons.sagealert.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The RPC interface for the settings server; allows getting/setting generic app data (settings); provides persistent data/state storage
 * @author dbattams
 * @version $Id$
 */
@RemoteServiceRelativePath("SettingsService")
public interface SettingsService extends RemoteService {
	/**
	 * Get a setting value from the server
	 * @param var The name of the setting to be retrieved
	 * @param defaultVal The value to be returned if the setting does not exist on the server
	 * @return The value of the requested setting, if it exists in the data store, or defaultVal
	 */
	public String getSetting(String var, String defaultVal);
	
	/**
	 * Store a key/value in the data store
	 * @param var The name of the setting to be stored
	 * @param val The value of the setting
	 */
	public void setSetting(String var, String val);
}
