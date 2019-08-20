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
 * EnvCounter to store the details of a counter in relation to an environment; details stored are envCounterId,
 * counterName, metricType Id, envName, pareamenter1 and parameter2 VO for Admin Counter data edit
 * 
 */
public class EnvCounters {

    private int envCounterId;
    private String counterName;
    private int metricTypeId;
    private String envName;
    private String parameter1;
    private String parameter2;
    private String platform;

    /**
     * @return the envCounterId
     */
    public int getEnvCounterId() {
        return envCounterId;
    }

    /**
     * @param envCounterId
     *            the envCounterId to set
     */
    public void setEnvCounterId(int envCounterId) {
        this.envCounterId = envCounterId;
    }

    /**
     * @return the counterName
     */
    public String getCounterName() {
        return counterName;
    }

    /**
     * @param counterName
     *            the counterName to set
     */
    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    /**
     * @return the metricTypeId
     */
    public int getMetricTypeId() {
        return metricTypeId;
    }

    /**
     * @param metricTypeId
     *            the metricTypeId to set
     */
    public void setMetricTypeId(int metricTypeId) {
        this.metricTypeId = metricTypeId;
    }

    /**
     * @return the envName
     */
    public String getEnvName() {
        return envName;
    }

    /**
     * @param envName
     *            the envName to set
     */
    public void setEnvName(String envName) {
        this.envName = envName;
    }

    /**
     * @return the parameter1
     */
    public String getParameter1() {
        return parameter1;
    }

    /**
     * @param parameter1
     *            the parameter1 to set
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
     * @param parameter2
     *            the parameter2 to set
     */
    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((counterName == null) ? 0 : counterName.hashCode());
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
		EnvCounters other = (EnvCounters) obj;
		if (counterName == null) {
			if (other.counterName != null)
				return false;
		} else if (!counterName.equals(other.counterName))
			return false;
		return true;
	}
}
