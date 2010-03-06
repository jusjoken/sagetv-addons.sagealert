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
package com.google.code.sagetvaddons.sagealert.rpc;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * <p>Developer API for generating remote alerts within a SageAlert server.</p>
 * 
 * <p>
 *   Use this API to connect to an active SageAlert server and generate a remote event.
 *   There are three types of remote events that an application can generate:
 * </p>
 * <ul>
 *   <li>Info: An informational alert</li>
 *   <li>Warning: A warning alert</li>
 *   <li>Error: An error alert</li>
 * </ul>
 * @author derek AT battams DOT ca
 * @version $Id$
 */
final public class RemoteEventLauncher {
	static private final int API_VERSION = 0;
	
	private URL url;
	private int serverApiVersion; 
	private XmlRpcClientConfigImpl rpcConfig;
	private XmlRpcClient rpcClnt;
	
	/**
	 * Class constructor
	 * @param url The base URL of the SageAlert server to generate the alerts on; the URL must point to a valid, active SageAlert installation and must not contain a trailing slash (i.e. http://192.168.0.1/sagealert)
	 * @param id The HTTP user id required to connect to SageAlert; null if no id is required
	 * @param pwd The HTTP password required to connect to SageAlert; null if no password is required
	 * @param userAgent The value of the HTTP UserAgent header for all RPC calls to the server; default value used if null or zero-length
	 * @throws MalformedURLException If the URL argument is invalid
	 * @throws ApiVersionException If the client and server are not running the same RPC API version
	 * @since 1.0.2
	 */
	public RemoteEventLauncher(URL url, String id, String pwd, String userAgent) throws MalformedURLException, ApiVersionException {
		if(!url.toString().endsWith("/sagealert"))
			throw new MalformedURLException("SageAlert URL must end with '/sagealert' and no trailing slash!");
		this.url = new URL(url.toString() + "/xmlrpc");
		rpcConfig = new XmlRpcClientConfigImpl();
		rpcConfig.setServerURL(this.url);
		if(id != null) {
			rpcConfig.setBasicUserName(id);
			rpcConfig.setBasicPassword(pwd != null ? pwd : "");
		}
		if(userAgent != null && userAgent.length() > 0)
			rpcConfig.setUserAgent(userAgent);
		rpcClnt = new XmlRpcClient();
		rpcClnt.setConfig(rpcConfig);
		
		try {
			int srvVersion = (Integer)rpcClnt.execute("RemoteEventLauncher.checkApiVersion", new Object[0]);
			serverApiVersion = srvVersion;
			if(srvVersion != API_VERSION)
				throw new ApiVersionException(srvVersion, API_VERSION);
		} catch(XmlRpcException e) {
			throw new ApiVersionException("Unable to verify server RPC API version; your SageAlert server probably needs to be upgraded!");
		}
	}

	/**
	 * Class constructor; using default value for UserAgent header
	 * @param url The base URL of the SageAlert server to generate the alerts on; the URL must point to a valid, active SageAlert installation and must not contain a trailing slash (i.e. http://192.168.0.1/sagealert)
	 * @param id The HTTP user id required to connect to SageAlert; null if no id is required
	 * @param pwd The HTTP password required to connect to SageAlert; null if no password is required
	 * @throws MalformedURLException If the URL argument is invalid
	 * @throws ApiVersionException If the client and server are not running the same RPC API version
	 */
	public RemoteEventLauncher(URL url, String id, String pwd) throws MalformedURLException, ApiVersionException {
		this(url, id, pwd, null);
	}
	
	/**
	 * Constructor that assumes there is no id/password required to connect to the SageAlert server and uses the default value for the UserAgent header
	 * @param url The base URL of the SageAlert server with no trailing slash (i.e. http://192.168.0.1./sagealert)
	 * @throws MalformedURLException Thrown if the given URL is invalid
	 * @throws ApiVersionException Thrown if this client and the SageAlert server are not speaking the same RPC API version
	 */
	public RemoteEventLauncher(URL url) throws MalformedURLException, ApiVersionException {
		this(url, null, null, null);
	}
	
	// Make the actual RPC call on the server
	private void fire(String methodName, String title, String desc) throws RpcException {
		Object[] params = new Object[] {API_VERSION, title, desc};
		try {
			rpcClnt.execute(methodName, params);
		} catch(XmlRpcException e) {
			throw new RpcException(e);
		}
	}
	
	/**
	 * Fire an info level remote event on the server
	 * @param title The alert's title/subject
	 * @param desc The alert's description
	 * @throws RpcException Thrown if the RPC call fails
	 */
	public void fireInfo(String title, String desc) throws RpcException {
		fire("RemoteEventLauncher.fireInfo", title, desc);
	}

	/**
	 * Fire a warning level remote event on the server
	 * @param title The alert's title/subject
	 * @param desc The alert's description
	 * @throws RpcException Thrown if the RPC call fails
	 */
	public void fireWarning(String title, String desc) throws RpcException {
		fire("RemoteEventLauncher.fireWarning", title, desc);
	}

	/**
	 * Fire an error level remote event on the server
	 * @param title The alert's title/subject
	 * @param desc The alert's description
	 * @throws RpcException Thrown if the RPC call fails
	 */
	public void fireError(String title, String desc) throws RpcException {
		fire("RemoteEventLauncher.fireError", title, desc);
	}

	/**
	 * Set the HTTP user id for connecting to the SageAlert server; setting the id takes effect on all future fire() calls after this call is made
	 * @param id The new HTTP user id to use for connecting to the SageAlert server
	 */
	public void setUserId(String id) {
		rpcConfig.setBasicUserName(id);
	}

	/**
	 * Get the current HTTP user name being used to connect to the SageAlert server to make the RPC calls
	 * @return The HTTP user name currently in use
	 */
	public String getUserId() {
		return rpcConfig.getBasicUserName();
	}
	
	/**
	 * Set the HTTP password for connecting to the SageAlert server; setting the password takes effect on all future fire() calls after this call is made
	 * @param pwd The new HTTP password to use for connecting to the SageAlert server
	 */
	public void setPassword(String pwd) {
		rpcConfig.setBasicPassword(pwd);
	}
	
	/**
	 * Get the current HTTP password being used to connect to the SageAlert server to make the RPC calls
	 * @return The HTTP password currently in use
	 */
	public String getPassword() {
		return rpcConfig.getBasicPassword();
	}
	
	/**
	 * Set the UserAgent header for the RPC call
	 * @param header The value of the HTTP UserAgent header when making all RPC calls to the server
	 * @since 1.0.2
	 */
	public void setUserAgent(String header) {
		rpcConfig.setUserAgent(header);
	}

	/**
	 * Returns the value of the HTTP UserAgent header being used for all RPC calls to the server
	 * @return The value of the UserAgent header
	 * @since 1.0.2
	 */
	public String getUserAgent() {
		return rpcConfig.getUserAgent();
	}
	
	/**
	 * Get the complete URL being used by this client for connecting to the SageAlert server (for debug purposes only)
	 * @return The SageAlert connection URL, as a string
	 */
	public String getRpcUrlString() { return url.toString(); }
	
	/**
	 * Get the API version this client speaks; the client must speak the same API version as the server in order to successfully communicate
	 * @return The client's API version
	 */
	public int getClientApiVersion() { return API_VERSION; }
	
	/**
	 * Get the API version being spoken by the SageAlert server this client is connected to; the client must speak the same API version as the server in order to successfully communicate
	 * @return The server's API version
	 */
	public int getServerApiVersion() {
		return serverApiVersion;
	}
}
