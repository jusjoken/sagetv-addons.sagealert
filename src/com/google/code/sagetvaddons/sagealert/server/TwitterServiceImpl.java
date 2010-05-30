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
package com.google.code.sagetvaddons.sagealert.server;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

import com.google.code.sagetvaddons.sagealert.shared.TwitterService;
import com.google.code.sagetvaddons.sagealert.shared.TwitterSettings;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author dbattams
 *
 */
@SuppressWarnings("serial")
final public class TwitterServiceImpl extends RemoteServiceServlet implements TwitterService {
	static private final Logger LOG = Logger.getLogger(TwitterServiceImpl.class);

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.TwitterService#getPinUrl()
	 */
	public String getPinUrl() throws IOException {
		HttpSession sess = this.getThreadLocalRequest().getSession();
		RequestToken rTok = null;
		Twitter t = new TwitterFactory().getInstance();
		t.setOAuthConsumer(TwitterServer.C_KEY, TwitterServer.C_HUSH);
		try {
			rTok = t.getOAuthRequestToken();
			sess.setAttribute("rTok", rTok);
			sess.setAttribute("srv", t);
		} catch(TwitterException e) {
			LOG.error("Twitter error", e);
			throw new IOException(e);
		}
		return rTok.getAuthenticationURL();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.shared.TwitterService#submitPin(java.lang.String)
	 */
	public TwitterSettings submitPin(String pin) throws IOException {
		HttpSession sess = this.getThreadLocalRequest().getSession(false);
		Twitter t = null;
		RequestToken rTok = null;
		if(sess != null) {
			t = (Twitter)sess.getAttribute("srv");
			rTok = (RequestToken)sess.getAttribute("rTok");
		}
		if(t == null || rTok == null)
			throw new IOException("Cannot submit PIN before generating RequestToken!");
		try {
			AccessToken aTok = t.getOAuthAccessToken(rTok, pin);
			TwitterSettings settings = new TwitterSettings(aTok.getScreenName(), aTok.getToken(), aTok.getTokenSecret());
			DataStore.getInstance().saveReporter(settings);
			return settings;
		} catch(TwitterException e) {
			LOG.error("Twitter error", e);
			throw new IOException(e);
		}
	}

}
