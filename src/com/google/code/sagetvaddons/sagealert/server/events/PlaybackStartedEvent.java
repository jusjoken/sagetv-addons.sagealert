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
package com.google.code.sagetvaddons.sagealert.server.events;

import org.apache.log4j.Logger;

import gkusnick.sagetv.api.AiringAPI;
import gkusnick.sagetv.api.MediaFileAPI;
import gkusnick.sagetv.api.ShowAPI;

import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
public final class PlaybackStartedEvent implements SageAlertEvent {
	static private final Logger LOG = Logger.getLogger(PlaybackStartedEvent.class);
	
	private MediaFileAPI.MediaFile mf;
	private Client clnt;
	private String eventId;
	
	public PlaybackStartedEvent(MediaFileAPI.MediaFile mf, Client clnt, String eventId) {
		this.mf = mf;
		this.clnt = clnt;
		this.eventId = eventId;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		StringBuilder msg = new StringBuilder("Client '" + clnt.getAlias() + "' is watching '" + mf.GetMediaTitle());
		AiringAPI.Airing a = mf.GetMediaFileAiring();
		if(a != null) {
			ShowAPI.Show s = a.GetShow();
			if(s != null) {
				String subtitle = s.GetShowEpisode();
				if(subtitle != null && subtitle.length() > 0)
					msg.append(": " + subtitle);
			} else
				LOG.warn("Airing show is unexpectedly null for Airing ID: " + a.GetAiringID());
		} else
			LOG.warn("MediaFile airing is unexpectedly null for MF ID: " + mf.GetMediaFileID());
		msg.append("'");
		return msg.toString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getShortDescription()
	 */
	public String getShortDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSource()
	 */
	public String getSource() {
		return eventId;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSubject()
	 */
	public String getSubject() {
		return "Client is playing back media";
	}
}
