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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author dbattams
 *
 */
public interface SageAlertClientBundle extends ClientBundle {
	static public final SageAlertClientBundle INSTANCE = GWT.create(SageAlertClientBundle.class);
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/heart.png")
	public ImageResource getDonateImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/lock_open.png")
	public ImageResource getRegisterImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/help.png")
	public ImageResource getHelpImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/server_edit.png")
	public ImageResource getSettingsImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/email_edit.png")
	public ImageResource getEmailImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/cog_edit.png")
	public ImageResource getPrefsImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/font.png")
	public ImageResource getAboutImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/book_go.png")
	public ImageResource getDocsImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/group.png")
	public ImageResource getSupportImg();
	
	@Source("com/google/code/sagetvaddons/sagealert/client/resources/page_add.png")
	public ImageResource getTixImg();

	@Source("com/google/code/sagetvaddons/sagealert/client/resources/house_go.png")
	public ImageResource getHomeImg();
}
