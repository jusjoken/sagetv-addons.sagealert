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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * @version $Id$
 */
public class SageAlert implements EntryPoint {

  /**
   * This is the entry point method.
   * 
   * Build the tab panel and display it on the root panel; the UI for this app is a tab panel
   * The Alerts tab must always remain as the first tab, then Settings second; About tab must always remain last
   */
  public void onModuleLoad() {
	  
	  TabPanel tabs = new TabPanel();
	  tabs.setWidth("640px");
	  tabs.getDeckPanel().setWidth("100%");
	  tabs.add(AlertSettingsPanel.getInstance(), "Alerts");
	  tabs.add(UserSettingsPanel.getInstance(), "Settings");
	  tabs.add(ClientSettingsPanel.getInstance(), "Clients");
	  tabs.add(TwitterSettingsPanel.getInstance(), "Twitter");
	  tabs.add(GrowlSettingsPanel.getInstance(), "Growl");
	  tabs.add(EmailSettingsPanel.getInstance(), "Email");
	  tabs.add(new AboutPanel(), "About");
	  tabs.selectTab(0);
	  tabs.addSelectionHandler(new SelectionHandler<Integer>() {
		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			int tab = event.getSelectedItem();
			if(tab != 2)
				return;
			ClientSettingsPanel.getInstance().refresh();
		}
	  });
	  
	  VerticalPanel holder = new VerticalPanel();
	  holder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	  holder.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
	  holder.setWidth("100%");
	  VerticalPanel imgHolder = new VerticalPanel();
	  imgHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	  imgHolder.add(new Image("gfx/sagetv_logo.jpg"));
	  imgHolder.add(new Label("SageAlert"));
	  holder.add(imgHolder);
	  holder.add(tabs);
	  holder.setSpacing(15);
	  
	  RootPanel.get().add(holder);
  }
}
