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
package com.tmobile.kardio.surveiller.constants;

/**
 * The interface contains all the constants used in this application.
 * 
 */
public class SurveillerConstants {
	private SurveillerConstants() {}
	
	public static final String CONFIG_DB_DRIVER_CLASS = "driver.class";
	public static final String CONFIG_DB_URL = "db.url";
	public static final String CONFIG_DB_USERNAME = "db.username";
	public static final String CONFIG_DB_PASSWORD = "db.password";
	public static final String CONFIG_DB_HIBERNATE_DIALECT = "hibernate.dialect";
	public static final String CONFIG_DB_HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";
	public static final String CONFIG_DB_HIBERNATE_SHOWSQL = "hibernate.show_sql";
	public static final String CONFIG_DB_HIBERNATE_POOLSIZE = "hibernate.connection.pool_size";
	public static final String CONFIG_DB_HIBERNATE_LX_INIT = "hibernate.enhancer.enableLazyInitialization";

	public static final String CONFIG_SURVEILLER_THREAD_COUNT = "surveiller.thread.count";
	
	public static final String CONFIG_SURVEILLER_ENVIRONMENT = "surveiller.environment";
	
	public static final String MAIL_FROM_ADDRESS = "mail.from.email";
	public static final String MAIL_SERVER_IP = "mail.server.ip";
	public static final String MAIL_SERVER_PORT = "mail.server.port";
	public static final String MAIL_SERVER_USERNAME = "mail.server.auth.username";
	public static final String MAIL_SERVER_PASSWORD = "mail.server.auth.password";
	public static final String MAIL_TASK_EXCEPTION_MAIL_TO = "mail.task.exception.mail.to";
	public static final String PROMETHEUS_PUSH_ENABLED = "prometheus.push.enabled";
	public static final String PROMETHEUS_PUSH_GATEWAY_URL = "prometheus.push.gateway.url";

	public static final String CONFIG_SSL_DISABLECERTIFICATE_CHECK = "ssl.disableCertificateCheck";

	public static final String CONFIG_PROXY_ENABLED = "proxy.enabled";
	public static final String CONFIG_PROXY_HTTP_HOST = "proxy.http.proxyHost";
	public static final String CONFIG_PROXY_HTTP_PORT = "proxy.http.proxyPort";
	public static final String CONFIG_PROXY_HTTPS_HOST = "proxy.https.proxyHost";
	public static final String CONFIG_PROXY_HTTPS_PORT = "proxy.https.proxyPort";
	public static final String CONFIG_PROXY_USERNAME = "proxy.username";
	public static final String CONFIG_PROXY_PASSWORD = "proxy.password";

	public static final String MAIL_MESSAGE_TEMPLATE = "mail.message.template";
	public static final String MAIL_SUBJECT_TEMPLATE = "mail.subject.template";
	
	public static final String PURGE_QUERY_FILENAME = "purge.data.filename";
	
	public static final String CONFIG_SLACK_URL = "slack.url";
	public static final String CONFIG_SLACK_TOKEN = "slack.auth.token";

	public static final int DB_MAX_FAILED_COUNT = 2147483647;

	public static final String GET_SLACK_CHANNEL_LIST_API = "channels.list";
	public static final String GET_SLACK_GROUP_LIST_API = "groups.list";
	public static final String POST_SLACK_MESSAGE = "chat.postMessage";
	
	public static final String K8S_API_DEPLOYMENT_PATH = "k8s.api.path.deployment";
	
	public static final String K8S_API_INGRESS_PATH = "k8s.api.path.ingress";
	
	public static final String K8S_API_LOGIN_API = "k8s.api.login.api";
	
	public static final String K8S_API_PODS_PATH = "k8s.api.path.pods";
	
	public static final String K8S_API_SERVICE_PATH = "k8s.api.path.service";
	
	public static final int SUBSCRIPTION_TYPE_EMAIL = 0;
	public static final int SUBSCRIPTION_TYPE_SLACK_WEB_HOOK = 1;
	public static final int SUBSCRIPTION_TYPE_SLACK_CHANNEL = 2;
	public static final int SUBSCRIPTION_TYPE_1C_WEBHOOK = 3;
	
	public static final int COUNTER_METRIC_TYPE_TOTAL_TRANSACTIONS = 1;
	public static final int COUNTER_METRIC_TYPE_DIRECT_PROMETHUES_QUERY = 2;
	public static final int COUNTER_METRIC_TYPE_PERCENTAGE_UP_TIME = 3;
	public static final int COUNTER_METRIC_TYPE_GLOBAL_COUNTER = 4;
	public static final int COUNTER_METRIC_TYPE_TASK_COMPLETE_METRIC = 5;
	public static final int COUNTER_METRIC_TYPE_TRANSACTION_PER_SEC = 6;
	public static final int COUNTER_METRIC_TYPE_NUMBER_OF_SERVICES = 7;
	
	public static final String PLAB_TPS_PROMETHEOUS_QUERY = "plab.tps.prometheous.query";
	public static final String STAGE_TPS_PROMETHEOUS_QUERY = "stage.tps.prometheous.query";
	public static final String PRODSD_TPS_PROMETHEOUS_QUERY = "prodsd.tps.prometheous.query";
	public static final String PRODCDE_TPS_PROMETHEOUS_QUERY = "prodcde.tps.prometheous.query";
	
	
	public static final String PLAB_LATENCY_PROMETHEOUS_QUERY = "plab.latency.prometheous.query";
	public static final String STAGE_LATENCY_PROMETHEOUS_QUERY = "stage.latency.prometheous.query";
	public static final String PRODSD_LATENCY_PROMETHEOUS_QUERY = "prodsd.latency.prometheous.query";
	public static final String PRODCDE_LATENCY_PROMETHEOUS_QUERY = "prodcde.latency.prometheous.query";
	
	public static final String K8S_STAGE_TPS_PROMETHEOUS_QUERY = "k8s.stage.tps.prometheous.query";
	public static final String K8S_PRODSD_TPS_PROMETHEOUS_QUERY = "k8s.prodsd.tps.prometheous.query";
	
	public static final String MESOS_PLATFORM = "Mesos";
	public static final String K8S_PLATFORM = "K8s";
	public static final String NUM_OF_TASKS_RUNNING = "tasksRunning";
	public static String URL_SUFFIX_BACKEND = "_backend";
	public static final String LABEL_HTTP_PORT_IDX_0_NAME = "HTTP_PORT_IDX_0_NAME";
	public static final String LABEL_HTTP_PATH_URL = "HTTP_PATH_URL";
	public static final String NUM_OF_TASKS_STAGED = "tasksStaged";
	public static final String BEARER_TOKEN = "Bearer ";
}
