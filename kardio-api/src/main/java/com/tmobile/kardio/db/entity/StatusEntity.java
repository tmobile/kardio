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
 * Entity class to for status table
 */
@Entity
@Table(name = "status")
public class StatusEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "status_desc")
    private String statusDesc;

    /**
     * Get statusId
     * 
     * @return statusId
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Set statusId
     * 
     * @param statusId
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * Get statusName
     * 
     * @return statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * Set statusName
     * 
     * @param statusName
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * Get statusDesc
     * 
     * @return statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * Set statusDesc
     * 
     * @param statusDesc
     */
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
