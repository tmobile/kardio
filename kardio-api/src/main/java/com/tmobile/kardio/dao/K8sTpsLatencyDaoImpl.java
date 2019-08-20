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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.TpsLatency;
import com.tmobile.kardio.bean.TpsLatencyHistory;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.K8sTpsLatencyHistoryEntity;
import com.tmobile.kardio.db.entity.TpsServiceEntity;
import com.tmobile.kardio.util.DaoUtil;

@Repository
public class K8sTpsLatencyDaoImpl implements K8sTpsLatencyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private EnvironmentDao environmentDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<TpsLatency> getCurrentTpsLatency(String environment) {
		Session session = sessionFactory.openSession();
		Criteria tpsLatCriteria = session.createCriteria(TpsServiceEntity.class);
		int envId = environmentDao.getEnironmentIdFromName(environment);
		tpsLatCriteria.add(Restrictions.eq("environment.environmentId", envId));
		List<TpsServiceEntity> apiTpsLatList = (List<TpsServiceEntity>) tpsLatCriteria.list();

		Query appTpsLatQuery = session.createQuery(HQLConstants.QUERY_GET_SUM_TPS_VALUE);
		appTpsLatQuery.setParameter("envId", envId);
		List<TpsLatency> rettps = DaoUtil.convertToTpsLatency(apiTpsLatList, appTpsLatQuery);
		session.close();
		return rettps;
	}
	
	/* Get the TPS & Latency history */
	@Override
	public List<TpsLatencyHistory> getTpsAndLatOfParent(String startDate, String endDate, String environment,
			String componentIdsStrg,String platform) throws ParseException {
		List<TpsLatencyHistory> tpsLatList = DaoUtil.getTpsAndLatOfParent(startDate, endDate, environment, componentIdsStrg,
				platform, K8sTpsLatencyHistoryEntity.class, sessionFactory, environmentDao);
        return tpsLatList;
	        
	}


	@Override
	public List<TpsLatencyHistory> getTpsAndLatOfComponent(String startDate, String endDate, String environment,
			String componentIdsStrg,String platform) throws ParseException {
		List<TpsLatencyHistory> listOfTpsLatency = DaoUtil.getTpsAndLatOfComponent(startDate, endDate, environment,
				componentIdsStrg, Constants.PLATFORM_K8S, K8sTpsLatencyHistoryEntity.class, sessionFactory, environmentDao);
		return listOfTpsLatency;
	}

	@Override
	public TpsLatency getCurrentTpsAndLatency(String environment, String componentIdsStrg, boolean isParent) {
		TpsLatency tpsLat = DaoUtil.getCurrentTpsAndLatency(environment, componentIdsStrg, 
				isParent, null, sessionFactory, environmentDao);
		return tpsLat;
	}
}
