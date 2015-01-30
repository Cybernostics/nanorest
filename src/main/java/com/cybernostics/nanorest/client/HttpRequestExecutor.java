package com.cybernostics.nanorest.client;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriTemplate;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class HttpRequestExecutor {

	private ServiceDirectory serviceDirectory;

	static Logger log;

	/**
	 * Executes a request on a list of servers in turn until one request succeeds.
	 * @param service - the endpoint which has the list of urls for that service
	 * @param requestSpecification - describes the type of request to make
	 * @param argsObjects - arbitrary request-specific arguments
	 * @return a ResponseEntity with a byte array
	 */
	public ResponseEntity<byte[]> doRequest(
			RemoteServiceEndpoint service,
			CallableHttpService callableService,
			RequestSpecification requestSpecification,
			Object... argsObjects) {
		UriTemplate uriTemplate = requestSpecification.getQueryTemplate();
		List<Object> variableValues = requestSpecification.arrangeArgsForURITemplate(uriTemplate, argsObjects);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<byte[]> entity = null;
		if (requestSpecification.getBodyIndex()!=RequestSpecification.NO_BODY) {
			Object bodyObject = requestSpecification.getBodyFromArgs(argsObjects);
			ObjectMapper mapper = new ObjectMapper();
			try {
				headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				byte[] bodyAsBytes = mapper.writeValueAsBytes(bodyObject);
				entity = new HttpEntity<byte[]>(bodyAsBytes,
						headers);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}else{
			entity = new HttpEntity<byte[]>(
					headers);
		}
		// TODO if post and argsObjects[0] is a map then post the variables in mime encoded body...
		String queryURL = uriTemplate.expand(variableValues).toASCIIString();
		return callableService.call(service, queryURL, requestSpecification.getHttpRequestMethod(),
				entity);

	}

}
