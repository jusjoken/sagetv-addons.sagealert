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

import gkusnick.sagetv.api.API;

import com.google.code.sagetvaddons.sagealert.server.CoreEventsManager;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
public final class ConflictStatusEvent implements SageAlertEvent {

	private int totalConflicts;
	private int unresolvedConflicts;
	
	public ConflictStatusEvent() {
		totalConflicts = API.apiNullUI.global.GetAiringsThatWontBeRecorded(false).size();
		unresolvedConflicts = API.apiNullUI.global.GetAiringsThatWontBeRecorded(true).size();
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getLongDescription()
	 */
	public String getLongDescription() {
		StringBuilder msg = new StringBuilder("The conflict status of your SageTV recording schedule has changed.  There are now " + totalConflicts + " conflict(s)");
		if(unresolvedConflicts != totalConflicts && unresolvedConflicts > 0)
			msg.append("; " + unresolvedConflicts + " are unresolved.");
		else if(unresolvedConflicts == totalConflicts && totalConflicts > 0)
			msg.append("; all of them are unresolved.");
		else
			msg.append(".");
		return msg.toString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getMediumDescription()
	 */
	public String getMediumDescription() {
		return "Conflict status change: " + totalConflicts + " total conflicts; " + unresolvedConflicts + " unresolved.";
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
		return CoreEventsManager.CONFLICTS;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent#getSubject()
	 */
	public String getSubject() {
		return "Conflict status changed in recording schedule";
	}

}
