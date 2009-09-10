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

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.google.code.sagetvaddons.sagealert.client.EmailSettings;
import com.google.code.sagetvaddons.sagealert.client.SmtpSettings;

/**
 * Event handler that sends events via email
 * @author dbattams
 * @version $Id$
 */
final class EmailServer implements SageEventHandler {

	static private final Logger LOG = Logger.getLogger(EmailServer.class);
	
	private EmailSettings emailSettings;
	private SmtpSettings smtpSettings;
	private Session session;
	private SageEvent event;
	private MimeMessage message;
	
	/**
	 * Constructor
	 * @param settings The details of where the event is to be sent (i.e. email address)
	 */
	EmailServer(EmailSettings settings) {
		emailSettings = settings;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEventHandler#getSettings()
	 */
	@Override
	public EmailSettings getSettings() {
		return emailSettings;
	}

	/* (non-Javadoc)
	 * @see com.google.code.sagetvaddons.sagealert.server.SageEventHandler#onEvent(com.google.code.sagetvaddons.sagealert.server.SageEvent)
	 */
	@Override
	public void onEvent(SageEvent e) {
		event = e;
		try {
			connectToSmtp();
			constructMsg();
			sendMessage();
		} catch(NotificationRecipientException x) {
			LOG.trace("Invalid SMTP settings", x);
			LOG.error(x);
			LOG.error("Email for '" + event.getSubject() + "' event FAILED to '" + emailSettings.getAddress() + "'");
		} catch(MessagingException x) {
			LOG.trace("Error sending email", x);
			LOG.error(x);
			LOG.error("Email for '" + event.getSubject() + "' event FAILED to '" + emailSettings.getAddress() + "'");
		}
	}
	
	private void connectToSmtp() throws NotificationRecipientException {
		smtpSettings = DataStore.getInstance().getSmtpSettings();
		Properties props = new Properties();
		
		props.setProperty("mail.transport.protocol", "smtp");
		if(smtpSettings.useSsl())
			props.setProperty("mail.smtp.ssl.enable", "true");
		
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
		message.setSubject(event.getSubject());
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
}
