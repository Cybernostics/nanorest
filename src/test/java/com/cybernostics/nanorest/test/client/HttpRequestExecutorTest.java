package com.cybernostics.nanorest.test.client;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cybernostics.nanorest.client.HttpRequestExecutor;
import com.cybernostics.nanorest.client.CallableHttpService;
import com.cybernostics.nanorest.client.NanoRestClientConfig;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.servicelocator.DefaultServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceEndpointDirectory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {HttpRequestExecutorTest.testConfig.class,NanoRestClientConfig.class})
public class HttpRequestExecutorTest {

	@Autowired
	ServiceEndpointDirectory directory;
	private MockCallableHttpService mockService;
	private RemoteServiceEndpoint endpoint;
	private HttpRequestExecutor httpRequestExecutor;

	public static class MockCallableHttpService extends CallableHttpService{
		public String urlstring = null;
		public HttpMethod method = null;
		public HttpEntity<byte[]> entity = null;
		@Override
		public ResponseEntity<byte[]> call(
				RemoteServiceEndpoint remoteServiceEndpoint,
				String resourcePath, HttpMethod method,
				HttpEntity<byte[]> requestEntity) {
			this.entity = requestEntity;
			this.urlstring = resourcePath;
			this.method=method;
			return null;
		}
	};

	@Before
	public void setup() {
		mockService = new MockCallableHttpService();
		endpoint = directory.getEndpoint(GreetingsService.class);
		assertThat(endpoint, Is.is(IsNot.not(IsNull.nullValue())));
		httpRequestExecutor = new HttpRequestExecutor();
	}

	@Test
	public void getXYZ() {
		RequestSpecification requestSpecification = new RequestSpecification()
		.appendURL("/sample")
		.withHttpRequestMethod(HttpMethod.GET)
		.withBodyIndex(-1);

		httpRequestExecutor.doRequest(endpoint,mockService, requestSpecification, new Object[0]);
		assertThat(mockService.urlstring, Is.is("/sample"));
		assertThat(mockService.method, Is.is(HttpMethod.GET));
	}


	@Configuration
	public static class testConfig{

		@Bean
		HttpMessageConverter<?> mappingJacksonHttpMessageConverter()
		{
			return new MappingJackson2HttpMessageConverter();
		}

		@Bean
		public RemoteServiceEndpoint greetingEndPoint()
		{
			return new DefaultServiceEndpoint(GreetingsService.class, "http://localhost:8090");
		}
	}

}
