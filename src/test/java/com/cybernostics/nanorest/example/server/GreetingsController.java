package com.cybernostics.nanorest.example.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cybernostics.nanorest.example.api.v1.Greeting;
import com.cybernostics.nanorest.example.api.v1.GreetingsService;
import com.cybernostics.nanorest.example.server.finders.ByFieldFinder;
import com.cybernostics.nanorest.example.server.finders.ByMapFinder;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Component
/**
 * Notice - there are no @Request, @RequestParam etc etc annotations.
 * They are inferred from the interface which this controller implements
 * @author jason
 *
 */
public class GreetingsController implements GreetingsService {

	private final AtomicLong counter = new AtomicLong();

	private Map<Long, Greeting> greetingsMap = new TreeMap<Long, Greeting>();

	@ExceptionHandler(Throwable.class)
	public Boolean handleCustomException(Throwable ex) {

		ex.printStackTrace();
		System.out.println();
		return false;
	}

	@Override
	public Greeting putGreeting(Greeting toPut) {
		Greeting freshGreeting = new Greeting(counter.incrementAndGet(),
				toPut.getContent(), toPut.getDescription());
		greetingsMap.put(freshGreeting.getId(), freshGreeting);
		return freshGreeting;
	}

	@Override
	public Greeting postGreeting(Greeting toPost) {
		greetingsMap.put(toPost.getId(), toPost);
		return toPost;
	}

	@Override
	public Greeting getGreeting(long id) {
		Greeting fetchedGreeting = greetingsMap.get(id);
		if (fetchedGreeting != null) {
			return fetchedGreeting;
		}
		return putGreeting(new Greeting(0, "unknown", "unknown"));
	}

	@Override
	public List<Greeting> getGreetings() {
		return new ArrayList<>(greetingsMap.values());
	}

	@Override
	public Boolean deleteGreeting(long id) {
		Greeting deletedGreeting = greetingsMap.remove(id);
		return deletedGreeting != null;
	}

	@Override
	public List<Greeting> findGreetingsByContent(String content) {
		return doFind(new ByFieldFinder<Greeting>("content", content,
				Greeting.class));
	}

	@Override
	public List<Greeting> findGreetingsByDescription(String description) {
		return doFind(new ByFieldFinder<Greeting>("description", description,
				Greeting.class));
	}

	@Override
	public List<Greeting> findGreetings(Map<String, String> criteria) {
		return doFind(new ByMapFinder<Greeting>(criteria, Greeting.class));
	}

	private List<Greeting> doFind(Predicate<Greeting> criteria) {
		return new ArrayList<>(Collections2.filter(greetingsMap.values(),
				criteria));
	}

	public void reset() {
		greetingsMap.clear();

	}

}