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
package com.tmobile.kardio.controller;


import com.tmobile.kardio.TestUtils;
import com.tmobile.kardio.bean.*;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.service.AdminService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ActiveProfiles("test")
public class AdminControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private GDMAdminController adminController;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .build();
    }

    @Test
    public void testCreateAppFullName() throws Exception {
        AppFullName afn = getAppFullName();
        doNothing().when(adminService).saveAppFullName(afn);

        String authToken = TestUtils.mockAppSession(adminService);

        mockMvc.perform(post("/addAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).saveAppFullName(afn);
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }


    @Test
    public void testGetAppFullName() throws Exception {
        AppFullName afn = getAppFullName();
        List<AppFullName> afns = new ArrayList<AppFullName>();
        afns.add(afn);
        when(adminService.getAppFullName()).thenReturn(afns);

        String authToken = TestUtils.mockAppSession(adminService);

        mockMvc.perform(get("/getAppFullName")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.responseContent[0].appId", is(afn.getAppId())))
                .andExpect(jsonPath("$.responseContent[0].componentId", is(afn.getComponentId())))
                .andExpect(jsonPath("$.responseContent[0].componentName", is(afn.getComponentName())))
                .andExpect(jsonPath("$.responseContent[0].componentFullName", is(afn.getComponentFullName())));

        verify(adminService, times(1)).getAppFullName();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }


    @Test
    public void testUpdateAppFullName_AppIDZero() throws Exception {
        AppFullName afn = getAppFullName();
        afn.setAppId(0);
        doNothing().when(adminService).saveAppFullName(afn);

        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Edit Application Full Name: " + afn.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).saveAppFullName(afn);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testUpdateAppFullName_FullNameEmpty() throws Exception {
        AppFullName afn = getAppFullName();
        // afn.setComponentFullName("");
        doNothing().when(adminService).editAppFullName(afn);

        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Edit Application Full Name: " + afn.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).editAppFullName(afn);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testUpdateAppFullName() throws Exception {
        AppFullName afn = getAppFullName();
        doNothing().when(adminService).editAppFullName(afn);

        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Edit Application Full Name: " + afn.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).editAppFullName(afn);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    private AppFullName getAppFullName() {
        AppFullName afn = new AppFullName();
        afn.setAppId(1);
        afn.setComponentId(1);
        afn.setComponentName("new_component_name");
        afn.setComponentFullName("new_component_full_name");
        return afn;
    }

    @Test
    public void testCreateComponent() throws Exception {
        Component comp = getComponent();
        doNothing().when(adminService).saveComponent(comp);

        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Added Component: " + comp.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(post("/addComponent")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(comp)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).saveComponent(comp);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testUpdateComponent() throws Exception {
        Component comp = getComponent();
        doNothing().when(adminService).editComponent(comp);

        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Modified Component: " + comp.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editComponent")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(comp)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).editComponent(comp);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }


    @Test
    public void testGetComponents() throws Exception {
        Component comp = getComponent();
        List<Component> comps = new ArrayList<Component>();
        comps.add(comp);
        when(adminService.getAllComponent()).thenReturn(comps);

        String authToken = TestUtils.mockAppSession(adminService);

        mockMvc.perform(get("/getAllComponent")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(content().string("emtpy"))
                .andExpect(jsonPath("$.responseContent[0].appFullName", is(comp.getAppFullName())))
                .andExpect(jsonPath("$.responseContent[0].childComponentName", is(comp.getChildComponentName())))
                .andExpect(jsonPath("$.responseContent[0].componentId", is(comp.getComponentId())))
                .andExpect(jsonPath("$.responseContent[0].componentName", is(comp.getComponentName())))
                .andExpect(jsonPath("$.responseContent[0].componentType", is(comp.getComponentType())))
                .andExpect(jsonPath("$.responseContent[0].compDesc", is(comp.getCompDesc())))
//            .andExpect(jsonPath("$.responseContent[0].children[0].componentId", is(comp.getChildren().get(0).getComponentId()))) TODO: children not returned.
                .andExpect(jsonPath("$.responseContent[0].delInd", is(comp.getDelInd())))
                .andExpect(jsonPath("$.responseContent[0].hcEnvList[0].envName", is(comp.getHcEnvList().get(0).getEnvName())))
                .andExpect(jsonPath("$.responseContent[0].hcEnvList[0].createdDate", is(comp.getHcEnvList().get(0).getCreatedDate().getTime())))
                .andExpect(jsonPath("$.responseContent[0].parentComponentId", is(comp.getParentComponentId())))
                .andExpect(jsonPath("$.responseContent[0].parentComponentName", is(comp.getParentComponentName())))
                .andExpect(jsonPath("$.responseContent[0].recentEvent", is(comp.isRecentEvent())))
//            .andExpect(jsonPath("$.responseContent[0].region[0].regionId", is(comp.getRegion().get(0).getRegionId())))  Region ID not returned.
                .andExpect(jsonPath("$.responseContent[0].region[0].regionName", is(comp.getRegion().get(0).getRegionName())))
                .andExpect(jsonPath("$.responseContent[0].region[0].regionMessage", is(comp.getRegion().get(0).getRegionMessage())))
                .andExpect(jsonPath("$.responseContent[0].region[0].regionStatus", is(comp.getRegion().get(0).getRegionStatus())))
                .andExpect(jsonPath("$.responseContent[0].roles[0]", is(comp.getRoles().get(0))));

        verify(adminService, times(1)).getAllComponent();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testDeleteComponent() throws Exception {
        Component comp = getComponent();
        doNothing().when(adminService).deleteComponent(comp);

        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Deleted Component: " + comp.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/deleteComponent")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(comp)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).deleteComponent(comp);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }


    private Component getComponent() {
        Component comp = new Component();
        comp.setAppFullName("app_full_name_1");
        comp.setChildComponentName("child_component_1");
        comp.setComponentId(1);
        comp.setComponentName("component_1");
        comp.setComponentType(ComponentType.APP.name());
        comp.setCompDesc("component_desc_1");

        List<Component> children = new ArrayList<Component>();
        Component child = new Component();
        child.setComponentId(10);
        children.add(child);
        comp.setChildren(children);

        comp.setDelInd(0);

        List<HealtCheckEnvironment> hcEnvList = new ArrayList<HealtCheckEnvironment>();
        HealtCheckEnvironment env = new HealtCheckEnvironment();
        env.setCreatedDate(new Date());
        env.setEnvName("env_1");
        hcEnvList.add(env);
        comp.setHcEnvList(hcEnvList);

        comp.setParentComponentId(0);
        comp.setParentComponentName("parent_component_1");
        comp.setRecentEvent(true);

        List<Region> regions = new ArrayList<>();
        Region region = new Region();
        region.setRegionId(1);
        region.setRegionMessage("message_1");
        region.setRegionName("region_1");
        region.setRegionStatus("active");
        region.setRecentEvent(true);
        regions.add(region);
        comp.setRegion(regions);

        List<String> roles = new ArrayList<String>();
        roles.add("role_1");
        comp.setRoles(roles);
        return comp;
    }

    @Test
    public void testGetHealthChecks() throws Exception {
        HealthCheckVO hc = new HealthCheckVO();
        hc.setComponentId(1);
        hc.setComponentName("component_1");
        hc.setComponentType(ComponentType.APP.name());
        hc.setDelInd(false);
        hc.setEnvironmentId(1);
        hc.setEnvironmentName("env_1");
        hc.setHealthCheckId(1);
        List<HealthCheckParamVO> params = new ArrayList<HealthCheckParamVO>();
        HealthCheckParamVO param = new HealthCheckParamVO();
        param.setHealthCheckParamId(1);
        param.setHealthCheckParamKey("key_1");
        param.setHealthCheckParamValue("value_1");
        params.add(param);
        hc.setHealthCheckParamList(params);
        hc.setHealthCheckTypeId(1);
        hc.setHealthCheckTypeName("type_name_1");
        hc.setMaxRetryCount(3);
        hc.setParentComponentId(1);
        hc.setParentComponentName("parent_component_1");
        hc.setRegionId(1);
        hc.setRegionName("region_1");
        List<HealthCheckVO> hcs = new ArrayList<HealthCheckVO>();
        hcs.add(hc);
        when(adminService.getAllHealthChecks()).thenReturn(hcs);

        String authToken = TestUtils.mockAppSession(adminService);

        mockMvc.perform(get("/getAllHealthChecks")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.responseContent[0].componentId", is(hc.getComponentId())))
                .andExpect(jsonPath("$.responseContent[0].componentName", is(hc.getComponentName())))
                .andExpect(jsonPath("$.responseContent[0].componentType", is(hc.getComponentType())))
                .andExpect(jsonPath("$.responseContent[0].delInd", is(hc.isDelInd())))
                .andExpect(jsonPath("$.responseContent[0].environmentId", is(hc.getEnvironmentId())))
                .andExpect(jsonPath("$.responseContent[0].environmentName", is(hc.getEnvironmentName())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckId", is(hc.getHealthCheckId())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckParamList[0].healthCheckParamId", is(hc.getHealthCheckParamList().get(0).getHealthCheckParamId())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckParamList[0].healthCheckParamKey", is(hc.getHealthCheckParamList().get(0).getHealthCheckParamKey())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckParamList[0].healthCheckParamValue", is(hc.getHealthCheckParamList().get(0).getHealthCheckParamValue())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckTypeId", is(hc.getHealthCheckTypeId())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckTypeName", is(hc.getHealthCheckTypeName())))
                .andExpect(jsonPath("$.responseContent[0].maxRetryCount", is(hc.getMaxRetryCount())))
                .andExpect(jsonPath("$.responseContent[0].parentComponentId", is(hc.getParentComponentId())))
                .andExpect(jsonPath("$.responseContent[0].parentComponentName", is(hc.getParentComponentName())))
                .andExpect(jsonPath("$.responseContent[0].regionId", is(hc.getRegionId())))
                .andExpect(jsonPath("$.responseContent[0].regionName", is(hc.getRegionName())));

        verify(adminService, times(1)).getAllHealthChecks();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }

    private Subscription getGlobalSubscription() {
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
        return gs;
    }

    @Test
    public void testGetGlobalSubscription() throws Exception {
        Subscription gs = getGlobalSubscription();
        List<Subscription> ags = new ArrayList<Subscription>();
        ags.add(gs);
        when(adminService.getAllGlobalSubscriptions()).thenReturn(ags);
        String authToken = TestUtils.mockAppSession(adminService);
        mockMvc.perform(get("/getGlobalSubscription")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                //.andExpect(content().string(""))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.responseContent[0].alertSubscriptionId", is(gs.getAlertSubscriptionId())))
                .andExpect(jsonPath("$.responseContent[0].componentId", is(gs.getComponentId())))
                .andExpect(jsonPath("$.responseContent[0].globalSubscriptionType", is(gs.getGlobalSubscriptionType())))
                //.andExpect(jsonPath("$.responseContent[0].environment_id", is(gs.getEnvironmentId())))
                //.andExpect(jsonPath("$.responseContent[0].subscriptionVal", is(gs.getSubsciptionVal())))
                //.andExpect(jsonPath("$.responseContent[0].subscriptionType", is(gs.getSubsciptionType())))
                .andExpect(jsonPath("$.responseContent[0].globalSubscriptionTypeId", is(gs.getGlobalSubscriptionTypeId())))
                /*  TODO	//.andExpect(jsonPath("$.responseContent[0].environmentName", is(gs.getEnvironmentName())))
                 */.andExpect(jsonPath("$.responseContent[0].authToken", is(gs.getAuthToken())));
        verify(adminService, times(1)).getAllGlobalSubscriptions();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }

    private Environment getEnvironment() {
        Environment env = new Environment();
        env.setEnvironmentId(1);
        env.setDisplayOrder(1);
        env.setEnvLock(1);
        env.setEnvironmentDesc("new environment Desc");
        env.setEnvironmentName("new environment Name");
        env.setMarathonPassword("new marathon Password");
        env.setMarathonUrl("new marathon Url");
        env.setMarathonUserName("new marathon UserName");
        return env;

    }

    @Test
    public void testGetEnvironmentLock() throws Exception {
        Environment env = getEnvironment();
        List<Environment> envs = new ArrayList<Environment>();
        envs.add(env);
        when(adminService.getEnvironmentLock()).thenReturn(envs);
        String authToken = TestUtils.mockAppSession(adminService);
        mockMvc.perform(get("/getEnvironmentLock")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.responseContent[0].environmentId", is(env.getEnvironmentId())))
                .andExpect(jsonPath("$.responseContent[0].envLock", is(env.getEnvLock())))
                .andExpect(jsonPath("$.responseContent[0].displayOrder", is(env.getDisplayOrder())))
                .andExpect(jsonPath("$.responseContent[0].environmentName", is(env.getEnvironmentName())))
                .andExpect(jsonPath("$.responseContent[0].environmentDesc", is(env.getEnvironmentDesc())))
                .andExpect(jsonPath("$.responseContent[0].marathonUrl", is(env.getMarathonUrl())))
                .andExpect(jsonPath("$.responseContent[0].marathonPassword", is(env.getMarathonPassword())))
                .andExpect(jsonPath("$.responseContent[0].marathonUserName", is(env.getMarathonUserName())));
        verify(adminService, times(1)).getEnvironmentLock();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }

    private CounterDetails getCounters() {
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
        return counterDetails;
    }

    @Test
    public void testGetCounters() throws Exception {
        CounterDetails counter = getCounters();
        List<CounterDetails> counters = new ArrayList<CounterDetails>();
        counters.add(counter);
        when(adminService.getAllCounters()).thenReturn(counter);
        String authToken = TestUtils.mockAppSession(adminService);
        mockMvc.perform(get("/getAllCounters")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                //.andExpect(content().string(""))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.responseContent.counter[0].counterName", is(counters.get(0).getCounter().get(0).getCounterName())))
                .andExpect(jsonPath("$.responseContent.counter[0].counterId", is(counter.getCounter().get(0).getCounterId())))
                .andExpect(jsonPath("$.responseContent.counter[0].delInd", is(counter.getCounter().get(0).getDelInd())))
                .andExpect(jsonPath("$.responseContent.counter[0].position", is(counter.getCounter().get(0).getPosition())))
                //.andExpect(jsonPath("$.responseContent.counter[0].metricVal", is(counter.getCounter().get(0).getMetricVal())))
                .andExpect(jsonPath("$.responseContent.counter[0].counterDesc", is(counter.getCounter().get(0).getCounterDesc())))
                /*TODO*/    //.andExpect(jsonPath("$.responseContent.counter[0].trend", is(counter.getCounter().get(0).getTrend())))
                .andExpect(jsonPath("$.responseContent.envCounters[0].envCounterId", is(counter.getEnvCounters().get(0).getEnvCounterId())))
                .andExpect(jsonPath("$.responseContent.envCounters[0].metricTypeId", is(counter.getEnvCounters().get(0).getMetricTypeId())))
                .andExpect(jsonPath("$.responseContent.envCounters[0].counterName", is(counter.getEnvCounters().get(0).getCounterName())))
                .andExpect(jsonPath("$.responseContent.envCounters[0].parameter1", is(counter.getEnvCounters().get(0).getParameter1())))
                .andExpect(jsonPath("$.responseContent.envCounters[0].parameter2", is(counter.getEnvCounters().get(0).getParameter2())))
                .andExpect(jsonPath("$.responseContent.envCounters[0].envName", is(counter.getEnvCounters().get(0).getEnvName())));
        verify(adminService, times(1)).getAllCounters();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);

    }

    private HealthCheckTypeVO getHealthType() {
        HealthCheckTypeVO typeVO = new HealthCheckTypeVO();
        typeVO.setHealthCheckTypeId(1);
        typeVO.setHealthCheckTypeDesc("new healthCheck TypeDesc");
        typeVO.setHealthCheckTypeName("new healthCheck TypeName");
        return typeVO;
    }

    @Test
    public void testGetHealthCheckType() throws Exception {
        HealthCheckTypeVO checkTypeVO = getHealthType();
        List<HealthCheckTypeVO> hct = new ArrayList<HealthCheckTypeVO>();
        hct.add(checkTypeVO);
        when(adminService.getAllHealthCheckTypes()).thenReturn(hct);
        String authToken = TestUtils.mockAppSession(adminService);
        mockMvc.perform(get("/getHealthCheckTypes")
                .param("authToken", authToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.responseContent[0].healthCheckTypeId", is(checkTypeVO.getHealthCheckTypeId())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckTypeName", is(checkTypeVO.getHealthCheckTypeName())))
                .andExpect(jsonPath("$.responseContent[0].healthCheckTypeDesc", is(checkTypeVO.getHealthCheckTypeDesc())));
        verify(adminService, times(1)).getAllHealthCheckTypes();
        verify(adminService, times(1)).getAppSession(authToken);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testCreateEnvironment() throws Exception {
        Environment env = getEnvironment();
        doNothing().when(adminService).addEnvironment(env);
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Added Environment: " + env.getEnvironmentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(post("/addEnvironment")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(env)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).addEnvironment(env);
        verify(adminService, times(1)).getAppSession(authToken);
        //verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testDeleteGlobalSubscription() throws Exception {
        GDMAdminController gdm = mock(GDMAdminController.class);
        Subscription subs = getGlobalSubscription();
        doNothing().when(adminService).deleteGlobalSubscription(subs.getAlertSubscriptionId());
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Deleted Global Subscription for Id: " + subs.getAlertSubscriptionId();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(delete("/deleteGlobalSubscription")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .param("alertSubscriptionId", "" + subs.getAlertSubscriptionId()))
                .andExpect(status().isOk());
        verify(adminService, times(1)).deleteGlobalSubscription(subs.getAlertSubscriptionId());
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testSubscribeGlobalAlerts() throws Exception {
        Subscription subsc = getGlobalSubscription();
        doNothing().when(adminService).subscribeGlobalAlert(subsc);
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Subscribed Global Alerts";
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(post("/subscribeGlobalAlerts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(subsc)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).subscribeGlobalAlert(subsc);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testEnvironmentLock() throws Exception {
        Environment env = getEnvironment();
        doNothing().when(adminService).doEnvironmentLock(env);
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Environment Lock Updated for Environment: " + env.getEnvironmentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/environmentLock")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(env)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).doEnvironmentLock(env);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    private EnvCounters getEnvCounters() {
        EnvCounters envc = new EnvCounters();
        envc.setEnvCounterId(1);
        envc.setMetricTypeId(1);
        envc.setCounterName("new counterName");
        envc.setParameter1("new parameter1");
        envc.setParameter2("new parameter2");
        return envc;
    }

    @Test
    public void testEditEnvCounterDetails() throws Exception {
        EnvCounters env = getEnvCounters();
        doNothing().when(adminService).editEnvCounterDetails(env);
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Updated the EnvCounter parameters for Counter: " + env.getCounterName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editEnvCounterDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(env)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).editEnvCounterDetails(env);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    private Counters getCounter() {
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
        return counters;
    }

    @Test
    public void testEditCounters() throws Exception {
        Counters cnts = getCounter();
        List<Counters> cntlsts = new ArrayList<Counters>();
        cntlsts.add(cnts);
        doNothing().when(adminService).editCounter(cntlsts);
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Updated Counter Configuration ";
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editCounters")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(cntlsts)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).editCounter(cntlsts);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);

    }

    private HealthCheckVO getHealthCheck() {
        HealthCheckVO healthCheckVO = new HealthCheckVO();
        healthCheckVO.setComponentId(1);
        healthCheckVO.setEnvironmentId(1);
        healthCheckVO.setHealthCheckId(1);
        healthCheckVO.setHealthCheckTypeId(1);
        healthCheckVO.setMaxRetryCount(1);
        healthCheckVO.setParentComponentId(1);
        healthCheckVO.setRegionId(1);
        healthCheckVO.setComponentName("new componentName");
        healthCheckVO.setComponentType("new componentType");
        healthCheckVO.setEnvironmentName("new environmentName");
        healthCheckVO.setHealthCheckTypeName("new healthCheckTypeName");
        healthCheckVO.setParentComponentName("new parentComponentName");
        healthCheckVO.setRegionName("new regionName");
        List<HealthCheckParamVO> params = new ArrayList<HealthCheckParamVO>();
        HealthCheckParamVO param = new HealthCheckParamVO();
        param.setHealthCheckParamId(1);
        param.setHealthCheckParamKey("key_1");
        param.setHealthCheckParamValue("value_1");
        params.add(param);
        return healthCheckVO;
    }

    @Test
    public void testEditHealthCheck() throws Exception {
        HealthCheckVO healthCheckVO = getHealthCheck();
        List<HealthCheckVO> hcs = new ArrayList<HealthCheckVO>();
        hcs.add(healthCheckVO);
        doNothing().when(adminService).editHealthCheck(healthCheckVO);
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Modified Health Check Id: " + healthCheckVO.getComponentName();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/editHealthCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .param("healthCheckName", healthCheckVO.getHealthCheckTypeName())
                .content(TestUtils.asJsonString(healthCheckVO)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).editHealthCheck(healthCheckVO);
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test
    public void testDeleteHealthCheck() throws Exception {
        HealthCheckVO healthCheckVO = getHealthCheck();
        doNothing().when(adminService).deleteHealthCheck(healthCheckVO.getHealthCheckId());
        String authToken = TestUtils.mockAppSession(adminService);
        String auditLog = "Deleted Health Check Id: " + healthCheckVO.getHealthCheckId();
        doNothing().when(adminService).addAuditLog(authToken, auditLog);
        mockMvc.perform(put("/deleteHealthCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(healthCheckVO)))
                .andExpect(status().isOk());
        verify(adminService, times(1)).deleteHealthCheck(healthCheckVO.getHealthCheckId());
        verify(adminService, times(1)).getAppSession(authToken);
        verify(adminService, times(1)).addAuditLog(authToken, auditLog);
        verifyNoMoreInteractions(adminService);
    }

    @Test(expected = NestedServletException.class)
    public void testAuthTokenEmpty() throws Exception {
        AppFullName afn = getAppFullName();
        String authToken = "";

        mockMvc.perform(post("/addAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)));
    }

    @Test(expected = NestedServletException.class)
    public void testAuthTokenNullSession() throws Exception {
        AppFullName afn = getAppFullName();
        String authToken = "auth_token";

        AppSession session = new AppSession();
        session.setAppSessionId(1);
        session.setAuthToken(null);
        session.setUserId("admin_id");
        session.setUserName("admin");
        session.setAdmin(true);
        session.setSessionStartTime(new Date());
        when(adminService.getAppSession(authToken)).thenReturn(session);

        mockMvc.perform(post("/addAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)));
    }

    @Test(expected = NestedServletException.class)
    public void testAuthTokenNotAdmin() throws Exception {
        AppFullName afn = getAppFullName();
        String authToken = "auth_token";

        AppSession session = new AppSession();
        session.setAppSessionId(1);
        session.setAuthToken(authToken);
        session.setUserId("admin_id");
        session.setUserName("admin");
        session.setAdmin(false);
        session.setSessionStartTime(new Date());
        when(adminService.getAppSession(authToken)).thenReturn(session);

        mockMvc.perform(post("/addAppFullName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("authToken", authToken)
                .content(TestUtils.asJsonString(afn)));
    }


}
