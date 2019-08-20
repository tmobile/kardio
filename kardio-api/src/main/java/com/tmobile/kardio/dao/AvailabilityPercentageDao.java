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

import java.text.ParseException;
import java.util.List;

import com.tmobile.kardio.bean.AvailabilityData;

/**
 * Interface to access data in regards to availability aercentage
 */
public interface AvailabilityPercentageDao {

    /**
     * Get all availability percentage list
     * 
     * @param environment
     * @param interval
     * @return List<AvailabilityData>
     * @throws ParseException
     */
    public List<AvailabilityData> getAllAvailabilityPercentage(String environment, String interval,String platform, String region) throws ParseException;

}
