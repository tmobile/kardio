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

import com.tmobile.kardio.bean.User;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.AppRoleEntity;
import com.tmobile.kardio.db.entity.AppSessionEntity;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DB operations for app_Session table Implementation of AppSessionDao
 */
@Repository
public class AppSessionDaoImpl implements AppSessionDao {

	private static Logger log = LoggerFactory.getLogger(AppSessionDaoImpl.class);

	@Autowired
    private SessionFactory sessionFactory;

    /**
	 * Function to save a new application session to the DB
	 */
    @Override
    public void saveAppSession(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User name is null");
        }
        String permission = user.getUserGroups() == null ? null : user.getUserGroups().toString();
        AppSessionEntity appSessionEntity = new AppSessionEntity();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        appSessionEntity.setAuthToken(user.getAuthToken());
        appSessionEntity.setSessionStartTime(new java.util.Date());
        appSessionEntity.setUserId(user.getUserId());
        appSessionEntity.setUserName(user.getUserName());
        appSessionEntity.setPermission(permission);
        appSessionEntity.setIsAdmin(user.isAdmin());
        session.save(appSessionEntity);
        tx.commit();
        session.close();
    }

    /**
     * Function to delete app session from database
     */
    @Override
    public void deleteAppSession(String authToken) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.DELETE_APP_SESSION).setString("authToken", authToken);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    /**
     * Gets the app session details using the give authToken
     */
    @Override
    public AppSessionEntity getAppSession(final String authToken) {
        Session session = sessionFactory.openSession();
        session.clear();
        Criteria appSessionCriteria = session.createCriteria(AppSessionEntity.class).setCacheable(false);
        appSessionCriteria.add(Restrictions.eq("authToken", authToken));
        @SuppressWarnings("unchecked")
        List<AppSessionEntity> results = appSessionCriteria.list();
        AppSessionEntity ase = null;
        if(results.size() > 0){
        	ase = results.get(0);
        }
        session.close();
        return ase;
    }

    /**
     * Function to get the list of app roles from the app_roles table
     */
    public Map<Integer, String> getAllAppRoles() {
        Map<Integer, String> listRoles = new HashMap<Integer, String>();
        Session session = sessionFactory.openSession();
        Criteria appSessionCriteria = session.createCriteria(AppRoleEntity.class);
        @SuppressWarnings("unchecked")
        List<AppRoleEntity> results = appSessionCriteria.list();
        session.close();
        for (AppRoleEntity role : results) {
            listRoles.put(role.getComponent() == null ? 0 : role.getComponent().getComponentId(), role.getAppRoleName());
        }
        return listRoles;
    }

}
