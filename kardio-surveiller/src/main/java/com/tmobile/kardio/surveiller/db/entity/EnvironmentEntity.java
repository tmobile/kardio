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
import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="environment")
public class EnvironmentEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="environment_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int environmentId;
    
    @Column(name="environment_name")
    private String environmentName;
    
    @Column(name="environment_desc")
    private String environmentDesc;
    
    @Column(name="general_message")
    private String generalMessage;
    
    @Column(name="app_message")
    private String appMessage;
    
    @Column(name="infra_message")
    private String infraMessage;
    
    @Column(name="display_order")
    private int displayOrder;
    
    @Column(name="marathon_cred")
    private String marathonCred;
    
    @Column(name="environment_lock")
    private int envLock;
    
    @Column(name="marathon_url")
    private String marathonURL;
    
    @Column(name="marathon_json")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob marathonJson;
    
    @Column(name="last_updated_time")
    private Timestamp lastUpdateTime;

    @Column(name="k8s_url")
    private String k8sUrl;
    
    @Column(name="k8s_cred")
    private String k8sCred;    
    
    @Column(name="k8s_tps_query")
    private String k8sTpsQuery; 
    
    @Column(name="k8s_latency_query")
    private String k8sLatencyQuery; 
    
    @Column(name="mesos_tps_query")
    private String mesosTpsQuery; 
    
    @Column(name="mesos_latency_query")
    private String mesosLatencyQuery; 
    
    @Column(name="east_marathon_url")
    private String eastMarathonURL;
    
    @Column(name="east_marathon_json")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob eastMarathonJson;

	@Column(name="east_last_updated_time")
    private Timestamp eastLastUpdateTime;
    
    


    /**
	 * @return the eastMarathonURL
	 */
	public String getEastMarathonURL() {
		return eastMarathonURL;
	}

	/**
	 * @param eastMarathonURL the eastMarathonURL to set
	 */
	public void setEastMarathonURL(String eastMarathonURL) {
		this.eastMarathonURL = eastMarathonURL;
	}

	/**
	 * @return the eastMarathonJson
	 */
	public Blob getEastMarathonJson() {
		return eastMarathonJson;
	}

	/**
	 * @param eastMarathonJson the eastMarathonJson to set
	 */
	public void setEastMarathonJson(Blob eastMarathonJson) {
		this.eastMarathonJson = eastMarathonJson;
	}

	/**
	 * @return the eastLastUpdateTime
	 */
	public Timestamp getEastLastUpdateTime() {
		return eastLastUpdateTime;
	}

	/**
	 * @param eastLastUpdateTime the eastLastUpdateTime to set
	 */
	public void setEastLastUpdateTime(Timestamp eastLastUpdateTime) {
		this.eastLastUpdateTime = eastLastUpdateTime;
	}

	/**
	 * @return the mesosTpsQuery
	 */
	public String getMesosTpsQuery() {
		return mesosTpsQuery;
	}

	/**
	 * @param mesosTpsQuery the mesosTpsQuery to set
	 */
	public void setMesosTpsQuery(String mesosTpsQuery) {
		this.mesosTpsQuery = mesosTpsQuery;
	}

	/**
	 * @return the mesosLatencyQuery
	 */
	public String getMesosLatencyQuery() {
		return mesosLatencyQuery;
	}

	/**
	 * @param mesosLatencyQuery the mesosLatencyQuery to set
	 */
	public void setMesosLatencyQuery(String mesosLatencyQuery) {
		this.mesosLatencyQuery = mesosLatencyQuery;
	}

	/**
	 * @return the k8sTpsQuery
	 */
	public String getK8sTpsQuery() {
		return k8sTpsQuery;
	}

	/**
	 * @param k8sTpsQuery the k8sTpsQuery to set
	 */
	public void setK8sTpsQuery(String k8sTpsQuery) {
		this.k8sTpsQuery = k8sTpsQuery;
	}

	/**
	 * @return the k8sLatencyQuery
	 */
	public String getK8sLatencyQuery() {
		return k8sLatencyQuery;
	}

	/**
	 * @param k8sLatencyQuery the k8sLatencyQuery to set
	 */
	public void setK8sLatencyQuery(String k8sLatencyQuery) {
		this.k8sLatencyQuery = k8sLatencyQuery;
	}

	/**
	 * @return the k8sUrl
	 */
	public String getK8sUrl() {
		return k8sUrl;
	}

	/**
	 * @param k8sUrl the k8sUrl to set
	 */
	public void setK8sUrl(String k8sUrl) {
		this.k8sUrl = k8sUrl;
	}

	/**
	 * @return the k8sCred
	 */
	public String getK8sCred() {
		return k8sCred;
	}

	/**
	 * @param k8sCred the k8sCred to set
	 */
	public void setK8sCred(String k8sCred) {
		this.k8sCred = k8sCred;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
     * Get marathonURL
     * @return marathonURL
     */
	public String getMarathonURL() {
		return marathonURL;
	}

	/**
	 * Set marathonURL
	 * @param marathonURL
	 */
	public void setMarathonURL(String marathonURL) {
		this.marathonURL = marathonURL;
	}

	/**
	 * @return the environmentId
	 */
	public int getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId the environmentId to set
	 */
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}

	/**
	 * @param environmentName the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	/**
	 * @return the environmentDesc
	 */
	public String getEnvironmentDesc() {
		return environmentDesc;
	}

	/**
	 * @param environmentDesc the environmentDesc to set
	 */
	public void setEnvironmentDesc(String environmentDesc) {
		this.environmentDesc = environmentDesc;
	}

	/**
	 * @return the generalMessage
	 */
	public String getGeneralMessage() {
		return generalMessage;
	}

	/**
	 * @param generalMessage the generalMessage to set
	 */
	public void setGeneralMessage(String generalMessage) {
		this.generalMessage = generalMessage;
	}

	/**
	 * @return the appMessage
	 */
	public String getAppMessage() {
		return appMessage;
	}

	/**
	 * @param appMessage the appMessage to set
	 */
	public void setAppMessage(String appMessage) {
		this.appMessage = appMessage;
	}

	/**
	 * @return the infraMessage
	 */
	public String getInfraMessage() {
		return infraMessage;
	}

	/**
	 * @param infraMessage the infraMessage to set
	 */
	public void setInfraMessage(String infraMessage) {
		this.infraMessage = infraMessage;
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
	 * @return the marathonCred
	 */
	public String getMarathonCred() {
		return marathonCred;
	}

	/**
	 * @param marathonCred the marathonCred to set
	 */
	public void setMarathonCred(String marathonCred) {
		this.marathonCred = marathonCred;
	}

	/**
	 * @return the envLock
	 */
	public int getEnvLock() {
		return envLock;
	}

	/**
	 * @param envLock the envLock to set
	 */
	public void setEnvLock(int envLock) {
		this.envLock = envLock;
	}

	/**
	 * @return the marathonJson
	 */
	public Blob getMarathonJson() {
		return marathonJson;
	}

	/**
	 * @param marathonJson the marathonJson to set
	 */
	public void setMarathonJson(Blob marathonJson) {
		this.marathonJson = marathonJson;
	}

}
