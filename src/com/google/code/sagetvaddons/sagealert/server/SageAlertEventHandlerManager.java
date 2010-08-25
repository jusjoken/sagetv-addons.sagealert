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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * The lone event handler manager; manages all handler registrations/removals and fires events to proper handlers 
 * @author dbattams
 * @version $Id$
 */
final class SageAlertEventHandlerManager implements HasHandlers {

	static private final Logger LOG = Logger.getLogger(SageAlertEventHandlerManager.class);

	static private final SageAlertEventHandlerManager INSTANCE = new SageAlertEventHandlerManager();
	/**
	 * Single accessor
	 * @return The single instance of the manager
	 */
	static final SageAlertEventHandlerManager get() { return INSTANCE; }


	private Map<String, Set<SageAlertEventHandler>> handlers;
	
	private SageAlertEventHandlerManager() {
		handlers = new HashMap<String, Set<SageAlertEventHandler>>();
	}

	synchronized public void addHandler(SageAlertEventHandler h, String eventId) {
		Set<SageAlertEventHandler> s = handlers.get(eventId);
		if(s == null) {
			s = new HashSet<SageAlertEventHandler>();
			handlers.put(eventId, s);
		}
		s.add(h);
		List<NotificationServerSettings> handlers = new ArrayList<NotificationServerSettings>();
		handlers.add(h.getSettings());
		DataStore.getInstance().registerHandlers(eventId, handlers);
	}
		
	synchronized public void fire(SageAlertEvent e) {
		Set<SageAlertEventHandler> s = handlers.get(e.getSource());
		if(s != null && s.size() > 0) {
			boolean eventFired = false;
			for(SageAlertEventHandler h : s) {
				if(eventFired && !License.get().isLicensed()) {
					LOG.warn("Only notifying one listener for event '" + e.getSubject() + "' because this copy of SageAlert is not licensed!");
					break;
				}
				eventFired = true;
				h.onEvent(e);
				LOG.debug("Event '" + e.getSource() + "' fired to '" + h + "'");
			}
		} else
			LOG.info("No SageAlert handlers registered for fired event: " + e.getSource());
	}
 		
	synchronized public void removeHandler(SageAlertEventHandler h, String eventId) {
		Set<SageAlertEventHandler> s = null;
		s = handlers.get(eventId);
		if(s != null)
			s.remove(h);				
		List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
		list.add(h.getSettings());
		DataStore.getInstance().removeHandlers(eventId, list);
	}

	synchronized public void removeHandlerFromAllEvents(SageAlertEventHandler h) {
		for(String key : handlers.keySet())
			handlers.get(key).remove(h);
		List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
		list.add(h.getSettings());		
		DataStore.getInstance().removeHandlers(list);
	}

	synchronized public void removeHandlers(Collection<SageAlertEventHandler> list, String eventId) {
		for(SageAlertEventHandler h : list)
			removeHandler(h, eventId);
	}

	public Set<SageAlertEventHandler> getHandlers(String eventId) {
		return handlers.get(eventId);
	}
	
	synchronized public void removeAllHandlers(String eventId) {
		Set<SageAlertEventHandler> set = null;
		set = handlers.get(eventId);
		if(set != null)
			set.clear();
		DataStore.getInstance().removeAllHandlers(eventId);
	}
	
	synchronized String dumpState() {
		StringWriter w = new StringWriter();
		for(String c : handlers.keySet()) {
			w.write(c + "\n");
			for(SageAlertEventHandler h : handlers.get(c))
				w.write("\t" + h + "\n");
			w.write("\n");
		}
		return w.toString();
	}

	public void addHandlers(Collection<SageAlertEventHandler> h, String e) {
		for(SageAlertEventHandler s : h)
			addHandler(s, e);
	}
}
