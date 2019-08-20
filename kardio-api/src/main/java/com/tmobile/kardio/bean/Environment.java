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
 * Environment stored the details of an environment. VO for environment data
 * 
 */
public class Environment {

    

	private int environmentId;
    private String environmentName;
    private String environmentDesc;
    private String marathonUrl;
    private String marathonUserName;
    private String marathonPassword;
    private int displayOrder;
    private int envLock;
    private String k8sUrl;
    private String k8sUserName;
    private String k8sPassword;

    /**
     * Get environmentId
     * 
     * @return environmentId
     */
    public int getEnvironmentId() {
        return environmentId;
    }

    /**
     * Set environmentId
     * 
     * @param environmentId
     */
    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    /**
     * Get environementName
     * 
     * @return environementName
     */
    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * Set environementName
     * 
     * @param environementName
     */
    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    /**
     * Get environmentDesc
     * 
     * @return the environmentDesc
     */
    public String getEnvironmentDesc() {
        return environmentDesc;
    }

    /**
     * Set environmentDesc
     * 
     * @param environmentDesc
     *            the environmentDesc to set
     */
    public void setEnvironmentDesc(String environmentDesc) {
        this.environmentDesc = environmentDesc;
    }
    
    /**
     * Get Marathon url
     * 
     * @return marathonUrl
     */
    public String getMarathonUrl() {
        return marathonUrl;
    }

    /**
     * Set Marathon url
     * 
     * @param marathonUserName
     */
    public void setMarathonUrl(String marathonUrl) {
        this.marathonUrl = marathonUrl;
    }

    /**
     * Get Marathon user name
     * 
     * @return marathonUserName
     */
    public String getMarathonUserName() {
        return marathonUserName;
    }

    /**
     * Set Marathon user name
     * 
     * @param marathonUserName
     */
    public void setMarathonUserName(String marathonUserName) {
        this.marathonUserName = marathonUserName;
    }

    /**
     * Get Marathon password
     * 
     * @return marathonPassword
     */
    public String getMarathonPassword() {
        return marathonPassword;
    }

    /**
     * Set marathon password
     * 
     * @param marathonPassword
     */
    public void setMarathonPassword(String marathonPassword) {
        this.marathonPassword = marathonPassword;
    }

	/**
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	 /**
     * @return the envLock
     */
    public int getEnvLock() {
        return envLock;
    }

    /**
     * @param envLock
     *            the envLock to set
     */
    public void setEnvLock(int envLock) {
        this.envLock = envLock;
    }

	public String getK8sUrl() {
		return k8sUrl;
	}

	public void setK8sUrl(String k8sUrl) {
		this.k8sUrl = k8sUrl;
	}

	public String getK8sUserName() {
		return k8sUserName;
	}

	public void setK8sUserName(String k8sUserName) {
		this.k8sUserName = k8sUserName;
	}

	public String getK8sPassword() {
		return k8sPassword;
	}

	public void setK8sPassword(String k8sPassword) {
		this.k8sPassword = k8sPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environmentName == null) ? 0 : environmentName.hashCode());
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
		Environment other = (Environment) obj;
		if (environmentName == null) {
			if (other.environmentName != null)
				return false;
		} else if (!environmentName.equals(other.environmentName))
			return false;
		return true;
	}

}
