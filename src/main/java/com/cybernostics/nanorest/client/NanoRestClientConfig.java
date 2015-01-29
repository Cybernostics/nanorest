package com.cybernostics.nanorest.client;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.lib.interfaceparsers.DefaultServiceInterfaceRequestMapper;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParserConfig;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;
import com.cybernostics.nanorest.servicelocator.DefaultServiceDirectory;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

@Configuration
@Import(InterfaceParserConfig.class)
public class NanoRestClientConfig  {

	@Autowired
	private RemoteServiceEndpoint[] endPoints;

	private static Logger LOG = Logger.getLogger(NanoRestClientFactory.class.getSimpleName());

	@Autowired
	DefaultServiceInterfaceRequestMapper interfaceParserSource;

	@Autowired(required=false)
	private HttpService httpService;

	@Bean
	public NanoRestClientFactory nanoRestClientFactory()
	{
		NanoRestClientFactory nanoRestClientFactory = new NanoRestClientFactory();
		nanoRestClientFactory.setDirectory(serviceDirectory());
		nanoRestClientFactory.setMapper(interfaceParserSource);
		if(httpService!=null){
			httpService=new HttpService();
		}else {
			LOG.info("No httpService autowired so creating one");
			httpService = new HttpService();
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
}
