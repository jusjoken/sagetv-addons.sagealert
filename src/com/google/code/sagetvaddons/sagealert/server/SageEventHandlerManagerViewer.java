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
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Dump the event manger's data structure to the browser
 * @author dbattams
 * @version $Id$
 */
public class SageEventHandlerManagerViewer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5105890941484764991L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "text/plain");
		resp.setDateHeader("Expires", 0);
		resp.setHeader("Cache-Control", "no-cache, must-revalidate");
		PrintWriter w = resp.getWriter();
		w.write("Event Handler Manager state as of " + new Date() + "\n\n");
		w.write(SageEventHandlerManager.getInstance().dumpState());
		w.close();
	}
}
