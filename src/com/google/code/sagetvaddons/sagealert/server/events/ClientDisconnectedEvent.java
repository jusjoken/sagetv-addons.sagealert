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
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
public final class ClientDisconnectedEvent implements SageAlertEvent {

	private Client clnt;
	
	public ClientDisconnectedEvent(Client c) {
		clnt = c;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		return "Client '" + clnt.getAlias() + "' has disconnected from the SageTV server.";
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getShortDescription()
	 */
	public String getShortDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSource()
	 */
	public String getSource() {
		return CoreEventsManager.CLIENT_DISCONNECTED;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSubject()
	 */
	public String getSubject() {
		return "Client disconnected from SageTV server";
	}
}
