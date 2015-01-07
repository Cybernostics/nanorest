package example.server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class ExampleServerApplication {

    public static void main(String[] args) {
    	System.setProperty("server.port", "8090");
        SpringApplication.run(ExampleServerApplication.class, args);
    }
}
