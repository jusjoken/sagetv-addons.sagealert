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
 * @author dbattams
 *
 */
public final class EmailSettings implements NotificationServerSettings {

	private String address;
	private String msgType;
	
	/**
	 * Default ctor for GWT RPC; do NOT use!
	 */
	public EmailSettings() {}
	
	/**
	 * Constructor
	 * @param address The email address to send alerts to
	 * @param msgType The type of alert (short, med, long)
	 */
	public EmailSettings(String address, String msgType) {
		this.address = address;
		this.msgType = msgType;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable#getDataStoreData()
	 */
	public String getDataStoreData() {
		return msgType;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.IsDataStoreSerializable#getDataStoreKey()
	 */
	public String getDataStoreKey() {
		return address;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the msgType
	 */
	public String getMsgType() {
		return msgType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
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
		if (!(obj instanceof EmailSettings)) {
			return false;
		}
		EmailSettings other = (EmailSettings) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		return true;
	}

	public void unserialize(String key, String data) {
		address = key;
		msgType = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Email: " + address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDisplayValue() {
		// TODO Auto-generated method stub
		return toString();
	}

	public void setDisplayValue(String s) {
		return;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return address;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
}
