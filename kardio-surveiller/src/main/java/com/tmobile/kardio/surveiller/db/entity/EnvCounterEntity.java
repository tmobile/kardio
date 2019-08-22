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
package com.tmobile.kardio.surveiller.db.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Operations on Environemnt_Counter
 *
 */
@Entity
@Table(name="env_counter")
public class EnvCounterEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="env_counter_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int envCounterId;
	
	@Column(name="environment_id", insertable=false, updatable=false)
	private Integer environmentId;
	
	@Column(name="counter_id", insertable=false, updatable=false)
	private int counterId;
	
	@Column(name = "counter_metric_type_id", insertable=false, updatable=false)
	private int metricTypeId;
	
	@ManyToOne
	@JoinColumn(name = "counter_id", nullable = false)
	private CounterEntity counter;

	@OneToMany(mappedBy = "envCounter", fetch = FetchType.LAZY)
	private Set<CounterMetricEntity> countMetric;
	
	@ManyToOne
	@JoinColumn(name = "environment_id", nullable = true)
	private EnvironmentEntity environment;
	
	@ManyToOne
	@JoinColumn(name = "counter_metric_type_id", nullable = true)
	private CounterMetricTypeEntity countMetricType;
	
	@Column(name = "parameter_1")
	private String parameter1;
	
	@Column(name = "parameter_2")
	private String parameter2;
	

	@Column(name = "platform")
	private String platform;

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the countMetric
	 */
	public Set<CounterMetricEntity> getCountMetric() {
		return countMetric;
	}

	/**
	 * @param countMetric the countMetric to set
	 */
	public void setCountMetric(Set<CounterMetricEntity> countMetric) {
		this.countMetric = countMetric;
	}

	
	
	/**
	 * @return the counter
	 */
	public CounterEntity getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(CounterEntity counter) {
		this.counter = counter;
	}

	/**
	 * @return the envCounterId
	 */
	public int getEnvCounterId() {
		return envCounterId;
	}

	/**
	 * @param envCounterId the envCounterId to set
	 */
	public void setEnvCounterId(int envCounterId) {
		this.envCounterId = envCounterId;
	}

	/**
	 * @return the environmentId
	 */
	public Integer getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @return the counterId
	 */
	public int getCounterId() {
		return counterId;
	}

	/**
	 * @param counterId the counterId to set
	 */
	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}

	/**
	 * @return the metricTypeId
	 */
	public int getMetricTypeId() {
		return metricTypeId;
	}

	/**
	 * @param metricTypeId the metricTypeId to set
	 */
	public void setMetricTypeId(int metricTypeId) {
		this.metricTypeId = metricTypeId;
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
	
	public CounterMetricTypeEntity getCountMetricType() {
		return countMetricType;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setCountMetricType(CounterMetricTypeEntity countMetricType) {
		this.countMetricType = countMetricType;
	}

	/**
	 * @return the parameter1
	 */
	public String getParameter1() {
		return parameter1;
	}

	/**
	 * @param parameter1 the parameter1 to set
	 */
	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	/**
	 * @return the parameter2
	 */
	public String getParameter2() {
		return parameter2;
	}

	/**
	 * @param parameter2 the parameter2 to set
	 */
	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}
}
