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

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * The handler used in health_check_type table. Performs health checks via URL's
 */
public class URLOpenableHandler extends SurveillerHandler {

	private static final Logger logger = Logger.getLogger(URLOpenableHandler.class);
	
	private static final String PARAM_KEY_URL = "URL";
	private static final String BASIC_AUTH_USERNAME = "BASIC_AUTH_USERNAME";
	private static final String BASIC_AUTH_PASSWORD = "BASIC_AUTH_PASSWORD";
	
	private static final int TIME_OUT = 15000;
	
	/**
	 * Get health check status via URL
	 * @see com.tmobile.kardio.surveiller.handler.SurveillerHandler#getSurveillerStatus()
	 **/
	@Override
	public StatusVO getSurveillerStatus() {
		HttpURLConnection conn = null;
		try {
			if(paramDetails.get(PARAM_KEY_URL)==null ){
				logger.error("Configuration Error : The URL is null in DB");
				throw new IllegalArgumentException("Configuration Error : The URL is null in DB");
			}
			
			URL url = new URL(paramDetails.get(PARAM_KEY_URL));
			conn = (HttpURLConnection) url.openConnection();
			
			if(paramDetails.get(BASIC_AUTH_USERNAME) != null && paramDetails.get(BASIC_AUTH_PASSWORD) != null){
				String authString = paramDetails.get(BASIC_AUTH_USERNAME) + ":" + paramDetails.get(BASIC_AUTH_PASSWORD);
				byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
				conn.setRequestProperty("Authorization", "Basic " + (new String(authEncBytes)));
			}
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setRequestMethod("GET");
			conn.connect();
			
			logger.debug("Connecting URL : " + paramDetails.get(PARAM_KEY_URL) + "; HTTP Status : " + conn.getResponseCode());
			
			if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_CREATED
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_ACCEPTED
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_NOT_AUTHORITATIVE
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_NO_CONTENT
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_RESET
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_PARTIAL){
				return new StatusVO(Status.UP);
			}else{
				return new StatusVO(Status.DOWN,"Got an HTTP error while connection. Http Error Code = " 
							+ conn.getResponseCode() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
			}
			
		} catch(UnknownHostException ue){
			logger.error("Got UnknownHostException", ue);
			return new StatusVO(Status.DOWN,"Unable to resolve DNS - " + ue.getMessage() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
		} catch (Exception e) {
			logger.error("Got Exception while connecting", e);
			return new StatusVO(Status.DOWN,e.getMessage() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
		}finally{
			try {
				if(conn != null){
					conn.disconnect();
				}
		    }catch (Exception e) {
		    	logger.error("Got Exception while disconnecting", e);		    	
		    }
		}
	}

}
