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
	
	static private final String ERR_VALUE = "<ERROR: Try refreshing browser>";
	static private final String[] ARG_TYPES = new String[] {"gkusnick.sagetv.api.MediaFileAPI$MediaFile", "gkusnick.sagetv.api.AiringAPI$Airing", "gkusnick.sagetv.api.ShowAPI$Show", "com.google.code.sagetvaddons.sagealert.shared.Client"};
	
	// If you change any of these defaults below you must also update them in CoreEventsManager
	
	static private final String STOPS_SUBJ = "Media playback has stopped";
	static private final String STOPS_LONG = "Media playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has stopped on client '$3.getAlias()'";
	static private final String STOPS_MED = "Media playback stopped ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String STOPS_SHORT = "Media playback stopped ($3.getAlias()): $0.GetMediaTitle()";
	
	static private final String STARTS_SUBJ = "Media playback has started";
	static private final String STARTS_LONG = "Media playback of '$0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())' has started on client '$3.getAlias()'";
	static private final String STARTS_MED = "Media playback started ($3.getAlias()): $0.GetMediaTitle() $utils.concatIfNotEmpty(\": \", $2.GetShowEpisode())";
	static private final String STARTS_SHORT = "Media playback started ($3.getAlias()): $0.GetMediaTitle()";
	
	ClientListenerSubscriptionForm(Client clnt, EventType type) {
		super(new SageAlertEventMetadata(type.toString() + "_" + clnt.getId(), "Client Media Event (" + type.toString() + ")", "Fires when client '" + clnt.getAlias() + "' " + type.toString().toLowerCase() + " playing back media.", Arrays.asList(ARG_TYPES), "", "", "", ""));

		String eventId = type.toString() + "_" + clnt.getId();
		String defaultSubj, defaultLong, defaultMed, defaultShort;
		if(type == EventType.STARTS) {
			defaultSubj = STARTS_SUBJ;
			defaultLong = STARTS_LONG;
			defaultMed = STARTS_MED;
			defaultShort = STARTS_SHORT;
		} else {
			defaultSubj = STOPS_SUBJ;
			defaultLong = STOPS_LONG;
			defaultMed = STOPS_MED;
			defaultShort = STOPS_SHORT;
		}
		SettingsServiceAsync rpc = GWT.create(SettingsService.class);
		
		rpc.getSetting(eventId + SageAlertEventMetadata.SUBJ_SUFFIX, defaultSubj, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setSubject(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setSubject(result);
			}
			
		});
		
		rpc.getSetting(eventId + SageAlertEventMetadata.SHORT_SUFFIX, defaultShort, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setShortMsg(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setShortMsg(result);
			}
			
		});

		rpc.getSetting(eventId + SageAlertEventMetadata.MED_SUFFIX, defaultMed, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setMedMsg(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setMedMsg(result);
			}
			
		});

		rpc.getSetting(eventId + SageAlertEventMetadata.LONG_SUFFIX, defaultLong, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				setLongMsg(ERR_VALUE);
			}

			public void onSuccess(String result) {
				setLongMsg(result);
			}
			
		});
	}
}
