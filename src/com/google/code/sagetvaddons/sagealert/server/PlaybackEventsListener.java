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

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.MediaFileAPI;

import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.PlaybackEvent;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

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
				String eventId = Client.EventType.STARTS + "_" + clnt.getId();
				SageAlertEventMetadata metadata = new SageAlertEventMetadata(eventId, "Media Playback Started: " + clnt.getAlias(), "Event fires when client '" + clnt.getAlias() + "' starts to playback media.", Arrays.asList(PlaybackEvent.ARG_TYPES), ds.getSetting(eventId + SageAlertEventMetadata.SUBJ_SUFFIX, CoreEventsManager.PLAYBACK_STARTED_SUBJ), ds.getSetting(eventId + SageAlertEventMetadata.SHORT_SUFFIX, CoreEventsManager.PLAYBACK_STARTED_SHORT_MSG), ds.getSetting(eventId + SageAlertEventMetadata.MED_SUFFIX, CoreEventsManager.PLAYBACK_STARTED_MED_MSG), ds.getSetting(eventId + SageAlertEventMetadata.LONG_SUFFIX, CoreEventsManager.PLAYBACK_STARTED_LONG_MSG));
				SageAlertEventHandlerManager.get().fire(new PlaybackEvent(mf, clnt, eventId, metadata));
			} else if(CoreEventsManager.PLAYBACK_STOPPED.equals(arg0)) {
				String eventId = Client.EventType.STOPS + "_" + clnt.getId();
				SageAlertEventMetadata metadata = new SageAlertEventMetadata(eventId, "Media Playback Stopped: " + clnt.getAlias(), "Event fires when client '" + clnt.getAlias() + "' stops playing back media.", Arrays.asList(PlaybackEvent.ARG_TYPES), ds.getSetting(eventId + SageAlertEventMetadata.SUBJ_SUFFIX, CoreEventsManager.PLAYBACK_STOPPED_SUBJ), ds.getSetting(eventId + SageAlertEventMetadata.SHORT_SUFFIX, CoreEventsManager.PLAYBACK_STOPPED_SHORT_MSG), ds.getSetting(eventId + SageAlertEventMetadata.MED_SUFFIX, CoreEventsManager.PLAYBACK_STOPPED_MED_MSG), ds.getSetting(eventId + SageAlertEventMetadata.LONG_SUFFIX, CoreEventsManager.PLAYBACK_STOPPED_LONG_MSG));
				SageAlertEventHandlerManager.get().fire(new PlaybackEvent(mf, clnt, eventId, metadata));
			} else
				LOG.error("Unhandled event: " + arg0);
		} else
			LOG.error("Contents of args map is invalid!  Event ignored.");
	}
	
	private PlaybackEventsListener() {}
}
