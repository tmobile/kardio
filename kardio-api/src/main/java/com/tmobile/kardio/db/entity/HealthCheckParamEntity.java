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

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class to for health_check_param table
 */
@Entity
@Table(name = "health_check_param")
public class HealthCheckParamEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "health_check_param_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int healthCheckParamId;

    @ManyToOne
    @JoinColumn(name = "health_check_id")
    private HealthCheckEntity healthCheck;

    @Column(name = "health_check_param_key")
    private String healthCheckParamKey;

    @Column(name = "health_check_param_val")
    private String healthCheckParamVal;

    /**
     * Get healthCheckParamId
     * 
     * @return healthCheckParamId
     */
    public int getHealthCheckParamId() {
        return healthCheckParamId;
    }

    /**
     * Set healthCheckParamId
     * 
     * @param healthCheckParamId
     */
    public void setHealthCheckParamId(int healthCheckParamId) {
        this.healthCheckParamId = healthCheckParamId;
    }

    /**
     * Get healthCheck details
     * 
     * @return healthCheck
     */
    public HealthCheckEntity getHealthCheck() {
        return healthCheck;
    }

    /**
     * Set healthCheck details
     * 
     * @param healthCheck
     */
    public void setHealthCheck(HealthCheckEntity healthCheck) {
        this.healthCheck = healthCheck;
    }

    /**
     * Get healthCheckParamKey
     * 
     * @return healthCheckParamKey
     */
    public String getHealthCheckParamKey() {
        return healthCheckParamKey;
    }

    /**
     * Set healthCheckParamKey
     * 
     * @param healthCheckParamKey
     */
    public void setHealthCheckParamKey(String healthCheckParamKey) {
        this.healthCheckParamKey = healthCheckParamKey;
    }

    /**
     * Get healthCheckParamVal
     * 
     * @return healthCheckParamVal
     */
    public String getHealthCheckParamVal() {
        return healthCheckParamVal;
    }

    /**
     * Set healthCheckParamVal
     * 
     * @param healthCheckParamVal
     */
    public void setHealthCheckParamVal(String healthCheckParamVal) {
        this.healthCheckParamVal = healthCheckParamVal;
    }
}
