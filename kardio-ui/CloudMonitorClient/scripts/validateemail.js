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
var validateEmailApp = angular.module('validateEmailApp',['ui.bootstrap']);

validateEmailApp.controller('validateEmailController', validateEmailController);
validateEmailController.$inject = [ '$scope', '$rootScope','$http', '$location'];
function validateEmailController($scope,$rootScope,$http, $location){
	$scope.showModalBgGnd = false;
	$scope.showError = false;
	$rootScope.props = prop;
	
	$scope.$on("showError", function(event,message) {
		$scope.errorMessage = message;
       	$scope.showError = true;
        $scope.$apply();
	});
	
	$scope.successMessage = null;
	
	$http({
			method: 'GET',
			url: config.confirmSubscription + window.location.href.substring(window.location.href.indexOf('?'))
		}).then(function (data) {
			console.log('Verification Complete');
			$scope.successMessage = 'Successfully Verified the email id';
		});
};

validateEmailApp.directive('loading',   ['$http' ,function ($http)
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

validateEmailApp.config([ '$httpProvider', function($httpProvider) {
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

