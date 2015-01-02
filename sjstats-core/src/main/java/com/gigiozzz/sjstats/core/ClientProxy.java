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

import java.util.Set;

import javax.management.ObjectName;

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
public interface ClientProxy<T> {
    /**
     * Gets the whole PMI stats subtree for the given interface type.
     * 
     * @param  name the NAME field of a specific PMI interface
     * @return a WSStats object for the appropriate PMI interface
     * @throws Exception
     */
    public T getStats(String name) throws Exception;
    
    /**
     * Gets the target application server logical name.
     * 
     * @return the WAS instance name
     * @throws Exception
     */
    public String getServerName() throws Exception;

    /**
     * Gets the target application server version.
     * 
     * @return the product version for the target instance
     * @throws Exception
     */
    public String getServerVersion() throws Exception;

    /**
     * Gets a set of MBeans.
     * 
     * @param  query
     * @return all the MBeans matching a specific query
     * @throws Exception
     */
    public Set<ObjectName> getMBeans(String query) throws Exception;
    
    /**
     * Gets a single MBean.
     * 
     * @param  query
     * @return a unique MBean matching a specific query
     * @throws Exception
     */
    public ObjectName getMBean(String query) throws Exception;

    /**
     * Gets one attribute of the given MBean.
     * 
     * @param  mbean
     * @param  attribute
     * @return the attribute value
     * @throws Exception
     */
    public Object getAttribute(ObjectName mbean, String attribute) throws Exception;

}
