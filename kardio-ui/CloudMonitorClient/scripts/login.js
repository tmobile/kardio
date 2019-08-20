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
var loginApp = angular.module('loginApp',['ui.bootstrap']);

loginApp.controller('loginController', loginController);
loginController.$inject = [ '$scope', '$rootScope','$http', '$location'];
function loginController($scope,$rootScope,$http, $location){
	$scope.showModalBgGnd = false;
	$scope.showError = false;
	$rootScope.props = prop;
	
	$scope.$on("showError", function(event,message) {
		$scope.errorMessage = message;
       	$scope.showError = true;
        $scope.$apply();
	});
	
	function updateLoginDetails(data) {
		if(!data.timeOutMinute){
			return;
		}
		localStorage.userId = data.userId;
		localStorage.userName = data.userName;
		localStorage.authToken = data.authToken;
		localStorage.userEmail = data.userEmail;
		localStorage.isAdminUser = data.admin;
		localStorage.userGroups = data.userGroups;
		var expDateObj = new Date();
		expDateObj.setTime((new Date()).getTime() + (data.timeOutMinute * 60 * 1000));
		localStorage.expirationTime = expDateObj;
		$scope.checkSessionValidity();
	};
	
	$scope.doLogin =  function() {
		if($scope.username == null || $scope.username.length == 0
				|| ($scope.password == null || $scope.password.length == 0) ){
			$rootScope.$broadcast("showError","Please enter Username and Password");
			return;
		}
		$scope.showError = false;
		var postData = {
				"userName" : $scope.username,
				"password" : $scope.password
			};
		$http({
			  method: 'POST',
			  url: config.loginURL,
			  data: JSON.stringify(postData)
			}).then(function (data) {
				updateLoginDetails(data);
			});
	}
	
	$scope.checkSessionValidity = function(){
		if(localStorage.expirationTime == null){
			return;
		}
		if(Date.parse(localStorage.expirationTime) > (new Date())){
			window.location.href = 'index.html';
		}
	};
	$scope.checkSessionValidity();
};

loginApp.directive('onEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.onEnter);
                });

                event.preventDefault();
            }
        });
    };
});

loginApp.directive('loading',   ['$http' ,function ($http)
{
	return {
		restrict: 'A',
		link: function (scope, elm, attrs)
		{
			scope.isLoading = function () {
				return $http.pendingRequests.length > 0;
			};

			scope.$watch(scope.isLoading, function (v)
			{
				if(v){
					elm.show();
				}else{
					elm.hide();
				}
			});
		}
	};

}]);

loginApp.config([ '$httpProvider', function($httpProvider) {
	if (!$httpProvider.defaults.headers.get) {
		$httpProvider.defaults.headers.get = {};
	}
	$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
	$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
	$httpProvider.interceptors.push('httpInterceptor');
} ]).factory('httpInterceptor',[ '$q', '$rootScope', '$window', function($q, $rootScope, $window) {
	return {
		'request' : function(config) {
			return config;
		},
		'requestError' : $q.reject,
		'response' : function(response) {
			var data = response.data; 
            if(typeof data === 'string') {
            	return response || $q.when(response);
            }
			var firstObj = data[Object.keys(data)[0]];
			
			if(firstObj !==  'GDM000') {
				$rootScope.$broadcast("showError",data.message);
				$q.reject(firstObj +  ' : ' + data.message);
			}
			
			return data.responseContent || $q.when(data.responseContent);
		},
		'responseError' : function(response) {
			if(response.status == -1){
				$rootScope.$broadcast("showError","Unable to get data from server");
			}else{
				$rootScope.$broadcast("showError","Server returned an error : Error Code = " + response.status);
			}
			return $q.reject("Server Not Rechable");
		}
	};
} ]);

