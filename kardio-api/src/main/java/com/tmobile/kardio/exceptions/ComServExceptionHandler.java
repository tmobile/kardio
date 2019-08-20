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
package com.tmobile.kardio.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tmobile.kardio.bean.GDMResponse;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.prometheus.PrometheusMetricService;

/**
 * 
 * This class is a global Exception handler for all our controllers
 * 
 */

@ControllerAdvice
public class ComServExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(ComServExceptionHandler.class);

    @ExceptionHandler(AppSessionExpiredException.class)
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, AppSessionExpiredException ex) {
        log.error("Got AppSessionExpiredException", ex);
        GDMResponse errorResponse = new GDMResponse(Constants.STATUS_SESSION_TERMINATED, "Your current session has expired. Please login again",
                ex.getMessage());
        PrometheusMetricService.setRequestStatus(req, "session-expired");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(LoginFailedException.class)
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, LoginFailedException ex) {
        log.error("Got LoginFailedException", ex);
        GDMResponse errorResponse = new GDMResponse(Constants.STATUS_FAILED_LOGIN, "Login Failed : " + ex.getMessage(), ex.getMessage());
        PrometheusMetricService.setRequestStatus(req, "login-failed");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(ValidationFailedException.class)
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, ValidationFailedException ex) {
        log.error("Got ValidationFailedException", ex);
        GDMResponse errorResponse = new GDMResponse(Constants.STATUS_VALIDATION_ERROR, ex.getMessage(), ex.getMessage());
        PrometheusMetricService.setRequestStatus(req, "validation-failed");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, Throwable ex) {
        log.error("Got Exception", ex);
        GDMResponse errorResponse = new GDMResponse(ex);
        PrometheusMetricService.setRequestStatus(req, "unknown-error");
        return new ResponseEntity<Object>(errorResponse, HttpStatus.OK);
    }

}
