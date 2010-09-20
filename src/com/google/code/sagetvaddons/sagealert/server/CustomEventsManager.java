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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;
import sage.SageTVPluginRegistry;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertCustomEvent;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
final class CustomEventsManager implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(CustomEventsManager.class);

	static private final CustomEventsManager INSTANCE = new CustomEventsManager();
	static final CustomEventsManager get() { return INSTANCE; }

	static private final SageTVPluginRegistry REGISTRY = (SageTVPluginRegistry)API.apiNullUI.pluginAPI.GetSageTVPluginRegistry();

	private final Map<String, Class<SageAlertCustomEvent>> events;

	private CustomEventsManager() {
		events = new HashMap<String, Class<SageAlertCustomEvent>>();
	}

	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		fireCustomEvent(arg0, arg1);
	}

	@SuppressWarnings("unchecked")
	synchronized void registerCustomEvent(String clsName, String pluginId, SageAlertEventMetadata metadata) {
		if(pluginId == null || pluginId.length() == 0)
			LOG.error("Custom event registration requires a valid plugin id!  Registration ignored!");
		else if(clsName == null || clsName.length() == 0)
			LOG.error("Custom event registration requires a valid class name!  Registration ignored!");
		else if(metadata.getEventId() == null || metadata.getEventId().length() == 0)
			LOG.error("Custom event registration requires a valid event id!  Registration ignored!");
		else {
			StringBuilder msg = new StringBuilder("Attempting registration of custom event '" + metadata.getEventId() + "' for plugin '" + pluginId + "' using class '" + clsName + "'... ");
			try {
				Class<?> cls = Class.forName(clsName);
				if(SageAlertCustomEvent.class.isAssignableFrom(cls)) {
					events.put(metadata.getEventId(), (Class<SageAlertCustomEvent>)cls);
					SageAlertEventMetadataManager mdMgr = SageAlertEventMetadataManager.get();
					mdMgr.putMetadata(metadata);
					REGISTRY.eventSubscribe(this, metadata.getEventId());
					msg.append("SUCCESS");
				} else
					msg.append("FAILED (class does not implement " + SageAlertCustomEvent.class.getCanonicalName() + ")");
				LOG.info(msg.toString());
			} catch (ClassNotFoundException e) {
				msg.append("FAILED (class not found)");
				LOG.info(msg.toString());
				LOG.error("ClassNotFound", e);
			}
		}
	}

	synchronized private void fireCustomEvent(String eventId, Map<?, ?> args) {
		if(LOG.isDebugEnabled()) {
			StringBuilder msg = new StringBuilder();
			msg.append("Firing custom event '" + eventId + "' : " + args.toString());
			LOG.debug(msg.toString());
		}
		Class<SageAlertCustomEvent> cls = events.get(eventId);
		if(cls != null) {
			SageAlertEventMetadata metadata = SageAlertEventMetadataManager.get().getMetadata(eventId);
			try {
				SageAlertCustomEvent event = cls.getConstructor(Map.class, SageAlertEventMetadata.class).newInstance(args, metadata);
				event.setSubject(new ApiInterpreter(event.getArgs(), metadata.getSubject()).interpret());
				event.setShortDescription(new ApiInterpreter(event.getArgs(), metadata.getShortMsg()).interpret());
				event.setMediumDescription(new ApiInterpreter(event.getArgs(), metadata.getMedMsg()).interpret());
				event.setLongDescription(new ApiInterpreter(event.getArgs(), metadata.getLongMsg()).interpret());
				SageAlertEventHandlerManager.get().fire(event);
			} catch (SecurityException e) {
				LOG.error("SecurityException", e);
			} catch (NoSuchMethodException e) {
				StringBuilder msg = new StringBuilder();
				for(Constructor<?> ctor : cls.getConstructors())
					msg.append(ctor.toString() + "\n");
				LOG.error("NoSuchMethod", e);
				LOG.error("Available ctors:\n" + msg.toString());
			} catch (IllegalArgumentException e) {
				LOG.error("IllegalArgument", e);
			} catch (InstantiationException e) {
				LOG.error("InstantiationException", e);
			} catch (IllegalAccessException e) {
				LOG.error("IllegalAccess", e);
			} catch (InvocationTargetException e) {
				LOG.error("InvocationTargetException", e);
			}
		}		
	}
}
