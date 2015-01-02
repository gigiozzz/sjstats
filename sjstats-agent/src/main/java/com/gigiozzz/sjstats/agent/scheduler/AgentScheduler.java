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

package com.gigiozzz.sjstats.agent.scheduler;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.gigiozzz.sjstats.core.CommonStats;
import com.gigiozzz.sjstats.core.SjStatsCore;
import com.gigiozzz.sjstats.core.config.ConfigServerToCheck;
import com.gigiozzz.sjstats.core.logger.AgentLoggerExecutor;

/**
 * Scheduler
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
@Component("agentScheduler")
public class AgentScheduler {

	
	@Autowired
	private SjStatsCore agent;
		
	@Autowired
	@Qualifier("statsDLoggerExecutor")
	private AgentLoggerExecutor statsd;
	
	@Autowired
	private ConfigServerToCheck serverMap;
		
		   
	public void statsdSend() {
		
		Map<String,List<CommonStats>> stats = agent.checkStats(serverMap.getServers());
		for(String key: stats.keySet()){
			List<CommonStats> l = stats.get(key);
			for(CommonStats s: l){
				statsd.doLog(s);
			}
		}
		
	}


}
