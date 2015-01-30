package com.cybernostics.nanorest.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

public class CallableHttpService {

	public ResponseEntity<byte[]> call( RemoteServiceEndpoint remoteServiceEndpoint, String resourcePath,HttpMethod method, HttpEntity<byte[]> requestEntity) {

		RestTemplate restTemplate = new RestTemplate();
		Deque<URL> urls = new ArrayDeque<>(remoteServiceEndpoint.getURLS());
		while (urls.size()>0) {
			URL eachUrl = urls.pop();
			try {
				return restTemplate.exchange(new  URI(eachUrl.toExternalForm()+resourcePath), method, requestEntity, byte[].class);
			} catch (RestClientException e) {
				throw new RuntimeException(e);
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		}
		return null;
	}

}
