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
import com.tmobile.kardio.TestDataProvider;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.db.entity.CounterEntity;
import com.tmobile.kardio.db.entity.CounterMetricEntity;
import com.tmobile.kardio.db.entity.EnvCounterEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterMatrixDaoTest extends AbstractDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CounterMatrixDao counterMatrixDao;

    @Autowired
    private TestDaoService daoService;

    @Autowired
    private EnvironmentDao envDao;

    //	@Test
    @Transactional
    @Rollback(true)
    public void testGetCounters() {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        CounterEntity ce = daoService.createCounterEntity(session);

        EnvCounterEntity ece = daoService.createEnvCounterEntity(session, ce);

        CounterMetricEntity cme = daoService.createCounterMetricEntity(session, ece);

        Set<CounterMetricEntity> cmes = new HashSet<CounterMetricEntity>();
        cmes.add(cme);
        ece.setCountMetric(cmes);
        ece.setPlatform(TestDataProvider.getPlatform());
        session.save(ece);

        Set<EnvCounterEntity> eces = new HashSet<EnvCounterEntity>();
        eces.add(ece);
        ce.setEnvCounter(eces);
        session.save(ce);

        tx.commit();
        session.close();

        List<Counters> counters = counterMatrixDao.getCounters(TestDataProvider.getPlatform());
        Assert.assertEquals("Counter size does not match", TestDaoService.counterID - 1, counters.size());
        Counters counter = counters.get(0);

        Assert.assertEquals("Counter name does not match", ce.getCounterName(), counter.getCounterName());
//        Assert.assertEquals("Description does not match", ce.getCounterDesc(), counter.getCounterDesc()); TODO: Description not returned.
        Assert.assertEquals("Position does not match", ce.getPosition(), counter.getPosition());
        Assert.assertEquals("Metric value does not match", cme.getMetricVal(), counter.getMetricVal().floatValue(), 0.0);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetEnvCounters() {
        String envName = "getenvcounter";
        daoService.createEnvironment(envName, 0);

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        CounterEntity ce = daoService.createCounterEntity(session);

        EnvCounterEntity ece = daoService.createEnvCounterEntity(session, ce);

        CounterMetricEntity cme = daoService.createCounterMetricEntity(session, ece);

        Set<CounterMetricEntity> cmes = new HashSet<CounterMetricEntity>();
        cmes.add(cme);
        ece.setCountMetric(cmes);
        ece.setEnvironment(envDao.getEnvironmentFromName(envName));
        session.save(ece);

        Set<EnvCounterEntity> eces = new HashSet<EnvCounterEntity>();
        eces.add(ece);
        ce.setEnvCounter(eces);
        session.save(ce);

        tx.commit();
        session.close();

        List<Counters> counters = counterMatrixDao.getEnvironmentCounters(envName, TestDataProvider.getPlatform());
        //  Assert.assertEquals("Counter size does not match",  TestDaoService.counterID, counters.size());
        Counters counter = counters.get(0);

        Assert.assertEquals("Counter name does not match", ce.getCounterName(), counter.getCounterName());
//        Assert.assertEquals("Description does not match", ce.getCounterDesc(), counter.getCounterDesc()); TODO: Description not returned.
        Assert.assertEquals("Position does not match", ce.getPosition(), counter.getPosition());
        Assert.assertEquals("Metric value does not match", cme.getMetricVal(), counter.getMetricVal().floatValue(), 0.0);
    }

}
