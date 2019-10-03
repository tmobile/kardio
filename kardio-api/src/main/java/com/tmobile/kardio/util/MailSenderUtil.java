/*******************************************************************************
 * Copyright 2019 T-Mobile USA, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * See the LICENSE file for additional language around disclaimer of warranties.
 * Trademark Disclaimer: Neither the name of "T-Mobile, USA" nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 ******************************************************************************/
package com.tmobile.kardio.util;

import com.tmobile.kardio.bean.Subscription;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * This class handles the mail sending feature and sending message to slack
 * 
 */
@Service
@PropertySource("classpath:application.properties")
public class MailSenderUtil {

    private static final Logger Log = Logger.getLogger(MailSenderUtil.class);

    @Value("${mail.subscribe.template}")
    private String mailSubscribeTemplate;

    @Value("${mail.unsubscribe.template}")
    private String mailUnSubscribeTemplate;

    @Value("${mail.subscribe.link}")
    private String mailSubscribeLink;

    @Value("${mail.unsubscribe.link}")
    private String mailUnSubscribeLink;

    @Value("${mail.server.ip}")
    private String mailServerIP;

    @Value("${mail.server.port}")
    private String mailServerPort;
    
    @Value("${mail.server.auth.username}")
    private String mailAuthUserName;
    
    @Value("${mail.server.auth.password}")
    private String mailAuthPassword;

    @Value("${mail.from.email}")
    private String fromEmailAddress;

    /**
     * Send message to Webhook URL
     * 
     * @param subscription
     * @throws IOException
     */
    public void sentMessageToWebhook(String webhookUrl, String message) throws IOException {
        
        Log.debug("Webhook Message : " + message);
        URL myurl = new URL(webhookUrl);
        HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-length", String.valueOf(message.length()));
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.writeBytes(message);
        output.flush();
        output.close();

        BufferedInputStream inStream = new BufferedInputStream(con.getInputStream());
        byte[] b = new byte[256];
        inStream.read(b);
        Log.debug("Response : " + new String(b));
    }

    /**
     * Function to send mail for subscription
     * 
     * @param subscription
     * @param subscribe
     */
    public void sendMailForSubscription(Subscription subscription, boolean subscribe) {
        String mailMessage = subscribe ? mailSubscribeTemplate : mailUnSubscribeTemplate;

        String params = "authToken=" + subscription.getAuthToken();

        mailMessage = mailMessage.replace("<component_name>", subscription.getComponentName());
        mailMessage = mailMessage.replace("<validationLink>", mailSubscribeLink);
        mailMessage = mailMessage.replace("<unsubscribe>", mailUnSubscribeLink);
        mailMessage = mailMessage.replace("<params>", params);
        this.sendMail(mailMessage, subscription.getSubsciptionVal(), subscribe ? "Subscribe your email" : "Un Subscribe your email");
    }

    /**
     * Sent Mail
     * 
     * @param Message
     */
    public void sendMail(String messageText, String toMail, String mailSubject) {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailServerIP);
        props.put("mail.smtp.port", mailServerPort);
        Session session = null;
		if (mailAuthUserName != null && mailAuthUserName.length() > 0 && mailAuthPassword != null
				&& mailAuthPassword.length() > 0) {
			props.put("mail.smtp.auth", "true");
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailAuthUserName, mailAuthPassword);
				}
			};
			session = Session.getDefaultInstance(props, auth);
		} else {
			props.put("mail.smtp.auth", "false");
			session = Session.getInstance(props);
		}

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmailAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
            message.setSubject(mailSubject);
            message.setText(messageText);
            Transport.send(message);
        } catch (MessagingException messagingException) {
            Log.error("Error in Mail Sending: " + messagingException);
            throw new RuntimeException(messagingException.getMessage());
        }

        Log.debug("Mail Sent to: " + toMail);
    }
}
