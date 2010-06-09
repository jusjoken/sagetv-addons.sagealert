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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.shared.CsvLogFileSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
class CsvLogFileServer extends LogFileServer {
	static private final Logger LOG = Logger.getLogger(CsvLogFileServer.class);
	static private final SimpleDateFormat FMT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z");
	
	private CsvLogFileSettings settings;
	private StringBuilder msg;
	
	/**
	 * @param target
	 */
	public CsvLogFileServer(CsvLogFileSettings settings) {
		super(new File(settings.getTarget()));
		this.settings = settings;
		msg = new StringBuilder();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEventHandler#getSettings()
	 */
	public NotificationServerSettings getSettings() {
		return settings;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEventHandler#onEvent(com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent)
	 */
	synchronized public void onEvent(SageAlertEvent e) {
		msg.setLength(0);
		msg.append(e.getSource() + ",");
		synchronized(FMT) {
			msg.append("\"" + FMT.format(new Date()) + "\",");
		}
		msg.append("\"" + e.getSubject().replaceAll("\"", "\"\"") + "\",");
		msg.append("\"" + e.getLongDescription().replaceAll("\"", "\"\"") + "\"\n");
		try {
			FileUtils.writeStringToFile(getTarget(), msg.toString(), "UTF-8");
			LOG.info("Alert for '" + e.getSubject() + "' event written successfully to '" + getTarget() + "'");
		} catch(IOException x) {
			LOG.error("IO Error", x);
			LOG.error("Unable to write alert for '" + e.getSubject() + "' event to '" + getTarget() + "'");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((settings == null) ? 0 : settings.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CsvLogFileServer)) {
			return false;
		}
		CsvLogFileServer other = (CsvLogFileServer) obj;
		if (settings == null) {
			if (other.settings != null) {
				return false;
			}
		} else if (!settings.equals(other.settings)) {
			return false;
		}
		return true;
	}
}
