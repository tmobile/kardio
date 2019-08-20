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
package com.tmobile.kardio.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
