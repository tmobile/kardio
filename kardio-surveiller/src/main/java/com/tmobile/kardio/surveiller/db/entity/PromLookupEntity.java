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
 * prom_lookup table holds the lookup name in Prometheous for each component.
 *
 */
@Entity
@Table(name="prom_lookup")
public class PromLookupEntity {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="prom_lookup_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int applookupId;

	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@Column(name = "http_path")
	private String httpPath;
	
	@Column(name = "launch_date")
	private Timestamp launchDate;
	
	@Column(name = "last_updated")
	private Timestamp lastUpdated;
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;

	/**
	 * @return the applookupId
	 */
	public int getApplookupId() {
		return applookupId;
	}

	/**
	 * @param applookupId the applookupId to set
	 */
	public void setApplookupId(int applookupId) {
		this.applookupId = applookupId;
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
	 * @return the httpPath
	 */
	public String getHttpPath() {
		return httpPath;
	}

	/**
	 * @param httpPath the httpPath to set
	 */
	public void setHttpPath(String httpPath) {
		this.httpPath = httpPath;
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
	
}
