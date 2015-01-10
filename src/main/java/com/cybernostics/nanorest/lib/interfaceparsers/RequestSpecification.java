package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriTemplate;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;

import dagger.Lazy;

public class RequestSpecification {
	private Method javaServiceMethod;

	public Method getjavajavaServiceMethod() {
		return javaServiceMethod;
	}

	public List<String> getHttpRequestParams() {
		return httpRequestParams;
	}

	public List<String> getJavaMethodArguments() {
		return javaMethodArguments;
	}

	private StringBuilder urlBuilder = new StringBuilder();
	private HttpMethod httpRequestMethod;
	private List<String> httpRequestParams = new ArrayList<String>();
	private List<String> javaMethodArguments = new ArrayList<String>();
	private int bodyIndex;
	private Class<?> entityClass;
	private Class<?> serviceClass;
	private Map<String, Integer> argNames;

	public HttpMethod getHttpRequestMethod() {
		return httpRequestMethod;
	}

	static Lazy<Paranamer> paranamer = new Lazy<Paranamer>() {

		@Override
		public Paranamer get() {
			return new AdaptiveParanamer();
		}
	};

	public RequestSpecification() {

	}

	public RequestSpecification withHttpRequestMethod(
			HttpMethod requestMethod) {
		this.httpRequestMethod = requestMethod;
		return this;
	}

	public RequestSpecification appendURL(String toAppend) {
		this.urlBuilder.append(toAppend);
		return this;
	}

	public RequestSpecification forJavaMethod(Method method) {
		this.javaServiceMethod = method;
		if (javaServiceMethod.getParameterTypes().length > 0) {
			String[] argNameList = paranamer.get()
					.lookupParameterNames(javaServiceMethod, false);
			javaMethodArguments.addAll(Arrays.asList(argNameList));
			for (int index = 0; index < argNameList.length; index++) {
				argNames.put(argNameList[index], index);
			}
		}
		serviceClass = method.getDeclaringClass();
		entityClass = InterfaceParserUtil.getEntity(serviceClass);
		return this;
	}

	public RequestSpecification withRequestParams(String... names) {
		for (String string : names) {
			httpRequestParams.add(string);
		}
		return this;
	}

	public Map<String, String> getQueryParams(Object... args) {
		Map<String, String> map = new HashMap<String, String>();
		for (int index = 0; index < args.length; index++) {
			String paramName = javaMethodArguments.get(index);
			if (this.httpRequestParams.contains(paramName)) {
				map.put(paramName, args[index].toString());
			}
		}
		return map;
	}

	public UriTemplate getQueryTemplate() {
		return new UriTemplate(urlBuilder.toString());
	}

	public int getBodyIndex() {
		return bodyIndex;
	}

	public RequestSpecification withBodyIndex(int index) {
		bodyIndex = index;
		return this;

	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public Class<?> getServiceClass() {
		return serviceClass;
	}

	public Object getBodyFromArgs(Object[] argsObjects) {
		if (bodyIndex >= argsObjects.length) {
			throw new IllegalArgumentException(
					"Cannot get body arg as there aren't enough arguments");
		}
		if (bodyIndex >= 0) {
			return argsObjects[bodyIndex];
		}
		return null;
	}

	public List<Object> arrangeArgsForURITemplate(UriTemplate template,Object[] argsObjects) {
		List<Object> objList = new ArrayList<>();
		List<String> variableNames = template.getVariableNames();
		for (String variable : variableNames) {
			objList.add(argsObjects[getArgIndexFor(variable)]);
		}
		return objList;
	}

	private int getArgIndexFor(String argName) {
		if(argNames.containsKey(argName))
		{
			return argNames.get(argName);
		}
		throw new IllegalArgumentException("Unknown method argument:"+argName);
	}

}
