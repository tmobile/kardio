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
package com.tmobile.kardio.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity class to for app_session table
 */
@Entity
@Table(name = "app_session")
public class AppSessionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "app_session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appSessionId;

    @Column(name = "auth_token")
    private String authToken;

    @Column(name = "session_start_time")
    private Date sessionStartTime;

    @Column(name = "userid")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "permission")
    private String permission;

    @Column(name = "is_admin")
    private boolean isAdmin;

    /**
     * Function to get the appSessionId
     * 
     * @return appSessionId
     */
    public int getAppSessionId() {
        return appSessionId;
    }

    /**
     * Function to set appSessionId
     * 
     * @param appSessionId
     */
    public void setAppSessionId(int appSessionId) {
        this.appSessionId = appSessionId;
    }

    /**
     * Function to get the authToken
     * 
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Function to set authToken
     * 
     * @param authToken
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Function to get the appSesssessionStartTimeionId
     * 
     * @return sessionStartTime
     */
    public Date getSessionStartTime() {
        return sessionStartTime;
    }

    /**
     * Function to set sessionStartTime
     * 
     * @param sessionStartTime
     */
    public void setSessionStartTime(Date sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    /**
     * Function to get the userId
     * 
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Function to set userId
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Function to get the userName
     * 
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Function to set userName
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Function to get the permission
     * 
     * @return permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Function to set permission
     * 
     * @param permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Function to get the isAdmin
     * 
     * @return isAdmin
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Function to set isAdmin
     * 
     * @param isAdmin
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}
