package com.cybernostics.nanorest.servicelocator;


public interface ServiceDirectory {
	RemoteServiceEndpoint getService(Class<?> serviceAPI);
}
