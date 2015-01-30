package com.cybernostics.nanorest.client;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceEndpointDirectory;

/**
 * NanoRestClientFactory creates a local proxy class to access
 * nanoRest Service controllers
 * @author Jason Wraxall (jason@cybernostics.com.au)
 */
public class NanoRestClientFactory {

	/**
	 * Provides a lookup of available services.
	 */
	private ServiceEndpointDirectory serviceDirectory;

	private RequestSpecificationMapper mapper;

	private CallableHttpService httpService;


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
			RemoteServiceEndpoint endpoint = serviceDirectory.getEndpoint(interfaceClass);
			restClientInvocationHandler = new RestClientInvocationHandler(endpoint,
					mapper, httpService);
			handlers.put(interfaceClass,restClientInvocationHandler);
		}

		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass }, restClientInvocationHandler);
	}

	public void setHttpService(CallableHttpService httpService) {
		this.httpService = httpService;
	}

	public void setDirectory(ServiceEndpointDirectory defaultServiceDirectory) {
		this.serviceDirectory = defaultServiceDirectory;

	}

}
