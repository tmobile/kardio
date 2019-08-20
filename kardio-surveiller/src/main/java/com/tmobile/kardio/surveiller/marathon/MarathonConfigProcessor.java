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
package com.tmobile.kardio.surveiller.marathon;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.HealthCheckType;
import com.tmobile.kardio.surveiller.enums.Region;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.util.CommonsUtil;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.ComponentVO;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * Class to handle the Marathon configurations
 */
public class MarathonConfigProcessor {
    
    private static final Logger logger = Logger.getLogger(MarathonConfigProcessor.class);
    
    private MarathonConfigProcessor() {}
    
    /**
     * Function to update the API list
     * @param environmentVOs Environment details
     * @throws JsonProcessingException
     * @throws SQLException
     * @throws IOException
     */
    public static void updateApiList(List<EnvironmentVO> environmentVOs) throws JsonProcessingException, SQLException, IOException{
        for(EnvironmentVO eVo : environmentVOs){
            if(eVo.getMarathonJson() != null && !eVo.getMarathonJson().equals("")){
                updateComponentFromMarathonConfig(eVo.getMarathonJson(), eVo.getEnvironmentId(), Region.WEST_REGION.getRegionId());
            }
            if(eVo.getEastMarathonJson() != null && !eVo.getEastMarathonJson().equals("")){
                updateComponentFromMarathonConfig(eVo.getEastMarathonJson(), eVo.getEnvironmentId(), Region.EAST_REGION.getRegionId());
            }
        }
    }
    
    /**
     * Function to update component table from marathon config 
     * @param json Marathon config JSON
     * @param environmentId environment Id
     * @throws JsonProcesssingException
     * @throws IOException
     * @throws SQLException
     */
    @SuppressWarnings("deprecation")
	public static void updateComponentFromMarathonConfig(String json, int environmentId, long regionId) throws SQLException, JsonProcessingException, IOException{
        logger.debug("Getting API's from Marathon configuration and posting to DB. environmentId = " + environmentId);

        List<ComponentVO> oldAPIComponentVOs = DBQueryUtil.getAllAPIComponentDetails(environmentId, SurveillerConstants.MESOS_PLATFORM, regionId);
        List<ComponentVO> newAPIComponentVOs = new ArrayList<ComponentVO>();
        List<HealthCheckVO> statusChangedList = new ArrayList<HealthCheckVO>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        ArrayNode appsNode = (ArrayNode) rootNode.get("apps");
        Iterator<JsonNode> appsIterator = appsNode.getElements();
        while (appsIterator.hasNext()) {
            JsonNode appsInNode = appsIterator.next();
            JsonNode id = appsInNode.get("id");
            String idStr = id.getValueAsText();
            String idSubStr = idStr.substring(id.getValueAsText().indexOf('/') + 1);
            if(idSubStr.indexOf('/')  < 0){
                logger.info("Unable to process App Id : " + idStr);
                continue;
            }
            String appName = idSubStr.substring(0, idSubStr.indexOf('/'));
            String apiName = idSubStr.substring(idSubStr.indexOf('/')+1);
            
            Integer tasksRunning = appsInNode.get(SurveillerConstants.NUM_OF_TASKS_RUNNING) == null ? null : appsInNode.get(SurveillerConstants.NUM_OF_TASKS_RUNNING).getIntValue();
            Integer tasksStaged = appsInNode.get(SurveillerConstants.NUM_OF_TASKS_RUNNING) == null ? null : appsInNode.get(SurveillerConstants.NUM_OF_TASKS_STAGED).getIntValue();
            
            if(tasksRunning != null && tasksRunning.intValue() == 0 && tasksStaged != null && tasksStaged.intValue() == 0){
            	continue;
            }
            
            JsonNode lblNode = appsInNode.get("labels");
            int apiId = 0;
            if(lblNode != null && lblNode.get("HEALTHCHECK") != null){
                String healthCheckUrl = lblNode.get("HEALTHCHECK").getTextValue();
                if(healthCheckUrl != null && !healthCheckUrl.trim().equals("")){
                    apiId = DBQueryUtil.checkAndInsertComponent(appName, apiName, ComponentType.APP, healthCheckUrl, environmentId,
                    		regionId, HealthCheckType.URL_200_CHECK, SurveillerConstants.MESOS_PLATFORM);
                }
            }
            // Case in which there is no health check URL configure inside label
            // Mark as Warning if there is atleast one unhealthy task
            // Mark as Down if all tasks are unhealthy
            if(apiId == 0){
            	Integer tasksUnhealthy = appsInNode.get("tasksUnhealthy") == null ? null : appsInNode.get("tasksUnhealthy").getIntValue();
                if(tasksRunning != null && tasksUnhealthy != null && tasksRunning.intValue() != 0){
                    Status status = Status.UP;
                    if(tasksRunning.equals(tasksUnhealthy) ){
                        status = Status.DOWN;
                    }else if(tasksUnhealthy.intValue() != 0){
                        status = Status.WARNING;
                    }
                    apiId = DBQueryUtil.checkAndInsertComponent(appName, apiName, ComponentType.APP, null, environmentId,
                    		regionId, HealthCheckType.DUMMY, SurveillerConstants.MESOS_PLATFORM);
                    String marathonMessage = "Marathon Status - Running Tasks = " + tasksRunning + "; UnHealthy Task = " + tasksUnhealthy;
                    boolean isStatusChanged = DBQueryUtil.checkAndUpdateMessage(apiId,environmentId,regionId, HealthCheckType.DUMMY,status,marathonMessage);
                    if(isStatusChanged){
                    	statusChangedList.add(createVoWithComponentDetails(apiId,environmentId,regionId,status,marathonMessage));
                    }
                    DBQueryUtil.updateMarathonHealthCheckStatus(apiId,environmentId,regionId, HealthCheckType.DUMMY,status, isStatusChanged);
                }
            }
            if(apiId != 0){
                newAPIComponentVOs.add(DBQueryUtil.getComponent(apiId));
            }
        }
        compareAPILists(oldAPIComponentVOs, newAPIComponentVOs, environmentId, regionId);
        if(statusChangedList.size() > 0){
        	CommonsUtil.sendMailForChangedStatus(statusChangedList);
        }
    }
    
    /**
     * Create VO With Component Details to send mail for status change.
     * 
     * @param apiId ID of API
     * @param environmentId ID for environment
     * @param regionId ID for region
     * @param status Status of API
     * @param marathonMessage Marathon message
     */
    private static HealthCheckVO createVoWithComponentDetails(int apiId, int environmentId, Long regionId, Status status,
			String marathonMessage) {
    	HealthCheckVO returnVal = new HealthCheckVO();
    	returnVal.setHealthCheckComponentId((long)apiId);
    	returnVal.setEnvironmentId(environmentId);
    	returnVal.setHealthCheckRegionId(regionId);
    	returnVal.setStatus(new StatusVO(status, marathonMessage));
    	returnVal.setFailureStatusMessage(marathonMessage);
    	return returnVal;
	}
    
	/**
     * Function to compare old list of ComponentVO and new list ComponentVO and update the delInd
     * @param oldAPIComponentVOs
     * @param newAPIComponentVOs
     * @throws SQLException
     */
    public static void compareAPILists(List<ComponentVO> oldAPIComponentVOs, List<ComponentVO> newAPIComponentVOs, int enviromentId, long regionId) throws SQLException{
        logger.debug("Comparing old DB API list and new API list from marathon config");
        for(ComponentVO cVo : oldAPIComponentVOs){
            int compId = cVo.getComponentId();
            boolean isfound = false;
            for(ComponentVO newCVo : newAPIComponentVOs){
                if(newCVo.getComponentId() == compId){
                    isfound = true;
                    break;
                }
            }
            //Update delInd of component and healthCheck. Note :- cVo.getDelInd() is the DEL_IND of healthCheck table - refer DB_QUERY_GET_COMPONENT_DETAILS
            if(!isfound && cVo.getDelInd() == 0){
                DBQueryUtil.updateDelIndForHealthCheck(compId, 1, enviromentId, regionId);
            } else if(isfound && cVo.getDelInd() == 1){
                DBQueryUtil.updateDelIndForHealthCheck(compId, 0, enviromentId, regionId);
            }
        }
    }
    
}
