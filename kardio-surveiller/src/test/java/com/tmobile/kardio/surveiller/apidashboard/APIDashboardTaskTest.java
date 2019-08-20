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
package com.tmobile.kardio.surveiller.apidashboard;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.PropertyUtil;
import com.tmobile.kardio.surveiller.util.ProxyUtilTest;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.test.TestDaoService;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author U29842
 *
 */

public class APIDashboardTaskTest extends ProxyUtilTest{

	File resourcesDirectory = new File("src/test/data");
	/**
	 * UT method for doApiDashboardDataLoad() method
	 * @throws Exception
	 */
	@Test
	public void testDoApiDashboardDataLoad() throws Exception{
		//APIDashboardTask apiTask = new APIDashboardTask();
		TestDaoService daoService = new TestDaoService();
		new MockUp<DBQueryUtil>() {
			
			@Mock
			public List<EnvironmentVO>  getAllEnvironments() throws SerialException, SQLException{
				EnvironmentVO env = new EnvironmentVO();
		    	env.setEnvironmentName("Development");
		    	File resourcesDirectory = new File("src/test/data");
		    	String content = null;
				try {
					content = new String ( Files.readAllBytes( Paths.get(resourcesDirectory.getAbsolutePath()+"/marathonJson.txt") ) );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				env.setMarathonJson(content);
		    	List<EnvironmentVO> envList = new ArrayList<EnvironmentVO>();
		    	envList.add(env);
				return envList;
			}
			@Mock
			public Map<String, String> getPromLookupDetails(int envId, String platform){
				Map<String, String> promMap = new HashMap<String, String>();
				promMap.put("ssp/prod/dc2/ssp-prod-dc2-promotion-v1", "http://test.com");
				return promMap;
			}
			@Mock
			public Set<Integer> getAppsLauchDateNull(int envId) {
				Set<Integer> compSet = new HashSet<Integer>();
				compSet.add(1);
				return compSet;
				
			}
		};
		
		APIDashboardTask.doApiDashboardDataLoad();
	}
	
		
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<APIDashboardTask> constructor = APIDashboardTask.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
	
	 @Test
	  public void testMethod(){
		  PropertyUtil prop_test = PropertyUtil.getInstance();
	  }
	
}
