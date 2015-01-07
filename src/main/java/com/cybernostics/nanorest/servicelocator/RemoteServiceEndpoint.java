package com.cybernostics.nanorest.servicelocator;

import java.net.URL;
import java.util.List;


public interface RemoteServiceEndpoint {
	String getServiceName();
	List<URL> getURLS();
	void add(URL remoteServiceEndpoint);
}
