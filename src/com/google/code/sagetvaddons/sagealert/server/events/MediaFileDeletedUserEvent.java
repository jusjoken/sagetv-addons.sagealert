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
package com.google.code.sagetvaddons.sagealert.server.events;

import gkusnick.sagetv.api.MediaFileAPI.MediaFile;

import org.apache.commons.lang.ArrayUtils;

import com.google.code.sagetvaddons.sagealert.server.ApiInterpreter;
import com.google.code.sagetvaddons.sagealert.server.CoreEventsManager;
import com.google.code.sagetvaddons.sagealert.server.DataStore;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public final class MediaFileDeletedUserEvent extends MediaFileDeletedEvent {
	static public final String[] ARG_TYPES = (String[])ArrayUtils.addAll(MediaFileDeletedEvent.ARG_TYPES, new String[] {Client.class.getName()});

	private Client clnt;
	private Object[] eventArgs;
	private SageAlertEventMetadata metadata;
	
	/**
	 * @param mf
	 */
	public MediaFileDeletedUserEvent(MediaFile mf, String deletedBy, SageAlertEventMetadata data) {
		super(mf, data);
		if(deletedBy == null || deletedBy.length() == 0)
			deletedBy = "<unknown>";
		clnt = DataStore.getInstance().getClient(deletedBy);
		eventArgs = ArrayUtils.addAll(super.getArgs(), new Object[] {clnt});
		metadata = data;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		return new ApiInterpreter(eventArgs, metadata.getLongMsg()).interpret();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return new ApiInterpreter(eventArgs, metadata.getMedMsg()).interpret();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getShortDescription()
	 */
	public String getShortDescription() {
		return new ApiInterpreter(eventArgs, metadata.getShortMsg()).interpret();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSource()
	 */
	public String getSource() {
		return CoreEventsManager.MEDIA_DELETED_USER;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSubject()
	 */
	public String getSubject() {
		return new ApiInterpreter(eventArgs, metadata.getSubject()).interpret();
	}
}
