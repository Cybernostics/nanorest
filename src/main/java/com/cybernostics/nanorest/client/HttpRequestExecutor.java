package com.cybernostics.nanorest.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriTemplate;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceEndpointDirectory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestExecutor {

	private ServiceEndpointDirectory serviceDirectory;

	static Logger log;

	/**
	 * Executes a request on a list of servers in turn until one request
	 * succeeds.
	 *
	 * @param service
	 *            - the endpoint which has the list of urls for that service
	 * @param requestSpecification
	 *            - describes the type of request to make
	 * @param argsObjects
	 *            - arbitrary request-specific arguments
	 * @return a ResponseEntity with a byte array
	 */
	public ResponseEntity<byte[]> doRequest(RemoteServiceEndpoint service,
			CallableHttpService callableService,
			RequestSpecification requestSpecification, Object... argsObjects) {
		UriTemplate uriTemplate = requestSpecification.getQueryTemplate();
		List<Object> variableValues = requestSpecification
				.arrangeArgsForURITemplate(uriTemplate, argsObjects);
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		// TODO if post and argsObjects[0] is a map then post the variables in
		// mime encoded body...
		String queryURL = uriTemplate.expand(variableValues).toASCIIString();

		HttpEntity<byte[]> entity = null;
		if (requestSpecification.getBodyIndex() != RequestSpecification.NO_BODY) {
			Object bodyObject = requestSpecification
					.getBodyFromArgs(argsObjects);
			try {
				headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				byte[] bodyAsBytes = mapper.writeValueAsBytes(bodyObject);
				entity = new HttpEntity<byte[]>(bodyAsBytes, headers);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		} else {
			entity = new HttpEntity<byte[]>(headers);
			List<String> httpRequestParams = requestSpecification
					.getHttpRequestParams();
			List<String> requestClausesList = new ArrayList<>();

			if(requestSpecification.isFinder())
			{
				// adhoc query
				if (argsObjects != null && argsObjects.length == 1) {
					if (argsObjects[0] instanceof Map) {
						@SuppressWarnings("unchecked")
						Map<String, String> params = (Map<String, String>) argsObjects[0];
						for (Entry<String, String> eachEntry : params.entrySet()) {
							requestClausesList.add(String.format("%s=%s",
									eachEntry.getKey(), eachEntry.getValue()));
						}
					}
				} else if (httpRequestParams.size() > 0) {
					for (int i = 0; i < httpRequestParams.size(); i++) {
						try {
							requestClausesList.add(String.format("%s=%s",
									httpRequestParams.get(i),
									mapper.writeValueAsBytes(argsObjects[i])));
						} catch (JsonProcessingException e) {
							throw new RuntimeException(e);
						}
					}
				}
				if (requestClausesList.size() > 0) {
					queryURL = queryURL + "?"
							+ StringUtils.join(requestClausesList, "&");
				}

			}

		}
		return callableService.call(service, queryURL,
				requestSpecification.getHttpRequestMethod(), entity);

	}

}
