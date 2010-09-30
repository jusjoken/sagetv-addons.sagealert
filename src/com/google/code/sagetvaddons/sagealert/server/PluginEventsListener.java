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
import gkusnick.sagetv.api.PluginAPI;

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.PluginStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.PluginStoppedEvent;

/**
 * @author dbattams
 *
 */
public class PluginEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(PluginEventsListener.class);
	
	static private final PluginEventsListener INSTANCE = new PluginEventsListener();
	static public final PluginEventsListener get() { return INSTANCE; }
	
	/**
	 * 
	 */
	private PluginEventsListener() {}

	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0 + " :: " + arg1.toString());
		PluginAPI.Plugin p = API.apiNullUI.pluginAPI.Wrap(arg1.get("Plugin"));
		if(CoreEventsManager.PLUGIN_STARTED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new PluginStartedEvent(p, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLUGIN_STARTED)));
		else if(CoreEventsManager.PLUGIN_STOPPED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new PluginStoppedEvent(p, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLUGIN_STOPPED)));
		else
			LOG.error("Unsupported event received! [" + arg0 + "]");
	}

}
