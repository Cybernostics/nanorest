package com.cybernostics.nanorest.lib;

public abstract class Lazy<T> {

	private T ref;

	protected abstract T create();

	synchronized public T get() {
		if (ref==null) {
			ref = create();
		}
		return ref;
	}

}
