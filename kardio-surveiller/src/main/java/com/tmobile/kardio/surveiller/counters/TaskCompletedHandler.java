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

import java.util.List;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;

/**
 * Class to handle tasks to get total tasks complete from the Prometheus
 */
public class TaskCompletedHandler  extends CounterMetricHandler {

	private static final Logger logger = Logger.getLogger(TaskCompletedHandler.class);
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Function to get counter metric value total tasks completed
	 */
	@Override
	public float getCounterMerticValue(CounterDetailVO counterDetails, List<CounterDetailVO> listCounterDetails) throws Exception {
		float totalTrans = DBQueryUtil.getTotalTransaction(counterDetails.getEnvironmentCounterId());
        float metricValue = getMetricValue(counterDetails);
        totalTrans += metricValue;
        return totalTrans;
	}
}
