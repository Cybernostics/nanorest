package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.ResponseBody;

@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
public @interface EntityRestService {

	String value() default "";

}

