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
 * A SageEvent that signifies the available recording space is below the user defined threshold
 * @author dbattams
 * @version $Id$
 */
final class LowSpaceEvent implements SageEvent {
	/**
	 * The metadata for this event
	 */
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(LowSpaceEvent.class.getCanonicalName(), "Low Space Alert", "Alert when recording space is low (set threshold on Settings tab).");
	
	private RecordingDevice device;
	
	/**
	 * Constructor
	 * @param device The abstract RecordingDevice object that caused this event
	 */
	LowSpaceEvent(RecordingDevice device) {
		this.device = device;
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
		return "Less than " + device.getMinSpace() + "GBs of recording space available (" + device.getFreeSpace() + " GBs free).";
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
	public RecordingDevice getSource() {
		return device;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return "Recording space is low...";
	}

}
