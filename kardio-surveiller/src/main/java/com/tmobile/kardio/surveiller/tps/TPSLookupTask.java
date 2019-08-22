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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.exception.GeneralException;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.CommonsUtil;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.PropertyUtil;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.surveiller.vo.ServiceLabelVO;

/**
 * @author U29842
 *
 */
public class TPSLookupTask {
	
	private static final Logger logger = Logger.getLogger(TPSLookupTask.class);
	
	private TPSLookupTask() {}
	/**
	 * @throws SQLException 
	 */
	public static void doTpsLookupLoad() throws SQLException {
		
		logger.info("************** STARTED doTpsLookupLoad **************");
		List<EnvironmentVO> environmentVOs = DBQueryUtil.getAllEnvironments();
		for(EnvironmentVO eVo : environmentVOs){
			if(eVo.getK8sUrl() == null){
		    	continue;
		    }
		    loadTpsLookup(eVo);
		}

	}

	/**
	 * Method to load the tps lookup parameter for each service
	 * @param eVo
	 */
	private static void loadTpsLookup(EnvironmentVO eVo) {
		
		try {
			Map<String, Integer> mapCompId = DBQueryUtil.getAllComponentIdForEnv(eVo.getEnvironmentId(),SurveillerConstants.K8S_PLATFORM);
			Map<String, String> mapPromLookup =  DBQueryUtil.getPromLookupDetails(eVo.getEnvironmentId(), SurveillerConstants.K8S_PLATFORM);
			String authToken = CommonsUtil.getK8sAuthToken(eVo.getK8sCred());
			if(authToken == null || authToken.equals("")){
				logger.error("Got Invalid token for the environment -"+eVo.getEnvironmentName());
				return;
			}
			/*FIXME: Add method comment*/
			Map<String, String> appServiceMap = getAppService(eVo, authToken);
			
			String ingressApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_INGRESS_PATH);
			String ingressJson = RestCommunicationHandler.getResponse(ingressApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);	
			ObjectMapper mapper = new ObjectMapper();
			JsonNode ingRootNode = mapper.readTree(ingressJson);
	        ArrayNode ingressNode = (ArrayNode) ingRootNode.get("items");
	        Iterator<JsonNode> ingressIterator = ingressNode.getElements();
	        while (ingressIterator.hasNext()) {
	        	JsonNode ingressInNode = ingressIterator.next();
	        	JsonNode metadataNode = ingressInNode.get("metadata");
	        	JsonNode nameNode = metadataNode.get("name");
	        	String ingressName = nameNode.getValueAsText();
	        	JsonNode namespaceNode = metadataNode.get("namespace");
	        	String namespace = namespaceNode.getValueAsText();
	        	if (!namespace.contains("-")) {
					logger.info("Excluding Ingress - " + ingressName + " in the namespace - " + namespace);
					continue;
				}
	        	
	        	String appName = namespace.substring(0, namespace.indexOf("-"));
	        	String serviceName = null;	            
	            JsonNode specNode = ingressInNode.get("spec");
	            if (specNode == null) {
					logger.info("The specNode is null for the ingress - " + ingressName + " in the namespace - "+ namespace);
					continue;
				}
	            ArrayNode ruleArrayNode = (ArrayNode)specNode.get("rules");
	            Iterator<JsonNode> ruleNodeItr = ruleArrayNode.getElements();
	            while (ruleNodeItr.hasNext()) {
	                JsonNode ruleNode = ruleNodeItr.next();
	                JsonNode httpNode = ruleNode.get("http");
	                if(httpNode == null){
	                	logger.info("The httpNode is null for the ingress - " + ingressName + " in the namespace - "+ namespace);
	                	continue;
	                }
	                ArrayNode pathArrayNode = (ArrayNode)httpNode.get("paths");
	                Iterator<JsonNode> pathNodeItr = pathArrayNode.getElements();
	                while (pathNodeItr.hasNext()) {
		                JsonNode pathNode = pathNodeItr.next();
		                JsonNode backendNode = pathNode.get("backend");
		                JsonNode serviceNode = backendNode.get("serviceName");
		                if (serviceNode == null) {
							logger.info("The serviceNode is null for the ingress - " + ingressName + " in the namespace - "+ namespace);
							continue;
						}
		                serviceName = serviceNode.getTextValue();
		                if(serviceName != null){
		                	break;
		                }
	                } 
	                if(serviceName != null){
	                	break;
	                }
	            }    
	            String appServiceName = appServiceMap.get(serviceName);
	            if(appServiceName == null){
	            	logger.info("The serviceName in ingress - " + ingressName + " in the namespace - "+ namespace +" is not available in appServiceMap");
					continue;
	            }
	            int compId = mapCompId.get(appName+"/"+appServiceName) == null ? 0 : mapCompId.get(appName+"/"+appServiceName);
	            if(compId == 0){
	            	continue;
	            }
                String lookupPath = namespace +"/"+ingressName;
	            String promLookupName = appServiceName+ "/" +lookupPath;
	            if(mapPromLookup.isEmpty() || mapPromLookup == null){
	            	   DBQueryUtil.loadPromLookup(eVo.getEnvironmentId(), compId, lookupPath);
	               }else{
	            	   if(mapPromLookup.containsKey(promLookupName)){
	            		   mapPromLookup.remove(promLookupName);
		               }else{
		            	   DBQueryUtil.loadPromLookup(eVo.getEnvironmentId(), compId, lookupPath);
		               }
	               }
	        }
	        for (Entry<String, String> promLookupInfo : mapPromLookup.entrySet()) {
	        	String appName = promLookupInfo.getKey().substring(0, promLookupInfo.getKey().indexOf("-"));
	        	String serviceName = promLookupInfo.getKey().substring(0, promLookupInfo.getKey().indexOf("/"+promLookupInfo.getValue()));
	        	//promLookupInfo.getKey().
	        	int compId = mapCompId.get(appName+"/"+serviceName) == null ? 0 : mapCompId.get(appName+"/"+serviceName);
	        	if(compId == 0){
	        		continue;
	        	}
	        	DBQueryUtil.deletePromLookup(eVo.getEnvironmentId(), compId, promLookupInfo.getValue());
	        }
		} catch (Exception ex) {
			logger.error("Surveiller task DOK8TPSLOOKUPLOAD - FAILED : Enviroment -"+eVo.getEnvironmentName(),ex);
			CommonsUtil.handleException(ex, "DOK8TPSLOOKUPLOAD");
		}
		
	}

	private static Map<String, String> getAppService(EnvironmentVO eVo, String authToken) throws Exception {
		// TODO Auto-generated method stub
		
	    /*Method to get All the deployment and the match label associated to each deployment.
	     * The method will return a Map with namespace as key and value List<ServiceLabelVO>
	     * */
		Map<String, List<ServiceLabelVO>> depLabelMap = getDepServiceLabel(eVo, authToken);
		/*Service Lookup Code*/
		
		String serviceApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_SERVICE_PATH);
		String serviceJson = RestCommunicationHandler.getResponse(serviceApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNodeServ = mapper.readTree(serviceJson);
		ArrayNode serviceNode = (ArrayNode) rootNodeServ.get("items");
		if (serviceNode.size() == 0) {
			String errorString = "Surveiller task DOK8STPSLOOKUPLOADTASK - FAILED : Enviroment -"
					+ eVo.getEnvironmentName() + " Service JSON Cannot be empty";
			logger.error(errorString);
			throw new GeneralException(errorString);
		}
		Map<String, String> appServiceMap = new HashMap<String, String>(); 
		Iterator<JsonNode> serviceIterator = serviceNode.getElements();
		while (serviceIterator.hasNext()) {
			JsonNode appsInNode = serviceIterator.next();
			JsonNode servMetadataNode = appsInNode.get("metadata");
			JsonNode servNameNode = servMetadataNode.get("name");
			String serviceName = servNameNode.getValueAsText();
			JsonNode namespaceNode = servMetadataNode.get("namespace");
			String namespace = namespaceNode.getValueAsText();
			JsonNode specNode = appsInNode.get("spec");
			JsonNode selectorNode = specNode.get("selector");
			if (namespace.equals("default") || !namespace.contains("-")) {
				logger.info("Excluding service - " + serviceName + " in the namespace - " + namespace);
				continue;
			}
			Map<String, String> selectorMap = mapper.convertValue(selectorNode, Map.class);
			List<ServiceLabelVO> servLblList = depLabelMap.get(namespace);
			if(servLblList == null){
				continue;
			}
			for(ServiceLabelVO servLabelVo : servLblList){
				if(servLabelVo.getLabel() == null){
					continue;
				}
				if(servLabelVo.getLabel().equals(selectorMap)){
					appServiceMap.put(serviceName, servLabelVo.getServiceName());
				}
			}
		}	
		return appServiceMap;
	}

	/**Method to get All the deployment and the match label associated to each deployment.
	 * @param eVo
	 * @param authToken
	 * @return
	 * @throws Exception
	 */
	private static Map<String, List<ServiceLabelVO>> getDepServiceLabel(EnvironmentVO eVo, String authToken) throws Exception {
		String deployementApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_DEPLOYMENT_PATH);
		String deploymentJson = RestCommunicationHandler.getResponse(deployementApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(deploymentJson);
		ArrayNode deploymentNode = (ArrayNode) rootNode.get("items");
		if (deploymentNode.size() == 0) {
			String errorString = "Surveiller task DOK8STPSLOOKUPLOADTASK - FAILED : Enviroment -"
					+ eVo.getEnvironmentName() + " Deployment JSON Cannot be empty";
			logger.error(errorString);
			throw new GeneralException(errorString);
		}
		Map<String, List<ServiceLabelVO>> depLabelMap = new HashMap<String, List<ServiceLabelVO>>();
		Iterator<JsonNode> deploymentIterator = deploymentNode.getElements();
		while (deploymentIterator.hasNext()) {
			JsonNode appsInNode = deploymentIterator.next();
			JsonNode depMetadataNode = appsInNode.get("metadata");
			JsonNode depNameNode = depMetadataNode.get("name");
			String depName = depNameNode.getValueAsText();
			JsonNode namespaceNode = depMetadataNode.get("namespace");
			String namespace = namespaceNode.getValueAsText();
			JsonNode specNode = appsInNode.get("spec");
			JsonNode selectorNode = specNode.get("selector");
			if (namespace.equals("default") || !namespace.contains("-")) {
				logger.info("Excluding deployment - " + depName + " in the namespace - " + namespace);
				continue;
			}
			JsonNode matchLabelsNode = selectorNode.get("matchLabels");
			if(matchLabelsNode == null) {
				logger.info("The matchLabelsNode is null for the deployment - " + depName + " in the namespace - "
						+ namespace);
				continue;
			}
			Map<String, String> labelMap = mapper.convertValue(matchLabelsNode, Map.class);
			ServiceLabelVO slVo = new ServiceLabelVO();
			slVo.setLabel(labelMap);
			String serviceName = namespace + "/" + depName;
			slVo.setServiceName(serviceName);
			String appName = namespace.substring(0, namespace.indexOf("-"));
			slVo.setAppName(appName);
			List<ServiceLabelVO> servLblList = null;
			if (depLabelMap.containsKey(namespace)) {
				servLblList = depLabelMap.get(namespace);
			} else {
				servLblList = new ArrayList<ServiceLabelVO>();
			}
			servLblList.add(slVo);
			depLabelMap.put(namespace, servLblList);
		}
		return depLabelMap;
	}

}
