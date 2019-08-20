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
var GDMApp = angular.module('GDMApp',['ui.bootstrap','ngRoute','ui.router','ui.bootstrap', 'ui.select', 'uiSwitch']);

GDMApp.controller('adminPageController', adminPageController);
adminPageController.$inject = [ '$scope', '$rootScope', '$http', 'callApiService', '$state', 'loginService'];
function adminPageController($scope, $rootScope, $http, callApiService, $state, loginService){
	$scope.showModalBgGnd = false;
	$scope.showError = false;
	$scope.successMessage = null;
	$scope.isLoaded = false;
	$scope.doLogout = loginService.doLogout;
	$rootScope.adminActiveTab = 1;
	$rootScope.redirectToLoginOnLogOut = true;
	$rootScope.props = prop;
	
	$scope.$on("showError", function(event,message) {
		$scope.errorMessage = message;
       	$scope.showError = true;
	});
	
	$scope.tabs = [
		{ tabTitle:'Services/Health Check Names', tabId:'adminComponent'},
		{ tabTitle:'Health Check Configurations', tabId:'adminHealth'},
		{ tabTitle:'Global Subscriptions', tabId:'adminSubscribe'},
		{ tabTitle:'Environments', tabId:'adminEnvLock'},
		{ tabTitle:'Counters', tabId:'adminCounter'},
		{ tabTitle:'Application Name', tabId:'adminAppName' }
	];
	
	$scope.onTabSelected = function(tabId){
		$state.go(tabId);
	};
	
	$scope.$on("hideError", function(event) {
       	$scope.showError = false;
	});
};

GDMApp.config(['$routeProvider','$stateProvider','$urlRouterProvider','$locationProvider',
    function($routeProvider,$stateProvider,$urlRouterProvider,$locationProvider)
{
	$urlRouterProvider.otherwise("/adminComponent");
	
	$stateProvider.state('adminComponent', {
    	url: "/adminComponent",
    	controller: 'adminCompCtrl',
		controllerAs: 'adminCompCtrl',
		templateUrl: 'pages/adminCompCtrl.html'
    }).state('adminHealth', {
        url: "/adminHealth",
        controller: 'adminHealthCtrl',
        controllerAs:'adminHealthCtrl',
        templateUrl: 'pages/adminHealthCtrl.html'
    }).state('adminSubscribe', {
        url: "/adminSubscribe",
        controller: 'adminSubscribeCtrl',
        controllerAs:'adminSubscribeCtrl',
        templateUrl: 'pages/adminSubscribe.html'
    }).state('adminEnvLock', {
        url: "/adminEnvLock",
        controller: 'adminEnvLockCtrl',
        controllerAs:'adminEnvLockCtrl',
        templateUrl: 'pages/adminEnvLock.html'
    }).state('adminCounter', {
        url: "/adminCounter",
        controller: 'adminCounterCtrl',
        controllerAs:'adminCounterCtrl',
        templateUrl: 'pages/adminCounter.html'
    }).state('adminAppName', {
        url: "/adminAppName",
        controller: 'adminAppNameCtrl',
        controllerAs:'adminAppNameCtrl',
        templateUrl: 'pages/adminAppName.html'
    })
}]);
