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
import gkusnick.sagetv.api.FavoriteAPI;

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.FavAddedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.FavModifiedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.FavRemovedEvent;

/**
 * @author dbattams
 *
 */
public class FavEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(FavEventsListener.class);
	
	static private final FavEventsListener INSTANCE = new FavEventsListener();
	static public final FavEventsListener get() { return INSTANCE; }
	
	/**
	 * 
	 */
	private FavEventsListener() {}

	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0 + " :: " + arg1);
		FavoriteAPI.Favorite fav = API.apiNullUI.favoriteAPI.Wrap(arg1.get("Favorite"));
		if(CoreEventsManager.FAV_ADDED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new FavAddedEvent(fav, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.FAV_ADDED)));
		else if(CoreEventsManager.FAV_MODDED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new FavModifiedEvent(fav, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.FAV_MODDED)));
		else if(CoreEventsManager.FAV_REMOVED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new FavRemovedEvent(fav, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.FAV_REMOVED)));
		else
			LOG.error("Unsupported event! [" + arg0 + "]");
	}

}
