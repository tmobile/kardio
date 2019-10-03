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

import com.tmobile.kardio.bean.CounterDetails;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.bean.EnvCounters;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.CounterEntity;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Operations on Counter Table Implements CountersDao
 */
@Repository
public class CountersDaoImpl implements CountersDao {

    @Autowired
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.tmobile.kardio.dao.CountersDao#getAllCountersDetails()
     */
    public CounterDetails getAllCountersDetails() {

        List<Counters> listOfCounters = getAllCounters();
        List<EnvCounters> listOfEnvCounters = getAllEnvCounterDetails();
        CounterDetails counterDetails = new CounterDetails();
        counterDetails.setCounter(listOfCounters);
        counterDetails.setEnvCounters(listOfEnvCounters);

        return counterDetails;
    }

    /**
     * Get all the Environment wise Counters parameters information from DB
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<EnvCounters> getAllEnvCounterDetails() {
        Session session = sessionFactory.openSession();
        Criteria counterCriteria = session.createCriteria(CounterEntity.class, "counter");
        counterCriteria.addOrder(Order.asc("counter.position"));
        counterCriteria.createCriteria("counter.envCounter", "ec");
        counterCriteria.createCriteria("ec.environment", "environment");

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("ec.envCounterId"));
        projectionList.add(Projections.property("counter.counterName"));
        projectionList.add(Projections.property("environment.environmentName"));
        projectionList.add(Projections.property("ec.metricTypeId"));
        projectionList.add(Projections.property("ec.parameter1"));
        projectionList.add(Projections.property("ec.parameter2"));
        projectionList.add(Projections.property("ec.platform"));
        counterCriteria.setProjection(projectionList);
        List<Object[]> result1 = counterCriteria.list();

        Criteria counterCriteria1 = session.createCriteria(CounterEntity.class, "counter");
        counterCriteria.addOrder(Order.asc("counter.position"));
        Criteria envCounCriteria1 = counterCriteria1.createCriteria("counter.envCounter", "envCounter");
        envCounCriteria1.add(Restrictions.isNull("environmentId"));

        ProjectionList projectionList1 = Projections.projectionList();
        projectionList1.add(Projections.property("envCounter.envCounterId"));
        projectionList1.add(Projections.property("counter.counterName"));
        projectionList1.add(Projections.property("envCounter.environmentId"));
        projectionList1.add(Projections.property("envCounter.metricTypeId"));
        projectionList1.add(Projections.property("envCounter.parameter1"));
        projectionList1.add(Projections.property("envCounter.parameter2"));
        projectionList1.add(Projections.property("envCounter.platform"));
        counterCriteria1.setProjection(projectionList1);
        List<Object[]> result2 = counterCriteria1.list();
        session.close();

        result1.addAll(result2);
        List unionResult = result1;
        List<EnvCounters> envCounterList = new ArrayList<EnvCounters>();
        for (Object[] counterObj : (List<Object[]>) unionResult) {
            EnvCounters envCounters = new EnvCounters();
            envCounters.setEnvCounterId(Integer.valueOf(String.valueOf(counterObj[0])));
            envCounters.setCounterName(String.valueOf(counterObj[1]));
            if (counterObj[2] != null) {
                envCounters.setEnvName(String.valueOf(counterObj[2]));
            } else {
                envCounters.setEnvName("");
            }
            envCounters.setMetricTypeId(Integer.valueOf(String.valueOf(counterObj[3])));
            envCounters.setParameter1(String.valueOf(counterObj[4]));
            envCounters.setParameter2(String.valueOf(counterObj[5]));
            envCounters.setPlatform(String.valueOf(counterObj[6]));
            envCounterList.add(envCounters);
        }
        return envCounterList;
    }

    /**
     * Get all the Counters information from DB
     */
    @SuppressWarnings("unchecked")
    private List<Counters> getAllCounters() {
        Session session = sessionFactory.openSession();
        Criteria counterCriteria = session.createCriteria(CounterEntity.class, "counter");
        counterCriteria.addOrder(Order.asc("counter.position"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("counter.counterId"));
        projectionList.add(Projections.property("counter.counterName"));
        projectionList.add(Projections.property("counter.position"));
        projectionList.add(Projections.property("counter.delInd"));

        counterCriteria.setProjection(projectionList);
        List<Counters> counterList = new ArrayList<Counters>();
        for (Object[] counterObj : (List<Object[]>) counterCriteria.list()) {
            Counters counter = new Counters();
            counter.setCounterId(Integer.parseInt(String.valueOf(counterObj[0])));
            counter.setCounterName(String.valueOf(counterObj[1]));
            counter.setPosition(Integer.parseInt(String.valueOf(counterObj[2])));
            counter.setDelInd(Integer.parseInt(String.valueOf(counterObj[3])));
            counterList.add(counter);
        }
        session.close();
        return counterList;
    }

    /**
     * Update the Counters details in DB
     */
    public void editCounters(final List<Counters> listOfCounters) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        for (Counters counter : listOfCounters) {
            if (counter.getCounterName() == null || counter.getCounterName().trim().isEmpty()) {
                throw new IllegalArgumentException("Counter Name cannot be Null/Blank");
            }
            if (counter.getPosition() <= 0 || counter.getPosition() > listOfCounters.size()) {
                throw new IllegalArgumentException("Counters Position is not valid");
            }

            Query query = session.createQuery(HQLConstants.QUERY_UPDATE_COUNTERS_DETAILS).setString("counterName", counter.getCounterName())
                    .setInteger("position", counter.getPosition()).setInteger("delInd", counter.getDelInd())
                    .setInteger("counterId", counter.getCounterId());
            query.executeUpdate();
        }
        tx.commit();
        session.close();
    }

    /**
     * Update the Counters details to DB
     */
    public void editEnvCounterDetails(EnvCounters envCounters) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.QUERY_UPDATE_ENV_COUNTERS_DETAILS).setString("param1", envCounters.getParameter1())
                .setString("param2", envCounters.getParameter2()).setInteger("envCountId", envCounters.getEnvCounterId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

}
