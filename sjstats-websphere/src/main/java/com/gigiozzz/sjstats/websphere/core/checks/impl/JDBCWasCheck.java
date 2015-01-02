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
import com.ibm.websphere.pmi.stat.WSJDBCConnectionPoolStats;
import com.ibm.websphere.pmi.stat.WSRangeStatistic;
import com.ibm.websphere.pmi.stat.WSStats;


/**
 * Gets statistics for JDBC datasources.
 * 
 * The following metrics are available:
 * 
 *   - The datasource current pool size
 *   - The datasource maximum pool size
 *   - The active connection count
 *   - The number of threads waiting for
 *     a connection from the pool
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
public class JDBCWasCheck extends CheckServiceUtils implements CheckService<WSStats> {

	private static final String CHECK_SERVICE_NAME ="jdbc";

	public String getName(){
		return CHECK_SERVICE_NAME;
	}

    /**
     * WebSphere JDBC datasources stats.
     * 
     * @param proxy   an applicative proxy for the target WAS instance
     * @param params  a comma separated list of datasource names, or
     *                a wildcard character (*) for all datasources
     * @return output a list of strings for collected data
     */
    public List<CheckServiceData> run(ClientProxy<WSStats> proxy, String params) {
        // HTTP query params
        List<String> datasources = Arrays.asList(params.split(","));

        // Graphite data
        List<CheckServiceData> output = new ArrayList<CheckServiceData>();

        // Datasource name
        String name;

        // PMI stats
        WSStats stats;
        WSBoundedRangeStatistic ps;
        WSBoundedRangeStatistic fps;
        WSRangeStatistic wtc;

        // Performance data
        long currentPoolSize, maximumPoolSize, freePoolSize, waitingThreadCount, activeCount;

        try {
            stats = (WSStats)proxy.getStats(WSJDBCConnectionPoolStats.NAME);
        } catch (Exception ignored) {
            return output;
        }

        WSStats[] stats1 = stats.getSubStats(); // JDBC Provider level
        for (WSStats stat1 : stats1) {
            WSStats[] stats2 = stat1.getSubStats(); // DataSource level
            for (WSStats stat2 : stats2) {

                // No statistics for WAS internal datasources
                if (stat2.getName().matches("jdbc/DefaultEJBTimerDataSource")) {
                    continue;
                }

                if (datasources.contains("*") || datasources.contains(stat2.getName())) {
                    ps = (WSBoundedRangeStatistic)stat2.getStatistic(WSJDBCConnectionPoolStats.PoolSize);
                    fps = (WSBoundedRangeStatistic)stat2.getStatistic(WSJDBCConnectionPoolStats.FreePoolSize);
                    wtc = (WSRangeStatistic)stat2.getStatistic(WSJDBCConnectionPoolStats.WaitingThreadCount);
                    try {
                        currentPoolSize = ps.getCurrent();
                        maximumPoolSize = ps.getUpperBound();
                        freePoolSize = fps.getCurrent();
                        waitingThreadCount = wtc.getCurrent();
                        activeCount = currentPoolSize - freePoolSize;
                    } catch (NullPointerException e) {
                        throw new RuntimeException("invalid 'JDBC Connection Pools' PMI settings.");
                    }

                    name = cleanUp(stat2.getName());

                    CheckServiceData data = new CheckServiceData();

                    data.setMetric("jdbc." + name + ".activeCount");
                    data.setValue(activeCount);
                    output.add(data);
                    
                    data = new CheckServiceData();
                    data.setMetric("jdbc." + name + ".currentPoolSize");
                    data.setValue(currentPoolSize);
                    output.add(data);

                    data = new CheckServiceData();
                    data.setMetric("jdbc." + name + ".maximumPoolSize");
                    data.setValue(maximumPoolSize);
                    output.add(data);

                    data = new CheckServiceData();
                    data.setMetric("jdbc." + name + ".waitingThreadCount");
                    data.setValue(waitingThreadCount);
                    output.add(data);

                }
            }
        }

        Collections.sort(output);
        return output;
    }

}
