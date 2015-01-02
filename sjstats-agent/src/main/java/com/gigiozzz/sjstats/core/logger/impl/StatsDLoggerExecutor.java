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

package com.gigiozzz.sjstats.core.logger.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gigiozzz.sjstats.core.CommonStats;
import com.gigiozzz.sjstats.core.logger.AgentLoggerExecutor;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

/**
 * Implementation of AgentLoggerExecutor interface to log stats on StatsD server.
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
@Component("statsDLoggerExecutor")
public class StatsDLoggerExecutor implements AgentLoggerExecutor,InitializingBean {

	private StatsDClient statsd = null;
	
	@Value("${agent.statsd.clientname}")
	private String clientName;
	
	@Value("${agent.statsd.hostname}")
	private String serverName;
	
	@Value("${agent.statsd.port}")
	private Integer serverPort;

    private final String[] searchList = new String[]{" ","."};
    private final String[] replaceList = new String[]{"_","_"};

    private Logger logger = LoggerFactory.getLogger(StatsDLoggerExecutor.class);

    public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

    public void doLog(CommonStats stats) {
        logger.info("[{}] [{}] [{}] [{}]", 
        		new Object[]{stats.getHostname(),stats.getServer(),stats.getMetric(),stats.getValue()});
        
        // elimino punti o spazi
        String cleanHostName = StringUtils.replaceEach(stats.getHostname(),searchList , replaceList);
        String cleanServer = StringUtils.replaceEach(stats.getServer(),searchList , replaceList);
        
		statsd.recordExecutionTime(cleanHostName+"."+cleanServer+"."+stats.getMetric(), stats.getValue());
		
    }


	public void afterPropertiesSet() throws Exception {
		statsd = new NonBlockingStatsDClient(clientName, serverName, serverPort);
		
	}


}
