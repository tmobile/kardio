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
package com.tmobile.kardio.surveiller.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="component")
public class ComponentEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="component_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int componentId;
	
	@Column(name="comp_name")
	private String componentName;
	
	@Column(name="comp_desc")
	private String componentDesc;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="parent_component_id", nullable = true)
	private ComponentEntity parentComponent;
	
	@ManyToOne
	@JoinColumn(name="component_type_id")
	private ComponentTypeEntity componentType;
	
	@Column(name="del_ind")
	private int delInd;
	
	@Column(name="platform")
	private String platform;
	
	@Column(name = "manual")
	private String manual;

	/**
	 * @return the manual
	 */
	public String getManual() {
		return manual;
	}

	/**
	 * @param manual the manual to set
	 */
	public void setManual(String manual) {
		this.manual = manual;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * Get the component Id
	 * @return componentId
	 */
	public int getComponentId() {
		return componentId;
	}

	/**
	 * Set the component Id
	 * @param componentId
	 */
	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	/**
	 * Get the component name
	 * @return componentName
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * Set the component name
	 * @param componentName
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	
	/**
	 * Get the component description
	 * @return componentDesc
	 */
	public String getComponentDesc() {
		return componentDesc;
	}
	
	/**
	 * Set component description
	 * @param componentDesc
	 */
	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

	/**
	 * Get the parent component
	 * @return parentComponent
	 */
	public ComponentEntity getParentComponent() {
		return parentComponent;
	}

	/**
	 * Set the parent component
	 * @param parentComponent
	 */
	public void setParentComponent(ComponentEntity parentComponent) {
		this.parentComponent = parentComponent;
	}

	/**
	 * Get the component type
	 * @return componentType
	 */
	public ComponentTypeEntity getComponentType() {
		return componentType;
	}

	/**
	 * Set the component type
	 * @param componentType
	 */
	public void setComponentType(ComponentTypeEntity componentType) {
		this.componentType = componentType;
	}

	/**
	 * Get the delete indicator
	 * @return delInd
	 */
	public int getDelInd() {
		return delInd;
	}

	/**
	 * Set the delete indicator
	 * @param delInd
	 */
	public void setDelInd(int delInd) {
		this.delInd = delInd;
	}
	
	
}
