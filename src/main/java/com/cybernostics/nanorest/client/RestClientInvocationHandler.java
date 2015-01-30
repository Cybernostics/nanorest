package com.cybernostics.nanorest.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecificationMapper;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientInvocationHandler implements InvocationHandler{

	private RemoteServiceEndpoint remoteServiceEndpoint;
	private RequestSpecificationMapper requestSpecMapper;
	private HttpRequestExecutor httpRequestExecutor;
	private ObjectMapper jsonmapper;
	private CallableHttpService httpService;

	public RestClientInvocationHandler(RemoteServiceEndpoint remoteServiceEndpoint,
			RequestSpecificationMapper mapper, CallableHttpService httpService) {
		this.remoteServiceEndpoint = remoteServiceEndpoint;
		this.requestSpecMapper = mapper;
		this.httpService = httpService;
		httpRequestExecutor = new HttpRequestExecutor();
		jsonmapper = new ObjectMapper();

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		RequestSpecification spec = requestSpecMapper.getForMethod(method, remoteServiceEndpoint.getServiceClass());
		ResponseEntity<byte[]> response = httpRequestExecutor.doRequest(remoteServiceEndpoint, httpService, spec, args);
		if(spec.getReturnClass()!=Void.class) {
			byte[] body = response.getBody();
			if (Collection.class.isAssignableFrom(method.getReturnType())) {
				List<Object> entities = new ArrayList<>();
				JsonNode node = jsonmapper.readTree(body);
				for (JsonNode jsonNode : node) {

//					String asText = jsonNode.toString();
//					System.out.println(asText);
					entities.add(jsonmapper.treeToValue(jsonNode, spec.getEntityClass()));
//					entities.add(jsonmapper.readValue(asText, ));
				}
				return entities;
			}
			return jsonmapper.readValue(body, spec.getReturnClass());
		}
		return null;
	}

}
