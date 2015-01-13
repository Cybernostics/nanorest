package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cybernostics.nanorest.Inflector;

import feign.RequestTemplate;

@Component
public class BasicEntityServiceParser  implements InterfaceParser{

	@Override
	public Map<Method, RequestSpecification> parse(Class<?> serviceClass) {
		Class<?> entity = InterfaceParserUtil.getEntity(serviceClass);
		EntityRestService entityRestService =  serviceClass.getAnnotation(EntityRestService.class);
		String pluralNameString = Inflector.toPlural(entity.getSimpleName());
		Method[] declaredMethods = serviceClass.getDeclaredMethods();
		Map<Method, RequestSpecification> map = new HashMap<>();
		for (Method method : declaredMethods) {
			String name = method.getName();
			RequestSpecification methodRequestTemplate = new RequestSpecification()
				.withHttpRequestMethod(InterfaceParserUtil.fromMethod(method))
				.withBodyIndex(-1)
				.appendURL(entityRestService.value()+"/"+pluralNameString + InterfaceParserUtil.getPathComponents(method))
				.forJavaMethod(method);
			if(name.startsWith("put")||name.startsWith("post"))
			{
				methodRequestTemplate.withBodyIndex(1);
			}
			map.put(method, methodRequestTemplate);
		}
		return map;
	}

	@Override
	public boolean applicableTo(Class<?> clazz) {
		return clazz.getAnnotation(EntityRestService.class)!=null;

	}

}
