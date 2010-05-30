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

import gkusnick.sagetv.api.SystemMessageAPI;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
public abstract class SystemMessageEvent implements SageAlertEvent {

	private SystemMessageAPI.SystemMessage msg;
	private String subject;
	
	public SystemMessageEvent(SystemMessageAPI.SystemMessage msg) {
		this.msg = msg;
		StringBuilder subj = new StringBuilder("New ");
		if(msg.GetSystemMessageLevel() == 1)
			subj.append("INFO ");
		else if(msg.GetSystemMessageLevel() == 2)
			subj.append("WARNING ");
		else if(msg.GetSystemMessageLevel() == 3)
			subj.append("ERROR ");
		subj.append("system message generated");
		subject = subj.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		return msg.GetSystemMessageString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return "New system message generated: " + msg.GetSystemMessageTypeName() + "; see SageTV server for details.";
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
	abstract public String getSource();

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSubject()
	 */
	public String getSubject() {
		return subject;
	}

}
