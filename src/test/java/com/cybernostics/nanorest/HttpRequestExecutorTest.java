package com.cybernostics.nanorest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.net.URL;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.omg.CosNaming.NamingContextExtPackage.URLStringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

	public static class NOPRestTemplate extends RestTemplate{
		public String urlstring = null;
		public HttpMethod method = null;

		@Override
		protected <T> T doExecute(URI url, HttpMethod method,
				RequestCallback requestCallback,
				ResponseExtractor<T> responseExtractor)
				throws RestClientException {
			urlstring = url.toASCIIString();
			this.method = method;
			return null;
		}
	};

	@Test
	public void test() {
		NOPRestTemplate restTemplate = new NOPRestTemplate();
		RequestSpecification requestSpecification = new RequestSpecification()
		.appendURL("/sample")
		.withHttpRequestMethod(HttpMethod.GET)
		;

		RemoteServiceEndpoint service = directory.getService(GreetingsService.class);
		assertThat(service, Is.is(IsNot.not(IsNull.nullValue())));
		HttpRequestExecutor httpRequestExecutor = new HttpRequestExecutor();
		httpRequestExecutor.doRequest(service,restTemplate, requestSpecification, new Object[0]);
		assertThat(restTemplate.urlstring, Is.is("http://localhost:8090/sample"));
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
