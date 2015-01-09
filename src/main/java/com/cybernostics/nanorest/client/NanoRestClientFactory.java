package com.cybernostics.nanorest.client;

import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.cybernostics.nanorest.servicelocator.DefaultServiceDirectory;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

public class NanoRestClientFactory {

	private ServiceDirectory serviceDirectory;

	private Map<Class<?>, RestClientInvocationHandler> handlers = new HashMap<>();

	public Object getClientFor(Class<?> interfaceClass) {
		RestClientInvocationHandler restClientInvocationHandler = null;
		if( handlers.containsKey( interfaceClass ))
		{
			restClientInvocationHandler = handlers.get(interfaceClass);

		}else {
			restClientInvocationHandler = new RestClientInvocationHandler(
					serviceDirectory.getService(interfaceClass));
		}

		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass }, restClientInvocationHandler);

	}

	public void setDirectory(ServiceDirectory defaultServiceDirectory) {
		this.serviceDirectory = defaultServiceDirectory;

		// TODO Auto-generated method stub

	}

}
