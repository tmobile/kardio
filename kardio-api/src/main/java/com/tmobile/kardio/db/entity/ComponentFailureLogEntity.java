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
package com.tmobile.kardio.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class to for comp_failure_log table
 */
@Entity
@Table(name = "comp_failure_log")
public class ComponentFailureLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "comp_failure_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int componentFailureLogId;

    @Column(name = "health_check_id")
    private int healthCheckId;

    @Column(name = "status_id")
    private int statusId;

    @Column(name = "failure_message")
    private String failureMessage;

    @Column(name = "comp_reg_sts_time")
    private Date compRegStsTime;

    /**
     * @return the componentFailureLogId
     */
    public int getComponentFailureLogId() {
        return componentFailureLogId;
    }

    /**
     * @param componentFailureLogId
     *            the componentFailureLogId to set
     */
    public void setComponentFailureLogId(int componentFailureLogId) {
        this.componentFailureLogId = componentFailureLogId;
    }

    /**
     * @return the healthCheckId
     */
    public int getHealthCheckId() {
        return healthCheckId;
    }

    /**
     * @param healthCheckId
     *            the healthCheckId to set
     */
    public void setHealthCheckId(int healthCheckId) {
        this.healthCheckId = healthCheckId;
    }

    /**
     * @return the statusId
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * @param statusId
     *            the statusId to set
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * @return the failureMessage
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * @param failureMessage
     *            the failureMessage to set
     */
    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    /**
     * @return the compRegStsTime
     */
    public Date getCompRegStsTime() {
        return compRegStsTime;
    }

    /**
     * @param compRegStsTime
     *            the compRegStsTime to set
     */
    public void setCompRegStsTime(Date compRegStsTime) {
        this.compRegStsTime = compRegStsTime;
    }
}
