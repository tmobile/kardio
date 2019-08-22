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
GDMApp.controller('adminCounterCtrl',adminCounterCtrl);

adminCounterCtrl.$inject = [ '$scope', 'callApiService', '$rootScope'];

function adminCounterCtrl($scope, callApiService, $rootScope){
	var adminCounterCtrl = this;
	$rootScope.adminActiveTab = 5;
	adminCounterCtrl.sort = {};
	adminCounterCtrl.headerEnvCounter = {
			counterName: "Counter Name",
			envName: 'Environment',
			platform: 'Platform'
		};
	
	adminCounterCtrl.counterList = [];
	adminCounterCtrl.envCounterList = [];
	
	adminCounterCtrl.counterEditSelected = {};
	adminCounterCtrl.counterEdited = {}
	adminCounterCtrl.isEditCounter = false;
	adminCounterCtrl.envCounterEdited = {};
	adminCounterCtrl.isEditEnvCounter = false;
	
	adminCounterCtrl.selectedCls = function(column) {
		return column == adminCounterCtrl.sort.column && 'sort-' + adminCounterCtrl.sort.descending;
	};
	adminCounterCtrl.changeSorting = function(column) {
		var sort = adminCounterCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
	
	adminCounterCtrl.clearCounterData = function() {
		adminCounterCtrl.counterEdited = {};
		adminCounterCtrl.isEditCounter = false;
	};
	adminCounterCtrl.moveUp = function() {
		var selectedIndex = adminCounterCtrl.getSelectedIndex(adminCounterCtrl.counterEditSelected);
		if(selectedIndex < 0 || selectedIndex == 0){
			return;
		}
		var temp = adminCounterCtrl.counterList[selectedIndex];
		adminCounterCtrl.counterList[selectedIndex] = adminCounterCtrl.counterList[selectedIndex - 1];
		adminCounterCtrl.counterList[selectedIndex - 1] = temp;
		adminCounterCtrl.setPositionOfCounter();
	};
	adminCounterCtrl.moveDown = function() {
		var selectedIndex = adminCounterCtrl.getSelectedIndex(adminCounterCtrl.counterEditSelected);
		if(selectedIndex < 0 || selectedIndex == adminCounterCtrl.counterList.length - 1){
			return;
		}
		var temp = adminCounterCtrl.counterList[selectedIndex];
		adminCounterCtrl.counterList[selectedIndex] = adminCounterCtrl.counterList[selectedIndex + 1];
		adminCounterCtrl.counterList[selectedIndex + 1] = temp;
		adminCounterCtrl.setPositionOfCounter();
	};
	adminCounterCtrl.setPositionOfCounter = function() {
		adminCounterCtrl.counterList.forEach(function(item, index){
			item.position = index + 1;
		});
	};
	adminCounterCtrl.getSelectedIndex = function(inputCounter) {
		var selectedIndex = -1;
		adminCounterCtrl.counterList.forEach(function(item, index){
			if(item.counterId == inputCounter.counterId){
				selectedIndex = index;
			}
		});
		return selectedIndex;
	};
	adminCounterCtrl.updateCounterData = function() {
		var selectedIndex = adminCounterCtrl.getSelectedIndex(adminCounterCtrl.counterEdited);
		if(selectedIndex > -1){
			adminCounterCtrl.counterList[selectedIndex].counterName = adminCounterCtrl.counterEdited.counterName;
			adminCounterCtrl.counterList[selectedIndex].delInd = adminCounterCtrl.counterEdited.statusInd == false ? 0 : 1;
			adminCounterCtrl.counterEdited = {};
			adminCounterCtrl.isEditCounter = false;
		}
	};
	adminCounterCtrl.editCounterData = function() {
		adminCounterCtrl.isEditCounter = true;
		adminCounterCtrl.counterEdited = {};
		adminCounterCtrl.counterEdited.counterId = adminCounterCtrl.counterEditSelected.counterId;
		adminCounterCtrl.counterEdited.counterName = adminCounterCtrl.counterEditSelected.counterName;
		adminCounterCtrl.counterEdited.statusInd = adminCounterCtrl.counterEditSelected.delInd == 0 ? false : true;
	};
	adminCounterCtrl.saveCounterData = function() {
		callApiService.updateAdminCounters($rootScope.userDetails.authToken, adminCounterCtrl.counterList).then(function(data){
			adminCounterCtrl.resetCounterData();
		});
	};
	
	adminCounterCtrl.clearCounterEnvData = function() {
		adminCounterCtrl.envCounterEdited = {};
		adminCounterCtrl.isEditEnvCounter = false;
	};
	adminCounterCtrl.editEnvConter = function(envCounter) {
		adminCounterCtrl.envCounterEdited = angular.copy(envCounter);
		adminCounterCtrl.isEditEnvCounter = true;
	};
	adminCounterCtrl.saveCounterEnvData = function() {
		callApiService.updateAdminEnvCounters($rootScope.userDetails.authToken, adminCounterCtrl.envCounterEdited).then(function(data){
			adminCounterCtrl.resetCounterData();
		});
	};
	
	adminCounterCtrl.loadData = function() {
		callApiService.getAdminCounters($rootScope.userDetails.authToken).then(function(data){
			adminCounterCtrl.counterList = data.counter;
			adminCounterCtrl.envCounterList = data.envCounters;
			if(adminCounterCtrl.counterList.length > 0){
				adminCounterCtrl.counterEditSelected = adminCounterCtrl.counterList[0];
			}
		});
	};
	adminCounterCtrl.resetCounterData = function() {
		adminCounterCtrl.counterList = [];
		adminCounterCtrl.envCounterList = [];
		
		adminCounterCtrl.counterEdited = {};
		adminCounterCtrl.isEditCounter = false;
		adminCounterCtrl.envCounterEdited = {};
		adminCounterCtrl.isEditEnvCounter = false;
		adminCounterCtrl.loadData();
	};
	adminCounterCtrl.resetCounterData();
	
	return adminCounterCtrl;
}
