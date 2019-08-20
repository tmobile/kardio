/*******************************************************************************
 * Copyright 2019 T-Mobile, Inc. or its affiliates. All Rights Reserved.
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
 ******************************************************************************/
package com.tmobile.kardio.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmobile.kardio.bean.AppFullName;
import com.tmobile.kardio.bean.AppSession;
import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.CounterDetails;
import com.tmobile.kardio.bean.Counters;
import com.tmobile.kardio.bean.EnvCounters;
import com.tmobile.kardio.bean.Environment;
import com.tmobile.kardio.bean.GDMResponse;
import com.tmobile.kardio.bean.HealthCheckVO;
import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.constants.Constants;
import com.tmobile.kardio.dao.AppSessionDao;
import com.tmobile.kardio.exceptions.AppSessionExpiredException;
import com.tmobile.kardio.service.AdminService;
import com.tmobile.kardio.service.RegionStatusServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller which takes care of all the admin operations in SHD Main controller of Application
 */
@RestController
@Api(description="GDM Admin Controller handles all the admin page API requests")
public class GDMAdminController {

    private static final String SESSION_EXPIRED_MESSAGE = "Your current session has expired. Please login again";

	private static Logger log = LoggerFactory.getLogger(GDMAdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppSessionDao appSessionDao;

    /**
     * Add Component.
     * 
     * @param component
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Add Component")
    @CrossOrigin
    @RequestMapping(value = "/addComponent", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> addComponent(@RequestBody Component component,
            @RequestParam(value = "authToken", required = false) String authToken) throws InstantiationException {
        log.info("********************* ADMIN : addComponent ******************************** " + component.getComponentName() + " : "
                + component.getParentComponentId());
        validateTokenIsOfAdmin(authToken);
        String auditLog="Added Component: "+component.getComponentName();
        adminService.saveComponent(component);
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Added the Component"), HttpStatus.OK);
    }

    /**
     * Gets Status of Components.
     * 
     * @return
     */
    @ApiOperation(value="Get All Component")
    @CrossOrigin
    @RequestMapping(value = "/getAllComponent", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getAllComponent(@RequestParam(value = "authToken", required = false) String authToken) {
        log.debug("********************* ADMIN : getAllComponent ******************************** ");
        validateTokenIsOfAdmin(authToken);
        List<Component> compList = adminService.getAllComponent();
        return new ResponseEntity<GDMResponse>(new GDMResponse(compList), HttpStatus.OK);
    }
    
    /**
     * Function to edit the component feature
     * 
     * @param component
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Edit Component")
    @CrossOrigin
    @RequestMapping(value = "/editComponent", method = { RequestMethod.PUT })
    public ResponseEntity<GDMResponse> editComponent(@RequestBody Component component,
            @RequestParam(value = "authToken", required = false) String authToken) throws InstantiationException {
        log.info("********************* ADMIN : editComponent ******************************** " + component.getComponentId());
        validateTokenIsOfAdmin(authToken);
        adminService.editComponent(component);
        String auditLog = "Modified Component: "+component.getComponentName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Updated the Component"), HttpStatus.OK);
    }

    /**
     * Function to set the del_ind of a component
     * 
     * @param component
     * @return
     */
    @ApiOperation(value="Delete Component")
    @CrossOrigin
    @RequestMapping(value = "/deleteComponent", method = { RequestMethod.PUT })
    public ResponseEntity<GDMResponse> deleteComponent(@RequestBody Component component,
            @RequestParam(value = "authToken", required = false) String authToken) {
        log.info("********************* ADMIN : deleteComponent ******************************** " + component.getComponentId());
        validateTokenIsOfAdmin(authToken);
        adminService.deleteComponent(component);
         String auditLog = "Deleted Component: "+component.getComponentName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Deleted the Component"), HttpStatus.OK);
    }

    /**
     * Returns all the health check details.
     * 
     * @return
     */
    @ApiOperation(value="Get All Health Checks")
    @CrossOrigin
    @RequestMapping(value = "/getAllHealthChecks", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getAllHealthChecks(@RequestParam(value = "authToken", required = false) String authToken) {
        log.info("********************* ADMIN : getAllHealthChecks ********************************");
        validateTokenIsOfAdmin(authToken);
        GDMResponse response = new GDMResponse(adminService.getAllHealthChecks());
        return new ResponseEntity<GDMResponse>(response, HttpStatus.OK);
    }

    /**
     * Returs the details of all the HealthCheckTypes.
     * 
     * @param authToken
     * @return
     */
    @ApiOperation(value="Get Health Check Types")
    @CrossOrigin
    @RequestMapping(value = "/getHealthCheckTypes", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getHealthCheckTypes(@RequestParam(value = "authToken", required = false) String authToken) {
        log.info("********************* ADMIN : getHealthCheckTypes ********************************");
        validateTokenIsOfAdmin(authToken);
        GDMResponse response = new GDMResponse(adminService.getAllHealthCheckTypes());
        return new ResponseEntity<GDMResponse>(response, HttpStatus.OK);
    }

    /**
     * Marks the given health check as soft deleted.
     * 
     * @return
     */
    @ApiOperation(value="Delete Health Check")
    @CrossOrigin
    @RequestMapping(value = "/deleteHealthCheck", method = RequestMethod.PUT)
    public ResponseEntity<GDMResponse> deleteHealthCheck(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestBody HealthCheckVO healthCheck) {
        log.info("********************* ADMIN : deleteHealthCheck ******************************** " + healthCheck.getHealthCheckId());
        validateTokenIsOfAdmin(authToken);
        adminService.deleteHealthCheck(healthCheck.getHealthCheckId());
        String auditLog = "Deleted Health Check Id: "+healthCheck.getHealthCheckId();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Deleted the Health Check configuration"), HttpStatus.OK);
    }

    /**
     * Updates/Adds the health check with given details. HealthCheckId = 0 for add.
     * 
     * @return
     */
    @ApiOperation(value="Edit Health Check")
    @CrossOrigin
    @RequestMapping(value = "/editHealthCheck", method = RequestMethod.PUT)
    public ResponseEntity<GDMResponse> editHealthCheck(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestBody HealthCheckVO healthCheck) {
        log.info("********************* ADMIN : editHealthCheck ******************************** " + healthCheck.getHealthCheckId());
        validateTokenIsOfAdmin(authToken);
        adminService.editHealthCheck(healthCheck);
        String auditLog = "Modified Health Check Id: "+healthCheck.getComponentName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Successfully Updated/Added the Health Check configuration"), HttpStatus.OK);
    }

    /**
     * Controller to save the subscription details when the subscribe button is clicked in the UI
     * 
     * @param subscription
     * @return
     */
    @ApiOperation(value="Subscribe Global Alerts")
    @CrossOrigin
    @RequestMapping(value = "/subscribeGlobalAlerts", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> subscribeGlobalAlerts(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestBody Subscription subscription) {
        // Logic to save subscription into the DB
        validateTokenIsOfAdmin(authToken);
        adminService.subscribeGlobalAlert(subscription);
        String auditLog = "Subscribed Global Alerts";
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Subscription Successfull. Regular alerts will be sent to the subscribed channel"
                + subscription.getSubsciptionVal()), HttpStatus.OK);
    }

    /**
     * Function to get all global subscriptions
     * 
     * @return
     */
    @ApiOperation(value="Get Global Subscription")
    @CrossOrigin
    @RequestMapping(value = "/getGlobalSubscription", method = { RequestMethod.GET })
    public ResponseEntity<GDMResponse> getGlobalSubscription(@RequestParam(value = "authToken", required = false) String authToken) {
        validateTokenIsOfAdmin(authToken);
        List<Subscription> subscriptionList = adminService.getAllGlobalSubscriptions();
        return new ResponseEntity<GDMResponse>(new GDMResponse(subscriptionList), HttpStatus.OK);
    }
    @ApiOperation(value="Delete Global Subscription")
    @CrossOrigin
    @RequestMapping(value = "/deleteGlobalSubscription", method = { RequestMethod.DELETE })
    public ResponseEntity<GDMResponse> deleteGlobalSubscription(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestParam(value = "alertSubscriptionId", required = true) int alertSubscriptionId) {
        log.debug("********************* ADMIN : deleteGlobalSubscription ******************************** " + alertSubscriptionId);
        validateTokenIsOfAdmin(authToken);
        adminService.deleteGlobalSubscription(alertSubscriptionId);
        String auditLog = "Deleted Global Subscription for Id: "+alertSubscriptionId;
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Successfully deleted subscription with Id: " + alertSubscriptionId), HttpStatus.OK);
    }

    /**
     * Get all Counters from DB
     * 
     * @param authToken
     * @return
     */
    @ApiOperation(value="Get All Counters")
    @CrossOrigin
    @RequestMapping(value = "/getAllCounters", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getAllCounters(@RequestParam(value = "authToken", required = false) String authToken) {
        log.debug("********************* ADMIN : getAllCounters ******************************** ");
        validateTokenIsOfAdmin(authToken);
        CounterDetails counterList = adminService.getAllCounters();
        return new ResponseEntity<GDMResponse>(new GDMResponse(counterList), HttpStatus.OK);
    }

    /**
     * Update/Edit the Counters information.
     * 
     * @return
     */
    @ApiOperation(value="Edit Counters")
    @CrossOrigin
    @RequestMapping(value = "/editCounters", method = RequestMethod.PUT)
    public ResponseEntity<GDMResponse> editCounters(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestBody List<Counters> counterList) {
        log.info("********************* ADMIN : editCounters ******************************** " + counterList.size());
        validateTokenIsOfAdmin(authToken);
        adminService.editCounter(counterList);
        String auditLog = "Updated Counter Configuration ";
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Updated the Counters"), HttpStatus.OK);
    }

    /**
     * Update/Edit the Counters information.
     * 
     * @return
     */
    @ApiOperation(value="Edit Environment Counter Details")
    @CrossOrigin
    @RequestMapping(value = "/editEnvCounterDetails", method = RequestMethod.PUT)
    public ResponseEntity<GDMResponse> editEnvCounterDetails(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestBody EnvCounters envCounters) {
        log.info("********************* ADMIN : editEnvCounterDetails ******************************** ");
        validateTokenIsOfAdmin(authToken);
        adminService.editEnvCounterDetails(envCounters);
        String auditLog = "Updated the EnvCounter parameters for Counter: "+envCounters.getCounterName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Updated the Counters parameters"), HttpStatus.OK);
    }

    /**
     * Lock the environment in DB based on the user input
     * 
     * @return
     */
    @ApiOperation(value="Environment Lock")
    @CrossOrigin
    @RequestMapping(value = "/environmentLock", method = RequestMethod.PUT)
    public ResponseEntity<GDMResponse> environmentLock(@RequestParam(value = "authToken", required = false) String authToken,
            @RequestBody Environment environment) {
        log.info("********************* ADMIN : environmentLock ******************************** " + environment.getEnvironmentName());
        validateTokenIsOfAdmin(authToken);
        adminService.doEnvironmentLock(environment);
        String auditLog = "Environment Lock Updated for Environment: "+environment.getEnvironmentName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Environment Lock Updated"), HttpStatus.OK);
    }

    /**
     * Function to get all environment
     * 
     * @param authToken
     * @return
     */
    @ApiOperation(value="Get Environment Lock")
    @CrossOrigin
    @RequestMapping(value = "/getEnvironmentLock", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getEnvironmentLock(@RequestParam("authToken") String authToken) {
        log.info("********************* getEnvironmentLock ********************************");
        validateTokenIsOfAdmin(authToken);
        List<Environment> environmentList = adminService.getEnvironmentLock();
        return new ResponseEntity<GDMResponse>(new GDMResponse(environmentList), HttpStatus.OK);
    }
    
    /**
     * Add Environment.
     * 
     * @param environment
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Add Environment")
    @CrossOrigin
    @RequestMapping(value = "/addEnvironment", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> addEnvironment(@RequestBody Environment environment,
            @RequestParam(value = "authToken", required = false) String authToken) throws InstantiationException {
        log.info("********************* ADMIN : addEnvironment ******************************** " + environment.getEnvironmentName() + " : "
                + environment.getEnvironmentDesc());
        validateTokenIsOfAdmin(authToken);
        adminService.addEnvironment(environment);
        String auditLog = "Added Environment: "+environment.getEnvironmentName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Added the Component"), HttpStatus.OK);
    }

    /**
     * Make sure that the token belongs to an admin user.
     * 
     * @param authToken
     */
    private void validateTokenIsOfAdmin(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            throw new AppSessionExpiredException(SESSION_EXPIRED_MESSAGE);
        }
        AppSession appSession = adminService.getAppSession(authToken);
        if (appSession.getAuthToken() == null
                || RegionStatusServiceImpl.getDateDiff(appSession.getSessionStartTime(), new Date()) > Constants.SESSION_EXPIRATION_TIME_MINUTES) {
            if (appSession.getAuthToken() != null) {
                appSessionDao.deleteAppSession(authToken);
            }
            throw new AppSessionExpiredException(SESSION_EXPIRED_MESSAGE);
        }
        if (!appSession.isAdmin()) {
            appSessionDao.deleteAppSession(authToken);
            throw new AppSessionExpiredException(SESSION_EXPIRED_MESSAGE);
        }
        log.info("Valid Admin User " + appSession.getUserId());
    }

    /**
     * Add AppFullName.
     * 
     * @param AppFullName
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Add Application Full Name")
    @CrossOrigin
    @RequestMapping(value = "/addAppFullName", method = { RequestMethod.POST })
    public ResponseEntity<GDMResponse> addFullName(@RequestBody AppFullName appFullName,
            @RequestParam(value = "authToken", required = false) String authToken) throws InstantiationException {
        log.info("********************* ADMIN : addFullName ******************************** " + appFullName.getComponentFullName() + " : "
                + appFullName.getComponentId());
        validateTokenIsOfAdmin(authToken);
        adminService.saveAppFullName(appFullName);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Added Full Name for the Component id: " + appFullName.getComponentId() + "-"
                + appFullName.getComponentFullName()), HttpStatus.OK);
    }

    /**
     * Gets all Apps Full Name.
     * 
     * @return
     */
    @ApiOperation(value="Get Application Full Name")
    @CrossOrigin
    @RequestMapping(value = "/getAppFullName", method = RequestMethod.GET)
    public ResponseEntity<GDMResponse> getAppFullName(@RequestParam(value = "authToken", required = false) String authToken) {
        log.debug("********************* ADMIN : getAppFullName ******************************** ");
        validateTokenIsOfAdmin(authToken);
        List<AppFullName> appFullList = adminService.getAppFullName();
        return new ResponseEntity<GDMResponse>(new GDMResponse(appFullList), HttpStatus.OK);
    }

    /**
     * Function to edit the component FullName
     * 
     * @param component ID
     * @return
     * @throws InstantiationException
     */
    @ApiOperation(value="Edit Application Full Name")
    @CrossOrigin
    @RequestMapping(value = "/editAppFullName", method = { RequestMethod.PUT })
    public ResponseEntity<GDMResponse> editAppFullName(@RequestBody AppFullName appFullName,
            @RequestParam(value = "authToken", required = false) String authToken) throws InstantiationException {
        log.info("********************* ADMIN : editAppFullName ******************************** " + appFullName.getComponentId());
        validateTokenIsOfAdmin(authToken);
        if(appFullName.getAppId() == 0) {
        	adminService.saveAppFullName(appFullName);
        } else if(appFullName.getComponentFullName() == null || appFullName.getComponentFullName().equals("")) {
        	adminService.deleteAppFullName(appFullName);
        } else {
        	adminService.editAppFullName(appFullName);
        }
        String auditLog = "Edit Application Full Name: "+appFullName.getComponentName();
        adminService.addAuditLog(authToken,auditLog);
        return new ResponseEntity<GDMResponse>(new GDMResponse("Updated the Component"), HttpStatus.OK);
    }
}
