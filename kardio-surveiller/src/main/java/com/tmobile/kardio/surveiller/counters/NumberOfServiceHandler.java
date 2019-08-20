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
 * A Handler Class to find the number of API services running on each instance
 */
public class NumberOfServiceHandler extends CounterMetricHandler {
	private static final Logger logger = Logger.getLogger(NumberOfServiceHandler.class);

	/* (non-Javadoc)
	 * @see com.tmobile.gdm.surveiller.counters.CounterMetricHandler#getCounterMerticValue(com.tmobile.gdm.surveiller.vo.CounterDetailVO, java.util.List)
	 */
	/**
	 * Function to get counter metric value number of service
	 */
	@Override
	public float getCounterMerticValue(CounterDetailVO counterDetails, List<CounterDetailVO> listCounterDetails)
			throws Exception {
		// TODO Auto-generated method stub
		int numberofService = DBQueryUtil.getNumberOfServices(counterDetails);
		return numberofService;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

}
