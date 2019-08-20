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
package com.tmobile.kardio.surveiller.vo;

import java.util.Map;

import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.handler.SurveillerHandler;

/**
 * Main VO class for HealthCheck component
 */
public class HealthCheckVO {
	private String healthCheckTypeName;
	private String healthCheckTypeClassName;
	private Long healthCheckRegionId;
	private Long healthCheckComponentId;
	private Long healthCheckId;
	private Long healthCheckRetryMaxCount;
	private Long healthCheckRetryCurrentCount;
	private SurveillerHandler surveillerHandler;
	private StatusVO status;
	private Status currentStatus;
	private String failureStatusMessage;
	private int environmentId;
	private String environmentName;
	private ComponentType componentType;
	private ComponentVO component;
	private String regionName;
	private Map<String,String> paramDetails;

	/**
	 * @return the healthCheckTypeName
	 */
	public String getHealthCheckTypeName() {
		return healthCheckTypeName;
	}

	/**
	 * @param healthCheckTypeName
	 *            the healthCheckTypeName to set
	 */
	public void setHealthCheckTypeName(String healthCheckTypeName) {
		this.healthCheckTypeName = healthCheckTypeName;
	}

	/**
	 * @return the healthCheckTypeClassName
	 */
	public String getHealthCheckTypeClassName() {
		return healthCheckTypeClassName;
	}

	/**
	 * @param healthCheckTypeClassName
	 *            the healthCheckTypeClassName to set
	 */
	public void setHealthCheckTypeClassName(String healthCheckTypeClassName) {
		this.healthCheckTypeClassName = healthCheckTypeClassName;
	}

	/**
	 * @return the healthCheckRegionId
	 */
	public Long getHealthCheckRegionId() {
		return healthCheckRegionId;
	}

	/**
	 * @param healthCheckRegionId
	 *            the healthCheckRegionId to set
	 */
	public void setHealthCheckRegionId(Long healthCheckRegionId) {
		this.healthCheckRegionId = healthCheckRegionId;
	}

	/**
	 * @return the healthCheckComponentId
	 */
	public Long getHealthCheckComponentId() {
		return healthCheckComponentId;
	}

	/**
	 * @param healthCheckComponentId
	 *            the healthCheckComponentId to set
	 */
	public void setHealthCheckComponentId(Long healthCheckComponentId) {
		this.healthCheckComponentId = healthCheckComponentId;
	}

	/**
	 * @return the healthCheckId
	 */
	public Long getHealthCheckId() {
		return healthCheckId;
	}

	/**
	 * @param healthCheckId
	 *            the healthCheckId to set
	 */
	public void setHealthCheckId(Long healthCheckId) {
		this.healthCheckId = healthCheckId;
	}

	/**
	 * @return the healthCheckRetryMaxCount
	 */
	public Long getHealthCheckRetryMaxCount() {
		return healthCheckRetryMaxCount;
	}

	/**
	 * @param healthCheckRetryMaxCount
	 *            the healthCheckRetryMaxCount to set
	 */
	public void setHealthCheckRetryMaxCount(Long healthCheckRetryMaxCount) {
		this.healthCheckRetryMaxCount = healthCheckRetryMaxCount;
	}

	/**
	 * @return the healthCheckRetryCurrentCount
	 */
	public Long getHealthCheckRetryCurrentCount() {
		return healthCheckRetryCurrentCount;
	}

	/**
	 * @param healthCheckRetryCurrentCount
	 *            the healthCheckRetryCurrentCount to set
	 */
	public void setHealthCheckRetryCurrentCount(
			Long healthCheckRetryCurrentCount) {
		this.healthCheckRetryCurrentCount = healthCheckRetryCurrentCount;
	}

	/**
	 * @return the surveillerHandler
	 */
	public SurveillerHandler getSurveillerHandler() {
		return surveillerHandler;
	}

	/**
	 * @param surveillerHandler
	 *            the surveillerHandler to set
	 */
	public void setSurveillerHandler(SurveillerHandler surveillerHandler) {
		this.surveillerHandler = surveillerHandler;
	}

	/**
	 * @return the statusVO
	 */
	public StatusVO getStatus() {
		return status;
	}

	/**
	 * @param statusVO
	 *            the statusVO to set
	 */
	public void setStatus(StatusVO status) {
		this.status = status;
	}

	/**
	 * @return the currentStatus
	 */
	public Status getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *            set the current status
	 */
	public void setCurrentStatus(Long currentStatusId) {
		if (currentStatusId == Status.UP.getStatusId()) {
			currentStatus = Status.UP;
		} else if (currentStatusId == Status.DOWN.getStatusId()) {
			currentStatus = Status.DOWN;
		} else if (currentStatusId == Status.DISRUPT.getStatusId()) {
			currentStatus = Status.DISRUPT;
		} else if (currentStatusId == Status.WARNING.getStatusId()) {
			currentStatus = Status.WARNING;
		} else {
			currentStatus = null;
		}
	}

	/**
	 * @return the failureStatusMessage
	 */
	public String getFailureStatusMessage() {
		return failureStatusMessage;
	}

	/**
	 * @param failureStatusMessage the failureStatusMessage to set
	 */
	public void setFailureStatusMessage(String failureStatusMessage) {
		this.failureStatusMessage = failureStatusMessage;
	}

	/**
	 * Get environmentId
	 * 
	 * @return environmentId
	 */
	public int getEnvironmentId() {
		return environmentId;
	}

	/**
	 * Set environmentId
	 * 
	 * @param environmentId
	 */
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * Get component
	 * @return component
	 */
	public ComponentVO getComponent() {
		return component;
	}

	/**
	 * @return the componentType
	 */
	public ComponentType getComponentType() {
		return componentType;
	}

	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(ComponentType componentType) {
		this.componentType = componentType;
	}

	/**
	 * Set component
	 * @param component
	 */
	public void setComponent(ComponentVO component) {
		this.component = component;
	}

	/**
	 * Get environmentName
	 * @return
	 */
	public String getEnvironmentName() {
		return environmentName;
	}

	/**
	 * Set environmentName
	 * @param environmentName
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	/**
	 * Get regionName
	 * @return
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * Set regionName
	 * @param regionName
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	/**
	 * @return the paramDetails
	 */
	public Map<String, String> getParamDetails() {
		return paramDetails;
	}

	/**
	 * @param paramDetails the paramDetails to set
	 */
	public void setParamDetails(Map<String, String> paramDetails) {
		this.paramDetails = paramDetails;
	}
}
