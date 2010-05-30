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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.code.sagetvaddons.sagealert.shared.EmailSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.code.sagetvaddons.sagealert.shared.ReporterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GUI panel for configuring email settings
 * @author dbattams
 * @version $Id$
 */
final class EmailSettingsPanel extends FormPanel {
	
	
	EmailSettingsPanel() {
		setHeading("Add New Email Address");
		setWidth(480);
		
		final TextField<String> addr = new TextField<String>();
		addr.setAllowBlank(false);
		addr.setFieldLabel("Email");
		add(addr);
		
		final SimpleComboBox<String> type = new SimpleComboBox<String>();
		type.add("Short");
		type.add("Medium");
		type.add("Long");
		type.setFieldLabel("Message Type");
		add(type);
		
		Button save = new Button("Create");
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(!EmailSettingsPanel.this.isValid())
					return;
				ReporterServiceAsync rpc = GWT.create(ReporterService.class);
				List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
				final EmailSettings s = new EmailSettings(addr.getValue(), type.getValue().getValue());
				list.add(s);
				rpc.save(list, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
						
					}

					public void onSuccess(Void result) {
						MessageBox.alert("Result", "Email address added!", null);
						MenuDataStore.get().addReporter(s, "Email");
					}
					
				});
			}
			
		});
		add(save);
	}
}
