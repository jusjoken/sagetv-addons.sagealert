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
package com.google.code.sagetvaddons.sagealert.server;

import java.util.Collection;
import java.util.List;

import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.ClientService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server side implementation of the ClientService interface
 * @author dbattams
 * @version $Id$
 */
@SuppressWarnings("serial")
final public class ClientServiceImpl extends RemoteServiceServlet implements
		ClientService {

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.ClientService#getClient(java.lang.String)
	 */
	public Client getClient(String id) {
		return DataStore.getInstance().getClient(id);
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.ClientService#getClients()
	 */
	public List<Client> getClients() {
		return DataStore.getInstance().getClients();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.ClientService#saveClient(com.google.code.sagetvaddons.sagealert.client.Client)
	 */
	public void saveClient(Client c) {
		DataStore.getInstance().saveClient(c);
	}

	public void deleteClients(Collection<Client> clients) {
		DataStore.getInstance().deleteClients(clients);
	}

	public void saveClients(Collection<Client> clients) {
		for(Client c : clients)
			DataStore.getInstance().saveClient(c);
	}
}
