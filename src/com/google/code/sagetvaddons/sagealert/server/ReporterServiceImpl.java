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

import java.util.List;

import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server side implementation for the ReporterService RPC interface
 * @author dbattams
 * @version $Id$
 */
@SuppressWarnings("serial")
public final class ReporterServiceImpl extends RemoteServiceServlet implements
		ReporterService {

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.ReporterService#getAllReporters()
	 */
	public List<NotificationServerSettings> getAllReporters() {
		return DataStore.getInstance().getAllReporters();
	}

	public void delete(List<NotificationServerSettings> settings) {
		for(NotificationServerSettings s : settings) {
			SageEventHandlerManager.get().removeHandlerFromAllEvents(NotificationServerFactory.getInstance(s));
			DataStore.getInstance().deleteReporter(s.getClass().getCanonicalName(), s.getDataStoreKey());
		}
	}

	public List<NotificationServerSettings> getReporters(String type) {
		return DataStore.getInstance().getReporters(type);
	}

	public void save(List<NotificationServerSettings> settings) {
		for(NotificationServerSettings s : settings)
			DataStore.getInstance().saveReporter(s);
	}

}
