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

package com.gigiozzz.sjstats.core.checks;

import java.util.List;

import com.gigiozzz.sjstats.core.ClientProxy;


/**
 * The common interface for a check service.
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
public interface CheckService<T> {

    /**
     * 
     * @param  proxy applicative proxy for a generic Java Application Server
     * 
     * @param  params specific params for the check, if needed.
     * 
     * @return a list of check service data composed by a couple of metrics tag and his value
     * 
     */
    List<CheckServiceData> run(ClientProxy<T> proxy, String params);

    /**
    * @return the name of the service
    * 
    */
    String getName();
    
}
