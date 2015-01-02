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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
* @author Luigi Sportelli gigiozzz@gmail.com
*
*/
public class CommonStats {

	private String hostname;
	private String server;
	private String metric;
	private long value;
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}


	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}
}
