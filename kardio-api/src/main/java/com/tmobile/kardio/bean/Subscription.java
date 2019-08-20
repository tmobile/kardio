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
package com.tmobile.kardio.bean;

/**
 * Subscription details in regards to subscription are stored in this class Component Subscription
 * 
 */
public class Subscription {
    private int alertSubscriptionId;
    private int componentId;
    private String componentName;
    private String subsciptionVal;
    private String environmentName;
    private int environmentId;
    private boolean activationStatus;
    private String authToken;
    private String subsciptionType;
    private int globalSubscriptionTypeId;
    private String globalSubscriptionType;
    private String platform;

    /**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
     * Get alertSubscriptionId for Subscription
     * 
     * @return alertSubscriptionId
     */
    public int getAlertSubscriptionId() {
        return alertSubscriptionId;
    }

    /**
     * Set alertSubscriptionId for Subscription
     * 
     * @param alertSubscriptionId
     */
    public void setAlertSubscriptionId(int alertSubscriptionId) {
        this.alertSubscriptionId = alertSubscriptionId;
    }

    /**
     * Get componentId for subscription
     * 
     * @return componentId
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * Set componentId for subscription
     * 
     * @param componentId
     */
    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    /**
     * Get subsciptionVal for subscription
     * 
     * @return subsciptionVal
     */
    public String getSubsciptionVal() {
        return subsciptionVal;
    }

    /**
     * Set subsciptionVal for subscription
     * 
     * @param subsciptionVal
     */
    public void setSubsciptionVal(String subsciptionVal) {
        this.subsciptionVal = subsciptionVal;
    }

    /**
     * Get environmentName for subscription
     * 
     * @return environmentName
     */
    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * Set environmentName for subscription
     * 
     * @param environmentName
     */
    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    /**
     * Get environmentId for subscription
     * 
     * @return environmentId
     */
    public int getEnvironmentId() {
        return environmentId;
    }

    /**
     * Set environmentId for subscription
     * 
     * @param environmentId
     */
    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    /**
     * Get componentName for subscription
     * 
     * @return
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Set componentName for subscription
     * 
     * @param componentName
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Get authToken for subscription
     * 
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Set authToken for subscription
     * 
     * @param authToken
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Get authToken for activationStatus
     * 
     * @return activationStatus
     */
    public boolean isActivationStatus() {
        return activationStatus;
    }

    /**
     * Set authToken for activationStatus
     * 
     * @param activationStatus
     */
    public void setActivationStatus(boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    /**
     * Get authToken for subsciptionType
     * 
     * @return subsciptionType
     */
    public String getSubsciptionType() {
        return subsciptionType;
    }

    /**
     * Set authToken for subsciptionType
     * 
     * @param subsciptionType
     */
    public void setSubsciptionType(String subsciptionType) {
        this.subsciptionType = subsciptionType;
    }

    /**
     * @return the globalSubscriptionTypeId
     */
    public int getGlobalSubscriptionTypeId() {
        return globalSubscriptionTypeId;
    }

    /**
     * @param globalSubscriptionTypeId
     *            the globalSubscriptionTypeId to set
     */
    public void setGlobalSubscriptionTypeId(int globalSubscriptionTypeId) {
        this.globalSubscriptionTypeId = globalSubscriptionTypeId;
    }

    /**
     * Get globalSubscriptionType for global subscription
     * 
     * @return globalSubscriptionType
     */
    public String getGlobalSubscriptionType() {
        return globalSubscriptionType;
    }

    /**
     * Set globalSubscriptionType for global subscription
     * 
     * @param globalSubscriptionType
     */
    public void setGlobalSubscriptionType(String globalSubscriptionType) {
        this.globalSubscriptionType = globalSubscriptionType;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + componentId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (componentId != other.componentId)
			return false;
		return true;
	}



}
