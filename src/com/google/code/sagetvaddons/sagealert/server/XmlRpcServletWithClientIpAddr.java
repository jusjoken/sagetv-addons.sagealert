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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.webserver.XmlRpcServlet;

/**
 * A subclass of the XmlRpcServlet that provides the requester's IP address via a static method
 * @author dbattams
 * @version $Id$
 */
public final class XmlRpcServletWithClientIpAddr extends XmlRpcServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1190094293738002490L;

	private static final ThreadLocal<String> REQ_IPS = new ThreadLocal<String>();
	
	/**
	 * Return the IP address of the requester
	 * @return The requester's IP address
	 */
	static public String getRequesterIpAddr() {
		return REQ_IPS.get();
	}

	/* (non-Javadoc)
	 * @see org.apache.xmlrpc.webserver.XmlRpcServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest pRequest,
			HttpServletResponse pResponse) throws IOException, ServletException {
		REQ_IPS.set(pRequest.getRemoteAddr());
		super.doPost(pRequest, pResponse);
	}
}
