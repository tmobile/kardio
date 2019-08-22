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
GDMApp.controller('dashboardController',dashboardController);

dashboardController.$inject = [ '$scope', '$filter','callApiService', '$rootScope', '$interval', '$sce'];

function dashboardController($scope, $filter, callApiService, $rootScope, $interval, $sce){
	var dashboardCtrl = this;
	var pform={};
	dashboardCtrl.infraIsCollapsed = false;
	dashboardCtrl.appIsCollapsed = false;
	dashboardCtrl.data = [];
	dashboardCtrl.recentFilter={"recentEvent": true};
	dashboardCtrl.nonRecentFilter={"recentEvent": false};

	dashboardCtrl.tableHeaderInfra = $rootScope.props.tableHeaderInfra;
	dashboardCtrl.tableHeaderApp = $rootScope.props.tableHeaderApp;
	dashboardCtrl.tableHeaderApi = $rootScope.props.tableHeaderApi;
	dashboardCtrl.regionWest = "West Region";
	dashboardCtrl.regionEast = "East Region";
	dashboardCtrl.startDate = "";
	
	dashboardCtrl.processDashboardData = function(data) {
		$rootScope.lastUpdatedTime = new Date();
		dashboardCtrl.dashboardData = data;

		dashboardCtrl.infraRecentData = $filter('orderBy')(
				$filter('filter')(data.infraComponents || [], dashboardCtrl.recentFilter),'componentName');
		dashboardCtrl.infraNonRecentData = $filter('orderBy')(
				$filter('filter')(data.infraComponents || [], dashboardCtrl.nonRecentFilter),'componentName');
		dashboardCtrl.appData = $filter('orderBy')(data.appComponents,'componentName');
		dashboardCtrl.apiRecentData = $filter('orderBy')(
				$filter('filter')(data.apiComponents, dashboardCtrl.recentFilter),['parentComponentName','componentName']);
		dashboardCtrl.apiNonRecentData = $filter('orderBy')(
				$filter('filter')(data.apiComponents, dashboardCtrl.nonRecentFilter),['parentComponentName','componentName']);
		dashboardCtrl.envMessageData =  data.envMessages;
		$rootScope.counterMessage = data.envMessages.counterMessage;
		console.log($rootScope.counterMessage);
		var prevdate = new Date();
		prevdate.setDate(prevdate.getDate() - 1);
		dashboardCtrl.startDate =  $filter('date')(prevdate, 'yyyy-MM-dd');
		dashboardCtrl.endDate = $filter('date')($rootScope.lastUpdatedTime,'yyyy-MM-dd');
				callApiService.getApiStats($rootScope.environment, dashboardCtrl.startDate,dashboardCtrl.endDate,"",$rootScope.globalPlatform).then(function(data){
			dashboardCtrl.serviceContainer = data;
				});
		
	}
    
	$rootScope.changeInterval = function(){
		console.log("Change Interval");
		$interval.cancel(dashboardCtrl.intervalPromise);
		dashboardCtrl.intervalPromise = undefined;
		dashboardCtrl.intervalPromise = $interval(function(){
			if(!angular.element(".modal-backdrop").is(':visible')){
				callApiService.getComponentDetails($rootScope.environment, $rootScope.userDetails.userId).then(dashboardCtrl.processDashboardData);
			}
		}, $rootScope.timeInterval);
	};

	if(!$rootScope.dashboardCtrlWarcherAdded){
		$rootScope.dashboardCtrlWarcherAdded = true;
		$rootScope.$watch('environment', function(newValue, oldValue){
			if(newValue != null && oldValue!= null && newValue != oldValue){
				callApiService.getComponentDetails($rootScope.environment, $rootScope.userDetails.userId).then(dashboardCtrl.processDashboardData);
				callApiService.getTpsAndLatency($rootScope.environment,$rootScope.globalPlatform).then(function(data){
					dashboardCtrl.tpsAndLatency = data;
				});
				callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeWest,$rootScope.globalPlatform,dashboardCtrl.regionWest).then(function(data){
					dashboardCtrl.componentPercentageWest = data;
				});
				callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeEast,$rootScope.globalPlatform,dashboardCtrl.regionEast).then(function(data){
					dashboardCtrl.componentPercentageEast = data;
				});
				dashboardCtrl.startDate =  $filter('date')($rootScope.lastUpdatedTime, 'yyyy-MM-dd');
	            console.log(dashboardCtrl.startDate); 
				callApiService.getApiStats($rootScope.environment, dashboardCtrl.startDate,dashboardCtrl.startDate,"",$rootScope.globalPlatform).then(function(data){
			dashboardCtrl.serviceContainer = data;
				});
			}
		});
		
	}

		if($rootScope.globalPlatform != null && $rootScope.globalPlatform != "" && !$rootScope.dashboardCtrlglobalPlatformWatcherAdded){
			$rootScope.dashboardCtrlglobalPlatformWatcherAdded = true;
			$rootScope.$watch('globalPlatform', function(newValue, oldValue){
				if(newValue != null && oldValue!= null && newValue != oldValue){
					callApiService.getTpsAndLatency($rootScope.environment,$rootScope.globalPlatform).then(function(data){
					dashboardCtrl.tpsAndLatency = data;
				});
				callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeWest,$rootScope.globalPlatform,dashboardCtrl.regionWest).then(function(data){
					dashboardCtrl.componentPercentageWest = data;
				});
				callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeEast,$rootScope.globalPlatform,dashboardCtrl.regionEast).then(function(data){
					dashboardCtrl.componentPercentageEast = data;
				});
                var prevdate = new Date();
		        prevdate.setDate(prevdate.getDate() - 1);
		        dashboardCtrl.startDate =  $filter('date')(prevdate, 'yyyy-MM-dd');
		        dashboardCtrl.endDate = $filter('date')($rootScope.lastUpdatedTime,'yyyy-MM-dd');
				callApiService.getApiStats($rootScope.environment, dashboardCtrl.startDate,dashboardCtrl.endDate,"",$rootScope.globalPlatform).then(function(data){
			dashboardCtrl.serviceContainer = data;
				});
				
				}
			});
		}

	if(!$rootScope.percentageTypeWestWarcherAdded){
		$rootScope.percentageTypeWestWarcherAdded = true;
		$rootScope.$watch('percentageTypeWest', function(newValue, oldValue){
			if(newValue != null && oldValue!= null && newValue != oldValue){
				callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeWest,$rootScope.globalPlatform,dashboardCtrl.regionWest).then(function(data){
					dashboardCtrl.componentPercentageWest = data;
				});
			}
		});
	}
	if(!$rootScope.percentageTypeEastWarcherAdded){
		$rootScope.percentageTypeEastWarcherAdded = true;
		$rootScope.$watch('percentageTypeEast', function(newValue, oldValue){
			if(newValue != null && oldValue!= null && newValue != oldValue){
				callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeEast,$rootScope.globalPlatform,dashboardCtrl.regionEast).then(function(data){
					dashboardCtrl.componentPercentageEast = data;
				});
			}
		});
	}


	callApiService.getComponentDetails($rootScope.environment, $rootScope.userDetails.userId).then(dashboardCtrl.processDashboardData);

	callApiService.getTpsAndLatency($rootScope.environment, $rootScope.globalPlatform).then(function(data){
					dashboardCtrl.tpsAndLatency = data;
	});
	callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeWest,$rootScope.globalPlatform,dashboardCtrl.regionWest).then(function(data){
		dashboardCtrl.componentPercentageWest = data;
	});
	callApiService.getAvailabilityPercent($rootScope.environment, $rootScope.percentageTypeEast,$rootScope.globalPlatform,dashboardCtrl.regionEast).then(function(data){
					dashboardCtrl.componentPercentageEast = data;
				});
	

	$rootScope.reloadComponentData = function() {
		callApiService.getComponentDetails($rootScope.environment, $rootScope.userDetails.userId).then(dashboardCtrl.processDashboardData);
	}
	return dashboardCtrl;
};
