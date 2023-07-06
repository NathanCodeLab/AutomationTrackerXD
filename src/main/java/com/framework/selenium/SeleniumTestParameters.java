package com.framework.selenium;

import org.openqa.selenium.Platform;

import com.framework.support.TestParameters;

public class SeleniumTestParameters extends TestParameters {

	private Browser browser;
	private String browserVersion;
	private Platform platform;

	public SeleniumTestParameters(final String currentScenario, final String currentTestCase) {
		super(currentScenario, currentTestCase);

	}

	public Browser getBrowser() {
		return this.browser;
	}

	public void setBrowser(final Browser browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return this.browserVersion;
	}

	public void setBrowserVersion(final String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public Platform getPlatform() {
		return this.platform;
	}

	public void setPlatform(final Platform platform) {
		this.platform = platform;
	}

}
