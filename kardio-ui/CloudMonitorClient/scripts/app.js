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
var GDMApp = angular.module('mainApp',['ui.bootstrap','ngRoute','ui.router','ui.bootstrap','720kb.tooltips', 'nvd3ChartDirectives', 'ngAnimate', 'textAngular','angularUtils.directives.dirPagination']);

GDMApp.controller('mainController', appController);
appController.$inject = [ '$scope', '$rootScope','$interval','callApiService','loginService','$filter'];
function appController($scope,$rootScope,$interval, callApiService,loginService,$filter){
	$scope.showModalBgGnd = false;
	$scope.lastUpdatedTime = null;
	$scope.showError = false;
	$rootScope.percentageTypeWest = 'Y';
	$rootScope.percentageTypeEast = 'Y';
	$rootScope.environment = prop.defaultEnvironmentName;
	$rootScope.platform = "All"; // Added for radio button code changes
	$rootScope.props = prop;
	$rootScope.globalPlatform = "All";
	$rootScope.filterByPlatform ='';
    $rootScope.counterMessage ='';
   
	$scope.goToApiDashboard = function(){
		window.location.href = 'apidashboard.html';
	}
	callApiService.getEnvironmentDetails().then(function(data){
		$rootScope.envData = data;
	});

	$scope.onTabSelected = function(selectedTab){
		$rootScope.environment = selectedTab;
		$rootScope.platform=$scope.pformGlobal;
		$rootScope.globalPlatform=$scope.pformGlobal;
	};
	$scope.$on("showError", function(event,message) {
		$scope.errorMessage = message;
       	$scope.showError = true;
	});

	$scope.$on("hideError", function(event) {
       	$scope.showError = false;
	});

	$scope.doLogout = loginService.doLogout;

	$scope.RadioChange = function(data) {
        
		$rootScope.globalPlatform=data;
		$rootScope.platform=data;
		//Code Change to filter components by platform.
		if (data!='All'){
			$rootScope.filterByPlatform = data;
		}
		else
		   $rootScope.filterByPlatform = '';
		/*
		callApiService.getGeneralCounters("", $rootScope.globalPlatform).then(function(data){

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
		});*/
		//counterGraphCtrl.reloadCounter("",$rootScope.globalPlatform);
	}
};



GDMApp.filter('trustAsHtml', function($sce) { return $sce.trustAsHtml; });

GDMApp.config([
    '$routeProvider','$stateProvider','$urlRouterProvider','$locationProvider',
    function($routeProvider,$stateProvider,$urlRouterProvider,$locationProvider)
{
	$urlRouterProvider.otherwise("/dashboard");

	$stateProvider.state('dashboard', {
    	url: "/dashboard",
    	controller: 'dashboardController',
		controllerAs: 'dashboardCtrl',
		templateUrl: 'pages/dashboard.html'
    }).state('statusHistory', {
        url: "/statusHistory",
        controller: 'statusHistoryController',
        controllerAs:'stsHisCtrl',
        templateUrl: 'pages/statusHistory.html',
		params: {
			data: {}
		}
      })
}])

GDMApp.run( ['$rootScope',function($rootScope) {
	$rootScope.$on( "$locationChangeStart", function(){
		angular.element("body").scope().lastUpdatedTime = null;
	});
}]);
