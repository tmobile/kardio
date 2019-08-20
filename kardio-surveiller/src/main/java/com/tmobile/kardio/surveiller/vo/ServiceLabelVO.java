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
/**
 * 
 */
package com.tmobile.kardio.surveiller.vo;

import java.util.Map;

/**
 * @author U29842
 *
 */
public class ServiceLabelVO {

	private String serviceName;
	private String appName;
	private int componentId;
	private int numOfContainers=0;
	private int numOfPods = 0;
	private Map<String, String> label;
	
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return the numOfContainers
	 */
	public int getNumOfContainers() {
		return numOfContainers;
	}
	/**
	 * @param numOfContainers the numOfContainers to set
	 */
	public void setNumOfContainers(int numOfContainers) {
		this.numOfContainers = numOfContainers;
	}
	/**
	 * @return the numOfPods
	 */
	public int getNumOfPods() {
		return numOfPods;
	}
	/**
	 * @param numOfPods the numOfPods to set
	 */
	public void setNumOfPods(int numOfPods) {
		this.numOfPods = numOfPods;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
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
	 * @return the label
	 */
	public Map<String, String> getLabel() {
		return label;
	}
	/**
	 * @param depLabelMap the label to set
	 */
	public void setLabel(Map<String, String> depLabelMap) {
		this.label = depLabelMap;
	}
		
}
