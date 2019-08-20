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
package com.tmobile.kardio.bean;

public class TpsLatencyHistory {

	private int componentId;
	private double tpsValue;
	private double latencyValue;
	private String componentName;
	private String statusDate;
	/**
	 * @return the componentId
	 */
	public int getComponentId() {
		return componentId;
	}
	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}
	/**
	 * @return the totalTpsValue
	 */
	public double getTpsValue() {
		return tpsValue;
	}
	/**
	 * @param totalTpsValue the totalTpsValue to set
	 */
	public void setTpsValue(double tpsValue) {
		this.tpsValue = tpsValue;
	}
	/**
	 * @return the totalLatencyValue
	 */
	public double getLatencyValue() {
		return latencyValue;
	}
	/**
	 * @param totalLatencyValue the totalLatencyValue to set
	 */
	public void setLatencyValue(double latencyValue) {
		this.latencyValue = latencyValue;
	}
	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}
	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	/**
	 * @return the statusDate
	 */
	public String getStatusDate() {
		return statusDate;
	}
	/**
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	} 
}
