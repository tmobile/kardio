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
 * Entity class for k8s_obj_pods
 * This table holds all the pods tagged to K8s object, other than deployment
 */
@Entity
@Table(name="k8s_obj_pods")
public class K8sObjectPodsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="k8s_obj_pods_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long k8sObjPodsId;
	
	@Column(name="obj_name")
	private String objectName;
	
	@Column(name="pods")
	private int pods;
	
	@Column(name="containers")
	private int containers;
	
	@Column(name="status_date")
	private Date statusDate;
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	

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
	 * @return the k8sObjPodsId
	 */
	public Long getK8sObjPodsId() {
		return k8sObjPodsId;
	}

	/**
	 * @param k8sObjPodsId the k8sObjPodsId to set
	 */
	public void setK8sObjPodsId(Long k8sObjPodsId) {
		this.k8sObjPodsId = k8sObjPodsId;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the pods
	 */
	public int getPods() {
		return pods;
	}

	/**
	 * @param pods the pods to set
	 */
	public void setPods(int pods) {
		this.pods = pods;
	}

	/**
	 * @return the containers
	 */
	public int getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public void setContainers(int containers) {
		this.containers = containers;
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
	
	
	
}
