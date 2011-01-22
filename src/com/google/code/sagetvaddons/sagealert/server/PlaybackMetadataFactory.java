/*
 *      Copyright 2011 Battams, Derek
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

import gkusnick.sagetv.api.MediaFileAPI;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.server.events.PlaybackEvent;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.code.sagetvaddons.sagealert.shared.Client.EventType;

/**
 * @author dbattams
 *
 */
final class PlaybackMetadataFactory {
	static private final Logger LOG = Logger.getLogger(PlaybackMetadataFactory.class);
	static final SageAlertEventMetadata getMetadata(Client clnt, Client.EventType action, MediaFileAPI.MediaFile mf) {
		String type, name, desc, subj, shortMsg, med, longMsg;
		if(mf.IsTVFile()) {
			type = "TV";
			name = (action == EventType.STARTS ? "TV Playback Started: " : "TV Playback Stopped: ") + clnt.getAlias();
			desc = "Event fired when client '" + clnt.getAlias() + "' " + (action == EventType.STARTS ? "starts to play back" : "stops playing back") + " a TV recording.";
			subj = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_TV_STARTED_SUBJ : CoreEventsManager.PLAYBACK_TV_STOPPED_SUBJ;
			shortMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_TV_STARTED_SHORT_MSG : CoreEventsManager.PLAYBACK_TV_STOPPED_SHORT_MSG;
			med = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_TV_STARTED_MED_MSG : CoreEventsManager.PLAYBACK_TV_STOPPED_MED_MSG;
			longMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_TV_STARTED_LONG_MSG : CoreEventsManager.PLAYBACK_TV_STOPPED_LONG_MSG;
		} else if(mf.IsDVD() || mf.IsBluRay()) {
			type = "DVD";
			name = (action == EventType.STARTS ? "DVD/BluRay Playback Started: " : "DVD/BluRay Playback Stopped: ") + clnt.getAlias();
			desc = "Event fired when client '" + clnt.getAlias() + "' " + (action == EventType.STARTS ? "starts to play back" : "stops playing back") + " a DVD/BluRay.";
			subj = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_DVD_STARTED_SUBJ : CoreEventsManager.PLAYBACK_DVD_STOPPED_SUBJ;
			shortMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_DVD_STARTED_SHORT_MSG : CoreEventsManager.PLAYBACK_DVD_STOPPED_SHORT_MSG;
			med = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_DVD_STARTED_MED_MSG : CoreEventsManager.PLAYBACK_DVD_STOPPED_MED_MSG;
			longMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_DVD_STARTED_LONG_MSG : CoreEventsManager.PLAYBACK_DVD_STOPPED_LONG_MSG;
		} else if(mf.IsVideoFile()) {
			type = "IMPORT";
			name = (action == EventType.STARTS ? "Import Playback Started: " : "Import Playback Stopped: ") + clnt.getAlias();
			desc = "Event fired when client '" + clnt.getAlias() + "' " + (action == EventType.STARTS ? "starts to play back" : "stops playing back") + " an imported video.";
			subj = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_IMPORT_STARTED_SUBJ : CoreEventsManager.PLAYBACK_IMPORT_STOPPED_SUBJ;
			shortMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_IMPORT_STARTED_SHORT_MSG : CoreEventsManager.PLAYBACK_IMPORT_STOPPED_SHORT_MSG;
			med = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_IMPORT_STARTED_MED_MSG : CoreEventsManager.PLAYBACK_IMPORT_STOPPED_MED_MSG;
			longMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_IMPORT_STARTED_LONG_MSG : CoreEventsManager.PLAYBACK_IMPORT_STOPPED_LONG_MSG;
		} else if(mf.IsMusicFile()) {
			type = "MUSIC";
			name = (action == EventType.STARTS ? "Music Playback Started: " : "Music Playback Stopped: ") + clnt.getAlias();
			desc = "Event fired when client '" + clnt.getAlias() + "' " + (action == EventType.STARTS ? "starts to play back" : "stops playing back") + " a music file.";
			subj = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_MUSIC_STARTED_SUBJ : CoreEventsManager.PLAYBACK_MUSIC_STOPPED_SUBJ;
			shortMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_MUSIC_STARTED_SHORT_MSG : CoreEventsManager.PLAYBACK_MUSIC_STOPPED_SHORT_MSG;
			med = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_MUSIC_STARTED_MED_MSG : CoreEventsManager.PLAYBACK_MUSIC_STOPPED_MED_MSG;
			longMsg = action == EventType.STARTS ? CoreEventsManager.PLAYBACK_MUSIC_STARTED_LONG_MSG : CoreEventsManager.PLAYBACK_MUSIC_STOPPED_LONG_MSG;
		} else
			return null; // Ignore pictures
		String eventId = action + "_" + type + "_" + clnt.getId();
		LOG.info("Returning eventId: " + eventId);
		DataStore ds = DataStore.getInstance();
		return new SageAlertEventMetadata(eventId, name, desc, Arrays.asList(PlaybackEvent.ARG_TYPES), ds.getSetting(eventId + SageAlertEventMetadata.SUBJ_SUFFIX, subj), ds.getSetting(eventId + SageAlertEventMetadata.SHORT_SUFFIX, shortMsg), ds.getSetting(eventId + SageAlertEventMetadata.MED_SUFFIX, med), ds.getSetting(eventId + SageAlertEventMetadata.LONG_SUFFIX, longMsg));
	}
	
	private PlaybackMetadataFactory() {}
}
