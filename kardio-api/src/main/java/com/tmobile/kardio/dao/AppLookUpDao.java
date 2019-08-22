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

import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.db.entity.AppLookUpEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;

/**
 * Interface for DAO on component table
 */
public interface AppLookUpDao {

    /**
     * Save component details into the database
     * 
     * @param component
     */
    public void saveAppFullName(AppFullName appFullName);

    /**
     * Get List of app FullName from the database
     * 
     * @return List<ComponentEntity>
     */
    public List<AppLookUpEntity> getAppFullNameWithAppId();

    /**
     * Get List of app Component for Component ID from the database
     * 
     * @return List<ComponentEntity>
     */
    public ComponentEntity getAppComponentId(int i);

    /**
     * Update the app fullname details
     * 
     * @param appFullName
     */
    public void editAppFullName(AppFullName appFullName);
    
    /**
     * Delete app full name from DB
     * @param appFullName
     */
    public void deleteAppFullName(AppFullName appFullName);

}
