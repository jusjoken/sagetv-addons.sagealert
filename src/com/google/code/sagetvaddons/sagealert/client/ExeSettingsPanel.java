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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.code.sagetvaddons.sagealert.shared.ExeServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.code.sagetvaddons.sagealert.shared.ReporterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * GUI panel for configuring exe processor settings
 * @author dbattams
 * @version $Id$
 */
final class ExeSettingsPanel extends FormPanel {
	
	
	ExeSettingsPanel(final ExeServerSettings settings) {
		String heading;
		if(settings == null)
			heading = "Add New Process Executor";
		else
			heading = "Edit Process Executor";
		setHeading(heading);
		setWidth(480);
		this.setLabelWidth(240);
		
		final TextField<String> desc = new TextField<String>();
		desc.setAllowBlank(false);
		desc.setFieldLabel("Description");
		if(settings != null) {
			desc.setValue(settings.getDesc());
			desc.setEnabled(false);
		}
		add(desc);
		
		final TextField<String> fName = new TextField<String>();
		fName.setAllowBlank(false);
		fName.setFieldLabel("Executable");
		if(settings != null)
			fName.setValue(settings.getExeName());
		add(fName);
		
		final TextField<String> args = new TextField<String>();
		args.setAllowBlank(true);
		args.setFieldLabel("Process Arguments");
		if(settings != null)
			args.setValue(settings.getArgs());
		add(args);
		
		String btnLbl;
		if(settings == null)
			btnLbl = "Create";
		else
			btnLbl = "Update";
		Button save = new Button(btnLbl);
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(!ExeSettingsPanel.this.isValid())
					return;
				final ReporterServiceAsync rpc = GWT.create(ReporterService.class);
				List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
				final ExeServerSettings s = new ExeServerSettings(desc.getValue(), fName.getValue(), args.getValue() == null ? "" : args.getValue());
				list.add(s);
				rpc.save(list, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						GWT.log("ERROR", caught);
						MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
						
					}

					public void onSuccess(Void result) {
						MessageBox.alert("Result", "Process executor saved!", null);
						if(settings == null)
							MenuDataStore.CURRENT.addReporter(BeanModelLookup.get().getFactory(s.getClass()).createModel(s), "Process Executor");
						else {
							rpc.getReporters(s.getClass().getName(), new AsyncCallback<List<NotificationServerSettings>>() {

								public void onFailure(Throwable caught) {
									MessageBox.alert("ERROR", "Please refresh your browser window!", null);
								}

								public void onSuccess(List<NotificationServerSettings> result) {
									boolean replaced = false;
									for(NotificationServerSettings srv : result)
										if(srv.getDataStoreKey().equals(s.getDataStoreKey())) {
											BeanModel b = BeanModelLookup.get().getFactory(srv.getClass()).createModel(srv);
											MenuDataStore.CURRENT.rmReporter(BeanModelLookup.get().getFactory(s.getClass()).createModel(s));
											MenuDataStore.CURRENT.addReporter(b, "Process Executor");
											replaced = true;
											break;
										}
									if(!replaced)
										MessageBox.alert("ERROR", "Please refresh your browser window!", null);
								}
								
							});
						}
					}
					
				});
			}
			
		});
		add(save);
	}
}
