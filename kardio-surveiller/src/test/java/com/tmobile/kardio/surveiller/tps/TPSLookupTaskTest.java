/**
 * Copyright 2019 T-Mobile USA, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * See the LICENSE file for additional language around disclaimer of warranties.
 * Trademark Disclaimer: Neither the name of "T-Mobile, USA" nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 */
/**
 *
 */
package com.tmobile.kardio.surveiller.tps;

import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;

import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Arun Nair
 *
 */
public class TPSLookupTaskTest {

    @Test
    public void doTpsLookupLoad() throws Exception {
        new MockUp<DBQueryUtil>() {

            @Mock
            public List<EnvironmentVO> getAllEnvironments() throws SerialException, SQLException {
                EnvironmentVO env = new EnvironmentVO();
                env.setEnvironmentName("Development");
                File resourcesDirectory = new File("src/test/data");
                String content = null;
                List<EnvironmentVO> envList = new ArrayList<EnvironmentVO>();
                envList.add(env);
                return envList;
            }

        };
        TPSLookupTask.doTpsLookupLoad();

    }

    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<TPSLookupTask> constructor = TPSLookupTask.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
//	@Test
//	public void testLoadTpsLookup() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
//		
//		new MockUp<DBQueryUtil>() {
//			
//			@Mock
//			public  Map<String, String> getPromLookupDetails(int envId, String platform){
//				Map<String, String> lookUpMap = new HashMap<String, String>();
//				lookUpMap.put("TestComponent", "http://testComponent.com");
//				return lookUpMap;
//				
//			}
//			
//	   };
//	   
//	   new MockUp<CommonsUtil>() {
//			
//			@Mock
//			public String getK8sAuthToken(String credentials){
//				return "testToken";
//			}
//			
//	   };
//	   
//	   File resourcesDirectory = new File("src/test/data");
//		MockUp<RestCommunicationHandler> rch = new MockUp<RestCommunicationHandler>() {
//			
//			@Mock
//			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
//				
//				
//				String content = null;
//				try {
//					content = new String ( Files.readAllBytes( Paths.get(resourcesDirectory.getAbsolutePath()+"/k8sIngress.txt") ) );
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return content;
//			}
//		
//		};
//		TPSLookupTask tpsTask = new TPSLookupTask();
//		EnvironmentVO env = new EnvironmentVO();
//    	env.setEnvironmentName("Development");
//    	env.setK8sCred("test");
//    	Object[] obj = {env};
//    	Method method = TPSLookupTask.class.getDeclaredMethod("loadTpsLookup",EnvironmentVO.class);
//        method.setAccessible(true);
//        String output = (String) method.invoke(tpsTask, obj);
//        rch.tearDown();
//		
//		
//	}

}
