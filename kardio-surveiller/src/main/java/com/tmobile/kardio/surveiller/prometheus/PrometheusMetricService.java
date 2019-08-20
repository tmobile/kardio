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
package com.tmobile.kardio.surveiller.prometheus;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.util.PropertyUtil;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

/**
 * Service class to Push Prometheus Metric
 */
public class PrometheusMetricService {
	private static final Logger logger = Logger.getLogger(PrometheusMetricService.class);
	
	private static String prometheusPushEnabled = PropertyUtil.getInstance().getValue(SurveillerConstants.PROMETHEUS_PUSH_ENABLED);
	
	private static String prometheusGateWayURL = PropertyUtil.getInstance().getValue(SurveillerConstants.PROMETHEUS_PUSH_GATEWAY_URL);
	
	private static final String SHD_APP_CODE = "shd";
	
	private PrometheusMetricService() {}
	
	/**
	 * Constructs the Push Metric and push it to Prometheus end point.
	 * 
	 * @param req
	 * @param httpStatusCode
	 */
	public static void pushMetric(String surveillerTaskName, String taskStatus, Date reqStartTime) {
		if(prometheusPushEnabled == null || !prometheusPushEnabled.equalsIgnoreCase("true")){
			return;
		}
		
		CollectorRegistry registry = createRegistry();
		
		setDurationTimerWithStatus(registry, reqStartTime, taskStatus);
		pushMetricToPrometheus(registry, surveillerTaskName);
	}
	
	/**
	 * Create CollectorRegistry
	 * @return
	 */
	private static CollectorRegistry createRegistry() {
		CollectorRegistry registry = new CollectorRegistry();
		return registry;
	}
	
	/**
	 * Creates a Metric with httpStatusCode and request time
	 * @param registry
	 * @param surveillerTaskName
	 * @param reqStartTime
	 * @param httpStatusCode
	 */
	private static void setDurationTimerWithStatus(CollectorRegistry registry, Date reqStartTime, String taskStatus){
		Gauge duration = Gauge.build("task_resp_time_milisec","Task Status and Duration of Task execution in miliseconds")
				.labelNames("apps","task_status").register(registry);
		long diffInMillies = System.currentTimeMillis() - reqStartTime.getTime();
	    
		duration.labels(SHD_APP_CODE, taskStatus).set(diffInMillies);
	}
	
	/**
	 * Push the Metric data to Prometheus end point.
	 * @param registry
	 */
	private static void pushMetricToPrometheus(final CollectorRegistry registry,final String surveillerTaskName){
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> {
			try {
				PushGateway pg = new PushGateway(prometheusGateWayURL);
				Map<String, String> groupingKey = new HashMap<String, String>();
				groupingKey.put("instance", InetAddress.getLocalHost().getHostName().replace(".corporate.t-mobile.com", ""));
				groupingKey.put("task_name", surveillerTaskName);
				pg.pushAdd(registry, "SHD_SURVEILLER", groupingKey);
			} catch (IOException e) {
				logger.error("Unable to push Prometheus Metric", e);
			}
		});
		executor.shutdown();
	}
}

