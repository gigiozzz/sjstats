/**
 * This file is part of Wasagent.
 *
 * Wasagent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wasagent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Wasagent. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.gigiozzz.sjstats.websphere.core;

import java.util.Properties;
import java.util.Set;

import javax.management.ObjectName;

import com.gigiozzz.sjstats.core.ClientProxy;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.pmi.stat.MBeanStatDescriptor;
import com.ibm.websphere.pmi.stat.StatDescriptor;
import com.ibm.websphere.pmi.stat.WSStats;

/**
 * An applicative proxy for a WebSphere instance.
 * Provides convenient methods to query stats objects
 * or mbeans. The proxy generic configuration is loaded
 * from the 'websphere.properties' file, and the specific
 * params are given through HTTP query parameters.
 * 
 * @author Yann Lambret
 *
 */
public class WASClientProxy implements ClientProxy<WSStats> {

    private AdminClient client;        // WebSphere JMX client
    private ObjectName serverMBean;    // WebSphere server MBean
    private ObjectName perfMBean;      // WebSphere Perf MBean

    public WASClientProxy(Properties props) throws Exception {
    	 client = AdminClientFactory.createAdminClient(props);

         // We need the perf MBean to query stats objects or other MBeans
         // The server MBean is used to retrieve the logical instance name
         serverMBean = getMBean("WebSphere:*,type=Server");
         perfMBean = getMBean("WebSphere:*,type=Perf");
    }

    /**
     * Gets the whole PMI stats subtree for the given interface type.
     * 
     * @param  name the NAME field of a specific PMI interface
     * @return a WSStats object for the appropriate PMI interface
     * @throws Exception
     */
    public WSStats getStats(String name) throws Exception {
        // We get a MBeanStatDescriptor for the specified PMI stats interface name
        StatDescriptor sd = new StatDescriptor(new String[] {name});
        MBeanStatDescriptor msd = new MBeanStatDescriptor(serverMBean, sd);

        // We want the query to be recursive
        Object[] parameters = new Object[] {msd, new Boolean(true)};
        String[] signature = new String[] {"com.ibm.websphere.pmi.stat.MBeanStatDescriptor", "java.lang.Boolean"};
        return (WSStats)client.invoke(perfMBean, "getStatsObject", parameters, signature);
    }

    /**
     * Gets the target application server logical name.
     * 
     * @return the WAS instance name
     * @throws Exception
     */
    public String getServerName() throws Exception {
        return (String)getAttribute(serverMBean, "name");
    }

    /**
     * Gets the target application server version.
     * 
     * @return the product version for the target instance
     * @throws Exception
     */
    public String getServerVersion() throws Exception {
        return (String)getAttribute(serverMBean, "platformVersion");
    }

    /**
     * Gets a set of MBeans.
     * 
     * @param  query
     * @return all the MBeans matching a specific query
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Set<ObjectName> getMBeans(String query) throws Exception {
        return client.queryNames(new ObjectName(query), null);
    }

    /**
     * Gets a single MBean.
     * 
     * @param  query
     * @return a unique MBean matching a specific query
     * @throws Exception
     */
    public ObjectName getMBean(String query) throws Exception {
        Set<ObjectName> mbeans = getMBeans(query);
        return mbeans.iterator().next();
    }

    /**
     * Gets one attribute of the given MBean.
     * 
     * @param  mbean
     * @param  attribute
     * @return the attribute value
     * @throws Exception
     */
    public Object getAttribute(ObjectName mbean, String attribute) throws Exception {
        return client.getAttribute(mbean, attribute);
    }

}
