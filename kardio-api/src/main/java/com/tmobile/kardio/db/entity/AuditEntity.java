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
package com.tmobile.kardio.db.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="audit")
public class AuditEntity {
	
@Id	
@Column(name="audit_id")
@GeneratedValue(strategy = GenerationType.IDENTITY)
private String audit_id;

public String getAudit_id() {
	return audit_id;
}


public void setAudit_id(String audit_id) {
	this.audit_id = audit_id;
}

@Column(name="user_id")
private String user_id;

/**
 * Function to get the getUser_id
 * 
 * @return user_id
 */
public String getUser_id() {
	return user_id;
}


/**
 * Function to set userId
 * 
 * @param user_id
 */
public void setUser_id(String user_id) {
	this.user_id = user_id;
}

@Column(name="audit_log")
private String audit_log;

/**
 * Function to get the getAudit_log
 * 
 * @return audit_log
 */
public String getAudit_log() {
	return audit_log;
}

/**
 * Function to set audit_log
 * 
 * @param audit_log
 */
public void setAudit_log(String audit_log) {
	this.audit_log = audit_log;
}

@Column(name="audit_date")
private Date date;

/**
 * Function to get the getDate
 * 
 * @return date
 */
public Date getDate() {
	return date;
}

/**
 * Function to set date
 * 
 * @param date
 */
public void setDate(Date date) {
	this.date = date;
}
}
