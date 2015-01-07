package apitest.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import example.api.v1.Greeting;

public final class MatchesGreeting extends BaseMatcher<Greeting> {
	private Greeting toCompare;

	public MatchesGreeting(Greeting toPut) {
		this.toCompare = toPut;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof Greeting) {
			Greeting greeting = (Greeting) item;
			return greeting.getContent().equals(toCompare.getContent())&&
					greeting.getDescription().equals(toCompare.getDescription());
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("match greeting without id");
	}

	public static MatchesGreeting matches(Greeting toMatch) {
		return new MatchesGreeting(toMatch);
	}

}