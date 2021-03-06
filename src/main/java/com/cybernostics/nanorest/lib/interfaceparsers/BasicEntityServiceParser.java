package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import org.jvnet.inflector.Noun;
import org.springframework.stereotype.Component;

@Component
public class BasicEntityServiceParser implements InterfaceParser {

	@Override
	public Map<Method, RequestSpecification> parse(Class<?> serviceClass) {
		Class<?> interfaceSpecClass = getServiceClass(serviceClass);
		Class<?> entity = InterfaceParserUtil.getEntity(interfaceSpecClass);
		EntityRestService entityRestService = getServiceAnnotation(interfaceSpecClass);
		String pluralNameString = Noun.pluralOf(entity.getSimpleName()
				.toLowerCase());
		Method[] declaredMethods = interfaceSpecClass.getDeclaredMethods();
		Map<Method, RequestSpecification> methodMap = new TreeMap<>(
				MethodSignatureComparator.get());
		for (Method method : declaredMethods) {
			String name = method.getName();
			RequestSpecification methodSpecification = new RequestSpecification()
					.withHttpRequestMethod(
							InterfaceParserUtil.fromMethod(method))
					.withBodyIndex(-1)
					.appendURL(
							entityRestService.value()
									+ "/"
									+ pluralNameString
									+ InterfaceParserUtil
											.getPathComponents(method))
					.forJavaMethod(method)
					.setFinder(true);
			if (name.startsWith("put") || name.startsWith("post")) {
				methodSpecification.withBodyIndex(0);
			}
			if(name.startsWith("find"))
			{
				methodSpecification.setFinder(true);
			}
			methodMap.put(method, methodSpecification);
		}
		return methodMap;
	}

	private Class<?> getServiceClass(Class<?> serviceClass) {
		EntityRestService annotation = serviceClass
				.getAnnotation(EntityRestService.class);
		if (annotation != null) {
			return serviceClass;
		}
		Class<?>[] interfaces = serviceClass.getInterfaces();
		for (Class<?> eachInterface : interfaces) {
			annotation = eachInterface.getAnnotation(EntityRestService.class);
			if (annotation != null) {
				return eachInterface;
			}
		}
		return null;
	}

	private EntityRestService getServiceAnnotation(Class<?> serviceClass) {
		Class<?> interfaceClass = getServiceClass(serviceClass);
		return interfaceClass != null ? interfaceClass
				.getAnnotation(EntityRestService.class) : null;
	}

	@Override
	public boolean canParse(Class<?> clazz) {
		return getServiceAnnotation(clazz) != null;

	}

}
