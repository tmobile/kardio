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
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class for counter_metric_type
 */
@Entity
@Table(name="counter_metric_type")
public class CounterMetricTypeEntity implements Serializable{
    
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "counter_metric_type_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int metricTypeId;
	
	@Column(name = "counter_metric_type")
	private String counterMetricType;
		

	@Column(name = "counter_metric_type_desc")
	private String counterMetricTypeDesc;
	
	@Column(name = "counter_metric_type_class_name")
	private String counterMetricTypeClassName;
	
	@OneToMany(mappedBy = "countMetricType", fetch = FetchType.LAZY)
	private Set<EnvCounterEntity> envCounter;
	
	/**
	 * @return the envCounter
	 */
	public Set<EnvCounterEntity> getEnvCounter() {
		return envCounter;
	}

	/**
	 * @param envCounter the envCounter to set
	 */
	public void setEnvCounter(Set<EnvCounterEntity> envCounter) {
		this.envCounter = envCounter;
	}

	/**
	 * @return the metricTypeId
	 */
	public int getMetricTypeId() {
		return metricTypeId;
	}

	/**
	 * @param metricId the metricTypeId to set
	 */
	public void setMetricTypeId(int metricTypeId) {
		this.metricTypeId = metricTypeId;
	}

	/**
	 * @return the counterMetricType
	 */
	public String getCounterMetricType() {
		return counterMetricType;
	}

	/**
	 * @param counterMetricType the counterMetricType to set
	 */
	public void setCounterMetricType(String counterMetricType) {
		this.counterMetricType = counterMetricType;
	}

	/**
	 * @return the counterMetricTypeDesc
	 */
	public String getCounterMetricTypeDesc() {
		return counterMetricTypeDesc;
	}

	/**
	 * @param counterMetricTypeDesc the counterMetricTypeDesc to set
	 */
	public void setCounterMetricTypeDesc(String counterMetricTypeDesc) {
		this.counterMetricTypeDesc = counterMetricTypeDesc;
	}

	/**
	 * @return the counterMetricTypeClassName
	 */
	public String getCounterMetricTypeClassName() {
		return counterMetricTypeClassName;
	}

	/**
	 * @param counterMetricTypeClassName the counterMetricTypeClassName to set
	 */
	public void setCounterMetricTypeClassName(String counterMetricTypeClassName) {
		this.counterMetricTypeClassName = counterMetricTypeClassName;
	}
	
}
