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

import com.google.code.sagetvaddons.sagealert.server.CoreEventsManager;
import com.google.code.sagetvaddons.sagealert.server.DataStore;

import gkusnick.sagetv.api.MediaFileAPI.MediaFile;

/**
 * @author dbattams
 *
 */
public final class MediaFileDeletedUserEvent extends MediaFileDeletedEvent {

	private String alias;
	
	/**
	 * @param mf
	 */
	public MediaFileDeletedUserEvent(MediaFile mf, String deletedBy) {
		super(mf);
		if(deletedBy != null && deletedBy.length() > 0)
			alias = DataStore.getInstance().getClient(deletedBy).getAlias();
		else
			alias = null;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		StringBuilder msg = new StringBuilder("The following media file was deleted by ");
		if(alias != null)
			msg.append("'" + alias + "'");
		else
			msg.append("the user");
		msg.append(": " + getTitle());
		return msg.toString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		StringBuilder msg = new StringBuilder("File deleted by ");
		if(alias != null)
			msg.append("'" + alias + "'");
		else
			msg.append("user");
		msg.append(": " + getTitle());
		return msg.toString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getShortDescription()
	 */
	public String getShortDescription() {
		return getMediumDescription();
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
		StringBuilder msg = new StringBuilder("Media file deleted by user");
		if(alias != null)
			msg.append(" '" + alias + "'");
		return msg.toString();
	}
}
