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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.UserSettings;


/**
 * A daemon thread that monitors recordings and fires both RecordingStartedEvents and RecordingFinishedEvents
 * @author dbattams
 * @version $Id$
 */
final class RecordingMonitor extends SageRunnable {

	static private final Logger LOG = Logger.getLogger(RecordingMonitor.class);
		
	private Set<MediaFileAPI.MediaFile> activeRecordings;
		
	/**
	 * ctor 
	 */
	RecordingMonitor() {
		activeRecordings = new HashSet<MediaFileAPI.MediaFile>();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("Thread initialized...");
		while(keepAlive()) {
			LOG.info("Thread started...");

			// Check active recordings and register new ones
			for(MediaFileAPI.MediaFile mf : API.apiNullUI.global.GetCurrentlyRecordingMediaFiles())
				if(activeRecordings.add(mf)) {
					SageEventHandlerManager.getInstance().fire(new RecordingStartedEvent(mf.GetMediaFileAiring()));
					LOG.info("Firing recording started event for '" + mf.GetMediaTitle() + "'");
				}
			
			// Check registered recordings and remove finished ones
			Iterator<MediaFileAPI.MediaFile> itr = activeRecordings.iterator();
			while(itr.hasNext()) {
				MediaFileAPI.MediaFile mf = itr.next();
				if(!mf.IsFileCurrentlyRecording()) {
					itr.remove();
					// Make sure the file hasn't been deleted between the time the recording actually ended and the time this event is fired
					if(API.apiNullUI.mediaFileAPI.GetMediaFileForID(mf.GetMediaFileID()) != null) {
						SageEventHandlerManager.getInstance().fire(new RecordingFinishedEvent(mf));
						LOG.info("Firing recording finished event for '" + mf.GetMediaTitle() + "'");
					}
				}
			}
			
			LOG.info("Thread sleeping...");
			
			try {
				Thread.sleep(Long.parseLong(DataStore.getInstance().getSetting(UserSettings.REC_MONITOR_SLEEP, UserSettings.REC_MONITOR_SLEEP_DEFAULT)));
			} catch(InterruptedException e) {
				LOG.info("Thread awakened early...");
			}
		}
		LOG.warn("Thread destroyed.");
	}		
}
