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
/**
 * 
 */
package com.tmobile.kardio.bean;

/**
 * 
 *
 */
public class ApiStatus extends KardioStatus {

	private long totalContainers;
	private long totalApis;
	private int deltaValue;
	private long totalPods;

	/**
	 * @return the totalPods
	 */
	public long getTotalPods() {
		return totalPods;
	}
	/**
	 * @param totalPods the totalPods to set
	 */
	public void setTotalPods(long totalPods) {
		this.totalPods = totalPods;
	}
	/**
	 * @return the totalApis
	 */
	public long getTotalApis() {
		return totalApis;
	}
	/**
	 * @param totalApis the totalApis to set
	 */
	public void setTotalApis(long totalApis) {
		this.totalApis = totalApis;
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
	
	public long getTotalContainers() {
		return totalContainers;
	}
	public void setTotalContainers(long totalContainers) {
		this.totalContainers = totalContainers;
	}
}
