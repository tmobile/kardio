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

import org.junit.Assert;
import org.junit.Test;

public class EqualsHashCodeTest {

    private void checkNotEqual(Object obj, Object obj1) {
        Assert.assertFalse("Target object null", obj.equals(null));
        Assert.assertFalse("Different class", obj.equals(""));
        Assert.assertFalse("Different objects", obj.equals(obj1));
        Assert.assertNotEquals("Hashcode not equal", obj.hashCode(), obj1.hashCode());
    }

    private void checkEqual(Object obj, Object obj2) {
        Assert.assertTrue("Same object", obj.equals(obj));
        Assert.assertTrue("Equal objects", obj.equals(obj2));
        Assert.assertEquals("Hashcode equal", obj.hashCode(), obj2.hashCode());
    }

    @Test
    public void testAppFullName() throws Exception {
        AppFullName a1 = new AppFullName();
        a1.setComponentName("c1");
        AppFullName a2 = new AppFullName();
        a2.setComponentName("c1");
        AppFullName a3 = new AppFullName();
        a3.setComponentName("c3");

        checkEqual(a1, a2);
        checkNotEqual(a1, a3);

        AppFullName az = new AppFullName();
        az.hashCode();
        Assert.assertFalse(az.equals(a1));
    }

    @Test
    public void testComponent() throws Exception {
        Component c1 = new Component();
        c1.setAppFullName("a1");
        c1.setChildComponentName("c1");
        c1.setComponentId(1);

        Component c2 = new Component();
        c2.setAppFullName("a1");
        c2.setChildComponentName("c1");
        c2.setComponentId(1);

        Component c3 = new Component();
        c3.setAppFullName("a3");
        c3.setChildComponentName("c3");
        c3.setComponentId(3);

        checkEqual(c1, c2);
        checkNotEqual(c1, c3);

        Component cz = new Component();
        cz.hashCode();
        Assert.assertFalse(cz.equals(c1));

        cz.setAppFullName("az");
        Assert.assertFalse(cz.equals(c1));

        cz.setAppFullName("a1");
        Assert.assertFalse(cz.equals(c1));

        cz.setAppFullName("a1");
        cz.setChildComponentName("cz");
        Assert.assertFalse(cz.equals(c1));

        cz.setAppFullName("a1");
        cz.setChildComponentName("c1");
        cz.setComponentId(100);
        Assert.assertFalse(cz.equals(c1));
    }

    @Test
    public void testComponentMessages() throws Exception {
        ComponentMessages c1 = new ComponentMessages();
        c1.setMessage("m1");

        ComponentMessages c2 = new ComponentMessages();
        c2.setMessage("m1");

        ComponentMessages c3 = new ComponentMessages();
        c3.setMessage("m3");

        checkEqual(c1, c2);
        checkNotEqual(c1, c3);

        ComponentMessages cz = new ComponentMessages();
        cz.hashCode();
        Assert.assertFalse(cz.equals(c1));
    }

    @Test
    public void testCounters() throws Exception {
        Counters c1 = new Counters();
        c1.setCounterName("m1");

        Counters c2 = new Counters();
        c2.setCounterName("m1");

        Counters c3 = new Counters();
        c3.setCounterName("m3");

        checkEqual(c1, c2);
        checkNotEqual(c1, c3);

        Counters cz = new Counters();
        cz.hashCode();
        Assert.assertFalse(cz.equals(c1));
    }

    @Test
    public void testEnvCounters() throws Exception {
        EnvCounters c1 = new EnvCounters();
        c1.setCounterName("m1");

        EnvCounters c2 = new EnvCounters();
        c2.setCounterName("m1");

        EnvCounters c3 = new EnvCounters();
        c3.setCounterName("m3");

        checkEqual(c1, c2);
        checkNotEqual(c1, c3);

        EnvCounters cz = new EnvCounters();
        cz.hashCode();
        Assert.assertFalse(cz.equals(c1));
    }

    @Test
    public void testEnvironment() throws Exception {
        Environment e1 = new Environment();
        e1.setEnvironmentName("m1");

        Environment e2 = new Environment();
        e2.setEnvironmentName("m1");

        Environment e3 = new Environment();
        e3.setEnvironmentName("m3");

        checkEqual(e1, e2);
        checkNotEqual(e1, e3);

        Environment ez = new Environment();
        ez.hashCode();
        Assert.assertFalse(ez.equals(e1));
    }

    @Test
    public void testEnvironmentMessages() throws Exception {
        EnvironmentMessages e1 = new EnvironmentMessages();
        e1.setGeneralMessage("m1");

        EnvironmentMessages e2 = new EnvironmentMessages();
        e2.setGeneralMessage("m1");

        EnvironmentMessages e3 = new EnvironmentMessages();
        e3.setGeneralMessage("m3");

        //checkEqual(e1, e2);
        checkNotEqual(e1, e3);

        EnvironmentMessages ez = new EnvironmentMessages();
        ez.hashCode();
        Assert.assertFalse(ez.equals(e1));
    }

    @Test
    public void testHealthCheckVO() throws Exception {
        HealthCheckVO h1 = new HealthCheckVO();
        h1.setHealthCheckId(1);

        HealthCheckVO h2 = new HealthCheckVO();
        h2.setHealthCheckId(1);

        HealthCheckVO h3 = new HealthCheckVO();
        h3.setHealthCheckId(3);

        checkEqual(h1, h2);
        checkNotEqual(h1, h3);

        HealthCheckVO hz = new HealthCheckVO();
        hz.hashCode();
        Assert.assertFalse(hz.equals(h1));
    }

    @Test
    public void testSubscription() throws Exception {
        Subscription s1 = new Subscription();
        s1.setComponentId(1);

        Subscription s2 = new Subscription();
        s2.setComponentId(1);

        Subscription s3 = new Subscription();
        s3.setComponentId(3);

        checkEqual(s1, s2);
        checkNotEqual(s1, s3);

        Subscription sz = new Subscription();
        sz.hashCode();
        Assert.assertFalse(sz.equals(s1));
    }
}
