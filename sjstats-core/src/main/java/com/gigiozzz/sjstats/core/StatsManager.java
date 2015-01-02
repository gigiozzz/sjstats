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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gigiozzz.sjstats.core.checks.CheckService;
import com.gigiozzz.sjstats.core.checks.CheckServiceData;

/**
 * This class manages the execution of the
 * check services, and builds the stats to log
 * using data supplied by the caller.
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
@Component
public class StatsManager {

	private static final Logger logger = LoggerFactory.getLogger(StatsManager.class);

	@Autowired
	private ClientProxyBuilder proxyBuilder;

	@Autowired
	private List<CheckService> checkServices;

    // Single test output (for a specific PMI interface)
    private List<CheckServiceData> metrics = null;

    /**
     * Instantiates a WebSphere proxy, and run all
     * the required tests based on the params contents.
     * 
     * @param  params HTTP request params
     * @return the Graphite metrics as plain old text
     */
    public List<CommonStats> process(ServerToCheck checkConfig) {

    	List<CommonStats> stats = new ArrayList<CommonStats>();
    	
    	try {
            // Proxy to the target WAS instance
            ClientProxy proxy = proxyBuilder.getProxy(checkConfig);

            // We get the information we need to build the Graphite scheme
            String serverName = proxy.getServerName(); // WAS instance name

            long now = System.currentTimeMillis() / 1000L;

            logger.debug("Start for hostname: {} servername: {}",checkConfig.getHostName(),serverName);

            for (CheckService service : checkServices) {
                if (checkConfig.getServiceTocheck().contains(service.getName())) {
                    logger.debug("Read metric: {}",service.getName());
                    metrics = service.run(proxy, "*");
                    for (CheckServiceData metric : metrics) {
                    	
                    	
                    	CommonStats s = new CommonStats();
                    	s.setHostname(checkConfig.getHostName());
                    	s.setServer(serverName);
                    	s.setMetric(metric.getMetric());
                    	s.setValue(metric.getValue());
                    	stats.add(s);
                    	
                        StringBuilder sb = new StringBuilder("");
                        
                        sb
                        .append(checkConfig.getHostName() + " ")
                        .append(serverName + ".")
                        .append(metric.getMetric() + " ")
                        .append(metric.getValue() + " ")
                        .append(now);
                        
                        
                        logger.debug("{}",sb.toString());
                    }
                }
            }
        } catch (Exception ex) {
        	logger.error("Error get value",ex);
        }

        return stats;
    }

}
