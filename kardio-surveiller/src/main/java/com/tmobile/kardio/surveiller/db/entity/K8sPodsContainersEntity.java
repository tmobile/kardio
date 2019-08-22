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
 * Entity class for k8s_pods_containers
 *
 */
@Entity
@Table(name="k8s_pods_containers")
public class K8sPodsContainersEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="k8s_pods_containers_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long k8sPodsStatusId;
	
	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	
	@Column(name="status_date")
	private Date statusDate;

	@Column(name="total_pods")
	private int totalPods;
	
	@Column(name="total_containers")
	private int totalContainers;

	/**
	 * @return the k8sPodsStatusId
	 */
	public Long getK8sPodsStatusId() {
		return k8sPodsStatusId;
	}

	/**
	 * @param k8sPodsStatusId the k8sPodsStatusId to set
	 */
	public void setK8sPodsStatusId(Long k8sPodsStatusId) {
		this.k8sPodsStatusId = k8sPodsStatusId;
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
	 * @return the totalPods
	 */
	public int getTotalPods() {
		return totalPods;
	}

	/**
	 * @param totalPods the totalPods to set
	 */
	public void setTotalPods(int totalPods) {
		this.totalPods = totalPods;
	}

	/**
	 * @return the totalContainer
	 */
	public int getTotalContainers() {
		return totalContainers;
	}

	/**
	 * @param totalContainer the totalContainer to set
	 */
	public void setTotalContainers(int totalContainers) {
		this.totalContainers = totalContainers;
	}
}
