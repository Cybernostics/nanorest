package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultRequestMapper implements  RequestSpecificationMapper{

	@Autowired
	private InterfaceParser[] interfaceParsers;

	public InterfaceParser getForClass(Class<?>clazz) {
		for (InterfaceParser interfaceParser : interfaceParsers) {
			if (interfaceParser.canParse(clazz)) {
				return interfaceParser;
			}
		}
		return null;
	}

	private Map<Class<?>, Map<Method, RequestSpecification>> specMap = new HashMap<>();

	private Map<Method, RequestSpecification> parse(Class<?> clazz) {
		Map<Method, RequestSpecification> specs = specMap.get(clazz);
		if (specs==null) {
			for (InterfaceParser interfaceParser : interfaceParsers) {
				if (interfaceParser==this) {
					continue;
				}
				if (interfaceParser.canParse(clazz)) {
					specs = interfaceParser.parse(clazz);
					specMap.put(clazz, specs);
					break;
				}
			}

		}
		return specs;
	}


	@Override
	public RequestSpecification getForMethod(Method method,Class<?> declaringClass) {
		Map<Method, RequestSpecification> parsedMap = parse(declaringClass);
			return parsedMap!=null?parsedMap.get(method):null;
	}

	@Override
	public boolean applicableTo(Class<?> clazz) {
		for (InterfaceParser interfaceParser : interfaceParsers) {
			if (interfaceParser==this) {
				continue;
			}
			if (interfaceParser.canParse(clazz)) {
				return true;
			}
		}
		return false;	}

}
