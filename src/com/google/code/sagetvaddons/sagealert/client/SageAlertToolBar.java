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
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.UserSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author dbattams
 *
 */
final class SageAlertToolBar extends ToolBar {

	private Button donate;
	private Button register;
	private Button help;
	
	SageAlertToolBar() {
		donate = new Button("Donate");
		donate.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getDonateImg()));
		donate.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("Donations", "Donations are used for the sole purpose of defraying out of pocket expenses for developing my plugins.  These expenses include the purchase of addtional SageTV server licenses and hardware used to create the development environment used solely for the purposes of plugin development.  Donators will receive a license file that unlocks all features of this plugin.  Do you wish to donate?", new Listener<MessageBoxEvent>() {

					public void handleEvent(MessageBoxEvent be) {
						if(be.getButtonClicked().getText().toUpperCase().equals("YES")) {
							MessageBox.alert("Taking You to PayPal", "If a new window directing you to PayPal does not appear then you may need to disable popup blockers in your browser.", new Listener<MessageBoxEvent>() {

								public void handleEvent(MessageBoxEvent be) {
									Window.open("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AQV6A8JXY7XRJ", "_blank", null);						
								}
								
							});
						}
					}
					
				});
			}
			
		});
		
		register = new Button("Register");
		register.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getRegisterImg()));
		register.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.prompt("Email", "Ensure your license file has been installed and then enter the email address for the license below:", new Listener<MessageBoxEvent>() {

					public void handleEvent(MessageBoxEvent be) {
						if(be.getButtonClicked().getText().toUpperCase().equals("OK")) {
							String email = be.getMessageBox().getTextBox().getValue();
							if(email == null || email.length() == 0)
								MessageBox.alert("ERROR", "Registered email cannot be blank!", null);
							else {
								SettingsServiceAsync rpc = GWT.create(SettingsService.class);
								rpc.setSetting(UserSettings.LIC_EMAIL, email, new AsyncCallback<Void>() {

									public void onFailure(Throwable caught) {
										MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
									}

									public void onSuccess(Void result) {
										MessageBox.alert("Restart SageAlert", "You must restart SageAlert for the license settings to take effect.", null);
									}
									
								});
							}
						}
					}
					
				});
			}
			
		});
		
		help = new Button("Help");
		help.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getHelpImg()));
		help.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				new AboutWindow().show();
			}
		});

		add(donate);
		add(register);
		add(help);
	}
}
