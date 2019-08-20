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
GDMApp.directive("historyTable",function($compile) { 
	return {
		restrict: "E",
	    controller: historyTableCtrl,
	    controllerAs: 'historyTableCtrl',
	    scope: {
	    	tableData: '=',
	    	tableHeader: '=',
			tableMessage: '@',
			displayParent: '@',
			tableIndex: '@',
			paginationId: '@'
	    },
	    bindToController : true,
	    templateUrl: 'pages/template/historyTable.html'
	};
});

historyTableCtrl.$inject  = ['$scope', '$state', '$rootScope', 'callApiService', '$filter'];

function historyTableCtrl($scope, $state, $rootScope, callApiService, $filter) {
	var historyTableCtrl = this;
	historyTableCtrl.showFilter = false;
	historyTableCtrl.isCollapsed = false;
	$scope.openMessage = function(compDet, date, regId){
		var compId = compDet.componentId;
		historyTableCtrl.popUpParentComponentName = (compDet.parentComponentName) ? compDet.parentComponentName : '';
		historyTableCtrl.popUpComponentName = (compDet.componentName) ? compDet.componentName : '';
    	historyTableCtrl.date = date;
    	$scope.processCompMsgData = function(data){
    		historyTableCtrl.compMsgdataHis = $filter('orderBy')(data, 'messageDate', false);
    	}
        $scope.modalName = compId;
        historyTableCtrl.compMsgdataHis = [];
        callApiService.getComponentMessagesHistory($rootScope.environment, regId, compId, date).then($scope.processCompMsgData);
    };
    
    historyTableCtrl.sort = {
		column: 'date',
		descending: true
	};
	
    historyTableCtrl.selectedCls = function(column) {
		return column == historyTableCtrl.sort.column && 'sort-' + historyTableCtrl.sort.descending;
	};

	historyTableCtrl.changeSorting = function(column) {
		var sort = historyTableCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
    
	return historyTableCtrl;
};
