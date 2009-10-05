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
package com.google.code.sagetvaddons.sagealert.server;

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.SystemMessageAPI;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.UserSettings;

/**
 * A thread that monitors SageTV system messages and fires SystemMessageEvents when a new system messages are generated
 * @author dbattams
 * @version $Id$
 */
final class SystemMessageMonitor extends SageAlertRunnable {

	static private final Logger LOG = Logger.getLogger(SystemMessageMonitor.class);
	static private final String SYSMSG_LAST_NOTICE = "SysMsgLastNotice";

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("Thread initialized.");
		DataStore store = DataStore.getInstance();
		while(keepAlive()) {
			LOG.info("Thread started.");
			SystemMessageAPI.List msgs = API.apiNullUI.systemMessageAPI.GetSystemMessages();
			long lastNotice = Long.parseLong(store.getSetting(SYSMSG_LAST_NOTICE, "0"));
			long newLastNotice = 0;
			SageEventHandlerManager mgr = SageEventHandlerManager.getInstance();
			for(SystemMessageAPI.SystemMessage msg : msgs)
				if(msg.GetSystemMessageLevel() > 0 && msg.GetSystemMessageTime() > lastNotice) {
					mgr.fire(new SystemMessageEvent(msg));
					switch(msg.GetSystemMessageLevel()) {
					case 1:	mgr.fire(new InfoSystemMessageEvent(msg)); break;
					case 2: mgr.fire(new WarningSystemMessageEvent(msg)); break;
					case 3: mgr.fire(new ErrorSystemMessageEvent(msg)); break;
					}
					if(msg.GetSystemMessageTime() > newLastNotice)
						newLastNotice = msg.GetSystemMessageTime();
					LOG.info("Firing system message event for '" + msg.GetSystemMessageTypeName() + "'");
				}
			if(newLastNotice != 0)
				store.setSetting(SYSMSG_LAST_NOTICE, Long.toString(newLastNotice));
			LOG.info("Thread sleeping.");
			try{
				Thread.sleep(Long.parseLong(store.getSetting(UserSettings.SYSMSG_MONITOR_SLEEP, UserSettings.SYSMSG_MONITOR_SLEEP_DEFAULT)));
			} catch(InterruptedException e) {
				LOG.info("Thread awakened early.");
			}
		}
		LOG.info("Thread destroyed.");
	}
}