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

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.shared.EmailSettings;
import com.google.code.sagetvaddons.sagealert.shared.NotificationServerSettings;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEvent;
import com.google.code.sagetvaddons.sagealert.shared.SmtpSettings;

/**
 * Event handler that sends events via email
 * @author dbattams
 * @version $Id$
 */
final class EmailServer implements SageAlertEventHandler {

	static private final Logger LOG = Logger.getLogger(EmailServer.class);

	private EmailSettings emailSettings;
	private final SmtpSettings smtpSettings;
	private Session session;
	private SageAlertEvent event;
	private MimeMessage message;

	/**
	 * Ctor
	 * @param settings The details of where the event is to be sent (i.e. email recipient, etc.)
	 */
	EmailServer(EmailSettings settings) {
		this(settings, null);
	}
	
	/**
	 * Constructor
	 * @param settings The details of where the event is to be sent (i.e. email address)
	 * @param smtpSettings The SMTP server settings to use
	 */
	EmailServer(EmailSettings settings, SmtpSettings smtpSettings) {
		emailSettings = settings;
		if(smtpSettings != null)
			this.smtpSettings = smtpSettings;
		else
			this.smtpSettings = DataStore.getInstance().getSmtpSettings();
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageAlertEventHandler#getSettings()
	 */
	public EmailSettings getSettings() {
		return emailSettings;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageAlertEventHandler#onEvent(com.google.code.sagetvaddons.sagealert.server.SageEvent)
	 */
	public void onEvent(final SageAlertEvent e) {
		if(License.get().isLicensed()) {
			setSettings(DataStore.getInstance().reloadSettings(getSettings()));
			new Thread() {
				@Override
				public void run() {
					synchronized(EmailServer.this) {
						event = e;
						try {
							connectToSmtp();
							constructMsg();
							sendMessage();
						} catch(NotificationRecipientException x) {
							LOG.error("Invalid SMTP settings", x);
							LOG.error("Email for '" + event.getSubject() + "' event FAILED to '" + emailSettings.getAddress() + "'");
						} catch(MessagingException x) {
							LOG.error("Error sending email", x);
							LOG.error("Email for '" + event.getSubject() + "' event FAILED to '" + emailSettings.getAddress() + "'");
						}
					}
				}
			}.start();
		} else
			LOG.warn("Email notification ignored; your copy of SageAlert is not licensed!");
	}

	private void connectToSmtp() throws NotificationRecipientException {
		Properties props = new Properties();

		props.setProperty("mail.transport.protocol", "smtp");
		if(smtpSettings.useSsl())
			props.setProperty("mail.smtp.ssl.enable", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.port", Integer.toString(smtpSettings.getPort()));

		if(smtpSettings.getHost().length() == 0) {
			String err = "Cannot send email notifications when no SMTP server host is defined!";
			LOG.error(err);
			throw new NotificationRecipientException(err);
		}
		props.setProperty("mail.smtp.host", smtpSettings.getHost());
		session = Session.getInstance(props, null);
	}

	private void constructMsg() throws MessagingException {
		message = new MimeMessage(session);
		message.setFrom(new InternetAddress(smtpSettings.getSenderAddress().length() == 0 ? "sagealert@" + smtpSettings.getHost() : smtpSettings.getSenderAddress()));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailSettings.getAddress()));
		message.setSubject("SageAlert: " + event.getSubject());
		message.setSentDate(new Date());
		String txt;
		if(emailSettings.getMsgType().equals("short"))
			txt = event.getShortDescription();
		else if(emailSettings.getMsgType().equals("medium"))
			txt = event.getMediumDescription();
		else
			txt = event.getLongDescription();
		message.setText(txt);
	}

	private void sendMessage() throws MessagingException {
		Transport t = session.getTransport();
		if(smtpSettings.getUser().length() > 0 && smtpSettings.getPwd().length() > 0)
			t.connect(smtpSettings.getUser(), smtpSettings.getPwd());
		else
			t.connect();
		t.sendMessage(message, message.getAllRecipients());
		LOG.info("Email for '" + event.getSubject() + "' event sent successfully to '" + emailSettings.getAddress() + "'");
	}

	@Override
	public String toString() {
		return emailSettings.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((emailSettings == null) ? 0 : emailSettings.hashCode());
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
		if (!(obj instanceof EmailServer)) {
			return false;
		}
		EmailServer other = (EmailServer) obj;
		if (emailSettings == null) {
			if (other.emailSettings != null) {
				return false;
			}
		} else if (!emailSettings.equals(other.emailSettings)) {
			return false;
		}
		return true;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void setSettings(NotificationServerSettings settings) {
		emailSettings = (EmailSettings)settings;
	}
}
