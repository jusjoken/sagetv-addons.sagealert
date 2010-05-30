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
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.IsSerializable;

public final class SageAlertEventMetadata implements IsSerializable, ModelData {
	private String eventId;
	private String eventName;
	private String desc;
	
	@SuppressWarnings("unused")
	private SageAlertEventMetadata() {}
	
	public SageAlertEventMetadata(String id, String name, String desc) {
		eventId = id;
		eventName = name;
		this.desc = desc;
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
}
