package com.cybernostics.nanorest.client;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

public class NanoRestClientFactory {

	private ServiceDirectory serviceDirectory;

	private RequestSpecificationMapper mapper;

	public RequestSpecificationMapper getMapper() {
		return mapper;
	}

	public void setMapper(RequestSpecificationMapper mapper) {
		this.mapper = mapper;
	}

	private Map<Class<?>, RestClientInvocationHandler> handlers = new HashMap<>();

	public Object getClientFor(Class<?> interfaceClass) {
		RestClientInvocationHandler restClientInvocationHandler = null;
		if( handlers.containsKey( interfaceClass ))
		{
			restClientInvocationHandler = handlers.get(interfaceClass);

		}else {
			RemoteServiceEndpoint endpoint = serviceDirectory.getService(interfaceClass);
			restClientInvocationHandler = new RestClientInvocationHandler(endpoint,
					mapper);
			handlers.put(interfaceClass,restClientInvocationHandler);
		}

		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass }, restClientInvocationHandler);

	}

	public void setDirectory(ServiceDirectory defaultServiceDirectory) {
		this.serviceDirectory = defaultServiceDirectory;

	}

}
