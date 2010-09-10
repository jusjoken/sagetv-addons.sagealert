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
package com.google.code.sagetvaddons.sagealert.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;

import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;
import com.google.code.sagetvaddons.sagealert.shared.TwitterSettings;

/**
 * An event handler that notifies Twitter
 * @author dbattams
 * @version $Id$
 */
final class TwitterServer implements SageAlertEventHandler {
	static private final Map<TwitterSettings, TwitterServer> SERVERS = new HashMap<TwitterSettings, TwitterServer>();
	static private final Logger LOG = Logger.getLogger(TwitterServer.class);
	static private final String TWEET_HASH = " #SageAlert";
	static private final int MAX_MSG_LENGTH = 140 - TWEET_HASH.length(); // Twiiter is 140 subtract the TWEET_HASH
	static final String C_KEY = "kZn7jCpged0emxGqWdg";
	static final String C_HUSH = "unmEgRvFyO3LYqMTxVSaMada8k6QWkI7wqEuE6GOqE";
	static private final Configuration TWIT_CONF = new ConfigurationBuilder()
	.setHttpReadTimeout(5000)
	.setHttpRetryCount(3)
	.setOAuthConsumerKey(C_KEY)
	.setOAuthConsumerSecret(C_HUSH)
	.build();
	static private final TwitterFactory TWIT_FACTORY = new TwitterFactory(TWIT_CONF);

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

	private TwitterSettings settings;
	private Twitter twitter;
	private String lastTweet;

	private TwitterServer(TwitterSettings settings) {
		this.settings = settings;
		lastTweet = null;
		twitter = TWIT_FACTORY.getOAuthAuthorizedInstance(new AccessToken(settings.getToken(), settings.getSecret()));
		try {
			settings.setAlias(twitter.getScreenName());
		} catch(TwitterException e) {
			LOG.error("Twitter error", e);
		}
	}

	public TwitterSettings getSettings() {
		return settings;
	}

	public void updateSettings(TwitterSettings settings) {
		this.settings = settings;
	}

	@Override
	public String toString() { return settings.toString(); }

	public void onEvent(final SageAlertEvent e) {
		final StringBuffer msg = new StringBuffer();
		if(e.getMediumDescription().length() <= MAX_MSG_LENGTH)
			msg.append(e.getMediumDescription() + TWEET_HASH);
		else {
			msg.append(e.getMediumDescription().substring(0, MAX_MSG_LENGTH - 3));
			msg.append("..." + TWEET_HASH);
		}

		synchronized(this) {
			if(!msg.toString().equals(lastTweet)) {
				new Thread() {
					@Override
					public void run() {
						try {
							twitter.updateStatus(msg.toString());
							synchronized(TwitterServer.this) {
								lastTweet = msg.toString();
							}
							LOG.info("'" + e.getSubject() + "' notification sent successfully to '" + settings + "'");
						} catch(TwitterException x) {
							LOG.error("Twitter exception", x);
						}		
					}
				}.start();
			} else
				LOG.info("Not sending event '" + e.getSubject() + "' to @" + getSettings().getId() + " because the generated tweet is exactly the same as the last tweet posted!");
		}
	}

	public void destroy() {

	}
}
