package com.framework.support;

import java.io.File;
import java.util.Properties;

public class TimeStamp {

	private static volatile String m_reportPathwithTimeStamp;

	private TimeStamp() {
	}

	public static String getInstance() {
		if (TimeStamp.m_reportPathwithTimeStamp == null) {
			synchronized (TimeStamp.class) {
				if (TimeStamp.m_reportPathwithTimeStamp == null) {
					final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
					if (frameworkParameters.getReleativePath() == null) {
						throw new FrameworkException("FrameworkParameters.relativePath is not set!");
					}
					if (frameworkParameters.getRunConfiguration() == null) {
						throw new FrameworkException("FrameworkParameters.runConfiguration is not set!");

					}
					final Properties properties = Settings.getInstance();
					final String timeStamp = "Run_" + Util.getCurrentFormattedTime(
							properties.getProperty("DateFormatString").replace(" ", "_").replace(":", "_"));
					TimeStamp.m_reportPathwithTimeStamp = String
							.valueOf(frameworkParameters.getReleativePath() + Util.getFileSeparator()) + "Results"
							+ Util.getFileSeparator() + frameworkParameters.getRunConfiguration()
							+ Util.getFileSeparator() + timeStamp;
					new File(TimeStamp.m_reportPathwithTimeStamp).mkdirs();
				}
			}
		}
		return TimeStamp.m_reportPathwithTimeStamp;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
