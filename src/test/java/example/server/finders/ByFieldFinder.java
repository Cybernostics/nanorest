package example.server.finders;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;

import example.api.v1.Greeting;

public class ByFieldFinder<T> implements Predicate<T> {

	private String valueTomatch;
	private Method getter;

	public ByFieldFinder(String fieldName, String valueTomatch, Class<T> clazz) {
		this.valueTomatch = valueTomatch;

		Method[] declaredMethods = clazz.getDeclaredMethods();
			boolean found = false;
			for (Method method : declaredMethods) {
				if(method.getName().equalsIgnoreCase("get"+fieldName))
				{
					this.getter =  method;
					found = true;
					break;
				}
			}
			if (found == false) {
				throw new IllegalStateException("Could not find getter for search term"+fieldName);
			}
	}

	@Override
	public boolean apply(T objectToTesT) {

			try {
				String toMatchPattern = this.valueTomatch.replace("*", ".*");
				if(!getter.invoke(objectToTesT).toString().matches(toMatchPattern)) {
					return false;
				}
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		return true;
	}

}