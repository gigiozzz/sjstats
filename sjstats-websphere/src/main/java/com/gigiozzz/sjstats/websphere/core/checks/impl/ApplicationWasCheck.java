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

package com.gigiozzz.sjstats.websphere.core.checks.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigiozzz.sjstats.core.ClientProxy;
import com.gigiozzz.sjstats.core.checks.CheckService;
import com.gigiozzz.sjstats.core.checks.CheckServiceData;
import com.gigiozzz.sjstats.core.checks.CheckServiceUtils;
import com.ibm.websphere.pmi.stat.WSRangeStatistic;
import com.ibm.websphere.pmi.stat.WSSessionManagementStats;
import com.ibm.websphere.pmi.stat.WSStats;


/**
 * Gets the current HTTP sessions count
 * for a web application.
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *  
 */
public class ApplicationWasCheck extends CheckServiceUtils implements CheckService<WSStats> {

	private static final Logger logger = LoggerFactory.getLogger(JVMWasCheck.class);
	private static final String CHECK_SERVICE_NAME ="application";

	public String getName(){
		return CHECK_SERVICE_NAME;
	}
    /**
     * WebSphere applications stats.
     * 
     * @param proxy   an applicative proxy for the target WAS instance
     * @param params  a comma separated list of web application names, or
     *                a wildcard character (*) for all web applications
     * @return output a list of strings for collected data
     */
    public List<CheckServiceData> run(ClientProxy<WSStats> proxy, String params) {
        // HTTP query params
        List<String> applications = Arrays.asList(params.split(","));

        // Graphite data
        List<CheckServiceData> output = new ArrayList<CheckServiceData>();

        // Application name
        String name;

        // PMI stats
        WSStats stats;
        WSRangeStatistic lc;

        // Performance data
        long liveCount;

        try {
            stats = (WSStats)proxy.getStats(WSSessionManagementStats.NAME);
        } catch (Exception ignored) {
        	logger.error("ignored exception",ignored);
        	return output;
        }

        WSStats[] stats1 = stats.getSubStats();
        for (WSStats stat1 : stats1) {

            if (stat1.getName().matches("ibmasyncrsp#ibmasyncrsp.war")) {
                continue;
            }

            if (applications.contains("*") || applications.contains(stat1.getName())) {
                lc = (WSRangeStatistic)stat1.getStatistic(WSSessionManagementStats.LiveCount);
                
                try {
                    liveCount = lc.getCurrent();
                } catch (NullPointerException e) {
                	logger.error("invalid 'Servlet Session Manager' PMI settings.",e);
                    throw new RuntimeException("invalid 'Servlet Session Manager' PMI settings.");
                }

                name = cleanUp(stat1.getName());
                CheckServiceData data = new CheckServiceData();
                data.setMetric("application." + name + ".liveCount");
                data.setValue(liveCount);
                output.add(data);
            }
        }

        Collections.sort(output);
        return output;
    }
	

}
