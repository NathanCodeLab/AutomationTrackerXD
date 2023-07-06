package com.framework.selenium;

public enum Browser {

	
	Chrome("Chrome", 0, "chrome"), Firefox("Firefox", 1, "firefox"), Edge("Edge", 2, "edge"),
	InternetExplorer("InternetExplorer", 3, "internet explorer"), Safari("Safari", 4, "safari"),
	HtmlUnit("HtmlUnit", 5, "htmlUnit"), Opera("Opera", 6, "opera");

	private String value;
	
	private Browser(final String name, final int ordinal, final String value) {
		this.value = value;
	}
	public String getValue() {
		return this.value;
	}
}
