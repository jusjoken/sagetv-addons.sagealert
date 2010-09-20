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

import gkusnick.sagetv.api.API;

/**
 * <p>Various methods for getting info about the SageTV server this instance is connected to</p>
 * <p>An instance of this class is provided via the <b>$srv</b> global object</p>
 * @author dbattams
 * @version $Id$
 */
public class SageTVServer {

	/**
	 * Return the hostname or IP address of the SageTV server this instance is connected to
	 * @return The hostname or IP address
	 */
	public String getAddress() {
		return API.apiNullUI.global.GetServerAddress();
	}
	
	/**
	 * Return the OS of the SageTV server this instance is connected to
	 * @return The SageTV server's host operating system
	 */
	public String getOS() {
		return API.apiNullUI.global.GetOS();
	}
}
