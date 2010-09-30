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
import gkusnick.sagetv.api.PlaylistAPI;

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.PlaylistAddedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.PlaylistModifiedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.PlaylistRemovedEvent;
import com.google.code.sagetvaddons.sagealert.shared.Client;

/**
 * @author dbattams
 *
 */
public class PlaylistEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(PlaylistEventsListener.class);
	
	static private final PlaylistEventsListener INSTANCE = new PlaylistEventsListener();
	static public final PlaylistEventsListener get() { return INSTANCE; }
	
	/**
	 * 
	 */
	private PlaylistEventsListener() {}

	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0 + " :: " + arg1);
		PlaylistAPI.Playlist playlist = API.apiNullUI.playlistAPI.Wrap(arg1.get("Playlist"));
		Client clnt = DataStore.getInstance().getClient(arg1.get("UIContext").toString());
		if(CoreEventsManager.PLAYLIST_ADDED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new PlaylistAddedEvent(playlist, clnt, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLAYLIST_ADDED)));
		else if(CoreEventsManager.PLAYLIST_MODDED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new PlaylistModifiedEvent(playlist, clnt, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLAYLIST_MODDED)));
		else if(CoreEventsManager.PLAYLIST_REMOVED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new PlaylistRemovedEvent(playlist, clnt, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLAYLIST_REMOVED)));
		else
			LOG.error("Unsupported event! [" + arg0 + "]");
	}

}
