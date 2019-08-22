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
/**
 * 
 */
package com.tmobile.kardio.surveiller.counters;

import org.junit.Test;

import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;
import com.tmobile.kardio.test.TestDaoService;

/**
 * UT for NumberOfServiceHandler class
 *
 */
public class NumberOfServiceHandlerTest {
	
TestDaoService daoService = new TestDaoService();
	
	/**
	 * UT for gGetCounterMerticValue() method in NumberOfServiceHandler
	 */
	@Test
	public void testGetCounterMerticValue() throws Exception{
		ComponentTypeEntity cte = daoService.createComponentType();
		HealthCheckEntity hce = daoService.createHealthCheckEntity(0,cte,0,"Mesos");
		CounterDetailVO counterDetails = new CounterDetailVO();
		counterDetails.setEnvironmentCounterId(6);
		counterDetails.setEnvironmentId(hce.getEnvironment().getEnvironmentId());
		NumberOfServiceHandler nunService = new NumberOfServiceHandler();
		float num = nunService.getCounterMerticValue(counterDetails, null);
		
	}
}
