package com.cybernostics.nanorest.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.cybernostics.nanorest.Inflector;
import com.cybernostics.nanorest.spring.NanaRestAPI;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class RestControllerHandlerMapping  extends RequestMappingHandlerMapping{

	private static class NanaData{
		private Class<?> controllerInterface;
		private String baseEntityName;
		private String rootURL;
		public NanaData(Class<?> controllerInterface, String baeNameString,String rootURL) {
			super();
			this.controllerInterface = controllerInterface;
			this.baseEntityName = baeNameString;
			this.rootURL = rootURL;
		}
		public Class<?> getControllerInterface() {
			return controllerInterface;
		}
		public String getBaseNameString() {
			return baseEntityName;
		}
		public String getRootURL() {
			return rootURL;
		}


	}
	Map<Class<?>, NanaData > nanaControllers = new HashMap<>();

	@Override
	protected boolean isHandler(Class<?> beanType) {

		Class<?>[] interfaces = beanType.getInterfaces();
		for (Class<?> eachInterface : interfaces) {
			NanaRestAPI annotation = eachInterface.getAnnotation(NanaRestAPI.class);
			if (annotation!=null) {
				String baseNameString = baseNameFor(beanType);
				if (baseNameString != null) {
					nanaControllers.put(beanType, new NanaData(eachInterface, baseNameString,annotation.value()));
					return true;
				}
			}
		}
		return super.isHandler(beanType);
	}

	private String baseNameFor(Class<?> beanType) {
		String name = beanType.getSimpleName();
		if(!name.endsWith("Controller")) {
			logger.warn("Controller annotated with NanoRestController must end with 'Controller' ... ingoring");
			return null;
		}
		return name.replaceAll("Controller$", "");
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> controllerType) {
		RequestMappingInfo mappingInfo = super.getMappingForMethod(method, controllerType);
		if(mappingInfo==null)
		{
			if (nanaControllers.containsKey(controllerType)) {
				NanaData nanaData = nanaControllers.get(controllerType);

				mappingInfo = getCOCMappingForMethod(method, nanaData);
			}
		}

		return mappingInfo;
	}

	private final static String [] JSON_MIME = {"application/json"};

	private RequestMappingInfo getCOCMappingForMethod(Method method, NanaData nanaData) {
		RequestMappingInfo mappingInfo;
		String baseMapping = String.format("%s/%s/", nanaData.getRootURL(), nanaData.getBaseNameString().toLowerCase());
		StringBuffer mappingURL = new StringBuffer(baseMapping);
		String methodName = method.getName();
		RequestMethodsRequestCondition requestCondition = new RequestMethodsRequestCondition(RequestMethod.GET);
		ParamsRequestCondition paramsRequestCondition = new ParamsRequestCondition();
		ConsumesRequestCondition consumesRequestCondition = new ConsumesRequestCondition();

		String restEntitySingularName = Inflector.toSingular(nanaData.getBaseNameString());

		Class<?> controllerInterface = nanaData.getControllerInterface();

		// Only map interface methods annotated by NanaRestController
		try {
			controllerInterface.getDeclaredMethod(methodName, method.getParameterTypes());

		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}

		if(methodName.contains("Exception")) {
			return null;
		}
		if (methodName.equals("get"+nanaData.getBaseNameString())) {
			if(method.getParameterTypes().length==0) {

			}else if(method.getParameterTypes().length==1){
				mappingURL.append("{id}");
			}
		}
		if (methodName.equals("get"+restEntitySingularName)) {
			if(method.getParameterTypes().length==1){
				mappingURL.append("{id}");
			}
		}
		if (methodName.equals("put"+restEntitySingularName)) {
			requestCondition = new RequestMethodsRequestCondition(RequestMethod.PUT);
		}
		if (methodName.equals("post"+restEntitySingularName)) {
			requestCondition = new RequestMethodsRequestCondition(RequestMethod.POST);

		}
		if (methodName.equals("delete"+restEntitySingularName)) {
			requestCondition = new RequestMethodsRequestCondition(RequestMethod.DELETE);
			mappingURL.append("{id}");
		}
		if(methodName.startsWith("find"+nanaData.getBaseNameString()))
		{
			String[] parameterNames = getParameterNames(method);
			paramsRequestCondition = new  ParamsRequestCondition(parameterNames);

		}


		String[] patternStrings = {mappingURL.toString()};
		String[] patterns = resolveEmbeddedValuesInPatterns(patternStrings);
		PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
				useRegisteredSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions());
		HeadersRequestCondition headersRequestCondition = new HeadersRequestCondition();
		String [] headersStrings = {};

		ProducesRequestCondition producesRequestCondition = new ProducesRequestCondition(
				JSON_MIME,
				headersStrings,
				getContentNegotiationManager());
		mappingInfo = new RequestMappingInfo(
						patternsRequestCondition,
						requestCondition,
				paramsRequestCondition,
				headersRequestCondition,
				consumesRequestCondition,
				producesRequestCondition,
				null);
		return mappingInfo;
	}

	private String[] getParameterNames(Method method) {
		Paranamer p = new AdaptiveParanamer();
		String[] parameterNames = p.lookupParameterNames(method);
		return parameterNames;
	}





}
