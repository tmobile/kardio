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
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.db.entity.K8sObjectPodsEntity;
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;
import com.tmobile.kardio.util.DaoUtil;

@Repository
public class K8sPodsStatusDaoImpl implements K8sPodsStatusDao {
    
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public List<ApiStatus> getPodsStatus(String startDate, String endDate, int envId, String componentIdsStrg, boolean isParentComponents) throws ParseException {
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

		ProjectionList projectionList= Projections.projectionList();
		projectionList.add(Projections.property("component.componentId"));
		projectionList.add(Projections.property("statusDate"));
		projectionList.add(Projections.property("totalPods"));
		projectionList.add(Projections.property("environment.environmentId"));
	
		containerCriteria.setProjection(projectionList);

		@SuppressWarnings("unchecked")
		List<Object[]> K8sPodsList = containerCriteria.list();
		List<ApiStatus> k8sList = new ArrayList<ApiStatus>();
		 for (Object[] aRow : K8sPodsList) {
			 ApiStatus k8sStatus = new ApiStatus();
			 Integer comId = (Integer) aRow[0];
			 k8sStatus.setComponentId(comId);
			 Date statsDate = (Date) aRow[1];
			 k8sStatus.setStatusDate(statsDate.toString());
			 Integer totalPods = (Integer)aRow[2];
			 k8sStatus.setTotalPods(totalPods);
			 Integer environmentId = (Integer)aRow[3];
			 k8sStatus.setEnvironmentId(environmentId);
			 k8sList.add(k8sStatus);
		 }
		session.close();
		return k8sList;
	}

	@Override
	public long getCurrentNumberOfPods(int envId, String componentIdsStrg, boolean isParentComponents) throws ParseException {
		
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);
		
		Session session = sessionFactory.openSession();
		DetachedCriteria subMaxDate = DetachedCriteria.forClass(K8sPodsContainersEntity.class);
		subMaxDate.setProjection(Projections.max("statusDate"));
		Criteria crtCurrentCont = session.createCriteria(K8sPodsContainersEntity.class, "contSts");
		crtCurrentCont.createCriteria("contSts.component", "component");
		crtCurrentCont.add(Property.forName("statusDate").eq(subMaxDate));
		
		DaoUtil.addEnvironmentToCriteria(envId, isParentComponents, comIdList, crtCurrentCont);

		crtCurrentCont.setProjection(Projections.sum("totalPods"));
		long currentNumOfCont = (long) (crtCurrentCont.uniqueResult() == null ? (long)0 : crtCurrentCont.uniqueResult());
		session.close();
		return currentNumOfCont;
		
	}
	
	/**
	 * Function to get the number of pods or containters of K8s objects, based on the object argument passed.
	 * @param environment
	 * @param objPodsCont :: Possible values are either "pods" or "containers"
	 * @return
	 */
	@Override
	public long getRemK8sObjectPods(int envId, String objPodsCont) {
		Session session = sessionFactory.openSession();
		DetachedCriteria subMaxDate = DetachedCriteria.forClass(K8sObjectPodsEntity.class);
		subMaxDate.setProjection(Projections.max("statusDate"));
		Criteria crtCurrentCont = session.createCriteria(K8sObjectPodsEntity.class, "objPods");
		crtCurrentCont.createCriteria("objPods.environment", "environment");
		crtCurrentCont.add(Property.forName("statusDate").eq(subMaxDate));
		if(envId != 0){
			crtCurrentCont.add(Restrictions.eq("environment.environmentId", envId));
		}
		crtCurrentCont.add(Restrictions.eq("environment.envLock", 0));
		crtCurrentCont.setProjection(Projections.sum(objPodsCont));
		long currentNumOfCont = (long) (crtCurrentCont.uniqueResult() == null ? (long)0 : crtCurrentCont.uniqueResult());
		session.close();
		return currentNumOfCont;
	}
}
