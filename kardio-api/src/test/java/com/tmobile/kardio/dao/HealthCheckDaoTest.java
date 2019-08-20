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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.HealthCheckTypeVO;
import com.tmobile.kardio.bean.HealthCheckVO;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.dao.HealthCheckDao;
import com.tmobile.kardio.exceptions.ValidationFailedException;



@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentStatusApplication.class})
public class HealthCheckDaoTest {

    @Autowired
    private HealthCheckDao healthCheckDao;
    
    @Autowired
    private TestDaoService daoService;
    
    @Test
    @Transactional
    @Rollback(true)
    public void testCreateHealthCheck() {
    	String envName = "createhealthcheck";
    	HealthCheckVO hcv = daoService.getHealthCheck(envName);
    	hcv.setHealthCheckId(0);
    	healthCheckDao.editHealthCheck(hcv);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateHealthCheck() {
    	String envName = "updatehealthcheck";
    	HealthCheckVO hcv = daoService.getHealthCheck(envName);
    	Environment env = daoService.createEnvironment(envName, 0);
    	hcv.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
    	healthCheckDao.editHealthCheck(hcv);
    	TestDaoService.healthCheckID++;
    }

    @Test(expected=ValidationFailedException.class)
    @Transactional
    @Rollback(true)
    public void testUpdateHealthCheck_EnvIDZero() {
    	HealthCheckVO hc = new HealthCheckVO();
    	hc.setEnvironmentId(0);
    	healthCheckDao.editHealthCheck(hc);
    }
    
    @Test(expected=ValidationFailedException.class)
    @Transactional
    @Rollback(true)
    public void testUpdateHealthCheck_RegionIDZero() {
    	HealthCheckVO hc = new HealthCheckVO();
    	hc.setEnvironmentId(1);
    	hc.setRegionId(0);
    	healthCheckDao.editHealthCheck(hc);
    }
    
    @Test(expected=ValidationFailedException.class)
    @Transactional
    @Rollback(true)
    public void testUpdateHealthCheck_ComponentIDZero() {
    	HealthCheckVO hc = new HealthCheckVO();
    	hc.setEnvironmentId(1);
    	hc.setRegionId(1);
    	hc.setComponentId(0);
    	healthCheckDao.editHealthCheck(hc);
    }
//    TODO: Revisit later. not able to make complete object.
//    @Test
//    @Transactional
//    @Rollback(true)
//    public void testUpdateHealthCheckParam() {
//    	String envName = "updatehealthcheckparam";
//    	HealthCheckVO hcv = daoService.createHealthCheck(envName);
//    	hcv.setHealthCheckId(TestDaoService.healthCheckID);
//    	healthCheckDao.editHealthCheck(hcv);
//    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteHealthCheck() {
    	String envName = "deletehealthcheck";
    	daoService.createHealthCheck(envName);
    	healthCheckDao.deleteHealthCheck(TestDaoService.healthCheckID);
    }
    
    @Test(expected=InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testDeleteHealthCheck_IDZero() {
    	healthCheckDao.deleteHealthCheck(0);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllHealthCheckDetails() {
    	String envName = "getallhealthcheck";
    	daoService.createHealthCheck(envName, ComponentType.INFRA.name());
    	List<HealthCheckVO> result = healthCheckDao.getAllHealthCheckDetails();
    	
    	//Assert.assertEquals("Size does not match", TestDaoService.healthCheckID, result.size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllHealthCheckTypes() {
    	String envName = "gethealthchecktypes";
//    	daoService.createEnvironment(envName, 0);
    	daoService.createHealthCheck(envName);
    	
    	List<HealthCheckTypeVO> result = healthCheckDao.getAllHealthCheckTypes();
    	
    	Assert.assertEquals("Size does not match", TestDaoService.healthCheckTypeID, result.size());
    }
}
