package com.cybernostics.nanorest.example.api.v1;

import java.util.List;
import java.util.Map;

import com.cybernostics.nanorest.lib.interfaceparsers.BasicEntityService;


@BasicEntityService("greeter/api/v1") //
public interface GreetingsService {

	Greeting putGreeting(Greeting toPut);

	Greeting postGreeting(Greeting toPost);

	Greeting getGreeting(long id);

	List<Greeting> getGreetings();

	Boolean deleteGreeting(long id);

	List<Greeting> findGreetingsByContent(String content);

	List<Greeting> findGreetingsByDescription(String description);

	List<Greeting> findGreetings(Map<String, String> criteria);
}
