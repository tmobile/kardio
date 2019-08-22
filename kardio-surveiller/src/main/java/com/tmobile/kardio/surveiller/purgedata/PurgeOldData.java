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
package com.tmobile.kardio.surveiller.purgedata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.constants.SurveillerConstants;
import com.tmobile.kardio.surveiller.util.DBQueryUtil;
import com.tmobile.kardio.surveiller.util.PropertyUtil;

/**
 * Function to purge old data form the DB
 */
public class PurgeOldData {

	private static final Logger logger = Logger.getLogger(PurgeOldData.class);

	private PurgeOldData() {}
	
	public static void doPurgeOldData() throws IOException {
		logger.info("************** STARTED doPurgeOldData **************");

		String s;
		StringBuilder sb = new StringBuilder();

		String purgeFileName = PropertyUtil.getInstance().getValue(SurveillerConstants.PURGE_QUERY_FILENAME);

		try (
			FileReader fr = new FileReader(new File(purgeFileName));
			BufferedReader br= new BufferedReader(fr);
		) {
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			
		} catch(IOException ioe) {
			throw ioe;
		}
		String[] inst = sb.toString().split(";");
		for (int i = 0; i < inst.length; i++) {
			if (!inst[i].trim().equals("")) {
				logger.info("Executing Query : " + inst[i]);
				DBQueryUtil.executeDelete(inst[i]);
			}
		}

		logger.info("************** COMPLETED doPurgeOldData **************");
	}
}
