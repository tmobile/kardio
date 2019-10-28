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
 * Entity class to for region table
 */
@Entity
@Table(name = "region")
public class RegionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regionId;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "region_desc")
    private String regionDesc;

    @Column(name = "region_lock")
    private int regionLock;

    /**
     * Get regionId
     * 
     * @return regionId
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * Set regionId
     * 
     * @param regionId
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    /**
     * Get regionName
     * 
     * @return regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * Set regionName
     * 
     * @param regionName
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * Get regionDesc
     * 
     * @return regionDesc
     */
    public String getRegionDesc() {
        return regionDesc;
    }

    /**
     * Set regionDesc
     * 
     * @param regionDesc
     */
    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
    }

    /**
     * Get regionLock
     * 
     * @return regionLock
     */
    public int getRegionLock() {
        return regionLock;
    }

    /**
     * Set regionLock
     * 
     * @param regionLock
     */
    public void setRegionLock(int regionLock) {
        this.regionLock = regionLock;
    }

}
