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
package com.tmobile.kardio.surveiller.main;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

import com.tmobile.kardio.surveiller.apidashboard.APIDashboardTask;
import com.tmobile.kardio.surveiller.counters.CounterDataLoader;
import com.tmobile.kardio.surveiller.db.config.HibernateConfig;
import com.tmobile.kardio.surveiller.kubernetes.KubernetesBackUpTask;
import com.tmobile.kardio.surveiller.marathon.MarathonBackUpTask;
import com.tmobile.kardio.surveiller.purgedata.PurgeOldData;
import com.tmobile.kardio.surveiller.tps.TPSDataLoadTask;
import com.tmobile.kardio.surveiller.tps.TPSLookupTask;

import mockit.Mock;
import mockit.MockUp;


public class SurveillerTest {
	
	private void mockHibernate() {
		new MockUp<HibernateConfig>() {
			@Mock
			public void closeSessionFactory() {
			}
		};
	}
	
	@Before
	public void setup() {
		mockHibernate();
	}

	@Test
	public void testMarathonBackup() throws Exception {

		new MockUp<MarathonBackUpTask>() {
			@Mock
			public void doMarathonBackUp(){
			}
		};
		
		Surveiller.main(new String[] {"DOMARATHONBACKUP"});
	}

	@Test
	public void testCounterDataLoader() throws Exception {

		new MockUp<CounterDataLoader>() {
			@Mock
			public void doDataLoad() {
			}
		};
		
		Surveiller.main(new String[] {"DOCOUNTERDATALOAD"});
	}

	@Test
	public void testPurgeOldData() throws Exception {

		new MockUp<PurgeOldData>() {
			@Mock
			public void doPurgeOldData() {
			}
		};
		
		Surveiller.main(new String[] {"DOPURGEOLDDATA"});
	}
	
	@Test
	public void testAPIDashboardTask() throws Exception {

		new MockUp<APIDashboardTask>() {
			@Mock
			public void doApiDashboardDataLoad() {
			}
		};
		
		Surveiller.main(new String[] {"DOAPIDASHBOARDTASK"});
	}
	
	@Test
	public void testMesosTPSDataLoad() throws Exception {

		new MockUp<TPSDataLoadTask>() {
			@Mock
			public void doTpsDataLoad(String type) {
			}
		};
		
		Surveiller.main(new String[] {"DOMESOSTPSDATALOAD"});
	}

	@Test
	public void testKubernetesBackUpTask() throws Exception {

		new MockUp<KubernetesBackUpTask>() {
			@Mock
			public void doKubernetesBackUp() {
			}
		};
		
		Surveiller.main(new String[] {"DOKUBERNETESBACKUP"});
	}

	@Test
	public void testK8STPSLookupLoad() throws Exception {

		new MockUp<TPSLookupTask>() {
			@Mock
			public void doTpsLookupLoad() {
			}
		};
		
		Surveiller.main(new String[] {"DOK8STPSLOOKUPLOADTASK"});
	}
	
	@Test
	public void testK8STPSDataLoad() throws Exception {

		new MockUp<TPSDataLoadTask>() {
			@Mock
			public void doTpsDataLoad(String type) {
			}
		};
		
		Surveiller.main(new String[] {"DOK8STPSDATALOAD"});
	}
	
	@Test
	public void testDefault() throws Exception {

		new MockUp<HealthCheck>() {
			@Mock
			public void doSurveiller(){
			}
		};
		
		Surveiller.main(new String[] {"DEFAULT"});
	}
	
	@Test
	public void testEmptyParam() throws Exception {

		new MockUp<HealthCheck>() {
			@Mock
			public void doSurveiller(){
			}
		};
		
		Surveiller.main(new String[] {});
	}
	
	@Test (expected = RuntimeException.class)
	public void testHandleException() throws Exception {

		new MockUp<HealthCheck>() {
			@Mock
			public void doSurveiller(){
				throw new RuntimeException("Surveiller exeception");
			}
		};
		Surveiller.main(new String[] {});
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<Surveiller> constructor = Surveiller.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
	
}
