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
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;
import com.tmobile.kardio.test.TestDaoService;
import com.tmobile.kardio.test.TestDataProvider;

public class DBHandlerTest extends SurveillerHandlerTest {
	TestDaoService daoService = new TestDaoService();
	DBHandler  dbHandler = new DBHandler();
	ComponentTypeEntity cte = daoService.createComponentType();
	int region_id=0;
	String url = "http://localhost:8080";

		
@Test
public void testgetSurveillerStatus() throws SerialException, SQLException {
	
	HealthCheckEntity hcv = daoService.createHealthCheckEntity(0, cte, region_id, TestDataProvider.getMesosPlatform());
	HealthCheckVO hcvo = new HealthCheckVO();
	
	Map<String,String> paramList = new HashMap<String,String>();
	paramList.put("DB-JDBC-URL","jdbc:h2:mem:ypsilon;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
	paramList.put("DB-USER","sa");
	paramList.put("DB-PASSWORD","sa");
	paramList.put("DB-JDBC-DRIVER-CLASS","org.h2.Driver");
	paramList.put("DB-QUERY","test");
	paramList.put("DB-EXPECTED-VALUE","test");
	hcvo.setParamDetails(paramList);
	dbHandler.setHealthCheckVO(hcvo);
	dbHandler.init(hcvo);
    StatusVO result = dbHandler.getSurveillerStatus();
    dbHandler.run();
    HealthCheckVO resultVO= dbHandler.getHealthCheckVO();

}
@Test
public void testgetSurveillerStatus_NullParams() throws SerialException, SQLException {
	
	HealthCheckEntity hcv = daoService.createHealthCheckEntity(0, cte, region_id, TestDataProvider.getMesosPlatform());
	HealthCheckVO hcvo = new HealthCheckVO();
	
	Map<String,String> paramList = new HashMap<String,String>();
	paramList.put("DB-JDBC-URL",null);
	paramList.put("DB-USER",null);
	paramList.put("DB-PASSWORD","sa");
	paramList.put("DB-JDBC-DRIVER-CLASS","org.h2.Driver");
	paramList.put("DB-QUERY","test");
	paramList.put("DB-EXPECTED-VALUE","test");
	hcvo.setParamDetails(paramList);
	dbHandler.setHealthCheckVO(hcvo);
	dbHandler.init(hcvo);
    StatusVO result = dbHandler.getSurveillerStatus();
    dbHandler.run();
    HealthCheckVO resultVO= dbHandler.getHealthCheckVO();

}

@Test
public void testgetSurveillerStatus_InvalidParams() throws SerialException, SQLException {
	
	HealthCheckEntity hcv = daoService.createHealthCheckEntity(0, cte, region_id, TestDataProvider.getMesosPlatform());
	HealthCheckVO hcvo = new HealthCheckVO();
	
	Map<String,String> paramList = new HashMap<String,String>();
	paramList.put("DB-JDBC-URL","test");
	paramList.put("DB-USER","test");
	paramList.put("DB-PASSWORD","sa");
	paramList.put("DB-JDBC-DRIVER-CLASS","org.h2.Driver");
	paramList.put("DB-QUERY","test");
	paramList.put("DB-EXPECTED-VALUE","test");
	hcvo.setParamDetails(paramList);
	dbHandler.setHealthCheckVO(hcvo);
	dbHandler.init(hcvo);
    StatusVO result = dbHandler.getSurveillerStatus();
    dbHandler.run();
    HealthCheckVO resultVO= dbHandler.getHealthCheckVO();

}
}
