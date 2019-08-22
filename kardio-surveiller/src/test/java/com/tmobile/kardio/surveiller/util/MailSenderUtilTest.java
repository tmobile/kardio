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
package com.tmobile.kardio.surveiller.util;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Transport;
import javax.net.ssl.HttpsURLConnection;

import org.junit.Test;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.enums.ComponentType;
import com.tmobile.kardio.surveiller.enums.HealthCheckType;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.ComponentVO;
import com.tmobile.kardio.surveiller.vo.HealthCheckVO;
import com.tmobile.kardio.surveiller.vo.StatusVO;
import com.tmobile.kardio.surveiller.vo.SubscriptionVO;
import com.tmobile.kardio.test.TestDaoService;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class MailSenderUtilTest {
    @Mocked URL mockURL;
    @Mocked HttpURLConnection mockCon;
    @Mocked HttpsURLConnection mockHttpsCon;
    MockUp<DBQueryUtil> dbMock;
    
    private void mockDB(int subType, String subVal) {
    	dbMock = new MockUp<DBQueryUtil>() {
			@Mock
			public List<SubscriptionVO> getAllSubscriptions(Set<Integer> compids){
				List<SubscriptionVO> result = new ArrayList<SubscriptionVO>();
				SubscriptionVO sv = new SubscriptionVO();
				sv.setSubscriptionType(subType);
				sv.setSubscriptionValue(subVal);
				sv.setEnvironmentId(0);
				sv.setComponentId(0);
				result.add(sv);
				return result;
			}
			
		};    	
    }
    
    @Test
    public void testSendMessageToSlackChannel() throws SQLException {
    	mockDB(SurveillerConstants.SUBSCRIPTION_TYPE_SLACK_CHANNEL, "https://hooks.slack.com/services/T00000000");
    	
    	List <HealthCheckVO> hcs = new ArrayList<HealthCheckVO>();
		HealthCheckVO hc = getHealthCheckVO();
		hcs.add(hc);
		hc.setFailureStatusMessage("Failed message");
    	MailSenderUtil.sendMailForHealthCheckVos(hcs);
    	
    	dbMock.tearDown();
    }

    @Test
    public void testSendSlackAlertByWebHook() throws SQLException {
    	mockDB(SurveillerConstants.SUBSCRIPTION_TYPE_SLACK_WEB_HOOK, "https://hooks.slack.com/services/T00000000");
    	
    	List <HealthCheckVO> hcs = new ArrayList<HealthCheckVO>();
		HealthCheckVO hc = getHealthCheckVO();
		hcs.add(hc);
		hc.setFailureStatusMessage("Failed message");
    	MailSenderUtil.sendMailForHealthCheckVos(hcs);
    	
    	dbMock.tearDown();
    }

    @Test
    public void testSendMail() throws SQLException {
    	mockDB(SurveillerConstants.SUBSCRIPTION_TYPE_EMAIL, "ut@tmo.com");
    	MockUp <Transport>mailMock = new MockUp<Transport>() {
    		@Mock
    		public void send(Message message) {
    			
    		}
    	};
    	List <HealthCheckVO> hcs = new ArrayList<HealthCheckVO>();
		HealthCheckVO hc = getHealthCheckVO();
		hcs.add(hc);
		hc.setFailureStatusMessage("Failed message");
    	MailSenderUtil.sendMailForHealthCheckVos(hcs);
    	
    	dbMock.tearDown();
    	mailMock.tearDown();
    }

    @Test
    public void testEmptyList() throws SQLException {
    	
    	List <HealthCheckVO> hcs = new ArrayList<HealthCheckVO>();
		hcs.clear();
    	MailSenderUtil.sendMailForHealthCheckVos(hcs);
    	
    }
    
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
	  Constructor<MailSenderUtil> constructor = MailSenderUtil.class.getDeclaredConstructor();
	  assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	  constructor.setAccessible(true);
	  constructor.newInstance();
	}

    private HealthCheckVO getHealthCheckVO() {
    	HealthCheckVO hvo= new HealthCheckVO();
    	StatusVO svo=new StatusVO(Status.UP);
    	ComponentVO cvo = new ComponentVO();
    	cvo.setComponentId(0);
    	cvo.setComponentName("hc_comp_1");
    	cvo.setComponentDesc("hc_comp_desc_1");
    	cvo.setParentComponentName("hc_parent_comp_1");
    	hvo.setComponent(cvo);
    	hvo.setHealthCheckRetryMaxCount(3L);
    	hvo.setHealthCheckTypeName(HealthCheckType.URL_200_CHECK.name());
        hvo.setHealthCheckRetryCurrentCount(1L);
    	hvo.setHealthCheckId(TestDaoService.healthCheckID);
    	hvo.setStatus(svo);
    	hvo.setCurrentStatus(1L);
    	hvo.setEnvironmentId(0);
    	hvo.setEnvironmentName("hc_env_1");
    	hvo.setRegionName("region0");
    	hvo.setFailureStatusMessage("Fail message");
    	hvo.setComponentType(ComponentType.APP);
    	return hvo;
    }
}

