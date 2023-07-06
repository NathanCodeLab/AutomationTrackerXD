package com.framework.support;

public enum OnError {

	NextIteration("NextIteration", 0), NextTestcase("NextTestcase", 1), Stop("Stop", 2);

	private OnError(final String name, final int ordinal) {
	}

}
