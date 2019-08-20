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
 * App Full Name to store the required details of a component VO for component data in dashboard
 * 
 */
public class AppFullName {

    private int appId;
    private int componentId;
    private String componentName;
    private String componentFullName;

    /**
     * @return the AppId
     */
    public int getAppId() {
        return appId;
    }

    /**
     * @param AppId
     *            the AppId to set
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * 
     * @return componentId
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * 
     * @param componentId
     */
    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    /**
     * @return childComponentName the childComponentName to set
     */

    public String getComponentFullName() {
        return componentFullName;
    }

    /**
     * @param childComponentName
     *            the childComponentName to set
     */
    public void setComponentFullName(String componentFullName) {
        this.componentFullName = componentFullName;
    }

    /**
     * @return componentName the basic name of the child component
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName
     *            the basic name of the child component tp set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppFullName other = (AppFullName) obj;
		if (componentId != other.componentId)
			return false;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		return true;
	}
}
