package com.cybernostics.nanorest.example.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.cybernostics.nanorest.server.NanaRestServerConfig;

@Configuration
@ComponentScan
@Import(NanaRestServerConfig.class)
public class ServerAppConfig {

	@Bean
	MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter()
	{
		return new MappingJackson2HttpMessageConverter();
	}

}
;