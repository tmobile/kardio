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
package com.tmobile.kardio.dao;

import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.ComponentHistory;
import com.tmobile.kardio.bean.HistoryResponse;
import com.tmobile.kardio.bean.StatusHistory;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.constants.Status;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.DaillyCompStatusEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * History page functionality. Implements RegionHistoryDao
 */
@Repository
public class RegionHistoryDaoImpl implements RegionHistoryDao {
    private static final int DAYS_TO_ADD = -9;

	private static Logger log = LoggerFactory.getLogger(RegionHistoryDaoImpl.class);

    @Autowired
    private EnvironmentDao environmentDao;

    @Autowired
    private RegionStatusDao regStatusDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ComponentDao componentDao;

    /**
     * Get the seven days History of all the components in the given Environment
     * @param environment
     * @return HistoryResponse
     * @throws ParseException 
     */
    @Override
    public HistoryResponse getRegionStatusHistory(String environment) throws ParseException {
        final int envId = environmentDao.getEnironmentIdFromName(environment);

        List<ComponentHistory> allHistory = getAllStatusHistory(envId);
        log.debug("All Component history size : " + allHistory.size());
        List<Component> allParentList = regStatusDao.getAllParentComponents();
        Map<Integer, String> appLookupMap = componentDao.getAppFullName();
        HistoryResponse response = new HistoryResponse();

        List<ComponentHistory> infraHistory = getParentHistory(appLookupMap, allHistory, allParentList, ComponentType.INFRA.componentTypeName(), envId);
        log.debug("Infra History size : " + infraHistory.size());
        response.setInfraHistory(infraHistory);

        List<ComponentHistory> appHistory = getParentHistory(appLookupMap, allHistory, allParentList, ComponentType.APP.componentTypeName(), envId);
        log.debug("App History size : " + allHistory.size());
        response.setAppHistory(appHistory);

        List<ComponentHistory> apiHistory = getApiHistory(allHistory);
        response.setApiHistory(apiHistory);

        return response;
    }

    /**
     * Get all parent component History
     * @param allHistory
     * @param allParentList
     * @param app
     * @return
     */
    private List<ComponentHistory> getParentHistory(Map<Integer, String> appLookupMap, List<ComponentHistory> allHistory,
            List<Component> allParentList, String componentType, int envId) {
        Map<Integer, ComponentHistory> allParentHistoryMap = new HashMap<Integer, ComponentHistory>();

        for (ComponentHistory compHistory : allHistory) {
            if (!compHistory.getComponentType().equalsIgnoreCase(componentType)) {
                continue;
            }
            ComponentHistory parentComp = allParentHistoryMap.get(compHistory.getParentComponentId());
            if (parentComp == null) {
                for (Component parent : allParentList) {
                    if (parent.getComponentId() == compHistory.getParentComponentId()) {
                        parentComp = compHistory.copy();
                        parentComp.setComponentName(appLookupMap.get(parent.getComponentId()) == null ? parent.getComponentName() : appLookupMap
                                .get(parent.getComponentId()));
                        parentComp.setComponentId(parent.getComponentId());
                        parentComp.setPlatform(regStatusDao.getParentPlatform(parent.getComponentId(), envId));//History tab code change
                        parentComp.setChildComponentName(compHistory.getComponentName());

                        // For Staus of parent
                        for (StatusHistory parentStatusHistory : parentComp.getStatusHistory()) {
                            List<StatusHistory> childStatusHistoryList = new ArrayList<StatusHistory>();
                            childStatusHistoryList.add(parentStatusHistory);
                            parentStatusHistory.setChildren(childStatusHistoryList);
                        }
                    }
                }
                if (parentComp != null) {
                    allParentHistoryMap.put(compHistory.getParentComponentId(), parentComp);
                }
                continue;
            }
            parentComp.setChildComponentName(parentComp.getChildComponentName() + " , " + compHistory.getComponentName());
            if (compHistory.getStatusHistory() == null) {
                continue;
            }
            for (StatusHistory statHisChild : compHistory.getStatusHistory()) {
                boolean isStatusHistPresentInParent = false;
                for (StatusHistory statHisParent : parentComp.getStatusHistory()) {
                    if (statHisParent.getStatusTime().equalsIgnoreCase(statHisChild.getStatusTime())) {
                        isStatusHistPresentInParent = true;
                        statHisParent.getChildren().add(statHisChild);
                        if (statHisParent.getPercentageUpTimeEast() == null
                        		|| (statHisChild.getPercentageUpTimeEast() != null
                                        && statHisChild.getPercentageUpTimeEast().floatValue() > statHisParent.getPercentageUpTimeEast().floatValue())) {
                            statHisParent.setPercentageUpTimeEast(statHisChild.getPercentageUpTimeEast());
                        }
                        if (statHisParent.getPercentageUpTimeWest() == null
                        		|| (statHisChild.getPercentageUpTimeWest() != null
                                        && statHisChild.getPercentageUpTimeWest().floatValue() > statHisParent.getPercentageUpTimeWest().floatValue()) ) {
                            statHisParent.setPercentageUpTimeWest(statHisChild.getPercentageUpTimeWest());
                        }
                        break;
                    }
                }
                if (!isStatusHistPresentInParent) {
                    List<StatusHistory> chldStsLst = new ArrayList<StatusHistory>();
                    chldStsLst.add(statHisChild);
                    statHisChild.setChildren(chldStsLst);
                    parentComp.getStatusHistory().add(statHisChild);
                }
            }
        }
        updateAllParentComponentStatus(allParentHistoryMap);
        return new ArrayList<ComponentHistory>(allParentHistoryMap.values());
    }

    /**
     * Sets the parent status from children.
     * 
     * @param allParentComponentsStatusMap
     * @return
     */
    private Map<Integer, ComponentHistory> updateAllParentComponentStatus(Map<Integer, ComponentHistory> allParentHistoryMap) {
        for (Integer parentCompId : allParentHistoryMap.keySet()) {
            ComponentHistory parentComp = allParentHistoryMap.get(parentCompId);
            for (StatusHistory parentStatusHistory : parentComp.getStatusHistory()) {
                List<StatusHistory> childStatusHistoryList = parentStatusHistory.getChildren();
                if (childStatusHistoryList != null && childStatusHistoryList.size() >= 0) {
                    int westDown = 0;
                    int eastDown = 0;
                    int westTotal = 0;
                    int eastTotal = 0;
                    for (StatusHistory childStatusHistory : childStatusHistoryList) {
                        if (childStatusHistory.getStatusEast() != null
                                && !childStatusHistory.getStatusEast().equalsIgnoreCase(Status.SERVICE_UP.statusName())) {
                            eastDown += 1;
                        }
                        if (childStatusHistory.getStatusWest() != null
                                && !childStatusHistory.getStatusWest().equalsIgnoreCase(Status.SERVICE_UP.statusName())) {
                            westDown += 1;
                        }
                        if (childStatusHistory.getStatusEast() != null) {
                            eastTotal += 1;
                        }
                        if (childStatusHistory.getStatusWest() != null) {
                            westTotal += 1;
                        }
                    }
                    parentStatusHistory.setStatusWest(RegionStatusDaoImpl.getRegionSatus(westDown, westTotal));
                    parentStatusHistory.setStatusEast(RegionStatusDaoImpl.getRegionSatus(eastDown, eastTotal));
                }
            }
        }
        return allParentHistoryMap;
    }

    /**
     * Get API History.
     * 
     * @param allHistory
     * @return
     */
    private List<ComponentHistory> getApiHistory(List<ComponentHistory> allHistory) {
        List<ComponentHistory> apiHistory = new ArrayList<ComponentHistory>();
        for (ComponentHistory compHist : allHistory) {
            if (compHist.getComponentType() != null && compHist.getComponentType().equalsIgnoreCase(ComponentType.APP.componentTypeName())) {
                apiHistory.add(compHist);
            }
        }
        return apiHistory;
    }

    /**
     * Get all the status history.
     * 
     * @param environment
     * @return
     * @throws ParseException 
     */
    private List<ComponentHistory> getAllStatusHistory(final int envId) throws ParseException {
    	//String sqlWhere = "status_date > DATE_SUB(NOW(), INTERVAL 9 DAY)";
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Session session = sessionFactory.openSession();
        Criteria statusHistoryCriteria = session.createCriteria(DaillyCompStatusEntity.class);
        final Calendar previousWeek = Calendar.getInstance();
        previousWeek.add(Calendar.DAY_OF_MONTH, DAYS_TO_ADD);
        statusHistoryCriteria.add(Restrictions.gt("statusDate", sdf.parse(sdf.format(previousWeek.getTime()))));
        //statusHistoryCriteria.add(Restrictions.sqlRestriction(sqlWhere));
        statusHistoryCriteria.createCriteria("healthCheck", "hc");
        statusHistoryCriteria.add(Restrictions.eq("hc.delInd", 0));
        statusHistoryCriteria.add(Restrictions.eq("hc.environment.environmentId", envId));
        statusHistoryCriteria.createCriteria("hc.component", "c");
        statusHistoryCriteria.add(Restrictions.eq("c.delInd", 0));
        @SuppressWarnings("unchecked")
        List<DaillyCompStatusEntity> compStatusEntities = statusHistoryCriteria.list();
        session.close();
        Map<Integer, ComponentHistory> componentHistMap = new HashMap<Integer, ComponentHistory>();
        Map<Integer, Map<Date, StatusHistory>> compIdStatDateList = new HashMap<Integer, Map<Date, StatusHistory>>();
        for (DaillyCompStatusEntity entity : compStatusEntities) {
            ComponentEntity component = entity.getHealthCheck().getComponent();
            int id = component.getComponentId();
            ComponentHistory componentHist = componentHistMap.get(id);
            Map<Date, StatusHistory> compHistoryMap = compIdStatDateList.get(id);
            if (componentHist == null) {
                componentHist = new ComponentHistory();
                componentHist.setComponentName(component.getComponentName());
                componentHist.setComponentId(id);
                componentHist.setPlatform(component.getPlatform());//code chnge for history tab
                componentHist.setComponentType(component.getComponentType().getComponentTypeName());
                componentHist.setParentComponentId(component.getParentComponent().getComponentId());
                componentHist.setParentComponentName(component.getParentComponent().getComponentName());
                componentHistMap.put(id, componentHist);
            }
            List<StatusHistory> stsHistoryList = componentHist.getStatusHistory();
            if (stsHistoryList == null) {
                stsHistoryList = new ArrayList<StatusHistory>();
                componentHist.setStatusHistory(stsHistoryList);
            }

            if (compHistoryMap == null) {
                compHistoryMap = new HashMap<Date, StatusHistory>();
                compIdStatDateList.put(id, compHistoryMap);
            }

            if (compHistoryMap.containsKey(entity.getStatusDate())) {
                StatusHistory statusHistory = compHistoryMap.get(entity.getStatusDate());
                updateStatusHistoryObj(sdf, entity, statusHistory);
            } else {
                StatusHistory statusHistory = new StatusHistory();
                updateStatusHistoryObj(sdf, entity, statusHistory);
                stsHistoryList.add(statusHistory);
                compHistoryMap.put(entity.getStatusDate(), statusHistory);
            }
        }
        List<ComponentHistory> componentList = new ArrayList<ComponentHistory>(componentHistMap.values());

        return componentList;

    }

	private void updateStatusHistoryObj(final SimpleDateFormat sdf, DaillyCompStatusEntity entity,
			StatusHistory statusHistory) {
		if (entity.getHealthCheck().getRegion().getRegionId() == com.tmobile.kardio.constants.Region.WEST_REGION.getRegionId()) {
		    statusHistory.setPercentageUpTimeWest(entity.getPercentageUpTime());
		    statusHistory.setStatusTime(sdf.format(entity.getStatusDate()));
		    statusHistory.setStatusWest(entity.getStatus().getStatusName());
		} else {
		    statusHistory.setPercentageUpTimeEast(entity.getPercentageUpTime());
		    statusHistory.setStatusTime(sdf.format(entity.getStatusDate()));
		    statusHistory.setStatusEast(entity.getStatus().getStatusName());
		}
	}

}
