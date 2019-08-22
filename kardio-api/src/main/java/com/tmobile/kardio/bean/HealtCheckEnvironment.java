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

import java.util.Date;

/**
 * Class that represents the environment for health check and the 
 * date the application is created for the environment
 *
 */
public class HealtCheckEnvironment {
	private String envName;
	private Date createdDate;

	/**
	 * Get environment Name
	 * @return envName
	 */
	public String getEnvName() {
		return envName;
	}

	/**
	 * Set environment Name
	 * @param envName
	 */
	public void setEnvName(String envName) {
		this.envName = envName;
	}

	/**
	 * Get Created Date for health check component
	 * @return createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
 
	/**
	 * Set Created Date for health check component
	 * @param createdDate
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
