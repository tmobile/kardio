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

/**
 * EnvironmentMessages store specific environment related messages VO for Edit Environment data
 * 
 */
public class EnvironmentMessages {
    private String generalMessage;
    private String appMessage;
    private String infraMessage;
    private String counterMessage;

    /**
     * @return the generalMessage
     */
    public String getGeneralMessage() {
        return generalMessage;
    }

    /**
     * @param generalMessage
     *            the generalMessage to set
     */
    public void setGeneralMessage(String generalMessage) {
        this.generalMessage = generalMessage;
    }

    /**
     * @return the appMessage
     */
    public String getAppMessage() {
        return appMessage;
    }

    /**
     * @param appMessage
     *            the appMessage to set
     */
    public void setAppMessage(String appMessage) {
        this.appMessage = appMessage;
    }

    /**
     * @return the infraMessage
     */
    public String getInfraMessage() {
        return infraMessage;
    }

    /**
     * @param infraMessage
     *            the infraMessage to set
     */
    public void setInfraMessage(String infraMessage) {
        this.infraMessage = infraMessage;
    }

	public String getCounterMessage() {
		return counterMessage;
	}

	public void setCounterMessage(String counterMessage) {
		this.counterMessage = counterMessage;
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnvironmentMessages other = (EnvironmentMessages) obj;
		if (generalMessage == null) {
			if (other.generalMessage != null)
				return false;
		} else if (!generalMessage.equals(other.generalMessage))
			return false;
		return true;
	}

}
