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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author dbattams
 *
 */
final class SageAlertToolBar extends ToolBar {

	private Button donate;
	
	SageAlertToolBar() {
		donate = new Button("Donate");
		donate.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getDonateImg()));
		donate.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm("Donations", "Donations are used for the sole purpose of defraying out of pocket expenses for developing my plugins.  These expenses include the purchase of addtional SageTV server licenses and hardware used to create the development environment used solely for the purposes of plugin development.  Donations are appreciated, but not required.  This software is fully functional with or without a donation.  Do you wish to donate?", new Listener<MessageBoxEvent>() {

					public void handleEvent(MessageBoxEvent be) {
						if(be.getButtonClicked().getText().toUpperCase().equals("YES")) {
							MessageBox.alert("Taking You to PayPal", "If a new window directing you to PayPal does not appear then you may need to disable popup blockers in your browser.", new Listener<MessageBoxEvent>() {

								public void handleEvent(MessageBoxEvent be) {
									Window.open("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AQV6A8JXY7XRJ", "_blank", null);						
								}
								
							});
						}
					}
					
				});
			}
			
		});
		add(donate);
	}
}
