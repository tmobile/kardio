// =============================================================================
// Copyright 2019 T-Mobile USA, Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License.  You may obtain a copy
// of the License at
// 
//   http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
// License for the specific language governing permissions and limitations under
// the License.
// See the LICENSE file for additional language around disclaimer of warranties.
// Trademark Disclaimer: Neither the name of "T-Mobile, USA" nor the names of
// its contributors may be used to endorse or promote products derived from this
// software without specific prior written permission.
// =============================================================================
// If the API is hosted on a different server, change 'apiHost' and 'apiPort' to point to the API server location.
//
// Leave 'apiHost' empty to use the same hostname as the UI.
// Default API port is 7070. Please update if you choose to run API server on a different port.
// eg: var apiHost = "https://api.kardio.io
var apiHost = "";
var apiPort = 7070;
if (!apiHost){
  apiHost = window.location.protocol + "//" + window.location.hostname + ":" + apiPort;
}
var config = {
        loginURL: apiHost+"/doLogin",
        loginOutURL: apiHost+"/doLogout",
        componentDetailsURL: apiHost+"/component",
        statusHistoryURL: apiHost+"/componentHistory",
        updateMessagesURL: apiHost+"/updateStatusMsg",
        updateComponentMessageURL: apiHost+"/saveCompMessage",
        getCompMessageURL: apiHost+"/componentMessage",
        getEnvironmentsURL: apiHost+"/getEnvironments",
        addEnvironmentURL: apiHost+"/addEnvironment",
        subscibeAlertURL: apiHost+"/subscribeAlerts",
        unSubscibeAlertURL: apiHost+"/unsubscribeAlerts",
        confirmSubscription: apiHost+"/confirmSubscription",
        deleteSubscription: apiHost+"/deletesubscription",
        getSubscriptionMails: apiHost+"/getSubscriptionMails",
        getGeneralCounters: apiHost+"/getCounterMetrix",
        getCompMessageHistoryURL: apiHost+"/getCompMessageHistory",
        getAvailabilityPercentURL: apiHost+"/getAvailabilityPercentage",
        getAllInfraURL: apiHost+"/getAllComponent",
        addComponentURL: apiHost+"/addComponent",
        editComponentURL: apiHost+"/editComponent",
        deleteComponentURL: apiHost+"/deleteComponent",
        getAllHealthChecksURL: apiHost+"/getAllHealthChecks",
        getHealthCheckTypesURL: apiHost+"/getHealthCheckTypes",
        deleteHealthCheckURL: apiHost+"/deleteHealthCheck",
        editHealthCheckURL: apiHost+"/editHealthCheck",
        getAllGlobalSubscriptionsURL: apiHost+"/getGlobalSubscription",
        addGlobalSubscriptionURL: apiHost+"/subscribeGlobalAlerts",
        deleteGlobalSubscriptionURL: apiHost+"/deleteGlobalSubscription",
        getEnvironmentForLocksURL: apiHost+"/getEnvironmentLock",
        editEnvironmentURL: apiHost+"/environmentLock",
        getAdminCountersURL: apiHost+"/getAllCounters",
        editCountersURL: apiHost+"/editCounters",
        editEnvironmentCounterURL: apiHost+"/editEnvCounterDetails",
        getAppLookUpURL: apiHost+"/getAppFullName",
        editAppLookUpURL: apiHost+"/editAppFullName",
        getMarathonComponentsURL: apiHost+"/getMarathonComponents",
        getAppContainersURL: apiHost+"/getAppContainers",
        getApplicationApisURL : apiHost+"/getApplicationApis",
        getCurrentApisURL : apiHost+"/getCurrentApis",
        getCurrentContainersURL : apiHost+"/getCurrentContainers",
        getCurrentTPSLatencyURL : apiHost+"/getAllCurrentTpsLatency",
        getTotalTpsLatencyURL : apiHost+"/getTotalTpsLatency",
        getTpsLatencyURL : apiHost+"/getTpsLatency",
        getK8sPodsStatusURL : apiHost+"/getK8sPodsStatus",
        getCurrentNumberOfPodsURL : apiHost+"/getCurrentPods",

};

