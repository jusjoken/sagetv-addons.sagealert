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

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.output.LockableFileWriter;
import org.apache.log4j.Logger;



/**
 * @author dbattams
 *
 */
abstract class LogFileServer implements SageEventHandler {
	static private final Logger LOG = Logger.getLogger(LogFileServer.class);
	
	File target;
	Writer w;
	
	/**
	 * 
	 */
	public LogFileServer(File target) {
		this.target = new File(new File(Plugin.RES_DIR, "alertLogs"), target.getName());
		if(!this.target.getParentFile().isDirectory() && !this.target.getParentFile().mkdirs())
			throw new IllegalArgumentException("Unable to create base directory for log! [" + this.target.getParent() + "]");
		if(this.target.exists() && !this.target.canWrite())
			throw new IllegalArgumentException("Unable to write to target log file! [" + this.target + "]");
		try {
			w = new LockableFileWriter(this.target, true);
		} catch(IOException e) {
			LOG.error("IO Error", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the target
	 */
	public File getTarget() {
		return target;
	}
	
	public Writer getWriter() {
		return w;
	}
	
	@Override
	protected void finalize() {
		if(w != null) {
			try {
				w.close();
			} catch(IOException e) {
				LOG.error("IO Error", e);
			}
		}
	}
}
