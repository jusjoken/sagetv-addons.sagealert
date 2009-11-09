/*
 *      Copyright 2009 Battams, Derek
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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;
import com.google.code.sagetvaddons.sagealert.client.UserSettings;

/**
 * @author dbattams
 *
 */
public final class AppStartedEvent implements SageEvent {
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(AppStartedEvent.class.getCanonicalName(), "App Started Alert", "This alert is triggered every time SageAlert is started.");
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getEventMetaData()
	 */
	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		return new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_LONG_DEFAULT)).format(new Date()) + ": SageAlert has started successfully!";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getMediumDescription()
	 */
	@Override
	public String getMediumDescription() {
		return new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_MEDIUM_DEFAULT)).format(new Date()) + ": SageAlert has started successfully!";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_SHORT_DEFAULT)).format(new Date()) + ": SageAlert has started successfully!";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return "SageAlert has successfully started...";
	}
}
