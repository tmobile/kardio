/*******************************************************************************
 * Copyright 2019 T-Mobile, Inc. or its affiliates. All Rights Reserved.
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
 ******************************************************************************/
package com.tmobile.kardio.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.bean.AppSession;
import com.tmobile.kardio.bean.AvailabilityData;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.ComponentMessages;
import com.tmobile.kardio.bean.ContainerStatus;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.HistoryResponse;
import com.tmobile.kardio.bean.K8sContainerStatus;
import com.tmobile.kardio.bean.Messages;
import com.tmobile.kardio.bean.StatusResponse;
import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.bean.TpsLatency;
import com.tmobile.kardio.bean.TpsLatencyHistory;
import com.tmobile.kardio.bean.User;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.dao.AlertSubscribeDao;
import com.tmobile.kardio.dao.ApiStatusDao;
import com.tmobile.kardio.dao.AppSessionDao;
import com.tmobile.kardio.dao.AvailabilityPercentageDao;
import com.tmobile.kardio.dao.ComponentDao;
import com.tmobile.kardio.dao.ContainerStatusDao;
import com.tmobile.kardio.dao.CounterMatrixDao;
import com.tmobile.kardio.dao.EnvironmentDao;
import com.tmobile.kardio.dao.K8sApiStatusDao;
import com.tmobile.kardio.dao.K8sContainerStatusDao;
import com.tmobile.kardio.dao.K8sPodsStatusDao;
import com.tmobile.kardio.dao.K8sTpsLatencyDao;
import com.tmobile.kardio.dao.RegionHistoryDao;
import com.tmobile.kardio.dao.RegionMessageDao;
import com.tmobile.kardio.dao.RegionStatusDao;
import com.tmobile.kardio.dao.TpsLatencyDao;
import com.tmobile.kardio.dao.TpsLatencyStatusDao;
import com.tmobile.kardio.db.entity.AlertSubscriptionEntity;
import com.tmobile.kardio.db.entity.AppSessionEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.ContainerStatusEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;
import com.tmobile.kardio.exceptions.AppSessionExpiredException;
import com.tmobile.kardio.exceptions.ValidationFailedException;
import com.tmobile.kardio.restservice.RestCommunicationHandler;
import com.tmobile.kardio.util.MailSenderUtil;

/**
 * Service layer class to handle Dao methods
 */
@Service
@PropertySource("classpath:application.properties")
public class RegionStatusServiceImpl implements RegionStatusService {

	private static final long CONVERT_TO_MINUTES = 60000;

	private static Logger log = LoggerFactory.getLogger(Constants.class);

    @Autowired
    private RegionStatusDao regStatusDao;

    @Autowired
    private RegionHistoryDao regionHistoryDao;

    @Autowired
    private RegionMessageDao regionMessageDao;

    @Autowired
    private AppSessionDao appSessionDao;

    @Autowired
    private EnvironmentDao environmentDao;

    @Autowired
    private AlertSubscribeDao alertSubscibeDao;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private CounterMatrixDao counterMatrixDao;

    @Autowired
    private AvailabilityPercentageDao availabilityPercentageDao;
    
    @Autowired
    private ComponentDao componentDao;
    
    @Autowired
	private ContainerStatusDao containerStatusDao; 
    
    @Autowired
	private ApiStatusDao apiStatusDao; 
    
    @Autowired
    private TpsLatencyDao tpsLatencyDao;
    
    @Autowired
    private K8sTpsLatencyDao k8sTpsLatencyDao;
    
    @Autowired
 	private K8sApiStatusDao k8sApiStatusDao; 
    
    @Autowired
    private K8sContainerStatusDao k8sContainerStatusDao;
    
    @Autowired
    private K8sPodsStatusDao k8sPodsStatusDao;

    @Value("${slack.url}")
    private String slackURL;

    @Value("${slack.auth.token}")
    private String slackAuthToken;

    @Value("${mail.subscribe.valid.domain}")
    private String emailValidDomain;

    @Value("${slack.webhook.validateurl}")
    private String slackWebhookValidURL;
    
    @Value("${oneconsole.webhook.validateurl}")
    private String oneConsoleWebhookValidURL;

    /**
     * Service layer method to handle Dao method return List<Component>
     */
    public StatusResponse getComponentRegion(String environment) {
        StatusResponse response = regStatusDao.getCompRegStatus(environment);
        return response;
    }
    
    public void setSlackWebhookValidURL(String validURL) {
    	this.slackWebhookValidURL = validURL;
    }

    public void loadMessages(String environmentName, String messageType, String message, String authToken) throws InstantiationException {
        AppSession appSession = makeAppSessionFromEntity(appSessionDao.getAppSession(authToken));
        if (appSession.getAuthToken() == null
                || getDateDiff(appSession.getSessionStartTime(), new Date()) > Constants.SESSION_EXPIRATION_TIME_MINUTES) {
            if (appSession.getAuthToken() != null) {
                appSessionDao.deleteAppSession(authToken);
            }
            throw new AppSessionExpiredException("Your current session has expired. Please login again");
        }
        regStatusDao.loadMessages(environmentName, messageType, message);
    }
    /**
     * Get the seven days status History of all the Services
     * 
     * @param environment
     * @return
     * @throws ParseException 
     */
    public HistoryResponse getRegionStatusHistory(String environment) throws ParseException {
        HistoryResponse compList = regionHistoryDao.getRegionStatusHistory(environment);
        return compList;
    }

    @Override
    public void loadComponentMessages(ComponentMessages compMessage) {
        AppSession appSession = makeAppSessionFromEntity(appSessionDao.getAppSession(compMessage.getAuthToken()));
        if (appSession.getAuthToken() == null
                || getDateDiff(appSession.getSessionStartTime(), new Date()) > Constants.SESSION_EXPIRATION_TIME_MINUTES) {
            if (appSession.getAuthToken() != null) {
                appSessionDao.deleteAppSession(compMessage.getAuthToken());
            }
            throw new AppSessionExpiredException("Your current session has expired. Please login again");
        }
        if (compMessage.getMessageId() == 0) {
            regionMessageDao.saveCompMessage(compMessage);
        } else {
            regionMessageDao.updateCompMessage(compMessage);
        }

    }

    /**
     * Get a diff between two dates.
     * 
     * @param startTime
     *            the oldest date
     * @param currentTime
     *            the newest date
     * @param timeUnit
     *            the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date startTime, Date currentTime) {
        long diffInMillies = currentTime.getTime() - startTime.getTime();
        return diffInMillies / CONVERT_TO_MINUTES;
    }
    /**
     * @param environmentName
     * @param componentId
     * @param region
     */
    @Override
    public List<Messages> getComponentMessage(String environmentName, String componentId, String region) {
        List<Messages> msgList = regionMessageDao.getCompRegionMessage(environmentName, componentId, region);
        return msgList;
    }

    /**
     * Function to login user by creating an application session for user
     * 
     * @param user
     *            user details for which the app session is created
     */
    @Override
    public void doLoginForUser(User user) {
        appSessionDao.saveAppSession(user);
    }

    /**
     * Function to logout user by deleting the application session for user
     * 
     * @param authToken
     *            token for which the app session is logged out off
     */
    @Override
    public void doLogOutForUser(String authToken) {
        appSessionDao.deleteAppSession(authToken);
    }

    /**
     * Function to get all environments in list that is not locked
     */
    @Override
    public List<Environment> getEnvironments() {
        return AdminServiceImpl.makeEnironmentFromEntity(environmentDao.getEnvironments());
    }

    /**
     * Function to subscribe to alerts related to component for user
     */
    @Override
    public void subscribeAlert(Subscription subscription) {
        if (subscription.getSubsciptionType().equalsIgnoreCase("email") && !checkValidDomainForEmail(subscription.getSubsciptionVal())) {
            throw new IllegalArgumentException("The email id provided given has an invalid domain name");
        }
        if (slackWebhookValidURL != null && slackWebhookValidURL.trim().length() != 0 && subscription.getSubsciptionType().equalsIgnoreCase("slack")
                && subscription.getSubsciptionVal() != null
                && !subscription.getSubsciptionVal().toLowerCase().startsWith(slackWebhookValidURL.toLowerCase())) {
            throw new ValidationFailedException("Slack URL must Start with " + slackWebhookValidURL);
        }
        if (oneConsoleWebhookValidURL != null && oneConsoleWebhookValidURL.trim().length() != 0 && subscription.getSubsciptionType().equalsIgnoreCase("oneConsoleWebhook")
                && subscription.getSubsciptionVal() != null
                && !subscription.getSubsciptionVal().toLowerCase().startsWith(oneConsoleWebhookValidURL.toLowerCase())) {
            throw new ValidationFailedException("One Console URL must Start with " + oneConsoleWebhookValidURL);
        }
        subscription.setAuthToken("SUB" + new Date().getTime());
        int envId = environmentDao.getEnironmentIdFromName(subscription.getEnvironmentName());
        subscription.setEnvironmentId(envId);

        if (checkForDuplicate(subscription)) {
            throw new IllegalArgumentException("Provided email id/slack url is already subscribed for this component");
        }
        if (subscription.getSubsciptionType().equalsIgnoreCase("slack")) {
        	try {
            	String slackMessage = "{\"username\": \"CCP Service Health Dashboard\", \"text\": \"" + "Successfully subscribed for Status change of "
                        + subscription.getComponentName() + "[" + subscription.getEnvironmentName() + "]\", \"icon_emoji\": \":satellite_antenna:\"}";
                mailSenderUtil.sentMessageToWebhook(subscription.getSubsciptionVal(), slackMessage);
            } catch (IOException e) {
                log.error("Subscription Failed : Unable to sent welcome message", e);
                throw new IllegalArgumentException("Subscription Failed : Unable to sent welcome message");
            }
        } else if (subscription.getSubsciptionType().equalsIgnoreCase("slackChannel")) {
            if (!validateSlackChannel(subscription.getSubsciptionVal())) {
                log.error("Subscription Failed : No such public channel : " + subscription.getSubsciptionVal());
                throw new IllegalArgumentException("Subscription Failed : No such public channel available");
            }
            try {
                String postSlackMessageURL = slackURL + Constants.POST_SLACK_MESSAGE + "?token=" + slackAuthToken + "&channel="
                        + URLEncoder.encode(subscription.getSubsciptionVal(), StandardCharsets.UTF_8.name()) + "&text="
                        + URLEncoder.encode("Successfully subscribed for Status change of " + subscription.getComponentName(), StandardCharsets.UTF_8.name())
                        + "&username=" + URLEncoder.encode("CCP Service Health Dashboard", StandardCharsets.UTF_8.name()) + "&icon_emoji="
                        + URLEncoder.encode(":satellite_antenna:", StandardCharsets.UTF_8.name());
                RestCommunicationHandler.postRequest(postSlackMessageURL, false, null);
            } catch (IOException e) {
                log.error("Subscription Failed : Unable to sent welcome message", e);
                throw new IllegalArgumentException("Subscription Failed : Unable to sent welcome message");
            }
        } else if (subscription.getSubsciptionType().equalsIgnoreCase("oneConsoleWebhook")) {
            try {
            	JSONObject js = new JSONObject();
        		js.put("from", "CCP Service Health Dashboard");
        		js.put("subject", "Successfully subscribed for Status change of "+subscription.getComponentName() );
        		js.put("application", subscription.getComponentName());
        		js.put("cluster", subscription.getEnvironmentName());
                mailSenderUtil.sentMessageToWebhook(subscription.getSubsciptionVal(), js.toString());
            } catch (IOException e) {
                log.error("Subscription Failed : Unable to sent welcome message", e);
                throw new IllegalArgumentException("Subscription Failed : Unable to sent welcome message");
            }
        }
        int subsType = Constants.SUBSCRIPTION_TYPE_ID_EMAIL;
        int validationLevel = 0;
        if (subscription.getSubsciptionType().equalsIgnoreCase(Constants.SUBSCRIPTION_TYPE_DESC_SLACK_WEBHOOK)) {
            subsType = Constants.SUBSCRIPTION_TYPE_ID_SLACK_WEBHOOK;
            validationLevel = 1;
        } else if (subscription.getSubsciptionType().equalsIgnoreCase(Constants.SUBSCRIPTION_TYPE_DESC_SLACK_CHNL)) {
            subsType = Constants.SUBSCRIPTION_TYPE_ID_SLACK_CHNL;
            validationLevel = 1;
        }else  if (subscription.getSubsciptionType().equalsIgnoreCase(Constants.SUBSCRIPTION_TYPE_DESC_ONECONSOLE_WEBHOOK)) {
            subsType = Constants.SUBSCRIPTION_TYPE_ID_ONECONSOLE_WEBHOOK;
            validationLevel = 1;
        } 
        alertSubscibeDao.saveSubscription(subscription, subsType, validationLevel, false);
        if (subscription.getSubsciptionType().equalsIgnoreCase("email")) {
            try {
                mailSenderUtil.sendMailForSubscription(subscription, true);
            } catch (Exception e) {
                log.error("Mailing Failed : Unable to sent validation mail ", e);
                throw new IllegalArgumentException("Unable to sent validation mail to this email");
            }
        }
    }

    /**
     * Function to validate slack channel
     * 
     * @param slackChannelName
     * @return
     * @throws Exception
     */
    @Override
    public boolean validateSlackChannel(String slackChannelName) {
        String channelListURL = slackURL + Constants.GET_SLACK_CHANNEL_LIST_API + "?token=" + slackAuthToken;
        String channelListJson = null;
        boolean isChannelPublic = false;
        try {
            channelListJson = RestCommunicationHandler.getResponse(channelListURL, false, null);
            log.debug("ChannelListJson " + channelListJson);
        } catch (Exception e) {
            log.error("Subscription Failed : Unable to validate Channel", e);
            throw new IllegalArgumentException("Subscription Failed : Unable to validate Channel");
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(channelListJson);
        } catch (IOException e) {
            log.error("Subscription Failed : Unable to validate Channel(Invalid JSON response)", e);
            throw new IllegalArgumentException("Subscription Failed : Unable to validate Channel(Invalid JSON response)");
        }
        ArrayNode channelsNode = (ArrayNode) rootNode.get("channels");
        Iterator<JsonNode> channelsIterator = channelsNode.elements();
        while (channelsIterator.hasNext()) {
            JsonNode channelNode = channelsIterator.next();
            JsonNode channelNmeNode = channelNode.get("name");
            String channelName = channelNmeNode.asText();
            if (channelName.equals(slackChannelName)) {
                isChannelPublic = true;
                break;
            }
        }
        return isChannelPublic;
    }

    /**
     * Function to unsubscribe to alerts related to a component for user
     */
    @Override
    public void unsubscribeAlert(Subscription subscription) {
        int envId = environmentDao.getEnironmentIdFromName(subscription.getEnvironmentName());
        subscription.setEnvironmentId(envId);

        String authToken = alertSubscibeDao.getSubscriptionToken(subscription.getSubsciptionVal(), subscription.getComponentId(), envId);
        if (authToken == null) {
            throw new IllegalArgumentException("Provided Email id is not yet subscribed for this component");
        }
        subscription.setAuthToken(authToken);
        if (subscription.getSubsciptionType().equals("email")) {
            mailSenderUtil.sendMailForSubscription(subscription, false);
        } else {
            alertSubscibeDao.deleteSububscription(subscription.getAuthToken());
        }

    }

    /**
     * Function to check the validity of the domain name for email id.
     * 
     * @param emailId
     * @return
     */
    @Override
    public boolean checkValidDomainForEmail(String emailId) {
        if (emailId == null || emailId.equals("") || emailId.indexOf("@") <= 0) {
            throw new ValidationFailedException("Please enter an valid email id");
        }
        if (emailValidDomain == null || emailValidDomain.trim().length() == 0) {
            return true;
        }
        String domain = emailId.substring(emailId.indexOf("@") + 1);
        return emailValidDomain.equalsIgnoreCase(domain) ? true : false;
    }

    /**
     * check For Duplicate subscription
     * 
     * @param subscription
     * @return
     */
    private boolean checkForDuplicate(Subscription subscription) {
        return alertSubscibeDao.checkforDuplicates(subscription.getComponentId(), subscription.getEnvironmentId(), subscription.getSubsciptionVal());
    }

    /**
     * Function to delete subscription
     */
    @Override
    public void deleteSubscription(String authToken) {
        alertSubscibeDao.deleteSububscription(authToken);
    }

    /**
     * Function to confirm subscription
     */
    @Override
    public void confirmSubscription(String authToken) {
        alertSubscibeDao.confirmSubscription(authToken);
    }

    /**
     * Get the list of emails subscribed for the given component.
     */
    @Override
    public List<Subscription> getSubscribedEmailIdList(int componentId, String environmentName) {
        int environmentId = environmentDao.getEnironmentIdFromName(environmentName);
        List<AlertSubscriptionEntity> alrtSubList = alertSubscibeDao.getSubscribedEmailIdList(componentId, environmentId);

        return makeSubscriptionList(alrtSubList);
    }

    /**
     * Make List of Subscription from List of AlertSubscriptionEntity
     * 
     * @param alrtSubList
     * @return
     */
    public List<Subscription> makeSubscriptionList(List<AlertSubscriptionEntity> alrtSubList) {
        ArrayList<Subscription> subList = new ArrayList<Subscription>();
        for (AlertSubscriptionEntity alrtSubscription : alrtSubList) {
            Subscription subresp = new Subscription();
            subresp.setAlertSubscriptionId(alrtSubscription.getAlertSubscriptionId());
            subresp.setAuthToken(alrtSubscription.getAuthToken());
            if (alrtSubscription.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_ID_EMAIL) {
                subresp.setSubsciptionType(Constants.SUBSCRIPTION_TYPE_DESC_EMAIL);
            } else if (alrtSubscription.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_ID_SLACK_WEBHOOK) {
                subresp.setSubsciptionType(Constants.SUBSCRIPTION_TYPE_DESC_SLACK_WEBHOOK);
            } else if (alrtSubscription.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_ID_SLACK_CHNL) {
                subresp.setSubsciptionType(Constants.SUBSCRIPTION_TYPE_DESC_SLACK_CHNL);
            }else if (alrtSubscription.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_ID_SLACK_WEBHOOK) {
                subresp.setSubsciptionType(Constants.SUBSCRIPTION_TYPE_DESC_ONECONSOLE_WEBHOOK);
            }
            subresp.setSubsciptionVal(alrtSubscription.getSubscriptionVal());
            subresp.setEnvironmentId(alrtSubscription.getEnvironment().getEnvironmentId());
            subresp.setEnvironmentName(alrtSubscription.getEnvironment().getEnvironmentName());
            subresp.setActivationStatus(alrtSubscription.getValidationLevel() == 1 ? true : false);
            subresp.setPlatform(alrtSubscription.getPlatform() == null ? "All" : alrtSubscription.getPlatform());
            if (alrtSubscription.getComponent() != null) {
                subresp.setComponentId(alrtSubscription.getComponent().getComponentId());
                subresp.setComponentName(alrtSubscription.getComponent().getComponentName());
            } else {
                subresp.setGlobalSubscriptionTypeId(alrtSubscription.getGlobalComponentTypeId());
                if (alrtSubscription.getGlobalComponentTypeId() == Constants.GLOBAL_SUBSCRIPTION_TYPE_ID_INFRA) {
                    subresp.setGlobalSubscriptionType(Constants.GLOBAL_SUBSCRIPTION_TYPE_DESC_INFRA);
                } else if (alrtSubscription.getGlobalComponentTypeId() == Constants.GLOBAL_SUBSCRIPTION_TYPE_ID_APP) {
                    subresp.setGlobalSubscriptionType(Constants.GLOBAL_SUBSCRIPTION_TYPE_DESC_APP);
                } else if (alrtSubscription.getGlobalComponentTypeId() == Constants.GLOBAL_SUBSCRIPTION_TYPE_ID_BOTH) {
                    subresp.setGlobalSubscriptionType(Constants.GLOBAL_SUBSCRIPTION_TYPE_DESC_BOTH);
                }
            }

            subList.add(subresp);
        }
        return subList;
    }

    /**
     * Gets the Counters Matrix for the Env
     */
    @Override
    public List<Counters> getCountersMatrix(String environment, String platform) {
        List<Counters> listOfCounters = null;
        if (environment == null || environment.equals("") || environment.trim().length() == 1) {
            listOfCounters = counterMatrixDao.getCounters(platform);
        } else {
            listOfCounters = counterMatrixDao.getEnvironmentCounters(environment, platform);
        }
        return listOfCounters;
    }

    /**
     * Gets the list of messages related to a component for a particular date
     */
    @Override
    public List<Messages> getComponentMessage(String environmentName, String componentId, String region, String logDate) {
        List<Messages> msgList = regionMessageDao.getCompRegionMessage(environmentName, componentId, region, logDate);
        return msgList;
    }

    /**
     * Get All the component availability % based on the input
     * 
     * @throws ParseException
     */
    @Override
    public List<AvailabilityData> getAvailabilityPercentage(String environment, String interval,String platform, String region) throws ParseException {
        List<AvailabilityData> avlList = availabilityPercentageDao.getAllAvailabilityPercentage(environment, interval,platform, region);
        return avlList;
    }

    /**
     * Function to convert AppSessionEntity into AppSession
     * 
     * @param appSessionEntity
     * @return
     */
    public AppSession makeAppSessionFromEntity(AppSessionEntity appSessionEntity) {
        AppSession appSession = new AppSession();
        appSession.setAppSessionId(appSessionEntity.getAppSessionId());
        appSession.setAuthToken(appSessionEntity.getAuthToken());
        appSession.setPermission(appSessionEntity.getPermission());
        appSession.setUserId(appSessionEntity.getUserId());
        appSession.setUserName(appSessionEntity.getUserName());
        appSession.setAdmin(appSessionEntity.getIsAdmin());
        appSession.setSessionStartTime(appSessionEntity.getSessionStartTime());
        return appSession;
    }
    
    /**
     *  Get all the components in the given platform & environment.
     *  
     *  @param environmentName
     *  @param platform
     *  @return List<Component>
     */
    @Override
    public List<Component> getPlatformComponents(String environmentName,String platform){
    	List<HealthCheckEntity> results = null;
    	int envId = 0;
    	if(environmentName != null && !environmentName.equalsIgnoreCase("all")){
    		envId = environmentDao.getEnironmentIdFromName(environmentName);
    		results = componentDao.getPlatformComponentForEnv(envId,platform);
    	} else {
    		results = componentDao.getPlatformComponentForEnv(envId,platform);
    	}
    	Map<Integer, Component> resultCompEntitiesMap = new HashMap<Integer, Component>();
    	
    	for(HealthCheckEntity hc : results){
    		ComponentEntity ce = hc.getComponent();
			Component mapComponent = resultCompEntitiesMap.get(ce.getComponentId());
    		resultCompEntitiesMap.put(ce.getComponentId(), AdminServiceImpl.convertHealthCheckEntity(hc, mapComponent));
    		
    		if(ce.getParentComponent() != null && resultCompEntitiesMap.get(ce.getParentComponent().getComponentId()) == null){
    			String Platform = regStatusDao.getParentPlatform(ce.getParentComponent().getComponentId(),envId);
    			ce.getParentComponent().setPlatform(Platform);
    			resultCompEntitiesMap.put(ce.getParentComponent().getComponentId(), AdminServiceImpl.convertComponentEntity(ce.getParentComponent()));
        	}
    	}
    	
    	List<Component> resultCompEntities = new ArrayList<Component>();
    	for (Integer key : resultCompEntitiesMap.keySet()) {
    		resultCompEntities.add(resultCompEntitiesMap.get(key));
    	}
    	return resultCompEntities;
    }
    
    /**
     * Get API container information.
     * @param startDate
     * @param endDate
     * @param environment
     * @param componentIdsStrg
     * @param isParentComponents
     * @param platform
     * @return
     * @throws ParseException
     */
    @Override
	public List<ContainerStatus> getAppContainers(String startDate, String endDate, String environment, String componentIdsStrg, boolean isParentComponents,String platform) throws ParseException {
		List<ContainerStatusEntity> listContStsEntitys = null;
		List<K8sPodsContainersEntity> k8sListContStsEntitys = null;
		List<ContainerStatus> statusList = null;
		List<ContainerStatus>  listContSts = new ArrayList<ContainerStatus>();
		List<ContainerStatus>  k8slistContSts =null;
		int envId = 0;
		if (environment.equals(Constants.PLATFORM_MESOS) || environment.equals(Constants.PLATFORM_K8S)) {
			envId = environmentDao.getEnironmentIdFromName(environment);;
		}
		if(platform==null||platform.equals(Constants.PLATFORM_MESOS)||platform.equals(Constants.PLATFORM_ALL)) {
			listContStsEntitys = containerStatusDao.getEnvContainers(startDate, endDate, envId, componentIdsStrg, isParentComponents);
			listContSts = makeContainerStatusFromEntity(listContStsEntitys);
		}
		if(platform==null||platform.equals(Constants.PLATFORM_K8S)||platform.equals(Constants.PLATFORM_ALL)) {
			k8sListContStsEntitys = k8sContainerStatusDao.getEnvContainers(startDate, endDate, envId, componentIdsStrg, isParentComponents);
			k8slistContSts = makeK8sContainerStatusFromEntity(k8sListContStsEntitys);
		}
		
		if(platform==null || platform.equals(Constants.PLATFORM_ALL)) {
			statusList=listContSts;
			statusList.addAll(k8slistContSts);
		}else{
			if(platform.equals(Constants.PLATFORM_MESOS))
				statusList=listContSts;
			else
			    statusList=k8slistContSts;
		}	
		return statusList;
	}
    
    /**
     * Make Container Status From Entity
     * @param listContStsEntitys
     * @return
     */
	private List<ContainerStatus> makeContainerStatusFromEntity(List<ContainerStatusEntity> listContStsEntitys) {
		List<ContainerStatus> listContainerStatus  = new ArrayList<ContainerStatus>();
		for(ContainerStatusEntity contStsEntity : listContStsEntitys) {
			ContainerStatus containerStatus = new ContainerStatus();
			containerStatus.setTotalContainers(contStsEntity.getTotalContainer());
			containerStatus.setDeltaValue(contStsEntity.getDeltaValue());
			containerStatus.setStatusDate(contStsEntity.getStatsDate().toString());
			if(contStsEntity.getComponent() != null){
				containerStatus.setComponentId(contStsEntity.getComponent().getComponentId());
				containerStatus.setComponentName(contStsEntity.getComponent().getComponentName());
			}
			
			listContainerStatus.add(containerStatus);
		}
		return listContainerStatus;
	}
	
	/**
	 * @param listContStsEntitys
	 * @return
	 */
	private List<ContainerStatus> makeK8sContainerStatusFromEntity(List<K8sPodsContainersEntity> listContStsEntitys) {
		List<ContainerStatus> listContainerStatus  = new ArrayList<ContainerStatus>();
		for(K8sPodsContainersEntity contStsEntity : listContStsEntitys) {
			ContainerStatus containerStatus = new ContainerStatus();
			containerStatus.setTotalContainers(contStsEntity.getTotalContainers());
			containerStatus.setStatusDate(contStsEntity.getStatusDate().toString());
			if(contStsEntity.getComponent() != null){
				containerStatus.setComponentId(contStsEntity.getComponent().getComponentId());
				containerStatus.setComponentName(contStsEntity.getComponent().getComponentName());
			}
			
			listContainerStatus.add(containerStatus);
		}
		return listContainerStatus;
	}
	
	 /**
     * Get API container information.
     */
    @Override
    public List<ApiStatus> getApplicationApis(String startDate, String endDate, String environment, String componentIdsStrg, String platform) throws ParseException {
    	List<ApiStatus> apiStatusList = null;
		List<ApiStatus> statusList = null;
		List<ContainerStatus> listContSts = null;
		List<ApiStatus> k8sApiStatusList = null;
		List<K8sContainerStatus> k8sListContSts = null;
		int envId = 0;
		/**Code change to include K8s services in API Dashboard
		 *  
		 * Added platform validation
		 * Added new Dao classes for K8s services and container criteria - k8sApiStatusDao & k8sContainerStatusDao
		 * Added statusList to merge Mesos and K8s List when platform is Null or All.
		 * 
		 */
		if (environment.equals(Constants.PLATFORM_MESOS) || environment.equals(Constants.PLATFORM_K8S)) {
			envId = environmentDao.getEnironmentIdFromName(environment);;
		}
		if(platform==null||platform.equals(Constants.PLATFORM_MESOS)||platform.equals(Constants.PLATFORM_ALL)) {
			apiStatusList= apiStatusDao.getEnvApis(startDate, endDate, envId, componentIdsStrg);
			listContSts =containerStatusDao.getAllContainersOfParent(startDate, endDate, envId, componentIdsStrg);
			for(ApiStatus apiStatus : apiStatusList){
			   for(ContainerStatus conts : listContSts){
				   if(apiStatus.getComponentId() == conts.getComponentId() && apiStatus.getStatusDate().equalsIgnoreCase(conts.getStatusDate())){
					   apiStatus.setTotalContainers(conts.getTotalContainers());
				   }
			   }
			}
	
		}
		if(platform==null||platform.equals(Constants.PLATFORM_K8S)||platform.equals(Constants.PLATFORM_ALL)) {
			k8sApiStatusList= k8sApiStatusDao.getEnvApis(startDate, endDate, envId, componentIdsStrg);
			k8sListContSts =k8sContainerStatusDao.getAllContainersOfParent(startDate, endDate, envId, componentIdsStrg);
			for(ApiStatus apiStatus : k8sApiStatusList){
			   for(K8sContainerStatus conts : k8sListContSts){
				   if(apiStatus.getComponentId() == conts.getComponentId() && apiStatus.getStatusDate().equalsIgnoreCase(conts.getStatusDate())){
					   apiStatus.setTotalContainers(conts.getTotalContainers());
				   }
			   }
			}
			
		}
		List k8sObjPods = null;
		if (platform == null || platform.equals("All")) {
			statusList = apiStatusList;
			statusList.addAll(k8sApiStatusList);
			if (componentIdsStrg == null|| componentIdsStrg.equals("") || componentIdsStrg.trim().length() == 0) {
				k8sObjPods = k8sContainerStatusDao.getRemK8sObjPodsCont(startDate, endDate, envId);
				statusList.addAll(k8sObjPods);
			}
		} else if (platform.equals("Mesos")) {
			statusList = apiStatusList;
		} else {
			statusList = k8sApiStatusList;
			if(componentIdsStrg == null || componentIdsStrg.equals("") || componentIdsStrg.trim().length() == 0 ){
				k8sObjPods = k8sContainerStatusDao.getRemK8sObjPodsCont(startDate, endDate, envId );
				statusList.addAll(k8sObjPods);
			}
    	}
	return statusList;
	}

    /**
     * @param environment
     * @param componentIdsStrg
     * @param platform
     * @return
     * @throws ParseException
     */
    public long getCurrentApi(String environment, String componentIdsStrg,String platform) throws ParseException{
  		long currentApisMesos=0;
  		long currentApisK8s=0;
  		long currentApis=0;
  		int envId = 0;
  		if (environment.equals(Constants.PLATFORM_MESOS) || environment.equals(Constants.PLATFORM_K8S)) {
			envId = environmentDao.getEnironmentIdFromName(environment);;
		}
  		if(platform==null||platform.equals(Constants.PLATFORM_MESOS)||platform.equals(Constants.PLATFORM_ALL))
		{
  			currentApisMesos = apiStatusDao.getCurrentNumberOfApis(envId, componentIdsStrg);
		}
		if(platform==null||platform.equals(Constants.PLATFORM_K8S)||platform.equals(Constants.PLATFORM_ALL))
		{
			currentApisK8s = k8sApiStatusDao.getCurrentNumberOfApis(envId, componentIdsStrg);
		}
		currentApis = 	currentApisMesos + currentApisK8s;
		return currentApis;
	}
	
    /**
     * @param environment
     * @param componentIdsStrg
     * @param isParentComponents
     * @param platform
     * @return
     * @throws ParseException
     */
    public long getCurrentContainer(String environment, String componentIdsStrg, boolean isParentComponents,String platform) throws ParseException{
		
		long currentContMesos=0;
  		long currentContK8s=0;
  		long currentCont=0;
  		long remObjPods = 0;
  		int envId = 0;
  		if (environment.equals(Constants.PLATFORM_MESOS) || environment.equals(Constants.PLATFORM_K8S)) {
			envId = environmentDao.getEnironmentIdFromName(environment);;
		}
  		if(platform==null||platform.equals(Constants.PLATFORM_MESOS)||platform.equals(Constants.PLATFORM_ALL))
		{
  			currentContMesos = containerStatusDao.getCurrentNumberOfContainsers(envId, componentIdsStrg, isParentComponents);
		}
		if(platform==null||platform.equals(Constants.PLATFORM_K8S)||platform.equals(Constants.PLATFORM_ALL))
		{
			currentContK8s = k8sContainerStatusDao.getCurrentNumberOfContainsers(envId, componentIdsStrg, isParentComponents);
			if(componentIdsStrg == null || componentIdsStrg.equals("")){
				remObjPods = k8sPodsStatusDao.getRemK8sObjectPods(envId, "containers");
			}	
		}
		currentCont = 	currentContMesos + currentContK8s + remObjPods;
		return currentCont;
	}

	public  List<TpsLatency> getTpsAndLatency(String environment,String platform) {
		List<TpsLatency> tpsList =tpsLatencyDao.getCurrentTpsLatency(environment,platform);	
		return tpsList;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
     * @param environment
     * @param platform
     * @param componentIdsStrg
     * @param isParentComponents
     * @return
     * @throws ParseException
     */
	@Override
	public List<TpsLatencyHistory> getAppTpsLatency(String startDate, String endDate, String environment,String platform,
			String componentIdsStrg, boolean isParent) throws ParseException {
		List<TpsLatencyHistory> tpsLatList = null;
		List<TpsLatencyHistory> k8sTpsLatList = null;
		List<TpsLatencyHistory> mesosTpsLatList = null;
		if(environment.equals("") || environment.trim().length() == 0|| environment.equalsIgnoreCase("all")){
			environment = null;
		}
		if(platform == null||platform.equalsIgnoreCase(Constants.PLATFORM_MESOS)||platform.equalsIgnoreCase(Constants.PLATFORM_ALL)){
			if(isParent){
				mesosTpsLatList = tpsLatencyDao.getTpsAndLatOfParent(startDate, endDate, environment, componentIdsStrg,platform); 
			}else{
				mesosTpsLatList = tpsLatencyDao.getTpsAndLatOfComponent(startDate, endDate, environment, componentIdsStrg,platform); 
			}
			tpsLatList=mesosTpsLatList;
		}
		if(platform==null||platform.equalsIgnoreCase(Constants.PLATFORM_K8S)||platform.equalsIgnoreCase(Constants.PLATFORM_ALL)) {
			if(isParent){
				k8sTpsLatList = k8sTpsLatencyDao.getTpsAndLatOfParent(startDate, endDate, environment, componentIdsStrg,platform); 
			}else{
				k8sTpsLatList = k8sTpsLatencyDao.getTpsAndLatOfComponent(startDate, endDate, environment, componentIdsStrg,platform); 
			}         
			if(tpsLatList!=null)
            	tpsLatList.addAll(k8sTpsLatList);
            else
            	tpsLatList=k8sTpsLatList;
		}
		
		return tpsLatList;
	}

	@Override
	public TpsLatency getCurrentTpsAndLatency(String environment, String componentIdsStrg,String platform, boolean isParent) {
		    
		TpsLatency tpsLatency = tpsLatencyDao.getCurrentTpsAndLatency(environment, componentIdsStrg, isParent,platform);	
		return tpsLatency;
	}	
	/**
	 * Method to get the number of pods in the given period of time
	 * @param startDate
	 * @param endDate
     * @param environment
     * @param componentIdsStrg
     * @param isParentComponents
     * @return
     * @throws ParseException
     */
	public List<ApiStatus> getTotalPodsStatus(String startDate, String endDate, String environment, String componentIdsStrg,boolean isParentComponents) throws ParseException{
		List<ApiStatus> k8sObjPods = null;
		List<ApiStatus> k8sPodsStatus = null;
		int envId = 0;
		if (environment.equals(Constants.PLATFORM_K8S)) {
			envId = environmentDao.getEnironmentIdFromName(environment);;
		}
		k8sPodsStatus = k8sPodsStatusDao.getPodsStatus(startDate, endDate, envId, componentIdsStrg, isParentComponents);
		if(componentIdsStrg == null || componentIdsStrg.equals("") || componentIdsStrg.trim().length() == 0 ){
		   k8sObjPods = k8sContainerStatusDao.getRemK8sObjPodsCont(startDate, endDate, envId);
		   k8sPodsStatus.addAll(k8sObjPods);
		}
		return k8sPodsStatus;
	}
	  /**
	 * Method to get the current number of pods associated to given environment and component
     * @param environment
     * @param componentIdsStrg
     * @param isParentComponents
     * @return
     * @throws ParseException
     */
	public long getCurrentNumberOfPods(String environment, String componentIdsStrg, boolean isParentComponents) throws ParseException{
		int envId = 0;
		long remObjPods = 0;
		if (environment.equals(Constants.PLATFORM_K8S)) {
			envId = environmentDao.getEnironmentIdFromName(environment);;
		}
	    long currntPods = k8sPodsStatusDao.getCurrentNumberOfPods(envId, componentIdsStrg, isParentComponents);
	    if(componentIdsStrg == null){
	    	remObjPods = k8sPodsStatusDao.getRemK8sObjectPods(envId, "pods");
	    }	
		return currntPods + remObjPods;	
  			
	}
	
}
