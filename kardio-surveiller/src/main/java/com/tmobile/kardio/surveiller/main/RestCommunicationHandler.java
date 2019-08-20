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
package com.tmobile.kardio.surveiller.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tmobile.kardio.surveiller.util.ProxyUtil;

public class RestCommunicationHandler {

	private RestCommunicationHandler() {}

	/**
	 * Get request for reset service client 
	 * @param urlStr URL string
	 * @param doAuth if authentication is required
	 * @param encodedAuth encoded basic auth
	 * @return JSON response
	 * @throws IOException 
	 */
	public static String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth) throws IOException {
		if(doAuth && (encodedAuth == null || encodedAuth.trim().length() == 0) ){
			throw new IllegalArgumentException("Authorization cretentials missing");
		}
		
		ProxyUtil.disableCertificates();
		ProxyUtil.setProxy();
		StringBuilder stb = new StringBuilder();
		URL url = new URL(urlStr);
					
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		if(doAuth){
			conn.setRequestProperty("Authorization", authType + encodedAuth);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		while ((output = br.readLine()) != null) {
			stb.append(output);
		}

		conn.disconnect();

		
		return stb.toString();
	}
	
	/**
	 * Post request function for rest service clients
	 * @param urlStr URL string
	 * @param doAuth if authentication is required
	 * @param encodedAuth encoded basic auth
	 * @return JSON response
	 * @throws IOException
	 */
	public static String postRequest(String urlStr, Boolean doAuth, String encodedAuth, String jsonCred) throws IOException {

		ProxyUtil.disableCertificates();
		ProxyUtil.setProxy();
		StringBuilder stb = new StringBuilder();
		URL url = new URL(urlStr);
					
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		if(doAuth) {
			if (jsonCred==null) {
				conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
			} else {
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(jsonCred);
				wr.flush();
			}
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		while ((output = br.readLine()) != null) {
			stb.append(output);
		}

		conn.disconnect();

		
		return stb.toString();
	}
}
