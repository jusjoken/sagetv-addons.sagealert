/*
 *      Copyright 2010 Battams, Derek
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.code.sagetvaddons.sagealert.shared.CsvLogFileSettings;
import com.google.code.sagetvaddons.sagealert.shared.EmailSettings;
import com.google.code.sagetvaddons.sagealert.shared.GrowlServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.HandlerService;
import com.google.code.sagetvaddons.sagealert.shared.HandlerServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.code.sagetvaddons.sagealert.shared.ReporterServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.code.sagetvaddons.sagealert.shared.TwitterSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbattams
 *
 */
final class MenuDataStore extends TreeStore<ModelData> {
	static private final MenuDataStore INSTANCE = new MenuDataStore();
	static final MenuDataStore get() { return INSTANCE; }
	
	Collection<BaseModelData> srvTypes;
	
	private MenuDataStore() {
		srvTypes = new ArrayList<BaseModelData>();
		HandlerServiceAsync rpc = GWT.create(HandlerService.class);
		rpc.getAllMetadata(new AsyncCallback<Collection<SageAlertEventMetadata>>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(Collection<SageAlertEventMetadata> result) {
				final BaseModelData events = new BaseModelData();
				events.set("id", "Events");
				add(events, false);
				final BaseModelData servers = new BaseModelData();
				servers.set("id", "Servers");
				add(servers, false);
				final BaseModelData smtp = new BaseModelData();
				smtp.set("id", "SMTP Settings");
				add(smtp, false);
				final BaseModelData clients = new BaseModelData();
				clients.set("id", "Clients");
				add(clients, false);
				
				ReporterServiceAsync reporters = GWT.create(ReporterService.class);
				reporters.getReporters(EmailSettings.class.getName(), new AsyncCallback<List<NotificationServerSettings>>() {

					public void onFailure(Throwable caught) {
						GWT.log(caught.getLocalizedMessage());
					}

					public void onSuccess(List<NotificationServerSettings> result) {
						BaseModelData srv = new BaseModelData();
						srv.set("id", "Email");
						srvTypes.add(srv);
						add(servers, srv, false);
						for(NotificationServerSettings s : result) {
							BeanModelFactory bmf = BeanModelLookup.get().getFactory(s.getClass());
							add(srv, bmf.createModel(s), false);
						}
					}
				});
				
				reporters.getReporters(TwitterSettings.class.getName(), new AsyncCallback<List<NotificationServerSettings>>() {

					public void onFailure(Throwable caught) {
					}

					public void onSuccess(List<NotificationServerSettings> result) {
						BaseModelData srv = new BaseModelData();
						srv.set("id", "Twitter");
						srvTypes.add(srv);
						add(servers, srv, false);
						for(NotificationServerSettings s : result) {
							BeanModelFactory bmf = BeanModelLookup.get().getFactory(s.getClass());
							add(srv, bmf.createModel(s), false);
						}												
					}
					
				});

				reporters.getReporters(GrowlServerSettings.class.getName(), new AsyncCallback<List<NotificationServerSettings>>() {

					public void onFailure(Throwable caught) {
					}

					public void onSuccess(List<NotificationServerSettings> result) {
						BaseModelData srv = new BaseModelData();
						srv.set("id", "Growl");
						srvTypes.add(srv);
						add(servers, srv, false);
						for(NotificationServerSettings s : result) {
							BeanModelFactory bmf = BeanModelLookup.get().getFactory(s.getClass());
							add(srv, bmf.createModel(s), false);
						}
					}
					
				});
				
				reporters.getReporters(CsvLogFileSettings.class.getName(), new AsyncCallback<List<NotificationServerSettings>>() {

					public void onFailure(Throwable caught) {
						
					}

					public void onSuccess(List<NotificationServerSettings> result) {
						BaseModelData srv = new BaseModelData();
						srv.set("id", "CSV File");
						srvTypes.add(srv);
						add(servers, srv, false);
						for(NotificationServerSettings s : result) {
							BeanModelFactory bmf = BeanModelLookup.get().getFactory(s.getClass());
							add(srv, bmf.createModel(s), false);
						}						
					}
					
				});

				for(SageAlertEventMetadata m : result)
					add(events, m, false);
				sort("id", SortDir.ASC);
			}
			
		});
	}
	
	public void addReporter(BeanModel s, String type) {
		for(BaseModelData d : srvTypes)
			if(d.get("id").equals(type)) {
				add(d, s, false);
				return;
			}
		Window.alert("Invalid server type! [" + type + "]");
	}
	
	public void rmReporter(BeanModel s) {
		remove(s);
	}
	
	public List<NotificationServerSettings> getReporters() {
		List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
		for(ModelData d : srvTypes)
			for(ModelData m : getChildren(d))
				list.add((NotificationServerSettings)(((BeanModel)m).getBean()));
		return list;
	}
}
