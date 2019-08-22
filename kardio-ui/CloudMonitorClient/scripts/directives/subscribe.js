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
GDMApp.controller('subscribeCtrl', ['$scope', '$rootScope', 'callApiService', function ($scope, $rootScope, callApiService) {
    $scope.openSubscriptionWin = function(prefix, compId, compRoles){
    	$rootScope.subResponseMessage = null;
    	$scope.processListSubscription = function(data){
    		$rootScope.subAvailableEmails = data;
    	}
    	$rootScope.subAvailableEmails = [];
        $scope.modalName = prefix + compId;
        if($rootScope.isRoleMatchedForUser(compRoles)){
        	callApiService.getSubscribedEmails($rootScope.environment, compId).then($scope.processListSubscription);
        }
    };
}]);

GDMApp.directive('subscribeModal', function () {
    return {
        restrict: 'EA',
        scope: {
            header: '@modalHeader',
            handler: '=modalHandler',
            component: '@'
        },
        templateUrl: 'pages/template/subscribe.html',
        transclude: true,
        controller: ['$scope', '$rootScope', '$window', 'callApiService', function ($scope, $rootScope, $window, callApiService) {

        	$scope.subscribeAlert = function(subscriptionType){
        		if(subscriptionType == 'email'){
        			$scope.subscribeText = $scope.subscribeEmail;
        		} else if(subscriptionType == 'slack'){
        			$scope.subscribeText = $scope.subscribeSlack;
        		} else if(subscriptionType == 'slackChannel'){
        			$scope.subscribeText = $scope.subscribeSlackChannel;
        		} else if(subscriptionType == 'oneConsoleWebhook'){
        			$scope.subscribeText = $scope.subscribeWebhook;
        		}
        		callApiService.subscribeAlert($scope.component, $scope.subscribeText, 'subscribe', $rootScope.environment, $scope.header, subscriptionType).then(function(data) {
        			$rootScope.subResponseMessage = data;
        			$scope.subscribeEmail = '';
        			$scope.subscribeSlack = '';
        			$scope.subscribeSlackChannel = '';
        			$scope.subscribeWebhook = '';
        		});
        	};
        	
        	$scope.unSubscribeAlert = function(subscriptionType){
        		if(subscriptionType == 'email'){
        			$scope.subscribeText = $scope.subscribeEmail;
        		} else if(subscriptionType == 'slack'){
        			$scope.subscribeText = $scope.subscribeSlack;
        		} else if(subscriptionType == 'slackChannel'){
        			$scope.subscribeText = $scope.subscribeSlackChannel;
        		}else if(subscriptionType == 'oneConsoleWebhook'){
        			$scope.subscribeText = $scope.subscribeWebhook;
        		}
        		callApiService.subscribeAlert($scope.component, $scope.subscribeText, 'unsubscribe', $rootScope.environment, $scope.header, subscriptionType).then(function(data) {
        			$rootScope.subResponseMessage = data;
        			$scope.subscribeEmail = '';
        			$scope.subscribeSlack = '';
        			$scope.subscribeWebhook = '';
        		});
        	};
        	
        	$scope.setsubscribeText = function(subscribeText, subscriptionType){
        		$scope.subscriptionType = subscriptionType;
        		if(subscriptionType == 'email'){
        			$scope.subscribeEmail = subscribeText;
        		} else if(subscriptionType == 'slack'){
        			$scope.subscribeSlack = subscribeText;
        		} else if(subscriptionType == 'slackChannel'){
        			$scope.subscribeSlackChannel = subscribeText;
        		} else if(subscriptionType == 'oneConsoleWebhook'){
        			$scope.subscribeText = $scope.subscribeWebhook;
        		}
        	};
        }]
    };
});
