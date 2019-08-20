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
package com.tmobile.kardio.surveiller.marathon;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

/**
 * Class to handle Marathon backup tasks
 */
public class MarathonBackUpTask {

	private static final Logger logger = Logger.getLogger(MarathonBackUpTask.class);
	
	private MarathonBackUpTask() {}
	/**
	 * Does activities for the task for Back Up marathon config.
	 * 
	 * @throws SQLException
	 */
	public static void doMarathonBackUp() throws Exception {
		logger.info("************** STARTED MarathonBackUp **************");
		List<EnvironmentVO> environmentVOs = DBQueryUtil.getAllEnvironments();
		for(EnvironmentVO eVo : environmentVOs){
			getMarathonJson(eVo);
			logger.info("Got Marathon JSON for " + eVo.getEnvironmentName() + " Time : " + new Date());
			logger.info("JSON " +  eVo.getMarathonJson());
		}
		DBQueryUtil.updateEnvironmentDetails(environmentVOs);
		MarathonConfigProcessor.updateApiList(environmentVOs);
		logger.info("************** COMPLETED MarathonBackUp **************");
	}
	
	/**
	 * Gets the Marathon JSON from for Back UP.
	 * 
	 * @param environmentVO
	 */
	private static void getMarathonJson(EnvironmentVO environmentVO) throws Exception {
		logger.debug("Running Marathon Backup for environment: " + environmentVO.getEnvironmentName());
		String marathonJson = null;
		String eastMarathonJson = null;
		if(environmentVO.getMarathonUrl() != null && !environmentVO.getMarathonUrl().equals("")){
			marathonJson = RestCommunicationHandler.getResponse(environmentVO.getMarathonUrl(), true,"Basic ", environmentVO.getMarathonCred());
		}
		 
		if(marathonJson == null || marathonJson.equals("failed") || marathonJson.equals("")){
			environmentVO.setMarathonJson(null);
		} else {
			environmentVO.setMarathonJson(marathonJson);
		}
		//Get EAST marathon JSON
		if(environmentVO.getEastMarathonUrl() != null && !environmentVO.getEastMarathonUrl().equals("")){
			eastMarathonJson = RestCommunicationHandler.getResponse(environmentVO.getEastMarathonUrl(), true,"Basic ", environmentVO.getMarathonCred());
		}
		
		if(eastMarathonJson == null || eastMarathonJson.equals("failed") || eastMarathonJson.equals("")){
			environmentVO.setEastMarathonJson(null);
		} else {
			environmentVO.setEastMarathonJson(eastMarathonJson);
		}
	}
}
