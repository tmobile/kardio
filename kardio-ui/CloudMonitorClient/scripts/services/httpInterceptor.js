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
GDMApp.config([ '$httpProvider', function($httpProvider) {
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
			
			if(firstObj == 'GDM003') {
				$window.location.href = 'login.html';
			}
			
			return response || $q.when(response);
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
