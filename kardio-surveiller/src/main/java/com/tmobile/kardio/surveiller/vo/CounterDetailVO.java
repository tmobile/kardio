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
package com.tmobile.kardio.surveiller.vo;

/**
 * Contains the details regarding counter
 */
public class CounterDetailVO {
	private int environmentCounterId;
	private int environmentId;
	private int counterId;
	private int counterMetricTypeId;
	private String parameter1;
	private String parameter2;
	private float metricValue;
	private String counterMetricTypeClassName;
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
	 * @return the environmentCounterId
	 */
	public int getEnvironmentCounterId() {
		return environmentCounterId;
	}
	/**
	 * @param environmentCounterId the environmentCounterId to set
	 */
	public void setEnvironmentCounterId(int environmentCounterId) {
		this.environmentCounterId = environmentCounterId;
	}
	/**
	 * @return the environmentId
	 */
	public int getEnvironmentId() {
		return environmentId;
	}
	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(int environmentId) {
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
	 * @return the counterMetricTypeId
	 */
	public int getCounterMetricTypeId() {
		return counterMetricTypeId;
	}
	/**
	 * @param counterMetricTypeId the counterMetricTypeId to set
	 */
	public void setCounterMetricTypeId(int counterMetricTypeId) {
		this.counterMetricTypeId = counterMetricTypeId;
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
	/**
	 * @return the metricValue
	 */
	public float getMetricValue() {
		return metricValue;
	}
	/**
	 * @param metricValue the metricValue to set
	 */
	public void setMetricValue(float metricValue) {
		this.metricValue = metricValue;
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
