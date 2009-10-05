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

/**
 * <p>All monitor threads must implement this interface.</p>
 * 
 * <p>A SageAlertRunnable is just like a java.lang.Runnable except it adds two additional rules that must be followed:</p>
 * 
 * <p>First, SageAlert will only create and start one instance of any given SageAlertRunnable.  Implementers must ensure that their runnable stays alive and monitors for as
 * long as SageAlert is running.  This would usually be achieved by putting the logic of your run() method inside a while loop like so:
 * <pre>
 * public void run() {
 *    while(keepAlive()) {
 *       // Monitor your event condition, fire it when necessary
 *       
 *       Thread.sleep(x); // Sleep after each run then first thing you check when done sleeping is if you should exit (via keepAlive() in while loop condition)
 *    }
 * }
 * </pre>
 * </p>
 * 
 * <p>Second, you must honour the keepAlive() flag and once it's false you must immediately cleanup and gracefully exit the run() method of your class.  The above skeleton shows
 * how you should do this.</p>
 * 
 * @author dbattams
 * @version $Id$
 */
abstract public class SageAlertRunnable implements Runnable {
	
	private boolean keepAlive;
	
	/**
	 * Constructor
	 */
	public SageAlertRunnable() {
		keepAlive = true;
	}

	/**
	 * State flag denoting if this thread should keep running
	 * @return True if the thread should keep running or false if it's time to cleanup and exit
	 */
	synchronized public final boolean keepAlive() {
		return keepAlive;
	}
	
	/**
	 * Set the keep alive state for this thread; should only be called by the AppInitializer class
	 * @param b The new value of the keep alive state flag for this thread
	 */
	synchronized final void setKeepAlive(boolean b) {
		keepAlive = b;
	}	
}
