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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for RPC reporter operations.
 * 
 * A reporter is the generic term used to describe the connection settings for a notification system.
 * 
 * @author dbattams
 * @version $Id$
 */
@RemoteServiceRelativePath("ReporterService")
public interface ReporterService extends RemoteService {
	/**
	 * Get a list of all the reporters registered with the server
	 * @return The list of all reporters registered with the server
	 */
	public List<NotificationServerSettings> getAllReporters();
	
	/**
	 * Get a list of all reporters for a given notification system
	 * @param type A description (FQ class name) of the notification system
	 * @return A list of all reporters for the given notification system; the list may be empty, but not null
	 */
	public List<NotificationServerSettings> getReporters(String type);
	
	/**
	 * Save the list of reporters to the data store
	 * @param settings The list of reporters to be saved
	 */
	public void save(List<NotificationServerSettings> settings);
	
	/**
	 * Delete the list of reporters from the data store; also removes them from any events they may have been listening to
	 * @param settings The list of reporters to be deleted
	 */
	public void delete(List<NotificationServerSettings> settings);
}
