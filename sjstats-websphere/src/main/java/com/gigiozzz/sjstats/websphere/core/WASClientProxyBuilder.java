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

package com.gigiozzz.sjstats.websphere.core;

import java.security.Security;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gigiozzz.sjstats.core.ClientProxyBuilder;
import com.gigiozzz.sjstats.core.ServerToCheck;
import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.pmi.stat.WSStats;

/**
 * Main Class to start application from OS console.
 * 
 * @author Luigi Sportelli gigiozzz@gmail.com
 *
 */
@Component
public class WASClientProxyBuilder implements ClientProxyBuilder<WSStats>,InitializingBean {

	
    private Properties props;          // Connection properties

    @Value("${ibm.soap.path}")
    private String soapPath;
    @Value("${ibm.ssl.path}")
    private String sslPath;

    @Value("${ibm.was.username}")
    private String wasUsername;
    @Value("${ibm.was.password}")
    private String wasPassowrd;

    @Value("${soap.keystore.path}")
    private String keyStorePath;
    @Value("${soap.keystore.password}")
    private String keyStorePassword;
    @Value("${soap.keystore.type}")
    private String keyStoreType;

    @Value("${soap.truststore.path}")
    private String trustStorePath;
    @Value("${soap.truststore.password}")
    private String trustStorePassword;
    @Value("${soap.truststore.type}")
    private String trustStoreType;

    

    /**
     * Default constructor.
     * 
     * @param params HTTP query parameters
     */
    public WASClientProxyBuilder() {
    }

    /**
     * Proxy initialization. Gets an AdminClient instance first,
     * and then the Perf MBean for the target WAS instance.
     * 
     * @throws Exception
     */
    public WASClientProxy getProxy(ServerToCheck checkConfig) throws Exception {
        // Properties initialization
        props = new Properties();

        // We use a SOAP connector with a 10 seconds timeout

        // We add WAS specific connection params to the default conf
        
        props.setProperty(AdminClient.CONNECTOR_HOST, checkConfig.getHostName());
        props.setProperty(AdminClient.CONNECTOR_PORT, checkConfig.getPort());
        props.setProperty(AdminClient.CONNECTOR_TYPE, AdminClient.CONNECTOR_TYPE_SOAP);
        props.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, "true");
        props.setProperty(AdminClient.CONNECTOR_SOAP_REQUEST_TIMEOUT, "10");
        props.setProperty(AdminClient.CACHE_DISABLED, "false");
        props.setProperty(AdminClient.USERNAME, wasUsername);
       	props.setProperty(AdminClient.PASSWORD, wasPassowrd);
        
       	props.setProperty("javax.net.ssl.trustStore", trustStorePath);
  		props.setProperty("javax.net.ssl.keyStore", keyStorePath);
  		props.setProperty("javax.net.ssl.trustStoreType",trustStoreType);
  		props.setProperty("javax.net.ssl.keyStoreType",keyStoreType);
  		props.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
  		props.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
 		  		
       	WASClientProxy was = new WASClientProxy(props);
       	
       	return was;
    }


	public void afterPropertiesSet() throws Exception {
//		  String pathFile = WASClientProxyBuilder.class.getResource("/soap.client.props").toString();
          System.setProperty(AdminClient.CONNECTOR_SOAP_CONFIG, soapPath);
//          pathFile = WASClientProxyBuilder.class.getResource("/ssl.client.props").toString();
          System.setProperty("com.ibm.SSL.ConfigURL",sslPath);
          /**/
      	  //System.setProperty("javax.net.debug","all:handshake:verbose");
          Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
          
		
	}

}
