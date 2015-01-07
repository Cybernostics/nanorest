package apitest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cybernostics.nanorest.Inflector;

public class RegularPluralsTest {

	@Test
	public void checkToSingular() {

		assertThat(Inflector.toSingular("People"), is("Person"));
		assertThat(Inflector.toSingular("Men"), is("Man"));
		assertThat(Inflector.toSingular("Greeters"), is("Greeter"));


	}

}
