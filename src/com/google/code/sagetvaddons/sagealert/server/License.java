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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;


/**
 * Keep the honest people honest and the people who really want to crack this software, well go nuts!
 * (Should take most who want to crack this less than 30 seconds)
 * @author dbattams
 *
 */
final class License {
	static private final Logger LOG = Logger.getLogger(License.class);

	static private final File LIC_FILE = new File(Plugin.RES_DIR, "license.txt");
	static private final String LIC_KEY = "SageAlert_OK";

	static private final License INSTANCE = new License();
	static final License get() { return INSTANCE; }

	private boolean isLicensed;

	private License() {
		if(!LIC_FILE.exists())
			isLicensed = false;
		else {
			try {
				String data = FileUtils.readFileToString(LIC_FILE, "UTF-8");
				if(DigestUtils.md5Hex(LIC_KEY).equals(data))
					isLicensed = true;
				else
					isLicensed = false;
			} catch(IOException e) {
				LOG.error("LicenseReadError", e);
				isLicensed = false;
			}
		}
		if(isLicensed)
			LOG.info("License found and verified; all features of software enabled!");
		else {
			StringBuilder msg = new StringBuilder("License file is invalid or not found; some features of this software have been disabled:\n");
			msg.append("\tSMTP server support disabled.\n");
			msg.append("\tOnly one notification sent per event.\n");
			LOG.info(msg.toString());
			API.apiNullUI.systemMessageAPI.PostSystemMessage(1204, 1, "SageAlert: " + msg.toString() + "\nPlease consider making a donation.", null);
		}
	}

	public boolean isLicensed() { return isLicensed; }
}
