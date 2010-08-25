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
package com.google.code.sagetvaddons.sagealert.client;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author dbattams
 *
 */
final class AboutWindow extends Window {
	
	AboutWindow() {
		setSize(480, 320);
		setHeading("SageAlert v" + Version.getFullVersion());

		add(new HTML("<p style=\"margin: 4px;\"><b>Author:</b> Derek Battams &lt;derek AT battams DOT ca&gt;</p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>Project Site:</b> <a target=\"_blank\" href=\"http://sagetv-addons.googlecode.com\">http://sagetv-addons.googlecode.com</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>GWT:</b> " + GWT.getVersion() + " | <a target=\"_blank\" href=\"http://code.google.com/webtoolkit/\">Project Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>GXT:</b> " + GXT.getVersion().getRelease() + " | <a target=\"_blank\" href=\"http://www.sencha.com/products/gwt/\">Web Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>GKusnick's Studio Tools (for SageTV):</b> <a target=\"_blank\" href=\"http://forums.sagetv.com/forums/showthread.php?t=48443\">User Forum Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>Apache Commons:</b> <a target=\"_blank\" href=\"http://commons.apache.org/\">Project Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>JSON.org:</b> <a target=\"_blank\" href=\"http://www.json.org/\">Web Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>log4j:</b> <a target=\"_blank\" href=\"http://logging.apache.org/\">Project Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>twitter4j:</b> <a target=\"_blank\" href=\"http://yusuke.homeip.net/twitter4j/en/index.html\">Project Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>libgrowl:</b> 0.1.1 + local patches for password support | <a target=\"_blank\" href=\"http://libgrowl.sf.net/\">Project Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>JavaMail:</b> <a target=\"_blank\" href=\"http://java.sun.com/products/javamail/\">Web Site</a></p>"));
		add(new HTML("<p style=\"margin: 4px;\"><b>SQLite JDBC:</b> <a target=\"_blank\" href=\"http://www.zentus.com/sqlitejdbc/\">Project Site</a></p>"));		
		add(new HTML("<p style=\"margin: 4px;\"><b>Icon Images:</b> <a target=\"_blank\" href=\"http://www.famfamfam.com/lab/icons/silk/\">Web Site</a></p>"));		
	}
}
