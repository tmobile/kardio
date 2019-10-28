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
 * Entity class to for component_type table
 */
@Entity
@Table(name = "component_type")
public class ComponentTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "component_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int componentTypeId;

    @Column(name = "component_type_name")
    private String componentTypeName;

    @Column(name = "component_type_desc")
    private String componentTypeDesc;

    /**
     * Get the component type's id
     * 
     * @return componentTypeId
     */
    public int getComponentTypeId() {
        return componentTypeId;
    }

    /**
     * Set the component type's id
     * 
     * @param componentTypeId
     */
    public void setComponentTypeId(int componentTypeId) {
        this.componentTypeId = componentTypeId;
    }

    /**
     * Get the component type's name
     * 
     * @return componentTypeName
     */
    public String getComponentTypeName() {
        return componentTypeName;
    }

    /**
     * Set the component type's name
     * 
     * @param componentTypeName
     */
    public void setComponentTypeName(String componentTypeName) {
        this.componentTypeName = componentTypeName;
    }

    /**
     * Get the component type's description
     * 
     * @return componentTypeDesc
     */
    public String getComponentTypeDesc() {
        return componentTypeDesc;
    }

    /**
     * Set the component type's description
     * 
     * @param componentTypeDesc
     */
    public void setComponentTypeDesc(String componentTypeDesc) {
        this.componentTypeDesc = componentTypeDesc;
    }

}
