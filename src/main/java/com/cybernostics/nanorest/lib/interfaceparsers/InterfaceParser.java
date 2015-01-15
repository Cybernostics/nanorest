package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * The InterfaceParser understands certain types conventions of API.
 *
 * Examples  might be:
 * i) Simple entity (called MyEntityService with getMyEntity,putMyentity, postMyEntity etc)
 * ii) Source of truth ( MyEntitySourceOfTruth with as above with getAllSince
 * iii) Nested entity (to allow paths like /myentities/{id}/beanProperty/{arg1})
 *
 * Each InterfaceParser knows about some conventional layout for the methods
 * so it can figure out the stuff which would normally require annotations, like request method.
 * To Add a new Parser simply register a Bean implementing this interface.
 * The parser knows what types of interface it can parse( applicableTo) and
 * returns a RequestSpecification for each method in the interface that it cares about.
 * @author jason
 *
 */
public interface InterfaceParser {
	Map<Method,RequestSpecification> parse(Class<?> clazz);
	boolean canParse(Class<?> clazz);
}
