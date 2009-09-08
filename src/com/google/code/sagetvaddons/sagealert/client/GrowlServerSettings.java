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


/**
 * Stores all data/settings required to connect to a Growl server instance
 * @author dbattams
 * @version $Id$
 */
public class GrowlServerSettings implements NotificationServerSettings {
	
	private String host;
	private String password;
	
	/**
	 * Default ctor required for GWT RPC; do not use!
	 */
	public GrowlServerSettings() {}
	
	/**
	 * Initialize a Growl settings object
	 * @param host The hostname or IP address of the Growl instance; use 'localhost' or '127.0.0.1' only for local apps
	 * @param password The password for authorization; null or empty string if password not needed (always needed for remote apps)
	 */
	public GrowlServerSettings(String host, String password) {
		this.host = host;
		this.password = password;
	}
	
	/**
	 * Initialize a Growl settings object with no password
	 * @param host The hostname or IP address of the Growl instance; use 'localhost' or '127.0.0.1' only for local apps
	 */
	public GrowlServerSettings(String host) {
		this(host, null);
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof GrowlServerSettings))
			return false;
		
		GrowlServerSettings srv = (GrowlServerSettings)o;
		return srv.getHost().equals(getHost());
	}
	
	@Override
	public int hashCode() {
		return getHost().hashCode() + 13;
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPassword() {
		if(password != null && password.length() == 0)
			return null;
		return password;
	}

	/**
	 * @param port the port to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "Growl @ " + getHost();
	}

	@Override
	public String getDataStoreKey() {
		return host;
	}
	
	@Override
	public String getDataStoreData() {
		return password;
	}
	
	/**
	 * Unserialize this object; restore the state of the object as described by the serialized data
	 * @param key Hostname
	 * @param data Password
	 */
	protected void unserialize(String key, String data) {
		host = key;
		password = data;
	}
}