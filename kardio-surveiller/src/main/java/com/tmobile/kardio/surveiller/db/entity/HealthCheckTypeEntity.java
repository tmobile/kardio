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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="health_check_type")
public class HealthCheckTypeEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="health_check_type_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int healthCheckTypeId;
	
	@Column(name="health_check_type_name")
	private String healthCheckTypeName;
	
	@Column(name="health_check_type_desc")
	private String healthCheckTypeDesc;
	
	@Column(name="health_check_class_name")
	private String healthCheckTypeClass;

	/**
	 * Get healthCheckTypeId
	 * @return healthCheckTypeId
	 */
	public int getHealthCheckTypeId() {
		return healthCheckTypeId;
	}

	/**
	 * Set healthCheckTypeId
	 * @param healthCheckTypeId
	 */
	public void setHealthCheckTypeId(int healthCheckTypeId) {
		this.healthCheckTypeId = healthCheckTypeId;
	}

	/**
	 * Get healthCheckTypeName
	 * @return healthCheckTypeName
	 */
	public String getHealthCheckTypeName() {
		return healthCheckTypeName;
	}

	/**
	 * Set healthCheckTypeName
	 * @param healthCheckTypeName
	 */
	public void setHealthCheckTypeName(String healthCheckTypeName) {
		this.healthCheckTypeName = healthCheckTypeName;
	}

	/**
	 * Get healthCheckTypeDesc
	 * @return healthCheckTypeDesc
	 */
	public String getHealthCheckTypeDesc() {
		return healthCheckTypeDesc;
	}

	/**
	 * Set healthCheckTypeDesc
	 * @param healthCheckTypeDesc
	 */
	public void setHealthCheckTypeDesc(String healthCheckTypeDesc) {
		this.healthCheckTypeDesc = healthCheckTypeDesc;
	}

	/**
	 * Get healthCheckTypeClass
	 * @return healthCheckTypeClass
	 */
	public String getHealthCheckTypeClass() {
		return healthCheckTypeClass;
	}

	/**
	 * Set healthCheckTypeClass
	 * @param healthCheckTypeClass
	 */
	public void setHealthCheckTypeClass(String healthCheckTypeClass) {
		this.healthCheckTypeClass = healthCheckTypeClass;
	}
}
