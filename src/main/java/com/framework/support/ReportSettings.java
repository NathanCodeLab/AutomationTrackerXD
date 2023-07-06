package com.framework.support;

import java.io.File;

public class ReportSettings {

	private final String reportPath;
	private final String reportName;
	private String projectName;
	private int logLevel;
	public boolean generateExcelReport;
	public boolean generateHTMLReport;
	public boolean generateExtendsReport;
	public boolean takeScreenshotFailedStep;
	public boolean takeScreenshotPassedStep;
	public boolean linkScreenshotToTestLog;
	public boolean linkTestLogsToSummary;
	public boolean consoidateScreenshotsInWordDoc;
	private String dateFormatString;

	public String getReportPath() {
		return this.reportPath;
	}

	public String getReportName() {
		return this.reportName;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getLogLevel() {
		return this.logLevel;
	}

	public void setLogLevel(int logLevel) {
		if (logLevel < 0) {
			logLevel = 0;
		}
		if (logLevel > 5) {
			logLevel = 5;
		}
		this.logLevel = logLevel;
	}

	public String getDateFormatString() {
		return this.dateFormatString;
	}

	public void setDateFormatString(String dateFormatString) {
		this.dateFormatString = dateFormatString;
	}

	public ReportSettings(final String reportPath, final String reportName) {

		this.projectName = "";
		this.logLevel = 4;
		this.generateExcelReport = true;
		this.generateHTMLReport = true;
		this.generateExtendsReport = true;
		this.takeScreenshotFailedStep = true;
		this.takeScreenshotPassedStep = false;
		this.linkTestLogsToSummary = true;
		this.linkScreenshotToTestLog = true;
		this.consoidateScreenshotsInWordDoc = false;
		this.dateFormatString = "dd-MM-yyyy hh:mm:ss a";
		final boolean reportPathExists = new File(reportPath).isDirectory();
		if (!reportPathExists) {
			throw new FrameworkException("The given report path does not exist!!!");
		}
		this.reportPath = reportPath;
		this.reportName = reportName;
	}

}
