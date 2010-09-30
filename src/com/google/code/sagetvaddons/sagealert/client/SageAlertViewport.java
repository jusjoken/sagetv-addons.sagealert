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

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dbattams
 *
 */
final class SageAlertViewport extends Viewport {
	static private final SageAlertViewport INSTANCE = new SageAlertViewport();
	static final SageAlertViewport get() { return INSTANCE; }
	private Window mainWindow;
	private ContentPanel centerPanel;
	private ContentPanel westPanel;
	
	/**
	 * 
	 */
	private SageAlertViewport() {
				
		westPanel = new ContentPanel();
		westPanel.setScrollMode(Scroll.AUTOY);
		BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 280);
		westPanel.add(new MenuTreePanel(new MenuDataStore()));
		westPanel.setHeaderVisible(false);
		westPanel.setBottomComponent(new SageAlertToolBar());
					
		centerPanel = new ContentPanel();
		centerPanel.setLayout(new FitLayout());
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerPanel.setHeaderVisible(false);

		
		mainWindow = new Window();
		mainWindow.setDraggable(false);
		mainWindow.setHeading("SageAlert v" + Version.getFullVersion());
		mainWindow.setClosable(false);
		mainWindow.setResizable(false);
		mainWindow.setSize(600, 450);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.add(westPanel, westData);
		mainWindow.add(centerPanel, centerData);
		add(mainWindow);
		SettingsServiceAsync rpc = GWT.create(SettingsService.class);
		rpc.isLicensed(new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				GWT.log("ERROR", caught);
			}

			public void onSuccess(Boolean result) {
				String newHeading = mainWindow.getHeading();
				if(!result)
					newHeading = newHeading.concat(" (Unlicensed)");
				else
					newHeading = newHeading.concat(" (Licensed)");
				mainWindow.setHeading(newHeading);
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Viewport#onRender(com.google.gwt.user.client.Element, int)
	 */
	@Override
	public void onAttach() {
		super.onAttach();
		mainWindow.setSize("96%", "96%");
		mainWindow.center();
	}
	
	public void setCenterContent(Widget w) {
		centerPanel.removeAll();
		if(w != null)
			centerPanel.add(w);
		centerPanel.layout();
	}
	
	public void refreshTree() {
		westPanel.removeAll();
		westPanel.add(new MenuTreePanel(new MenuDataStore()));
		westPanel.layout();
	}

}
