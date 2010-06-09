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
package com.google.code.sagetvaddons.sagealert.shared;

/**
 * @author dbattams
 *
 */
public class CsvLogFileSettings implements NotificationServerSettings {

	private String target;
	private char separator;
	
	// Required for GWT RPC; do NOT use!
	public CsvLogFileSettings() {}
	
	/**
	 * 
	 */
	public CsvLogFileSettings(String target, char separator) {
		this.target = target;
		this.separator = separator;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings#getDisplayValue()
	 */
	public String getDisplayValue() {
		return "CSV File: " + target;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings#getId()
	 */
	public String getId() {
		return target;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings#setDisplayValue(java.lang.String)
	 */
	public void setDisplayValue(String s) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings#setId(java.lang.String)
	 */
	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.IsDataStoreSerializable#getDataStoreData()
	 */
	public String getDataStoreData() {
		return String.valueOf(separator);
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.IsDataStoreSerializable#getDataStoreKey()
	 */
	public String getDataStoreKey() {
		return target;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the separator
	 */
	public char getSeparator() {
		return separator;
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(char separator) {
		this.separator = separator;
	}

	@Override
	public String toString() {
		return getDisplayValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		if (!(obj instanceof CsvLogFileSettings)) {
			return false;
		}
		CsvLogFileSettings other = (CsvLogFileSettings) obj;
		if (target == null) {
			if (other.target != null) {
				return false;
			}
		} else if (!target.equals(other.target)) {
			return false;
		}
		return true;
	}
	
	public void unserialize(String key, String data) {
		target = key;
		separator = data.charAt(0);
	}
}
