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
package com.tmobile.kardio.surveiller.vo;

/**
 * Contains the subscription details
 */
public class SubscriptionVO {

	private int componentId;
	private int subscriptionType;
	private String subscriptionValue;
	private int environmentId;
	private int globalComponentTypeId;

	/**
	 * Get componentId
	 * 
	 * @return componentId
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * Set componentId
	 * 
	 * @param componentId
	 */
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	/**
	 * @return the subscriptionType
	 */
	public int getSubscriptionType() {
		return subscriptionType;
	}

	/**
	 * @param subscriptionType the subscriptionType to set
	 */
	public void setSubscriptionType(int subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	/**
	 * @return the subscriptionValue
	 */
	public String getSubscriptionValue() {
		return subscriptionValue;
	}

	/**
	 * @param subscriptionValue the subscriptionValue to set
	 */
	public void setSubscriptionValue(String subscriptionValue) {
		this.subscriptionValue = subscriptionValue;
	}

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
	 * @return the globalComponentTypeId
	 */
	public int getGlobalComponentTypeId() {
		return globalComponentTypeId;
	}

	/**
	 * @param globalComponentTypeId the globalComponentTypeId to set
	 */
	public void setGlobalComponentTypeId(int globalComponentTypeId) {
		this.globalComponentTypeId = globalComponentTypeId;
	}

}
