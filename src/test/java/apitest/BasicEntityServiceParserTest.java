package apitest;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.lib.interfaceparsers.BasicEntityServiceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;



//@RunWith(SpringJUnit4ClassRunner.class)
//@EnableWebMvc
//@WebAppConfiguration
//@ContextConfiguration(classes= {ClientAppConfiguration.class})
public class BasicEntityServiceParserTest {

	private InterfaceParser parser;

	private Map<Method, RequestSpecification> methodMap;

	@Before
	public void init()
	{
		parser = new BasicEntityServiceParser();
		methodMap = parser.parse(GreetingsService.class);
		assertThat(methodMap, is(notNullValue()));
		assertThat(methodMap.size(), is(not(0)));
		assertThat(methodMap.size(), is(GreetingsService.class.getDeclaredMethods().length));
	}

	@Test
	public void shouldMapGetByIdMethod() throws Exception {
		Method getByIdMethod = methodWith("getGreeting",1);
		assertThat(methodMap,hasKey(getByIdMethod));
		RequestSpecification requestSpecification = methodMap.get(getByIdMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(), is(RequestMethod.GET));
		assertThat(requestSpecification.getQueryURL(), is("/Greeting/{id}"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(-1));
	}

	@Test
	public void shouldMapPutMethod() throws Exception {
		Method putMethod = methodWith("putGreeting",1);
		assertThat(methodMap,hasKey(putMethod));
		RequestSpecification requestSpecification = methodMap.get(putMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(), is(RequestMethod.PUT));
		assertThat(requestSpecification.getQueryURL(), is("/Greeting"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(1));
	}

	@Test
	public void shouldMapPostMethod() throws Exception {
		Method postMethod = methodWith("postGreeting",1);
		assertThat(methodMap,hasKey(postMethod));
		RequestSpecification requestSpecification = methodMap.get(postMethod);
		assertThat(requestSpecification, is(notNullValue()));
		assertThat(requestSpecification.getHttpRequestMethod(), is(RequestMethod.POST));
		assertThat(requestSpecification.getQueryURL(), is("/Greeting"));
		assertThat(requestSpecification.getHttpRequestParams(), is(empty()));
		assertThat(requestSpecification.getBodyIndex(), is(1));
	}

	private Method methodWith(String string, int i) {
		Method[] declaredMethods = GreetingsService.class.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().equals(string)&&(method.getParameterTypes().length==i)) {
				return method;
			}
		}
		return null;
	}

}
