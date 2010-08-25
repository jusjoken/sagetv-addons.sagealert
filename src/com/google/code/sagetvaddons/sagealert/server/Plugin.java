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

import gkusnick.sagetv.api.API;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sage.SageTVPlugin;
import sage.SageTVPluginRegistry;

/**
 * @author dbattams
 *
 */
public final class Plugin implements SageTVPlugin {
	static private Logger LOG = null;
	static public final File RES_DIR = new File("plugins/sagealert");
	
	static public final String OPT_IGNORE_REPEAT_SYS_MSGS = "IgnoreRepeatSysMsgs";
	static public final String OPT_IGNORE_REPEAT_SYS_MSGS_DEFAULT = "true";
	
	/**
	 * 
	 */
	public Plugin(SageTVPluginRegistry reg) {
		synchronized(Plugin.class) {
			if(LOG == null) {
				PropertyConfigurator.configure(new File(RES_DIR, "sagealert.log4j.properties").getAbsolutePath());
				LOG = Logger.getLogger(Plugin.class);
			}
		}
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigHelpText(java.lang.String)
	 */
	public String getConfigHelpText(String arg0) {
		String helpTxt;
		if(OPT_IGNORE_REPEAT_SYS_MSGS.equals(arg0))
			helpTxt = "If true, repeated system messages, as determined by the core, will be ignored and not processed by SageAlert.";
		else
			helpTxt = "No help available.";
		return helpTxt;
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigLabel(java.lang.String)
	 */
	public String getConfigLabel(String arg0) {
		String lbl;
		if(OPT_IGNORE_REPEAT_SYS_MSGS.equals(arg0))
			lbl = "Ignore Repeated System Messages";
		else
			lbl = "No label available";
		return lbl;
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigOptions(java.lang.String)
	 */
	public String[] getConfigOptions(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigSettings()
	 */
	public String[] getConfigSettings() {
		return new String[] {OPT_IGNORE_REPEAT_SYS_MSGS};
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigType(java.lang.String)
	 */
	public int getConfigType(String arg0) {
		if(OPT_IGNORE_REPEAT_SYS_MSGS.equals(arg0))
			return SageTVPlugin.CONFIG_BOOL;
		throw new RuntimeException("Unknown option! [" + arg0 + "]");
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigValue(java.lang.String)
	 */
	public String getConfigValue(String arg0) {
		String defaultVal = null;
		if(OPT_IGNORE_REPEAT_SYS_MSGS.equals(arg0))
			defaultVal = OPT_IGNORE_REPEAT_SYS_MSGS_DEFAULT;
		return API.apiNullUI.configuration.GetServerProperty(arg0, defaultVal);
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#getConfigValues(java.lang.String)
	 */
	public String[] getConfigValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#resetConfig()
	 */
	public void resetConfig() {
		setConfigValue(OPT_IGNORE_REPEAT_SYS_MSGS, OPT_IGNORE_REPEAT_SYS_MSGS_DEFAULT);
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#setConfigValue(java.lang.String, java.lang.String)
	 */
	public void setConfigValue(String arg0, String arg1) {
		API.apiNullUI.configuration.SetServerProperty(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#setConfigValues(java.lang.String, java.lang.String[])
	 */
	public void setConfigValues(String arg0, String[] arg1) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#start()
	 */
	public void start() {
		LOG.info("Deploying SageAlert v2.x into Jetty plugin...");
		try {
			FileUtils.copyFileToDirectory(new File(RES_DIR, "SageAlert.war"), new File("jetty/webapps"), true);
			FileUtils.copyFileToDirectory(new File(RES_DIR, "SageAlert.context.xml"), new File("jetty/contexts"), false);
			LOG.info("Deployment successful!");
		} catch(IOException e) {
			LOG.fatal("Deployment failed!", e);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see sage.SageTVPlugin#stop()
	 */
	public void stop() {
		LOG.info("Undeploying SageAlert from Jetty plugin...");
		if(!new File("jetty/webapps/SageAlert.war").delete())
			LOG.warn("Unable to delete SageAlert war file; you may need to restart SageTV to correct this error!");
		if(!new File("jetty/contexts/SageAlert.context.xml").delete())
			LOG.error("Unable to delete SageAlert context file; you will need to stop SageTV and delete this file manually!");
		LOG.info("Undeployment completed!");
	}

	/* (non-Javadoc)
	 * @see sage.SageTVEventListener#sageEvent(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void sageEvent(String arg0, Map arg1) {
		// TODO Auto-generated method stub

	}
}
