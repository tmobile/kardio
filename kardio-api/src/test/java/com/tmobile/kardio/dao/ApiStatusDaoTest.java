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
package com.tmobile.kardio.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.dao.ApiStatusDao;
import com.tmobile.kardio.db.entity.ApiStatusEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class ApiStatusDaoTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ApiStatusDao apiStatusDao;
	
	@Autowired
	private TestDaoService daoService;

	@Test
	public void testGetEnvApis() throws ParseException {
		String envName = "getenvapis";
		ApiStatusEntity ase = daoService.createApiStatusEntity(envName);
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		List <ApiStatus> result = apiStatusDao.getEnvApis(startDate, endDate, ase.getEnvironment().getEnvironmentId(), ""+ase.getComponent().getComponentId());

		Assert.assertEquals("Size does not match", 1, result.size());
		ApiStatus actual = result.get(0);
		Assert.assertEquals("EnvironmentID does not match", ase.getEnvironment().getEnvironmentId(), TestDaoService.getLastCreatedEnvironmentID());
		Assert.assertEquals("ComponentID does not match", ase.getComponent().getComponentId(), TestDaoService.compID);
//		Assert.assertEquals("Delta value does not match", ase.getDeltaValue(), actual.getDeltaValue()); TODO: Delta not set.
		Assert.assertEquals("Total API does not match", ase.getTotalApi(), actual.getTotalApis());
	}

	@Test
	public void testGetEnvApis_MulitpleComponent() throws ParseException {
		String envName = "getenvapismulticomp";
		ApiStatusEntity ase = daoService.createApiStatusEntity(envName);
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		List <ApiStatus> result = apiStatusDao.getEnvApis(startDate, endDate, 
				ase.getEnvironment().getEnvironmentId(), 
				ase.getComponent().getComponentId() + "," + ase.getComponent().getParentComponent().getComponentId());

		Assert.assertEquals("Size does not match", 1, result.size());
		ApiStatus actual = result.get(0);
		Assert.assertEquals("EnvironmentID does not match", ase.getEnvironment().getEnvironmentId(), TestDaoService.getLastCreatedEnvironmentID());
		Assert.assertEquals("ComponentID does not match", ase.getComponent().getComponentId(), TestDaoService.compID);
//		Assert.assertEquals("Delta value does not match", ase.getDeltaValue(), actual.getDeltaValue()); TODO: Delta not set.
		Assert.assertEquals("Total API does not match", ase.getTotalApi(), actual.getTotalApis());
	}
	
	@Test
	public void getCurrentNumberOfApis() throws ParseException  {
		String envName = "getcurnumapi";
		ApiStatusEntity ase = daoService.createApiStatusEntity(envName); 
		Environment env = new Environment();
        long currentApi = apiStatusDao.getCurrentNumberOfApis(env.getEnvironmentId(), ""+TestDaoService.compID);
		Assert.assertEquals("Count does not match", 1, currentApi);	
		Assert.assertEquals("EnvironmentID does not match", ase.getEnvironment().getEnvironmentId(), TestDaoService.getLastCreatedEnvironmentID());
		Assert.assertEquals("ComponentID does not match", ase.getComponent().getComponentId(), TestDaoService.compID);		
	}  

	 @Test
	public void getCurrentNumberOfApis_MultipleComponet() throws ParseException {
		String envName = "getcurrnumapimulticomp";
		ApiStatusEntity aseM = daoService.createApiStatusEntity(envName);
		Environment env = new Environment();
		env.setEnvironmentName(envName);
		long currentApiM = apiStatusDao.getCurrentNumberOfApis(env.getEnvironmentId(), ""
		+aseM.getComponent().getComponentId() + ","
		+ aseM.getComponent().getParentComponent().getComponentId());
		Assert.assertEquals("Count does not match", 1, currentApiM);
		Assert.assertEquals("EnvironmentID does not match", aseM.getEnvironment().getEnvironmentId(),
				TestDaoService.getLastCreatedEnvironmentID());
		Assert.assertEquals("ComponentID does not match", aseM.getComponent().getComponentId(), TestDaoService.compID);	 
	 }
}
