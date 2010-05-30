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
 * RPC interface for saving/loading Client instances from the server
 * @author dbattams
 * @version $Id$
 */
@RemoteServiceRelativePath("ClientService")
public interface ClientService extends RemoteService {
	/**
	 * Save a Client instance to the data store
	 * @param c The client to be saved
	 */
	public void saveClient(Client c);
	
	/**
	 * Load all the Client instances from the server
	 * @return A Collection of Client instances; the collection may be empty, but not null
	 */
	public List<Client> getClients();
	
	/**
	 * Get a Client by id
	 * @param id The client id
	 * @return A Client instance for the given id
	 */
	public Client getClient(String id);
	
	/**
	 * Delete a collection of clients from the data store
	 * @param clients The collection of clients to be deleted
	 */
	public void deleteClients(Collection<Client> clients);
	
	public void saveClients(Collection<Client> clients);
}
