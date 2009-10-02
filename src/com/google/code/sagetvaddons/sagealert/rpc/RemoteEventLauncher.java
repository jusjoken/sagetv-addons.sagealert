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
package com.google.code.sagetvaddons.sagealert.rpc;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * @author dbattams
 *
 */
public class RemoteEventLauncher {

	private URL url;
	
	private XmlRpcClientConfigImpl rpcConfig;
	private XmlRpcClient rpcClnt;
	
	public RemoteEventLauncher(URL url) throws MalformedURLException {
		if(!url.toString().endsWith("sagealert"))
			throw new MalformedURLException("SageAlert URL must end with 'sagealert' and no trailing slash!");
		this.url = new URL(url.toString() + "/xmlrpc");
		rpcConfig = new XmlRpcClientConfigImpl();
		rpcConfig.setServerURL(this.url);
		rpcClnt = new XmlRpcClient();
		rpcClnt.setConfig(rpcConfig);
	}
	
	public boolean fire(String title, String desc) {
		Object[] params = new Object[] {title, desc};
		try {
			return (Boolean)rpcClnt.execute("RemoteEventLauncher.fire", params);
		} catch(XmlRpcException e) {
			return false;
		}
	}
	
	public void setUserId(String id) {
		rpcConfig.setBasicUserName(id);
	}
	
	public void setPassword(String pwd) {
		rpcConfig.setBasicPassword(pwd);
	}
	
	public String getRpcUrlString() { return url.toString(); }
}
