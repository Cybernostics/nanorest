package example.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.client.NanaRestClientConfig;
import com.cybernostics.nanorest.servicelocator.DefaultServiceEndpoint;
import com.cybernostics.nanorest.servicelocator.RemoteServiceEndpoint;

import example.api.v1.GreetingsService;

@Configuration
@ComponentScan( basePackageClasses= {GreetingsService.class})
@Import(NanaRestClientConfig.class)
public class ClientAppConfiguration {

	@Bean
	public RemoteServiceEndpoint greetingEndPoint()
	{
		return new DefaultServiceEndpoint(GreetingsService.class, "http://localhost:8090");
	}

	@Bean
	GreetingsService greetingsService()
	{
		return null;
	}

}
;