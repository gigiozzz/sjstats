/**
 * This file is part of SJStats.
 *
 * SJStats is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SJStats is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SJStats. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.gigiozzz.sjstats.core;

import java.util.ArrayList;
import java.util.List;

/**
* @author Luigi Sportelli gigiozzz@gmail.com
*
*/public class ServerToCheck {
	
	private String serverName;
	private String hostName;
	private String port;
	private List<String> serviceTocheck;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public List<String> getServiceTocheck() {
		if(serviceTocheck == null){
			serviceTocheck = new ArrayList<String>();
    	}
		return serviceTocheck;
	}

	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	
}
