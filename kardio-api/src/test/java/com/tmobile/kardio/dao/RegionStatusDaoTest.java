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
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.HealthCheckVO;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.db.entity.DaillyCompStatusEntity;
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class RegionStatusDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private TestDaoService daoService;
	@Autowired
	private EnvironmentDao envDao;
	@Autowired
	private RegionStatusDao rgDao;


    @Test
    @Transactional
	public void testGetAllParentComponents() throws ParseException {
		daoService.createComponentType();
    	daoService.createComponent();    	
    	Component component = daoService.getComponent();
    	component.setParentComponentId(TestDaoService.compID);
    	daoService.createComponent(component);
    	List<Component> components=rgDao.getAllParentComponents();
    	Assert.assertEquals("Size does not match", TestDaoService.compID - TestDaoService.parentCompID, components.size());    	
	}
    
    @Test
    public void testLoadMessages() throws ParseException, InstantiationException{
    	String environmentName = "envloadmessages";
		String messageType = Constants.MESSAGE_TYPE_APP;
		String message = "message_1";
		DaillyCompStatusEntity dcse=daoService.createDailyStatusEntity(environmentName);
    	rgDao.loadMessages(environmentName, messageType, message);   	
    	
    	int actual = envDao.getEnironmentIdFromName(environmentName);    	
    	Assert.assertEquals("Size does not match", TestDaoService.getLastCreatedEnvironmentID(), actual); 
    }
    
    @Test
    public void testLoadMessages_Infra() throws ParseException, InstantiationException{
    	String environmentName = "envloadmessagesinfra";
		String messageType = Constants.MESSAGE_TYPE_INFRA;
		String message = "message_1";
		DaillyCompStatusEntity dcse=daoService.createDailyStatusEntity(environmentName);
    	rgDao.loadMessages(environmentName, messageType, message);   	
    	
    	int actual = envDao.getEnironmentIdFromName(environmentName);    	
    	Assert.assertEquals("Size does not match", TestDaoService.getLastCreatedEnvironmentID(), actual); 
    }

    @Test
    public void testLoadMessages_General() throws ParseException, InstantiationException{
    	String environmentName = "envloadmessagesgeneral";
		String messageType = Constants.MESSAGE_TYPE_GENERAL;
		String message = "message_1";
		DaillyCompStatusEntity dcse=daoService.createDailyStatusEntity(environmentName);
    	rgDao.loadMessages(environmentName, messageType, message);   	
    	
    	int actual = envDao.getEnironmentIdFromName(environmentName);    	
    	Assert.assertEquals("Size does not match", TestDaoService.getLastCreatedEnvironmentID(), actual); 
    }

    @Test(expected=InvalidDataAccessApiUsageException.class)
    public void testLoadMessages_Invalid() throws ParseException, InstantiationException{
    	String environmentName = "envloadmsginvalid";
		String messageType = "INVALID";
		String message = "message_1";
		DaillyCompStatusEntity dcse=daoService.createDailyStatusEntity(environmentName);
    	rgDao.loadMessages(environmentName, messageType, message);    	
    }

    @Test
	public void testGetParentPlatform() throws ParseException {
		Session session = sessionFactory.openSession();
		String envName = "envgetparentplatform";

		daoService.createHealthCheckTypeEntity(session);
		daoService.createHealthCheck(envName);

		HealthCheckVO hcv = daoService.createHealthCheck(envName);
		int parentComponentId = TestDaoService.parentCompID;
		int envId = TestDaoService.getLastCreatedEnvironmentID();
		String result = rgDao.getParentPlatform(parentComponentId, envId);
// TODO: Verify the result properly
//		Assert.assertEquals("Size does not match", 1, result);
	}
    
    @Test
    public void testGetCompRegStatus() throws Exception {
		Session session = sessionFactory.openSession();
		String envName = "envgetcompregstatus";

    	daoService.createHealthCheckTypeEntity(session);
		daoService.createHealthCheck(envName);
		
		rgDao.getCompRegStatus(envName);
    	
    }

}
