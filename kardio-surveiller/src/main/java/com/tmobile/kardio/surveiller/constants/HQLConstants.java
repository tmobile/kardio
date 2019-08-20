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
package com.tmobile.kardio.surveiller.constants;

public class HQLConstants {
	private HQLConstants() {}
	public static final String UPDATE_HEALTH_CHECK_NO_CHANGE = "UPDATE HealthCheckEntity SET statusUpdateTime = :updateTime, failedCount = :failedCount WHERE healthCheckId = :healthCheckId";
	public static final String UPDATE_HEALTH_CHECK_NO_CHANGE_WITH_STATUS = "UPDATE HealthCheckEntity SET currentStatus = :status, statusUpdateTime = :updateTime, failedCount = :failedCount WHERE healthCheckId = :healthCheckId";
	public static final String UPDATE_HEALTH_CHECK_CHANGE = "UPDATE HealthCheckEntity SET currentStatus = :status, statusUpdateTime = :updateTime, lastStatusChange = :lastStatusChange, failedCount = :failedCount WHERE healthCheckId = :healthCheckId";
	public static final String DB_QUERY_GET_NUMBER_OF_SERVICES = "SELECT count(distinct c.componentId) FROM ComponentEntity c, HealthCheckEntity h WHERE c.parentComponent.componentId IS NOT NULL AND c.componentId = h.component.componentId AND h.delInd = 0 AND c.delInd = 0 AND h.region.regionId = 1 AND c.parentComponent.manual is null AND h.environment.environmentId = :envId AND c.platform = :platform";
	public static final String DB_QUERY_GET_COUNTER_CCP_UPTIME = "SELECT c.parentComponent.componentId, MAX(d.percentageUpTime) FROM ComponentEntity c, HealthCheckEntity h, DaillyCompStatusEntity d WHERE c.componentId = h.component.componentId AND h.healthCheckId = d.healthCheck.healthCheckId AND c.componentType.componentTypeId = 1 AND c.delInd = 0 AND h.delInd = 0 AND h.environment.environmentId = :envId AND h.region.regionId = 1 AND d.statusDate = :stsDate AND c.parentComponent.componentId IN (select c2.componentId from ComponentEntity c2  where c2.delInd = 0 and c2.componentName in (:compNames) ) GROUP BY c.parentComponent.componentId";
	public static final String DB_QUERY_GET_COUNTER_T_TRANSACTION = "select cm.metricVal from CounterMetricEntity cm where cm.envCounter.envCounterId= :envCountId AND cm.metricDate = (select MAX(cm2.metricDate) from CounterMetricEntity cm2 where cm2.envCounter.envCounterId= :envCountId )";
	public static final String DB_QUERY_DELETE_HEALTH_CHECK_PARAMS = "DELETE from HealthCheckParamEntity hp WHERE hp.healthCheck.healthCheckId = :healthCheckId";
	public static final String DB_QUERY_UPDATE_HEALTH_CHECK_TYPE = "UPDATE HealthCheckEntity SET healthCheckType.healthCheckTypeId = :hcTypeId, delInd = 0 WHERE healthCheckId = :healthCheckId ";
	public static final String DB_QUERY_UPDATE_COMPONENT_DELIND = "UPDATE ComponentEntity SET delInd = :delInd  WHERE componentId = :componentId";
	public static final String DB_QUERY_UPDATE_HEALTH_CHECK_DELIND = "UPDATE HealthCheckEntity SET delInd = :delInd WHERE component.componentId = :compId AND environment.environmentId = :enviromentId AND region.regionId = :regionID";
	public static final String DB_QUERY_UPDATE_HEALTH_CHECK_STATUS_CHG = "UPDATE HealthCheckEntity SET currentStatus.statusId = :statusId, statusUpdateTime = :statusUpdateTime, lastStatusChange = :statusUpdateTime, failedCount = 0 WHERE healthCheckType.healthCheckTypeId = :healthCheckTypeId AND environment.environmentId = :environmentId AND component.componentId = :componentId AND region.regionId = :regionId";
	public static final String DB_QUERY_UPDATE_HEALTH_CHECK_STATUS_NO_CHG = "UPDATE HealthCheckEntity SET currentStatus.statusId = :statusId, statusUpdateTime = :statusUpdateTime, failedCount = 0 WHERE healthCheckType.healthCheckTypeId = :healthCheckTypeId AND environment.environmentId = :environmentId AND component.componentId = :componentId AND region.regionId = :regionId";
	public static final String DB_QUERY_UPDATE_MARATHON_JSON = "UPDATE EnvironmentEntity SET marathonJson = :marathonJson, lastUpdateTime = :lastUpdateTime WHERE environmentId = :environmentId";
	public static final String DB_QUERY_ALERT_SUB_WITH_COMP = "FROM AlertSubscriptionEntity ase where componentId is null or (validationLevel = 1 and componentId in (:componentIdList))";
	public static final String DB_QUERY_UPDATE_APP_LAUNCH_DATE = "UPDATE HealthCheckEntity SET createdDate = :createdDate WHERE component.componentId = :compId AND environment.environmentId = :enviromentId";
	public static final String UPDATE_API_DETAILS = "UPDATE ApiStatusEntity SET totalApi = :numOfApi WHERE component.componentId = :compId AND environment.environmentId = :environmentId AND statusDate = :stsDate ";
	public static final String UPDATE_CONTAINER_DETAILS = "UPDATE ContainerStatsEntity SET totalContainer = :numOfContainers WHERE component.componentId = :compId AND environment.environmentId = :environmentId AND statsDate = :stsDate ";
	public static final String UPDATE_PROM_LOOKUP_DETAILS = "UPDATE PromLookupEntity SET httpPath = :httpPath, lastUpdated = :lastUpdateDate WHERE component.componentId = :compId AND environment.environmentId = :environmentId ";

	public static final String UPDATE_TPS_SERVICE_DETAILS = "UPDATE TpsServiceEntity SET tpsValue = :tpsVaule, latencyValue = :latencyValue, lastUpdated = :lastUpdateDate WHERE component.componentId = :compId AND environment.environmentId = :environmentId ";
	public static final String UPDATE_MESOS_TPS_LAT_HISTORY = "UPDATE TpsLatHistoryEntity SET tpsValue = :tpsVaule, latencyValue = :latencyValue WHERE component.componentId = :compId AND environment.environmentId = :environmentId AND statusDate = :statusDate";
	public static final String UPDATE_K8S_TPS_LAT_HISTORY = "UPDATE K8sTpsLatHistoryEntity SET tpsValue = :tpsVaule, latencyValue = :latencyValue WHERE component.componentId = :compId AND environment.environmentId = :environmentId AND statusDate = :statusDate";
	public static final String GET_MESOS_TPS_LAT_HISTORY = "SELECT t.tpsValue, t.latencyValue, c.componentId from  TpsLatHistoryEntity as t join t.component as c where t.environment.environmentId = :envId AND t.statusDate = :statusDate";
	public static final String GET_K8S_TPS_LAT_HISTORY = "SELECT t.tpsValue, t.latencyValue, c.componentId from  K8sTpsLatHistoryEntity as t join t.component as c where t.environment.environmentId = :envId AND t.statusDate =:statusDate";
	
	public static final String QUERY_GET_NUM_APP_APIS = "select c.parentComponent.componentName, count(c.componentId) from HealthCheckEntity as h join h.component as c where h.environment.environmentId= :envId and c.parentComponent.componentId is not null and c.platform= :platform and c.delInd = 0 and h.delInd = 0 group by c.parentComponent.componentName";
	public static final String UPDATE_K8S_API_DETAILS = "UPDATE K8sApiStatusEntity SET totalApi = :numOfApi WHERE component.componentId = :compId AND environment.environmentId = :environmentId AND statusDate = :stsDate ";
	public static final String UPDATE_K8S_PODS_CONT_DETAILS = "UPDATE K8sPodsContainersEntity SET totalPods = :totalPods, totalContainers = :totalContainers WHERE component.componentId = :compId AND environment.environmentId = :environmentId AND statusDate = :stsDate ";
	
	public static final String DB_QUERY_UPDATE_EAST_MARATHON_JSON = "UPDATE EnvironmentEntity SET eastMarathonJson = :marathonJson, eastLastUpdateTime = :lastUpdateTime WHERE environmentId = :environmentId";
	
	public static final String DB_QUERY_DELETE_PROM_LOOKUP = "DELETE from PromLookupEntity pl WHERE pl.component.componentId = :compId AND pl.environment.environmentId = :envId AND pl.httpPath = :lookupVaule";

	public static final String UPDATE_K8S_OBJECT_PODS = "UPDATE K8sObjectPodsEntity SET pods = :pods, containers = :containers WHERE objectName = :objName AND environment.environmentId = :environmentId AND statusDate = :stsDate ";

	public static final String QUERY_GET_K8S_OBJECT_PODS = "select p.objectName, p.pods from K8sObjectPodsEntity as p where p.environment.environmentId = :environmentId AND p.statusDate = :statusDate";

}
