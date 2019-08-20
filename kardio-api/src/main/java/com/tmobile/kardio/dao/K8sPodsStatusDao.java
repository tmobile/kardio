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

/**
 *  Dao Class to get the K8s Pod status.
 *
 */
public interface K8sPodsStatusDao {

	public List<ApiStatus> getPodsStatus(String startDate, String endDate, int envId, String componentIdsStrg, boolean isParentComponents) throws ParseException;
	public long getCurrentNumberOfPods(int envId,String componentIdsStrg, boolean isParentComponents) throws ParseException;
	public long getRemK8sObjectPods(int envId, String objPodsCont);
}
