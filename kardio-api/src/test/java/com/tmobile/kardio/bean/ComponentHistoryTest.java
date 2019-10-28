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

import java.util.ArrayList;
import java.util.List;

public class ComponentHistoryTest {
    @Test
    public void testComponentHistoryCopy() throws Exception {
        ComponentHistory componentHistory = new ComponentHistory();
        componentHistory.setComponentId(1);
        componentHistory.setParentComponentId(1);
        componentHistory.setComponentName("component_Name");
        componentHistory.setChildComponentName("child_ComponentName");
        componentHistory.setComponentType("component_Type");
        componentHistory.setParentComponentName("parent_ComponentName");
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setPercentageUpTimeEast(0.0F);
        statusHistory.setPercentageUpTimeWest(0.0F);
        statusHistory.setStatusEast("status_East");
        statusHistory.setStatusTime("statusTime");
        statusHistory.setStatusWest("status_West");
        List<StatusHistory> hists = new ArrayList<StatusHistory>();
        hists.add(statusHistory);
        componentHistory.setStatusHistory(hists);
        ComponentHistory copy = componentHistory.copy();
        Assert.assertEquals("Copied component history is not same", componentHistory.getComponentName(), copy.getComponentName());
    }
}
