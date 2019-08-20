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
package com.tmobile.kardio.dao;

import java.util.List;

import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.db.entity.AlertSubscriptionEntity;

/**
 * Interface for DAO for alert_subscription
 * 
 * 
 * 
 */
public interface AlertSubscribeDao {

    /**
     * Function to save the subscription to the alerts of a components status change
     * 
     * @param subscription
     * @param subsType
     * @param validationLevel
     * @param isGlobalSubscription
     */
    public void saveSubscription(Subscription subscription, int subsType, int validationLevel, boolean isGlobalSubscription);

    /**
     * Check it subscription is already done for the component for email or slack channel
     * 
     * @param subscription
     * @param subsType
     * @return
     */
    public boolean isSubscribtionAvailable(final Subscription subscription, final int subsType);

    /**
     * Delete subscription based on auth token
     * 
     * @param authToken
     */
    public void deleteSububscription(String authToken);

    /**
     * Get auth token for subscription
     * 
     * @param userId
     * @param compId
     * @param environmentId
     * @return
     */
    public String getSubscriptionToken(String userId, int compId, int environmentId);

    /**
     * Confirm the subscription made for email id
     * 
     * @param authToken
     */
    public void confirmSubscription(String authToken);

    /**
     * Get a list of subscribed email ids
     * 
     * @param componentId
     * @param environmentId
     * @return
     */
    public List<AlertSubscriptionEntity> getSubscribedEmailIdList(int componentId, int environmentId);

    /**
     * Check if there is duplicates of subscription
     * 
     * @param componentId
     * @param environmentId
     * @param subsciptionText
     * @return
     */
    public boolean checkforDuplicates(int componentId, int environmentId, String subsciptionText);

    /**
     * Get list of all global subscriptions list
     * 
     * @return
     */
    public List<AlertSubscriptionEntity> getAllGlobalSubscriptions();

    /**
     * Deleted global subscription from database
     * 
     * @param subscriptionId
     */
    public void deleteGlobalSubscription(int subscriptionId);
}
