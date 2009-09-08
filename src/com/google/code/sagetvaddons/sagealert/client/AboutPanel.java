/**
 * 
 */
package com.google.code.sagetvaddons.sagealert.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dbattams
 *
 */
final class AboutPanel extends VerticalPanel {
	AboutPanel() {
		add(new Label("You are running SageAlert v" + Version.getFullVersion()));
	}
}
