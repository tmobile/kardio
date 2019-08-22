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
GDMApp.factory('graphService', graphService);

graphService.$inject = [ 'httpService', '$filter', 'callApiService'];

function graphService(httpService, $filter, callApiService) {
	var graphService = {};
	graphService.ctx = document.getElementById("canvas").getContext("2d");
	graphService.getDataForDate = function(inputData, labelDataArray, totalNumDataArray, deltaNumDataArray){
		var matchingRecord = null;
		for(var i = 0; i < labelDataArray.length; i++){
			if(labelDataArray[i] == inputData.statusDate){
				matchingRecord = {
						totalContainers: totalNumDataArray[i],
						deltaValue: deltaNumDataArray[i],
						indexOfData: i
					};
				break;
			}
		}
		return matchingRecord;
	};
	
	graphService.alterLabelForInterval = function(labelData, interval){
		if(interval == null || interval == 'day'){
			return labelData;
		}
		var labelDataNew = [];
		var lastLabelDate = null;
		for(var i = 0; i < labelData.length; i++){
			if(lastLabelDate == null){
				labelDataNew.push(labelData[i]);
				lastLabelDate = new Date(labelData[i]);
				continue;
			}
			if(interval == 'week' && new Date(labelData[i]).getDay() == lastLabelDate.getDay()){
				labelDataNew.push(labelData[i]);
				lastLabelDate = new Date(labelData[i]);
				continue;
			}
			var diffMonth = new Date(labelData[i]).getMonth() - lastLabelDate.getMonth();
			if(interval == 'month' && (diffMonth == 1 || diffMonth == 11) 
					&& (new Date(labelData[i]).getDate() == lastLabelDate.getDate() )
							|| (lastLabelDate.getDate() == 31 && new Date(labelData[i]).getDate() == 30)){
				labelDataNew.push(labelData[i]);
				lastLabelDate = new Date(labelData[i]);
				continue;
			}
		}
		return labelDataNew;
	};
	
	graphService.alterDataForInterval = function(labelData, totalNumData, interval, isDeltaCalc){
		if(interval == null || interval == 'day'){
			return totalNumData;
		}
		var totalNumDataNew = [];
		var lastLabelDate = null;
		var totalNumSum = 0;
		var deltaPrev = 0;
		var totalNumCount = 0;
		for(var i = 0; i < labelData.length; i++){
			if(lastLabelDate == null){
				totalNumDataNew.push(totalNumData[i]);
				lastLabelDate = new Date(labelData[i]);
				continue;
			}
			if(interval == 'week' && new Date(labelData[i]).getDay() == lastLabelDate.getDay()){
				totalNumDataNew.push(Math.round((totalNumSum + totalNumData[i]) / (totalNumCount + 1)));
				lastLabelDate = new Date(labelData[i]);
				totalNumSum = 0;
				totalNumCount = 0;
				deltaPrev = 0;
				continue;
			}
			var diffMonth = new Date(labelData[i]).getMonth() - lastLabelDate.getMonth();
			if(interval == 'month' && (diffMonth == 1 || diffMonth == 11) 
					&& (new Date(labelData[i]).getDate() == lastLabelDate.getDate() )
							|| (lastLabelDate.getDate() == 31 && new Date(labelData[i]).getDate() == 30)){
				totalNumDataNew.push(Math.round((totalNumSum + totalNumData[i]) / (totalNumCount + 1)));
				lastLabelDate = new Date(labelData[i]);
				totalNumSum = 0;
				totalNumCount = 0;
				deltaPrev = 0;
				continue;
			}
			if(isDeltaCalc && totalNumData[i] == 0){
				totalNumSum += deltaPrev;
			}else{
				totalNumSum += totalNumData[i];
				deltaPrev = totalNumData[i];
			}
			totalNumCount += 1;
		}
		return totalNumDataNew;
	};
	graphService.plotGraph = function(labelData ,totalNumData, deltaNumData, tpsData, latencyData,k8sPodsData,graphType, platform) {
		var chartData = null;
		
		if(graphType == 'Services'){
					   
			if(platform == 'Mesos'){
				chartData = {
			   labels: labelData,
			    datasets: [{
			        type: 'line',
			        label: 'Total Number of Services',
			        borderColor: 'rgb(54, 162, 235)',
			        borderWidth: 2,
			        fill: false,
			        data: totalNumData
					 
			    }, {
			         type: 'line',
			        label: 'Total Number of Containers',
			        borderColor: 'rgb(54, 002, 235)',
			        borderWidth: 2,
			        fill: false,
			        data: deltaNumData
			    },{
			        type: 'line',
			        label: 'Total RPS',
			        borderColor: 'rgb(75, 192, 192)',
			        borderWidth: 2,
			        fill: false,
			        data: tpsData
			    },{
			         type: 'line',
			        label: 'Max Latency',
			        borderColor: 'rgb(91,200,84)',
			        borderWidth: 2,
			        fill: false,
			        data: latencyData
			    }]
			};}
			else{
			   chartData = {
			   labels: labelData,
			    datasets: [{
			        type: 'line',
			        label: 'Total Number of Services',
			        borderColor: 'rgb(54, 162, 235)',
			        borderWidth: 2,
			        fill: false,
			        data: totalNumData
					 
			    }, {
			         type: 'line',
			        label: 'Total Number of Containers',
			        borderColor: 'rgb(54, 002, 235)',
			        borderWidth: 2,
			        fill: false,
			        data: deltaNumData
			    },{
			        type: 'line',
			        label: 'Total RPS',
			        borderColor: 'rgb(75, 192, 192)',
			        borderWidth: 2,
			        fill: false,
			        data: tpsData
			    },{
			         type: 'line',
			        label: 'Max Latency',
			        borderColor: 'rgb(91,200,84)',
			        borderWidth: 2,
			        fill: false,
			        data: latencyData
			    },{
			         type: 'line',
			        label: 'Total Number of Pods',
			        borderColor: 'rgb(60,007,60)',
			        borderWidth: 2,
			        fill: false,
			        data: k8sPodsData
			    }]
			};
			}
		   }
			else{
				if(platform == 'Mesos'){
			  chartData = {
			    labels: labelData,
			    datasets: [{
			         type: 'line',
			        label: 'Total Number of Containers',
			        borderColor: 'rgb(54, 002, 235)',
			        borderWidth: 2,
			        fill: false,
			        data: deltaNumData
			    },{
			        type: 'line',
			        label: 'Total RPS',
			        borderColor: 'rgb(75, 192, 192)',
			        borderWidth: 2,
			        fill: false,
			        data: tpsData
			    },{
			         type: 'line',
			        label: 'Max Latency',
			        borderColor: 'rgb(91,200,84)',
			        borderWidth: 2,
			        fill: false,
			        data: latencyData
			    }]
			};
				}
				
				else{
					chartData = {
			   labels: labelData,
			    datasets: [{
			        type: 'line',
			        label: 'Total Number of Containers',
			        borderColor: 'rgb(54, 002, 235)',
			        borderWidth: 2,
			        fill: false,
			        data: deltaNumData
			    },{
			        type: 'line',
			        label: 'Total RPS',
			        borderColor: 'rgb(75, 192, 192)',
			        borderWidth: 2,
			        fill: false,
			        data: tpsData
			    },{
			         type: 'line',
			        label: 'Max Latency',
			        borderColor: 'rgb(91,200,84)',
			        borderWidth: 2,
			        fill: false,
			        data: latencyData
			    },{
			         type: 'line',
			        label: 'Total Number of Pods',
			        borderColor: 'rgb(60,007,60)',
			        borderWidth: 2,
			        fill: false,
			        data: k8sPodsData
			    }]
			};
				}
			}
		if(graphService.statsChart){
			graphService.statsChart.destroy();
		}
		
		if(graphType == 'Containers'/* && platform == 'Mesos'*/){
			if(platform == 'Mesos'){
		graphService.statsChart = new Chart(graphService.ctx, {
		    type: 'bar',
		    data: chartData,
		    options: {
		        responsive: true,
		        title: {
		            display: true,
					text: 'Current Number Of Containers : '+graphService.currentApiContainerData+ '              Current TPS : '+graphService.currentTps+ '              Current Latency : '+graphService.currentLatency
		        },
		        tooltips: {
		            mode: 'index',
		            intersect: true
		        }
		    }
		});
		}
		
		else /*if(graphType == 'Containers'/*&& platform == 'All')*/
		{
			graphService.statsChart = new Chart(graphService.ctx, {
		    type: 'bar',
		    data: chartData,
		    options: {
		        responsive: true,
		        title: {
		            display: true,
					text: 'Current Number Of Containers : '+graphService.currentApiContainerData+'              Current TPS : '+graphService.currentTps+ '              Current Latency : '+graphService.currentLatency +'              Current Number of Pods : '+graphService.currPodsData
		        },
		        tooltips: {
		            mode: 'index',
		            intersect: true
		        }
		    }
		});
		}
		}
	
		else 
		{
		if(/*graphType == 'Services' &&*/ platform == 'Mesos'){
			graphService.statsChart = new Chart(graphService.ctx, {
		    type: 'bar',
		    data: chartData,
		    options: {
		        responsive: true,
		        title: {
		            display: true,
					text: 'Current Number Of Apis : '+graphService.currentApiData+ '              Current Number Of Containers : '+graphService.currentContainerData+ '              Current TPS : '+graphService.currentTps+ '              Current Latency : '+graphService.currentLatency
		        },
		        tooltips: {
		            mode: 'index',
		            intersect: true
		        }
		    }
		});
		}
		else{
			graphService.statsChart = new Chart(graphService.ctx, {
		    type: 'bar',
		    data: chartData,
		    options: {
		        responsive: true,
		        title: {
		            display: true,
					text: 'Current Number Of Apis : '+graphService.currentApiData+ '              Current Number Of Containers : '+graphService.currentContainerData+ '              Current TPS : '+graphService.currentTps+ '              Current Latency : '+graphService.currentLatency+ '              Current Number of Pods : '+graphService.currPodsData
		        },
		        tooltips: {
		            mode: 'index',
		            intersect: true
		        }
		    }
		});
		}	
	}
	}
	/** Function to merge labelData1,k8sLabels with labelData 
		 ** after removing the duplicate values
		**/
	graphService.mergeLabels = function(labelData,labelData1){	
		var labelData2=[];
		
		
		labelData = labelData.sort();
		
		for(var i=0;i<labelData1.length;i++)
		{
		if(labelData.indexOf(labelData1[i])==-1)
	        labelData2.push(labelData1[i]);
		}
		
		if(labelData2.length!=0)
		{
		labelData = labelData.concat(labelData2);
		labelData = labelData.sort();
		}
		
		return labelData;
	}
	graphService.processAndPlotGraph = function(data, interval, graphType, platform) {
		var labelDataCS = [];
		var labelData = [];
		var labelData1 = [];
		var k8sLabels = [];
		var totalNumData = [];
		var deltaNumData = [];
		var numData =[];
		var deltaNumDatas =[];
		var totalTpsData = [];
		var totalLatencyData = [];
		var totalPodsData = [];
		var data1=graphService.tlData;
		var k8sData = graphService.k8sData;
		
		for(var i = 0; i < data.length; i++){
			var statusDate = data[i].statusDate;
			var totalContainers = data[i].totalApis;
			var deltaValue = data[i].totalContainers;
			var matchingRecord = graphService.getDataForDate(data[i], labelDataCS, numData, deltaNumDatas);
			if(matchingRecord == null) {
				labelDataCS.push(statusDate);
				numData.push(totalContainers);
				deltaNumDatas.push(deltaValue);
			} else {
				numData[matchingRecord.indexOfData] = matchingRecord.totalContainers + totalContainers;
				deltaNumDatas[matchingRecord.indexOfData] = matchingRecord.deltaValue + deltaValue;
			}
			
			
		}
		
		// Code for K8s Pods Status Data

		if(k8sData!=null){
		for(var i = 0; i < k8sData.length; i++){
			var statusDate = k8sData[i].statusDate;
			var totalPods = k8sData[i].totalPods;
			
	
			var matchingRecord = graphService.getDataForDate(k8sData[i], k8sLabels, totalPodsData, totalPodsData);
			if(matchingRecord == null) {
				k8sLabels.push(statusDate);
			    totalPodsData.push(totalPods);
			    
			} else {
				totalPodsData[matchingRecord.indexOfData] = matchingRecord.totalContainers + totalPods;
				
			}
		}
		}	

	  if(data1!=null){
		for(var i = 0; i < data1.length; i++){
			var statusDate = data1[i].statusDate;
			var totalTps = data1[i].tpsValue;
			var totalLatency = data1[i].latencyValue;
	
			var matchingRecord = graphService.getDataForDate(data1[i], labelData1, totalTpsData, totalLatencyData);
			if(matchingRecord == null) {
				labelData1.push(statusDate);
			    totalTpsData.push(totalTps);
			    totalLatencyData.push(totalLatency);	
			} else {
				totalTpsData[matchingRecord.indexOfData] = parseFloat((matchingRecord.totalContainers + totalTps)).toFixed(2);
				totalLatencyData[matchingRecord.indexOfData] = parseFloat((matchingRecord.deltaValue + totalLatency)).toFixed(2);
			}
		}
		}	

	    /** Merging labelData1,k8sLabels with labelDataCS 
	    ** as labelData after removing the duplicate values
	    */
	
		if(platform == "Mesos"||platform == "All" || platform == null)
		labelDataA1 = graphService.mergeLabels(labelDataCS,labelData1);
	    if(platform == "K8s"||platform == "All" || platform == null)
		labelDataA2 = graphService.mergeLabels(labelDataCS,k8sLabels);
	    labelData = graphService.mergeLabels(labelDataA1,labelDataA2);
	
		//var currentPodsData=0;
		var tpsData =[];
		var latencyData=[];
		var k8sPodsData=[];
		for(var i = 0;i <labelData.length;i++){
			if(labelData1.indexOf(labelData[i])!=-1)
			{
		      tpsData[i] = totalTpsData[labelData1.indexOf(labelData[i])];
			  latencyData[i] = totalLatencyData[labelData1.indexOf(labelData[i])];
			}
			else{
			tpsData[i]=null;
			latencyData[i]=null;
			}
			
			if(k8sLabels.indexOf(labelData[i])!=-1)
			{
		      k8sPodsData[i] = totalPodsData[k8sLabels.indexOf(labelData[i])];
			 // currentPodsData = currentPodsData + k8sPodsData[i];//Current Number of Pods
			}
			else{
			 k8sPodsData[i]=null;
			}
			if(labelDataCS.indexOf(labelData[i])!=-1)
			{
		      totalNumData[i] = numData[labelDataCS.indexOf(labelData[i])];
			  deltaNumData[i] = deltaNumDatas[labelDataCS.indexOf(labelData[i])];
			}
			else{
			totalNumData[i]=null;
			deltaNumData[i]=null;
			}
		}
	
		totalNumData = graphService.alterDataForInterval(labelData, totalNumData, interval, false);
		deltaNumData = graphService.alterDataForInterval(labelData, deltaNumData, interval, true);
	    tpsData = graphService.alterDataForInterval(labelData, tpsData, interval, false);
		latencyData = graphService.alterDataForInterval(labelData, latencyData, interval, false);
		k8sPodsData = graphService.alterDataForInterval(labelData,k8sPodsData ,interval, false);
		labelData = graphService.alterLabelForInterval(labelData, interval);
	    graphService.plotGraph(labelData,totalNumData, deltaNumData, tpsData, latencyData,k8sPodsData,graphType, platform);
		
	}
	
	
	    
	graphService.plotContainerGraph = function(environment, fromDate, toDate, componentIdList, parentComponentIdList,  interval, platform){
		callApiService.getCurrentContainers(environment, componentIdList, false,platform).then(function(data){
			graphService.currentApiContainerData = data;
		});
		callApiService.getTpsLatency(environment,componentIdList,platform, false).then(function(data){
		    graphService.currentTps = data.tpsValue;
			graphService.currentLatency = data.latencyValue;
		});
				
		callApiService.getCurrentNumberOfPods(environment, componentIdList, false).then(function(data){
			graphService.currPodsData = data;
		});
		/**  K8s services code change
		 **  Updated Code for API Dashboard Bug Fix
		 */
		callApiService.getContainerStats(environment, $filter('date')(fromDate, "yyyy-MM-dd"), 
				$filter('date')(toDate, "yyyy-MM-dd"), componentIdList != null ? componentIdList : parentComponentIdList,
				parentComponentIdList == null ? false: true,platform).then(function(data){
			graphService.currentRawData = data;
			callApiService.getTotalTpsLatency(environment,$filter('date')(fromDate, "yyyy-MM-dd"), 
				$filter('date')(toDate, "yyyy-MM-dd"), componentIdList,false,platform).then(function(data){
			graphService.tlData = data;
			callApiService.getK8sPodsStatus(environment,$filter('date')(fromDate, "yyyy-MM-dd"), 
				$filter('date')(toDate, "yyyy-MM-dd"), componentIdList,false).then(function(data){
			graphService.k8sData = data;
			graphService.processAndPlotGraph(graphService.currentRawData, interval, 'Containers', platform);
		});
		});
		});
		
		
		
	};
	/** Added platform parameter to plotServiceGraph function 
	 ** to get current apis based on platform
	 */
	graphService.plotServiceGraph = function(environment, fromDate, toDate, componentIdList, interval, platform){
		callApiService.getCurrentApis(environment, componentIdList, platform).then(function(data1){
			graphService.currentApiData = data1;
		});
		
		callApiService.getCurrentContainers(environment, componentIdList, true, platform).then(function(data){
			graphService.currentContainerData = data;

		});
		
		callApiService.getCurrentNumberOfPods(environment, componentIdList, true).then(function(data){
			graphService.currPodsData = data;
		});
		callApiService.getTpsLatency(environment,componentIdList,platform, true).then(function(data){
		    graphService.currentTps = data.tpsValue;
			graphService.currentLatency = data.latencyValue;
		});
				
		/** K8s services code change - added parameter platform
		 ** Updated Code for API Dashboard Bug Fix
		 */
		callApiService.getApiStats(environment, $filter('date')(fromDate, "yyyy-MM-dd"), 
				$filter('date')(toDate, "yyyy-MM-dd"), componentIdList,platform).then(function(data){
			graphService.currentRawData = data;
			callApiService.getK8sPodsStatus(environment,$filter('date')(fromDate, "yyyy-MM-dd"), 
				$filter('date')(toDate, "yyyy-MM-dd"), componentIdList,true).then(function(data){

			graphService.k8sData = data;
			callApiService.getTotalTpsLatency(environment,$filter('date')(fromDate, "yyyy-MM-dd"), 
				$filter('date')(toDate, "yyyy-MM-dd"), componentIdList,true,platform).then(function(data){

			graphService.tlData = data;
			graphService.processAndPlotGraph(graphService.currentRawData, interval, 'Services', platform);
			});
	
		});
		});
	
		
	};

	graphService.changeIntervalContainerGraph = function(interval){
		graphService.processAndPlotGraph(graphService.currentRawData, interval, 'Containers');
	};
	
	graphService.changeIntervalServiceGraph = function(interval){
		graphService.processAndPlotGraph(graphService.currentRawData, interval, 'Services');
	};
	
	return graphService;
}
