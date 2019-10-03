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
package com.tmobile.kardio.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.tmobile.kardio.surveiller.vo.EnvironmentVO;

/**
 * @author Arun Nair
 *
 */
public class EnvironmentTestData {

	public List<EnvironmentVO> getEnvironmentList(){
		EnvironmentVO env = new EnvironmentVO();
    	env.setEnvironmentName("Development11");
    	//env.setEnvironmentDesc("env_desc_1");
    	//env.setEnvLock(0);
    	//env.setDisplayOrder(0);
    	File resourcesDirectory = new File("src/test/data");
    	String content = null;
		try {
			content = new String ( Files.readAllBytes( Paths.get(resourcesDirectory.getAbsolutePath()+"/marathonJson.txt") ) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		env.setMarathonJson(content);
    	List<EnvironmentVO> envList = new ArrayList<EnvironmentVO>();
    	envList.add(env);
    	return envList;
	}
	
}
