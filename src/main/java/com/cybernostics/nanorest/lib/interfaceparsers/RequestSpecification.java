package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;

import dagger.Lazy;

public class RequestSpecification{
	private Method javaServiceMethod;
	public Method getjavajavaServiceMethod() {
		return javaServiceMethod;
	}

	public List<String> getHttpRequestParams() {
		return httpRequestParams;
	}

	public String[] getJavaMethodArguments() {
		return javaMethodArguments;
	}

	private StringBuilder urlBuilder = new StringBuilder();
	private RequestMethod httpRequestMethod;
	private List<String> httpRequestParams = new ArrayList<String>();
	private String[] javaMethodArguments;

	public RequestMethod getHttpRequestMethod() {
		return httpRequestMethod;
	}

	static Lazy<Paranamer> paranamer = new Lazy<Paranamer>() {

		@Override
		public Paranamer get() {
			return new AdaptiveParanamer();
		}
	};
	public RequestSpecification(){

	}

	public RequestSpecification withHttpRequestMethod(RequestMethod requestMethod)
	{
		this.httpRequestMethod = requestMethod;
		return this;
	}

	public RequestSpecification appendURL(String toAppend)
	{
		this.urlBuilder.append(toAppend);
		return this;
	}

	public RequestSpecification forJavaMethod(Method method)
	{
		this.javaServiceMethod=method;
		paranamer.get().lookupParameterNames(javaServiceMethod);
		return this;
	}

	public RequestSpecification withRequestParams(String ...names)
	{
		for (String string : names) {
			httpRequestParams.add(string);
		}
		return this;
	}

	public Map<String, String> getQueryParams(Object...args)
	{
		Map<String, String> map = new HashMap<String, String>();
		for (int index = 0; index < args.length; index++) {
			String paramName = javaMethodArguments[index];
			if(this.httpRequestParams.contains(paramName))
			{
				map.put(paramName,args[index].toString());
			}
		}
		return map;
	}

	public String getQueryURL(Object...args)
	{
		String request = urlBuilder.toString();
		for (int index = 0; index < args.length; index++) {
			String currentTag = "{"+javaMethodArguments[index]+"}";
			if(request.contains(currentTag)) {
				request=request.replace(currentTag, args[index].toString());
			}
		}
		return request;
	}
}