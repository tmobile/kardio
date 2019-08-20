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
package com.tmobile.kardio.bean;

import com.tmobile.kardio.constants.Constants;

/**
 * Main Response Object. This stores the reply status, message and response content Main Response Object.
 * 
 */
public class GDMResponse {
    private String status;
    private String message;
    private Object responseContent;
    
    public GDMResponse() {
    	
    }

    /**
     * General Error Case constructor.
     * 
     * @param ex
     */
    public GDMResponse(Throwable ex) {
        status = Constants.STATUS_FAILED_GENERAL;
        message = Constants.MESSAGE_FAILED_GENERAL;
        responseContent = ex.getMessage();
    }

    /**
     * General Response Case constructor.
     * 
     * @param responseContent
     */
    public GDMResponse(Object responseContent) {
        status = Constants.STATUS_SUCCESS;
        message = Constants.MESSAGE_SUCCESS;
        this.responseContent = responseContent;
    }

    /**
     * Custom Response constructor.
     */
    public GDMResponse(String status, String message, Object responseContent) {
        this.status = status;
        this.message = message;
        this.responseContent = responseContent;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the responseContent
     */
    public Object getResponseContent() {
        return responseContent;
    }

    /**
     * @param responseContent
     *            the responseContent to set
     */
    public void setResponseContent(Object responseContent) {
        this.responseContent = responseContent;
    }
}
