package com.framework.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.framework.support.FrameworkException;
import com.framework.support.Settings;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverFactory {

	private static Properties properties;

	private WebDriverFactory() {
	}

	public static WebDriver getDriver(final Browser browser) {
		WebDriver driver = null;
		WebDriverFactory.properties = Settings.getInstance();
		final boolean proxyRequired = Boolean.parseBoolean(WebDriverFactory.properties.getProperty("ProxyRequired"));
		switch (browser) {
		case Chrome:
			ChromeOptions options = new ChromeOptions();
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case Firefox:
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		case Edge:
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		case InternetExplorer:
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			break;
		case Safari:
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
			break;
		case Opera:
			WebDriverManager.operadriver().setup();
			driver = new OperaDriver();
			break;
		case HtmlUnit:
			break;

		default:
			throw new FrameworkException("Unhandled Browser!");
		}
		return driver;
	}

	private static DesiredCapabilities getProxyCapabilities() {
		final Proxy proxy = new Proxy();
		proxy.setProxyType(Proxy.ProxyType.MANUAL);
		WebDriverFactory.properties = Settings.getInstance();
		final String proxyUrl = String.valueOf(WebDriverFactory.properties.getProperty("ProxyHost")) + ":"
				+ WebDriverFactory.properties.getProperty("ProxyPort");
		proxy.setHttpProxy(proxyUrl);
		proxy.setFtpProxy(proxyUrl);
		proxy.setSslProxy(proxyUrl);
		final DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("proxy", (Object) proxy);
		return desiredCapabilities;
	}

	public static WebDriver getDriver(final Browser browser, final String remoteUrl) {
		return getDriver(browser, null, null, remoteUrl);
	}

	public static WebDriver getDriver(Browser browser, final String browserVersion, final Platform platform,
			String remoteUrl) {
		WebDriverFactory.properties = Settings.getInstance();
		final boolean proxyRequired = Boolean.parseBoolean(WebDriverFactory.properties.getProperty("ProxyRequired"));
		DesiredCapabilities desiredCapabilities = null;
		if (browser.equals(Browser.HtmlUnit) || browser.equals(Browser.Opera) && proxyRequired) {
			desiredCapabilities = getProxyCapabilities();
		}
		desiredCapabilities.setBrowserName(browser.getValue());
		if (browserVersion != null) {
			desiredCapabilities.setVersion(browserVersion);

		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}
		desiredCapabilities.setJavascriptEnabled(true);
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is Malformed");
		}

		return (WebDriver) new RemoteWebDriver(url, (Capabilities)desiredCapabilities);
	}
}
