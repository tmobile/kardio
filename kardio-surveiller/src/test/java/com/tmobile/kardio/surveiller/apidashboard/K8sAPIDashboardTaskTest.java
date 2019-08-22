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
package com.tmobile.kardio.surveiller.apidashboard;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.ProxyUtilTest;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author U29842
 *
 */
public class K8sAPIDashboardTaskTest extends ProxyUtilTest{

	/**
	 * @throws Exception
	 */
	@Test
	public void testDoK8sApiDashboardDataLoad() throws Exception{
		
		new MockUp<DBQueryUtil>() {
			
			@Mock
			public List<EnvironmentVO>  getAllEnvironments() throws SerialException, SQLException{
				EnvironmentVO env = new EnvironmentVO();
		    	env.setEnvironmentName("Development");
		    	List<EnvironmentVO> envList = new ArrayList<EnvironmentVO>();
		    	envList.add(env);
				return envList;
			}
			
	   };
	   K8sAPIDashboardTask.doK8sApiDashboardDataLoad();
	}	
	
	/**
	 * 
	 */
	@Test
	public  void testLoadKubernetesApiDashboard(){
		
		EnvironmentVO env = new EnvironmentVO();
    	env.setEnvironmentName("Development");
   		
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<K8sAPIDashboardTask> constructor = K8sAPIDashboardTask.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
}
