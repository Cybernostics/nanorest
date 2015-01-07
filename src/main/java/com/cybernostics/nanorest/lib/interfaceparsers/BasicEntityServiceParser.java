package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.cybernostics.nanorest.Inflector;

import feign.RequestTemplate;

public class BasicEntityServiceParser  implements InterfaceParser{

	@Override
	public Map<Method, RequestSpecification> parse(Class<?> serviceClass) {
		Class<?> entity = InterfaceParserUtil.getEntity(serviceClass);
		String pluralNameString = Inflector.toPlural(entity.getName());
		Method[] declaredMethods = serviceClass.getDeclaredMethods();
		Map<Method, RequestSpecification> map = new HashMap<>();
		for (Method method : declaredMethods) {
			RequestSpecification methodRequestTemplate = new RequestSpecification()
				.withHttpRequestMethod(InterfaceParserUtil.fromMethod(method))
				.appendURL("/"+pluralNameString + InterfaceParserUtil.getPathComponents(method))
				.forJavaMethod(method);
			map.put(method, methodRequestTemplate);
		}
		return map;
	}

	@Override
	public boolean applicableTo(Class<?> clazz) {
		return clazz.getAnnotation(BasicEntityService.class)!=null;

	}

}
