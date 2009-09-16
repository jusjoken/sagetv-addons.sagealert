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

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Widget for configuring event recipients.
 * @author dbattams
 * @version $Id$
 */
final class AlertSettingsPanel extends VerticalPanel {
	static private final AlertSettingsPanel INSTANCE = new AlertSettingsPanel();
	/**
	 * Return the singleton instance of the alert settings panel widget.
	 * @return The single instance, properly initialized
	 */
	static final AlertSettingsPanel getInstance() { return INSTANCE; }
	
	private SageEventMetaData[] events;
	private EventConfigurator[] panels;
	private HorizontalPanel toolbar;
	
	private AlertSettingsPanel() {
		setSize("100%", "100%");
		toolbar = new HorizontalPanel();
		Button save = new Button("Save");
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for(EventConfigurator e : panels)
					e.save();
			}
		});
		toolbar.add(save);
		Button reset = new Button("Reset");
		reset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for(EventConfigurator e : panels)
					e.refresh();
			}
		});
		toolbar.add(reset);
		loadEvents();
	}
	
	private void loadEvents() {
		checkForReporters();
		HandlerServiceAsync rpc = GWT.create(HandlerService.class);
		rpc.getEventMetaData(new AsyncCallback<Collection<SageEventMetaData>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}
			
			@Override
			public void onSuccess(Collection<SageEventMetaData> result) {
				events = new SageEventMetaData[result.size()];
				panels = new EventConfigurator[result.size()];
				int i = 0;
				for(SageEventMetaData e : result) {
					events[i] = e;
					panels[i] = new EventConfigurator(e);
					add(panels[i++]);
				}
				add(toolbar);
			}
		});
	}
	
	void refresh() {
		checkForReporters();
		for(EventConfigurator e : panels)
			e.refresh();
	}
	
	void checkForReporters() {
		ReporterServiceAsync reporters = GWT.create(ReporterService.class);
		reporters.getAllReporters(new AsyncCallback<List<NotificationServerSettings>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NotificationServerSettings> result) {
				if(result.size() == 0) {
					final PopupPanel welcome = new PopupPanel(false);
					welcome.setModal(true);
					welcome.setSize("320px", "240px");
					VerticalPanel p = new VerticalPanel();
					p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					p.add(new HTML("<b>Welcome to SageAlert!</b>"));
					Label l = new Label("You must register one or more notification servers.  Please register a server on the Twitter, Growl or Email tabs.");
					l.setWordWrap(true);
					p.add(l);
					Button b = new Button("OK");
					b.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							welcome.hide();
							SageAlert.getInstance().goToServerTab();
						}
					});
					p.add(b);
					welcome.setWidget(p);
					welcome.center();
				}
			}
		});
	}
}
