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
GDMApp.controller('adminHealthCtrl',adminHealthCtrl);

adminHealthCtrl.$inject = [ '$scope', 'callApiService', '$state', '$rootScope'];

function adminHealthCtrl($scope, callApiService, $state, $rootScope){
	var adminHealthCtrl = this;
	$rootScope.adminActiveTab = 2;
	adminHealthCtrl.component = { selected : null};
	adminHealthCtrl.MESSAGE_DELETE = "Work in Progress";
	adminHealthCtrl.healthCheckTypeVal = 0;
	adminHealthCtrl.envData = [];
	adminHealthCtrl.title = "Add Health Check";
	adminHealthCtrl.component = { selected : null};
	adminHealthCtrl.infraComponent = [];
	adminHealthCtrl.filterEnvironmentId = 1;
	adminHealthCtrl.filterRegionId = 1;
	adminHealthCtrl.regData = [{
		regionId: 1,
		regionName: "West Region"
	},{
		regionId: 2,
		regionName: "East Region"
	}];

	callApiService.getAllInfra().then(function(data){
		adminHealthCtrl.infraComponent = data;
	});

	callApiService.getEnvironmentDetails().then(function(data){
		adminHealthCtrl.envData = data;
	});

	adminHealthCtrl.sort = {
		column: 'healthCheckId',
		descending: true
	};

	adminHealthCtrl.selectedCls = function(column) {
		return column == adminHealthCtrl.sort.column && 'sort-' + adminHealthCtrl.sort.descending;
	};

	adminHealthCtrl.changeSorting = function(column) {
		var sort = adminHealthCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};

	adminHealthCtrl.head = {
		parentComponentName: "Service Name",
		componentName: "Health Check Name",
		platform: "Platform",
		environmentName: 'Environment',
		regionName: 'Region'
	};

	adminHealthCtrl.healthCheckArray = [];
	adminHealthCtrl.healthCheckTypesArray = [];
	adminHealthCtrl.healthCheckParamsArray = [];
	adminHealthCtrl.healthCheck = {};
	adminHealthCtrl.healthCheckParam = {};

	callApiService.getAllHealthChecks($rootScope.userDetails.authToken).then(function(data){
		adminHealthCtrl.healthCheckArray = data;
		adminHealthCtrl.healthCheckArray.forEach(function(item, index){
			item.parentComponentName = (item.parentComponentName == null) ? '': item.parentComponentName;
		});
	});

	callApiService.getHealthCheckTypes($rootScope.userDetails.authToken).then(function(data){
		adminHealthCtrl.healthCheckTypesArray = data;
	});
	adminHealthCtrl.goToComponentTab = function(){
		$state.go('adminComponent');
	};

	adminHealthCtrl.saveHealthCheck = function(healthCheck){
		if(isNaN(healthCheck.maxRetryCount)){
			$rootScope.$broadcast("showError","Max Retry Count is not a number");
		} else {
			healthCheck.healthCheckParamList = adminHealthCtrl.healthCheckParamsArray;
			if(healthCheck.healthCheckId == undefined){
				healthCheck.healthCheckId = 0;
				healthCheck.componentId = adminHealthCtrl.component.selected.componentId;
				healthCheck.componentName = adminHealthCtrl.component.selected.componentName;
			}
			callApiService.editHeathCheck($rootScope.userDetails.authToken, healthCheck).then(function(data){
				if(data.indexOf("Success") >= 0){
					adminHealthCtrl.reloadHealthChecks();
					adminHealthCtrl.cancelHealthCheck();
				}
			});
		}
	};

	adminHealthCtrl.editHealthCheck = function(healthCheck){
		adminHealthCtrl.isReadOnly = true;
		adminHealthCtrl.healthCheck = angular.copy(healthCheck);
		adminHealthCtrl.healthCheckParamsArray = angular.copy(healthCheck.healthCheckParamList);
		adminHealthCtrl.healthCheckTypeVal = healthCheck.healthCheckTypeId;
		for(var i=0; i<adminHealthCtrl.infraComponent.length; i++){
			if(adminHealthCtrl.infraComponent[i].componentId == healthCheck.componentId){
				adminHealthCtrl.component.selected = adminHealthCtrl.infraComponent[i];
			}
			if(adminHealthCtrl.infraComponent[i].componentId == healthCheck.parentComponentId){
				adminHealthCtrl.component.selectedParent = adminHealthCtrl.infraComponent[i];
			}
		}
		adminHealthCtrl.title = "Edit Heath Check";
		adminHealthCtrl.isAddHealthCheck = true;
	};

	adminHealthCtrl.addHealthCheckParam = function(healthCheckParam){
		if(healthCheckParam.healthCheckParamKey == null || healthCheckParam.healthCheckParamKey.trim() == ''
			|| healthCheckParam.healthCheckParamValue == null || healthCheckParam.healthCheckParamValue.trim() == ''){
			return;
		}
		healthCheckParam.healthCheckId = 0;
		adminHealthCtrl.healthCheckParamsArray.push(angular.copy(healthCheckParam));
		adminHealthCtrl.clearHealthCheckParam();
	};

	adminHealthCtrl.editHealthCheckParam = function(healthCheckParam){
		adminHealthCtrl.isEdit = true;
		adminHealthCtrl.healthCheckParam = angular.copy(healthCheckParam);
	};

	adminHealthCtrl.updateHealthCheckParam = function(healthCheckParam){
		for(var i=0; i<adminHealthCtrl.healthCheckParamsArray.length; i++){
			adminHealthCtrl.healthCheckParamsArray[i];
			if(adminHealthCtrl.healthCheckParamsArray[i].healthCheckParamId == healthCheckParam.healthCheckParamId){
				adminHealthCtrl.healthCheckParamsArray[i].healthCheckParamKey = healthCheckParam.healthCheckParamKey;
				adminHealthCtrl.healthCheckParamsArray[i].healthCheckParamValue = healthCheckParam.healthCheckParamValue;
			}
		}
		adminHealthCtrl.clearHealthCheckParam();
	};

	adminHealthCtrl.clearHealthCheckParam = function(){
		adminHealthCtrl.isEdit = false;
		adminHealthCtrl.healthCheckParam = {};
	};

	adminHealthCtrl.deleteHealthCheckParam = function(healthCheckParam){
		for(var i=0; i<adminHealthCtrl.healthCheckParamsArray.length; i++){
			if(adminHealthCtrl.healthCheckParamsArray[i].healthCheckParamId == healthCheckParam.healthCheckParamId){
				adminHealthCtrl.healthCheckParamsArray.splice(i,1);
			}
		}
	};

	adminHealthCtrl.cancelHealthCheck = function(){
		adminHealthCtrl.isReadOnly = false;
		adminHealthCtrl.isEdit = false;
		adminHealthCtrl.healthCheck = {};
		adminHealthCtrl.healthCheckParamsArray = [];
		adminHealthCtrl.healthCheckParam = {};
		adminHealthCtrl.component = {};
		adminHealthCtrl.title = "Add Heath Check";
		adminHealthCtrl.isAddHealthCheck = false;
	};

	adminHealthCtrl.reloadHealthChecks = function(){
		adminHealthCtrl.healthCheckArray = [];
		callApiService.getAllHealthChecks($rootScope.userDetails.authToken).then(function(data){
			adminHealthCtrl.healthCheckArray = data;
			adminHealthCtrl.healthCheckArray.forEach(function(item, index){
				item.parentComponentName = (item.parentComponentName == null) ? '': item.parentComponentName;
			});
		});
	};

	adminHealthCtrl.deleteHealthCheck = function(healthCheckId){
		callApiService.deleteHealthCheck($rootScope.userDetails.authToken, healthCheckId)
		adminHealthCtrl.reloadHealthChecks();
	};

	adminHealthCtrl.onEnvTabSelect = function(environmentId){
		adminHealthCtrl.filterEnvironmentId = environmentId;
	};

	adminHealthCtrl.onRegionTabSelect = function(regionId){
		adminHealthCtrl.filterRegionId = regionId;
	};

	return adminHealthCtrl;
};
