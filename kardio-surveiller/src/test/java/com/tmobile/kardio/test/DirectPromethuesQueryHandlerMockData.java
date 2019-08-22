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
package com.tmobile.kardio.test;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tmobile.kardio.surveiller.vo.CounterDetailVO;



/**
 * @author U29842
 *
 */
public class DirectPromethuesQueryHandlerMockData {

	public String mockSingleVauleJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "success");
		JSONObject dataObj = new JSONObject();
		JSONObject  resultTypeObj = new JSONObject();
		resultTypeObj.put("resultType", "vector");
		JSONArray resultArr = new JSONArray();
		JSONObject resultObj1 = new JSONObject();
		//JSONObject value = new JSONObject();
		JSONArray valueArr = new JSONArray();
		valueArr.put("11111");
		valueArr.put("1333");
		resultObj1.put("value", valueArr);
		resultArr.put(resultObj1);
		dataObj.put("resultType", "vector");
		dataObj.put("result", resultArr);
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public CounterDetailVO getMockedCounterDetails(){
		CounterDetailVO counterDetails = new CounterDetailVO();
		counterDetails.setEnvironmentCounterId(6);
		counterDetails.setEnvironmentId(1);
		counterDetails.setCounterId(1);
		counterDetails.setCounterMetricTypeId(1);
		counterDetails.setParameter1("promentheous url");
		counterDetails.setParameter2("Prometheous query");
		return counterDetails;
	}
	
	public String mockValueNodeEmptyPromJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "success");
		JSONObject dataObj = new JSONObject();
		JSONObject  resultTypeObj = new JSONObject();
		resultTypeObj.put("resultType", "vector");
		JSONArray resultArr = new JSONArray();
		
		JSONObject resultObj1 = new JSONObject();
		//JSONObject value = new JSONObject();
		//resultObj1.put("value", null);
		resultArr.put(resultObj1);
		dataObj.put("resultType", "vector");
		dataObj.put("result", resultArr);
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public String mockDataNodeEmptyPromJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "success");
		JSONObject dataObj = new JSONObject();
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public String mockDataStatusFailedPromJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "failed");
		JSONObject dataObj = new JSONObject();
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public String mockDataNodeNullPromJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "success");
		JSONObject dataObj = null;
		JSONObject  resultTypeObj = new JSONObject();
		resultTypeObj.put("resultType", "vector");
		JSONArray resultArr = null;
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public String mockStatusNullPromJson(){
		JSONObject rootObj = new JSONObject();
		//rootObj.put("status", "success");
		JSONObject dataObj = null;
		JSONObject  resultTypeObj = new JSONObject();
		resultTypeObj.put("resultType", "vector");
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public String mockMatrixVauleZeroJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "success");
		JSONObject dataObj = new JSONObject();
		JSONObject  resultTypeObj = new JSONObject();
		resultTypeObj.put("resultType", "vector");
		JSONArray resultArr = new JSONArray();
		
		JSONObject resultObj1 = new JSONObject();
		//JSONObject value = new JSONObject();
		JSONArray valueArr = new JSONArray();
		valueArr.put("11111");
		valueArr.put("0");
		resultObj1.put("value", valueArr);
		resultArr.put(resultObj1);
		dataObj.put("resultType", "vector");
		dataObj.put("result", resultArr);
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
	
	public String mockMultipleVauleJson(){
		JSONObject rootObj = new JSONObject();
		rootObj.put("status", "success");
		JSONObject dataObj = new JSONObject();
		JSONObject  resultTypeObj = new JSONObject();
		resultTypeObj.put("resultType", "vector");
		JSONArray resultArr = new JSONArray();
		
		JSONObject resultObj1 = new JSONObject();
		//JSONObject value = new JSONObject();
		JSONArray valueArr = new JSONArray();
		valueArr.put("11111");
		valueArr.put("1333");
		resultObj1.put("values", valueArr);
		resultArr.put(resultObj1);
		dataObj.put("resultType", "vector");
		dataObj.put("result", resultArr);
		rootObj.put("data", dataObj);
		return rootObj.toString();
	}
}
