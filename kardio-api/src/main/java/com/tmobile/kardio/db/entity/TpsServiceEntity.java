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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity class to for tps_service table
 */
@Entity
@Table(name = "tps_service")
public class TpsServiceEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	    @Id
	    @Column(name = "tps_service_id")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int tpsServiceId;
	    
	    @ManyToOne
	    @JoinColumn(name = "component_id")
	    private ComponentEntity component;
        
	    @ManyToOne
	    @JoinColumn(name= "environment_id")
	    private EnvironmentEntity environment;
	    
	    @Column(name= "tps_value")
	    private double tpsValue;
	    
	    @Column(name= "latency_value")
	    private double latencyValue;
	  

		/**
		 * @return the latencyValue
		 */
		public double getLatencyValue() {
			return latencyValue;
		}

		/**
		 * @param latencyValue the latencyValue to set
		 */
		public void setLatencyValue(double latencyValue) {
			this.latencyValue = latencyValue;
		}

	
		/**
		 * @return the environment
		 */
		public EnvironmentEntity getEnvironment() {
			return environment;
		}

		/**
		 * @param environment the environment to set
		 */
		public void setEnvironment(EnvironmentEntity environment) {
			this.environment = environment;
		}

		/**
		 * @return the tpsServiceId
		 */
		public int getTpsServiceId() {
			return tpsServiceId;
		}

		/**
		 * @param tpsServiceId the tpsServiceId to set
		 */
		public void setTpsServiceId(int tpsServiceId) {
			this.tpsServiceId = tpsServiceId;
		}

		/**
		 * @return the component
		 */
		public ComponentEntity getComponent() {
			return component;
		}

		/**
		 * @param component the component to set
		 */
		public void setComponent(ComponentEntity component) {
			this.component = component;
		}

		/**
		 * @return the tpsValue
		 */
		public double getTpsValue() {
			return tpsValue;
		}

		/**
		 * @param tpsValue the tpsValue to set
		 */
		public void setTpsValue(double tpsValue) {
			this.tpsValue = tpsValue;
		}

		
 	    	    
}
