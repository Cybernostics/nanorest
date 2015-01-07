package com.cybernostics.nanorest.server.resolvers;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import com.cybernostics.nanorest.spring.NanaRestAPI;


public class IdPathArgumentResolver extends
		PathVariableMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (AnnotationUtils.findAnnotation(parameter.getDeclaringClass(), NanaRestAPI.class) == null)
		{
			return false;
		}
		String methodName = parameter.getMethod().getName();
		if(methodName.startsWith("delete") ||
				(methodName.startsWith("get") && parameter.getMethod().getParameterTypes().length==1))
		{
			return true;
		}
		return false;

	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		return new IdPathVariableNamedValueInfo(parameter);
	}

	private static class IdPathVariableNamedValueInfo extends NamedValueInfo {

		public IdPathVariableNamedValueInfo(MethodParameter parameter) {
			super(parameter.getParameterName(), true, ValueConstants.DEFAULT_NONE);
		}
	}

}
