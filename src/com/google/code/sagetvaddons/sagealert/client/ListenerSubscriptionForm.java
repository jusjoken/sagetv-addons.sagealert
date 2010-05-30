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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.code.sagetvaddons.sagealert.shared.HandlerService;
import com.google.code.sagetvaddons.sagealert.shared.HandlerServiceAsync;
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
class ListenerSubscriptionForm extends FormPanel {

	List<CheckBox> selections;
	List<NotificationServerSettings> settings;
	Button submit;
	
	ListenerSubscriptionForm(final SageAlertEventMetadata data) {
		selections = new ArrayList<CheckBox>();
		settings = new ArrayList<NotificationServerSettings>();
		submit = new Button("Save");
		submit.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				List<NotificationServerSettings> list = new ArrayList<NotificationServerSettings>();
				for(int i = 0; i < selections.size(); ++i)
					if(selections.get(i).getValue())
						list.add(settings.get(i));
				HandlerServiceAsync rpc = GWT.create(HandlerService.class);
				rpc.setHandlers(data.getEventId(), list, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Void result) {
						MessageBox.alert("Result", "Subscriptions saved!", null);
					}
					
				});
			}
			
		});
		setWidth(480);
		this.setLabelWidth(340);
		this.setFieldWidth(20);
		this.setLabelAlign(LabelAlign.RIGHT);
		setHeading("Configure Listeners: " + data.getEventName());
		LabelField lbl = new LabelField("");
		lbl.setFieldLabel(data.getDesc());
		add(lbl);
		final ReporterServiceAsync rSvc = GWT.create(ReporterService.class);
		final HandlerServiceAsync hSvc = GWT.create(HandlerService.class);
		GWT.log("ID: " + data.getEventId());
		hSvc.getHandlers(data.getEventId(), new AsyncCallback<List<NotificationServerSettings>>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(final List<NotificationServerSettings> subscribed) {
				rSvc.getAllReporters(new AsyncCallback<List<NotificationServerSettings>>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(List<NotificationServerSettings> result) {
						for(NotificationServerSettings s : result) {
							CheckBox cb = new CheckBox();
							cb.setFieldLabel(s.toString());
							if(subscribed.contains(s))
								cb.setValue(true);
							else
								cb.setValue(false);
							selections.add(cb);
							settings.add(s);
							add(cb);
						}
						add(submit);
						layout();
						GWT.log(ListenerSubscriptionForm.this.getParent().toString());
						((LayoutContainer)ListenerSubscriptionForm.this.getParent()).layout(true);
					}			
				});
				
			}
			
		});
	}
}
