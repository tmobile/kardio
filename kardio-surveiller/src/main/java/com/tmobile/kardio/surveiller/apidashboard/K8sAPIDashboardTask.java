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
package com.tmobile.kardio.surveiller.apidashboard;

import java.io.IOException;
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
public class K8sAPIDashboardTask {
	
	private static final Logger logger = Logger.getLogger(K8sAPIDashboardTask.class);
	
	private K8sAPIDashboardTask() {}
	public static void doK8sApiDashboardDataLoad() throws Exception {
		logger.info("************** STARTED doK8sApiDashboardDataLoad **************");
		List<EnvironmentVO> envInfoList = DBQueryUtil.getAllEnvironments();
		for (EnvironmentVO eVo : envInfoList) {
			if (eVo.getK8sUrl() == null || eVo.getK8sCred() == null) {
				logger.info("The Kubernetes URL/Credentials are null for the Environment -" + eVo.getEnvironmentName());
				continue;
			}
			loadKubernetesApiDashboard(eVo);
		}
	}

	/**
	 * Load Kubernetes Api, Pods & Container information for API Dashboard
	 * 
	 * @param eVo
	 */
	private static void loadKubernetesApiDashboard(EnvironmentVO eVo) {
		logger.info("Surveiller task DOK8SAPIDASHBOARDTASK - STARTED : Enviroment -" + eVo.getEnvironmentName());
		try {
			Map<String, Integer> mapApiSts = DBQueryUtil.getK8sCurrentApiDetails(eVo.getEnvironmentId());
			Map<String, Integer> mapContainerSts = DBQueryUtil.getK8sCurrContainerDetails(eVo.getEnvironmentId(),"K8s");
			Map<String, Integer> mapObjPods = DBQueryUtil.getK8sObjPodsContDetails(eVo.getEnvironmentId());
			Map<String, Integer> mapCurrApiSts = DBQueryUtil.getNumOfActiveApisOfApp(eVo.getEnvironmentId(), "K8s");
			//Map<String, Integer> mapPodsSts = DBQueryUtil.getCurrentPodsDetails(eVo.getEnvironmentId());
			//Map<String, Integer> mapCompId = DBQueryUtil.getAllComponentIdForEnv(eVo.getEnvironmentId(),"K8s");
			for (String comp : mapCurrApiSts.keySet()) {
				if (mapApiSts.containsKey(comp)) {
					DBQueryUtil.updateApiDetails(eVo.getEnvironmentId(), comp, mapCurrApiSts.get(comp), "K8s");
				} else {
					DBQueryUtil.loadK8sApiStatus(eVo.getEnvironmentId(), comp, mapCurrApiSts.get(comp));
				}
			}
			String authToken = CommonsUtil.getK8sAuthToken(eVo.getK8sCred());
			if (authToken == null || authToken.equals("")) {
				String errorString = "Got Invalid token for the environment - "
						+ eVo.getEnvironmentName();
				logger.error(errorString);
				throw new GeneralException(errorString);
			}
			Map<String, List<ServiceLabelVO>> depLabelMap = new HashMap<String, List<ServiceLabelVO>>();
			Map<String, ArrayList<Integer>> externalPodsMap = new HashMap<String, ArrayList<Integer>>();
			/* Method to get All the Deployment and its MatchLabels*/
			getDeploymentAndMatchLabels(authToken, eVo, depLabelMap);
			/*Method to get the Pods and Containers associated to each deployment*/
			getPodsAndContainersOfDeployments(authToken, eVo, depLabelMap, externalPodsMap);
		
			for (Entry<String, ArrayList<Integer>> depMap : externalPodsMap.entrySet()) {
				if(mapObjPods.containsKey(depMap.getKey())){
					DBQueryUtil.updateObjPodss(eVo.getEnvironmentId(), depMap.getKey(), depMap.getValue());
				}else{
					DBQueryUtil.loadK8sObjectPods(eVo.getEnvironmentId(), depMap.getKey(), depMap.getValue());
				}
			}
			
			for (Entry<String, List<ServiceLabelVO>> depMap : depLabelMap.entrySet()) {

				for (ServiceLabelVO serviceLabelVO : depMap.getValue()) {
					String componentName = serviceLabelVO.getAppName() + "/" + serviceLabelVO.getServiceName();
					if (mapContainerSts.containsKey(componentName)) {
						if(serviceLabelVO.getNumOfContainers() == mapContainerSts.get(componentName)){
							continue;
						}
						DBQueryUtil.updatePodsAndContainers(eVo.getEnvironmentId(), serviceLabelVO.getServiceName(),
								serviceLabelVO.getNumOfPods(), serviceLabelVO.getAppName(),serviceLabelVO.getNumOfContainers());
					} else {
						DBQueryUtil.loadPodsAndContainers(eVo.getEnvironmentId(), serviceLabelVO.getServiceName(),
								serviceLabelVO.getAppName(), serviceLabelVO.getNumOfPods(), serviceLabelVO.getNumOfContainers());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Surveiller task DOK8SAPIDASHBOARDTASK - FAILED : Enviroment -" + eVo.getEnvironmentName(),
					ex);
			CommonsUtil.handleException(ex, "DOK8SAPIDASHBOARDTASK");
		}
		logger.info("Surveiller task DOK8SAPIDASHBOARDTASK - COMPLETED : Enviroment -" + eVo.getEnvironmentName());
	}
	/**
	 * Method to get all Pods and Containers associated to the deployment.
	 * 
	 * @param authToken
	 * @param eVo
	 * @param depLabelMap
	 * @param externalPodsMap
	 *
	 */
	private static void getPodsAndContainersOfDeployments(String authToken, EnvironmentVO eVo,
			Map<String, List<ServiceLabelVO>> depLabelMap, Map<String, ArrayList<Integer>> externalPodsMap) throws IOException {
		String podApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_PODS_PATH);
		/* Call - the Kube Pod api to get all the Pods in the Cluster */
		String podJson = RestCommunicationHandler.getResponse(podApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode podRootNode = mapper.readTree(podJson);
		ArrayNode appsNode = (ArrayNode) podRootNode.get("items");
		Iterator<JsonNode> podsIterator = appsNode.getElements();
		while (podsIterator.hasNext()) {
			JsonNode appsInNode = podsIterator.next();
			JsonNode metadataNode = appsInNode.get("metadata");
			JsonNode nameNode = metadataNode.get("name");
			String podName = nameNode.getValueAsText();
			JsonNode namespaceNode = metadataNode.get("namespace");
			String namespace = namespaceNode.getValueAsText();
			if (namespace.equals("default") || !namespace.contains("-")) {
				logger.info("Excluding Pods - " + podName + " in the namespace - " + namespace);
				continue;
			}
			JsonNode specNode = appsInNode.get("spec");
			ArrayNode contArrayNode = (ArrayNode) specNode.get("containers");
			/* Number of containers in Pod */
			int numCount = contArrayNode.size();
			//JsonNode ownerReferencesNode = 
			ArrayNode ownerReferencesArray = (ArrayNode) metadataNode.get("ownerReferences");
			if(ownerReferencesArray == null){
				loadPodsAndContainer("Kube-Systems",namespace, externalPodsMap, numCount);
				continue;
			}
			JsonNode ownerReferencesNode = ownerReferencesArray.get(0);
			JsonNode kindNode = ownerReferencesNode.get("kind");
			String kind = kindNode.getTextValue();
			if(!kind.equalsIgnoreCase("ReplicaSet")){
				loadPodsAndContainer(kind,namespace , externalPodsMap, numCount);
				continue;
			}
			
			JsonNode labelsNode = metadataNode.get("labels");
			if (labelsNode == null) {
				logger.info("The labelsNode is null for the pod - " + podName + " in the namespace - " + namespace);
				continue;
			}
			Map<String, String> podLabelMap = mapper.convertValue(labelsNode, Map.class);
			List<ServiceLabelVO> servLblList = depLabelMap.get(namespace);
			if(servLblList == null){
				continue;
			}
			serviceLabelLoop: for (ServiceLabelVO servLabelVo : servLblList) {
				int labelCount = 0;
				for (Entry<String, String> labelInfo : servLabelVo.getLabel().entrySet()) {
					if (podLabelMap.containsKey(labelInfo.getKey())) {
						if (podLabelMap.get(labelInfo.getKey()).equals(labelInfo.getValue())) {
							labelCount = labelCount + 1;
						} else {
							continue serviceLabelLoop;
						}
					} else {
						continue serviceLabelLoop;
					}
				}
				if (servLabelVo.getLabel().size() == labelCount) {
					
					servLabelVo.setNumOfContainers(servLabelVo.getNumOfContainers() + numCount);
					servLabelVo.setNumOfPods(servLabelVo.getNumOfPods() + 1);
					break;
				}
			}
		}
		
	}
	/**
	 * Method to get all Deployment and MatchLabels of the given cluster
	 * 
	 * @param authToken
	 * @param eVo
	 * @param depLabelMap
	 *
	 */
	private static void getDeploymentAndMatchLabels(String authToken, EnvironmentVO eVo, Map<String, List<ServiceLabelVO>> depLabelMap) throws IOException {

		String deployementApiUrl = eVo.getK8sUrl() + PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_DEPLOYMENT_PATH);
		String deploymentJson = RestCommunicationHandler.getResponse(deployementApiUrl, true, SurveillerConstants.BEARER_TOKEN, authToken);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(deploymentJson);
		ArrayNode deploymentNode = (ArrayNode) rootNode.get("items");
		if (deploymentNode.size() == 0) {
			String errorString = "Surveiller task DOK8SAPIDASHBOARDTASK - FAILED : Enviroment -"
					+ eVo.getEnvironmentName() + " Deployment JSON Cannot be empty";
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
			JsonNode selectorNode = specNode.get("selector");
			if (namespace.equals("default") || !namespace.contains("-")) {
				logger.info("Excluding deployment - " + depName + " in the namespace - " + namespace);
				continue;
			}
			JsonNode matchLabelsNode = selectorNode.get("matchLabels");
			if (matchLabelsNode == null) {
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
	}
	private static void loadPodsAndContainer(String objName, String namespace, Map<String, ArrayList<Integer>> externalPodsMap,
			int numCount) {
		// TODO Auto-generated method stub
		String remK8sObj = namespace+"/"+objName;
		ArrayList<Integer> podContList = null;
		if(externalPodsMap.get(remK8sObj) == null){
			podContList = new ArrayList<Integer>();
			podContList.add(1);
			podContList.add(numCount);
			externalPodsMap.put(remK8sObj, podContList);
		}else{
			podContList = externalPodsMap.get(remK8sObj);
			podContList.set(0, podContList.get(0)+1);
			podContList.set(1, podContList.get(1)+numCount);
			externalPodsMap.put(remK8sObj,podContList);
		}
	}
}
