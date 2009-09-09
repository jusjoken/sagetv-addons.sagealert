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

import gkusnick.sagetv.api.MediaFileAPI;

import com.google.code.sagetvaddons.sagealert.client.Client;

/**
 * Describes an active UI context and the media it is currently viewing
 * @author dbattams
 * @version $Id$
 */
public class ViewingClient {

	private Client client;
	private MediaFileAPI.MediaFile media;
	
	/**
	 * Constructor
	 * @param id The UI context name of the client viewing the given media file
	 * @param mf The media file being viewed
	 */
	public ViewingClient(Client c, MediaFileAPI.MediaFile mf) {
		client = c;
		media = mf;
	}

	/**
	 * @return the clientId
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @return the media
	 */
	public MediaFileAPI.MediaFile getMedia() {
		return media;
	}
}
