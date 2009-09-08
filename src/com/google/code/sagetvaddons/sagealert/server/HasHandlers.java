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

import java.util.Collection;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

/**
 * Describes the operations that the event handler manager must implement
 * @author dbattams
 * @version $Id$
 */
interface HasHandlers {
	/**
	 * Add a new handler to the given event
	 * @param h The handler to be added
	 * @param e The event the handler will be added to
	 */
	void addHandler(SageEventHandler h, SageEvent e);
	
	/**
	 * Add a new handler to the described event
	 * @param h The handler to be added
	 * @param e The event description the handler will be added to
	 */
	void addHandler(SageEventHandler h, SageEventMetaData e);
	
	/**
	 * Add all the given handlers to the given event
	 * @param h A list of handlers to be added to the event
	 * @param e The event the handlers will be attached to
	 */
	void addHandlers(Collection<SageEventHandler> h, SageEvent e);
	
	/**
	 * Add the list of handlers to the given event
	 * @param h The list of handlers to attach to the event
	 * @param e A description of the event being attached to
	 */
	void addHandlers(Collection<SageEventHandler> h, SageEventMetaData e);
	
	/**
	 * Remove a handler from the given event
	 * @param h The handler being removed
	 * @param e The event being detached from
	 */
	void removeHandler(SageEventHandler h, SageEvent e);

	/**
	 * Remove a handler from all events it was listening to
	 * @param h The handler to be removed
	 */
	void removeHandlerFromAllEvents(SageEventHandler h);
	
	/**
	 * Remove all handlers from an event
	 * @param e The event that will have all handlers dropped
	 */
	void removeAllHandlers(SageEvent e);
	
	/**
	 * Remove all handlers from an event
	 * @param e A description of the event that will have all handlers dropped
	 */
	void removeAllHandlers(SageEventMetaData e);
	
	/**
	 * Remove all handlers from a given event
	 * @param h The list of handlers to be removed 
	 * @param e The event from which the handlers will be dropped
	 */
	void removeHandlers(Collection<SageEventHandler> h, SageEvent e);
	
	/**
	 * Get a list of all handlers attached to an event
	 * @param e The event to get all handlers for
	 * @return The list of handlers attached to the event; the list may be empty, but not null
	 */
	Collection<SageEventHandler> getHandlers(SageEvent e);
	
	/**
	 * Fire an event to all attached handlers
	 * @param e The event to be fired
	 */
	void fire(SageEvent e);
}
