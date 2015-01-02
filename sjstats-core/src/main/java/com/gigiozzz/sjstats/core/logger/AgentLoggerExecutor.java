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

package com.gigiozzz.sjstats.core.logger;

import com.gigiozzz.sjstats.core.CommonStats;

/**
 * The common interface to log SJStats.
 *
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
public interface AgentLoggerExecutor {

	public void doLog(CommonStats stat);
	
}
