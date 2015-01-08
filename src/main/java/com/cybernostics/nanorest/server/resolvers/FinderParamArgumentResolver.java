package com.cybernostics.nanorest.server.resolvers;

import javax.servlet.ServletException;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import com.cybernostics.nanorest.lib.interfaceparsers.EntityRestService;

public class FinderParamArgumentResolver extends RequestParamMethodArgumentResolver{

	public FinderParamArgumentResolver(ConfigurableBeanFactory beanFactory,
			boolean useDefaultResolution) {
		super(beanFactory, useDefaultResolution);

	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (AnnotationUtils.findAnnotation(parameter.getDeclaringClass(), EntityRestService.class) == null)
		{
			return false;
		}
		String methodName = parameter.getMethod().getName();
		if(methodName.startsWith("find") )
		{
			return true;
		}
		return false;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		// TODO Auto-generated method stub
		return super.createNamedValueInfo(parameter);
	}

	@Override
	protected void handleMissingValue(String paramName,
			MethodParameter parameter) throws ServletException {
		// TODO Auto-generated method stub
		super.handleMissingValue(paramName, parameter);
	}

	public static class FinderParamNamedValueInfo extends NamedValueInfo
	{
		public FinderParamNamedValueInfo(MethodParameter parameter) {
			super(parameter.getParameterName(), false, null);
		}

	}


}
