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

import java.io.StringWriter;

import com.google.code.sagetvaddons.sagealert.client.SageEventMetaData;

/**
 * A SageEvent that signifies that media is being played locally.
 * 
 * <p>If installed on the SageTV server and the server is running as a service then SageAlert can only notify about extenders and placeshifters and those clients 
 * should be identified by mac address.</p>
 * 
 * <p>If installed on the SageTV server and the server is not running as a service then SageAlert can also alert about what the local client is playing.  The client id for
 * the local client is SAGETV_PROCESS_LOCAL_UI.</p>
 * 
 * <p>If you wish to alert about what the local server is playing and you're running as a service then you must install SageAlert into the SageClient.properties.  You could do
 * this instead of installing it in the server process.
 * </p>
 * 
 * <p>If you wish to send alerts about what a PC client is playing then you must install a separate instance of SageAlert on each client.  The client id would also be SAGETV_PROCESS_LOCAL_UI.
 * If you do this then you'll want to be sure not to attach any listeners to the other event types on the client installation otherwise you will start to get multiple notifications per event.</p>
 * 
 * <p>The multiple installations are necessary because of the fact that the SageTV server is not able to know what a PC client is doing, it only can know that the client has
 * connected to the server (or at least this is the only info exposed in the public SageTV API).</p>
 * 
 * @author dbattams
 * @version $Id$
 */
final class PlayingMediaEvent implements SageEvent {
	/**
	 * The meta data for this event
	 */
	static final SageEventMetaData EVENT_METADATA = new SageEventMetaData(PlayingMediaEvent.class.getCanonicalName(), "Alert when a local client, extender or placeshifter is playing back a media file.");

	private ViewingClient source;
	
	PlayingMediaEvent(ViewingClient client) {
		source = client;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getEventMetaData()
	 */
	@Override
	public SageEventMetaData getEventMetaData() {
		return EVENT_METADATA;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getLongDescription()
	 */
	@Override
	public String getLongDescription() {
		StringWriter w = new StringWriter();
		w.write("Client '" + (source.getClient().getAlias().length() == 0 ? source.getClient().getId() : source.getClient().getAlias()) + "' is currently ");
		if(!source.getMedia().IsMusicFile())
			w.write("watching ");
		else
			w.write("listening to ");
		w.write("'" + source.getMedia().GetMediaTitle() + "'");
		return w.toString();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getMediumDescription()
	 */
	@Override
	public String getMediumDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return getLongDescription();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSource()
	 */
	@Override
	public ViewingClient getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEvent#getSubject()
	 */
	@Override
	public String getSubject() {
		return "A client is playing a media file...";
	}
}
