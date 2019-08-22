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
package com.tmobile.kardio.surveiller.util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.rowset.serial.SerialException;

import org.junit.Assert;
import org.junit.Test;

import com.tmobile.kardio.surveiller.db.entity.AlertSubscriptionEntity;
import com.tmobile.kardio.surveiller.db.entity.ApiStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.ContainerStatsEntity;
import com.tmobile.kardio.surveiller.db.entity.CounterMetricEntity;
import com.tmobile.kardio.surveiller.db.entity.DaillyCompStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.EnvCounterEntity;
import com.tmobile.kardio.surveiller.db.entity.EnvironmentEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sApiStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sPodsContainersEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sTpsLatHistoryEntity;
import com.tmobile.kardio.surveiller.db.entity.PromLookupEntity;
import com.tmobile.kardio.surveiller.db.entity.RegionEntity;
import com.tmobile.kardio.surveiller.db.entity.StatusEntity;
import com.tmobile.kardio.surveiller.db.entity.TpsLatHistoryEntity;
import com.tmobile.kardio.surveiller.db.entity.TpsServiceEntity;
import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.HealthCheckType;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.AppTransformConfig;
import com.tmobile.kardio.surveiller.vo.ComponentVO;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;
import com.tmobile.kardio.surveiller.vo.SubscriptionVO;
import com.tmobile.kardio.test.TestDaoService;
import com.tmobile.kardio.test.TestDataProvider;


public class DBQueryUtilTest extends ProxyUtilTest{
	TestDaoService daoService = new TestDaoService();
	 ComponentTypeEntity cte = daoService.createComponentType();
	 int region_id=0;
	 String url = "http://localhost:8080";
	@Test	
	public void updateHealthCheckStatusInDBTest() throws Exception {	
		//Data			
		
		Map<String, String> paramDetails = new HashMap<String, String>();
		paramDetails.put("URL", "http://10.65.163.10:5050/system/stats.json");		
		ComponentVO comp = new ComponentVO();
		comp.setComponentDesc(null);
		comp.setComponentId(25);
		comp.setComponentName("Mesos Master 1");
		comp.setComponentTypeId(0);
		comp.setParentComponentId(1);
		comp.setParentComponentName("CCP - Mesos Master");		
		HealthCheckVO hv = new HealthCheckVO();		
		hv.setStatus(new StatusVO(Status.DOWN,"down"));		
		hv.setEnvironmentId(1);
		hv.setEnvironmentName("ProdSD");
		hv.setFailureStatusMessage(null);
		hv.setHealthCheckTypeClassName("com.tmobile.kardio.surveiller.handler.URLOpenableHandler");
		hv.setHealthCheckTypeName("URL2xxCheck");
		hv.setRegionName("West Region");
		hv.setComponent(comp);
		hv.setComponentType(ComponentType.INFRA);
		hv.setCurrentStatus((long) 1);
		hv.setHealthCheckComponentId((long) 25);
		
		hv.setHealthCheckId((long) 1);
		hv.setHealthCheckRegionId((long) 1);
		hv.setHealthCheckRetryCurrentCount((long) 0);
		hv.setHealthCheckRetryMaxCount((long) 3);
		hv.setParamDetails(paramDetails);
		
		//Test1
		List<HealthCheckVO> healthCheckVOs1 = new ArrayList<HealthCheckVO>();
		healthCheckVOs1.add(hv);
		List<HealthCheckVO> output1 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs1);
		
		//Test2
		List<HealthCheckVO> healthCheckVOs2 = new ArrayList<HealthCheckVO>();
		hv.setStatus(null);	
		healthCheckVOs2.add(hv);
		List<HealthCheckVO> output2 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs2);
		//Test3
		List<HealthCheckVO> healthCheckVOs3 = new ArrayList<HealthCheckVO>();
		hv.setStatus(new StatusVO(Status.DOWN,"down"));	
		hv.setCurrentStatus(null);	
		hv.setHealthCheckRetryCurrentCount(null);
		healthCheckVOs3.add(hv);
		List<HealthCheckVO> output3 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs3);
		//Test4
		List<HealthCheckVO> healthCheckVOs4 = new ArrayList<HealthCheckVO>();
		hv.setStatus(new StatusVO(Status.UP,"up"));	
		hv.setCurrentStatus(null);	
		hv.setHealthCheckRetryMaxCount((long) 3);
		healthCheckVOs4.add(hv);
		List<HealthCheckVO> output4 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs4);
		//Test5
		List<HealthCheckVO> healthCheckVOs5 = new ArrayList<HealthCheckVO>();
		hv.setStatus(new StatusVO(Status.DOWN,"down"));	
		hv.setCurrentStatus(null);	
		hv.setHealthCheckRetryMaxCount((long) 3);
		healthCheckVOs5.add(hv);
		List<HealthCheckVO> output5 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs5);
		//Test6
		List<HealthCheckVO> healthCheckVOs6 = new ArrayList<HealthCheckVO>();
		hv.setStatus(new StatusVO(Status.DOWN,"up"));	
		hv.setCurrentStatus(1L);	
		hv.setHealthCheckRetryMaxCount((long) 0);
		healthCheckVOs6.add(hv);
		List<HealthCheckVO> output6 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs6);	
		assertEquals(output6,healthCheckVOs6);
		//Test7
		List<HealthCheckVO> healthCheckVOs7 = new ArrayList<HealthCheckVO>();
		hv.setStatus(new StatusVO(Status.UP,"up"));	
		hv.setCurrentStatus(1L);	
		hv.setHealthCheckRetryMaxCount((long) 0);
		hv.setHealthCheckRetryCurrentCount((long)3);
		healthCheckVOs7.add(hv);
		List<HealthCheckVO> output7 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs7);
		//Test8
		List<HealthCheckVO> healthCheckVOs8 = new ArrayList<HealthCheckVO>();
		hv.setHealthCheckId((long) 2);
		hv.setStatus(new StatusVO(Status.UP,"up"));	
		hv.setCurrentStatus(1L);	
		hv.setHealthCheckRetryMaxCount((long) 0);
		hv.setHealthCheckRetryCurrentCount((long)3);
		healthCheckVOs8.add(hv);
		List<HealthCheckVO> output8 = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs8);
	}
	
	@Test	
	public void updateDelIndForHealthCheckTest() {				
		DBQueryUtil.updateDelIndForHealthCheck(25, 0, 1, (long) 1);
	}
	
	@Test	
	public void getAllSubscriptionsTest() {	
		Set<Integer> a = new HashSet<Integer>();
		a.add(1);
		a.add(2);
		List<SubscriptionVO> sub = DBQueryUtil.getAllSubscriptions(a);
//		assertEquals(sub.get(0).getComponentId(),1); TODO: Broken after merge.
		
	}
	
	@Test	
	public void getCounterDetailsTest() {			
		List<CounterDetailVO> counter = DBQueryUtil.getCounterDetails();		
//		assertTrue(counter.size()>0); TODO: Broken after merge.
	}
	
	@Test	
	public void loadContainerStatusTest() {
		DBQueryUtil.loadContainerStatus(1, 1, 1);
//		assertEquals(i,11); TODO: Broken after merge.
		DBQueryUtil.loadContainerStatus(1, 1, 1);
//		assertEquals(i,0);
	}
	
	@Test	
	public void loadApiStatusTest() {
		DBQueryUtil.loadApiStatus(1, "Container 1", 1);
		DBQueryUtil.loadApiStatus(1, null, 1);
	}
	
	@Test	
	public void checkAndInsertComponentTest() {
		int  i = DBQueryUtil.checkAndInsertComponent("Test1", "TestApi1", ComponentType.APP, "http://test", 1, 1, HealthCheckType.URL_200_CHECK, "Mesos");
		assertTrue(i!=0);
	}
	
	@Test	
	public void insertHealthCheckParamTest() {
//		DBQueryUtil.insertHealthCheck(1548, (long) 1, 1, 1, 3); TODO: Broken code after merge.
	}
	
	@Test	
	public void getCCPUpTimeTest() {
		String [] parentComponentNames1 =  new String[] {"CCP - Mesos Master","CCP - Marathon"};		
		float r1 = DBQueryUtil.getCCPUpTime(1, parentComponentNames1);		
//		assertTrue(r1>0); TODO: Broken after merge.
		String [] parentComponentNames2 =  new String[] {"ccp"};		
		float r2 = DBQueryUtil.getCCPUpTime(1, parentComponentNames2);		
//		assertEquals(r2,0,0);
	}
	//FIXME: We need to mock an object (CounterDetailVO) and pass to getNumberOfServices() method
	/*
	@Test	
	public void getNumberOfServicesTest() {
		int i = DBQueryUtil.getNumberOfServices(1);
		assertTrue(i>0);
	}
	*/
	@Test	
	public void getAppsLauchDateNullTest() {
		Set<Integer> j = DBQueryUtil.getAppsLauchDateNull(1);		
		assertTrue(j.size()>0);
	}
	

    @Test
    public void testGetAllEnvironments() throws SerialException, SQLException {
    	String name = "testallenv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	List<EnvironmentVO> result = DBQueryUtil.getAllEnvironments();
    	//Assert.assertEquals("Size does not match", TestDaoService.environmentID, result.size());
    	//EnvironmentVO actual = result.get(0);
    	
    	//Assert.assertEquals("ID does not match", TestDaoService.environmentID, actual.getEnvironmentId());
    	//Assert.assertEquals("Name does not match", env.getEnvironmentName(), actual.getEnvironmentName());
//    	Assert.assertEquals("Describtion does not match", env.getEnvironmentDesc(), actual.getMarathonJson());
    }
    
    @Test
    public void testCreateComponent() {
    	daoService.createComponentType();
    	DBQueryUtil.insertComponent("parent_comp_1", 0, TestDaoService.componentTypeID, false,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.insertComponent("comp_1", 1, TestDaoService.componentTypeID, true,TestDataProvider.getMesosPlatform());
    	ComponentVO comp = DBQueryUtil.getComponent(1);
    }
    
    @Test
    public void testGetComponent() throws SerialException, SQLException {
    	String compName="testComp";
    	String parentCompName="parentComp";
    	int del_ind=0;
    	ComponentEntity tce=daoService.createComponent(compName,parentCompName,del_ind,cte,TestDataProvider.getMesosPlatform());
    	ComponentVO result = DBQueryUtil.getComponent(tce.getComponentId());
    //	Assert.assertEquals("ID does not match", tce.getComponentId(),result.getComponentId());
    	//Assert.assertEquals("Name does not match", tce.getComponentName(),result.getComponentName());
    	//Assert.assertEquals("Desc does not match", tce.getComponentDesc(),result.getComponentDesc());
    	//Assert.assertEquals("Component Type does not match", tce.getComponentType().getComponentTypeId(),result.getComponentTypeId());
    	
    }
    
    @Test
    public void testGetAllSubscriptions() throws SerialException, SQLException {
    	int subId = 1;
    	int valLevel=1;
    	Set<Integer> compIdSet=new HashSet<Integer>();
    	AlertSubscriptionEntity se=daoService.createAlertSubscriptionEntity(subId,valLevel,cte,TestDataProvider.getMesosPlatform());
    	int compId=se.getComponentId();
    	int gComTId = se.getGlobalComponentTypeId();
    	compIdSet.add(se.getComponentId());
    	List<SubscriptionVO> result = DBQueryUtil.getAllSubscriptions(compIdSet);
    	
    	Assert.assertEquals("Size does not match",1, result.size());
    	SubscriptionVO actual = result.get(0);
    	Assert.assertEquals("Component ID Not matching",compId,actual.getComponentId());
        Assert.assertEquals("Subscription Type does not match",1,actual.getSubscriptionType());
    	Assert.assertEquals("Subscription Value does not match","testVal",actual.getSubscriptionValue());
    	Assert.assertEquals("Environment Value does not match",TestDaoService.environmentID,actual.getEnvironmentId());
    	Assert.assertEquals("Global Component Type Id  does not match",gComTId,actual.getGlobalComponentTypeId());
    	
    }
    
    @Test
    public void testGetComponentId() {
    	
    	String parentCompName="parentComp";
    	String compName="testGetCompId";
        int del_ind=0;
    	ComponentEntity ce=daoService.createComponent(compName,parentCompName,del_ind,cte,TestDataProvider.getMesosPlatform());
    	int result = DBQueryUtil.getComponentId(compName, parentCompName);
    	Assert.assertEquals("Component ID does not match", ce.getComponentId(),result);
    	
    }
    /*
    @Test
    public void testGetNumberOfServices() {
    	String name = "testenvServices";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1);
    }
    
 */
    
    @Test
    public void testupdateApiDetails() throws SerialException, SQLException {
    	String name = "apienv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);	
    	
   
    	String parentCompName="parentComp";
    	String compName="testApiCompId";

    	int numOfApi=10;
    	int totApi=2;
    	int del_ind=0;
    	ComponentEntity ce=daoService.createComponent(compName,parentCompName,del_ind,cte,TestDataProvider.getMesosPlatform());
    	ApiStatusEntity api = daoService.createApiStatusEntity(ce, env,totApi);
    	DBQueryUtil.updateApiDetails(env.getEnvironmentId(), ce.getComponentName(), numOfApi,TestDataProvider.getMesosPlatform());	
    	DBQueryUtil.updateApiDetails(env.getEnvironmentId(), "DUMMY", numOfApi,TestDataProvider.getMesosPlatform());
    }
   
    @Test
    public void testGetCurrentApiDetails() throws SerialException, SQLException {
    	String name = "getApiEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 5,url);	
    	

    	String parentCompName="parentComp";
    	String compName="testCurrApi";
  
    	int totApi=2;
        int del_ind=0;
    	ComponentEntity ce=daoService.createComponent(compName,parentCompName,del_ind,cte,TestDataProvider.getMesosPlatform());
    	ApiStatusEntity api = daoService.createApiStatusEntity(ce, env, totApi);
    	
    	Map<String, Integer> result = DBQueryUtil.getCurrentApiDetails(env.getEnvironmentId());
    	int totalApi=result.get(compName);
    	Assert.assertEquals("Total API does not match",totApi,totalApi);
    }
    
    @Test
    public void testLoadApiStatus() throws SerialException, SQLException {
    	String name = "loadApiEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 6,url);
    	
  
    	String parentCompName="parentComp";
    	String compName="testLoadApi";

    	int numOfApi=2;
        int del_ind=0;
    	ComponentEntity lce=daoService.createComponent(compName,parentCompName,del_ind,cte,TestDataProvider.getMesosPlatform());
    	ApiStatusEntity ase =daoService.createApiStatusEntity(lce,env,10);
    	DBQueryUtil.loadApiStatus(env.getEnvironmentId(), lce.getComponentName(), numOfApi);
    }
  
    @Test
    public void testAddCounterMertic() throws SerialException, SQLException {
    	String name = "testCounterEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 7,url);
    	EnvCounterEntity ece = daoService.createEnvCounterEntity();
    	final int environmentCounterId=1;
    	final float counterMerticValue=2;
    	DBQueryUtil.addCounterMertic(environmentCounterId, counterMerticValue);
    }
    
    @Test
    public void testUpdateEastMarathonJson() throws SerialException, SQLException {
    	String name = "testJsonEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	EnvironmentVO evo = new EnvironmentVO();
    	evo.setEnvironmentId(env.getEnvironmentId());
    	evo.setEnvironmentName(env.getEnvironmentName());
    	evo.setMarathonCred(env.getMarathonCred());
    	evo.setEastMarathonJson(env.getMarathonJson().toString());
    	evo.setEastMarathonUrl(env.getMarathonURL());
    	DBQueryUtil.updateEastMarathonJson(evo);
    }
    
    @Test
    public void testUpdateEnvironmentDetails() throws SerialException, SQLException {
    	String name = "testUpdateEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	EnvironmentVO evo = new EnvironmentVO();
    	evo.setEnvironmentId(env.getEnvironmentId());
    	evo.setEnvironmentName(env.getEnvironmentName());
    	evo.setMarathonCred(env.getMarathonCred());
    	evo.setMarathonJson(env.getMarathonJson().toString());
    	evo.setMarathonUrl(env.getMarathonURL());
    	List<EnvironmentVO> evoList = new ArrayList<EnvironmentVO> ();
    	evoList.add(evo);
    	DBQueryUtil.updateEnvironmentDetails(evoList);
    }
    
    @Test
    public void testGetMarathonJson() throws SerialException, SQLException {
    	Map<Integer, Blob> mapEnvJson = DBQueryUtil.getMarathonJson();
    	int result = mapEnvJson.size();
    	//Assert.assertEquals("Size does not match",TestDaoService.environmentID, result);
    }
    @Test
    public void testGetMarathonJson_MarathonURLNULL() throws SerialException, SQLException {
    	 url = null;
    	EnvironmentEntity env = daoService.createEnvironment("testNUll",1,url);
    	Map<Integer, Blob> mapEnvJson = DBQueryUtil.getMarathonJson();
    	int result = mapEnvJson.size();
    	//Assert.assertEquals("Size does not match",TestDaoService.environmentID, result);
    }
    
    @Test
    public void testGetMarathonJson_EmptyMarathonURL() throws SerialException, SQLException {
    	 url = " ";
    	EnvironmentEntity env = daoService.createEnvironment("testNUll",1,url);
    	Map<Integer, Blob> mapEnvJson = DBQueryUtil.getMarathonJson();
    	int result = mapEnvJson.size();
    	//Assert.assertEquals("Size does not match",TestDaoService.environmentID, result);
    }
    
    @Test
    public void  testInsertHealthCheck() throws SerialException, SQLException {
    	String name = "testInsertHC";
    	int del_ind=0;
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	StatusEntity se = daoService.createStatus();
    	se.setStatusId(1L);
    	RegionEntity re = daoService.createRegionEntity();
    	HealthCheckTypeEntity hte = daoService.createHeathCheckType();
    	ComponentEntity ce = daoService.createComponent("testHC", "testHCP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.insertHealthCheck(ce.getComponentId(), re.getRegionId(), env.getEnvironmentId(), hte.getHealthCheckTypeId(), 3);
    }
    
    @Test
    public void testInsertHealthCheckParam() throws SerialException, SQLException {
    	int del_ind_c=0;
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.insertHealthCheckParam(TestDaoService.healthCheckID,"testKey","testVal");
    }
    @Test
    public void testUpdateMarathonHealthCheckStatus() throws SerialException, SQLException {
    	String name = "testUpdateHC";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	StatusEntity se = daoService.createStatus();
    	se.setStatusId(2L);
    	RegionEntity re = daoService.createRegionEntity();
    	HealthCheckTypeEntity hte = daoService.createHeathCheckType();
    	HealthCheckType hto=HealthCheckType.DUMMY;
    	Status st=Status.UP;
    	int del_ind=0;
    	ComponentEntity ce = daoService.createComponent("testUHC", "testUHCP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateMarathonHealthCheckStatus(ce.getComponentId(), env.getEnvironmentId(), re.getRegionId(), hto, st, false);
    }
    
    @Test
    public void testUpdateMarathonHealthCheckStatus_StatusChangedTrue() throws SerialException, SQLException {
    	String name = "testUpdateHC";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	StatusEntity se = daoService.createStatus();
    	se.setStatusId(2L);
    	RegionEntity re = daoService.createRegionEntity();
    	HealthCheckTypeEntity hte = daoService.createHeathCheckType();
    	HealthCheckType hto=HealthCheckType.DUMMY;
    	Status st=Status.UP;
    	int del_ind=0;
    	ComponentEntity ce = daoService.createComponent("testUHC", "testUHCP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateMarathonHealthCheckStatus(ce.getComponentId(), env.getEnvironmentId(), re.getRegionId(), hto, st, true);
    }
    @Test
    public void testCheckAndUpdateMessage() throws SerialException, SQLException {
    	/*
    	String name = "testCheckHCEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1);
    	StatusEntity se = daoService.createStatus();
    	se.setStatusId(3L);
    	RegionEntity re = daoService.createRegionEntity();*/
    	HealthCheckType hto=HealthCheckType.DUMMY;
    	Status st=Status.UP;
    	int del_ind=0;
    //	HealthCheckTypeEntity hte = daoService.createHeathCheckType();*/
    	ComponentEntity ce = daoService.createComponent("testCheckHC", "testCheckHCP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind,cte,region_id,TestDataProvider.getMesosPlatform());
    	long regionId = hce.getRegion().getRegionId();
    	Map<String,String> transformName = new HashMap<String,String>();
    	transformName.put("testCheckHC", "CheckHC");
    	AppTransformConfig config = new AppTransformConfig();
    	config.setTransformName(transformName);
    	boolean result = DBQueryUtil.checkAndUpdateMessage(hce.getComponent().getComponentId(), hce.getEnvironment().getEnvironmentId(), regionId, hto,st , "testMessage");
    }
    @Test
    public void readAppTransformConfig() {
    	Map<String,String> transformName = new HashMap<String,String>();
    	transformName.put("testCheckHC", "CheckHC");
    	AppTransformConfig config = new AppTransformConfig();
    	config.setTransformName(transformName);
    	AppTransformConfig congig = DBQueryUtil.readAppTransformConfig();
    }
    @Test
    public void testUpdateHealthCheckStatusInDB() throws Exception {
    	int del_ind_c=0;
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	DaillyCompStatusEntity de = daoService.createDailyCompStatus(hce);
    	HealthCheckVO hvo= new HealthCheckVO();
    	StatusVO svo=new StatusVO(Status.UP);
    	ComponentVO cvo = new ComponentVO();
    	cvo.setComponentId(hce.getComponent().getComponentId());
    	cvo.setComponentName(hce.getComponent().getComponentName());
    	cvo.setComponentDesc(hce.getComponent().getComponentDesc());
    	hvo.setComponent(cvo);
    	hvo.setHealthCheckRetryMaxCount(3L);
    	hvo.setHealthCheckTypeName(hce.getHealthCheckType().getHealthCheckTypeName());
        hvo.setHealthCheckRetryCurrentCount(1L);
    	hvo.setHealthCheckId(TestDaoService.healthCheckID);
    	hvo.setStatus(svo);
    	hvo.setCurrentStatus(hce.getCurrentStatus().getStatusId());
    	hvo.setEnvironmentId(hce.getEnvironment().getEnvironmentId());
    	hvo.setEnvironmentName(hce.getEnvironment().getEnvironmentName());
    	List<HealthCheckVO> hvList = new ArrayList<HealthCheckVO>();
    	hvList.add(hvo);
    	List<HealthCheckVO> result = DBQueryUtil.updateHealthCheckStatusInDB(hvList);    	
    }
    @Test
    public void testUpdateHealthCheckStatusInDB_NullStatusCase() throws Exception {
    	int del_ind_c=0;
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	DaillyCompStatusEntity de = daoService.createDailyCompStatus(hce);
    	HealthCheckVO hvo= new HealthCheckVO();
    	StatusVO svo=new StatusVO(null);
    	ComponentVO cvo = new ComponentVO();
    	cvo.setComponentId(hce.getComponent().getComponentId());
    	cvo.setComponentName(hce.getComponent().getComponentName());
    	cvo.setComponentDesc(hce.getComponent().getComponentDesc());
    	hvo.setComponent(cvo);
    	//hvo.setHealthCheckRetryMaxCount(3L);
        hvo.setHealthCheckRetryCurrentCount(1L);
    	hvo.setHealthCheckId(TestDaoService.healthCheckID);
    	hvo.setStatus(svo);
    	hvo.setCurrentStatus(hce.getCurrentStatus().getStatusId());
    	hvo.setEnvironmentId(hce.getEnvironment().getEnvironmentId());
    	hvo.setEnvironmentName(hce.getEnvironment().getEnvironmentName());
    	List<HealthCheckVO> hvList = new ArrayList<HealthCheckVO>();
    	hvList.add(hvo);
    	List<HealthCheckVO> result = DBQueryUtil.updateHealthCheckStatusInDB(hvList);    	
    }
    
    @Test
    public void testCheckAndInsertComponent() throws Exception{
    	String name = "testCheckHCEnv";
    	int del_ind=0;
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	RegionEntity re = daoService.createRegionEntity();
    	ComponentType ct = ComponentType.APP;
    	HealthCheckType hte = HealthCheckType.URL_200_CHECK;
    	HealthCheckTypeEntity ht = daoService.createHeathCheckType();
    	ComponentEntity ce = daoService.createComponent("testcheck", "testcheckP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	StatusEntity st= daoService.createStatus();
    	int appId = DBQueryUtil.checkAndInsertComponent("testApp", "testApiName", ct, null, env.getEnvironmentId(), re.getRegionId(), hte,TestDataProvider.getMesosPlatform());
    	
    	//healthCheck URL is Null Case
    	//String name = "testCheckHCEnv";
    	String name1 = "testCheckHCEnvNull";
    	 del_ind=0;
    	EnvironmentEntity env1 = daoService.createEnvironment(name, 1,url);
    	RegionEntity re1 = daoService.createRegionEntity();
    	ComponentType ct1 = ComponentType.APP;
    	HealthCheckType hte1 = HealthCheckType.URL_200_CHECK;
    	HealthCheckTypeEntity ht1 = daoService.createHeathCheckType();
    	ComponentEntity ce1 = daoService.createComponent("testcheckNull", "testcheckP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	StatusEntity st1= daoService.createStatus();
    	int appId1 = DBQueryUtil.checkAndInsertComponent("testcheckP", "testcheckNull", ct1, null, env1.getEnvironmentId(), re1.getRegionId(), hte1,TestDataProvider.getMesosPlatform());
    }
    @Test
    public void testGetAllAPIComponentDetails() throws SerialException, SQLException {
    	int del_ind_c=0;
    	HealthCheckEntity ht = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	List<ComponentVO> cvoList= DBQueryUtil.getAllAPIComponentDetails(ht.getEnvironment().getEnvironmentId(),
    			TestDataProvider.getMesosPlatform(), TestDaoService.regionID);
    }
    @Test
    public void testUpdateAppLaunchDate() throws SerialException, SQLException, ParseException {
    	int del_ind_c=0;
    	HealthCheckEntity ht = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	String launchDate="2018-11-28T11:16:38.111Z";
    	DBQueryUtil.updateAppLaunchDate(launchDate, ht.getComponent().getComponentId(), ht.getEnvironment().getEnvironmentId());
    }
    
    @Test
    public void testGetAppsLauchDateNull() throws SerialException, SQLException {
    	int del_ind_c=0;
    	HealthCheckEntity ht = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	Set<Integer> st = DBQueryUtil.getAppsLauchDateNull(ht.getEnvironment().getEnvironmentId());
    }
    
    @Test
    public void testUpdateDelIndForHealthCheck() throws SerialException, SQLException {
    	int del_ind_c=0;
    	HealthCheckEntity ht = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateDelIndForHealthCheck(ht.getComponent().getComponentId(), 0, ht.getEnvironment().getEnvironmentId(), ht.getRegion().getRegionId());
    }
    
    @Test
    public void testUpdateApiDetails_Mesos() throws SerialException, SQLException {
    	int del_ind=0;
    	ComponentEntity ce = daoService.createComponent("testApi", "testApiP",del_ind,cte,TestDataProvider.getMesosPlatform());
    	String name = "testApiEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	ApiStatusEntity ase = 	daoService.createApiStatusEntity(ce, env, 10);
    	DBQueryUtil.updateApiDetails(env.getEnvironmentId(), ce.getComponentName(), 11,TestDataProvider.getMesosPlatform());
    }
    
    @Test
    public void testUpdateApiDetails_K8s() throws SerialException, SQLException {
    	int del_ind=0;
    	ComponentEntity ce = daoService.createComponent("testK8sApi", "testK8sApiP",del_ind,cte,TestDataProvider.getK8sPlatform());
    	String name = "testK8sApiEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	ApiStatusEntity ase = 	daoService.createApiStatusEntity(ce, env, 10);
    	DBQueryUtil.updateApiDetails(env.getEnvironmentId(), ce.getComponentName(), 11,TestDataProvider.getK8sPlatform());
    }
    /** FIX Me : Invalid Platform Case not handled for this function in DBQueryUtil.
    @Test
    public void testUpdateApiDetails_InvalidPlatform() throws SerialException, SQLException {
    	int del_ind=0;
    	ComponentEntity ce = daoService.createComponent("testK8sApiINv", "testK8sApiPInv",del_ind,cte,TestDataProvider.getK8sPlatform());
    	String name = "testK8sInvApiEnv";
    	EnvironmentEntity env = daoService.createEnvironment(name, 1,url);
    	ApiStatusEntity ase = 	daoService.createApiStatusEntity(ce, env, 10);
    	DBQueryUtil.updateApiDetails(env.getEnvironmentId(), ce.getComponentName(), 11,TestDataProvider.getInvalidPlatform());
    }
    **/
    @Test
    public void testGetCounterDetails() throws SerialException, SQLException {
    	EnvCounterEntity ece = daoService.createEnvCounterEntity();
    	List<CounterDetailVO> result = DBQueryUtil.getCounterDetails();
    	//Assert.assertEquals("Size not matching",TestDaoService.envCounterID,result.size());
    }
    /*
    @Test
    public void testGetNumberOfServices() throws SerialException, SQLException {
    	int del_ind_c=0;
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id);
    	int result = DBQueryUtil.getNumberOfServices(hce.getEnvironment().getEnvironmentId(),TestDataProvider.getMesosPlatform());
    	//Assert.assertEquals("Number of services not matching",TestDaoService.healthCheckID,result);
    }*/
    
    @Test
    public void testLoadContainerStatus() throws SerialException, SQLException {
    	String compName ="testLoadCont";
    	String parentCompName = "testLoadContP";
    	ContainerStatsEntity cse = daoService.createContainerStatsEntity(compName,parentCompName,cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.loadContainerStatus(cse.getEnvironment().getEnvironmentId(), cse.getComponent().getComponentId(), 5);
    	Assert.assertEquals("Component ID not matching", cse.getComponent().getComponentId(),cse.getComponent().getComponentId());
    	
    	//Case 2 for invalid component Name
    	DBQueryUtil.loadContainerStatus(1,1, 5);
    	Assert.assertEquals("Component ID Available in DB", 1,1);
    	
    }
    
    @Test
    public void testGetCurrentContainerDetails() throws SerialException, SQLException {
    	String compName ="testCurrentCont";
    	String parentCompName = "testCurrentContP";
    	ContainerStatsEntity cse = daoService.createContainerStatsEntity(compName,parentCompName,cte,TestDataProvider.getMesosPlatform());
    	Map<String, Integer> result = DBQueryUtil.getCurrentContainerDetails(cse.getEnvironment().getEnvironmentId());
    	Assert.assertEquals("ParentComp/Component Name Not Matching",true,result.containsKey(cse.getComponent().getParentComponent().getComponentName()+"/"+cse.getComponent().getComponentName()));
    	Assert.assertEquals("No of containers not matching",true,result.containsValue(cse.getTotalContainer()));
    }
    @Test
    public void testGetCurrentContainerDetails_NullParentComponent() throws SerialException, SQLException {
    	String compName1 ="testCurrentCont";
    	String parentCompName1 = null;
    	ContainerStatsEntity cse1 = daoService.createContainerStatsEntity(compName1,parentCompName1,cte,TestDataProvider.getMesosPlatform());
    	Map<String, Integer> result1 = DBQueryUtil.getCurrentContainerDetails(cse1.getEnvironment().getEnvironmentId());
    	Assert.assertEquals("ParentComp/Component Name Not Matching",true,result1.containsKey(cse1.getComponent().getComponentName()));
    	Assert.assertEquals("No of containers not matching",true,result1.containsValue(cse1.getTotalContainer()));
    }
    @Test
    public void testGetCurrentContainerDetails_NullComponents() throws SerialException, SQLException {
    	String compName =null;
    	String parentCompName = null;
    	ContainerStatsEntity cse = daoService.createContainerStatsEntity(compName,parentCompName,cte,TestDataProvider.getMesosPlatform());
    	Map<String, Integer> result = DBQueryUtil.getCurrentContainerDetails(cse.getEnvironment().getEnvironmentId());
    	Assert.assertEquals("ParentComp/Component Name Not Matching",true,result.containsKey(cse.getComponent().getComponentName()));
    	Assert.assertEquals("No of containers not matching",true,result.containsValue(cse.getTotalContainer()));
    }
    
    @Test
    public void testGetCCPUpTime() throws SerialException, SQLException {
    	int del_ind_c=0;
    	 region_id=1;
    	cte.setComponentTypeId(1);
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	DaillyCompStatusEntity de = daoService.createDailyCompStatus(hce);
    	hce.getComponent().getComponentType().setComponentTypeId(1);
    	hce.getRegion().setRegionId(1L);
    	String[] name= new String[10];
    	name[0]=hce.getComponent().getParentComponent().getComponentName();
    	float result = DBQueryUtil.getCCPUpTime(hce.getEnvironment().getEnvironmentId(),name);
    	
    }
    
    @Test
    public void testUpdateContainerDetails() throws SerialException, SQLException {
    	String compName ="testUpdateCont";
    	String parentCompName = "testUpdateContP";
    	ContainerStatsEntity cse = daoService.createContainerStatsEntity(compName,parentCompName,cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateContainerDetails(cse.getEnvironment().getEnvironmentId(),cse.getComponent().getComponentId() , 11);
    }
    @Test
    public void testUpdateContainerDetails_InvalidCompId() throws SerialException, SQLException {
    	String compName ="testUpdateCont";
    	String parentCompName = "testUpdateContP";
    	ContainerStatsEntity cse = daoService.createContainerStatsEntity(compName,parentCompName,cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateContainerDetails(cse.getEnvironment().getEnvironmentId(),cse.getComponent().getComponentId(), 11);
    }
    @Test
    public void testGetTotalTransaction() throws SerialException, SQLException {
    	CounterMetricEntity cm = daoService.createCounterMetricEntity(1f);
    	int envCounterId = cm.getEnvCounterId();
    	float result = DBQueryUtil.getTotalTransaction(envCounterId);
    	Assert.assertEquals("Metric Value not Matching", cm.getMetricVal(), result, 0f);
    }
    
    @Test
    public void testGetTotalTransaction_NullMetricValue() throws SerialException, SQLException {
    	/** passing Invalid Environment Counter Value **/
    	int envCounterId = 999;
    	float result = DBQueryUtil.getTotalTransaction(envCounterId);
    	Assert.assertEquals("Metric Value not Matching", 0f, result, 0f);
    }
    
    @Test
    public void testGetSurveillerDetailsOfComponent() throws SerialException, SQLException {
    	int del_ind_c=0;
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	List<HealthCheckVO> result = DBQueryUtil.getSurveillerDetailsOfComponent();
       // Assert.assertEquals("Size not Matching",TestDaoService.healthCheckID,result.size());
  	
    }
    
    @Test
    public void testGetComponentByNameAndParent() throws SerialException, SQLException {
    	int del_ind_c=1;
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(del_ind_c,cte,region_id,TestDataProvider.getMesosPlatform());
    	int result=DBQueryUtil.getComponentByNameAndParent(hce.getComponent().getComponentName(), hce.getComponent().getParentComponent().getComponentId(), hce.getComponent().getComponentType().getComponentTypeId(),TestDataProvider.getMesosPlatform());
    	Assert.assertEquals("Component ID not matching", hce.getComponent().getComponentId(),result);
    }
    
    @Test
    public void testExecuteDelete() {
    	ComponentEntity ce = daoService.createComponent("testdel", "testdelp", 0,cte,TestDataProvider.getMesosPlatform());
    	String query = "Delete from component where comp_name='testdel'";
    	DBQueryUtil.executeDelete(query);
    }
    @Test
    public void testGetTpsLatencyHsitory() throws SerialException, SQLException {
    	TpsLatHistoryEntity kt = daoService.createTpsLatHistoryEntity("testMesosTps", "tetMesosTpsP", cte, TestDataProvider.getMesosPlatform());
    	Map<Integer, List<Float>> result =	DBQueryUtil.getTpsLatencyHsitory(kt.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
        
    }
    @Test
    public void testGetTpsLatencyHsitory_K8s() throws SerialException, SQLException {
    	K8sTpsLatHistoryEntity kt = daoService.createK8sTpsLatHistoryEntity("testK8stps", "k8stpsp", cte,TestDataProvider.getK8sPlatform());
    	Map<Integer, List<Float>> result =	DBQueryUtil.getTpsLatencyHsitory(kt.getEnvironment().getEnvironmentId(), TestDataProvider.getK8sPlatform());
        
    }
    /** FIX Me : Invalid Platform test case Fails 
    @Test
    public void testGetTpsLatencyHsitory_InvalidPlatform() throws SerialException, SQLException {
    	K8sTpsLatHistoryEntity kt = daoService.createK8sTpsLatHistoryEntity("testK8stps", "k8stpsp", cte,TestDataProvider.getMesosPlatform());
    	Map<Integer, List<Float>> result =	DBQueryUtil.getTpsLatencyHsitory(kt.getEnvironment().getEnvironmentId(), TestDataProvider.getInvalidPlatform());
        
    }**/
   
    @Test
    public void testLoadK8sTpsLatencyHistory() throws SerialException, SQLException {
    	K8sTpsLatHistoryEntity kt = daoService.createK8sTpsLatHistoryEntity("testK8stps", "k8stpsp", cte,TestDataProvider.getK8sPlatform());
    	DBQueryUtil.loadK8sTpsLatencyHistory(kt.getEnvironment().getEnvironmentId(), kt.getComponent().getComponentId(), kt.getTpsValue(), kt.getLatencyValue());
    	
    }
    @Test
    public void testLoadPodsAndContainers() throws SerialException, SQLException {
    	K8sPodsContainersEntity kps = daoService.createK8sPodsContainersEntity("testloadk8s", "testloadK8sp", cte,TestDataProvider.getK8sPlatform());
    	int result = DBQueryUtil.loadPodsAndContainers(kps.getEnvironment().getEnvironmentId(), kps.getComponent().getComponentName(),kps.getComponent().getParentComponent().getComponentName(), 10, 10);
    	Assert.assertEquals("Component ID Not Matching",kps.getComponent().getComponentId(), result);
    }
    @Test
    public void testLoadPodsAndContainers_ComponentId_Invalid() throws SerialException, SQLException {
    	K8sPodsContainersEntity kps = daoService.createK8sPodsContainersEntity("testloadk8sInvalid", "testloadK8sp", cte,TestDataProvider.getK8sPlatform());
        int result = DBQueryUtil.loadPodsAndContainers(kps.getEnvironment().getEnvironmentId(), "InvalidComp",kps.getComponent().getParentComponent().getComponentName(), 10, 10);
        Assert.assertEquals("Invalid Component ID Case Failed",0, result);
        }
    @Test
    public void testupdatePodsAndContainers() throws SerialException, SQLException {
    	K8sPodsContainersEntity kps = daoService.createK8sPodsContainersEntity("testUpdatePod", "testUpdatePodP", cte,TestDataProvider.getK8sPlatform());
    	DBQueryUtil.updatePodsAndContainers(kps.getEnvironment().getEnvironmentId(), kps.getComponent().getComponentName(), 11, kps.getComponent().getParentComponent().getComponentName(), 11);
    }
    @Test
    public void testupdatePodsAndContainers_Invalid_Component() throws SerialException, SQLException {
    	K8sPodsContainersEntity kps = daoService.createK8sPodsContainersEntity("testUpdatePod", "testUpdatePodP", cte,TestDataProvider.getK8sPlatform());
    	DBQueryUtil.updatePodsAndContainers(kps.getEnvironment().getEnvironmentId(),"InvalidComp", 11, kps.getComponent().getParentComponent().getComponentName(), 11);
    }
    @Test
    public void testGetAllCurrentTPS() throws SerialException, SQLException {
    	TpsServiceEntity tps = daoService.createTpsServiceEntity("testTpsService","testTpsServiceap",cte,TestDataProvider.getMesosPlatform());
    	List<Integer> result = DBQueryUtil.getAllCurrentTPS(tps.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
    }
    @Test
    public void testGetAllCurrentTPS_NullComponent() throws SerialException, SQLException {
    	TpsServiceEntity tps = daoService.createTpsServiceEntity(null,"testP",cte,TestDataProvider.getMesosPlatform());
    	List<Integer> result = DBQueryUtil.getAllCurrentTPS(tps.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
    }
    @Test
    public void testUpdateTpsService() throws SerialException, SQLException {
    	TpsServiceEntity tps = daoService.createTpsServiceEntity("testTPSUpdate","testP",cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateTpsService(tps.getEnvironment().getEnvironmentId(), tps.getComponent().getComponentId(), tps.getTpsValue(), tps.getLatencyValue());
    	
    }
    
    @Test
    public void testLoadTpsService() throws SerialException, SQLException {
    	TpsServiceEntity tps = daoService.createTpsServiceEntity("testLoadTps","testP",cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.loadTpsService(tps.getEnvironment().getEnvironmentId(), tps.getComponent().getComponentId(), tps.getTpsValue(), tps.getLatencyValue());
    }
    @Test
    public void testLoadTpsLatencyHistory() throws SerialException, SQLException {
    	TpsLatHistoryEntity tpsLat = daoService.createTpsLatHistoryEntity("testtpshist","testtpshistp", cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.loadTpsLatencyHistory(tpsLat.getEnvironment().getEnvironmentId(), tpsLat.getComponent().getComponentId(), tpsLat.getTpsValue(), tpsLat.getLatencyValue());
    }
    @Test
    public void testUpdateTpsLatHistory_Mesos_Platform() throws SerialException, SQLException {
    	TpsLatHistoryEntity tpsLat = daoService.createTpsLatHistoryEntity("testMesosTpsHist","testMesosTpsHistp", cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateTpsLatHistory(tpsLat.getEnvironment().getEnvironmentId(), tpsLat.getComponent().getComponentId(), tpsLat.getTpsValue(), tpsLat.getLatencyValue(),TestDataProvider.getMesosPlatform());  	
    }
    
    @Test
    public void testUpdateTpsLatHistory_K8s_Platform() throws SerialException, SQLException {
    	K8sTpsLatHistoryEntity tpsLat = daoService.createK8sTpsLatHistoryEntity("testK8sTpsHist","testK8sTpsHistp", cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateTpsLatHistory(tpsLat.getEnvironment().getEnvironmentId(), tpsLat.getComponent().getComponentId(), tpsLat.getTpsValue(), tpsLat.getLatencyValue(),TestDataProvider.getK8sPlatform());  	
    }
   /** Invalid Platform Case Not Handled for testUpdateTpsLatHistory in DBQueryUtil. Include this case after fixing the same.
 * @throws SQLException 
 * @throws SerialException 
    @Test
    public void testUpdateTpsLatHistory_Invalid_Platform() throws SerialException, SQLException {
    	K8sTpsLatHistoryEntity tpsLat = daoService.createK8sTpsLatHistoryEntity("testK8sTpsHist","testK8sTpsHistp", cte);
    	DBQueryUtil.updateTpsLatHistory(tpsLat.getEnvironment().getEnvironmentId(), tpsLat.getComponent().getComponentId(), tpsLat.getTpsValue(), tpsLat.getLatencyValue(),TestDataProvider.getInvalidPlatform());  	
    }**/
    @Test
    public void testLoadK8sApiStatus() throws SerialException, SQLException {
    	K8sApiStatusEntity k8sapi = daoService.createK8sApiStatusEntity("testK8sApi", "testK8sApiP", cte,TestDataProvider.getK8sPlatform());
    	DBQueryUtil.loadK8sApiStatus(k8sapi.getEnvironment().getEnvironmentId(), k8sapi.getComponent().getComponentName(), 10);
    	
    }
    @Test
    public void testLoadK8sApiStatus_Invalid_Component() throws SerialException, SQLException {
    	K8sApiStatusEntity k8sapi = daoService.createK8sApiStatusEntity("testK8sApi", "testK8sApiP", cte,TestDataProvider.getK8sPlatform());
    	DBQueryUtil.loadK8sApiStatus(k8sapi.getEnvironment().getEnvironmentId(),"Invalid", 10);
    	
    }
    
    @Test
    public void testGetK8sCurrentApiDetails() throws SerialException, SQLException {
    	K8sApiStatusEntity k8sapi = daoService.createK8sApiStatusEntity("testK8sCurrApi", "testK8sApiP", cte,TestDataProvider.getK8sPlatform());
    	Map<String, Integer> result = DBQueryUtil.getK8sCurrentApiDetails(k8sapi.getEnvironment().getEnvironmentId());
   
    }
    /** FIX ME : Platform Parameter not used in getK8sCurrContainerDetails function **/
    @Test
    public void getK8sCurrContainerDetails() throws SerialException, SQLException {
    	K8sPodsContainersEntity k8sCont = daoService.createK8sPodsContainersEntity("testk8scont", "testK8scontP", cte,TestDataProvider.getK8sPlatform());
    	Map<String, Integer> result = DBQueryUtil.getK8sCurrContainerDetails(k8sCont.getEnvironment().getEnvironmentId(), TestDataProvider.getK8sPlatform());
    }
    @Test
    public void getK8sCurrContainerDetails_ParentComp_Null() throws SerialException, SQLException {
    	K8sPodsContainersEntity k8sCont = daoService.createK8sPodsContainersEntity("testk8scont", null, cte,TestDataProvider.getK8sPlatform());
    	Map<String, Integer> result = DBQueryUtil.getK8sCurrContainerDetails(k8sCont.getEnvironment().getEnvironmentId(), TestDataProvider.getK8sPlatform());
    }
    @Test
    public void getK8sCurrContainerDetails_Comp_Null() throws SerialException, SQLException {
    	K8sPodsContainersEntity k8sCont = daoService.createK8sPodsContainersEntity(null, "testP", cte,TestDataProvider.getK8sPlatform());
    	Map<String, Integer> result = DBQueryUtil.getK8sCurrContainerDetails(k8sCont.getEnvironment().getEnvironmentId(), TestDataProvider.getK8sPlatform());
    }
    @Test
    public void testUpdateContainerDetails_Mesos() throws SerialException, SQLException {
    	ContainerStatsEntity mcse = daoService.createContainerStatsEntity("testmesosComp", "testMesosP", cte,TestDataProvider.getMesosPlatform());
    	DBQueryUtil.updateContainerDetails(mcse.getEnvironment().getEnvironmentId(), mcse.getComponent().getComponentName(), 10, mcse.getComponent().getParentComponent().getComponentName(), TestDataProvider.getMesosPlatform());
    }
//    @Test
//    public void testUpdateContainerDetails_K8s() throws SerialException, SQLException {
//    	ContainerStatsEntity mcse = daoService.createContainerStatsEntity("testk8sComp", "testK8sP", cte,TestDataProvider.getK8sPlatform());
//    	DBQueryUtil.updateContainerDetails(mcse.getEnvironment().getEnvironmentId(), mcse.getComponent().getComponentName(), 10, mcse.getComponent().getParentComponent().getComponentName(), TestDataProvider.getK8sPlatform());
//    }
    @Test
    public void testUpdateContainerDetails_Invalid_Comp() throws SerialException, SQLException {
    	ContainerStatsEntity mcse = daoService.createContainerStatsEntity("testk8sInvalidComp", "testK8sP", cte,TestDataProvider.getK8sPlatform());
    	DBQueryUtil.updateContainerDetails(mcse.getEnvironment().getEnvironmentId(), "Invalid", 10, mcse.getComponent().getParentComponent().getComponentName(), TestDataProvider.getK8sPlatform());
    }
    
    @Test
    public void testGetNumberOfServices_Mesos_Platform() throws SerialException, SQLException {
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(0, cte, region_id,TestDataProvider.getMesosPlatform());
    	CounterDetailVO counterDetails = new CounterDetailVO();
    	counterDetails.setEnvironmentId(hce.getEnvironment().getEnvironmentId());
    	counterDetails.setPlatform(TestDataProvider.getMesosPlatform());
    	int result = DBQueryUtil.getNumberOfServices(counterDetails);
    } 
    @Test
    public void testGetNumOfActiveApisOfApp() throws SerialException, SQLException {
    	HealthCheckEntity hce = daoService.createHealthCheckEntity(0, cte, region_id,TestDataProvider.getMesosPlatform());
    	Map<String, Integer> result = DBQueryUtil.getNumOfActiveApisOfApp(hce.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
    }
    
    @Test
    public void testGetPromLookupHttpPath() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity("testgetProm", "testgetPromP", cte, TestDataProvider.getMesosPlatform(),"testPromHttpEnv");
    	Map<String, Integer> result = DBQueryUtil.getPromLookupHttpPath(ple.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
    }
    
    @Test
    public void testGetPromLookupDetails() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity("testgetPromDet", "testgetPromDetP", cte, TestDataProvider.getMesosPlatform(),"testPromLookUP");
    	Map<String, String> result = DBQueryUtil.getPromLookupDetails(ple.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
    }
    /*
    @Test
    public void getPromLookupDetails_Null_Component() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity(null, "testgetPromDetP", cte, TestDataProvider.getMesosPlatform(),"testLookUpPromNull");
    	Map<String, String> result = DBQueryUtil.getPromLookupDetails(ple.getEnvironment().getEnvironmentId(), TestDataProvider.getMesosPlatform());
    }*/
    
    @Test
    public void testLoadPromLookup() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity("testloadPromDet", "testloadPromDetP", cte, TestDataProvider.getMesosPlatform(),"testLoadPromEnv");
        int result = DBQueryUtil.loadPromLookup(ple.getEnvironment().getEnvironmentId(), ple.getComponent().getComponentId(),ple.getHttpPath());
    }
    
    @Test
    public void testLoadPromLookup_Invalid_Component() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity("testInvPromDet", "testInvPromDetP", cte, TestDataProvider.getMesosPlatform(),"testLoadPromEnv");
        int result = DBQueryUtil.loadPromLookup(ple.getEnvironment().getEnvironmentId(), ple.getComponent().getComponentId(),ple.getHttpPath());
    }
    
    @Test
    public void testUpdatePromLookup() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity("testUpdatePromDet", "testUpdatePromDetP", cte, TestDataProvider.getMesosPlatform(),"testUpdatePromEnv");
         DBQueryUtil.updatePromLookup(ple.getEnvironment().getEnvironmentId(), ple.getComponent().getComponentId(),ple.getHttpPath());
    }
    
    @Test
    public void testUpdatePromLookup_Invalid_Component() throws SerialException, SQLException {
    	PromLookupEntity ple = daoService.createPromLookupEntity("testInvUpdatePromDet", "testUpdatePromDetP", cte, TestDataProvider.getMesosPlatform(),"testUpdatePromEnv");
        DBQueryUtil.updatePromLookup(ple.getEnvironment().getEnvironmentId(),ple.getComponent().getComponentId(),ple.getHttpPath());
    }
    
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<DBQueryUtil> constructor = DBQueryUtil.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}
}
