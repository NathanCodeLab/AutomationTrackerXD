package com.framework.selenium;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.framework.support.FrameworkException;
import com.framework.support.FrameworkParameters;
import com.framework.support.ReportSettings;
import com.framework.support.ReportTheme;
import com.framework.support.ReportThemeFactory;
import com.framework.support.Settings;
import com.framework.support.TimeStamp;
import com.framework.support.Util;

public class ResultSummaryManager {

	private static SeleniumReport summayReport;
	private static ReportSettings reportSettings;
	private static String reportPath;
	private static Date overallStartTime;
	private static Date overallEndTime;
	private Properties properties;
	private FrameworkParameters frameworkParameters;

	public ResultSummaryManager() {
		this.frameworkParameters = FrameworkParameters.getInstance();
	}

	public void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if (relativePath.contains("supportlibraries")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		this.frameworkParameters.setReleativePath(relativePath);
	}

	public void initializeTestBatch(final String runConfiguration) {
		ResultSummaryManager.overallStartTime = Util.getCurrentTime();
		this.properties = Settings.getInstance();
		this.frameworkParameters.setRunConfirguration(runConfiguration);
	}

	public void initializeSummaryReport(final int nThread) {
		this.initializeReportSettings();
		final ReportTheme reportTheam = ReportThemeFactory
				.getReportsTheme(ReportThemeFactory.Theme.valueOf(this.properties.getProperty("ReportsTheme")));
		(ResultSummaryManager.summayReport = new SeleniumReport(ResultSummaryManager.reportSettings, reportTheam))
				.initialize();
		ResultSummaryManager.summayReport.initializeResultSummary();
		this.createResultSummaryHeader(nThread);
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			ResultSummaryManager.reportPath = System.getProperty("ReportPath");
		} else {
			ResultSummaryManager.reportPath = TimeStamp.getInstance();
		}
		(ResultSummaryManager.reportSettings = new ReportSettings(ResultSummaryManager.reportPath, ""))
				.setDateFormatString(this.properties.getProperty("DateFormatString"));
		ResultSummaryManager.reportSettings.setProjectName(this.properties.getProperty("ProjectName"));
		ResultSummaryManager.reportSettings.generateExcelReport = Boolean
				.parseBoolean(this.properties.getProperty("ExcelReport"));
		ResultSummaryManager.reportSettings.generateHTMLReport = Boolean
				.parseBoolean(this.properties.getProperty("HtmlReport"));
		ResultSummaryManager.reportSettings.linkTestLogsToSummary = true;
		ResultSummaryManager.reportSettings.generateExtendsReport = Boolean
				.parseBoolean(this.properties.getProperty("ExtendsReport"));
		ResultSummaryManager.reportSettings.linkTestLogsToSummary = true;
	}

	private void createResultSummaryHeader(final int nThread) {
		ResultSummaryManager.summayReport
				.addResultSummaryHeading(String.valueOf(ResultSummaryManager.reportSettings.getProjectName()) + " - "
						+ " Automation Execution Result Summary");
		ResultSummaryManager.summayReport.addResultSummarySubHeading("Date & Time",
				" : " + Util.getCurrentFormattedTime(this.properties.getProperty("DateFormatString")), "OnError",
				this.properties.getProperty("OnError"));
		ResultSummaryManager.summayReport.addResultSummarySubHeading("Run Configutation",
				" : " + frameworkParameters.getRunConfiguration(), "No of Threads", " : " + nThread);
		ResultSummaryManager.summayReport.addResultSummaryTableHeading();
	}

	public void setUpErrorLog() {
		final String errorLogFile = String.valueOf(ResultSummaryManager.reportPath) + Util.getFileSeparator()
				+ "ErrorLogs.txt";
		try {
			System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while Setting up the Error log!");
		}
	}

	public void updateResultSummary(final String scenarioName, final String testcaseName, final String testcaseDes,
			final String executionTime, final String testStatus) {
		ResultSummaryManager.summayReport.updateResultSummary(scenarioName, testcaseName, testcaseDes, executionTime,
				testStatus);
	}

	public void wrapUp(final boolean testExecutedInUnitTestFramework) {
		ResultSummaryManager.overallEndTime = Util.getCurrentTime();
		final String totalExecutionTime = Util.getTimeDifference(overallStartTime, overallEndTime);
		ResultSummaryManager.summayReport.addResultSummaryFooter(totalExecutionTime);
		if (testExecutedInUnitTestFramework) {
			final File testNgResultSrc = new File(String.valueOf(this.frameworkParameters.getReleativePath())
					+ Util.getFileSeparator() + this.properties.getProperty("TestNgReportPath"));
			final File testNgResultDest = new File(
					String.valueOf(ResultSummaryManager.reportPath) + Util.getFileSeparator() + "TestNG Results");
			try {
				FileUtils.copyDirectory(testNgResultSrc, testNgResultDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void launchResultSummary() {
		if (ResultSummaryManager.reportSettings.generateHTMLReport) {
			try {
				Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + ResultSummaryManager.reportPath
						+ "\\HTML Results\\Summary.Html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (ResultSummaryManager.reportSettings.generateExcelReport) {
			try {
				Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + ResultSummaryManager.reportPath
						+ "\\Excell Results\\Summary.xls");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
