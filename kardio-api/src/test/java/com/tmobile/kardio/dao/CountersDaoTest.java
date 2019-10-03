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

import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.CounterDetails;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.bean.EnvCounters;
import com.tmobile.kardio.db.entity.CounterEntity;
import com.tmobile.kardio.db.entity.CounterMetricEntity;
import com.tmobile.kardio.db.entity.EnvCounterEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CountersDaoTest extends AbstractDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EnvironmentDao envDao;


    @Autowired
    private CountersDao countersDao;

    @Autowired
    private TestDaoService daoService;

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllCountersDetails() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        CounterEntity ce = daoService.createCounterEntity(session);

        EnvCounterEntity ece = daoService.createEnvCounterEntity(session, ce);

        CounterMetricEntity cme = daoService.createCounterMetricEntity(session, ece);

        Set<CounterMetricEntity> cmes = new HashSet<CounterMetricEntity>();
        cmes.add(cme);
        ece.setCountMetric(cmes);
        session.save(ece);

        Set<EnvCounterEntity> eces = new HashSet<EnvCounterEntity>();
        eces.add(ece);
        ce.setEnvCounter(eces);
        session.save(ce);

        tx.commit();
        session.close();

        CounterDetails result = countersDao.getAllCountersDetails();
        List<Counters> counters = result.getCounter();
        List<EnvCounters> envCounters = result.getEnvCounters();

        Assert.assertEquals("Counter size does not match", TestDaoService.counterID, counters.size());
        Assert.assertEquals("EnvCounter size does not match", TestDaoService.envCounterID, envCounters.size());

        Counters counter = counters.get(TestDaoService.counterID - 1);
        EnvCounters envCounter = envCounters.get(TestDaoService.envCounterID - 1);

        Assert.assertEquals("Counter name does not match", ce.getCounterName(), counter.getCounterName());
//        Assert.assertEquals("Description does not match", ce.getCounterDesc(), counter.getCounterDesc()); TODO: Description not returned.
        Assert.assertEquals("DelInd does not match", ce.getDelInd(), counter.getDelInd());
        Assert.assertEquals("Position does not match", ce.getPosition(), counter.getPosition());
//        Assert.assertEquals("Metric date does not match", cme.getMetricDate(), counter.getMetricDate());
//        Assert.assertEquals("Metric value does not match", cme.getMetricVal(), counter.getMetricVal().floatValue()); TODO: Metric value not returned       
        Assert.assertEquals("Metric type does not match", ece.getMetricTypeId(), envCounter.getMetricTypeId());
        Assert.assertEquals("Parameter 1 does not match", ece.getParameter1(), envCounter.getParameter1());
        Assert.assertEquals("Parameter 2 does not match", ece.getParameter2(), envCounter.getParameter2());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testEditCounters() {
        List<Counters> counters = new ArrayList<Counters>();
        Counters counter = getCounter();
        counters.add(counter);

        countersDao.editCounters(counters);
    }

    private Counters getCounter() {
        Counters counter = new Counters();
        counter.setCounterId(1);
        counter.setCounterName("new_counter_name");
        counter.setDelInd(1);
        counter.setPosition(1);
        return counter;
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testEditCounters_NameEmpty() {
        List<Counters> counters = new ArrayList<Counters>();
        Counters counter = getCounter();
        counter.setCounterName("");
        counters.add(counter);

        countersDao.editCounters(counters);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testEditCounters_PositionZero() {
        List<Counters> counters = new ArrayList<Counters>();
        Counters counter = getCounter();
        counter.setPosition(0);
        counters.add(counter);

        countersDao.editCounters(counters);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testEditEnvCounters() {
        EnvCounters envCounter = new EnvCounters();
        envCounter.setEnvCounterId(1);
        envCounter.setParameter1("new_param_1");
        countersDao.editEnvCounterDetails(envCounter);
    }
}
