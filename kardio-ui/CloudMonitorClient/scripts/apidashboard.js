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
var GDMApp = angular.module('GDMApp',['ngAnimate', 'ui.bootstrap', 'ui.select', 'uiSwitch', 'nvd3ChartDirectives']);
GDMApp.filter('trustAsHtml', function($sce) { return $sce.trustAsHtml; });
GDMApp.filter('filterList', filterList);
function filterList() {
	return function(componentList, parentList, addAllApis){
		var childrenComponents = [];

		for(componentId in componentList){
			for(selectedId in parentList){
				if(componentList[componentId].parentComponentId == parentList[selectedId].componentId
						 || (addAllApis && componentList[componentId].componentId == -1) ) {
					if(componentList[componentId].componentId == -1){
						addAllApis = false;//Add All only one time of multiple parents are selected
					}
					childrenComponents.push(componentList[componentId])
				}
			}
		}
		return childrenComponents;
	};
}

GDMApp.controller('mainController', mainController);
mainController.$inject = [ '$scope', '$rootScope', '$http', '$window' ,'callApiService', 'graphService'];
function mainController($scope, $rootScope, $http, $window, callApiService, graphService){
	var mainCtrl = this;
	$scope.showModalBgGnd = false;
	$scope.showError = false;
	$rootScope.props = prop;
	mainCtrl.graphInterval = 'day';
	mainCtrl.dateFormat = 'dd MMMM yyyy';
	mainCtrl.dataLoadedDate = new Date(prop.apiDashBoardDataLoadedDate);
	var prevMonthDate = new Date();
	prevMonthDate.setMonth(prevMonthDate.getMonth() - 1);
	if(prevMonthDate.getTime() < mainCtrl.dataLoadedDate.getTime()){
		prevMonthDate = mainCtrl.dataLoadedDate;
	}
	mainCtrl.fromDate = prevMonthDate;
	mainCtrl.toDate = new Date();
	mainCtrl.selectedApplications = null;
	mainCtrl.selectedPlatform = null;
	mainCtrl.selectedApis = null;
	mainCtrl.environment = 'all';
	mainCtrl.envData = [];
	mainCtrl.marathonComponents = [];
	mainCtrl.platform =[];

	mainCtrl.apiDetailsHeader = {
			"parentComponentName": "Application Name",
			"componentName": "Api Name",
			"createdDate" : "Created On",
			"envName": "Environment"
		};

	mainCtrl.sort = {
			column: 'componentId',
			descending: true
		};
	
	mainCtrl.selectedCls = function(column) {
		return column == mainCtrl.sort.column && 'sort-' + mainCtrl.sort.descending;
	};

	mainCtrl.changeSorting = function(column) {
		var sort = mainCtrl.sort;
		if (sort.column == column) {
			sort.descending = !sort.descending;
		} else {
			sort.column = column;
			sort.descending = false;
		}
	};
mainCtrl.alignCol = function (HookData){
		var col = HookData.column.index;
  if(col==2 || col==3){
    HookData.cell.styles.halign = 'center';
    console.log('cell', HookData.cell);
   
  }
  };
mainCtrl.createPDF = function() {

var canvas = document.querySelector('#canvas');
var canvasImg = canvas.toDataURL();
var doc = new jsPDF('landscape');
doc.setFontSize(10);
doc.setFontType("bold");
doc.text(135,4,"API DASHBOARD");
doc.setFontSize(8);
if(mainCtrl.selectedPlatform!=null){
doc.text(5, 7, 'Platform      : '+mainCtrl.selectedPlatform);
}
else{
doc.text(5, 7, 'Platform      :  All');	
}
if((mainCtrl.selectedApplications!=null && mainCtrl.selectedApplications.length!=0)){
var compId = covertComponentArray(mainCtrl.selectedApplications);	
doc.text(5, 10, 'Application : '+mainCtrl.getParentFullName(compId));	
}
if(mainCtrl.selectedApis!=null && mainCtrl.selectedApis.length!=0)	{
var compId = covertComponentArray(mainCtrl.selectedApis);
doc.text(5, 13, 'Api               : '+mainCtrl.getParentFullName(compId));	
}
doc.addImage(canvasImg, 'JPEG',10,10,280,150);

if(mainCtrl.selectedApplications != null && mainCtrl.selectedApplications.length!=0){
doc.addPage();
doc.setFontSize(10);
doc.text(135,4,"API DETAILS");
/*
doc.text(26,14,mainCtrl.apiDetailsHeader.parentComponentName);
doc.text(102,14,mainCtrl.apiDetailsHeader.componentName);
doc.text(173,14,mainCtrl.apiDetailsHeader.createdDate);
doc.text(240,14,mainCtrl.apiDetailsHeader.envName);*/
doc.autoTable({html: '#apiHeader',
//theme:'striped',
headStyles: {fillColor: [234, 10, 142],halign:'left'},
columnStyles:{0 : {halign: 'left'}},
didParseCell: function (HookData) {
       		 mainCtrl.alignCol(HookData);
        },
startY:16});
doc.autoTable({html: '#apiBody',
theme:'grid',
columnStyles:{0:{cellWidth:30},
              1 :{cellWidth:40},
			  2 : {cellWidth:30},
			  3 : {cellWidth:30} },
startY:22});
	
}
doc.save('ApiDashboard.pdf');
};

	mainCtrl.dateOptions = {
		minDate: mainCtrl.dataLoadedDate
	};
	
	mainCtrl.openFromDate = function() {
		if(mainCtrl.toDate.getTime() < mainCtrl.fromDate.getTime()){
			mainCtrl.toDate = mainCtrl.fromDate;
		}
		$scope.toMinDate = mainCtrl.fromDate;
		mainCtrl.fromDateOpened = true;
	};
		  
	mainCtrl.openToDate = function() {
		$scope.toMinDate = mainCtrl.fromDate;
		mainCtrl.toDateOpened = true;
	};
	  
	$scope.$on("showError", function(event,message) {
		$scope.errorMessage = message;
       	$scope.showError = true;
        $scope.$apply();
	});
	
	mainCtrl.onEnvSelected = function(selectedEnv){
		if(mainCtrl.environment == selectedEnv){
			return;
		}
		mainCtrl.environment = selectedEnv;
		callApiService.getPlatformComponents(mainCtrl.environment,mainCtrl.selectedPlatform).then(function(data){
			mainCtrl.marathonComponents = data;
		});
		mainCtrl.replotGraph();
	};

	mainCtrl.changeInterval = function(){
		if(mainCtrl.selectedApis != null){
			graphService.changeIntervalContainerGraph(mainCtrl.graphInterval);
		}else {
			graphService.changeIntervalServiceGraph(mainCtrl.graphInterval);
		}
	};
	
	mainCtrl.replotGraph = function(){
		if(mainCtrl.selectedApis != null && mainCtrl.selectedApis.length > 0){
			if(covertComponentArray(mainCtrl.selectedApis) != null){
				graphService.plotContainerGraph(mainCtrl.environment, mainCtrl.fromDate, mainCtrl.toDate, 
						covertComponentArray(mainCtrl.selectedApis), null, mainCtrl.graphInterval, mainCtrl.selectedPlatform);
			}else{
				graphService.plotContainerGraph(mainCtrl.environment, mainCtrl.fromDate, mainCtrl.toDate, 
						null, covertComponentArray(mainCtrl.selectedApplications), mainCtrl.graphInterval, mainCtrl.selectedPlatform);
			}
		}else {
			graphService.plotServiceGraph(mainCtrl.environment, mainCtrl.fromDate, mainCtrl.toDate, 
					covertComponentArray(mainCtrl.selectedApplications), mainCtrl.graphInterval, mainCtrl.selectedPlatform)
		}
	};
	
	mainCtrl.getParentFullName = function(parentComponentId){
		for(var i =0; i < mainCtrl.marathonComponents.length; i++){
			if(mainCtrl.marathonComponents[i].componentId == parentComponentId){
				return (mainCtrl.marathonComponents[i].appFullName != null ? mainCtrl.marathonComponents[i].appFullName: mainCtrl.marathonComponents[i].componentName)
			}
		}
	};
	
	var covertComponentArray = function(componentArray) {
		var componentString = null;
		if(componentArray == null){
			return componentString;
		}
		for(componentId in componentArray){
			if(componentString == null && componentArray[componentId].componentId != -1){
				componentString = componentArray[componentId].componentId;
			} else if(componentArray[componentId].componentId != -1){
				componentString += "," + componentArray[componentId].componentId;
			}			
		}
		
		return componentString;
	}
	
	$scope.$watch("mainCtrl.fromDate", function(newValue, oldValue) {
		if(newValue != oldValue){
			mainCtrl.replotGraph();
		}
	});
	
	$scope.$watch("mainCtrl.toDate", function(newValue, oldValue) {
		if(newValue != oldValue){
			mainCtrl.replotGraph();
		}
	});
	
	$scope.$watch("mainCtrl.selectedApis", function(newValue, oldValue) {
		if(newValue != null && newValue != oldValue){
			mainCtrl.replotGraph();
		}
	});
	
	$scope.$watch("mainCtrl.selectedApplications", function(newValue, oldValue) {
		if(newValue != null && newValue != oldValue){
			mainCtrl.selectedApis = null;
			mainCtrl.replotGraph();
		}
	});
	//Code for Platform Selection
	$scope.$watch("mainCtrl.selectedPlatform", function(newValue, oldValue) {
				
		callApiService.getPlatformComponents(mainCtrl.environment,mainCtrl.selectedPlatform).then(function(data){
			mainCtrl.platform = data;
		});
			mainCtrl.replotGraph();
	});
	
	callApiService.getEnvironmentDetails().then(function(data){
		mainCtrl.envData = data;
	});
	
	callApiService.getPlatformComponents(mainCtrl.environment, mainCtrl.selectedPlatform).then(function(data){
		var apiDtsTableData = [];
		for(var i = 0; i < data.length; i++){
			for(var j = 0; data[i].hcEnvList != null && j < data[i].hcEnvList.length; j++){
				apiDtsTableData.push({
					"componentId": data[i].componentId,
					"parentComponentId": data[i].parentComponentId,
					"parentComponentName": data[i].parentComponentName,
					"componentName": data[i].componentName,
					"createdDate": data[i].hcEnvList[j].createdDate,
					"envName": data[i].hcEnvList[j].envName
				});
			}
		}
		mainCtrl.apiDtsTableData = apiDtsTableData;
		mainCtrl.marathonComponents = data;
		mainCtrl.marathonComponents.push({componentId: -1, parentComponentId: -1, componentName: ' All '});
	});
	
	angular.element($window).bind('resize', function(){
		mainCtrl.changeInterval();
	});
	
	mainCtrl.replotGraph();
	
    return mainCtrl;
};
