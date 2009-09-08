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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

/**
 * The lone event handler manager; manages all handler registrations/removals and fires events to proper handlers 
 * @author dbattams
 * @version $Id$
 */
final class SageEventHandlerManager implements HasHandlers {

	static private final Logger LOG = Logger.getLogger(SageEventHandlerManager.class);

	static private final SageEventHandlerManager INSTANCE = new SageEventHandlerManager();
	/**
	 * Single accessor
	 * @return The single instance of the manager
	 */
	static final SageEventHandlerManager getInstance() { return INSTANCE; }


	private Map<Class<?>, Set<SageEventHandler>> handlers;
	
	private SageEventHandlerManager() {
		handlers = new HashMap<Class<?>, Set<SageEventHandler>>();
	}

	@Override
	synchronized public void addHandler(SageEventHandler h, SageEvent e) {
		addHandler(h, e.getEventMetaData());
	}
	
	@Override
	synchronized public void addHandler(SageEventHandler h, SageEventMetaData e) {
		try {
			Class<?> cls = Class.forName(e.getClassName());
			Set<SageEventHandler> s = handlers.get(cls);
			if(s == null) {
				s = new HashSet<SageEventHandler>();
				handlers.put(cls, s);
			}
			s.add(h);
		} catch(ClassNotFoundException x) {
			LOG.trace("Class not found error", x);
			LOG.error(x);			
		}
		List<NotificationServerSettings> handlers = new ArrayList<NotificationServerSettings>();
		handlers.add(h.getSettings());
		DataStore.getInstance().registerHandlers(e, handlers);
	}
	
	@Override
	synchronized public void addHandlers(Collection<SageEventHandler> list, SageEvent e) {
		for(SageEventHandler h : list)
			addHandler(h, e.getEventMetaData());
	}
	
	@Override
	synchronized public void fire(SageEvent e) {
		Set<SageEventHandler> s = handlers.get(e.getClass());
		if(s != null) {
			for(SageEventHandler h : s) {
				h.onEvent(e);
				LOG.debug("Event '" + e.getClass().getCanonicalName() + "' fired to '" + h + "'");
			}
		} else {
			LOG.debug("No registered handlers for fired event: " + e.getClass().getCanonicalName());
		}
	}
 
	@Override
	synchronized public void removeAllHandlers(SageEvent e) {
		removeAllHandlers(e.getEventMetaData());
	}
	
	@Override
	synchronized public void removeHandler(SageEventHandler h, SageEvent e) {
		removeHandler(h, e.getEventMetaData());
	}
	
	synchronized private void removeHandler(SageEventHandler h, SageEventMetaData e) {
		Set<SageEventHandler> s = handlers.get(e.getClass());
		if(s != null)
			s.remove(h);				
		List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
		list.add(h.getSettings());
		DataStore.getInstance().removeHandlers(e, list);
	}

	@Override
	synchronized public void removeHandlerFromAllEvents(SageEventHandler h) {
		for(Class<?> key : handlers.keySet())
			handlers.get(key).remove(h);
		List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
		list.add(h.getSettings());		
		DataStore.getInstance().removeHandlers(list);
	}

	@Override
	synchronized public void removeHandlers(Collection<SageEventHandler> list, SageEvent e) {
		for(SageEventHandler h : list)
			removeHandler(h, e);
	}

	@Override
	public Set<SageEventHandler> getHandlers(SageEvent e) {
		return getHandlers(e.getClass());
	}
	
	private Set<SageEventHandler> getHandlers(Class<?> c) {
		return handlers.get(c);
	}

	@Override
	public void addHandlers(Collection<SageEventHandler> h, SageEventMetaData e) {
		addHandlers(h, e);
	}

	@Override
	synchronized public void removeAllHandlers(SageEventMetaData e) {
		Set<SageEventHandler> set = handlers.get(e.getClass());
		if(set != null)
			set.clear();
		DataStore.getInstance().removeAllHandlers(e);
	}
}
