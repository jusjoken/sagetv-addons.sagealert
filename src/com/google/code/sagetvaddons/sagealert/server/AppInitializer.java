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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.code.sagetvaddons.sagealert.client.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

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

	private Map<SageRunnable, Thread> monitors;
	private Date startTime;
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		for(SageRunnable r : monitors.keySet()) {
			Thread t = monitors.get(r);
			if(t.isAlive()) {
				r.setKeepAlive(false);
				t.interrupt();
				LOG.info("Marked '" + t.getName() + "' for destruction.");
			} else
				LOG.info("Thread '" + t.getName() + "' was already dead.");
		}
		LOG.fatal("^^^ APP ENDED: " + startTime + "/" + new Date() + " ^^^");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		monitors = new HashMap<SageRunnable, Thread>();
		startTime = new Date();
		PropertyConfigurator.configure("sagealert.log4j.properties");
		
		LOG.fatal("^^^ APP STARTED: " + startTime + " ^^^");
		REGISTERED_EVENT_CLASSES = sce.getServletContext().getRealPath("/WEB-INF/config/event_classes.conf");
		LOG.debug("RegEventClsFile = " + REGISTERED_EVENT_CLASSES);
		REGISTERED_HANDLER_CLASSES = sce.getServletContext().getRealPath("/WEB-INF/config/monitor_classes.conf");
		LOG.debug("RegHandlerClsFile = " + REGISTERED_HANDLER_CLASSES);
		
		registerCurrentHandlers();
		launchMonitorThreads();

		LOG.info("Context initialized...");
	}
	
	private void launchMonitorThreads() {
		try {
			for(Class<?> cls : RegisteredClasses.getSageMonitorClasses()) {
				SageRunnable runnable = (SageRunnable)cls.newInstance();
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				t.setName("SageAlert-" + cls.getSimpleName());
				t.start();
				monitors.put(runnable, t);
				LOG.info("Launched monitor thread: " + cls.getSimpleName());
			}
		} catch(IOException e) {
			LOG.trace("IO error detected", e);
			LOG.error(e);
		} catch(IllegalAccessException e) {
			LOG.trace("Constructor is not visible", e);
			LOG.error(e);
		} catch(InstantiationException e) {
			LOG.trace("Unable to instantiate monitor class", e);
			LOG.error(e);
		}
	}
	
	private void registerCurrentHandlers() {
		int count = 0;
		try {
			for(Class<?> cls : RegisteredClasses.getSageEventClasses()) {
				// Let's try to get the metadata from or if it's not there then create a generic one for it
				SageEventMetaData metadata = null;
				try {
					Field f = cls.getDeclaredField("EVENT_METADATA");
					f.setAccessible(true);
					metadata = (SageEventMetaData)f.get(null); 
				} catch(NoSuchFieldException e) {
					metadata = new SageEventMetaData(cls.getCanonicalName(), cls.getSimpleName(), cls.getCanonicalName());
					LOG.warn("Class '" + cls.getCanonicalName() + "' does not define static SageEventMetaData EVENT_METADATA field; using generic one instead (you probably want to fix this)");
				} catch(IllegalAccessException e) {
					LOG.trace("Unexpected exception thrown", e);
					LOG.fatal(e);
				}

				for(NotificationServerSettings s : DataStore.getInstance().getHandlers(metadata)) {
					SageEventHandlerManager.getInstance().addHandler(NotificationServerFactory.getInstance(s), metadata);
					++count;
				}
			}
		} catch(IOException e) {
			LOG.trace("IO exception thrown", e);
			LOG.fatal(e);
		}
				
		LOG.info("Registered " + count + " saved handler(s).");
	}
}
