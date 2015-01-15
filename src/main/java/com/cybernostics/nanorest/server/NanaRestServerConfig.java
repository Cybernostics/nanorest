package com.cybernostics.nanorest.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParserConfig;
import com.cybernostics.nanorest.lib.interfaceparsers.DefaultRequestMapper;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;
import com.cybernostics.nanorest.server.resolvers.NanaRestResolverConfig;

@Configuration
@ComponentScan
@Import(InterfaceParserConfig.class)
public class NanaRestServerConfig extends WebMvcConfigurationSupport {

	@Autowired
	private NanaRestResolverConfig resolverConfig;

	@Autowired
	RequestSpecificationMapper interfaceParserSource;

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = new RestControllerHandlerMapping(interfaceParserSource);
		return handlerMapping;
	}

	@Override
	protected void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(resolverConfig.getArgumentResolvers());
		super.addArgumentResolvers(argumentResolvers);
	}


}
