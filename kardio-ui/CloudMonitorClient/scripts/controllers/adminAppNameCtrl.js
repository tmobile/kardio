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
GDMApp.controller('adminAppNameCtrl',adminAppNameCtrl);

adminAppNameCtrl.$inject = [ '$scope', 'callApiService', '$state', '$rootScope'];

function adminAppNameCtrl($scope, callApiService, $state, $rootScope){
	var adminAppNameCtrl = this;
	$rootScope.adminActiveTab = 6;
	adminAppNameCtrl.appLookUpData = [];
	adminAppNameCtrl.appLookUpTblHeader = {
			componentFullName: "Application Name",
			componentName: 'Component'
		};
	
	callApiService.getAppLookUp($rootScope.userDetails.authToken).then(function(data){
		adminAppNameCtrl.appLookUpData = data;
	});
	
	
	adminAppNameCtrl.editAppLookUpName = function(appLookUp){
		callApiService.updateAdminEnvCounters($rootScope.userDetails.authToken, appLookUp).then(function(data){
			adminAppNameCtrl.reloadAppLookUpData();
		});
	};
	
	adminAppNameCtrl.editBtnClick = function(appLookUp){
		adminAppNameCtrl.componentId = appLookUp.componentId
		adminAppNameCtrl.appLookUpId = appLookUp.appId;
		adminAppNameCtrl.isEdit = true;
	};
	
	adminAppNameCtrl.checkIsEdit = function(appLookUp){
		if(adminAppNameCtrl.componentId == appLookUp.componentId 
				&& adminAppNameCtrl.appLookUpId == appLookUp.appId
				&& adminAppNameCtrl.isEdit){
			return true;
		}
		return false;
	};
	
	adminAppNameCtrl.reloadAppLookUpData = function(){
		callApiService.getAppLookUp($rootScope.userDetails.authToken).then(function(data){
			adminAppNameCtrl.appLookUpData = data;
			adminAppNameCtrl.appLookUpId = 0;
			adminAppNameCtrl.isEdit = false;
		});
	};
	
	adminAppNameCtrl.sort = {
			column: 'appId',
			descending: true
		};
	
	adminAppNameCtrl.selectedCls = function(column) {
		return column == adminAppNameCtrl.sort.column && 'sort-' + adminAppNameCtrl.sort.descending;
	};

	adminAppNameCtrl.changeSorting = function(column) {
		var sort = adminAppNameCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
}
