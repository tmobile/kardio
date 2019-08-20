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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.K8sContainerStatus;
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class K8sContainerStatusDaoTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private K8sContainerStatusDao containerStatusDao;
	
	@Autowired
	private TestDaoService daoService;

	@Test
	public void testGetEnvContainers() throws ParseException {
		String envName = "k8sgetenvcontainers";
    	Session session = sessionFactory.openSession();
    	K8sPodsContainersEntity k8scse = daoService.createK8sContainerStatusEntity(session, envName);
    	Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		List <K8sPodsContainersEntity> result = containerStatusDao.getEnvContainers(startDate, endDate, k8scse.getEnvironment().getEnvironmentId(), "" +k8scse.getComponent().getComponentId(), true);

	}

	@Test
	public void testGetAllContainersOfParent() throws ParseException {
		String envName = "k8sgetallcontainersofparent";
    	Session session = sessionFactory.openSession();
    	K8sPodsContainersEntity k8scse = daoService.createK8sContainerStatusEntity(session, envName);
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		List<K8sContainerStatus> result = containerStatusDao.getAllContainersOfParent(startDate, endDate, k8scse.getEnvironment().getEnvironmentId(), ""+k8scse.getComponent().getComponentId() );
	}
	 
@Test
public void getCurrentNumberOfApis() throws ParseException  {
		Session session=sessionFactory.openSession();
		String envName = "k8sgetcurnumapis";
		boolean isParentComponents=true;
		K8sPodsContainersEntity k8scse = daoService.createK8sContainerStatusEntity(session, envName);
        long currentApi = containerStatusDao.getCurrentNumberOfContainsers(k8scse.getEnvironment().getEnvironmentId(), "" +k8scse.getComponent().getComponentId(), isParentComponents);
		
		}  

 
}
