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
package com.tmobile.kardio.restservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestCommunicationHandler {
	
	private RestCommunicationHandler() {}

    /**
     * Function for get request in rest client
     * 
     * @param urlStr
     * @param queryParams
     * @param doAuth
     * @param encodedAuth
     * @return jsonResponse
     */
    public static String getResponse(String urlStr, Boolean doAuth, String encodedAuth) throws IOException {
        return sendRequest(urlStr, doAuth, encodedAuth, "GET");
    }

    /**
     * Function for post request in rest client
     * 
     * @param urlStr
     * @param doAuth
     * @param encodedAuth
     * @return jsonResponse
     * @throws IOException
     */
    public static String postRequest(String urlStr, Boolean doAuth, String encodedAuth) throws IOException {
    	return sendRequest(urlStr, doAuth, encodedAuth, "POST");
    }
    
    private static String sendRequest(String urlStr, Boolean doAuth, String encodedAuth, String method) throws IOException {
        if (doAuth && (encodedAuth == null || encodedAuth.trim().length() == 0)) {
            throw new IllegalArgumentException("Authorization cretentials missing");
        }
        StringBuilder stb = new StringBuilder();
        URL url = new URL(urlStr);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        if (doAuth) {
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
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
