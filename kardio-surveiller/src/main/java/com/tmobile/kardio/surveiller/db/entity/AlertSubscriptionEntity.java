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
package com.tmobile.kardio.surveiller.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="alert_subscription")
public class AlertSubscriptionEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="alert_subscription_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int alertSubscriptionId;

	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@Column(name = "component_id", insertable= false, updatable=false)
	private Integer componentId;
	
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
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	
	/**
	 * @return the alertSubscriptionId
	 */
	public int getAlertSubscriptionId() {
		return alertSubscriptionId;
	}

	/**
	 * @param alertSubscriptionId the alertSubscriptionId to set
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
	 * @param component the component to set
	 */
	public void setComponent(ComponentEntity component) {
		this.component = component;
	}

	/**
	 * @return the componentId
	 */
	public Integer getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}
	
	/**
	 * @return the subscriptionVal
	 */
	public String getSubscriptionVal() {
		return subscriptionVal;
	}

	/**
	 * @param subscriptionVal the subscriptionVal to set
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
	 * @param authToken the authToken to set
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
	 * @param validationLevel the validationLevel to set
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
	 * @param subscriptionType the subscriptionType to set
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
	 * @param globalComponentTypeId the globalComponentTypeId to set
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
	 * @param environment the environment to set
	 */
	public void setEnvironment(EnvironmentEntity environment) {
		this.environment = environment;
	}
}
