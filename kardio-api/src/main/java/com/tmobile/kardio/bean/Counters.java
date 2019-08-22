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

import java.util.List;

/**
 * Counters to store the details in regards to a counter displayed in the UI the details stored are counterName,
 * position, metric value, metric date, trend, del ind, counter description, counter Id VO for counter metadata
 * 
 */
public class Counters {

    private String counterName;
    private int position;
    private Float metricVal;
    private String metricDate;
    private List<Float> trend;
    private int delInd;
    private String counterDesc;
    private int counterId;

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
     * @return the counterDesc
     */
    public String getCounterDesc() {
        return counterDesc;
    }

    /**
     * @param counterDesc
     *            the counterDesc to set
     */
    public void setCounterDesc(String counterDesc) {
        this.counterDesc = counterDesc;
    }

    /**
     * @return the counterId
     */
    public int getCounterId() {
        return counterId;
    }

    /**
     * @param counterId
     *            the counterId to set
     */
    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    /**
     * @return the counterName
     */
    public String getCounterName() {
        return counterName;
    }

    /**
     * @return the trend
     */
    public List<Float> getTrend() {
        return trend;
    }

    /**
     * @param trend
     *            the trend to set
     */
    public void setTrend(List<Float> trend) {
        this.trend = trend;
    }

    /**
     * @param counterName
     *            the counterName to set
     */
    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the metricVal
     */
    public Float getMetricVal() {
        return metricVal;
    }

    /**
     * @param metricVal
     *            the metricVal to set
     */
    public void setMetricVal(Float metricVal) {
        this.metricVal = metricVal;
    }

    /**
     * @return the metricDate
     */
    public String getMetricDate() {
        return metricDate;
    }

    /**
     * @param metricDate
     *            the metricDate to set
     */
    public void setMetricDate(String metricDate) {
        this.metricDate = metricDate;
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
		Counters other = (Counters) obj;
		if (counterName == null) {
			if (other.counterName != null)
				return false;
		} else if (!counterName.equals(other.counterName))
			return false;
		return true;
	}



}
