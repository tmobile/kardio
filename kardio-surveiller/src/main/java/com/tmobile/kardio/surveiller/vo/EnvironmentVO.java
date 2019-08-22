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
 * Contains details regarding the environment
 * @author 
 *
 */
public class EnvironmentVO {

	private int environmentId;
	private String environmentName;
	private String marathonUrl;
	private String marathonJson;
	private String marathonCred;
	private String k8sUrl;
	private String k8sCred;
	private String k8sTpsQuery;
	private String k8sLatencyQuery;
	private String mesosTpsQuery;
	private String mesosLatencyQuery;
	private String eastMarathonJson;
	private String eastMarathonUrl;

	/**
	 * @return the eastMarathonJson
	 */
	public String getEastMarathonJson() {
		return eastMarathonJson;
	}

	/**
	 * @param eastMarathonJson the eastMarathonJson to set
	 */
	public void setEastMarathonJson(String eastMarathonJson) {
		this.eastMarathonJson = eastMarathonJson;
	}

	/**
	 * @return the eastMarathonUrl
	 */
	public String getEastMarathonUrl() {
		return eastMarathonUrl;
	}

	/**
	 * @param eastMarathonUrl the eastMarathonUrl to set
	 */
	public void setEastMarathonUrl(String eastMarathonUrl) {
		this.eastMarathonUrl = eastMarathonUrl;
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
	 * @return environmentId
	 */
	public int getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @param environmentId set the environmentId
	 */
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @return environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}

	/**
	 * @param environmentName set the environmentName
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	/**
	 * @return marathonUrl
	 */
	public String getMarathonUrl() {
		return marathonUrl;
	}

	/**
	 * @param marathonUrl set the marathonUrl
	 */
	public void setMarathonUrl(String marathonUrl) {
		this.marathonUrl = marathonUrl;
	}

	/**
	 * @return marathonJson
	 */
	public String getMarathonJson() {
		return marathonJson;
	}

	/**
	 * @param marathonJson set the marathonJson
	 */
	public void setMarathonJson(String marathonJson) {
		this.marathonJson = marathonJson;
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

}
