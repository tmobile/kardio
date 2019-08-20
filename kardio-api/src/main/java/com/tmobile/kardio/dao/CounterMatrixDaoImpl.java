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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.db.entity.CounterEntity;
import com.tmobile.kardio.db.entity.CounterMetricEntity;
import com.tmobile.kardio.db.entity.EnvCounterEntity;

/**
 * Data access operations on Counters Matrix
 * Implements CounterMatrixDao
 * Counters Matrix Features
 */
@Repository
public class CounterMatrixDaoImpl implements CounterMatrixDao {

	@Autowired
	private EnvironmentDao environmentDao;

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final int HOURS_TO_ADD = -24;

	/**
	 * Get All the global Counters for the given platform
	 * @param platform
	 * @return
	 */
	@Override
	public List<Counters> getCounters(String platform) {
		final Map<Integer, List<Float>> metrixMap = getMetrixTrend(platform);
		Session session = sessionFactory.openSession();
		List<Counters> counterList = getCounterList(metrixMap, createCounterCriteria(0, session, platform), platform);
		session.close();
		return counterList;
	}


	/**
	 * Function to get a list of counters from the created counterCriteria
	 * @param metrixMap
	 * @param counterCriteria
	 * @param platform
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Counters> getCounterList(final Map<Integer, List<Float>> metrixMap, Criteria counterCriteria, String platform) {
		List<Counters> counterList = new ArrayList<Counters>();
		for (Object[] counterObj : (List<Object[]>) counterCriteria.list()) {
			Counters counter = new Counters();
			counter.setCounterName(String.valueOf(counterObj[0]));
			counter.setMetricDate(String.valueOf(counterObj[1]));
			float metricVal = Float.parseFloat(String.valueOf(counterObj[2]));
			/*Checking the Up time: And making sure the value is 100 or less than 100*/
			if(counter.getCounterName().equalsIgnoreCase("Up Time In %")){
				metricVal = metricVal > 100 ? metricVal/2 : metricVal;
			}
			counter.setMetricVal(metricVal);
			counter.setPosition(Integer.parseInt(String.valueOf(counterObj[3])));
			if (metrixMap != null) {
				counter.setTrend(metrixMap.get(Integer.valueOf(String.valueOf(counterObj[4]))));
			}
			counterList.add(counter);
		}
		return counterList;
	}

	/**
	 * Get the Metrix trend - For ploating graph.
	 * 
	 * @return
	 */
	public Map<Integer, List<Float>> getMetrixTrend(String platform) {
		Session session = sessionFactory.openSession();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -24);
		Map<Integer, List<Float>> mapOfMertix = createMatrixTrendMap(createCriteriaMatrixTrend(0, session, platform),platform, 0);
		session.close();
		return mapOfMertix;

	}


	/**
	 * Function to get a map of matrix trends from the counterCriteria
	 * @param counterCriteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, List<Float>> createMatrixTrendMap(Criteria counterCriteria, String platform, int envId) {
		Map<Integer, List<Float>> mapOfMertix = new HashMap<Integer, List<Float>>();
		Map<Integer, Integer> envCounterMap = null;
		if((platform == null || platform.equals("All")) && envId !=0){
			envCounterMap = getCounterEnvCount();
		}
		for (Object[] counterObj : (List<Object[]>) counterCriteria.list()) {
			List<Float> metricList = mapOfMertix.get(Integer.valueOf(String.valueOf(counterObj[0])));
			if(platform == null || platform.equals("All")){
					if( (Integer.valueOf(String.valueOf(counterObj[2]))==1 
							&& envCounterMap.get(Integer.valueOf(String.valueOf(counterObj[3])))==2)) {
					continue;
			}
			}

			if (metricList == null) {
				metricList = new ArrayList<Float>();
			}
			int counterId = Integer.valueOf(String.valueOf(counterObj[0]));
			float metricVal;
			metricVal = Float.valueOf(String.valueOf(counterObj[1]));
			if(counterId == 5){
				metricVal = metricVal > 100 ? metricVal/2 : metricVal ;
			}
			metricList.add(metricVal);
			mapOfMertix.put(counterId, metricList);
		}
		return mapOfMertix;
	}

	 /**
     * Get list of all counter in the given platform & environment
     * 
     * @param environment
     * @param platform
     * @return List<Counters>
     */
	@Override
	public List<Counters> getEnvironmentCounters(String environment, String platform) {
		final int envId = environmentDao.getEnironmentIdFromName(environment);
		final Map<Integer, List<Float>> metrixMap = getEnvironmentMetrixTrend(envId, platform);
		Session session = sessionFactory.openSession();
		List<Counters> counterList = getCounterList(metrixMap, createCounterCriteria(envId, session, platform), platform);
		session.close();
		return counterList;
	}

	/**
	 * Function to create criteria for CounterEntity class
	 * @param envId
	 * @param session
	 * @param platform
	 * @return Criteria
	 */
	private Criteria createCounterCriteria(final int envId, Session session, String platform) {
		DetachedCriteria counterSubquery = DetachedCriteria.forClass(CounterMetricEntity.class, "cm")
				.setProjection(Property.forName("metricDate").max());
		Criteria counterCriteria = session.createCriteria(CounterEntity.class, "counter");
		counterCriteria.addOrder(Order.asc("counter.position"));
		Criteria envCounCriteria = counterCriteria.createCriteria("counter.envCounter", "ec");
		if(platform != null && !platform.equalsIgnoreCase("All")){
			envCounCriteria.add(Restrictions.eq("ec.platform", platform));
		}
		Criteria metricCriteria = envCounCriteria.createCriteria("ec.countMetric", "counterMetric");
		counterSubquery.add(Property.forName("ec.envCounterId").eqProperty("cm.envCounterId"));
		metricCriteria.add(Subqueries.propertyEq("counterMetric.metricDate", counterSubquery));
		//metricCriteria.add(Restrictions.le("counterMetric.metricDate", new Date()));
		counterCriteria.add(Restrictions.eq("counter.delInd", 0));
		if(envId != 0) {
			envCounCriteria.add(Restrictions.eq("environmentId", envId));
		} else {
			envCounCriteria.add(Restrictions.isNull("environmentId"));
		}
		counterCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.max("counter.counterName"));
		projectionList.add(Projections.max("counterMetric.metricDate"));
		projectionList.add(Projections.sum("counterMetric.metricVal"));
		projectionList.add(Projections.max("counter.position"));
		projectionList.add(Projections.property("counter.counterId"));
		projectionList.add(Projections.groupProperty("counter.counterId"));
		counterCriteria.setProjection(projectionList);
		
		return counterCriteria;
	}

	/**
	 * Get Environment Metrix Trend with environment.
	 * 
	 * @param envId
	 * @param platform
	 * @return
	 */
	private Map<Integer, List<Float>> getEnvironmentMetrixTrend(final int envId, String platform) {
		Session session = sessionFactory.openSession();
		Map<Integer, List<Float>> mapOfMertix = createMatrixTrendMap(createCriteriaMatrixTrend(envId, session, platform), platform, envId);
		session.close();
		return mapOfMertix;
	}


	private Criteria createCriteriaMatrixTrend(final int envId, Session session, String platform) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, HOURS_TO_ADD);
		Criteria counterCriteria = session.createCriteria(CounterEntity.class, "counter");
		counterCriteria.addOrder(Order.asc("counter.position"));
		Criteria envCounCriteria = counterCriteria.createCriteria("counter.envCounter", "ec");
		if(platform != null && !platform.equalsIgnoreCase("All")){
			envCounCriteria.add(Restrictions.eq("ec.platform", platform));
		}
		Criteria metricCriteria = envCounCriteria.createCriteria("ec.countMetric", "counterMetric");
		counterCriteria.add(Restrictions.eq("counter.delInd", 0));
		if(envId != 0) {
			Criteria environmentCriteria = envCounCriteria.createCriteria("ec.environment", "environment");
			environmentCriteria.add(Restrictions.eq("environment.environmentId", envId));
		} else {
			envCounCriteria.add(Restrictions.isNull("environmentId"));
		}		
		metricCriteria.add(Restrictions.gt("counterMetric.metricDate", c.getTime()));
		metricCriteria.addOrder(Order.asc("counterMetric.metricDate"));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("counter.counterId"));
		projectionList.add(Projections.sum("counterMetric.metricVal"));
		projectionList.add(Projections.count("counter.counterId"));
		projectionList.add(Projections.max("ec.environment.environmentId"));
		projectionList.add(Projections.groupProperty("counter.counterId"));
		projectionList.add(Projections.groupProperty("counterMetric.metricDate"));
		counterCriteria.setProjection(projectionList);
		return counterCriteria;
	}
	
	/**
	 * Get Environment Metrix Trend with environment.
	 * 
	 * @param envId
	 * @return
	 */
	private Map<Integer, Integer> getCounterEnvCount() {
		Session session = sessionFactory.openSession();
		Map<Integer, Integer> mapOfMertix = new HashMap<Integer, Integer>();
		Criteria counterCriteria = session.createCriteria(EnvCounterEntity.class, "envCounter");
		counterCriteria.add(Restrictions.isNotNull("environmentId"));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("envCounter.environmentId"));
		projectionList.add(Projections.count("envCounter.environmentId"));
		projectionList.add(Projections.groupProperty("envCounter.counterId"));
		projectionList.add(Projections.groupProperty("envCounter.environmentId"));
		counterCriteria.setProjection(projectionList);
		for (Object[] counterObj : (List<Object[]>) counterCriteria.list()) {
			mapOfMertix.put(Integer.valueOf(String.valueOf(counterObj[0])), Integer.valueOf(String.valueOf(counterObj[1])));
		}
		session.close();
		return mapOfMertix;
	}

}
