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
package com.tmobile.kardio.surveiller.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.SubscriptionVO;

/**
 * Send Mail or Slack Alert For HealthCheckVos if a subscription is available.
 */
public class MailSenderUtil {

	private static final String ENCODE_UTF_8 = "UTF-8";

	private static final Logger logger = Logger.getLogger(MailSenderUtil.class);
	
	private static PropertyUtil propertyUtil = PropertyUtil.getInstance();

	private MailSenderUtil() {}
	/**
	 * Send Mail or Slack Alert For HealthCheckVos if a subscription is available.
	 * 
	 * @param stsChgList
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void sendMailForHealthCheckVos(List<HealthCheckVO> stsChgList) throws SQLException{
		logger.info("************** STARTED Mail Sending **************");
		if(stsChgList.size() == 0){
			logger.info("************** END Mail Sending - No Status change**************");
			return;
		}
		Set<Integer> parentAndCompIdSet = getParentAndCompIdSet(stsChgList);
		List<SubscriptionVO> subscriptionVOs = DBQueryUtil.getAllSubscriptions(parentAndCompIdSet);
		
		PropertyUtil prop = PropertyUtil.getInstance();
		String mailTemplate = prop.getValue(SurveillerConstants.MAIL_MESSAGE_TEMPLATE);
		String mailSubjectTemplate = prop.getValue(SurveillerConstants.MAIL_SUBJECT_TEMPLATE);
		if(!stsChgList.isEmpty()){
			for(HealthCheckVO hVo : stsChgList){
				for(SubscriptionVO sVo : subscriptionVOs){
					if(isSubscriptionForComponent(hVo, sVo)){
						String mailMsg = mailTemplate;
						String mailSub = mailSubjectTemplate;
						
						mailSub = mailSub.replace("<EnvironmentName>", hVo.getEnvironmentName());
						mailSub = mailSub.replace("<RegionName>", hVo.getRegionName());
						mailSub = mailSub.replace("<Status>", hVo.getStatus().getStatus().getStatusDesc());
						String serviceName = hVo.getComponent().getParentComponentName() + "/" + hVo.getComponent().getComponentName();
						mailSub = mailSub.replace("<ServiceName>",  serviceName);
						
						mailMsg = mailMsg.replace("<parent_component_name>", hVo.getComponent().getParentComponentName());
						mailMsg = mailMsg.replace("<component_name>", hVo.getComponent().getComponentName());
						mailMsg = mailMsg.replace("<status>", hVo.getStatus().getStatus().getStatusDesc());
						mailMsg = mailMsg.replace("<message>", hVo.getFailureStatusMessage());
						mailMsg = mailMsg.replace("<EnvironmentName>", hVo.getEnvironmentName());
						mailMsg = mailMsg.replace("<RegionName>", hVo.getRegionName());
						logger.debug("Subject: " + mailSub + "\nMessage: " + mailMsg);
						switch (sVo.getSubscriptionType()) {
							case SurveillerConstants.SUBSCRIPTION_TYPE_EMAIL:
								MailSenderUtil.sendMail(mailMsg, sVo.getSubscriptionValue(), mailSub);
								break;
							case SurveillerConstants.SUBSCRIPTION_TYPE_SLACK_WEB_HOOK:
								try {
									MailSenderUtil.sendSlackAlertByWebHook(hVo, sVo.getSubscriptionValue(), mailSub);
								} catch (IOException e) {
									logger.error("Error in Senting Slack Message. URL = " + sVo.getSubscriptionValue(), e);
								}
								break;
							case SurveillerConstants.SUBSCRIPTION_TYPE_SLACK_CHANNEL:
								MailSenderUtil.sendMessageToSlackChannel(hVo, sVo.getSubscriptionValue(), mailSub);
								break;
							default:
								break;
						}
					}
				}
			}
		}
		logger.info("************** END OF Mail Sending **************");
	}

	/**
	 * Is this subscription For given Component.
	 * 
	 * @param hVo
	 * @param sVo
	 * @return
	 */
	private static boolean isSubscriptionForComponent(HealthCheckVO hVo, SubscriptionVO sVo) {
		if( (hVo.getComponent().getComponentId() == sVo.getComponentId() || hVo.getComponent().getParentComponentId() == sVo.getComponentId() )
				&& hVo.getEnvironmentId() == sVo.getEnvironmentId() ){
			return true;
		}
		if(sVo.getComponentId() == 0 && sVo.getGlobalComponentTypeId() == 0 && hVo.getEnvironmentId() == sVo.getEnvironmentId()){
			return true;
		}
		if(sVo.getComponentId() == 0 && sVo.getGlobalComponentTypeId() == hVo.getComponentType().getComponentTypeId()
				&& hVo.getEnvironmentId() == sVo.getEnvironmentId()){
			return true;
		}
		return false;
	}

	/**
	 * Sends the slack Alert.
	 * 
	 * @param hVo
	 * @param slackURL
	 * @throws IOException
	 */
	private static void sendSlackAlertByWebHook(HealthCheckVO hVo, String slackURL, String subject) throws IOException {
		String slackMessage = "{\"username\": \"CCP Service Health Dashboard\", \"text\": \""
								+ subject + ".\n"
								+ "Current Status : " + hVo.getStatus().getStatus().getStatusDesc()
								+ "\nMessage : " + (hVo.getFailureStatusMessage() == null ? "" : hVo.getFailureStatusMessage().replace('\"', '\''))
								+ "\n\", \"icon_emoji\": \":satellite_antenna:\"}";
		
		logger.debug("Slack Message : " + slackMessage);
		URL myurl = new URL(slackURL);
		HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-length", String.valueOf(slackMessage.length())); 
		con.setRequestProperty("Content-Type","application/json"); 
		con.setDoOutput(true); 
		con.setDoInput(true); 

		DataOutputStream output = new DataOutputStream(con.getOutputStream());  
		output.writeBytes(slackMessage);
		output.flush();
		output.close();

		logger.debug("Message pushed to slack channnel : " + slackURL);
		logger.debug("Resp Code:" + con.getResponseCode()); 
		logger.debug("Resp Message:" + con.getResponseMessage()); 
	}

	/*
	 * Function to send slack alert to slack channel by rest API
	 */
	public static void sendMessageToSlackChannel(HealthCheckVO hVo, String slackChannelName, String subject) {
		ObjectMapper mapper = new ObjectMapper();
		String postSlackMessageURL;
		
		String slackMessage = subject + ".\n"
		+ "Current Status : " + hVo.getStatus().getStatus().getStatusDesc()
		+ "\nMessage : " + (hVo.getFailureStatusMessage() == null ? "" : hVo.getFailureStatusMessage().replace('\"', '\''));
		try {
			postSlackMessageURL = propertyUtil.getValue(SurveillerConstants.CONFIG_SLACK_URL) + SurveillerConstants.POST_SLACK_MESSAGE + "?token=" + propertyUtil.getValue(SurveillerConstants.CONFIG_SLACK_TOKEN)
					+ "&channel=" + URLEncoder.encode(slackChannelName, ENCODE_UTF_8) 
					+ "&text=" + URLEncoder.encode(slackMessage, ENCODE_UTF_8)
					+ "&username=" + URLEncoder.encode("CCP Service Health Dashboard" , ENCODE_UTF_8)
					+ "&icon_emoji=" + URLEncoder.encode(":satellite_antenna:", ENCODE_UTF_8);
			String postSlackMessageJson = RestCommunicationHandler.postRequest(postSlackMessageURL, false, null, null);
			JsonNode rootMsgNode = mapper.readTree(postSlackMessageJson);
			JsonNode okNode = rootMsgNode.get("ok");
			if (okNode.asBoolean()) {
				logger.debug("Message pushed to slack channnel: " + slackChannelName);
			} else {
				logger.info("Message pushed to slack channnel Failed: " + slackChannelName);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException in Senting Slack Messageto Channel URL = " + slackChannelName, e);
		} catch (IOException e) {
			logger.error("IOException in Senting Slack Messageto Channel URL = " + slackChannelName, e);
		}
	}

	/**
	 * Get ParentId And Component Id Set from stsChgList.
	 * 
	 * @param stsChgList
	 * @return
	 */
	private static Set<Integer> getParentAndCompIdSet(List<HealthCheckVO> stsChgList) {
		Set<Integer> parentAndCompIdSet = new HashSet<Integer>();
		for(HealthCheckVO hVo : stsChgList){
			parentAndCompIdSet.add(hVo.getComponent().getComponentId());
			parentAndCompIdSet.add(hVo.getComponent().getParentComponentId());
		}
		return parentAndCompIdSet;
	}

	/**
	 * Send Mail Alert.
	 * 
	 * @param Message
	 */
	public static void sendMail(String messageText, String toMail, String subject){
		
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", propertyUtil.getValue(SurveillerConstants.MAIL_SERVER_IP));
        props.put("mail.smtp.port", propertyUtil.getValue(SurveillerConstants.MAIL_SERVER_PORT));
        Session session = null;
        String mailAuthUserName = propertyUtil.getValue(SurveillerConstants.MAIL_SERVER_USERNAME);
        String mailAuthPassword = propertyUtil.getValue(SurveillerConstants.MAIL_SERVER_PASSWORD);
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
			message.setFrom(new InternetAddress(propertyUtil.getValue(SurveillerConstants.MAIL_FROM_ADDRESS) ));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
			message.setSubject(subject);
			message.setText(messageText);
			Transport.send(message);
		} catch (MessagingException me) {
			logger.error("Error in Mail Sending: " +  me);
			throw new RuntimeException(me.getMessage());
		}

		logger.debug("Mail Sent to: " + toMail);
	}
	
	/**
	 * Sends the Alert to Webhook.
	 * 
	 * @param hVo
	 * @param webhookUrl
	 * @param subject
	 * @throws IOException
	 */
	private static void sendAlertTo1ConsoleWebHook(HealthCheckVO hVo, String webhookUrl, String subject) throws IOException {
		
		JSONObject js = new JSONObject();
		js.put("from", "Service Health Dashboard");
		js.put("subject", subject);
		js.put("application", hVo.getComponent().getParentComponentName());
		js.put("service", hVo.getComponent().getComponentName());
		js.put("cluster", hVo.getEnvironmentName());
		js.put("status", hVo.getStatus().getStatus().getStatusDesc());
		js.put("message", hVo.getFailureStatusMessage() == null ? "" : hVo.getFailureStatusMessage());
		
		logger.info("json : " + js);
		
		URL myurl = new URL(webhookUrl);
		HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-length", String.valueOf(js.length())); 
		con.setRequestProperty("Content-Type","application/json"); 
		con.setDoOutput(true); 
		con.setDoInput(true); 

		DataOutputStream output = new DataOutputStream(con.getOutputStream());  
		output.writeBytes(js.toString());
		output.flush();
		output.close();
		
		logger.debug("Message pushed to IC Webhook : " + webhookUrl);
		logger.debug("Resp Code:" + con.getResponseCode()); 
		logger.debug("Resp Message:" + con.getResponseMessage()); 
	}
}
