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
package com.tmobile.kardio.surveiller.marathon;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.junit.Test;

import com.tmobile.kardio.surveiller.apidashboard.APIDashboardTask;
import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.HealthCheckType;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.ComponentVO;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.test.EnvironmentTestData;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author U29842
 *
 */
public class MarathonConfigProcessorTest {
	
	EnvironmentTestData envTestData = new EnvironmentTestData();
	
	@Test
	 public void updateApiList() throws JsonProcessingException, SQLException, IOException{
		
		
		new MockUp<DBQueryUtil>() {
			
			@Mock
			public int checkAndInsertComponent(final String appName, final String apiName, final ComponentType compType,
			        final String healthCheckURL, final int environmentId, final long regionID,
			        final HealthCheckType healthCheckType, String platform){
				return 40;
			}
			
			@Mock
			public boolean checkAndUpdateMessage(final int componentId, final int environmentId,
					final long regionID,final HealthCheckType healthCheckType, final Status currentStatus,
					final String marathonMessage){
				return true;
			}
			
			@Mock
			public ComponentVO getComponent(final int componentId){
				ComponentVO compVO = new ComponentVO();
				//compVO.setComponentId(50);
				compVO.setComponentName("testComp1");
				compVO.setComponentDesc("testDesc1");
				compVO.setParentComponentId(8);
				compVO.setParentComponentName("Test1");
				compVO.setComponentTypeId(2);
				compVO.setDelInd(0);
				
				return compVO;
			}
		};	
			
		
		List<EnvironmentVO> envList = envTestData.getEnvironmentList();
    	MarathonConfigProcessor.updateApiList(envList);
		
	}
	
	@Test
	 public void compareAPILists() throws SQLException{
		
		ComponentVO cVo = new ComponentVO();
		cVo.setComponentId(1);
		List<ComponentVO> oldCompList = new ArrayList<ComponentVO>();
		List<ComponentVO> newCompList = new ArrayList<ComponentVO>();
		oldCompList.add(cVo);
		newCompList.add(cVo);
		MarathonConfigProcessor.compareAPILists(oldCompList, newCompList, 2, 1);
		
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<MarathonConfigProcessor> constructor = MarathonConfigProcessor.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}

}
