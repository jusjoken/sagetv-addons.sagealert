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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.SmtpSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dbattams
 *
 */
final class SmtpSettingsPanel extends FormPanel {
	static private final SmtpSettingsPanel INSTANCE = new SmtpSettingsPanel();
	static final SmtpSettingsPanel get() { return INSTANCE; }
	
	private SmtpSettingsPanel() {
		final TextField<String> host = new TextField<String>();
		host.setFieldLabel("Hostname");
		
		final NumberField port = new NumberField();
		port.setFieldLabel("Port");
		port.setAllowDecimals(false);
		
		final TextField<String> id = new TextField<String>();
		id.setFieldLabel("Username");
		
		final TextField<String> pwd = new TextField<String>();
		pwd.setFieldLabel("Password");
		pwd.setPassword(true);
		
		final CheckBox useSsl = new CheckBox();
		useSsl.setFieldLabel("Connect over SSL");
		
		final TextField<String> sender = new TextField<String>();
		sender.setFieldLabel("Sender address");
		
		add(host);
		add(port);
		add(id);
		add(pwd);
		add(useSsl);
		add(sender);

		final SettingsServiceAsync rpc = GWT.create(SettingsService.class);

		Button save = new Button("Save");
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				rpc.saveSmtpSettings(new SmtpSettings(host.getValue(), port.getValue().intValue(), id.getValue(), pwd.getValue(), sender.getValue(), useSsl.getValue()), new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Void result) {
						MessageBox.alert("Result", "SMTP settings have been saved!", null);
					}
					
				});
			}
			
		});
		add(save);
		
		rpc.getSmtpSettings(new AsyncCallback<SmtpSettings>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(SmtpSettings result) {
				if(result != null) {
					host.setValue(result.getHost());
					port.setValue(result.getPort());
					id.setValue(result.getUser());
					pwd.setValue(result.getPwd());
					useSsl.setValue(result.useSsl());
					sender.setValue(result.getSenderAddress());
				}
			}
			
		});
	}
}
