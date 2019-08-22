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

import java.util.HashMap;
import java.util.Map;

import com.tmobile.kardio.surveiller.util.ProxyUtilTest;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;

public class SurveillerHandlerTest extends ProxyUtilTest{
	SurveillerHandler  dbHandler = new DBHandler();
	Map<String,String> paramList = new HashMap<String,String>();
	private HealthCheckVO hcvo;
	/*
	@Test
	public void testInit() {
	
		paramList.put("PARAM_KEY_JDBC_URL","jdbc:h2:mem:ypsilon");
		paramList.put("PARAM_KEY_USER","sa");
		paramList.put("PARAM_KEY_PWD","sa");
		paramList.put("PARAM_KEY_JDBC_DRIVER_CLASS","org.h2.Driver");
		paramList.put("PARAM_KEY_QUERY","test");
		paramList.put("PARAM_KEY_EXPVALUE","test");
		hcvo.setParamDetails(paramList);
		dbHandler.init(hcvo);
	}
	@Test
	public void testRun() {
		
		paramList.put("PARAM_KEY_JDBC_URL","jdbc:h2:mem:ypsilon");
		paramList.put("PARAM_KEY_USER","sa");
		paramList.put("PARAM_KEY_PWD","sa");
		paramList.put("PARAM_KEY_JDBC_DRIVER_CLASS","org.h2.Driver");
		paramList.put("PARAM_KEY_QUERY","test");
		paramList.put("PARAM_KEY_EXPVALUE","test");
		hcvo.setParamDetails(paramList);
		dbHandler.init(hcvo);
		dbHandler.run();
	}*/
}
