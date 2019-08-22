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
/**
 * 
 */
package com.tmobile.kardio.surveiller.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;

/**
 * @author U29842
 *
 */
public class CommonsUtil {
	
	private CommonsUtil() {}

	public static String getK8sAuthToken(String credentials) throws IOException{
		
		String cred = CommonsUtil.decodeCredentials(credentials);
		String credArray[] = cred.split(":");
		JSONObject credJson = new JSONObject();
		credJson.put("username",credArray[0]);
		credJson.put("password", credArray[1]);
		String loginApiUrl = PropertyUtil.getInstance().getValue(SurveillerConstants.K8S_API_LOGIN_API);
		String jsonOutput = RestCommunicationHandler.postRequest(loginApiUrl, true, null, credJson.toString());
		JSONObject js = new JSONObject(jsonOutput);
		String authToken = js.getString("token");
		return authToken;
	}
	
	public static void handleException(Exception ex, String jobName){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String stackTraceString = "\n\nStack Trace \n\n" + sw.toString() + "\n\nRegards,\n" + PropertyUtil.getInstance().getValue(SurveillerConstants.MAIL_FROM_ADDRESS) ;
		String subject = null;
		stackTraceString = "CCP Service Health Dashboard - Surveiller "+ jobName +" task failed" + stackTraceString;
		subject = "Surveiller "+ jobName +" task failed";
		MailSenderUtil.sendMail(stackTraceString, PropertyUtil.getInstance().getValue(SurveillerConstants.MAIL_TASK_EXCEPTION_MAIL_TO), subject);
	}

	public static String decodeCredentials(String encodedData){
		
		byte[] decodedArray = Base64.getDecoder().decode(encodedData.getBytes());
		String decodedString = new String(decodedArray);
		return decodedString;
	}

	/**
     * Send Mail For Changed Status.
     * 
     * @param statusChangedList List of health check components that has change on status 
     * @throws SQLException 
     */
    public static void sendMailForChangedStatus(List<HealthCheckVO> statusChangedList) throws SQLException {
    	//Create VO with more details
    	List<HealthCheckVO> allHealthCheckVOs = DBQueryUtil.getSurveillerDetailsOfComponent();
    	List<HealthCheckVO> compListForMail = new ArrayList<HealthCheckVO>();
    	for(HealthCheckVO healthCheckVO:allHealthCheckVOs){
    		for(HealthCheckVO statusChangedVo:statusChangedList){
    			if(statusChangedVo.getHealthCheckComponentId().equals(healthCheckVO.getHealthCheckComponentId())
    					&& statusChangedVo.getEnvironmentId() == healthCheckVO.getEnvironmentId()
    					&& statusChangedVo.getHealthCheckRegionId().equals(healthCheckVO.getHealthCheckRegionId())){
    				healthCheckVO.setStatus(statusChangedVo.getStatus());
    				healthCheckVO.setFailureStatusMessage(statusChangedVo.getFailureStatusMessage());
    				compListForMail.add(healthCheckVO);
    			}
    		}
    	}
    	MailSenderUtil.sendMailForHealthCheckVos(compListForMail); 
	}
}
