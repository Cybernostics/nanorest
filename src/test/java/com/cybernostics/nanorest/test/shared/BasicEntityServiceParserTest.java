package com.cybernostics.nanorest.test.shared;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.lib.interfaceparsers.BasicEntityServiceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.EntityRestService;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;

public class BasicEntityServiceParserTest {

	private InterfaceParser parser;

	private Map<Method, RequestSpecification> methodMap;

	private String expectedRootPath;

	@Before
	public void init() {
		parser = new BasicEntityServiceParser();
		methodMap = parser.parse(GreetingsService.class);
		EntityRestService entityRestService = GreetingsService.class
				.getAnnotation(EntityRestService.class);
		assertThat(entityRestService, is(notNullValue()));
		expectedRootPath = entityRestService.value();
		assertThat(methodMap, is(notNullValue()));
		assertThat(methodMap.size(), is(not(0)));
		assertThat(methodMap.size(),
				is(GreetingsService.class.getDeclaredMethods().length));
	}

	@Test
	public void shouldMapGetByIdMethod() throws Exception {
		Method getByIdMethod = methodWith("getGreeting", 1);
		assertThat(methodMap, hasKey(getByIdMethod));
		RequestSpecification requestSpecification = methodMap
				.get(getByIdMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(),
				is(HttpMethod.GET));
		assertThat(requestSpecification.getQueryTemplate().expand(21)
				.toASCIIString(), is(expectedRootPath + "/greetings/21"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(-1));
	}

	@Test
	public void shouldMapPutMethod() throws Exception {
		Method putMethod = methodWith("putGreeting", 1);
		assertThat(methodMap, hasKey(putMethod));
		RequestSpecification requestSpecification = methodMap.get(putMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(),
				is(HttpMethod.PUT));
		assertThat(requestSpecification.getQueryTemplate().expand()
				.toASCIIString(), is(expectedRootPath + "/greetings"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(0));
	}

	@Test
	public void shouldMapPostMethod() throws Exception {
		Method postMethod = methodWith("postGreeting", 1);
		assertThat(methodMap, hasKey(postMethod));
		RequestSpecification requestSpecification = methodMap.get(postMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(),
				is(HttpMethod.POST));
		assertThat(requestSpecification.getQueryTemplate().expand()
				.toASCIIString(), is(expectedRootPath + "/greetings"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(0));
	}

	@Test
	public void shouldMapDeleteMethod() throws Exception {
		Method deleteMethod = methodWith("deleteGreeting", 1);
		assertThat(methodMap, hasKey(deleteMethod));
		RequestSpecification requestSpecification = methodMap.get(deleteMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(),
				is(HttpMethod.DELETE));
		assertThat(requestSpecification.getQueryTemplate().expand(21)
				.toASCIIString(), is(expectedRootPath + "/greetings/21"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(-1));
	}

	@Test
	public void shouldMapFinderMethods() throws Exception {

		checkFinder("content");
		checkFinder("description");
		checkFinder("");

	}

	private RequestSpecification checkFinder(String byArg) {
		String suffix = byArg.length() > 0 ? "By"
				+ byArg.substring(0, 1).toUpperCase() + byArg.substring(1) : "";
		Method findMethod = methodWith("findGreetings" + suffix, 1);
		assertThat(findMethod, is(notNullValue()));
		assertThat(methodMap, hasKey(findMethod));
		RequestSpecification requestSpecification = methodMap.get(findMethod);

		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(),
				is(HttpMethod.GET));
		assertThat(requestSpecification.getQueryTemplate().toString(),
				is(expectedRootPath + "/greetings"));
		if (byArg.length() > 0) {
			assertThat(requestSpecification.getHttpRequestParams(),
					containsInAnyOrder(byArg));
		}
		assertThat(requestSpecification.getBodyIndex(), is(-1));

		return requestSpecification;
	}

	private Method methodWith(String string, int i) {
		Method[] declaredMethods = GreetingsService.class.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().equals(string)
					&& (method.getParameterTypes().length == i)) {
				return method;
			}
		}
		return null;
	}

}
