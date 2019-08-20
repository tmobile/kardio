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
package com.tmobile.kardio.surveiller.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.tmobile.kardio.surveiller.constants.HQLConstants;
import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.db.config.HibernateConfig;
import com.tmobile.kardio.surveiller.db.entity.AlertSubscriptionEntity;
import com.tmobile.kardio.surveiller.db.entity.ApiStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentFailureLogEntity;
import com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.ContainerStatsEntity;
import com.tmobile.kardio.surveiller.db.entity.CounterMetricEntity;
import com.tmobile.kardio.surveiller.db.entity.CounterMetricHistoryEntity;
import com.tmobile.kardio.surveiller.db.entity.DaillyCompStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.EnvCounterEntity;
import com.tmobile.kardio.surveiller.db.entity.EnvironmentEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckParamEntity;
import com.tmobile.kardio.surveiller.db.entity.HealthCheckTypeEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sApiStatusEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sObjectPodsEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sPodsContainersEntity;
import com.tmobile.kardio.surveiller.db.entity.K8sTpsLatHistoryEntity;
import com.tmobile.kardio.surveiller.db.entity.PromLookupEntity;
import com.tmobile.kardio.surveiller.db.entity.RegionEntity;
import com.tmobile.kardio.surveiller.db.entity.StatusEntity;
import com.tmobile.kardio.surveiller.db.entity.TpsLatHistoryEntity;
import com.tmobile.kardio.surveiller.db.entity.TpsServiceEntity;
import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.HealthCheckType;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.AppTransformConfig;
import com.tmobile.kardio.surveiller.vo.ComponentVO;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;
import com.tmobile.kardio.surveiller.vo.SubscriptionVO;

/**
 * Does all the work related to DB.
 * 
 */
public class DBQueryUtil {
	
	private static final Logger logger = Logger.getLogger(DBQueryUtil.class);

	private static boolean triedLoadingTransformConfig = false;
	private static AppTransformConfig config = null;
	
	private DBQueryUtil() {}
	/**
	 * Get Surveiller Details Of all the Component.
	 * 
	 * @return
	 */
	public static List<HealthCheckVO> getSurveillerDetailsOfComponent() {
		HashMap<Long,HealthCheckVO> healthCheckVOMap = new HashMap<Long,HealthCheckVO>();
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();		
		Transaction txn = session.beginTransaction();
		Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class);
		healthCheckCriteria.add(Restrictions.eq("delInd", 0));
		//healthCheckCriteria.add(Restrictions.ne("healthCheckType.healthCheckTypeId", HealthCheckType.Dummy.getHealthCheckTypeId()));
		@SuppressWarnings("unchecked")
		List<HealthCheckEntity> checkEntities = healthCheckCriteria.list();
		for(HealthCheckEntity entity : checkEntities) {
			HealthCheckVO hcVO = new HealthCheckVO();
			hcVO.setHealthCheckId(entity.getHealthCheckId());
			hcVO.setHealthCheckComponentId((long) entity.getComponent().getComponentId());
			hcVO.setHealthCheckRegionId(entity.getRegion().getRegionId());
			hcVO.setHealthCheckTypeClassName(entity.getHealthCheckType().getHealthCheckTypeClass());
			hcVO.setHealthCheckTypeName(entity.getHealthCheckType().getHealthCheckTypeName());
			hcVO.setHealthCheckRetryCurrentCount(entity.getFailedCount());
			hcVO.setHealthCheckRetryMaxCount(entity.getMaxRetryCount());
			if(entity.getCurrentStatus() != null){
				hcVO.setCurrentStatus(entity.getCurrentStatus().getStatusId());
			}
			hcVO.setEnvironmentId(entity.getEnvironment().getEnvironmentId());
			hcVO.setEnvironmentName(entity.getEnvironment().getEnvironmentName());
			hcVO.setRegionName(entity.getRegion().getRegionName());
			ComponentEntity component = entity.getComponent();
			int compTypeId = component.getComponentType().getComponentTypeId();
			hcVO.setComponentType(ComponentType.INFRA.getComponentTypeId() == compTypeId ? ComponentType.INFRA: ComponentType.APP);
			ComponentVO cVo = new ComponentVO();
			cVo.setComponentId(component.getComponentId());
			cVo.setComponentName(component.getComponentName());
			if(component.getParentComponent() != null) {
				cVo.setParentComponentId(component.getParentComponent().getComponentId());
				cVo.setParentComponentName(component.getParentComponent().getComponentName());
			}
			hcVO.setComponent(cVo);
			
            if(entity.getHealthCheckParams() !=null && entity.getHealthCheckParams().size() > 0) {
                Map<String,String> paramDetails = new HashMap<String,String>();
                for(HealthCheckParamEntity hcp: entity.getHealthCheckParams()){
                    paramDetails.put(hcp.getHealthCheckParamKey(), hcp.getHealthCheckParamVal());
                }
                hcVO.setParamDetails(paramDetails);
			}
			healthCheckVOMap.put(hcVO.getHealthCheckId(), hcVO);
		}
		txn.commit();
		return new ArrayList<HealthCheckVO>(healthCheckVOMap.values());
	}

	/**
	 * Get all the environment details
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public static List<EnvironmentVO> getAllEnvironments() throws SQLException {
		ArrayList<EnvironmentVO> environmentVOs = new ArrayList<EnvironmentVO>();
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria envCriteria = session.createCriteria(EnvironmentEntity.class);
		envCriteria.add(Restrictions.eq("envLock", 0));
		@SuppressWarnings("unchecked")
		List<EnvironmentEntity> environmentEntities = envCriteria.list();
		for(EnvironmentEntity entity : environmentEntities) {
			EnvironmentVO enVo = new EnvironmentVO();
			enVo.setEnvironmentId(entity.getEnvironmentId());
			enVo.setEnvironmentName(entity.getEnvironmentName());
			enVo.setMarathonUrl(entity.getMarathonURL());
			enVo.setMarathonCred(entity.getMarathonCred());
			if(entity.getMarathonJson()!=null){
				byte[] data = entity.getMarathonJson().getBytes(1, (int) entity.getMarathonJson().length());
				enVo.setMarathonJson(new String(data));
			}
			enVo.setEastMarathonUrl(entity.getEastMarathonURL());
			if(entity.getEastMarathonJson()!=null){
				byte[] data = entity.getEastMarathonJson().getBytes(1, (int) entity.getEastMarathonJson().length());
				enVo.setEastMarathonJson(new String(data));
			}
			enVo.setK8sUrl(entity.getK8sUrl());
			enVo.setK8sCred(entity.getK8sCred());
			enVo.setK8sTpsQuery(entity.getK8sTpsQuery());
			enVo.setK8sLatencyQuery(entity.getK8sLatencyQuery());
			enVo.setMesosTpsQuery(entity.getMesosTpsQuery());
			enVo.setMesosLatencyQuery(entity.getMesosLatencyQuery());
			environmentVOs.add(enVo);
		}
		txn.commit();
		return environmentVOs;
	}

	/**
	 * Updates the Health CheckStatus In DB.
	 * 
	 * @param healthCheckVOs
	 * @throws Exception 
	 */
	public static List<HealthCheckVO> updateHealthCheckStatusInDB(List<HealthCheckVO> healthCheckVOs) throws SQLException {
		
		List<HealthCheckVO> returnStatusChangedList = new ArrayList<HealthCheckVO>();
		for (HealthCheckVO healthCheckVO : healthCheckVOs) {
			if(healthCheckVO.getStatus() == null) {
				logger.error("STATUS CHECK FAILED>> ComponentId : " + healthCheckVO.getHealthCheckId()
				+ "; RegionId : " + healthCheckVO.getHealthCheckRegionId());
				continue;
			}
			Status status = healthCheckVO.getStatus().getStatus();
			if (status == null) {
				logger.error("STATUS CHECK FAILED>> ComponentId : " + healthCheckVO.getHealthCheckId()
						+ "; RegionId : " + healthCheckVO.getHealthCheckRegionId());
				continue;
			}
			if(!status.equals(Status.UP)){
				logger.info("STATUS CHECK SUCCESS>> HealthCheckId : " + healthCheckVO.getHealthCheckId()
						+ "; RegionId : " + healthCheckVO.getHealthCheckRegionId()
						+ "; isSuccess : " + status.getStatusName()
						+ "; Message : " + status.getStatusDesc());
			}
			try {
				boolean isStatusChanged = updateStatus(healthCheckVO);
				if(isStatusChanged){
					returnStatusChangedList.add(healthCheckVO);
				}
			} catch (Exception ex) {
				logger.error("Exception in DB Update Doing Rollback", ex);
				throw ex;
			}
		}
		logger.debug("DB Update Success - Doing commit");
		return returnStatusChangedList;
	}

	/**
	 * Function to update the marathon JSON in envronment
	 * 
	 * @param environmentVOs
	 */
	public static void updateEnvironmentDetails(
			List<EnvironmentVO> environmentVOs) {
		for (EnvironmentVO eVO : environmentVOs) {
			if (eVO.getMarathonJson() != null && JSONUtil.isJSONValid(eVO.getMarathonJson())) {
				updateWestMarathonJson(eVO);
			}
			if (eVO.getEastMarathonJson() != null && JSONUtil.isJSONValid(eVO.getEastMarathonJson())) {
				updateEastMarathonJson(eVO);
			}
		}
	}

	/**
	 * Update the status in DB for the given healthCheckVO.
	 * 
	 * @param conn
	 * @param healthCheckVO
	 */
	private static boolean updateStatus(HealthCheckVO healthCheckVO)
			{
		Status status = healthCheckVO.getStatus().getStatus();
		long currentFailedCount = 0;
		if (status != Status.UP) {
			if (healthCheckVO.getHealthCheckRetryCurrentCount() == null) {
				currentFailedCount = 1;
			} else {
				currentFailedCount = healthCheckVO.getHealthCheckRetryCurrentCount() + 1;
			}
		}
		updateHealthCheckStatus(healthCheckVO, (int)currentFailedCount);
		boolean isStatusChaged = updateFilureLog(healthCheckVO, currentFailedCount);
		updateHistory(healthCheckVO, (currentFailedCount >= healthCheckVO.getHealthCheckRetryMaxCount().longValue()) );
		return isStatusChaged;
	}

	/**
	 * Update dailly_comp_status table.
	 * 
	 * @param conn
	 * @param healthCheckVO
	 * @param currentFailedCount
	 */
	private static void updateHistory(final HealthCheckVO healthCheckVO, final boolean isMaxRetryCrossed) {
		
		HealthCheckEntity hc = new HealthCheckEntity();
		hc.setHealthCheckId(healthCheckVO.getHealthCheckId());
		
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria criteria = session.createCriteria(DaillyCompStatusEntity.class);
		criteria.createCriteria("healthCheck", "hc");
		criteria.add(Restrictions.eq("hc.healthCheckId", healthCheckVO.getHealthCheckId()));
		criteria.add(Restrictions.eq("statusDate", todayDate));
		criteria.setMaxResults(1);
		DaillyCompStatusEntity daillyCompStatusEntity =(DaillyCompStatusEntity) criteria.uniqueResult();
		Long totalFailureCount = daillyCompStatusEntity == null ? 0 : (long) daillyCompStatusEntity.getTotalFailureCount();

		if (daillyCompStatusEntity == null) {
			DaillyCompStatusEntity daillyCompStatus = new DaillyCompStatusEntity();
			daillyCompStatus.setHealthCheck(hc);
			Long statusId = (isMaxRetryCrossed && healthCheckVO.getStatus().getStatus() != Status.UP) ? 
					healthCheckVO.getStatus().getStatus().getStatusId() : Status.UP.getStatusId();
			StatusEntity sts = new StatusEntity();
			sts.setStatusId(statusId);
			daillyCompStatus.setStatus(sts);
			daillyCompStatus.setStatusDate(todayDate);
			daillyCompStatus.setPercentageUpTime((!isMaxRetryCrossed || healthCheckVO.getStatus().getStatus() == Status.UP) ? 100.00f : 99.93f);
			daillyCompStatus.setTotalFailureCount((!isMaxRetryCrossed || healthCheckVO.getStatus().getStatus() == Status.UP) ? 0 : 1);
			session.save(daillyCompStatus);
		} else {
			// UPDATE dailly_comp_status
			final long updatedFailureCount = (isMaxRetryCrossed && healthCheckVO.getStatus().getStatus() == Status.DOWN) ? totalFailureCount + 1: totalFailureCount;
			if(updatedFailureCount == totalFailureCount){
				return;
			}
			final float percentageUpTime = 100f - (100f * updatedFailureCount / (60f * 24f));
			
			if(totalFailureCount == 0){
				Long statusId = (isMaxRetryCrossed && healthCheckVO.getStatus().getStatus() != Status.UP) ? healthCheckVO.getStatus().getStatus().getStatusId() : Status.UP.getStatusId();
				StatusEntity sts = new StatusEntity();
				sts.setStatusId(statusId);
				daillyCompStatusEntity.setStatus(sts);
			}
			daillyCompStatusEntity.setPercentageUpTime(percentageUpTime);
			daillyCompStatusEntity.setTotalFailureCount((int)updatedFailureCount);
			session.update(daillyCompStatusEntity);
		}
		txn.commit();
	}

	/**
	 * Update the status and message in comp_failure_log.
	 * 
	 * @param conn
	 * @param healthCheckVO
	 * @param currentFailedCount
	 */
	private static boolean updateFilureLog(final HealthCheckVO healthCheckVO, final long currentFailedCount) {
		final Status status = healthCheckVO.getStatus().getStatus();
		
		//Update the failure log only if there is a status change
		if (status == healthCheckVO.getCurrentStatus()) {
			return false;
		}
		//Even if there is a failure and the max retry count is not reached - ignore
		if (status != Status.UP && currentFailedCount < healthCheckVO.getHealthCheckRetryMaxCount().longValue()) {
			return false;
		}
		//First time status check and if success - ignore
		if(healthCheckVO.getCurrentStatus() == null && status == Status.UP){
			return false;
		}
				
		String message = healthCheckVO.getStatus().getMessage();
		if (status != Status.UP && currentFailedCount >= healthCheckVO.getHealthCheckRetryMaxCount().longValue()) {
			message = healthCheckVO.getStatus().getMessage();
		} else if (status == Status.UP) {
			message = "Status changed from \"" + (healthCheckVO.getCurrentStatus() == null ? "Not Available"
							: healthCheckVO.getCurrentStatus().getStatusDesc()) + "\" to \"" + status.getStatusDesc() + "\"";
		}
		final String updateMessage = message;
		healthCheckVO.setFailureStatusMessage(updateMessage);
		ComponentFailureLogEntity cfl = new ComponentFailureLogEntity();
		cfl.setCompRegStsTime(new java.sql.Timestamp(System.currentTimeMillis()));
		cfl.setFailureMessage(updateMessage);
		cfl.setStatusId(status.getStatusId());
		cfl.setHealthCheckId(healthCheckVO.getHealthCheckId());
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		session.save(cfl);
		txn.commit();
		
		logger.debug("Inserted Failed Status comp_failure_log : HEALTH_CHECK = " + healthCheckVO.getHealthCheckId());
		return true;
	}

	/**
	 * Updates the HEALTH_CHECK table with status.
	 * 
	 * @param conn
	 * @param healthCheckVO
	 */
	private static void updateHealthCheckStatus(final HealthCheckVO healthCheckVO, final int currentFailedCount) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		
		final Status status = healthCheckVO.getStatus().getStatus();
		if ((healthCheckVO.getCurrentStatus() == null  && status == Status.UP) || status == healthCheckVO.getCurrentStatus()
				|| (status != Status.UP && currentFailedCount < healthCheckVO.getHealthCheckRetryMaxCount().longValue()) ) {
			String queryString = HQLConstants.UPDATE_HEALTH_CHECK_NO_CHANGE_WITH_STATUS;
			if (healthCheckVO.getCurrentStatus() == null
					&& status != Status.UP && currentFailedCount < healthCheckVO.getHealthCheckRetryMaxCount().longValue()) {
				queryString = HQLConstants.UPDATE_HEALTH_CHECK_NO_CHANGE;
			}
			StatusEntity sts = new StatusEntity();
			Query query = session.createQuery(queryString)
					.setTimestamp("updateTime", new java.sql.Timestamp( Calendar.getInstance().getTimeInMillis() ))
					.setInteger("failedCount", (currentFailedCount >= SurveillerConstants.DB_MAX_FAILED_COUNT) ? SurveillerConstants.DB_MAX_FAILED_COUNT : currentFailedCount)
					.setInteger("healthCheckId",  healthCheckVO.getHealthCheckId().intValue());
			if (healthCheckVO.getCurrentStatus() != null 
					&& status != Status.UP && currentFailedCount < healthCheckVO.getHealthCheckRetryMaxCount().longValue()){
				/*NOT - First time status check. Failed but did not cross max-retry count */
				sts.setStatusId(healthCheckVO.getCurrentStatus().getStatusId());
				query.setEntity("status", sts);
			} else if (!(healthCheckVO.getCurrentStatus() == null
					&& status != Status.UP && currentFailedCount < healthCheckVO.getHealthCheckRetryMaxCount().longValue())) {
				/* First time status check. Status changed and max-retry count is crossed. No need to update lastStatusChange */
				sts.setStatusId(status.getStatusId());
				query.setEntity("status", sts);
			}
			query.executeUpdate();
		} else {
			Query query = session.createQuery(HQLConstants.UPDATE_HEALTH_CHECK_CHANGE)
					.setTimestamp("updateTime", new java.sql.Timestamp( Calendar.getInstance().getTimeInMillis() ))
					.setTimestamp("lastStatusChange", new java.sql.Timestamp( Calendar.getInstance().getTimeInMillis() ))
					.setInteger("failedCount", (currentFailedCount >= SurveillerConstants.DB_MAX_FAILED_COUNT) ? SurveillerConstants.DB_MAX_FAILED_COUNT : currentFailedCount)
					.setInteger("healthCheckId", healthCheckVO.getHealthCheckId().intValue());
			StatusEntity sts = new StatusEntity();
			sts.setStatusId(status.getStatusId());
			query.setEntity("status", sts);
			query.executeUpdate();
		}
		txn.commit();
	}

	/**
	 * Function the update the marathon json in the environment table
	 * 
	 * @param conn
	 * @param environmentVO
	 * 
	 */
	public static void updateWestMarathonJson(final EnvironmentVO environmentVO) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_UPDATE_MARATHON_JSON);
		query.setBinary("marathonJson", environmentVO.getMarathonJson().getBytes());
		query.setTimestamp("lastUpdateTime", new java.sql.Timestamp(new Date().getTime()));
		query.setLong("environmentId", environmentVO.getEnvironmentId());
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Function the update the East marathon json in the environment table
	 * 
	 * @param conn
	 * @param environmentVO
	 * 
	 */
	public static void updateEastMarathonJson(final EnvironmentVO environmentVO) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_UPDATE_EAST_MARATHON_JSON);
		query.setBinary("marathonJson", environmentVO.getEastMarathonJson().getBytes());
		query.setTimestamp("lastUpdateTime", new java.sql.Timestamp(new Date().getTime()));
		query.setLong("environmentId", environmentVO.getEnvironmentId());
		query.executeUpdate();
		txn.commit();
	}

	/**
	 * Checks the current Status and updates the comp_failure_log.
	 * 
	 * @param componentId
	 * @param environmentId
	 * @param regionID
	 * @param healthCheckType
	 * @param status
	 * 
	 */
	public static boolean checkAndUpdateMessage(final int componentId, final int environmentId,
			final long regionID,final HealthCheckType healthCheckType, final Status currentStatus,
			final String marathonMessage) {
		HealthCheckVO currentDBData = getCurrentHealthCheckDetails(componentId, environmentId,regionID,healthCheckType);
		if(currentDBData == null){
			return false;
		}
		currentDBData.setStatus(new StatusVO(currentStatus, marathonMessage));
		currentDBData.setHealthCheckComponentId((long)componentId);
		currentDBData.setHealthCheckRegionId(regionID);
		currentDBData.setHealthCheckRetryCurrentCount(1L);
		currentDBData.setHealthCheckRetryMaxCount(1L);
		return updateFilureLog(currentDBData, 6);
	}

	/**
	 * get CurrentHealth Check Details from DB.
	 * 
	 * @param componentId
	 * @param environmentId
	 * @param regionID
	 * @return
	 * 
	 */
	private static HealthCheckVO getCurrentHealthCheckDetails(final int componentId, final int environmentId, final long regionID) {
		return getCurrentHealthCheckDetails(componentId, environmentId, regionID, null);
	}
	
	/**
	 * get CurrentHealth Check Details from DB.
	 * 
	 * @param componentId
	 * @param environmentId
	 * @param regionID
	 * @param healthCheckType
	 * @return
	 * @
	 */
	private static HealthCheckVO getCurrentHealthCheckDetails(final int componentId, final int environmentId,
			final long regionID,final HealthCheckType healthCheckType) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria hcCrit = session.createCriteria(HealthCheckEntity.class,"hc");
		hcCrit.add(Restrictions.eq("hc.component.componentId", componentId));
		hcCrit.add(Restrictions.eq("hc.environment.environmentId", environmentId));
		hcCrit.add(Restrictions.eq("hc.region.regionId", regionID));
		if(healthCheckType != null) {
			hcCrit.add(Restrictions.eq("hc.healthCheckType.healthCheckTypeId", healthCheckType.getHealthCheckTypeId()));
		}
		hcCrit.setMaxResults(1);
		HealthCheckEntity hcEnt = (HealthCheckEntity) hcCrit.uniqueResult();
		txn.commit();
		if(hcEnt == null){
			return null;
		}
		HealthCheckVO hcVO = new HealthCheckVO();
		hcVO.setHealthCheckId(hcEnt.getHealthCheckId());
		if(hcEnt.getCurrentStatus() != null){
			hcVO.setCurrentStatus(hcEnt.getCurrentStatus().getStatusId());
		}
		return hcVO;
	}

	/**
	 * Updates the initial status for DUMMY Health Check Handler.
	 * 
	 * @param componentId
	 * @param environmentId
	 * @param regionID
	 * @param healthCheckType
	 * @param status
	 * 
	 */
	public static void updateMarathonHealthCheckStatus(final int componentId, final int environmentId,
			final long regionID,final HealthCheckType healthCheckType, final Status status, boolean isStatusChanged) {
		String queryString = HQLConstants.DB_QUERY_UPDATE_HEALTH_CHECK_STATUS_NO_CHG;
		if(isStatusChanged){
			queryString = HQLConstants.DB_QUERY_UPDATE_HEALTH_CHECK_STATUS_CHG;
		}
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(queryString);
		query.setLong("statusId", status.getStatusId());
		query.setTimestamp("statusUpdateTime", new java.sql.Timestamp(System.currentTimeMillis()));
		query.setLong("healthCheckTypeId", healthCheckType.getHealthCheckTypeId());
		query.setLong("environmentId", environmentId);
		query.setLong("componentId", componentId);
		query.setLong("regionId", regionID);
		int rowCount = query.executeUpdate();
		logger.info("Number of Updated Kubernetes row - "+ rowCount);
		txn.commit();
		
	}
	
	/**
	 * Function to check and insert components into the DB
	 * 
	 * @param appName
	 * @param apiName
	 * @param compType
	 * 
	 */
	public static int checkAndInsertComponent(final String appName, final String apiName, final ComponentType compType,
	        final String healthCheckURL, final int environmentId, final long regionID,
	        final HealthCheckType healthCheckType, String platform) {

		//Handle special case - Name change and Component type change
		String appNameToInsert = appName;
		ComponentType compTypeToInsert = compType;
		AppTransformConfig config = readAppTransformConfig();
		if (config != null && config.getTransformName().containsKey(appName)) {
			appNameToInsert = config.getTransformName().get(appName);
		}
		if (config != null && config.getConvertToInfra().contains(appName)) {
			compTypeToInsert = ComponentType.INFRA;
		}

		int appId = insertParentComponent(appNameToInsert, compTypeToInsert, platform);
		int APIId = getComponentByNameAndParent(apiName, appId, compTypeToInsert.getComponentTypeId(), platform);
		if (APIId == 0) {
			insertComponent(apiName, appId, compTypeToInsert.getComponentTypeId(), true, platform);
			APIId = getComponentByNameAndParent(apiName,  appId, compTypeToInsert.getComponentTypeId(),platform);
			insertHealthCheck(APIId, regionID, environmentId, healthCheckType.getHealthCheckTypeId(), 3);
			if (healthCheckURL != null) {
				HealthCheckVO hcvo = getCurrentHealthCheckDetails(APIId, environmentId, regionID, healthCheckType);
				if (hcvo != null) {
					insertHealthCheckParam(hcvo.getHealthCheckId(), "URL", healthCheckURL);					
				}
			}
		} else {
			// Update existing component. Assuming single health check per Component
			HealthCheckVO currentDBData = getCurrentHealthCheckDetails(APIId, environmentId, regionID);
			if (currentDBData == null) {
				insertHealthCheck(APIId, regionID, environmentId, healthCheckType.getHealthCheckTypeId(), 3);
				if (healthCheckURL != null) {
					HealthCheckVO hcvo = getCurrentHealthCheckDetails(APIId, environmentId, regionID, healthCheckType);
					if (hcvo != null) {
						insertHealthCheckParam(hcvo.getHealthCheckId(), "URL", healthCheckURL);					
					}
				}
			} else {
				// update existing health check
				updateHealthCheck(currentDBData.getHealthCheckId(), healthCheckType.getHealthCheckTypeId());
				// remove all existing health check params
				removeHealthCheckParams(currentDBData.getHealthCheckId());
				if (healthCheckURL != null) {
					insertHealthCheckParam(currentDBData.getHealthCheckId(), "URL", healthCheckURL);
				}
			}
			
			APIId = getComponentByNameAndParent(apiName, appId, compTypeToInsert.getComponentTypeId(), platform);
		}
		return APIId;
	}

	/**
	 * Read the application config for Marathon refresh transformation.
	 * 
	 * @return
	 */
	public static AppTransformConfig readAppTransformConfig() {
		if(triedLoadingTransformConfig){
			return config;
		}
		triedLoadingTransformConfig = true;
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream in = new FileInputStream("transform.json");
			config = mapper.readValue(in, AppTransformConfig.class);
			return config;
		} catch (FileNotFoundException e) {
			logger.error("Error in reading transform.json", e);
		} catch (JsonParseException e) {
			logger.error("Error in parsing transform.json", e);
		} catch (JsonMappingException e) {
			logger.error("Error in parsing transform.json", e);
		} catch (IOException e) {
			logger.error("Error in reading transform.json", e);
		}
		return null;

	}
	
	/**
	 * Delete all the health_chaeck param of the healthCheckId 
	 * @param healthCheckId
	 * 
	 */
	private static void removeHealthCheckParams(final Long healthCheckId) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_DELETE_HEALTH_CHECK_PARAMS);
		query.setLong("healthCheckId", healthCheckId).executeUpdate();
		txn.commit();
	}

	/**
	 * Update the healthCheckTypeId and delInd = 0 for the given healthCheckId.
	 * 
	 * @param healthCheckId
	 * @param healthCheckTypeId
	 * 
	 */
	private static void updateHealthCheck(final Long healthCheckId, final Integer healthCheckTypeId) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_UPDATE_HEALTH_CHECK_TYPE);
		query.setLong("hcTypeId", healthCheckTypeId);
		query.setLong("healthCheckId", healthCheckId);
		query.executeUpdate();
		txn.commit();
	}

	/**
	 * Get the if Component Table if component is already present.
	 * Insert Data if the Component is not present.
	 * Update del_ind if component is deleted.
	 * 
	 * @param appName
	 * @param compType
	 * @return
	 * 
	 */
	private static int insertParentComponent(final String appName, final ComponentType compType, String platform) {
		int appId = getComponentByNameAndParent(appName, 0, compType.getComponentTypeId(), platform);
		if (appId == 0) {
			insertComponent(appName, 0, compType.getComponentTypeId(), false, platform);
			appId = getComponentByNameAndParent(appName, 0, compType.getComponentTypeId(), platform);
		}
		return appId;
	}

	/**
	 * Get the component id with the given name and parent id. 
	 * If a row is already present with del_ind = 1, it will be updated as 0.
	 * 
	 * @param name
	 * @param parentId
	 * @param componentTypeId
	 * @return
	 * 
	 */
	public static int getComponentByNameAndParent(final String name, final int parentId, final int componentTypeId, String platform) {
		
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria healthCheckCriteria = session.createCriteria(ComponentEntity.class, "c");
		healthCheckCriteria.createCriteria("c.componentType","ct");
		if(parentId != 0){
			healthCheckCriteria.createCriteria("c.parentComponent", "prnt");
			healthCheckCriteria.add(Restrictions.eq("prnt.componentId", parentId));
			healthCheckCriteria.add(Restrictions.eq("c.platform", platform));
		} else {
			healthCheckCriteria.add(Restrictions.isNull("c.parentComponent"));
		}
		healthCheckCriteria.add(Restrictions.eq("c.componentName", name));
		healthCheckCriteria.add(Restrictions.eq("ct.componentTypeId", componentTypeId));
		
		healthCheckCriteria.setMaxResults(1);
		ComponentEntity comp = (ComponentEntity) healthCheckCriteria.uniqueResult();
		txn.commit();
		
		if(comp == null){
			return 0;
		}
		
		if(comp.getDelInd() == 1){
			DBQueryUtil.setDelIndOfComponent(comp.getComponentId(), 0);
		}
		return comp.getComponentId();
	}

	/**
	 * Set the DelInd of Component table.
	 * 
	 * @param componentId
	 * @param delInd
	 * @
	 */
	private static void setDelIndOfComponent(final int componentId, final int delInd) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query =session.createQuery(HQLConstants.DB_QUERY_UPDATE_COMPONENT_DELIND);
		query.setLong("delInd", delInd);
		query.setLong("componentId", componentId);
		query.executeUpdate();
		txn.commit();
	}

	/**
	 * Function to get Component from component id
	 * 
	 * @param componentId
	 * @return
	 * 
	 */
	public static ComponentVO getComponent(final int componentId) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		ComponentEntity comp = session.get(ComponentEntity.class, componentId);
		txn.commit();
		
		ComponentVO cVO = new ComponentVO();
		cVO.setComponentId(comp.getComponentId());
		cVO.setComponentName(comp.getComponentName());
		cVO.setComponentDesc(comp.getComponentDesc());
		if(comp.getParentComponent() != null){
			cVO.setParentComponentId(comp.getParentComponent().getComponentId());
			cVO.setParentComponentName(comp.getParentComponent().getComponentName());
		}
		cVO.setComponentTypeId(comp.getComponentType().getComponentTypeId());
		cVO.setDelInd(comp.getDelInd());
		
		return cVO;
	}

	/**
	 * Function to insert a new component into the DB.
	 * 
	 * @param conn
	 * @param name
	 * @param parentId
	 * @param componentType
	 * @param withParent
	 * 
	 */
	public static void insertComponent(final String name, final int parentId,
			final int componentType, boolean hasParent, String platform) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		ComponentEntity componentEntity = new ComponentEntity();
		componentEntity.setComponentName(name);
		componentEntity.setDelInd(0);
		ComponentTypeEntity compTypeEntity = new ComponentTypeEntity();
		compTypeEntity.setComponentTypeId(componentType);
		componentEntity.setComponentType(compTypeEntity);
		if (hasParent) {
			componentEntity.setPlatform(platform);
			ComponentEntity parCompEntity = new ComponentEntity();
			parCompEntity.setComponentId(parentId);
			componentEntity.setParentComponent(parCompEntity);
		}
		session.save(componentEntity);
		txn.commit();
	}

	/**
	 * Insert in to Health_Check_Param table.
	 * 
	 * @param healthCheckId
	 * @param healthCheckParamKey
	 * @param healthCheckPramVal
	 * 
	 */
	public static void insertHealthCheckParam(final long healthCheckId,
			final String healthCheckParamKey, final String healthCheckPramVal) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		HealthCheckParamEntity hcpEntity = new HealthCheckParamEntity();
		HealthCheckEntity healthEntity = new HealthCheckEntity();
		healthEntity.setHealthCheckId(healthCheckId);
		hcpEntity.setHealthCheck(healthEntity);
		hcpEntity.setHealthCheckParamKey(healthCheckParamKey);
		hcpEntity.setHealthCheckParamVal(healthCheckPramVal);
		session.save(hcpEntity);
		txn.commit();
	}

	/**
	 * Insert in Health_Check table.
	 * 
	 * @param componentId
	 * @param regionId
	 * @param environmentId
	 * @param healthCheckTypeId
	 * @param maxRetryCount
	 * @param statusId
	 * 
	 */
	public static void insertHealthCheck(final int componentId,
			final Long regionId, final int environmentId,
			final int healthCheckTypeId, final int maxRetryCount) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		HealthCheckEntity healthEntity = new HealthCheckEntity();
		ComponentEntity compEntity = new ComponentEntity();
		compEntity.setComponentId(componentId);
		healthEntity.setComponent(compEntity);
		RegionEntity regEntity = new RegionEntity();
		regEntity.setRegionId(regionId);
		healthEntity.setRegion(regEntity);
		EnvironmentEntity envEntity = new EnvironmentEntity();
		envEntity.setEnvironmentId(environmentId);
		healthEntity.setEnvironment(envEntity);
		HealthCheckTypeEntity hctEntity = new HealthCheckTypeEntity();
		hctEntity.setHealthCheckTypeId(healthCheckTypeId);
		healthEntity.setHealthCheckType(hctEntity);
		StatusEntity upStatus = new StatusEntity();
		upStatus.setStatusId(Status.UP.getStatusId());
		healthEntity.setCurrentStatus(upStatus);
		healthEntity.setMaxRetryCount((long) maxRetryCount);
		healthEntity.setFailedCount(0L);
		session.save(healthEntity);
		txn.commit();
	}

	/**
	 * Function to get all components in database
	 * 
	 * @return
	 * 
	 */
	public static List<ComponentVO> getAllAPIComponentDetails(final int environmentId, String platform, long regionId) {
		
		AppTransformConfig config = readAppTransformConfig();
		
		
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class, "hc");
		healthCheckCriteria.createCriteria("hc.component","cmp");
		healthCheckCriteria.createCriteria("hc.component.parentComponent","prnt");
		healthCheckCriteria.createCriteria("hc.component.componentType", "ct");
		healthCheckCriteria.add(Restrictions.eq("hc.environment.environmentId", environmentId));
		healthCheckCriteria.add(Restrictions.eq("hc.region.regionId", regionId));
		healthCheckCriteria.add(Restrictions.isNotNull("prnt.componentId"));
		healthCheckCriteria.add(Restrictions.eq("cmp.platform", platform));
		if(config == null){
			healthCheckCriteria.add(Restrictions.eq("ct.componentTypeId", ComponentType.APP.getComponentTypeId()));
		}else{
			healthCheckCriteria.add(Restrictions.or(Restrictions.eq("ct.componentTypeId",ComponentType.APP.getComponentTypeId()),
	        		Restrictions.in("prnt.componentName", config.getTransformName().values() )) );
		}
		
		@SuppressWarnings("unchecked")
		List<HealthCheckEntity> compHltDetails = healthCheckCriteria.list();
		txn.commit();
		ArrayList<ComponentVO> returnList = new ArrayList<ComponentVO>();

		for (HealthCheckEntity hc: compHltDetails) {
			ComponentVO cVO = new ComponentVO();
			cVO.setDelInd(hc.getDelInd());
			ComponentEntity comp = hc.getComponent();
			cVO.setComponentId(comp.getComponentId());
			cVO.setComponentName(comp.getComponentName());
			cVO.setComponentDesc(comp.getComponentDesc());
			cVO.setParentComponentId(comp.getParentComponent().getComponentId());
			cVO.setComponentTypeId(comp.getComponentType().getComponentTypeId());
			
			returnList.add(cVO);
		}
		return returnList;
	}

	/**
	 * Function to update the delInd of component and health check
	 * 
	 * @param compId
	 * @param delInd
	 * 
	 */
	public static void updateDelIndForHealthCheck(final int compId,
			final int delInd, final int enviromentId, final Long regionID) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query =session.createQuery(HQLConstants.DB_QUERY_UPDATE_HEALTH_CHECK_DELIND);
		query.setLong("delInd", delInd);
		query.setLong("compId", compId);
		query.setLong("enviromentId", enviromentId);
		query.setLong("regionID", regionID);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Function to get all subscriptions from the DB
	 * 
	 * @param compIdSet
	 * @return
	 * 
	 */
	public static List<SubscriptionVO> getAllSubscriptions(Set<Integer> compIdSet) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_ALERT_SUB_WITH_COMP);
		query.setParameterList("componentIdList", compIdSet);
		
		@SuppressWarnings("unchecked")
		List<AlertSubscriptionEntity> resultList = query.list();
		txn.commit();
		
		ArrayList<SubscriptionVO> returnList = new ArrayList<SubscriptionVO>();
		for(AlertSubscriptionEntity ase : resultList){
			SubscriptionVO sVO = new SubscriptionVO();
			if(ase.getComponentId() != null){
				sVO.setComponentId(ase.getComponentId());
			}
			sVO.setEnvironmentId(ase.getEnvironment().getEnvironmentId());
			sVO.setSubscriptionValue(ase.getSubscriptionVal());
			sVO.setSubscriptionType(ase.getSubscriptionType());
			if(ase.getGlobalComponentTypeId() != null){
				sVO.setGlobalComponentTypeId(ase.getGlobalComponentTypeId());
			}
			returnList.add(sVO);
		}
        return returnList;

	}
	
	/**
	 * Function to get all subscriptions from the DB
	 * 
	 * @param compIdSet
	 * @return
	 * 
	 */
	public static List<CounterDetailVO> getCounterDetails() {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria envCriteria = session.createCriteria(EnvCounterEntity.class);
		envCriteria.addOrder(Order.desc("environmentId"));
		envCriteria.addOrder(Order.asc("counterId"));
		@SuppressWarnings("unchecked")
		List<EnvCounterEntity> envCounterEntities = envCriteria.list();
		ArrayList<CounterDetailVO> returnList = new ArrayList<CounterDetailVO>();
		for(EnvCounterEntity entity : envCounterEntities) {
			CounterDetailVO cVO = new CounterDetailVO();
			if(entity.getEnvironment() != null){
				cVO.setEnvironmentId(entity.getEnvironmentId());
			}			
			cVO.setEnvironmentCounterId(entity.getEnvCounterId());
			cVO.setCounterId(entity.getCounterId());
			cVO.setCounterMetricTypeId(entity.getMetricTypeId());
			cVO.setParameter1(entity.getParameter1());
			cVO.setParameter2(entity.getParameter2());
			cVO.setCounterMetricTypeClassName(entity.getCountMetricType().getCounterMetricTypeClassName());
			cVO.setPlatform(entity.getPlatform());
			returnList.add(cVO);
		}
		txn.commit();
		return returnList;
	}
	
	/**
	 * Insert value into COUNTER_METRIC table.
	 * 
	 * @param compId
	 * @param delInd
	 * @param enviromentId
	 * @param regionID
	 * 
	 */
	public static void addCounterMertic(final int environmentCounterId, final float counterMerticValue) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 00);
		Timestamp currDate = new java.sql.Timestamp(calendar.getTimeInMillis());
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		CounterMetricEntity counterMetric = new CounterMetricEntity();
		EnvCounterEntity envCounterEntity = new EnvCounterEntity();
		envCounterEntity.setEnvCounterId(environmentCounterId);
		counterMetric.setEnvCounter(envCounterEntity);
		counterMetric.setMetricVal(counterMerticValue);
		counterMetric.setMetricDate(currDate);
		session.save(counterMetric);
		txn.commit();
	}
	
	/**
	 * Code Changes to store Counter Metric History
	 * Insert value into COUNTER_METRIC_HISTORY table.
	 * 
	 * @param compId
	 * @param delInd
	 * @param enviromentId
	 * @param regionID
	 * 
	 */
	public static void addCounterMetricHistory(final int environmentCounterId, final float counterMerticValue) {	
		Timestamp currDate = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		CounterMetricHistoryEntity counterMetricHistory = new CounterMetricHistoryEntity();
		EnvCounterEntity envCounterEntity = new EnvCounterEntity();
		envCounterEntity.setEnvCounterId(environmentCounterId);
		counterMetricHistory.setEnvCounter(envCounterEntity);
		counterMetricHistory.setMetricVal(counterMerticValue);
		counterMetricHistory.setMetricDate(currDate);
		session.save(counterMetricHistory);
		txn.commit();
	}
	
	/**
	 * Get the metric_val for the latest metric_date for the given envCountId.
	 * 
	 * @param envCountId
	 * @return
	 * 
	 */
	public static float getTotalTransaction(final int envCountId)
			{
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_GET_COUNTER_T_TRANSACTION);
		query.setInteger("envCountId",envCountId);
		Float metricVal = (Float)query.uniqueResult();
		txn.commit();
		return metricVal == null ? 0f : metricVal;
		
	}

	/**
	 * Function to execute a delete statement..
	 * 
	 * @param query
	 */
	public static void executeDelete(String query) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		session.createSQLQuery(query).executeUpdate();
		txn.commit();
	}
	
	/**
	 * Function to find the % of UPTIME counter value
	 * 
	 */
	public static float getCCPUpTime(final int envId, final String [] parentComponentNames) {
		
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_GET_COUNTER_CCP_UPTIME);
		query.setInteger("envId", envId);
		query.setDate("stsDate", todayDate);
		query.setParameterList("compNames", parentComponentNames);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.list();
		txn.commit();
		if(resultList.size() == 0){
			return 0.0f;
		}
		float sumOfUpTime = 0.0f;
		for(Object[] resultObj : resultList){
			sumOfUpTime += (Float)resultObj[1];
		}
		return sumOfUpTime/resultList.size();
	}
	
	/**
	 * Get the Number of Services Running
	 * 
	 * @return
	 */
	public static int getNumberOfServices(CounterDetailVO counterDetails) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_GET_NUMBER_OF_SERVICES);
		query.setInteger("envId", counterDetails.getEnvironmentId());
		query.setString("platform", counterDetails.getPlatform());
		Long count = (Long)query.uniqueResult();
		txn.commit();
		return count.intValue();
	}
	
	/**
	 * Get the Marathon Json for each environment for all Environment
	 * 
	 * @return Map<Integer, Blob>
	 */
	public static Map<Integer, Blob> getMarathonJson() {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria envCrit = session.createCriteria(EnvironmentEntity.class, "env");
		envCrit.add(Restrictions.isNotNull("env.marathonURL"));
		@SuppressWarnings("unchecked")
		List<EnvironmentEntity> envList = (List<EnvironmentEntity>)envCrit.list();
		
		Map<Integer, Blob> mapEnvJson = new HashMap<Integer,Blob>();
		for(EnvironmentEntity ee: envList){
			if(ee.getMarathonURL() == null || ee.getMarathonURL().trim().length() == 0){
				continue;
			}
			mapEnvJson.put(ee.getEnvironmentId(), ee.getMarathonJson());
		}
		txn.commit();
		return mapEnvJson;
	}
	
	/**
	 * Load the data to the container_status table
	 * 
	 * @return 
	 */
	public static void loadContainerStatus(final int envId, int compId, final int instanceNum) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		
//		final int compId = getComponentId(compName, parentCompName);
//		if(compId == 0){
//			logger.info("Component Name = " + compName + "; Parent Component Name = " + parentCompName + "; is not available in the DB");
//			return 0;
//		}

		int prevNumInstance = getPreiousNumInstance(compId, todayDate, envId);
		final int deltaVal = prevNumInstance == 0 ? 0 : instanceNum - prevNumInstance;
		
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		ContainerStatsEntity contStat = new ContainerStatsEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		contStat.setEnvironment(environment);
		contStat.setStatsDate(todayDate);
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(compId);
		contStat.setComponent(component);
		contStat.setTotalContainer(instanceNum);
		contStat.setDeltaValue(deltaVal);
		session.save(contStat);
		txn.commit();
	}
	
	/**
	 * Get the previous days number of instance for a particular component
	 * 
	 * @return int
	 */
	private static int getPreiousNumInstance(final int compId, final Date date, final int envId) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria ctrStsCrit = session.createCriteria(ContainerStatsEntity.class, "cs");
		ctrStsCrit.add(Restrictions.eq("cs.component.componentId",compId));
		ctrStsCrit.add(Restrictions.eq("cs.environment.environmentId",envId));
		ctrStsCrit.add(Restrictions.eq("cs.statsDate", new java.sql.Date(cal.getTimeInMillis())  ));
		ctrStsCrit.setMaxResults(1);
		ContainerStatsEntity conSts =(ContainerStatsEntity) ctrStsCrit.uniqueResult();
		int totalContainer = 0;
		if(conSts != null){
			totalContainer = conSts.getTotalContainer();
		}
		txn.commit();
		return totalContainer;
					
	}
	
	/**
	 * Get the component id for the given compName
	 * 
	 * @return int
	 */
	public static int getComponentId(final String compName, final String parentCompName) {
	
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria componentCriteria = session.createCriteria(ComponentEntity.class);
		componentCriteria.createCriteria("parentComponent", "pc");
		componentCriteria.add(Restrictions.eq("componentName",compName));
		componentCriteria.add(Restrictions.eq("pc.componentName",parentCompName));
		componentCriteria.setMaxResults(1);
		ComponentEntity com =(ComponentEntity) componentCriteria.uniqueResult();
		int compId = 0;
		if(com != null){
			compId = com.getComponentId();
		}
		txn.commit();
		return compId;
	}
	
	/**
	 * Get All the APPS with the launch date null
	 */
	public static Set<Integer> getAppsLauchDateNull(int envId) {
	
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class, "health");
		healthCheckCriteria.createCriteria("health.component", "component");
		healthCheckCriteria.add(Restrictions.isNull("health.createdDate"));
		healthCheckCriteria.add(Restrictions.eq("health.environment.environmentId", envId));
		
		ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("component.componentId"));
        healthCheckCriteria.setProjection(projectionList);
        
        Set<Integer> compSet = new HashSet<Integer>();
		@SuppressWarnings("unchecked")
		List<Integer> resultList = (List<Integer>) healthCheckCriteria.list();
		compSet.addAll(resultList); 
		txn.commit();
		return compSet;         
	}
	
	/**
	 * @param launchDate
	 * @param compId
	 * @param envId
	 * @throws ParseException
	 */
	public static void updateAppLaunchDate(String launchDate, int compId, int envId) throws ParseException {
		SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		Date dt = sd1.parse(launchDate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query =session.createQuery(HQLConstants.DB_QUERY_UPDATE_APP_LAUNCH_DATE);
		query.setTimestamp("createdDate", new java.sql.Timestamp(format.parse(format.format(dt)).getTime()));
		query.setLong("compId", compId);
		query.setLong("enviromentId", envId);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Load the data to the container_status table
	 * 
	 * @return 
	 */
	public static void loadApiStatus(final int envId, String compName, int numOfApi) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		
		final int compId = getComponentIdFromCompName(compName);
		if(compId == 0){
			logger.info("Component Name = " + compName + "; is not available in the DB");
			return;
		}

		int prevNumApis = getPreiousNumApis(compId, todayDate, envId);
		final int deltaVal = prevNumApis == 0 ? 0 : numOfApi - prevNumApis;
		
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		ApiStatusEntity apiStatus = new ApiStatusEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		apiStatus.setEnvironment(environment);
		apiStatus.setStatusDate(todayDate);
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(compId);
		apiStatus.setComponent(component);
		apiStatus.setTotalApi(numOfApi);
		apiStatus.setDeltaValue(deltaVal);
		session.save(apiStatus);
		txn.commit();
	}
	
	/**
	 * Gets the Component Id from Component Name.
	 * 
	 * @param compName
	 * @return
	 */
	private static int getComponentIdFromCompName(String compName){
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria componentCriteria = session.createCriteria(ComponentEntity.class);
		componentCriteria.add(Restrictions.eq("componentName",compName));
		componentCriteria.add(Restrictions.eq("delInd", 0));
		componentCriteria.setMaxResults(1);
		ComponentEntity com =(ComponentEntity) componentCriteria.uniqueResult();
		int compId = 0;
		if(com != null){
			compId = com.getComponentId();
		}
		txn.commit();
		return compId;
	}
	
	/**
	 * Get the previous days number of apis for a particular component
	 * 
	 * @return int
	 */
	private static int getPreiousNumApis(final int compId, final Date date, final int envId) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria ctrStsCrit = session.createCriteria(ApiStatusEntity.class, "as");
		ctrStsCrit.add(Restrictions.eq("as.component.componentId",compId));
		ctrStsCrit.add(Restrictions.eq("as.environment.environmentId",envId));
		ctrStsCrit.add(Restrictions.eq("as.statusDate", new java.sql.Date(cal.getTimeInMillis())  ));
		ctrStsCrit.setMaxResults(1);
		ApiStatusEntity apiSts =(ApiStatusEntity) ctrStsCrit.uniqueResult();
		int totalApi = 0;
		if(apiSts != null){
			totalApi = apiSts.getTotalApi();
		}
		txn.commit();
		return totalApi;
	}
	/**
	 * Get the current api details of the environment
	 * 
	 */
	public static Map<String, Integer> getCurrentApiDetails(int envId) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria apiCriteria = session.createCriteria(ApiStatusEntity.class, "apiSts");
		apiCriteria.add(Restrictions.eq("apiSts.statusDate", todayDate ));
		apiCriteria.add(Restrictions.eq("apiSts.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<ApiStatusEntity> apiEntityList = apiCriteria.list();
		Map<String, Integer> mapApiSts = new HashMap<String, Integer>();
		for(ApiStatusEntity apiStsEntity : apiEntityList){
			if(apiStsEntity.getComponent() != null){
				mapApiSts.put(apiStsEntity.getComponent().getComponentName(), apiStsEntity.getTotalApi());
		    }
	    }
		txn.commit();
		return mapApiSts;
	}
	
	/**
	 * Update the api_status/k8s_api_status table with latest number of apis
	 * @param envId
	 * @param compName
	 * @param numOfApi
	 * @param platform
	 */
	public static void updateApiDetails(final int envId, String compName, int numOfApi, String platform){
		final int compId = getComponentIdFromCompName(compName);
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		if(compId == 0){
			logger.info("Component Name = " + compName + "; is not available in the DB");
			return;
		}
		String updateApiStsQry = null;
		if(platform.equalsIgnoreCase("Mesos")){
			updateApiStsQry = HQLConstants.UPDATE_API_DETAILS;
		}else if(platform.equalsIgnoreCase("K8s")){
			updateApiStsQry = HQLConstants.UPDATE_K8S_API_DETAILS;
		}
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(updateApiStsQry);
		query.setInteger("numOfApi", numOfApi);
		query.setLong("compId", compId);
		query.setLong("environmentId", envId);
		query.setDate("stsDate", todayDate);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Get the current api details of the environment
	 * 
	 */
	public static Map<String, Integer> getCurrentContainerDetails(int envId) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria apiCriteria = session.createCriteria(ContainerStatsEntity.class, "contSts");
		apiCriteria.add(Restrictions.eq("contSts.statsDate", todayDate ));
		apiCriteria.add(Restrictions.eq("contSts.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<ContainerStatsEntity> contEntityList = apiCriteria.list();
		Map<String, Integer> mapApiSts = new HashMap<String, Integer>();
		for(ContainerStatsEntity contStsEntity : contEntityList){
			if(contStsEntity.getComponent() != null && contStsEntity.getComponent().getParentComponent() != null){
				String fullAppName = contStsEntity.getComponent().getParentComponent().getComponentName() + "/" + contStsEntity.getComponent().getComponentName();
				mapApiSts.put(fullAppName, contStsEntity.getTotalContainer());
		    }else if(contStsEntity.getComponent() != null){
		    	mapApiSts.put(contStsEntity.getComponent().getComponentName(), contStsEntity.getTotalContainer());
		    }
	    }
		txn.commit();
		return mapApiSts;
	}
	/**
	 * Update the container_status table with latest number of apis
	 * 
	 */
	public static void updateContainerDetails(final int envId, int compId, int numOfcont){
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;

		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.UPDATE_CONTAINER_DETAILS);
		query.setInteger("numOfContainers", numOfcont);
		query.setLong("compId", compId);
		query.setLong("environmentId", envId);
		query.setDate("stsDate", todayDate);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Get the current prom_lookup details of the environment
	 * 
	 */
	public static Map<String, String> getPromLookupDetails(int envId, String platform) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria promLookCriteria = session.createCriteria(PromLookupEntity.class, "promLook");
		promLookCriteria.createCriteria("promLook.component", "comp");
		promLookCriteria.add(Restrictions.eq("comp.platform", platform));
		promLookCriteria.add(Restrictions.eq("promLook.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<PromLookupEntity> promLookEntityList = promLookCriteria.list();
		Map<String, String> mapPromLook = new HashMap<String, String>();
		for(PromLookupEntity promLookEntity : promLookEntityList){
			if(promLookEntity.getComponent() != null){
				if(platform.equals(SurveillerConstants.K8S_PLATFORM)){
					mapPromLook.put(promLookEntity.getComponent().getComponentName()+"/"+promLookEntity.getHttpPath(), promLookEntity.getHttpPath());
		    	}else{
		    		mapPromLook.put(promLookEntity.getComponent().getParentComponent().getComponentName()+"/"+promLookEntity.getComponent().getComponentName(), promLookEntity.getHttpPath());
		    	}		 
			}
	    }
		txn.commit();
		return mapPromLook;
	}
	
	/**
	 * Load the data to the prom_lookup table
	 * 
	 * @return 
	 */
	public static int loadPromLookup(final int envId, int compId, String httpPath) {

		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		PromLookupEntity promLook = new PromLookupEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		promLook.setEnvironment(environment);
		promLook.setLastUpdated(new java.sql.Timestamp(System.currentTimeMillis()));
		
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(compId);
		promLook.setComponent(component);
		promLook.setLaunchDate(new java.sql.Timestamp(System.currentTimeMillis()));
		promLook.setHttpPath(httpPath);
		session.save(promLook);
		txn.commit();
		return compId;
	}
	
	/**
	 * Update the prom_lookup table with latest http path. 
	 * 
	 */
	public static void updatePromLookup(final int envId, int compId, String httpPath){

		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.UPDATE_PROM_LOOKUP_DETAILS);
		query.setString("httpPath", httpPath);
		query.setTimestamp("lastUpdateDate", new java.sql.Timestamp(System.currentTimeMillis()));
		query.setLong("compId", compId);
		query.setLong("environmentId", envId);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Get the current prom_lookup details of the environment
	 * 
	 */
	public static Map<String, Integer> getPromLookupHttpPath(int envId, String platform) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria promLookCriteria = session.createCriteria(PromLookupEntity.class, "promLook");
		promLookCriteria.createCriteria("promLook.component", "comp");
		promLookCriteria.add(Restrictions.eq("comp.platform", platform));
		promLookCriteria.add(Restrictions.eq("promLook.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<PromLookupEntity> promLookEntityList = promLookCriteria.list();
		Map<String, Integer> mapPromLook = new HashMap<String, Integer>();
		for(PromLookupEntity promLookEntity : promLookEntityList){
			if(promLookEntity.getComponent() != null){
				mapPromLook.put(promLookEntity.getHttpPath(), promLookEntity.getComponent().getComponentId());
		    }
	    }
		txn.commit();
		return mapPromLook;
	}
	
	/**
	 * Get the current tps_service details of the environment
	 * 
	 */
	public static List<Integer> getAllCurrentTPS(int envId, String platform) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria tpsCriteria = session.createCriteria(TpsServiceEntity.class, "tps");
		tpsCriteria.createCriteria("tps.component", "comp");
		tpsCriteria.add(Restrictions.eq("comp.platform", platform));
		tpsCriteria.add(Restrictions.eq("tps.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<TpsServiceEntity> tpsEntityList = tpsCriteria.list();
		List<Integer> listOfComp = new ArrayList<Integer>();
		for(TpsServiceEntity tpsEntity : tpsEntityList){
			if(tpsEntity.getComponent() != null){
				listOfComp.add(tpsEntity.getComponent().getComponentId());
		    }
	    }
		txn.commit();
		return listOfComp;
	}
	
	/**
	 * Update the tps_service table with latest tps values. 
	 * 
	 */
	public static void updateTpsService(final int envId, int componentId, float tpsVaule, float latencyValue){
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.UPDATE_TPS_SERVICE_DETAILS);
		query.setFloat("tpsVaule", tpsVaule);
		query.setFloat("latencyValue", latencyValue);
		query.setTimestamp("lastUpdateDate", new java.sql.Timestamp(System.currentTimeMillis()));
		query.setLong("compId", componentId);
		query.setLong("environmentId", envId);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Insert data to tps_service table
	 * 
	 * @return 
	 */
	public static void loadTpsService(final int envId, int conponentId , float tpsVaule, float latencyValue) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		TpsServiceEntity tpsService = new TpsServiceEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		tpsService.setEnvironment(environment);
		tpsService.setLastUpdated(new java.sql.Timestamp(System.currentTimeMillis()));
		
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(conponentId);
		tpsService.setComponent(component);
		tpsService.setLaunchDate(new java.sql.Timestamp(System.currentTimeMillis()));
		tpsService.setTpsValue(tpsVaule);
		tpsService.setLatencyValue(latencyValue);
		session.save(tpsService);
		txn.commit();
	}

	/**
	 * Get the current tps_latency_history details of the environment
	 * 
	 */
	public static Map<Integer, List<Float>> getTpsLatencyHsitory(int envId, String platform) {
		String queryStr = null;
		if(platform.equals("K8s")){
			queryStr = HQLConstants.GET_K8S_TPS_LAT_HISTORY;
		}else if(platform.equals("Mesos")){
			queryStr = HQLConstants.GET_MESOS_TPS_LAT_HISTORY;
		}else{
			logger.error("Not a valid platform");
		}
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(queryStr);
		query.setInteger("envId", envId);
		query.setDate("statusDate", todayDate);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.list();
		txn.commit();
		Map<Integer, List<Float>> mapTpsLat = new HashMap<Integer, List<Float>>();
		List<Float> listTpsLat = null;
		for(Object[] resultObj : resultList){
			Float tpsValue = (Float)resultObj[0];
			Float latencyValues = (Float)resultObj[1];
			Integer comId = (Integer)resultObj[2];
			listTpsLat = new ArrayList<Float>();
			listTpsLat.add(tpsValue);
			listTpsLat.add(latencyValues);
			mapTpsLat.put(comId, listTpsLat);
		}
		return mapTpsLat;
	}
	
	/**
	 * Insert data to tps_latency_history table
	 * 
	 * @return 
	 */
	public static void loadTpsLatencyHistory(final int envId, int conponentId , float tpsVaule, float latencyValue) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		TpsLatHistoryEntity tpsLabHis = new TpsLatHistoryEntity();
		setTPSLatencyHistory(envId, conponentId, tpsVaule, latencyValue, todayDate, tpsLabHis);
		session.save(tpsLabHis);
		txn.commit();
	}
	private static void setTPSLatencyHistory(final int envId, int conponentId, float tpsVaule, float latencyValue,
			final java.sql.Date todayDate, TpsLatHistoryEntity tpsLabHis) {
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		tpsLabHis.setEnvironment(environment);
		tpsLabHis.setStatusDate(todayDate);
		
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(conponentId);
		tpsLabHis.setComponent(component);
		tpsLabHis.setTpsValue(tpsVaule);
		tpsLabHis.setLatencyValue(latencyValue);
	}
	
	/**
	 * Insert data to k8s_tps_latency_history table
	 * 
	 * @return 
	 */
	public static void loadK8sTpsLatencyHistory(final int envId, int conponentId , float tpsVaule, float latencyValue) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		K8sTpsLatHistoryEntity tpsLabHis = new K8sTpsLatHistoryEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		tpsLabHis.setEnvironment(environment);
		tpsLabHis.setStatusDate(todayDate);
		
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(conponentId);
		tpsLabHis.setComponent(component);
		tpsLabHis.setTpsValue(tpsVaule);
		tpsLabHis.setLatencyValue(latencyValue);
		session.save(tpsLabHis);
		txn.commit();
	}
	
	/**
	 * Update the tps_latency_history/k8s_tps_latency_history table with tps & Latency values. 
	 * 
	 */
	public static void updateTpsLatHistory(final int envId, int componentId, float tpsVaule, float latencyValue, String platform){
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		
		String queryStr = null;
		if(platform.equals("K8s")){
			queryStr = HQLConstants.UPDATE_K8S_TPS_LAT_HISTORY;
		}else if(platform.equals("Mesos")){
			queryStr = HQLConstants.UPDATE_MESOS_TPS_LAT_HISTORY;
		}else{
			return;
		}
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(queryStr);
		query.setFloat("tpsVaule", tpsVaule);
		query.setFloat("latencyValue", latencyValue);
		query.setLong("compId", componentId);
		query.setLong("environmentId", envId);
		query.setDate("statusDate", todayDate);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Get the current number of apis from k8s_api_status
	 * @param envId
	 * @return
	 */
	public static Map<String, Integer> getK8sCurrentApiDetails(int envId) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria apiCriteria = session.createCriteria(K8sApiStatusEntity.class, "apiSts");
		apiCriteria.add(Restrictions.eq("apiSts.statusDate", todayDate ));
		apiCriteria.add(Restrictions.eq("apiSts.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<K8sApiStatusEntity> apiEntityList = apiCriteria.list();
		Map<String, Integer> mapApiSts = new HashMap<String, Integer>();
		for(K8sApiStatusEntity apiStsEntity : apiEntityList){
			if(apiStsEntity.getComponent() != null){
				mapApiSts.put(apiStsEntity.getComponent().getComponentName(), apiStsEntity.getTotalApi());
		    }
	    }
		txn.commit();
		return mapApiSts;
	}
	
	/**
	 * Get the current number of container from k8s_pods_containers
	 * @param envId
	 * @param platform
	 * @return
	 */
	public static Map<String, Integer> getK8sCurrContainerDetails(int envId, String platform) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Criteria contCriteria = session.createCriteria(K8sPodsContainersEntity.class, "contSts");
		contCriteria.add(Restrictions.eq("contSts.statusDate", todayDate ));
		contCriteria.add(Restrictions.eq("contSts.environment.environmentId", envId));
		@SuppressWarnings("unchecked")
		List<K8sPodsContainersEntity> contEntityList = contCriteria.list();
		Map<String, Integer> mapContSts = new HashMap<String, Integer>();
		for(K8sPodsContainersEntity contStsEntity : contEntityList){
			if(contStsEntity.getComponent() != null && contStsEntity.getComponent().getParentComponent() != null){
				String fullAppName = contStsEntity.getComponent().getParentComponent().getComponentName() + "/" + contStsEntity.getComponent().getComponentName();
				mapContSts.put(fullAppName, contStsEntity.getTotalContainers());
		    }else if(contStsEntity.getComponent() != null){
		    	mapContSts.put(contStsEntity.getComponent().getComponentName(), contStsEntity.getTotalContainers());
		    }
	    }
		txn.commit();
		return mapContSts;
	}
	
	
	/**
	 * Get the current number of container from k8s_obj_pods
	 * This table holds the pods&containers of K8s Objects other than deployment.
	 * @param envId
	 * @param platform
	 * @return
	 */
	public static Map<String, Integer> getK8sObjPodsContDetails(int envId) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query numApisQuery = session.createQuery(HQLConstants.QUERY_GET_K8S_OBJECT_PODS);
		numApisQuery.setParameter("environmentId", envId);
		numApisQuery.setParameter("statusDate", todayDate);
		@SuppressWarnings("unchecked")
		List<Object[]> objPodsList = numApisQuery.list();
		Map<String, Integer> mapObjPods = new HashMap<String, Integer>();
		for (Object[] tRow : objPodsList) {
			mapObjPods.put((String)tRow[0], ((Integer)tRow[1]));
		}
		txn.commit();
		return mapObjPods;
	}
	
	
	/**
	 * Get the current number of K8s active apis of apps in an environment
	 * @param envId
	 * @param platform
	 * @return
	 */
	public static Map<String, Integer> getNumOfActiveApisOfApp(int envId, String platform) {
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query numApisQuery = session.createQuery(HQLConstants.QUERY_GET_NUM_APP_APIS);
		numApisQuery.setParameter("envId", envId);
		numApisQuery.setParameter("platform", platform);
		@SuppressWarnings("unchecked")
		List<Object[]> totTps = numApisQuery.list();
		Map<String, Integer> mapNumApis = new HashMap<String, Integer>();
		for (Object[] tRow : totTps) {
			mapNumApis.put((String)tRow[0], ((Long)tRow[1]).intValue());
		}
		txn.commit();
		return mapNumApis;
	}
	
	
	/**
	 * Load the data to the k8s_api_status table
	 * @param envId
	 * @param compName
	 * @param numOfApi
	 */
	public static void loadK8sApiStatus(final int envId, String compName, int numOfApi) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		
		final int compId = getComponentIdFromCompName(compName);
		if(compId == 0){
			logger.info("Component Name = " + compName + "; is not available in the DB");
			return;
		}

		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		K8sApiStatusEntity apiStatus = new K8sApiStatusEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		apiStatus.setEnvironment(environment);
		apiStatus.setStatusDate(todayDate);
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(compId);
		apiStatus.setComponent(component);
		apiStatus.setTotalApi(numOfApi);
		session.save(apiStatus);
		txn.commit();
	}
	
	/**
	 * Update the container_status/k8s_container_status table with latest number of containers
	 * @param envId
	 * @param compName
	 * @param numOfcont
	 * @param parentCompName
	 * @param platform
	 */
	public static void updateContainerDetails(final int envId, String compName, int numOfcont, String parentCompName, String platform){
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		final int compId = getComponentId(compName, parentCompName);
		if(compId == 0){
			logger.info("Component Name = " + compName + "; Parent Component Name = " + parentCompName + "; is not available in the DB");
			return;
		}
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		String updateApiStsQry = null;
		if(platform.equalsIgnoreCase("Mesos")){
			updateApiStsQry = HQLConstants.UPDATE_CONTAINER_DETAILS;
		}
		Query query = session.createQuery(updateApiStsQry);
		query.setInteger("numOfContainers", numOfcont);
		query.setLong("compId", compId);
		query.setLong("environmentId", envId);
		query.setDate("stsDate", todayDate);
		query.executeUpdate();
		txn.commit();
	}
	
	
	/**
	 * Update the Pods&Containers of the object other than deployment. 
	 * @param environmentId
	 * @param objName
	 * @param podContList
	 */
	public static void updateObjPodss(int environmentId, String objName, ArrayList<Integer> podContList){
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		String updateApiStsQry = null;
		updateApiStsQry = HQLConstants.UPDATE_K8S_OBJECT_PODS;
		
		Query query = session.createQuery(updateApiStsQry);
		query.setInteger("pods", podContList.get(0));
		query.setInteger("containers", podContList.get(1));
		query.setString("objName", objName);
		query.setLong("environmentId", environmentId);
		query.setDate("stsDate", todayDate);
		query.executeUpdate();
		txn.commit();
	}
	
	
	/**
	 * Load the Pods&Containers of the objects other than Deployment
	 * @param environmentId
	 * @param objName
	 * @param podContList
	 */
	public static void loadK8sObjectPods(int environmentId, String objName, ArrayList<Integer> podContList) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		K8sObjectPodsEntity contStat = new K8sObjectPodsEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(environmentId);
		contStat.setEnvironment(environment);
		contStat.setStatusDate(todayDate);
		contStat.setObjectName(objName);
		contStat.setPods(podContList.get(0));
		contStat.setContainers(podContList.get(1));
		//contStat.setDeltaValue(deltaVal);
		session.save(contStat);
		txn.commit();
	}
	
	
	/**
	 * Update the k8s_pods_containers table with number of Pods&Containers
	 * @param envId
	 * @param compName
	 * @param numOfPods
	 * @param parentCompName
	 * @param numOfCont
	 */
	public static void updatePodsAndContainers(final int envId, String compName, int numOfPods, String parentCompName, int numOfCont){
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		final int compId = getComponentId(compName, parentCompName);
		if(compId == 0){
			logger.info("Component Name = " + compName + "; Parent Component Name = " + parentCompName + "; is not available in the DB");
			return;
		}
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.UPDATE_K8S_PODS_CONT_DETAILS);
		query.setInteger("totalPods", numOfPods);
		query.setInteger("totalContainers", numOfCont);
		query.setLong("compId", compId);
		query.setLong("environmentId", envId);
		query.setDate("stsDate", todayDate);
		query.executeUpdate();
		txn.commit();
	}
	
	/**
	 * Load the Pods&Containers information to k8s_pods_containers table
	 * @param envId
	 * @param compName
	 * @param parentCompName
	 * @param instanceNum
	 * @return
	 */
	public static int loadPodsAndContainers(final int envId,final String compName,final String parentCompName, final int numPods, int numCont ) {
		final java.sql.Date todayDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());;
		
		final int compId = getComponentId(compName, parentCompName);
		if(compId == 0){
			logger.info("Component Name = " + compName + "; Parent Component Name = " + parentCompName + "; is not available in the DB");
			return 0;
		}

		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		K8sPodsContainersEntity  podStat = new K8sPodsContainersEntity();
		EnvironmentEntity environment = new EnvironmentEntity();
		environment.setEnvironmentId(envId);
		podStat.setEnvironment(environment);
		podStat.setStatusDate(todayDate);
		ComponentEntity component = new ComponentEntity();
		component.setComponentId(compId);
		podStat.setComponent(component);
		podStat.setTotalPods(numPods);
		podStat.setTotalContainers(numCont);
		session.save(podStat);
		txn.commit();
		return compId;
	}
	
	 /**
     * Get List of components from the database for the given EnvironmentEntity.
     *
   	 * @param envId
   	 * @param platform
   	 * @return
   	 */
   	public static Map<String, Integer> getAllComponentIdForEnv(int envId,String platform) {
    	Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
        Criteria hcCriteria = session.createCriteria(HealthCheckEntity.class,"hc");
        hcCriteria.createCriteria("hc.component", "c");
        hcCriteria.createCriteria("c.parentComponent", "pc");
        hcCriteria.add(Restrictions.eq("hc.delInd", 0));
        hcCriteria.add(Restrictions.eq("c.delInd", 0));
        hcCriteria.add(Restrictions.eq("pc.delInd", 0));
        hcCriteria.add(Restrictions.eq("hc.environment.environmentId", envId));
        hcCriteria.add(Restrictions.eq("c.platform", platform));
        Map<String, Integer> compMap = new HashMap<String, Integer>();
        List<HealthCheckEntity> results = hcCriteria.list();
        for(HealthCheckEntity healthCheckEntity : results){
        	String comp = healthCheckEntity.getComponent().getParentComponent().getComponentName()+"/"+healthCheckEntity.getComponent().getComponentName();
        	compMap.put(comp, healthCheckEntity.getComponent().getComponentId());
        }
        txn.commit();
		return compMap;
	}

	public static void deletePromLookup(int envId, int compId, String lookupVaule) {
		// TODO Auto-generated method stub
		Session session = HibernateConfig.getSessionFactory().getCurrentSession();
		Transaction txn = session.beginTransaction();
		Query query = session.createQuery(HQLConstants.DB_QUERY_DELETE_PROM_LOOKUP);
		query.setLong("compId", compId);
		query.setLong("envId", envId);
		query.setString("lookupVaule", lookupVaule);
		query.executeUpdate();
		txn.commit();
	}
}
