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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.HandlerService;
import com.google.code.sagetvaddons.sagealert.client.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server side implementation of the HandlerService RPC interface
 * @author dbattams
 * @version $Id$
 */
@SuppressWarnings("serial")
public final class HandlerServiceImpl extends RemoteServiceServlet implements	HandlerService {
	
	static private final Logger LOG = Logger.getLogger(HandlerServiceImpl.class);
	static private final Collection<SageEventMetaData> EVENT_METADATA = new ArrayList<SageEventMetaData>();

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.HandlerService#getHandlers(com.google.code.sagetvaddons.sagealert.client.SupportedEvent)
	 */
	@Override
	public List<NotificationServerSettings> getHandlers(SageEventMetaData event) {
		return DataStore.getInstance().getHandlers(event);
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.HandlerService#setHandlers(com.google.code.sagetvaddons.sagealert.client.SupportedEvent, java.util.List)
	 */
	@Override
	public void setHandlers(SageEventMetaData e, List<NotificationServerSettings> handlers) {
		SageEventHandlerManager mgr = SageEventHandlerManager.getInstance();
		mgr.removeAllHandlers(e);
		for(NotificationServerSettings s : handlers) {
			mgr.addHandler(NotificationServerFactory.getInstance(s), e);
		}
	}
	
	@Override
	public Collection<SageEventMetaData> getEventMetaData() {
		if(EVENT_METADATA.size() == 0) {
			String title, desc;
			try {
				for(Class<?> cls : RegisteredClasses.getSageEventClasses()) {
					title = cls.getSimpleName();
					desc = cls.getCanonicalName();
					Field f = null;
					try {
						f = cls.getDeclaredField("EVENT_METADATA");
						f.setAccessible(true);
						title = ((SageEventMetaData)f.get(null)).getEventTitle();
						desc = ((SageEventMetaData)f.get(null)).getEventDescription();
					} catch(NoSuchFieldException e) {
						LOG.error("Class '" + cls.getCanonicalName() + "' does not provide field: static SageEventMetaData EVENT_METADATA");
					} catch(IllegalAccessException e) {
						LOG.error("Unexpected error", e);
					}
					EVENT_METADATA.add(new SageEventMetaData(cls.getCanonicalName(), title, desc));
				}
			} catch(IOException e) {
				LOG.error("IO exception reading registered class file", e);
				throw new RuntimeException(e);
			}
		}
		return EVENT_METADATA;
	}

	@Override
	public void testServer(NotificationServerSettings settings) {
		SageEventHandlerManager mgr = SageEventHandlerManager.getInstance();
		TestEvent e = new TestEvent();
		SageEventHandler h = NotificationServerFactory.getInstance(settings);
		mgr.addHandler(h, e);
		mgr.fire(e);
		mgr.removeHandler(h, e);
	}
}
