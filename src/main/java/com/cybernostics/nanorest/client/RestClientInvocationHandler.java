package com.cybernostics.nanorest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

public class RestClientInvocationHandler implements InvocationHandler{

	private RemoteServiceEndpoint remoteServiceEndpoint;
	private RequestSpecificationMapper requestSpecMapper;

	public RestClientInvocationHandler(RemoteServiceEndpoint remoteServiceEndpoint,
			RequestSpecificationMapper mapper) {
		this.remoteServiceEndpoint = remoteServiceEndpoint;
		this.requestSpecMapper = mapper;

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
