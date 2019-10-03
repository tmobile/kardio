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
import java.util.Date;

/**
 * Entity class to for dailly_comp_status table
 */
@Entity
@Table(name = "dailly_comp_status")
public class DaillyCompStatusEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dailly_comp_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dailyCompStatusId;

    @ManyToOne
    @JoinColumn(name = "health_check_id")
    private HealthCheckEntity healthCheck;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private StatusEntity status;

    @Column(name = "status_date")
    private Date statusDate;

    @Column(name = "percentage_up_time")
    private float percentageUpTime;

    @Column(name = "total_failure_count")
    private int totalFailureCount;

    /**
     * Get dailyCompStatusId
     * 
     * @return dailyCompStatusId
     */
    public int getDailyCompStatusId() {
        return dailyCompStatusId;
    }

    /**
     * Set dailyCompStatusId
     * 
     * @param dailyCompStatusId
     */
    public void setDailyCompStatusId(int dailyCompStatusId) {
        this.dailyCompStatusId = dailyCompStatusId;
    }

    /**
     * Get healthCheck details
     * 
     * @return healthCheck
     */
    public HealthCheckEntity getHealthCheck() {
        return healthCheck;
    }

    /**
     * Set healthCheck
     * 
     * @param healthCheck
     */
    public void setHealthCheck(HealthCheckEntity healthCheck) {
        this.healthCheck = healthCheck;
    }

    /**
     * Get status details
     * 
     * @return status
     */
    public StatusEntity getStatus() {
        return status;
    }

    /**
     * Set status
     * 
     * @param status
     */
    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    /**
     * Get statusDate
     * 
     * @return statusDate
     */
    public Date getStatusDate() {
        return statusDate;
    }

    /**
     * Set statusDate
     * 
     * @param statusDate
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * Get percentageUpTime
     * 
     * @return percentageUpTime
     */
    public float getPercentageUpTime() {
        return percentageUpTime;
    }

    /**
     * Set percentageUpTime
     * 
     * @param percentageUpTime
     */
    public void setPercentageUpTime(float percentageUpTime) {
        this.percentageUpTime = percentageUpTime;
    }

    /**
     * Get dailyCompStatusId
     * 
     * @return totalFailureCount
     */
    public int getTotalFailureCount() {
        return totalFailureCount;
    }

    /**
     * Set totalFailureCount
     * 
     * @param totalFailureCount
     */
    public void setTotalFailureCount(int totalFailureCount) {
        this.totalFailureCount = totalFailureCount;
    }

}
