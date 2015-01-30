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

import com.cybernostics.nanorest.lib.Lazy;
import com.thoughtworks.paranamer.Paranamer;

/**
 * The RequestSpecification is shared by clients and server controllers
 * to provide a common description of a rest endpoint.
 * It describes the url, the object returned and what parameters
 * are required for the call. This is typically generated by a RequestSpecificationMapper
 * to remove the need for all the normal annotations like @RequestParam, @PathVariable etc
 */
public class RequestSpecification {

	public static final int NO_BODY = -1;

	private Method javaServiceMethod;

	/**
	 * The interface method to which this specification applies
	 */
	public Method getJavaServiceMethod() {
		return javaServiceMethod;
	}

	/**
	 * Returns a list of the parameter names for the query.
	 * (Typically from a method argument list)
	 */
	public List<String> getHttpRequestParams() {
		List<String> variableNames = getQueryTemplate().getVariableNames();
		for (String eachArgument : javaMethodArguments) {
			if (!variableNames.contains(eachArgument)) {
				if(bodyIndex==NO_BODY) {
					httpRequestParams.add(eachArgument);
				}
			}
		}
		return httpRequestParams;
	}

	/**
	 * Convenience method to return the arguments for the method
	 */
	public List<String> getJavaMethodArguments() {
		return javaMethodArguments;
	}

	private StringBuilder urlBuilder = new StringBuilder();
	private HttpMethod httpRequestMethod;
	private List<String> httpRequestParams = new ArrayList<>();
	private List<String> javaMethodArguments = new ArrayList<String>();
	private int bodyIndex=NO_BODY;
	private Class<?> entityClass;
	private Class<?> serviceClass;
	private Map<String, Integer> argNames = new HashMap<>();
	public HttpMethod getHttpRequestMethod() {
		return httpRequestMethod;
	}

	/**
	 * Returns the HTTP method (GET,PUT,POST etc) for this request
	 */
	public RequestMethod getHttpServerRequestMethod() {
		return RequestMethod.valueOf(httpRequestMethod.toString());
	}

	static Lazy<Paranamer> paranamer = InterfaceParserUtil.paranamer;

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
				String temp = argNameList[index];
				argNames.put(temp, index);
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

	/**
	 * Takes a list of arguments and matches them in order
	 * with the parameter names for the method.
	 */
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

	/***
	 * Returns the URITemplate which can be used to build the query string
	 * for the request.
	 */
	public UriTemplate getQueryTemplate() {
		return new UriTemplate(urlBuilder.toString());
	}

	/**
	 * Returns the index of the body parameter in the method call
	 * Used to replace the @RequestBody annotation.
	 */
	public int getBodyIndex() {
		return bodyIndex;
	}

	public RequestSpecification withBodyIndex(int index) {
		bodyIndex = index;
		return this;

	}

	/***
	 * Returns the entity class managed by an interface. This will typically be
	 * the return XYZ.class type of the getXYZ() method.
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}

	/**
	 * Returns the service class which declares the method for this request.
	 */
	public Class<?> getServiceClass() {
		return serviceClass;
	}

	public Object getBodyFromArgs(Object[] argsObjects) {
		if (bodyIndex >= argsObjects.length) {
			throw new IllegalArgumentException(
					"Cannot get body arg as there aren't enough arguments");
		}
		if (bodyIndex != NO_BODY) {
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

	public Class<?> getReturnClass() {
		return this.javaServiceMethod.getReturnType();

	}

	private int getArgIndexFor(String argName) {
		if(argNames.containsKey(argName))
		{
			return argNames.get(argName);
		}
		throw new IllegalArgumentException( "Unknown method argument:" + argName );
	}

}
