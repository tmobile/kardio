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
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.TestDataProvider;
import com.tmobile.kardio.bean.AvailabilityData;
import com.tmobile.kardio.db.entity.DaillyCompStatusEntity;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class AvailabilityPercentageDaoTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private AvailabilityPercentageDao availabilityPercentageDao;
	@Autowired 
	private EnvironmentDao envDao;
	@Autowired
	private TestDaoService daoService;
	@Test
	public void testGetAppAvailabilityPercentage() throws ParseException{
		String envName="getappavailabilitypercentage";
		String interval="D";
		DaillyCompStatusEntity dcse=daoService.createDailyStatusEntity(envName);
		//int envId = envDao.getEnironmentIdFromName(envName);
		List<AvailabilityData> result=availabilityPercentageDao.getAllAvailabilityPercentage(envName, interval, TestDataProvider.DEFAULT_PLATFORM, TestDataProvider.DEFAULT_REGION);
		
	}
	

}
