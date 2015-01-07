package com.cybernostics.nanorest.servicelocator;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DefaultServiceDirectory implements ServiceDirectory {

	private Map<String,RemoteServiceEndpoint > serviceMap = new HashMap<String, RemoteServiceEndpoint>();

	@Override
	public RemoteServiceEndpoint getService(Class<?> serviceAPI) {
		return serviceMap.get(serviceAPI);
	}

	public void registerService(Class<?> api, RemoteServiceEndpoint sources)
	{
		serviceMap.put(api.getName(), sources);
	}

	public void addSource(Class<?> api, URL remoteServiceEndpoint)
	{
		serviceMap.get( api).add(remoteServiceEndpoint);
	}

	public void addSource(Class<?> api, RemoteServiceEndpoint remoteServiceEndpoint)
	{
		serviceMap.put( api.getName(),remoteServiceEndpoint);
	}

	public void addSource(RemoteServiceEndpoint... endPoints) {
		for (RemoteServiceEndpoint remoteServiceEndpoint : endPoints) {
			addSource(remoteServiceEndpoint.getClass(), remoteServiceEndpoint);
		}


	}

}
