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
package com.tmobile.kardio.bean;

import java.util.Date;

/**
 * AppSession bean to store the details of an application session
 * 
 */
public class AppSession {

    private int appSessionId;
    private String authToken;
    private String userId;
    private String userName;
    private Date sessionStartTime;
    private String permission;
    private boolean isAdmin;

    /**
     * @return the appSessionId
     */
    public int getAppSessionId() {
        return appSessionId;
    }

    /**
     * @param appSessionId
     *            the appSessionId to set
     */
    public void setAppSessionId(int appSessionId) {
        this.appSessionId = appSessionId;
    }

    /**
     * @return the authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken
     *            the authToken to set
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the sessionStartTime
     */
    public Date getSessionStartTime() {
        return sessionStartTime;
    }

    /**
     * @param sessionStartTime
     *            the sessionStartTime to set
     */
    public void setSessionStartTime(Date sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    /**
     * @return the permission
     */
    public final String getPermission() {
        return permission;
    }

    /**
     * @param permission
     *            the permission to set
     */
    public final void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * @return the isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin
     *            the isAdmin to set
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}
