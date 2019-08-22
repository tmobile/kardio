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
package com.tmobile.kardio.bean;

import java.util.List;

/**
 * StatusResponse Response returned via rest web service for status queries for an environment Dashboard response main
 * VO
 * 
 */
public class StatusResponse {
    private EnvironmentMessages envMessages;
    private List<Component> apiComponents;
    private List<Component> appComponents;
    private List<Component> infraComponents;

    /**
     * @return the envMessages
     */
    public EnvironmentMessages getEnvMessages() {
        return envMessages;
    }

    /**
     * @param envMessages
     *            the envMessages to set
     */
    public void setEnvMessages(EnvironmentMessages envMessages) {
        this.envMessages = envMessages;
    }

    /**
     * @return the apiComponents
     */
    public List<Component> getApiComponents() {
        return apiComponents;
    }

    /**
     * @param apiComponents
     *            the apiComponents to set
     */
    public void setApiComponents(List<Component> apiComponents) {
        this.apiComponents = apiComponents;
    }

    /**
     * @return the appComponents
     */
    public List<Component> getAppComponents() {
        return appComponents;
    }

    /**
     * @param appComponents
     *            the appComponents to set
     */
    public void setAppComponents(List<Component> appComponents) {
        this.appComponents = appComponents;
    }

    /**
     * @return the infraComponents
     */
    public List<Component> getInfraComponents() {
        return infraComponents;
    }

    /**
     * @param infraComponents
     *            the infraComponents to set
     */
    public void setInfraComponents(List<Component> infraComponents) {
        this.infraComponents = infraComponents;
    }
}
