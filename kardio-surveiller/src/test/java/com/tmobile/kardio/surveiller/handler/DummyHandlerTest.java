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
package com.tmobile.kardio.surveiller.handler;

import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;
import com.tmobile.kardio.test.TestDaoService;
import com.tmobile.kardio.test.TestDataProvider;

public class DummyHandlerTest extends DummyHandler {
	TestDaoService daoService = new TestDaoService();
	ComponentTypeEntity cte = daoService.createComponentType();
	int region_id=0;

	@Test
	public void testgetSurveillerStatus() throws SerialException, SQLException {
		
		HealthCheckEntity hcv = daoService.createHealthCheckEntity(0, cte, region_id, TestDataProvider.getMesosPlatform());
		HealthCheckVO hcvo = new HealthCheckVO();
		hcvo.setCurrentStatus(1L);
		init(hcvo);
		StatusVO  result = getSurveillerStatus();
		
}
}
