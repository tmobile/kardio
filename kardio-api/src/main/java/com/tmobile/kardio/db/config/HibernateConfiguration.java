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
package com.tmobile.kardio.db.config;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean that returns the SessionFactory
 */
@Configuration
public class HibernateConfiguration {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Configure hibernate session factory
     * 
     * @return
     */
    @Bean
    public SessionFactory getSessionFactory() {
        SessionFactory sf = entityManagerFactory.unwrap(SessionFactory.class);
        if (sf == null) {
            throw new IllegalArgumentException("Factory is not a hibernate factory");
        }
        return sf;
    }
}
