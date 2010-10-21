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

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.AllPluginsLoadedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.AppStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.ConflictStatusEvent;
import com.google.code.sagetvaddons.sagealert.server.events.EpgUpdatedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.ImportCompletedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.ImportStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.RecSchedChangedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.UnresolvedConflictStatusEvent;

/**
 * @author dbattams
 *
 */
final class AppEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(AppEventsListener.class);
	static private final AppEventsListener INSTANCE = new AppEventsListener();
	static final AppEventsListener get() { return INSTANCE; }

	private AppEventsListener() {}
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0 + " :: " + arg1.toString());
		if(AppStartedEvent.EVENT_ID.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new AppStartedEvent(SageAlertEventMetadataManager.get().getMetadata(AppStartedEvent.EVENT_ID)));
		else if(CoreEventsManager.EPG_UPDATED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new EpgUpdatedEvent(SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.EPG_UPDATED)));
		else if(CoreEventsManager.CONFLICTS.equals(arg0)) {
			int unresolved = API.apiNullUI.global.GetAiringsThatWontBeRecorded(true).size();
			if(unresolved > 0)
				SageAlertEventHandlerManager.get().fire(new UnresolvedConflictStatusEvent(SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.UNRESOLVED_CONFLICTS)));
			SageAlertEventHandlerManager.get().fire(new ConflictStatusEvent(SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.CONFLICTS)));
		} else if(CoreEventsManager.REC_SCHED_CHANGED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new RecSchedChangedEvent(SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.REC_SCHED_CHANGED)));
		else if(CoreEventsManager.PLUGINS_LOADED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new AllPluginsLoadedEvent(SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.PLUGINS_LOADED)));
		else if(CoreEventsManager.IMPORT_STARTED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new ImportStartedEvent(SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.IMPORT_STARTED)));
		else if(CoreEventsManager.IMPORT_COMPLETED.equals(arg0))
			SageAlertEventHandlerManager.get().fire(new ImportCompletedEvent((Boolean)arg1.get("FullReindex"), SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.IMPORT_COMPLETED)));
		else
			LOG.error("Unhandled event: " + arg0);
	}
}
