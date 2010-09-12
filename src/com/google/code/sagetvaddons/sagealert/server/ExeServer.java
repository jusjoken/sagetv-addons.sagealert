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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.shared.ExeServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;

/**
 * @author dbattams
 *
 */
class ExeServer implements SageAlertEventHandler {
	static private final Logger LOG = Logger.getLogger(ExeServer.class);
	
	private ExeServerSettings settings;
	
	/**
	 * 
	 */
	public ExeServer(ExeServerSettings settings) {
		this.settings = settings;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageAlertEventHandler#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageAlertEventHandler#getSettings()
	 */
	public NotificationServerSettings getSettings() {
		return settings;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageAlertEventHandler#onEvent(com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent)
	 */
	public void onEvent(SageAlertEvent e) {
		setSettings(DataStore.getInstance().reloadSettings(getSettings()));
		final Map<String, String> env = new HashMap<String, String>();
		env.put("SA_SUBJ", e.getSubject());
		env.put("SA_SOURCE", e.getSource());
		env.put("SA_SHORT", e.getShortDescription());
		env.put("SA_MED", e.getMediumDescription());
		env.put("SA_LONG", e.getLongDescription());
		final File exe = new File(settings.getExeName());
		LOG.info("Processing event '" + e.getSubject() + "' with exe '" + exe.getAbsolutePath() + "' and env " + env.toString());
		if(exe.canExecute()) {
			Thread t = new Thread() {
				@Override
				public void run() {
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					CommandLine cmd = new CommandLine(exe);
					cmd.addArguments(settings.getArgs());
					StringBuilder msg = new StringBuilder("Running '" + cmd.toString() + "'");
					Executor executor = new DefaultExecutor();
					executor.setStreamHandler(new PumpStreamHandler(output));
					int rc;
					try {
						rc = executor.execute(cmd, env);
					} catch(ExecuteException e) { 
						rc = e.getExitValue();
						if(e.getCause() != null)
							LOG.error("ExecuteException", e.getCause());
					} catch (IOException e) {
						rc = -1;
						LOG.error("IOException running exe", e);
					}
					if(rc == 0) {
						if(output.toString().length() > 0)
							msg.append(" (output below)\n\n" + output.toString());
						else
							msg.append(" (no output captured from successful execution)");
					} else
						msg.append(" (error during execution [rc = " + rc + "]; output below)\n\n" + output.toString());
					LOG.warn(msg);
				}
			};
			t.setDaemon(true);
			t.start();
		} else
			LOG.error("Specified executable is invalid! [" + exe.getAbsolutePath() + "]");
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
		if (!(obj instanceof ExeServer)) {
			return false;
		}
		ExeServer other = (ExeServer) obj;
		if (settings == null) {
			if (other.settings != null) {
				return false;
			}
		} else if (!settings.equals(other.settings)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return settings.toString();
	}

	public void setSettings(NotificationServerSettings settings) {
		this.settings = (ExeServerSettings)settings;
	}
}
