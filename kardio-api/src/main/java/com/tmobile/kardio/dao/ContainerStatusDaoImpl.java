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

import com.tmobile.kardio.bean.ContainerStatus;
import com.tmobile.kardio.db.entity.ContainerStatusEntity;
import com.tmobile.kardio.util.DaoUtil;

/**
 * Dao Class to get the container stats.
 *
 */
@Repository
public class ContainerStatusDaoImpl implements ContainerStatusDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/*
	 * Get APP containers, based on the input parameters date, environment & component ids
	 */
	@Override
	public List<ContainerStatusEntity> getEnvContainers(String startDate, String endDate, int envId, String componentIdsStrg, boolean isParentComponents) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);
		
		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);
		Session session = sessionFactory.openSession();
		Criteria containerCriteria = session.createCriteria(ContainerStatusEntity.class, "contSts");
		containerCriteria.createCriteria("contSts.component", "component");
		containerCriteria.add(Restrictions.ge("contSts.statsDate", sDate ));
		containerCriteria.add(Restrictions.le("contSts.statsDate", eDate ));
		DaoUtil.addEnvironmentToCriteria(envId, isParentComponents, comIdList, containerCriteria);

		
		@SuppressWarnings("unchecked")
		List<ContainerStatusEntity> listContEntity = containerCriteria.list();
		session.close();
		return listContEntity;
	}
	
	
	@Override
	public List<ContainerStatus> getAllContainersOfParent(String startDate, String endDate, int envId, String componentIdsStrg) throws ParseException {
		
		final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);
		
		Date sDate = sdf1.parse(startDate);
		Date eDate = sdf1.parse(endDate);
		Session session = sessionFactory.openSession();
		Criteria containerCriteria = session.createCriteria(ContainerStatusEntity.class, "contSts");
		containerCriteria.createCriteria("contSts.component", "component");
		containerCriteria.createCriteria("contSts.environment", "environment");
		containerCriteria.add(Restrictions.ge("contSts.statsDate", sDate ));
		containerCriteria.add(Restrictions.le("contSts.statsDate", eDate ));
		if(envId != 0){
			containerCriteria.add(Restrictions.eq("environment.environmentId", envId));
		}
		containerCriteria.add(Restrictions.eq("environment.envLock", 0));
		if(comIdList != null && comIdList.size() != 0){
			containerCriteria.add(Restrictions.in("component.parentComponent.componentId", comIdList));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("component.parentComponent.componentId"));
		projectionList.add(Projections.property("contSts.statsDate"));
		projectionList.add(Projections.sum("contSts.totalContainer"));
	    projectionList.add(Projections.groupProperty("component.parentComponent.componentId"));
		projectionList.add(Projections.groupProperty("contSts.statsDate"));
		containerCriteria.setProjection(projectionList);
	    @SuppressWarnings("unchecked")
		List<Object[]> conList = containerCriteria.list();
	    List<ContainerStatus> contStatusList = DaoUtil.createContainerStatuses(conList);
        session.close();
        return contStatusList;    
	}

	@Override
	public long getCurrentNumberOfContainsers(int envId,String componentIdsStrg, boolean isParentComponents) throws ParseException {
		
		List<Integer> comIdList = DaoUtil.convertCSVToList(componentIdsStrg);

		Session session = sessionFactory.openSession();
		DetachedCriteria subMaxDate = DetachedCriteria.forClass(ContainerStatusEntity.class);
		subMaxDate.setProjection(Projections.max("statsDate"));
		Criteria crtCurrentCont = session.createCriteria(ContainerStatusEntity.class, "contSts");
		crtCurrentCont.createCriteria("contSts.component", "component");
		crtCurrentCont.add(Property.forName("statsDate").eq(subMaxDate));
		
		DaoUtil.addEnvironmentToCriteria(envId, isParentComponents, comIdList, crtCurrentCont);

		crtCurrentCont.setProjection(Projections.sum("totalContainer"));
		long currentNumOfCont = (long) (crtCurrentCont.uniqueResult() == null ? (long)0 : crtCurrentCont.uniqueResult());
		session.close();
		return currentNumOfCont;
		
	}
	
}
