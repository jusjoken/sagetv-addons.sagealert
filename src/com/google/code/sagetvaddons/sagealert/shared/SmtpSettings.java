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
package com.google.code.sagetvaddons.sagealert.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Encapsulates all the settings required to connect to an SMTP server and send alerts via email
 * @author dbattams
 * @version $Id$
 */
public final class SmtpSettings implements IsSerializable {
	
	private String host;
	private int port;
	private String user;
	private String pwd;
	private String senderAddress;
	private boolean useSsl;
	
	/**
	 * @param host
	 * @param port
	 * @param user
	 * @param pwd
	 * @param senderAddress
	 * @param useSsl
	 */
	public SmtpSettings(String host, int port, String user, String pwd, String senderAddress, boolean useSsl) {
		this.host = host != null ? host : "";
		this.port = port;
		this.user = user != null ? user : "";
		this.pwd = pwd != null ? pwd : "";
		this.senderAddress = senderAddress != null ? senderAddress : "";
		this.useSsl = useSsl;
	}
	
	/**
	 * Default ctor req'd by GWT RPC; do not use this ctor!
	 */
	public SmtpSettings() {}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @return the senderAddress
	 */
	public String getSenderAddress() {
		return senderAddress;
	}

	/**
	 * @return the useSsl
	 */
	public boolean useSsl() {
		return useSsl;
	}
}
