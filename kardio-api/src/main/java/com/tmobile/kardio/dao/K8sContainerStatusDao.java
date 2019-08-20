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
package com.tmobile.kardio.dao;

import java.text.ParseException;
import java.util.List;

import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.bean.K8sContainerStatus;
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;

public interface K8sContainerStatusDao {
	/**
	 * Get APP containers, based on the input parameters date, environment & component ids
	 * 
	 * @param startDate
	 * @param endDate
	 * @param environment
	 * @param component
	 * @return
	 * @throws ParseException
	 */
	public List<K8sPodsContainersEntity> getEnvContainers(String startDate, String endDate, int envId, String component, boolean isParentComponents)throws ParseException;

	public List<K8sContainerStatus> getAllContainersOfParent(String startDate, String endDate, int envId, String componentIdsStrg) throws ParseException;

	public long getCurrentNumberOfContainsers(int envId,String componentIdsStrg, boolean isParentComponents) throws ParseException;

	public List<ApiStatus> getRemK8sObjPodsCont(String startDate, String endDate, int envId) throws ParseException;

}
