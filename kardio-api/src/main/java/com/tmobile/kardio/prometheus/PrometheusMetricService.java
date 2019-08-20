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
package com.tmobile.kardio.prometheus;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.tmobile.kardio.constants.Constants;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

/**
 * Service class to Push Prometheus Metric
 * 
 */
@Service
@PropertySource("classpath:application.properties")
public class PrometheusMetricService {
    private static Logger log = LoggerFactory.getLogger(PrometheusMetricService.class);

    @Value("${prometheus.push.enabled}")
    private String prometheusPushEnabled;

    @Value("${prometheus.push.gateway.url}")
    private String prometheusGateWayURL;
    
    private final static String SHD_APP_CODE = "shd";

    /**
     * Sets the Request start time and endpoint name to HttpServletRequest attribute.
     * 
     * @param req
     * @param endPointName
     */
    public void initializeMetricData(HttpServletRequest req, String endPointName) {
        if (prometheusPushEnabled == null || !prometheusPushEnabled.equalsIgnoreCase("true")) {
            return;
        }
        req.setAttribute(Constants.PROMETHEUS_APP_ENDPOINT_NAME, endPointName);
        req.setAttribute(Constants.PROMETHEUS_REQ_TIMER, new Date());
        setRequestStatus(req, "success");
    }

    /**
     * Sets the status of request.
     * 
     * @param req
     * @param requestStatus
     */
    public static void setRequestStatus(HttpServletRequest req, String requestStatus) {
        req.setAttribute(Constants.PROMETHEUS_REQ_STATUS, requestStatus);
    }

    /**
     * Constructs the Push Metric and push it to Prometheus end point.
     * 
     * @param req
     * @param httpStatusCode
     */
    public void pushMetric(HttpServletRequest req) {
        if (prometheusPushEnabled == null || !prometheusPushEnabled.equalsIgnoreCase("true")) {
            return;
        }

        String requestStatus = (String) req.getAttribute(Constants.PROMETHEUS_REQ_STATUS);
        String endPointName = (String) req.getAttribute(Constants.PROMETHEUS_APP_ENDPOINT_NAME);
        Date reqStartTime = (Date) req.getAttribute(Constants.PROMETHEUS_REQ_TIMER);
        CollectorRegistry registry = createRegistry();

        setDurationTimerWithStatus(registry, reqStartTime, requestStatus);
        pushMetricToPrometheus(registry, endPointName);
    }

    /**
     * Create CollectorRegistry
     * 
     * @return
     */
    private CollectorRegistry createRegistry() {
        CollectorRegistry registry = new CollectorRegistry();
        return registry;
    }

    /**
     * Creates a Metric with httpStatusCode and request time
     * 
     * @param registry
     * @param endPointName
     * @param reqStartTime
     * @param httpStatusCode
     */
    private void setDurationTimerWithStatus(CollectorRegistry registry, Date reqStartTime, String requestStatus) {
        Gauge duration = Gauge.build("endpoint_resp_time_milisec", "Request Status and Duration of Endpoint execution in miliseconds")
                .labelNames("apps","http_status").register(registry);
        long diffInMillies = System.currentTimeMillis() - reqStartTime.getTime();

        duration.labels(SHD_APP_CODE, requestStatus).set(diffInMillies);
    }

    /**
     * Push the Metric data to Prometheus end point.
     * 
     * @param registry
     */
    private void pushMetricToPrometheus(final CollectorRegistry registry, final String endPointName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PushGateway pg = new PushGateway(prometheusGateWayURL);
                    Map<String, String> groupingKey = new HashMap<String, String>();
                    groupingKey.put("instance", InetAddress.getLocalHost().getHostName().replace(".corporate.t-mobile.com", ""));
                    groupingKey.put("endpoint_name", endPointName);
                    pg.pushAdd(registry, "SHD_API", groupingKey);
                } catch (IOException e) {
                    log.error("Unable to push Prometheus Metric", e);
                }
            }
        });
        executor.shutdown();
    }
}
