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
package com.tmobile.kardio.surveiller.db.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity class for container_stats
 */
@Entity
@Table(name="container_stats")
public class ContainerStatsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="container_stats_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long containerStatsId;
	
	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	
	@Column(name="stats_date")
	private Date statsDate;

	@Column(name="total_container")
	private int totalContainer;
	
	@Column(name="delta_value")
	private int deltaValue;

	/**
	 * @return the containerStatsId
	 */
	public Long getContainerStatsId() {
		return containerStatsId;
	}

	/**
	 * @param containerStatsId the containerStatsId to set
	 */
	public void setContainerStatsId(Long containerStatsId) {
		this.containerStatsId = containerStatsId;
	}

	/**
	 * @return the component
	 */
	public ComponentEntity getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(ComponentEntity component) {
		this.component = component;
	}

	/**
	 * @return the environment
	 */
	public EnvironmentEntity getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(EnvironmentEntity environment) {
		this.environment = environment;
	}

	/**
	 * @return the statsDate
	 */
	public Date getStatsDate() {
		return statsDate;
	}

	/**
	 * @param statsDate the statsDate to set
	 */
	public void setStatsDate(Date statsDate) {
		this.statsDate = statsDate;
	}

	/**
	 * @return the totalContainer
	 */
	public int getTotalContainer() {
		return totalContainer;
	}

	/**
	 * @param totalContainer the totalContainer to set
	 */
	public void setTotalContainer(int totalContainer) {
		this.totalContainer = totalContainer;
	}

	/**
	 * @return the deltaValue
	 */
	public int getDeltaValue() {
		return deltaValue;
	}

	/**
	 * @param deltaValue the deltaValue to set
	 */
	public void setDeltaValue(int deltaValue) {
		this.deltaValue = deltaValue;
	}
}
