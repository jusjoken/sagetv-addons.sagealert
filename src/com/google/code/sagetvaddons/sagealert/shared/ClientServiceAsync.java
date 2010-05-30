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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async version of the ClientService interface
 * @author dbattams
 * @version $Id$
 */
public interface ClientServiceAsync {
	/**
	 * Save a Client instance to the data store
	 * @param c The client to be saved
	 * @param cb The async callback
	 */
	public void saveClient(Client c, AsyncCallback<Void> cb);
	
	/**
	 * Load all the Client instances from the server
	 * @param cb The async callback
	 */
	public void getClients(AsyncCallback<List<Client>> cb);
	
	/**
	 * Get a Client by id
	 * @param id The client id
	 * @param cb The async callback
	 */
	public void getClient(String id, AsyncCallback<Client> cb);

	/**
	 * Delete a list of clients from the data store
	 * @param clients The list of clients to be deleted
	 * @param cb The async callback
	 */
	public void deleteClients(Collection<Client> clients, AsyncCallback<Void> cb);
	
	public void saveClients(Collection<Client> clients, AsyncCallback<Void> cb);
}
