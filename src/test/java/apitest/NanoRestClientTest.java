package apitest;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import example.api.v1.GreetingsService;
import example.client.ClientAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes= {ClientAppConfiguration.class})

public class NanoRestClientTest {

	@Autowired
	private GreetingsService greetingsService;

	@Test
	public void test() {
		assertThat(greetingsService, Is.is(IsNull.notNullValue()));
	}

}
