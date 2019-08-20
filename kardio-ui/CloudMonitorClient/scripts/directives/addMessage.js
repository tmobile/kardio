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
GDMApp.controller('addMessageCtrl', ['$scope', '$rootScope', '$filter', 'callApiService', function ($scope, $rootScope, $filter, callApiService) {
    $scope.openAddMessage = function(regId, compId){
    	$rootScope.compMsgComment = '';
    	$scope.processCompMsgData = function(data){
			var msgData = [];
			for(var i=0; i<data.length; i++){
				message = data[i];
				if(!message.message){
					continue;
				}
				msgData.push({"date": message.messageDate, "name": message.componentName,
					"message": message.message, "userId": message.userId, "messageId": message.messageId});
			}
			$rootScope.cMsgData = msgData;
    	}
        $scope.modalName = compId;
        $scope.regId = regId;
        $rootScope.compMsgdata = [];
        callApiService.getComponentMessages($rootScope.environment, regId, compId).then($scope.processCompMsgData);
    };
}]);

GDMApp.directive('messageModal', function () {
    return {
        restrict: 'EA',
        scope: {
            header: '@modalHeader',
            handler: '=modalHandler',
            componentRoles: '=componentRoles',
            component: '@',
            region: '@'
        },
        templateUrl: 'pages/template/addMessage.html',
        transclude: true,
        controller: ['$scope', '$rootScope', '$window', 'callApiService', function ($scope, $rootScope, $window, callApiService) {
        	$scope.messaageId = 0;
			$scope.head = {
				date: "Date Time",
				name: "Service",
				message: "Message",
			};
        	$scope.addMessage = function(){
        		//environment, regId, compId, message, authToken, userName, userid
				callApiService.postComponentMessages($rootScope.environment, $scope.region, $scope.component,$rootScope.compMsgComment,
						$rootScope.userDetails.authToken, $rootScope.userDetails.userName, $rootScope.userDetails.userId, $scope.messaageId).then(function(data){
					callApiService.getComponentMessages($rootScope.environment, 
							$scope.$parent.regId, $scope.$parent.modalName).then($scope.$parent.processCompMsgData);
				});
            };
			
			$scope.editBtnClick = function(message, date, componentName, messageId){
				$rootScope.compMsgComment = message;
				$scope.date = date;
				$scope.componentName = componentName;
				$scope.messaageId = messageId;
			};
			
			$scope.isUserIdMathched = function(userId){
				if($rootScope.userDetails.userId.toUpperCase() == userId.toUpperCase()){
					return true;
				}
				return false;
			};
			
			$scope.sort = {
				column: 'date',
				descending: true
			};
			
			$scope.selectedCls = function(column) {
				return column == $scope.sort.column && 'sort-' + $scope.sort.descending;
			};
    
			$scope.changeSorting = function(column) {
				var sort = $scope.sort;
				if (sort.column == column) {
					sort.descending = !sort.descending;
				} else {
					sort.column = column;
					sort.descending = false;
				}
			};
        }]
    };
});
