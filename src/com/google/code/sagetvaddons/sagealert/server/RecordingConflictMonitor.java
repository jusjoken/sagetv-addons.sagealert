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

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.UserSettings;

/**
 * A daemon thread that monitors for recording conflicts and fires RecordingConflictEvents when detected
 * @author dbattams
 * @version $Id$
 */
final class RecordingConflictMonitor extends SageAlertRunnable {

	static private final Logger LOG = Logger.getLogger(RecordingConflictMonitor.class);
	static private final String CONFLICT_LAST_NOTICE = "ConflictLastNotice";

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("Thread initialized.");
		while(keepAlive()) {
			LOG.info("Thread started.");
			int conflictsSize = API.apiNullUI.global.GetAiringsThatWontBeRecorded(true).size();
			DataStore store = DataStore.getInstance();
			long now = System.currentTimeMillis();
			if(conflictsSize > 0 && now - Long.parseLong(store.getSetting(CONFLICT_LAST_NOTICE, "0")) >= Long.parseLong(store.getSetting(UserSettings.CONFLICT_REPORT_DELAY, UserSettings.CONFLICT_REPORT_DELAY_DEFAULT))) {
				store.setSetting(CONFLICT_LAST_NOTICE, Long.toString(now));
				SageEventHandlerManager.getInstance().fire(new RecordingConflictEvent(conflictsSize));
				LOG.info("Firing recording conflict event (" + conflictsSize + " unresolved conflict(s) detected).");
			}
			
			LOG.info("Thread sleeping.");
			try {
				Thread.sleep(Long.parseLong(store.getSetting(UserSettings.CONFLICT_MONITOR_SLEEP, UserSettings.CONFLICT_MONITOR_SLEEP_DEFAULT)));
			} catch(InterruptedException e) {
				LOG.warn("Awakened early.");
			}
		}
		LOG.info("Thread destroyed.");
	}
}
