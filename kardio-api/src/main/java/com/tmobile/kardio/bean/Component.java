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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Component to store the required details of a component VO for component data in dashboard
 * 
 */
public class Component {

    private int componentId;
    private int parentComponentId;
    private String parentComponentName;
    private String componentName;
    private String childComponentName;
    private String componentType;
    private boolean recentEvent;
    private List<Region> region;
    private Timestamp componentDate;
    private boolean isSubscribed;
    private List<String> roles;
    private List<HealtCheckEnvironment> hcEnvList;
    @JsonIgnore
    private List<Component> children;
    private int delInd;
    private String compDesc;
    private String appFullName;
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
     * Makes a copy.
     * 
     * @return
     */
    public Component copy() {
        Component copy = new Component();
        copy.setComponentId(componentId);
        copy.setParentComponentId(parentComponentId);
        copy.setComponentName(componentName);
        copy.setChildComponentName(childComponentName);
        copy.setComponentType(componentType);
        copy.setRecentEvent(recentEvent);
        copy.setComponentDate(componentDate);
        if (region != null) {
            List<Region> copyRegion = new ArrayList<Region>();
            for (Region reg : region) {
                copyRegion.add(reg.copy());
            }
            copy.setRegion(copyRegion);
        }
        if (roles != null) {
            List<String> copyRole = new ArrayList<String>();
            for (String roleStr : roles) {
                copyRole.add(roleStr);
            }
            copy.setRoles(copyRole);
        }
        return copy;
    }

    /**
     * 
     * @return
     */
    @JsonIgnore
    public Timestamp getComponentDate() {
        return componentDate;
    }

    /**
     * 
     * @param componentDate
     */
    public void setComponentDate(Timestamp componentDate) {
        this.componentDate = componentDate;
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
     * @return the region
     */
    public List<Region> getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(List<Region> region) {
        this.region = region;
    }

    /**
     * 
     * @return
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
     * 
     * @return
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * 
     * @param componentType
     */
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * 
     * @return
     */
    public boolean isRecentEvent() {
        return recentEvent;
    }

    /**
     * 
     * @param recentEvent
     */
    public void setRecentEvent(boolean recentEvent) {
        this.recentEvent = recentEvent;
    }

    /**
     * Get the subscription status for user
     * 
     * @return isSubscribed
     */
    public boolean isSubscribed() {
        return isSubscribed;
    }

    /**
     * Set the subscription status for user
     * 
     * @param isSubscribed
     */
    public void setSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @param roles
     *            the roles to set
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * @return children
     */
    public List<Component> getChildren() {
        return children;
    }

    /**
     * @param childern
     *            set list of child components
     */
    public void setChildren(List<Component> children) {
        this.children = children;
    }

    /**
     * @return the delInd
     */
    public int getDelInd() {
        return delInd;
    }

    /**
     * @param delInd
     *            the delInd to set
     */
    public void setDelInd(int delInd) {
        this.delInd = delInd;
    }

    /**
     * Get the description of the component
     * 
     * @return
     */
    public String getCompDesc() {
        return compDesc;
    }

    /**
     * Set the description of the component
     * 
     * @param compDesc
     */
    public void setCompDesc(String compDesc) {
        this.compDesc = compDesc;
    }

    /**
     * Get the hcEnvList
     * @return hcEnvList
     */
	public List<HealtCheckEnvironment> getHcEnvList() {
		return hcEnvList;
	}

	/**
	 * Set the hcEnvList
	 * @param hcEnvList
	 */
	public void setHcEnvList(List<HealtCheckEnvironment> hcEnvList) {
		this.hcEnvList = hcEnvList;
	}

    /**
     * Get the FullName of the component
     * 
     * @return
     */
    public String getAppFullName() {
        return appFullName;
    }

    /**
     * Set the description of the component
     * 
     * @param compDesc
     */
    public void setAppFullName(String appFullName) {
        this.appFullName = appFullName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appFullName == null) ? 0 : appFullName.hashCode());
		result = prime * result + ((childComponentName == null) ? 0 : childComponentName.hashCode());
		result = prime * result + componentId;
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
		Component other = (Component) obj;
		if (appFullName == null) {
			if (other.appFullName != null)
				return false;
		} else if (!appFullName.equals(other.appFullName))
			return false;
		if (childComponentName == null) {
			if (other.childComponentName != null)
				return false;
		} else if (!childComponentName.equals(other.childComponentName))
			return false;
		if (componentId != other.componentId)
			return false;
		return true;
	} 
    
}
