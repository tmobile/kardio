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
package com.tmobile.kardio.surveiller.handler;

import java.util.Map;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * Abstract class that has the common methods for Health Check Handler.
 * All the classes in health_check_type must be extending this class.
 * 
 */
public abstract class SurveillerHandler implements Runnable{
	
	private static final Logger logger = Logger.getLogger(SurveillerHandler.class);

	protected Map<String,String> paramDetails;

	private HealthCheckVO healthCheckVO;
	
	/**
	 * Does the status check for the Component with the given parameters
	 * @return
	 */
	public abstract StatusVO getSurveillerStatus();
	
	/**
	 * Run method of the thread to survey the health of components
	 * @see java.lang.Runnable#run()
	 **/
	public void run() {
		logger.debug("RUNNING >> ComponentId : " + healthCheckVO.getHealthCheckComponentId() +
				"; RegionId : " + healthCheckVO.getHealthCheckRegionId());
		try{
			healthCheckVO.setStatus(getSurveillerStatus());
		}catch (Exception e) {
			logger.error("Unhandled scenario : Got Error while doing Health check : " + healthCheckVO.getHealthCheckId());
			healthCheckVO.setStatus(new StatusVO(Status.DOWN,"Unhandled scenario : Got Error while doing Health check") );
		}
		logger.debug("COMPLETED >> ComponentId : " + healthCheckVO.getHealthCheckComponentId() +
				"; RegionId : " + healthCheckVO.getHealthCheckRegionId());
	}

	/**
	 * Set the VO and set the parameters from health_check_param.
	 * 
	 * @param healthCheckVO
	 */
	public void init(HealthCheckVO healthCheckVO){
		this.healthCheckVO = healthCheckVO;
		paramDetails = healthCheckVO.getParamDetails();
	}
	
	/**
	 * Get healthCheckVO
	 * @return the healthCheckVO
	 */
	public HealthCheckVO getHealthCheckVO() {
		return healthCheckVO;
	}

	/**
	 * Set healthCheckVO
	 * @param healthCheckVO the healthCheckVO to set
	 */
	public void setHealthCheckVO(HealthCheckVO healthCheckVO) {
		this.healthCheckVO = healthCheckVO;
	}
}
