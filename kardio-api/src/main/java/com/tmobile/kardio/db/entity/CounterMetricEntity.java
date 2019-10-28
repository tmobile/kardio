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
 * Entity class to for counter_metric table Entity class for counter_metric
 */
@Entity
@Table(name = "counter_metric")
public class CounterMetricEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "counter_metric_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int metricId;

    @Column(name = "env_counter_id", nullable = false, insertable = false, updatable = false)
    private int envCounterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "env_counter_id", nullable = false)
    private EnvCounterEntity envCounter;

    @Column(name = "metric_val")
    private float metricVal;

    @Column(name = "metric_date")
    private Date metricDate;

    /**
     * @return the metricId
     */
    public int getMetricId() {
        return metricId;
    }

    /**
     * @param metricId
     *            the metricId to set
     */
    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    /**
     * @return the envCounterId
     */
    public int getEnvCounterId() {
        return envCounterId;
    }

    /**
     * @param envCounterId
     *            the envCounterId to set
     */
    public void setEnvCounterId(int envCounterId) {
        this.envCounterId = envCounterId;
    }

    /**
     * @return the envCounter
     */
    public EnvCounterEntity getEnvCounter() {
        return envCounter;
    }

    /**
     * @param envCounter
     *            the envCounter to set
     */
    public void setEnvCounter(EnvCounterEntity envCounter) {
        this.envCounter = envCounter;
    }

    /**
     * @return the metricVal
     */
    public float getMetricVal() {
        return metricVal;
    }

    /**
     * @param metricVal
     *            the metricVal to set
     */
    public void setMetricVal(float metricVal) {
        this.metricVal = metricVal;
    }

    /**
     * @return the metricDate
     */
    public Date getMetricDate() {
        return metricDate;
    }

    /**
     * @param metricDate
     *            the metricDate to set
     */
    public void setMetricDate(Date metricDate) {
        this.metricDate = metricDate;
    }

}
