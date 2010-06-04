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

import org.apache.log4j.Logger;

import gkusnick.sagetv.api.API;
import sage.SageTVPluginRegistry;

import com.google.code.sagetvaddons.sagealert.server.events.AppStartedEvent;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
final public class CoreEventsManager {
	static private final Logger LOG = Logger.getLogger(CoreEventsManager.class);
	static private final CoreEventsManager INSTANCE = new CoreEventsManager();
	static final public CoreEventsManager get() { return INSTANCE; }
	
	static final public String REC_STARTED = "RecordingStarted"; //
	static final public String REC_COMPLETED = "RecordingCompleted"; //
	static final public String PLUGINS_LOADED = "AllPluginsLoaded"; //
	static final public String CONFLICTS = "ConflictStatusChanged"; //
	static final public String SYSMSG_POSTED = "SystemMessagePosted"; //
	static final public String INFO_SYSMSG_POSTED = "InfoSysMsgPosted"; //
	static final public String WARN_SYSMSG_POSTED = "WarnSysMsgPosted"; //
	static final public String ERROR_SYSMSG_POSTED = "ErrorSysMsgPosted"; //
	static final public String EPG_UPDATED = "EPGUpdateCompleted"; //
	static final public String PLAYBACK_STARTED = "PlaybackStarted"; //
	static final public String PLAYBACK_STOPPED = "PlaybackStopped"; //
	static final public String CLIENT_CONNECTED = "ClientConnected"; //
	static final public String CLIENT_DISCONNECTED = "ClientDisconnected"; //
	static final public String MEDIA_DELETED = "MediaFileRemoved"; //
	static final public String MEDIA_DELETED_LOW_SPACE = "Diskspace"; //
	static final public String MEDIA_DELETED_KEEP_AT_MOST = "KeepAtMost"; //
	static final public String MEDIA_DELETED_USER = "User"; //
	static final public String MEDIA_DELETED_VERIFY_FAILED = "VerifyFailed"; //
	static final public String MEDIA_DELETED_PARTIAL_OR_UNWANTED = "PartialOrUnwanted"; //
		
	private final SageTVPluginRegistry PLUGIN_REG = (SageTVPluginRegistry)API.apiNullUI.pluginAPI.GetSageTVPluginRegistry();
	
	private CoreEventsManager() {}
	
	public void init() {
		SageAlertEventMetadataManager mgr = SageAlertEventMetadataManager.get();

		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), PLUGINS_LOADED);
		LOG.info("Subscribed to " + PLUGINS_LOADED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), AppStartedEvent.EVENT_ID);
		mgr.putMetadata(new SageAlertEventMetadata(AppStartedEvent.EVENT_ID, "SageAlert App Started", "Event fired when SageAlert has successfully started."));
		LOG.info("Subscribed to " + AppStartedEvent.EVENT_ID + " event!");
		
		PLUGIN_REG.eventSubscribe(RecordingEventsListener.get(), REC_STARTED);
		mgr.putMetadata(new SageAlertEventMetadata(REC_STARTED, "Recording Started", "Event fired when the SageTV system starts a recording."));
		LOG.info("Subscribed to " + REC_STARTED + " event!");
		
		PLUGIN_REG.eventSubscribe(RecordingEventsListener.get(), REC_COMPLETED);
		mgr.putMetadata(new SageAlertEventMetadata(REC_COMPLETED, "Recording Completed", "Event fired when the SageTV system completes a recording."));
		LOG.info("Subscribed to " + REC_COMPLETED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), EPG_UPDATED);
		mgr.putMetadata(new SageAlertEventMetadata(EPG_UPDATED, "EPG Updated", "Event fired when the SageTV EPG data has been successfully updated."));
		LOG.info("Subscribed to " + EPG_UPDATED + " event!");
		
		PLUGIN_REG.eventSubscribe(ClientEventsListener.get(), CLIENT_CONNECTED);
		mgr.putMetadata(new SageAlertEventMetadata(CLIENT_CONNECTED, "Client Connected", "Event fired when a client, extender, or placeshifter connects to the server."));
		LOG.info("Subscribed to " + CLIENT_CONNECTED + " event!");
		
		PLUGIN_REG.eventSubscribe(ClientEventsListener.get(), CLIENT_DISCONNECTED);
		mgr.putMetadata(new SageAlertEventMetadata(CLIENT_DISCONNECTED, "Client Disconnected", "Event fired when a client, extender, or placeshifter disconnects from the server."));
		LOG.info("Subscribed to " + CLIENT_DISCONNECTED + " event!");
		
		PLUGIN_REG.eventSubscribe(PlaybackEventsListener.get(), PLAYBACK_STARTED);
		LOG.info("Subscribed to " + PLAYBACK_STARTED + " event!");
		
		PLUGIN_REG.eventSubscribe(PlaybackEventsListener.get(), PLAYBACK_STOPPED);
		LOG.info("Subscribed to " + PLAYBACK_STOPPED + " event!");
		
		PLUGIN_REG.eventSubscribe(SystemMessageEventsListener.get(), SYSMSG_POSTED);
		mgr.putMetadata(new SageAlertEventMetadata(INFO_SYSMSG_POSTED, "System Message Posted (INFO)", "Event fired when a system message with level INFO is posted."));
		mgr.putMetadata(new SageAlertEventMetadata(WARN_SYSMSG_POSTED, "System Message Posted (WARN)", "Event fired when a system message with level WARN is posted."));
		mgr.putMetadata(new SageAlertEventMetadata(ERROR_SYSMSG_POSTED, "System Message Posted (ERROR)", "Event fired when a system message with level ERROR is posted."));
		LOG.info("Subscribed to " + SYSMSG_POSTED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), CONFLICTS);
		mgr.putMetadata(new SageAlertEventMetadata(CONFLICTS, "Recording Conflicts", "Fired when the conflict status of your recording schedule changes."));
		LOG.info("Subscribed to " + CONFLICTS + " event!");
		
		PLUGIN_REG.eventSubscribe(MediaDeletedEventsListener.get(), MEDIA_DELETED);
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_LOW_SPACE, "Media Deleted (Low Space)", "Event fired when a media file is deleted by the core due to low disk space."));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_KEEP_AT_MOST, "Media Delete (Keep at Most)", "Event fired when a media file is deleted by the core due to a keep at most rule."));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_USER, "Media Deleted (User)", "Event fired when a user deletes a media file."));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_VERIFY_FAILED, "Media Deleted (Verify Failed)", "Event fired when a media file is deleted by the core due to a verification failure."));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_PARTIAL_OR_UNWANTED, "Media Deleted (Partial/Unwanted)", "Event fired when a media file is deleted by the core because it is a partial file or it's unwanted."));
		LOG.info("Subscribed to " + MEDIA_DELETED + " event!");
	}
	
	public void destroy() {
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), PLUGINS_LOADED);
		LOG.info("Unsubscribed from " + PLUGINS_LOADED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), AppStartedEvent.EVENT_ID);
		LOG.info("Unsubscrbied from " + AppStartedEvent.EVENT_ID + " event!");
		
		PLUGIN_REG.eventUnsubscribe(RecordingEventsListener.get(), REC_STARTED);
		LOG.info("Unsubscribed from " + REC_STARTED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(RecordingEventsListener.get(), REC_COMPLETED);
		LOG.info("Unsubscribed from " + REC_COMPLETED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), EPG_UPDATED);
		LOG.info("Unsubscribed from " + EPG_UPDATED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(ClientEventsListener.get(), CLIENT_CONNECTED);
		LOG.info("Unsubscribed from " + CLIENT_CONNECTED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(ClientEventsListener.get(), CLIENT_DISCONNECTED);
		LOG.info("Unsubscribed from " + CLIENT_DISCONNECTED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(PlaybackEventsListener.get(), PLAYBACK_STARTED);
		LOG.info("Unsubscribed from " + PLAYBACK_STARTED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(PlaybackEventsListener.get(), PLAYBACK_STOPPED);
		LOG.info("Unsubscribed from " + PLAYBACK_STOPPED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(SystemMessageEventsListener.get(), SYSMSG_POSTED);
		LOG.info("Unsubscribed from " + SYSMSG_POSTED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), CONFLICTS);
		LOG.info("Unsubscribed from " + CONFLICTS + " event!");
		
		PLUGIN_REG.eventUnsubscribe(MediaDeletedEventsListener.get(), MEDIA_DELETED);
		LOG.info("Unsubscribed from " + MEDIA_DELETED + " event!");
	}
}
