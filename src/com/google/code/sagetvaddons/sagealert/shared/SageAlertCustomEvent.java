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
package com.google.code.sagetvaddons.sagealert.shared;

/**
 * <p>Plugin developers who wish to provide custom events for SageAlert must implement this interface when writing new event classes</p>
 * <p>The methods defined in this interface are used by the CustomEventsManager to update the messages for an event at runtime via API replacement.  No one else should be calling these methods!</p>
 * <p>When implementing this interface, you basically should have instance variables for each of subject, medium, short, and long and then your getters (defined in the super interface) will return the instance
 * variables and the setters defined here simply update the instance vars. Look at the CustomEvents plugin for an example of how this is implemented.</p>
 * @author dbattams
 * @version $Id$
 */
public interface SageAlertCustomEvent extends SageAlertEvent {

	/**
	 * Update the long description for this event to the given value
	 * @param msg The new value for the long description
	 */
	public void setLongDescription(String msg);
	
	/**
	 * Update the medium description for this event to the given value
	 * @param msg The new value for the medium description
	 */
	public void setMediumDescription(String msg);
	
	/**
	 * Update the short description for this event to the given value
	 * @param msg The new value for the short description
	 */
	public void setShortDescription(String msg);
	
	/**
	 * Update the subject for this event to the given value
	 * @param subj The new value for the subject
	 */
	public void setSubject(String subj);
	
	/**
	 * <p>Return the ordered list of arguments for this event.</p>
	 * <p>The order of the arguments in this array must match the order of your <code>ARGTYPEn</code> listings from your <code>sagealert.properties</code> file.  This array is passed to the SageAlert API interpreter as is and is why the arg list must be in the defined order.</p>
	 * @return The ordered list of arguments for this event to be passed to the API interpreter for processing.
	 */
	public Object[] getArgs();
}
