package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.RequestMethod;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.Paranamer;

import dagger.Lazy;

public class InterfaceParserUtil {
	private InterfaceParserUtil(){

	}

	public static Pattern requestMethodPattern = Pattern.compile("^(get|put|post|delete)");
	public static Pattern getDeleteMethodPattern = Pattern.compile("^(get|delete)");
	private static Lazy<Paranamer> paranamer = new Lazy<Paranamer>() {

		@Override
		public Paranamer get() {
			return new AdaptiveParanamer();
		}
	};

	public static RequestMethod fromMethod(Method m)
	{
		String name = m.getName();
		Matcher matcher = requestMethodPattern.matcher(name);
		if (matcher.find()) {
			return RequestMethod.valueOf(matcher.group(1).toUpperCase());
		}
		return RequestMethod.GET;
	}
	public static Class<?> getEntity(Class<?> serviceClass) {
		Method[] declaredMethods = serviceClass.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().startsWith("put")) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length==1) {
					return  parameterTypes[0];
				}else {
					throw new IllegalStateException(String.format("Method %s.put() should take a single argument which is the entity managed by the service",serviceClass.getCanonicalName()));
				}
			}
		}
		throw new IllegalStateException(String.format("Method %s.put() the service",serviceClass.getCanonicalName()));
	}
	static String getPathComponents(Method method) {
		if(getDeleteMethodPattern.matcher(method.getName()).find()) {
			if (method.getParameterTypes().length==1) {
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
