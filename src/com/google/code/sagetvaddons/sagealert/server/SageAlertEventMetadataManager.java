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
package com.google.code.sagetvaddons.sagealert.server;

import java.util.HashMap;
import java.util.Map;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
final class SageAlertEventMetadataManager {
	static private final SageAlertEventMetadataManager INSTANCE = new SageAlertEventMetadataManager();
	static final SageAlertEventMetadataManager get() { return INSTANCE; }
	
	private Map<String, SageAlertEventMetadata> map;
	
	private SageAlertEventMetadataManager() {
		map = new HashMap<String, SageAlertEventMetadata>();
	}
	
	synchronized public SageAlertEventMetadata getMetadata(String eventId) {
		return map.get(eventId);
	}
	
	synchronized public void putMetadata(SageAlertEventMetadata data) {
		map.put(data.getEventId(), data);
	}
	
	synchronized public SageAlertEventMetadata[] getAllMetadata() {
		return map.values().toArray(new SageAlertEventMetadata[map.values().size()]);
	}
}
