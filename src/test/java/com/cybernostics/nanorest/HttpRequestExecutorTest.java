package com.cybernostics.nanorest;

import static org.junit.Assert.assertThat;

import java.net.URI;

import javax.swing.text.html.parser.Entity;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
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
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cybernostics.nanorest.client.HttpRequestExecutor;
import com.cybernostics.nanorest.client.HttpService;
import com.cybernostics.nanorest.client.NanoRestClientConfig;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;
import com.cybernostics.nanorest.servicelocator.DefaultServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.ServiceDirectory;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ComponentScan
@ContextConfiguration( classes = {HttpRequestExecutorTest.testConfig.class,NanoRestClientConfig.class})
public class HttpRequestExecutorTest {

	@Autowired
	ServiceDirectory directory;

	public static class NOPHttpServiceTemplate extends HttpService{
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

	@Test
	public void test() {
		NOPHttpServiceTemplate restTemplate = new NOPHttpServiceTemplate();
		RequestSpecification requestSpecification = new RequestSpecification()
		.appendURL("/sample")
		.withHttpRequestMethod(HttpMethod.GET)
		.withBodyIndex(-1);

		RemoteServiceEndpoint service = directory.getService(GreetingsService.class);
		assertThat(service, Is.is(IsNot.not(IsNull.nullValue())));
		HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor();
		httpRequestExecutor.doRequest(service,restTemplate, requestSpecification, new Object[0]);
		assertThat(restTemplate.urlstring, Is.is("/sample"));
		assertThat(restTemplate.method, Is.is(HttpMethod.GET));

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
