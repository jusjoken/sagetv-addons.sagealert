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

import java.util.Arrays;
import java.util.List;

import com.google.code.sagetvaddons.sagealert.server.ApiInterpreter;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public abstract class ClientConnectionEvent implements SageAlertEvent {

	static public final List<String> EVENT_ARG_TYPES = Arrays.asList(new String[] {Client.class.getName()});

	private Client clnt;
	private SageAlertEventMetadata metadata;

	public ClientConnectionEvent(Client c, SageAlertEventMetadata data) {
		clnt = c;
		metadata = data;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		return new ApiInterpreter(new Object[] {clnt}, metadata.getLongMsg()).interpret();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return new ApiInterpreter(new Object[] {clnt}, metadata.getMedMsg()).interpret();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getShortDescription()
	 */
	public String getShortDescription() {
		return new ApiInterpreter(new Object[] {clnt}, metadata.getShortMsg()).interpret();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSource()
	 */
	abstract public String getSource();

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSubject()
	 */
	public String getSubject() {
		return new ApiInterpreter(new Object[] {clnt}, metadata.getSubject()).interpret();
	}
}
