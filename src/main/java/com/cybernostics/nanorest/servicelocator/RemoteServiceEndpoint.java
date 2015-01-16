package com.cybernostics.nanorest.servicelocator;

import java.net.URL;
import java.util.List;


public interface RemoteServiceEndpoint {
	String getServiceName();
	List<URL> getURLS();
	Class<?> getServiceClass();
	void add(URL remoteServiceEndpoint);
}
