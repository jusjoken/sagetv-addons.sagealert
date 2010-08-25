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
package com.google.code.sagetvaddons.sagealert.server;

import gkusnick.sagetv.api.API;
import gkusnick.sagetv.api.SystemMessageAPI;

import java.util.Map;

import org.apache.log4j.Logger;

import sage.SageTVEventListener;

import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageErrorEvent;
import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageInfoEvent;
import com.google.code.sagetvaddons.sagealert.server.events.SystemMessageWarningEvent;

/**
 * @author dbattams
 *
 */
final class SystemMessageEventsListener implements SageTVEventListener {
	static private final Logger LOG = Logger.getLogger(SystemMessageEventsListener.class);
	static private final SystemMessageEventsListener INSTANCE = new SystemMessageEventsListener();
	static final SystemMessageEventsListener get() { return INSTANCE; }
	
	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		LOG.info("Event received: " + arg0);
		if(CoreEventsManager.SYSMSG_POSTED.equals(arg0)) {
			SystemMessageAPI.SystemMessage msg = API.apiNullUI.systemMessageAPI.Wrap(arg1.get("SystemMessage"));
			if(msg.GetSystemMessageRepeatCount() > 1 && Boolean.parseBoolean(API.apiNullUI.configuration.GetServerProperty(Plugin.OPT_IGNORE_REPEAT_SYS_MSGS, Plugin.OPT_IGNORE_REPEAT_SYS_MSGS_DEFAULT)))
				LOG.warn("Not firing event for system message '" + msg.GetSystemMessageTypeName() + "' because it is a repeated message!");
			else if(msg.GetSystemMessageLevel() == 1)
				SageEventHandlerManager.get().fire(new SystemMessageInfoEvent(msg));
			else if(msg.GetSystemMessageLevel() == 2)
				SageEventHandlerManager.get().fire(new SystemMessageWarningEvent(msg));
			else if(msg.GetSystemMessageLevel() == 3)
				SageEventHandlerManager.get().fire(new SystemMessageErrorEvent(msg));
		} else
			LOG.error("Unhandled event: " + arg0);
	}
}
