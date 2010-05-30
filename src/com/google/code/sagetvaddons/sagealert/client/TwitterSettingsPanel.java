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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.code.sagetvaddons.sagealert.shared.TwitterService;
import com.google.code.sagetvaddons.sagealert.shared.TwitterServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.TwitterSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The GUI widget for registering Twitter accounts with SageAlert
 * @author dbattams
 * @version $Id$
 */
final class TwitterSettingsPanel extends FormPanel {
		
	private TwitterServiceAsync rpc;
	
	TwitterSettingsPanel() {
		rpc = GWT.create(TwitterService.class);
		setHeading("New Twitter Account");
		setFrame(true);
		setWidth(400);

		Button urlBtn = new Button();
		urlBtn.setText("Click here");
		urlBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				rpc.getPinUrl(new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {
						Window.alert(caught.getLocalizedMessage());
					}

					public void onSuccess(String result) {
						Window.open(result, "twitterPin", null);
					}
					
				});
			}			
		});
		AdapterField url = new AdapterField(urlBtn);
		url.setFieldLabel("Obtain PIN");
		
		final TextField<String> pin = new TextField<String>();
		pin.setPassword(true);
		pin.setFieldLabel("PIN");
		pin.setAllowBlank(false);
	
		add(url);
		add(pin);
		
		Button b = new Button("Add Twitter Account");
		add(b);
		FormButtonBinding binder = new FormButtonBinding(this);
		binder.addButton(b);
		b.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				rpc.submitPin(pin.getValue(), new AsyncCallback<TwitterSettings>() {

					public void onFailure(Throwable caught) {
						Window.alert(caught.getLocalizedMessage());
					}

					public void onSuccess(TwitterSettings result) {
						MenuDataStore.get().addReporter(result, "Twitter");
						Window.alert(result.getAlias() + " registered with SageAlert!");
						SageAlertViewport.get().setCenterContent(null);
					}
					
				});
			}
			
		});
	}
}
