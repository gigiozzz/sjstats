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

package com.gigiozzz.sjstats.agent.web;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gigiozzz.sjstats.core.CommonStats;
import com.gigiozzz.sjstats.core.ServerToCheck;
import com.gigiozzz.sjstats.core.SjStatsCore;
import com.gigiozzz.sjstats.core.config.ConfigServerToCheck;
import com.gigiozzz.sjstats.core.logger.AgentLoggerExecutor;

/**
 * Agent Web for simple check with html output
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
@Controller
public class AgentWebController {

	
	private static final String STATS_MAP = "statsMap";

	@Autowired
	private SjStatsCore agent;
	
	@Autowired
	@Qualifier("fileLoggerExecutor")
	private AgentLoggerExecutor file;
	
	@Autowired
	@Qualifier("statsDLoggerExecutor")
	private AgentLoggerExecutor statsd;
	
	@Autowired
	private ConfigServerToCheck serverMap;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) {
		
		Map<String,List<CommonStats>> stats = agent.checkStats(serverMap.getServers());
		
		model.addAttribute(STATS_MAP,stats);
		
		return "index";
	}
	   
	@RequestMapping(value = "/statsd/send", method = RequestMethod.GET)
	public String statsdSend(Model model) {
		
		Map<String,List<CommonStats>> stats = agent.checkStats(serverMap.getServers());
		for(String key: stats.keySet()){
			List<CommonStats> l = stats.get(key);
			for(CommonStats s: l){
				statsd.doLog(s);
			}
		}
		
		return "send";
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String check(@RequestParam(value="hostname") String hostName,
			@RequestParam(value="port") String port,
			@RequestParam(value="servername") String serverName,
			@RequestParam(value="services") String services,
			Model model) {

		ServerToCheck s = new ServerToCheck();
		s.setHostName(hostName);
		s.setPort(port);
		s.setServerName(serverName);
		s.getServiceTocheck().addAll(Arrays.asList(services.split(",")));
		
		Map<String,List<CommonStats>> stats = agent.check(s);
		
		model.addAttribute(STATS_MAP,stats);
		
		return "index";
	}

}
