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
package com.tmobile.kardio.surveiller.db.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class for Counter
 */
@Entity
@Table(name="counter")
public class CounterEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="counter_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int counterId;
	
	@Column(name="counter_name")
	private String counterName;
	
	@Column(name="counter_desc")
	private String counterDesc;
	
	@Column(name="position")
	private int position;
	
	@Column(name="del_ind")
	private int delInd;

	@OneToMany(mappedBy = "counter", fetch = FetchType.LAZY)
	private Set<EnvCounterEntity> envCounter;
	
	/**
	 * @return the envCounter
	 */
	public Set<EnvCounterEntity> getEnvCounter() {
		return envCounter;
	}

	/**
	 * @param envCounter the envCounter to set
	 */
	public void setEnvCounter(Set<EnvCounterEntity> envCounter) {
		this.envCounter = envCounter;
	}

	/**
	 * @return the counterId
	 */
	public int getCounterId() {
		return counterId;
	}

	/**
	 * @param counterId the counterId to set
	 */
	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}

	/**
	 * @return the counterName
	 */
	public String getCounterName() {
		return counterName;
	}

	/**
	 * @param counterName the counterName to set
	 */
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	/**
	 * @return the counterDesc
	 */
	public String getCounterDesc() {
		return counterDesc;
	}

	/**
	 * @param counterDesc the counterDesc to set
	 */
	public void setCounterDesc(String counterDesc) {
		this.counterDesc = counterDesc;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the delInd
	 */
	public int getDelInd() {
		return delInd;
	}

	/**
	 * @param delInd the delInd to set
	 */
	public void setDelInd(int delInd) {
		this.delInd = delInd;
	}

	
}
