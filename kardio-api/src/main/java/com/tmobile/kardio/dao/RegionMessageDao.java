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

import com.tmobile.kardio.bean.ComponentMessages;
import com.tmobile.kardio.bean.Messages;

/**
 * Interface to do operations for component messages Region History Dao
 * 
 */
public interface RegionMessageDao {

    /**
     * Save component message
     * 
     * @param compMessage
     */
    public void saveCompMessage(ComponentMessages compMessage);

    /**
     * Update component message
     * 
     * @param compMessage
     */
    public void updateCompMessage(ComponentMessages compMessage);

    /**
     * Get component message for a particular region in a environment
     * 
     * @param environmentName
     * @param componentId
     * @param region
     * @return List<Messages>
     */
    public List<Messages> getCompRegionMessage(String environmentName, String componentId, String region);

    /**
     * Get component message for a region in a environment for a particular date
     * 
     * @param environmentName
     * @param componentId
     * @param region
     * @param date
     * @return List<Messages>
     */
    public List<Messages> getCompRegionMessage(String environmentName, String componentId, String region, String date);

}
