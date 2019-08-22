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
GDMApp.controller('adminSubscribeCtrl',adminSubscribeCtrl);

adminSubscribeCtrl.$inject = [ '$scope', 'callApiService', '$rootScope'];

function adminSubscribeCtrl($scope, callApiService, $rootScope){
	var adminSubscribeCtrl = this;
	$rootScope.adminActiveTab = 3;
	adminSubscribeCtrl.subscriptionList = [];
	adminSubscribeCtrl.subscription = {};
	adminSubscribeCtrl.subscription.subsciptionType = "email"
	adminSubscribeCtrl.head = {
		subsciptionVal: "Email address/Slack URL",
		subsciptionType: "Subscription Type",
		globalSubscriptionType: "Global Subscription For",
		platform: "Platform",
		environmentName: 'Environment'
	};
	
	callApiService.getAllGlobalSubscriptions($rootScope.userDetails.authToken).then(function(data){
		adminSubscribeCtrl.subscriptionList = data;
	});
	
	adminSubscribeCtrl.sort = {
		column: 'alertSubscriptionId',
		descending: true
	};
				
	adminSubscribeCtrl.selectedCls = function(column) {
		return column == adminSubscribeCtrl.sort.column && 'sort-' + adminSubscribeCtrl.sort.descending;
	};

	adminSubscribeCtrl.changeSorting = function(column) {
		var sort = adminSubscribeCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
	
	callApiService.getEnvironmentDetails().then(function(data){
		adminSubscribeCtrl.envData = data;
	});
	

	adminSubscribeCtrl.reloadSubscriptions = function(){
		adminSubscribeCtrl.subscriptionList = [];
		callApiService.getAllGlobalSubscriptions($rootScope.userDetails.authToken).then(function(data){
			adminSubscribeCtrl.subscriptionList = data;
		});
	};
	
	adminSubscribeCtrl.clearSubscription = function(){
		adminSubscribeCtrl.subscription = {};
		adminSubscribeCtrl.subscription.subsciptionType = "email"
		adminSubscribeCtrl.isAddSubscription = false;
	};
	
	adminSubscribeCtrl.saveSubscription = function(subscription){
		if(!adminSubscribeCtrl.validateSubscribe(subscription)){
			callApiService.addGlobalSubscription($rootScope.userDetails.authToken, subscription).then(function(data){
				if(data.indexOf("Subscription Successfull") < 0){
					return;
				}
				adminSubscribeCtrl.clearSubscription();
				adminSubscribeCtrl.subscriptionList = [];
				callApiService.getAllGlobalSubscriptions($rootScope.userDetails.authToken).then(function(data){
					adminSubscribeCtrl.subscriptionList = data;
				});
			});	
		}		
	};
	
	adminSubscribeCtrl.validateSubscribe = function(subscription){
		console.log(subscription);
		for(var i=0; i<adminSubscribeCtrl.subscriptionList.length; i++){
			console.log(adminSubscribeCtrl.subscriptionList[i].globalSubscriptionTypeId + "==" + subscription.globalSubscriptionTypeId);
			console.log(adminSubscribeCtrl.subscriptionList[i].subsciptionVal === subscription.subsciptionVal);
			console.log(adminSubscribeCtrl.subscriptionList[i].globalSubscriptionTypeId == 0 || adminSubscribeCtrl.subscriptionList[i].globalSubscriptionTypeId == subscription.globalSubscriptionTypeId);
			//console.log()
			if(adminSubscribeCtrl.subscriptionList[i].subsciptionType == subscription.subsciptionType &&
					adminSubscribeCtrl.subscriptionList[i].environmentId == subscription.environmentId && 
					adminSubscribeCtrl.subscriptionList[i].subsciptionVal === subscription.subsciptionVal && 
					(adminSubscribeCtrl.subscriptionList[i].globalSubscriptionTypeId == 0 || adminSubscribeCtrl.subscriptionList[i].globalSubscriptionTypeId == subscription.globalSubscriptionTypeId)){
				$rootScope.$broadcast("showError","You have already subscribed for this");
				return true;
			}
		}
		return false;
	};
	
	adminSubscribeCtrl.deleteSubscription = function(subscriptionId){
		adminSubscribeCtrl.clearSubscription();
		callApiService.deleteGlobalSubscription($rootScope.userDetails.authToken, subscriptionId).then(function(){
			adminSubscribeCtrl.subscriptionList = [];
			callApiService.getAllGlobalSubscriptions($rootScope.userDetails.authToken).then(function(data){
				adminSubscribeCtrl.subscriptionList = data;
			});
		});
	};
	
	return adminSubscribeCtrl;
}
