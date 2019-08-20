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
GDMApp.controller('statusHistoryController',statusHistoryController);

statusHistoryController.$inject = [ '$scope', '$filter', 'callApiService', '$rootScope'];

function statusHistoryController($scope, $filter, callApiService, $rootScope){
	var stsHisCtrl = this;
	
	stsHisCtrl.processStatusHistory = function (statusHistoryData){
		$rootScope.lastUpdatedTime = new Date();
		stsHisCtrl.appHistory = $filter('orderBy')(statusHistoryData.appHistory,'componentName');
		stsHisCtrl.infraHistory = $filter('orderBy')(statusHistoryData.infraHistory,'componentName');
		stsHisCtrl.apiHistory = $filter('orderBy')(statusHistoryData.apiHistory,['parentComponentName','componentName']);
	};
	
	if(!$rootScope.stsHisCtrlWarcherAdded){
		$rootScope.stsHisCtrlWarcherAdded = true;
		$rootScope.$watch('environment', function(newValue, oldValue){
			if(newValue != null && oldValue!= null && newValue != oldValue){
				callApiService.getStatusHistory($rootScope.environment).then(stsHisCtrl.processStatusHistory);
			}
		});
	}
	
	callApiService.getStatusHistory($rootScope.environment).then(stsHisCtrl.processStatusHistory);
	
	stsHisCtrl.formatDate = function(date){
		var today;
		var dd = date.getDate();
		var mm = date.getMonth()+1; //January is 0!
		var yyyy = date.getFullYear();

		if(dd<10) {
			dd = '0'+dd
		} 

		if(mm<10) {
			mm = '0'+mm
		} 
	
		today = yyyy + '-' + mm + '-' + dd;
		return today;
	};
	
	var currentDate = new Date(),
		loopCount = 7,		
		tblHeaders = new Array();
	currentDate.setDate(currentDate.getDate() - 1);

	for (var i = 0 ; i < loopCount; i++){
		tblHeaders.push(stsHisCtrl.formatDate(currentDate));
		currentDate.setDate(currentDate.getDate() - 1);
	}
	stsHisCtrl.tblHeaders = tblHeaders;
	
	return stsHisCtrl;
};
