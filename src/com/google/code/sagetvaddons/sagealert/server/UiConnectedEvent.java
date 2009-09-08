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
 * A SageEvent that signifies when a new UI has connected to the SageTV server
 * @author dbattams
 * @version $Id$
 */
final class UiConnectedEvent implements SageEvent {

	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(UiConnectedEvent.class.getCanonicalName(), "Alert when a UI (client, extender, placeshifter) connects to the SageTV server.");
	
	private String uiContextName;
	
	/**
	 * ctor
	 * @param uiContextName The name of the UI context that triggered this event; the source of the event
	 */
	UiConnectedEvent(String uiContextName) {
		this.uiContextName = uiContextName;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		return "A new client has connected to the server. [" + uiContextName + "]";
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
		return uiContextName;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return "New client connected...";
	}

	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}
}
