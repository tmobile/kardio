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
 * Entity class for k8s_api_status
 */
@Entity
@Table(name="k8s_api_status")
public class K8sApiStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="k8s_api_status_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long k8sApiStatusId;
	
	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	
	@Column(name="status_date")
	private Date statusDate;

	@Column(name="total_api")
	private int totalApi;

	/**
	 * @return the apiStatusId
	 */
	public Long getK8sApiStatusId() {
		return k8sApiStatusId;
	}

	/**
	 * @param apiStatusId the apiStatusId to set
	 */
	public void setK8sApiStatusId(Long k8sApiStatusId) {
		this.k8sApiStatusId = k8sApiStatusId;
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
	 * @return the statusDate
	 */
	public Date getStatusDate() {
		return statusDate;
	}

	/**
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	/**
	 * @return the totalApi
	 */
	public int getTotalApi() {
		return totalApi;
	}

	/**
	 * @param totalApi the totalApi to set
	 */
	public void setTotalApi(int totalApi) {
		this.totalApi = totalApi;
	}
}
