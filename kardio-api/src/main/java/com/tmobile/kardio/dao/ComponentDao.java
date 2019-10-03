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

import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;

import java.util.List;
import java.util.Map;

/**
 * Interface for DAO on component table
 */
public interface ComponentDao {

    /**
     * Save component details into the database
     * 
     * @param component
     */
    public void saveComponent(Component component);

    /**
     * Get List of components from the database
     * 
     * @return List<ComponentEntity>
     */
    public List<ComponentEntity> getComponents();
    
    /**
     * Get List of components from the database for the given Environment & Platform.
     * 
     * @param env EnvironmentEntity
     * @return
     */
    public List<HealthCheckEntity> getPlatformComponentForEnv(int envId,String platform);

    /**
     * Update the component details
     * 
     * @param component
     */
    public void editComponent(Component component);

    /**
     * Update the del ind parameter of component
     * 
     * @param component
     */
    public void deleteComponent(Component component);

    /**
     * Function to get a map of full app Name
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getAppFullName();
    
    /**
     * Get a list of app parent components
     * @return List<ComponentEntity>
     */
    public List<ComponentEntity> getAppParentComponents();
}
