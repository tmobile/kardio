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
package com.tmobile.kardio.service;

import com.tmobile.kardio.bean.*;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.dao.*;
import com.tmobile.kardio.db.entity.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @Mock
    private AppLookUpDao appDao;
    @Mock
    private CountersDao counterDao;
    @Mock
    private ComponentDao compDao;
    @Mock
    private EnvironmentDao environmentDao;
    @Mock
    private AlertSubscribeDao alertSubscibeDao;
    @Mock
    private HealthCheckDao healthCheckDao;
    @Mock
    private AppSessionDao appSessionDao;
    @Mock
    private RegionStatusService regionStatusService;


    @InjectMocks
    private AdminServiceImpl adminService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAppFullName() throws Exception {
        AppFullName afn = new AppFullName();
        afn.setAppId(1);
        afn.setComponentId(1);
        afn.setComponentName("component_1");
        afn.setComponentFullName("component_full_name_1");
        doNothing().when(appDao).saveAppFullName(afn);
        adminService.saveAppFullName(afn);
        verify(appDao, times(1)).saveAppFullName(afn);
    }

    @Test
    public void testCreateComponent() throws Exception {
        Component component = new Component();
        component.setComponentId(1);
        component.setParentComponentId(1);
        component.setComponentName("component_name");
        doNothing().when(compDao).saveComponent(component);
        adminService.saveComponent(component);
        verify(compDao, times(1)).saveComponent(component);
    }

    @Test
    public void testGetAllComponent() throws Exception {
        List<String> skipComps = new ArrayList();
        skipComps.add("test");
        adminService.setSkippableInfraComponents(skipComps);
        AppLookUpEntity aluEntity = new AppLookUpEntity();
        aluEntity.setApplookupId(1);
        aluEntity.setComponentFullName("component_full_name_1");

        ComponentTypeEntity cte = new ComponentTypeEntity();
        cte.setComponentTypeId(ComponentType.APP.ordinal());
        cte.setComponentTypeName(ComponentType.APP.name());

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

        List<ComponentEntity> componentEntities = new ArrayList<ComponentEntity>();
        componentEntities.add(compEntity);

        when(compDao.getComponents()).thenReturn(componentEntities);
        Component expected = new Component();
        expected.setComponentId(1);
        expected.setComponentName("parent_component_1");

        List<Component> result = adminService.getAllComponent();
        verify(compDao, times(1)).getComponents();
        verifyNoMoreInteractions(compDao);

        Assert.assertEquals("Number of entries does not match", 1, result.size());
        Component actual = result.get(0);
        Assert.assertEquals("Component ID does not match", expected.getComponentId(), actual.getComponentId());
        Assert.assertEquals("Component Name does not match", expected.getComponentName(), actual.getComponentName());

    }

    @Test
    public void testGetAppFullName() throws Exception {
        AppLookUpEntity aluEntity = new AppLookUpEntity();
        aluEntity.setApplookupId(1);
        aluEntity.setComponentFullName("component_full_name_1");

        ComponentTypeEntity cte = new ComponentTypeEntity();
        cte.setComponentTypeId(ComponentType.APP.ordinal());
        cte.setComponentTypeName(ComponentType.APP.name());

        ComponentEntity compEntity = new ComponentEntity();
        compEntity.setAppLookUpEntity(aluEntity);
        compEntity.setComponentId(1);
        compEntity.setComponentName("component_1");
        compEntity.setComponentType(cte);
        compEntity.setComponentDesc("component_desc");
        compEntity.setDelInd(1);
        compEntity.setParentComponent(null);

        List<ComponentEntity> compEntities = new ArrayList<ComponentEntity>();
        compEntities.add(compEntity);

        List<AppLookUpEntity> aluEntities = new ArrayList<AppLookUpEntity>();
        aluEntity.setComponent(compEntity);
        aluEntities.add(aluEntity);


        when(appDao.getAppFullNameWithAppId()).thenReturn(aluEntities);
        when(compDao.getAppParentComponents()).thenReturn(compEntities);

        AppFullName expected = new AppFullName();
        expected.setAppId(1);
        expected.setComponentId(1);
        expected.setComponentName("component_1");
        expected.setComponentFullName("component_full_name_1");

        List<AppFullName> result = adminService.getAppFullName();
        verify(appDao, times(1)).getAppFullNameWithAppId();
        verify(compDao, times(1)).getAppParentComponents();
        verifyNoMoreInteractions(appDao);
        verifyNoMoreInteractions(compDao);

        Assert.assertEquals("Number of entries does not match", 1, result.size());

        AppFullName actual = result.get(0);
        Assert.assertEquals("ID does not match", expected.getAppId(), actual.getAppId());
        Assert.assertEquals("Component ID does not match", expected.getComponentId(), actual.getComponentId());
        Assert.assertEquals("Component Name does not match", expected.getComponentName(), actual.getComponentName());
        Assert.assertEquals("Component Full Name does not match", expected.getComponentFullName(), actual.getComponentFullName());

    }

    @Test
    public void testEditComponent() throws Exception {
        Component component = new Component();
        component.setComponentId(1);
        component.setParentComponentId(1);
        component.setComponentName("component_name");
        doNothing().when(compDao).editComponent(component);
        adminService.editComponent(component);
        verify(compDao, times(1)).editComponent(component);

    }

    @Test
    public void testDeleteComponent() throws Exception {
        Component component = new Component();
        component.setComponentId(1);
        component.setParentComponentId(1);
        component.setComponentName("component_name");
        doNothing().when(compDao).deleteComponent(component);
        adminService.deleteComponent(component);
        verify(compDao, times(1)).deleteComponent(component);

    }

    @Test
    public void testEditAppFullName() throws Exception {
        AppFullName afn = new AppFullName();
        afn.setAppId(1);
        afn.setComponentId(1);
        afn.setComponentName("component_1");
        afn.setComponentFullName("component_full_name_1");
        doNothing().when(appDao).editAppFullName(afn);
        adminService.editAppFullName(afn);
        verify(appDao, times(1)).editAppFullName(afn);
    }

    @Test
    public void testDeleteAppFullName() throws Exception {
        AppFullName afn = new AppFullName();
        afn.setAppId(1);
        afn.setComponentId(1);
        afn.setComponentName("component_1");
        afn.setComponentFullName("component_full_name_1");
        doNothing().when(appDao).deleteAppFullName(afn);
        adminService.deleteAppFullName(afn);
        verify(appDao, times(1)).deleteAppFullName(afn);
    }

    @Test
    public void testDeleteHealthCheck() throws Exception {
        int healthCheckId = 1;
        doNothing().when(healthCheckDao).deleteHealthCheck(healthCheckId);
        adminService.deleteHealthCheck(healthCheckId);
        verify(healthCheckDao, times(1)).deleteHealthCheck(healthCheckId);
    }

    @Test
    public void testAddEnvironment() throws Exception {
        Environment env = new Environment();
        env.setEnvironmentId(1);
        env.setEnvLock(1);
        env.setEnvironmentName("environmentName_1");
        doNothing().when(environmentDao).addEnvironment(env);
        adminService.addEnvironment(env);
        verify(environmentDao, times(1)).addEnvironment(env);
    }

    @Test
    public void testEditHealthCheck() throws Exception {
        HealthCheckVO hc = new HealthCheckVO();
        hc.setComponentId(1);
        hc.setEnvironmentId(1);
        hc.setHealthCheckId(1);
        hc.setMaxRetryCount(1);
        hc.setHealthCheckTypeId(1);
        hc.setHealthCheckTypeName("healthCheckTypeName_1");
        List<HealthCheckParamVO> healthCheckParamList = new ArrayList<HealthCheckParamVO>();
        HealthCheckParamVO checkParamVO = new HealthCheckParamVO();
        checkParamVO.setHealthCheckParamId(1);
        healthCheckParamList.add(checkParamVO);
        doNothing().when(healthCheckDao).editHealthCheck(hc);
        adminService.editHealthCheck(hc);
        verify(healthCheckDao, times(1)).editHealthCheck(hc);
    }

    @Test
    public void getAllHealthChecks() throws Exception {
        HealthCheckVO hc = new HealthCheckVO();
        List<HealthCheckVO> healthChecks = new ArrayList<HealthCheckVO>();
        healthChecks.add(hc);
        hc.setComponentId(1);
        hc.setEnvironmentId(1);
        hc.setHealthCheckId(1);
        hc.setMaxRetryCount(1);
        hc.setHealthCheckTypeId(1);
        hc.setHealthCheckTypeName("healthCheckTypeName_1");
        List<HealthCheckParamVO> healthCheckParamList = new ArrayList<HealthCheckParamVO>();
        HealthCheckParamVO checkParamVO = new HealthCheckParamVO();
        checkParamVO.setHealthCheckParamId(1);
        healthCheckParamList.add(checkParamVO);

        when(healthCheckDao.getAllHealthCheckDetails()).thenReturn(healthChecks);
        List<HealthCheckVO> result = adminService.getAllHealthChecks();
        verify(healthCheckDao, times(1)).getAllHealthCheckDetails();
        verifyNoMoreInteractions(healthCheckDao);

        Assert.assertEquals("Number of entries does not match", 1, result.size());

    }

    @Test
    public void testAllGlobalSubscription() throws Exception {
        AppLookUpEntity aluEntity = new AppLookUpEntity();
        aluEntity.setApplookupId(1);
        aluEntity.setComponentFullName("component_full_name_1");

        ComponentTypeEntity cte = new ComponentTypeEntity();
        cte.setComponentTypeId(ComponentType.APP.ordinal());
        cte.setComponentTypeName(ComponentType.APP.name());

        ComponentEntity compEntity = new ComponentEntity();
        compEntity.setAppLookUpEntity(aluEntity);
        compEntity.setComponentId(1);
        compEntity.setComponentName("component_1");
        compEntity.setComponentType(cte);
        compEntity.setComponentDesc("component_desc");
        compEntity.setDelInd(1);

        EnvironmentEntity env = new EnvironmentEntity();
        env.setEnvironmentId(1);
        env.setDisplayOrder(1);
        env.setEnvironmentName("environmentName_1");

        AlertSubscriptionEntity ase = new AlertSubscriptionEntity();
        ase.setComponent(compEntity);
        ase.setAlertSubscriptionId(1);
        ase.setEnvironment(env);
        ase.setSubscriptionType(1);

        Subscription subs = new Subscription();
        subs.setComponentId(1);
        subs.setEnvironmentId(1);
        subs.setGlobalSubscriptionTypeId(1);
        subs.setAlertSubscriptionId(1);
        subs.setSubsciptionVal("subsciptionVal_1");
        List<Subscription> subsls = new ArrayList<Subscription>();
        subsls.add(subs);

        List<ComponentEntity> componentEntities = new ArrayList<ComponentEntity>();
        componentEntities.add(compEntity);

        List<EnvironmentEntity> envs = new ArrayList<EnvironmentEntity>();
        envs.add(env);
        List<AlertSubscriptionEntity> asls = new ArrayList<AlertSubscriptionEntity>();
        asls.add(ase);
        when(regionStatusService.makeSubscriptionList(asls)).thenReturn(subsls);
        adminService.getAllGlobalSubscriptions();
        verify(alertSubscibeDao, times(1)).getAllGlobalSubscriptions();
    }


    @Test
    public void testGetEnvironmentLock() throws Exception {
        List<EnvironmentEntity> entities = new ArrayList<EnvironmentEntity>();
        EnvironmentEntity environmentEntity = new EnvironmentEntity();
        environmentEntity.setEnvironmentId(1);
        environmentEntity.setEnvironmentName("environmentName_1");
        entities.add(environmentEntity);
        when(environmentDao.getEnvironmentWithLock()).thenReturn(entities);
        adminService.getEnvironmentLock();
        verify(environmentDao, times(1)).getEnvironmentWithLock();
        verifyNoMoreInteractions(environmentDao);
    }

    @Test
    public void testDoEnvironmentLock() throws Exception {
        Environment env = new Environment();
        env.setEnvironmentId(1);
        env.setDisplayOrder(1);
        env.setEnvLock(1);
        env.setEnvironmentDesc("new environment Desc");
        env.setEnvironmentName("new environment Name");
        env.setMarathonPassword("new marathon Password");
        env.setMarathonUrl("new marathon Url");
        env.setMarathonUserName("new marathon UserName");
        doNothing().when(environmentDao).updateEnvironment(env);
        adminService.doEnvironmentLock(env);
        verify(environmentDao, times(1)).updateEnvironment(env);
    }

    @Test
    public void testEditEnvCounterDetails() throws Exception {
        EnvCounters envc = new EnvCounters();
        envc.setEnvCounterId(1);
        envc.setMetricTypeId(1);
        envc.setCounterName("new counterName");
        envc.setParameter1("new parameter1");
        envc.setParameter2("new parameter2");
        doNothing().when(counterDao).editEnvCounterDetails(envc);
        adminService.editEnvCounterDetails(envc);
        verify(counterDao, times(1)).editEnvCounterDetails(envc);
    }

    @Test
    public void testEditCounters() throws Exception {
        Counters counters = new Counters();
        List<Counters> cntlst = new ArrayList<Counters>();
        counters.setCounterId(1);
        counters.setDelInd(1);
        counters.setPosition(1);
        counters.setCounterName("new counterName");
        counters.setCounterDesc("new counterDesc");
        counters.setMetricDate("metricDate");
        counters.setMetricVal(1F);
        List<Float> trend = new ArrayList<Float>();
        trend.add(1F);
        counters.setTrend(trend);
        cntlst.add(counters);
        doNothing().when(counterDao).editCounters(cntlst);
        adminService.editCounter(cntlst);
        verify(counterDao, times(1)).editCounters(cntlst);
    }

    @Test
    public void testGetAllCounters() {
        CounterDetails counterDetails = new CounterDetails();
        List<Counters> cts = new ArrayList<Counters>();
        Counters cn = new Counters();
        cn.setCounterId(1);
        cn.setDelInd(1);
        cn.setMetricVal(1.0F);
        cn.setMetricDate("new Matric date");
        cn.setPosition(1);
        cn.setCounterDesc("new counter Desc");
        cn.setCounterName("new counter Name");
        List<Float> trend = new ArrayList<Float>();
        trend.add(1F);
        cn.setTrend(trend);
        cts.add(cn);
        counterDetails.setCounter(cts);
        List<EnvCounters> encd = new ArrayList<EnvCounters>();
        EnvCounters envCounters = new EnvCounters();
        envCounters.setEnvCounterId(1);
        envCounters.setMetricTypeId(1);
        envCounters.setCounterName("new counterName");
        envCounters.setParameter1("new parameter1");
        envCounters.setParameter2("new parameter2");
        envCounters.setEnvName("new envName");
        encd.add(envCounters);
        counterDetails.setEnvCounters(encd);
        when(counterDao.getAllCountersDetails()).thenReturn(counterDetails);
        counterDetails = adminService.getAllCounters();
        verify(counterDao, times(1)).getAllCountersDetails();
        verifyNoMoreInteractions(counterDao);
    }

    @Test
    public void getAllHealthCheckTypes() throws Exception {
        HealthCheckTypeVO typeVO = new HealthCheckTypeVO();
        List<HealthCheckTypeVO> checkTypeVOs = new ArrayList<HealthCheckTypeVO>();
        checkTypeVOs.add(typeVO);
        typeVO.setHealthCheckTypeId(1);
        typeVO.setHealthCheckTypeDesc("new healthCheck TypeDesc");
        typeVO.setHealthCheckTypeName("new healthCheck TypeName");

        when(healthCheckDao.getAllHealthCheckTypes()).thenReturn(checkTypeVOs);
        adminService.getAllHealthCheckTypes();
        verify(healthCheckDao, times(1)).getAllHealthCheckTypes();
        verifyNoMoreInteractions(healthCheckDao);

    }

    @Test
    public void testDeleteGlobalSubscription() throws Exception {
        Subscription gs = new Subscription();
        gs.setComponentId(1);
        gs.setEnvironmentId(1);
        gs.setAlertSubscriptionId(1);
        gs.setGlobalSubscriptionTypeId(1);
        gs.setComponentName("new component name");
        gs.setSubsciptionVal("new subscription value");
        gs.setSubsciptionType("new subscription type");
        gs.setGlobalSubscriptionType("new globalSubscriptionType");
        gs.setGlobalSubscriptionTypeId(1);
        gs.setAuthToken("auth_token_1");
        gs.setSubsciptionVal("new subsciptionVal");
        doNothing().when(alertSubscibeDao).deleteGlobalSubscription(gs.getAlertSubscriptionId());
        adminService.deleteGlobalSubscription(gs.getAlertSubscriptionId());
        verify(alertSubscibeDao, times(1)).deleteGlobalSubscription(gs.getAlertSubscriptionId());

    }

    @Test
    public void testGetAppSession() throws Exception {
        String authToken = "authToken_1";
        AppSessionEntity ase = new AppSessionEntity();
        ase.setAppSessionId(1);
        ase.setUserId("userId_1");
        ase.setAuthToken("authToken_1");
        ase.setPermission("permission_1");
        ase.setSessionStartTime(new Date());
        ase.setUserName("userName_1");
        ase.setIsAdmin(true);
        when(appSessionDao.getAppSession(authToken)).thenReturn(ase);
        adminService.getAppSession(authToken);
        verify(appSessionDao, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(appSessionDao);
    }
/*	//@Test
	public void testCheckForDuplicate() throws Exception{
		   Subscription subs=new Subscription();
	       subs.setComponentId(1);
	       subs.setEnvironmentId(1);
	       subs.setGlobalSubscriptionTypeId(1);
	       subs.setAlertSubscriptionId(1);
	       subs.setSubsciptionVal("subsciptionVal_1");
	       when(alertSubscibeDao.checkforDuplicates(subs.getComponentId(), subs.getEnvironmentId(), subs.getSubsciptionVal())).thenReturn(null);
			verify(alertSubscibeDao, times(1)).checkforDuplicates(subs.getComponentId(), subs.getEnvironmentId(), subs.getSubsciptionVal());
			verifyNoMoreInteractions(alertSubscibeDao);
	}
	public void testConvertHealthCheckEntity() throws Exception{
		AppLookUpEntity aluEntity = new AppLookUpEntity();
    	aluEntity.setApplookupId(1);
    	aluEntity.setComponentFullName("component_full_name_1");
    	
    	ComponentTypeEntity cte = new ComponentTypeEntity();
    	cte.setComponentTypeId(ComponentType.APP.ordinal());
    	cte.setComponentTypeName(ComponentType.APP.name());
    	
		ComponentEntity compEntity=new ComponentEntity();
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
    	
		EnvironmentEntity environment=new EnvironmentEntity();
		environment.setEnvironmentId(1);
		environment.setEnvLock(1);
		environment.setEnvironmentName("environmentName_");
		
		HealthCheckTypeEntity healthCheckType=new HealthCheckTypeEntity();
		
		healthCheckType.setHealthCheckTypeId(1);
		healthCheckType.setHealthCheckTypeName("healthCheckTypeName_1");
		
		
		HealthCheckEntity hc=new HealthCheckEntity();
		hc.setEnvironment(environment);
		
		HealtCheckEnvironment hcEnvironment = new HealtCheckEnvironment();
		hcEnvironment.setEnvName("environmentName_");
		hcEnvironment.setCreatedDate(new Date());
		
		List<HealtCheckEnvironment> hcEnvironments = new ArrayList<HealtCheckEnvironment>();
		hcEnvironments.add(hcEnvironment);
		
		Component component=new Component();
		component.setComponentId(1);
		component.setDelInd(1);
		component.setParentComponentId(1);
		component.setComponentName("componentName_1");
		adminService.convertComponentEntity(compEntity);			
	}	*/

    @Test
    public void testSubscribeGlobalAlert() throws Exception {
        Subscription subs = new Subscription();
        subs.setComponentId(1);
        subs.setEnvironmentId(1);
        subs.setGlobalSubscriptionTypeId(1);
        subs.setAlertSubscriptionId(1);
        subs.setSubsciptionVal("subsciptionVal_1");
        subs.setSubsciptionType("subsciptionType_1");
        int subsType = 0;
        int validationLevel = 1;
        boolean isGlobalSubscription = true;
        doNothing().when(regionStatusService).subscribeAlert(subs);
        adminService.subscribeGlobalAlert(subs);
        verify(alertSubscibeDao, times(1)).saveSubscription(subs, subsType, validationLevel, isGlobalSubscription);
    }

}
