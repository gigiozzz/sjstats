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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigiozzz.sjstats.core.ClientProxy;
import com.gigiozzz.sjstats.core.checks.CheckService;
import com.gigiozzz.sjstats.core.checks.CheckServiceData;
import com.gigiozzz.sjstats.core.checks.CheckServiceUtils;
import com.ibm.websphere.pmi.stat.WSBoundedRangeStatistic;
import com.ibm.websphere.pmi.stat.WSCountStatistic;
import com.ibm.websphere.pmi.stat.WSJVMStats;
import com.ibm.websphere.pmi.stat.WSStats;


/**
 * Gets statistics for the the target
 * WAS instance JVM.
 * 
 * The following metrics are available:
 * 
 *   - The JVM current heap size (MB)
 *   - The JVM maximum heap size (MB)
 *   - The current amount of memory used by the JVM (MB)
 *   - The amount of CPU resources used by the JVM (%)
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
public class JVMWasCheck extends CheckServiceUtils implements CheckService<WSStats> {

	private static final Logger logger = LoggerFactory.getLogger(JVMWasCheck.class);
	private static final String CHECK_SERVICE_NAME ="jvm";

	public String getName(){
		return CHECK_SERVICE_NAME;
	}
	/**
     * WebSphere JVM stats.
     * 
     * @param proxy   an applicative proxy for the target WAS instance
     * @param params  null for this test
     * @return output a list of strings for collected data
     */
    public List<CheckServiceData> run(ClientProxy<WSStats> proxy, String params) {
        // Graphite data
        List<CheckServiceData> output = new ArrayList<CheckServiceData>();

        // PMI stats
        WSStats stats;
        WSBoundedRangeStatistic hs;
        WSCountStatistic um;
        WSCountStatistic cu;

        // Performance data
        long cpuUsage, currentHeapSize, currentHeapUsed, maximumHeapSize;

        try {
            stats = (WSStats)proxy.getStats(WSJVMStats.NAME);
        } catch (Exception ignored) {
        	logger.error("ignored exception",ignored);
            return output;
        }

        hs = (WSBoundedRangeStatistic)stats.getStatistic(WSJVMStats.HeapSize);
        um = (WSCountStatistic)stats.getStatistic(WSJVMStats.UsedMemory);
        cu = (WSCountStatistic)stats.getStatistic(WSJVMStats.cpuUsage);

        try {
            // Memory values are expressed as Megabytes
            maximumHeapSize = hs.getUpperBound() / 1024L;
            currentHeapSize = hs.getCurrent() / 1024L;
            currentHeapUsed = um.getCount() / 1024L;
            cpuUsage = cu.getCount();
        } catch (NullPointerException e) {
        	logger.error("invalid 'JVM Runtime' PMI settings.",e);
            throw new RuntimeException("invalid 'JVM Runtime' PMI settings.");
        }

        
        CheckServiceData data = new CheckServiceData();

        data.setMetric("jvm.cpuUsage");
        data.setValue(cpuUsage);
        output.add(data);

        data = new CheckServiceData();
        data.setMetric("jvm.currentHeapSize");
        data.setValue(currentHeapSize);
        output.add(data);

        data = new CheckServiceData();
        data.setMetric("jvm.currentHeapUsed");
        data.setValue(currentHeapUsed);
        output.add(data);
        
        data = new CheckServiceData();
        data.setMetric("jvm.maximumHeapSize");
        data.setValue(maximumHeapSize);
        output.add(data);
        
        return output;
    }

}
