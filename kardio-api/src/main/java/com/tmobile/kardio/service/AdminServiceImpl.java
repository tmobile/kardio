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
package com.tmobile.kardio.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.bean.AppSession;
import com.tmobile.kardio.bean.Audit;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.CounterDetails;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.bean.EnvCounters;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.HealtCheckEnvironment;
import com.tmobile.kardio.bean.HealthCheckTypeVO;
import com.tmobile.kardio.bean.HealthCheckVO;
import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.dao.AlertSubscribeDao;
import com.tmobile.kardio.dao.AppLookUpDao;
import com.tmobile.kardio.dao.AppSessionDao;
import com.tmobile.kardio.dao.AuditDao;
import com.tmobile.kardio.dao.ComponentDao;
import com.tmobile.kardio.dao.CountersDao;
import com.tmobile.kardio.dao.EnvironmentDao;
import com.tmobile.kardio.dao.HealthCheckDao;
import com.tmobile.kardio.db.entity.AppLookUpEntity;
import com.tmobile.kardio.db.entity.AppSessionEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;
import com.tmobile.kardio.exceptions.ValidationFailedException;

/**
 * Admin Service Impl
 * 
 */
@Service
@PropertySource("classpath:application.properties")
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AuditDao auditDao;
	
	@Autowired
	private ComponentDao componentDao;

	@Autowired
	private HealthCheckDao healthCheckDao;

	@Autowired
	private EnvironmentDao environmentDao;

	@Autowired
	private AlertSubscribeDao alertSubscibeDao;

	@Autowired
	private RegionStatusService regionStatusService;

	@Autowired
	private CountersDao counterDao;

	@Autowired
	private AppSessionDao appSessionDao;

	@Autowired
	private AppLookUpDao appLookUpDao;

	@Value("${slack.url}")
	private String slackURL;

	@Value("${slack.auth.token}")
	private String slackAuthToken;

	@Value("${adminpage.skip.infra.component.name}")
	private String skipInfraCompNames;

	@Value("${slack.webhook.validateurl}")
	private String slackWebhookValidURL;

	private List<String> skipableInfraComponents;

	@PostConstruct
	public void inititializeDao() throws Exception {
		skipableInfraComponents = Arrays.asList(skipInfraCompNames.trim().split(","));
	}

	public void setSkippableInfraComponents(List<String> comps) {
		this.skipableInfraComponents = comps;
	}
	/**
	 * Add the component to the Database
	 */
	@Override
	public void saveComponent(Component component) {
		componentDao.saveComponent(component);
	}

	/**
	 * Get all the infra components from DB
	 */
	@Override
	public List<Component> getAllComponent() {
		List<Component> components = makeComponentFromEntity(componentDao.getComponents());
		return removeSkippableComponent(components);
	}

	/**
	 * Function to remove components from list
	 */
	public List<Component> removeSkippableComponent(List<Component> components) {
		List<Component> removeList = new ArrayList<Component>();
		for (Component component : components) {
			if (skipableInfraComponents.contains(component.getParentComponentName())
					|| skipableInfraComponents.contains(component.getComponentName())) {
				removeList.add(component);
			}
		}
		components.removeAll(removeList);
		return components;
	}

	/**
	 * Update the given Component details to DB
	 */
	@Override
	public void editComponent(Component component) {
		componentDao.editComponent(component);
	}

	/**
	 * Deletes the given Component from DB
	 */
	@Override
	public void deleteComponent(Component component) {
		componentDao.deleteComponent(component);
	}

	/**
	 * Returns all the health check Details
	 */
	@Override
	public List<HealthCheckVO> getAllHealthChecks() {
		return healthCheckDao.getAllHealthCheckDetails();
	}

	/**
	 * Returns all the health check Types from DB
	 */
	@Override
	public List<HealthCheckTypeVO> getAllHealthCheckTypes() {
		return healthCheckDao.getAllHealthCheckTypes();
	}

	/**
	 * Marks the del_ind as 1 for the given health_check_id in the health_check
	 * table.
	 */
	@Override
	public void deleteHealthCheck(int healthCheckId) {
		healthCheckDao.deleteHealthCheck(healthCheckId);
	}

	/**
	 * Updates/Adds the health check with given details
	 */
	@Override
	public void editHealthCheck(HealthCheckVO healthCheck) {
		healthCheckDao.editHealthCheck(healthCheck);
	}

	/**
	 * Function to add new global subscription to a particular mail or slack channel
	 */
	@Override
	public void subscribeGlobalAlert(Subscription subscription) {
		if (subscription.getSubsciptionType().equalsIgnoreCase("email")
				&& !regionStatusService.checkValidDomainForEmail(subscription.getSubsciptionVal())) {
			throw new ValidationFailedException("The email id provided given has an invalid domain name");
		}
		if (slackWebhookValidURL != null && slackWebhookValidURL.trim().length() != 0
				&& subscription.getSubsciptionType().equalsIgnoreCase("slack")
				&& subscription.getSubsciptionVal() != null
				&& !subscription.getSubsciptionVal().toLowerCase().startsWith(slackWebhookValidURL.toLowerCase())) {
			throw new ValidationFailedException("Slack URL must Start with " + slackWebhookValidURL);
		}
		if (subscription.getSubsciptionType().equalsIgnoreCase("slackChannel")
				&& !regionStatusService.validateSlackChannel(subscription.getSubsciptionVal())) {
			throw new ValidationFailedException("Subscription Failed : No such public channel available");
		}
		if (checkForDuplicate(subscription)) {
			throw new ValidationFailedException(
					"Provided email id/slack channel is already subscribed for this component");
		}

		int subsType = Constants.SUBSCRIPTION_TYPE_ID_EMAIL;
		int validationLevel = 1;
		if (subscription.getSubsciptionType().equalsIgnoreCase(Constants.SUBSCRIPTION_TYPE_DESC_SLACK_WEBHOOK)) {
			subsType = Constants.SUBSCRIPTION_TYPE_ID_SLACK_WEBHOOK;
		} else if (subscription.getSubsciptionType().equalsIgnoreCase(Constants.SUBSCRIPTION_TYPE_DESC_SLACK_CHNL)) {
			subsType = Constants.SUBSCRIPTION_TYPE_ID_SLACK_CHNL;
		}

		if (alertSubscibeDao.isSubscribtionAvailable(subscription, subsType)) {
			throw new ValidationFailedException("Duplicate Record : The given input already exist");
		}

		alertSubscibeDao.saveSubscription(subscription, subsType, validationLevel, true);
	}

	/**
	 * check For Duplicate subscription
	 * 
	 * @param subscription
	 * @return
	 */
	private boolean checkForDuplicate(Subscription subscription) {
		return alertSubscibeDao.checkforDuplicates(subscription.getComponentId(), subscription.getEnvironmentId(),
				subscription.getSubsciptionVal());
	}

	@Override
	public List<Subscription> getAllGlobalSubscriptions() {
		return regionStatusService.makeSubscriptionList(alertSubscibeDao.getAllGlobalSubscriptions());
	}

	@Override
	public void deleteGlobalSubscription(int subscriptionId) {
		alertSubscibeDao.deleteGlobalSubscription(subscriptionId);
	}

	/**
	 * Get All Counters from DB
	 */
	@Override
	public CounterDetails getAllCounters() {
		CounterDetails counterDetails = counterDao.getAllCountersDetails();
		return counterDetails;
	}

	/**
	 * Updates Counters with given details
	 */
	@Override
	public void editCounter(List<Counters> listOfCounters) {
		counterDao.editCounters(listOfCounters);
	}

	/**
	 * Add the appFullName to the Database
	 */
	@Override
	public void saveAppFullName(AppFullName appFullName) {
		appLookUpDao.saveAppFullName(appFullName);
	}

	/**
	 * Get all the App Full Names from DB
	 */
	@Override
	public List<AppFullName> getAppFullName() {
		List<AppFullName> appFullName = makeAppFullNameFromEntity(appLookUpDao.getAppFullNameWithAppId());
		List<AppFullName> componentAppFullName = makeAppFullNameFromComponent(componentDao.getAppParentComponents());
		mergeAppNameLists(appFullName, componentAppFullName);
		return appFullName;
	}

	/**
	 * Update the given App Fullname to DB
	 */
	@Override
	public void editAppFullName(AppFullName appFullName) {
		appLookUpDao.editAppFullName(appFullName);
	}

	@Override
	public void deleteAppFullName(AppFullName appFullName) {
		// Delete App Fullname
		appLookUpDao.deleteAppFullName(appFullName);
	}

	/**
	 * Update Counters query parameters
	 **/
	@Override
	public void editEnvCounterDetails(EnvCounters envCounters) {
		counterDao.editEnvCounterDetails(envCounters);
	}

	/**
	 * Get All Counters from DB
	 */
	@Override
	public void doEnvironmentLock(Environment environment) {
		environmentDao.updateEnvironment(environment);

	}

    /**
     * add new Environment for adminPage
     */
    @Override
    public void addEnvironment(Environment environment) {
        environmentDao.addEnvironment(environment);

    }
    
	@Override
	public List<Environment> getEnvironmentLock() {
		return makeEnironmentFromEntity(environmentDao.getEnvironmentWithLock());
	}

	/**
	 * Create List of com.tmobile.kardio.bean.Environment from List of
	 * com.tmobile.kardio.db.entity.EnvironmentEntity
	 * 
	 * @param environmentEntityList
	 * @return
	 */
	public static List<Environment> makeEnironmentFromEntity(List<EnvironmentEntity> environmentEntityList) {
		List<Environment> envList = new ArrayList<Environment>();
		for (EnvironmentEntity envEntity : environmentEntityList) {
			Environment env = new Environment();
			env.setEnvironmentId(envEntity.getEnvironmentId());
			env.setEnvironmentName(envEntity.getEnvironmentName());
			env.setEnvironmentDesc(envEntity.getEnvironmentDesc());
			env.setMarathonUrl(envEntity.getMarathonUrl());
			env.setEnvLock(envEntity.getEnvLock());
			env.setDisplayOrder(envEntity.getDisplayOrder());
			env.setK8sUrl(envEntity.getK8sUrl());//Code change for edit environment URL
			envList.add(env);
		}
		return envList;
	}

	/**
	 * Function to create a list of component from a list of componentEntity
	 * 
	 * @param componentEntities
	 * @return
	 */
	public static List<Component> makeComponentFromEntity(List<ComponentEntity> componentEntities) {
		List<Component> components = new ArrayList<Component>();
		for (ComponentEntity componentEntity : componentEntities) {
			components.add(convertComponentEntity(componentEntity));
		}
		return components;
	}
	
	 /**
     * Convert ComponentEntity to Component bean.
     * 
     * @param componentEntitie
     * @return
     */
    public static Component convertComponentEntity(ComponentEntity componentEntity) {
    	Component component = new Component();
        component.setComponentId(componentEntity.getComponentId());
        component.setComponentName(componentEntity.getComponentName());
        if(componentEntity.getAppLookUpEntity() != null) {
            component.setAppFullName(componentEntity.getAppLookUpEntity().getComponentFullName());
        }        
        component.setCompDesc(componentEntity.getComponentDesc());
        component.setDelInd(componentEntity.getDelInd());
        //Change to include platform of parent
        component.setPlatform(componentEntity.getPlatform());
        component.setComponentType(componentEntity.getComponentType().getComponentTypeName());
        if (componentEntity.getParentComponent() != null) {
            component.setParentComponentId(componentEntity.getParentComponent().getComponentId());
            component.setParentComponentName(componentEntity.getParentComponent().getComponentName());
        }
        return component;
    }

	/**
	 * Convert HealthCheckEntity to Component bean.
	 * 
	 * @param componentEntitie
	 * @return
	 */
	public static Component convertHealthCheckEntity(HealthCheckEntity hc, Component component) {
		ComponentEntity componentEntity = hc.getComponent();
		if (component == null || component.getComponentName() == null) {
			component = new Component();
			component.setComponentId(componentEntity.getComponentId());
			component.setComponentName(componentEntity.getComponentName());
			component.setCompDesc(componentEntity.getComponentDesc());
			component.setDelInd(componentEntity.getDelInd());
			component.setComponentType(componentEntity.getComponentType().getComponentTypeName());
			if (componentEntity.getParentComponent() != null) {
				component.setParentComponentId(componentEntity.getParentComponent().getComponentId());
				component.setParentComponentName(componentEntity.getParentComponent().getComponentName());
				component.setPlatform(componentEntity.getPlatform());
			}
		}
		HealtCheckEnvironment hcEnvironment = new HealtCheckEnvironment();
		hcEnvironment.setEnvName(hc.getEnvironment().getEnvironmentName());
		hcEnvironment.setCreatedDate(hc.getCreatedDate());
		if(component.getHcEnvList() == null) {
			List<HealtCheckEnvironment> hcEnvironments = new ArrayList<HealtCheckEnvironment>();
			hcEnvironments.add(hcEnvironment);
			component.setHcEnvList(hcEnvironments);
		} else {
			component.getHcEnvList().add(hcEnvironment);
		}
		return component;
	}

	/**
	 * Function to get app session from database
	 * 
	 * @param authToken
	 * @return
	 */
	public AppSession getAppSession(String authToken) {
		return makeAppSessionFromEntity(appSessionDao.getAppSession(authToken));
	}

	/**
	 * Function to convert AppSessionEntity into AppSession
	 * 
	 * @param appSessionEntity
	 * @return
	 */
	public static AppSession makeAppSessionFromEntity(AppSessionEntity appSessionEntity) {
		AppSession appSession = new AppSession();
		if (appSessionEntity == null) {
			return appSession;
		}
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
	 * Function to create a list of App Full Name from a list of AppFullNameEntity
	 * 
	 * @param componentEntities
	 * @return
	 */
	public static List<AppFullName> makeAppFullNameFromEntity(List<AppLookUpEntity> appLookUpEntities) {
		List<AppFullName> appFullNames = new ArrayList<AppFullName>();
		for (AppLookUpEntity appLookUpEntity : appLookUpEntities) {
			AppFullName appFullName = new AppFullName();
			appFullName.setAppId(appLookUpEntity.getApplookupId());
			appFullName.setComponentId(appLookUpEntity.getComponent().getComponentId());
			appFullName.setComponentName(appLookUpEntity.getComponent().getComponentName());
			appFullName.setComponentFullName(appLookUpEntity.getComponentFullName());
			appFullNames.add(appFullName);
		}
		return appFullNames;
	}

	/**
	 * Function to make list of AppFullName objects from list of ComponentEntity
	 * 
	 * @param componentEntities
	 * @return
	 */
	public static List<AppFullName> makeAppFullNameFromComponent(List<ComponentEntity> componentEntities) {
		List<AppFullName> appFullNames = new ArrayList<AppFullName>();

		for (ComponentEntity entity : componentEntities) {
			AppFullName appFullName = new AppFullName();
			appFullName.setComponentId(entity.getComponentId());
			appFullName.setComponentName(entity.getComponentName());
			appFullNames.add(appFullName);
		}

		return appFullNames;
	}

	/**
	 * To merge two list of app names after comparison
	 * 
	 * @param appFullNames
	 *            base app full name list
	 * @param componentAppFullNames
	 *            app full name list to compare
	 */
	public static void mergeAppNameLists(List<AppFullName> appFullNames, List<AppFullName> componentAppFullNames) {
		List<AppFullName> newAppList = new ArrayList<AppFullName>();
		for (AppFullName compAppName : componentAppFullNames) {
			boolean appFound = false;
			for (AppFullName appName : appFullNames) {
				if (compAppName.equals(appName)) {
					appFound = true;
					break;
				}
			}
			if (!appFound) {
				newAppList.add(compAppName);
			}
		}
		appFullNames.addAll(newAppList);
	}
	
	public void addAuditLog(String authToken, String audit_log) {
		  AppSession appSession = getAppSession(authToken);
		  Audit audit = new Audit();
		  audit.setAudit_log(audit_log);
		  audit.setUser_id(appSession.getUserId());
		  audit.setDate(appSession.getSessionStartTime());
		  auditDao.saveAuditLog(audit);
		  
	}

}
