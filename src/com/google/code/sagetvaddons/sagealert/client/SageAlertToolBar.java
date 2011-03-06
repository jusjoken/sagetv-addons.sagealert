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

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author dbattams
 *
 */
final class SageAlertToolBar extends ToolBar {

	static final private class UrlLauncher {
		static final void launch(String url) {
			Window.open(url, "_blank", null);
		}
	}

	static private final class SettingsMenu extends Menu {
		private MenuItem smtp;
		private MenuItem pref;
		private MenuItem reset;
		
		private SettingsMenu() {
			smtp = new MenuItem("SMTP Settings");
			smtp.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getEmailImg()));
			smtp.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					SageAlertViewport.get().setCenterContent(SmtpSettingsPanel.get());
				}
				
			});
			
			pref = new MenuItem("Preferences");
			pref.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getPrefsImg()));
			pref.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					SageAlertViewport.get().setCenterContent(PreferencesPanel.get());
				}
				
			});
			
			reset = new MenuItem("Reset ALL Messages");
			reset.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					MessageBox.confirm("Reset All Messages?", "Are you sure you want to reset ALL messages for ALL alerts back to the default value?", new Listener<MessageBoxEvent>() {

						public void handleEvent(MessageBoxEvent be) {
							if(be.getButtonClicked().getText().toUpperCase().equals("YES")) {
								SettingsServiceAsync rpc = GWT.create(SettingsService.class);
								rpc.resetAllAlertMessages(new AsyncCallback<Void>() {

									public void onFailure(Throwable caught) {
										MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
									}

									public void onSuccess(Void result) {
										MessageBox.alert("Success", "All messages have been reset!", null);
										SageAlertViewport view = SageAlertViewport.get();
										view.refreshTree();
										view.setCenterContent(null);
									}
									
								});
							}
						}
						
					});
				}
				
			});
			
			add(smtp);
			add(pref);
			add(reset);
		}
	}
	
	static private final class RegisterMenu extends Menu {
		private MenuItem donate;
		private MenuItem reg;
		private MenuItem faq;
		
		private RegisterMenu() {
			donate = new MenuItem("Donate (Obtain License)");
			donate.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getDonateImg()));
			donate.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					MessageBox.confirm("Donations", "Donations are used for the sole purpose of defraying out of pocket expenses for developing my plugins.  These expenses include the purchase of addtional SageTV server licenses and hardware used to create the development environment used solely for the purposes of plugin development.  Donators will receive a license file that unlocks all features of this plugin.  Do you wish to donate?", new Listener<MessageBoxEvent>() {

						public void handleEvent(MessageBoxEvent be) {
							if(be.getButtonClicked().getText().toUpperCase().equals("YES")) {
								MessageBox.alert("Taking You to PayPal", "If a new window directing you to PayPal does not appear then you may need to disable popup blockers in your browser.", new Listener<MessageBoxEvent>() {

									public void handleEvent(MessageBoxEvent be) {
										UrlLauncher.launch("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AQV6A8JXY7XRJ");						
									}
									
								});
							}
						}
						
					});
				}
				
			});
			
			faq = new MenuItem("Donations FAQ");
			faq.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getFaqImg()));
			faq.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					UrlLauncher.launch("http://code.google.com/p/sagetv-addons/wiki/DonationsFaq");					
				}
				
			});
			
			reg = new MenuItem("Register License");
			reg.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getRegisterImg()));
			reg.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					MessageBox.alert("Configure License on Server", "Licensing is now handled via sagetv-addons license server plugin.", null);
				}
				
			});
			
			add(donate);
			add(reg);
			add(faq);
		}
	}
	
	static private final class HelpMenu extends Menu {
		private MenuItem about;
		private MenuItem guide;
		private MenuItem support;
		private MenuItem tix;
		private MenuItem home;
		
		private HelpMenu() {
			SageAlertClientBundle bundle = SageAlertClientBundle.INSTANCE;
			
			about = new MenuItem("About");
			about.setIcon(AbstractImagePrototype.create(bundle.getAboutImg()));
			about.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					new AboutWindow().show();					
				}
				
			});
			
			guide = new MenuItem("User Guide");
			guide.setIcon(AbstractImagePrototype.create(bundle.getDocsImg()));
			guide.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					UrlLauncher.launch("http://code.google.com/p/sagetv-addons/wiki/SageAlertUserGuide");					
				}
				
			});
			
			support = new MenuItem("Support Forum");
			support.setIcon(AbstractImagePrototype.create(bundle.getSupportImg()));
			support.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					UrlLauncher.launch("http://forums.sagetv.com/forums/showthread.php?t=48574");					
				}
				
			});
			
			tix = new MenuItem("Feature/Bug Request");
			tix.setIcon(AbstractImagePrototype.create(bundle.getTixImg()));
			tix.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					UrlLauncher.launch("http://code.google.com/p/sagetv-addons/issues/list");					
				}
				
			});
			
			home = new MenuItem("Project Home");
			home.setIcon(AbstractImagePrototype.create(bundle.getHomeImg()));
			home.addSelectionListener(new SelectionListener<MenuEvent>() {

				@Override
				public void componentSelected(MenuEvent ce) {
					UrlLauncher.launch("http://sagetv-addons.googlecode.com");
				}
				
			});
			
			add(home);
			add(guide);
			add(support);
			add(tix);
			add(about);

		}
	}
	
	private Button register;
	private Button settings;
	private Button help;
	
	SageAlertToolBar() {
		register = new Button("Register");
		register.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getRegisterImg()));
		register.setMenu(new RegisterMenu());
		
		settings = new Button("Settings");
		settings.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getSettingsImg()));
		settings.setMenu(new SettingsMenu());
		
		help = new Button("Help");
		help.setIcon(AbstractImagePrototype.create(SageAlertClientBundle.INSTANCE.getHelpImg()));
		help.setMenu(new HelpMenu());
		
		add(register);
		add(settings);
		add(help);
	}
}
