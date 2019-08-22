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
package com.tmobile.kardio.surveiller.main;

import java.util.Date;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.apidashboard.APIDashboardTask;
import com.tmobile.kardio.surveiller.apidashboard.K8sAPIDashboardTask;
import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.counters.CounterDataLoader;
import com.tmobile.kardio.surveiller.db.config.HibernateConfig;
import com.tmobile.kardio.surveiller.kubernetes.KubernetesBackUpTask;
import com.tmobile.kardio.surveiller.marathon.MarathonBackUpTask;
import com.tmobile.kardio.surveiller.prometheus.PrometheusMetricService;
import com.tmobile.kardio.surveiller.purgedata.PurgeOldData;
import com.tmobile.kardio.surveiller.tps.TPSDataLoadTask;
import com.tmobile.kardio.surveiller.tps.TPSLookupTask;
import com.tmobile.kardio.surveiller.util.CommonsUtil;

/**
 * Entry point for Surveiller process.
 */
public class Surveiller {

	private static final Logger logger = Logger.getLogger(Surveiller.class);

	private Surveiller() {}
	/**
	 * Entry point.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		String surveillerTaskName = "SurveillerStatusCheck";
		String taskStatus = "SUCCESS";
		Date reqStartTime = new Date();
		
		try {
			if(args == null || args.length == 0){
				HealthCheck.doSurveiller();
				return;
			}
			surveillerTaskName = args[0];
			switch (args[0].toUpperCase()) {
				case "DOMARATHONBACKUP":
					MarathonBackUpTask.doMarathonBackUp();
					break;
				case "DOCOUNTERDATALOAD":
					CounterDataLoader.doDataLoad();
					break;
				case "DOPURGEOLDDATA":
					PurgeOldData.doPurgeOldData();
					break;
				case "DOAPIDASHBOARDTASK":
					APIDashboardTask.doApiDashboardDataLoad();
					break;
				case "DOMESOSTPSDATALOAD":
					TPSDataLoadTask.doTpsDataLoad(SurveillerConstants.MESOS_PLATFORM);
					break;	
				case "DOKUBERNETESBACKUP":
					KubernetesBackUpTask.doKubernetesBackUp();
					break;	
				case "DOK8SAPIDASHBOARDTASK":
					K8sAPIDashboardTask.doK8sApiDashboardDataLoad();
					break;
				case "DOK8STPSLOOKUPLOADTASK":
					TPSLookupTask.doTpsLookupLoad();
					break;		
				case "DOK8STPSDATALOAD":
					TPSDataLoadTask.doTpsDataLoad(SurveillerConstants.K8S_PLATFORM);
					break;						
				default:
					HealthCheck.doSurveiller();
					break;
			}
			
		} catch (Exception ex) {
			logger.error("Surveiller task FAILED",ex);
			CommonsUtil.handleException(ex , surveillerTaskName);
			taskStatus = "FAILED";
			throw ex;
		}finally {
			HibernateConfig.closeSessionFactory();
			PrometheusMetricService.pushMetric(surveillerTaskName, taskStatus, reqStartTime);
		}
	}
}
