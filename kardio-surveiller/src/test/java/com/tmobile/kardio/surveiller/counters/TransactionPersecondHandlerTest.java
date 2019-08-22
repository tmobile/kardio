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
/**
 * 
 */
package com.tmobile.kardio.surveiller.counters;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.tmobile.kardio.surveiller.main.RestCommunicationHandler;
import com.tmobile.kardio.test.DirectPromethuesQueryHandlerMockData;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author U29842
 *
 */
public class TransactionPersecondHandlerTest {

	DirectPromethuesQueryHandlerMockData mcData= new DirectPromethuesQueryHandlerMockData();
	
	/**
	 * UT for gGetCounterMerticValue() method in TransactionPersecondHandler
	 */
	@Test()
	public void testGetCounterMerticValue(){
		/*Mocking the getResponse, Since its calling prometheous in the original system*/
		MockUp<RestCommunicationHandler> rch = new MockUp<com.tmobile.kardio.surveiller.main.RestCommunicationHandler>() {
			@Mock
			public String getResponse(String urlStr, Boolean doAuth,String authType, String encodedAuth){
				return mcData.mockSingleVauleJson();
			}
		};
		TransactionPersecondHandler ss = new TransactionPersecondHandler();
        try {
			ss.getCounterMerticValue(mcData.getMockedCounterDetails(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        rch.tearDown();
         /***Test Scenario : Value node in json is empty***/     
        
        /*Mocking the getResponse, Since its calling prometheous in the original system*/
		rch = new MockUp<com.tmobile.kardio.surveiller.main.RestCommunicationHandler>() {
			@Mock
			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
				return mcData.mockValueNodeEmptyPromJson();
			}
		};
		boolean thrown = false;
		try {
			ss.getCounterMerticValue(mcData.getMockedCounterDetails(), null);
		} catch (Exception e) {
			thrown = true;
		}
		assertTrue(thrown);
        rch.tearDown();
		
		/***Test Scenario : data node in json is empty***/     
		
		rch = new MockUp<com.tmobile.kardio.surveiller.main.RestCommunicationHandler>() {
			@Mock
			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
				String jsonString = mcData.mockDataNodeEmptyPromJson();
				return jsonString;
			}
		};
		boolean emptyDataException = false;
		try {
			ss.getCounterMerticValue(mcData.getMockedCounterDetails(), null);
		} catch (Exception e) {
			emptyDataException = true;
		}
		assertTrue(emptyDataException);
        rch.tearDown();
	
		/***Test Scenario : Status is failed in json***/    
	
		rch = new MockUp<com.tmobile.kardio.surveiller.main.RestCommunicationHandler>() {
			@Mock
			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
				return mcData.mockDataStatusFailedPromJson();
			}
		};
		boolean statusFailedException = false;
		try {
			ss.getCounterMerticValue(mcData.getMockedCounterDetails(), null);
		} catch (Exception e) {
			statusFailedException = true;
		}
		assertTrue(statusFailedException);
        rch.tearDown();
		
		/***Test Scenario : Data is null in json***/ 
		
		rch = new MockUp<com.tmobile.kardio.surveiller.main.RestCommunicationHandler>() {
			@Mock
			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
				return mcData.mockDataNodeNullPromJson();
			}
		};
		boolean resultEmptyException = false;
		try {
			ss.getCounterMerticValue(mcData.getMockedCounterDetails(), null);
		} catch (Exception e) {
			resultEmptyException = true;
		}
		assertTrue(resultEmptyException);
        rch.tearDown();
		
		/***Test Scenario : Status is null in json***/ 
		
		rch = new MockUp<com.tmobile.kardio.surveiller.main.RestCommunicationHandler>() {
			@Mock
			public String getResponse(String urlStr, Boolean doAuth, String authType, String encodedAuth){
				return mcData.mockStatusNullPromJson();
			}
		};
		boolean statusNullException = false;
		try {
			ss.getCounterMerticValue(mcData.getMockedCounterDetails(), null);
		} catch (Exception e) {
			statusNullException = true;
		}
		assertTrue(statusNullException);
        rch.tearDown();
	}
	
}
