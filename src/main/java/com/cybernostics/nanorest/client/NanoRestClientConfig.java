package com.cybernostics.nanorest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParserConfig;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParserSource;
import com.cybernostics.nanorest.servicelocator.DefaultServiceDirectory;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

@Configuration
@Import(InterfaceParserConfig.class)
public class NanoRestClientConfig  {

	@Autowired
	private RemoteServiceEndpoint[] endPoints;

	@Autowired
	InterfaceParserSource interfaceParserSource;

	@Bean
	public NanoRestClientFactory nanoRestClientFactory()
	{
		NanoRestClientFactory nanoRestClientFactory = new NanoRestClientFactory();
		nanoRestClientFactory.setDirectory(defaultServiceDirectory());
		return nanoRestClientFactory;
	}

	@Bean
	public DefaultServiceDirectory defaultServiceDirectory()
	{
		DefaultServiceDirectory defaultServiceDirectory = new DefaultServiceDirectory();
		defaultServiceDirectory.addSource(endPoints);
		return defaultServiceDirectory;
	}
}
