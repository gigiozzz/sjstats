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

import com.gigiozzz.sjstats.core.ClientProxy;
import com.gigiozzz.sjstats.core.checks.CheckService;
import com.gigiozzz.sjstats.core.checks.CheckServiceData;
import com.gigiozzz.sjstats.core.checks.CheckServiceUtils;
import com.ibm.websphere.pmi.stat.WSBoundedRangeStatistic;
import com.ibm.websphere.pmi.stat.WSRangeStatistic;
import com.ibm.websphere.pmi.stat.WSStats;
import com.ibm.websphere.pmi.stat.WSThreadPoolStats;


/**
 * Gets statistics for WebSphere thread pools.
 * 
 * The following metrics are available:
 * 
 *   - The thread pool current size
 *   - The thread pool maximum size
 *   - The active thread count
 *   - The hung thread count
 * 
 * @author Luigi Sportelli
 *      
 */
public class ThreadPoolWasCheck extends CheckServiceUtils implements CheckService<WSStats> {

	private static final String CHECK_SERVICE_NAME ="thread-pool";

	public String getName(){
		return CHECK_SERVICE_NAME;
	}

    /**
     * WebSphere thread pools stats.
     * 
     * @param proxy   an applicative proxy for the target WAS instance
     * @param params  a comma separated list of thread pool names, or
     *                a wildcard character (*) for all thread pools
     * @return output a list of strings for collected data
     */
    public List<CheckServiceData> run(ClientProxy<WSStats> proxy, String params) {
        // HTTP query params
        List<String> pools = Arrays.asList(params.split(","));

        // Graphite data
        List<CheckServiceData> output = new ArrayList<CheckServiceData>();

        // Thread pool name
        String name;

        // WAS version
        String version = "";

        // PMI stats
        WSStats stats;
        WSBoundedRangeStatistic ps;
        WSBoundedRangeStatistic ac;
        WSRangeStatistic chtc;

        // Performance data
        long activeCount, currentPoolSize, maximumPoolSize, hungCount;

        try {
            stats = (WSStats)proxy.getStats(WSThreadPoolStats.NAME);
            version = proxy.getServerVersion();
        } catch (Exception ignored) {
            return output;
        }

        WSStats[] stats1 = stats.getSubStats();
        for (WSStats stat1 : stats1) {

            if (pools.contains("*") || pools.contains(stat1.getName())) {
                ps = (WSBoundedRangeStatistic)stat1.getStatistic(WSThreadPoolStats.PoolSize);
                ac = (WSBoundedRangeStatistic)stat1.getStatistic(WSThreadPoolStats.ActiveCount);
                try {
                    currentPoolSize = ps.getCurrent();
                    maximumPoolSize = ps.getUpperBound();
                    activeCount = ac.getCurrent();
                } catch (NullPointerException e) {
                    throw new RuntimeException("invalid 'Thread Pools' PMI settings.");
                }

                name = cleanUp(stat1.getName());
                
                CheckServiceData data = new CheckServiceData();

                data.setMetric("threadPool." + name + ".activeCount");
                data.setValue(activeCount);
                output.add(data);
                
                data = new CheckServiceData();
                data.setMetric("threadPool." + name + ".currentPoolSize");
                data.setValue(currentPoolSize);
                output.add(data);
                
                data = new CheckServiceData();
                data.setMetric("threadPool." + name + ".maximumPoolSize");
                data.setValue(maximumPoolSize);
                output.add(data);

                // Hung thread detection, only for WAS 7.0 & 8.x
                if (version.matches("^[78]\\..*")) {
                    chtc = (WSRangeStatistic)stat1.getStatistic(WSThreadPoolStats.ConcurrentHungThreadCount);
                    try {
                        hungCount = chtc.getCurrent();
                        data = new CheckServiceData();
                        data.setMetric("threadPool." + name + ".hungCount");
                        data.setValue(hungCount);
                        output.add(data);
                    } catch (NullPointerException ignored) {
                        /*
                         * PMI settings may be wrong, or this metric is not available due to a specific 
                         * configuration ('com.ibm.websphere.threadmonitor.interval' = 0 for instance)
                         * Anyway we don't want to pollute the regular test output.
                         * 
                         */
                    }                           
                }
            }
        }

        Collections.sort(output);
        return output;
    }

}
