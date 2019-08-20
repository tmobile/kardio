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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.EnvironmentMessages;
import com.tmobile.kardio.bean.Region;
import com.tmobile.kardio.bean.StatusResponse;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.constants.Status;
import com.tmobile.kardio.db.entity.AppRoleEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;

/**
 * Dao class to fetch all the Components and region wise status from database Implements RegionStatusDao
 * 
 */
@Repository
public class RegionStatusDaoImpl implements RegionStatusDao {

    private static final long CONVERT_TO_HOURS = 3600000;

	private static final long RELEVENCE_TIME = 32;

	private static Logger log = LoggerFactory.getLogger(RegionStatusDaoImpl.class);

    @Autowired
    private EnvironmentDao environmentDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ComponentDao componentDao;

    /**
     * Get all the Components and region wise status, respective messages form DB 
     * 
     */
    @Override
    public StatusResponse getCompRegStatus(String environmentName) {
    	log.info("HE: " + environmentName );
        final int envId = environmentDao.getEnironmentIdFromName(environmentName);
        Map<Integer, String> appLookupMap = componentDao.getAppFullName();
        StatusResponse response = new StatusResponse();
        List<Component> allComponentStatus = getAllComponentsStatus(envId, appLookupMap);
        log.debug("All Component status size : " + allComponentStatus.size());
        List<Component> allParentList = getAllParentComponents();

        Map<Integer, List<String>> listAppRoles = getAllAppRoles();

        List<Component> infraComponentList = getParentComponentsStatus(appLookupMap, allComponentStatus, allParentList,
                ComponentType.INFRA.componentTypeName(), listAppRoles, envId);
        log.debug("Infra Component status size : " + infraComponentList.size());
        response.setInfraComponents(infraComponentList);

        List<Component> appComponentList = getParentComponentsStatus(appLookupMap, allComponentStatus, allParentList,
                ComponentType.APP.componentTypeName(), listAppRoles, envId);
        log.debug("App Component status size : " + allComponentStatus.size());
        response.setAppComponents(appComponentList);

        List<Component> apiComponentList = getApiStatus(allComponentStatus, listAppRoles);
        log.debug("Api Component status size : " + apiComponentList.size());
        response.setApiComponents(apiComponentList);

        EnvironmentMessages envMessages = getEnvironmentMessages(envId);
        response.setEnvMessages(envMessages);

        return response;
    }

    /**
     * Get the Environment Messages from Environment table.
     * 
     * @param envId
     * @return
     */
    private EnvironmentMessages getEnvironmentMessages(final int envId) {
        EnvironmentMessages envMessages = new EnvironmentMessages();
        Session session = sessionFactory.openSession();
        Criteria envMsgCriteria = session.createCriteria(EnvironmentEntity.class);
        envMsgCriteria.add(Restrictions.eq("environmentId", envId));
        @SuppressWarnings("unchecked")
        List<EnvironmentEntity> environmentEntities = envMsgCriteria.list();
        EnvironmentEntity environmentEntity = environmentEntities.get(0);
        envMessages.setAppMessage(environmentEntity.getAppMessage());
        envMessages.setInfraMessage(environmentEntity.getInfraMessage());
        envMessages.setGeneralMessage(environmentEntity.getGeneralMessage());
        envMessages.setCounterMessage(environmentEntity.getCounterMessage());
        session.close();
        return envMessages;
    }

    /**
     * Get list of all the parents.
     * 
     * @return
     */
    public List<Component> getAllParentComponents() {
        List<Component> parentComponentList = new ArrayList<Component>();
        Session session = sessionFactory.openSession();
        Criteria componentCriteria = session.createCriteria(ComponentEntity.class);
        componentCriteria.add(Restrictions.eq("delInd", 0));
        componentCriteria.add(Restrictions.isNull("parentComponent"));
        @SuppressWarnings("unchecked")
        List<ComponentEntity> componentEntities = componentCriteria.list();
        for (ComponentEntity entity : componentEntities) {
            Component parent = new Component();
            parent.setComponentName(entity.getComponentName());
            parent.setComponentId(entity.getComponentId());
            parent.setComponentType(entity.getComponentType().getComponentTypeName());
            parent.setPlatform(entity.getPlatform());
            parentComponentList.add(parent);
        }
        session.close();
        return parentComponentList;
    }

    /**
     * Make the parent
     * 
     * @param allComponentList
     * @param componentType
     */
    private List<Component> getParentComponentsStatus(Map<Integer, String> appLookupMap, List<Component> allComponentStatus,
            List<Component> allParentList, String componentType, Map<Integer, List<String>> listAppRoles, int envId) {
        Map<Integer, Component> allParentComponentsStatusMap = new HashMap<Integer, Component>();
        for (Component compStatus : allComponentStatus) {
            if (!compStatus.getComponentType().equalsIgnoreCase(componentType)) {
                continue;
            }
            Component parentComp = allParentComponentsStatusMap.get(compStatus.getParentComponentId());
            if (parentComp == null) {
                for (Component parent : allParentList) {
                    if (parent.getComponentId() == compStatus.getParentComponentId()) {
                        parentComp = compStatus.copy();
                        parentComp.setComponentName(appLookupMap.get(parent.getComponentId()) == null ? parent.getComponentName() : appLookupMap
                                .get(parent.getComponentId()));
                        parentComp.setComponentId(parent.getComponentId());
                        parentComp.setChildComponentName(compStatus.getComponentName());
                        parentComp.setPlatform(getParentPlatform(parent.getComponentId(), envId));
                        List<Component> childCompList = new ArrayList<Component>();
                        childCompList.add(compStatus);
                        parentComp.setChildren(childCompList);
                        if (listAppRoles != null) {
                            parentComp.setRoles(listAppRoles.get(parent.getComponentId()));
                        }
                    }
                }
                if (parentComp != null) {
                    setRecentEventFlag(parentComp);
                    allParentComponentsStatusMap.put(compStatus.getParentComponentId(), parentComp);
                }
                continue;
            }
            parentComp.getChildren().add(compStatus);
            parentComp.setChildComponentName(parentComp.getChildComponentName() + " , " + compStatus.getComponentName());
            if (compStatus.getRegion() == null) {
                continue;
            }
            setRecentEventFlag(parentComp);
        }
        return new ArrayList<Component>(updateAllParentComponentStatus(allParentComponentsStatusMap).values());
    }

    /**
     * Gets the platform of Parent Component
     * 
     * @param allParentComponentsStatusMap
     * @return
     */

    public String getParentPlatform(int parentComponentId, int envId) {
    	Session session=sessionFactory.openSession();
    	Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class);
        healthCheckCriteria.add(Restrictions.eq("delInd", 0));
        if(envId!=0) {
            healthCheckCriteria.add(Restrictions.eq("environment.environmentId", envId));
            }
    	healthCheckCriteria.createCriteria("component","comp");
    	healthCheckCriteria.add(Restrictions.eq("comp.parentComponent.componentId",parentComponentId));
    	healthCheckCriteria.setProjection(Projections.distinct(Projections.property("comp.platform")));
    	healthCheckCriteria.addOrder(Order.asc("comp.platform"));
    	List<Object[]> pformList =  healthCheckCriteria.list();
    	List<String> pform = new ArrayList<String>();
    	for(Object aRow : pformList) {
    		if(aRow!=null) {
    			pform.add(aRow.toString());
    		 
    	}
    	}
    	String platform = String.join("/", pform);
    	 session.close();
    	return platform;
    }
    
    /**
     * Sets the parent status from children.
     * 
     * @param allParentComponentsStatusMap
     * @return
     */
    private Map<Integer, Component> updateAllParentComponentStatus(Map<Integer, Component> allParentComponentsStatusMap) {
        for (Integer parentCompId : allParentComponentsStatusMap.keySet()) {
            int westDown = 0;
            int eastDown = 0;
            int westTotal = 0;
            int eastTotal = 0;
            boolean isWestRecentEvent = false;
            boolean isEastRecentEvent = false;
            Component parentComp = allParentComponentsStatusMap.get(parentCompId);
            List<Component> childComponents = parentComp.getChildren();
            if (childComponents != null) {
                for (Component childComp : childComponents) {
                    for (Region compRegion : childComp.getRegion()) {
                        if (compRegion.getRegionName().equalsIgnoreCase(com.tmobile.kardio.constants.Region.WEST_REGION.getRegionDescription())) {
                            if (compRegion.getRegionStatus() !=null && !compRegion.getRegionStatus().equalsIgnoreCase(Status.SERVICE_UP.statusName())) {
                                westDown += 1;
                            }
                            westTotal += 1;
                            if (compRegion.isRecentEvent()) {
                                isWestRecentEvent = true;
                            }
                        } else if (compRegion.getRegionName().equalsIgnoreCase(com.tmobile.kardio.constants.Region.EAST_REGION.getRegionDescription())) {
                            if (compRegion.getRegionStatus() !=null && !compRegion.getRegionStatus().equalsIgnoreCase(Status.SERVICE_UP.statusName())) {
                                eastDown += 1;
                            }
                            eastTotal += 1;
                            if (compRegion.isRecentEvent()) {
                                isEastRecentEvent = true;
                            }
                        }
                    }
                }
                parentComp.getRegion().clear();
                if (westTotal > 0) {
                    Region westRegion = new Region();
                    westRegion.setRegionName(com.tmobile.kardio.constants.Region.WEST_REGION.getRegionDescription());
                    westRegion.setRecentEvent(isWestRecentEvent);
                    westRegion.setRegionStatus(getRegionSatus(westDown, westTotal));
                    parentComp.getRegion().add(westRegion);
                }
                if (eastTotal > 0) {
                    Region eastRegion = new Region();
                    eastRegion.setRegionName(com.tmobile.kardio.constants.Region.EAST_REGION.getRegionDescription());
                    eastRegion.setRecentEvent(isEastRecentEvent);
                    eastRegion.setRegionStatus(getRegionSatus(eastDown, eastTotal));
                    parentComp.getRegion().add(eastRegion);
                }
                if (isWestRecentEvent || isEastRecentEvent) {
                    parentComp.setRecentEvent(true);
                }
            }
        }

        return allParentComponentsStatusMap;
    }

    /**
     * Contains logic for calculating the parent status.
     * 
     * @param downCount
     * @param childTotalCount
     * @return
     */
    public static String getRegionSatus(int downCount, int childTotalCount) {
        if (downCount >= childTotalCount && childTotalCount != 0) {
            return Status.SERVICE_DISRUPTION.statusName();
        } else if (downCount < childTotalCount && downCount > 0) {
            return Status.SERVICE_DEGRADATION.statusName();
        } else if (downCount == 0 && childTotalCount != 0) {
            return Status.SERVICE_UP.statusName();
        }
        return null;
    }

    /**
     * Get list of all roles for all component_id.
     * 
     * @return
     */
    private Map<Integer, List<String>> getAllAppRoles() {
        Map<Integer, List<String>> listRoles = new HashMap<Integer, List<String>>();
        Session session = sessionFactory.openSession();
        Criteria appRoleCriteria = session.createCriteria(AppRoleEntity.class);
        appRoleCriteria.add(Restrictions.isNotNull("component"));
        @SuppressWarnings("unchecked")
        List<AppRoleEntity> appRoleEntities = appRoleCriteria.list();
        for (AppRoleEntity entity : appRoleEntities) {
            List<String> roleList = listRoles.get(entity.getComponent().getComponentId());
            if (roleList == null) {
                roleList = new ArrayList<String>();
                roleList.add(entity.getAppRoleName());
                listRoles.put(entity.getComponent().getComponentId(), roleList);
            } else {
                roleList.add(entity.getAppRoleName());
            }
        }
        session.close();
        return listRoles;
    }

    /**
     * Sets the Recent Event Flag
     * 
     * @param parentComp
     */
    private void setRecentEventFlag(Component parentComp) {
        for (Region reg : parentComp.getRegion()) {
            if (reg.getRegionStatus() != null && !reg.getRegionStatus().equalsIgnoreCase(Status.SERVICE_UP.statusName())) {
                reg.setRecentEvent(true);
                parentComp.setRecentEvent(true);
            }
        }
    }

    /**
     * Get Api Component Status
     * 
     * @param allComponentList
     * @return
     */
    private List<Component> getApiStatus(List<Component> allComponentList, Map<Integer, List<String>> listAppRoles) {
        List<Component> apiComponentList = new ArrayList<Component>();
        for (Component comp : allComponentList) {
            if (comp.getComponentType() != null && comp.getComponentType().equalsIgnoreCase(ComponentType.APP.componentTypeName())) {
                apiComponentList.add(comp);
                if (listAppRoles != null) {
                    comp.setRoles(listAppRoles.get(comp.getParentComponentId()));
                    if (listAppRoles.get(comp.getComponentId()) != null) {
                        if (listAppRoles.get(comp.getParentComponentId()) == null) {
                            comp.setRoles(listAppRoles.get(comp.getParentComponentId()));
                        } else {
                            comp.getRoles().addAll(listAppRoles.get(comp.getParentComponentId()));
                        }
                    }
                }
            }
        }
        return apiComponentList;
    }

    /**
     * Get Api Components
     * 
     * @param envId
     * @return
     */
    private List<Component> getAllComponentsStatus(final int envId, Map<Integer, String> appLookupMap) {
       log.info("HE: EnvID: " + envId);
       
        List<Component> apiComponentList = new ArrayList<Component>();
        Session session = sessionFactory.openSession();
        Criteria healthCheckCriteria = session.createCriteria(HealthCheckEntity.class);
        healthCheckCriteria.add(Restrictions.eq("delInd", 0));
        healthCheckCriteria.add(Restrictions.eq("environment.environmentId", envId));
        healthCheckCriteria.createCriteria("region", "r");
        healthCheckCriteria.add(Restrictions.eq("r.regionLock", 0));
        healthCheckCriteria.createCriteria("component", "c");
        healthCheckCriteria.add(Restrictions.eq("c.delInd", 0));
        @SuppressWarnings("unchecked")
        List<HealthCheckEntity> healthCheckEntities = healthCheckCriteria.list();
        log.info("HE Size: " + healthCheckEntities.size());
        session.close();
        Map<Integer, Component> map = new HashMap<Integer, Component>();
        for (HealthCheckEntity entity : healthCheckEntities) {
            ComponentEntity componentEntity = entity.getComponent();
            int id = componentEntity.getComponentId();
            Component component = map.get(id);
            if (component == null) {
                component = new Component();
                component.setComponentName(componentEntity.getComponentName());
                component.setComponentId(componentEntity.getComponentId());
                component.setPlatform(componentEntity.getPlatform());
                if (componentEntity.getParentComponent() != null) {
                    component.setParentComponentId(componentEntity.getParentComponent().getComponentId());
                    component.setParentComponentName(appLookupMap.get(componentEntity.getParentComponent().getComponentId()) == null ? componentEntity.getParentComponent().getComponentName() : appLookupMap
                            .get(componentEntity.getParentComponent().getComponentId()));
                }
                component.setComponentType(componentEntity.getComponentType().getComponentTypeName());
                if (entity.getLastStatusChange() != null) {
                    component.setComponentDate(new java.sql.Timestamp(entity.getLastStatusChange().getTime()));
                }
                apiComponentList.add(component);
            }
            List<Region> regList = component.getRegion();
            if (regList == null) {
                regList = new ArrayList<Region>();
            }
            Region reg = new Region();
            reg.setRegionName(entity.getRegion().getRegionName());
            if(entity.getCurrentStatus() != null){
            	reg.setRegionStatus(entity.getCurrentStatus().getStatusName());
            }
            boolean recentEvent = false;
            if (entity.getLastStatusChange() != null) {
                long diff = Math.abs(new Date().getTime() - entity.getLastStatusChange().getTime());
                long diffHours = diff / CONVERT_TO_HOURS;
                if (diffHours < RELEVENCE_TIME) {
                    recentEvent = true;
                }
            }
            reg.setRecentEvent(recentEvent);
            if (recentEvent) {
                component.setRecentEvent(recentEvent);
            }
            regList.add(reg);
            component.setRegion(regList);
            if (map.get(id) != null && entity.getLastStatusChange() != null 
        		&& component.getComponentDate() != null
                && entity.getLastStatusChange().after(component.getComponentDate())) {
            	
                component.setComponentDate(new java.sql.Timestamp(entity.getLastStatusChange().getTime()));
            }
            map.put(id, component);
            setRecentEventFlag(component);
        }
        return apiComponentList;
    }

    /**
     * Loads the given messages to environment table
     */
    public void loadMessages(String environmentName, String messageType, String message) throws InstantiationException {
        final int envId = environmentDao.getEnironmentIdFromName(environmentName);
        String updateQuery = null;
        if (messageType.equalsIgnoreCase(Constants.MESSAGE_TYPE_APP)) {
            updateQuery = HQLConstants.UPDATE_APP_MESSAGE;
        } else if (messageType.equalsIgnoreCase(Constants.MESSAGE_TYPE_INFRA)) {
            updateQuery = HQLConstants.UPDATE_INFRA_MESSAGE;
        } else if (messageType.equalsIgnoreCase(Constants.MESSAGE_TYPE_GENERAL)) {
            updateQuery = HQLConstants.UPDATE_GENERAL_MESSAGE;
        } else if (messageType.equalsIgnoreCase(Constants.MESSAGE_TYPE_COUNTER)) {
            updateQuery = HQLConstants.UPDATE_COUNTER_MESSAGE;
        }
        else {
            throw new IllegalArgumentException("Invalid value for Message Type : " + messageType);
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(updateQuery).setInteger("envId", envId).setString("message", message);
        query.executeUpdate();
        tx.commit();
        session.close();
    }
}
