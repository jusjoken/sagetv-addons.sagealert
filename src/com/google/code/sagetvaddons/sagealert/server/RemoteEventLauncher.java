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
import org.apache.xmlrpc.XmlRpcException;


/**
 * The class that implements the various RPC calls available through the RPC event firing interface
 * @author dbattams
 * @version $Id$
 */
public final class RemoteEventLauncher {
	static private final int API_VERSION = 0;
	static private final Logger LOG = Logger.getLogger(RemoteEventLauncher.class);
	
	/**
	 * Fire an information level RemoteEvent type event
	 * @param clntApiVersion The API version of the calling client
	 * @param title The title/subject of the event
	 * @param desc The textual description of the event (the event's body)
	 * @return Always returns true; if there was an error with the RPC call then an XmlRpcException is thrown
	 * @throws XmlRpcException If the calling client's API version is not the same as the server's
	 */
	public boolean fireInfo(int clntApiVersion, String title, String desc) throws XmlRpcException {
		validateClientApiVersion(clntApiVersion);
		String clientIpAddr = XmlRpcServletWithClientIpAddr.getRequesterIpAddr();
		SageEventHandlerManager.getInstance().fire(new RemoteInfoEvent(title, desc, clientIpAddr));
		LOG.info("Remote info event '" + title + "' fired by " + clientIpAddr);
		return true;
	}
	
	/**
	 * Fire a warning level RemoteEvent type
	 * @param clntApiVersion The API version of the calling client
	 * @param title The title/subject of the event
	 * @param desc The description of the event
	 * @return Always returns true; if there was an error with the RPC call then an XmlRpcException is thrown
	 * @throws XmlRpcException If the calling client's API version is not the same as the server's
	 */
	public boolean fireWarning(int clntApiVersion, String title, String desc) throws XmlRpcException {
		validateClientApiVersion(clntApiVersion);
		String clientIpAddr = XmlRpcServletWithClientIpAddr.getRequesterIpAddr();
		SageEventHandlerManager.getInstance().fire(new RemoteWarningEvent(title, desc, clientIpAddr));
		LOG.info("Remote warning event '" + title + "' fired by " + clientIpAddr);
		return true;
	}
	
	/**
	 * Fire an error level RemoteEvent type
	 * @param clntApiVersion The API version of the calling client
	 * @param title The title/subject of the event
	 * @param desc The description of the event
	 * @return Always returns true; if there was an error with the RPC call then an XmlRpcException is thrown
	 * @throws XmlRpcException If the calling client's API version is not the same as the server's
	 */
	public boolean fireError(int clntApiVersion, String title, String desc) throws XmlRpcException {
		validateClientApiVersion(clntApiVersion);
		String clientIpAddr = XmlRpcServletWithClientIpAddr.getRequesterIpAddr();
		SageEventHandlerManager.getInstance().fire(new RemoteErrorEvent(title, desc, clientIpAddr));
		LOG.info("Remote error event '" + title + "' fired by " + clientIpAddr);
		return true;
	}
	
	/**
	 * Return the server's API version
	 * @return The server's API version
	 */
	public int checkApiVersion() {
		return API_VERSION;
	}
	
	// Simple validation of the client and server API versions (make sure they're the same)
	private void validateClientApiVersion(int clntApiVersion) throws XmlRpcException {
		if(clntApiVersion != API_VERSION)
			throw new XmlRpcException("SERVER API = " + API_VERSION + ", CLIENT API = " + clntApiVersion + "; the SageAlert server appears to have been upgraded after the client connected!");
	}
}
