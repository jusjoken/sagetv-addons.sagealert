/*
 *      Copyright 2010-2011 Battams, Derek
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
package com.google.code.sagetvaddons.sagealert.client;

import java.util.Arrays;

import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.Client.EventType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbattams
 *
 */
class ClientListenerSubscriptionForm extends ListenerSubscriptionForm {
	
	static enum MediaType {
		TV,
		IMPORT,
		DVD,
		MUSIC
	}
	
	static private final String ERR_VALUE = "<ERROR: Try refreshing browser>";
	static private final String[] ARG_TYPES = new String[] {"gkusnick.sagetv.api.MediaFileAPI$MediaFile", "gkusnick.sagetv.api.AiringAPI$Airing", "gkusnick.sagetv.api.ShowAPI$Show", "com.google.code.sagetvaddons.sagealert.shared.Client"};
	
	// If you change any of these defaults below you must also update them in CoreEventsManager
	
	static private final String TV_STOPS_SUBJ = "TV playback has stopped";
	static private final String TV_STOPS_LONG = "TV playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has stopped on client '$3.getAlias()'";
	static private final String TV_STOPS_MED = "TV playback stopped ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String TV_STOPS_SHORT = "TV playback stopped ($3.getAlias()): $0.GetMediaTitle()";
	
	static private final String TV_STARTS_SUBJ = "TV playback has started";
	static private final String TV_STARTS_LONG = "TV playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has started on client '$3.getAlias()'";
	static private final String TV_STARTS_MED = "TV playback started ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String TV_STARTS_SHORT = "TV playback started ($3.getAlias()): $0.GetMediaTitle()";

	static private final String TV_PAUSED_SUBJ = "TV playback paused";
	static private final String TV_PAUSED_LONG = "TV playback of '$0.GetMediaTitle()' has been paused on client '$3.getAlias()'";
	static private final String TV_PAUSED_MED = "TV playback paused ($3.getAlias()): $0.GetMediaTitle()";
	static private final String TV_PAUSED_SHORT = TV_PAUSED_MED;

	static private final String TV_RESUMED_SUBJ = "TV playback resumed";
	static private final String TV_RESUMED_LONG = "TV playback of '$0.GetMediaTitle()' has resumed on client '$3.getAlias()'";
	static private final String TV_RESUMED_MED = "TV playback resumed ($3.getAlias()): $0.GetMediaTitle()";
	static private final String TV_RESUMED_SHORT = TV_RESUMED_MED;

	static private final String IMPORT_STOPS_SUBJ = "Import playback has stopped";
	static private final String IMPORT_STOPS_LONG = "Import playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has stopped on client '$3.getAlias()'";
	static private final String IMPORT_STOPS_MED = "Import playback stopped ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String IMPORT_STOPS_SHORT = "Import playback stopped ($3.getAlias()): $0.GetMediaTitle()";
	
	static private final String IMPORT_STARTS_SUBJ = "Import playback has started";
	static private final String IMPORT_STARTS_LONG = "Import playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has started on client '$3.getAlias()'";
	static private final String IMPORT_STARTS_MED = "Import playback started ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String IMPORT_STARTS_SHORT = "Import playback started ($3.getAlias()): $0.GetMediaTitle()";

	static final private String IMPORT_PAUSED_SUBJ = "Import playback paused";
	static final private String IMPORT_PAUSED_LONG = "Import playback of '$0.GetMediaTitle()' has been paused on client '$3.getAlias()'";
	static final private String IMPORT_PAUSED_MED = "Import playback paused ($3.getAlias()): $0.GetMediaTitle()";
	static final private String IMPORT_PAUSED_SHORT = IMPORT_PAUSED_MED;

	static final private String IMPORT_RESUMED_SUBJ = "Import playback resumed";
	static final private String IMPORT_RESUMED_LONG = "Import playback of '$0.GetMediaTitle()' has resumed on client '$3.getAlias()'";
	static final private String IMPORT_RESUMED_MED = "Import playback resumed ($3.getAlias()): $0.GetMediaTitle()";
	static final private String IMPORT_RESUMED_SHORT = IMPORT_RESUMED_MED;

	static private final String DVD_STOPS_SUBJ = "DVD/BluRay playback has stopped";
	static private final String DVD_STOPS_LONG = "DVD/BluRay playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has stopped on client '$3.getAlias()'";
	static private final String DVD_STOPS_MED = "DVD/BluRay playback stopped ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String DVD_STOPS_SHORT = "DVD/BluRay playback stopped ($3.getAlias()): $0.GetMediaTitle()";
	
	static private final String DVD_STARTS_SUBJ = "DVD/BluRay playback has started";
	static private final String DVD_STARTS_LONG = "DVD/BluRay playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has started on client '$3.getAlias()'";
	static private final String DVD_STARTS_MED = "DVD/BluRay playback started ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String DVD_STARTS_SHORT = "DVD/BluRay playback started ($3.getAlias()): $0.GetMediaTitle()";

	static final private String DVD_PAUSED_SUBJ = "DVD/BluRay playback paused";
	static final private String DVD_PAUSED_LONG = "DVD/BluRay playback of '$0.GetMediaTitle()' has been paused on client '$3.getAlias()'";
	static final private String DVD_PAUSED_MED = "DVD/BluRay playback paused ($3.getAlias()): $0.GetMediaTitle()";
	static final private String DVD_PAUSED_SHORT = DVD_PAUSED_MED;

	static final private String DVD_RESUMED_SUBJ = "DVD/BluRay playback resumed";
	static final private String DVD_RESUMED_LONG = "DVD/BluRay playback of '$0.GetMediaTitle()' has resumed on client '$3.getAlias()'";
	static final private String DVD_RESUMED_MED = "DVD/BluRay playback resumed ($3.getAlias()): $0.GetMediaTitle()";
	static final private String DVD_RESUMED_SHORT = DVD_RESUMED_MED;

	static private final String MUSIC_STOPS_SUBJ = "Music playback has stopped";
	static private final String MUSIC_STOPS_LONG = "Music playback of '$0.GetMediaTitle()'$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow()) has started on client '$3.getAlias()'";
	static private final String MUSIC_STOPS_MED = "Music playback stopped ($3.getAlias()): $0.GetMediaTitle()$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow())";
	static private final String MUSIC_STOPS_SHORT = "Music playback stopped ($3.getAlias()): $0.GetMediaTitle()$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow())";
	
	static private final String MUSIC_STARTS_SUBJ = "Music playback has started";
	static private final String MUSIC_STARTS_LONG = "Music playback of '$0.GetMediaTitle()'$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow()) has started on client '$3.getAlias()'";
	static private final String MUSIC_STARTS_MED = "Music playback started ($3.getAlias()): $0.GetMediaTitle()$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow())";
	static private final String MUSIC_STARTS_SHORT = "Music playback started ($3.getAlias()): $0.GetMediaTitle()$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow())";

	static final private String MUSIC_PAUSED_SUBJ = "Music playback paused";
	static final private String MUSIC_PAUSED_LONG = "Music playback of '$0.GetMediaTitle()'$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow()) has been paused on client '$3.getAlias()'";
	static final private String MUSIC_PAUSED_MED = "Music playback paused ($3.getAlias()): $0.GetMediaTitle()$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow())";
	static final private String MUSIC_PAUSED_SHORT = MUSIC_PAUSED_MED;

	static final private String MUSIC_RESUMED_SUBJ = "Music playback resumed";
	static final private String MUSIC_RESUMED_LONG = "Music playback of '$0.GetMediaTitle()'$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow()) has resumed on client '$3.getAlias()'";
	static final private String MUSIC_RESUMED_MED = "Music playback resumed ($3.getAlias()): $0.GetMediaTitle()$utils.concatIfNotEmpty(\" by \", $2.GetPeopleInShow())";
	static final private String MUSIC_RESUMED_SHORT = MUSIC_RESUMED_MED;

	private boolean[] isBuilt;
	
	ClientListenerSubscriptionForm(Client clnt, EventType eventType, MediaType mediaType) {
		super(new SageAlertEventMetadata(eventType.toString() + "_" + mediaType.toString() + "_" + clnt.getId(), "Client Media Event (" + eventType.toString() + ")", "Fires when client '" + clnt.getAlias() + "' " + eventType.toString().toLowerCase() + " playing back media.", Arrays.asList(ARG_TYPES), "", "", "", ""));

		isBuilt = new boolean[4];
		String eventId = eventType.toString() + "_" + mediaType.toString() + "_" + clnt.getId();
		String defaultSubj, defaultLong, defaultMed, defaultShort;
		if(mediaType == MediaType.TV) {
			if(eventType == EventType.STARTS) {
				defaultSubj = TV_STARTS_SUBJ;
				defaultLong = TV_STARTS_LONG;
				defaultMed = TV_STARTS_MED;
				defaultShort = TV_STARTS_SHORT;
			} else if(eventType == EventType.STOPS) {
				defaultSubj = TV_STOPS_SUBJ;
				defaultLong = TV_STOPS_LONG;
				defaultMed = TV_STOPS_MED;
				defaultShort = TV_STOPS_SHORT;
			} else if(eventType == EventType.PAUSES) {
				defaultSubj = TV_PAUSED_SUBJ;
				defaultLong = TV_PAUSED_LONG;
				defaultMed = TV_PAUSED_MED;
				defaultShort = TV_PAUSED_SHORT;				
			} else {
				defaultSubj = TV_RESUMED_SUBJ;
				defaultLong = TV_RESUMED_LONG;
				defaultMed = TV_RESUMED_MED;
				defaultShort = TV_RESUMED_SHORT;
			}
		} else if(mediaType == MediaType.IMPORT) {
			if(eventType == EventType.STARTS) {
				defaultSubj = IMPORT_STARTS_SUBJ;
				defaultLong = IMPORT_STARTS_LONG;
				defaultMed = IMPORT_STARTS_MED;
				defaultShort = IMPORT_STARTS_SHORT;
			} else if(eventType == EventType.STOPS) {
				defaultSubj = IMPORT_STOPS_SUBJ;
				defaultLong = IMPORT_STOPS_LONG;
				defaultMed = IMPORT_STOPS_MED;
				defaultShort = IMPORT_STOPS_SHORT;
			} else if(eventType == EventType.PAUSES) {
				defaultSubj = IMPORT_PAUSED_SUBJ;
				defaultLong = IMPORT_PAUSED_LONG;
				defaultMed = IMPORT_PAUSED_MED;
				defaultShort = IMPORT_PAUSED_SHORT;				
			} else {
				defaultSubj = IMPORT_RESUMED_SUBJ;
				defaultLong = IMPORT_RESUMED_LONG;
				defaultMed = IMPORT_RESUMED_MED;
				defaultShort = IMPORT_RESUMED_SHORT;
			}
		} else if(mediaType == MediaType.DVD) {
			if(eventType == EventType.STARTS) {
				defaultSubj = DVD_STARTS_SUBJ;
				defaultLong = DVD_STARTS_LONG;
				defaultMed = DVD_STARTS_MED;
				defaultShort = DVD_STARTS_SHORT;
			} else if(eventType == EventType.STOPS) {
				defaultSubj = DVD_STOPS_SUBJ;
				defaultLong = DVD_STOPS_LONG;
				defaultMed = DVD_STOPS_MED;
				defaultShort = DVD_STOPS_SHORT;
			} else if(eventType == EventType.PAUSES) {
				defaultSubj = DVD_PAUSED_SUBJ;
				defaultLong = DVD_PAUSED_LONG;
				defaultMed = DVD_PAUSED_MED;
				defaultShort = DVD_PAUSED_SHORT;
			} else {
				defaultSubj = DVD_RESUMED_SUBJ;
				defaultLong = DVD_RESUMED_LONG;
				defaultMed = DVD_RESUMED_MED;
				defaultShort = DVD_RESUMED_SHORT;
			}
		} else {
			if(eventType == EventType.STARTS) {
				defaultSubj = MUSIC_STARTS_SUBJ;
				defaultLong = MUSIC_STARTS_LONG;
				defaultMed = MUSIC_STARTS_MED;
				defaultShort = MUSIC_STARTS_SHORT;
			} else if(eventType == EventType.STOPS) {
				defaultSubj = MUSIC_STOPS_SUBJ;
				defaultLong = MUSIC_STOPS_LONG;
				defaultMed = MUSIC_STOPS_MED;
				defaultShort = MUSIC_STOPS_SHORT;
			} else if(eventType == EventType.PAUSES) {
				defaultSubj = MUSIC_PAUSED_SUBJ;
				defaultLong = MUSIC_PAUSED_LONG;
				defaultMed = MUSIC_PAUSED_MED;
				defaultShort = MUSIC_PAUSED_SHORT;				
			} else {
				defaultSubj = MUSIC_RESUMED_SUBJ;
				defaultLong = MUSIC_RESUMED_LONG;
				defaultMed = MUSIC_RESUMED_MED;
				defaultShort = MUSIC_RESUMED_SHORT;		
			}
		}
		SettingsServiceAsync rpc = GWT.create(SettingsService.class);
		
		rpc.getSetting(eventId + SageAlertEventMetadata.SUBJ_SUFFIX, defaultSubj, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setSubject(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setSubject(result);
				isBuilt[0] = true;
			}
			
		});
		
		rpc.getSetting(eventId + SageAlertEventMetadata.SHORT_SUFFIX, defaultShort, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setShortMsg(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setShortMsg(result);
				isBuilt[1] = true;
			}
			
		});

		rpc.getSetting(eventId + SageAlertEventMetadata.MED_SUFFIX, defaultMed, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setMedMsg(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setMedMsg(result);
				isBuilt[2] = true;
			}
			
		});

		rpc.getSetting(eventId + SageAlertEventMetadata.LONG_SUFFIX, defaultLong, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setLongMsg(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setLongMsg(result);
				isBuilt[3] = true;
			}
			
		});
	}
	
	public boolean isBuilt() {
		for(boolean b : isBuilt)
			if(!b) return false;
		return super.isBuilt();
	}
}
