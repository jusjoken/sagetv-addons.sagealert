/*
 *      Copyright 2009-2010 Battams, Derek
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

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for RPC handler operations
 * @author dbattams
 * @version $Id$
 */
@RemoteServiceRelativePath("HandlerService")
public interface HandlerService extends RemoteService {
	/**
	 * Get a list of all the handlers for a given event
	 * @param event The event id to retrieve handlers for
	 * @return A list of handlers that are currently registered to receive the given event when fired
	 */
	public List<NotificationServerSettings> getHandlers(String event);
	
	/**
	 * Attach the given list of handlers to the given event
	 * @param event The event to attach the handlers to
	 * @param handlers The list of handlers to attach to the event
	 */
	public void setHandlers(String event, List<NotificationServerSettings> handlers);
	
	/**
	 * Get a collection of all events defined and registered with the server
	 * @return A collection of all registered event descriptions; the list may be empty if no events are registered
	 */
	public Collection<String> getAllEvents();
	
	/**
	 * Get the metadata for the specified event id
	 * @param eventId The unique event id
	 * @return The metadata object for the given eventId or null if the metadata is unavailable
	 */
	public SageAlertEventMetadata getMetadata(String eventId);
	
	/**
	 * Return a collection of all registered metadata objects
	 * @return The collection of all metadata objects
	 */
	public Collection<SageAlertEventMetadata> getAllMetadata();
	
	/**
	 * Test a notification server
	 * @param settings The settings object of the server to test
	 */
	public void testServer(NotificationServerSettings settings);
}
