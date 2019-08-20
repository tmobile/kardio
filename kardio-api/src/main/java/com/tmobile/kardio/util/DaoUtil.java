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
package com.tmobile.kardio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.bean.ContainerStatus;
import com.tmobile.kardio.bean.TpsLatency;
import com.tmobile.kardio.bean.TpsLatencyHistory;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.dao.EnvironmentDao;
import com.tmobile.kardio.db.entity.K8sApiStatusEntity;
import com.tmobile.kardio.db.entity.K8sTpsLatencyHistoryEntity;
import com.tmobile.kardio.db.entity.TpsLatencyHistoryEntity;
import com.tmobile.kardio.db.entity.TpsServiceEntity;

public class DaoUtil {
	

	private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

	public static ProjectionList getContainerStatusProjectionList() {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("component.parentComponent.componentId"));
		projectionList.add(Projections.property("contSts.statusDate"));
		projectionList.add(Projections.sum("contSts.totalContainers"));
	    projectionList.add(Projections.groupProperty("component.parentComponent.componentId"));
		projectionList.add(Projections.groupProperty("contSts.statusDate"));
		return projectionList;
	}
	
	public static List<TpsLatency> convertToTpsLatency(List<TpsServiceEntity> apiTpsLatList, Query appTpsLatQuery) {
		@SuppressWarnings("unchecked")
		List<Object[]> totTps = appTpsLatQuery.list();

		List<TpsLatency> rettps = new ArrayList<TpsLatency>();
		for (Object[] tRow : totTps) {
			TpsLatency tps = new TpsLatency();
			tps.setComponentId((Integer) tRow[0]);
			tps.setTpsValue((Double) tRow[1]);
			tps.setLatencyValue((Double) tRow[2]);
			rettps.add(tps);
		}
		for (TpsServiceEntity tList : apiTpsLatList) {
			int compId = tList.getComponent().getComponentId();
			double tpsV = tList.getTpsValue();
			TpsLatency tps = new TpsLatency();
			tps.setComponentId(compId);
			tps.setTpsValue(tpsV);
			tps.setLatencyValue(tList.getLatencyValue());
			rettps.add(tps);

		}
		return rettps;
	}
	
	public static List<TpsLatencyHistory> getTpsAndLatOfParent(String startDate, String endDate, String environment,
			String componentIdsStrg, String platform, Class entityClass, SessionFactory sessionFactory, EnvironmentDao environmentDao) throws ParseException {

		Session session = sessionFactory.openSession();
		
		final SimpleDateFormat sdf1 = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		List<Integer> comIdList = convertCSVToList(componentIdsStrg);
		
		Date sDate = sdf1.parse(startDate);
		Date eDate = sdf1.parse(endDate);
		Criteria tpaLatCriteria = session.createCriteria(entityClass, "tpsLat");
		tpaLatCriteria.createCriteria("tpsLat.component", "component");
		tpaLatCriteria.add(Restrictions.gt("tpsLat.statusDate", sDate ));
		tpaLatCriteria.add(Restrictions.le("tpsLat.statusDate", eDate ));
		tpaLatCriteria.add(Restrictions.isNotNull("component.parentComponent.componentId"));
		if(environment != null){
			int envId = environmentDao.getEnironmentIdFromName(environment);
			tpaLatCriteria.add(Restrictions.eq("tpsLat.environment.environmentId", envId));
		}

		if(platform!=null && !platform.equalsIgnoreCase("All")){
			tpaLatCriteria.add(Restrictions.eq("component.platform", platform));
		}
		if(comIdList.size() > 0){
			tpaLatCriteria.add(Restrictions.in("component.parentComponent.componentId", comIdList));
		}
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("component.parentComponent.componentId"));
		projectionList.add(Projections.property("tpsLat.statusDate"));
		projectionList.add(Projections.sum("tpsLat.tpsValue"));
		projectionList.add(Projections.sum("tpsLat.latencyValue"));
	    projectionList.add(Projections.groupProperty("component.parentComponent.componentId"));
		projectionList.add(Projections.groupProperty("tpsLat.statusDate"));
		tpaLatCriteria.setProjection(projectionList);
	    @SuppressWarnings("unchecked")
		List<Object[]> conList = tpaLatCriteria.list();
	    List<TpsLatencyHistory> tpsLatList = new ArrayList<TpsLatencyHistory>();
        for (Object[] aRow : conList) {
        	TpsLatencyHistory tpsLatHist = new TpsLatencyHistory();
            Integer comId = (Integer) aRow[0];
            tpsLatHist.setComponentId(comId);
            Date statusDate = (Date) aRow[1];
            tpsLatHist.setStatusDate(statusDate.toString());
            double tpsVal = (double) aRow[2];
            tpsLatHist.setTpsValue(tpsVal);
            double latencyVal = (double) aRow[3];
            tpsLatHist.setLatencyValue(latencyVal);
            tpsLatList.add(tpsLatHist);   		
        } 	    
        session.close();
		return tpsLatList;
	}

	public static List<TpsLatencyHistory> getTpsAndLatOfComponent(String startDate, String endDate, String environment,
			String componentIdsStrg, String platform, Class entityClass, SessionFactory sessionFactory, EnvironmentDao environmentDao) throws ParseException {

		Session session = sessionFactory.openSession();
		final SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		List<Integer> comIdList = convertCSVToList(componentIdsStrg);
		
		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);
		Criteria tpsLatencyCriteria = session.createCriteria(entityClass, "tpsLat");
		tpsLatencyCriteria.createCriteria("tpsLat.component", "component");
		tpsLatencyCriteria.add(Restrictions.gt("tpsLat.statusDate", sDate ));
		tpsLatencyCriteria.add(Restrictions.le("tpsLat.statusDate", eDate ));
		if(environment != null){
			int envId = environmentDao.getEnironmentIdFromName(environment);
			tpsLatencyCriteria.add(Restrictions.eq("tpsLat.environment.environmentId", envId));
		}
		if(platform!=null && !platform.equalsIgnoreCase("All")){
			tpsLatencyCriteria.add(Restrictions.eq("component.platform", platform));
		}
		if(comIdList.size() > 0){
			tpsLatencyCriteria.add(Restrictions.in("component.componentId", comIdList));
		}
		List<TpsLatencyHistory> listOfTpsLatency  = new ArrayList<TpsLatencyHistory>();
		if(platform.equals(Constants.PLATFORM_K8S)){
			getK8sTpsLatency(tpsLatencyCriteria, listOfTpsLatency);
		}else if (platform.equals(Constants.PLATFORM_MESOS)){
			getMesosTpsLatency(tpsLatencyCriteria, listOfTpsLatency);
		}
		session.close();
		return listOfTpsLatency;
	}
	
	private static void getMesosTpsLatency(Criteria tpsLatencyCriteria, List<TpsLatencyHistory> listOfTpsLatency) {
		List<TpsLatencyHistoryEntity> listTpsLatEntity = tpsLatencyCriteria.list();
		for(TpsLatencyHistoryEntity tpsLatEntity : listTpsLatEntity) {
			TpsLatencyHistory tpsLatHistory = new TpsLatencyHistory();
			tpsLatHistory.setStatusDate(tpsLatEntity.getStatusDate().toString());
			tpsLatHistory.setTpsValue(tpsLatEntity.getTpsValue());
			tpsLatHistory.setLatencyValue(tpsLatEntity.getLatencyValue());
			listOfTpsLatency.add(tpsLatHistory);
		}
	}

	private static void getK8sTpsLatency(Criteria tpsLatencyCriteria, List<TpsLatencyHistory> listOfTpsLatency) {
		List<K8sTpsLatencyHistoryEntity> listTpsLatEntity = tpsLatencyCriteria.list();
		for(K8sTpsLatencyHistoryEntity tpsLatEntity : listTpsLatEntity) {
			TpsLatencyHistory tpsLatHistory = new TpsLatencyHistory();
			tpsLatHistory.setStatusDate(tpsLatEntity.getStatusDate().toString());
			tpsLatHistory.setTpsValue(tpsLatEntity.getTpsValue());
			tpsLatHistory.setLatencyValue(tpsLatEntity.getLatencyValue());
			listOfTpsLatency.add(tpsLatHistory);
		}
	}

	public static TpsLatency getCurrentTpsAndLatency(String environment, String componentIdsStrg, boolean isParent,
			String platform, SessionFactory sessionFactory, EnvironmentDao environmentDao) {
		Session session = sessionFactory.openSession();
		List<Integer> comIdList = convertCSVToList(componentIdsStrg);
		
		Criteria crtCurrentCont = session.createCriteria(TpsServiceEntity.class, "tpsLat");
		crtCurrentCont.createCriteria("tpsLat.component", "component");
		if(environment != null && environment.length() != 0 && !environment.equalsIgnoreCase("all")){
			int envId = environmentDao.getEnironmentIdFromName(environment);
			crtCurrentCont.add(Restrictions.eq("environment.environmentId", envId));
		}
		/**
		 * Adding platform criteria for current TPS & Latency.
		 */
		if(platform!=null && !platform.equalsIgnoreCase("All"))
			crtCurrentCont.add(Restrictions.eq("component.platform", platform));
		if (comIdList.size() > 0) {
			if (isParent) {
				crtCurrentCont.add(Restrictions.in("component.parentComponent.componentId", comIdList));				
			} else {
				crtCurrentCont.add(Restrictions.in("component.componentId", comIdList));				
			}
		}
		
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.sum("tpsValue"));
		projList.add(Projections.sum("latencyValue"));
		crtCurrentCont.setProjection(projList);
		List<Object[]> curTpsLatList = crtCurrentCont.list();
		TpsLatency tpsLat = new TpsLatency();
	        for (Object[] aRow : curTpsLatList) {
	        	if(aRow[0] == null){
	        		continue;
	        	}
	        	double tpsVal = (double) aRow[0];
	            double latencyVal = (double) aRow[1];
	            tpsLat.setTpsValue(tpsVal);
	            tpsLat.setLatencyValue(latencyVal);
	        } 	   
		session.close();
		return tpsLat;
	}
	
	public static List<ApiStatus> getEnvApis(String startDate, String endDate, int envId, String componentIdsStrg,
			Session session, Class enitityClass) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);
		
		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);
				
		Criteria apiCriteria = session.createCriteria(enitityClass, "apiSts");
		apiCriteria.createCriteria("apiSts.component", "component");
		apiCriteria.createCriteria("apiSts.environment", "environment");
		apiCriteria.add(Restrictions.gt("apiSts.statusDate", sDate ));
		apiCriteria.add(Restrictions.le("apiSts.statusDate", eDate ));
		if(envId != 0){
			apiCriteria.add(Restrictions.eq("environment.environmentId", envId));
		}
		if(comIdList.size() > 0){
			apiCriteria.add(Restrictions.in("component.componentId", comIdList));
		}
		apiCriteria.add(Restrictions.eq("environment.envLock", 0));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("component.componentId"));
		projectionList.add(Projections.property("apiSts.statusDate"));
		projectionList.add(Projections.property("component.componentName"));
        projectionList.add(Projections.sum("apiSts.totalApi"));
	    projectionList.add(Projections.groupProperty("component.componentId"));
		projectionList.add(Projections.groupProperty("apiSts.statusDate"));
	    apiCriteria.setProjection(projectionList);
	    @SuppressWarnings("unchecked")
		List<Object[]> appList = apiCriteria.list();
	    
	    List<ApiStatus> apiStatusList = new ArrayList<ApiStatus>();
        for (Object[] aRow : appList) {
        	ApiStatus apisStatus = new ApiStatus();
            Integer comId = (Integer) aRow[0];
            apisStatus.setComponentId(comId);
            Date statsDate = (Date) aRow[1];
            apisStatus.setStatusDate(statsDate.toString());
            String compName = (String) aRow[2];
            apisStatus.setComponentName(compName);
            long totalApi = (long) aRow[3];
            apisStatus.setTotalApis(totalApi);
            apiStatusList.add(apisStatus); 		
        }
        session.close();
        return apiStatusList;
	}
	
	public static void addEnvironmentToCriteria(int envId, List<Integer> compIds, Criteria criteria) {
		criteria.createCriteria("environment", "environment");
		if(envId != 0){
			criteria.add(Restrictions.eq("environment.environmentId", envId));
		}
		criteria.add(Restrictions.eq("environment.envLock", 0));
		if(compIds.size() > 0){
			criteria.add(Property.forName("component.componentId").in(compIds));
		}
	}
	

	public static void addEnvironmentToCriteria(int envId, boolean isParentComponents, List<Integer> compIds,
			Criteria criteria) {
		criteria.createCriteria("environment", "environment");
		if(envId != 0){
			criteria.add(Restrictions.eq("environment.environmentId", envId));
		}
		criteria.add(Restrictions.eq("environment.envLock", 0));
		if (compIds.size() > 0) {
			if (isParentComponents) {
				criteria.add(Restrictions.in("component.parentComponent.componentId", compIds));
			} else {
				criteria.add(Restrictions.in("component.componentId", compIds));
			}
		}
	}
	
	public static List<ContainerStatus> createContainerStatuses(List<Object[]> conList) {
		List<ContainerStatus> contStatusList = new ArrayList<ContainerStatus>();
        for (Object[] aRow : conList) {
        	ContainerStatus contStatus = new ContainerStatus();
            Integer comId = (Integer) aRow[0];
            if(comId == null){
            	continue;
            }
            contStatus.setComponentId(comId);
            Date statsDate = (Date) aRow[1];
            contStatus.setStatusDate(statsDate.toString());
            long totalCont = (long) aRow[2];
            contStatus.setTotalContainers(totalCont);
            contStatusList.add(contStatus);   		
        }
		return contStatusList;
	}

	public static List<Integer> convertCSVToList(String csv) {
		List<Integer> comIdList = new ArrayList<Integer>();
		if(csv !=null && csv.indexOf(',') > -1){
			for(String compIdStg : csv.split(",")){
				comIdList.add(Integer.parseInt(compIdStg) );
			}
		} else if(csv != null && csv.length() != 0) {
			comIdList.add(Integer.parseInt(csv));
		}
		return comIdList;
	}
}
