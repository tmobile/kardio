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

public class KardioStatus {

	protected int componentId;
	protected String componentName;
	protected int environmentId;
	protected String statusDate;
	
	public int getComponentId() {
		return componentId;
	}
	
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	public int getEnvironmentId() {
		return environmentId;
	}
	
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}
	
	public String getStatusDate() {
		return statusDate;
	}
	
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
}
