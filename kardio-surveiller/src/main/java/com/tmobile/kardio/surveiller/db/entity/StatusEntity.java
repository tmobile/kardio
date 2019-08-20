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
package com.tmobile.kardio.surveiller.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="status")
public class StatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="status_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long statusId;
	
	@Column(name="status_name")
	private String statusName;
	
	@Column(name="status_desc")
	private String statusDesc;

	/**
	 * Get statusId
	 * @return statusId
	 */
	public Long getStatusId() {
		return statusId;
	}

	/**
	 * Set statusId
	 * @param statusId
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	/**
	 * Get statusName
	 * @return statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * Set statusName
	 * @param statusName
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * Get statusDesc
	 * @return statusDesc
	 */
	public String getStatusDesc() {
		return statusDesc;
	}

	/**
	 * Set statusDesc
	 * @param statusDesc
	 */
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	
}
