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
 * Entity class to for component_message table
 */
@Entity
@Table(name = "component_message")
public class ComponentMessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "component_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int componentMessageId;

    @Column(name = "message")
    private String message;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private ComponentEntity component;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @ManyToOne
    @JoinColumn(name = "environment_id")
    private EnvironmentEntity environment;

    @Column(name = "message_date")
    private Date messageDate;

    /**
     * @return the componentMessageId
     */
    public int getComponentMessageId() {
        return componentMessageId;
    }

    /**
     * @param componentMessageId
     *            the componentMessageId to set
     */
    public void setComponentMessageId(int componentMessageId) {
        this.componentMessageId = componentMessageId;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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
     * @return the component
     */
    public ComponentEntity getComponent() {
        return component;
    }

    /**
     * @param component
     *            the component to set
     */
    public void setComponent(ComponentEntity component) {
        this.component = component;
    }

    /**
     * @return the region
     */
    public RegionEntity getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(RegionEntity region) {
        this.region = region;
    }

    /**
     * @return the environment
     */
    public EnvironmentEntity getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(EnvironmentEntity environment) {
        this.environment = environment;
    }

    /**
     * @return the messageDate
     */
    public Date getMessageDate() {
        return messageDate;
    }

    /**
     * @param messageDate
     *            the messageDate to set
     */
    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
