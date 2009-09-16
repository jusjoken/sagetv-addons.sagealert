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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A popup widget used to attach listeners to a specific event
 * @author dbattams
 * @version $Id$
 */
final class EventConfigurator extends SimplePanel {
	
	private SageEventMetaData event;
	
	private List<CheckBox> reporterChoices;
	
	private List<NotificationServerSettings> reporters; 
	private VerticalPanel container;
	private FlexTable grid;
	
	EventConfigurator(SageEventMetaData event) {
		this.event = event;
		DisclosurePanel disPanel = new DisclosurePanel(event.getEventTitle(), false);
		grid = new FlexTable();
		container = new VerticalPanel();
		container.add(new Label(event.getEventDescription()));
		container.add(new Label("Select all the servers that should be notified of this event."));
		
		reporterChoices = new ArrayList<CheckBox>();
		
		
		container.add(grid);
		disPanel.add(container);
		setWidget(disPanel);
		loadReporters();
	}

	void refresh() {
		loadReporters();
	}
	
	private void loadReporters() {
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
				reporterChoices.clear();
				grid.clear();
				CheckBox b;
				for(int i = 0; i < reporters.size(); ++i) {
					reporterChoices.add(b = new CheckBox(reporters.get(i).toString()));
					grid.setWidget(i / 2, i % 2, b);
				}
				setSelected();
			}			
		});
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
				for(int i = 0; i < result.size(); ++i) {
					for(int j = 0; j < reporters.size(); ++j) {
						if(reporters.get(j).equals(result.get(i))) {
							reporterChoices.get(j).setValue(true);
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
		for(int i = 0; i < reporterChoices.size(); ++i)
			if(reporterChoices.get(i).getValue())
				handlers.add(reporters.get(i));
		HandlerServiceAsync rpc = (HandlerServiceAsync)GWT.create(HandlerService.class);
		rpc.setHandlers(event, handlers, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(Void result) {
				
			}			
		});
	}
	
	void save() {
		registerHandlers();
	}
}
