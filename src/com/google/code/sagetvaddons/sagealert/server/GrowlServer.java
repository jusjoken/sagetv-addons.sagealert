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

import java.util.HashMap;
import java.util.Map;

import net.sf.libgrowl.Application;
import net.sf.libgrowl.GrowlConnector;
import net.sf.libgrowl.IResponse;
import net.sf.libgrowl.Notification;
import net.sf.libgrowl.NotificationType;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.shared.GrowlServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * An event handler that notifies a Growl server
 * @author dbattams
 * @version $Id$
 */
final class GrowlServer implements SageAlertEventHandler {
	static private final Map<GrowlServerSettings, GrowlServer> SERVERS = new HashMap<GrowlServerSettings, GrowlServer>();
	static private final Logger LOG = Logger.getLogger(GrowlServer.class);
	
	/**
	 * Get the Growl server for the given settings; only one instance of a server is created for any given settings object
	 * @param settings The settings required to connect to the server
	 * @return The single instance of the growl server for the given settings
	 */
	synchronized static final GrowlServer getServer(GrowlServerSettings settings) {
		GrowlServer srv = SERVERS.get(settings);
		if(srv == null) {
			srv = new GrowlServer(settings);
			SERVERS.put(settings, srv);
			LOG.debug(SERVERS.size() + " server(s) now in the cache.");
		} else
			srv.updateSettings(settings); // In case the pwd was updated
		return srv;
	}
	
	synchronized static final void deleteServer(GrowlServerSettings settings) {
		if(SERVERS.remove(settings) != null)
			LOG.info("Removed '" + settings.toString() + "' from cache!");
	}

	static private final Application GROWL_APP = new Application("SageTV");
	static private final NotificationType[] NOTIFICATION_TYPES = {
		new NotificationType("General"),
		new NotificationType("System Message"),
		new NotificationType("Low Space"),
		new NotificationType("Recording Conflict"),
		new NotificationType("Recording Started"),
		new NotificationType("Recording Finished"),
		new NotificationType("UI Connected"),
		new NotificationType("UI Disconnected"),
		new NotificationType("Playing Media"),
		new NotificationType("Remote Info"),
		new NotificationType("Remote Warning"),
		new NotificationType("Remote Error")
	};
	
	private GrowlServerSettings settings;
	private GrowlConnector gConn;
	private boolean isRegistered;
	
	private GrowlServer(GrowlServerSettings settings) {
		this.settings = settings;
		gConn = new GrowlConnector(settings.getHost(), settings.getPassword());
		isRegistered = false;
		register();
	}

	private boolean register() {
		if(!isRegistered) {
			if(gConn.register(GROWL_APP, NOTIFICATION_TYPES) == IResponse.OK) {
				isRegistered = true;
				LOG.info("Connected successfully: '" + settings + "'");
			}
			else {
				String msg = "Failed to connect with '" + settings + "'; will try again on next use of object";
				LOG.error(msg);
				isRegistered = false;
			}			
		}
		return isRegistered;
	}
		
	synchronized public GrowlServerSettings getSettings() {
		return settings;
	}

	/**
	 * Replace the settings used to connect to the server
	 * @param settings The new settings to be used to connect to this server
	 */
	synchronized public void updateSettings(GrowlServerSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public String toString() { return settings.toString(); }

	public void onEvent(final SageAlertEvent e) {
		new Thread() {
			@Override
			public void run() {
				synchronized(GrowlServer.this) {
					Notification msg = new Notification(GROWL_APP, NOTIFICATION_TYPES[getNotificationType(e)], e.getSubject(), e.getLongDescription());
					if((isRegistered || register()) && gConn.notify(msg) == IResponse.OK)
						LOG.info("'" + e.getSubject() + "' notification sent successfully to '" + settings + "'");
					else
						LOG.error("'" + e.getSubject() + "' notification FAILED to '" + settings + "'");
				}
			}
		}.start();
	}

	private int getNotificationType(SageAlertEvent e) {
//		if(e instanceof SystemMessageEvent)
//			return 1;
//		if(e instanceof LowSpaceEvent)
//			return 2;
//		if(e instanceof RecordingConflictEvent)
//			return 3;
//		if(e instanceof RecordingStartedEvent)
//			return 4;
//		if(e instanceof RecordingFinishedEvent)
//			return 5;
//		if(e instanceof UiConnectedEvent)
//			return 6;
//		if(e instanceof UiDisconnectedEvent)
//			return 7;
//		if(e instanceof PlayingMediaEvent)
//			return 8;
//		if(e instanceof RemoteInfoEvent)
//			return 9;
//		if(e instanceof RemoteWarningEvent)
//			return 10;
//		if(e instanceof RemoteErrorEvent)
//			return 11;
		return 0; // Unknown event; someone probably added a new event and didn't map it here
	}

	public void destroy() {
		deleteServer(settings);
	}

	public void setSettings(NotificationServerSettings settings) {
		throw new UnsupportedOperationException("Growl server settings cannot be modified!");
	}
}
