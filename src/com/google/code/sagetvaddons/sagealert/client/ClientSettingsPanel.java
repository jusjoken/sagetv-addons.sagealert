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
import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A GUI widget for modifying client alias and notification settings
 * @author dbattams
 * @version $Id$
 */
final class ClientSettingsPanel extends VerticalPanel {
	static private final ClientSettingsPanel INSTANCE = new ClientSettingsPanel();
	static final ClientSettingsPanel getInstance() { return INSTANCE; }
	
	private FlexTable tbl;	
	
	/**
	 * Constructor
	 */
	ClientSettingsPanel() {
		setSize("100%", "100%");
		
		tbl = new FlexTable();
		tbl.setText(0, 0, "Client ID");
		tbl.setText(0, 1, "Alias");
		tbl.setText(0, 2, "Enable Viewing Notifications?");
		
		
		Button save = new Button("Save Alias Changes");
		save.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				final Collection<Client> saved = new ArrayList<Client>();
				for(int i = 1; i < tbl.getRowCount(); ++i) {
					String id = ((TextBox)tbl.getWidget(i, 0)).getValue();
					if(id.length() > 0)
						saved.add(new Client(id, ((TextBox)tbl.getWidget(i, 1)).getValue(), ((CheckBox)tbl.getWidget(i, 2)).getValue()));
				}
				saveClients(saved);
			}
		});
		
		Button reset = new Button("Cancel");
		reset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadClients();
			}
		});
		
		loadClients();
		
		add(new Label("SageAlert cannot send viewing notifications for remote PC clients (those listed by IP address here), regardless if the checkbox is checked or not."));
		add(new Label("Clients/extenders/placeshifters are added to this table the first time they connect to this instance of SageAlert (click the tab or cancel button to force refresh of tab)."));
		add(tbl);
		
		HorizontalPanel toolbar = new HorizontalPanel();
		toolbar.setSpacing(5);
		toolbar.add(save);
		toolbar.add(reset);
		setSpacing(8);
		add(toolbar);
	}
	
	private void saveClients(final Collection<Client> clients) {
		ClientServiceAsync rpc = GWT.create(ClientService.class);
		for(Client c : clients)
			rpc.saveClient(c, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(Void result) {
					
				}
			});
		DeferredCommand.addCommand(new Command() {
			@Override
			public void execute() {
				if(clients.size() > 0)
					loadClients();
			}
		});
	}
	
	private void loadClients() {
		ClientServiceAsync rpc = GWT.create(ClientService.class);
		rpc.getClients(new AsyncCallback<Collection<Client>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(Collection<Client> result) {
				while(tbl.getRowCount() > 1)
					tbl.removeRow(1);
				
				for(Client c : result)
					addRow(c.getId(), c.getAlias(), c.doNotify());
			}
		});
	}
	
	/**
	 * Reload the registered clients on the panel
	 */
	public void refresh() {
		loadClients();
	}
	
	private void addRow(String idVal, String aliasVal, boolean doNotify) {
		int newRow = tbl.insertRow(tbl.getRowCount());
		
		TextBox id = new TextBox();
		id.setValue(idVal);
		if(idVal != null && idVal.length() > 0)
			id.setEnabled(false);
		
		TextBox alias = new TextBox();
		alias.setValue(aliasVal);
		
		tbl.setWidget(newRow, 0, id);
		tbl.setWidget(newRow, 1, alias);
		CheckBox box = new CheckBox();
		box.setValue(doNotify, false);
		tbl.setWidget(newRow, 2, box);
	}
}
