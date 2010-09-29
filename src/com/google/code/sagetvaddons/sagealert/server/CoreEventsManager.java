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

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import sage.SageTVPluginRegistry;

import com.google.code.sagetvaddons.sagealert.server.events.AppStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.ClientConnectionEvent;
import com.google.code.sagetvaddons.sagealert.server.events.ConflictStatusEvent;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedUserEvent;
import com.google.code.sagetvaddons.sagealert.server.events.RecordingEvent;
import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageEvent;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public final class CoreEventsManager {
	static private final Logger LOG = Logger.getLogger(CoreEventsManager.class);
	static private final CoreEventsManager INSTANCE = new CoreEventsManager();
	static public final CoreEventsManager get() { return INSTANCE; }
	
	// This is the master list of core events; those with a double slash at the end have been implemented in SageAlert
	static final public String REC_STARTED = "RecordingStarted"; //
	static final public String REC_COMPLETED = "RecordingCompleted"; //
	static final public String REC_STOPPED = "RecordingStopped"; //
	static final public String PLUGINS_LOADED = "AllPluginsLoaded";
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
	static final public String MEDIA_DELETED_IMPORT_LOST = "ImportLost"; //
	static final public String PLUGIN_STARTED = "PluginStarted";
	static final public String MEDIA_FILE_IMPORTED = "MediaFileImported";
	static final public String IMPORT_STARTED = "ImportingStarted";
	static final public String IMPORT_COMPLETED = "ImportingCompleted";
	static final public String REC_SEG_ADDED = "RecordingSegmentAdded";
	static final public String PLUGIN_STOPPED = "PluginStopped";
	static final public String REC_SCHED_CHANGED = "RecordingScheduleChanged"; //
	static final public String PLAYBACK_FINISHED = "PlaybackFinished";
	static final public String FAV_ADDED = "FavoriteAdded";
	static final public String FAV_MODDED = "FavoriteModified";
	static final public String FAV_REMOVED = "FavoriteRemoved";
	static final public String PLAYLIST_ADDED = "PlaylistAdded";
	static final public String PLAYLIST_MODDED = "PlaylistModified";
	static final public String PLAYLIST_REMOVED = "PlaylistRemoved";
	
	static final public String CLIENT_CONNECTED_SUBJ = "New client connected to SageTV server";
	static final public String CLIENT_CONNECTED_SHORT_MSG = "Client '$0.getAlias()' has connected to the SageTV server.";
	static final public String CLIENT_CONNECTED_MED_MSG = CLIENT_CONNECTED_SHORT_MSG;
	static final public String CLIENT_CONNECTED_LONG_MSG = CLIENT_CONNECTED_SHORT_MSG;
	
	static final public String CLIENT_DISCONNECTED_SUBJ = "Client disconnected from SageTV server";
	static final public String CLIENT_DISCONNECTED_SHORT_MSG = "Client '$0.getAlias()' has disconnected from the SageTV server.";
	static final public String CLIENT_DISCONNECTED_MED_MSG = CLIENT_DISCONNECTED_SHORT_MSG;
	static final public String CLIENT_DISCONNECTED_LONG_MSG = CLIENT_DISCONNECTED_SHORT_MSG;

	static final public String REC_STARTED_SUBJ = "A new recording has started";
	static final public String REC_STARTED_SHORT_MSG = "A new recording has started: $0.GetMediaTitle()";
	static final public String REC_STARTED_MED_MSG = "A new recording has started: $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static final public String REC_STARTED_LONG_MSG = REC_STARTED_MED_MSG;

	static final public String REC_STOPPED_SUBJ = "A recording has stopped";
	static final public String REC_STOPPED_SHORT_MSG = "A recording has stopped: $0.GetMediaTitle()";
	static final public String REC_STOPPED_MED_MSG = "A recording has stopped: $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static final public String REC_STOPPED_LONG_MSG = REC_STOPPED_MED_MSG;

	static final public String REC_COMPLETED_SUBJ = "A recording has completed";
	static final public String REC_COMPLETED_SHORT_MSG = "A recording has completed: $0.GetMediaTitle()";
	static final public String REC_COMPLETED_MED_MSG = "A recording has completed: $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static final public String REC_COMPLETED_LONG_MSG = REC_COMPLETED_MED_MSG;

	static final public String EPG_UPDATED_SUBJ = "An EPG update has completed";
	static final public String EPG_UPDATED_SHORT_MSG = "An EPG update has completed.";
	static final public String EPG_UPDATED_MED_MSG = "An EPG update has completed successfully.";
	static final public String EPG_UPDATED_LONG_MSG = EPG_UPDATED_MED_MSG;

	static final public String CONFLICTS_SUBJ = "Conflict status changed in recording schedule";
	static final public String CONFLICTS_LONG_MSG = "The conflict status of your SageTV recording schedule has changed.  There are now $0.toString() conflict(s); $1.toString() are unresolved.";
	static final public String CONFLICTS_MED_MSG = "Conflict status change: $0.toString() total conflicts; $1.toString() unresolved.";
	static final public String CONFLICTS_SHORT_MSG = EPG_UPDATED_MED_MSG;

	static final public String SYSMSG_POSTED_SUBJ = "New $utils.sysMsgLevelToString($0.GetSystemMessageLevel()) system message generated";
	static final public String SYSMSG_POSTED_LONG_MSG = "$0.GetSystemMessageString()";
	static final public String SYSMSG_POSTED_MED_MSG = "A new system message generated: $0.GetSystemMessageTypeName(); see SageTV server for details.";
	static final public String SYSMSG_POSTED_SHORT_MSG = SYSMSG_POSTED_MED_MSG;

	static final public String MEDIA_DELETED_LOW_SPACE_SUBJ = "Media file deleted (min space)";
	static final public String MEDIA_DELETED_LOW_SPACE_LONG_MSG = "The following media file was deleted because the minimum space setting was violated: $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_LOW_SPACE_MED_MSG = "File deleted (min space): $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_LOW_SPACE_SHORT_MSG = MEDIA_DELETED_LOW_SPACE_MED_MSG;

	static final public String MEDIA_DELETED_KEEP_AT_MOST_SUBJ = "Media file deleted (keep at most)";
	static final public String MEDIA_DELETED_KEEP_AT_MOST_LONG_MSG = "The following media file was deleted because the keep at most setting was exceeded: $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_KEEP_AT_MOST_MED_MSG = "File deleted (keep at most): $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_KEEP_AT_MOST_SHORT_MSG = "File deleted (keep at most): $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";

	static final public String MEDIA_DELETED_USER_SUBJ = "Media file deleted by user '$3.getAlias()'";
	static final public String MEDIA_DELETED_USER_LONG_MSG = "File deleted by '$3.getAlias()': $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_USER_MED_MSG = "File deleted by '$3.getAlias()': $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())/MediaID: $0.GetMediaFileID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_USER_SHORT_MSG = "File deleted by '$3.getAlias()': $0.GetMediaTitle()$utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	
	static final public String MEDIA_DELETED_VERIFY_FAILED_SUBJ = "Media file deleted (verify failed)";
	static final public String MEDIA_DELETED_VERIFY_FAILED_LONG_MSG = "The following media file was deleted because verification failed: $0.GetMediaTitle()/$0.GetMediaFileID()";
	static final public String MEDIA_DELETED_VERIFY_FAILED_MED_MSG = "File deleted (verify failed): $0.GetMediaTitle()/$0.GetMediaFileID()";
	static final public String MEDIA_DELETED_VERIFY_FAILED_SHORT_MSG = MEDIA_DELETED_VERIFY_FAILED_MED_MSG;

	static final public String MEDIA_DELETED_PARTIAL_OR_UNWANTED_SUBJ = "Media file deleted (partial/unwanted)";
	static final public String MEDIA_DELETED_PARTIAL_OR_UNWANTED_LONG_MSG = "The following media file was deleted because it is partial or unwanted: $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_PARTIAL_OR_UNWANTED_MED_MSG = "File deleted (partial/unwanted): $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()/AiringID: $1.GetAiringID()/ShowEID: $2.GetShowExternalID()";
	static final public String MEDIA_DELETED_PARTIAL_OR_UNWANTED_SHORT_MSG = MEDIA_DELETED_PARTIAL_OR_UNWANTED_MED_MSG;

	static final public String MEDIA_DELETED_IMPORT_LOST_SUBJ = "Media file deleted (import lost)";
	static final public String MEDIA_DELETED_IMPORT_LOST_LONG_MSG = "The following media file was deleted because the import location was lost: $0.GetMediaTitle()/MediaID: $0.GetMediaFileID()";
	static final public String MEDIA_DELETED_IMPORT_LOST_MED_MSG = "File deleted (import lost): $0.GetMediaTitle()/$0.GetMediaFileID()";
	static final public String MEDIA_DELETED_IMPORT_LOST_SHORT_MSG = MEDIA_DELETED_IMPORT_LOST_MED_MSG;

	static final public String REC_SCHED_CHANGED_SUBJ = "Recording schedule updated";
	static final public String REC_SCHED_CHANGED_LONG_MSG = "The recording schedule has changed.";
	static final public String REC_SCHED_CHANGED_MED_MSG = REC_SCHED_CHANGED_LONG_MSG;
	static final public String REC_SCHED_CHANGED_SHORT_MSG = REC_SCHED_CHANGED_LONG_MSG;
	
	// If you change any of the defaults for PLAYBACK_* events below then you must also update them in ClientListenerSubscriptionForm
	
	static final public String PLAYBACK_STARTED_SUBJ = "Media playback has started";
	static final public String PLAYBACK_STARTED_LONG_MSG = "Media playback of '$0.GetMediaTitle()' has started on client '$3.getAlias()'";
	static final public String PLAYBACK_STARTED_MED_MSG = "Media playback started ($3.getAlias()): $0.GetMediaTitle()";
	static final public String PLAYBACK_STARTED_SHORT_MSG = PLAYBACK_STARTED_MED_MSG;

	static final public String PLAYBACK_STOPPED_SUBJ = "Media playback has stopped";
	static final public String PLAYBACK_STOPPED_LONG_MSG = "Media playback of '$0.GetMediaTitle()' has stopped on client '$3.getAlias()'";
	static final public String PLAYBACK_STOPPED_MED_MSG = "Media playback stopped ($3.getAlias()): $0.GetMediaTitle()";
	static final public String PLAYBACK_STOPPED_SHORT_MSG = PLAYBACK_STOPPED_MED_MSG;

	private final SageTVPluginRegistry PLUGIN_REG = (SageTVPluginRegistry)API.apiNullUI.pluginAPI.GetSageTVPluginRegistry();
	private final CustomEventLoader CUSTOM_LOADER = new CustomEventLoader();
	
	private CoreEventsManager() {}
	
	public void init() {
		DataStore ds = DataStore.getInstance();
		SageAlertEventMetadataManager mgr = SageAlertEventMetadataManager.get();

		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), PLUGINS_LOADED);
		LOG.info("Subscribed to " + PLUGINS_LOADED + " event!");
		
		PLUGIN_REG.eventSubscribe(CUSTOM_LOADER, PLUGIN_STARTED);
		LOG.info("Subscribed to " + PLUGIN_STARTED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), AppStartedEvent.EVENT_ID);
		mgr.putMetadata(new SageAlertEventMetadata(AppStartedEvent.EVENT_ID, "SageAlert App Started", "Event fired when SageAlert has successfully started.", new ArrayList<String>(), ds.getSetting(AppStartedEvent.EVENT_ID + SageAlertEventMetadata.SUBJ_SUFFIX, AppStartedEvent.SUBJ), ds.getSetting(AppStartedEvent.EVENT_ID + SageAlertEventMetadata.SHORT_SUFFIX, AppStartedEvent.SHORT_MSG), ds.getSetting(AppStartedEvent.EVENT_ID + SageAlertEventMetadata.MED_SUFFIX, AppStartedEvent.MED_MSG), ds.getSetting(AppStartedEvent.EVENT_ID + SageAlertEventMetadata.LONG_SUFFIX, AppStartedEvent.LONG_MSG)));
		LOG.info("Subscribed to " + AppStartedEvent.EVENT_ID + " event!");
		
		PLUGIN_REG.eventSubscribe(RecordingEventsListener.get(), REC_STARTED);
		mgr.putMetadata(new SageAlertEventMetadata(REC_STARTED, "Recording Started", "Event fired when the SageTV system starts a recording.", Arrays.asList(RecordingEvent.EVENT_ARG_TYPES), ds.getSetting(REC_STARTED + SageAlertEventMetadata.SUBJ_SUFFIX, REC_STARTED_SUBJ), ds.getSetting(REC_STARTED + SageAlertEventMetadata.SHORT_SUFFIX, REC_STARTED_SHORT_MSG), ds.getSetting(REC_STARTED + SageAlertEventMetadata.MED_SUFFIX, REC_STARTED_MED_MSG), ds.getSetting(REC_STARTED + SageAlertEventMetadata.LONG_SUFFIX, REC_STARTED_LONG_MSG)));
		LOG.info("Subscribed to " + REC_STARTED + " event!");

		PLUGIN_REG.eventSubscribe(RecordingEventsListener.get(), REC_STOPPED);
		mgr.putMetadata(new SageAlertEventMetadata(REC_STOPPED, "Recording Stopped", "Event fired when the SageTV system stops a recording for any other reason besides it being fully completed.", Arrays.asList(RecordingEvent.EVENT_ARG_TYPES), ds.getSetting(REC_STOPPED + SageAlertEventMetadata.SUBJ_SUFFIX, REC_STOPPED_SUBJ), ds.getSetting(REC_STOPPED + SageAlertEventMetadata.SHORT_SUFFIX, REC_STOPPED_SHORT_MSG), ds.getSetting(REC_STOPPED + SageAlertEventMetadata.MED_SUFFIX, REC_STOPPED_MED_MSG), ds.getSetting(REC_STOPPED + SageAlertEventMetadata.LONG_SUFFIX, REC_STOPPED_LONG_MSG)));
		LOG.info("Subscribed to " + REC_STOPPED + " event!");
		
		PLUGIN_REG.eventSubscribe(RecordingEventsListener.get(), REC_COMPLETED);
		mgr.putMetadata(new SageAlertEventMetadata(REC_COMPLETED, "Recording Completed", "Event fired when the SageTV system completes a recording.", Arrays.asList(RecordingEvent.EVENT_ARG_TYPES), ds.getSetting(REC_COMPLETED + SageAlertEventMetadata.SUBJ_SUFFIX, REC_COMPLETED_SUBJ), ds.getSetting(REC_COMPLETED + SageAlertEventMetadata.SHORT_SUFFIX, REC_COMPLETED_SHORT_MSG), ds.getSetting(REC_COMPLETED + SageAlertEventMetadata.MED_SUFFIX, REC_COMPLETED_MED_MSG), ds.getSetting(REC_COMPLETED + SageAlertEventMetadata.LONG_SUFFIX, REC_COMPLETED_LONG_MSG)));
		LOG.info("Subscribed to " + REC_COMPLETED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), EPG_UPDATED);
		mgr.putMetadata(new SageAlertEventMetadata(EPG_UPDATED, "EPG Updated", "Event fired when the SageTV EPG data has been successfully updated.", new ArrayList<String>(), ds.getSetting(EPG_UPDATED + SageAlertEventMetadata.SUBJ_SUFFIX, EPG_UPDATED_SUBJ), ds.getSetting(EPG_UPDATED + SageAlertEventMetadata.SHORT_SUFFIX, EPG_UPDATED_SHORT_MSG), ds.getSetting(EPG_UPDATED + SageAlertEventMetadata.MED_SUFFIX, EPG_UPDATED_MED_MSG), ds.getSetting(EPG_UPDATED + SageAlertEventMetadata.LONG_SUFFIX, EPG_UPDATED_LONG_MSG)));
		LOG.info("Subscribed to " + EPG_UPDATED + " event!");
		
		PLUGIN_REG.eventSubscribe(ClientEventsListener.get(), CLIENT_CONNECTED);
		mgr.putMetadata(new SageAlertEventMetadata(CLIENT_CONNECTED, "Client Connected", "Event fired when a client, extender, or placeshifter connects to the server.", ClientConnectionEvent.EVENT_ARG_TYPES, ds.getSetting(CLIENT_CONNECTED + SageAlertEventMetadata.SUBJ_SUFFIX, CLIENT_CONNECTED_SUBJ), ds.getSetting(CLIENT_CONNECTED + SageAlertEventMetadata.SHORT_SUFFIX, CLIENT_CONNECTED_SHORT_MSG), ds.getSetting(CLIENT_CONNECTED + SageAlertEventMetadata.MED_SUFFIX, CLIENT_CONNECTED_MED_MSG), ds.getSetting(CLIENT_CONNECTED + SageAlertEventMetadata.LONG_SUFFIX, CLIENT_CONNECTED_LONG_MSG)));
		LOG.info("Subscribed to " + CLIENT_CONNECTED + " event!");
		
		PLUGIN_REG.eventSubscribe(ClientEventsListener.get(), CLIENT_DISCONNECTED);
		mgr.putMetadata(new SageAlertEventMetadata(CLIENT_DISCONNECTED, "Client Disconnected", "Event fired when a client, extender, or placeshifter disconnects from the server.", ClientConnectionEvent.EVENT_ARG_TYPES, ds.getSetting(CLIENT_DISCONNECTED + SageAlertEventMetadata.SUBJ_SUFFIX, CLIENT_DISCONNECTED_SUBJ), ds.getSetting(CLIENT_DISCONNECTED + SageAlertEventMetadata.SHORT_SUFFIX, CLIENT_DISCONNECTED_SHORT_MSG), ds.getSetting(CLIENT_DISCONNECTED + SageAlertEventMetadata.MED_SUFFIX, CLIENT_DISCONNECTED_MED_MSG), ds.getSetting(CLIENT_DISCONNECTED + SageAlertEventMetadata.LONG_SUFFIX, CLIENT_DISCONNECTED_LONG_MSG)));
		LOG.info("Subscribed to " + CLIENT_DISCONNECTED + " event!");
		
		PLUGIN_REG.eventSubscribe(PlaybackEventsListener.get(), PLAYBACK_STARTED);
		LOG.info("Subscribed to " + PLAYBACK_STARTED + " event!");
		
		PLUGIN_REG.eventSubscribe(PlaybackEventsListener.get(), PLAYBACK_STOPPED);
		LOG.info("Subscribed to " + PLAYBACK_STOPPED + " event!");
		
		PLUGIN_REG.eventSubscribe(SystemMessageEventsListener.get(), SYSMSG_POSTED);
		mgr.putMetadata(new SageAlertEventMetadata(INFO_SYSMSG_POSTED, "System Message Posted (INFO)", "Event fired when a system message with level INFO is posted.", Arrays.asList(SystemMessageEvent.ARG_TYPES), ds.getSetting(INFO_SYSMSG_POSTED + SageAlertEventMetadata.SUBJ_SUFFIX, SYSMSG_POSTED_SUBJ), ds.getSetting(INFO_SYSMSG_POSTED + SageAlertEventMetadata.SHORT_SUFFIX, SYSMSG_POSTED_SHORT_MSG), ds.getSetting(INFO_SYSMSG_POSTED + SageAlertEventMetadata.MED_SUFFIX, SYSMSG_POSTED_MED_MSG), ds.getSetting(INFO_SYSMSG_POSTED + SageAlertEventMetadata.LONG_SUFFIX, SYSMSG_POSTED_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(WARN_SYSMSG_POSTED, "System Message Posted (WARN)", "Event fired when a system message with level WARN is posted.", Arrays.asList(SystemMessageEvent.ARG_TYPES), ds.getSetting(WARN_SYSMSG_POSTED + SageAlertEventMetadata.SUBJ_SUFFIX, SYSMSG_POSTED_SUBJ), ds.getSetting(WARN_SYSMSG_POSTED + SageAlertEventMetadata.SHORT_SUFFIX, SYSMSG_POSTED_SHORT_MSG), ds.getSetting(WARN_SYSMSG_POSTED + SageAlertEventMetadata.MED_SUFFIX, SYSMSG_POSTED_MED_MSG), ds.getSetting(WARN_SYSMSG_POSTED + SageAlertEventMetadata.LONG_SUFFIX, SYSMSG_POSTED_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(ERROR_SYSMSG_POSTED, "System Message Posted (ERROR)", "Event fired when a system message with level ERROR is posted.", Arrays.asList(SystemMessageEvent.ARG_TYPES), ds.getSetting(ERROR_SYSMSG_POSTED + SageAlertEventMetadata.SUBJ_SUFFIX, SYSMSG_POSTED_SUBJ), ds.getSetting(ERROR_SYSMSG_POSTED + SageAlertEventMetadata.SHORT_SUFFIX, SYSMSG_POSTED_SHORT_MSG), ds.getSetting(ERROR_SYSMSG_POSTED + SageAlertEventMetadata.MED_SUFFIX, SYSMSG_POSTED_MED_MSG), ds.getSetting(ERROR_SYSMSG_POSTED + SageAlertEventMetadata.LONG_SUFFIX, SYSMSG_POSTED_LONG_MSG)));
		LOG.info("Subscribed to " + SYSMSG_POSTED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), CONFLICTS);
		mgr.putMetadata(new SageAlertEventMetadata(CONFLICTS, "Recording Conflicts", "Fired when the conflict status of your recording schedule changes.", Arrays.asList(ConflictStatusEvent.ARG_TYPES), ds.getSetting(CONFLICTS + SageAlertEventMetadata.SUBJ_SUFFIX, CONFLICTS_SUBJ), ds.getSetting(CONFLICTS + SageAlertEventMetadata.SHORT_SUFFIX, CONFLICTS_SHORT_MSG), ds.getSetting(CONFLICTS + SageAlertEventMetadata.MED_SUFFIX, CONFLICTS_MED_MSG), ds.getSetting(CONFLICTS + SageAlertEventMetadata.LONG_SUFFIX, CONFLICTS_LONG_MSG)));
		LOG.info("Subscribed to " + CONFLICTS + " event!");
		
		PLUGIN_REG.eventSubscribe(MediaDeletedEventsListener.get(), MEDIA_DELETED);
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_LOW_SPACE, "Media Deleted (Low Space)", "Event fired when a media file is deleted by the core due to low disk space.", Arrays.asList(MediaFileDeletedEvent.ARG_TYPES), ds.getSetting(MEDIA_DELETED_LOW_SPACE + SageAlertEventMetadata.SUBJ_SUFFIX, MEDIA_DELETED_LOW_SPACE_SUBJ), ds.getSetting(MEDIA_DELETED_LOW_SPACE + SageAlertEventMetadata.SHORT_SUFFIX, MEDIA_DELETED_LOW_SPACE_SHORT_MSG), ds.getSetting(MEDIA_DELETED_LOW_SPACE + SageAlertEventMetadata.MED_SUFFIX, MEDIA_DELETED_LOW_SPACE_MED_MSG), ds.getSetting(MEDIA_DELETED_LOW_SPACE + SageAlertEventMetadata.LONG_SUFFIX, MEDIA_DELETED_LOW_SPACE_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_KEEP_AT_MOST, "Media Delete (Keep at Most)", "Event fired when a media file is deleted by the core due to a keep at most rule.", Arrays.asList(MediaFileDeletedEvent.ARG_TYPES), ds.getSetting(MEDIA_DELETED_KEEP_AT_MOST + SageAlertEventMetadata.SUBJ_SUFFIX, MEDIA_DELETED_KEEP_AT_MOST_SUBJ), ds.getSetting(MEDIA_DELETED_KEEP_AT_MOST + SageAlertEventMetadata.SHORT_SUFFIX, MEDIA_DELETED_KEEP_AT_MOST_SHORT_MSG), ds.getSetting(MEDIA_DELETED_KEEP_AT_MOST + SageAlertEventMetadata.MED_SUFFIX, MEDIA_DELETED_KEEP_AT_MOST_MED_MSG), ds.getSetting(MEDIA_DELETED_KEEP_AT_MOST + SageAlertEventMetadata.LONG_SUFFIX, MEDIA_DELETED_KEEP_AT_MOST_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_USER, "Media Deleted (User)", "Event fired when a user deletes a media file.", Arrays.asList(MediaFileDeletedUserEvent.ARG_TYPES), ds.getSetting(MEDIA_DELETED_USER + SageAlertEventMetadata.SUBJ_SUFFIX, MEDIA_DELETED_USER_SUBJ), ds.getSetting(MEDIA_DELETED_USER + SageAlertEventMetadata.SHORT_SUFFIX, MEDIA_DELETED_USER_SHORT_MSG), ds.getSetting(MEDIA_DELETED_USER + SageAlertEventMetadata.MED_SUFFIX, MEDIA_DELETED_USER_MED_MSG), ds.getSetting(MEDIA_DELETED_USER + SageAlertEventMetadata.LONG_SUFFIX, MEDIA_DELETED_USER_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_VERIFY_FAILED, "Media Deleted (Verify Failed)", "Event fired when a media file is deleted by the core due to a verification failure.", Arrays.asList(MediaFileDeletedEvent.ARG_TYPES), ds.getSetting(MEDIA_DELETED_VERIFY_FAILED + SageAlertEventMetadata.SUBJ_SUFFIX, MEDIA_DELETED_VERIFY_FAILED_SUBJ), ds.getSetting(MEDIA_DELETED_VERIFY_FAILED + SageAlertEventMetadata.SHORT_SUFFIX, MEDIA_DELETED_VERIFY_FAILED_SHORT_MSG), ds.getSetting(MEDIA_DELETED_VERIFY_FAILED + SageAlertEventMetadata.MED_SUFFIX, MEDIA_DELETED_VERIFY_FAILED_MED_MSG), ds.getSetting(MEDIA_DELETED_VERIFY_FAILED + SageAlertEventMetadata.LONG_SUFFIX, MEDIA_DELETED_VERIFY_FAILED_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_PARTIAL_OR_UNWANTED, "Media Deleted (Partial/Unwanted)", "Event fired when a media file is deleted by the core because it is a partial file or it's unwanted.", Arrays.asList(MediaFileDeletedEvent.ARG_TYPES), ds.getSetting(MEDIA_DELETED_PARTIAL_OR_UNWANTED + SageAlertEventMetadata.SUBJ_SUFFIX, MEDIA_DELETED_PARTIAL_OR_UNWANTED_SUBJ), ds.getSetting(MEDIA_DELETED_PARTIAL_OR_UNWANTED + SageAlertEventMetadata.SHORT_SUFFIX, MEDIA_DELETED_PARTIAL_OR_UNWANTED_SHORT_MSG), ds.getSetting(MEDIA_DELETED_PARTIAL_OR_UNWANTED + SageAlertEventMetadata.MED_SUFFIX, MEDIA_DELETED_PARTIAL_OR_UNWANTED_MED_MSG), ds.getSetting(MEDIA_DELETED_PARTIAL_OR_UNWANTED + SageAlertEventMetadata.LONG_SUFFIX, MEDIA_DELETED_PARTIAL_OR_UNWANTED_LONG_MSG)));
		mgr.putMetadata(new SageAlertEventMetadata(MEDIA_DELETED_IMPORT_LOST, "Media Deleted (Import Lost)", "Event fired when a media file is removed from the core database because the import dir was removed.", Arrays.asList(MediaFileDeletedEvent.ARG_TYPES), ds.getSetting(MEDIA_DELETED_IMPORT_LOST + SageAlertEventMetadata.SUBJ_SUFFIX, MEDIA_DELETED_IMPORT_LOST_SUBJ), ds.getSetting(MEDIA_DELETED_IMPORT_LOST + SageAlertEventMetadata.SHORT_SUFFIX, MEDIA_DELETED_IMPORT_LOST_SHORT_MSG), ds.getSetting(MEDIA_DELETED_IMPORT_LOST + SageAlertEventMetadata.MED_SUFFIX, MEDIA_DELETED_IMPORT_LOST_MED_MSG), ds.getSetting(MEDIA_DELETED_IMPORT_LOST + SageAlertEventMetadata.LONG_SUFFIX, MEDIA_DELETED_IMPORT_LOST_LONG_MSG)));
		LOG.info("Subscribed to " + MEDIA_DELETED + " event!");
		
		PLUGIN_REG.eventSubscribe(AppEventsListener.get(), REC_SCHED_CHANGED);
		mgr.putMetadata(new SageAlertEventMetadata(REC_SCHED_CHANGED, "Recording Schedule Changed", "Fired when the recording schedule has been modified.", new ArrayList<String>(), ds.getSetting(REC_SCHED_CHANGED + SageAlertEventMetadata.SUBJ_SUFFIX, REC_SCHED_CHANGED_SUBJ), ds.getSetting(REC_SCHED_CHANGED + SageAlertEventMetadata.SHORT_SUFFIX, REC_SCHED_CHANGED_SHORT_MSG), ds.getSetting(REC_SCHED_CHANGED + SageAlertEventMetadata.MED_SUFFIX, REC_SCHED_CHANGED_MED_MSG), ds.getSetting(REC_SCHED_CHANGED + SageAlertEventMetadata.LONG_SUFFIX, REC_SCHED_CHANGED_LONG_MSG)));
		LOG.info("Subscribed to " + REC_SCHED_CHANGED + " event!");
	}
	
	public void destroy() {
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), PLUGINS_LOADED);
		LOG.info("Unsubscribed from " + PLUGINS_LOADED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(CUSTOM_LOADER, PLUGIN_STARTED);
		LOG.info("Unsubscribed from " + CUSTOM_LOADER + " event!");
		
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), AppStartedEvent.EVENT_ID);
		LOG.info("Unsubscrbied from " + AppStartedEvent.EVENT_ID + " event!");
		
		PLUGIN_REG.eventUnsubscribe(RecordingEventsListener.get(), REC_STARTED);
		LOG.info("Unsubscribed from " + REC_STARTED + " event!");
		
		PLUGIN_REG.eventUnsubscribe(RecordingEventsListener.get(), REC_STOPPED);
		LOG.info("Unsubscribed from " + REC_STOPPED + " event!");
		
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
		
		PLUGIN_REG.eventUnsubscribe(AppEventsListener.get(), REC_SCHED_CHANGED);
		LOG.info("Unsubscribed from " + REC_SCHED_CHANGED + " event!");
	}
}
