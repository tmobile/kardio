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
GDMApp.controller('adminEnvLockCtrl',adminEnvLockCtrl);

adminEnvLockCtrl.$inject = [ '$scope', 'callApiService', '$rootScope'];

function adminEnvLockCtrl($scope, callApiService, $rootScope){
	var adminEnvLockCtrl = this;
	$rootScope.adminActiveTab = 4;
	adminEnvLockCtrl.environment = {};
	
	adminEnvLockCtrl.envTblHead = {
		environmentName: "Environment ID",
		environmentDesc: 'Environment Display Name',
		envLock: "Lock"
	};
	
	callApiService.getEnvironmentForLocks($rootScope.userDetails.authToken).then(function(data){		
		adminEnvLockCtrl.envData = data;
	});
	
	adminEnvLockCtrl.sort = {
		column: 'environmentId',
		descending: true
	};
					
	adminEnvLockCtrl.selectedCls = function(column) {
		return column == adminEnvLockCtrl.sort.column && 'sort-' + adminEnvLockCtrl.sort.descending;
	};

	adminEnvLockCtrl.changeSorting = function(column) {
		var sort = adminEnvLockCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
	
	adminEnvLockCtrl.editBtnClick = function(environment){
		adminEnvLockCtrl.isEditEnvironment = true;
		adminEnvLockCtrl.environment =  angular.copy(environment);
	};
	
	adminEnvLockCtrl.clearData = function(){
		adminEnvLockCtrl.environment = {};
		adminEnvLockCtrl.isEditEnvironment = false;
		adminEnvLockCtrl.isEditPassword = false;
		adminEnvLockCtrl.isAddEnvironment = false;
	};
	
	adminEnvLockCtrl.addEnvironment = function(environment){		
		callApiService.addEnvironment($rootScope.userDetails.authToken,environment).then(function(data){
			adminEnvLockCtrl.envData =  [];
			callApiService.getEnvironmentForLocks($rootScope.userDetails.authToken).then(function(data){
				adminEnvLockCtrl.envData = data;
			});
		});
		
		adminEnvLockCtrl.clearData();
	};
	
	adminEnvLockCtrl.saveEnviromentData = function(environment, isMarathonCredEdit,isK8sCredEdit){
		if(isMarathonCredEdit == 0){
			environment.marathonUserName = "";
			environment.marathonPassword = "";
		}
		if(isK8sCredEdit == 0){
			environment.k8sUserName = "";
			environment.k8sPassword = "";
		}
		environment.envLock = environment.envLock ? 1:0;
		console.log("saveEnviromentData"+isMarathonCredEdit+" "+isK8sCredEdit);
		callApiService.editEnvironment($rootScope.userDetails.authToken,environment).then(function(data){
			adminEnvLockCtrl.envData =  [];
			console.log("editEnvironment");
			callApiService.getEnvironmentForLocks($rootScope.userDetails.authToken).then(function(data){
				adminEnvLockCtrl.envData = data;
			});
		});
		
		adminEnvLockCtrl.clearData();
	};
	
	return adminEnvLockCtrl;
}
