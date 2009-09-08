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
 * Base implementation for all events
 * @author dbattams
 * @version $Id$
 */
public interface SageEvent {
	/**
	 * A subject suitable for a message about this event; should be relatively short
	 * @return The event's subject text
	 */
	public String getSubject();
	
	/**
	 * A short description of the event; should be less than 140 characters (suitable for SMS transmission)
	 * @return The event's short description
	 */
	public String getShortDescription();
	
	/**
	 * A medium description of the event; should also be relatively short, but not necessarily less than 140 characters (suitable for OS notifications)
	 * @return The event's medium description
	 */
	public String getMediumDescription();
	
	/**
	 * A longer description of the event; no length restrictions (suitable for email notification)
	 * @return
	 */
	public String getLongDescription();
	
	/**
	 * The cause of this event being triggered
	 * @return The event's source/cause
	 */
	public Object getSource();
	
	/**
	 * The metadata description of this event type
	 * @return The metadata for the event type
	 */
	public SageEventMetaData getEventMetaData();
}
