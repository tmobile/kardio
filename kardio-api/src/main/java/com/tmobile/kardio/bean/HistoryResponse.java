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
package com.tmobile.kardio.bean;

import java.util.List;

/**
 * HistoryResponse contains the history respons coming from the history page Response for History page
 * 
 */
public class HistoryResponse {
    private List<ComponentHistory> appHistory;
    private List<ComponentHistory> apiHistory;
    private List<ComponentHistory> infraHistory;

    /**
     * @return the appHistory
     */
    public List<ComponentHistory> getAppHistory() {
        return appHistory;
    }

    /**
     * @param appHistory
     *            the appHistory to set
     */
    public void setAppHistory(List<ComponentHistory> appHistory) {
        this.appHistory = appHistory;
    }

    /**
     * @return the apiHistory
     */
    public List<ComponentHistory> getApiHistory() {
        return apiHistory;
    }

    /**
     * @param apiHistory
     *            the apiHistory to set
     */
    public void setApiHistory(List<ComponentHistory> apiHistory) {
        this.apiHistory = apiHistory;
    }

    /**
     * @return the infraHistory
     */
    public List<ComponentHistory> getInfraHistory() {
        return infraHistory;
    }

    /**
     * @param infraHistory
     *            the infraHistory to set
     */
    public void setInfraHistory(List<ComponentHistory> infraHistory) {
        this.infraHistory = infraHistory;
    }
}
