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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.ContainerStatus;
import com.tmobile.kardio.dao.AppLookUpDao;
import com.tmobile.kardio.dao.ComponentDao;
import com.tmobile.kardio.dao.ContainerStatusDao;
import com.tmobile.kardio.dao.EnvironmentDao;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.ContainerStatusEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentStatusApplication.class})
public class ContainerStatusDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EnvironmentDao envDao;

    @Autowired
    private ComponentDao compDao;

    @Autowired
    private AppLookUpDao appDao;

    @Autowired
    private ContainerStatusDao containerStatusDao;

    @Autowired
    private TestDaoService daoService;
    
    private int comp1, comp2;
    private void createComponentWithParent() {
    	daoService.createComponentType();
    	daoService.createComponent();
    	comp1 = TestDaoService.compID;
    	
    	Component component = daoService.getComponent();
    	component.setParentComponentId(TestDaoService.compID);
    	daoService.createComponent(component);
    	comp2 = TestDaoService.compID;
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvContainers_ChildComponent() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus1";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = formatter.format(new Date());
        String endDate = formatter.format(new Date());
        List<ContainerStatusEntity> result = containerStatusDao.getEnvContainers(startDate, endDate, envEntity.getEnvironmentId(), ""+comp2, false);

        Assert.assertEquals("Size does not match", 1, result.size());
    
        ContainerStatusEntity actual = result.get(0);
        Assert.assertEquals("Component does not match", csEntity.getComponent().getComponentId(), actual.getComponent().getComponentId());
        Assert.assertEquals("Environment does not match", csEntity.getEnvironment().getEnvironmentId(), actual.getEnvironment().getEnvironmentId());
        Assert.assertEquals("Container status does not match", csEntity.getContainerStatsId(), actual.getContainerStatsId());
        Assert.assertEquals("Delta value does not match", csEntity.getDeltaValue(), actual.getDeltaValue());
        Assert.assertEquals("Stats date does not match", csEntity.getStatsDate().toString(), actual.getStatsDate().toString());
        Assert.assertEquals("Total container does not match", csEntity.getTotalContainer(), actual.getTotalContainer());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvContainers_ParentComponent() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus2";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = formatter.format(new Date());
        String endDate = formatter.format(new Date());
        List<ContainerStatusEntity> result = containerStatusDao.getEnvContainers(startDate, endDate, envEntity.getEnvironmentId(), ""+comp1, true);

        Assert.assertEquals("Size does not match", 1, result.size());
    
        ContainerStatusEntity actual = result.get(0);
        Assert.assertEquals("Component does not match", csEntity.getComponent().getComponentId(), actual.getComponent().getComponentId());
        Assert.assertEquals("Environment does not match", csEntity.getEnvironment().getEnvironmentId(), actual.getEnvironment().getEnvironmentId());
        Assert.assertEquals("Container status does not match", csEntity.getContainerStatsId(), actual.getContainerStatsId());
        Assert.assertEquals("Delta value does not match", csEntity.getDeltaValue(), actual.getDeltaValue());
        Assert.assertEquals("Stats date does not match", csEntity.getStatsDate().toString(), actual.getStatsDate().toString());
        Assert.assertEquals("Total container does not match", csEntity.getTotalContainer(), actual.getTotalContainer());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvContainers_Multiple() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus3";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = formatter.format(new Date());
        String endDate = formatter.format(new Date());
        List<ContainerStatusEntity> result = containerStatusDao.getEnvContainers(startDate, endDate, envEntity.getEnvironmentId(), comp1+","+comp2, true);

        Assert.assertEquals("Size does not match", 1, result.size());
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllContainersOfParent() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus4";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = formatter.format(new Date());
        String endDate = formatter.format(new Date());
        List<ContainerStatus> result = containerStatusDao.getAllContainersOfParent(startDate, endDate, envEntity.getEnvironmentId(), ""+comp1);

        Assert.assertEquals("Size does not match", 1, result.size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllContainersOfParent_Multiple() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus5";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = formatter.format(new Date());
        String endDate = formatter.format(new Date());
        List<ContainerStatus> result = containerStatusDao.getAllContainersOfParent(startDate, endDate, envEntity.getEnvironmentId(), comp1+","+comp2);

        Assert.assertEquals("Size does not match", 1, result.size());
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testGetCurrentNumberOfContainsers() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus6";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        long actual = containerStatusDao.getCurrentNumberOfContainsers(envEntity.getEnvironmentId(), ""+comp1, true);

        Assert.assertEquals("Size does not match", 1L, actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetCurrentNumberOfContainsers_Multiple() throws ParseException {
    	createComponentWithParent();
    	String envName = "contStatus7";
    	daoService.createEnvironment(envName, 1);
    	EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
    	ComponentEntity compEntity = appDao.getAppComponentId(comp2);
    	
    	ContainerStatusEntity csEntity = new ContainerStatusEntity();
    	csEntity.setComponent(compEntity);
    	csEntity.setEnvironment(envEntity);
    	csEntity.setContainerStatsId(1L);
    	csEntity.setDeltaValue(1);
    	csEntity.setStatsDate(new java.sql.Date(new Date().getTime()));
    	csEntity.setTotalContainer(1);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(csEntity);
        tx.commit();
        session.close();
        
        long actual = containerStatusDao.getCurrentNumberOfContainsers(envEntity.getEnvironmentId(), comp1+","+comp2, false);

        Assert.assertEquals("Size does not match", 1L, actual);
    }

}
