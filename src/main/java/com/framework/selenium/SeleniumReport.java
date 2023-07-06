package com.framework.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.framework.support.FrameworkException;
import com.framework.support.Report;
import com.framework.support.ReportSettings;
import com.framework.support.ReportTheme;

public class SeleniumReport extends Report {
	private WebDriver driver;

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public SeleniumReport(final ReportSettings reportSettings, final ReportTheme reportTheme) {
		super(reportSettings, reportTheme);
	}

	protected void takeScreenshot(final String screenshotPath) {

		if (this.driver == null) {
			throw new FrameworkException("Report.driver is not initialized!.");
		}
		if (this.driver.getClass().getSimpleName().equals("HtmlUnitDriver") || this.driver.getClass()
				.getGenericInterfaces().toString().equals("class org.openqa.selenium.htmlunit.HtmlUnitDriver")) {
			return;
		}
		File srcFile;
		if (this.driver.getClass().getSimpleName().equals("RemoteWebDriver")) {
			final Capabilities capabilities = ((RemoteWebDriver) this.driver).getCapabilities();
			if (capabilities.getBrowserName().equals("htmlunit")) {
				return;
			}
			WebDriver augmentedDriver = new Augmenter().augment(this.driver);
			srcFile = (File) ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);

		} else {
			srcFile = (File) ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		}
		try {
			FileUtils.copyFile(srcFile, new File(screenshotPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screeshot to file...");
		}

	}
}
