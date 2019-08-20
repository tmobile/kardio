// =============================================================================
// Copyright 2019 T-Mobile, Inc. or its affiliates. All Rights Reserved.
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
// =============================================================================
GDMApp.factory('callApiService', callApiService);

callApiService.$inject = [ 'httpService', '$q', '$filter', '$rootScope'];

function callApiService(httpService,$q, $filter, $rootScope) {
	var callApiService = {};

	callApiService.callLogOut = function(token){
		var postData = token;
		var deferred = $q.defer();
		httpService.async(config.loginOutURL, "POST", {}, token).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getComponentDetails = function(environment, userId){
		var url = config.componentDetailsURL;
		var method = 'GET';
		var params = {
			"environment" : (environment == null)?prop.defaultEnvironmentName:environment
		};
		//(userId == undefined)||(userId == null)?'':userId
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getEnvironmentDetails = function(){
		var url = config.getEnvironmentsURL;
		var method = 'GET';
		var params = '';
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getStatusHistory = function(environment){
		var url = config.statusHistoryURL;
		var method = 'GET';
		var params = {
			"environment" : (environment == null)?prop.defaultEnvironmentName:environment
		};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.postMessages = function(environment, type, message, authToken){
		var url = config.updateMessagesURL;
		var method = 'PUT';
		var params = {
			"environment" : (environment == null)?prop.defaultEnvironmentName:environment,
			"type" : type,
			"messages": (message == null)? '':message,
			"authToken": authToken
		};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.postComponentMessages = function(environment, regId, compId, message, authToken, userName, userid, messageId){
		var url = config.updateComponentMessageURL;
		var method = 'POST';
		var region = '';
		if(regId == 1){
			region = 'West Region';
		} else if(regId == 2){
			region = 'East Region';
		}
		var data = {
			"componentid": compId,
			"environment" : (environment == null)?prop.defaultEnvironmentName:environment,
			"region" : region,
			"message": (message == null)? '':message,
			"userid" : userid,
			"username" : userName,
			"authToken": authToken,
			"messageId": messageId
		};
		var deferred = $q.defer();
		httpService.async(url,method, {}, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getComponentMessages = function(environment, regId, compId){
		var url = config.getCompMessageURL;
		var method = 'GET';
		var region = '';
		if(regId == 1){
			region = 'West Region';
		} else if(regId == 2){
			region = 'East Region';
		}

		var params = {
				"componentid": compId,
				"environment" : (environment == null)?prop.defaultEnvironmentName:environment,
				"region" : region
		};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.subscribeAlert = function(componentId, subsciptionText, type, environment, componentName, subsciptionType){
		var url = type == "subscribe" ? config.subscibeAlertURL:config.unSubscibeAlertURL;
		var method = 'POST';

		var data = {
				"componentId": componentId,
				"subsciptionVal" : subsciptionText,
				"environmentName" : (environment == null)?prop.defaultEnvironmentName:environment,
				"componentName" : componentName,
				"subsciptionType" : subsciptionType
		};
		var deferred = $q.defer();
		httpService.async(url,method, {},data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	}

	callApiService.getSubscribedEmails = function(environment, compId){
		var url = config.getSubscriptionMails;
		var params = {
				"componentId": compId,
				"environment" : (environment == null)?prop.defaultEnvironmentName:environment,
		}
		var deferred = $q.defer();
		httpService.async(url,"GET", params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	}

	callApiService.getGeneralCounters = function(environmentName, platform){
		var url = config.getGeneralCounters;
		var params = {};
		if(environmentName != null && environmentName.length > 0){
			params = {
					"environment" :environmentName,
					"platform" :platform
			};
		}
		else{
			params = {
					"platform" :platform
			};
		}
		var deferred = $q.defer();
		httpService.async(url, 'GET', params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};


	callApiService.getComponentMessagesHistory = function(environment, regId, compId, logDate){
		var url = config.getCompMessageHistoryURL;
		var method = 'GET';
		var region = '';
		if(regId == 1){
			region = 'West Region';
		} else if(regId == 2){
			region = 'East Region';
		}

		var params = {
				"componentid": compId,
				"environment" : (environment == null)?prop.defaultEnvironmentName:environment,
				"region" : region,
				"logDate" : logDate
		};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getAvailabilityPercent = function(environmentName, percentageType, platform,region){
		var url = config.getAvailabilityPercentURL;
		var params = {
				"environment" :(environmentName == null)?prop.defaultEnvironmentName:environmentName,
				"interval": (percentageType == null)?'D':percentageType,
				"platform": platform,
				"region":region
			};
		var deferred = $q.defer();
		httpService.async(url,"GET", params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getAllInfra = function(){
		var url = config.getAllInfraURL;
		var deferred = $q.defer();
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		httpService.async(url,"GET", param).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.addInfraComponent = function(componentName, compDesc,platform, parentComponentId){
		var url = config.addComponentURL;
		var method = 'POST';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = {
				"componentName": componentName,
				"compDesc" : compDesc,
				"platform" : platform,
				"parentComponentId" : parentComponentId,
				"componentType": "INFRA"
		};
		var deferred = $q.defer();
		httpService.async(url, method, param ,data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.editInfraComponent = function(componentId, componentName, compDesc,platform, parentComponentId, delInd){
		var url = config.editComponentURL;
		var method = 'PUT';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = {
				"componentId": componentId,
				"componentName": componentName,
				"compDesc" : compDesc,
				"platform" : platform,
				"parentComponentId" : parentComponentId,
				"delInd": delInd
		};
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.deleteInfraComponent = function(componentId){
		var url = config.deleteComponentURL;
		var method = 'PUT';
		var param = {
				"componentId": componentId,
				"authToken": $rootScope.userDetails.authToken
			};
		var data = {
				"authToken": $rootScope.userDetails.authToken
			};
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getAllHealthChecks = function(authToken){
		var url = config.getAllHealthChecksURL;
		var method = 'GET';
		var params = {
				"authToken": authToken
				};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getHealthCheckTypes = function(authToken){
		var url = config.getHealthCheckTypesURL;
		var method = 'GET';
		var params = {
				"authToken": authToken
				};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.deleteHealthCheck = function(authToken, healthCheckId){
		var url = config.deleteHealthCheckURL;
		var method = 'PUT';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = {
				"healthCheckId": healthCheckId,
			};
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	}

	callApiService.addHealthCheck = function(authToken, healthCheck){
		var url = config.addComponentURL;
		var method = 'POST';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = healthCheck;
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	}

	callApiService.editHeathCheck = function(authToken, healthCheck){
		var url = config.editHealthCheckURL;
		var method = 'PUT';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = healthCheck;
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	}

	callApiService.getAllGlobalSubscriptions = function(authToken){
		var url = config.getAllGlobalSubscriptionsURL;
		var method = 'GET';
		var params = {
				"authToken": authToken
				};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.addGlobalSubscription = function(authToken, subscription){
		var url = config.addGlobalSubscriptionURL;
		var method = 'POST';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = subscription;
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.deleteGlobalSubscription = function(authToken, subscriptionId){
		var url = config.deleteGlobalSubscriptionURL;
		var method = 'DELETE';
		var param = {
				"authToken": $rootScope.userDetails.authToken,
				"alertSubscriptionId": subscriptionId
			};
		var deferred = $q.defer();
		httpService.async(url, method, param).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getEnvironmentForLocks = function(authToken){
		var url = config.getEnvironmentForLocksURL;
		var method = 'GET';
		var params = {
				"authToken": authToken
				};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.addEnvironment = function(authToken, environment){
		var url = config.addEnvironmentURL;
		var method = 'POST';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = environment;
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.editEnvironment = function(authToken, environement){
		var url = config.editEnvironmentURL;
		var method = 'PUT';
		var param = {
				"authToken": $rootScope.userDetails.authToken
			};
		var data = environement;
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getAdminCounters = function(authToken){
		var url = config.getAdminCountersURL;
		var method = 'GET';
		var params = { "authToken": authToken };
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.updateAdminCounters = function(authToken, counterList){
		var url = config.editCountersURL;
		var params = { "authToken": authToken };
		var deferred = $q.defer();
		httpService.async(url, 'PUT', params, counterList).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.updateAdminEnvCounters = function(authToken, envCounter){
		var url = config.editEnvironmentCounterURL;
		var params = { "authToken": authToken };
		var deferred = $q.defer();
		httpService.async(url, 'PUT', params, envCounter).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getAppLookUp = function(authToken){
		var url = config.getAppLookUpURL;
		var method = 'GET';
		var params = { "authToken": authToken };
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.updateAdminEnvCounters = function(authToken, appLookUp){
		var url = config.editAppLookUpURL;
		var method = 'PUT';
		var param = {
				"authToken": authToken
			};
		var data = appLookUp;
		var deferred = $q.defer();
		httpService.async(url, method, param, data).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getPlatformComponents = function(environmentName,platform){
		var url = config.getPlatformComponentsURL;
		var method = 'GET';
		var params = {
		"environment": environmentName ,
		"platform" :platform};
		var deferred = $q.defer();
		httpService.async(url,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};
	//Code change for K8s services - added platform parameter
	callApiService.getContainerStats = function(environmentName, fromDate, toDate, componentIdList, isParentComponents,platform){
		var method = 'GET';
		var params = {
				"environment": environmentName,
				"startDate": fromDate,
				"endDate": toDate,
				"component": componentIdList == null? "": componentIdList,
				"isParentComponents": isParentComponents,
				"platform": platform
			};
		var deferred = $q.defer();
		httpService.async(config.getAppContainersURL,method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};
	//Code change for K8s services - added platform parameter
	callApiService.getApiStats = function(environmentName, fromDate, toDate, componentIdList,platform){
		var method = 'GET';
		var params = {
				"environment": environmentName,
				"startDate": fromDate,
				"endDate": toDate,
				"component": componentIdList == null? "": componentIdList,
				"platform" : platform
			};
		var deferred = $q.defer();
		httpService.async(config.getApplicationApisURL, method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getCurrentApis = function(environmentName, componentIdList,platform){
		var method = 'GET';
		var params = {
				"environment": environmentName,
				"platform":platform,
				"component": componentIdList == null? "": componentIdList
			};
		var deferred = $q.defer();
		httpService.async(config.getCurrentApisURL, method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getCurrentContainers = function(environmentName, componentIdList, isParent,platform){
		var method = 'GET';
		var params = {
				"environment": environmentName,
				"platform": platform,
				"component": componentIdList == null? "": componentIdList,
				"isParentComponents": isParent
			};
		var deferred = $q.defer();
		httpService.async(config.getCurrentContainersURL, method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

		callApiService.getCurrentNumberOfPods = function(environmentName, componentIdList, isParent){
		var method = 'GET';
		var params = {
				"environment": environmentName,
				"component": componentIdList == null? "": componentIdList,
				"isParentComponents": isParent
			};
		var deferred = $q.defer();
		httpService.async(config.getCurrentNumberOfPodsURL, method, params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getTpsAndLatency = function(environmentName,platform){
		var url = config.getCurrentTPSLatencyURL;
		var params = {
				"environment" :(environmentName == null)?prop.defaultEnvironmentName:environmentName,
				"platform" : platform
			};
		var deferred = $q.defer();
		httpService.async(url,"GET", params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getTotalTpsLatency = function(environmentName,fromDate, toDate,componentIdList,isParent,platform){
		var url = config.getTotalTpsLatencyURL;
		var params = {
				"environment": environmentName,
				"startDate": fromDate,
				"endDate": toDate,
				"component": componentIdList == null? "": componentIdList,
				"isParent" : isParent,
				"platform" : platform
			};
		var deferred = $q.defer();
		httpService.async(url,"GET", params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getTpsLatency = function(environmentName,componentIdList,platform,isParent){
		var url = config.getTpsLatencyURL;
		var params = {
				"environment": environmentName,
				"component": componentIdList == null? "": componentIdList,
				"platform" : platform,
				"isParent" : isParent
			};
		var deferred = $q.defer();
		httpService.async(url,"GET", params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};

	callApiService.getK8sPodsStatus= function(environmentName,fromDate, toDate,componentIdList,isParent){
		var url = config.getK8sPodsStatusURL;
		var params = {
				"environment": environmentName,
				"startDate": fromDate,
				"endDate": toDate,
				"component": componentIdList == null? "": componentIdList,
				"isParent" : isParent
			};
		var deferred = $q.defer();
		httpService.async(url,"GET", params).then(function (response) {
			deferred.resolve(response.data.responseContent);
		});
		return deferred.promise;
	};
	return callApiService;
}
