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
import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.dao.AppLookUpDao;
import com.tmobile.kardio.db.entity.AppLookUpEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentStatusApplication.class})
public class AppFullNameDaoTest {
    @Autowired
    private AppLookUpDao appFullNameDao;
    
    @Autowired
    private TestDaoService daoService;
    

    private AppFullName createAppFullName() {
		AppFullName afn = daoService.createAppFullName();
		return afn;
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testCreateAppFullName() {
    	AppFullName afn = createAppFullName();
        appFullNameDao.deleteAppFullName(afn);
    }

	@Test(expected=InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testCreateAppFullName_EmptyCompName() {
    	AppFullName afn = daoService.getAppFullName();
    	afn.setComponentFullName("");
		appFullNameDao.saveAppFullName(afn);
	}
	
    @Test
    @Transactional
    @Rollback(true)
    public void testGetAppFullNameWithAppId() {
    	
    	AppFullName afn = createAppFullName();
        List<AppLookUpEntity> result = appFullNameDao.getAppFullNameWithAppId();
        Assert.assertEquals("Size does not match", TestDaoService.appID, result.size());
        
//        AppLookUpEntity actual = result.get(0);
//        Assert.assertEquals("ID does not match", afn.getAppId(), actual.getApplookupId()-1);
//        Assert.assertEquals("Component ID does not match", afn.getComponentId(), actual.getComponent().getComponentId());
//        Assert.assertEquals("Component name does not match", afn.getComponentName(), actual.getComponent().getComponentName());
//        Assert.assertEquals("Component full name does not match", afn.getComponentFullName(), actual.getComponentFullName());

        appFullNameDao.deleteAppFullName(afn);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAppComponentId() {
    	
//    	AppFullName afn = createAppFullName();
//    	Component comp = createComponent();
        ComponentEntity actual = appFullNameDao.getAppComponentId(1);
//        Assert.assertEquals("ID does not match", 2, actual.getComponentId());
//        Assert.assertEquals("Name does not match", ce.getComponentName(), actual.getComponentName()); TODO
//        Assert.assertEquals("Type does not match", comp.getComponentType(), actual.getComponentType()); TODO
//        Assert.assertEquals("ID does not match", ce.getAppFullName(), result.getAppLookUpEntity().); TODO: Check later
//        appFullNameDao.deleteAppFullName(afn);
    }
    
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateAppFullName() {
		AppFullName afn = createAppFullName();
		afn.setComponentFullName("new_comp_name");
		afn.setComponentId(1);
		appFullNameDao.editAppFullName(afn);
		appFullNameDao.deleteAppFullName(afn);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testUpdateAppFullName_EmptyCompName() {
    	AppFullName afn = daoService.getAppFullName();
    	afn.setComponentFullName("");
		appFullNameDao.editAppFullName(afn);
	}
}
