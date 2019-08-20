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

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.EnvironmentEntity;

/**
 * Dao class to Query and update the Environment Table. Implements EnvironmentDao
 * 
 */
@Repository
public class EnvironmentDaoImpl implements EnvironmentDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String ALPHABET_PATTER = "[a-zA-Z0-9]*";

    /**
     * Get EnironmentId From Name
     */
    @Override
    public int getEnironmentIdFromName(final String environmentName) {
        return getEnvironmentFromName(environmentName).getEnvironmentId();
    }
    
    /**
     * Get Environment From Name
     */
    @Override
	public EnvironmentEntity getEnvironmentFromName(String environmentName) {
    	if (environmentName == null || !Pattern.matches(ALPHABET_PATTER, environmentName)) {
            throw new IllegalArgumentException("Environment name is null or contains something other than alphabets");
        }
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(HQLConstants.GET_ENIRONMENT_ID_FROM_NAME).setString("envName", environmentName.toLowerCase()).setCacheable(true).setCacheRegion("GET_ENIRONMENT_ID_FROM_NAME");
        EnvironmentEntity ee = (EnvironmentEntity) query.uniqueResult();        
        session.close();
        int envId = ee.getEnvironmentId();
        if (envId == 0) {
            throw new IllegalArgumentException("Environment name is not valid : " + environmentName);
        }
        return ee;
	}

    /**
     * Update the Environment details in DB
     * Updated Code to include changes for K8s Credentials & URL
     * Updated UPDATE_ENVIRONMENT_BASIC, UPDATE_ENVIRONMENT_MARATHON_CRED queries.
     * Added new query UPDATE_ENVIRONMENT_K8S_CRED
     */
    public void updateEnvironment(Environment environment) {
    	String marathonUrl = null;
    	String k8sUrl = null;
    	
        if (environment.getEnvironmentId() == 0) {
            throw new IllegalArgumentException("Environment cannot be null");
        } 
        
        if(environment.getMarathonUrl() != null && !environment.getMarathonUrl().isEmpty()){
        	marathonUrl = environment.getMarathonUrl();
        }
        if(environment.getK8sUrl() != null && !environment.getK8sUrl().isEmpty()){
        	k8sUrl = environment.getK8sUrl();
        }

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = null;
        
        
        if (environment.getMarathonUserName() == null || environment.getMarathonUserName().equals("")) {
        	 query = session.createQuery(HQLConstants.UPDATE_ENVIRONMENT_BASIC).setString("environmentDesc", environment.getEnvironmentDesc())
             		.setString("marathonUrl", marathonUrl).setString("k8sUrl", k8sUrl).setInteger("envLock", environment.getEnvLock()).setInteger("dispOrdr", environment.getDisplayOrder())
             		.setInteger("environmentId", environment.getEnvironmentId());
             query.executeUpdate();
    
        } else {
        	String marathonCredStr = environment.getMarathonUserName() + ":" + environment.getMarathonPassword();
            String marathonCred = new String(Base64.encodeBase64(marathonCredStr.getBytes()));
            query = session.createQuery(HQLConstants.UPDATE_ENVIRONMENT_MARATHON_CRED)
                    .setString("environmentDesc", environment.getEnvironmentDesc()).setString("marathonUrl", marathonUrl)
                    .setString("k8sUrl", k8sUrl)
                    .setInteger("envLock", environment.getEnvLock())
                    .setString("marathonCred", marathonCred)
                    .setInteger("dispOrdr", environment.getDisplayOrder())
                    .setInteger("environmentId", environment.getEnvironmentId());
            query.executeUpdate();
          
        }
        if(environment.getK8sUserName() == null || environment.getK8sUserName().equals("")) {
            query = session.createQuery(HQLConstants.UPDATE_ENVIRONMENT_BASIC).setString("environmentDesc", environment.getEnvironmentDesc())
            		.setString("marathonUrl", marathonUrl).setString("k8sUrl", k8sUrl).setInteger("envLock", environment.getEnvLock()).setInteger("dispOrdr", environment.getDisplayOrder())
            		.setInteger("environmentId", environment.getEnvironmentId());
            query.executeUpdate();
   
        	}
        	else {
        		String k8sCredStr = environment.getK8sUserName() + ":" + environment.getK8sPassword();
        		String k8sCred = new String(Base64.encodeBase64(k8sCredStr.getBytes()));
        		query = session.createQuery(HQLConstants.UPDATE_ENVIRONMENT_K8S_CRED).setString("environmentDesc", environment.getEnvironmentDesc()).setString("marathonUrl", marathonUrl)
                        .setString("k8sUrl", k8sUrl)
                        .setString("marathonUrl", marathonUrl)
        				.setInteger("envLock", environment.getEnvLock())
                        .setString("k8sCred", k8sCred)
                        .setInteger("dispOrdr", environment.getDisplayOrder())
                        .setInteger("environmentId", environment.getEnvironmentId());
                query.executeUpdate();
              
        	}
        tx.commit();     
       session.close();
    }
    /**
     * Add new environment details in DB
     * Updated code to include K8sUrl and K8sCred.
     */
	public void addEnvironment(Environment environment) {
		String marathonCred = null;
		String k8sCred = null;
		 Session session = sessionFactory.openSession();
	     Transaction tx = session.beginTransaction();
	     if (environment.getMarathonUserName() != null) {
	          String marathonCredStr = environment.getMarathonUserName() + ":" + environment.getMarathonPassword();
	    	  marathonCred = new String(Base64.encodeBase64(marathonCredStr.getBytes()));
	     }
	     if (environment.getK8sUserName() != null) {
     		  String k8sCredStr = environment.getK8sUserName() + ":" + environment.getK8sPassword();
	    	  k8sCred = new String(Base64.encodeBase64(k8sCredStr.getBytes()));
	     }
	     EnvironmentEntity environmentEntity = new EnvironmentEntity();
	     environmentEntity.setEnvironmentName(environment.getEnvironmentName());
	     environmentEntity.setEnvironmentDesc(environment.getEnvironmentDesc());
	     environmentEntity.setMarathonUrl(environment.getMarathonUrl());
	     environmentEntity.setDisplayOrder(environment.getDisplayOrder());
	     environmentEntity.setMarathonCred(marathonCred);
	     environmentEntity.setK8sUrl(environment.getK8sUrl());
	     environmentEntity.setK8sCred(k8sCred);
	     session.save(environmentEntity);
	     tx.commit();
	     session.close();
	}

    /**
     * Get all the Environments with environment_lock = 0
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<EnvironmentEntity> getEnvironments() {
        Session session = sessionFactory.openSession();
        Criteria envCriteria = session.createCriteria(EnvironmentEntity.class).setCacheable(true);
        envCriteria.add(Restrictions.eq("envLock", 0));
        envCriteria.addOrder(Order.asc("displayOrder"));
        List<EnvironmentEntity> retEnvs = (List<EnvironmentEntity>) envCriteria.list();
        session.close();
        return retEnvs;
    }

    /**
     * Get all the Environments With out considering the environment_lock column
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<EnvironmentEntity> getEnvironmentWithLock() {
        Session session = sessionFactory.openSession();
        Criteria envCriteria = session.createCriteria(EnvironmentEntity.class);
        envCriteria.addOrder(Order.asc("displayOrder"));
        List<EnvironmentEntity> retEnvs = (List<EnvironmentEntity>) envCriteria.list();
        session.close();
        return retEnvs;
    }
}
