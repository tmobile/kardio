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

import com.tmobile.kardio.bean.*;
import com.tmobile.kardio.db.entity.AlertSubscriptionEntity;
import com.tmobile.kardio.db.entity.AppSessionEntity;

import java.text.ParseException;
import java.util.List;

/**
 * Service interface for main controller
 * 
 */
public interface RegionStatusService {

    /**
     * Get region status for components in and environment
     * 
     * @param environment
     * @return StatusResponse
     */
    public StatusResponse getComponentRegion(String environment);

    /**
     * Update the Message
     * 
     * @param environmentName
     * @param messageType
     * @param message
     * @param authToken
     * @throws InstantiationException
     */
    public void loadMessages(String environmentName, String messageType, String message, String authToken) throws InstantiationException;

    /**
     * Get region status history
     * 
     * @param environment
     * @return
     * @throws ParseException 
     */
    public HistoryResponse getRegionStatusHistory(String environment) throws ParseException;

    /**
     * Update component message
     * 
     * @param compMessage
     */
    public void loadComponentMessages(ComponentMessages compMessage);

    /**
     * Get all component message for a region in a environment
     * 
     * @param environmentName
     * @param componentId
     * @param region
     * @return List<Messages>
     */
    public List<Messages> getComponentMessage(String environmentName, String componentId, String region);

    /**
     * Get all component message for a region in a environment for a particular date
     * 
     * @param environmentName
     * @param componentId
     * @param region
     * @param logDate
     * @return List<Messages>
     */
    public List<Messages> getComponentMessage(String environmentName, String componentId, String region, String logDate);

    /**
     * Do login for user
     * 
     * @param user
     */
    public void doLoginForUser(User user);

    /**
     * Do log out for user
     * 
     * @param authToken
     */
    public void doLogOutForUser(String authToken);

    /**
     * Get list of environments
     * 
     * @return List<Environment>
     */
    public List<Environment> getEnvironments();

    /**
     * Subscribe alerts
     * 
     * @param subscription
     */
    public void subscribeAlert(Subscription subscription);

    /**
     * Unsubscribe alerts
     * 
     * @param subscription
     */
    public void unsubscribeAlert(Subscription subscription);

    /**
     * Delete subscription
     * 
     * @param authToken
     */
    public void deleteSubscription(String authToken);

    /**
     * Confirm subscription
     * 
     * @param authToken
     */
    public void confirmSubscription(String authToken);

    /**
     * Get subscribed email list
     * 
     * @param componentId
     * @param environmentName
     * @return List<Subscription>
     */
    public List<Subscription> getSubscribedEmailIdList(int componentId, String environmentName);

    /**
     * Get counters matrix
     * 
     * @param environment
     * @return List<Counters>
     */
    public List<Counters> getCountersMatrix(String environment, String platform);

    /**
     * Get list of availability percentage
     * 
     * @param environment
     * @param interval
     * @return List<AvailabilityData>
     * @throws ParseException
     */
    public List<AvailabilityData> getAvailabilityPercentage(String environment, String interval, String platform,String region) throws ParseException;

    /**
     * Validate slack channel
     * 
     * @param slackChannelName
     * @return true/false
     */
    public boolean validateSlackChannel(String slackChannelName);

    /**
     * Check if the domain used for mail is valid
     * 
     * @param emailId
     * @return
     */
    public boolean checkValidDomainForEmail(String emailId);

    /**
     * Creating an AppSession bean from AppSessionEntity
     * 
     * @param appSessionEntity
     * @return AppSession
     */
    public AppSession makeAppSessionFromEntity(AppSessionEntity appSessionEntity);

    /**
     * Get a list of Subscription bean from List<AlertSubscriptionEntity>
     * 
     * @param alrtSubList
     * @return List<Subscription>
     */
    public List<Subscription> makeSubscriptionList(List<AlertSubscriptionEntity> alrtSubList);
    
    /**
     * Get all the components loaded from Marathon.
     * 
     * @param environmentName
     * @return
     */
    public List<Component> getPlatformComponents(String environmentName,String platform);

	/**
	 * Get the Component Container stats for API Dashboard.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param environment
	 * @param component
	 * @param isParentComponents
	 * @return
	 * @throws ParseException
	 */
	public List<ContainerStatus> getAppContainers(String startDate, String endDate, String environment, String component, boolean isParentComponents,String platform)
			throws ParseException;

	/**
	 * @param startDate
	 * @param endDate
	 * @param environment
	 * @param component
	 * @return
	 * @throws ParseException
	 */
	public List<ApiStatus> getApplicationApis(String startDate, String endDate, String environment, String componentIdsStrg, String platfrom) throws ParseException;

	public long getCurrentApi(String environment, String componentIdsStrg,String platform) throws ParseException;
	
	public long getCurrentContainer(String environment, String componentIdsStrg, boolean isParentComponents,String platform) throws ParseException;
	
	public  List<TpsLatency> getTpsAndLatency(String environment,String platform);

	public List<TpsLatencyHistory> getAppTpsLatency(String startDate, String endDate, String environment,String platform,
			String componentIdsStrg, boolean isParent) throws ParseException;

	public TpsLatency getCurrentTpsAndLatency(String environment, String componentIdsStrg, String platform,boolean isParent);
	
	public List<ApiStatus> getTotalPodsStatus(String startDate, String endDate, String environment, String componentIdsStrg,boolean isParentComponents) throws ParseException;

	public long getCurrentNumberOfPods(String environment, String componentIdsStrg, boolean isParentComponents) throws ParseException;
}
