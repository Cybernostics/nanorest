package apitest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.api.v1.Greeting;

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
