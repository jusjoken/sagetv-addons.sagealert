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
package com.google.code.sagetvaddons.sagealert.server.events;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public final class AppStartedEvent extends NoArgEvent {
	
	static public final String EVENT_ID = "SageAlert_AppStarted";
	
	static public final String SUBJ = "SageAlert has started successfully";
	static public final String LONG_MSG = "$date.format(\"H:mm z\"): SageAlert has started successfully!";
	static public final String MED_MSG = LONG_MSG;
	static public final String SHORT_MSG = LONG_MSG;
	
	public AppStartedEvent(SageAlertEventMetadata data) {
		super(data);
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	public String getSource() {
		return EVENT_ID;
	}
}
