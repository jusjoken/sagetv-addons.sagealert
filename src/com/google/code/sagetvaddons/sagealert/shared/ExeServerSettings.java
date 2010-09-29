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
public class ExeServerSettings implements NotificationServerSettings {
	
	private String desc;
	private String exeName;
	private String args;
	
	/**
	 * For GWT RPC only; do NOT use!
	 */
	public ExeServerSettings() { }
	
	/**
	 * 
	 */
	public ExeServerSettings(String desc, String exeName, String args) {
		this.desc = desc;
		this.exeName = exeName;
		if(args != null)
			this.args = args;
		else
			this.args = "";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings#getDisplayValue()
	 */
	public String getDisplayValue() {
		return "EXE: " + desc;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings#getId()
	 */
	public String getId() {
		return desc;
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
		return exeName.replace("|", "\\|") + "|" + args;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.IsDataStoreSerializable#getDataStoreKey()
	 */
	public String getDataStoreKey() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.IsDataStoreSerializable#unserialize(java.lang.String, java.lang.String)
	 */
	public void unserialize(String key, String data) {
		desc = key;
		String[] exeAndArgs = data.split("\\|", 2);
		if(exeAndArgs.length == 1) {
			exeName = key;
			args = exeAndArgs[0];
		} else {
			exeName = exeAndArgs[0];
			args = exeAndArgs[1];
		}
	}
	
	public String getExeName() {
		return exeName;
	}

	public String getArgs() {
		return args;
	}

	public String getDesc() {
		return desc;
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
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
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
		if (!(obj instanceof ExeServerSettings)) {
			return false;
		}
		ExeServerSettings other = (ExeServerSettings) obj;
		if (desc == null) {
			if (other.desc != null) {
				return false;
			}
		} else if (!desc.equals(other.desc)) {
			return false;
		}
		return true;
	}
}
