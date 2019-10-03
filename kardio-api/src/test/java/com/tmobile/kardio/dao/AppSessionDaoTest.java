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
package com.tmobile.kardio.dao;

import com.tmobile.kardio.bean.User;
import com.tmobile.kardio.db.entity.AppSessionEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


public class AppSessionDaoTest extends AbstractDaoTest {
    @Autowired
    private AppSessionDao appSessionDao;

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateAppSession() {
        User user = getUser("1");
        appSessionDao.saveAppSession(user);
    }

    private User getUser(String suffix) {
        User user = new User();
        user.setUserId("userid" + suffix);
        user.setUserName("username" + suffix);
        user.setPassword("password" + suffix);
        user.setAdmin(false);
        user.setAuthToken("auth_token" + suffix);
        user.setEmailId("email@email.com");
        user.setTimeOutMinute("5");
        Set<String> groups = new HashSet<String>();
        groups.add("group" + suffix);
        user.setUserGroups(groups);
        return user;
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testCreateAppSession_NullUser() {
        appSessionDao.saveAppSession(null);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAppSession() {
        User user = getUser("2");
        appSessionDao.saveAppSession(user);
        AppSessionEntity actual = appSessionDao.getAppSession(user.getAuthToken());

        Assert.assertEquals("ID does not match", user.getUserId(), actual.getUserId());
        Assert.assertEquals("Name does not match", user.getUserName(), actual.getUserName());
        Assert.assertEquals("Auth token does not match", user.getAuthToken(), actual.getAuthToken());
        Assert.assertEquals("Groups does not match", user.getUserGroups().toString(), actual.getPermission());
        Assert.assertEquals("Admin does not match", user.isAdmin(), actual.getIsAdmin());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAppSession_NonExisting() {
        AppSessionEntity actual = appSessionDao.getAppSession("NOT_EXISTING");
        Assert.assertNull(actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteAppSession() {
        User user = getUser("3");
        user.setUserGroups(null);
        appSessionDao.saveAppSession(user);
        appSessionDao.deleteAppSession(user.getAuthToken());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllAppRoles() {
        User user = getUser("4");
        appSessionDao.saveAppSession(user);
//    	TODO: Find how to create app roles.

    }
}
