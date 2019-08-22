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
package com.tmobile.kardio.surveiller.counters;

import java.util.List;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;

/**
 * Global Counter - Sum/Avg of all the values of a all environment
 */
public class GlobalCounterHandler extends CounterMetricHandler {
	private static final Logger logger = Logger.getLogger(GlobalCounterHandler.class);
	
	/**
	 * Get the metric value of the counter
	 */
	@Override
	public float getCounterMerticValue(CounterDetailVO counterDetails, List<CounterDetailVO> listCounterDetails) throws Exception {
		String[] environmentCounterIDs = counterDetails.getParameter1().split(",");
		float matchingCounters = 0f;
		float sumMatchingCounters = 0f;
		for(String environmentCounterID: environmentCounterIDs){
			int envCountId = Integer.parseInt(environmentCounterID);
			for(CounterDetailVO couDet: listCounterDetails){
				if(couDet.getEnvironmentCounterId() == envCountId){
					float metricValue = couDet.getMetricValue();
					//This block will execute only if any counter exited due to exception.
					if(metricValue<0){
						metricValue= DBQueryUtil.getTotalTransaction(envCountId);
					}
					matchingCounters++;
					sumMatchingCounters += metricValue;
				}
			}
		}
		
		logger.debug("EnvironmentCounterId=" + counterDetails.getEnvironmentCounterId() + "; matchingCounters = " 
						+ matchingCounters + "; sumMatchingCounters = " + sumMatchingCounters);
		
		if(counterDetails.getParameter2() != null 
				&& counterDetails.getParameter2().equalsIgnoreCase("AVG")
				&& matchingCounters > 0){
				return sumMatchingCounters/matchingCounters;				
		}
		return sumMatchingCounters;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}
}
