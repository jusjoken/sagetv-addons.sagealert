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

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;
import com.google.code.sagetvaddons.sagealert.client.UserSettings;

import gkusnick.sagetv.api.MediaFileAPI;

/**
 * A SageEvent that signifies a recording has completed.
 * @author dbattams
 * @version $Id$
 */
class RecordingFinishedEvent implements SageEvent {
	/**
	 * Metadata for this event
	 */
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(RecordingFinishedEvent.class.getCanonicalName(), "Recording Finished Alert", "Alert when a recording has completed.");
	
	private MediaFileAPI.MediaFile source;
	
	/**
	 * Ctor
	 * @param source The media file that just completed recording; the source of this event
	 */
	RecordingFinishedEvent(MediaFileAPI.MediaFile source) {
		this.source = source;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		StringWriter w = new StringWriter();
		w.write("'"  + source.GetMediaTitle());
		String subtitle = source.GetMediaFileAiring().GetShow().GetShowEpisode();
		if(subtitle != null && subtitle.length() > 0)
			w.write(": " + subtitle);
		w.write("' finished recording at " + new SimpleDateFormat(DataStore.getInstance().getSetting(UserSettings.TIME_FORMAT_LONG, UserSettings.TIME_FORMAT_LONG_DEFAULT)).format(new Date(source.GetMediaFileAiring().GetScheduleEndTime())));
		return w.toString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getMediumDescription()
	 */
	@Override
	public String getMediumDescription() {
		return "'" + source.GetMediaTitle() + "' has finished recording.";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return "A recording has just finished on SageTV.";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	@Override
	public MediaFileAPI.MediaFile getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return "Recording finished...";
	}

	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}

}
