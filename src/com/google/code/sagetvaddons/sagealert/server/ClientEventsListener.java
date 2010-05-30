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

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.ClientConnectedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.ClientDisconnectedEvent;
import com.google.code.sagetvaddons.sagealert.shared.Client;

/**
 * @author dbattams
 *
 */
final class ClientEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(ClientEventsListener.class);
	static private final ClientEventsListener INSTANCE = new ClientEventsListener();
	static final ClientEventsListener get() { return INSTANCE; }
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0);
		if(CoreEventsManager.CLIENT_CONNECTED.equals(arg0)) {
			String ipAddr = (String)arg1.get("IPAddress");
			String macAddr = (String)arg1.get("MACAddress");
			DataStore ds = DataStore.getInstance();
			Client clnt;
			if(macAddr == null)
				clnt = ds.getClient(ipAddr);
			else
				clnt = ds.getClient(macAddr);
			SageEventHandlerManager.get().fire(new ClientConnectedEvent(clnt));
			ds.registerClient(clnt.getId());
		} else if(CoreEventsManager.CLIENT_DISCONNECTED.equals(arg0)) {
			String ipAddr = (String)arg1.get("IPAddress");
			String macAddr = (String)arg1.get("MACAddress");
			Client clnt;
			if(macAddr == null)
				clnt = DataStore.getInstance().getClient(ipAddr);
			else
				clnt = DataStore.getInstance().getClient(macAddr);
			SageEventHandlerManager.get().fire(new ClientDisconnectedEvent(clnt));			
		} else
			LOG.error("Unhandled event: " + arg0);
	}
	
	private ClientEventsListener() {}
}
