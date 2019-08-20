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
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class K8sPodsStatusDaoTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private K8sPodsStatusDao k8sPodsStatusDao;
	
	@Autowired
	private TestDaoService testDaoService;
	@Test
	public void testGetPodsStatus() throws ParseException{
		String envName = "getenvprod";
    	Session session = sessionFactory.openSession();
    	K8sPodsContainersEntity k8pse= testDaoService.createK8sPodsStatusEntity(session, envName);
    	Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		List<ApiStatus> result=k8sPodsStatusDao.getPodsStatus(startDate, endDate, k8pse.getEnvironment().getEnvironmentId(),""+k8pse.getComponent().getComponentId(), true);
		//Assert.assertEquals("Size does not match", 1, result.size());
		//Assert.assertEquals("ComponentID does not match", k8pse.getComponent().getComponentId(), TestDaoService.compID);
	}
	
	@Test
	public void testGetPodsStatus_multiple() throws ParseException{
		String envName = "getenvprodMultple";
    	Session session = sessionFactory.openSession();
    	K8sPodsContainersEntity k8pse= testDaoService.createK8sPodsStatusEntity(session, envName);
    	Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		List<ApiStatus> result=k8sPodsStatusDao.getPodsStatus(startDate, endDate, k8pse.getEnvironment().getEnvironmentId(),""+ k8pse.getComponent().getParentComponent().getComponentId(), true);
		ApiStatus actual=result.get(0);
		Assert.assertEquals("Size does not match", 1, result.size());
	//	Assert.assertEquals("ComponentID does not match", k8pse.getComponent().getParentComponent().getComponentId(), TestDaoService.parentCompID);
		Assert.assertEquals("TotalProd does not match", k8pse.getTotalPods(), actual.getTotalPods());
	}
	
	@Test
	public void getCurrentNumberOfApis() throws ParseException  {
			Session session=sessionFactory.openSession();
			String envName = "getcurnumprod";
			K8sPodsContainersEntity k8pse= testDaoService.createK8sPodsStatusEntity(session, envName);
	        long currentNoProd = k8sPodsStatusDao.getCurrentNumberOfPods(k8pse.getEnvironment().getEnvironmentId(), ""+testDaoService.compID, true);
			/*Assert.assertEquals("Count does not match", 1, currentNoProd);	*/
			Assert.assertEquals("EnvironmentID does not match", k8pse.getEnvironment().getEnvironmentId(), TestDaoService.getLastCreatedEnvironmentID());
			Assert.assertEquals("ComponentID does not match", k8pse.getComponent().getComponentId(), TestDaoService.compID);
			
			}  

}
