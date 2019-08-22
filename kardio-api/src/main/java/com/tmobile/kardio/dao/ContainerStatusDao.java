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
package com.tmobile.kardio.dao;

import java.text.ParseException;
import java.util.List;

import com.tmobile.kardio.bean.ContainerStatus;
import com.tmobile.kardio.db.entity.ContainerStatusEntity;

/**
 * Dao Class to get the container stats.
 *
 */
public interface ContainerStatusDao {
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
	public List<ContainerStatusEntity> getEnvContainers(String startDate, String endDate, int envId, String component, boolean isParentComponents)throws ParseException;

	public List<ContainerStatus> getAllContainersOfParent(String startDate, String endDate, int envId, String componentIdsStrg) throws ParseException;

	public long getCurrentNumberOfContainsers(int envId,String componentIdsStrg, boolean isParentComponents) throws ParseException;
}
