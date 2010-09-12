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
import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.code.sagetvaddons.sagealert.shared.EmailSettings;
import com.google.code.sagetvaddons.sagealert.shared.ExeServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.code.sagetvaddons.sagealert.shared.ReporterServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbattams
 *
 */
final class MenuTreePanel extends TreePanel<ModelData> {
	
	private class ReporterContextMenu extends Menu {
		MenuItem create;
		MenuItem edit;
		MenuItem delete;

		ReporterContextMenu() {
			create = new MenuItem();
			create.setText("Create");
			create.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					ModelData d = MenuTreePanel.this.getSelectionModel().getSelectedItem();
					String srvType = (String)d.get("id");
					if(srvType.equals("Twitter"))
						SageAlertViewport.get().setCenterContent(new TwitterSettingsPanel());
					else if(srvType.equals("Email"))
						SageAlertViewport.get().setCenterContent(new EmailSettingsPanel(null));
					else if(srvType.equals("Growl"))
						SageAlertViewport.get().setCenterContent(new GrowlSettingsPanel());
					else if(srvType.equals("CSV File"))
						SageAlertViewport.get().setCenterContent(new CsvLogFileSettingsPanel());
					else if(srvType.equals("Process Executor"))
						SageAlertViewport.get().setCenterContent(new ExeSettingsPanel(null));
				}			
			});
			this.add(create);

			edit = new MenuItem();
			edit.setText("Edit");
			edit.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					final NotificationServerSettings s = ((BeanModel)MenuTreePanel.this.getSelectionModel().getSelectedItem()).getBean();
					if(s instanceof ExeServerSettings)
						SageAlertViewport.get().setCenterContent(new ExeSettingsPanel((ExeServerSettings)s));
					else if(s instanceof EmailSettings)
						SageAlertViewport.get().setCenterContent(new EmailSettingsPanel((EmailSettings)s));
					else
						MessageBox.alert("Edit Not Available", "The selected object cannot be edited!", null);
				}

			});
			this.add(edit);

			delete = new MenuItem();
			delete.setText("Delete");
			delete.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					final NotificationServerSettings s = ((BeanModel)MenuTreePanel.this.getSelectionModel().getSelectedItem()).getBean();
					MessageBox.confirm("Confirm Removal", "Are you sure you want to delete this reporter: '" + s + "'?", new Listener<MessageBoxEvent>() {

						public void handleEvent(MessageBoxEvent be) {
							if(be.getButtonClicked().getText().toLowerCase().equals("yes")) {
								ReporterServiceAsync rpc = GWT.create(ReporterService.class);
								List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
								list.add(s);
								rpc.delete(list, new AsyncCallback<Void>() {

									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									public void onSuccess(Void result) {
										MenuDataStore.CURRENT.rmReporter(BeanModelLookup.get().getFactory(s.getClass()).createModel(s));
										MessageBox.alert("Success", "The reporter was deleted successfully!", null);
									}
									
								});
							}
						}
					});
				}

			});
			this.add(delete);			
		}

		@Override
		public void onShow() {
			super.onShow();
			ModelData d = getSelectionModel().getSelectedItem();
			if(!(d instanceof BeanModel)) {
				delete.disable();
				edit.disable();
			}
			if(store.getParent(d) == null || (isLeaf(d) && !store.getParent(d).get("id").equals("Servers")))
				create.disable();
		}

		@Override
		public void onHide() {
			super.onHide();
			delete.enable();
			create.enable();
			edit.enable();
		}
	}

	MenuTreePanel(final TreeStore<ModelData> store) {
		super(store);
		setDisplayProperty("id");
		this.addListener(Events.OnClick, new Listener<TreePanelEvent<ModelData>>() {

			public void handleEvent(TreePanelEvent<ModelData> be) {
				if(be.getItem() instanceof SageAlertEventMetadata)
					SageAlertViewport.get().setCenterContent(new ListenerSubscriptionForm((SageAlertEventMetadata)be.getItem()));
				else if(store.getParent(be.getItem()) == null && be.getItem().get("id").equals("Clients"))
					SageAlertViewport.get().setCenterContent(ClientSettingsPanel.getInstance());
			}

		});
		this.setContextMenu(new ReporterContextMenu());
	}
}
