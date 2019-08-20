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
/**
 * 
 */
package com.tmobile.kardio.surveiller.counters;

import org.junit.Test;

import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.DaillyCompStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;
import com.tmobile.kardio.test.TestDaoService;

/**
 * UT for PercentageUpTimeHandler class
 *
 */
public class PercentageUpTimeHandlerTest {

	TestDaoService daoService = new TestDaoService();
	
	/**
	 * UT for gGetCounterMerticValue() method in DirectPromethuesQueryHandler
	 */
	@Test
	public void testGetCounterMerticValue() throws Exception{
		PercentageUpTimeHandler perHandler = new PercentageUpTimeHandler();
		ComponentTypeEntity cte = daoService.createComponentType();
		HealthCheckEntity hce = daoService.createHealthCheckEntity(0,cte,0,"Mesos");
        DaillyCompStatusEntity de = daoService.createDailyCompStatus(hce);
        String[] name= new String[10];
        name[0]=hce.getComponent().getParentComponent().getComponentName();
        CounterDetailVO counterDetails = new CounterDetailVO();
		counterDetails.setEnvironmentCounterId(6);
		counterDetails.setEnvironmentId(1);
		counterDetails.setCounterId(1);
		counterDetails.setCounterMetricTypeId(1);
		counterDetails.setParameter1("1,2");
		counterDetails.setParameter2("Prometheous query");
        float uptime = perHandler.getCounterMerticValue(counterDetails, null);
        
	}
	
	
}
