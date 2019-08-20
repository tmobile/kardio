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
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.tmobile.kardio.surveiller.apidashboard.APIDashboardTask;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author U29842
 *
 */
public class MarathonBackUpTaskTest {
	
	@Test
	public void doMarathonBackUp() throws Exception{
		
		new MockUp<DBQueryUtil>() {
			
			@Mock
			public List<EnvironmentVO>  getAllEnvironments() throws SerialException, SQLException{
				EnvironmentVO env = new EnvironmentVO();
		    	File resourcesDirectory = new File("src/test/data");
		    	String content = null;
				List<EnvironmentVO> envList = new ArrayList<EnvironmentVO>();
		    	envList.add(env);
				return envList;
			}
			
	   };
	   
	   MarathonBackUpTask.doMarathonBackUp();
		
	}
	
	
	/**
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
//	@Test
//	public void testGetMarathonJson() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//		File resourcesDirectory = new File("src/test/data");
//		MockUp<RestCommunicationHandler> rch = new MockUp<RestCommunicationHandler>() {
//			
//			@Mock
//			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
//				
//				
//				String content = null;
//				try {
//					content = new String ( Files.readAllBytes( Paths.get(resourcesDirectory.getAbsolutePath()+"/marathonJson.txt") ) );
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return content;
//			}
//		
//		};
//		
//		EnvironmentVO env = new EnvironmentVO();
//    	env.setEnvironmentName("Development");
//    	env.setMarathonUrl("http://marathon.com/v2/app");
//    	Object[] obj = {env};
//    	Method method = MarathonBackUpTask.class.getDeclaredMethod("getMarathonJson",EnvironmentVO.class);
//        method.setAccessible(true);
//        String output = (String) method.invoke(marBkpTask, obj);
//        rch.tearDown();
//		
//	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<MarathonBackUpTask> constructor = MarathonBackUpTask.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
}
