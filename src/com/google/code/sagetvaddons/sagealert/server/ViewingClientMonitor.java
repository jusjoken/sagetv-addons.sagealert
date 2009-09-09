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

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.MediaFileAPI;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.Client;
import com.google.code.sagetvaddons.sagealert.client.UserSettings;

/**
 * A daemon thread that monitors for local clients that are viewing media and fires PlayingMediaEvents
 * @author dbattams
 * @version $Id$
 */
final class ViewingClientMonitor extends SageRunnable {

	static private final Logger LOG = Logger.getLogger(ViewingClientMonitor.class);
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("Thread initialized...");
		Map<String, MediaFileAPI.MediaFile> lastStatus = new HashMap<String, MediaFileAPI.MediaFile>();
		while(keepAlive()) {
			LOG.info("Thread started...");
			for(String ui : API.apiNullUI.global.GetUIContextNames()) {
				API api = new API(ui);
				MediaFileAPI.MediaFile mf = api.mediaPlayerAPI.GetCurrentMediaFile();
				if(mf == null) {
					lastStatus.remove(ui);
				} else {
					MediaFileAPI.MediaFile lastMf = lastStatus.get(ui);
					if(lastMf == null || lastMf.GetMediaFileID() != mf.GetMediaFileID()) {
						lastStatus.put(ui, mf);
						Client c = DataStore.getInstance().findClient(ui);
						if(c.doNotify())
							SageEventHandlerManager.getInstance().fire(new PlayingMediaEvent(new ViewingClient(c, mf)));
						else
							LOG.info("Viewing notifications disabled for client '" + c.getId() + "'; event not fired.");
					}
				}
			}
			LOG.info("Thread sleeping...");
			try {
				Thread.sleep(Long.parseLong(DataStore.getInstance().getSetting(UserSettings.VIEWING_CLNT_MONITOR_SLEEP, UserSettings.VIEWING_CLNT_MONITOR_SLEEP_DEFAULT)));
			} catch(InterruptedException e) {
				LOG.info("Thread awakened early...");
			}
		}
		LOG.info("Thread destroyed...");
	}
}
