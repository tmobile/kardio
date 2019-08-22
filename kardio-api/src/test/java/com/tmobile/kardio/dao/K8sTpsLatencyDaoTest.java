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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
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
import com.tmobile.kardio.bean.TpsLatency;
import com.tmobile.kardio.bean.TpsLatencyHistory;
import com.tmobile.kardio.db.entity.K8sTpsLatencyHistoryEntity;
import com.tmobile.kardio.db.entity.TpsServiceEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class K8sTpsLatencyDaoTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private K8sTpsLatencyDao latencyDao;
	//@Autowired
	//private TpsLatencyStatusDao tpsLatencyStatusDao;

	
	@Autowired
	private TestDaoService testDaoService;
	@Test
	public void testGetTpsAndLatOfParent() throws ParseException{
		String envName = "getpslathist";
    	Session session = sessionFactory.openSession();
    	K8sTpsLatencyHistoryEntity he=testDaoService.createK8sTpsLatencyHistoryEntity(session, envName);
    	Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		String platform="plarform";
		List<TpsLatencyHistory> tpsLatList=latencyDao.getTpsAndLatOfParent(startDate, endDate, he.getEnvironment().getEnvironmentName(), ""+he.getComponent().getComponentId(), platform);
		//Assert.assertEquals("Size does not match", 1, tpsLatList.size());
	}
	
	@Test
	public void testGetCurrentTpsLatency() throws ParseException{
		String envName="getcurrenttpslatency";
		Session session = sessionFactory.openSession();
		TpsServiceEntity tse=testDaoService.createTpsServiceEntity(session, envName);
		List<TpsLatency> rettps=latencyDao.getCurrentTpsLatency(tse.getEnvironment().getEnvironmentName());
		//Assert.assertEquals("Size does not match", 1, rettps.size());
	}
	
	@Test
	public void testGetTpsAndLatOfComponent() throws ParseException{
		String envName = "getpslathistComp";
    	Session session = sessionFactory.openSession();
    	K8sTpsLatencyHistoryEntity he=testDaoService.createK8sTpsLatencyHistoryEntity(session, envName);
    	Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		String endDate = sdf.format(tomorrow.getTime());
		String platform="plarform";
		List<TpsLatencyHistory> tpsLatList=latencyDao.getTpsAndLatOfComponent(startDate, endDate, 
				he.getEnvironment().getEnvironmentName(), ""+he.getComponent().getComponentId(), platform);
		//Assert.assertEquals("Size does not match", 1, tpsLatList.size());
	}
	
	

}
