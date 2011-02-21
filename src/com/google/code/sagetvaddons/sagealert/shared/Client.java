/*
 *      Copyright 2009-2011 Battams, Derek
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

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A simple representation of a SageTV client; a client, in this context, refers to a client PC, extender, or a placeshifter.
 * @author dbattams
 * @version $Id$
 */
public class Client implements BeanModelTag, IsSerializable {
	public enum EventType {
		STARTS,
		STOPS,
		PAUSES,
		RESUMES
	}
	
	private String id;
	private String alias;
	
	/**
	 * Required for GWT RPC; should NOT be used
	 */
	public Client() {}
	
	/**
	 * Constructor
	 * @param id The Client id
	 * @param alias The client's alias
	 */
	public Client(String id, String alias) {
		this.id = id;
		this.alias = alias;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias != null && alias.length() > 0 ? alias : id;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
