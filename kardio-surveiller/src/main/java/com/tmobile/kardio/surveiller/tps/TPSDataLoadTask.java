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
/**
 * 
 */
package com.tmobile.kardio.surveiller.tps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.exception.GeneralException;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.PropertyUtil;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

/**
 * @author U29842
 *
 */
public class TPSDataLoadTask {

	
	private static final Logger logger = Logger.getLogger(TPSDataLoadTask.class);
	
	static PropertyUtil prop = PropertyUtil.getInstance();
	
	private TPSDataLoadTask() {}
	/**
	 * To load the TPS for each service
	 * @throws Exception 
	 */
	public static void doTpsDataLoad(String platform) throws Exception{
		logger.info("************** STARTED doTpsDataLoad - "+platform+"**************");
		List<EnvironmentVO> envMarInfoList = DBQueryUtil.getAllEnvironments();
		for(EnvironmentVO envInfo :envMarInfoList){
			logger.info("STARTED doTpsDataLoad for Environment - "+envInfo.getEnvironmentName());
			if(platform.equalsIgnoreCase(SurveillerConstants.MESOS_PLATFORM)){
			   if(envInfo.getMesosTpsQuery() == null &&  envInfo.getMesosLatencyQuery() == null){
				   logger.info("TPS & Latency query not available for environment - "+envInfo.getEnvironmentName());
				   continue;
			    }
			   loadTpsLatency(envInfo.getMesosTpsQuery(),envInfo.getMesosLatencyQuery(),platform,envInfo);
			}else if(platform.equalsIgnoreCase(SurveillerConstants.K8S_PLATFORM)){
				if(envInfo.getK8sTpsQuery() == null &&  envInfo.getK8sLatencyQuery() == null){
				  logger.info("TPS & Latency query not available for environment - "+envInfo.getEnvironmentName());
				  continue;
				}
				loadTpsLatency(envInfo.getK8sTpsQuery(),envInfo.getK8sLatencyQuery(),platform,envInfo);
			}
			logger.info("COMPLETED doTpsDataLoad for Environment - "+envInfo.getEnvironmentName());
		}
		logger.info("************** COMPLETED doTpsDataLoad - "+platform+"**************");
	}

	/**
	 * A common method to Call the prometheous based on the queries passed (TPS/Latency query).
	 * Iterate the prometheous output json, the response of this method is a Map with componentID as key and
	 * TPS/Latency value as value of the Map.
	 * @param promUrl
	 * @param EnvName
	 * @param promLookMap
	 * @param platform
	 * @return
	 * @throws Exception
	 */
	private static Map<Integer, Float> getMatrxValuesFromPrometeous(String promUrl, String EnvName, Map<String, Integer> promLookMap, String platform) throws Exception{
		String lookUpPath = null;
		if(platform.equals("Mesos")){
			lookUpPath = "proxyname";
		}else if(platform.equals("K8s")){
			lookUpPath = "service";
		}else{
			logger.error("Not a valid platform");
		}
		String prometheusResponse = RestCommunicationHandler.getResponse(promUrl, false, null, null);
		ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(prometheusResponse);
        if(rootNode.get("status") == null || !rootNode.get("status").getTextValue().equalsIgnoreCase("success")){
        	logger.error("Prometheus : Status Check Failed For Environment " + EnvName );
        	logger.error("Prometheus Response : \n" + prometheusResponse);
        	throw new GeneralException("Prometheus : Status Check Failed. Environment name " + EnvName);
        }
        if(rootNode.get("data") == null
        		|| rootNode.get("data").get("result") == null
        		|| !rootNode.get("data").get("result").isArray()){
        	logger.error("/data/result Node is missing in prometheus response. Environment name " + EnvName );
        	logger.error("Prometheus Response : \n" + prometheusResponse);
        	throw new GeneralException("/data/result Node is missing in prometheus response. Environment name " + EnvName);
        }
        
        Map<Integer, Float> valueMap = new HashMap<Integer, Float>();
        JsonNode valueNode = null;
        JsonNode metricNode = null;
        for (final JsonNode arrayNode : rootNode.get("data").get("result")) {
        	metricNode = arrayNode.get("metric");
        	if(metricNode == null){
        		logger.error("Metric Node cannot be null");
        		continue;
        	}
        	valueNode = arrayNode.get("value");
        	if(valueNode == null){
        		logger.error("/data/result/value Node is missing in prometheus response. Environment name " + EnvName);
                logger.error("Prometheus Response : \n" + prometheusResponse);
                throw new GeneralException("/data/result/value Node is missing in prometheus response. Environment name " + EnvName);
            }
        	String tpsValueString = valueNode.get(1).getTextValue();
            float tpsValue = Float.parseFloat(tpsValueString);
            if(metricNode.get(lookUpPath) == null){
            	logger.error("Metric Node without lookUpPath");
        		continue;
            }
            JsonNode lookupPathNode = metricNode.get(lookUpPath);
            String metricLookup = lookupPathNode.getTextValue();
        	if(promLookMap.containsKey(metricLookup)){
        		
        		int compId = promLookMap.get(metricLookup);
        		if(valueMap == null || valueMap.isEmpty()){
        			valueMap.put(compId, tpsValue);
        		}else if(valueMap.containsKey(compId)){
        			valueMap.put(compId, valueMap.get(compId)+tpsValue);
        		}else{
        			valueMap.put(compId, tpsValue);
        		}
        	}
        }
		return valueMap;
	}
	
	/**
	 * @param promTpsUrl
	 * @param promLatencyUrl
	 * @param platform
	 * @param envInfo
	 * @throws Exception
	 */
	private static void loadTpsLatency(String promTpsUrl, String promLatencyUrl, String platform, EnvironmentVO envInfo) throws Exception{
		
				Map<String, Integer> promLookMap = DBQueryUtil.getPromLookupHttpPath(envInfo.getEnvironmentId(), platform);	
				List<Integer> listOfComp = DBQueryUtil.getAllCurrentTPS(envInfo.getEnvironmentId(), platform);
				Map<Integer, List<Float>> mapTpsLat = DBQueryUtil.getTpsLatencyHsitory(envInfo.getEnvironmentId(), platform);
				Map<Integer, Float> tpsValues = null;
				Map<Integer, Float> latencyValues = null;
				if(promTpsUrl != null){
				    tpsValues = getMatrxValuesFromPrometeous(promTpsUrl, envInfo.getEnvironmentName(), promLookMap, platform);
				}
				if(promLatencyUrl != null){
					latencyValues = getMatrxValuesFromPrometeous(promLatencyUrl, envInfo.getEnvironmentName(), promLookMap, platform);
				}
				
				if(tpsValues == null && latencyValues == null){
					return;
				}
				/*Move all the values of Map to set to avoid duplicate*/
				Set<Integer> componentSet = new HashSet<Integer>(promLookMap.values());
				/*
				 * Iterate the tpsValue & latencyValue maps.
				 * If the component is already in the tps_service table update the tps&latency value,
				 * if not insert a new row with tps & latency values.
				 */
				//for(Entry<String, Integer> promLookInfo : promLookMap.entrySet()){
				for(Integer compId : componentSet){
					float tpsVal = 0;	
					float latencyVal = 0;
					if(tpsValues != null){
					   tpsVal = tpsValues.get(compId) == null ? 0 : tpsValues.get(compId);
					}
					if(latencyValues != null){ 
					   latencyVal = latencyValues.get(compId) == null ? 0 : latencyValues.get(compId);
				    }
					if(listOfComp != null && listOfComp.contains(compId)){
		    			//Update the new TPS & Latency vaule
		    			DBQueryUtil.updateTpsService(envInfo.getEnvironmentId(), compId, tpsVal, latencyVal);
		    		}else{
		    			//Insert the new TPS & Latency vaule
		    			DBQueryUtil.loadTpsService(envInfo.getEnvironmentId(), compId, tpsVal, latencyVal );
		    		}
					/* Code to load TPS & Latency to tps_latency_history table
					 * This code put daily one entry in tps_latency_history table.
					 */
					if(mapTpsLat.containsKey(compId)){
						List<Float> tpsLat = mapTpsLat.get(compId);
						tpsVal = (tpsLat.get(0) + tpsVal)/2;
						
						latencyVal = (tpsLat.get(1) + latencyVal)/2;
						if(tpsVal == tpsLat.get(0) && latencyVal == tpsLat.get(0)){
							continue;
						}
						//Update the TPS & Latency vaules to tps_latency_history table
						DBQueryUtil.updateTpsLatHistory(envInfo.getEnvironmentId(), compId, tpsVal, latencyVal, platform);
					}else{
						//Insert a new row in to tps_latency_history table with TPS & Latency values
						if(platform.equals(SurveillerConstants.MESOS_PLATFORM)){
						   DBQueryUtil.loadTpsLatencyHistory(envInfo.getEnvironmentId(), compId, tpsVal, latencyVal);
						}else if(platform.equals(SurveillerConstants.K8S_PLATFORM)){
						   DBQueryUtil.loadK8sTpsLatencyHistory(envInfo.getEnvironmentId(), compId, tpsVal, latencyVal);
						}
					}
				}
	}
}
