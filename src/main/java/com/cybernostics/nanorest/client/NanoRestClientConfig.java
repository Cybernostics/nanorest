package com.cybernostics.nanorest.client;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.lib.interfaceparsers.BasicEntityServiceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.DefaultServiceInterfaceRequestMapper;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParserConfig;
import com.cybernostics.nanorest.servicelocator.DefaultServiceDirectory;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

/**
 * Standard Spring Config for setting up a client
 * @author jason
 *
 */
@Configuration
@Import(InterfaceParserConfig.class)
public class NanoRestClientConfig  {

	@Autowired
	private RemoteServiceEndpoint[] endPoints;

	private static Logger LOG = Logger.getLogger(NanoRestClientFactory.class.getSimpleName());

	@Autowired
	DefaultServiceInterfaceRequestMapper interfaceParserSource;

	/**
	 * For testing you could provide your own mock version which will be fed into the
	 * client factory.
	 */
	@Autowired(required=false)
	private CallableHttpService httpService;

	/**
	 * This is the bean you wire to get dynamic clients cooked up for your interfaces
	 * @See com.cybernostics.nanorest.example.client.ExampleClientApplication for usage
	 * @return a factory instance for all your nanoRest client generation needs.
	 */
	@Bean
	public NanoRestClientFactory nanoRestClientFactory()
	{
		NanoRestClientFactory nanoRestClientFactory = new NanoRestClientFactory();
		nanoRestClientFactory.setDirectory(serviceDirectory());
		nanoRestClientFactory.setMapper(interfaceParserSource);
		if(httpService!=null){
			httpService=new CallableHttpService();
		}else {
			LOG.info("No httpService autowired so creating one");
			httpService = new CallableHttpService();
		}
		nanoRestClientFactory.setHttpService(httpService);
		return nanoRestClientFactory;
	}

	@Bean
	public ServiceDirectory serviceDirectory()
	{
		DefaultServiceDirectory defaultServiceDirectory = new DefaultServiceDirectory();
		defaultServiceDirectory.addSource(endPoints);
		return defaultServiceDirectory;
	}


	@Bean
	InterfaceParser basicEntityServiceParser() {
		return new BasicEntityServiceParser();
	}
}
