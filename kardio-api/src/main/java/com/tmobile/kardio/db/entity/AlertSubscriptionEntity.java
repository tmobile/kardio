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
package com.tmobile.kardio.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class to for alert_subscription table
 */
@Entity
@Table(name = "alert_subscription")
public class AlertSubscriptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "alert_subscription_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int alertSubscriptionId;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private ComponentEntity component;

    @Column(name = "subscription_val")
    private String subscriptionVal;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "validation_level")
    private Integer validationLevel;

    @Column(name = "subscription_type")
    private Integer subscriptionType;

    @Column(name = "global_component_type_id")
    private Integer globalComponentTypeId;

    @ManyToOne
    @JoinColumn(name = "environment_id")
    private EnvironmentEntity environment;
    
    @Column(name = "platform")
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
     * @return the alertSubscriptionId
     */
    public int getAlertSubscriptionId() {
        return alertSubscriptionId;
    }

    /**
     * @param alertSubscriptionId
     *            the alertSubscriptionId to set
     */
    public void setAlertSubscriptionId(int alertSubscriptionId) {
        this.alertSubscriptionId = alertSubscriptionId;
    }

    /**
     * @return the component
     */
    public ComponentEntity getComponent() {
        return component;
    }

    /**
     * @param component
     *            the component to set
     */
    public void setComponent(ComponentEntity component) {
        this.component = component;
    }

    /**
     * @return the subscriptionVal
     */
    public String getSubscriptionVal() {
        return subscriptionVal;
    }

    /**
     * @param subscriptionVal
     *            the subscriptionVal to set
     */
    public void setSubscriptionVal(String subscriptionVal) {
        this.subscriptionVal = subscriptionVal;
    }

    /**
     * @return the authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken
     *            the authToken to set
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return the validationLevel
     */
    public Integer getValidationLevel() {
        return validationLevel;
    }

    /**
     * @param validationLevel
     *            the validationLevel to set
     */
    public void setValidationLevel(Integer validationLevel) {
        this.validationLevel = validationLevel;
    }

    /**
     * @return the subscriptionType
     */
    public Integer getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * @param subscriptionType
     *            the subscriptionType to set
     */
    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    /**
     * @return the globalComponentTypeId
     */
    public Integer getGlobalComponentTypeId() {
        return globalComponentTypeId;
    }

    /**
     * @param globalComponentTypeId
     *            the globalComponentTypeId to set
     */
    public void setGlobalComponentTypeId(Integer globalComponentTypeId) {
        this.globalComponentTypeId = globalComponentTypeId;
    }

    /**
     * @return the environment
     */
    public EnvironmentEntity getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(EnvironmentEntity environment) {
        this.environment = environment;
    }
}
