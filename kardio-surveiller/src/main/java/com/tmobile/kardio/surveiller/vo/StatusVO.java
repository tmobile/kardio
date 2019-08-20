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
package com.tmobile.kardio.surveiller.vo;

import com.tmobile.kardio.surveiller.enums.Status;

/**
 * Return VO with status of the health check.
 */
public class StatusVO {
	private Status status;
	private String message;
	
	public StatusVO(Status status){
		this.status = status;
	}
	
	public StatusVO(Status status, String message){
		this.status = status;
		this.message = message;
	}

	/**
	 * @return statusId the statusId
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 *  @param statusId the statusId to set
	 */
	public void setStatusId(Status status) {
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
