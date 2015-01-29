package com.cybernostics.nanorest.example.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.client.NanoRestClientConfig;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.lib.interfaceparsers.BasicEntityServiceParser;
import com.cybernostics.nanorest.servicelocator.DefaultServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

@Configuration
@ComponentScan( basePackageClasses= {GreetingsService.class})
@Import(NanoRestClientConfig.class)
public class ClientAppConfiguration {



	@Bean
	BasicEntityServiceParser basicEntityServiceParser() {
		return new BasicEntityServiceParser();
	}

	@Bean
	public RemoteServiceEndpoint greetingEndPoint()
	{
		return new DefaultServiceEndpoint(GreetingsService.class, "http://localhost:8090");
	}
//	@Bean
//	public static PropertyPlaceholderConfigurer properties(){
//	   PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
//	   ppc.setLocation(new ClassPathResource( "test.properties" ));
//	   ppc.setIgnoreUnresolvablePlaceholders( true );
//	   return ppc;
//	}
}
;