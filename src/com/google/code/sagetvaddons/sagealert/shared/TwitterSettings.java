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
package com.google.code.sagetvaddons.sagealert.shared;


/**
 * Store the data required to connect to Twitter
 * @author dbattams
 * @version $Id$
 */
public final class TwitterSettings implements NotificationServerSettings {

	private String token;
	private String secret;
	private String alias;
	
	/**
	 * Default ctor needed for GWT RPC; do NOT use!
	 */
	public TwitterSettings() {} // Needed for GWT RPC; do not use
	
	/**
	 * Constructor
	 * @param id Twitter id
	 * @param pwd Twitter password
	 */
	public TwitterSettings(String alias, String token, String secret) {
		this.token = token;
		this.secret = secret;
		this.alias = alias;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable#getDataStoreData()
	 */
	synchronized public String getDataStoreData() {
		return alias;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable#getDataStoreKey()
	 */
	synchronized public String getDataStoreKey() {
		return token + "&" + secret;
	}	

	/**
	 * Unserialize this object; restore the state of the object as described by the serialized data
	 * @param key Hostname
	 * @param data Password
	 */
	synchronized public void unserialize(String key, String data) {
		String[] tokStr = key.split("&", 2);
		token = tokStr[0];
		secret = tokStr[1];
		alias = data;
	}
	
	@Override
	synchronized public String toString() {
		return "Twitter @" + alias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TwitterSettings)) {
			return false;
		}
		TwitterSettings other = (TwitterSettings) obj;
		if (secret == null) {
			if (other.secret != null) {
				return false;
			}
		} else if (!secret.equals(other.secret)) {
			return false;
		}
		if (token == null) {
			if (other.token != null) {
				return false;
			}
		} else if (!token.equals(other.token)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getDisplayValue() {
		return toString();
	}

	public void setDisplayValue(String s) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return alias;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
}
