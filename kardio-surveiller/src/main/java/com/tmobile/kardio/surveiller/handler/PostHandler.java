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
package com.tmobile.kardio.surveiller.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.StatusVO;
import com.tmobile.kardio.surveiller.vo.ValidationVO;

public class PostHandler extends SurveillerHandler {

	private static final String REGEX_SUFFIX = ".regex";

	private static final Logger logger = Logger.getLogger(PostHandler.class);

	private static final String PARAM_KEY_URL = "URL";
	private static final String PAYLOAD = "PAYLOAD";
	/**
	 * Example JSON.
	 *[
	 *  {
	 *    "path": "$['data']['user_id']",
	 *    "value": "UserId"
	 *  },
	 *  {
	 *    "path": "$['data']['name']",
	 *    "value": "User Name"
	 *  },
	 *  {
	 *    "path": "$['data']['token'].regex",
	 *    "value": "ey\\w+VCJ9\\.{1}ey\\w+\\.{1}+"
	 *  }
	 *]
	 */
	private static final String VALIDATION = "VALIDATION";

	private static final int TIME_OUT = 15000;

	/**
	 * Get health check status via URL
	 * @see com.tmobile.kardio.surveiller.handler.SurveillerHandler#getSurveillerStatus()
	 **/

	@Override
	public StatusVO getSurveillerStatus() {

		HttpURLConnection conn = null;
		int responseCode;
		boolean patternMatch = true, responseflag = true;

		//HashMap<String, Object> responseData = new HashMap<String, Object>();

		try {
			if(paramDetails.get(PARAM_KEY_URL)==null||paramDetails.get(PARAM_KEY_URL)==""){
				throw new IllegalArgumentException("Configuration Error : The URL is null in DB");
			}

			URL url = new URL(paramDetails.get(PARAM_KEY_URL));
			conn = (HttpURLConnection) url.openConnection();


			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			logger.debug("Connecting URL : " + paramDetails.get(PARAM_KEY_URL));

			OutputStream os = conn.getOutputStream();
			os.write( paramDetails.get(PAYLOAD).getBytes());
			os.flush();
			os.close();
			logger.debug("Payload Sent");
			responseCode = conn.getResponseCode();
			logger.debug("POST Response Code :  " + responseCode);
			logger.debug("POST Response Message : " + conn.getResponseMessage());

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer jsonString = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();


			/** Response Code Validation
			 * 
			 */
			if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_CREATED
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_ACCEPTED
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_NOT_AUTHORITATIVE
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_NO_CONTENT
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_RESET
					|| conn.getResponseCode() == HttpsURLConnection.HTTP_PARTIAL){

				ObjectMapper mapper = new ObjectMapper();
				JsonNode validationNode = mapper.readTree(paramDetails.get(VALIDATION));
				List<ValidationVO> valList = mapper.readValue(validationNode.toString(),TypeFactory.defaultInstance().constructCollectionType(List.class,  
						ValidationVO.class));
				DocumentContext jsonContext = JsonPath.parse(jsonString.toString());

				for (ValidationVO validation : valList) {
					boolean isRegex = false;
					String path = validation.getPath();
					if (path.endsWith(REGEX_SUFFIX)) {
						path = path.substring(0, path.length() - REGEX_SUFFIX.length());
						isRegex = true;
					}
					String actualValue = jsonContext.read(path);
					String expectedValue = validation.getValue();
					if (isRegex) {
						Pattern pattern = Pattern.compile(expectedValue);
						Matcher matcher = pattern.matcher(actualValue);
						patternMatch = matcher.find();
					} else {
						if (!expectedValue.equals(actualValue)) {
							responseflag=false;
							break;
						} 
					}

				}
				if(patternMatch){
					if(responseflag)
						return new StatusVO(Status.UP);
					else {
						return new StatusVO(Status.DOWN,"Response not Matching with the validation parameters " 
								+ conn.getResponseCode() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
					}
				}else{
					return new StatusVO(Status.DOWN,"Response not Matching with the Regex provided " 
							+ conn.getResponseCode() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
				}


			} 
			else {
				return new StatusVO(Status.DOWN,"Got an HTTP error while connection. Http Error Code = " 
						+ conn.getResponseCode() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
			}
		}
		catch(UnknownHostException ue){
			logger.error("Got UnknownHostException");
			return new StatusVO(Status.DOWN,"Unable to resolve DNS - " + ue.getMessage() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
		} catch(JsonParseException je) {
			logger.error("Got JsonParseException");
			return new StatusVO(Status.DOWN,"Got JsonParseException");
		}
		catch(JsonMappingException je) {
			logger.error("Got JsonMappingException");
			return new StatusVO(Status.DOWN,"Got JsonMappingException");
		}
		catch (Exception e) {
			logger.error("Got Exception while connecting Post Handler");
			return new StatusVO(Status.DOWN,e.getMessage() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
		}finally{
			try {
				if(conn != null){
					conn.disconnect();
				}
			}catch (Exception e) {
				logger.error("Got Exception while disconnecting Post Handler");		    	
			}
		}
	}

}
