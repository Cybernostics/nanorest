package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpMethod;

import com.cybernostics.nanorest.lib.Lazy;
import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.DefaultParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class InterfaceParserUtil {
	private InterfaceParserUtil() {

	}

	static final String[] EMPTY_NAMES = new String[0];

	public static Pattern requestMethodPattern = Pattern
			.compile("^(get|put|post|delete)");
	public static Pattern getDeleteMethodPattern = Pattern
			.compile("^(get|delete)");
	public static Lazy<Paranamer> paranamer = new Lazy<Paranamer>() {

		@Override
		public Paranamer create() {
			return new AdaptiveParanamer(new DefaultParanamer(), new BytecodeReadingParanamer(),new NamedAnnotationParanamer());
		}
	};

	public static class NamedAnnotationParanamer implements Paranamer {

		@Override
		public String[] lookupParameterNames(
				AccessibleObject methodOrConstructor) {
			String[] names = null;
			ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
			if (methodOrConstructor instanceof Method) {
				names = parameterNameDiscoverer.getParameterNames((Method) methodOrConstructor);
			}
			else {
				names = parameterNameDiscoverer.getParameterNames((Constructor<?>)methodOrConstructor);
			}
			if (names==null) {
				Annotation[][] parameterAnnotations = ((Method) methodOrConstructor).getParameterAnnotations();

				names = new String[parameterAnnotations.length];
				for (int index = 0; index < parameterAnnotations.length; index++) {
					Annotation[] annotations = parameterAnnotations[index];
					boolean found = false;
					for (Annotation annotation : annotations) {
						if (annotation instanceof Named ) {
							Named named = (Named) annotation;
							names[index]=named.value();
							found=true;
						}
					}
					if (!found) {
						names[index]="arg_"+index;
					}
				}
			}


			return names;
		}

		@Override
		public String[] lookupParameterNames(
				AccessibleObject methodOrConstructor,
				boolean throwExceptionIfMissing) {
			try {
				return lookupParameterNames(methodOrConstructor);
			} catch (Exception e) {
				if(throwExceptionIfMissing) {
					throw e;
				}
			}
			return EMPTY_NAMES;
		}

	}

	public static HttpMethod fromMethod(Method m) {
		String name = m.getName();
		Matcher matcher = requestMethodPattern.matcher(name);
		if (matcher.find()) {
			return HttpMethod.valueOf(matcher.group(1).toUpperCase());
		}
		return HttpMethod.GET;
	}

	public static Class<?> getEntity(Class<?> serviceClass) {
		Method[] declaredMethods = serviceClass.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().startsWith("put")) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length == 1) {
					return parameterTypes[0];
				} else {
					throw new IllegalStateException(
							String.format(
									"Method %s.put() should take a single argument which is the entity managed by the service",
									serviceClass.getCanonicalName()));
				}
			}
		}
		throw new IllegalStateException(String.format(
				"Method %s.put() the service", serviceClass.getCanonicalName()));
	}

	static String getPathComponents(Method method) {
		if (getDeleteMethodPattern.matcher(method.getName()).find()) {
			if (method.getParameterTypes().length == 1) {
				return "/{id}";
			}
		}
		return "";
	}

	public static String[] getParameterNames(Method method) {
		String[] parameterNames = paranamer.get().lookupParameterNames(method);
		return parameterNames;
	}

}
