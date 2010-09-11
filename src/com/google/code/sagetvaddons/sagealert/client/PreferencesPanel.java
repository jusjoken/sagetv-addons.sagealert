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
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GUI panel for configuring app preferences
 * @author dbattams
 * @version $Id$
 */
final class PreferencesPanel extends FormPanel {
	static private final PreferencesPanel INSTANCE = new PreferencesPanel();
	static final PreferencesPanel get() { return INSTANCE; }
	
	static private final String IGNORE_REPEAT_SYS_MSGS = "sagealert/IgnoreRepeatSysMsgs";
	
	private PreferencesPanel() {
		setHeading("SageAlert Preferences");
		setWidth(480);
		setFieldWidth(80);
		setLabelWidth(385);
		
		final CheckBox ignoreRepeatSysMsgs = new CheckBox();
		ignoreRepeatSysMsgs.setFieldLabel("Ignore repeated system messages");
		add(ignoreRepeatSysMsgs);

		final SettingsServiceAsync rpc = GWT.create(SettingsService.class);

		Button save = new Button("Save");
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(!PreferencesPanel.this.isValid())
					return;
				rpc.setSetting(IGNORE_REPEAT_SYS_MSGS, ignoreRepeatSysMsgs.getValueAttribute().toString(), new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
						
					}

					public void onSuccess(Void result) {
						MessageBox.alert("Result", "Preferences saved!", null);
					}
				});
			}
			
		});
		add(save);
		rpc.getSetting(IGNORE_REPEAT_SYS_MSGS, "true", new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
			}

			public void onSuccess(String result) {
				ignoreRepeatSysMsgs.setValue(Boolean.parseBoolean(result));
			}
			
		});
	}
}
