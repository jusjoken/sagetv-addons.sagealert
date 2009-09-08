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
 * Store the data required to connect to Twitter
 * @author dbattams
 * @version $Id$
 */
public class TwitterSettings implements NotificationServerSettings {

	private String id;
	private String pwd;
	
	/**
	 * Default ctor needed for GWT RPC; do NOT use!
	 */
	public TwitterSettings() {} // Needed for GWT RPC; do not use
	
	/**
	 * Constructor
	 * @param id Twitter id
	 * @param pwd Twitter password
	 */
	public TwitterSettings(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable#getDataStoreData()
	 */
	@Override
	public String getDataStoreData() {
		return pwd;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable#getDataStoreKey()
	 */
	@Override
	public String getDataStoreKey() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(!(o instanceof TwitterSettings))
			return false;
		
		TwitterSettings srv = (TwitterSettings)o;
		return srv.getId().equals(getId());
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode() + 13;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * Unserialize this object; restore the state of the object as described by the serialized data
	 * @param key Hostname
	 * @param data Password
	 */
	protected void unserialize(String key, String data) {
		this.id = key;
		this.pwd = data;
	}
	
	@Override
	public String toString() {
		return "Twitter @" + id;
	}
}
