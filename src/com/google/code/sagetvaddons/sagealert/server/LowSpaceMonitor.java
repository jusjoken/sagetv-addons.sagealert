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
 * A daemon thread that monitors the SageTV recording disk space and fires LowSpaceEvents when the available space is considered too low
 * @author dbattams
 * @version $Id$
 */
final class LowSpaceMonitor extends SageAlertRunnable {
	static private final Logger LOG = Logger.getLogger(LowSpaceMonitor.class);
	static private final String LOW_SPACE_LAST_NOTIFY = "LowSpaceLastNotify";
	/**
	 * Wait this many ms before repeating this event
	 */
	static public final long MIN_NOTIFY_DELAY = 14400000; // 4 hours
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		DataStore store = DataStore.getInstance();
		LOG.info("Thread initialized...");
		while(keepAlive()) {
			LOG.info("Thread started...");
			long now = System.currentTimeMillis();
			long freeSpace = API.apiNullUI.global.GetTotalDiskspaceAvailable();
			long minSpace  = 1024L * 1024L * 1024L * Long.parseLong(DataStore.getInstance().getSetting(UserSettings.LOW_SPACE_THRESHOLD, UserSettings.LOW_SPACE_THRESHOLD_DEFAULT));
			if(freeSpace < minSpace && now - Long.parseLong(store.getSetting(LOW_SPACE_LAST_NOTIFY, "0")) > MIN_NOTIFY_DELAY) {
				store.setSetting(LOW_SPACE_LAST_NOTIFY, Long.toString(now));
				SageEventHandlerManager.getInstance().fire(new LowSpaceEvent(new RecordingDevice(freeSpace, minSpace)));
				LOG.info("Firing low space event [min/free = " + minSpace + "/" + freeSpace + "]");
			}
			LOG.info("Thread sleeping...");
			try {
				Thread.sleep(Long.parseLong(store.getSetting(UserSettings.LOW_SPACE_MONITOR_SLEEP, UserSettings.LOW_SPACE_MONITOR_SLEEP_DEFAULT)));
			} catch(InterruptedException e) {
				LOG.info("Thread awakened early.");
			}
		}
		LOG.info("Thread detroyed...");
	}
}
