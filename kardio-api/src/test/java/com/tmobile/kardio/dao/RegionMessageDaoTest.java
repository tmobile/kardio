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

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import com.tmobile.kardio.bean.ComponentMessages;
import com.tmobile.kardio.bean.Messages;
import com.tmobile.kardio.db.entity.ComponentMessageEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class RegionMessageDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private TestDaoService daoService;
	@Autowired
	private RegionMessageDao regionMessageDao;
	
	@Test
	public void testGetCompRegionMessage() throws ParseException{
		Session session = sessionFactory.openSession();
    	String envName = "getcompregionmessage";
    	String regionName = "region_" + envName;
		ComponentMessages componentMessage = daoService.createComponentMessages(session, envName, regionName);
		List<Messages> result = regionMessageDao.getCompRegionMessage(envName, ""+TestDaoService.compID, regionName);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void testGetCompRegionMessage_Invalid() throws ParseException{
		regionMessageDao.getCompRegionMessage("envname", "1", null);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void testGetCompRegionMessage_EmptyRegion() throws ParseException{
		regionMessageDao.getCompRegionMessage("envname", "1", "");
	}

	@Test
	public void testGetCompRegionMessage_WithDate() throws ParseException{
		Session session = sessionFactory.openSession();
    	String envName="getcompregionmessagemore";
    	String regionName = "region_" + envName;
    	Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(new Date());
		yesterday.add(Calendar.DATE, -1);
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE, 1);
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		
    	ComponentMessages cme=daoService.createComponentMessages(session, envName, regionName);
    	List<Messages> result=regionMessageDao.getCompRegionMessage(envName, ""+TestDaoService.compID, regionName, startDate);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void testGetCompRegionMessage_WithDateNullRegtion() throws ParseException{
    	Calendar yesterday = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yesterday.getTime());
		regionMessageDao.getCompRegionMessage("envname", "1", null, startDate);
	}

	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void testGetCompRegionMessage_InvalidDateFormat() throws ParseException{
		regionMessageDao.getCompRegionMessage("envname", "1", null, "INVALIDDATE");
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testCreateCompRegionMessage() {
		Session session = sessionFactory.openSession();
    	String envName="createcompregionmessage";
    	String regionName = "region_" + envName;
    	daoService.createComponentMessages(session, envName, regionName);
	}

	@Test
    @Transactional
    @Rollback(true)
    public void testUpdateCompRegionMessage() {
		Session session = sessionFactory.openSession();
    	String envName="updatecompregionmessage";
    	String regionName = "region_" + envName;
    	ComponentMessages cm = daoService.createComponentMessages(session, envName, regionName);
    	cm.setMessageId(TestDaoService.compMessageID);
    	cm.setMessage("New message");
    	regionMessageDao.updateCompMessage(cm);
	}
	
}
