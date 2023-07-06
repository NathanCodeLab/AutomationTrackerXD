package com.framework.support;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.exception.CloneFailedException;

public class Settings {

	private static Properties properties;

	static {
		Settings.properties = loadFromPropertiesFile();
	}

	private Settings() {
	}
	public static Properties getInstance() {
		return Settings.properties;
	}

	private static Properties loadFromPropertiesFile() {

		final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		if (frameworkParameters.getReleativePath() == null) {
			throw new FrameworkException("FrameworkParameter.relativePath is not set");
		}
		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(String.valueOf(frameworkParameters.getReleativePath())
					+ Util.getFileSeparator() + "GlobalSettings.properties"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("FileNotFoundException While loading GlobalSetting Property File");
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new FrameworkException("IOException while loading the GlobalSetting Property File");
		}
		return properties;
	}
	public Object  clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
}
