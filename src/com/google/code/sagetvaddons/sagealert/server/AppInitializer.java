/*
 *      Copyright 2009-2010 Battams, Derek
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

import java.util.Collections;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import sage.SageTVPluginRegistry;

import com.google.code.sagetvaddons.sagealert.server.events.AppStartedEvent;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * Initialize/kill daemon threads when the app is started
 * @author dbattams
 * @version $Id$
 */
public final class AppInitializer implements ServletContextListener {
	static private final Logger LOG = Logger.getLogger(AppInitializer.class);

	/**
	 * Path to the file containing the registered event classes
	 */
	static public String REGISTERED_EVENT_CLASSES = null;
	
	/**
	 * Path to the file containing the registered handler classes
	 */
	static public String REGISTERED_HANDLER_CLASSES = null;

	
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		CoreEventsManager.get().destroy();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("Loading registered handlers...");
		registerCurrentHandlers();
		LOG.info("Registering event listeners with SageTV core...");
		CoreEventsManager.get().init();
		LOG.info("Firing app started event...");
		((SageTVPluginRegistry)API.apiNullUI.pluginAPI.GetSageTVPluginRegistry()).postEvent(AppStartedEvent.EVENT_ID, Collections.singletonMap(SageAlertEvent.LISTENER_KEY, new AppStartedEvent()));		
	}
	
	private void registerCurrentHandlers() {
		int count = 0;
		DataStore ds = DataStore.getInstance();
		for(String eventId : ds.getRegisteredEvents()) {
			for(NotificationServerSettings s : ds.getHandlers(eventId)) {
				SageEventHandlerManager.get().addHandler(NotificationServerFactory.getInstance(s), eventId);
				++count;
			}
		}				
		LOG.info("Registered " + count + " saved handler(s).");
	}
}
