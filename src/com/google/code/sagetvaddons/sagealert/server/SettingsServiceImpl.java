/*
 *      Copyright 2009-2011 Battams, Derek
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
package com.google.code.sagetvaddons.sagealert.server;

import com.google.code.sagetvaddons.license.License;
import com.google.code.sagetvaddons.sagealert.plugin.Plugin;
import com.google.code.sagetvaddons.sagealert.server.events.SmtpTestEvent;
import com.google.code.sagetvaddons.sagealert.shared.EmailSettings;
import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SmtpSettings;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server side implementation for the SettingsService RPC interface
 * @author dbattams
 * @version $Id$
 */
@SuppressWarnings("serial")
public final class SettingsServiceImpl extends RemoteServiceServlet implements SettingsService {

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.SettingsService#getSetting(java.lang.String, java.lang.String)
	 */
	public String getSetting(String var, String defaultVal) {
		if(!var.equals(Plugin.OPT_IGNORE_REPEAT_SYS_MSGS))
			return DataStore.getInstance().getSetting(var, defaultVal);
		else
			return Plugin.INSTANCE.getConfigValue(Plugin.OPT_IGNORE_REPEAT_SYS_MSGS);
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.client.SettingsService#setSetting(java.lang.String, java.lang.String)
	 */
	public void setSetting(String var, String val) {
		if(!var.equals(Plugin.OPT_IGNORE_REPEAT_SYS_MSGS))
			DataStore.getInstance().setSetting(var, val);
		else
			Plugin.INSTANCE.setConfigValue(var, val);
	}

	public SmtpSettings getSmtpSettings() {
		return DataStore.getInstance().getSmtpSettings();
	}

	public void saveSmtpSettings(SmtpSettings settings) {
		DataStore.getInstance().saveSmtpSettings(settings);
	}

	public void testSmtpSettings(String addr, SmtpSettings smtpSettings) {
		EmailSettings msgSettings = new EmailSettings(addr, "Long");
		EmailServer srv = new EmailServer(msgSettings, smtpSettings);
		srv.onEvent(new SmtpTestEvent(), false);
	}

	public boolean isLicensed() {
		return License.isLicensed(Plugin.PLUGIN_ID).isLicensed();
	}

	public void clearSettingsStartingWith(String prefix) {
		DataStore.getInstance().clearSettingsStartingWith(prefix);
	}
	
	public void clearSettingsEndingWith(String suffix) {
		DataStore.getInstance().clearSettingsEndingWith(suffix);
	}

	public void resetAllAlertMessages() {
		DataStore.getInstance().resetAllAlertMessages();
		CoreEventsManager.get().init();
		new CustomEventLoader().run();
	}
}
