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
GDMApp.controller('adminCompCtrl',adminCompCtrl);

adminCompCtrl.$inject = [ '$scope', 'callApiService', '$state', '$rootScope'];

function adminCompCtrl($scope, callApiService, $state, $rootScope){
	var adminCompCtrl = this;
	$rootScope.adminActiveTab = 1;
	adminCompCtrl.infraComponent = [];
	adminCompCtrl.showAddInfraComponent = false;
	adminCompCtrl.component = { selected : null};
	adminCompCtrl.parentComponents = [];
	adminCompCtrl.title = "Add Component";
	adminCompCtrl.parentSelected = {};
	adminCompCtrl.isParentEdit = false;
	adminCompCtrl.isParentAdd = false;
	adminCompCtrl.parentHead = {
		componentName: "Service Name",
		delInd: 'Status'
	};
	adminCompCtrl.childHead = {
		componentName: "Health Check Name",
		platform: "Platform",
		delInd: 'Status'
	};
	
	adminCompCtrl.sort = {
		column: 'componentId',
		descending: true
	};
	
	adminCompCtrl.goToHealthCheckTab = function() {
		$state.go('adminHealth');
	};
		
	adminCompCtrl.selectedCls = function(column) {
		return column == adminCompCtrl.sort.column && 'sort-' + adminCompCtrl.sort.descending;
	};

	adminCompCtrl.changeSorting = function(column) {
		var sort = adminCompCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
	
	adminCompCtrl.addParent = function(){
		adminCompCtrl.isParentAdd = true;
		adminCompCtrl.isParentEdit = false;
		adminCompCtrl.showAddInfraComponent = true;
		adminCompCtrl.clearComponent();
	};
	
	adminCompCtrl.addChild = function(){
		adminCompCtrl.isParentAdd = false;
		adminCompCtrl.isParentEdit = false;
		adminCompCtrl.showAddInfraComponent = true;
		adminCompCtrl.clearComponent();
	};
	
	adminCompCtrl.saveComponent = function(editComponent){
		console.log("editComponent.platform"+editComponent.platform);
		if(editComponent.componentId == null || editComponent.componentId == 0){
			callApiService.addInfraComponent(editComponent.componentName, editComponent.compDesc,editComponent.platform,
					(adminCompCtrl.component.selected != null ) ? adminCompCtrl.component.selected.componentId : null).then(function(data){
				adminCompCtrl.reloadInfraComponents();
			});
		} else {
			delIndVal = editComponent.delInd ? 1:0;
			callApiService.editInfraComponent(editComponent.componentId, editComponent.componentName, editComponent.compDesc,editComponent.platform, 
					(adminCompCtrl.component.selected != null) ? adminCompCtrl.component.selected.componentId : null, delIndVal).then(function(data){
				adminCompCtrl.reloadInfraComponents();
			});
		}
	};
	
	adminCompCtrl.clearComponent = function(){
		adminCompCtrl.editComponent = {};
		adminCompCtrl.parentSelected = {};
		adminCompCtrl.component = { selected : null};
		adminCompCtrl.title = "Add Component";
	};
	
	adminCompCtrl.cancelAdd = function(){
		adminCompCtrl.clearComponent();
		adminCompCtrl.showAddInfraComponent = false;
	};
	
	adminCompCtrl.listChildren = function(component){
		adminCompCtrl.cancelAdd();
		adminCompCtrl.parentSelected = component;
	};
	
	adminCompCtrl.editComponentForm = function(component, isParentEdit){
		adminCompCtrl.isParentEdit = isParentEdit;
		adminCompCtrl.isParentAdd = false;
		adminCompCtrl.component = { selected : null};
		adminCompCtrl.infraComponent.forEach(function(item, index){
			if(item.componentId == component.parentComponentId){
				adminCompCtrl.component.selected = item;
			}
		});
		adminCompCtrl.editComponent = angular.copy(component);
		adminCompCtrl.title = "Edit Component";
		adminCompCtrl.showAddInfraComponent = true;
	};
	
	adminCompCtrl.deleteComponent = function(componentId){
		callApiService.deleteInfraComponent(componentId).then(function(data){
			adminCompCtrl.reloadInfraComponents();
		});
	};
	
	adminCompCtrl.reloadInfraComponents = function(){
		adminCompCtrl.parentComponents = [];
		adminCompCtrl.infraComponent = [];
		adminCompCtrl.clearComponent();
		adminCompCtrl.showAddInfraComponent = false;
		callApiService.getAllInfra().then(function(data){
			adminCompCtrl.infraComponent = data;
			adminCompCtrl.infraComponent.forEach(function(item, index){
				item.parentComponentName = (item.parentComponentName == null) ? '': item.parentComponentName;
				if(item.parentComponentName == ""){
					adminCompCtrl.parentComponents.push(item);
				}
			});
		});
	};
	
	adminCompCtrl.reloadInfraComponents();
	
	return adminCompCtrl;
};
