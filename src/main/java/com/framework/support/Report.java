package com.framework.support;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.Dimension;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.FilenameFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Report {

	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private int stepNumber;
	private int nStepPassed;
	private int nStepFailed;
	private int nTestPassed;
	private int nTestFailed;
	private List<ReportType> reportType;
	public String testStatus;
	private String failureDescription;

	public String getTestStatus() {
		return this.testStatus;
	}

	public String getFailureDescription() {
		return this.failureDescription;
	}

	public Report(final ReportSettings reportSettings, final ReportTheme reportTheme) {

		this.nStepPassed = 0;
		this.nStepFailed = 0;
		this.nTestPassed = 0;
		this.nTestFailed = 0;
		this.reportType = new ArrayList<ReportType>();
		this.testStatus = "Passed";
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
	}

	public void initialize() {
		if (this.reportSettings.generateExcelReport) {
			new File(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator() + "Excel Results")
					.mkdir();
			final ExcelReport excelReport = new ExcelReport(this.reportSettings, this.reportTheme);
			this.reportType.add(excelReport);
		}
		if (this.reportSettings.generateHTMLReport) {
			new File(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator() + "HTML Results")
					.mkdir();
			final HtmlReport htmlReport = new HtmlReport(this.reportSettings, this.reportTheme);
			this.reportType.add(htmlReport);

		}
	/*	if (this.reportSettings.generateExtendsReport) {
			new File(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator() + "Extends Results")
					.mkdir();
			final ExtendsReport extendsReport = new ExtendsReport();
			// this.reportType.add(extendsReport);
		}*/
		new File(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator() + "Screenshots").mkdir();
	}

	public void initializeTestLog() {
		if (this.reportSettings.getReportName().equals("")) {
			throw new FrameworkException("The report name cannot be empty!");
		}
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).initializeTestLog();
		}

	}

	public void addTestLogHeading(final String heading) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addTestLogHeading(heading);
		}
	}

	public void addTestLogSubHeading(final String subHeading, final String subHeading1, final String subHeading2,
			final String subHeading3) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addTestLogSubHeading(subHeading, subHeading1, subHeading2, subHeading3);
		}
	}

	public void addTestLogTableHeading() {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addTestLogTableHeading();
		}
	}

	public void addTestLogSection(final String section) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addTestLogSection(section);
		}
		this.stepNumber = 1;
	}

	public void addTestLogSubSection(final String subSection) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addTestLogSubSection(subSection);
		}
	}

	public void updateTestLog(final String stepName, final String stepDes, final Status stepStatus) {
		if (stepStatus.equals(Status.FAIL)) {
			this.testStatus = "Failed";
			if (this.failureDescription == null) {
				this.failureDescription = stepDes;
			} else {
				this.failureDescription = String.valueOf(this.failureDescription) + "; " + stepDes;
			}
			++this.nStepFailed;
		}
		if (stepStatus.equals(Status.PASS)) {
			++this.nStepPassed;
		}
		if (stepStatus.ordinal() <= this.reportSettings.getLogLevel()) {
			String screenShotName = null;
			if (stepStatus.equals(Status.FAIL) && this.reportSettings.takeScreenshotFailedStep) {
				screenShotName = String.valueOf(this.reportSettings.getReportName()) + "_"
						+ Util.getCurrentFormattedTime(this.reportSettings.getDateFormatString()).replace(" ", "_")
								.replace(":", "-")
						+ ".png";
				this.takeScreenshot(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator()
						+ "Screenshots" + Util.getFileSeparator() + screenShotName);
			}
			if (stepStatus.equals(Status.PASS) && this.reportSettings.takeScreenshotPassedStep) {
				screenShotName = String.valueOf(this.reportSettings.getReportName()) + "_"
						+ Util.getCurrentFormattedTime(this.reportSettings.getDateFormatString()).replace(" ", "_")
								.replace(":", "-")
						+ ".png";
				this.takeScreenshot(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator()
						+ "Screenshots" + Util.getFileSeparator() + screenShotName);
			}
			if (stepStatus.equals(Status.SCREENSHOT)) {
				screenShotName = String.valueOf(this.reportSettings.getReportName()) + "_"
						+ Util.getCurrentFormattedTime(this.reportSettings.getDateFormatString()).replace(" ", "_")
								.replace(":", "-")
						+ ".png";
				this.takeScreenshot(String.valueOf(this.reportSettings.getReportPath()) + Util.getFileSeparator()
						+ "Screenshots" + Util.getFileSeparator() + screenShotName);
			}
			for (int i = 0; i < this.reportType.size(); ++i) {
				this.reportType.get(i).updateTestLog(Integer.toString(this.stepNumber), stepName, stepDes, stepStatus,
						screenShotName);
			}
			++this.stepNumber;
		}

	}

	protected void takeScreenshot(final String screenshotPath) {
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screensize = toolkit.getScreenSize();
		final Rectangle rectangle = new Rectangle(0, 0, screensize.width, screensize.height);
		Robot robot;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating Robot object (for taking Screenshot)");
		}
		final BufferedImage screenshotImage = robot.createScreenCapture(rectangle);
		final File screenshotFile = new File(screenshotPath);
		try {
			ImageIO.write(screenshotImage, "jpg", screenshotFile);
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new FrameworkException("Errow while writing screenshot to .jpg file");
		}

	}

	public void addTestLogFooter(final String executionTime) {
		for (int i = 0; i < this.reportType.size(); i++) {
			this.reportType.get(i).addTestLogFooter(executionTime, this.nStepPassed, this.nStepFailed);
		}
	}

	public void consolidateScreenshotsinWordDoc() {
		final String screenshotConsolidatedFolderPath = String.valueOf(this.reportSettings.getReportPath())
				+ Util.getFileSeparator() + "Screenshots (consolidated)";

		new File(screenshotConsolidatedFolderPath).mkdir();
		final WordDocumentManager wordDocumentManager = new WordDocumentManager(screenshotConsolidatedFolderPath,
				this.reportSettings.getReportName());
		final String screenshotFolderPath = String.valueOf(this.reportSettings.getReportPath())
				+ Util.getFileSeparator() + "Screenshots";
		final File screenshotFolder = new File(screenshotFolderPath);
		final FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String fileName) {
				return fileName.contains(Report.this.reportSettings.getReportName());
			}
		};

		final File[] screenshots = screenshotFolder.listFiles(fileNameFilter);
		if (screenshots != null && screenshots.length > 0) {
			wordDocumentManager.createDocument();
			File[] array;
			for (int length = (array = screenshots).length, i = 0; i < length; ++i) {
				final File screenshot = array[i];
				wordDocumentManager.addPicture(screenshot);
			}
		}
	}

	public void initializeResultSummary() {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).initializeResultSummary();
		}
	}

	public void addResultSummaryHeading(final String heading) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addResultSummaryHeading(heading);
		}
	}

	public void addResultSummarySubHeading(final String subHeading, final String subHeading1, final String subHeading2,
			final String subHeading3) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addResultSummarySubHeading(subHeading, subHeading1, subHeading2, subHeading3);
		}
	}

	public void addResultSummaryTableHeading() {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addResultSummaryTableHeading();
		}
	}

	public synchronized void updateResultSummary(final String scenarioName, final String testcaseName, final String testcaseDes,
			final String executionTime, final String testStatus) {
		if (testStatus.equalsIgnoreCase("Failed")) {
			++this.nTestFailed;
		}
		if (testStatus.equalsIgnoreCase("Passed")) {
			++this.nTestPassed;
		}

		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).updateResultSummary(scenarioName, testcaseName, testcaseDes, executionTime,
					testStatus);
		}
	}

	public void addResultSummaryFooter(final String totalExecutionTime) {
		for (int i = 0; i < this.reportType.size(); ++i) {
			this.reportType.get(i).addResultSummaryFooter(totalExecutionTime, this.nTestPassed, this.nTestFailed);
		}	
	}

}
