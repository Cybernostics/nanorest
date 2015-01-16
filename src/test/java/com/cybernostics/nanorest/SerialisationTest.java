package com.cybernostics.nanorest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.cybernostics.nanorest.example.api.v1.Greeting;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SerialisationTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Greeting toPut = mapper.readValue("{\"content\":\"hello\"}", Greeting.class);
		assertThat(toPut.getContent(), Is.is("hello"));

	}

	@Test
	public void schema() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.generateJsonSchema(Greeting.class));
	}
}
