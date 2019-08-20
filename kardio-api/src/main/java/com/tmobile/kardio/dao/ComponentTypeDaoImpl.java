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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.tmobile.kardio.constants.ComponentType;
import com.tmobile.kardio.db.entity.ComponentTypeEntity;

@Repository
@PropertySource("classpath:application.properties")
public class ComponentTypeDaoImpl implements ComponentTypeDao {
    @Autowired
    private SessionFactory sessionFactory;

	@Override
	public void saveComponentType(ComponentType componentType) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        ComponentTypeEntity componentTypeEntity = new ComponentTypeEntity();
        componentTypeEntity.setComponentTypeName(componentType.componentTypeName());
        componentTypeEntity.setComponentTypeDesc(componentType.componentTypeName());
        session.save(componentTypeEntity);
        tx.commit();
        session.close();
	}

}
