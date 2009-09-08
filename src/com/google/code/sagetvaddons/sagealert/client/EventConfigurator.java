/*
 *      Copyright 2009 Battams, Derek
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A popup widget used to attach listeners to a specific event
 * @author dbattams
 * @version $Id$
 */
final class EventConfigurator extends PopupPanel {
	static private final EventConfigurator INSTANCE = new EventConfigurator();
	static final EventConfigurator getInstance(SageEventMetaData event) {
		INSTANCE.setEvent(event);
		INSTANCE.loadListBox();
		return INSTANCE;
	}
	
	private SageEventMetaData event;
	
	private Label desc;
	private ListBox reporterChoices;
	private Button saveBtn;
	private Button cancelBtn;
	
	private List<NotificationServerSettings> reporters; 
	private VerticalPanel container;
	private HorizontalPanel btnPanel;
	
	private EventConfigurator() {
		setModal(true);
		event = null;
		container = new VerticalPanel();
		btnPanel = new HorizontalPanel();
		desc = new Label();
		
		reporterChoices = new ListBox(true);
		
		saveBtn = new Button("Save");
		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				registerHandlers();
			}
		});
		cancelBtn = new Button("Cancel");
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		btnPanel.add(saveBtn);
		btnPanel.add(cancelBtn);
		
		container.add(desc);
		container.add(reporterChoices);
		container.add(btnPanel);
		setWidget(container);
	}
	
	private void loadListBox() {
		String clsName = event.getClassName();
		desc.setText("Select all servers that should be notified of this event [" + clsName.substring(clsName.lastIndexOf('.') + 1) + "]:");
		reporterChoices.clear();
		ReporterServiceAsync rpc = (ReporterServiceAsync)GWT.create(ReporterService.class);
		rpc.getAllReporters(new AsyncCallback<List<NotificationServerSettings>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NotificationServerSettings> result) {
				reporters = result;
				for(int i = 0; i < reporters.size(); ++i) {
					IsDataStoreSerializable obj = reporters.get(i);
					reporterChoices.addItem(obj.toString());
				}
				setSelected();
			}			
		});
	}
	
	private void setEvent(SageEventMetaData event) {
		this.event = event;
	}
	
	private void setSelected() {
		if(event == null)
			return;
		
		HandlerServiceAsync rpc = (HandlerServiceAsync)GWT.create(HandlerService.class);
		rpc.getHandlers(event, new AsyncCallback<List<NotificationServerSettings>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NotificationServerSettings> result) {
				for(int i = 0; i < reporters.size(); ++i) {
					reporterChoices.setItemSelected(i, false);
					for(IsDataStoreSerializable obj : result) {
						if(obj.equals(reporters.get(i))) {
							reporterChoices.setItemSelected(i, true);
							break;
						}
					}
				}
			}
		});
	}
	
	private void registerHandlers() {
		if(event == null)
			return;
		
		List<NotificationServerSettings> handlers = new ArrayList<NotificationServerSettings>();
		for(int i = 0; i < reporterChoices.getItemCount(); ++i)
			if(reporterChoices.isItemSelected(i))
				handlers.add(reporters.get(i));
		HandlerServiceAsync rpc = (HandlerServiceAsync)GWT.create(HandlerService.class);
		rpc.setHandlers(event, handlers, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(Void result) {
				hide();
			}			
		});
	}
}
