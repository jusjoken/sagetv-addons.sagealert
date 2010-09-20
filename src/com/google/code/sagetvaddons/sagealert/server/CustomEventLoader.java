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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import sage.SageTV;
import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

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
							// Get the csv list of event names
							String eventsList = props.getProperty("events");
							if(eventsList != null) {
								String[] events = eventsList.split(",");
								for(String e : events) {
									e = e.trim();
									if(e.length() > 0) {
										String eCls = props.getProperty(e + "_CLS");
										if(eCls == null || eCls.length() == 0) {
											LOG.error("No class name specified for event ID '" + e + "'; skipping event!");
											continue;
										}
										String eName = props.getProperty(e + "_DISPLAY");
										if(eName == null || eName.length() == 0) {
											LOG.error("No display name for event ID '" + e + "'; skipping event!");
											continue;
										}
										String eDesc = props.getProperty(e + "_DESC");
										if(eDesc == null || eDesc.length() == 0) {
											LOG.error("No description for event ID '" + e + "'; skipping event!");
											continue;
										}
										String eSubj = props.getProperty(e + SageAlertEventMetadata.SUBJ_SUFFIX);
										if(eSubj == null || eSubj.length() == 0) {
											LOG.error("No subject text provided for event ID '" + e + "'; skipping event!");
											continue;
										}
										String eShort = props.getProperty(e + SageAlertEventMetadata.SHORT_SUFFIX);
										if(eShort == null || eShort.length() == 0) {
											LOG.error("No short text provided for event ID '" + e + "'; skipping event!");
											continue;
										}
										String eMed = props.getProperty(e + SageAlertEventMetadata.MED_SUFFIX);
										if(eMed == null || eMed.length() == 0) {
											LOG.error("No medium text provided for event ID '" + e + "'; skipping event!");
											continue;
										}
										String eLong = props.getProperty(e + SageAlertEventMetadata.LONG_SUFFIX);
										if(eLong == null || eLong.length() == 0) {
											LOG.error("No long text provided for event ID '" + e + "'; skipping event!");
											continue;
										}
										List<String> argTypes = new ArrayList<String>();
										boolean foundArg = true;
										int i = 0;
										do {
											String arg = props.getProperty(e + "ARGTYPE" + i++);
											if(arg != null)
												argTypes.add(arg);
											else
												foundArg = false;
										} while(foundArg);
										CustomEventsManager.get().registerCustomEvent(eCls, p.GetPluginIdentifier(), new SageAlertEventMetadata(e, eName, eDesc, argTypes, eSubj, eShort, eMed, eLong));
									}
								}
							} else {
								msg = "No event list provided via 'events' property; ignoring plugin!";
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
