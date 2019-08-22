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

/**
 * HealthCheckParamVO stores details of the health check parameters for a component Health check Edit
 * 
 */
public class HealthCheckParamVO {
    private int healthCheckParamId;
    private String healthCheckParamKey;
    private String healthCheckParamValue;

    /**
     * @return the healthCheckParamId
     */
    public int getHealthCheckParamId() {
        return healthCheckParamId;
    }

    /**
     * @param healthCheckParamId
     *            the healthCheckParamId to set
     */
    public void setHealthCheckParamId(int healthCheckParamId) {
        this.healthCheckParamId = healthCheckParamId;
    }

    /**
     * @return the healthCheckParamKey
     */
    public String getHealthCheckParamKey() {
        return healthCheckParamKey;
    }

    /**
     * @param healthCheckParamKey
     *            the healthCheckParamKey to set
     */
    public void setHealthCheckParamKey(String healthCheckParamKey) {
        this.healthCheckParamKey = healthCheckParamKey;
    }

    /**
     * @return the healthCheckParamValue
     */
    public String getHealthCheckParamValue() {
        return healthCheckParamValue;
    }

    /**
     * @param healthCheckParamValue
     *            the healthCheckParamValue to set
     */
    public void setHealthCheckParamValue(String healthCheckParamValue) {
        this.healthCheckParamValue = healthCheckParamValue;
    }
}
