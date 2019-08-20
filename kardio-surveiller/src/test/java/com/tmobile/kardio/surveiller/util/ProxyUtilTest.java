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
package com.tmobile.kardio.surveiller.util;

import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
//import org.mockito.Mock;

import mockit.Mock;
import mockit.MockUp;


public class ProxyUtilTest {
	
	
	//@Mock
	private PropertyUtil instance;

	private Properties prop = new Properties();
	
	//private static ProxyUtilTest instance = null;
	
	  @BeforeClass
      public static void onceExecutedBeforeAll() throws IOException {
          
          new MockUp<PropertyUtil>() {
  			
  			@Mock
  			public PropertyUtil  getInstance() {
  				PropertyUtil instance = new PropertyUtil();
  				try{
  		        	instance = new PropertyUtil();
  		        	InputStream in = new FileInputStream("./config/config_test.properties");
  		        	instance.prop.load(in);
  		        }catch(Exception ex){
  		        	System.out.println(ex);
  		        }
  				return instance;
  				
  			}
          }; 
      }
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<ProxyUtil> constructor = ProxyUtil.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
	


	  @Test
	  public void testMethod(){
		  PropertyUtil prop_test = PropertyUtil.getInstance();
	  }
	  
}

