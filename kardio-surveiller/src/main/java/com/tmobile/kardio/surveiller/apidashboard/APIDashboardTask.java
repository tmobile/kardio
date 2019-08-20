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
package com.tmobile.kardio.surveiller.apidashboard;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.AppTransformConfig;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

/**
 * This manages the different tasks required for API dashboard  
 *
 */
public class APIDashboardTask {
	
	private static final Logger logger = Logger.getLogger(APIDashboardTask.class);
	
	private APIDashboardTask() {}
	
	/**
	 * To load the API Dashboard data
	 * @throws Exception 
	 */
	public static void doApiDashboardDataLoad() throws Exception {
		logger.info("************** STARTED doApiDashboardDataLoad **************");
		
		List<EnvironmentVO> envMarInfoList = DBQueryUtil.getAllEnvironments();
		AppTransformConfig config = DBQueryUtil.readAppTransformConfig();
		for(EnvironmentVO envInfo :envMarInfoList){
			logger.info("Surveiller task DOAPIDASHBOARDTASK - STARTED : Enviroment -"+envInfo.getEnvironmentName());
			if(envInfo.getMarathonJson() == null){
				continue;
			}
			
			Map<String, String> mapPromLookup =  DBQueryUtil.getPromLookupDetails(envInfo.getEnvironmentId(), SurveillerConstants.MESOS_PLATFORM);
			
			Map<String, Integer> mapCompId = DBQueryUtil.getAllComponentIdForEnv(envInfo.getEnvironmentId(), SurveillerConstants.MESOS_PLATFORM);
			String json = envInfo.getMarathonJson();
			Map<String, Integer> mapApiSts =  DBQueryUtil.getCurrentApiDetails(envInfo.getEnvironmentId());
			Map<String, Integer> mapContainerSts =  DBQueryUtil.getCurrentContainerDetails(envInfo.getEnvironmentId());
			Set<Integer> componentsListWithNoCreatedDate = DBQueryUtil.getAppsLauchDateNull(envInfo.getEnvironmentId());
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(json);
			Map<String , Integer> appApiMap = new HashMap<String , Integer>();
			ArrayNode appsNode = (ArrayNode) rootNode.get("apps");
			Iterator<JsonNode> appsIterator = appsNode.getElements();
			while (appsIterator.hasNext()) {
				JsonNode appsInNode = appsIterator.next();
				JsonNode id = appsInNode.get("id");
				String idStr = id.getTextValue();
				@SuppressWarnings("deprecation")
				String idSubStr = idStr.substring(id.getValueAsText().indexOf('/') + 1);
				if (idSubStr.indexOf('/') < 0) {
					logger.info("Unable to process App Id : " + idStr);
					continue;
				}
				String appName = idSubStr.substring(0, idSubStr.indexOf('/'));
				String apiName = idSubStr.substring(idSubStr.indexOf('/') + 1);
				if (config != null && config.getTransformName().containsKey(appName)) {
					appName = config.getTransformName().get(appName);
				}
				String fullAppName = appName + "/" + apiName;
				if(!mapCompId.containsKey(fullAppName)){
	            	logger.info("This Service - " + apiName + " is not available in the System");
	            	continue;
	            }
				int compId = mapCompId.get(fullAppName);
				int instance = appsInNode.get("instances").getIntValue();
				if(mapContainerSts.containsKey(appName + "/" + apiName)){
					if(instance != mapContainerSts.get(appName + "/" + apiName)){
						DBQueryUtil.updateContainerDetails(envInfo.getEnvironmentId(), compId, instance);
					}
				} else {
					/*This is the call to load container status */
					DBQueryUtil.loadContainerStatus(envInfo.getEnvironmentId(), compId, instance);
					/*Checking Whether the api name on the launch date Map*/
					if(componentsListWithNoCreatedDate.contains(compId)){
						String createdDate = getMarathonAppLaunchDate(envInfo, idSubStr);
						if(createdDate != null){
							DBQueryUtil.updateAppLaunchDate(createdDate, compId, envInfo.getEnvironmentId());
						}
					}
				}
				Integer tasksRunning = appsInNode.get(SurveillerConstants.NUM_OF_TASKS_RUNNING) == null ? null : appsInNode.get(SurveillerConstants.NUM_OF_TASKS_RUNNING).getIntValue();
				Integer tasksStaged = appsInNode.get(SurveillerConstants.NUM_OF_TASKS_RUNNING) == null ? null : appsInNode.get(SurveillerConstants.NUM_OF_TASKS_STAGED).getIntValue();
				if(tasksRunning != null && tasksRunning.intValue() == 0 && tasksStaged != null && tasksStaged.intValue() == 0){
					continue;
				}
				/*This is for adding the number of APIS to a map*/
				if(appApiMap.containsKey(appName)){
					appApiMap.put(appName, appApiMap.get(appName) + 1);
				}else{
					appApiMap.put(appName, 1);
				}
				
				/*Prom lookup table data load*/
				//String fullAppName = appName+"/"+apiName;
				JsonNode lblNode = appsInNode.get("labels");
	            String httpPath = null;
	            if(lblNode != null){
	               if(lblNode.get(SurveillerConstants.LABEL_HTTP_PATH_URL) != null){
	            	  String httpPathUrl = lblNode.get(SurveillerConstants.LABEL_HTTP_PATH_URL).getTextValue();
	                  if(httpPathUrl != null && !httpPathUrl.trim().equals("")){
	                	  httpPath = httpPathUrl+SurveillerConstants.URL_SUFFIX_BACKEND;
	                  }else{
	                	  String httpPortIdx = lblNode.get(SurveillerConstants.LABEL_HTTP_PORT_IDX_0_NAME).getTextValue();
	                	  String httpPathUri = lblNode.get("HTTP_PATH_URI").getTextValue();
	                	  httpPath = httpPortIdx+httpPathUri.replaceAll("/", "_") + SurveillerConstants.URL_SUFFIX_BACKEND;
	                  }
	               }else if(lblNode.get(SurveillerConstants.LABEL_HTTP_PORT_IDX_0_NAME) != null){
	            	   httpPath = lblNode.get(SurveillerConstants.LABEL_HTTP_PORT_IDX_0_NAME).getTextValue()+SurveillerConstants.URL_SUFFIX_BACKEND;
	               }else{
	            	   logger.info("Label empty for the application - " + fullAppName);
	            	   continue;
	               }
	               if(mapPromLookup.isEmpty() || mapPromLookup == null){
	            	  DBQueryUtil.loadPromLookup(envInfo.getEnvironmentId(), compId, httpPath);
	               }else{
		               if(mapPromLookup.containsKey(fullAppName)){
		            	   if(mapPromLookup.get(fullAppName) == null || !mapPromLookup.get(fullAppName).equalsIgnoreCase(httpPath)){
		            		   DBQueryUtil.updatePromLookup(envInfo.getEnvironmentId(), compId, httpPath);
		            	   }
		               }else{
		            	   DBQueryUtil.loadPromLookup(envInfo.getEnvironmentId(), compId, httpPath);
		               }
	               }
	            }
			}
			for(Entry<String, Integer> appApiInfo : appApiMap.entrySet()){
				if(mapApiSts.containsKey(appApiInfo.getKey())){
					if(appApiInfo.getValue() != mapApiSts.get(appApiInfo.getKey())){
						DBQueryUtil.updateApiDetails(envInfo.getEnvironmentId(),appApiInfo.getKey(),appApiInfo.getValue(), SurveillerConstants.MESOS_PLATFORM);
					}
				} else {
					DBQueryUtil.loadApiStatus(envInfo.getEnvironmentId(), appApiInfo.getKey(), appApiInfo.getValue());
				}
			}
			logger.info("Surveiller task DOAPIDASHBOARDTASK - COMPLETED : Enviroment -"+envInfo.getEnvironmentName());
		}
		logger.info("************** COMPLETED doApiDashboardDataLoad **************");
	}

	/**
	 * @param envInfo
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	private static String getMarathonAppLaunchDate(EnvironmentVO envInfo,String appName) throws Exception {
		String appVerUrl = envInfo.getMarathonUrl()+"//" + appName + "/versions";
		String appVersionJson = null;
		try{
			appVersionJson = RestCommunicationHandler.getResponse(appVerUrl, true, "Basic ", envInfo.getMarathonCred());
		}catch(FileNotFoundException ex){
			logger.error("Unable to get Version of API : AppName: " + appName + "; URL :"+ appVerUrl, ex);
		}

		if(appVersionJson == null){
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(appVersionJson).get("versions");
		String launchDate = null; 
		if(rootNode.isArray()){
			launchDate = rootNode.get(rootNode.size() - 1).asText();
		} else {
			logger.info("Launch Date for "+ appName +" is null");
		}
		return launchDate;
	}

}
