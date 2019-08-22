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
package com.tmobile.kardio.surveiller.db.config;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.util.PropertyUtil;

/**
 * Hibernate Configuration Loader
 */
public class HibernateConfig {
	private static final Logger logger = Logger.getLogger(HibernateConfig.class);
			
	private static SessionFactory sessionFactory;
	
	private static PropertyUtil propertyUtil = PropertyUtil.getInstance();
	
	/**
	 * Constructor to initialize the hibernate configuration
	 */
	private HibernateConfig(){
		logger.info("** Initializing Hibernate Connection **");
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.AlertSubscriptionEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.ComponentEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.ComponentFailureLogEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.ComponentTypeEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.CounterEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.CounterMetricEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.CounterMetricHistoryEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.CounterMetricTypeEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.DaillyCompStatusEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.EnvCounterEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.EnvironmentEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.HealthCheckEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.HealthCheckParamEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.HealthCheckTypeEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.RegionEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.StatusEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.ContainerStatsEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.ApiStatusEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.PromLookupEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.TpsServiceEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.TpsLatHistoryEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.K8sApiStatusEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.K8sTpsLatHistoryEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.K8sObjectPodsEntity.class);
		configuration.addAnnotatedClass(com.tmobile.kardio.surveiller.db.entity.K8sPodsContainersEntity.class);
		
		configuration.setProperty("hibernate.connection.driver_class", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_DRIVER_CLASS) );
	    configuration.setProperty("hibernate.connection.url", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_URL) );                                
	    configuration.setProperty("hibernate.connection.username", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_USERNAME) );     
	    configuration.setProperty("hibernate.connection.password", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_PASSWORD) );
	    configuration.setProperty("hibernate.dialect", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_HIBERNATE_DIALECT) );
	    configuration.setProperty("hibernate.hbm2ddl.auto", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_HIBERNATE_HBM2DDL) );
	    configuration.setProperty("hibernate.show_sql", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_HIBERNATE_SHOWSQL) );
	    configuration.setProperty("hibernate.connection.pool_size", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_HIBERNATE_POOLSIZE) );
	    configuration.setProperty("hibernate.enhancer.enableLazyInitialization", propertyUtil.getValue(SurveillerConstants.CONFIG_DB_HIBERNATE_LX_INIT) );
	    configuration.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
	    
	    StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		sessionFactory = configuration.buildSessionFactory(builder.build());
		logger.info("** Initializing Hibernate Connection Completed **");
	}

	/**
	 * Function to get the session factory object
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null){
			synchronized (HibernateConfig.class) {
				new HibernateConfig();
			}
		}
		return sessionFactory;
	}

	/**
	 * Function to close the connection to the database
	 */
	public static synchronized void closeSessionFactory() {
		if(sessionFactory == null){
			return;
		}
		sessionFactory.close();
		sessionFactory= null;
	}
}
