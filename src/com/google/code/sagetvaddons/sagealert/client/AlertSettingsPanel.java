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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
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
	
	private FlexTable grid;
	private SageEventMetaData[] events;
	
	private AlertSettingsPanel() {
		setSize("100%", "100%");
		
		grid = new FlexTable();
		grid.setSize("100%", "100%");
		grid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HTMLTable tbl = (HTMLTable)event.getSource();
				HTMLTable.Cell cell = tbl.getCellForEvent(event);
				if(cell == null || cell.getCellIndex() != 1)
					return;				
				EventConfigurator.getInstance(events[cell.getRowIndex()]).center();
			}
		});
		
		
		HTMLTable.ColumnFormatter cFmt = grid.getColumnFormatter();
		cFmt.setWidth(1, "15%");
		
		loadEvents();
		add(grid);
	}
	
	private void loadEvents() {
		HandlerServiceAsync rpc = GWT.create(HandlerService.class);
		rpc.getEventMetaData(new AsyncCallback<Collection<SageEventMetaData>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}
			
			@Override
			public void onSuccess(Collection<SageEventMetaData> result) {
				events = new SageEventMetaData[result.size()];
				int i = 0;
				HTMLTable.RowFormatter rFmt = grid.getRowFormatter();
				for(SageEventMetaData e : result) {
					int newRow = grid.insertRow(i);
					grid.setText(newRow, 0, e.getEventDescription());
					Label configLink = new Label("Configure");
					configLink.addStyleName("sageHyperlink");
					grid.setWidget(newRow, 1, configLink);
					if(i % 2 == 1)
						rFmt.addStyleName(i, "sageOddRow");
					events[i++] = e;
				}
			}
		});
	}
}
