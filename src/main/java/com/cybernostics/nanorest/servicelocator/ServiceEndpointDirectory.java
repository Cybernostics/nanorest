package com.cybernostics.nanorest.servicelocator;


public interface ServiceEndpointDirectory {
	RemoteServiceEndpoint getEndpoint(Class<?> serviceAPI);
}
