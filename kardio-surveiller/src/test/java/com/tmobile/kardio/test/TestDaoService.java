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
package com.tmobile.kardio.test;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.sql.rowset.serial.SerialException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tmobile.kardio.surveiller.db.config.HibernateConfig;
import com.tmobile.kardio.surveiller.db.entity.AlertSubscriptionEntity;
import com.tmobile.kardio.surveiller.db.entity.ApiStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.ContainerStatsEntity;
import com.tmobile.kardio.surveiller.db.entity.CounterEntity;
import com.tmobile.kardio.surveiller.db.entity.CounterMetricEntity;
import com.tmobile.kardio.surveiller.db.entity.CounterMetricTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.DaillyCompStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.EnvCounterEntity;
import com.tmobile.kardio.surveiller.db.entity.EnvironmentEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckParamEntity;
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
import com.tmobile.kardio.surveiller.util.ProxyUtilTest;

public class TestDaoService extends ProxyUtilTest{


    public static int environmentID,k8sPodStatusID,tpsLatencyHistoryID,k8sTpsLatencyHistoryID,dailyCompID, compID, appID, parentCompID, componentTypeID,apiStatusID,healthCheckTypeID,regionID, statusID, counterID,envCounterID,counterMetricTypeID,counterMetricID;
    public static long healthCheckID,containerStatusID;	
//    public Component createComponent() {
//		Component ce = getComponent();
//    	componentDao.saveComponent(ce);
//    	compID++;
//    	return ce;
//	}
//
//    public Component createComponent(Component comp) {
//    	if (comp.getParentComponentId() != 0) {
//    		parentCompID++;
//    	}
//    	componentDao.saveComponent(comp);
//    	compID++;
//    	return comp;
//	}
//    public Component getComponent() {
//		Component ce = new Component();
//    	ce.setComponentName("component_1");
//    	ce.setComponentType("APP");
//    	ce.setAppFullName("app_full_name_1");
//    	ce.setComponentDate(new Timestamp(new Date().getTime()));
//		return ce;
//	}
    
    static class MarathonJSON implements Serializable {
    	String url;
    	String username;
    	String password;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
    	
    	
    }
	
    public EnvironmentEntity createEnvironment(String envName, int lock, String url) throws SerialException, SQLException {
		EnvironmentEntity env = new EnvironmentEntity();
    	env.setEnvironmentName(envName);
    	env.setEnvironmentDesc("env_desc_1");
    	env.setEnvLock(lock);
    	env.setDisplayOrder(0);
    	
    	MarathonJSON marathonJSON = new MarathonJSON();
    	marathonJSON.url = url;
    	marathonJSON.username = "marathon_user_1";
    	marathonJSON.password = "marathon_pass_1";
    	String marathonBlob = TestUtils.asJsonString(marathonJSON);
    	Blob b = new javax.sql.rowset.serial.SerialBlob(marathonBlob.getBytes());
    	env.setMarathonJson(b);
    	env.setMarathonURL(marathonJSON.url);
    	saveEntity(env);      
    	environmentID++;
    	return env;
	}
    
    public ComponentTypeEntity createComponentType() {
    	ComponentTypeEntity cte = new ComponentTypeEntity();
    	cte.setComponentTypeName(ComponentType.APP.name());
    	cte.setComponentTypeDesc("desc");
    	saveEntity(cte);
    	componentTypeID++;
    	return cte;
    }

	private void saveEntity(Object obj) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();    	    	
        session.save(obj);
        tx.commit();
	}
	
	public ComponentEntity createComponent(String compName,String parentCompName, int del_ind,ComponentTypeEntity cte,String platform) {
		ComponentEntity pc = null;
		
		if(parentCompName!=null) {
	    pc = new ComponentEntity();
		pc.setComponentName(parentCompName);
		pc.setComponentDesc("testDesc");
		pc.setComponentType(cte);
		pc.setDelInd(del_ind);
		pc.setPlatform(platform);
		saveEntity(pc);
		parentCompID++;
		compID++;
		}
		
		ComponentEntity ce = new ComponentEntity();
		ce.setComponentName(compName);
		ce.setComponentDesc("testDesc");
		ce.setParentComponent(pc);
		ce.setComponentType(cte);
		ce.setDelInd(del_ind);
		ce.setPlatform(platform);
		saveEntity(ce);
    	compID++;
	
		return ce;
	}

	
public AlertSubscriptionEntity createAlertSubscriptionEntity(int subId,int valLevel,ComponentTypeEntity cte,String platform) throws SerialException, SQLException{
    
	EnvironmentEntity env = createEnvironment("alertEnvTest",1,"http://localhost:8080");
    ComponentEntity ce = createComponent("testAlert","testAlertP",0,cte,platform);
    AlertSubscriptionEntity ase=new AlertSubscriptionEntity();
	ase.setAlertSubscriptionId(subId);
	ase.setAuthToken("token");
	ase.setComponentId(ce.getComponentId());
	ase.setComponent(ce);
	ase.setEnvironment(env);
	ase.setSubscriptionVal("testVal");
	ase.setSubscriptionType(1);
	ase.setValidationLevel(valLevel);
	ase.setGlobalComponentTypeId(ce.getComponentType().getComponentTypeId());
	saveEntity(ase);
	return ase;
	}

public CounterEntity createCounterEntity() {
	CounterEntity ce = new CounterEntity();
   	ce.setCounterName("counter_1");
	ce.setCounterDesc("counter_desc_1");
	ce.setDelInd(0);
   ce.setPosition(1);
    saveEntity(ce);
   counterID++;
	return ce;
}
public CounterMetricEntity createCounterMetricEntity(float metricVal) throws SerialException, SQLException {
	EnvCounterEntity envC = createEnvCounterEntity();
	 final java.sql.Date metricDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	CounterMetricEntity cm = new CounterMetricEntity();
	cm.setEnvCounter(envC);
	cm.setEnvCounterId(envC.getEnvCounterId());
	cm.setMetricDate(metricDate);
	cm.setMetricVal(metricVal);
	counterMetricID++;
	saveEntity(cm);
	return cm;
}
public CounterMetricTypeEntity createCounterMetricTypeEntity()
{
	CounterMetricTypeEntity cme = new CounterMetricTypeEntity();
	cme.setCounterMetricType("test");
	cme.setCounterMetricTypeDesc("test");
	counterMetricTypeID++;
	saveEntity(cme);
	return cme;
}
public EnvCounterEntity createEnvCounterEntity() throws SerialException, SQLException {
	CounterEntity ce = createCounterEntity();
	EnvironmentEntity env = createEnvironment("testCounterEnv",1,"http://localhost:8080");
	CounterMetricTypeEntity cme = createCounterMetricTypeEntity();
	EnvCounterEntity ece = new EnvCounterEntity();
	ece.setCountMetricType(cme);
	ece.setCounter(ce);
	ece.setCounterId(ce.getCounterId());
	ece.setEnvironment(env);
	ece.setEnvironmentId(env.getEnvironmentId());
	ece.setMetricTypeId(1);
	ece.setParameter1("param_value_1");
	ece.setParameter2("param_value_2");
    saveEntity(ece);
    envCounterID++;
	return ece;
}
	
   public ApiStatusEntity createApiStatusEntity(ComponentEntity comp,EnvironmentEntity env,int totApi) {
	   ApiStatusEntity api =new ApiStatusEntity();
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   api.setStatusDate(todayDate);
	   api.setComponent(comp);
	   api.setEnvironment(env);
	   api.setDeltaValue(1);
	   api.setTotalApi(totApi);
       saveEntity(api);
       apiStatusID++;
	   return api;
   }
   
   public HealthCheckTypeEntity createHeathCheckType() {
	   HealthCheckTypeEntity hce = new HealthCheckTypeEntity();
	   hce.setHealthCheckTypeName("DUMMY");
	   hce.setHealthCheckTypeDesc("testDesc");
	   hce.setHealthCheckTypeClass("dummy");
	   saveEntity(hce);
	   healthCheckTypeID++;
	   return hce;
   }
   public RegionEntity createRegionEntity()
   {
	   RegionEntity re = new RegionEntity();
	   re.setRegionDesc("test");
	   re.setRegionLock(1);
	   re.setRegionName("testRegion");
	   saveEntity(re);
	   regionID++;
	   return re;  
   }
   
   public StatusEntity createStatus() {
	   StatusEntity se = new StatusEntity();
	   se.setStatusDesc("test");
	   se.setStatusName("testStatus");
	   saveEntity(se);
	   statusID++;
	   return se;
   }
   
   public DaillyCompStatusEntity createDailyCompStatus(HealthCheckEntity healthCheck) {
	   DaillyCompStatusEntity de = new DaillyCompStatusEntity();
	   StatusEntity se = createStatus();
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   de.setHealthCheck(healthCheck);
	   de.setTotalFailureCount(0);
	   de.setStatusDate(todayDate);
	   de.setStatus(se);
	   de.setPercentageUpTime(95.5F);
	   saveEntity(de);
	   dailyCompID++;
	   return de;
	   
   }
   
   public HealthCheckParamEntity createHealthCheckParamEntity()
   {
	   HealthCheckParamEntity hpe = new HealthCheckParamEntity();
	   hpe.setHealthCheckParamKey("test");
	   hpe.setHealthCheckParamVal("testVal");
	   saveEntity(hpe);
	   return hpe;
	   
   }
   
   public HealthCheckEntity  createHealthCheckEntity(int del_ind_c,ComponentTypeEntity cte, long regionId,String platform) throws SerialException, SQLException {
	   HealthCheckTypeEntity hte = createHeathCheckType();
	   HealthCheckParamEntity hpe = createHealthCheckParamEntity();
	   Set<HealthCheckParamEntity> healthCheckParams = new HashSet<HealthCheckParamEntity>();
	   healthCheckParams.add(hpe);
	   StatusEntity se = createStatus();
	   RegionEntity re = createRegionEntity();
	   EnvironmentEntity env = createEnvironment("testHcEnv",1,"http://localhost:8080");
	   ComponentEntity ce = createComponent("testHc","testHcp",del_ind_c,cte,platform);
	   HealthCheckEntity hce = new HealthCheckEntity();
	   hce.setCurrentStatus(se);
	   hce.setComponent(ce);
	   hce.setHealthCheckParams(healthCheckParams);
	   hce.setEnvironment(env);
	   hce.setHealthCheckType(hte);
	   if(regionId!=0) {
	   re.setRegionId(regionId);
	   }
	   hce.setRegion(re);
	   hce.setDelInd(0);
	   saveEntity(hce);
	   hpe.setHealthCheck(hce);
	   saveEntity(hpe);
	   healthCheckID++;
	   return hce;
	  
   }
   
   public ContainerStatsEntity createContainerStatsEntity(String compName,String parentCompName,ComponentTypeEntity cte,String platform) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,parentCompName,0,cte,platform);
	   EnvironmentEntity env =createEnvironment("testContEnv",1,"http://localhost:8080");
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   ContainerStatsEntity ce = new ContainerStatsEntity();
	   ce.setComponent(comp);
	   ce.setEnvironment(env);
	   ce.setDeltaValue(1);
	   ce.setStatsDate(todayDate);
	   ce.setTotalContainer(10);
	   saveEntity(ce);
	   containerStatusID++;
	  return ce;
   }
   
   public K8sTpsLatHistoryEntity createK8sTpsLatHistoryEntity(String compName,String parentCompName,ComponentTypeEntity cte,String platform) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,parentCompName,0,cte,platform);
	   EnvironmentEntity env =createEnvironment("testContEnv",1,"http://localhost:8080");
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   K8sTpsLatHistoryEntity k8stps=new K8sTpsLatHistoryEntity();
	   k8stps.setComponent(comp);
	   k8stps.setEnvironment(env);
	   k8stps.setLatencyValue(2f);
	   k8stps.setTpsValue(0.02f);
	   k8stps.setStatusDate(todayDate);
	   saveEntity(k8stps);
	   k8sTpsLatencyHistoryID++;
	   return k8stps;
	   
   }
   public TpsLatHistoryEntity createTpsLatHistoryEntity(String compName,String parentCompName,ComponentTypeEntity cte,String platform) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,parentCompName,0,cte,platform);
	   EnvironmentEntity env =createEnvironment("testContEnv",1,"http://localhost:8080");
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   TpsLatHistoryEntity tpsLat = new TpsLatHistoryEntity();
	   tpsLat.setComponent(comp);
	   tpsLat.setEnvironment(env);
	   tpsLat.setLatencyValue(0.9f);
	   tpsLat.setTpsValue(1f);
	   tpsLat.setStatusDate(todayDate);
	   saveEntity(tpsLat);
	   tpsLatencyHistoryID++;
	return tpsLat;
	   
   }
   
   public PromLookupEntity createPromLookupEntity(String compName,String parentCompName,ComponentTypeEntity cte,String platform,String envName) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,parentCompName,0,cte,platform);
	   EnvironmentEntity env =createEnvironment(envName,1,"http://localhost:8080");
	   PromLookupEntity ple = new PromLookupEntity();
	   ple.setComponent(comp);
	   ple.setEnvironment(env);
	   ple.setHttpPath("TEst http url");
	   saveEntity(ple);
	   return ple;
   }
   
   public K8sPodsContainersEntity createK8sPodsContainersEntity(String compName,String parentCompName,ComponentTypeEntity cte,String platform) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,parentCompName,0,cte,platform);
	   EnvironmentEntity env =createEnvironment("testContEnv",1,"http://localhost:8080");
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   K8sPodsContainersEntity kps = new K8sPodsContainersEntity();
	   kps.setComponent(comp);
	   kps.setEnvironment(env);
	   kps.setStatusDate(todayDate);
	   kps.setTotalPods(10);
	   kps.setTotalContainers(10);
	   k8sPodStatusID++;
	   saveEntity(kps);
	   return kps;     
   }
   public K8sApiStatusEntity createK8sApiStatusEntity(String compName,String parentCompName,ComponentTypeEntity cte,String platform) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,parentCompName,0,cte,platform);
	   EnvironmentEntity env =createEnvironment("testK8sApiEnv",1,"http://localhost:8080");
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   K8sApiStatusEntity k8sapi = new K8sApiStatusEntity();
	   k8sapi.setComponent(comp);
       k8sapi.setEnvironment(env);
       k8sapi.setStatusDate(todayDate);
       k8sapi.setTotalApi(7);
       saveEntity(k8sapi);
       return k8sapi;
   }
   public TpsServiceEntity createTpsServiceEntity(String compName,String compP,ComponentTypeEntity cte,String platform) throws SerialException, SQLException {
	   ComponentEntity comp = createComponent(compName,compP,0,cte,platform);
	   EnvironmentEntity env =createEnvironment("testtpsenv",1,"http://localhost:8080");
	   final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	   TpsServiceEntity tps = new TpsServiceEntity();
	   tps.setComponent(comp);
	   tps.setEnvironment(env);
	   tps.setLatencyValue(0.2f);
	   tps.setTpsValue(1.2f);
	  // tps.setLastUpdated(todayDate.);
	   saveEntity(tps);
	   return tps;  
   }
   
  //    public ComponentType createComponentType() {
//		ComponentType ct = ComponentType.APP;
//    	componentTypeDao.saveComponentType(ct);
//    	return ct;
//	}
//
//    public ComponentType createComponentType(String value) {
//		ComponentType ct = ComponentType.valueOf(value);
//    	componentTypeDao.saveComponentType(ct);
//    	return ct;
//	}
//    
//	public CounterMetricEntity createCounterMetricEntity(Session session, EnvCounterEntity ece) {
//		CounterMetricEntity cme = new CounterMetricEntity();
//    	cme.setMetricId(1);
//    	cme.setEnvCounter(ece);
//    	cme.setMetricDate(new Date());
//    	cme.setMetricVal(1.0f);
//        session.save(cme);
//        counterMatrixID++;
//		return cme;
//	}
//
//	public EnvCounterEntity createEnvCounterEntity(Session session, CounterEntity ce) {
//		EnvCounterEntity ece = new EnvCounterEntity();
//    	ece.setCounterId(1);
//    	ece.setEnvCounterId(1);
//    	ece.setMetricTypeId(1);
//    	ece.setParameter1("param_value_1");
//    	ece.setParameter2("param_value_2");
//    	ece.setCounterNum(ce);
//    	ece.setEnvironment(null);
//        session.save(ece);
//        envCounterID++;
//		return ece;
//	}
//
//	public CounterEntity createCounterEntity(Session session) {
//		CounterEntity ce = new CounterEntity();
//    	ce.setCounterId(1);
//    	ce.setCounterName("counter_1");
//    	ce.setCounterDesc("counter_desc_1");
//    	ce.setDelInd(0);
//    	ce.setPosition(1);
//        session.save(ce);
//        counterID++;
//		return ce;
//	}
//	
//	public RegionEntity createRegionEntity(Session session) {
//		RegionEntity region = new RegionEntity();
//		region.setRegionDesc("region_desc_1");
//		region.setRegionName("region_1");
//		region.setRegionLock(0);
//		session.save(region);
//		regionID++;
//		return region;
//	}
//	
//	public HealthCheckTypeEntity createHealthCheckTypeEntity(Session session) {
//		HealthCheckTypeEntity hcte = new HealthCheckTypeEntity();
//		hcte.setHealthCheckTypeName("healthchecktype_1");
//		hcte.setHealthCheckTypeDesc("healthchecktype_desc_1");
//		hcte.setHealthCheckTypeClass("healtchecktype.class");
//		session.save(hcte);
//		healthCheckTypeID++;
//		return hcte;
//	}
//
//    public HealthCheckVO createHealthCheck(String envName) {
//    	HealthCheckVO hc = getHealthCheck(envName);
//    	healthCheckDao.editHealthCheck(hc);
//    	
//    	Session session = sessionFactory.openSession();
//        Transaction tx = session.beginTransaction();
//        HealthCheckParamEntity hcpe = new HealthCheckParamEntity();
//        hcpe.setHealthCheck(session.get(HealthCheckEntity.class, new Integer(healthCheckID)));
//        hcpe.setHealthCheckParamKey(hc.getHealthCheckParamList().get(0).getHealthCheckParamKey());
//        hcpe.setHealthCheckParamVal(hc.getHealthCheckParamList().get(0).getHealthCheckParamValue());
//        session.save(hcpe);
//        healthCheckParamID++;
//        
//        tx.commit();
//        session.close();
//
//        healthCheckID++;
//    	return hc;
//    }
//
//	public HealthCheckVO getHealthCheck(String envName) {
//    	Session session = sessionFactory.openSession();
//        Transaction tx = session.beginTransaction();
//
//        Environment env = createEnvironment(envName, 0);
//    	ComponentType compType = createComponentType();
//    	createComponent();
//    	
//    	Component component = getComponent();
//    	component.setParentComponentId(TestDaoService.compID);
//    	component.setParentComponentName("parent_comp_name_1");
//    	createComponent(component);
//    	
//    	RegionEntity region = createRegionEntity(session);
//    	HealthCheckTypeEntity healthCheckType = createHealthCheckTypeEntity(session);
//    	
//    	
//        tx.commit();
//        session.close();
//
//        HealthCheckVO hcv = new HealthCheckVO();
//    	hcv.setComponentId(TestDaoService.compID);
//    	hcv.setComponentName(component.getComponentName());
//    	hcv.setComponentType(compType.componentTypeName());
//    	hcv.setDelInd(false);
//    	hcv.setEnvironmentId(TestDaoService.environmentID-1);
//    	hcv.setEnvironmentName(env.getEnvironmentName());
//    	
//    	HealthCheckParamVO hcp = new HealthCheckParamVO();
//    	hcp.setHealthCheckParamKey("key_1");
//    	hcp.setHealthCheckParamValue("value_1");
//    	List<HealthCheckParamVO> hcps = new ArrayList<HealthCheckParamVO>();
//    	hcps.add(hcp);
//    	hcv.setHealthCheckParamList(hcps);
//    	
//    	hcv.setHealthCheckTypeId(TestDaoService.healthCheckTypeID);
//    	hcv.setHealthCheckTypeName(healthCheckType.getHealthCheckTypeName());
//    	hcv.setMaxRetryCount(3);
//    	hcv.setParentComponentId(1);
//    	hcv.setParentComponentName("parent_1");
//    	hcv.setRegionId(TestDaoService.regionID);
//    	hcv.setRegionName(region.getRegionName());
//		return hcv;
//	}
}
