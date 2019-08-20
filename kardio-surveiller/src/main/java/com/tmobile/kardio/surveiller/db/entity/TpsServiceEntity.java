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
/**
 * 
 */
package com.tmobile.kardio.surveiller.db.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * tps_service table holds the TPS values for each service.
 *
 */
@Entity
@Table(name="tps_service")
public class TpsServiceEntity {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="tps_service_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int tpsserviceId;

	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	
	@Column(name = "tps_value")
	private Float tpsValue;
	
	@Column(name = "launch_date")
	private Timestamp launchDate;
	
	@Column(name = "last_updated")
	private Timestamp lastUpdated;
	
	@Column(name = "latency_value")
	private Float latencyValue;
	
	/**
	 * @return the latencyValue
	 */
	public float getLatencyValue() {
		return latencyValue;
	}

	/**
	 * @param latencyValue the latencyValue to set
	 */
	public void setLatencyValue(float latencyValue) {
		this.latencyValue = latencyValue;
	}

	/**
	 * @return the tpsserviceId
	 */
	public int getTpsserviceId() {
		return tpsserviceId;
	}

	/**
	 * @param tpsserviceId the tpsserviceId to set
	 */
	public void setTpsserviceId(int tpsserviceId) {
		this.tpsserviceId = tpsserviceId;
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
	 * @return the tpsValue
	 */
	public float getTpsValue() {
		return tpsValue;
	}

	/**
	 * @param tpsValue the tpsValue to set
	 */
	public void setTpsValue(float tpsValue) {
		this.tpsValue = tpsValue;
	}

	/**
	 * @return the launchDate
	 */
	public Timestamp getLaunchDate() {
		return launchDate;
	}

	/**
	 * @param launchDate the launchDate to set
	 */
	public void setLaunchDate(Timestamp launchDate) {
		this.launchDate = launchDate;
	}

	/**
	 * @return the lastUpdated
	 */
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	
}
