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

import com.tmobile.kardio.bean.TpsLatencyHistory;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.TpsLatencyHistoryEntity;
import com.tmobile.kardio.util.DaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Repository
public class TpsLatencyStatusDaoImpl implements TpsLatencyStatusDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private EnvironmentDao environmentDao;
	@Override
	public List<TpsLatencyHistory> getTotalTpsLatency(String startDate, String endDate, String environment, String componentIdsStrg)
			throws ParseException {

		List<TpsLatencyHistory> tpsLatStsList = new ArrayList<TpsLatencyHistory>();
		int envId;
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);
		
		
		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);
		
		Session session= sessionFactory.openSession();
		
		List<Object[]> totTps=null;
		Criteria tpsLatencyCriteria = session.createCriteria(TpsLatencyHistoryEntity.class,"tpsLatSts");
		tpsLatencyCriteria.createCriteria("tpsLatSts.component", "component");
		tpsLatencyCriteria.add(Restrictions.gt("tpsLatSts.statusDate", sDate ));
		tpsLatencyCriteria.add(Restrictions.le("tpsLatSts.statusDate", eDate ));
		if(comIdList.size() > 0){
			tpsLatencyCriteria.add(Restrictions.in("component.componentId", comIdList));
			if(environment != null && !environment.equalsIgnoreCase("all")) {
				envId = environmentDao.getEnironmentIdFromName(environment);
				tpsLatencyCriteria.add(Restrictions.eq("tpsLatSts.environment.environmentId", envId));
			  
			      Query parentTpsLatQuery = session.createQuery(HQLConstants.QUERY_GET_SUM_PARENT_TPS_LATENCY_HISTORY_ENV);
			      parentTpsLatQuery.setParameter("envId",envId);
				  parentTpsLatQuery.setParameter("startDate", sDate);
				  parentTpsLatQuery.setParameter("endDate", eDate);
			      parentTpsLatQuery.setParameterList("parentComponentList", comIdList);
			      totTps = parentTpsLatQuery.list();	     
			 
			} else {
				   Query parentTpsLatQuery = session.createQuery(HQLConstants.QUERY_GET_SUM_PARENT_TPS_LATENCY_HISTORY_ALL_ENV);
				   parentTpsLatQuery.setParameterList("parentComponentList", comIdList);
				   parentTpsLatQuery.setParameter("startDate", sDate);
				   parentTpsLatQuery.setParameter("endDate", eDate);
				   totTps = parentTpsLatQuery.list();
			}
			
		} else {
			if(environment != null && !environment.equalsIgnoreCase("all")) {
				envId = environmentDao.getEnironmentIdFromName(environment);
				tpsLatencyCriteria.add(Restrictions.eq("tpsLatSts.environment.environmentId", envId));
			  
				Query parentTpsLatQuery = session.createQuery(HQLConstants.QUERY_GET_SUM_ALL_TPS_LATENCY_HISTORY_ENV);
				parentTpsLatQuery.setParameter("envId",envId);
				totTps = parentTpsLatQuery.list();
			 
			} else {
				   Query parentTpsLatQuery = session.createQuery(HQLConstants.QUERY_GET_SUM_ALL_TPS_LATENCY_HISTORY_ALL_ENV);
				   totTps = parentTpsLatQuery.list();
			}
		}
		
		ProjectionList projectionList= Projections.projectionList();
		projectionList.add(Projections.property("component.componentId"));
		projectionList.add(Projections.property("component.componentName"));
		projectionList.add(Projections.property("tpsLatSts.statusDate"));
		projectionList.add(Projections.property("tpsLatSts.tpsValue"));
		projectionList.add(Projections.property("tpsLatSts.latencyValue"));
		tpsLatencyCriteria.setProjection(projectionList);
		
		 @SuppressWarnings("unchecked")
			List<Object[]> appList = tpsLatencyCriteria.list();
		        for (Object[] aRow : appList) {
		        	createTPSLatency(tpsLatStsList, aRow); 
		         
		        }
		        
		   // Adding Tps and Latency for parent component id 
		      
		        for(Object[] aRow : totTps) {
		        	createTPSLatency(tpsLatStsList, aRow); 
		        }        
		
		session.close();
        return tpsLatStsList;
	}
	
	private void createTPSLatency(List<TpsLatencyHistory> tpsLatStsList, Object[] aRow) {
		TpsLatencyHistory tpsLatencyStatus = new TpsLatencyHistory();
		Integer comId = (Integer) aRow[0];
		tpsLatencyStatus.setComponentId(comId);
		String comName = (String)aRow[1];
		tpsLatencyStatus.setComponentName(comName);
		Date statsDate = (Date) aRow[2];
		tpsLatencyStatus.setStatusDate(statsDate.toString());
		double tpsValue = (double) aRow[3];
		tpsLatencyStatus.setTpsValue(tpsValue);
		double latencyValue = (double) aRow[4];
		tpsLatencyStatus.setLatencyValue(latencyValue);
		tpsLatStsList.add(tpsLatencyStatus);
	}	
}
