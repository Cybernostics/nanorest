package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;


/**
 * Provides a RequestSpecification for each method of an interface.
 * These can be used to generate controller mappings and client classes.
 *
 * @author Jason Wraxall (jason@cybernostics.com.au)
 */
public interface RequestSpecificationMapper {
	/**
	 * Gets the specification for a given interface description.
	 * Depending on the mapper it could use different rules to
	 * derive a specification. For example the DefaultServiceInterfaceMapper
	 * uses a combination of the method name and arguments to figure out
	 * the specification of the request.
	 */
	public RequestSpecification getForMethod(Method method,Class<?> source);

	/**
	 * Return true if this mapper is intended to provide mappings for the given
	 * interface class.
	 */
	boolean applicableTo(Class<?> clazz);

}
