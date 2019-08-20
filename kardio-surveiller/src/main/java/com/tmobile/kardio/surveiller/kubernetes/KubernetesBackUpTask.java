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
/**
 * 
 */
package com.tmobile.kardio.surveiller.kubernetes;

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
import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.HealthCheckType;
import com.tmobile.kardio.surveiller.enums.Region;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.exception.GeneralException;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.marathon.MarathonConfigProcessor;
import com.tmobile.kardio.surveiller.util.CommonsUtil;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.PropertyUtil;
import com.tmobile.kardio.surveiller.vo.ComponentVO;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * @author U29842
 *
 */
public class KubernetesBackUpTask {

/*	private static final String K8S_PROPERTY_ANNOTATIONS = "annotations";
	private static final String K8S_PROPERTY_MATCH_LABELS = "matchLabels";
	private static final String K8S_PROPERTY_SELECTOR = "selector";
	private static final String K8S_PROPERTY_SPEC = "spec";
	private static final String K8S_PROPERTY_RELEASE = "release";
	private static final String K8S_PROPERTY_LABELS = "labels";
	private static final String K8S_PROPERTY_NAME = "name";
	private static final String K8S_PROPERTY_NAMESPACE = "namespace";
	private static final String K8S_PROPERTY_METADATA = "metadata";
	public static final String K8S_PROPERTY_ITEMS = "items";
	*/
	private static final Logger logger = Logger.getLogger(KubernetesBackUpTask.class);
	
	private KubernetesBackUpTask() {}
	/**
	 * Does activities for the task for Back Up Kubernetes.
	 * 
	 * @throws SQLException
	 */
	public static void doKubernetesBackUp() throws SQLException {
		logger.info("************** STARTED KubernetesBackUp **************");
		List<EnvironmentVO> environmentVOs = DBQueryUtil.getAllEnvironments();
		for(EnvironmentVO eVo : environmentVOs){
			logger.info("STARTED KubernetesBackUp : "+eVo.getEnvironmentName());
		    if(eVo.getK8sUrl() == null){
		    	continue;
		    }
		    loadKubernetesHealthcheck(eVo);
		    logger.info("COMPLETED KubernetesBackUp : "+eVo.getEnvironmentName());
		}
		
	}


	/**
	 * @param eVo
	 */
	private static void loadKubernetesHealthcheck(EnvironmentVO eVo) {
		 logger.info("Surveiller task DOKUBERNETESBACKUP - STARTED : Enviroment -"+eVo.getEnvironmentName());
		 try {
			List<ComponentVO> oldAPIComponentVOs = DBQueryUtil.getAllAPIComponentDetails(eVo.getEnvironmentId(), SurveillerConstants.K8S_PLATFORM, Region.WEST_REGION.getRegionId());
			List<ComponentVO> newAPIComponentVOs = new ArrayList<ComponentVO>();
			List<HealthCheckVO> statusChangedList = new ArrayList<HealthCheckVO>();
			String authToken = CommonsUtil.getK8sAuthToken(eVo.getK8sCred());
       		if(authToken == null || authToken.equals("")){
       			logger.error("Got Invalid token for the environment -"+eVo.getEnvironmentName());
				return;
			}
			// Call Kube API to get all the deployments
			String deploymentApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_DEPLOYMENT_PATH);
			String deploymentJson = RestCommunicationHandler.getResponse(deploymentApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);	
			/*Method to get all the services and their labelSelectors*/
			Map<String, Map<String, String>> servLabelMap = getServiceLabelValues(authToken, eVo);
		    /*Method to get all the Ingress*/
			Map<String, JsonNode> ingressMap = getIngressLookupMap(authToken, eVo);
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(deploymentJson);
	        ArrayNode deploymentNode = (ArrayNode) rootNode.get("items");
	        if(deploymentNode.size()==0){
            	String errorString = "Surveiller task DOKUBERNETESBACKUP - FAILED : Enviroment -"+eVo.getEnvironmentName()+ " Deployment JSON Cannot be empty";
	        	logger.error(errorString);
	        	throw new GeneralException(errorString);
            }
	        Iterator<JsonNode> deploymentIterator = deploymentNode.getElements();
	        while (deploymentIterator.hasNext()) {
	            JsonNode appsInNode = deploymentIterator.next();
	            JsonNode depMetadataNode = appsInNode.get("metadata");
	            JsonNode depNameNode = depMetadataNode.get("name");
	            String depName = depNameNode.getValueAsText();
	            JsonNode namespaceNode = depMetadataNode.get("namespace");
	            String namespace = namespaceNode.getValueAsText();
	            JsonNode specNode = appsInNode.get("spec");
	            if(namespace.equals("default") || !namespace.contains("-")){
	            	logger.info("Excluding deployment - "+depName+" in the namespace - "+ namespace);
	            	continue;
	            }
	            /*If Service or Ingress Map is null then check the Replica count*/         
	            if(servLabelMap == null || servLabelMap.isEmpty()){
	            	logger.info("No Service is available for the environment : "+eVo.getEnvironmentName());
	            	doCheckDeploymentReplicas(appsInNode, eVo.getEnvironmentId(), statusChangedList, newAPIComponentVOs);
	            	continue;
		        }
	            if(ingressMap == null || ingressMap.isEmpty()){
	            	logger.info("No Ingress is available for the environment : "+eVo.getEnvironmentName());
	            	doCheckDeploymentReplicas(appsInNode, eVo.getEnvironmentId(), statusChangedList, newAPIComponentVOs);
	            	continue;
		        }
	            JsonNode templateNode = specNode.get("template");
	            JsonNode specJsonNode = templateNode.get("spec");
	            ArrayNode contArrayNode = (ArrayNode) specJsonNode.get("containers");
	            String healthCheckPort = null;
	            boolean doCheckReplicas = false;
	            String pathValue = null;
	            String healthCheckUrl = null;
	            Iterator<JsonNode> contNodeItr = contArrayNode.getElements();
	            while (contNodeItr.hasNext()) {
	                JsonNode containerNode = contNodeItr.next();
	                JsonNode probeNode = containerNode.get("livenessProbe");
	                if(probeNode == null){
	                	logger.info("The liveness probe is null for the deployment - "+depName+" in the namespace - "+ namespace);
	                	doCheckReplicas = true;
	                }else{
		                JsonNode httpNode = probeNode.get("httpGet");
		                if(httpNode == null){
		                	logger.info("The httpNode is null for the deployment - "+depName+" in the namespace - "+ namespace);
		                	doCheckReplicas = true;
		                }else{
			                JsonNode pathNode = httpNode.get("path");
			                if(pathNode == null){
			                	logger.info("The path/scheme in liveness probe is null for the deployment - "+depName+" in the namespace - "+ namespace);
			                	doCheckReplicas = true;
			                	break;
			                }
			                   	JsonNode portNode = httpNode.get("port");
			                	healthCheckPort = portNode.asText();
			                    pathValue = pathNode.getValueAsText();
			            }   
	                }
	                break;
	            }   
	            if(doCheckReplicas){
	            	doCheckDeploymentReplicas(appsInNode, eVo.getEnvironmentId(), statusChangedList, newAPIComponentVOs);
	            	continue;
	            }
	           	JsonNode selectorNode = specNode.get("selector");
	            JsonNode matchLabelsNode = selectorNode.get("matchLabels");
	            if(matchLabelsNode == null) {
					logger.info("The matchLabelsNode is null for the deployment - " + depName + " in the namespace - "
							+ namespace);
					doCheckDeploymentReplicas(appsInNode, eVo.getEnvironmentId(), statusChangedList, newAPIComponentVOs);
					continue;
				}
	            /*For Service lookup for a Map from the matchLabels Json node*/          
	            Map<String, String> matchLabelMap = mapper.convertValue(matchLabelsNode, Map.class);
	            String serviceName = null;
	            /*Find the service name by comparing the matchLabels in deployment and Label Selectors in service*/
	            for (Entry<String, Map<String, String>> servLabel : servLabelMap.entrySet()) {
	            	String serviceNamespace = servLabel.getKey().substring(0, servLabel.getKey().indexOf("/"));
	            	if(serviceNamespace.equals(namespace) && matchLabelMap.equals(servLabel.getValue())){
	            		serviceName = servLabel.getKey();
	            	}
	            }
	            if(!ingressMap.containsKey(serviceName)){
	            	logger.info("Ingress is ot found for the - "+serviceName+" in the namespace - "+ namespace);
	            	doCheckDeploymentReplicas(appsInNode, eVo.getEnvironmentId(), statusChangedList, newAPIComponentVOs);
	            	continue;
	            }
	            /*Get the Ingress root Node from the Map*/
	            JsonNode ingressNode = ingressMap.get(serviceName);
	            healthCheckUrl = getHealthCheckUrl(ingressNode, healthCheckPort, pathValue);
	            String appName = namespace.substring(0, namespace.indexOf("-"));
	            int apiId = 0;
	            if(healthCheckUrl == null || healthCheckUrl.trim().equals("")){
	            	logger.info("The HealthCheck Url is null for the deployment - " + depName + " in the namespace - "
							+ namespace);
                	doCheckDeploymentReplicas(appsInNode, eVo.getEnvironmentId(), statusChangedList, newAPIComponentVOs);
	            	continue;
                }else{
                	String appServiceName = namespace+"/"+depName;
                	apiId = DBQueryUtil.checkAndInsertComponent(appName, appServiceName, ComponentType.APP, healthCheckUrl, eVo.getEnvironmentId(),
	                           Region.WEST_REGION.getRegionId(), HealthCheckType.URL_200_CHECK, SurveillerConstants.K8S_PLATFORM);
                }
	            
	            if(apiId != 0){
	                newAPIComponentVOs.add(DBQueryUtil.getComponent(apiId));
	            }
	        } 
	       MarathonConfigProcessor.compareAPILists(oldAPIComponentVOs, newAPIComponentVOs, eVo.getEnvironmentId(),Region.WEST_REGION.getRegionId());
	       if(statusChangedList.size() > 0){
	        	CommonsUtil.sendMailForChangedStatus(statusChangedList);
	        }
		} catch (Exception ex) {
			logger.error("Surveiller task DOKUBERNETESBACKUP - FAILED : Enviroment -"+eVo.getEnvironmentName(),ex);
			CommonsUtil.handleException(ex, "DOKUBERNETESBACKUP");
		}
		logger.info("Surveiller task DOKUBERNETESBACKUP - COMPLETED : Enviroment -"+eVo.getEnvironmentName());
	}

	/**
	 * This function returns the Health check url for the service
	 * @param ingressNode
	 * @param healthCheckPort
	 * @param pathValue
	 * @return
	 */
	private static String getHealthCheckUrl(JsonNode ingressNode, String healthCheckPort, String pathValue) {
		// TODO Auto-generated method stub
		 JsonNode metadataNode = ingressNode.get("metadata");
	     JsonNode annotNode = metadataNode.get("annotations");
	     JsonNode namespaceNode = metadataNode.get("namespace");
	     JsonNode nameNode = metadataNode.get("name");
	     String ingressName = nameNode.asText();
	     String namespace = namespaceNode.asText();
	     /*The below block of code is for checking path based routing
	      *CASE:: We are using nginx controller,below is the logic to find the app is using path based routing or not
	      * If the "nginx.ingress.kubernetes.io/rewrite-target" is present in the annotation then app has path based routing */
	     boolean isPathNeeded = false;
         if(annotNode == null){
         	logger.info("The annotations node is null for the Ingress - "+ingressName+" in the namespace - "+ namespace);
         	isPathNeeded = false;
         }else{
         	isPathNeeded = annotNode.get("nginx.ingress.kubernetes.io/rewrite-target") == null? false : true;
         }
         
         JsonNode specIngNode = ingressNode.get("spec");
     	 JsonNode tlsNode = specIngNode.get("tls");
     	 String schemeValue = tlsNode == null ? "http" : "https";
     	 String hostUrlPath = null;
         hostUrlPath =  getHostUrlAndPath(specIngNode, healthCheckPort, isPathNeeded);
         if(hostUrlPath == null){
        	 return null;
         }
		return schemeValue+ "://" + hostUrlPath + pathValue;
	}

	/**
	 * This function returns the host url.
	 * If the app is using path based routing, then path is appended to end of the host url 
	 * @param specIngNode
	 * @param healthCheckPort
	 * @param isPathNeeded
	 * @return
	 */
	private static String getHostUrlAndPath(JsonNode specIngNode, String healthCheckPort, boolean isPathNeeded) {
		// TODO Auto-generated method stub
		ArrayNode rulesArrayNode = (ArrayNode)specIngNode.get("rules");
    	Iterator<JsonNode> rulesNodeItr = rulesArrayNode.getElements();
    	while(rulesNodeItr.hasNext()){
    		JsonNode rulesNode = rulesNodeItr.next();
    		JsonNode hostNode = rulesNode.get("host");
    		String host = hostNode.asText();
    		if(!isPathNeeded){
    			return host;
    		}
    		JsonNode httpRuleNode = rulesNode.get("http");
    		ArrayNode pathsArrayNode = (ArrayNode)httpRuleNode.get("paths");
    		Iterator<JsonNode> pathsNodeItr = pathsArrayNode.getElements();
    		while(pathsNodeItr.hasNext()){
    			JsonNode pathsRootNode = pathsNodeItr.next();
    			JsonNode backendNode = pathsRootNode.get("backend");
    			JsonNode servicePortNode = backendNode.get("servicePort");
    			if(servicePortNode.asText().equals(healthCheckPort)){
    				JsonNode pathNode = pathsRootNode.get("path");
    				return host+pathNode.getTextValue();
    			}
    		}
    	}
    	return null;
	}

	/**
	 * The function returns all Ingress as a Map, with key as service name & Ingress root node
	 * @param ingressJson
	 * @param eVo
	 * @return
	 * @throws Exception 
	 */
	private static Map<String, JsonNode> getIngressLookupMap(String authToken, EnvironmentVO eVo) throws Exception {
		// TODO Auto-generated method stub
		String ingressApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_INGRESS_PATH);
		String ingressJson = RestCommunicationHandler.getResponse(ingressApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode ingRootNode = mapper.readTree(ingressJson);
        ArrayNode ingressArrayNode = (ArrayNode) ingRootNode.get("items");
        if(ingressArrayNode == null || ingressArrayNode.size() == 0){
        	logger.info("No Ingress is available for the environment : "+eVo.getEnvironmentName());
			return null;
        }
        Iterator<JsonNode> ingressIterator = ingressArrayNode.getElements();
        Map<String, JsonNode> ingressMap = new HashMap<String, JsonNode>();
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
        	/*This particular block of code is to avoid code executions for the applications with multiple ingress
        	 * FIXME: Remove if this logic is not necessary
        	 * CASE: Services with multiple Ingress 
        	 * In our case the annotation "kubernetes.io/ingress.class" contains values "nginx-internal" or "nginx-external"
        	 * */
        	JsonNode annotNode = metadataNode.get("annotations");
        	if(annotNode == null){
        		logger.info("The annotations node is null for the Ingress - "+ingressName+" in the namespace - "+ namespace);
        	}else{
		        JsonNode ingClasstNode = annotNode.get("kubernetes.io/ingress.class");
	            if(ingClasstNode == null || !ingClasstNode.getTextValue().equals("nginx-internal")){
	            	logger.info("The hostname node is "+ingClasstNode.getTextValue()+ "for the Ingress - "+ingressName+" in the namespace - "+ namespace);
	            	continue;
	            }
        	}   
            /******/
        	JsonNode specNode = ingressInNode.get("spec");
            if (specNode == null) {
				logger.info("The specNode is null for the ingress - " + ingressName + " in the namespace - "+ namespace);
				continue;
			}
            String serviceName = null;
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
	                ingressMap.put(namespace+"/"+serviceName, ingressInNode);
                }    
           }	
        }
		return ingressMap;
	}

	/**
	 * This function returns a Map with key as service name and value as Map with all selector values
	 * @param authToken
	 * @param eVo
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Map<String, String>> getServiceLabelValues(String authToken, EnvironmentVO eVo) throws Exception {
		// TODO Auto-generated method stub
		String serviceApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_SERVICE_PATH);
		String serviceJson = RestCommunicationHandler.getResponse(serviceApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNodeServ = mapper.readTree(serviceJson);
		ArrayNode serviceNode = (ArrayNode) rootNodeServ.get("items");
		if(serviceNode == null || serviceNode.size() == 0){
			logger.info("No Service is available for the environment : "+eVo.getEnvironmentName());
			return null;
		}
		Map<String, Map<String, String>> serviceLabelMap = new HashMap<String, Map<String, String>>(); 
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
			serviceLabelMap.put(namespace+"/"+serviceName, selectorMap);
		}
		return serviceLabelMap;
	}

	/**This function will get invoked if there is no Health check enabled for the service.
	 * This function do the health check of the service by comparing the replica counts.
	 * @param appsInNode
	 * @param environmentId
	 * @param statusChangedList
	 * @param newAPIComponentVOs
	 */
	private static void doCheckDeploymentReplicas(JsonNode appsInNode, int environmentId, List<HealthCheckVO> statusChangedList, List<ComponentVO> newAPIComponentVOs) {
		JsonNode metaDataNode = appsInNode.get("metadata");
		JsonNode nameNode = metaDataNode.get("name");
		String depName = nameNode.getTextValue();
		JsonNode namespaceNode = metaDataNode.get("namespace");
		String namespace = namespaceNode.getTextValue();
		JsonNode statusNode = appsInNode.get("status");
		Integer numReplicas = statusNode.get("replicas") == null ? null : statusNode.get("replicas").getIntValue();
		Integer numAvlReplicas = statusNode.get("availableReplicas") == null ? null : statusNode.get("availableReplicas").getIntValue();
		Integer numUnAvlReplicas = statusNode.get("unavailableReplicas") == null ? null : statusNode.get("unavailableReplicas").getIntValue();
		int apiId = 0;
		Status status = null;
		String kubeMessage = null;
		if(numReplicas == null){
			status = Status.DOWN;
			kubeMessage = "Kubernetes Status - No replicas Available";
		}else if(numReplicas.intValue() > 0 && numUnAvlReplicas == null){
			status = Status.UP;
		}else if(numReplicas.intValue() > 0 && numAvlReplicas == null){
			status = Status.DOWN;
			kubeMessage = "Kubernetes Status - Number of Replicas = "+numReplicas+ "; Unavailable Replicas = "+numUnAvlReplicas;
		}else if(numReplicas.intValue() != numAvlReplicas.intValue()){
			status = Status.DOWN;
			kubeMessage = "Kubernetes Status - Number of Replicas = "+numReplicas+ "; Available Replicas = "+numAvlReplicas;
		}
		//FIXME :: Needs to fix the scenario where replica, availablereplica & nonavailablereplica having same count
		if(status == null){
			logger.info("The doCheckDeploymentReplicas check - status is null for the deployment - " + depName + " in the namespace - "+ namespace);
			return;
		}
		String serviceName = namespace+"/"+depName;
		String appName= namespace.substring(0, namespace.indexOf("-"));
		apiId = DBQueryUtil.checkAndInsertComponent(appName, serviceName, ComponentType.APP, null, environmentId,
        		Region.WEST_REGION.getRegionId(), HealthCheckType.DUMMY, SurveillerConstants.K8S_PLATFORM);
		boolean isStatusChanged = DBQueryUtil.checkAndUpdateMessage(apiId,environmentId,Region.WEST_REGION.getRegionId(), HealthCheckType.DUMMY,status,kubeMessage);
		if(isStatusChanged){
        	statusChangedList.add(createVoWithComponentDetails(apiId,environmentId,Region.WEST_REGION.getRegionId(),status,kubeMessage));
        }
		logger.info("updateHealthCheckStatus : ApiId -"+apiId+"  environmentId -"+environmentId+"  HeathCheckType -"+HealthCheckType.DUMMY+" status - "+status.getStatusId()+" isStatusChanged -"+isStatusChanged);
		DBQueryUtil.updateMarathonHealthCheckStatus(apiId,environmentId, Region.WEST_REGION.getRegionId(), HealthCheckType.DUMMY,status, isStatusChanged);
		if(apiId != 0){
            newAPIComponentVOs.add(DBQueryUtil.getComponent(apiId));
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
}
