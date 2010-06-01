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
package com.google.code.sagetvaddons.sagealert.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.code.sagetvaddons.sagealert.shared.GrowlServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.code.sagetvaddons.sagealert.shared.ReporterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Widget to register/configure Growl instances
 * @author dbattams
 * @version $Id$
 */
final class GrowlSettingsPanel extends FormPanel {

	GrowlSettingsPanel() {
		setHeading("Add Growl Server");
		setWidth(480);
		
		final TextField<String> host = new TextField<String>();
		host.setAllowBlank(false);
		host.setFieldLabel("Host");
		add(host);
		
		final TextField<String> pwd = new TextField<String>();
		pwd.setPassword(true);
		pwd.setFieldLabel("Password (optional)");
		add(pwd);
		
		Button save = new Button("Create");
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(!GrowlSettingsPanel.this.isValid())
					return;
				ReporterServiceAsync rpc = GWT.create(ReporterService.class);
				List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
				final GrowlServerSettings s = new GrowlServerSettings(host.getValue(), pwd.getValue());
				list.add(s);
				rpc.save(list, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
						
					}

					public void onSuccess(Void result) {
						MessageBox.alert("Result", "Growl server added!", null);
						MenuDataStore.get().addReporter(BeanModelLookup.get().getFactory(s.getClass()).createModel(s), "Growl");
					}
					
				});
			}
			
		});
		add(save);
	}	
}
