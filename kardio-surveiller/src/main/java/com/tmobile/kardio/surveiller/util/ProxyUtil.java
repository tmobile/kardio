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
package com.tmobile.kardio.surveiller.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;

/**
 * All the proxy and certificate related functionality..
 */
public class ProxyUtil {
	
	private ProxyUtil() {}
	/**
	 * Sets an HTTP proxy as per the values from config.property
	 */
	public static void setProxy() {
		PropertyUtil prop = PropertyUtil.getInstance();
		if(prop.getValue(SurveillerConstants.CONFIG_PROXY_ENABLED) == null || !prop.getValue(SurveillerConstants.CONFIG_PROXY_ENABLED).equalsIgnoreCase("true")){
			return;
		}
		
		class ProxyAuth extends Authenticator {
    	    private PasswordAuthentication auth;

    	    private ProxyAuth(String user, String password) {
    	        auth = new PasswordAuthentication(user, password == null ? new char[]{} : password.toCharArray());
    	    }
    	    @Override
    	    protected PasswordAuthentication getPasswordAuthentication() {
    	        return auth;
    	    }
    	}
		
        System.setProperty("http.proxyHost", prop.getValue(SurveillerConstants.CONFIG_PROXY_HTTP_HOST));
        System.setProperty("http.proxyPort", prop.getValue(SurveillerConstants.CONFIG_PROXY_HTTP_PORT));
        System.setProperty("https.proxyHost", prop.getValue(SurveillerConstants.CONFIG_PROXY_HTTPS_HOST));
        System.setProperty("https.proxyPort", prop.getValue(SurveillerConstants.CONFIG_PROXY_HTTPS_PORT));
        
        Authenticator.setDefault(new ProxyAuth(prop.getValue(SurveillerConstants.CONFIG_PROXY_USERNAME), prop.getValue(SurveillerConstants.CONFIG_PROXY_PASSWORD) ) );
	}
	
	/**
	 * Disable certificate validation as per the configuration in config.property
	 */
	public static void disableCertificates() {
		PropertyUtil prop = PropertyUtil.getInstance();
		if(prop.getValue(SurveillerConstants.CONFIG_SSL_DISABLECERTIFICATE_CHECK) == null || !prop.getValue(SurveillerConstants.CONFIG_SSL_DISABLECERTIFICATE_CHECK).equalsIgnoreCase("true")){
			return;
		}

	    TrustManager[] trustAllCerts = new TrustManager[]{
	        new X509TrustManager() {

	            @Override
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return new java.security.cert.X509Certificate[] {};
	            }

	            /**
	             * Skipping the client check.
	             */
	            @Override
	            public void checkClientTrusted(
	                    java.security.cert.X509Certificate[] certs, String authType) {
	            }

	            /**
	             * Skipping the serve check.
	             */
	            @Override
	            public void checkServerTrusted(
	                    java.security.cert.X509Certificate[] certs, String authType) {
	            }
	        }
	    };
	    
	    //Create all-trusting host name verifier
        HostnameVerifier allHostsValid = (hostname, session) -> true;

	    try {
	    	SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
	}
}
