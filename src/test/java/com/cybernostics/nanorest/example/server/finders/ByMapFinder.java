package com.cybernostics.nanorest.example.server.finders;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;


public class ByMapFinder<T> implements Predicate<T> {

	private Map<String, String> params;
	private Map<String, Method> getters;

	public ByMapFinder(Map<String, String> params, Class<T> clazz) {
		this.params = params;
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (String eachParam : params.keySet()) {
			boolean found = false;
			for (Method method : declaredMethods) {
				if(method.getName().equalsIgnoreCase("get"+eachParam))
				{
					getters.put(eachParam, method);
					found = true;
					break;
				}
			}
			if (found == false) {
				throw new IllegalStateException("Could not find getter for search term"+eachParam);
			}
		}
	}

	@Override
	public boolean apply(T arg0) {

		for (Entry<String, String> eachQueryParam : params.entrySet()) {
			try {
				String toMatch = eachQueryParam.getValue().replace("*", ".*");
				if(!getters.get(eachQueryParam.getKey()).invoke(arg0).toString().matches(toMatch)) {
					return false;
				}
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return true;
	}

}