package apitest;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cybernostics.nanorest.spring.NanaRestAPI;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.api.v1.Greeting;
import example.server.ServerAppConfiguration;
import static apitest.matchers.MatchesGreeting.matches;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes= {ServerAppConfiguration.class,NanoRestControllerTest.TestConfig.class})
public class NanoRestControllerTest {

	 @Autowired
	 private WebApplicationContext wac;

	 private ObjectMapper objectMapper = new ObjectMapper();

	 private MockMvc mockMvc;

	 @Before
	 public void setup() {
	  this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	 }
	@Test
	public void testAnnotation() {
		assertThat(Annotated.class.getAnnotation(NanaRestAPI.class), is(not(nullValue())));
	}

	@Test
	public void getWithNoArgsReturnsEmptyListWithNoData() {
		try {
			String contentAsString = mockMvc.perform(get("/greetings/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			assertThat(contentAsString, is("[]"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void putJSONFollowedByGetReturnsTheNewEntityWithANonzeroId() {
		try {
			Greeting toPut = new Greeting("Hello There", "A description");
			String jsonRequest = objectMapper.writeValueAsString(toPut);

			String contentAfterPut = mockMvc.perform(put("/greetings/")
					.content(jsonRequest)
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			Greeting afterPut = objectMapper.readValue(contentAfterPut, Greeting.class);
			assertThat(afterPut, matches(toPut));

			String contentAfterGet = mockMvc.perform(get("/greetings/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			List<?> afterGetList = objectMapper.readValue(contentAfterGet, ArrayList.class);
			assertThat(afterGetList.size(), is(1));
			Greeting greetingReturned = (Greeting) afterGetList.get(0);
			assertThat(greetingReturned.getId(), is(not(0L)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@NanaRestAPI
	public static class Annotated
	{

	}

	@Configuration
	public static class TestConfig
	{

		@Bean
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter()
		{
			return new MappingJackson2HttpMessageConverter();
		}
	}

}