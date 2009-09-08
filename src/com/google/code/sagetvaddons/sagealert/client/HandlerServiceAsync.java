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

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async version of the HandlerService interface
 * @author dbattams
 * @version $Id$
 */
public interface HandlerServiceAsync {
	/**
	 * Get a list of all registered handlers for a given event
	 * @param event A description of the event
	 * @param cb The callback object to be called when the async call completes
	 */
	public void getHandlers(SageEventMetaData event, AsyncCallback<List<NotificationServerSettings>> cb);
	
	/**
	 * Attach the list of handlers to the given event
	 * @param event A description of the event to attach to
	 * @param handlers The list of handlers to attach to the event
	 * @param cb The callback object to be called when the async call completes
	 */
	public void setHandlers(SageEventMetaData event, List<NotificationServerSettings> handlers, AsyncCallback<Void> cb);
	
	/**
	 * Get a collection of all the registered events from the server
	 * @param cb The callback object to be called when the async call completes
	 */
	public void getEventMetaData(AsyncCallback<Collection<SageEventMetaData>> cb);
}
