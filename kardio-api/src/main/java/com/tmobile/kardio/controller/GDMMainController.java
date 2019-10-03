/*******************************************************************************
 * Copyright 2019 T-Mobile USA, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * See the LICENSE file for additional language around disclaimer of warranties.
 * Trademark Disclaimer: Neither the name of "T-Mobile, USA" nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 ******************************************************************************/
package com.tmobile.kardio.controller;

import com.tmobile.kardio.bean.*;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.service.AdminService;
import com.tmobile.kardio.service.RegionStatusService;
import com.tmobile.kardio.util.LDAPAuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * This main RestController handles the GET method requests Handles all the common request in SHD Controller for the
 * Admin page.
 */
@RestController
@Api(description="GDM Main Controller handles the other requests")
public class GDMMainController {
    private static Logger log = LoggerFactory.getLogger(GDMMainController.class);

    @Autowired
    private RegionStatusService regionStatusService;
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private LDAPAuthUtil ldapAuthUtil;

    /**
     * Login controller
     * 
     * @param user
     * @return
     * @throws NamingException
     */
    @ApiOperation(value="Login Authentiction")
    @CrossOrigin
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public ResponseEntity<GDMResponse> doLogin(@RequestBody User user) throws NamingException {
        log.debug("********************* doLogin ********************************");

        if (user.getUserName() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User Id and password cannot be null");
        }

        User responseUser = ldapAuthUtil.authenticateAndGetGroups(user.getUserName(), user.getPassword());
        responseUser.setAuthToken("GDM" + new Date().getTime());
        responseUser.setTimeOutMinute(Constants.SESSION_EXPIRATION_TIME_MINUTES + "");
        regionStatusService.doLoginForUser(responseUser);

        return new ResponseEntity<GDMResponse>(new GDMResponse(responseUser), HttpStatus.OK);
    }

    /**
     * Log out controller
     * 
     * @param token
     * @return
     */
    @ApiOperation(value="Logout API")
    @CrossOrigin
    @RequestMapping(value = "/doLogout", method = RequestMethod.POST)
    public ResponseEntity<GDMResponse> doLogout(@RequestBody String token) {
        regionStatusService.doLogOutForUser(token);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Logged Out successfully"), HttpStatus.OK);
    }

    /**
     * Gets Status of Components.
     * 
     * @return
     */
    @ApiOperation(value="Get all Component Status")
    @CrossOrigin
    @RequestMapping(value = "/component", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getComponentStatus(@RequestParam("environment") String environmentName) {
        log.debug("********************* getComponentStatus ********************************");
        StatusResponse response = regionStatusService.getComponentRegion(environmentName);
        return new ResponseEntity<GDMResponse>(new GDMResponse(response), HttpStatus.OK);
    }

    /**
     * Saves the message
     * 
     * @param compMessage
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Update Status Message")
    @CrossOrigin
    @RequestMapping(value = "/updateStatusMsg", method = { RequestMethod.PUT })
    public ResponseEntity<GDMResponse> saveMessage(@RequestParam("environment") String environmentName, @RequestParam("type") String messageType,
            @RequestParam("messages") String message, @RequestParam("authToken") String authToken) throws InstantiationException {
        log.debug("********************* saveMessage ********************************");
        regionStatusService.loadMessages(environmentName, messageType, message, authToken);
        String auditLog = "Added Message: "+message+" for Message Type: "+messageType;
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Updated Data"), HttpStatus.OK);
    }

    /**
     * Gets History of Components.
     * 
     * @param environmentName
     * @return
     * @throws ParseException 
     */
    @ApiOperation(value="Get all Components Status History")
    @CrossOrigin
    @RequestMapping(value = "/componentHistory", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getComponentStatusHistory(@RequestParam("environment") String environmentName) throws ParseException {
        log.debug("********************* getComponentStatusHistory ********************************");
        HistoryResponse components = regionStatusService.getRegionStatusHistory(environmentName);
        return new ResponseEntity<GDMResponse>(new GDMResponse(components), HttpStatus.OK);
    }

    /**
     * Get Component Message.
     * 
     * @param environmentName
     * @param componentId
     * @param region
     * @return
     */
    @ApiOperation(value="Get Component Message")
    @CrossOrigin
    @RequestMapping(value = "/componentMessage", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getComponentMessage(@RequestParam("environment") String environmentName,
            @RequestParam("componentid") String componentId, @RequestParam("region") String region) {
        log.debug("********************* getComponentMessage ********************************");
        List<Messages> listMessages = regionStatusService.getComponentMessage(environmentName, componentId, region);
        return new ResponseEntity<GDMResponse>(new GDMResponse(listMessages), HttpStatus.OK);
    }

    /**
     * Save Component Message.
     * 
     * @param compMessage
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Save Component Message")
    @CrossOrigin
    @RequestMapping(value = "/saveCompMessage", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> saveComponentMessage(@RequestBody ComponentMessages compMessage) throws InstantiationException {
        log.debug("********************* saveMessage ********************************");
        regionStatusService.loadComponentMessages(compMessage);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Updated Data"), HttpStatus.OK);
    }

    /**
     * Get all the Environments.
     * 
     * @return
     */
    @ApiOperation(value="Get All Environments")
    @CrossOrigin
    @RequestMapping(value = "/getEnvironments", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getEnvironments() {
        log.debug("********************* getEnvironments ********************************");
        List<Environment> environments = regionStatusService.getEnvironments();
        return new ResponseEntity<GDMResponse>(new GDMResponse(environments), HttpStatus.OK);
    }

    /**
     * Controller to save the subscription details when the subscribe button is clicked in the UI
     * 
     * @param subscription
     * @return
     */
    @ApiOperation(value="Subscribe alerts")
    @CrossOrigin
    @RequestMapping(value = "/subscribeAlerts", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> subscribeAlerts(@RequestBody Subscription subscription) {
        // Logic to save subscription into the DB
        regionStatusService.subscribeAlert(subscription);
        String returnString = "";
        if (subscription.getSubsciptionType().equalsIgnoreCase("email")) {
            returnString = "A validation is mail is sent to " + subscription.getSubsciptionVal() + ". Please check the mail and confirm.";
        } else if (subscription.getSubsciptionType().equalsIgnoreCase("slack")
                || subscription.getSubsciptionType().equalsIgnoreCase("slackChannel")) {
            returnString = "Subscription Successfull. Regular alerts will be sent to the subscribed channel " + subscription.getSubsciptionVal();
        } else if (subscription.getSubsciptionType().equalsIgnoreCase("oneConsoleWebhook")) {
            returnString = "Subscription Successfull. Regular alerts will be sent to the subscribed Webhook URL " + subscription.getSubsciptionVal();
        }
        return new ResponseEntity<GDMResponse>(new GDMResponse(returnString), HttpStatus.OK);
    }

    /**
     * Confirm Subscription
     * 
     * @param authToken
     * @return
     */
    @ApiOperation(value="Confirm Subscription")
    @CrossOrigin
    @RequestMapping(value = "/confirmSubscription", method = { RequestMethod.GET })
    public ResponseEntity<GDMResponse> confirmSubscription(@RequestParam("authToken") String authToken) {
        regionStatusService.confirmSubscription(authToken);
        return new ResponseEntity<GDMResponse>(new GDMResponse("You have successfully subscribed to receive alerts for the component"),
                HttpStatus.OK);
    }

    /**
     * Un subscribe alert related to a component
     * 
     * @param subscription
     * @return
     */
    @ApiOperation(value="Un Subscribe alerts")
    @CrossOrigin
    @RequestMapping(value = "/unsubscribeAlerts", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> unsubscribeAlerts(@RequestBody Subscription subscription) {
        // Logic to delete subscription from the DB
        regionStatusService.unsubscribeAlert(subscription);
        String returnString = "";
        if (subscription.getSubsciptionType().equals("email")) {
            returnString = "An email is sent to " + subscription.getSubsciptionVal() + " for confirmation.";
        } else {
            returnString = "You have unsubscribed to receive alerts in the channel " + subscription.getSubsciptionVal();
        }
        return new ResponseEntity<GDMResponse>(new GDMResponse(returnString), HttpStatus.OK);
    }

    /**
     * Function to delete subscription for a component
     * 
     * @param subscription
     * @return
     */
    @ApiOperation(value="Delete Subscription")
    @CrossOrigin
    @RequestMapping(value = "/deletesubscription", method = { RequestMethod.DELETE })
    public ResponseEntity<GDMResponse> deletesubscription(@RequestParam("authToken") String authToken) {
        regionStatusService.deleteSubscription(authToken);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Unsubscribed to stop receiving alerts for component"), HttpStatus.OK);
    }

    /**
     * List the Subscriptions.
     * 
     * @param componentId
     * @param environment
     * @return
     */
    @ApiOperation(value="Get Subscription Email")
    @CrossOrigin
    @RequestMapping(value = "/getSubscriptionMails", method = { RequestMethod.GET })
    public ResponseEntity<GDMResponse> getSubscriptionMails(@RequestParam("componentId") int componentId,
            @RequestParam("environment") String environment) {
        List<Subscription> emails = regionStatusService.getSubscribedEmailIdList(componentId, environment);
        return new ResponseEntity<GDMResponse>(new GDMResponse(emails), HttpStatus.OK);
    }

    /**
     * Get All Counter Metrix related to the given environment and platform.
     * 
     * @param environmentName
     * @param platform
     * @return
     */
    @ApiOperation(value="Get all Counter Metrics")
    @CrossOrigin
    @RequestMapping(value = "/getCounterMetrix", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getCounterMetrix(@RequestParam(value = "environment", required = false) String environmentName,
    		@RequestParam(value = "platform", required = false) String platform) {
        log.debug("********************* getCounterMetrix ********************************");
        List<Counters> listOfCounters = regionStatusService.getCountersMatrix(environmentName, platform);
        return new ResponseEntity<GDMResponse>(new GDMResponse(listOfCounters), HttpStatus.OK);
    }

    /**
     * Function to get older component related messages for a particular date
     * 
     * @param environmentName
     * @param componentId
     * @param region
     * @param logDate
     * @return
     */
    @ApiOperation(value="Get Component Message History")
    @CrossOrigin
    @RequestMapping(value = "/getCompMessageHistory", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getCompMessageHistory(@RequestParam("environment") String environmentName,
            @RequestParam("componentid") String componentId, @RequestParam("region") String region, @RequestParam("logDate") String logDate) {
        List<Messages> listMessages = regionStatusService.getComponentMessage(environmentName, componentId, region, logDate);
        return new ResponseEntity<GDMResponse>(new GDMResponse(listMessages), HttpStatus.OK);
    }

    /**
     * Get the Region wise availability percentage of all the Infa & App Services.
     * 
     * @param environmentName
     * @param interval
     * @param platform
     * @param region
     * @return
     * @throws ParseException
     */
    @ApiOperation(value="Get all component availability percentage")
    @CrossOrigin
    @RequestMapping(value = "/getAvailabilityPercentage", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getAvailabilityPercentage(@RequestParam("environment") String environmentName,
            @RequestParam("interval") String interval,
            @RequestParam("platform") String platform,
            @RequestParam("region") String region) throws ParseException {
        List<AvailabilityData> listAvlData = regionStatusService.getAvailabilityPercentage(environmentName, interval,platform,region);
        return new ResponseEntity<GDMResponse>(new GDMResponse(listAvlData), HttpStatus.OK);
    }

    /**
     * Get all the components in the given platform & environment.
     * 
     * @param environmentName
     * @param platform
     * @return
     */
    @ApiOperation(value="Get All Components in the given platform")
    @CrossOrigin
    @RequestMapping(value = "/getPlatformComponents", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getPlatformComponents(@RequestParam("environment") String environmentName,
    		@RequestParam(value="platform",required = false) String platform) {
    	 log.debug("********************* /getPlatformComponents ********************************");
        List<Component> compList = regionStatusService.getPlatformComponents(environmentName,platform);
        return new ResponseEntity<GDMResponse>(new GDMResponse(compList), HttpStatus.OK);
    }
    
    /**
     * Get The Service Container info based on the input parameters.
     * @param startDate
     * @param endDate
     * @param environment
     * @param componentIdsStrg
     * @param platform
     * @param isParentComponents
     * @return
     * @throws ParseException
     */
    @ApiOperation(value="Get all the Service related container Info")
    @CrossOrigin
   	@RequestMapping(value = "/getAppContainers", method = RequestMethod.GET)
   	public ResponseEntity<GDMResponse> getAppContainers(@RequestParam("startDate") String startDate,
   			@RequestParam("endDate") String endDate, @RequestParam(value = "environment",required = false) String environment,
   			@RequestParam(value = "component",required = false) String componentIdsStrg,
   			@RequestParam(value = "platform",required = false) String platform,
   			@RequestParam(value = "isParentComponents",required = false) boolean isParentComponents) throws ParseException{
   		List<ContainerStatus> listContSts = regionStatusService.getAppContainers(startDate, endDate, environment, componentIdsStrg, isParentComponents,platform);
   		return new ResponseEntity<GDMResponse>(new GDMResponse(listContSts),HttpStatus.OK);
   	}
       
    /**
     * Get The Application APIS info based on the input parameters.
     
     * @param startDate
     * @param endDate
     * @param environment
     * @param componentIdsStrg
     * @param platform
     * @return
     * @throws ParseException
     */
    @ApiOperation(value="Get all the Application related container Info")
    @CrossOrigin
  	@RequestMapping(value = "/getApplicationApis", method = RequestMethod.GET)
  	public ResponseEntity<GDMResponse> getApplicationApis(@RequestParam("startDate") String startDate,
  			@RequestParam("endDate") String endDate, @RequestParam(value = "environment",required = false) String environment,
  			@RequestParam(value = "component",required = false) String componentIdsStrg,
  			@RequestParam(value = "platform",required = false) String platform) throws ParseException{
    	List<ApiStatus> listOfApiSts = regionStatusService.getApplicationApis(startDate, endDate, environment, componentIdsStrg, platform);
  		return new ResponseEntity<GDMResponse>(new GDMResponse(listOfApiSts),HttpStatus.OK);
  	}
   
    /**
     * Get Current Apis
     * @param environment
     * @param platform
     * @param componentIdsStrg
     * @return
     * @throws ParseException
     */
    @ApiOperation(value="Get the current Application Apis Info")
    @CrossOrigin
  	@RequestMapping(value = "/getCurrentApis", method = RequestMethod.GET)
  	public ResponseEntity<GDMResponse> getCurrentApis(@RequestParam(value = "environment",required = false) String environment,
  			@RequestParam(value = "platform",required = false) String platform,
  			@RequestParam(value = "component",required = false) String componentIdsStrg) throws ParseException{
  		 long currentApis = regionStatusService.getCurrentApi(environment, componentIdsStrg,platform);
    	 return new ResponseEntity<GDMResponse>(new GDMResponse(currentApis),HttpStatus.OK);
  	}
    
    /**
     * get Current Containers.
     * @param environment
     * @param componentIdsStrg
     * @param platform
     * @param isParentComponents
     * @return
     * @throws ParseException
     */
    @ApiOperation(value="Get the current Application container Info")
    @CrossOrigin
  	@RequestMapping(value = "/getCurrentContainers", method = RequestMethod.GET)
  	public ResponseEntity<GDMResponse> getCurrentContainers(@RequestParam(value = "environment",required = false) String environment,
  			@RequestParam(value = "component",required = false) String componentIdsStrg,
  			@RequestParam(value = "platform",required = false) String platform,
  			@RequestParam(value = "isParentComponents",required = false) boolean isParentComponents) throws ParseException{
  		 long currentApis = regionStatusService.getCurrentContainer(environment, componentIdsStrg,isParentComponents,platform);
    	 return new ResponseEntity<GDMResponse>(new GDMResponse(currentApis),HttpStatus.OK);
  	}
    
    /**
     * Get Current TPS and Latency
     * @param environment
     * @return
     */
    @ApiOperation(value="Get Current Tps and Latency Info")
    @CrossOrigin
    @RequestMapping(value = "/getAllCurrentTpsLatency", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getCurrentTPS(@RequestParam(value = "environment",required = false) String environment,
    		@RequestParam(value = "platform",required = false) String platform) throws ParseException{
    	log.debug("********************* getTPS ********************************");
    	List<TpsLatency> tpsLatencyValues = regionStatusService.getTpsAndLatency(environment,platform);
    	return new ResponseEntity<GDMResponse>(new GDMResponse(tpsLatencyValues), HttpStatus.OK);
    }
    @ApiOperation(value="Get the Total Tps and Latency Info")
    @CrossOrigin
  	@RequestMapping(value = "/getTotalTpsLatency", method = RequestMethod.GET)
  	public ResponseEntity<GDMResponse> getTotalTpsLatency(@RequestParam(value ="startDate") String startDate,
  			@RequestParam(value ="endDate") String endDate, @RequestParam(value = "environment",required = false) String environment,
  			@RequestParam(value = "platform",required = false) String platform,
  	        @RequestParam(value = "component",required = false) String componentIdsStrg, @RequestParam(value = "isParent",required = false) boolean isParent)throws ParseException{
  		List<TpsLatencyHistory> listOfTpsLatencySts = regionStatusService.getAppTpsLatency(startDate, endDate, environment,platform,componentIdsStrg, isParent);
  		return new ResponseEntity<GDMResponse>(new GDMResponse(listOfTpsLatencySts),HttpStatus.OK);
  	}
    
    /**
     * Get Current TPS and Latency
     * @param environment
     * @return
     */
    @ApiOperation(value="Get Tps Latency API")
    @CrossOrigin
    @RequestMapping(value = "/getTpsLatency", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getCurrentTpsLatency(@RequestParam(value = "environment",required = false) String environment,
    		@RequestParam(value = "component",required = false) String componentIdsStrg,
    		@RequestParam(value = "platform",required = false) String platform,
  			@RequestParam(value = "isParent",required = false) boolean isParent) throws ParseException{
    	log.debug("********************* getCurrentTpsLatency ********************************");
    	TpsLatency tpsLatencyValues = regionStatusService.getCurrentTpsAndLatency(environment,componentIdsStrg,platform, isParent);
    	return new ResponseEntity<GDMResponse>(new GDMResponse(tpsLatencyValues), HttpStatus.OK);
    }
    
    /**
     * Get k8s Pods Status
     * @param environment
     * @return
     */
    @ApiOperation(value="Get K8s Pods Status")
    @CrossOrigin
    @RequestMapping(value = "/getK8sPodsStatus", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getK8sPodsStatus(@RequestParam(value ="startDate") String startDate,
  			@RequestParam(value ="endDate") String endDate, @RequestParam(value = "environment",required = false) String environment,
  	        @RequestParam(value = "component",required = false) String componentIdsStrg,
  	      @RequestParam(value = "isParent",required = false) boolean isParent) throws ParseException{
    	log.debug("********************* getK8sPodsStatus ********************************");
    	List<ApiStatus> k8sPodsList= regionStatusService.getTotalPodsStatus(startDate, endDate, environment, componentIdsStrg,isParent);
    	return new ResponseEntity<GDMResponse>(new GDMResponse(k8sPodsList), HttpStatus.OK);
    }
    
    /**
     * Get current number of pods
     * @param environment
     * @return
     */
    @ApiOperation(value="Get the Current Application's K8s Pods Status")
    @CrossOrigin
    @RequestMapping(value = "/getCurrentPods", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getCurrentPods(@RequestParam(value = "environment",required = false) String environment,
  			@RequestParam(value = "component",required = false) String componentIdsStrg,
  			@RequestParam(value = "isParentComponents",required = false) boolean isParentComponents) throws ParseException{
    	log.debug("********************* getCurrentPods ********************************");
    	long currPods= regionStatusService.getCurrentNumberOfPods(environment, componentIdsStrg, isParentComponents);
    	return new ResponseEntity<GDMResponse>(new GDMResponse(currPods), HttpStatus.OK);
    }
}
