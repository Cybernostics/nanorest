package com.cybernostics.nanorest.lib.interfaceparsers;

import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;

public class HttpRequest extends HttpEntity<Object>{
	/**
	 * This is the path of the request URL relative to the root for a given service endpoint.
	 * eg /invoice/{id} which could be appended to endpoint http://localhost:8080
	 */
	private String requestPath;

	public HttpRequest(String requestPath, Object body, MultiValueMap<String, String> headers) {
		super(body, headers);
		this.requestPath = requestPath;
	}


}
