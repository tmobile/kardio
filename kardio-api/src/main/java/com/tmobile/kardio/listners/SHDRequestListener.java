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
package com.tmobile.kardio.listners;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.tmobile.kardio.prometheus.PrometheusMetricService;

/**
 * Request listener for Prometheus
 * 
 */
@Configuration
@WebListener
public class SHDRequestListener implements ServletRequestListener {
    @Autowired
    private PrometheusMetricService prometheusMetricService;

    @Override
    public void requestDestroyed(ServletRequestEvent requestEvent) {
        HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
        prometheusMetricService.pushMetric(request);
    }

    @Override
    public void requestInitialized(ServletRequestEvent requestEvent) {
        HttpServletRequest request = (HttpServletRequest) requestEvent.getServletRequest();
        String endPointName = request.getRequestURI();
        endPointName = endPointName.substring(1);
        endPointName = endPointName.replaceAll("/", "_");
        prometheusMetricService.initializeMetricData(request, endPointName);
    }
}
