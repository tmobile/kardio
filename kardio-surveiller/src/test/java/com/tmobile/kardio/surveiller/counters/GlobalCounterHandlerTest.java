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
package com.tmobile.kardio.surveiller.counters;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tmobile.kardio.surveiller.vo.CounterDetailVO;

/**
 * @author U29842
 *
 */
public class GlobalCounterHandlerTest {

	/**
	 * @throws Exception
	 */
	@Test
	public void testGetCounterMerticValue() throws Exception{
		
		CounterDetailVO counterDetails = new CounterDetailVO();
		counterDetails.setEnvironmentCounterId(6);
		counterDetails.setEnvironmentId(1);
		counterDetails.setCounterId(1);
		counterDetails.setCounterMetricTypeId(1);
		counterDetails.setParameter1("4,6");
		counterDetails.setParameter2("AVG");
		List<CounterDetailVO> counterList = new ArrayList<CounterDetailVO>();
		counterList.add(counterDetails);
		GlobalCounterHandler gcHandler = new GlobalCounterHandler();
		
		gcHandler.getCounterMerticValue(counterDetails, counterList);
		
	}
	
}
