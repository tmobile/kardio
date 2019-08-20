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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.bean.K8sContainerStatus;
import com.tmobile.kardio.db.entity.K8sObjectPodsEntity;
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;
import com.tmobile.kardio.util.DaoUtil;
@Repository
public class K8sContainerStatusDaoImpl implements K8sContainerStatusDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	/*
	 * Get APP containers, based on the input parameters date, environment & component ids
	 */
	@Override
	public List<K8sPodsContainersEntity> getEnvContainers(String startDate, String endDate, int envId, String componentIdsStrg, boolean isParentComponents) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);

		
		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);
		Session session = sessionFactory.openSession();
		Criteria containerCriteria = session.createCriteria(K8sPodsContainersEntity.class, "contSts");
		containerCriteria.createCriteria("contSts.component", "component");
		containerCriteria.add(Restrictions.gt("contSts.statusDate", sDate ));
		containerCriteria.add(Restrictions.le("contSts.statusDate", eDate ));

		DaoUtil.addEnvironmentToCriteria(envId, isParentComponents, comIdList, containerCriteria);

		@SuppressWarnings("unchecked")
		List<K8sPodsContainersEntity> listContEntity = containerCriteria.list();
		session.close();
		return listContEntity;
	}
	

	@Override
	public List<K8sContainerStatus> getAllContainersOfParent(String startDate, String endDate, int envId, String componentIdsStrg) throws ParseException {
		
		final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);

		Date sDate = sdf1.parse(startDate);
		Date eDate = sdf1.parse(endDate);
		Session session = sessionFactory.openSession();
		Criteria containerCriteria = session.createCriteria(K8sPodsContainersEntity.class, "contSts");
		containerCriteria.createCriteria("contSts.component", "component");
		containerCriteria.createCriteria("contSts.environment", "environment");
		containerCriteria.add(Restrictions.ge("contSts.statusDate", sDate ));
		containerCriteria.add(Restrictions.le("contSts.statusDate", eDate ));
		if(envId != 0){
		   containerCriteria.add(Restrictions.eq("environment.environmentId", envId));
		}
		containerCriteria.add(Restrictions.eq("environment.envLock", 0));
		if(comIdList != null && comIdList.size() != 0){
			containerCriteria.add(Restrictions.in("component.parentComponent.componentId", comIdList));
		}
		
		ProjectionList projectionList = DaoUtil.getContainerStatusProjectionList();
		containerCriteria.setProjection(projectionList);
	    @SuppressWarnings("unchecked")
		List<Object[]> conList = containerCriteria.list();
	    List<K8sContainerStatus> contStatusList = new ArrayList<K8sContainerStatus>();
        for (Object[] aRow : conList) {
        	K8sContainerStatus contStatus = new K8sContainerStatus();
            Integer comId = (Integer) aRow[0];
            contStatus.setComponentId(comId);
            Date statsDate = (Date) aRow[1];
            contStatus.setStatusDate(statsDate.toString());
            long totalCont = (long) aRow[2];
            contStatus.setTotalContainers(totalCont);
            contStatusList.add(contStatus);   		
        } 	    
        session.close();
        return contStatusList;
	        
	}
	

	@Override
	public long getCurrentNumberOfContainsers(int envId,String componentIdsStrg, boolean isParentComponents) throws ParseException {
		
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);

		Session session = sessionFactory.openSession();
		DetachedCriteria subMaxDate = DetachedCriteria.forClass(K8sPodsContainersEntity.class);
		subMaxDate.setProjection(Projections.max("statusDate"));
		Criteria crtCurrentCont = session.createCriteria(K8sPodsContainersEntity.class, "contSts");
		crtCurrentCont.createCriteria("contSts.component", "component");
		crtCurrentCont.add(Property.forName("statusDate").eq(subMaxDate));
		
		DaoUtil.addEnvironmentToCriteria(envId, isParentComponents, comIdList, crtCurrentCont);
		
		crtCurrentCont.setProjection(Projections.sum("totalContainers"));
		long currentNumOfCont = (long) (crtCurrentCont.uniqueResult() == null ? (long)0 : crtCurrentCont.uniqueResult());
		session.close();
		return currentNumOfCont;
		
	}


	@Override
	public List<ApiStatus> getRemK8sObjPodsCont(String startDate, String endDate, int envId) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);
		Session session = sessionFactory.openSession();
		Criteria podsCriteria = session.createCriteria(K8sObjectPodsEntity.class, "objPods");
		podsCriteria.createCriteria("objPods.environment", "environment");
		podsCriteria.add(Restrictions.gt("objPods.statusDate", sDate ));
		podsCriteria.add(Restrictions.le("objPods.statusDate", eDate ));
		if(envId != 0){
			podsCriteria.add(Restrictions.eq("environment.environmentId", envId));
		}
		podsCriteria.add(Restrictions.eq("environment.envLock", 0));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("objPods.objectName"));
		projectionList.add(Projections.sum("objPods.pods"));
        projectionList.add(Projections.sum("objPods.containers"));
        projectionList.add(Projections.property("objPods.statusDate"));
	    projectionList.add(Projections.groupProperty("objPods.objectName"));
		projectionList.add(Projections.groupProperty("objPods.statusDate"));
		podsCriteria.setProjection(projectionList);
		podsCriteria.addOrder(Order.asc("objPods.statusDate"));
	    @SuppressWarnings("unchecked")
		List<Object[]> appList = podsCriteria.list();
	    
	    List<ApiStatus> apiStatusList = new ArrayList<ApiStatus>();
        for (Object[] aRow : appList) {
        	ApiStatus apisStatus = new ApiStatus();
           
            String objName = (String) aRow[0];
            apisStatus.setComponentName(objName);
            long pods = (long) aRow[1];
            apisStatus.setTotalPods(pods);
            long cont = (long) aRow[2];
            apisStatus.setTotalContainers(cont);
            Date statsDate = (Date) aRow[3];
            apisStatus.setStatusDate(statsDate.toString());
            apiStatusList.add(apisStatus); 		
        }
        session.close();
        return apiStatusList;
		
	}


}
