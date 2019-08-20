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
package com.tmobile.kardio.surveiller.counters;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.tmobile.kardio.surveiller.exception.GeneralException;
import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;

/**
 * Abstract Counter Metric Handler.
 */
public abstract class CounterMetricHandler { 
	/**
	 * Get Counter Metric Value.
	 * 
	 * @param counterDetails
	 * @return
	 */
	public abstract float getCounterMerticValue(CounterDetailVO counterDetails, List<CounterDetailVO> listCounterDetails) throws Exception;
	
	public abstract Logger getLogger();
	
	protected float getMetricValue(CounterDetailVO counterDetails)
			throws IOException {
		String prometheusUrl = counterDetails.getParameter1();
    	String query = counterDetails.getParameter2();

    	String prometheusResponse = RestCommunicationHandler.getResponse(
    			prometheusUrl + URLEncoder.encode(query, "UTF-8") , false, null, null);
    	
    	ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(prometheusResponse);
        if(rootNode.get("status") == null || !rootNode.get("status").getTextValue().equalsIgnoreCase("success")){
        	getLogger().error("Prometheus : Status Check Failed. Environment Counter Id = " + counterDetails.getEnvironmentCounterId() );
        	getLogger().error("Prometheus Response : \n" + prometheusResponse);
        	throw new GeneralException("Prometheus : Status Check Failed. Environment Counter Id = " + counterDetails.getEnvironmentCounterId());
        }
        if(rootNode.get("data") == null
        		|| rootNode.get("data").get("result") == null
        		|| !rootNode.get("data").get("result").isArray()){
        	getLogger().error("/data/result Node is missing in prometheus response. Environment Counter Id = " + counterDetails.getEnvironmentCounterId() );
        	getLogger().error("Prometheus Response : \n" + prometheusResponse);
        	throw new GeneralException("/data/result Node is missing in prometheus response. Environment Counter Id = " + counterDetails.getEnvironmentCounterId());
        }
        
        JsonNode valuesNode = null;
        for (final JsonNode arrayNode : rootNode.get("data").get("result")) {
        	valuesNode = arrayNode.get("value");
        }
        if(valuesNode == null){
        	getLogger().error("/data/result/values Node is missing in prometheus response. Environment Counter Id = " + counterDetails.getEnvironmentCounterId() );
        	getLogger().error("Prometheus Response : \n" + prometheusResponse);
        	throw new GeneralException("Number of Trasaction in prometheus response is 0. "
        			+ "Environment Counter Id = " + counterDetails.getEnvironmentCounterId()
        			+ "Environment Id = " + counterDetails.getEnvironmentId());
        }
        
        float metricValue = Float.parseFloat(valuesNode.get(1).getTextValue());
        return metricValue;
	}
}
