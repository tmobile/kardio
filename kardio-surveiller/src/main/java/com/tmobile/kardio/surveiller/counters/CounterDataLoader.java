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

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.util.CommonsUtil;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.ProxyUtil;
import com.tmobile.kardio.surveiller.vo.CounterDetailVO;
/**
 * Class contains the functions to load the counter data from PROMETHUES
 */
public class CounterDataLoader {
	
	private static final Logger logger = Logger.getLogger(CounterDataLoader.class);
	private CounterDataLoader() {}
	
	/**
	 * Function to load the data from PROMETHUES by query
	 * @throws Exception
	 */
	public static void doDataLoad() throws Exception{
		logger.info("************** STARTED Counter Data Loader **************");
		ProxyUtil.setProxy();
		ProxyUtil.disableCertificates();
		List<CounterDetailVO> listCounterDetails = DBQueryUtil.getCounterDetails();
		for(CounterDetailVO counterDetails: listCounterDetails){
		   try{
				int counterMetricTypeId = counterDetails.getCounterMetricTypeId();
				CounterMetricHandler metricHandler = null;
				if(counterDetails.getCounterMetricTypeClassName() != null)
				{
					ClassLoader classLoader = CounterDataLoader.class.getClassLoader();				
					@SuppressWarnings("unchecked")
					Class<CounterMetricHandler> objectClass = (Class<CounterMetricHandler>)classLoader.loadClass(counterDetails.getCounterMetricTypeClassName());			    		
				     metricHandler = objectClass.newInstance();
				}
				else{
					throw new ConfigurationException("No class configured for counterMetricTypeId = " + counterMetricTypeId);
				}		
				counterDetails.setMetricValue(metricHandler.getCounterMerticValue(counterDetails, listCounterDetails));
				DBQueryUtil.addCounterMertic(counterDetails.getEnvironmentCounterId(), counterDetails.getMetricValue());
				/**
				 * Code Changes to store Counter Metric History
				 */
				DBQueryUtil.addCounterMetricHistory(counterDetails.getEnvironmentCounterId(), counterDetails.getMetricValue());
		      }catch(Exception ex){
		    	  //Adding an abnormal value for identification
		    	  logger.error("Surveiller Counter DataLoad Failed for Environment Counter ID :-"+counterDetails.getEnvironmentCounterId()+":: Exception : "+ex);
		    	  counterDetails.setMetricValue(-1);
		    	  CommonsUtil.handleException(ex, "doCounterDataLoad");
		      }
		   }
		
		logger.info("************** COMPLETED Counter Data Loader **************");
	}
}
