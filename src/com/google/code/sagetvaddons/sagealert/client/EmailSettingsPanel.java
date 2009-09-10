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
import java.util.List;

import com.google.code.gwtsrwc.client.ValidatedIntegerTextBox;
import com.google.code.gwtsrwc.client.ValidatedTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * GUI panel for configuring email settings
 * @author dbattams
 * @version $Id$
 */
final class EmailSettingsPanel extends VerticalPanel {
	static private final EmailSettingsPanel INSTANCE = new EmailSettingsPanel();
	static final EmailSettingsPanel getInstance() { return INSTANCE; }
	
	private FlexTable smtpTbl;
	private TextBox smtpHost, smtpUser, smtpPassword, smtpFromAddr;
	private ValidatedTextBox smtpPort;
	private CheckBox smtpUseSsl;
	private HorizontalPanel smtpToolbar;
	
	private FlexTable emails;
	private HorizontalPanel emailsToolbar;
	
	private Collection<EmailSettings> deleted;
	
	private EmailSettingsPanel() {
		deleted = new ArrayList<EmailSettings>();
		smtpTbl = new FlexTable();
		smtpToolbar = new HorizontalPanel();
		Button saveBtn = new Button("Save");
		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(smtpHost.getValue().length() == 0) {
					Window.alert("You must define an SMTP host!");
					return;
				}
				
				int port = 25;
				if(smtpPort.getValue().length() > 0 && smtpPort.isValueValid())
					port = Integer.parseInt(smtpPort.getValue());
				
				SettingsServiceAsync rpc = GWT.create(SettingsService.class);
				rpc.saveSmtpSettings(new SmtpSettings(smtpHost.getValue(), port, smtpUser.getValue(), smtpPassword.getValue(), smtpFromAddr.getValue(), smtpUseSsl.getValue()), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(Void result) {

					}
				});
			}
		});
		smtpToolbar.add(saveBtn);
		
		smtpTbl.setText(0, 0, "Host:");
		smtpTbl.setWidget(0, 1, smtpHost = new TextBox());
		smtpTbl.setText(0, 2, "Port:");
		smtpTbl.setWidget(0, 3, smtpPort = new ValidatedIntegerTextBox(1, 65000));
		smtpTbl.setText(1, 0, "User:");
		smtpTbl.setWidget(1, 1, smtpUser = new TextBox());
		smtpTbl.setText(1, 2, "Password:");
		smtpTbl.setWidget(1, 3, smtpPassword = new PasswordTextBox());
		smtpTbl.setText(2, 0, "From Address:");
		smtpTbl.setWidget(2, 1, smtpFromAddr = new TextBox());
		smtpTbl.setText(2, 2, "Use SSL:");
		smtpTbl.setWidget(2, 3, smtpUseSsl = new CheckBox());
		
		emails = new FlexTable();
		emails.setText(0, 0, "Email Address");
		emails.setText(0, 1, "Notification Type");
		emails.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HTMLTable.Cell cell = emails.getCellForEvent(event);
				if(cell.getCellIndex() != 2)
					return;
				
				int row = cell.getRowIndex();
				String addr = ((TextBox)emails.getWidget(row, 0)).getValue();
				ListBox box = ((ListBox)emails.getWidget(row, 1));
				String type = box.getValue(box.getSelectedIndex());
				deleted.add(new EmailSettings(addr, type));
				emails.removeRow(row);
			}
		});

		emailsToolbar = new HorizontalPanel();
		Button emailsBtn = new Button("Add Email");
		emailsBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//addRow();
			}
		});
		
		Button addRow = new Button("Add Email");
		addRow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addRow(null, null);
			}
		});
		emailsToolbar.add(addRow);
		
		Button emailSave = new Button("Save");
		emailSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ReporterServiceAsync rpc = GWT.create(ReporterService.class);
				if(deleted.size() > 0) {
					rpc.delete(new ArrayList<NotificationServerSettings>(deleted), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getLocalizedMessage());
						}

						@Override
						public void onSuccess(Void result) {
							deleted.clear();
							saveEmails();
						}						
					});
				} else
					saveEmails();
			}			
		});
		emailsToolbar.add(emailSave);
		
		setSpacing(10);
		add(new Label("SMTP (Outgoing) Server Settings"));
		add(smtpTbl);
		add(smtpToolbar);
		add(new Label("Email addresses below can be attached to the various alerts."));
		add(new Label("If you're sending your email alerts to an email to SMS gateway in order to receive alerts via SMS, you will want to pick medium or short message types.  If you're sending alerts to an actual email account then you can choose long."));
		add(emails);
		add(emailsToolbar);
	}
	
	private void addRow(String email, String type) {
		int newRow = emails.insertRow(emails.getRowCount());
		TextBox addr = new TextBox();
		ListBox list = getListBox();
		emails.setWidget(newRow, 0, addr);
		emails.setWidget(newRow, 1, list);
		Label del = new Label("Delete");
		del.addStyleName("sageHyperlink");
		emails.setWidget(newRow, 2, del);
		
		if(email != null)
			addr.setValue(email);
		if(type != null) {
			if(type.equals("short"))
				list.setSelectedIndex(0);
			else if(type.equals("medium"))
				list.setSelectedIndex(1);
			else
				list.setSelectedIndex(2);
		}
	}

	private ListBox getListBox() {
		ListBox box = new ListBox();
		box.addItem("Short", "short");
		box.addItem("Medium", "medium");
		box.addItem("Long", "long");
		return box;
	}
	
	@Override
	protected void onLoad() {
		loadSmtpSettings();
		loadEmails();
	}
	
	private void loadSmtpSettings() {
		SettingsServiceAsync rpc = GWT.create(SettingsService.class);
		rpc.getSmtpSettings(new AsyncCallback<SmtpSettings>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(SmtpSettings result) {
				if(result == null)
					return;
				smtpHost.setValue(result.getHost());
				smtpPort.setValue(Integer.toString(result.getPort()));
				smtpUser.setValue(result.getUser());
				smtpPassword.setValue(result.getPwd());
				smtpFromAddr.setValue(result.getSenderAddress());
				smtpUseSsl.setValue(result.useSsl());
			}
		});
	}
	
	private void loadEmails() {
		while(emails.getRowCount() > 1)
			emails.removeRow(1);
		
		ReporterServiceAsync rpc = GWT.create(ReporterService.class);
		rpc.getReporters(EmailSettings.class.getName(), new AsyncCallback<List<NotificationServerSettings>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<NotificationServerSettings> result) {
				for(NotificationServerSettings n : result) {
					EmailSettings e = (EmailSettings)n;
					addRow(e.getAddress(), e.getMsgType());
				}
			}
		});
	}
	
	private void saveEmails() {
		Collection<EmailSettings> list = new ArrayList<EmailSettings>();
		for(int i = 1; i < emails.getRowCount(); ++i) {
			String addr = ((TextBox)emails.getWidget(i, 0)).getValue();
			ListBox box = (ListBox)emails.getWidget(i, 1);
			String type = box.getValue(box.getSelectedIndex());
			if(addr.length() > 0)
				list.add(new EmailSettings(addr, type));
		}
		if(list.size() > 0) {
			ReporterServiceAsync rpc = GWT.create(ReporterService.class);
			rpc.save(new ArrayList<NotificationServerSettings>(list), new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(Void result) {
					loadEmails();
				}
			});
		}
	}
}
