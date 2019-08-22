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
package com.tmobile.kardio.surveiller.main;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tmobile.kardio.surveiller.apidashboard.APIDashboardTask;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.MailSenderUtil;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;

import mockit.Mock;
import mockit.MockUp;


public class HealthCheckTest {
	

	
	@Test
	public void testHealthCheck() throws Exception {

		MockUp<DBQueryUtil> dbMock = new MockUp<DBQueryUtil>() {
			@Mock
			public List<HealthCheckVO> getSurveillerDetailsOfComponent(){
				List<HealthCheckVO> result = new ArrayList<HealthCheckVO>();
				HealthCheckVO hc = new HealthCheckVO();
				hc.setHealthCheckTypeClassName("com.tmobile.kardio.surveiller.handler.URLOpenableHandler");
				result.add(hc);
				return result;
			}
			
			@Mock
			public List<HealthCheckVO> updateHealthCheckStatusInDB(List <HealthCheckVO> hcvs){
				List<HealthCheckVO> result = new ArrayList<HealthCheckVO>();
				HealthCheckVO hc = new HealthCheckVO();
				hc.setHealthCheckTypeClassName("com.tmobile.kardio.surveiller.handler.URLOpenableHandler");
				result.add(hc);
				return result;
			}
		};
		
		MockUp<MailSenderUtil> mailMock = new MockUp<MailSenderUtil>() {
			@Mock
			public void sendMailForHealthCheckVos(List<HealthCheckVO> hcvs) {
				
			}
		};
		
		HealthCheck.doSurveiller();
		
		dbMock.tearDown();
		mailMock.tearDown();
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<HealthCheck> constructor = HealthCheck.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
}
