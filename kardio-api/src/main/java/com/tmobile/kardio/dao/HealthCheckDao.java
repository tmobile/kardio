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
package com.tmobile.kardio.dao;

import java.util.List;

import com.tmobile.kardio.bean.HealthCheckTypeVO;
import com.tmobile.kardio.bean.HealthCheckVO;

/**
 * Interface to do operations on heath_check table Health Check Dao
 */
public interface HealthCheckDao {

    /**
     * Get a list of all Health Checks
     * 
     * @return List<HealthCheckVO>
     */
    public List<HealthCheckVO> getAllHealthCheckDetails();

    /**
     * Get list of all Health Check Type
     * 
     * @return List<HealthCheckTypeVO>
     */
    public List<HealthCheckTypeVO> getAllHealthCheckTypes();

    /**
     * Set del ind of Health Check
     * 
     * @param healthCheckId
     */
    public void deleteHealthCheck(int healthCheckId);

    /**
     * Update health check details
     * 
     * @param healthCheck
     */
    public void editHealthCheck(HealthCheckVO healthCheck);
}
