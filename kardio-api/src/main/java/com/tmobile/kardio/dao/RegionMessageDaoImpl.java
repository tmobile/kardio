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

import com.tmobile.kardio.bean.ComponentMessages;
import com.tmobile.kardio.bean.Messages;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.ComponentMessageEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;
import com.tmobile.kardio.db.entity.RegionEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Updates the Message for component Implements RegionMessageDao
 */
@Repository
public class RegionMessageDaoImpl implements RegionMessageDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EnvironmentDao environmentDao;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	private static final int HOURS_TO_ADD = -24;

    private static Date getPreviousDayDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, HOURS_TO_ADD);
        return cal.getTime();
    }

    /**
     * Get the Component message for the componentId for the given environmentName and regionName
     */
    public List<Messages> getCompRegionMessage(String environmentName, String componentId, String regionName) {

        if (componentId == null || environmentName == null || regionName == null) {
            throw new IllegalArgumentException("Request Method parameter cannot be null");
        }
        int regionId = getRegionId(regionName);
        int environmentId = environmentDao.getEnironmentIdFromName(environmentName);
        Long compId = Long.parseLong(componentId);
        Set<Integer> childCompIdList = getAllChildWithStatusChange(compId.intValue(), regionId, environmentId, null);
        childCompIdList.add(compId.intValue());

        List<Messages> msgList = new ArrayList<Messages>();

        Session session = sessionFactory.openSession();
        Query query = session.createQuery(HQLConstants.QUERY_COMP_FAILURE_LOG);
        Date previousDay = getPreviousDayDate();
        for (Integer childCompId : childCompIdList) {
            query.setInteger("envId", environmentId).setInteger("regId", regionId).setInteger("compId", childCompId)
                    .setDate("prevoiusDay", previousDay);
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.list();
            msgList.addAll(makeMessage(resultList));
        }
        query = session.createQuery(HQLConstants.QUERY_COMP_MESSAGE);
        query.setInteger("envId", environmentId).setInteger("regId", regionId).setParameterList("compIds", childCompIdList)
                .setDate("prevoiusDay", previousDay);
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.list();
        msgList.addAll(makeMessage(resultList));
        session.close();
        return msgList;
    }

    /**
     * Convert the result set to List of Messages
     * 
     * @param resultList
     * @return
     */
    private List<Messages> makeMessage(List<Object[]> resultList) {
        List<Messages> messageList = new ArrayList<Messages>();
        for (Object[] resultObj : resultList) {
            Messages msg = new Messages();
            msg.setComponentName((String) resultObj[0]);
            msg.setMessageId((Integer) resultObj[1]);
            msg.setMessageDate(sdf.format((java.sql.Timestamp) resultObj[2]));
            msg.setMessage((String) resultObj[3]);
            msg.setUserId((String) resultObj[4]);
            messageList.add(msg);
        }
        return messageList;
    }

    /**
     * Gets the list of messages related to a component for a particular date
     */
    @Override
    public List<Messages> getCompRegionMessage(final String environmentName, final String componentId, final String regionName,
            final String histDateString) {

        if (componentId == null || environmentName == null || regionName == null || histDateString == null) {
            throw new IllegalArgumentException("Request Method parameter cannot be null");
        }
        Calendar histDate = Calendar.getInstance();
        try {
            histDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(histDateString));
            histDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException e) {
            throw new IllegalArgumentException("History Date must be of yyyy-MM-dd format");
        }

        int regionId = getRegionId(regionName);
        int environmentId = environmentDao.getEnironmentIdFromName(environmentName);
        Long compId = Long.parseLong(componentId);
        Set<Integer> childCompIdList = getAllChildWithStatusChange(compId.intValue(), regionId, environmentId, histDate);
        childCompIdList.add(compId.intValue());

        List<Messages> msgList = new ArrayList<Messages>();
        histDate.add(Calendar.SECOND, ((24 * 60 * 60) -1) );//Getting messages till 11:59:59 pm of this day.
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(HQLConstants.QUERY_COMP_FAILURE_LOG_HIS);
        for (Integer childCompId : childCompIdList) {
            query.setInteger("envId", environmentId).setInteger("regId", regionId).setInteger("compId", childCompId)
                    .setString("hisDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(histDate.getTime()) );
            @SuppressWarnings("unchecked")
            List<Messages> resultList = makeMessage(query.list());
            List<Messages> messageList = new ArrayList<Messages>();
            Messages previousDateMessage = null;
            for (Messages resultMsg : resultList) {
                if (resultMsg.getMessageDate().startsWith(histDateString)) {
                    messageList.add(resultMsg);
                } else if (previousDateMessage == null) {
                    previousDateMessage = resultMsg;
                }
            }

            if (messageList.size() > 0) {
                msgList.addAll(messageList);
            } else if (previousDateMessage != null) {
                msgList.add(previousDateMessage);
            }
        }
        query = session.createQuery(HQLConstants.QUERY_COMP_MESSAGE_HIS);
        query.setInteger("envId", environmentId).setInteger("regId", regionId).setParameterList("compIds", childCompIdList)
                .setCalendarDate("hisDate", histDate);
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.list();
        msgList.addAll(makeMessage(resultList));
        session.close();

        return msgList;
    }

    /**
     * Returns the list of all the child component_id for the given parent_component_id.
     * 
     * @param componentId
     * @return
     */
    @SuppressWarnings("unchecked")
    private Set<Integer> getAllChildWithStatusChange(int componentId, int regionId, int environmentId, Calendar histDate) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(HQLConstants.QUERY_GET_CHILDREN_WITH_ERROR).setInteger("envId", environmentId)
                .setInteger("regId", regionId).setInteger("parentCompId", componentId);
        if(histDate == null){
        	query.setDate("statusChangeTime", getPreviousDayDate());
        }else{
        	query.setDate("statusChangeTime", histDate.getTime());
        }

        Set<Integer> childrenList = new HashSet<Integer>();
        for (ComponentEntity compObj : (List<ComponentEntity>) query.list()) {
            childrenList.add(compObj.getComponentId());
        }
        session.close();
        return childrenList;
    }

    /**
     * Insert data in ComponentMessages.
     */
    @Override
    public void saveCompMessage(ComponentMessages compMessage) {
        final int envId = environmentDao.getEnironmentIdFromName(compMessage.getEnvironment());
        final int regionId = getRegionId(compMessage.getRegion());
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        ComponentMessageEntity cme = new ComponentMessageEntity();
        cme.setMessage(compMessage.getMessage());
        cme.setUserId(compMessage.getUserid());
        cme.setUserName(compMessage.getUsername());
        cme.setMessageDate(new java.sql.Timestamp(System.currentTimeMillis()));
        ComponentEntity comp = new ComponentEntity();
        comp.setComponentId(Integer.parseInt(compMessage.getComponentid()));
        cme.setComponent(comp);
        EnvironmentEntity env = new EnvironmentEntity();
        env.setEnvironmentId(envId);
        cme.setEnvironment(env);
        RegionEntity reg = new RegionEntity();
        reg.setRegionId(regionId);
        cme.setRegion(reg);
        session.save(cme);
        tx.commit();
        session.close();
    }

    /**
     * Get region Id from region Name.
     * 
     * @param region
     * @return
     */
    private int getRegionId(final String regionName) {
        if (regionName == null || regionName.trim().length() == 0) {
            throw new IllegalArgumentException("Region Name is not valid");
        }
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(HQLConstants.GET_REGION_ID_FROM_NAME).setString("regName", regionName);
        RegionEntity reg = (RegionEntity) query.uniqueResult();
        session.close();
        return reg.getRegionId();
    }

    /**
     * Updates the message of component_message for the given message_id.
     */
    @Override
    public void updateCompMessage(ComponentMessages compMessage) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.QUERY_UPDATE_COMP_MESSAGE).setString("msg", compMessage.getMessage())
                .setInteger("messageId", compMessage.getMessageId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

}
