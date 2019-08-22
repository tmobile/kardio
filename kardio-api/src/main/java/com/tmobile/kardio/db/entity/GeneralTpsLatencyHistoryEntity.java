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
package com.tmobile.kardio.db.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class GeneralTpsLatencyHistoryEntity {
	
	@ManyToOne
	@JoinColumn(name="environment_id")
	private EnvironmentEntity environment;
	
	@ManyToOne
	@JoinColumn(name="component_id")
	private ComponentEntity component;
	
	@Column(name="tps_value")
	private double tpsValue;
	
	@Column(name="latency_value")
	private double latencyValue;
	
	@Column(name="status_date")
	private Date statusDate;
	
	public ComponentEntity getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(ComponentEntity component) {
		this.component = component;
	}
	
	public EnvironmentEntity getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(EnvironmentEntity environment) {
		this.environment = environment;
	}
	
	public void setTpsValue(Double tpsValue)
	{
		this.tpsValue = tpsValue;
	}
	
	public Double getTpsValue()
	{
		return tpsValue;
	}
	
	public void setLatencyValue(Double latencyValue)
	{
		this.latencyValue = latencyValue;
	}
	
	public Date getStatusDate()
	{
		return statusDate;
	}
	
	public void setStatusDate(Date statusDate)
	{
		this.statusDate = statusDate;
	}
	public double getLatencyValue() {
		return latencyValue;
	}

	public void setLatencyValue(double latencyValue) {
		this.latencyValue = latencyValue;
	}
}
