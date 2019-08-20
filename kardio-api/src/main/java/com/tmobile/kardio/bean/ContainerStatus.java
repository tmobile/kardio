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

/**
 * Bean class for Container Status graph Api
 */
public class ContainerStatus {

	private int componentId;
	private String componentName;
	private String statusDate;
	private long totalContainers;
	private int deltaValue;
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
	/**
	 * @return the totalContainers
	 */
	public long getTotalContainers() {
		return totalContainers;
	}
	/**
	 * @param totalCont the totalContainers to set
	 */
	public void setTotalContainers(long totalCont) {
		this.totalContainers = totalCont;
	}
	/**
	 * @return the deltaValue
	 */
	public int getDeltaValue() {
		return deltaValue;
	}
	/**
	 * @param deltaValue the deltaValue to set
	 */
	public void setDeltaValue(int deltaValue) {
		this.deltaValue = deltaValue;
	}
	
}
