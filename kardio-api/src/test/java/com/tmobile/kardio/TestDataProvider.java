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

import com.tmobile.kardio.bean.Component;
import com.tmobile.kardio.bean.HealtCheckEnvironment;
import com.tmobile.kardio.bean.Region;
import com.tmobile.kardio.bean.Subscription;
import com.tmobile.kardio.constants.ComponentType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataProvider {
    public static Component getComponent() {
        Component comp = new Component();
        comp.setAppFullName("app_full_name_1");
        comp.setChildComponentName("child_component_1");
        comp.setComponentId(1);
        comp.setComponentName("component_1");
        comp.setComponentType(ComponentType.APP.name());
        comp.setCompDesc("component_desc_1");

        List<Component> children = new ArrayList<Component>();
        Component child = new Component();
        child.setComponentId(10);
        children.add(child);
        comp.setChildren(children);

        comp.setDelInd(0);

        List<HealtCheckEnvironment> hcEnvList = new ArrayList<HealtCheckEnvironment>();
        HealtCheckEnvironment env = new HealtCheckEnvironment();
        env.setCreatedDate(new Date());
        env.setEnvName("env_1");
        hcEnvList.add(env);
        comp.setHcEnvList(hcEnvList);

        comp.setParentComponentId(0);
        comp.setParentComponentName("parent_component_1");
        comp.setRecentEvent(true);

        List<Region> regions = new ArrayList<>();
        Region region = new Region();
        region.setRegionId(1);
        region.setRegionMessage("message_1");
        region.setRegionName("region_1");
        region.setRegionStatus("active");
        region.setRecentEvent(true);
        regions.add(region);
        comp.setRegion(regions);

        List<String> roles = new ArrayList<String>();
        roles.add("role_1");
        comp.setRoles(roles);
        return comp;
    }

    public static Subscription getSubscription() {
        Subscription gs = new Subscription();
        gs.setComponentId(1);
        gs.setEnvironmentId(1);
        gs.setAlertSubscriptionId(1);
        gs.setGlobalSubscriptionTypeId(1);
        gs.setComponentName("new_component_name");
        gs.setSubsciptionVal("new_subscription_value");
        gs.setSubsciptionType("new_subscription_type");
        gs.setGlobalSubscriptionType("new_globalSubscriptionType");
        gs.setGlobalSubscriptionTypeId(1);
        gs.setAuthToken("auth_token_1");
        gs.setSubsciptionVal("new_subsciptionVal");
        return gs;
    }

    public static String getPlatform() {
        return DEFAULT_PLATFORM;
    }

    public static String DEFAULT_PLATFORM = "All";

    public static String DEFAULT_REGION = "West Region";

}
