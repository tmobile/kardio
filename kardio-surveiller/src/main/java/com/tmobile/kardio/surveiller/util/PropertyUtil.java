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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;

/**
 * Getting values from the config.properties file. The file must be present in the working directory.
 * Singleton class.
 */
public class PropertyUtil {
	private static final Logger logger = Logger.getLogger(PropertyUtil.class);
	
	Properties prop = new Properties();
	
	private static PropertyUtil instance = null;
	
	public PropertyUtil(){}
	/**
	 * Get Instance
	 * @return
	 */
	public synchronized static PropertyUtil getInstance(){
		if(instance != null){
			return instance;
		}
		String configFileName = System.getenv(SurveillerConstants.CONFIG_FILE_ENV_VAR);
		if(configFileName == null || configFileName.trim().length() == 0){
			configFileName = "config.properties"; // Default Path
		}
        try{
        	instance = new PropertyUtil();
        	InputStream in = new FileInputStream(configFileName);
        	instance.prop.load(in);
        }catch(Exception ex){
        	logger.error("Initialization failed config.properties not found",ex);
        	throw new InternalError("Initialization failed config.properties not found");
        }
		return instance;
	}
	
	/**
	 * The the value for the key.
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		return (prop == null)?null:prop.getProperty(key);
	}
}
