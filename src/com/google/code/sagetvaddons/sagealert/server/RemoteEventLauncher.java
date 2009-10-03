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

import org.apache.log4j.Logger;


/**
 * The class that implements the various RPC calls available through the RPC event firing interface
 * @author dbattams
 * @version $Id$
 */
public final class RemoteEventLauncher {
	static private final Logger LOG = Logger.getLogger(RemoteEventLauncher.class);
	
	/**
	 * Fire a RemoteEvent type event
	 * @param title The title/subject of the event
	 * @param desc The textual description of the event (the event's body)
	 * @return True on success or false if the event could not be fired by SageAlert
	 */
	public boolean fireInfo(String title, String desc) {
		String clientIpAddr = XmlRpcServletWithClientIpAddr.getRequesterIpAddr();
		SageEventHandlerManager.getInstance().fire(new RemoteInfoEvent(title, desc, clientIpAddr));
		LOG.info("Remote info event '" + title + "' fired by " + clientIpAddr);
		return true;
	}
	
	public boolean fireWarning(String title, String desc) {
		String clientIpAddr = XmlRpcServletWithClientIpAddr.getRequesterIpAddr();
		SageEventHandlerManager.getInstance().fire(new RemoteWarningEvent(title, desc, clientIpAddr));
		LOG.info("Remote warning event '" + title + "' fired by " + clientIpAddr);
		return true;		
	}
	
	public boolean fireError(String title, String desc) {
		String clientIpAddr = XmlRpcServletWithClientIpAddr.getRequesterIpAddr();
		SageEventHandlerManager.getInstance().fire(new RemoteErrorEvent(title, desc, clientIpAddr));
		LOG.info("Remote error event '" + title + "' fired by " + clientIpAddr);
		return true;		
	}
}
