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
GDMApp.directive("counterGraph",function($compile) {
	return {
		restrict: "E",
	    controller: counterGraphCtrl,
		controllerAs: 'ctrGraphCtrl',
	    scope: {
	    	environment: '@',
			platform: '@'
	    },
	    link: function($scope, element, attrs) {
	    	$scope.initializeCounter(attrs.environment,attrs.platform);
	    },
	    bindToController : true,
	    templateUrl: 'pages/template/counterGraph.html'
	};
});

counterGraphCtrl.$inject  = ['$scope', 'callApiService', '$rootScope', '$filter' , '$interval', '$window'];

function counterGraphCtrl($scope, callApiService, $rootScope, $filter, $interval, $window) {
	var counterGraphCtrl = this;
	counterGraphCtrl.isCollapsed = false;
	counterGraphCtrl.counterData = [];
	counterGraphCtrl.graphHeight = 35;
	counterGraphCtrl.colorFn = function() {
		return function(d, i) {
	    	return '#999'
	    };
	};

	counterGraphCtrl.reloadCounter = function(environmentName, platform){
		counterGraphCtrl.counterData = [];
		callApiService.getGeneralCounters(environmentName, platform).then(function(data){
			counterGraphCtrl.counterData = $filter('orderBy')(data, 'position');
			for(var count = 0; counterGraphCtrl.counterData != null && count < counterGraphCtrl.counterData.length; count++){
				counterGraphCtrl.counterData[count].chartData = [];
				for(var i = 0; counterGraphCtrl.counterData[count].trend != null && i < counterGraphCtrl.counterData[count].trend.length; i++){
					counterGraphCtrl.counterData[count].chartData.push([i+1,counterGraphCtrl.counterData[count].trend[i]]);
				}
			}
			counterGraphCtrl.graphBoxWidth = (100/counterGraphCtrl.counterData.length - 1) + '%';
			counterGraphCtrl.graphWidth = Math.floor((window.innerWidth * 80/100)
							* (100/counterGraphCtrl.counterData.length - 1)/ 100);
			//Fix for IE
			setTimeout(function(){angular.element('.info-box').css('width', counterGraphCtrl.graphBoxWidth); }, 1000);
		});
	};

	$scope.initializeCounter = function(environmentName, platform){
		//Radio Button Code Change
		if(platform != null && platform != "" && !$rootScope.counterGraphCtrlPlatformWatcherAdded){
			$rootScope.counterGraphCtrlPlatformWatcherAdded = true;
			$rootScope.$watch('platform', function(newValue, oldValue){
				if(newValue != null && oldValue!= null && newValue != oldValue){
					counterGraphCtrl.reloadCounter($rootScope.environment,$rootScope.platform);
				} 
			});
		}
		if($rootScope.globalPlatform != null && $rootScope.globalPlatform != "" && !$rootScope.counterGraphCtrlglobalPlatformWatcherAdded){
			$rootScope.counterGraphCtrlglobalPlatformWatcherAdded = true;
			$rootScope.$watch('globalPlatform', function(newValue, oldValue){
				if(newValue != null && oldValue!= null && newValue != oldValue){
					counterGraphCtrl.reloadCounter("",$rootScope.globalPlatform);
				}
			});
		}
		

		if(environmentName != null && environmentName != "" && !$rootScope.EnvTabChangeCounterGraphCtrlWatcherAdded){
			$rootScope.EnvTabChangeCounterGraphCtrlWatcherAdded = true;
			$rootScope.$watch('environment', function(newValue, oldValue){
				if(newValue != null && oldValue!= null && newValue != oldValue){
					counterGraphCtrl.reloadCounter($rootScope.environment,$rootScope.platform);//Radio Button Code Change - added platform parameter
				}
			});
		}
		

		if($rootScope.timeInterval != null){
			counterGraphCtrl.intervalPromise = $interval(function(){
				if(!angular.element(".modal-backdrop").is(':visible')){
					counterGraphCtrl.reloadCounter(environmentName);
				}
			}, $rootScope.timeInterval);
			$rootScope.$watch('timeInterval', function(newValue, oldValue){
				if(newValue != null && oldValue!= null && newValue != oldValue){
					$interval.cancel(counterGraphCtrl.intervalPromise);
					counterGraphCtrl.intervalPromise = undefined;
					counterGraphCtrl.intervalPromise = $interval(function(){
						if(!angular.element(".modal-backdrop").is(':visible')){
							if(environmentName == null || environmentName == ""){
								counterGraphCtrl.reloadCounter("");
							}else{
								counterGraphCtrl.reloadCounter($rootScope.environment);
							}
						}
					}, $rootScope.timeInterval);
				}
			});
		}

		angular.element($window).bind('resize', function(){
			counterGraphCtrl.reloadCounter(environmentName,platform);
		});
		counterGraphCtrl.reloadCounter(environmentName,platform);
	};

	counterGraphCtrl.yFunction = function(){
		return function(d){
			return d[1];
		}
	};

	counterGraphCtrl.xFunction = function(){
		return function(d){
			return d[0];
		}
	};

	counterGraphCtrl.ytickformat = function(){
		return d3.format(',r');
	}

   counterGraphCtrl.ytickformatT = function(){
		return d3.format(',.2f');
	}
	return counterGraphCtrl;
};
