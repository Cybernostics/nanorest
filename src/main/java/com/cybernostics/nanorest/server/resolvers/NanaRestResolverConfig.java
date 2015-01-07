package com.cybernostics.nanorest.server.resolvers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;


@Component
public class NanaRestResolverConfig  implements BeanFactoryAware{

	@Autowired
	private HttpMessageConverter<?>[] messageConverters;

	private ConfigurableBeanFactory beanFactory;

	public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
		PostPutBodyArgumentResolver argumentResolver = new PostPutBodyArgumentResolver();
		argumentResolver.setMessageConverters(messageConverters);
		argumentResolvers.add(argumentResolver);
		argumentResolvers.add(new IdPathArgumentResolver());
		argumentResolvers.add(new FinderParamArgumentResolver(beanFactory, true));
		return argumentResolvers;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableBeanFactory) beanFactory;

	}


}
