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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Entity class for counter_metric
 */
@MappedSuperclass
public class GeneralCounterMetricEntity {

		
	@Column(name = "env_counter_id", nullable = false , insertable= false, updatable=false)
	private int envCounterId;
			
	@Column(name = "metric_val")
	private float metricVal;
	
	@Column(name = "metric_date")
	private Date metricDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "env_counter_id", nullable = false)
	private EnvCounterEntity envCounter;

	
	/**
	 * @return the envCounter
	 */
	public EnvCounterEntity getEnvCounter() {
		return envCounter;
	}

	/**
	 * @param envCounter the envCounter to set
	 */
	public void setEnvCounter(EnvCounterEntity envCounter) {
		this.envCounter = envCounter;
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
	 * @return the metricVal
	 */
	public float getMetricVal() {
		return metricVal;
	}

	/**
	 * @param metricVal the metricVal to set
	 */
	public void setMetricVal(float metricVal) {
		this.metricVal = metricVal;
	}

	/**
	 * @return the metricDate
	 */
	public Date getMetricDate() {
		return metricDate;
	}

	/**
	 * @param metricDate the metricDate to set
	 */
	public void setMetricDate(Date metricDate) {
		this.metricDate = metricDate;
	}
	
}
