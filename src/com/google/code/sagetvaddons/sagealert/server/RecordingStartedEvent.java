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

import gkusnick.sagetv.api.AiringAPI;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;
import com.google.code.sagetvaddons.sagealert.client.UserSettings;

/**
 * A SageEvent that signifies a recording has started
 * @author dbattams
 * @version $Id$
 */
final class RecordingStartedEvent implements SageEvent {
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(RecordingStartedEvent.class.getCanonicalName(), "Recording Started Alert", "Alert when a recording starts.");
	
	private AiringAPI.Airing source;
	
	/**
	 * ctor
	 * @param source The Airing object that started recording; the source of the event
	 */
	RecordingStartedEvent(AiringAPI.Airing source) {
		this.source = source;
	}
	
	@Override
	public String getLongDescription() {
		StringWriter w = new StringWriter();
		w.write("'"  + source.GetAiringTitle());
		String subtitle = source.GetShow().GetShowEpisode();
		if(subtitle != null && subtitle.length() > 0)
			w.write(": " + subtitle);
		w.write("' started recording at " + new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_LONG_DEFAULT)).format(new Date(source.GetScheduleStartTime())));
		return w.toString();
	}

	@Override
	public String getMediumDescription() {
		return "'" + source.GetAiringTitle() + "' has just started recording.";
	}

	@Override
	public String getShortDescription() {
		return "A new recording has started on SageTV.";
	}

	@Override
	public String getSubject() {
		return "New recording started...";
	}

	@Override
	public AiringAPI.Airing getSource() {
		return source;
	}

	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}
}
