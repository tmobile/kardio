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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmobile.kardio.bean.AppSession;
import com.tmobile.kardio.service.AdminService;

import java.util.Date;

import static org.mockito.Mockito.when;

public class TestUtils {
    public static String mockAppSession(AdminService adminService) {
        String authToken = "auth_token";
        AppSession session = new AppSession();
        session.setAppSessionId(1);
        session.setAuthToken(authToken);
        session.setUserId("admin_id");
        session.setUserName("admin");
        session.setAdmin(true);
        session.setSessionStartTime(new Date());
        when(adminService.getAppSession(authToken)).thenReturn(session);
        return authToken;
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
