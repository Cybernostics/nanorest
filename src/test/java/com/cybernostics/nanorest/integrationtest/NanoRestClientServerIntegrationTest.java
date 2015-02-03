package com.cybernostics.nanorest.integrationtest;

import static com.cybernostics.nanorest.test.MatchesGreeting.matches;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.cybernostics.nanorest.client.NanoRestClientFactory;
import com.cybernostics.nanorest.example.api.v1.Greeting;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.example.client.ClientAppConfig;
import com.cybernostics.nanorest.example.server.ServerAppConfig;
import com.cybernostics.nanorest.servicelocator.DefaultServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ClientAppConfig.class,
		NanoRestClientServerIntegrationTest.TestConfig.class })
public class NanoRestClientServerIntegrationTest {

	private static EmbeddedTomcatServer embeddedTomactServer;

	@Autowired
	NanoRestClientFactory nanoRestClientFactory;

	@BeforeClass
	public static void setupServer() {

		embeddedTomactServer = new EmbeddedTomcatServer(8090,ServerAppConfig.class);
		embeddedTomactServer.startServer();
		embeddedTomactServer.awaitServerStart();
	}

	@AfterClass
	public static void allDone() {
		embeddedTomactServer.stopServer();
	}

	@Test
	public void shouldWireInClientFactory() {
		assertThat(nanoRestClientFactory, is(notNullValue()));
	}

	@Test
	public void shouldReturnServiceFromFactory() {
		GreetingsService clientGreetingsService = (GreetingsService) nanoRestClientFactory
				.getClientFor(GreetingsService.class);
		assertThat(clientGreetingsService, is(notNullValue()));
	}

	@Test
	public void shouldProxyMethodCallsToService() throws Exception {

		// Create a magic proxy-based client in one line...
		GreetingsService clientGreetingsService = (GreetingsService) nanoRestClientFactory
				.getClientFor(GreetingsService.class);
		assertThat(clientGreetingsService, is(notNullValue()));

		// Use a put method to create anew resource
		Greeting newGreeting = clientGreetingsService.putGreeting(new Greeting(
				"content", "description"));
		assertThat(newGreeting.getId(), is(notNullValue()));

		Greeting updatedGreeting = clientGreetingsService.postGreeting(new Greeting(
				newGreeting.getId(),
				"updatedcontent",
				"description"));

		assertThat(updatedGreeting.getId(), is(newGreeting.getId()));
		assertThat(updatedGreeting.getContent(), is("updatedcontent"));

		clientGreetingsService
				.putGreeting(new Greeting("hello", "Description"));
		clientGreetingsService.putGreeting(new Greeting("hello1",
				"Description1"));
		clientGreetingsService.putGreeting(new Greeting("hello2",
				"Description2"));

		List<Greeting> greetings = clientGreetingsService.getGreetings();
		for (Greeting eachGreeting : greetings) {
			System.out.println(String.format(
					"Id: %d Content:%s Description: %s", eachGreeting.getId(),
					eachGreeting.getContent(), eachGreeting.getDescription()));
		}

		Map<String,String> criteria = new HashMap<>();
		List<Greeting> foundGreetings = clientGreetingsService.findGreetings(criteria);
		assertThat(foundGreetings.size(), is(notNullValue()));

		criteria.put("content", "updated*");
		foundGreetings = clientGreetingsService.findGreetings(criteria);
		assertThat(foundGreetings.size(), is(1));
		Greeting foundGreeting = foundGreetings.get(0);
		assertThat(foundGreeting, matches(updatedGreeting));
	}

	@Configuration
	public static class TestConfig {
		@Bean
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
			return new MappingJackson2HttpMessageConverter();
		}

		@Bean
		public RemoteServiceEndpoint greetingEndPoint()
		{
			return new DefaultServiceEndpoint(GreetingsService.class, embeddedTomactServer.getRootURL() );
		}
	}
}
