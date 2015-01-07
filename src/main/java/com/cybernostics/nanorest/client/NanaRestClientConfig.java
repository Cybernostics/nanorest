package com.cybernostics.nanorest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cybernostics.nanorest.servicelocator.DefaultServiceDirectory;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

@Configuration
public class NanaRestClientConfig  {

	@Autowired
	private RemoteServiceEndpoint[] endPoints;

//	@Bean
//	public NanaRestAPIHandler nanaRestAPIHandler() {
//		return new NanaRestAPIHandler();
//	}

	@Bean
	public DefaultServiceDirectory defaultServiceDirectory()
	{
		DefaultServiceDirectory defaultServiceDirectory = new DefaultServiceDirectory();
		defaultServiceDirectory.addSource(endPoints);
		return defaultServiceDirectory;
	}
}
