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
package com.google.code.sagetvaddons.sagealert.client;

import com.google.code.gwtsrwc.client.VersionLabel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dbattams
 *
 */
final class AboutPanel extends VerticalPanel {
	private VerticalPanel container;
	private DisclosurePanel deps;
	private VerticalPanel depsContainer;
	
	AboutPanel() {
		deps = new DisclosurePanel("Additional Libraries", false);

		depsContainer = new VerticalPanel();
		depsContainer.add(new HTML("<b>GWT:</b> " + GWT.getVersion() + " | <a target=\"_blank\" href=\"http://code.google.com/webtoolkit/\">Project Site</a>"));
		depsContainer.add(new HTML("<b>GKusnick's Studio Tools (for SageTV):</b> <a target=\"_blank\" href=\"http://forums.sagetv.com/forums/downloads.php?do=file&id=128\">Download Site</a>"));
		depsContainer.add(new HTML("<b>Apache Commons:</b> <a target=\"_blank\" href=\"http://commons.apache.org/\">Project Site</a>"));
		depsContainer.add(new HTML("<b>gwtsrwc:</b> " + VersionLabel.getVersion() + " | <a target=\"_blank\" href=\"http://gwtsrwc.googlecode.com/\">Project Site</a>"));
		depsContainer.add(new HTML("<b>JSON.org:</b> <a target=\"_blank\" href=\"http://www.json.org/\">Web Site</a>"));
		depsContainer.add(new HTML("<b>log4j:</b> <a target=\"_blank\" href=\"http://logging.apache.org/\">Project Site</a>"));
		depsContainer.add(new HTML("<b>twitter4j:</b> <a target=\"_blank\" href=\"http://yusuke.homeip.net/twitter4j/en/index.html\">Project Site</a>"));
		depsContainer.add(new HTML("<b>libgrowl:</b> 0.1.1 + local patches for password support | <a target=\"_blank\" href=\"http://libgrowl.sf.net/\">Project Site</a>"));
		depsContainer.add(new HTML("<b>JavaMail:</b> <a target=\"_blank\" href=\"http://java.sun.com/products/javamail/\">Web Site</a>"));
		depsContainer.add(new HTML("<b>SQLite JDBC:</b> <a target=\"_blank\" href=\"http://www.zentus.com/sqlitejdbc/\">Project Site</a>"));
		depsContainer.add(new HTML("<b>XML-RPC:</b> <a target=\"_blank\" href=\"http://www.xmlrpc.com\">Project Site</a>"));
		deps.add(depsContainer);
		
		container = new VerticalPanel();
		container.add(new HTML("<b>SageAlert v" + Version.getFullVersion() + "</b>"));
		container.add(new HTML("<b>Author:</b> Derek Battams <derek AT battams DOT ca>"));
		container.add(new HTML("<b>Project Site:</b> <a target=\"_blank\" href=\"http://sagetv-addons.googlecode.com\">http://sagetv-addons.googlecode.com</a>"));
		container.add(deps);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		add(container);
	}
}
