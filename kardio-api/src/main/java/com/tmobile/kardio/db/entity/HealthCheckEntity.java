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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entity class to for health_check table
 */
@Entity
@Table(name = "health_check")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="com.tmobile.kardio.db.entity.HealthCheckEntity")
public class HealthCheckEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "health_check_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int healthCheckId;

    @ManyToOne
    @JoinColumn(name = "component_id")    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="com.tmobile.kardio.db.entity.HealthCheckEntity")
    private ComponentEntity component;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @ManyToOne
    @JoinColumn(name = "environment_id")
    private EnvironmentEntity environment;

    @ManyToOne
    @JoinColumn(name = "health_check_type_id")
    private HealthCheckTypeEntity healthCheckType;

    @Column(name = "max_retry_count")
    private int maxRetryCount;

    @Column(name = "failed_count")
    private int failedCount;

    @ManyToOne
    @JoinColumn(name = "current_status_id")
    private StatusEntity currentStatus;

    @Column(name = "status_update_time")
    private Date statusUpdateTime;

    @Column(name = "last_status_change")
    private Date lastStatusChange;
    
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "del_ind")
    private int delInd;

    
    /**
     * Get healtCheckId
     * 
     * @return healtCheckId
     */
    public int getHealthCheckId() {
        return healthCheckId;
    }

    /**
     * Set healtCheckId
     * 
     * @param healtCheckId
     */
    public void setHealthCheckId(int healthCheckId) {
        this.healthCheckId = healthCheckId;
    }

    /**
     * Get component details
     * 
     * @return component
     */
    public ComponentEntity getComponent() {
        return component;
    }

    /**
     * Set component details
     * 
     * @param component
     */
    public void setComponent(ComponentEntity component) {
        this.component = component;
    }

    /**
     * Get region details
     * 
     * @return region
     */
    public RegionEntity getRegion() {
        return region;
    }

    /**
     * Set region details
     * 
     * @param region
     */
    public void setRegion(RegionEntity region) {
        this.region = region;
    }

    /**
     * Get environment details
     * 
     * @return environment
     */
    public EnvironmentEntity getEnvironment() {
        return environment;
    }

    /**
     * Set environment details
     * 
     * @param environment
     */
    public void setEnvironment(EnvironmentEntity environment) {
        this.environment = environment;
    }

    /**
     * Get healthCheckType details
     * 
     * @return healthCheckType
     */
    public HealthCheckTypeEntity getHealthCheckType() {
        return healthCheckType;
    }

    /**
     * Set healthCheckType details
     * 
     * @param healthCheckType
     */
    public void setHealthCheckType(HealthCheckTypeEntity healthCheckType) {
        this.healthCheckType = healthCheckType;
    }

    /**
     * Get maxRetryCount
     * 
     * @return maxRetryCount
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * Set maxRetryCount
     * 
     * @param maxRetryCount
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * Get failedCount
     * 
     * @return failedCount
     */
    public int getFailedCount() {
        return failedCount;
    }

    /**
     * Set failedCount
     * 
     * @param failedCount
     */
    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    /**
     * Get currentStatus
     * 
     * @return currentStatus
     */
    public StatusEntity getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Set currentStatus
     * 
     * @param currentStatus
     */
    public void setCurrentStatus(StatusEntity currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * Get statusUpdateTime time
     * 
     * @return statusUpdateTime
     */
    public Date getStatusUpdateTime() {
        return statusUpdateTime;
    }

    /**
     * Set statusUpdateTime time
     * 
     * @param statusUpdateTime
     */
    public void setStatusUpdateTime(Date statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    /**
     * Get lastStatusChange time
     * 
     * @return lastStatusChange
     */
    public Date getLastStatusChange() {
        return lastStatusChange;
    }

    /**
     * Set lastStatusChange time
     * 
     * @param lastStatusChange
     */
    public void setLastStatusChange(Date lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }

    /**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
     * Get delInd
     * 
     * @return delInd
     */
    public int getDelInd() {
        return delInd;
    }

    /**
     * Set delInd
     * 
     * @param delInd
     */
    public void setDelInd(int delInd) {
        this.delInd = delInd;
    }

}
