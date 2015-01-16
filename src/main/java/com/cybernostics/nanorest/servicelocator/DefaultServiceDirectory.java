package com.cybernostics.nanorest.servicelocator;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DefaultServiceDirectory implements ServiceDirectory {

	private Map<String,RemoteServiceEndpoint > serviceMap = new HashMap<String, RemoteServiceEndpoint>();

	@Override
	public RemoteServiceEndpoint getService(Class<?> serviceAPI) {
		return serviceMap.get(serviceAPI.getCanonicalName());
	}

	public void registerService(Class<?> api, RemoteServiceEndpoint sources)
	{
		serviceMap.put(api.getCanonicalName(), sources);
	}

	public void addSource(Class<?> api, URL remoteServiceEndpoint)
	{
		serviceMap.get( api.getCanonicalName()).add(remoteServiceEndpoint);
	}

	public void addSource(Class<?> api, RemoteServiceEndpoint remoteServiceEndpoint)
	{
		serviceMap.put( api.getCanonicalName(),remoteServiceEndpoint);
	}

	public void addSource(RemoteServiceEndpoint... endPoints) {
		for (RemoteServiceEndpoint remoteServiceEndpoint : endPoints) {
			addSource(remoteServiceEndpoint.getServiceClass(), remoteServiceEndpoint);
		}


	}

}
