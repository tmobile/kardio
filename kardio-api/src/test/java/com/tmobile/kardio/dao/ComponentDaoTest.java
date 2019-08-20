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
import java.util.Map;

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
import com.tmobile.kardio.TestDataProvider;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.dao.ComponentDao;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentStatusApplication.class})
public class ComponentDaoTest {
    @Autowired
    private ComponentDao compDao;
    
    @Autowired
    private TestDaoService daoService;

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateComponent() {
    	daoService.createComponentType();
    	daoService.createComponent();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateComponent_WithParent() {
    	daoService.createComponentType();
    	daoService.createComponent();
    	
    	Component component = daoService.getComponent();
    	component.setParentComponentId(TestDaoService.compID);
    	daoService.createComponent(component);
    }
    
    @Test(expected=InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testCreateComponent_EmptyComponentName() {
    	Component component = daoService.getComponent();
    	component.setComponentName("");
    	compDao.saveComponent(component);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetComponents() {
    	ComponentType componentType = daoService.createComponentType("INFRA");
    	Component component = daoService.getComponent();
    	component.setComponentType(componentType.name());
    	daoService.createComponent(component);
    	
    	List<ComponentEntity> result = compDao.getComponents();
    	//Assert.assertEquals("Size does not match", TestDaoService.compID-1, result.size()); //Reduce the one INFRA component
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateComponent() {
    	daoService.createComponentType();
    	Component component = daoService.createComponent();
    	component.setComponentName("new_comp_name");
    	compDao.editComponent(component);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdateComponent_WithParent() {
    	daoService.createComponentType();
    	daoService.createComponent();
    	
    	Component component = daoService.getComponent();
    	component.setParentComponentId(TestDaoService.compID);
    	daoService.createComponent(component);
    	
    	component.setComponentName("new_comp_name_1");
    	compDao.editComponent(component);
    }
    
    @Test(expected=InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testUpdateComponent_EmptyComponentName() {
    	Component component = daoService.getComponent();
    	component.setComponentName("");
    	compDao.editComponent(component);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testGetAppFullName() {
    	daoService.createComponentType();
    	daoService.createComponent();
    	daoService.createAppFullName();
    	Map<Integer, String> result = compDao.getAppFullName();
    	
    	Assert.assertEquals("Size does not match", TestDaoService.appID, result.size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAppParentComponents() {
    	daoService.createComponentType();
    	daoService.createComponent();
    	
    	Component component = daoService.getComponent();
    	component.setParentComponentId(TestDaoService.compID);
    	daoService.createComponent(component);
    	
    	List<ComponentEntity> result = compDao.getAppParentComponents();
    	Assert.assertEquals("Size does not match", TestDaoService.compID-TestDaoService.parentCompID, result.size()); //TODO: check later. This is suppose to return comps with parent only.
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteComponent() {
    	daoService.createComponentType();
    	Component component = daoService.createComponent();
    	compDao.deleteComponent(component);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testGetMarathonComponentForEnv() {
    	daoService.createEnvironment("compdao", 0);
    	daoService.createComponentType();
    	daoService.createComponent();
    	
    	EnvironmentEntity env = new EnvironmentEntity();
    	env.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
    	List<HealthCheckEntity> result = compDao.getPlatformComponentForEnv(env.getEnvironmentId(),TestDataProvider.getPlatform());
    	Assert.assertEquals("Size does not match", 0, result.size()); //Reduce the one INFRA component
    }
    
}
