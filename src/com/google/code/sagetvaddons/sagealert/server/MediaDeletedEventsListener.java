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
import gkusnick.sagetv.api.MediaFileAPI;

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedImportLost;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedKeepAtMostEvent;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedLowSpaceEvent;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedPartialOrUnwantedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedUserEvent;
import com.google.code.sagetvaddons.sagealert.server.events.MediaFileDeletedVerifyFailedEvent;


/**
 * @author dbattams
 *
 */
final class MediaDeletedEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(MediaDeletedEventsListener.class);
	static private final MediaDeletedEventsListener INSTANCE = new MediaDeletedEventsListener();
	static final MediaDeletedEventsListener get() { return INSTANCE; }
	
	static private final String MF_KEY = "MediaFile";
	static private final String REASON_KEY = "Reason";
	static private final String UI_KEY = "UIContext";
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0 + " :: " + arg1.toString());
		MediaFileAPI.MediaFile mf = null;
		Object obj = arg1.get(MF_KEY);
		if(Utils.canWrapAsMediaFile(obj)) {
			mf = API.apiNullUI.mediaFileAPI.Wrap(obj);
			String reason = (String)arg1.get(REASON_KEY);
			String deletedBy = (String)arg1.get(UI_KEY);
			if(reason == null || reason.length() == 0) {
				LOG.warn("Handling of this event requires SageTV 7.0.10 or newer; event ignored.");
				return;
			}
			if(reason.equals(CoreEventsManager.MEDIA_DELETED_KEEP_AT_MOST))
				SageAlertEventHandlerManager.get().fire(new MediaFileDeletedKeepAtMostEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_DELETED_KEEP_AT_MOST)));
			else if(reason.equals(CoreEventsManager.MEDIA_DELETED_LOW_SPACE))
				SageAlertEventHandlerManager.get().fire(new MediaFileDeletedLowSpaceEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_DELETED_LOW_SPACE)));
			else if(reason.equals(CoreEventsManager.MEDIA_DELETED_PARTIAL_OR_UNWANTED))
				SageAlertEventHandlerManager.get().fire(new MediaFileDeletedPartialOrUnwantedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_DELETED_PARTIAL_OR_UNWANTED)));
			else if(reason.equals(CoreEventsManager.MEDIA_DELETED_USER))
				SageAlertEventHandlerManager.get().fire(new MediaFileDeletedUserEvent(mf, deletedBy, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_DELETED_USER)));
			else if(reason.equals(CoreEventsManager.MEDIA_DELETED_VERIFY_FAILED))
				SageAlertEventHandlerManager.get().fire(new MediaFileDeletedVerifyFailedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_DELETED_VERIFY_FAILED)));
			else if(reason.equals(CoreEventsManager.MEDIA_DELETED_IMPORT_LOST))
				SageAlertEventHandlerManager.get().fire(new MediaFileDeletedImportLost(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_DELETED_IMPORT_LOST)));
			else
				LOG.error("Unknown reason received, event ignored! [" + reason + "]");
		} else
			LOG.error("Invalid contents in args map!  Event ignored.");
	}

	private MediaDeletedEventsListener() {}
}
