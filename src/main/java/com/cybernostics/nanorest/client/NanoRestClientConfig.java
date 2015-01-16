package com.cybernostics.nanorest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.lib.interfaceparsers.DefaultRequestMapper;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParserConfig;
import com.cybernostics.nanorest.servicelocator.DefaultServiceDirectory;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

@Configuration
@Import(InterfaceParserConfig.class)
public class NanoRestClientConfig  {

	@Autowired
	private RemoteServiceEndpoint[] endPoints;

	@Autowired
	DefaultRequestMapper interfaceParserSource;

	@Bean
	public NanoRestClientFactory nanoRestClientFactory()
	{
		NanoRestClientFactory nanoRestClientFactory = new NanoRestClientFactory();
		nanoRestClientFactory.setDirectory(serviceDirectory());
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
