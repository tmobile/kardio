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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity class for counter_metric
 */
@Entity
@Table(name="counter_metric")
public class CounterMetricEntity extends GeneralCounterMetricEntity implements Serializable {    
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "counter_metric_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int metricId;
		
	/**
	 * @return the metricId
	 */
	public int getMetricId() {
		return metricId;
	}

	/**
	 * @param metricId the metricId to set
	 */
	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}
}
