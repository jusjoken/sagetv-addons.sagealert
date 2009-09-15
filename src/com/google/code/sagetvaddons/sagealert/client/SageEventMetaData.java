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
package com.google.code.sagetvaddons.sagealert.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Describes the details of a SageEvent type
 * 
 * SageEvent objects can't be used on the client side because they contain SageTV objects, which aren't serializable so instead these metadata objects are used between
 * client and server to describe/reference SageEvent objects
 * @author dbattams
 * @version $Id$
 */
public final class SageEventMetaData implements IsSerializable {

	private String className;
	private String eventTitle;
	private String eventDescription;
	
	// Required for GWT RPC; do not use
	@SuppressWarnings("unused")
	private SageEventMetaData() {}
	
	/**
	 * Constructor
	 * @param clsName The FQ class name being described by this metadata object
	 * @param desc A textual description of what the event signifies (i.e. "Alert when a recording has started.")
	 */
	public SageEventMetaData(String clsName, String title, String desc) {
		className = clsName;
		eventTitle = title;
		eventDescription = desc;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the eventDescription
	 */
	public String getEventDescription() {
		return eventDescription;
	}
	
	/**
	 * @return the eventTitle
	 */
	public String getEventTitle() {
		return eventTitle;
	}
}
