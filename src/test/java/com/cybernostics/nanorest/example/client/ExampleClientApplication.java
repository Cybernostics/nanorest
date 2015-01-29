package com.cybernostics.nanorest.example.client;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.cybernostics.nanorest.client.NanoRestClientFactory;
import com.cybernostics.nanorest.example.api.v1.Greeting;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;


@ComponentScan
@EnableAutoConfiguration
public class ExampleClientApplication {

    public static void main(String[] args) {
    	System.setProperty("server.port","8500");
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleClientApplication.class, args);

        NanoRestClientFactory nanoRestClientFactory = applicationContext.getBean(NanoRestClientFactory.class);
        GreetingsService greetingsService = (GreetingsService) nanoRestClientFactory.getClientFor(GreetingsService.class);
        greetingsService.putGreeting(new Greeting("hello", "Description" ));
        greetingsService.putGreeting(new Greeting("hello1", "Description1" ));
        greetingsService.putGreeting(new Greeting("hello2", "Description2" ));
        List<Greeting> greetings = greetingsService.getGreetings();
		for (Greeting eachGreeting : greetings) {
			System.out.println(String.format(eachGreeting.getContent()+eachGreeting.getDescription()));
		}
    }
}
