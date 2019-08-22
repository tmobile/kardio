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
package com.tmobile.kardio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * Entry Point. Contains the main method for the spring boot application
 * 
 * Application Entry Point.
 * 
 */
@SpringBootApplication
public class ComponentStatusApplication {

    /**
     * Entry Point.
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ComponentStatusApplication.class);
		ApplicationPidFileWriter appPidWriter = new ApplicationPidFileWriter("kardio-api.pid");
        springApplication.addListeners(appPidWriter);
        springApplication.run(args);
    }
}
