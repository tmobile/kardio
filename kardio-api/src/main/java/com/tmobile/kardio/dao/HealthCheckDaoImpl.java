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
package com.tmobile.kardio.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.HealthCheckParamVO;
import com.tmobile.kardio.bean.HealthCheckTypeVO;
import com.tmobile.kardio.bean.HealthCheckVO;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;
import com.tmobile.kardio.db.entity.HealthCheckParamEntity;
import com.tmobile.kardio.db.entity.HealthCheckTypeEntity;
import com.tmobile.kardio.db.entity.RegionEntity;
import com.tmobile.kardio.exceptions.ValidationFailedException;

/**
 * Implements interface HealthCheckDao To query and do CRUD opeerations on health_check table
 * 
 * Health Check Dao Impl
 * 
 */
@Repository
@PropertySource("classpath:application.properties")
public class HealthCheckDaoImpl implements HealthCheckDao {
    private static Logger log = LoggerFactory.getLogger(HealthCheckDaoImpl.class);

    @Value("${adminpage.skip.infra.component.name}")
    private String skipInfraCompNames;

    private List<String> skipableInfraComponents;

    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void inititializeDao() throws Exception {
        skipableInfraComponents = Arrays.asList(skipInfraCompNames.trim().split(","));
    }

    /**
     * Get all Health Check and Health Check Param details.
     */
    @Override
    public List<HealthCheckVO> getAllHealthCheckDetails() {

        List<HealthCheckVO> healthCheckDtls = getInfraHealthCheckDetails();
        log.debug("Size healthCheckDtls : " + healthCheckDtls.size());
        Map<Integer, List<HealthCheckParamVO>> healthCheckParamDtls = getHealthCheckParamDetails(0, true);
        log.debug("Size healthCheckParamDtls : " + healthCheckParamDtls.size());
        for (HealthCheckVO healthCheck : healthCheckDtls) {
            healthCheck.setHealthCheckParamList(healthCheckParamDtls.get(healthCheck.getHealthCheckId()));
        }
        return healthCheckDtls;
    }

    /**
     * Get all Health Check Types from DB.
     */
    @Override
    public List<HealthCheckTypeVO> getAllHealthCheckTypes() {
        List<HealthCheckTypeVO> checkTypeVOs = new ArrayList<HealthCheckTypeVO>();
        Session session = sessionFactory.openSession();
        Criteria checkTypeCriteria = session.createCriteria(HealthCheckTypeEntity.class);
        @SuppressWarnings("unchecked")
        List<HealthCheckTypeEntity> checkTypeEntities = checkTypeCriteria.list();
        for (HealthCheckTypeEntity entity : checkTypeEntities) {
            HealthCheckTypeVO healthCheckType = new HealthCheckTypeVO();
            healthCheckType.setHealthCheckTypeId(entity.getHealthCheckTypeId());
            healthCheckType.setHealthCheckTypeName(entity.getHealthCheckTypeName());
            healthCheckType.setHealthCheckTypeDesc(entity.getHealthCheckTypeDesc());
            checkTypeVOs.add(healthCheckType);
        }
        session.close();
        return checkTypeVOs;
    }

    /**
     * Marks the del_ind as 1 for the given health_check_id in the health_check table.
     */
    @Override
    public void deleteHealthCheck(int healthCheckId) {
        if (healthCheckId == 0) {
            throw new IllegalArgumentException("Invalid Health Check deletion");
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.UPDATE_HEALTH_CHECK_DEL_IND).setInteger("hcId", healthCheckId);
        query.executeUpdate();
        tx.commit();
        session.close();
        log.info("Deleted HEALTH_CHECK : " + healthCheckId);
    }

    /**
     * Updates/Adds the health check with given details. HealthCheckId = 0 for add.
     */
    @Override
    public void editHealthCheck(HealthCheckVO healthCheck) {
        if (healthCheck.getEnvironmentId() == 0) {
            throw new ValidationFailedException("Invalid Environment");
        }
        if (healthCheck.getRegionId() == 0) {
            throw new ValidationFailedException("Invalid Region");
        }
        if (healthCheck.getComponentId() == 0) {
            throw new ValidationFailedException("Invalid Component");
        }

        List<HealthCheckVO> healthCheckDtls = getHealthCheckDetails(healthCheck.getEnvironmentId(), healthCheck.getRegionId(),
                healthCheck.getComponentId());
        if (healthCheckDtls.size() > 1) {
            throw new ValidationFailedException(
                    "Multiple Health Check config already exist for the given Environment + Region + Component. Delete one at back end DB");
        }
        if (healthCheckDtls.size() == 1 && healthCheckDtls.get(0).getHealthCheckId() != healthCheck.getHealthCheckId()) {
            throw new ValidationFailedException("Another Health Check config already exist for the given Environment + Region + Component.");
        }
        if (healthCheck.getHealthCheckId() == 0) {
            addNewHealthCheck(healthCheck);
        } else {
            updateHealthCheck(healthCheck);
        }
    }

    /**
     * Insert data to HEALTH_CHECK and HEALTH_CHECK_PARAM.
     * 
     * @param healthCheck
     */
    private void addNewHealthCheck(final HealthCheckVO healthCheck) {
        HealthCheckEntity healthCheckEntity = new HealthCheckEntity();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        healthCheckEntity.setComponent(session.get(ComponentEntity.class, healthCheck.getComponentId()));
        healthCheckEntity.setRegion(session.get(RegionEntity.class, healthCheck.getRegionId()));
        healthCheckEntity.setEnvironment(session.get(EnvironmentEntity.class, healthCheck.getEnvironmentId()));
        healthCheckEntity.setMaxRetryCount(healthCheck.getMaxRetryCount() <= 1 ? 1 : healthCheck.getMaxRetryCount());
        healthCheckEntity.setHealthCheckType(session.get(HealthCheckTypeEntity.class, healthCheck.getHealthCheckTypeId()));
        healthCheckEntity.setDelInd(healthCheck.isDelInd()?1:0);
        session.save(healthCheckEntity);
        tx.commit();
        session.close();

        int newHealthCheckId = healthCheckEntity.getHealthCheckId();
        log.info("Inserted HEALTH_CHECK : " + newHealthCheckId);
        for (HealthCheckParamVO healthCheckParam : healthCheck.getHealthCheckParamList()) {
            addNewHealthCheckParam(newHealthCheckId, healthCheckParam);
        }
    }

    /**
     * Insert data to HEALTH_CHECK_PARAM
     */
    private void addNewHealthCheckParam(final long healthCheckId, final HealthCheckParamVO healthCheckParam) {
        HealthCheckParamEntity checkParamEntity = new HealthCheckParamEntity();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        checkParamEntity.setHealthCheck(session.get(HealthCheckEntity.class, (int)healthCheckId));
        checkParamEntity.setHealthCheckParamKey(healthCheckParam.getHealthCheckParamKey());
        checkParamEntity.setHealthCheckParamVal(healthCheckParam.getHealthCheckParamValue());
        session.save(checkParamEntity);
        tx.commit();
        session.close();

        int newHealthCheckParamId = checkParamEntity.getHealthCheckParamId();
        log.info("Inserted HEALTH_CHECK_PARAM : " + newHealthCheckParamId);
    }

    /**
     * Update data in HEALTH_CHECK_PARAM.
     * 
     * @param healthCheckParam
     */
    private void updateHealthCheckParam(HealthCheckParamVO healthCheckParam) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.UPDATE_HEALTH_CHECK_PARAM).setString("paramKey", healthCheckParam.getHealthCheckParamKey())
                .setString("paramVal", healthCheckParam.getHealthCheckParamValue())
                .setInteger("hcParamId", healthCheckParam.getHealthCheckParamId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    /**
     * Delete data in HEALTH_CHECK_PARAM.
     * 
     * @param healthCheckParam
     */
    private void deleteHealthCheckParam(HealthCheckParamVO healthCheckParam) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.DELETE_APP_SESSION).setInteger("hcParamId", healthCheckParam.getHealthCheckParamId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    /**
     * Update data in HEALTH_CHECK and HEALTH_CHECK_PARAM.
     * 
     * @param healthCheck
     */
    private void updateHealthCheck(HealthCheckVO newhealthCheck) {
        int delInd = newhealthCheck.isDelInd() ? 1 : 0;
        // UPDATE HEALTH_CHECK
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.UPDATE_HEALTH_CHECK)
                .setEntity("component", session.get(ComponentEntity.class, newhealthCheck.getComponentId()))
                .setEntity("region", session.get(RegionEntity.class, newhealthCheck.getRegionId()))
                .setEntity("environment", session.get(EnvironmentEntity.class, newhealthCheck.getEnvironmentId()))
                .setInteger("maxRetryCount", newhealthCheck.getMaxRetryCount())
                .setEntity("hcType", session.get(HealthCheckTypeEntity.class, newhealthCheck.getHealthCheckTypeId())).setInteger("delInd", delInd)
                .setInteger("hcId", newhealthCheck.getHealthCheckId());
        query.executeUpdate();
        tx.commit();
        session.close();

        log.info("Updated HEALTH_CHECK : " + newhealthCheck.getHealthCheckId());

        Map<Integer, List<HealthCheckParamVO>> healthCheckParamDtls = getHealthCheckParamDetails(newhealthCheck.getHealthCheckId(), false);
        // DELETE or UPDATE existing HealthCheckParam
        if (healthCheckParamDtls.get(newhealthCheck.getHealthCheckId()) != null) {
            for (HealthCheckParamVO currentHealthCheckParam : healthCheckParamDtls.get(newhealthCheck.getHealthCheckId())) {
                boolean isHealthCheckParamMatched = false;
                for (HealthCheckParamVO newHealthCheckParam : newhealthCheck.getHealthCheckParamList()) {
                    if (currentHealthCheckParam.getHealthCheckParamId() == newHealthCheckParam.getHealthCheckParamId()) {
                        updateHealthCheckParam(newHealthCheckParam);
                        isHealthCheckParamMatched = true;
                        break;
                    }
                }
                if (!isHealthCheckParamMatched) {
                    deleteHealthCheckParam(currentHealthCheckParam);
                }
            }
        }
        // INSERT newly added HealthCheckParam
        for (HealthCheckParamVO newHealthCheckParam : newhealthCheck.getHealthCheckParamList()) {
            if (newHealthCheckParam.getHealthCheckParamId() == 0) {
                addNewHealthCheckParam(newhealthCheck.getHealthCheckId(), newHealthCheckParam);
            }
        }
    }

    /**
     * GetHealthCheckDetails for the given environmentId + regionId + componentId combination.
     * 
     * @param environmentId
     * @param regionId
     * @param componentId
     * @return
     */
    private List<HealthCheckVO> getHealthCheckDetails(final int environmentId, final int regionId, final int componentId) {
        Session session = sessionFactory.openSession();
        Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class);
        healthCheckCriteria.createCriteria("region", "r");
        healthCheckCriteria.add(Restrictions.eq("r.regionId", regionId));
        healthCheckCriteria.createCriteria("environment", "e");
        healthCheckCriteria.add(Restrictions.eq("e.environmentId", environmentId));
        healthCheckCriteria.createCriteria("component", "c");
        healthCheckCriteria.add(Restrictions.eq("c.componentId", componentId));
        @SuppressWarnings("unchecked")
        List<HealthCheckVO> healthCheckList = makeHealthCheckVO(healthCheckCriteria.list());
        session.close();
        return healthCheckList;
    }

    /**
     * Query the HEALTH_CHECK table and returns the List of HealthCheckVO
     * 
     * @return
     */
    private List<HealthCheckVO> getInfraHealthCheckDetails() {
        Session session = sessionFactory.openSession();
        Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class);
        healthCheckCriteria.createCriteria("component.parentComponent", "parComp");
        healthCheckCriteria.createCriteria("component.componentType", "ct");
        healthCheckCriteria.add(Restrictions.eq("ct.componentTypeId", ComponentType.INFRA.componentTypeId()));
        healthCheckCriteria.add(Restrictions.eq("parComp.manual", "Y"));
        @SuppressWarnings("unchecked")
        List<HealthCheckEntity> healthCheckList = removeSkippableHealthChecks(healthCheckCriteria.list());
        session.close();
        return makeHealthCheckVO(healthCheckList);
    }

    /**
     * Function to remove skippable component heath check from list
     */
    public List<HealthCheckEntity> removeSkippableHealthChecks(List<HealthCheckEntity> checkEntities) {
        List<HealthCheckEntity> removeList = new ArrayList<HealthCheckEntity>();
        for (HealthCheckEntity entity : checkEntities) {
            ComponentEntity entityComponent = entity.getComponent();
            if (skipableInfraComponents.contains(entityComponent.getParentComponent().getComponentName())
                    || skipableInfraComponents.contains(entityComponent.getComponentName())) {
                removeList.add(entity);
            }
        }
        checkEntities.removeAll(removeList);
        return checkEntities;
    }

    /**
     * Function to make a list HealthCheckVOs from a list of HealthCheckEntities
     * 
     * @param rs
     * @param healthCheckDtls
     * @throws SQLException
     */
    private static List<HealthCheckVO> makeHealthCheckVO(List<HealthCheckEntity> healthCheckEntities) {
    	log.info("HCE: " + healthCheckEntities);
        List<HealthCheckVO> healthCheckVOList = new ArrayList<HealthCheckVO>();
        for (HealthCheckEntity entity : healthCheckEntities) {
            HealthCheckVO healthCheck = new HealthCheckVO();
            healthCheck.setHealthCheckId(entity.getHealthCheckId());
            healthCheck.setComponentId(entity.getComponent().getComponentId());
            healthCheck.setComponentName(entity.getComponent().getComponentName());
            healthCheck.setComponentType(entity.getComponent().getComponentType().getComponentTypeName());
            healthCheck.setParentComponentId(entity.getComponent().getParentComponent().getComponentId());
            healthCheck.setParentComponentName(entity.getComponent().getParentComponent().getComponentName());
            healthCheck.setPlatform(entity.getComponent().getPlatform());
            healthCheck.setRegionId(entity.getRegion().getRegionId());
            healthCheck.setRegionName(entity.getRegion().getRegionName());
            healthCheck.setEnvironmentId(entity.getEnvironment().getEnvironmentId());
            healthCheck.setEnvironmentName(entity.getEnvironment().getEnvironmentName());
            healthCheck.setHealthCheckTypeId(entity.getHealthCheckType().getHealthCheckTypeId());
            healthCheck.setHealthCheckTypeName(entity.getHealthCheckType().getHealthCheckTypeName());
            healthCheck.setMaxRetryCount(entity.getMaxRetryCount());
            healthCheck.setDelInd(entity.getDelInd() == 0 ? false : true);
            healthCheckVOList.add(healthCheck);
        }
        return healthCheckVOList;
    }

    /**
     * Query the HEALTH_CHECK_PARAM table and returns the Map of Integer >> HealthCheckParamVO.
     * 
     * @return
     */
    private Map<Integer, List<HealthCheckParamVO>> getHealthCheckParamDetails(int healthCheckId, boolean isManual) {
        Map<Integer, List<HealthCheckParamVO>> healthCheckParamDtls = new HashMap<Integer, List<HealthCheckParamVO>>();
        Session session = sessionFactory.openSession();
        Criteria healthCheckCriteria = session.createCriteria(HealthCheckParamEntity.class);
        healthCheckCriteria.createCriteria("healthCheck", "hc");
        if (healthCheckId != 0) {
            healthCheckCriteria.createCriteria("healthCheck", "hc");
            healthCheckCriteria.add(Restrictions.eq("hc.healthCheckId", healthCheckId));
        }
        if(isManual){
        	healthCheckCriteria.createCriteria("hc.component.parentComponent", "parComp");
            healthCheckCriteria.add(Restrictions.eq("parComp.manual", "Y"));
        }
        @SuppressWarnings("unchecked")
        List<HealthCheckParamEntity> results = healthCheckCriteria.list();
        for (HealthCheckParamEntity healthCheckParamEntity : results) {
            int hcId = healthCheckParamEntity.getHealthCheck().getHealthCheckId();
            List<HealthCheckParamVO> healthCheckParamList = healthCheckParamDtls.computeIfAbsent(hcId, k -> {
            	List<HealthCheckParamVO> newParam = new ArrayList<HealthCheckParamVO>();
            	healthCheckParamDtls.put(hcId, newParam);
            	return newParam;
            });
            HealthCheckParamVO healthCheckParam = new HealthCheckParamVO();
            healthCheckParam.setHealthCheckParamId(healthCheckParamEntity.getHealthCheckParamId());
            healthCheckParam.setHealthCheckParamKey(healthCheckParamEntity.getHealthCheckParamKey());
            healthCheckParam.setHealthCheckParamValue(healthCheckParamEntity.getHealthCheckParamVal());
            healthCheckParamList.add(healthCheckParam);
        }
        session.close();
        return healthCheckParamDtls;
    }
}
