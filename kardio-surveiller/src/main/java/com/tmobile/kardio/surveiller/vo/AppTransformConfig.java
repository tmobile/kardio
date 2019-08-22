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
package com.tmobile.kardio.surveiller.vo;

import java.util.Map;
import java.util.Set;

/**
 * 
 * Class that holds the structure of transform.json that 
 * contains the config to load the application from marthon as infra component
 *
 */
public class AppTransformConfig {

	Map<String, String> transformName;
	Set<String> convertToInfra;
	
	/**
	 * @return the transformName
	 */
	public Map<String, String> getTransformName() {
		return transformName;
	}
	/**
	 * @param transformName the transformName to set
	 */
	public void setTransformName(Map<String, String> transformName) {
		this.transformName = transformName;
	}
	/**
	 * @return the convertToInfra
	 */
	public Set<String> getConvertToInfra() {
		return convertToInfra;
	}
	/**
	 * @param convertToInfra the convertToInfra to set
	 */
	public void setConvertToInfra(Set<String> convertToInfra) {
		this.convertToInfra = convertToInfra;
	}

}
