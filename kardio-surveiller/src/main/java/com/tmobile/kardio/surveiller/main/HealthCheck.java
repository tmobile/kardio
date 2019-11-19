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
package com.tmobile.kardio.surveiller.main;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.handler.SurveillerHandler;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.MailSenderUtil;
import com.tmobile.kardio.surveiller.util.PropertyUtil;
import com.tmobile.kardio.surveiller.util.ProxyUtil;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;

/**
 * Health Check Task - Must be scheduled for every minute
 */
public class HealthCheck {
	private static final Logger logger = Logger.getLogger(HealthCheck.class);
	
	private HealthCheck() {}
	/**
	 * Does the Surveiller task.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void doSurveiller() throws Exception{
		logger.info("************** STARTED Surveiller **************");
		
		ProxyUtil.setProxy();
		ProxyUtil.disableCertificates();
		
		ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(PropertyUtil.getInstance().getValue(SurveillerConstants.CONFIG_SURVEILLER_THREAD_COUNT)));//creating a pool 
        
		List<HealthCheckVO> healthCheckVOs = DBQueryUtil.getSurveillerDetailsOfComponent(false);
		for(HealthCheckVO healthCheckVO:healthCheckVOs){
			ClassLoader classLoader = Surveiller.class.getClassLoader();
		    @SuppressWarnings("unchecked")
			Class<SurveillerHandler> objectClass = (Class<SurveillerHandler>)classLoader.loadClass(
		    		healthCheckVO.getHealthCheckTypeClassName());
		    SurveillerHandler surveillerHandler = objectClass.newInstance();
			healthCheckVO.setSurveillerHandler(surveillerHandler);
			
			surveillerHandler.init(healthCheckVO);
			executor.execute(surveillerHandler);
		}
		
		executor.shutdown();
		while (!executor.isTerminated()){Thread.sleep(500);}		
		List<HealthCheckVO> statusChangedList = DBQueryUtil.updateHealthCheckStatusInDB(healthCheckVOs);		

		MailSenderUtil.sendMailForHealthCheckVos(statusChangedList);
		logger.info("************** COMPLETED Surveiller **************");
	}

}
