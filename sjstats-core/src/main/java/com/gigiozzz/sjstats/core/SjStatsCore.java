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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
@Service
public class SjStatsCore {
	
	private static final Logger logger = LoggerFactory.getLogger(SjStatsCore.class);
	
	
	@Autowired
	private StatsManager manager;

    public Map<String,List<CommonStats>> checkStats(List<ServerToCheck> servers) {
    	Map<String,List<CommonStats>> outValue = new HashMap<String,List<CommonStats>>();
    	logger.debug("map bean config server to check params: {}",ReflectionToStringBuilder.toString(servers));
    	
    	for(ServerToCheck s:servers){
	        
    		check(s);
	        
    	}      
    	
    	return outValue;
    }

    public Map<String,List<CommonStats>> check(ServerToCheck s) {
    	Map<String,List<CommonStats>> outValue = new HashMap<String,List<CommonStats>>();

    	List<CommonStats> out = manager.process(s);
        
        logger.debug("{}",out);
        outValue.put(s.getServerName(), out);
        
    	return outValue;
    }
}
