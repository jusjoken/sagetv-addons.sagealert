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

import com.google.code.sagetvaddons.sagealert.client.Client;
import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;


/**
 * A SageEvent that signifies a UI has disconnected from the SageTV server
 * @author dbattams
 * @version $Id$
 */
final class UiDisconnectedEvent implements SageEvent {

	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(UiDisconnectedEvent.class.getCanonicalName(), "Alert when a UI (client, extender, placeshifter) disconnects from the SageTV server.");
	
	private Client source;
	
	/**
	 * ctor 
	 * @param uiContextName
	 */
	UiDisconnectedEvent(Client c) {
		source = c;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		return "Client '" + source.getAlias() + "' has disconnected from the SageTV server.";
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
	public Client getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return "Client disconnected...";
	}

	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}
}
