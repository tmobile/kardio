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
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.db.entity.ApiStatusEntity;
import com.tmobile.kardio.util.DaoUtil;

/**
 *  Dao Class to get the API status.
 *
 */

@Repository
public class ApiStatusDaoImpl implements ApiStatusDao {

	@Autowired
	private SessionFactory sessionFactory;
	
		
	/*
	 * Get Application APIS , based on the input parameters date, environment & component ids
	 */
	
	@Override
	public List<ApiStatus> getEnvApis(String startDate, String endDate, int envId,
			String componentIdsStrg) throws ParseException {			
		Session session = sessionFactory.openSession();
		return DaoUtil.getEnvApis(startDate, endDate, envId, componentIdsStrg, session, ApiStatusEntity.class);
	}
	
	
	@Override
	public long getCurrentNumberOfApis(int envId,String componentIdsStrg) throws ParseException {
		
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);
		
		Session session = sessionFactory.openSession();
		DetachedCriteria subMaxDate = DetachedCriteria.forClass(ApiStatusEntity.class);
		subMaxDate.setProjection(Projections.max("statusDate"));
		Criteria crtCurrenrApi = session.createCriteria(ApiStatusEntity.class);
		crtCurrenrApi.add(Property.forName("statusDate").eq(subMaxDate));
		DaoUtil.addEnvironmentToCriteria(envId, comIdList, crtCurrenrApi);
		crtCurrenrApi.setProjection(Projections.sum("totalApi"));
		long currentNumberOfApi = (long) (crtCurrenrApi.uniqueResult() == null ? (long)0 : crtCurrenrApi.uniqueResult());
		session.close();
		return currentNumberOfApi;
	}

}
