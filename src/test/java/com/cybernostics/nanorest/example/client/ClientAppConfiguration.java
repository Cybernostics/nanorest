package com.cybernostics.nanorest.example.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.client.NanoRestClientConfig;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.servicelocator.DefaultServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;



@Configuration
@ComponentScan( basePackageClasses= {GreetingsService.class})
@Import(NanoRestClientConfig.class)
public class ClientAppConfiguration {

	@Bean
	public RemoteServiceEndpoint greetingEndPoint()
	{
		return new DefaultServiceEndpoint(GreetingsService.class, "http://localhost:8090");
	}

	@Bean
	GreetingsService greetingsService()
	{
		return null;
	}

}
;