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

import com.google.code.sagetvaddons.sagealert.server.events.MediaFileImportedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.RecSegmentAddedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.RecordingCompletedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.RecordingStartedEvent;
import com.google.code.sagetvaddons.sagealert.server.events.RecordingStoppedEvent;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;


/**
 * A SageTVEventListener that will listen for and handle events related to media files
 * @author Derek Battams &lt;derek AT battams DOT ca&gt;
 * @version $Id$
 */
final class MediaFileEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(MediaFileEventsListener.class);
	static private final MediaFileEventsListener INSTANCE = new MediaFileEventsListener();
	static final MediaFileEventsListener get() { return INSTANCE; }

	static private final String MF_KEY = "MediaFile";
	
	private MediaFileEventsListener() {}

	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Received event: " + arg0 + " :: " + arg1.toString());
		MediaFileAPI.MediaFile mf = null;
		Object obj = arg1.get(MF_KEY);
		if(Utils.canWrapAsMediaFile(obj)) {
			mf = API.apiNullUI.mediaFileAPI.Wrap(obj);
			if(mf != null) {
				SageAlertEvent e = null;
				if(CoreEventsManager.REC_STARTED.equals(arg0))
					e = new RecordingStartedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.REC_STARTED));
				else if(CoreEventsManager.REC_COMPLETED.equals(arg0))
					e = new RecordingCompletedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.REC_COMPLETED));
				else if(CoreEventsManager.REC_STOPPED.equals(arg0))
					e = new RecordingStoppedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.REC_STOPPED));
				else if(CoreEventsManager.MEDIA_FILE_IMPORTED.equals(arg0))
					e = new MediaFileImportedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.MEDIA_FILE_IMPORTED));
				else if(CoreEventsManager.REC_SEG_ADDED.equals(arg0))
					e = new RecSegmentAddedEvent(mf, SageAlertEventMetadataManager.get().getMetadata(CoreEventsManager.REC_SEG_ADDED));
				if(e != null)
					SageAlertEventHandlerManager.get().fire(e);
				else
					LOG.error("Unhandled event: " + arg0);
			} else
				LOG.error("MediaFile is unexpectedly null!  Event ignored.");
		} else
			LOG.error("MediaFile argument is invalid!  Event ignored.");
	}		
}
