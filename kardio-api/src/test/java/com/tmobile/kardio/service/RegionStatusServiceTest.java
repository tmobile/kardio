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

import com.tmobile.kardio.TestDataProvider;
import com.tmobile.kardio.bean.*;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.dao.*;
import com.tmobile.kardio.db.entity.*;
import com.tmobile.kardio.exceptions.ValidationFailedException;
import com.tmobile.kardio.util.MailSenderUtil;
import org.hibernate.SessionFactory;
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
public class RegionStatusServiceTest {

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
    private RegionHistoryDao regionHistoryDao;
    @Mock
    private RegionStatusDao regStatusDao;
    @Mock
    private ApiStatusDao apiStatusDao;
    @Mock
    private K8sApiStatusDao k8sApiStatusDao;
    @Mock
    private AvailabilityPercentageDao availabilityPercentageDao;
    @Mock
    private ContainerStatusDao containerStatusDao;
    @Mock
    private K8sContainerStatusDao k8sContainerStatusDao;
    @Mock
    private RegionMessageDao regionMessageDao;
    @Mock
    private CounterMatrixDao counterMatrixDao;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private MailSenderUtil mailSenderUtil;


    @InjectMocks
    private RegionStatusServiceImpl regionStatusService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoLoginForUser() throws Exception {
        User user = new User();
        user.setUserId("user_id");
        user.setUserName("user_name");
        user.setEmailId("email_id");
        user.setPassword("user_password");
        user.setTimeOutMinute("timeOut_Minute");
        user.setAuthToken("auth Token");
        user.setAdmin(true);
        doNothing().when(appSessionDao).saveAppSession(user);
        regionStatusService.doLoginForUser(user);
        verify(appSessionDao, times(1)).saveAppSession(user);
    }

    @Test
    public void testConfirmSubscription() throws Exception {
        String authToken = "authToken_1";
        doNothing().when(alertSubscibeDao).confirmSubscription(authToken);
        regionStatusService.confirmSubscription(authToken);
        verify(alertSubscibeDao, times(1)).confirmSubscription(authToken);
    }

    @Test
    public void testDeleteSubscription() throws Exception {
        String authToken = "authToken_1";
        doNothing().when(alertSubscibeDao).deleteSububscription(authToken);
        regionStatusService.deleteSubscription(authToken);
        verify(alertSubscibeDao, times(1)).deleteSububscription(authToken);
    }

    @Test
    public void testDoLogoutForUser() throws Exception {
        String authToken = "authToken_1";
        doNothing().when(appSessionDao).deleteAppSession(authToken);
        regionStatusService.doLogOutForUser(authToken);
        verify(appSessionDao, times(1)).deleteAppSession(authToken);
    }

    @Test
    public void testGetEnvironments() throws Exception {
        List<EnvironmentEntity> envLists = new ArrayList<EnvironmentEntity>();
        EnvironmentEntity entity = new EnvironmentEntity();
        entity.setEnvironmentId(1);
        entity.setAppMessage("appMessage_1");
        entity.setDisplayOrder(1);
        entity.setEnvironmentDesc("environmentDesc_1");
        entity.setEnvLock(1);
        entity.setGeneralMessage("generalMessage_1");
        entity.setInfraMessage("infraMessage_1");
        entity.setMarathonCred("marathonCred_1");
        entity.setMarathonUrl("marathonUrl_1");
        envLists.add(entity);
        List<Environment> envList = new ArrayList<Environment>();
        Environment environment = new Environment();
        environment.setEnvironmentId(1);
        environment.setDisplayOrder(1);
        environment.setEnvLock(1);
        environment.setEnvironmentName("environmentName_1");
        environment.setMarathonUserName("marathonUserName_1");
        environment.setMarathonUrl("marathonUrl_1");
        environment.setEnvironmentDesc("environmentDesc_1");
        environment.setMarathonPassword("marathonPassword_1");
        envList.add(environment);
        when(environmentDao.getEnvironments()).thenReturn(envLists);
        regionStatusService.getEnvironments();
        verify(environmentDao, times(1)).getEnvironments();
        verifyNoMoreInteractions(environmentDao);
    }

    @Test
    public void testGetRegionStatusHistory() throws Exception {
        String environment = "environment_1";
        HistoryResponse hr = new HistoryResponse();
        List<ComponentHistory> appHistory = new ArrayList<ComponentHistory>();
        List<ComponentHistory> apiHistory = new ArrayList<ComponentHistory>();
        List<ComponentHistory> infraHistory = new ArrayList<ComponentHistory>();
        List<StatusHistory> statusHistory = new ArrayList<StatusHistory>();
        ComponentHistory appH = new ComponentHistory();
        StatusHistory history = new StatusHistory();
        appH.setComponentId(1);
        appH.setParentComponentId(1);
        appH.setComponentName("component_Name");
        appH.setChildComponentName("child_ComponentName");
        appH.setComponentType("component_Type");
        appH.setParentComponentName("parent_ComponentName");
        history.setPercentageUpTimeEast(0.0F);
        history.setPercentageUpTimeWest(0.0F);
        history.setStatusEast("status_East");
        history.setStatusTime("statusTime");
        history.setStatusWest("status_West");
        appH.setStatusHistory(statusHistory);
        appHistory.add(appH);
        ComponentHistory apiH = new ComponentHistory();
        StatusHistory history1 = new StatusHistory();
        apiH.setComponentId(1);
        apiH.setParentComponentId(1);
        apiH.setComponentName("component_Name");
        apiH.setChildComponentName("child_ComponentName");
        apiH.setComponentType("component_Type");
        apiH.setParentComponentName("parent_ComponentName");
        history1.setPercentageUpTimeEast(0.0F);
        history1.setPercentageUpTimeWest(0.0F);
        history1.setStatusEast("status_East");
        history1.setStatusTime("statusTime");
        history1.setStatusWest("status_West");
        apiH.setStatusHistory(statusHistory);
        apiHistory.add(apiH);
        statusHistory.add(history);
        ComponentHistory infrH = new ComponentHistory();
        StatusHistory history2 = new StatusHistory();
        infrH.setComponentId(1);
        infrH.setParentComponentId(1);
        infrH.setComponentName("component_Name");
        infrH.setChildComponentName("child_ComponentName");
        infrH.setComponentType("component_Type");
        infrH.setParentComponentName("parent_ComponentName");
        history2.setPercentageUpTimeEast(0.0F);
        history2.setPercentageUpTimeWest(0.0F);
        history2.setStatusEast("status_East");
        history2.setStatusTime("statusTime");
        history2.setStatusWest("status_West");
        infraHistory.add(infrH);
        infrH.setStatusHistory(statusHistory);
        hr.setApiHistory(apiHistory);
        hr.setAppHistory(appHistory);
        hr.setInfraHistory(infraHistory);
        when(regionHistoryDao.getRegionStatusHistory(environment)).thenReturn(hr);
        regionStatusService.getRegionStatusHistory(environment);
        verify(regionHistoryDao, times(1)).getRegionStatusHistory(environment);
        verifyNoMoreInteractions(regionHistoryDao);
    }

    @Test
    public void testGetComponentRegion() throws Exception {
        String environment = "environment_1";
        StatusResponse sr = new StatusResponse();
        EnvironmentMessages envMessages = new EnvironmentMessages();
        envMessages.setAppMessage("app_Message");
        envMessages.setGeneralMessage("general_Message");
        envMessages.setInfraMessage("infra_Message");
        sr.setEnvMessages(envMessages);
        List<Component> apiComponent = new ArrayList<Component>();
        List<Component> appComponents = new ArrayList<Component>();
        List<Component> infraComponents = new ArrayList<Component>();
        Component apicomp = new Component();
        apicomp.setComponentId(1);
        apiComponent.add(apicomp);
        sr.setApiComponents(apiComponent);
        Component appComp = new Component();
        appComp.setComponentId(1);
        appComp.setAppFullName("app FullName");
        appComponents.add(appComp);
        sr.setAppComponents(appComponents);
        Component infrComp = new Component();
        infrComp.setComponentId(1);
        infrComp.setComponentName("component_Name");
        infraComponents.add(infrComp);
        sr.setInfraComponents(infraComponents);
        when(regStatusDao.getCompRegStatus(environment)).thenReturn(sr);
        regionStatusService.getComponentRegion(environment);
        verify(regStatusDao, times(1)).getCompRegStatus(environment);
        verifyNoMoreInteractions(regStatusDao);
    }

    @Test
    public void testGetCurrentApi() throws Exception {
        String environment = "environment_1";
        String componentIdsStrg = "componentIdsStrg_1";
        long currentApis = 1;
        when(apiStatusDao.getCurrentNumberOfApis(1, componentIdsStrg)).thenReturn(currentApis);
        when(k8sApiStatusDao.getCurrentNumberOfApis(1, componentIdsStrg)).thenReturn(0L);
        regionStatusService.getCurrentApi(environment, componentIdsStrg, TestDataProvider.getPlatform());
        verify(apiStatusDao, times(1)).getCurrentNumberOfApis(0, componentIdsStrg);
        verifyNoMoreInteractions(apiStatusDao);

    }

    @Test
    public void testGetAvailabilityPercentage() throws Exception {
        String environment = "environment_1";
        String interval = "interval_1";
        AvailabilityData avd = new AvailabilityData();
        avd.setAvailabilityPercentageEast(0.0);
        avd.setAvailabilityPercentageWest(0.0);
        avd.setComponentId(1);
        List<AvailabilityData> availabilityDatas = new ArrayList<AvailabilityData>();
        availabilityDatas.add(avd);
        when(availabilityPercentageDao.getAllAvailabilityPercentage(environment, interval, TestDataProvider.DEFAULT_PLATFORM, TestDataProvider.DEFAULT_REGION)).thenReturn(availabilityDatas);
        regionStatusService.getAvailabilityPercentage(environment, interval, TestDataProvider.DEFAULT_PLATFORM, TestDataProvider.DEFAULT_REGION);
        verify(availabilityPercentageDao, times(1)).getAllAvailabilityPercentage(environment, interval, TestDataProvider.DEFAULT_PLATFORM, TestDataProvider.DEFAULT_REGION);
        verifyNoMoreInteractions(availabilityPercentageDao);
    }

    @Test
    public void testGetCurrentContainer() throws Exception {
        String environment = "environment_1";
        String componentIdsStrg = "componentIdsStrg_1";
        Environment env = new Environment();
        env.setEnvironmentName(environment);
        boolean isParentComponents = true;
        long currentApis = 1;
        when(containerStatusDao.getCurrentNumberOfContainsers(env.getEnvironmentId(), componentIdsStrg, isParentComponents)).thenReturn(currentApis);
        regionStatusService.getCurrentContainer(environment, componentIdsStrg, isParentComponents, "Mesos");
        verify(containerStatusDao, times(1)).getCurrentNumberOfContainsers(env.getEnvironmentId(), componentIdsStrg, isParentComponents);
        verifyNoMoreInteractions(containerStatusDao);
    }

    @Test
    public void testGetComponentMessage() throws Exception {
        String environmentName = "environment";
        String componentId = "componentId";
        String region = "region";
        Messages messages = new Messages();
        messages.setMessageId(1);
        messages.setComponentName("component_Name");
        messages.setMessageDate("message_Date");
        messages.setUserId("user_Id");
        messages.setMessage("new_message");
        List<Messages> list = new ArrayList<Messages>();
        list.add(messages);
        when(regionMessageDao.getCompRegionMessage(environmentName, componentId, region)).thenReturn(list);
        regionStatusService.getComponentMessage(environmentName, componentId, region);
        verify(regionMessageDao, times(1)).getCompRegionMessage(environmentName, componentId, region);
        verifyNoMoreInteractions(regionMessageDao);
    }

    @Test
    public void testGetComponentMessages() throws Exception {
        String environmentName = "environment";
        String componentId = "componentId";
        String region = "region";
        String logDate = "logDate";
        Messages messages = new Messages();
        messages.setMessageId(1);
        messages.setComponentName("component_Name");
        messages.setMessageDate("message_Date");
        messages.setUserId("user_Id");
        messages.setMessage("new_message");
        List<Messages> list = new ArrayList<Messages>();
        list.add(messages);
        when(regionMessageDao.getCompRegionMessage(environmentName, componentId, region, logDate)).thenReturn(list);
        regionStatusService.getComponentMessage(environmentName, componentId, region, logDate);
        verify(regionMessageDao, times(1)).getCompRegionMessage(environmentName, componentId, region, logDate);
        verifyNoMoreInteractions(regionMessageDao);
    }

    @Test
    public void testGetSubscribedEmailIdList() throws Exception {
        AlertSubscriptionEntity ase = new AlertSubscriptionEntity();
        List<AlertSubscriptionEntity> aseList = new ArrayList<AlertSubscriptionEntity>();
        EnvironmentEntity envE = new EnvironmentEntity();
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
        envE.setEnvironmentId(1);
        envE.setAppMessage("appMessage_1");
        envE.setDisplayOrder(1);
        envE.setEnvironmentDesc("environmentDesc_1");
        envE.setEnvironmentName("environmentName_1");
        envE.setEnvLock(1);
        envE.setInfraMessage("infraMessage_1");
        envE.setMarathonCred("marathonCred_1");
        envE.setMarathonUrl("marathonUrl_1");

        ase.setEnvironment(envE);
        ase.setAlertSubscriptionId(1);
        ase.setAuthToken("authToken_1");
        ase.setComponent(compEntity);
        ase.setGlobalComponentTypeId(1);
        ase.setSubscriptionType(1);
        ase.setSubscriptionVal("subscriptionVal_1");
        ase.setValidationLevel(1);
        aseList.add(ase);

        int componentId = 1;
        String environmentName = "environment";
        int environmentId = 1;
        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(componentId);
        when(alertSubscibeDao.getSubscribedEmailIdList(componentId, environmentId)).thenReturn(aseList);
        regionStatusService.getSubscribedEmailIdList(componentId, environmentName);
        verify(alertSubscibeDao, times(1)).getSubscribedEmailIdList(componentId, environmentId);
        verifyNoMoreInteractions(alertSubscibeDao);


    }

    @Test
    public void testGetCountersMatrix() throws Exception {
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
        String environment = "environment";
        if (environment == null || environment.equals("") || environment.trim().length() == 1) {
            when(counterMatrixDao.getCounters(TestDataProvider.getPlatform())).thenReturn(cntlst);
            regionStatusService.getCountersMatrix(environment, TestDataProvider.getPlatform());
            verify(counterMatrixDao, times(1)).getCounters(TestDataProvider.getPlatform());
            verifyNoMoreInteractions(counterMatrixDao);
        } else {
//        	 int envId=1;
//        	 when(environmentDao.getEnironmentIdFromName(environment)).thenReturn(envId);        	
            when(counterMatrixDao.getEnvironmentCounters(environment, TestDataProvider.getPlatform())).thenReturn(cntlst);
            regionStatusService.getCountersMatrix(environment, TestDataProvider.getPlatform());
            verify(counterMatrixDao, times(1)).getEnvironmentCounters(environment, TestDataProvider.getPlatform());
            verifyNoMoreInteractions(counterMatrixDao);
        }
    }

    @Test
    public void testLoadMessages() throws Exception {
        AppSessionEntity appSessionEntity = new AppSessionEntity();
        appSessionEntity.setAppSessionId(1);
        appSessionEntity.setAuthToken("authToken_1");
        appSessionEntity.setIsAdmin(true);
        appSessionEntity.setPermission("permission_1");
        appSessionEntity.setSessionStartTime(new Date());
        appSessionEntity.setUserId("userId_1");
        appSessionEntity.setUserName("userName_1");
        String authToken = "authToken_1";
        String environmentName = "environmentName_1";
        String messageType = "messageType_1";
        String message = "message_1";
        when(appSessionDao.getAppSession(authToken)).thenReturn(appSessionEntity);
        doNothing().when(regStatusDao).loadMessages(environmentName, messageType, message);
        regionStatusService.loadMessages(environmentName, messageType, message, authToken);
        verify(regStatusDao, times(1)).loadMessages(environmentName, messageType, message);
        verifyNoMoreInteractions(regStatusDao);
    }

    @Test
    public void testGetAppContainers() throws Exception {
        ContainerStatusEntity containerStatusEntity = new ContainerStatusEntity();

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

        EnvironmentEntity environment = new EnvironmentEntity();
        environment.setEnvironmentId(1);
        environment.setDisplayOrder(1);
        environment.setEnvLock(1);
        environment.setEnvironmentName("environmentName_1");
        compEntity.setParentComponent(parentComp);
        containerStatusEntity.setComponent(compEntity);
        containerStatusEntity.setEnvironment(environment);
        containerStatusEntity.setEnvironment(environment);
        containerStatusEntity.setContainerStatsId(1L);
        containerStatusEntity.setTotalContainer(1);
        String date = "2018-05-05";
        containerStatusEntity.setStatsDate(java.sql.Date.valueOf(date));

        List<ContainerStatusEntity> csens = new ArrayList<ContainerStatusEntity>();
        csens.add(containerStatusEntity);

        ContainerStatus cs = new ContainerStatus();
        cs.setComponentId(1);
        cs.setComponentName("componentName_1");
        cs.setDeltaValue(1);
        cs.setStatusDate("statusDate_1");
        cs.setTotalContainers(1);

        String startDate = "startDate";
        String endDate = "endDate";
        String environmentName = "environment";
        String componentIdsStrg = "componentIdsStrg";
        boolean isParentComponents = true;
        AppSessionEntity appSessionEntity = new AppSessionEntity();
        appSessionEntity.setAppSessionId(1);
        appSessionEntity.setAuthToken("authToken_1");
        appSessionEntity.setIsAdmin(true);
        appSessionEntity.setPermission("permission_1");
        appSessionEntity.setSessionStartTime(new Date());
        appSessionEntity.setUserId("userId_1");
        appSessionEntity.setUserName("userName_1");

        List<K8sPodsContainersEntity> ksens = new ArrayList<K8sPodsContainersEntity>();

        when(containerStatusDao.getEnvContainers(startDate, endDate, environment.getEnvironmentId(), componentIdsStrg, isParentComponents)).thenReturn(csens);
        when(k8sContainerStatusDao.getEnvContainers(startDate, endDate, environment.getEnvironmentId(), componentIdsStrg, isParentComponents)).thenReturn(ksens);
        regionStatusService.getAppContainers(startDate, endDate, environmentName, componentIdsStrg, isParentComponents, TestDataProvider.getPlatform());
        regionStatusService.makeAppSessionFromEntity(appSessionEntity);
        //verify(containerStatusDao, times(1)).getEnvContainers(startDate, endDate, 1, componentIdsStrg, isParentComponents);
        //verify(k8sContainerStatusDao, times(1)).getEnvContainers(startDate, endDate, environment.getEnvironmentId(), componentIdsStrg, isParentComponents);
//		verifyNoMoreInteractions(containerStatusDao);   	
//		verifyNoMoreInteractions(k8sContainerStatusDao);   	
    }

    @Test
    public void testLoadComponentMessage() throws Exception {
        AppSessionEntity appSessionEntity = new AppSessionEntity();
        appSessionEntity.setAppSessionId(1);
        appSessionEntity.setAuthToken("authToken_1");
        appSessionEntity.setIsAdmin(true);
        appSessionEntity.setPermission("permission_1");
        appSessionEntity.setSessionStartTime(new Date());
        appSessionEntity.setUserId("userId_1");
        appSessionEntity.setUserName("userName_1");
        String authToken = "authToken_1";
        ComponentMessages cm = new ComponentMessages();
        cm.setMessageId(1);
        cm.setAuthToken("authToken_1");
        cm.setComponentid("componentid_1");
        cm.setEnvironment("environment_1");
        cm.setRegion("region_1");
        cm.setUserid("userid_1");
        cm.setUsername("username_1");

        when(appSessionDao.getAppSession(authToken)).thenReturn(appSessionEntity);

        regionStatusService.loadComponentMessages(cm);
        verify(appSessionDao, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(appSessionDao);

    }

    @Test
    public void testGetApplicationApis() throws Exception {
        String startDate = "startDate_1";
        String endDate = "endDate";
        String environment = "environment";
        String componentIdsStrg = "componentIdsStrg";
        Environment env = new Environment();
        env.setEnvironmentName(environment);
        List<ApiStatus> apiStatusList = new ArrayList<ApiStatus>();
        List<ContainerStatus> listContSts = new ArrayList<ContainerStatus>();
        ApiStatus apiStatus = new ApiStatus();
        apiStatus.setComponentId(1);
        apiStatus.setDeltaValue(1);
        apiStatus.setComponentName("componentName_1");
        apiStatus.setEnvironmentId(1);
        apiStatus.setStatusDate("statusDate_1");
        apiStatus.setTotalContainers(1L);
        apiStatusList.add(apiStatus);

        ContainerStatus containerStatus = new ContainerStatus();
        containerStatus.setComponentId(1);
        containerStatus.setDeltaValue(1);
        containerStatus.setComponentName("componentName_1");
        containerStatus.setStatusDate("statusDate_1");
        containerStatus.setTotalContainers(1L);
        listContSts.add(containerStatus);

        List<K8sContainerStatus> k8sContainerStatuses = new ArrayList<K8sContainerStatus>();
        List<ApiStatus> k8sApiStatus = new ArrayList<>();

        when(apiStatusDao.getEnvApis(startDate, endDate, apiStatus.getEnvironmentId(), componentIdsStrg)).thenReturn(apiStatusList);
        when(k8sApiStatusDao.getEnvApis(startDate, endDate, apiStatus.getEnvironmentId(), componentIdsStrg)).thenReturn(k8sApiStatus);
        when(containerStatusDao.getAllContainersOfParent(startDate, endDate, apiStatus.getEnvironmentId(), componentIdsStrg)).thenReturn(listContSts);
        when(k8sContainerStatusDao.getAllContainersOfParent(startDate, endDate, apiStatus.getEnvironmentId(), componentIdsStrg)).thenReturn(k8sContainerStatuses);
        regionStatusService.getApplicationApis(startDate, endDate, environment, componentIdsStrg, TestDataProvider.getPlatform());
        verify(apiStatusDao, times(1)).getEnvApis(startDate, endDate, env.getEnvironmentId(), componentIdsStrg);
        verify(containerStatusDao, times(1)).getAllContainersOfParent(startDate, endDate, env.getEnvironmentId(), componentIdsStrg);
        verify(k8sContainerStatusDao, times(1)).getAllContainersOfParent(startDate, endDate, env.getEnvironmentId(), componentIdsStrg);
        verifyNoMoreInteractions(apiStatusDao);
        verifyNoMoreInteractions(containerStatusDao);
        verifyNoMoreInteractions(k8sContainerStatusDao);

    }

    @Test
    public void testGetPlatformComponents() throws Exception {
        String environmentName = "environmentName_1";

        AppLookUpEntity aluEntity = new AppLookUpEntity();
        aluEntity.setApplookupId(1);
        aluEntity.setComponentFullName("component_full_name_1");

        ComponentTypeEntity cte = new ComponentTypeEntity();
        List<ComponentTypeEntity> list = new ArrayList<ComponentTypeEntity>();
        list.add(cte);
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
        parentComp.setAppLookUpEntity(aluEntity);
        parentComp.setComponentId(2);
        parentComp.setComponentName("parent_component_1");
        parentComp.setComponentType(cte);

        compEntity.setParentComponent(parentComp);

        RegionEntity re = new RegionEntity();
        re.setRegionId(1);
        re.setRegionLock(1);
        re.setRegionDesc("regionDesc_1");
        re.setRegionName("regionName_1");

        EnvironmentEntity envE = new EnvironmentEntity();
        envE.setEnvironmentId(1);
        envE.setEnvLock(1);
        envE.setEnvironmentName("environmentName_1");
        envE.setMarathonUrl("marathonUrl_1");
        envE.setMarathonCred("marathonCred_1");
        envE.setInfraMessage("infraMessage_1");
        envE.setDisplayOrder(1);

        HealthCheckTypeEntity healthCheckType = new HealthCheckTypeEntity();
        healthCheckType.setHealthCheckTypeId(1);
        healthCheckType.setHealthCheckTypeClass("healthCheckTypeClass_1");
        healthCheckType.setHealthCheckTypeName("healthCheckTypeName_1");

        StatusEntity se = new StatusEntity();
        se.setStatusId(1);
        se.setStatusName("statusName_1");
        se.setStatusDesc("statusDesc_1");

        RegionEntity region = new RegionEntity();
        region.setRegionId(1);
        region.setRegionLock(1);
        region.setRegionName("regionName_1");
        region.setRegionDesc("regionDesc_1");

        HealthCheckEntity hce = new HealthCheckEntity();
        hce.setComponent(compEntity);
        hce.setCurrentStatus(se);
        hce.setCreatedDate(new Date());
        hce.setDelInd(1);
        hce.setEnvironment(envE);
        hce.setFailedCount(1);
        hce.setHealthCheckId(1);
        hce.setHealthCheckType(healthCheckType);
        hce.setLastStatusChange(new Date());
        hce.setMaxRetryCount(1);
        hce.setRegion(region);
        hce.setStatusUpdateTime(new Date());

        List<HealthCheckEntity> checkEntities = new ArrayList<HealthCheckEntity>();
        checkEntities.add(hce);

        when(environmentDao.getEnvironmentFromName(environmentName)).thenReturn(envE);
        when(compDao.getPlatformComponentForEnv(envE.getEnvironmentId(), TestDataProvider.getPlatform())).thenReturn(checkEntities);
        regionStatusService.getPlatformComponents(environmentName, TestDataProvider.getPlatform());
        //verify(environmentDao, times(1)).getEnvironmentFromName(environmentName);
        //verify(compDao, times(1)).getPlatformComponentForEnv(1, TestDataProvider.getPlatform());
        //verifyNoMoreInteractions(compDao);
    }


    @Test
    public void testUnsubscribeAlert() throws Exception {
        Subscription subscr = new Subscription();
        int envId = 1;
        String authToken = "authToken";
        String environmentName = "environmentName";
        subscr.setComponentId(1);
        subscr.setEnvironmentId(1);
        subscr.setAlertSubscriptionId(1);
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setEnvironmentName(environmentName);
        subscr.setComponentName("new_component_name");
        subscr.setSubsciptionVal("new_subscription_value");
        subscr.setSubsciptionType("new_subscription_type");
        subscr.setGlobalSubscriptionType("new_globalSubscriptionType");
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setAuthToken(authToken);
        subscr.setSubsciptionVal("new_subsciptionVal");

        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(envId);
        when(alertSubscibeDao.getSubscriptionToken(subscr.getSubsciptionVal(), subscr.getComponentId(), envId)).thenReturn(authToken);
        doNothing().when(alertSubscibeDao).deleteSububscription(authToken);
        regionStatusService.unsubscribeAlert(subscr);
        verify(environmentDao, times(1)).getEnironmentIdFromName(environmentName);
        verify(alertSubscibeDao, times(1)).getSubscriptionToken(subscr.getSubsciptionVal(), subscr.getComponentId(), envId);
        verifyNoMoreInteractions(environmentDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsubscribeAlert_NullAuthToken() throws Exception {
        Subscription subscr = new Subscription();
        int envId = 1;
        String authToken = "authToken";
        String environmentName = "environmentName";
        subscr.setComponentId(1);
        subscr.setEnvironmentId(1);
        subscr.setAlertSubscriptionId(1);
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setEnvironmentName(environmentName);
        subscr.setComponentName("new_component_name");
        subscr.setSubsciptionVal("new_subscription_value");
        subscr.setSubsciptionType("new_subscription_type");
        subscr.setGlobalSubscriptionType("new_globalSubscriptionType");
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setAuthToken(authToken);
        subscr.setSubsciptionVal("new_subsciptionVal");

        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(envId);
        when(alertSubscibeDao.getSubscriptionToken(subscr.getSubsciptionVal(), subscr.getComponentId(), envId)).thenReturn(null);
        regionStatusService.unsubscribeAlert(subscr);
    }

    @Test
    public void testUnsubscribeAlert_Email() throws Exception {
        Subscription subscr = new Subscription();
        int envId = 1;
        String authToken = "authToken";
        String environmentName = "environmentName";
        subscr.setComponentId(1);
        subscr.setEnvironmentId(1);
        subscr.setAlertSubscriptionId(1);
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setEnvironmentName(environmentName);
        subscr.setComponentName("new_component_name");
        subscr.setSubsciptionVal("new_subscription_value");
        subscr.setSubsciptionType("new_subscription_type");
        subscr.setGlobalSubscriptionType("new_globalSubscriptionType");
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setAuthToken(authToken);
        subscr.setSubsciptionVal("new_subsciptionVal");

        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(envId);
        when(alertSubscibeDao.getSubscriptionToken(subscr.getSubsciptionVal(), subscr.getComponentId(), envId)).thenReturn(authToken);
        doNothing().when(mailSenderUtil).sendMailForSubscription(subscr, false);
        regionStatusService.unsubscribeAlert(subscr);
        verify(environmentDao, times(1)).getEnironmentIdFromName(environmentName);
        verify(alertSubscibeDao, times(1)).getSubscriptionToken(subscr.getSubsciptionVal(), subscr.getComponentId(), envId);
        verifyNoMoreInteractions(environmentDao);
    }

    //    @Test
    public void testSubscribeAlert_Slack() throws Exception {

        int envId = 1;
        String authToken = "authToken";
        String environmentName = "environmentName";
        int subsType = 1;
        String subsTypes = "slack";
        Subscription subscription = getSubscription(authToken, environmentName, subsTypes);
        subscription.setSubsciptionVal("subscription_1");
        int validationLevel = 1;

        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(envId);
        doNothing().when(mailSenderUtil).sentMessageToWebhook("slack URL", "Some Message");
        doNothing().when(alertSubscibeDao).saveSubscription(subscription, subsType, validationLevel, false);

        regionStatusService.subscribeAlert(subscription);

        verify(environmentDao, times(1)).getEnironmentIdFromName(environmentName);
        verify(alertSubscibeDao, times(1)).saveSubscription(subscription, subsType, validationLevel, false);
        verifyNoMoreInteractions(environmentDao);
        verifyNoMoreInteractions(alertSubscibeDao);
    }

    private Subscription getSubscription(String authToken, String environmentName, String type) {
        Subscription subscr = new Subscription();
        subscr.setComponentId(1);
        subscr.setEnvironmentId(1);
        subscr.setAlertSubscriptionId(1);
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setEnvironmentName(environmentName);
        subscr.setComponentName("new_component_name");
        subscr.setSubsciptionVal("new_subscription_value");
        subscr.setSubsciptionType(type);
        subscr.setGlobalSubscriptionType("new_globalSubscriptionType");
        subscr.setGlobalSubscriptionTypeId(1);
        subscr.setAuthToken(authToken);
        subscr.setSubsciptionVal("new_subsciptionVal");
        return subscr;
    }


    @Test
    public void testSubscribeAlert_Email() throws Exception {
        int envId = 1;
        String authToken = "authToken";
        String environmentName = "environmentName";
        int subsType = 0;
        int validationLevel = 0;
        Subscription subscription = getSubscription(authToken, environmentName, "email");
        subscription.setSubsciptionVal("email@company.com");

        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(envId);
        doNothing().when(alertSubscibeDao).saveSubscription(subscription, subsType, validationLevel, false);
        doNothing().when(mailSenderUtil).sendMailForSubscription(subscription, true);

        regionStatusService.subscribeAlert(subscription);

        verify(environmentDao, times(1)).getEnironmentIdFromName(environmentName);
        verify(alertSubscibeDao, times(1)).saveSubscription(subscription, subsType, validationLevel, false);
        verifyNoMoreInteractions(environmentDao);
    }

    @Test(expected = ValidationFailedException.class)
    public void testSubscribeAlert_EmailInvalid() throws Exception {
        String authToken = "authToken";
        String environmentName = "environmentName";
        Subscription subscription = getSubscription(authToken, environmentName, "email");
        subscription.setSubsciptionVal("INVALID");

        regionStatusService.subscribeAlert(subscription);
    }

    @Test(expected = ValidationFailedException.class)
    public void testSubscribeAlert_SlackURLInvalid() throws Exception {
        String authToken = "authToken";
        String environmentName = "environmentName";
        Subscription subscription = getSubscription(authToken, environmentName, "slack");
        subscription.setSubsciptionVal("INVALID");

        regionStatusService.setSlackWebhookValidURL("http://");

        regionStatusService.subscribeAlert(subscription);
    }

    //    @Test
    public void testSubscribeAlert_SlackChannel() throws Exception {
        int envId = 1;
        String authToken = "authToken";
        String environmentName = "environmentName";
        int subsType = 2;
        int validationLevel = 1;
        String subStype = "slackChannel";
        Subscription slackChannel = getSubscription(authToken, environmentName, subStype);
        slackChannel.setSubsciptionVal("new_subsciptionVal");

        when(environmentDao.getEnironmentIdFromName(environmentName)).thenReturn(envId);
        doNothing().when(alertSubscibeDao).saveSubscription(slackChannel, subsType, validationLevel, false);
        doNothing().when(mailSenderUtil).sendMailForSubscription(slackChannel, true);

        regionStatusService.subscribeAlert(slackChannel);

        verify(environmentDao, times(1)).getEnironmentIdFromName(environmentName);
        verify(alertSubscibeDao, times(1)).saveSubscription(slackChannel, subsType, validationLevel, false);
        verifyNoMoreInteractions(environmentDao);
    }
}
