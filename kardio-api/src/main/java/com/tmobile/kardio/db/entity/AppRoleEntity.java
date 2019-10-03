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

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class to for app_role table
 */
@Entity
@Table(name = "app_role")
public class AppRoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "app_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int appRoleId;

    @Column(name = "app_role_name")
    private String appRoleName;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private ComponentEntity component;

    /**
     * Get appRoleId
     * 
     * @return
     */
    public int getAppRoleId() {
        return appRoleId;
    }

    /**
     * Set appRoleId
     * 
     * @param appRoleId
     */
    public void setAppRoleId(int appRoleId) {
        this.appRoleId = appRoleId;
    }

    /**
     * Get appRoleName
     * 
     * @return
     */
    public String getAppRoleName() {
        return appRoleName;
    }

    /**
     * Set appRoleId
     * 
     * @param appRoleId
     */
    public void setAppRoleName(String appRoleName) {
        this.appRoleName = appRoleName;
    }

    /**
     * Get component
     * 
     * @return
     */
    public ComponentEntity getComponent() {
        return component;
    }

    /**
     * Set appRoleId
     * 
     * @param appRoleId
     */
    public void setComponent(ComponentEntity component) {
        this.component = component;
    }

}
