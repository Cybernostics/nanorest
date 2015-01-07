package com.cybernostics.nanorest.servicelocator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultServiceEndpoint implements RemoteServiceEndpoint {

	private Class<?> serviceClass;
	private List<URL> urls;

	public DefaultServiceEndpoint(Class<?> interfaceClass, List<URL> urls) {
		super();
		this.serviceClass = interfaceClass;
		this.urls = urls;
	}

	public DefaultServiceEndpoint(Class<?> interfaceClass, String ... urls)
	{
		this.serviceClass = interfaceClass;
		this.urls = new ArrayList<>();
		for (String url : urls) {
			try {
				this.urls.add(new URL(url));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}

	public DefaultServiceEndpoint(Class<?> interfaceClass, URL url) {
		super();
		this.serviceClass = interfaceClass;
		this.urls = new ArrayList<URL>();
		this.urls.add(url);
	}


	public Class<?> getServiceClass() {
		return serviceClass;
	}

	@Override
	public List<URL> getURLS() {
		return this.urls;
	}

	@Override
	public void add(URL remoteServiceEndpoint) {
		this.urls.add(remoteServiceEndpoint);

	}

	@Override
	public String getServiceName() {
		return this.serviceClass.getName();
	}

}
