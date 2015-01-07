package com.cybernostics.nanorest.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.cybernostics.nanorest.server.resolvers.NanaRestResolverConfig;

@Configuration
@ComponentScan
public class NanaRestServerConfig extends WebMvcConfigurationSupport {

	@Autowired
	private NanaRestResolverConfig resolverConfig;

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = new RestControllerHandlerMapping();
		return handlerMapping;
	}

	@Override
	protected void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(resolverConfig.getArgumentResolvers());
		super.addArgumentResolvers(argumentResolvers);
	}

	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter requestMappingHandlerAdapter = super
				.requestMappingHandlerAdapter();
		requestMappingHandlerAdapter.getCustomArgumentResolvers();
		return requestMappingHandlerAdapter;
	}



}
