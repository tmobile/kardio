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

/**
 * App_Look_Up table holds the full name of a component. This class is the Entity calls for the same.
 * 
 */
@Entity
@Table(name = "app_lookup")
public class AppLookUpEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "app_lookup_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applookupId;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "component_id")
    private ComponentEntity component;

    @Column(name = "component_full_name")
    private String componentFullName;

    /**
     * @return the applookupId
     */
    public int getApplookupId() {
        return applookupId;
    }

    /**
     * @param applookupId
     *            the applookupId to set
     */
    public void setApplookupId(int applookupId) {
        this.applookupId = applookupId;
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
     * @return the componentFullName
     */
    public String getComponentFullName() {
        return componentFullName;
    }

    /**
     * @param componentFullName
     *            the componentFullName to set
     */
    public void setComponentFullName(String componentFullName) {
        this.componentFullName = componentFullName;
    }

}
