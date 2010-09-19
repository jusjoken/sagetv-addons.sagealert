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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.IsSerializable;

public final class SageAlertEventMetadata implements IsSerializable, ModelData {
	static public final String SUBJ_SUFFIX = "_SUBJ";
	static public final String SHORT_SUFFIX = "_SHORT";
	static public final String MED_SUFFIX = "_MED";
	static public final String LONG_SUFFIX = "_LONG";
	
	private String eventId;
	private String eventName;
	private String desc;
	private String subject;
	private String shortMsg;
	private String medMsg;
	private String longMsg;
	private List<String> objTypes;
	
	@SuppressWarnings("unused")
	private SageAlertEventMetadata() {}
		
	public SageAlertEventMetadata(String id, String name, String desc, List<String> objTypes, String subj, String shortMsg, String medMsg, String longMsg) {
		eventId = id;
		eventName = name;
		this.desc = desc;
		this.shortMsg = shortMsg;
		this.medMsg = medMsg;
		this.longMsg = longMsg;
		this.objTypes = objTypes;
		this.subject = subj;
	}

	/**
	 * @return the objType
	 */
	public List<String> getObjTypes() {
		return objTypes;
	}
	
	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	public String getShortMsg() {
		return shortMsg;
	}
	
	public String getMedMsg() {
		return medMsg;
	}
	
	public String getLongMsg() {
		return longMsg;
	}
	
	@SuppressWarnings("unchecked")
	public <X> X get(String property) {
		return (X)eventName;
	}

	public Map<String, Object> getProperties() {
		return Collections.singletonMap("name", (Object)eventName);
	}

	public Collection<String> getPropertyNames() {
		return Collections.singletonList("name");
	}

	public <X> X remove(String property) {
		throw new UnsupportedOperationException("Cannot remove properties from model!");
	}

	public <X> X set(String property, X value) {
		throw new UnsupportedOperationException("Cannot add properties to model!");
	}


	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


	/**
	 * @param shortMsg the shortMsg to set
	 */
	public void setShortMsg(String shortMsg) {
		this.shortMsg = shortMsg;
	}


	/**
	 * @param medMsg the medMsg to set
	 */
	public void setMedMsg(String medMsg) {
		this.medMsg = medMsg;
	}


	/**
	 * @param longMsg the longMsg to set
	 */
	public void setLongMsg(String longMsg) {
		this.longMsg = longMsg;
	}
}
