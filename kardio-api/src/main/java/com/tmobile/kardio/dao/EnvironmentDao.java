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
package com.tmobile.kardio.dao;

import java.util.List;

import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.db.entity.EnvironmentEntity;

/**
 * Handles the Environment table operations
 * 
 */
public interface EnvironmentDao {

    /**
     * Get environment id from environment name
     * 
     * @param environmentName
     * @return environmentId
     */
    public int getEnironmentIdFromName(String environmentName);
    
    /**
     * Get EnvironmentEntity from environment name.
     * 
     * @param environmentName
     * @return EnvironmentEntity
     */
    public EnvironmentEntity getEnvironmentFromName(String environmentName);

    /**
     * Get a list of environments
     * 
     * @return List<EnvironmentEntity>
     */
    public List<EnvironmentEntity> getEnvironments();

    /**
     * Get a list environment entity
     * 
     * @return
     */
    public List<EnvironmentEntity> getEnvironmentWithLock();

    /**
     * Update the details of the environment
     * 
     * @param environment
     */
    public void updateEnvironment(Environment environment);

    /**
     * Add new Environment from Admin Page
     * 
     * @param environment
     */
	public void addEnvironment(Environment environment);
}
