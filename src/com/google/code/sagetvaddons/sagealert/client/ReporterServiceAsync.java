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

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async version of the ReportService interface
 * @author dbattams
 * @version $Id$
 */
public interface ReporterServiceAsync {
	/**
	 * Get a list of all reporters registered with the server
	 * @param cb The callback object to be called when the async call completes
	 */
	public void getAllReporters(AsyncCallback<List<NotificationServerSettings>> cb);
	
	/**
	 * Get a list of all reporters for a given notification system
	 * @param type The notification system description (FQ class name)
	 * @param cb The async callback
	 */
	public void getReporters(String type, AsyncCallback<List<NotificationServerSettings>> cb);
	
	/**
	 * Save the list of reporters to the data store
	 * @param settings The list of reporters to be saved
	 * @param cb The async callback
	 */
	public void save(List<NotificationServerSettings> settings, AsyncCallback<Void> cb);
	
	/**
	 * Delete the list of reporters; also removes the reporters from any events they may have been listening to
	 * @param settings The list of reporters to delete
	 * @param cb The async callback
	 */
	public void delete(List<NotificationServerSettings> settings, AsyncCallback<Void> cb);
}
