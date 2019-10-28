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

import java.util.List;

/**
 * Stores the history of the status based on date Status for History tab
 * 
 */
public class StatusHistory {
    private String statusTime;
    private String statusEast;
    private String statusWest;
    private Float percentageUpTimeEast = 0.0F;
    private Float percentageUpTimeWest = 0.0F;
    @JsonIgnore
    private List<StatusHistory> children;

    /**
     * Makes a copy of VO
     */
    public StatusHistory copy() {
        StatusHistory copy = new StatusHistory();
        copy.setStatusTime(statusTime);
        copy.setStatusEast(statusEast);
        copy.setStatusWest(statusWest);
        copy.setPercentageUpTimeEast(percentageUpTimeEast);
        copy.setPercentageUpTimeWest(percentageUpTimeWest);
        return copy;
    }

    /**
     * @return the statusTime
     */
    public String getStatusTime() {
        return statusTime;
    }

    /**
     * @param statusTime
     *            the statusTime to set
     */
    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    /**
     * @return the statusEast
     */
    public String getStatusEast() {
        return statusEast;
    }

    /**
     * @param statusEast
     *            the statusEast to set
     */
    public void setStatusEast(String statusEast) {
        this.statusEast = statusEast;
    }

    /**
     * @return the statusWest
     */
    public String getStatusWest() {
        return statusWest;
    }

    /**
     * @param statusWest
     *            the statusWest to set
     */
    public void setStatusWest(String statusWest) {
        this.statusWest = statusWest;
    }

    /**
     * @return the percentageUpTimeEast
     */
    public Float getPercentageUpTimeEast() {
        return percentageUpTimeEast;
    }

    /**
     * @param percentageUpTimeEast
     *            the percentageUpTimeEast to set
     */
    public void setPercentageUpTimeEast(Float percentageUpTimeEast) {
        this.percentageUpTimeEast = percentageUpTimeEast;
    }

    /**
     * @return the percentageUpTimeWest
     */
    public Float getPercentageUpTimeWest() {
        return percentageUpTimeWest;
    }

    /**
     * @param percentageUpTimeWest
     *            the percentageUpTimeWest to set
     */
    public void setPercentageUpTimeWest(Float percentageUpTimeWest) {
        this.percentageUpTimeWest = percentageUpTimeWest;
    }

    /**
     * @return the children
     */
    public List<StatusHistory> getChildren() {
        return children;
    }

    /**
     * @param childern
     *            the children to set
     */
    public void setChildren(List<StatusHistory> children) {
        this.children = children;
    }

}
