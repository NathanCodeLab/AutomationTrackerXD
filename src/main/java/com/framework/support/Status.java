package com.framework.support;

public enum Status {

	FAIL("FAIL", 0), WARNING("WARNING", 1), PASS("PASS", 2), SCREENSHOT("SCREENSHOT", 3), DONE("DONE", 4),
	DEBUG("DEBUG", 5);

	private Status(final String name, final int ordinal) {

	}

}
