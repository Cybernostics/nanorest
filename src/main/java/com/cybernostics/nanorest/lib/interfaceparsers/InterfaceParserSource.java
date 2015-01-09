package com.cybernostics.nanorest.lib.interfaceparsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterfaceParserSource {

	@Autowired
	private InterfaceParser[] interfaceParsers;

	public InterfaceParser getForClass(Class<?>clazz) {
		for (InterfaceParser interfaceParser : interfaceParsers) {
			if (interfaceParser.applicableTo(clazz)) {
				return interfaceParser;
			}
		}
		return null;
	}

}
