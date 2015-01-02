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

package com.gigiozzz.sjstats.agent.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gigiozzz.sjstats.core.SjStatsCore;
import com.gigiozzz.sjstats.core.config.ConfigServerToCheck;

/**
 * Main Class to start application from OS console.
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
public class AgentConsole {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentConsole.class);
	
	private ConfigServerToCheck serverMap;
	
    public static void main(String[] args) throws Exception {
    	logger.info("[WASAgentConsole] Start Console Application");
    	ApplicationContext context =   new ClassPathXmlApplicationContext("applicationContext.xml");    	
    	
    	AgentConsole wac = new AgentConsole();
    	SjStatsCore wa = context.getBean(SjStatsCore.class);
    	
    	if(wac.serverMap == null){
    		wac.serverMap = (ConfigServerToCheck)context.getBean("serverMap");
    	}
    	
    	wa.checkStats(wac.serverMap.getServers());
    	
    	logger.info("[WASAgentConsole] Stop Console Application");
    	System.exit(0);
    } 
    
}
