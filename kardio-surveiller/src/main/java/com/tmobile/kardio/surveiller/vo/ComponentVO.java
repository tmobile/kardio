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
package com.tmobile.kardio.surveiller.vo;

/**
 * The VO that contains the component details
 */
public class ComponentVO {

	private int componentId;
	private String componentName;
	private String componentDesc;
	private int parentComponentId;
	private String parentComponentName;
	private int componentTypeId;
	private int delInd;

	/**
	 * Get componentId
	 * @return componentId
	 */
	public synchronized int getComponentId() {
		return componentId;
	}

	/**
	 * Set componentId
	 * @param componentId
	 */
	public synchronized void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	/**
	 * Get componentName
	 * @return
	 */
	public synchronized String getComponentName() {
		return componentName;
	}

	/**
	 * set the componentName
	 * @param componentName
	 */
	public synchronized void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * Get componentDesc
	 * @return
	 */
	public synchronized String getComponentDesc() {
		return componentDesc;
	}

	/**
	 * Set componentDesc
	 * @param componentDesc
	 */
	public synchronized void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

	/**
	 * Get parentComponentId;
	 * @return
	 */
	public synchronized int getParentComponentId() {
		return parentComponentId;
	}

	/**
	 * Set parentComponentId
	 * @param parentComponentId
	 */
	public synchronized void setParentComponentId(int parentComponentId) {
		this.parentComponentId = parentComponentId;
	}

	/**
	 * Get componentTypeId 
	 * @return
	 */
	public synchronized int getComponentTypeId() {
		return componentTypeId;
	}

	/**
	 * Set componentTypeId
	 * @param componentTypeId
	 */
	public synchronized void setComponentTypeId(int componentTypeId) {
		this.componentTypeId = componentTypeId;
	}

	/**
	 * Get delInd
	 * @return delInd
	 */
	public synchronized int getDelInd() {
		return delInd;
	}
	
	/**
	 * Set delInd
	 * @param delInd
	 */
	public synchronized void setDelInd(int delInd) {
		this.delInd = delInd;
	}

	/**
	 * Get parentComponentName
	 * @return parentComponentName
	 */
	public String getParentComponentName() {
		return parentComponentName;
	}

	/**
	 * Set parentComponentName
	 * @param parentComponentName
	 */
	public void setParentComponentName(String parentComponentName) {
		this.parentComponentName = parentComponentName;
	}
	
	
}
