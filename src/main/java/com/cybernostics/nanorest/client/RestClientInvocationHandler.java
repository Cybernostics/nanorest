package com.cybernostics.nanorest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;

import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

public class RestClientInvocationHandler implements InvocationHandler{

	private RemoteServiceEndpoint remoteServiceEndpoint;

	public RestClientInvocationHandler(RemoteServiceEndpoint remoteServiceEndpoint) {
		this.remoteServiceEndpoint = remoteServiceEndpoint;

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
