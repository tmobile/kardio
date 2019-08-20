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
 * HealthCheckTypeVO this stores the health check type details Health Check type listing
 * 
 */
public class HealthCheckTypeVO {

    private int healthCheckTypeId;
    private String healthCheckTypeName;
    private String healthCheckTypeDesc;

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
     * @return the healthCheckTypeDesc
     */
    public String getHealthCheckTypeDesc() {
        return healthCheckTypeDesc;
    }

    /**
     * @param healthCheckTypeDesc
     *            the healthCheckTypeDesc to set
     */
    public void setHealthCheckTypeDesc(String healthCheckTypeDesc) {
        this.healthCheckTypeDesc = healthCheckTypeDesc;
    }

}
