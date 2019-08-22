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
package com.tmobile.kardio.constants;

/**
 * HQL Queries
 */
public class HQLConstants {
	private HQLConstants() {}
	
    public static final String GET_REGION_ID_FROM_NAME = "FROM RegionEntity reg where lower(reg.regionName) = :regName";
    public static final String GET_ENIRONMENT_ID_FROM_NAME = "FROM EnvironmentEntity env where lower(env.environmentName) = :envName";
    public static final String UPDATE_ENVIRONMENT_BASIC = "update EnvironmentEntity env "
            + " SET env.environmentDesc = :environmentDesc, env.marathonUrl = :marathonUrl, env.k8sUrl = :k8sUrl, env.displayOrder = :dispOrdr, env.envLock = :envLock " + "WHERE env.environmentId = :environmentId";
    public static final String UPDATE_ENVIRONMENT_MARATHON_CRED = "update EnvironmentEntity env "
            + " SET env.environmentDesc = :environmentDesc, env.marathonUrl = :marathonUrl, env.k8sUrl = :k8sUrl, env.displayOrder = :dispOrdr, env.envLock = :envLock, "
            + "marathonCred = :marathonCred WHERE env.environmentId = :environmentId";
    public static final String DELETE_APP_SESSION = "DELETE AppSessionEntity WHERE authToken = :authToken";
    public static final String UPDATE_COMPONENT_WITHOUT_PARENT = "UPDATE ComponentEntity SET componentName =  :compName, componentDesc = :compDesc, "
            + " delInd = :delInd WHERE componentId = :compId";
    public static final String UPDATE_COMPONENT_WITH_PARENT = "UPDATE ComponentEntity SET componentName =  :compName, "
            + " componentDesc = :compDesc, platform = :platform, parentComponent = :compParent, delInd = :delInd WHERE componentId = :compId";
    public static final String UPDATE_COMPONENT_DEL_IND = "UPDATE ComponentEntity SET delInd = 1 WHERE componentId = :compId";
    public static final String UPDATE_HEALTH_CHECK_DEL_IND = "UPDATE HealthCheckEntity SET delInd = 1 WHERE healthCheckId = :hcId";
    public static final String UPDATE_HEALTH_CHECK = "UPDATE HealthCheckEntity SET component = :component, region = :region, "
            + " environment = :environment, maxRetryCount = :maxRetryCount, healthCheckType = :hcType, delInd = :delInd WHERE healthCheckId = :hcId";
    public static final String DELETE_HEALTH_CHECK_PARAM = "DELETE FROM HealthCheckParamEntity WHERE healthCheckParamId = :hcParamId";
    public static final String UPDATE_HEALTH_CHECK_PARAM = "UPDATE HealthCheckParamEntity SET healthCheckParamKey = :paramKey, "
            + " healthCheckParamVal = :paramVal WHERE healthCheckParamId = :hcParamId";
    public static final String QUERY_UPDATE_ENV_COUNTERS_DETAILS = "UPDATE EnvCounterEntity SET parameter1 = :param1, "
            + " parameter2 = :param2 WHERE envCounterId = :envCountId";
    public static final String UPDATE_APP_MESSAGE = "UPDATE EnvironmentEntity SET APP_MESSAGE = :message WHERE environmentId = :envId";
    public static final String UPDATE_INFRA_MESSAGE = "UPDATE EnvironmentEntity SET INFRA_MESSAGE = :message WHERE environmentId = :envId";
    public static final String UPDATE_GENERAL_MESSAGE = "UPDATE EnvironmentEntity SET generalMessage = :message WHERE environmentId = :envId";
    public static final String UPDATE_COUNTER_MESSAGE = "UPDATE EnvironmentEntity SET counterMessage = :message WHERE environmentId = :envId";
    public static final String QUERY_UPDATE_COMP_MESSAGE = "UPDATE ComponentMessageEntity SET message = :msg WHERE componentMessageId = :messageId";
    public static final String QUERY_COMP_FAILURE_LOG = "SELECT c.componentName, 0, cf.compRegStsTime, cf.failureMessage, '' "
            + "FROM ComponentEntity c, HealthCheckEntity h, ComponentFailureLogEntity cf "
            + "WHERE h.component.componentId = c.componentId and h.region.regionId = :regId and h.environment.environmentId = :envId "
            + "and h.healthCheckId = cf.healthCheckId and h.delInd = 0  AND c.componentId = :compId " + "and (cf.compRegStsTime >= "
            + "(select max(cf2.compRegStsTime) from ComponentFailureLogEntity cf2, HealthCheckEntity h2 "
            + "where h2.environment.environmentId = h.environment.environmentId "
            + "AND h2.region.regionId = h.region.regionId AND cf2.healthCheckId = h2.healthCheckId and h2.component.componentId = c.componentId ) "
            + "OR cf.compRegStsTime >= :prevoiusDay ) order by cf.compRegStsTime desc";

    public static final String QUERY_COMP_MESSAGE = "SELECT cm.component.componentName, cm.componentMessageId, cm.messageDate, cm.message, cm.userId"
            + " FROM ComponentMessageEntity cm WHERE "
            + " cm.environment.environmentId = :envId AND cm.region.regionId = :regId AND cm.component.componentId  in( :compIds ) "
            + " AND cm.messageDate >= :prevoiusDay  order by cm.messageDate desc";

    public static final String QUERY_COMP_FAILURE_LOG_HIS = "SELECT c.componentName, 0, cf.compRegStsTime, cf.failureMessage, '' "
            + "FROM ComponentEntity c, HealthCheckEntity h, ComponentFailureLogEntity cf "
            + "WHERE h.component.componentId = c.componentId and h.region.regionId = :regId and h.environment.environmentId = :envId "
            + "and h.healthCheckId = cf.healthCheckId and h.delInd = 0  AND c.componentId = :compId "
            + "and cf.compRegStsTime <= :hisDate  order by cf.compRegStsTime desc";

    public static final String QUERY_COMP_MESSAGE_HIS = "SELECT cm.component.componentName, cm.componentMessageId, cm.messageDate, cm.message, "
            + " cm.userId " + " FROM ComponentMessageEntity cm WHERE "
            + " cm.environment.environmentId = :envId AND cm.region.regionId = :regId AND cm.component.componentId  in( :compIds ) "
            + " AND year(cm.messageDate) = year(:hisDate) and month(cm.messageDate) = month(:hisDate) and day(cm.messageDate) = day(:hisDate) "
            + " order by cm.messageDate desc";

    public static final String QUERY_GET_CHILDREN_WITH_ERROR = "SELECT c FROM ComponentEntity c, HealthCheckEntity h "
            + " WHERE h.component.componentId = c.componentId and h.delInd = 0 "
            + " and h.region.regionId = :regId and h.environment.environmentId = :envId and c.delInd = 0 "
            + " AND (h.currentStatus.statusId != 1 or h.lastStatusChange > :statusChangeTime)"
            + " AND c.parentComponent.componentId = :parentCompId";

    public static final String QUERY_UPDATE_COUNTERS_DETAILS = "UPDATE CounterEntity "
            + " SET counterName = :counterName, position = :position, delInd= :delInd WHERE counterId = :counterId";

    public static final String QUERY_GET_INFRA_AVAILABILITY_PERCENTAGE = " select c.parentComponent.componentId , max(d.percentageUpTime) "
            + " from DaillyCompStatusEntity as d join d.healthCheck as h"
            + " join h.component as c where c.componentType.componentTypeId = 1 and h.environment.environmentId = :envId"
            + " and h.region.regionName = :regionName and d.statusDate >= :startDate and d.statusDate <= :endDate "
            + " and c.parentComponent.componentId is not null group by d.statusDate,c.parentComponent ";
    public static final String QUERY_GET_INFRA_AVAILABILITY_PERCENTAGE_PLATFORM = " select c.parentComponent.componentId , max(d.percentageUpTime) "
            + " from DaillyCompStatusEntity as d join d.healthCheck as h"
            + " join h.component as c where c.componentType.componentTypeId = 1 and h.environment.environmentId = :envId"
            + " and h.region.regionName = :regionName and d.statusDate >= :startDate and d.statusDate <= :endDate and c.platform = :platform"
            + " and c.parentComponent.componentId is not null group by d.statusDate,c.parentComponent ";
    public static final String QUERY_GET_API_AVAILABILITY_PERCENTAGE = " select c.componentId,avg(dc.percentageUpTime) "
            + " from DaillyCompStatusEntity as dc join dc.healthCheck as h "
            + " join h.component as c where c.componentType.componentTypeId = 2 and h.environment.environmentId = :envId"
            + " and h.region.regionName = :regionName and dc.statusDate >= :startDate and dc.statusDate <= :endDate "
            + " group by c.componentId, c.componentName ";
    public static final String QUERY_GET_API_AVAILABILITY_PERCENTAGE_PLATFORM = " select c.componentId,avg(dc.percentageUpTime) "
            + " from DaillyCompStatusEntity as dc join dc.healthCheck as h "
            + " join h.component as c where c.componentType.componentTypeId = 2 and h.environment.environmentId = :envId"
            + " and h.region.regionName = :regionName and dc.statusDate >= :startDate and dc.statusDate <= :endDate and c.platform = :platform"
            + " group by c.componentId, c.componentName ";

    public static final String QUERY_GET_APP_AVAILABILITY_PERCENTAGE = "select  comp.componentId, (select avg(d.percentageUpTime) "
            + " from DaillyCompStatusEntity as d join d.healthCheck as h"
            + " join h.component as c where c.componentType.componentTypeId = 2 and h.environment.environmentId = :envId"
            + " and h.region.regionName = :regionName and d.statusDate >= :startDate and d.statusDate <= :endDate "
            + " and c.parentComponent.componentId = comp.componentId ) "
            + " from ComponentEntity as comp where comp.componentType.componentTypeId = 2 and comp.parentComponent.componentId is null "
            + " group by comp.componentId ";
    
    public static final String QUERY_GET_APP_AVAILABILITY_PERCENTAGE_PLATFORM = "select  comp.componentId, (select avg(d.percentageUpTime) "
            + " from DaillyCompStatusEntity as d join d.healthCheck as h"
            + " join h.component as c where c.componentType.componentTypeId = 2 and h.environment.environmentId = :envId"
            + " and h.region.regionName = :regionName and d.statusDate >= :startDate and d.statusDate <= :endDate and c.platform = :platform"
            + " and c.parentComponent.componentId = comp.componentId ) "
            + " from ComponentEntity as comp where comp.componentType.componentTypeId = 2 and comp.parentComponent.componentId is null "
            + " group by comp.componentId ";

    public static final String UPDATE_APP_LOOKUP_FULLNAME = "UPDATE AppLookUpEntity SET component_full_name =  :component_full_name "
            + " WHERE component_id = :component_id";
    
    public static final String DELETE_APP_LOOK_UP = "DELETE AppLookUpEntity WHERE applookupId = :applookupId";

    public static final String QUERY_GET_SUM_TPS_VALUE = "select c.parentComponent.componentId, sum(t.tpsValue), sum(t.latencyValue) " 
    		+ "from TpsServiceEntity as t join t.component as c " 
    		+ "where t.environment.environmentId= :envId and c.parentComponent.componentId is not null " 
    		+ "group by c.parentComponent.componentId";
    public static final String QUERY_GET_SUM_TPS_VALUE_PLATFORM = "select c.parentComponent.componentId, sum(t.tpsValue), sum(t.latencyValue) " 
    		+ "from TpsServiceEntity as t join t.component as c " 
    		+ "where t.environment.environmentId= :envId and c.platform= :platform and c.parentComponent.componentId is not null " 
    		+ "group by c.parentComponent.componentId";
    public static final String QUERY_GET_SUM_ALL_TPS_LATENCY_HISTORY_ENV = "select c.parentComponent.componentId,c.parentComponent.componentName,t.statusDate,sum(t.tpsValue), sum(t.latencyValue) "
    		+ "from TpsLatencyHistoryEntity as t join t.component as c "
    		+ "where t.environment.environmentId= :envId and c.parentComponent.componentId is not null "
    		+ "group by c.parentComponent.componentId";
    public static final String QUERY_GET_SUM_ALL_TPS_LATENCY_HISTORY_ALL_ENV = "select c.parentComponent.componentId,c.parentComponent.componentName,t.statusDate,sum(t.tpsValue), sum(t.latencyValue) "
    		+ "from TpsLatencyHistoryEntity as t join t.component as c "
    		+ "where c.parentComponent.componentId is not null "
    		+ "group by c.parentComponent.componentId";
    
    public static final String QUERY_GET_SUM_PARENT_TPS_LATENCY_HISTORY_ENV = "select c.parentComponent.componentId,c.parentComponent.componentName,t.statusDate,sum(t.tpsValue), sum(t.latencyValue) "
    		+ "from TpsLatencyHistoryEntity as t join t.component as c "
    		+ "where t.environment.environmentId= :envId and c.parentComponent.componentId is not null "
    		+ "and t.statusDate>:startDate and t.statusDate<=:endDate "
    		+ "group by c.parentComponent.componentId "
    		+ "having c.parentComponent.componentId in (:parentComponentList)";
    public static final String QUERY_GET_SUM_PARENT_TPS_LATENCY_HISTORY_ALL_ENV = "select c.parentComponent.componentId,c.parentComponent.componentName,t.statusDate,sum(t.tpsValue), sum(t.latencyValue) "
    		+ "from TpsLatencyHistoryEntity as t join t.component as c "
    		+ "where c.parentComponent.componentId is not null "
    		+ "and t.statusDate>:startDate and t.statusDate<=:endDate "
    		+ "group by c.parentComponent.componentId "
    		+ "having c.parentComponent.componentId in (:parentComponentList)";
    public static final String UPDATE_ENVIRONMENT_K8S_CRED = "update EnvironmentEntity env "
            + " SET env.environmentDesc = :environmentDesc, env.marathonUrl = :marathonUrl, env.k8sUrl = :k8sUrl, env.displayOrder = :dispOrdr, env.envLock = :envLock, "
            + "k8sCred = :k8sCred WHERE env.environmentId = :environmentId";
    
}
