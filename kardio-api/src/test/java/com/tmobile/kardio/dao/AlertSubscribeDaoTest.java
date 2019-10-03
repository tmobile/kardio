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

import com.tmobile.kardio.TestDaoService;
import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.db.entity.AlertSubscriptionEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AlertSubscribeDaoTest extends AbstractDaoTest {
    @Autowired
    private AlertSubscribeDao alertSubscribeDao;

    @Autowired
    private TestDaoService daoService;

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateAlertSubscribtion() {
        String envName = "alertsubenv";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);

        alertSubscribeDao.saveSubscription(sub, 0, 1, false);
    }

    private Subscription getSubscription(String envName) {
        Subscription sub = new Subscription();
        sub.setAlertSubscriptionId(1);
        sub.setComponentId(TestDaoService.compID);
        sub.setComponentName("component_1");
        sub.setEnvironmentId(TestDaoService.getLastCreatedEnvironmentID());
        sub.setEnvironmentName("abc");
        sub.setAuthToken("auth_token_1");
        sub.setSubsciptionType("sub_type_1");
        sub.setSubsciptionVal("sub_val_1");
        return sub;
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreateAlertSubscribtion_Global() {
        String envName = "alertsubglobal";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);

        alertSubscribeDao.saveSubscription(sub, 0, 1, true);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testCreateAlertSubscribtion_NullSubscription() {
        alertSubscribeDao.saveSubscription(null, 0, 1, true);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testCreateAlertSubscribtion_CompIDZero() {
        String envName = "alertsubzerocompid";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        sub.setComponentId(0);

        alertSubscribeDao.saveSubscription(sub, 0, 1, false);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetSubscriptionToken() {
        String envName = "getsubtoken";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        alertSubscribeDao.saveSubscription(sub, 0, 1, false);

        String result = alertSubscribeDao.getSubscriptionToken("sub_val_1", TestDaoService.compID, TestDaoService.getLastCreatedEnvironmentID());
        Assert.assertEquals("Token does not match", "auth_token_1", result);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testConfirmSubscription() {
        String envName = "confirmsub";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        sub.setAuthToken("auth_token_2");
        alertSubscribeDao.saveSubscription(sub, 0, 1, false);

        alertSubscribeDao.confirmSubscription("auth_token_2");
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testConfirmSubscription_AuthTokenNull() {
        alertSubscribeDao.confirmSubscription(null);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetSubscribedEmailIdList() {
        String envName = "subemail";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        alertSubscribeDao.saveSubscription(sub, 0, 1, false);

        List<AlertSubscriptionEntity> result = alertSubscribeDao.getSubscribedEmailIdList(TestDaoService.compID, TestDaoService.getLastCreatedEnvironmentID());
        Assert.assertEquals("Size does not match", 1, result.size());
        AlertSubscriptionEntity actual = result.get(0);

        Assert.assertEquals("Component ID does not match", TestDaoService.compID, actual.getComponent().getComponentId());
        Assert.assertEquals("Environment ID does not match", TestDaoService.getLastCreatedEnvironmentID(), actual.getEnvironment().getEnvironmentId());
        Assert.assertEquals("Validation level does not match", 1, actual.getValidationLevel().intValue());
        Assert.assertEquals("Subscription type does not match", 0, actual.getSubscriptionType().intValue());
        Assert.assertEquals("Subscription value does not match", sub.getSubsciptionVal(), actual.getSubscriptionVal());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCheckForDuplicates() {
        String envName = "dupcheck";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        alertSubscribeDao.saveSubscription(sub, 0, 1, false);

        boolean actual = alertSubscribeDao.checkforDuplicates(TestDaoService.compID, TestDaoService.getLastCreatedEnvironmentID(), sub.getSubsciptionVal());

        Assert.assertTrue("No duplicate entry", actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testIsSubscriptionAvailable() {
        String envName = "subavail";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        alertSubscribeDao.saveSubscription(sub, 0, 1, true);

        boolean actual = alertSubscribeDao.isSubscribtionAvailable(sub, sub.getGlobalSubscriptionTypeId());

        Assert.assertTrue("Subscription is not available", actual);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testGetAllGlobalSubscriptions() {
        String envName = "subavail";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        alertSubscribeDao.saveSubscription(sub, 0, 1, true);

        List<AlertSubscriptionEntity> actual = alertSubscribeDao.getAllGlobalSubscriptions();

        Assert.assertTrue("Global subscription not present", actual.size() > 0);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteGlobalSubscriptions() {
        String envName = "deleteglobal";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        alertSubscribeDao.saveSubscription(sub, 0, 1, true);
        alertSubscribeDao.deleteGlobalSubscription(1);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testDeleteGlobalSubscriptions_IDZero() {
        alertSubscribeDao.deleteGlobalSubscription(0);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDeleteSubscription() {
        String envName = "deletesub";
        String authToken = "delete_auth_token";
        daoService.createEnvironment(envName, 1);
        daoService.createComponentType();
        daoService.createComponent();

        Subscription sub = getSubscription(envName);
        sub.setAuthToken(authToken);
        alertSubscribeDao.saveSubscription(sub, 0, 1, false);
        alertSubscribeDao.deleteSububscription(authToken);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    @Transactional
    @Rollback(true)
    public void testDeleteSubscriptions_AuthTokenNull() {
        alertSubscribeDao.deleteSububscription(null);
    }

}
