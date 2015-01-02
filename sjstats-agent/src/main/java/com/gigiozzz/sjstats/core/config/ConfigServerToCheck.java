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

package com.gigiozzz.sjstats.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gigiozzz.sjstats.core.ServerToCheck;

/**
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 * 
 */
public class ConfigServerToCheck {

	
	private Map<String, String[]> arrays; 
	private List<ServerToCheck> servers;
	

	public void setArrays(Map<String, String[]> arrays) {
        this.arrays = arrays;        
    }
    
    public Map<String, String[]> getArrays() {
        return arrays;
    }
    
    public String[] getArray(String key) {
        return arrays.get(key);
    }

    public List<ServerToCheck> getServers() {
    	if(servers == null){
    		servers = new ArrayList<ServerToCheck>();
    		
            for(String key:arrays.keySet()){
            	String[] val = arrays.get(key);
            	
            	ServerToCheck stc = new ServerToCheck();
            	
            	stc.setServerName(key);
            	stc.setHostName(val[0]);
            	stc.setPort(val[1]);

            	for(int i=2; i<val.length; i++ ){
    		        stc.getServiceTocheck().add(val[i]);	        	
    	        }			
            	
            	this.getServers().add(stc);
            	
            }

    	}
		return servers;
	}

}
