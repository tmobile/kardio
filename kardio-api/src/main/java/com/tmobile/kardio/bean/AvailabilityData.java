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

/**
 * AvailabilityData to store the availability information of each component VO for showing Availability percentage
 * 
 */
public class AvailabilityData {
    private int componentId;
    private double availabilityPercentageWest;
    private double availabilityPercentageEast;

    /**
     * @return the componentId
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * @param componentId
     *            the componentId to set
     */
    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the availabilityPercentageWest
     */
    public double getAvailabilityPercentageWest() {
        return availabilityPercentageWest;
    }

    /**
     * @param availabilityPercentageWest
     *            the availabilityPercentageWest to set
     */
    public void setAvailabilityPercentageWest(double availabilityPercentageWest) {
        this.availabilityPercentageWest = availabilityPercentageWest;
    }

    /**
     * @return the availabilityPercentageEast
     */
    public double getAvailabilityPercentageEast() {
        return availabilityPercentageEast;
    }

    /**
     * @param availabilityPercentageEast
     *            the availabilityPercentageEast to set
     */
    public void setAvailabilityPercentageEast(double availabilityPercentageEast) {
        this.availabilityPercentageEast = availabilityPercentageEast;
    }
}
