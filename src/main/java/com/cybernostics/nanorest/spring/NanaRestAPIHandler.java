package com.cybernostics.nanorest.spring;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cybernostics.nanorest.lib.interfaceparsers.EntityRestService;
import com.google.common.base.Predicate;

public class NanaRestAPIHandler implements ApplicationContextAware,
		InitializingBean, BeanFactoryAware {
	private ApplicationContext applicationContext;
	private DefaultListableBeanFactory beanFactory;

	@Override
	public void afterPropertiesSet() throws Exception {


		final Map<String, Object> nanaClients = ((ListableBeanFactory) applicationContext)
				.getBeansWithAnnotation((Class<? extends Annotation>) EntityRestService.class);
		for (final Object myClient : nanaClients.values()) {
			final Class<? extends Object>clientClass = myClient.getClass();
			final EntityRestService annotation = clientClass
					.getAnnotation(EntityRestService.class);
			System.out.println(String.format("Found class %s\n",clientClass.getName() ));
		}

		Predicate<String> myClassAnnotationsFilter = new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return true;
			}
		};
		final Reflections reflections = new Reflections(
				           new ConfigurationBuilder()
				               .filterInputsBy(new FilterBuilder().include("example.api"))
				               .setUrls(ClasspathHelper.forClassLoader())
				               .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner().filterResultsBy(myClassAnnotationsFilter)));

		Set<Class<?>> theFoos = reflections.getTypesAnnotatedWith(EntityRestService.class);
		System.out.println(theFoos.size());
		for(Class<?> eachFoo : theFoos)
		{
			System.out.println(String.format("Found class %s\n",eachFoo.getName() ));
			;
		}
	}

	@Override
	public void setApplicationContext(
			org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;


	}

}