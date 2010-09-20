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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import sage.SageTV;
import sage.SageTVEventListener;

/**
 * @author dbattams
 *
 */
final class CustomEventLoader implements Runnable, SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(CustomEventLoader.class);
		
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			boolean doScan = true;
			while(!((Boolean)SageTV.api("IsPluginStartupComplete", null))) {
				LOG.info("Waiting for core to load all active plugins...");
				try {
					Thread.sleep(15000);
				} catch(InterruptedException e) {
					LOG.info("Received shutdown signal...");
					doScan = false;
					break;
				}
			}
			if(doScan) {
				LOG.info("SageAlert is now looking for custom events to register from other plugins...");
				for(PluginAPI.Plugin p : API.apiNullUI.pluginAPI.GetInstalledPlugins()) {
					String resVal = p.GetPluginResourcePath();
					String msg = null;
					if(resVal == null || resVal.length() == 0)
						msg = "Invalid resource path defined for plugin, ignoring plugin!";
					else {
						File resDir = new File(new File(System.getProperty("user.dir")), resVal);
						File input = new File(resDir, "sagealert.properties");
						if(!resDir.exists())
							msg = "Defined resource path does not exist, ignoring plugin! [" + resDir.getAbsolutePath() + "]";
						else if(!input.exists())
							msg = "No sagealert.properties file found in resource path, ignoring plugin!";
						else if(!input.canRead())
							msg = "Unable to read the sagealert.properties file, ignoring plugin!";
						else {
							Properties props = new Properties();
							try {
								props.load(new FileReader(input));
							} catch (IOException e) {
								LOG.error("IOError", e);
							}
							for(Object eventId : props.keySet()) {
								String[] data = props.getProperty(eventId.toString()).split(":", 3);
								String eventCls, eventName, eventDesc;
								if(data.length == 3) {
									eventCls = data[0];
									eventName = data[1];
									eventDesc = data[2];
								} else if(data.length == 2) {
									eventCls = data[0];
									eventName = data[1];
									eventDesc = "";
								} else if(data[0].length() > 0){
									eventCls = data[0];
									eventName = "Unknown";
									eventDesc = "";
								} else
									continue;
								CustomEventsManager.get().registerCustomEvent(eventCls, eventId.toString(), p.GetPluginIdentifier(), eventName, eventDesc);
							}
						}
					}
					if(msg != null)
						LOG.info(msg);
				}				
			}
		} catch (InvocationTargetException e) {
			LOG.error("APIError", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		run();
	}
}
