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

import java.util.List;

import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.AppLookUpEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;

/**
 * Operation on component table. Implements AppLookUpDao interface
 */
@Repository
@PropertySource("classpath:application.properties")
@Table(name = "app_lookup")
public class AppLookUpDaoImpl implements AppLookUpDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Value("${adminpage.skip.infra.component.name}")
    private String skipInfraCompNames;

    /**
     * Add a new Component to DB
     */
    public void saveAppFullName(AppFullName appFullName) {
        if (appFullName.getComponentId() == 0 || appFullName.getComponentFullName() == null || appFullName.getComponentFullName().isEmpty()) {
            throw new IllegalArgumentException("Component Full Name and ID cannot be null");
        }
        AppLookUpEntity appLookUpEntity = new AppLookUpEntity();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        appLookUpEntity.setComponent(getAppComponentId(appFullName.getComponentId()));
        appLookUpEntity.setComponentFullName(appFullName.getComponentFullName());
        session.save(appLookUpEntity);
        tx.commit();
        session.close();
    }

    /**
     * Get All the APP full name from app_lookup table
     */
    @Override
    public List<AppLookUpEntity> getAppFullNameWithAppId() {

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(AppLookUpEntity.class);
        @SuppressWarnings("unchecked")
        List<AppLookUpEntity> appLookupList = criteria.list();
        session.close();
        return appLookupList;
    }

    /**
     * Check if Component id exist in Component Table before inserting it in App lookup Table
     */
    @Override
    public ComponentEntity getAppComponentId(int componentId) {

        ComponentEntity result = new ComponentEntity();
        Session session = sessionFactory.openSession();
        Criteria compCriteria = session.createCriteria(ComponentEntity.class);
        compCriteria.add(Restrictions.eq("componentId", componentId));
        @SuppressWarnings("unchecked")
        List<ComponentEntity> results = compCriteria.list();
        session.close();
        result.setComponentId(results.get(0).getComponentId());
        return result;

    }

    /**
     * Update the given Component details to DB
     */

    public void editAppFullName(AppFullName appFullName) {
       if (appFullName.getComponentId() == 0 || appFullName.getComponentFullName() == null || appFullName.getComponentFullName().isEmpty()) {
            throw new IllegalArgumentException("Component Full Name and ID cannot be null");
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = null;
        query = session.createQuery(HQLConstants.UPDATE_APP_LOOKUP_FULLNAME).setString("component_full_name", appFullName.getComponentFullName())
                .setInteger("component_id", appFullName.getComponentId());
        query.executeUpdate();

        tx.commit();
        session.close();
    }
    
    @Override
    public void deleteAppFullName(AppFullName appFullName) {
    	Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.DELETE_APP_LOOK_UP).setInteger("applookupId", appFullName.getAppId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

}
