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
import com.tmobile.kardio.EnvironmentType;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.dao.EnvironmentDao;
import com.tmobile.kardio.db.entity.EnvironmentEntity;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentStatusApplication.class})
public class EnvironmentDaoTest {
    @Autowired
    private EnvironmentDao envDao;

    @Autowired
    private TestDaoService daoService;
    
    @Test
    @Transactional
    @Rollback(true)
    public void testCreateEnvironment() {
    	daoService.createEnvironment("abc", 1);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateK8SEnvironment() {
    	daoService.createEnvironment("abc1", 1, EnvironmentType.K8S);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvironmentFromName() {
    	String name = "namedenv";
    	Environment env = daoService.createEnvironment(name, 1);
    	EnvironmentEntity actual = envDao.getEnvironmentFromName(name);
    	
    	Assert.assertEquals("ID does not match", TestDaoService.getLastCreatedEnvironmentID(), actual.getEnvironmentId());
    	Assert.assertEquals("Name does not match", env.getEnvironmentName(), actual.getEnvironmentName());
    	Assert.assertEquals("Describtion does not match", env.getEnvironmentDesc(), actual.getEnvironmentDesc());
    	Assert.assertEquals("Display order does not match", env.getDisplayOrder(), actual.getDisplayOrder());
    	Assert.assertEquals("Marathon URL does not match", env.getMarathonUrl(), actual.getMarathonUrl());
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvironmentIDFromName() {
    	String name = "envgetbyid";
    	daoService.createEnvironment(name, 1);
    	int actual = envDao.getEnironmentIdFromName(name);
    	
    	Assert.assertEquals("ID does not match", TestDaoService.getLastCreatedEnvironmentID(), actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvironments() {
    	String name = "envget";
    	daoService.createEnvironment(name, 0);
    	
    	List<EnvironmentEntity> result = envDao.getEnvironments();
    	Assert.assertEquals("Size does not match", TestDaoService.getLastCreatedEnvironmentID(), result.size()+1);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvironmentsWithLocks() {
    	String name = "envwithlocks";
    	daoService.createEnvironment(name, 1);
    	
    	List<EnvironmentEntity> result = envDao.getEnvironmentWithLock();
    	Assert.assertEquals("Size does not match", TestDaoService.getLastCreatedEnvironmentID(), result.size());
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateEnvironment() {
    	String name = "updateenv";
    	Environment env = daoService.createEnvironment(name, 0);
    	env.setEnvironmentDesc("new_desc");
    	env.setEnvLock(1);
    	env.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
    	env.setMarathonUrl("http://localhost");
    	env.setMarathonUserName("new_user");
    	env.setMarathonPassword("new_pass");
    	envDao.updateEnvironment(env);
	}

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateK8SEnvironment() {
    	String name = "updatek8senv";
    	Environment env = daoService.createEnvironment(name, 0, EnvironmentType.K8S);
    	env.setEnvironmentDesc("new_desc");
    	env.setEnvLock(1);
    	env.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
		env.setK8sUrl("http://localhost");
		env.setK8sUserName("k8s_user_1");
		env.setK8sPassword("k8s_pass_1");
		envDao.updateEnvironment(env);
	}
    
    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateEnvironment_Basic() {
    	String name = "updateenvbasic";
    	Environment env = daoService.createEnvironment(name, 0);
    	env.setEnvironmentDesc("new_desc");
    	env.setEnvLock(1);
    	env.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
    	env.setMarathonUrl("http://localhost");
    	env.setMarathonUserName("");
    	envDao.updateEnvironment(env);
	}
    
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateEnvironment_IDZero() {
    	Environment env = new Environment();
    	env.setEnvironmentId(0);
    	envDao.updateEnvironment(env);
    }
}
