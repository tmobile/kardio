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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * The handler used in health_check_type table. Performs health checks via URL's
 */
public class DBHandler extends SurveillerHandler {

	private static final Logger logger = Logger.getLogger(DBHandler.class);
	
	private static final String PARAM_KEY_JDBC_URL = "DB-JDBC-URL";
	private static final String PARAM_KEY_JDBC_DRIVER_CLASS = "DB-JDBC-DRIVER-CLASS";
	private static final String PARAM_KEY_USER = "DB-USER";
	private static final String PARAM_KEY_PWD = "DB-PASSWORD";
	private static final String PARAM_KEY_QUERY = "DB-QUERY";
	private static final String PARAM_KEY_EXPVALUE = "DB-EXPECTED-VALUE";
	
	/**
	 * Get health check status via URL
	 * @see com.tmobile.kardio.surveiller.handler.SurveillerHandler#getSurveillerStatus()
	 **/
	@Override
	public StatusVO getSurveillerStatus() {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			if(paramDetails.get(PARAM_KEY_JDBC_URL) == null || paramDetails.get(PARAM_KEY_JDBC_DRIVER_CLASS) == null 
					|| paramDetails.get(PARAM_KEY_USER) == null || paramDetails.get(PARAM_KEY_PWD) == null 
					|| paramDetails.get(PARAM_KEY_QUERY) == null || paramDetails.get(PARAM_KEY_EXPVALUE) == null ){
				logger.error("Configuration Error : The health check is not having all the mandatory parameters");
				return new StatusVO(Status.DOWN, "Configuration Error : The health check is not having all the mandatory parameters");
			}
			
			Class.forName(paramDetails.get(PARAM_KEY_JDBC_DRIVER_CLASS)).newInstance();
			 
			String connectionUrl = paramDetails.get(PARAM_KEY_JDBC_URL);
			logger.debug("Connecting URL : " + connectionUrl);
			
			connection = DriverManager.getConnection(connectionUrl, paramDetails.get(PARAM_KEY_USER), paramDetails.get(PARAM_KEY_PWD));
			if (connection == null) {
				logger.error("Unable to Connect to Database. URL = " + connectionUrl);
				return new StatusVO(Status.DOWN,"Unable to Connect to Database. URL = " + connectionUrl);
			}
			statement = connection.createStatement();
			rs = statement.executeQuery(paramDetails.get(PARAM_KEY_QUERY));
			if (!rs.next()){
				logger.error("SQL Query returned no result Query : " + paramDetails.get(PARAM_KEY_QUERY));
				return new StatusVO(Status.DOWN,"SQL Query returned no result Query : " + paramDetails.get(PARAM_KEY_QUERY));
			}
			if(paramDetails.get(PARAM_KEY_EXPVALUE).equals(rs.getString(1))) {
				return new StatusVO(Status.UP);
			} else {	
				logger.error("Expected Value does not match from Result returned. Returned result = " + rs.getString(1) + "; Expected result =" + paramDetails.get(PARAM_KEY_EXPVALUE));
				return new StatusVO(Status.DOWN,"Expected Value does not match from Result returned. Returned result = " + rs.getString(1) + "; Expected result =" + paramDetails.get(PARAM_KEY_EXPVALUE));
			}
			
		} catch(SQLException ue){
			logger.error("Got SQLException : Unable to Connect to Database. URL = " + paramDetails.get(PARAM_KEY_JDBC_URL), ue);
			return new StatusVO(Status.DOWN,"Got SQLException : Unable to Connect to Database. URL = " + paramDetails.get(PARAM_KEY_JDBC_URL));
		} catch (Exception e) {
			logger.error("Got Exception while connecting URL = " + paramDetails.get(PARAM_KEY_JDBC_URL), e);
			return new StatusVO(Status.DOWN,"Got Exception while connecting URL = " + paramDetails.get(PARAM_KEY_JDBC_URL));
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Got Exception while disconnecting connecting URL = " + paramDetails.get(PARAM_KEY_JDBC_URL), e);
			};
		}
	}
}
