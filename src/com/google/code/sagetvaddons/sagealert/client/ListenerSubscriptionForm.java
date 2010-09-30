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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.code.sagetvaddons.sagealert.shared.HandlerService;
import com.google.code.sagetvaddons.sagealert.shared.HandlerServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.ReporterService;
import com.google.code.sagetvaddons.sagealert.shared.ReporterServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author dbattams
 *
 */
class ListenerSubscriptionForm extends FormPanel {

	TextField<String> subj, shortMsg, medMsg, longMsg;
	List<CheckBox> selections;
	List<NotificationServerSettings> settings;
	Button submit;
	SageAlertEventMetadata metadata;
	boolean isBuilt;
	ToolBar toolbar;
	
	ListenerSubscriptionForm(final SageAlertEventMetadata data) {
		metadata = data;
		isBuilt = false;
		toolbar = new ToolBar();
		setWidth(640);
		setAutoHeight(false);
		this.setScrollMode(Scroll.AUTOY);
		setLabelWidth(380);
		setFieldWidth(240);
		setLabelAlign(LabelAlign.RIGHT);
		setHeading("Configure Listeners: " + data.getEventName());
		
		subj = new TextField<String>();
		subj.setFieldLabel("Alert Subject");
		if(data.getSubject() != null)
			subj.setValue(data.getSubject());
		
		shortMsg = new TextField<String>();
		shortMsg.setFieldLabel("Short Message");
		if(data.getShortMsg() != null)
			shortMsg.setValue(data.getShortMsg());
		
		medMsg = new TextField<String>();
		medMsg.setFieldLabel("Medium Message");
		if(data.getMedMsg() != null)
			medMsg.setValue(data.getMedMsg());
		
		longMsg = new TextField<String>();
		longMsg.setFieldLabel("Long Message");
		if(data.getLongMsg() != null)
			longMsg.setValue(data.getLongMsg());
		
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
				
				metadata.setSubject(subj.getValue());
				metadata.setShortMsg(shortMsg.getValue());
				metadata.setMedMsg(medMsg.getValue());
				metadata.setLongMsg(longMsg.getValue());
				rpc.saveMetadata(metadata, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						GWT.log("ERROR", caught);
						MessageBox.alert("ERROR", "Failed to save alert settings!", null);
					}

					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
			
		});
		toolbar.add(submit);
		Button argList = new Button("View Arguments");
		argList.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				Dialog w = new Dialog();
				w.setHeading("Event Arguments: " + data.getEventName());
				w.setAutoHide(false);
				w.setAutoHeight(true);
				w.setAutoWidth(true);
				w.setCollapsible(true);
				w.setHideOnButtonClick(true);
				List<String> types = data.getObjTypes();
				
				if(types.size() > 0)
					for(int i = 0; i < types.size(); ++i)
						w.add(new HTML("<p style=\"margin: 6px;\"><b>$" + i + "</b>: " + types.get(i) + "</p>"));
				else
					w.add(new HTML("<p style=\"margin: 6px;\"><b>No objects provided by this event.</b></p>"));
				w.show();
			}
			
		});
		toolbar.add(argList);
		setBottomComponent(toolbar);
		LabelField lbl = new LabelField();
		lbl.setFieldLabel(data.getDesc());
		add(lbl);
		add(subj);
		add(shortMsg);
		add(medMsg);
		add(longMsg);
		final ReporterServiceAsync rSvc = GWT.create(ReporterService.class);
		final HandlerServiceAsync hSvc = GWT.create(HandlerService.class);
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
						layout();
						GWT.log(ListenerSubscriptionForm.this.getParent().toString());
						((LayoutContainer)ListenerSubscriptionForm.this.getParent()).layout(true);
						isBuilt = true;
					}			
				});
				
			}
			
		});
	}
	
	protected void setSubject(String subj) {
		this.subj.setValue(subj);
	}
	
	protected void setShortMsg(String msg) {
		this.shortMsg.setValue(msg);
	}
	
	protected void setMedMsg(String msg) {
		this.medMsg.setValue(msg);
	}
	
	protected void setLongMsg(String msg) {
		this.longMsg.setValue(msg);
	}
	
	public boolean isBuilt() { return isBuilt; }
}
