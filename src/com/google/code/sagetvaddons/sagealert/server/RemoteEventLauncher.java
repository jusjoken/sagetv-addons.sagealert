/**
 * 
 */
package com.google.code.sagetvaddons.sagealert.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author dbattams
 *
 */
public final class RemoteEventLauncher {
	public boolean fire(String title, String desc) {
		try {
			SageEventHandlerManager.getInstance().fire(new RemoteEvent(title, desc, InetAddress.getByName("127.0.0.1")));
			return true;
		} catch(UnknownHostException e) {
			return false;
		}
	}
}
