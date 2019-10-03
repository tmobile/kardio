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
package com.tmobile.kardio.service;

import com.tmobile.kardio.bean.*;

import java.util.List;

/**
 * Interface for service methods of Admin controller Admin Service
 * 
 */
public interface AdminService {

    /**
     * Function to save component
     * 
     * @param component
     */
    public void saveComponent(Component component);

    /**
     * Get List of all components
     * 
     * @return List<Component>
     */
    public List<Component> getAllComponent();

    /**
     * Update component details
     * 
     * @param component
     */
    public void editComponent(Component component);

    /**
     * Update delete indicator of component
     * 
     * @param component
     */
    public void deleteComponent(Component component);

    /**
     * Get all health check list
     * 
     * @return List<HealthCheckVO>
     */
    public List<HealthCheckVO> getAllHealthChecks();

    /**
     * Get all health check types
     * 
     * @return List<HealthCheckTypeVO>
     */
    public List<HealthCheckTypeVO> getAllHealthCheckTypes();

    /**
     * Update delete indicator of health check
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

    /**
     * Subscribe to global alerts
     * 
     * @param subscription
     */
    public void subscribeGlobalAlert(Subscription subscription);

    /**
     * Get all global subscriptions
     * 
     * @return List<Subscription>
     */
    public List<Subscription> getAllGlobalSubscriptions();

    /**
     * Delete global subscription
     * 
     * @param subscriptionId
     */
    public void deleteGlobalSubscription(int subscriptionId);

    /**
     * Get all counters
     * 
     * @return
     */
    public CounterDetails getAllCounters();

    /**
     * Edit counter data
     * 
     * @param counter
     */
    public void editCounter(List<Counters> counter);

    /**
     * Lock environment
     * 
     * @param environment
     */
    public void doEnvironmentLock(Environment environment);

    /**
     * Edit environment couter details
     * 
     * @param envCounters
     */
    public void editEnvCounterDetails(EnvCounters envCounters);

    /**
     * Get list of environments
     * 
     * @return List<Environment>
     */
    public List<Environment> getEnvironmentLock();

    /**
     * Get application session
     * 
     * @param authToken
     * @return
     */
    public AppSession getAppSession(String authToken);

    /**
     * Function to save AppFullName
     * 
     * @param AppFullName
     */
    public void saveAppFullName(AppFullName appFullName);

    /**
     * Get List of App Full Names
     * 
     * @return List<Component>
     */
    public List<AppFullName> getAppFullName();

    /**
     * Update app FullName details
     * 
     * @param appFullName
     */
    public void editAppFullName(AppFullName appFullName);
    
    /**
     * Delete app Full Name row
     * 
     * @param appFullName
     */
    public void deleteAppFullName(AppFullName appFullName);

    /**
     * add New Environment
     * 
     * @param environment
     */
    
	public void addEnvironment(Environment environment);

	public void addAuditLog(String authToken, String string);
}
