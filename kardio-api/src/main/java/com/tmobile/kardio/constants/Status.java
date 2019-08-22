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
package com.tmobile.kardio.constants;

/**
 * Stores the different statuses that are used in SHD
 * 
 * Status Enum
 */
public enum Status {
    SERVICE_UP(1, "u", "Service is operating normally"), SERVICE_DISRUPTION(2, "d", "Service disruption"), SERVICE_DEGRADATION(3, "w",
            "Service degradation"), SERVICE_UNEXPECTED_BEHAVIOUR(4, "g", "Information");

    private int statusId;
    private String statusName;
    private String statusDescription;

    Status(int statusId, String statusName, String statusDescription) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.statusDescription = statusDescription;
    }

    public int statusId() {
        return statusId;
    }

    public String statusName() {
        return statusName;
    }

    public String statusDescription() {
        return statusDescription;
    }
}
