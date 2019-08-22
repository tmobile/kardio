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
package com.tmobile.kardio.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * ComponentHistory to store the historical data about the status of a component over period of time VO for History tab
 * 
 */
public class ComponentHistory {
    private int componentId;
    private String componentName;
    private String childComponentName;
    private String componentType;
    private int parentComponentId;
    private String parentComponentName;
    private String platform;
    private List<StatusHistory> statusHistory;
    

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

	public ComponentHistory copy() {
        ComponentHistory copy = new ComponentHistory();
        copy.setComponentName(componentName);
        copy.setComponentId(componentId);
        copy.setComponentType(componentType);
        copy.setStatusHistory(statusHistory);
        copy.setParentComponentId(parentComponentId);
        if (statusHistory != null) {
            List<StatusHistory> copyStatusHistory = new ArrayList<StatusHistory>();
            for (StatusHistory statHis : statusHistory) {
                copyStatusHistory.add(statHis.copy());
            }
            copy.setStatusHistory(copyStatusHistory);
        }
        return copy;
    }

    /**
     * @return the componentId
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * @param componentId
     *            the componentId to set
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
     * @param componentName
     *            the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the childComponentName
     */
    public String getChildComponentName() {
        return childComponentName;
    }

    /**
     * @param childComponentName
     *            the childComponentName to set
     */
    public void setChildComponentName(String childComponentName) {
        this.childComponentName = childComponentName;
    }

    /**
     * Get Component Type.
     * 
     * @return
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * Set Component Type.
     * 
     * @param componentType
     */
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * @return the parentComponentId
     */
    public int getParentComponentId() {
        return parentComponentId;
    }

    /**
     * @param parentComponentId
     *            the parentComponentId to set
     */
    public void setParentComponentId(int parentComponentId) {
        this.parentComponentId = parentComponentId;
    }

    /**
     * @return the parentComponentName
     */
    public String getParentComponentName() {
        return parentComponentName;
    }

    /**
     * @param parentComponentName
     *            the parentComponentName to set
     */
    public void setParentComponentName(String parentComponentName) {
        this.parentComponentName = parentComponentName;
    }

    /**
     * @return the statusHistory
     */
    public List<StatusHistory> getStatusHistory() {
        return statusHistory;
    }

    /**
     * @param statusHistory
     *            the statusHistory to set
     */
    public void setStatusHistory(List<StatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

}
