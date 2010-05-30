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

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.PlaybackStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.PlaybackStoppedEvent;
import com.google.code.sagetvaddons.sagealert.shared.Client;

/**
 * @author dbattams
 *
 */
final class PlaybackEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(PlaybackEventsListener.class);
	static private final PlaybackEventsListener INSTANCE = new PlaybackEventsListener();
	static final PlaybackEventsListener get() { return INSTANCE; }
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0);
		MediaFileAPI.MediaFile mf = API.apiNullUI.mediaFileAPI.Wrap(arg1.get("MediaFile"));
		Client clnt = DataStore.getInstance().findClient((String)arg1.get("UIContext"));
		
		if(CoreEventsManager.PLAYBACK_STARTED.equals(arg0)) {
			SageEventHandlerManager.get().fire(new PlaybackStartedEvent(mf, clnt, Client.EventType.STARTS + "_" + clnt.getId()));
		} else if(CoreEventsManager.PLAYBACK_STOPPED.equals(arg0))
			SageEventHandlerManager.get().fire(new PlaybackStoppedEvent(mf, clnt, Client.EventType.STOPS + "_" + clnt.getId()));
		else
			LOG.error("Unhandled event: " + arg0);
	}
	
	private PlaybackEventsListener() {}
}
