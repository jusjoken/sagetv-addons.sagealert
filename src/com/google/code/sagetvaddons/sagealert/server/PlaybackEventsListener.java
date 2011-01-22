/*
 *      Copyright 2010-2011 Battams, Derek
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

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.MediaFileAPI;

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.PlaybackEvent;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.code.sagetvaddons.sagealert.shared.Client.EventType;

/**
 * @author dbattams
 *
 */
final class PlaybackEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(PlaybackEventsListener.class);
	static private final PlaybackEventsListener INSTANCE = new PlaybackEventsListener();
	static final PlaybackEventsListener get() { return INSTANCE; }
	
	static private final String MF_KEY = "MediaFile";
	static private final String UI_KEY = "UIContext";
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0 + " :: " + arg1.toString());
		Object obj = arg1.get(MF_KEY);
		if(Utils.canWrapAsMediaFile(obj)) {
			DataStore ds = DataStore.getInstance();
			MediaFileAPI.MediaFile mf = API.apiNullUI.mediaFileAPI.Wrap(obj);
			Client clnt = ds.getClient((String)arg1.get(UI_KEY));
			
			if(CoreEventsManager.PLAYBACK_STARTED.equals(arg0)) {
				SageAlertEventMetadata metadata = PlaybackMetadataFactory.getMetadata(clnt, EventType.STARTS, mf);
				if(metadata != null)
					SageAlertEventHandlerManager.get().fire(new PlaybackEvent(mf, clnt, metadata.getEventId(), metadata));
				else
					LOG.warn("Playback started event ignored; probably because a picture is being played back!");
			} else if(CoreEventsManager.PLAYBACK_STOPPED.equals(arg0)) {
				SageAlertEventMetadata metadata = PlaybackMetadataFactory.getMetadata(clnt, EventType.STOPS, mf);
				if(metadata != null)
					SageAlertEventHandlerManager.get().fire(new PlaybackEvent(mf, clnt, metadata.getEventId(), metadata));
				else
					LOG.warn("Playback stopped event ignored; probably because a picture is being played back!");
			} else
				LOG.error("Unhandled event: " + arg0);
		} else
			LOG.error("Contents of args map is invalid!  Event ignored.");
	}
	
	private PlaybackEventsListener() {}
}
