package com.cybernostics.nanorest;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
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
import com.cybernostics.nanorest.example.client.ClientAppConfiguration;
import com.cybernostics.nanorest.example.server.ServerAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {
		ClientAppConfiguration.class,
		BasicEntityServiceProxyTest.TestConfig.class})
public class BasicEntityServiceProxyTest {

	 private static CountDownLatch cdl = new CountDownLatch(1);

	@Autowired
	NanoRestClientFactory nanoRestClientFactory;

	private static Tomcat tomcat;

	@BeforeClass
	public static void setupServer() {
		if ( tomcat == null ) {
	        tomcat = new Tomcat();
	        tomcat.setPort(8090);

	        File base = new File("");
	        Context rootCtx = tomcat.addContext("/", base.getAbsolutePath());
	        AnnotationConfigWebApplicationContext aactx = new AnnotationConfigWebApplicationContext();
	        aactx.register(ServerAppConfiguration.class);
	        DispatcherServlet dispatcher = new DispatcherServlet(aactx);
	        Tomcat.addServlet(rootCtx, "SpringMVC", dispatcher);
	        rootCtx.addServletMapping("/*", "SpringMVC");
	        tomcat.getServer().addLifecycleListener(new LifecycleListener() {

				@Override
				public void lifecycleEvent(LifecycleEvent event) {
					if (event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
						System.out.println("Started yay!");
						cdl.countDown();
					}
				}
			});

	        try {
				tomcat.start();
			} catch (LifecycleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        try {
				assertThat("Server Started correctly",cdl.await(30, TimeUnit.SECONDS), is(true));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@AfterClass
	public static void allDone() {
		try {
			tomcat.stop();
		} catch (LifecycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void shouldWireInClientFactory(){
		assertThat(nanoRestClientFactory,is(notNullValue()));
	}

	@Test
	public void shouldReturnServiceFromFactory(){
		GreetingsService clientGreetingsService = (GreetingsService) nanoRestClientFactory.getClientFor(GreetingsService.class);
		assertThat(clientGreetingsService,is(notNullValue()));
	}

	@Test
	public void shouldProxyMethodCallsToService() throws Exception {
		GreetingsService clientGreetingsService = (GreetingsService) nanoRestClientFactory.getClientFor(GreetingsService.class);
		assertThat(clientGreetingsService,is(notNullValue()));
		Greeting newGreeting = clientGreetingsService.putGreeting(new Greeting("content","description"));
		assertThat(newGreeting.getId(), is(notNullValue()));
		String output = newGreeting.toString();
		System.out.println(output);
		clientGreetingsService.putGreeting(new Greeting("hello", "Description" ));
		clientGreetingsService.putGreeting(new Greeting("hello1", "Description1" ));
		clientGreetingsService.putGreeting(new Greeting("hello2", "Description2" ));
        List<Greeting> greetings = clientGreetingsService.getGreetings();
		for (Greeting eachGreeting : greetings) {
			System.out.println(String.format("Id: %d Content:%s Description: %s",eachGreeting.getId(),eachGreeting.getContent(),eachGreeting.getDescription()));
		}

	}

	@Configuration
	public static class TestConfig
	{
		@Bean
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter()
		{
			return new MappingJackson2HttpMessageConverter();
		}

	}

}
