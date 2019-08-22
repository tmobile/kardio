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
package com.tmobile.kardio.bean;

import java.util.List;

/**
 * HealthCheckVO stores the different parameters required for doing health check and the current status VO for Admin
 * health check tab
 * 
 */
public class HealthCheckVO {
    private int healthCheckId;
    private int componentId;
    private String componentName;
    private String componentType;
    private int parentComponentId;
    private String parentComponentName;
    private int regionId;
    private String regionName;
    private int environmentId;
    private String environmentName;
    private int healthCheckTypeId;
    private String healthCheckTypeName;
    private int maxRetryCount;
    private boolean delInd;
    private String platform;//Code changes to include platform in getAllHealthChecks api
    private List<HealthCheckParamVO> healthCheckParamList;

    /**
     * @return the healthCheckId
     */
    public int getHealthCheckId() {
        return healthCheckId;
    }

    /**
     * @param healthCheckId
     *            the healthCheckId to set
     */
    public void setHealthCheckId(int healthCheckId) {
        this.healthCheckId = healthCheckId;
    }

    /**
     * @return the componentId
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * @param componentId
     *            the componentId to set
     */
    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName
     *            the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the componentType
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * @param componentType
     *            the componentType to set
     */
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * @return the parentComponentId
     */
    public int getParentComponentId() {
        return parentComponentId;
    }

    /**
     * @param parentComponentId
     *            the parentComponentId to set
     */
    public void setParentComponentId(int parentComponentId) {
        this.parentComponentId = parentComponentId;
    }

    /**
     * @return the parentComponentName
     */
    public String getParentComponentName() {
        return parentComponentName;
    }

    /**
     * @param parentComponentName
     *            the parentComponentName to set
     */
    public void setParentComponentName(String parentComponentName) {
        this.parentComponentName = parentComponentName;
    }

    /**
     * @return the regionId
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * @param regionId
     *            the regionId to set
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    /**
     * @return the regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName
     *            the regionName to set
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return the environmentId
     */
    public int getEnvironmentId() {
        return environmentId;
    }

    /**
     * @param environmentId
     *            the environmentId to set
     */
    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    /**
     * @return the environmentName
     */
    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * @param environmentName
     *            the environmentName to set
     */
    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    /**
     * @return the healthCheckTypeId
     */
    public int getHealthCheckTypeId() {
        return healthCheckTypeId;
    }

    /**
     * @param healthCheckTypeId
     *            the healthCheckTypeId to set
     */
    public void setHealthCheckTypeId(int healthCheckTypeId) {
        this.healthCheckTypeId = healthCheckTypeId;
    }

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
     * @return the maxRetryCount
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * @param maxRetryCount
     *            the maxRetryCount to set
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * @return the delInd
     */
    public boolean isDelInd() {
        return delInd;
    }

    /**
     * @param delInd
     *            the delInd to set
     */
    public void setDelInd(boolean delInd) {
        this.delInd = delInd;
    }

    /**
     * @return the healthCheckParamList
     */
    public List<HealthCheckParamVO> getHealthCheckParamList() {
        return healthCheckParamList;
    }

    /**
     * @param healthCheckParamList
     *            the healthCheckParamList to set
     */
    public void setHealthCheckParamList(List<HealthCheckParamVO> healthCheckParamList) {
        this.healthCheckParamList = healthCheckParamList;
    }

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + healthCheckId;
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
		HealthCheckVO other = (HealthCheckVO) obj;
		if (healthCheckId != other.healthCheckId)
			return false;
		return true;
	}

}
