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
GDMApp.directive("componentTable",function($compile) {
	return {
		restrict: "E",
	    controller: componentTableCtrl,
	    controllerAs: 'componentTableCtrl',
	    scope: {
	    	tableData: '=',
	    	tableHeader: '=',
			tableEmptyMessage: '@',
			tableIndex: '@',
			componentParentHeader: '@',
			percentageWest: '=',
			percentageEast: '=',
			serviceContainer: '=',
			tpsLatency: '=',
			paginationId: '@'
	    },
	    bindToController : true,
	    templateUrl: 'pages/template/componentTable.html'
	};
});

componentTableCtrl.$inject  = ['$scope','$state', 'callApiService', '$rootScope'];

function componentTableCtrl($scope, $state, callApiService, $rootScope) {
	var componentTableCtrl = this;
	componentTableCtrl.showFilter = false;
	componentTableCtrl.isCollapsed = false;
	$scope.reverse = false;

	//Code Changes for Adding Sort Feature for columns in dashboard
	$scope.sort = function(keyname){
		$scope.sortKey = keyname;
		$scope.reverse = !$scope.reverse;
	}

	return componentTableCtrl;
};
