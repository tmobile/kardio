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
package com.tmobile.kardio.surveiller.enums;

/**
 * Enumeration to store the statuses
 */
public enum Status {

	UP (1L, "u", "Service is operating normally"),
	DOWN (2L, "d", "Service disruption"),
	WARNING(3L, "w", "Service degradation"),
	DISRUPT(4L, "g", "Information");
	
	private final Long statusId;
	private String statusName;
	private String statusDesc;
	
	private Status(Long statusId, String statusName, String statusDesc) {
		this.statusId = statusId;
		this.statusName = statusName;
		this.statusDesc = statusDesc;
	}

	/**
	 * Get statusName
	 * @return statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	/**
	 * Get statusId
	 * @return statusId
	 */
	public Long getStatusId() {
		return statusId;
	}
	
	
}
