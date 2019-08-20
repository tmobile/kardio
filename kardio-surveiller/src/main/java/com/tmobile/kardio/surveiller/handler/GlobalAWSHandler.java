/*******************************************************************************
 * Copyright 2019 T-Mobile, Inc. or its affiliates. All Rights Reserved.
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
 ******************************************************************************/
package com.tmobile.kardio.surveiller.handler;

import java.net.URL;

import org.apache.log4j.Logger;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * The handler used in health_check_type table. Performs health checks via URL's
 */
public class GlobalAWSHandler extends SurveillerHandler {

	private static final Logger logger = Logger.getLogger(GlobalAWSHandler.class);	
	private static final String PARAM_KEY_URL = "URL";	
	
	/**
	 * Get health check status via URL
	 * @see com.tmobile.kardio.surveiller.handler.SurveillerHandler#getSurveillerStatus()
	 **/
	@Override
	public StatusVO getSurveillerStatus() {		
		XmlReader reader = null;
		try {
			if(paramDetails.get(PARAM_KEY_URL)==null ){
				logger.error("Configuration Error : The RSS URL is null in DB");
				throw new IllegalArgumentException("Configuration Error : The RSS URL is null in DB");
			}
			
			URL url = new URL(paramDetails.get(PARAM_KEY_URL));					
			reader = new XmlReader(url);
			SyndFeed feed = new SyndFeedInput().build(reader);			
			if(feed.getEntries().size() == 0){
				return new StatusVO(Status.UP);
			}
			else{
				SyndEntry entry = (SyndEntry) feed.getEntries().get(0);				
				if(entry.getTitle().contains("operating normally") || (entry.getDescription().getValue().contains("operating normally"))){
					return new StatusVO(Status.UP);
				}
				else{
					logger.debug(feed.getTitle() + " - " + entry.getTitle() + "for" + paramDetails.get(PARAM_KEY_URL));
					return new StatusVO(Status.DOWN,feed.getTitle() + " - " + entry.getTitle() + ". Date -" + entry.getPublishedDate());	
				}											
			}					 	
			
		} catch (Exception e) {
			logger.error("Got Exception while connecting", e);
			return new StatusVO(Status.DOWN,e.getMessage() + ". URL=" + paramDetails.get(PARAM_KEY_URL));
		} finally{
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				logger.error("Got Exception while closing reader", e);
			}
		}
	}

}
