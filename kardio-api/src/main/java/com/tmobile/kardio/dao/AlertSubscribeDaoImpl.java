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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.db.entity.AlertSubscriptionEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.EnvironmentEntity;

/**
 * Implementation of AlertSubscribeDao to access data of alertSubscription table
 */
@Repository
public class AlertSubscribeDaoImpl implements AlertSubscribeDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Function to save the subscription to the DB
     */
    @Override
    public void saveSubscription(Subscription subscription, int subsType, int validationLevel, boolean isGlobalSubscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription is null");
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        AlertSubscriptionEntity ase = new AlertSubscriptionEntity();
        if (!isGlobalSubscription) {
            if (subscription.getComponentId() == 0) {
                throw new IllegalArgumentException("ComponentId is 0");
            }
            ComponentEntity component = new ComponentEntity();
            component.setComponentId(subscription.getComponentId());
            ase.setComponent(component);
        }
        ase.setSubscriptionVal(subscription.getSubsciptionVal());
        ase.setAuthToken(subscription.getAuthToken());
        ase.setSubscriptionType(subsType);
        ase.setValidationLevel(validationLevel);
        EnvironmentEntity env = new EnvironmentEntity();
        env.setEnvironmentId(subscription.getEnvironmentId());
        ase.setEnvironment(env);
        if (isGlobalSubscription) {
            ase.setGlobalComponentTypeId(subscription.getGlobalSubscriptionTypeId());
        }
        ase.setPlatform(subscription.getPlatform());
        session.save(ase);
        tx.commit();
        session.close();
    }

    /**
     * Function to delete the subscription from the DB
     */
    @Override
    public void deleteSububscription(String authToken) {
        if (authToken == null) {
            throw new IllegalArgumentException("authToken is null");
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.add(Restrictions.eq("authToken", authToken));
        AlertSubscriptionEntity ase = (AlertSubscriptionEntity) aseCriteria.uniqueResult();
        session.delete(ase);
        tx.commit();
        session.close();
    }

    /**
     * Function to get the subscription status to a component for a user
     */
    @Override
    public String getSubscriptionToken(final String userEmail, final int compId, final int environmentId) {
        Session session = sessionFactory.openSession();
        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.createCriteria("component", "c");
        aseCriteria.createCriteria("environment", "e");
        aseCriteria.add(Restrictions.eq("subscriptionVal", userEmail));
        aseCriteria.add(Restrictions.eq("c.componentId", compId));
        aseCriteria.add(Restrictions.eq("e.environmentId", environmentId));

        AlertSubscriptionEntity ase = (AlertSubscriptionEntity) aseCriteria.uniqueResult();
        session.close();
        return ase == null ? null : ase.getAuthToken();
    }

    /**
     * Set the validation level as true/1 for the subscription with give authToken
     */
    @Override
    public void confirmSubscription(String authToken) {
        if (authToken == null) {
            throw new IllegalArgumentException("authToken is null");
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.add(Restrictions.eq("authToken", authToken));
        AlertSubscriptionEntity ase = (AlertSubscriptionEntity) aseCriteria.uniqueResult();
        ase.setValidationLevel(1);
        session.update(ase);
        tx.commit();
        session.close();
    }

    /**
     * Get the list of all the subscription for the given componentId and environmentId
     */
    @Override
    public List<AlertSubscriptionEntity> getSubscribedEmailIdList(final int componentId, final int environmentId) {
        Session session = sessionFactory.openSession();

        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.createCriteria("component", "c");
        aseCriteria.createCriteria("environment", "e");
        aseCriteria.add(Restrictions.eq("c.componentId", componentId));
        aseCriteria.add(Restrictions.eq("e.environmentId", environmentId));

        @SuppressWarnings("unchecked")
        List<AlertSubscriptionEntity> alertSubscriptionList = (List<AlertSubscriptionEntity>) aseCriteria.list();
        session.close();
        return alertSubscriptionList;
    }

    /**
     * Function to check for if a DB row exist for the given componentId and environmentId and subsciptionText
     */
    @Override
    public boolean checkforDuplicates(final int componentId, final int environmentId, final String subsciptionText) {
        Session session = sessionFactory.openSession();
        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.createCriteria("component", "c");
        aseCriteria.createCriteria("environment", "e");
        aseCriteria.add(Restrictions.eq("subscriptionVal", subsciptionText));
        aseCriteria.add(Restrictions.eq("c.componentId", componentId));
        aseCriteria.add(Restrictions.eq("e.environmentId", environmentId));

        Number rowCount = (Number) aseCriteria.setProjection(Projections.rowCount()).uniqueResult();
        session.close();
        return rowCount.intValue() > 0 ? true : false;

    }

    /**
     * Checks whether the given subscription is available in DB.
     */
    @Override
    public boolean isSubscribtionAvailable(final Subscription subscription, final int subsType) {
        Session session = sessionFactory.openSession();
        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.createCriteria("environment", "e");
        aseCriteria.add(Restrictions.eq("subscriptionVal", subscription.getSubsciptionVal()));
        aseCriteria.add(Restrictions.isNull("component"));
        aseCriteria.add(Restrictions.eq("subscriptionType", subsType));
        aseCriteria.add(Restrictions.eq("globalComponentTypeId", subscription.getGlobalSubscriptionTypeId()));
        aseCriteria.add(Restrictions.eq("e.environmentId", subscription.getEnvironmentId()));

        Number rowCount = (Number) aseCriteria.setProjection(Projections.rowCount()).uniqueResult();
        session.close();
        return rowCount.intValue() > 0 ? true : false;
    }

    /**
     * Function to get all global subscriptions from the database
     */
    @Override
    public List<AlertSubscriptionEntity> getAllGlobalSubscriptions() {
        Session session = sessionFactory.openSession();

        Criteria aseCriteria = session.createCriteria(AlertSubscriptionEntity.class);
        aseCriteria.add(Restrictions.isNull("component"));

        @SuppressWarnings("unchecked")
        List<AlertSubscriptionEntity> alertSubscriptionList = (List<AlertSubscriptionEntity>) aseCriteria.list();
        session.close();

        return alertSubscriptionList;
    }

    /**
     * Function to delete global subscription from the database.
     */
    @Override
    public void deleteGlobalSubscription(int subscriptionId) {
        if (subscriptionId == 0) {
            throw new IllegalArgumentException("Invalid subscription Id");
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        AlertSubscriptionEntity ase = session.load(AlertSubscriptionEntity.class, subscriptionId);
        session.delete(ase);
        tx.commit();
        session.close();
    }

}
