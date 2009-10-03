/*
 *      Copyright 2009 Battams, Derek
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

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

/**
 * The RemoteEvent is an event that can be fired by remote clients via RPC calls
 * @author dbattams
 * @version $Id$
 */
final class RemoteEvent implements SageEvent {
	/**
	 * The metadata for this event.
	 */
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(RemoteEvent.class.getCanonicalName(), "Remote Events", "Remote events are events triggered via the SageAlert XML-RPC API.");
	
	private String title;
	private String desc;
	private String source;
	
	RemoteEvent(String title, String desc, String source) {
		this.title = title;
		this.desc = desc;
		this.source = source;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getEventMetaData()
	 */
	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getMediumDescription()
	 */
	@Override
	public String getMediumDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	@Override
	public String getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return title;
	}
}
