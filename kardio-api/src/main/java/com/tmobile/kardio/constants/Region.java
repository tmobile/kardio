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
package com.tmobile.kardio.constants;

/**
 * Stores all the regions that are used in SHD Regions Enum
 */
public enum Region {
    WEST_REGION(1, "West Region"), EAST_REGION(2, "East Region");

    private int regionId;
    private String description;

    Region(int regionId, String description) {
        this.regionId = regionId;
        this.description = description;
    }

    public int getRegionId() {
        return regionId;
    }

    public String getRegionDescription() {
        return description;
    }
}
