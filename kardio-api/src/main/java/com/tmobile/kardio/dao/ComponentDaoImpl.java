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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.constants.HQLConstants;
import com.tmobile.kardio.db.entity.AppLookUpEntity;
import com.tmobile.kardio.db.entity.ComponentEntity;
import com.tmobile.kardio.db.entity.ComponentTypeEntity;
import com.tmobile.kardio.db.entity.HealthCheckEntity;

/**
 * Operation on component table. Implements ComponentDao interface
 */
@Repository
@PropertySource("classpath:application.properties")
public class ComponentDaoImpl implements ComponentDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Value("${adminpage.skip.infra.component.name}")
    private String skipInfraCompNames;
    
    private List<String> skipableInfraComponents;

    @PostConstruct
    public void inititializeDao() throws Exception {
        skipableInfraComponents = Arrays.asList(skipInfraCompNames.trim().split(","));
    }


    /**
     * Add a new Component to DB
     */
    public void saveComponent(Component component) {
        if (component.getComponentType() == null || component.getComponentName() == null || component.getComponentName().isEmpty()) {
            throw new IllegalArgumentException("Component Name cannot be null");
        }
        ComponentEntity componentEntity = new ComponentEntity();
        ComponentEntity parentComponent = null;
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        componentEntity.setComponentName(component.getComponentName());
        componentEntity.setComponentDesc(component.getCompDesc());
        componentEntity.setManual("Y");
        //Changes for K8s-Admin
        componentEntity.setPlatform(component.getPlatform());
        if (component.getParentComponentId() != 0) {
            parentComponent = session.get(ComponentEntity.class, component.getParentComponentId());
            componentEntity.setParentComponent(parentComponent);
        }
        componentEntity.setComponentType(getComponentType(component.getComponentType()));
        session.save(componentEntity);
        tx.commit();
        session.close();
    }

    /**
     * Get all the infra components from DB
     */
    public List<ComponentEntity> getComponents() {
        Session session = sessionFactory.openSession();
        Criteria compCriteria = session.createCriteria(ComponentEntity.class);
        compCriteria.createCriteria("componentType", "ct");
        compCriteria.add(Restrictions.eq("ct.componentTypeId", ComponentType.INFRA.componentTypeId()));
        compCriteria.add(Restrictions.eq("manual", "Y"));
        @SuppressWarnings("unchecked")
        List<ComponentEntity> results = compCriteria.list();
        session.close();
        return results;
    }
    
    /**
     * Get List of components from the database for the given EnvironmentEntity.
     */
    @Override
	public List<HealthCheckEntity> getPlatformComponentForEnv(int envId,String platform) {
    	Transaction tx;
    	Session session = sessionFactory.openSession();
    	tx = session.beginTransaction();
        Criteria hcCriteria = session.createCriteria(HealthCheckEntity.class,"hc");
        hcCriteria.createCriteria("hc.component", "c");
        hcCriteria.createCriteria("c.parentComponent", "pc");
        if(envId != 0){
        	hcCriteria.add(Restrictions.eq("hc.environment.environmentId", envId));
        }
        hcCriteria.add(Restrictions.eq("hc.delInd", 0));
        hcCriteria.add(Restrictions.eq("c.delInd", 0));
        hcCriteria.add(Restrictions.eq("pc.delInd", 0));
        /**
         * API Dashboard change to include platform
         */
        if(platform != null && !platform.equals("All")) {
        	 hcCriteria.add(Restrictions.eq("c.platform",platform));
        }
        hcCriteria.add(Restrictions.or(Restrictions.eq("c.componentType.componentTypeId",ComponentType.APP.componentTypeId()),
        		Restrictions.in("pc.componentName", skipableInfraComponents)));
        @SuppressWarnings("unchecked")
        List<HealthCheckEntity> results = hcCriteria.list();
        tx.commit();
        session.close();
        
		return results;
	}

    /**
     * Update the given Component details to DB
     * K8s-Admin Changes - Updated UPDATE_COMPONENT_WITH_PARENT query.
     */
    public void editComponent(Component component) {
        if (component.getComponentName().isEmpty()) {
            throw new IllegalArgumentException("Component Name cannot be null");
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = null;
        if (component.getParentComponentId() == 0) {
            query = session.createQuery(HQLConstants.UPDATE_COMPONENT_WITHOUT_PARENT).setString("compName", component.getComponentName())
                    .setString("compDesc", component.getCompDesc()).setInteger("delInd", component.getDelInd())
                    .setInteger("compId", component.getComponentId());
            query.executeUpdate();
        } else {
            query = session.createQuery(HQLConstants.UPDATE_COMPONENT_WITH_PARENT).setString("compName", component.getComponentName())
                    .setString("compDesc", component.getCompDesc()).setString("platform", component.getPlatform()).setInteger("compParent", component.getParentComponentId())
                    .setInteger("delInd", component.getDelInd()).setInteger("compId", component.getComponentId());
        }

        query.executeUpdate();
        tx.commit();
        session.close();
    }

    /**
     * Query the component type table using componentTypeName.
     * 
     * @param componentType
     * @return
     */
    private ComponentTypeEntity getComponentType(String componentType) {
        Session session = sessionFactory.openSession();
        Criteria compCriteria = session.createCriteria(ComponentTypeEntity.class);
        compCriteria.add(Restrictions.eq("componentTypeName", componentType));
        @SuppressWarnings("unchecked")
        List<ComponentTypeEntity> results = compCriteria.list();
        session.close();
        return results.get(0);
    }

    /**
     * Delete the component from DB
     */
    public void deleteComponent(Component component) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery(HQLConstants.UPDATE_COMPONENT_DEL_IND).setInteger("compId", component.getComponentId());
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    /**
     * Get All the APP full name from app_lookup table
     */
    @Override
    public Map<Integer, String> getAppFullName() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(AppLookUpEntity.class, "al");
        @SuppressWarnings("unchecked")
        List<AppLookUpEntity> appLookupList = criteria.list();
        session.close();
        Map<Integer, String> mapLookup = new HashMap<Integer, String>();
        for (AppLookUpEntity appEntity : appLookupList) {
            mapLookup.put(appEntity.getComponent().getComponentId(), appEntity.getComponentFullName());
        }
        return mapLookup;
    }
    
    public List<ComponentEntity> getAppParentComponents(){
    	Session session = sessionFactory.openSession();
        Criteria compCriteria = session.createCriteria(ComponentEntity.class);
        compCriteria.createCriteria("componentType", "ct");
        compCriteria.add(Restrictions.eq("ct.componentTypeName", "APP"));
        compCriteria.add(Restrictions.isNull("parentComponent"));
        @SuppressWarnings("unchecked")
        List<ComponentEntity> results = compCriteria.list();
        session.close();
        return results;
    }
}
