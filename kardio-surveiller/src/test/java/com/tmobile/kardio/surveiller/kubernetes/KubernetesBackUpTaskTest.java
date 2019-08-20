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
package com.tmobile.kardio.surveiller.kubernetes;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.tmobile.kardio.surveiller.apidashboard.APIDashboardTask;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.CommonsUtil;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

import mockit.Mock;
import mockit.MockUp;


public class KubernetesBackUpTaskTest {

	@Test
	public void doKubernetesBackUp() throws Exception {
		
		new MockUp<DBQueryUtil>() {
			
			@Mock
			public List<EnvironmentVO>  getAllEnvironments() throws SerialException, SQLException{
				EnvironmentVO env = new EnvironmentVO();
				env.setK8sUrl("http://kube.com");
				env.setK8sCred("kubecred");
				List<EnvironmentVO> envList = new ArrayList<EnvironmentVO>();
		    	envList.add(env);
				return envList;
			}
			
	   };
	   
	   MockUp<CommonsUtil> cumock = new MockUp<CommonsUtil>() {
		   @Mock
		   public String getK8sAuthToken(String credentials) {
			   return "k8s_token";
		   }
	   };
	   
		File resourcesDirectory = new File("src/test/data");
		MockUp<RestCommunicationHandler> rch = new MockUp<RestCommunicationHandler>() {
			
			@Mock
			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
				String filename = null;
				if (urlStr.indexOf("deployments") > -1) {
					filename = "/k8sDeployments.txt";
				} else {
					filename = "/k8sIngress.txt";
				}
				
				String content = null;
				try {
					content = new String ( Files.readAllBytes( Paths.get(resourcesDirectory.getAbsolutePath() + filename) ) );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return content;
			}
		
		};
	   
	   KubernetesBackUpTask.doKubernetesBackUp();
       rch.tearDown();
       cumock.tearDown();
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<KubernetesBackUpTask> constructor = KubernetesBackUpTask.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
}
