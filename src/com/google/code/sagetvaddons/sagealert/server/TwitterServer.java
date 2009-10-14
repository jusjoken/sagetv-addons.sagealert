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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.google.code.sagetvaddons.sagealert.client.TwitterSettings;

/**
 * An event handler that notifies Twitter
 * @author dbattams
 * @version $Id$
 */
final class TwitterServer implements SageEventHandler {
	static private final Map<TwitterSettings, TwitterServer> SERVERS = new HashMap<TwitterSettings, TwitterServer>();
	static private final Logger LOG = Logger.getLogger(TwitterServer.class);
	static private final String TWEET_HASH = " #SageAlert";
	static private final int MAX_MSG_LENGTH = 129; // Twiiter is 140 subtract the TWEET_HASH
	
	/**
	 * Get the Twitter server for the given Twitter settings
	 * @param settings The settings (id/pwd) for a Twitter instance
	 * @return The Twitter server
	 */
	synchronized static final TwitterServer getServer(TwitterSettings settings) {
		TwitterServer srv = SERVERS.get(settings);
		if(srv == null) {
			srv = new TwitterServer(settings);
			SERVERS.put(settings, srv);
			LOG.debug(SERVERS.size() + " server(s) now in the cache.");
		} else
			srv.updateSettings(settings); // In case the pwd was updated
		return srv;
	}
	
	synchronized static public final void deleteServer(TwitterSettings settings) {
		TwitterServer old = SERVERS.remove(settings);
		if(old != null)
			LOG.debug("Deleted '" + old + "' from the cache.");
		else
			LOG.debug("Server '" + old + "' does not exist in cache.");
		LOG.debug(SERVERS.size() + " server(s) remain in the cache.");
	}
	
	synchronized static public final List<TwitterSettings> getAllServerSettings() {
		List<TwitterSettings> servers = new ArrayList<TwitterSettings>();
		for(TwitterServer gs : SERVERS.values())
			servers.add(gs.getSettings());
		return servers;
	}

	synchronized static public final List<TwitterServer> getAllServers() {
		List<TwitterServer> servers = new ArrayList<TwitterServer>();
		for(TwitterServer gs : SERVERS.values())
			servers.add(gs);
		return servers;
	}

	synchronized static public final void removeServer(TwitterSettings settings) {
		SERVERS.remove(getServer(settings));
	}
	
	synchronized static public final void removeServer(TwitterServer server) {
		SERVERS.remove(server);
	}
		
	private TwitterSettings settings;
	private Twitter twitter;
	
	protected TwitterServer(TwitterSettings settings) {
		this.settings = settings;
		twitter = new Twitter(settings.getId(), settings.getPwd());
		twitter.setSource("SageAlert");
	}

	@Override
	public TwitterSettings getSettings() {
		return settings;
	}
	
	public void updateSettings(TwitterSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public String toString() { return settings.toString(); }

	@Override
	public void onEvent(SageEvent e) {
		String msg;
		if(e.getMediumDescription().length() <= MAX_MSG_LENGTH)
			msg = e.getMediumDescription() + TWEET_HASH;
		else {
			msg = e.getMediumDescription().substring(0, MAX_MSG_LENGTH - 3);
			msg = msg.concat("..." + TWEET_HASH);
		}
		
		try {
			twitter.updateStatus(msg);
			LOG.info("'" + e.getSubject() + "' notification sent successfully to '" + settings + "'");
		} catch(TwitterException x) {
			LOG.error("Twitter exception", x);
		}		
	}
}
