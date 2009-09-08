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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.UserSettings;


/**
 * A daemon thread that monitors UI connections to the SageTV server and fires both UiConnectedEvents and UiDisconnectedEvents
 * @author dbattams
 * @version $Id$
 */
final class UiMonitor extends SageRunnable {

	static private final Logger LOG = Logger.getLogger(UiMonitor.class);
		
	private Set<String> remoteConnections;
		
	/**
	 * ctor 
	 */
	UiMonitor() {
		remoteConnections = new HashSet<String>();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("Thread initialized...");
		while(keepAlive()) {
			LOG.info("Thread started...");

			Set<String> currentConnections = new HashSet<String>();
			
			for(String c : API.apiNullUI.global.GetConnectedClients())
				currentConnections.add(c);
			
			for(String c : API.apiNullUI.global.GetUIContextNames())
				currentConnections.add(c);
			
			for(String s : remoteConnections)
				if(!currentConnections.contains(s)) {
					SageEventHandlerManager.getInstance().fire(new UiDisconnectedEvent(s));
					LOG.info("Firing UI disconnected event for '" + s + "'");
				}
			
			for(String s : currentConnections)
				if(!remoteConnections.contains(s)) {
					SageEventHandlerManager.getInstance().fire(new UiConnectedEvent(s));
					LOG.info("Firing UI connected event for '" + s + "'");
				}
			
			remoteConnections = currentConnections;				

			LOG.info("Thread sleeping...");
			
			try {
				Thread.sleep(Long.parseLong(DataStore.getInstance().getSetting(UserSettings.UI_MONITOR_SLEEP, UserSettings.UI_MONITOR_SLEEP_DEFAULT)));
			} catch(InterruptedException e) {
				LOG.info("Thread awakened early...");
			}
		}
		LOG.warn("Thread destroyed.");
	}	
}
