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

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Class to handle the JSON validation
 */
public class JSONUtil {
	private static final Logger logger = Logger.getLogger(JSONUtil.class);
	
	private JSONUtil() {}
	/**
	 * Function to validate the json string
	 * @param json
	 * @return
	 */
	public static boolean isJSONValid(String json) {
		 boolean valid = false;
		   try {
		      final JsonParser parser = new ObjectMapper().getJsonFactory()
		            .createJsonParser(json);
		      while (parser.nextToken() != null) {
		      }
		      valid = true;
		   } catch (JsonParseException jpe) {
			   logger.error("Invalid JSON String JsonParseException");
		   } catch (IOException ioe) {
			   logger.error("Invalid JSON String IOException");
		   }

		   return valid;
	}
}
