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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Widget to register/configure Growl instances
 * @author dbattams
 * @version $Id$
 */
final class GrowlSettingsPanel extends VerticalPanel {
	static private final GrowlSettingsPanel INSTANCE = new GrowlSettingsPanel();
	static final GrowlSettingsPanel getInstance() { return INSTANCE; }
	
	private FlexTable grid;
	private HorizontalPanel btnContainer;
	private Button saveBtn;
	private Button addBtn;
	
	private List<GrowlServerSettings> deleted;
	
	private GrowlSettingsPanel() {
		deleted = new ArrayList<GrowlServerSettings>();
		grid = new FlexTable();
		grid.setWidth("100%");
		grid.setText(0, 0, "Host");
		grid.setText(0, 1, "Password");
		grid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HTMLTable.Cell clicked = ((HTMLTable)event.getSource()).getCellForEvent(event);
				if(clicked == null || clicked.getCellIndex() != 2)
					return;
				
				int row = clicked.getRowIndex();
				String host = ((TextBox)grid.getWidget(row, 0)).getValue();
				String pwd  = ((TextBox)grid.getWidget(row, 1)).getValue();
				deleted.add(new GrowlServerSettings(host, pwd));
				grid.removeRow(row);
			}
		});
		
		btnContainer = new HorizontalPanel();
		btnContainer.setSpacing(8);
		
		addBtn = new Button("Add Growl Server");
		addBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int newRow = grid.insertRow(grid.getRowCount());
				grid.setWidget(newRow, 0, new TextBox());
				grid.setWidget(newRow, 1, new PasswordTextBox());
				grid.setText(newRow, 2, "Delete");
				((TextBox)grid.getWidget(newRow, 0)).setFocus(true);
			}
		});
		btnContainer.add(addBtn);
		
		saveBtn = new Button("Save");
		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final List<GrowlServerSettings> settings = new ArrayList<GrowlServerSettings>();
				for(int i = 1; i < grid.getRowCount(); ++i) {
					TextBox host = (TextBox)grid.getWidget(i, 0);
					TextBox pwd  = (TextBox)grid.getWidget(i, 1);
					if(host.getValue().length() > 0)
						settings.add(new GrowlServerSettings(host.getValue(), pwd.getValue()));
				}
				ReporterServiceAsync rpc = GWT.create(ReporterService.class);
				if(deleted.size() > 0)
					rpc.delete(new ArrayList<NotificationServerSettings>(deleted), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getLocalizedMessage());
						}

						@Override
						public void onSuccess(Void result) {
							deleted.clear();
							rpcSave(settings);
						}
					});
				else
					rpcSave(settings);
			}
		});
		btnContainer.add(saveBtn);
		
		Button cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleted.clear();
				loadGrid();
			}
		});
		btnContainer.add(cancel);
		
		setWidth("100%");
		add(grid);
		add(btnContainer);
	}
	
	@Override
	public void onLoad() {
		loadGrid();
	}
	
	private void rpcSave(List<GrowlServerSettings> settings) {
		ReporterServiceAsync rpc = GWT.create(ReporterService.class);
		rpc.save(new ArrayList<NotificationServerSettings>(settings), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Save failed; reloading old settings...");
				loadGrid();
			}
			
			@Override
			public void onSuccess(Void result) {
				loadGrid();
			}
		});		
	}
	
	private void loadGrid() {
		saveBtn.setEnabled(false);
		while(grid.getRowCount() != 1)
			grid.removeRow(1);
		ReporterServiceAsync rpc = GWT.create(ReporterService.class);
		rpc.getReporters(GrowlServerSettings.class.getName(), new AsyncCallback<List<NotificationServerSettings>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NotificationServerSettings> result) {
				for(NotificationServerSettings n : result) {
					GrowlServerSettings s = (GrowlServerSettings)n;
					TextBox host = new TextBox();
					host.setValue(s.getHost());
					host.setEnabled(false);
					PasswordTextBox pwd = new PasswordTextBox();
					pwd.setValue(s.getPassword());
					pwd.setEnabled(false);
					int rowId = grid.insertRow(grid.getRowCount());
					grid.setWidget(rowId, 0, host);
					grid.setWidget(rowId, 1, pwd);
					Label delLbl = new Label("Delete");
					delLbl.addStyleName("sageHyperlink");
					grid.setWidget(rowId, 2, delLbl);
				}
				saveBtn.setEnabled(true);
			}
		});				
	}
}
