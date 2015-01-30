# nanoRest making MicroServices simpler...
## A convention over configuration approach to rest client and server development

Lots of people are spending lots of time cooking up microservices and many of these services
talk to each other using some form of REST.

Spring has lots of annotations and templates to make things easier, but nanoRest is an attempt to
make things easier still and also more consistent:

 1. Make it easier to create consistent looking REST controllers requiring a minimum of annotations.
 2. Create clients automatically based on a shared interface used to create the controller.
 3. Ensure that the interface controls what both the client and server look like, resulting in
a lower burden on testing to ensure everything plays nicely together.
 4. Discourage people doing things with REST which the Lord never intended, like variants of RPC.
(Although the excellent [json-rpc](https://github.com/briandilley/jsonrpc4j) library helps create REST RPC services if you are inclined
in that direction)
 5. Make it easier to enforce patterns of interfaces.

How does it do all of these things?

Good Question. Glad to see you're interested. Sitting comfortably? Then here we go...

## Step 1 - Define an interface and DTO
The first step to defining a nanoRest Service is the interface for your API.
The key to nanoRest's simplicity is the conventions for your API.

Assume the resource you want to manage is  Greeting containing a name and descripton.

Your DTO class would look something like the class in
[/src/test/examples/api/v1/Greeting.java](https://github.com/Cybernostics/nanorest/blob/master/src/test/java/com/cybernostics/nanorest/example/api/v1/Greeting.java)

A nanoRest API for your Greeting would then define the following interface:

	@EntityRestService("/greeter/api/v1") //

	public interface GreetingsService {

	Greeting putGreeting(@Named("toPut")Greeting toPut);

	Greeting postGreeting(@Named("toPost")Greeting toPost);

	Greeting getGreeting(@Named("id")long id);

	List<Greeting> getGreetings();

	Boolean deleteGreeting(@Named("id")long id);

	}

The names of these methods matter. NanoRest looks at these to figure out what
queries to serve in the controller. That way, we avoid alot of stating the obvious
with a slew of annotations.

They cover the basic REST verbs GET,PUT,POST and DELETE.

The getGreeting method is assumed to take a path variable whose name is the name of the one parameter. Likewise, the delete method also takes an identifier in the URL path.

(If you are using Java 8 then you wont even need the @Named annotation, as the
parameter names are retained at runtime)

You can also create some finders. To create a finder with specific query params, you need
to pay attention to both the method name and the parameter name, which should correspond to
some attribute of your DTO.

	List<Greeting> findGreetingsByContent(@Named("content")String content);

	List<Greeting> findGreetingsByDescription(@Named("description")String description);

You can also

	List<Greeting> findGreetings(@Named("criteria")Map<String, String> criteria);


So what tells nanoRest this is anything special? The @EntityRestService annotation.
It takes an optional URL prefix which allows to add an arbitrary text to the start of the
URLs. In this case it is being used to version the api (as is the package path).

## Step 2 - Your REST controller
Before you charge off with a fist full of annotations for Path variables, request methods,
Query and body params... stop right there. You don't need them. Away put your weapons.

To implement a service, simply define a class which implements your API interface and is annotated
with component or defined as a @Bean.

Done.

Check out the [GreetingController](https://github.com/Cybernostics/nanorest/blob/master/src/test/java/com/cybernostics/nanorest/example/server/GreetingsController.java) if you don't believe me you skeptical types...

This example controller just uses in-memory maps to keep things simple. But you could use JPA, Hibernate or a wet sponge nanoRest makes no restrictions on how you manage your resource.

## Step 3 - Your REST client
So now you need to write your REST client to match all the endpoints, methods, args... OR NOT!

You defined your API in step 1. We let the computer figure out all the paths to expose
as REST services - surely it can also define the client to access those services? YES it can.

This time we simply ask a factory class to create a client for us and wire in a little
endpoint info as to where the service lives, and bingo client done!

See the [ExampleClientApplication](https://github.com/Cybernostics/nanorest/blob/master/src/test/java/com/cybernostics/nanorest/example/client/ExampleClientApplication.java) to see how to cook up
a client in under 10 lines of code. (The endpoint is wired using Spring configuration)

Nanorest also includes basic failover support in case your server is deplyed to multiple locations.
The Endpoint class has a list of URLs to try when accessing the service, but all that is hidden within the proxy client class. You just call your client method and get your data.
