/*
 *      Copyright 2009-2010 Battams, Derek
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
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * @version $Id$
 */
public final class SageAlert implements EntryPoint {	
	/**
	 * This is the entry point method.
	 * 
	 * Build the tab panel and display it on the root panel; the UI for this app is a tab panel
	 * The Alerts tab must always remain as the first tab, then Settings second; About tab must always remain last
	 */
	public void onModuleLoad() {
		RootPanel.get().add(SageAlertViewport.get());
	}
}
