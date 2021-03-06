package com.cybernostics.nanorest.test.client;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.example.client.ClientAppConfig;
import com.cybernostics.nanorest.lib.interfaceparsers.DefaultServiceInterfaceRequestMapper;
import com.cybernostics.nanorest.lib.interfaceparsers.InterfaceParser;
import com.cybernostics.nanorest.lib.interfaceparsers.RequestSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes= {ClientAppConfig.class})

public class NanoRestClientTest {

	@Autowired
	private DefaultServiceInterfaceRequestMapper interfaceParserSource;

	@Test
	public void shouldSupplyInterfaceParsers() {
		assertThat(interfaceParserSource, is(notNullValue()));
		assertThat(interfaceParserSource.getForClass(Long.class), is(nullValue()));
		InterfaceParser parser = interfaceParserSource.getForClass(GreetingsService.class);
		assertThat(parser, is(notNullValue()));
		Map<Method, RequestSpecification> methodMap = parser.parse(GreetingsService.class);
		assertThat(methodMap, is(notNullValue()));
		assertThat(methodMap.size(), is(not(0)));
		assertThat(methodMap.size(), is(GreetingsService.class.getDeclaredMethods().length));
	}
}
