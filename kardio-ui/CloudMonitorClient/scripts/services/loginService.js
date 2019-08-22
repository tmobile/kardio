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
GDMApp.factory('loginService', loginService);

loginService.$inject = [ '$rootScope', '$interval', 'callApiService'];

function loginService($rootScope, $interval, callApiService) {
	$rootScope.timeInterval = "300000";
	$rootScope.isSessionValid = false;
	$rootScope.userDetails = {};
	
	var loginService = {};

	loginService.doLogout = function(redirectToLogin){
		callApiService.callLogOut(localStorage.authToken).then(function(){ 
			localStorage.userId = null;
			localStorage.userName = null;
			localStorage.authToken = null;
			localStorage.expirationTime = null;
			localStorage.userGroups = null;
			$rootScope.userDetails = {};
			$rootScope.isSessionValid = false;
			if(redirectToLogin){
				window.location.href = '/';
			}
		});
	};
	
	loginService.validitateSession = function(){
		if(localStorage.expirationTime == 'null' || localStorage.expirationTime == null){
			$rootScope.isSessionValid = false;
			return;
		}
		if(Date.parse(localStorage.expirationTime) < (new Date())){
			localStorage.userId = null;
			localStorage.userName = null;
			localStorage.authToken = null;
			localStorage.expirationTime = null;
			localStorage.userGroups = null;
			$rootScope.userDetails = {};
			$rootScope.isSessionValid = false;
			return;
		}
		
		$rootScope.userDetails = {
			"userName" : localStorage.userName,
			"userId" : localStorage.userId,
			"userEmail" : localStorage.userEmail,
			"authToken": localStorage.authToken,
			"isAdminUser": (localStorage.isAdminUser == "true" ? true : false),
			"roles": (localStorage.userGroups == null)? []: localStorage.userGroups.split(',')
		};
		$rootScope.isSessionValid = true;
	};
	
	$rootScope.isAdminUser = function(){
		if($rootScope.userDetails && $rootScope.userDetails.isAdminUser == true){
			return true;
		}
		return false;
	};
	
	$rootScope.isRoleMatchedForUser = function(roleList){
		if($rootScope.isAdminUser()){
			return true;
		}
		if(!$rootScope.userDetails.roles || roleList == null){
			return false;
		}
		for(var i = 0; i < roleList.length; i++){
			 if($rootScope.userDetails.roles.indexOf(roleList[i]) >= 0){
				 return true;
			 }
		}
		return false;
	};
	
	$interval(function(){loginService.validitateSession()}, 60000);
	loginService.validitateSession();
	
	return loginService;
}
