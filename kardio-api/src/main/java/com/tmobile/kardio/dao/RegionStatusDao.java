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

import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.StatusResponse;

/**
 * Interface to get region based status for components Region Status Dao
 * 
 */
public interface RegionStatusDao {

    /**
     * The Component Status
     * 
     * @param environment
     * @return
     */
    public StatusResponse getCompRegStatus(String environment);

    /**
     * Update the Message in Environment Table.
     * 
     * @param environmentName
     * @param messageType
     * @param message
     * @throws InstantiationException
     */
    public void loadMessages(String environmentName, String messageType, String message) throws InstantiationException;

    /**
     * Function to get all parent components from the database
     * 
     * @return
     */
    public List<Component> getAllParentComponents();
    
    /**
     * @param parentComponentId
     * @param envId
     * @return
     */
    public String getParentPlatform(int parentComponentId, int envId);

}
