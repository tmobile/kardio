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
package com.tmobile.kardio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.ComponentMessages;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.HealthCheckParamVO;
import com.tmobile.kardio.bean.HealthCheckVO;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.dao.AppLookUpDao;
import com.tmobile.kardio.dao.ComponentDao;
import com.tmobile.kardio.dao.ComponentTypeDao;
import com.tmobile.kardio.dao.EnvironmentDao;
import com.tmobile.kardio.dao.HealthCheckDao;
import com.tmobile.kardio.dao.RegionMessageDao;
import com.tmobile.kardio.db.entity.ApiStatusEntity;
import com.tmobile.kardio.db.entity.AppLookUpEntity;
import com.tmobile.kardio.db.entity.AppRoleEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.db.entity.CounterEntity;
import com.tmobile.kardio.db.entity.CounterMetricEntity;
import com.tmobile.kardio.db.entity.DaillyCompStatusEntity;
import com.tmobile.kardio.db.entity.EnvCounterEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;
import com.tmobile.kardio.db.entity.HealthCheckParamEntity;
import com.tmobile.kardio.db.entity.HealthCheckTypeEntity;
import com.tmobile.kardio.db.entity.K8sApiStatusEntity;
import com.tmobile.kardio.db.entity.K8sPodsContainersEntity;
import com.tmobile.kardio.db.entity.K8sTpsLatencyHistoryEntity;
import com.tmobile.kardio.db.entity.RegionEntity;
import com.tmobile.kardio.db.entity.StatusEntity;
import com.tmobile.kardio.db.entity.TpsServiceEntity;

@Service
public class TestDaoService {
    @Autowired
    private SessionFactory sessionFactory;
	
    @Autowired
    private AppLookUpDao appFullNameDao;
    
    @Autowired
    private ComponentDao componentDao;
    
    @Autowired
    private ComponentTypeDao componentTypeDao;

    @Autowired
    private EnvironmentDao envDao;

    @Autowired
    private HealthCheckDao healthCheckDao;
    
    @Autowired
    private RegionMessageDao regionMessageDao;

    public static int environmentID=1, compID, appID, parentCompID,
    	counterID, envCounterID, counterMatrixID, regionID, 
    	healthCheckTypeID, healthCheckID=1, healthCheckParamID, apiStatusID, 
    	dailyCompStatusID,appRoleID,k8sContainerStatsID,
    	k8sPodsStatusID,k8sApiStatusID,tpsLatencyID,tpsServiceID,appSessionID,
    	compMessageID;
    
    public static int getLastCreatedEnvironmentID() {
    	return environmentID -1;
    }
    
    public static int getEnvironmentsCount() {
    	return environmentID;
    }

    public AppFullName createAppFullName() {
		AppFullName afn = getAppFullName();       
        appFullNameDao.saveAppFullName(afn);
        appID++;
		return afn;
	}

    public AppFullName getAppFullName() {
		ComponentType ct = ComponentType.APP;
    	componentTypeDao.saveComponentType(ct);
    	
    	createComponent();
        
        AppFullName afn = new AppFullName();
        afn.setComponentId(compID);
        afn.setComponentName("component_1");
        afn.setComponentFullName("component_full_name_1");
		return afn;
	}

    public Component createComponent() {
		Component ce = getComponent();
    	componentDao.saveComponent(ce);
    	compID++;
    	return ce;
	}

    public synchronized Component createComponent(Component comp) {
    	if (comp.getParentComponentId() != 0) {
    		parentCompID++;
    	}
    	componentDao.saveComponent(comp);
    	compID++;
    	return comp;
	}
    
    public Component getComponent() {
		Component ce = new Component();
    	ce.setComponentName("component_1");
    	ce.setComponentType("APP");
    	ce.setAppFullName("app_full_name_1");
    	ce.setComponentDate(new Timestamp(new Date().getTime()));
    	ce.setPlatform("Marathon");
		return ce;
	}
	
    public Environment createEnvironment(String envName, int lock) {
    	return createEnvironment(envName, lock, EnvironmentType.MARATHON);
    }
    
    public synchronized Environment createEnvironment(String envName, int lock, EnvironmentType type) {
		Environment env = new Environment();
    	env.setEnvironmentName(envName);
    	env.setEnvironmentDesc("env_desc_1");
    	env.setEnvLock(lock);
    	env.setDisplayOrder(0);

    	switch (type) {
		case MARATHON:
	    	env.setMarathonUrl("http://localhost:8080");
	    	env.setMarathonUserName("marathon_user_1");
	    	env.setMarathonPassword("marathon_pass_1");			
			break;
		case K8S:
			env.setK8sUrl("http://localhost");
			env.setK8sUserName("k8s_user_1");
			env.setK8sPassword("k8s_pass_1");
			break;
		default:
			break;
		}

    	envDao.addEnvironment(env);
    	environmentID++;
    	return env;
	}
    
	
    public ComponentType createComponentType() {
		ComponentType ct = ComponentType.APP;
    	componentTypeDao.saveComponentType(ct);
    	return ct;
	}

    public ComponentType createComponentType(String value) {
		ComponentType ct = ComponentType.valueOf(value);
    	componentTypeDao.saveComponentType(ct);
    	return ct;
	}
    
	public CounterMetricEntity createCounterMetricEntity(Session session, EnvCounterEntity ece) {
		CounterMetricEntity cme = new CounterMetricEntity();
    	cme.setMetricId(1);
    	cme.setEnvCounter(ece);
    	cme.setMetricDate(new Date());
    	cme.setMetricVal(1.0f);
        session.save(cme);
        counterMatrixID++;
		return cme;
	}

	public EnvCounterEntity createEnvCounterEntity(Session session, CounterEntity ce) {
		EnvCounterEntity ece = new EnvCounterEntity();
    	ece.setCounterId(1);
    	ece.setEnvCounterId(1);
    	ece.setMetricTypeId(1);
    	ece.setParameter1("param_value_1");
    	ece.setParameter2("param_value_2");
    	ece.setCounterNum(ce);
    	ece.setEnvironment(null);
        session.save(ece);
        envCounterID++;
		return ece;
	}

	public CounterEntity createCounterEntity(Session session) {
		CounterEntity ce = new CounterEntity();
    	ce.setCounterId(1);
    	ce.setCounterName("counter_1");
    	ce.setCounterDesc("counter_desc_1");
    	ce.setDelInd(0);
    	ce.setPosition(1);
        session.save(ce);
        counterID++;
		return ce;

	}

	public synchronized RegionEntity createRegionEntity(Session session, String regionName) {
		RegionEntity region = new RegionEntity();
		region.setRegionDesc("region_desc_1");
		region.setRegionName(regionName);
		region.setRegionLock(0);
		session.save(region);
		regionID++;
		return region;
	}
	
	public RegionEntity createRegionEntity(Session session) {
		return createRegionEntity(session, "region_1");
	}
	
	public HealthCheckTypeEntity createHealthCheckTypeEntity(Session session) {
		HealthCheckTypeEntity hcte = new HealthCheckTypeEntity();
		hcte.setHealthCheckTypeName("healthchecktype_1");
		hcte.setHealthCheckTypeDesc("healthchecktype_desc_1");
		hcte.setHealthCheckTypeClass("healtchecktype.class");
		session.save(hcte);
		healthCheckTypeID++;
		return hcte;
	}

    public HealthCheckVO createHealthCheck(String envName) {
    	return createHealthCheck(envName, ComponentType.APP.name());
    }
    
    public HealthCheckVO createHealthCheck(String envName, String compType) {
    	HealthCheckVO hc = getHealthCheck(envName, compType);
    	healthCheckDao.editHealthCheck(hc);
    	
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        HealthCheckParamEntity hcpe = new HealthCheckParamEntity();
        hcpe.setHealthCheck(session.get(HealthCheckEntity.class, new Integer(healthCheckID)));
        hcpe.setHealthCheckParamKey(hc.getHealthCheckParamList().get(0).getHealthCheckParamKey());
        hcpe.setHealthCheckParamVal(hc.getHealthCheckParamList().get(0).getHealthCheckParamValue());
        session.save(hcpe);
        healthCheckParamID++;
        
        tx.commit();
        session.close();

        healthCheckID++;
    	return hc;
    }

	public HealthCheckVO getHealthCheck(String envName) {
		return getHealthCheck(envName, ComponentType.APP.name());
	}
    
	public HealthCheckVO getHealthCheck(String envName, String componentType) {
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Environment env = createEnvironment(envName, 0);
    	ComponentType compType = createComponentType(componentType);
    	createComponent();
    	
    	Component component = getComponent();
    	component.setParentComponentId(TestDaoService.compID);
    	component.setParentComponentName("parent_comp_name_1");
    	component.setDelInd(0);
    	createComponent(component);
    	
    	RegionEntity region = createRegionEntity(session);
    	HealthCheckTypeEntity healthCheckType = createHealthCheckTypeEntity(session);
    	
    	
        tx.commit();
        session.close();

        HealthCheckVO hcv = new HealthCheckVO();
    	hcv.setComponentId(TestDaoService.compID);
    	hcv.setComponentName(component.getComponentName());
    	hcv.setComponentType(compType.componentTypeName());
    	hcv.setDelInd(false);
    	hcv.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
    	
    	HealthCheckParamVO hcp = new HealthCheckParamVO();
    	hcp.setHealthCheckParamKey("key_1");
    	hcp.setHealthCheckParamValue("value_1");
    	List<HealthCheckParamVO> hcps = new ArrayList<HealthCheckParamVO>();
    	hcps.add(hcp);
    	hcv.setHealthCheckParamList(hcps);
    	
    	hcv.setHealthCheckTypeId(TestDaoService.healthCheckTypeID);
    	hcv.setHealthCheckTypeName(healthCheckType.getHealthCheckTypeName());
    	hcv.setMaxRetryCount(3);
    	hcv.setParentComponentId(1);
    	hcv.setParentComponentName("parent_1");
    	hcv.setRegionId(TestDaoService.regionID);
    	hcv.setRegionName(region.getRegionName());
		return hcv;
	}
	
//	public ApiStatusEntity createApiStatusEntity(Session session, String envName) {
//		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		ApiStatusEntity apse = new ApiStatusEntity();
//		session = sessionFactory.openSession();
//		Transaction trx = session.beginTransaction();
//		
//		createEnvironment(envName, 0);
//		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
//		
//		createComponentType();
//		createComponent();
//		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
//		createComponent();
//		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
//		componentEntity.setParentComponent(parentComponentEntity);
//		parentCompID++;
//		session.save(componentEntity);
//		
//		apse.setEnvironment(envEntity);
//		apse.setComponent(componentEntity);
//		long date = System.currentTimeMillis();
//		apse.setStatsDate(new java.sql.Date(date));
//		apse.setDeltaValue(1);
//		apse.setTotalApi(1);
//		session.save(apse);
//		apiStatusID++;
//		trx.commit();
//		session.close();
//		return apse;
//
//	}
//	
//	
//	public AppRoleEntity createAppRoleEntity() {
//		AppRoleEntity appRoleEntity=new AppRoleEntity();
//		Session session = sessionFactory.openSession();
//		Transaction trx = session.beginTransaction();
//		ComponentTypeEntity cte = new ComponentTypeEntity();
//		cte.setComponentTypeId(ComponentType.APP.ordinal());
//		cte.setComponentTypeName(ComponentType.APP.name());
//		
//		AppLookUpEntity aluEntity = new AppLookUpEntity();
//		aluEntity.setApplookupId(1);
//		aluEntity.setComponentFullName("component_full_name_1");
//
//		ComponentEntity compEntity = new ComponentEntity();
//		compEntity.setAppLookUpEntity(aluEntity);
//		compEntity.setComponentId(1);
//		compEntity.setComponentName("component_1");
//		compEntity.setComponentType(cte);
//		compEntity.setComponentDesc("component_desc");
//		compEntity.setDelInd(1);
//
//		ComponentEntity parentComp = new ComponentEntity();
//		compEntity.setAppLookUpEntity(aluEntity);
//		compEntity.setComponentId(1);
//		compEntity.setComponentName("parent_component_1");
//		compEntity.setComponentType(cte);
//		compEntity.setParentComponent(parentComp);
//		appRoleEntity.setComponent(compEntity);
//		appRoleEntity.setAppRoleName("appRoleName");
//		session.save(appRoleEntity);
//		trx.commit();
//		session.close();
//		appRoleID++;
//		return appRoleEntity;
//	}
//	
//	public DaillyCompStatusEntity getDailyStatusEntity(String envName) {
//		DaillyCompStatusEntity daillyCompStatusEntity=new DaillyCompStatusEntity();		
//		Session session = sessionFactory.openSession();
//		Transaction trx = session.beginTransaction();
//		createHealthCheck(envName);
//		daillyCompStatusEntity.setHealthCheck(session.get(HealthCheckEntity.class, new Integer(healthCheckID)));	
//		
//		StatusEntity se=new StatusEntity();
//		se.setStatusId(1);
//		se.setStatusName("statusName");		
//		daillyCompStatusEntity.setStatus(se);		
//		
//		createComponentType();
//		createComponent();
//		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
//		createComponent();
//		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
//		componentEntity.setParentComponent(parentComponentEntity);
//		parentCompID++;
//		session.save(componentEntity);
//		
//		long date = System.currentTimeMillis();
//		daillyCompStatusEntity.setStatusDate(new java.sql.Date(date));
//	
//		session.save(se);
//		session.save(daillyCompStatusEntity);
//		dailyCompStatusID++;
//		trx.commit();
//		session.close();		
//		return daillyCompStatusEntity;
//		
//	}
//	
//	public K8sContainerStatusEntity createK8sContainerStatusEntity(Session session, String envName) {
//		K8sContainerStatusEntity k8sContainerStatusEntity=new K8sContainerStatusEntity();
//		session = sessionFactory.openSession();
//		Transaction trx = session.beginTransaction();
//		
//		createEnvironment(envName, 0);
//		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
//		
//		createComponentType();
//		createComponent();
//		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
//		createComponent();
//		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
//		componentEntity.setParentComponent(parentComponentEntity);
//		parentCompID++;
//		session.save(componentEntity);
//		
//		k8sContainerStatusEntity.setEnvironment(envEntity);
//		k8sContainerStatusEntity.setComponent(componentEntity);
//		long date = System.currentTimeMillis();
//		k8sContainerStatusEntity.setStatsDate(new java.sql.Date(date));
//		k8sContainerStatusEntity.setTotalContainer(1);
//		
//		session.save(k8sContainerStatusEntity);
//		k8sContainerStatsID++;
//		trx.commit();
//		session.close();
//		return k8sContainerStatusEntity;
//		
//	}
	
	public K8sApiStatusEntity createK8sApiStatusEntity(Session session,String envName) {
		K8sApiStatusEntity k8sApiStatusEntity=new K8sApiStatusEntity();
		session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		
		createEnvironment(envName, 0);
		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
		
		createComponentType();
		createComponent();
		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
		createComponent();
		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
		componentEntity.setParentComponent(parentComponentEntity);
		parentCompID++;
		session.save(componentEntity);
		k8sApiStatusEntity.setComponent(componentEntity);
		k8sApiStatusEntity.setEnvironment(envEntity);
		k8sApiStatusEntity.setDeltaValue(1);
		k8sApiStatusEntity.setTotalApi(1);
		long date = System.currentTimeMillis();
		k8sApiStatusEntity.setStatusDate(new java.sql.Date(date));
		k8sApiStatusID++;
		trx.commit();
		session.close();		
		
		return k8sApiStatusEntity;
		
	}
	//test
	public  K8sTpsLatencyHistoryEntity  createK8sTpsLatencyHistoryEntity(Session session, String envName) {
		K8sTpsLatencyHistoryEntity historyEntity=new K8sTpsLatencyHistoryEntity();
		session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		
		createEnvironment(envName, 0);
		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
		
		createComponentType();
		createComponent();
		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
		createComponent();
		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
		componentEntity.setParentComponent(parentComponentEntity);
		parentCompID++;
		session.save(componentEntity);
		historyEntity.setComponent(componentEntity);
		historyEntity.setEnvironment(envEntity);
		historyEntity.setLatencyValue(0.0);
		historyEntity.setTpsValue(0.0);
		long date = System.currentTimeMillis();
		historyEntity.setStatusDate(new java.sql.Date(date));
		tpsLatencyID++;
		trx.commit();
		session.close();		
		return historyEntity;
		
	}
	//test
	public TpsServiceEntity createTpsServiceEntity(Session session, String envName) {
		TpsServiceEntity tse=new TpsServiceEntity();
		session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		
		createEnvironment(envName, 0);
		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
		
		createComponentType();
		createComponent();
		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
		createComponent();
		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
		componentEntity.setParentComponent(parentComponentEntity);
		parentCompID++;
		session.save(componentEntity);
		tse.setComponent(componentEntity);
		tse.setEnvironment(envEntity);
		tse.setLatencyValue(0.0);
		tse.setTpsValue(0.0);
		tpsServiceID++;
		session.save(tse);
		trx.commit();
		session.close();
		return tse;
		
	}
	

	public ApiStatusEntity createApiStatusEntity(String envName) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ApiStatusEntity apse = new ApiStatusEntity();
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		
		createEnvironment(envName, 0);
		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
		
		createComponentType();
		createComponent();
		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
		createComponent();
		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
		componentEntity.setParentComponent(parentComponentEntity);
		parentCompID++;
		session.save(componentEntity);
		
		apse.setEnvironment(envEntity);
		apse.setComponent(componentEntity);
		long date = System.currentTimeMillis();
		apse.setStatusDate(new java.sql.Date(date));
		apse.setDeltaValue(1);
		apse.setTotalApi(1);
		session.save(apse);
		apiStatusID++;
		trx.commit();
		session.close();
		return apse;

	}
	//test
	public K8sPodsContainersEntity createK8sPodsStatusEntity(Session session, String envName) {
		K8sPodsContainersEntity k8sPStE=new K8sPodsContainersEntity();
		session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		
		createEnvironment(envName, 0);
		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
		
		createComponentType();
		createComponent();
		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
		createComponent();
		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
		componentEntity.setParentComponent(parentComponentEntity);
		parentCompID++;
		session.save(componentEntity);
		k8sPStE.setEnvironment(envEntity);
		k8sPStE.setComponent(componentEntity);
		long date = System.currentTimeMillis();
		k8sPStE.setStatusDate(new java.sql.Date(date));
		k8sPStE.setTotalPods(1);
		k8sPStE.setTotalContainers(1);
		session.save(k8sPStE);
		k8sPodsStatusID++;
		trx.commit();
		session.close();
		return k8sPStE;
		
	}
	
	
	//test
	public AppRoleEntity createAppRoleEntity() {
		AppRoleEntity appRoleEntity=new AppRoleEntity();
		Session session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		//appRoleEntity.setAppRoleId(1);
		ComponentTypeEntity cte = new ComponentTypeEntity();
		cte.setComponentTypeId(ComponentType.APP.ordinal());
		cte.setComponentTypeName(ComponentType.APP.name());
		
		AppLookUpEntity aluEntity = new AppLookUpEntity();
		aluEntity.setApplookupId(1);
		aluEntity.setComponentFullName("component_full_name_1");

		ComponentEntity compEntity = new ComponentEntity();
		compEntity.setAppLookUpEntity(aluEntity);
		compEntity.setComponentId(1);
		compEntity.setComponentName("component_1");
		compEntity.setComponentType(cte);
		compEntity.setComponentDesc("component_desc");
		compEntity.setDelInd(1);

		ComponentEntity parentComp = new ComponentEntity();
		compEntity.setAppLookUpEntity(aluEntity);
		compEntity.setComponentId(1);
		compEntity.setComponentName("parent_component_1");
		compEntity.setComponentType(cte);
		compEntity.setParentComponent(parentComp);
		appRoleEntity.setComponent(compEntity);
		appRoleEntity.setAppRoleName("appRoleName");
		session.save(appRoleEntity);
		trx.commit();
		session.close();
		appRoleID++;
		return appRoleEntity;
	}
	//test
	public DaillyCompStatusEntity createDailyStatusEntity(String envName) {
		createHealthCheck(envName);
		
		Session session = sessionFactory.openSession();		
		Transaction trx = session.beginTransaction();
		DaillyCompStatusEntity daillyCompStatusEntity=new DaillyCompStatusEntity();		
		daillyCompStatusEntity.setHealthCheck(session.get(HealthCheckEntity.class, new Integer(healthCheckID-1)));	

		StatusEntity se=new StatusEntity();
		se.setStatusId(1);
		se.setStatusName("statusName");
		session.save(se);

		daillyCompStatusEntity.setStatus(se);
		
		long date = System.currentTimeMillis();
		daillyCompStatusEntity.setStatusDate(new java.sql.Date(date));
	
		session.save(daillyCompStatusEntity);
		dailyCompStatusID++;
		trx.commit();
		session.close();		
		return daillyCompStatusEntity;
	}
	
	//test
	public ComponentMessages createComponentMessages(Session session, String envName, String region) {
		session = sessionFactory.openSession();
		
		createEnvironment(envName, 0);
		createComponentType();
		Component comp = createComponent();
		createComponent(comp);
		createRegionEntity(session, region);

		ComponentMessages cm = getComponentMessage();
		cm.setEnvironment(envName);
		cm.setComponentid(""+compID);
		cm.setRegion(region);
		regionMessageDao.saveCompMessage(cm);
		compMessageID++;
		session.close();
		return cm;
	}
	
	public ComponentMessages  getComponentMessage() {
		ComponentMessages  cm = new ComponentMessages ();
		cm.setAuthToken("authToken_1");
		cm.setMessage("message");
		cm.setUserid("userid");
		cm.setUsername("username");
		return cm;
	}
	
	//test
	public K8sPodsContainersEntity createK8sContainerStatusEntity(Session session, String envName) {
		K8sPodsContainersEntity k8sContainerStatusEntity=new K8sPodsContainersEntity();
		session = sessionFactory.openSession();
		Transaction trx = session.beginTransaction();
		
		createEnvironment(envName, 0);
		EnvironmentEntity envEntity = envDao.getEnvironmentFromName(envName);
		
		createComponentType();
		createComponent();
		ComponentEntity parentComponentEntity = session.get(ComponentEntity.class, compID);
		createComponent();
		ComponentEntity componentEntity = session.get(ComponentEntity.class, compID);
		componentEntity.setParentComponent(parentComponentEntity);
		parentCompID++;
		session.save(componentEntity);
		
		k8sContainerStatusEntity.setEnvironment(envEntity);
		k8sContainerStatusEntity.setComponent(componentEntity);
		long date = System.currentTimeMillis();
		k8sContainerStatusEntity.setStatusDate(new java.sql.Date(date));
		k8sContainerStatusEntity.setTotalContainers(1);
		
		session.save(k8sContainerStatusEntity);
		k8sContainerStatsID++;
		trx.commit();
		session.close();
		return k8sContainerStatusEntity;
		
	}

/*	public ComponentEntity getComponentEntity() {

		ComponentTypeEntity cte = new ComponentTypeEntity();
		cte.setComponentTypeId(ComponentType.APP.ordinal());
		cte.setComponentTypeName(ComponentType.APP.name());
		
		AppLookUpEntity aluEntity = new AppLookUpEntity();
		aluEntity.setApplookupId(1);
		aluEntity.setComponentFullName("component_full_name_1");

		ComponentEntity compEntity = new ComponentEntity();
		compEntity.setAppLookUpEntity(aluEntity);
		compEntity.setComponentId(1);
		compEntity.setComponentName("component_1");
		compEntity.setComponentType(cte);
		compEntity.setComponentDesc("component_desc");
		compEntity.setDelInd(1);

		ComponentEntity parentComp = new ComponentEntity();
		compEntity.setAppLookUpEntity(aluEntity);
		compEntity.setComponentId(1);
		compEntity.setComponentName("parent_component_1");
		compEntity.setComponentType(cte);
		
		AppRoleEntity are=new AppRoleEntity();
		are.setAppRoleId(1);
		are.setAppRoleName("appRoleName_1");
		are.setComponent(compEntity);

		compEntity.setParentComponent(parentComp);
		
		HealthCheckEntity hce=new HealthCheckEntity();
		hce.setComponent(compEntity);
		
		StatusEntity se=new StatusEntity();
		se.setStatusId(1);
		se.setStatusName("statusName");		
		hce.setCurrentStatus(se);
		return compEntity;

	}
    public EnvironmentMessages createEnvironmentmessages() {
    	EnvironmentMessages envMesg = new EnvironmentMessages();
    	envMesg.setAppMessage("appMessage");
    	envMesg.setGeneralMessage("generalMessage");
    	envMesg.setInfraMessage("infraMessage"); 
    	Session session=sessionFactory.openSession();
    	Transaction trx = session.beginTransaction();
    	session.save(envMesg);
    	trx.commit();
       	return envMesg;
  	}

	public EnvironmentEntity createEnvEntity() {
		EnvironmentEntity envty = new EnvironmentEntity();
		envty.setEnvironmentId(1);
		envty.setEnvLock(1);
		envty.setDisplayOrder(1);
		envty.setAppMessage("appMessage_1");
		envty.setEnvironmentDesc("environmentDesc_1");
		envty.setEnvironmentName("environmentName_1");
		envty.setGeneralMessage("generalMessage_1");
		envty.setMarathonCred("marathonCred_1");
		envty.setMarathonUrl("marathonUrl_1");
		return envty;
	}*/
}
