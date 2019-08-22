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
package com.tmobile.kardio.surveiller.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.tmobile.kardio.surveiller.enums.Status;
import com.tmobile.kardio.surveiller.vo.StatusVO;

/**
 * Class to handle health checks through TCP communication protocol
 */
public class TCPHandler extends SurveillerHandler{

	private static final Logger logger = Logger.getLogger(TCPHandler.class);
			
	private static final String PARAM_KEY_PORT = "PORT";
	private static final String PARAM_KEY_IP = "IP";
	private static final int TIME_OUT = 15000;
	
	/**
	 * Check whether the IP:ports is reachable.
	 * @see com.tmobile.kardio.surveiller.handler.SurveillerHandler#getSurveillerStatus()
	 */
	@Override
	public StatusVO getSurveillerStatus() {
		if(paramDetails.get(PARAM_KEY_IP)==null || paramDetails.get(PARAM_KEY_PORT)==null ){
			logger.error("The IP address and PORT is null");
			throw new IllegalArgumentException("The IP & PORT cannot be null");
		}
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(paramDetails.get(PARAM_KEY_IP), Integer.parseInt(paramDetails.get(PARAM_KEY_PORT))), TIME_OUT);
			if(socket.isConnected()){
				return new StatusVO(Status.UP);
			}else{
				return new StatusVO(Status.DOWN,"Port is not open : " + " IP = " + paramDetails.get(PARAM_KEY_IP) + "; PORT = " + paramDetails.get(PARAM_KEY_PORT));
			}
		}catch (Exception e) {
			logger.error("Got Exception while connecting to Socket", e);
			return new StatusVO(Status.DOWN, e.getMessage()	 + " : IP = " + paramDetails.get(PARAM_KEY_IP) + "; PORT = " + paramDetails.get(PARAM_KEY_PORT));
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("Got Exception while closing the connection", e);
			}
		}
		
	}

}
