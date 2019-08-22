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
package com.tmobile.kardio.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Region stores details about region Region-Status Details for Dashboard
 * 
 */
public class Region {

    private int regionId;
    private String regionName;
    private String regionStatus;
    private String regionMessage;
    private boolean recentEvent;

    /**
     * Makes a copy.
     * 
     * @return
     */
    public Region copy() {
        Region copy = new Region();
        copy.setRegionId(regionId);
        copy.setRegionName(regionName);
        copy.setRegionStatus(regionStatus);
        copy.setRecentEvent(recentEvent);
        copy.setRegionMessage(regionMessage);
        return copy;
    }

    public String getRegionMessage() {
        return regionMessage;
    }

    public void setRegionMessage(String regionMessage) {
        this.regionMessage = regionMessage;
    }

    /**
     * @return the regionStatus
     */
    public String getRegionStatus() {
        return regionStatus;
    }

    /**
     * @param regionStatus
     *            the regionStatus to set
     */
    public void setRegionStatus(String regionStatus) {
        this.regionStatus = regionStatus;
    }

    /**
     * @return the regionId
     */
    @JsonIgnore
    public int getRegionId() {
        return regionId;
    }

    /**
     * @param regionId
     *            the regionId to set
     */
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    /**
     * @return the regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName
     *            the regionName to set
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return the recentEvent
     */
    public boolean isRecentEvent() {
        return recentEvent;
    }

    /**
     * @param recentEvent
     *            the recentEvent to set
     */
    public void setRecentEvent(boolean recentEvent) {
        this.recentEvent = recentEvent;
    }
}
