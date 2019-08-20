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
package com.tmobile.kardio.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.TestDataProvider;
import com.tmobile.kardio.TestUtils;
import com.tmobile.kardio.bean.ApiStatus;
import com.tmobile.kardio.bean.AvailabilityData;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.ComponentHistory;
import com.tmobile.kardio.bean.ComponentMessages;
import com.tmobile.kardio.bean.ContainerStatus;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.EnvironmentMessages;
import com.tmobile.kardio.bean.HistoryResponse;
import com.tmobile.kardio.bean.Messages;
import com.tmobile.kardio.bean.StatusHistory;
import com.tmobile.kardio.bean.StatusResponse;
import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.bean.User;
import com.tmobile.kardio.controller.GDMMainController;
import com.tmobile.kardio.service.AdminService;
import com.tmobile.kardio.service.RegionStatusService;
import com.tmobile.kardio.util.LDAPAuthUtil;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ComponentStatusApplication.class })
public class MainControllerTest {
	private MockMvc mockMvc;

	@Mock
	private RegionStatusService regionStatusService;
	
    @Mock
    private AdminService adminService;

	@Mock
	private LDAPAuthUtil ldapAuthUtil;

	@InjectMocks
	private GDMMainController mainController;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
	}

	private User getUser() {
		User user = new User();
		user.setUserId("user_id");
		user.setUserName("user_name");
		user.setEmailId("email_id");
		user.setPassword("user_password");
		user.setTimeOutMinute("timeOut_Minute");
		user.setAuthToken("auth Token");
		user.setAdmin(true);
		Set<String> userGroups = new HashSet<String>();
		userGroups.add("user_groups");
		user.setUserGroups(userGroups);
		return user;
	}

	@Test
	public void testDoLoginForUser() throws Exception {
		User user = getUser();
		when(ldapAuthUtil.authenticateAndGetGroups(user.getUserName(), user.getPassword())).thenReturn(user);
		doNothing().when(regionStatusService).doLoginForUser(user);
		mockMvc.perform(post("/doLogin").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(user)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseContent.userName", is(user.getUserName())))
				.andExpect(jsonPath("$.responseContent.password", is(user.getPassword())))
				.andExpect(jsonPath("$.responseContent.timeOutMinute", is(user.getTimeOutMinute())))
				.andExpect(jsonPath("$.responseContent.authToken", is(user.getAuthToken())))
				.andExpect(jsonPath("$.responseContent.userId", is(user.getUserId())));
		// .andExpect(jsonPath("$.responseContent.userGroups",
		// is(user.getUserGroups())));
		verify(ldapAuthUtil, times(1)).authenticateAndGetGroups(user.getUserName(), user.getPassword());
		verify(regionStatusService, times(1)).doLoginForUser(user);
		verifyNoMoreInteractions(regionStatusService);
	}
	
	@Test(expected=NestedServletException.class)
	public void testDoLoginForUser_UserPassEmpty() throws Exception {
		User user = getUser();
		user.setPassword(null);
		mockMvc.perform(post("/doLogin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(user)));
	}

	private Environment getEnvironment() {
		Environment environment = new Environment();
		environment.setDisplayOrder(1);
		environment.setEnvironmentId(1);
		environment.setEnvLock(1);
		environment.setEnvironmentDesc("environment_Desc");
		environment.setEnvironmentName("environment_Name");
		environment.setMarathonPassword("marathon_Password");
		environment.setMarathonUserName("marathon_UserName");
		environment.setMarathonUrl("marathon_Url");
		return environment;
	}

	@Test
	public void testGetEnvironments() throws Exception {
		Environment environment = getEnvironment();
		List<Environment> environments = new ArrayList<Environment>();
		environments.add(environment);
		when(regionStatusService.getEnvironments()).thenReturn(environments);
		mockMvc.perform(get("/getEnvironments").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(environment))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.responseContent[0].environmentId", is(environment.getEnvironmentId())))
				.andExpect(jsonPath("$.responseContent[0].displayOrder", is(environment.getDisplayOrder())))
				.andExpect(jsonPath("$.responseContent[0].envLock", is(environment.getEnvLock())))
				.andExpect(jsonPath("$.responseContent[0].environmentName", is(environment.getEnvironmentName())))
				.andExpect(jsonPath("$.responseContent[0].environmentDesc", is(environment.getEnvironmentDesc())))
				.andExpect(jsonPath("$.responseContent[0].marathonUrl", is(environment.getMarathonUrl())))
				.andExpect(jsonPath("$.responseContent[0].marathonUserName", is(environment.getMarathonUserName())))
				.andExpect(jsonPath("$.responseContent[0].marathonPassword", is(environment.getMarathonPassword())));
		verify(regionStatusService, times(1)).getEnvironments();
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testSubscribeAlerts() throws Exception {
		Subscription subsc = TestDataProvider.getSubscription();
		doNothing().when(regionStatusService).subscribeAlert(subsc);
		mockMvc.perform(
				post("/subscribeAlerts").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(subsc)))
				.andExpect(status().isOk());
		verify(regionStatusService, times(1)).subscribeAlert(subsc);
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testSubscribeAlerts_Email() throws Exception {
		Subscription subsc = TestDataProvider.getSubscription();
		subsc.setSubsciptionType("email");
		doNothing().when(regionStatusService).subscribeAlert(subsc);
		mockMvc.perform(
				post("/subscribeAlerts").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(subsc)))
				.andExpect(status().isOk());
		verify(regionStatusService, times(1)).subscribeAlert(subsc);
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testSubscribeAlerts_Slack() throws Exception {
		Subscription subsc = TestDataProvider.getSubscription();
		subsc.setSubsciptionType("slackChannel");
		doNothing().when(regionStatusService).subscribeAlert(subsc);
		mockMvc.perform(
				post("/subscribeAlerts").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(subsc)))
				.andExpect(status().isOk());
		verify(regionStatusService, times(1)).subscribeAlert(subsc);
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testUnSubscribeAlerts() throws Exception {
		Subscription unsubsc = TestDataProvider.getSubscription();
		doNothing().when(regionStatusService).unsubscribeAlert(unsubsc);
		mockMvc.perform(post("/unsubscribeAlerts").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(unsubsc))).andExpect(status().isOk());
		verify(regionStatusService, times(1)).unsubscribeAlert(unsubsc);
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testUnSubscribeAlerts_Email() throws Exception {
		Subscription unsubsc = TestDataProvider.getSubscription();
		unsubsc.setSubsciptionType("email");
		doNothing().when(regionStatusService).unsubscribeAlert(unsubsc);
		mockMvc.perform(post("/unsubscribeAlerts").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(unsubsc))).andExpect(status().isOk());
		verify(regionStatusService, times(1)).unsubscribeAlert(unsubsc);
		verifyNoMoreInteractions(regionStatusService);
	}
	
	@Test
	public void testDeletesubscription() throws Exception {
		Subscription delsubsc = TestDataProvider.getSubscription();
		doNothing().when(regionStatusService).deleteSubscription(delsubsc.getAuthToken());
		mockMvc.perform(delete("/deletesubscription").param("authToken", delsubsc.getAuthToken())
				.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(delsubsc)))
				.andExpect(status().isOk());
		verify(regionStatusService, times(1)).deleteSubscription(delsubsc.getAuthToken());
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testConfirmSubscription() throws Exception {
		Subscription confsubsc = TestDataProvider.getSubscription();
		doNothing().when(regionStatusService).confirmSubscription(confsubsc.getAuthToken());
		mockMvc.perform(get("/confirmSubscription").param("authToken", confsubsc.getAuthToken())
				.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(confsubsc)))
				.andExpect(status().isOk());
		verify(regionStatusService, times(1)).confirmSubscription(confsubsc.getAuthToken());
		verifyNoMoreInteractions(regionStatusService);
	}

	private ComponentMessages getComponentMessages() {
		ComponentMessages cm = new ComponentMessages();
		cm.setComponentid("component_id");
		cm.setUserid("user_id");
		cm.setEnvironment("new_environment");
		cm.setAuthToken("auth_Token");
		cm.setMessage("new_message");
		cm.setRegion("new_region");
		cm.setMessageId(1);
		cm.setUsername("new_username");
		return cm;
	}

	private Counters getCounter() {
		Counters counters = new Counters();
		List<Counters> cntlst = new ArrayList<Counters>();
		counters.setCounterId(1);
		counters.setDelInd(1);
		counters.setPosition(1);
		counters.setCounterName("counter_Name");
		counters.setCounterDesc("counter_Desc");
		counters.setMetricDate("metric_Date");
		counters.setMetricVal(1F);
		List<Float> trend = new ArrayList<Float>();
		trend.add(1F);
		counters.setTrend(trend);
		cntlst.add(counters);
		return counters;
	}

	@Test
	public void testGetCountersMatrix() throws Exception {
		String envName = "getcounterenv";
		Counters counters = getCounter();
		List<Counters> cntlsts = new ArrayList<Counters>();
		cntlsts.add(counters);
		when(regionStatusService.getCountersMatrix(envName, TestDataProvider.getPlatform())).thenReturn(cntlsts);
		mockMvc.perform(get("/getCounterMetrix").contentType(MediaType.APPLICATION_JSON)
				.param("environment", envName)
				.param("platform", TestDataProvider.DEFAULT_PLATFORM))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.responseContent[0].counterId", is(cntlsts.get(0).getCounterId())))
				.andExpect(jsonPath("$.responseContent[0].position", is(counters.getPosition())))
				.andExpect(jsonPath("$.responseContent[0].delInd", is(counters.getDelInd())))
				// .andExpect(jsonPath("$.responseContent[0].metricVal",
				// is(counters.getMetricVal())))
				.andExpect(jsonPath("$.responseContent[0].metricDate", is(counters.getMetricDate())))
				.andExpect(jsonPath("$.responseContent[0].counterName", is(counters.getCounterName())))
				.andExpect(jsonPath("$.responseContent[0].counterDesc", is(counters.getCounterDesc())));
		// .andExpect(jsonPath("$.responseContent[0].trend", is(counters.getTrend())));

		verify(regionStatusService, times(1)).getCountersMatrix(envName, TestDataProvider.getPlatform());
		verifyNoMoreInteractions(regionStatusService);

	}

	private StatusResponse getStatusResponse() {
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
		return sr;
	}

	@Test
	public void testGetComponentRegion() throws Exception {
		String envName = "getcompenv";
		StatusResponse sr = getStatusResponse();
		when(regionStatusService.getComponentRegion(envName)).thenReturn(sr);

		mockMvc.perform(get("/component").contentType(MediaType.APPLICATION_JSON).param("environment", envName))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseContent.envMessages.generalMessage",
						is(sr.getEnvMessages().getGeneralMessage())))
				.andExpect(
						jsonPath("$.responseContent.envMessages.appMessage", is(sr.getEnvMessages().getAppMessage())))
				.andExpect(jsonPath("$.responseContent.envMessages.infraMessage",
						is(sr.getEnvMessages().getInfraMessage())));

		verify(regionStatusService, times(1)).getComponentRegion(envName);
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testSaveCompMessage() throws Exception {
		ComponentMessages cm = getComponentMessages();
		doNothing().when(regionStatusService).loadComponentMessages(cm);
		mockMvc.perform(post("/saveCompMessage").param("Tset", cm.getMessage()).contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(cm))).andExpect(status().isOk());

		verify(regionStatusService, times(1)).loadComponentMessages(cm);
		verifyNoMoreInteractions(regionStatusService);
	}

	@Test
	public void testGetMarathonComponents() throws Exception {
		String envName = "getcompenv";
		List<Component> comps = new ArrayList<Component>();
		mockMvc.perform(
				get("/getPlatformComponents").contentType(MediaType.APPLICATION_JSON)
				.param("environment", envName)
				.param("platform", TestDataProvider.getPlatform()))
				.andExpect(status().isOk());
		verify(regionStatusService, times(1)).getPlatformComponents(envName, TestDataProvider.getPlatform());
		verifyNoMoreInteractions(regionStatusService);
		when(regionStatusService.getPlatformComponents(envName, TestDataProvider.getPlatform())).thenReturn(comps);
	}

	private HistoryResponse getHistoryResponse() {
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
		return hr;
	}

	@Test
	public void testGetComponentStatusHistory() throws Exception {
		String envName = "getcompenv";
		HistoryResponse hr = getHistoryResponse();
		when(regionStatusService.getRegionStatusHistory(envName)).thenReturn(hr);
		mockMvc.perform(get("/componentHistory").contentType(MediaType.APPLICATION_JSON).param("environment", envName))
				.andExpect(status().isOk())
				// .andExpect(content().string(""))
				.andExpect(jsonPath("$.responseContent.appHistory[0].componentId",
						is(hr.getAppHistory().get(0).getComponentId())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].componentName",
						is(hr.getAppHistory().get(0).getComponentName())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].childComponentName",
						is(hr.getAppHistory().get(0).getChildComponentName())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].componentType",
						is(hr.getAppHistory().get(0).getComponentType())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].parentComponentId",
						is(hr.getAppHistory().get(0).getParentComponentId())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].parentComponentName",
						is(hr.getAppHistory().get(0).getParentComponentName())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].statusHistory[0].statusTime",
						is(hr.getAppHistory().get(0).getStatusHistory().get(0).getStatusTime())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].statusHistory[0].statusEast",
						is(hr.getAppHistory().get(0).getStatusHistory().get(0).getStatusEast())))
				.andExpect(jsonPath("$.responseContent.appHistory[0].statusHistory[0].statusWest",
						is(hr.getAppHistory().get(0).getStatusHistory().get(0).getStatusWest())))
				// .andExpect(jsonPath("$.responseContent.appHistory[0].statusHistory[0].percentageUpTimeEast",is(hr.getAppHistory().get(0).getStatusHistory().get(0).getPercentageUpTimeEast())))
				// .andExpect(jsonPath("$.responseContent.appHistory[0].statusHistory[0].percentageUpTimeWest",is(hr.getAppHistory().get(0).getStatusHistory().get(0).getPercentageUpTimeWest())));
				.andExpect(jsonPath("$.responseContent.apiHistory[0].componentId",
						is(hr.getApiHistory().get(0).getComponentId())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].componentName",
						is(hr.getApiHistory().get(0).getComponentName())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].childComponentName",
						is(hr.getApiHistory().get(0).getChildComponentName())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].componentType",
						is(hr.getApiHistory().get(0).getComponentType())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].parentComponentId",
						is(hr.getApiHistory().get(0).getParentComponentId())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].parentComponentName",
						is(hr.getApiHistory().get(0).getParentComponentName())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].statusTime",
						is(hr.getApiHistory().get(0).getStatusHistory().get(0).getStatusTime())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].statusEast",
						is(hr.getApiHistory().get(0).getStatusHistory().get(0).getStatusEast())))
				.andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].statusWest",
						is(hr.getApiHistory().get(0).getStatusHistory().get(0).getStatusWest())))
				// .andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].percentageUpTimeEast",is(hr.getApiHistory().get(0).getStatusHistory().get(0).getPercentageUpTimeEast())))
				// .andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].percentageUpTimeWest",is(hr.getApiHistory().get(0).getStatusHistory().get(0).getPercentageUpTimeWest())));verify(regionStatusService,
				// times(1)).getRegionStatusHistory(envName);
				.andExpect(jsonPath("$.responseContent.infraHistory[0].componentId",
						is(hr.getInfraHistory().get(0).getComponentId())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].componentName",
						is(hr.getInfraHistory().get(0).getComponentName())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].childComponentName",
						is(hr.getInfraHistory().get(0).getChildComponentName())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].componentType",
						is(hr.getInfraHistory().get(0).getComponentType())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].parentComponentId",
						is(hr.getInfraHistory().get(0).getParentComponentId())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].parentComponentName",
						is(hr.getInfraHistory().get(0).getParentComponentName())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].statusHistory[0].statusTime",
						is(hr.getInfraHistory().get(0).getStatusHistory().get(0).getStatusTime())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].statusHistory[0].statusEast",
						is(hr.getInfraHistory().get(0).getStatusHistory().get(0).getStatusEast())))
				.andExpect(jsonPath("$.responseContent.infraHistory[0].statusHistory[0].statusWest",
						is(hr.getInfraHistory().get(0).getStatusHistory().get(0).getStatusWest())));
		// .andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].percentageUpTimeEast",is(hr.getInfraHistory().get(0).getStatusHistory().get(0).getPercentageUpTimeEast())))
		// .andExpect(jsonPath("$.responseContent.apiHistory[0].statusHistory[0].percentageUpTimeWest",is(hr.getInfraHistory().get(0).getStatusHistory().get(0).getPercentageUpTimeWest())));
		verify(regionStatusService,times(1)).getRegionStatusHistory(envName);
		verifyNoMoreInteractions(regionStatusService);
	}
    @Test
    public void testSaveMessage() throws Exception {
          String environmentName="environmentName";
          String messageType="app";
          String message="message";
          String authToken="authToken";        
           doNothing().when(regionStatusService).loadMessages(environmentName, messageType, message, authToken);
           String auditLog = "Added Message: "+message+" for Message Type: "+messageType;
           doNothing().when(adminService).addAuditLog(authToken,auditLog);
           mockMvc.perform(put("/updateStatusMsg") 
                        .param("environment", environmentName)
                        .param("type", messageType)
                        .param("messages", message)
                        .param("authToken", authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
          verify(regionStatusService, times(1)).loadMessages(environmentName, messageType, message, authToken);
          verifyNoMoreInteractions(regionStatusService);
    }
	@Test
	public void testGetCurrentApis() throws Exception{
		String environment="environment";
		String componentIdsStrg="component";
		Long currentApi=1L;
		when(regionStatusService.getCurrentApi(environment, componentIdsStrg, TestDataProvider.getPlatform())).thenReturn(currentApi);
		  mockMvc.perform(get("/getCurrentApis") 
                  .param("environment", environment)
                  .param("component", componentIdsStrg)
                  .param("platform", TestDataProvider.getPlatform())
                  .contentType(MediaType.APPLICATION_JSON))
		  			//.andExpect(content().string(""))
                  .andExpect(status().isOk());
    verify(regionStatusService, times(1)).getCurrentApi(environment, componentIdsStrg, TestDataProvider.getPlatform());
    verifyNoMoreInteractions(regionStatusService);
		
	}
	private Messages getMessages() {
		Messages messages=new Messages();
		messages.setMessageId(1);
		messages.setComponentName("component_Name");
		messages.setMessageDate("message_Date");
		messages.setUserId("user_Id");
		messages.setMessage("new_message");
		return messages;
	}
	@Test
	public void testGetComponentMessage() throws Exception {
		Messages messages = getMessages();
		String environmentName="environment";
		String componentId="componentid";
		String region="region";
		List<Messages> msgs = new ArrayList<Messages>();
		msgs.add(messages);	
		when(regionStatusService.getComponentMessage(environmentName, componentId, region)).thenReturn(msgs);
		mockMvc.perform(get("/componentMessage")
				.param("environment", environmentName)
				.param("componentid", componentId)
				.param("region", region)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(messages)))
		.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseContent[0].messageId", is(msgs.get(0).getMessageId())))
				.andExpect(jsonPath("$.responseContent[0].userId", is(messages.getUserId())))
				.andExpect(jsonPath("$.responseContent[0].componentName", is(messages.getComponentName())))
				.andExpect(jsonPath("$.responseContent[0].message", is(messages.getMessage())))
				.andExpect(jsonPath("$.responseContent[0].messageDate", is(messages.getMessageDate())));		
		verify(regionStatusService, times(1)).getComponentMessage(environmentName, componentId, region);
		verifyNoMoreInteractions(regionStatusService);		
	}
	private AvailabilityData getAvailabilityData() {
		AvailabilityData availabilityData=new AvailabilityData();
		availabilityData.setComponentId(1);
		availabilityData.setAvailabilityPercentageEast(1);
		availabilityData.setAvailabilityPercentageWest(1);
		return availabilityData;
	}
	@Test
	public void testGetAvailabilityPercentage() throws Exception{
		String environmentName="environment";
		String interval="interval";
		
		AvailabilityData availabilityData=getAvailabilityData();
		List<AvailabilityData> availabilityDatas=new ArrayList<AvailabilityData>();
		availabilityDatas.add(availabilityData);
		when(regionStatusService.getAvailabilityPercentage(environmentName, interval, TestDataProvider.DEFAULT_PLATFORM, TestDataProvider.DEFAULT_REGION )).thenReturn(availabilityDatas);
		mockMvc.perform(get("/getAvailabilityPercentage")
				.param("environment", environmentName)
				.param("interval", interval)
				.param("platform", TestDataProvider.DEFAULT_PLATFORM)
				.param("region", TestDataProvider.DEFAULT_REGION)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(availabilityData)))
		         .andExpect(status().isOk())
		         .andExpect(jsonPath("$.responseContent[0].componentId", is(availabilityData.getComponentId())))
		         .andExpect(jsonPath("$.responseContent[0].availabilityPercentageWest", is(availabilityData.getAvailabilityPercentageWest())))
		         .andExpect(jsonPath("$.responseContent[0].availabilityPercentageEast", is(availabilityData.getAvailabilityPercentageEast())));
		verify(regionStatusService,times(1)).getAvailabilityPercentage(environmentName, interval, TestDataProvider.DEFAULT_PLATFORM, TestDataProvider.DEFAULT_REGION);
		verifyNoMoreInteractions(regionStatusService);			
	}
	private ApiStatus getApiStatus() {
		ApiStatus apiStatus=new ApiStatus();
		apiStatus.setComponentId(1);
		apiStatus.setDeltaValue(1);
		apiStatus.setEnvironmentId(1);
		apiStatus.setTotalContainers(1L);
		apiStatus.setComponentName("new_componentName");
		apiStatus.setStatusDate("new_statusDate");
		apiStatus.setTotalApis(1L);
		return apiStatus;		
	}
	@Test
	public void testGetApplicationApis() throws Exception{
		String startDate="startDate";
		String endDate="endDate";
		String environment="environment";
		String componentIdsStrg="component";
		ApiStatus apiStatus=getApiStatus();
		List<ApiStatus> apiStatusls=new ArrayList<ApiStatus>();
		apiStatusls.add(apiStatus);
		when(regionStatusService.getApplicationApis(startDate, endDate, environment, componentIdsStrg, TestDataProvider.getPlatform())).thenReturn(apiStatusls);
		mockMvc.perform(get("/getApplicationApis")
				.param("startDate", startDate)
				.param("endDate", endDate)
				.param("environment", environment)
				.param("component", componentIdsStrg)
				.param("platform", TestDataProvider.DEFAULT_PLATFORM)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(apiStatus)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.responseContent[0].componentId", is(apiStatus.getComponentId())))
		.andExpect(jsonPath("$.responseContent[0].componentName", is(apiStatus.getComponentName())))
		.andExpect(jsonPath("$.responseContent[0].environmentId", is(apiStatus.getEnvironmentId())))
		.andExpect(jsonPath("$.responseContent[0].statusDate", is(apiStatus.getStatusDate())))
		//TODO	//.andExpect(jsonPath("$.responseContent[0].totalContainers", is(apiStatus.getTotalContainers())))
	//TODO	//.andExpect(jsonPath("$.responseContent[0].totalApis", is(apiStatus.getTotalApis())))
		.andExpect(jsonPath("$.responseContent[0].deltaValue", is(apiStatus.getDeltaValue())));
		verify(regionStatusService,times(1)).getApplicationApis(startDate, endDate, environment, componentIdsStrg, TestDataProvider.getPlatform());
		verifyNoMoreInteractions(regionStatusService);			
	}
	@Test
	public void testGetSubscriptionMails() throws Exception{
		String environmentName="environment";
		int componentId=1;
		Subscription emails=TestDataProvider.getSubscription();
		List<Subscription> emaills=new ArrayList<Subscription>();
		emaills.add(emails);
		when(regionStatusService.getSubscribedEmailIdList(componentId, environmentName)).thenReturn(emaills);
		mockMvc.perform(get("/getSubscriptionMails")
				.param("environment", environmentName)
				.param("componentId", ""+emails.getComponentId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(emails)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.responseContent[0].alertSubscriptionId", is(emails.getAlertSubscriptionId())))
		.andExpect(jsonPath("$.responseContent[0].componentId", is(emails.getComponentId())))
		.andExpect(jsonPath("$.responseContent[0].componentName", is(emails.getComponentName())))
		.andExpect(jsonPath("$.responseContent[0].subsciptionVal", is(emails.getSubsciptionVal())))
		.andExpect(jsonPath("$.responseContent[0].environmentName", is(emails.getEnvironmentName())))
		.andExpect(jsonPath("$.responseContent[0].environmentId", is(emails.getEnvironmentId())))
		.andExpect(jsonPath("$.responseContent[0].authToken", is(emails.getAuthToken())))
		.andExpect(jsonPath("$.responseContent[0].subsciptionType", is(emails.getSubsciptionType())))
		.andExpect(jsonPath("$.responseContent[0].globalSubscriptionTypeId", is(emails.getGlobalSubscriptionTypeId())))
		.andExpect(jsonPath("$.responseContent[0].globalSubscriptionType", is(emails.getGlobalSubscriptionType())));
		verify(regionStatusService,times(1)).getSubscribedEmailIdList(componentId, environmentName);
		verifyNoMoreInteractions(regionStatusService);			
	}
	@Test
    public void testGetCurrentContainers() throws Exception {
          String environmentName="environment";
          String componentIdsStrg="component";
          boolean isParentComponents =false;  
          Long currentApi=1L;
         when(regionStatusService.getCurrentContainer(environmentName, componentIdsStrg, isParentComponents, TestDataProvider.getPlatform())).thenReturn(currentApi);
          mockMvc.perform(get("/getCurrentContainers") 
                        .param("environment", environmentName)
                        .param("component", componentIdsStrg)
                        .param("false", ""+isParentComponents)
                        .param("platform", TestDataProvider.getPlatform())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
          verify(regionStatusService, times(1)).getCurrentContainer(environmentName, componentIdsStrg, isParentComponents, TestDataProvider.getPlatform());
          verifyNoMoreInteractions(regionStatusService);
    }
	@Test
	public void testGetCompMessageHistory() throws Exception{
		String environmentName="environment";
        String componentId="componentid";
        String region="region";
        String logDate="logDate";
        Messages messages=getMessages();
       List<Messages> msgList=new ArrayList<Messages>();
       msgList.add(messages);
       when(regionStatusService.getComponentMessage(environmentName, componentId, region, logDate)).thenReturn(msgList);
		mockMvc.perform(get("/getCompMessageHistory")
				.param("environment", environmentName)
				.param("componentid", componentId)
				.param("region", region)
				.param("logDate", logDate)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(messages)))
		.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseContent[0].messageId", is(messages.getMessageId())))
				.andExpect(jsonPath("$.responseContent[0].userId", is(messages.getUserId())))
				.andExpect(jsonPath("$.responseContent[0].componentName", is(messages.getComponentName())))
				.andExpect(jsonPath("$.responseContent[0].message", is(messages.getMessage())))
				.andExpect(jsonPath("$.responseContent[0].messageDate", is(messages.getMessageDate())));		
		verify(regionStatusService, times(1)).getComponentMessage(environmentName, componentId, region, logDate);
		verifyNoMoreInteractions(regionStatusService);		
	}
	private ContainerStatus getContainerStatus() {
		ContainerStatus containerStatus=new ContainerStatus();
		containerStatus.setComponentId(1);
		containerStatus.setComponentName("new_componentName");
		containerStatus.setDeltaValue(1);
		containerStatus.setStatusDate("new_statusDate");
		containerStatus.setTotalContainers(1L);
		return containerStatus;		
	}
	
	@Test
	public void testGetAppContainers() throws Exception{
		String startDate="startDate";
		String endDate="endDate";
		String environment="environment";
		String componentIdsStrg="component";
		boolean isParentComponents=false;
		ContainerStatus containerStatus=getContainerStatus();
		List<ContainerStatus> containerStatusls=new ArrayList<ContainerStatus>();
		containerStatusls.add(containerStatus);
		when(regionStatusService.getAppContainers(startDate, endDate, environment, componentIdsStrg, isParentComponents, TestDataProvider.getPlatform())).thenReturn(containerStatusls);
		mockMvc.perform(get("/getAppContainers")
				.param("startDate", startDate)
				.param("endDate", endDate)
				.param("environment", environment)
				.param("component", componentIdsStrg)
				.param("false", ""+isParentComponents)
				.param("platform", TestDataProvider.DEFAULT_PLATFORM)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(containerStatus)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.responseContent[0].componentId", is(containerStatus.getComponentId())))
		.andExpect(jsonPath("$.responseContent[0].componentName", is(containerStatus.getComponentName())))
		.andExpect(jsonPath("$.responseContent[0].statusDate", is(containerStatus.getStatusDate())))
		//TODO .andExpect(jsonPath("$.responseContent[0].totalContainers", is(containerStatus.getTotalContainers())))
		.andExpect(jsonPath("$.responseContent[0].deltaValue", is(containerStatus.getDeltaValue())));
		verify(regionStatusService,times(1)).getAppContainers(startDate, endDate, environment, componentIdsStrg, isParentComponents, TestDataProvider.getPlatform());
		verifyNoMoreInteractions(regionStatusService);			
	}
	
	@Test
    public void testDoLogout() throws Exception {
	 String authToken="token";	      
        doNothing().when(regionStatusService).doLogOutForUser(authToken);
        mockMvc.perform(post("/doLogout")
        	.content(authToken))	            	        
            .andExpect(status().isOk());
        verify(regionStatusService, times(1)).doLogOutForUser(authToken);
        verifyNoMoreInteractions(regionStatusService);
    }
}
