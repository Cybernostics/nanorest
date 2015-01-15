package com.cybernostics.nanorest.lib.interfaceparsers;

import java.lang.reflect.Method;
import java.util.Comparator;

import dagger.Lazy;

public class MethodSignatureComparator implements Comparator<Method> {

	private static Lazy<MethodSignatureComparator> comparator = new Lazy<MethodSignatureComparator>() {

		@Override
		public MethodSignatureComparator get() {
			return new MethodSignatureComparator();
		}

	};
	public static MethodSignatureComparator get () {
		return comparator.get();
	}

	@Override
	public int compare(Method o1, Method o2) {
		int comparison = o1.getName().compareTo(o2.getName());
		if (comparison==0) {
			comparison = compareClassLists(o1.getParameterTypes(), o2.getParameterTypes());
		}
		return comparison;
	}

	private int compareClassLists(Class<?>[] o1Types, Class<?>[] o2Types) {
		int comparison;
		comparison = Integer.compare(o1Types.length, o2Types.length);
		if (comparison==0) {
			for (int i = 0; i < o2Types.length; i++) {
				Class<?> class1 = o2Types[i];
				comparison = class1.getCanonicalName().compareTo(o2Types[i].getCanonicalName());
				if (comparison!=0) {
					break;
				}
			}
		}
		return comparison;
	}

}
