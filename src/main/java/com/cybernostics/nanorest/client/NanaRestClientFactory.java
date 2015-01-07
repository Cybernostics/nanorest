package com.cybernostics.nanorest.client;

import java.lang.reflect.Proxy;
import java.net.URL;

public class NanaRestClientFactory {

	public Object getClientFor(Class<?> interfaceClass, URL[] sources) {
		RestClientInvocationHandler restClientInvocationHandler = new RestClientInvocationHandler(
				sources);
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass }, restClientInvocationHandler);

	}

}
