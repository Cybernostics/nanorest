package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;

public interface RequestSpecificationMapper {
	public RequestSpecification getForMethod(Method method,Class<?> source);
	boolean applicableTo(Class<?> clazz);

}
