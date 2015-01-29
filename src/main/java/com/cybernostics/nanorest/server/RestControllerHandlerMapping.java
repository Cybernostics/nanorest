package com.cybernostics.nanorest.server;

import java.lang.reflect.Method;

import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;

public class RestControllerHandlerMapping  extends RequestMappingHandlerMapping{


	private RequestSpecificationMapper interfaceRequestMapper;

	public RestControllerHandlerMapping(
			RequestSpecificationMapper requestMapper) {
				this.interfaceRequestMapper = requestMapper;

	}

	@Override
	protected boolean isHandler(Class<?> beanType) {

		if(interfaceRequestMapper.applicableTo(beanType)) {
			return true;
		}
		return super.isHandler(beanType);
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> controllerType) {
		RequestMappingInfo mappingInfo = super.getMappingForMethod(method, controllerType);
		if(mappingInfo==null)
		{
			RequestSpecification forMethod = interfaceRequestMapper.getForMethod(method, controllerType);
			return this.getFromRequestSpecification(forMethod);
		}

		return mappingInfo;
	}

	private final static String [] JSON_MIME = {"application/json"};

	private RequestMappingInfo getFromRequestSpecification(RequestSpecification requestSpecification) {
		if (requestSpecification==null) {
			return null;
		}
		RequestMethodsRequestCondition requestCondition = new RequestMethodsRequestCondition(requestSpecification.getHttpServerRequestMethod());
		ParamsRequestCondition paramsRequestCondition = new ParamsRequestCondition();
		String[] emptyStrings = new String[0];
		paramsRequestCondition = new  ParamsRequestCondition(requestSpecification.getHttpRequestParams().toArray(emptyStrings));
		ConsumesRequestCondition consumesRequestCondition = new ConsumesRequestCondition();
		HeadersRequestCondition headersRequestCondition = new HeadersRequestCondition();
		String [] headersStrings = {};

		String[] patternStrings = {requestSpecification.getQueryTemplate().toString()};
		String[] patterns = resolveEmbeddedValuesInPatterns(patternStrings);
		PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
				useRegisteredSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions());

		ProducesRequestCondition producesRequestCondition = new ProducesRequestCondition(
				JSON_MIME,
				headersStrings,
				getContentNegotiationManager());
		RequestMappingInfo mappingInfo = new RequestMappingInfo(
						patternsRequestCondition,
						requestCondition,
				paramsRequestCondition,
				headersRequestCondition,
				consumesRequestCondition,
				producesRequestCondition,
				null);
		return mappingInfo;
	}


}
