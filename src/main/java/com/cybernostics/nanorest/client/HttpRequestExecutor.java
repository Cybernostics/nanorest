package com.cybernostics.nanorest.client;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.cybernostics.nanorest.lib.interfaceparsers.HttpRequest;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

@Component
public class HttpRequestExecutor {

	@Autowired
	List<HttpMessageConverter<?>> converters;

	@Autowired
	ServiceDirectory serviceDirectory;

	@Autowired
	static Logger log;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Executes a request on a list of servers in turn until one request succeeds.
	 *
	 * @param requestSpecification
	 * @param argsObjects
	 * @return
	 */
	public ResponseEntity<byte[]> doRequest(RequestSpecification requestSpecification,
			Object... argsObjects) {
		RemoteServiceEndpoint service = serviceDirectory
				.getService(requestSpecification.getServiceClass());
		UriTemplate uriTemplate = requestSpecification.getQueryTemplate();
		List<Object> variableValues = requestSpecification.arrangeArgsForURITemplate(uriTemplate, argsObjects);

		List<URL> urls = service.getURLS();
		for (URL url : urls) {
			try {
				String URI = url.toURI().toASCIIString();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				HttpEntity<String> entity = new HttpEntity<String>(
						headers);

				String queryURL = uriTemplate.expand(variableValues).toASCIIString();
				return restTemplate.exchange(
						URI+queryURL,
						requestSpecification.getHttpRequestMethod(),
						entity,
						byte[].class, variableValues);
			} catch (URISyntaxException e) {
				log.warn(e);
			} catch (HttpClientErrorException e) {
				log.warn(e);
			}
		}
		log.error("Service list exhausted without response.");
		return null;
	}

}
