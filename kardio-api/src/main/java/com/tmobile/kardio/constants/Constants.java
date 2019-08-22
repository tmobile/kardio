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
package com.tmobile.kardio.constants;

/**
 * This class holds all the constants
 */
public class Constants {
    public static final String STATUS_SUCCESS = "GDM000";
    public static final String STATUS_FAILED_GENERAL = "GDM001";
    public static final String STATUS_FAILED_LOGIN = "GDM002";
    public static final String STATUS_SESSION_TERMINATED = "GDM003";
    public static final String STATUS_VALIDATION_ERROR = "GDM004";
    public static final String MESSAGE_SUCCESS = "Success";
    public static final String MESSAGE_FAILED_GENERAL = "There was an error in processing your request";

    public static final String PROMETHEUS_APP_ENDPOINT_NAME = "PROMETHEUS_APP_ENDPOINT_NAME";
    public static final String PROMETHEUS_REQ_TIMER = "PROMETHEUS_REQ_TIMER";
    public static final String PROMETHEUS_REQ_STATUS = "PROMETHEUS_REQ_STATUS";

    public static final long SESSION_EXPIRATION_TIME_MINUTES = 60L;

    public static final int SUBSCRIPTION_TYPE_ID_EMAIL = 0;
    public static final int SUBSCRIPTION_TYPE_ID_SLACK_WEBHOOK = 1;
    public static final int SUBSCRIPTION_TYPE_ID_SLACK_CHNL = 2;
    public static final int SUBSCRIPTION_TYPE_ID_ONECONSOLE_WEBHOOK = 3;
    public static final String SUBSCRIPTION_TYPE_DESC_EMAIL = "email";
    public static final String SUBSCRIPTION_TYPE_DESC_SLACK_WEBHOOK = "slack";
    public static final String SUBSCRIPTION_TYPE_DESC_SLACK_CHNL = "slackChannel";
    public static final String SUBSCRIPTION_TYPE_DESC_ONECONSOLE_WEBHOOK = "oneConsoleWebhook";
    
    public static final int GLOBAL_SUBSCRIPTION_TYPE_ID_INFRA = 1;
    public static final int GLOBAL_SUBSCRIPTION_TYPE_ID_APP = 2;
    public static final int GLOBAL_SUBSCRIPTION_TYPE_ID_BOTH = 0;
    public static final String GLOBAL_SUBSCRIPTION_TYPE_DESC_INFRA = "Infra";
    public static final String GLOBAL_SUBSCRIPTION_TYPE_DESC_APP = "App";
    public static final String GLOBAL_SUBSCRIPTION_TYPE_DESC_BOTH = "Both";

    public static final String MESSAGE_TYPE_GENERAL = "general";
    public static final String MESSAGE_TYPE_APP = "app";
    public static final String MESSAGE_TYPE_INFRA = "infra";
    public static final String MESSAGE_TYPE_COUNTER = "counter";

    public static final String GET_SLACK_CHANNEL_LIST_API = "channels.list";
    public static final String GET_SLACK_GROUP_LIST_API = "groups.list";
    public static final String POST_SLACK_MESSAGE = "chat.postMessage";

    public static final String PLATFORM_K8S = "K8s";
	public static final String PLATFORM_ALL = "All";
	public static final String PLATFORM_MESOS = "Mesos";
}
