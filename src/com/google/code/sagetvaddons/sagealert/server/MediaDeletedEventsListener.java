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
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0);
		MediaFileAPI.MediaFile mf = API.apiNullUI.mediaFileAPI.Wrap(arg1.get("MediaFile"));
		String reason = (String)arg1.get("Reason");
		String deletedBy = (String)arg1.get("UIContext");
		if(reason == null || reason.length() == 0) {
			LOG.warn("Handling of this event requires SageTV 7.0.10 or newer; event ignored.");
			return;
		}

		if(reason.equals(CoreEventsManager.MEDIA_DELETED_KEEP_AT_MOST))
			SageEventHandlerManager.get().fire(new MediaFileDeletedKeepAtMostEvent(mf));
		else if(reason.equals(CoreEventsManager.MEDIA_DELETED_LOW_SPACE))
			SageEventHandlerManager.get().fire(new MediaFileDeletedLowSpaceEvent(mf));
		else if(reason.equals(CoreEventsManager.MEDIA_DELETED_PARTIAL_OR_UNWANTED))
			SageEventHandlerManager.get().fire(new MediaFileDeletedPartialOrUnwantedEvent(mf));
		else if(reason.equals(CoreEventsManager.MEDIA_DELETED_USER))
			SageEventHandlerManager.get().fire(new MediaFileDeletedUserEvent(mf, deletedBy));
		else if(reason.equals(CoreEventsManager.MEDIA_DELETED_VERIFY_FAILED))
			SageEventHandlerManager.get().fire(new MediaFileDeletedVerifyFailedEvent(mf));
		else if(reason.equals(CoreEventsManager.MEDIA_DELETED_IMPORT_LOST))
			SageEventHandlerManager.get().fire(new MediaFileDeletedImportLost(mf));
		else
			LOG.error("Unknown reason received, event ignored! [" + reason + "]");
	}

	private MediaDeletedEventsListener() {}
}
