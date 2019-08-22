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
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.tmobile.kardio.ComponentStatusApplication;
import com.tmobile.kardio.bean.StatusHistory;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentStatusApplication.class})
public class StatusHistoryTest {
	@Test
    public void testComponentHistoryCopy() throws Exception {
		StatusHistory history = new StatusHistory();
		history.setPercentageUpTimeEast(0.0F);
		history.setPercentageUpTimeWest(0.0F);
		history.setStatusEast("status_East");
		history.setStatusTime("statusTime");
		history.setStatusWest("status_West");
		StatusHistory copy = history.copy();
		Assert.assertEquals("Copied status history is not same", history.getPercentageUpTimeEast(), copy.getPercentageUpTimeEast());
    }
}
