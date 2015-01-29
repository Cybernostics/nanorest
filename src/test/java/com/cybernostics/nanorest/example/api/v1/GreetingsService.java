package com.cybernostics.nanorest.example.api.v1;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.cybernostics.nanorest.lib.interfaceparsers.EntityRestService;

@EntityRestService("/greeter/api/v1") //
public interface GreetingsService {

	Greeting putGreeting(@Named("toPut")Greeting toPut);

	Greeting postGreeting(@Named("toPost")Greeting toPost);

	Greeting getGreeting(@Named("id")long id);

	List<Greeting> getGreetings();

	Boolean deleteGreeting(@Named("id")long id);

	List<Greeting> findGreetingsByContent(@Named("content")String content);

	List<Greeting> findGreetingsByDescription(@Named("description")String description);

	List<Greeting> findGreetings(@Named("criteria")Map<String, String> criteria);
}
