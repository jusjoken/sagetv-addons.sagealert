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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.code.sagetvaddons.sagealert.server.DataStore;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;
import com.google.code.sagetvaddons.sagealert.shared.UserSettings;

/**
 * @author dbattams
 *
 */
public final class AppStartedEvent implements SageAlertEvent {
	static public final String EVENT_ID = "SageAlert_AppStarted";
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	public String getLongDescription() {
		return new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_LONG_DEFAULT)).format(new Date()) + ": SageAlert has started successfully!";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_MEDIUM_DEFAULT)).format(new Date()) + ": SageAlert has started successfully!";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getShortDescription()
	 */
	public String getShortDescription() {
		return new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_SHORT_DEFAULT)).format(new Date()) + ": SageAlert has started successfully!";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	public String getSource() {
		return EVENT_ID;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	public String getSubject() {
		return "SageAlert has successfully started...";
	}
}
