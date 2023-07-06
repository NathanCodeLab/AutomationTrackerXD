package com.framework.support;

public class ExcelReport implements ReportType {

	private ExcelDataAccess testLogAccess;
	private ExcelDataAccess resultSummaryAccess;
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private ExcellCellFormatting cellFormatting;
	private int currentSectionRowNum;
	private int currentSubSectionRowNum;

	public ExcelReport(final ReportSettings reportSettings, final ReportTheme reportTheme) {
		this.cellFormatting = new ExcellCellFormatting();
		this.currentSectionRowNum = 0;
		this.currentSubSectionRowNum = 0;
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		this.testLogAccess = new ExcelDataAccess(
				String.valueOf(reportSettings.getReportPath()) + Util.getFileSeparator() + "Excel Results",
				reportSettings.getReportName());
		this.resultSummaryAccess = new ExcelDataAccess(
				String.valueOf(reportSettings.getReportPath()) + Util.getFileSeparator() + "Excel Results", "Summary");
	}

	@Override
	public void initializeTestLog() {
		this.testLogAccess.createWorkBook();
		this.testLogAccess.addsheet("Cover_Page");
		this.testLogAccess.addsheet("Test_Log");
		this.initializeTestLogColorPalette();
		this.testLogAccess.setRowSumBelow(false);
	}

	private void initializeTestLogColorPalette() {
		this.testLogAccess.setCustomPaletteColor((short) 8, this.reportTheme.getHeadingBackColor());
		this.testLogAccess.setCustomPaletteColor((short) 9, this.reportTheme.getHeadingForeColor());
		this.testLogAccess.setCustomPaletteColor((short) 10, this.reportTheme.getSelectionBackColor());
		this.testLogAccess.setCustomPaletteColor((short) 11, this.reportTheme.getSelectionForeColor());
		this.testLogAccess.setCustomPaletteColor((short) 12, this.reportTheme.getContentBackColor());
		this.testLogAccess.setCustomPaletteColor((short) 13, this.reportTheme.getContentForeColor());
		this.testLogAccess.setCustomPaletteColor((short) 14, "#008000");
		this.testLogAccess.setCustomPaletteColor((short) 15, "#FF0000");
		this.testLogAccess.setCustomPaletteColor((short) 16, "#FF8000");
		this.testLogAccess.setCustomPaletteColor((short) 17, "#000000");
		this.testLogAccess.setCustomPaletteColor((short) 18, "#00FF80");
	}

	@Override
	public void addTestLogHeading(final String p0) {
		this.testLogAccess.setDataSheetName("Cover_Page");
		int rowNum = this.testLogAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = this.testLogAccess.addRow();
		}
		this.cellFormatting.setFontName("Copperplate Gothic Bold");
		this.cellFormatting.setFontSize((short) 12);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 8);
		this.cellFormatting.setForeColorIndex((short) 9);
		this.testLogAccess.setValue(rowNum, 0, p0, this.cellFormatting);
		this.testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}

	@Override
	public void addTestLogSubHeading(final String subHeading, final String subHeading1, final String subHeading2,
			final String subHeading3) {
		this.testLogAccess.setDataSheetName("Cover_Page");
		final int rowNum = this.testLogAccess.addRow();
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 12);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 9);
		this.cellFormatting.setForeColorIndex((short) 8);
		this.testLogAccess.setValue(rowNum, 0, subHeading, this.cellFormatting);
		this.testLogAccess.setValue(rowNum, 1, subHeading1, this.cellFormatting);
		this.testLogAccess.setValue(rowNum, 2, "", this.cellFormatting);
		this.testLogAccess.setValue(rowNum, 3, subHeading2, this.cellFormatting);
		this.testLogAccess.setValue(rowNum, 4, subHeading3, this.cellFormatting);
	}

	@Override
	public void addTestLogTableHeading() {
		this.testLogAccess.setDataSheetName("Cover_Page");
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 8);
		this.cellFormatting.setForeColorIndex((short) 9);
		this.testLogAccess.addColumn("Step_No", this.cellFormatting);
		this.testLogAccess.addColumn("Step_Name", this.cellFormatting);
		this.testLogAccess.addColumn("Discription", this.cellFormatting);
		this.testLogAccess.addColumn("Status", this.cellFormatting);
		this.testLogAccess.addColumn("Step_Time", this.cellFormatting);
	}

	@Override
	public void addTestLogSection(final String section) {
		this.testLogAccess.setDataSheetName("Test_Log");
		final int rowNum = this.testLogAccess.addRow();
		if (this.currentSubSectionRowNum != 0) {
			this.testLogAccess.groupRows(this.currentSubSectionRowNum, rowNum - 1);
		}
		if (this.currentSectionRowNum != 0) {
			this.testLogAccess.groupRows(this.currentSectionRowNum, rowNum - 1);
		}
		this.currentSectionRowNum = rowNum + 1;
		this.currentSubSectionRowNum = 0;
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = false;
		this.cellFormatting.setBackColorIndex((short) 10);
		this.cellFormatting.setForeColorIndex((short) 11);
		this.testLogAccess.setValue(rowNum, 0, section, this.cellFormatting);
		this.testLogAccess.mergeCells(rowNum, rowNum, 0, 4);

	}

	@Override
	public void addTestLogSubSection(final String subSection) {
		this.testLogAccess.setDataSheetName("Test_Log");
		final int rowNum = this.testLogAccess.addRow();
		if (this.currentSubSectionRowNum != 0) {
			this.testLogAccess.groupRows(this.currentSubSectionRowNum, rowNum - 1);
		}
		this.currentSubSectionRowNum = rowNum + 1;
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = false;
		this.cellFormatting.setBackColorIndex((short) 9);
		this.cellFormatting.setForeColorIndex((short) 8);
		this.testLogAccess.setValue(rowNum, 0, " " + subSection, this.cellFormatting);
		this.testLogAccess.mergeCells(rowNum, rowNum, 0, 4);

	}

	@Override
	public void updateTestLog(final String stepNo, final String stepName, final String stepDes, final Status stepStatus,
			final String screenShotName) {
		this.testLogAccess.setDataSheetName("Test_log");
		final int rowNum = this.testLogAccess.addRow();
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		final boolean stepContainsScreenshot = this.processStatusColumn(stepStatus);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		final int columnNum = this.testLogAccess.getColumnNum("Status", 0);
		this.testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), this.cellFormatting);
		this.cellFormatting.setForeColorIndex((short) 13);
		this.cellFormatting.bold = false;
		this.testLogAccess.setValue(rowNum, "Step_No", stepNo, this.cellFormatting);
		this.testLogAccess.setValue(rowNum, "Step_Time",
				Util.getCurrentFormattedTime(this.reportSettings.getDateFormatString()), this.cellFormatting);
		this.cellFormatting.centred = true;
		this.testLogAccess.setValue(rowNum, "Step_Name", stepName, this.cellFormatting);
		if (stepContainsScreenshot) {
			if (this.reportSettings.linkScreenshotToTestLog) {
				this.testLogAccess.setHyperLink(rowNum, columnNum, " ..\\Screenshots\\" + screenShotName);
				this.testLogAccess.setValue(rowNum, "Description", stepDes, this.cellFormatting);
			} else {
				this.testLogAccess.setValue(rowNum, "Description",
						String.valueOf(stepDes) + "(Refer Screenshot @ " + screenShotName + ")", this.cellFormatting);
			}
		} else {
			this.testLogAccess.setValue(rowNum, "Description", stepDes, this.cellFormatting);
		}
	}
	@Override
	public void addTestLogFooter(final String executionTime, final int nStepPassed, final  int nStepFailed) {

		this.testLogAccess.setDataSheetName("Test_Log");
		int rowNum = this.testLogAccess.addRow();
		if (this.currentSubSectionRowNum != 0) {
			this.testLogAccess.groupRows(this.currentSubSectionRowNum, rowNum - 1);
		}
		if (this.currentSectionRowNum != 0) {
			this.testLogAccess.groupRows(this.currentSectionRowNum, rowNum - 1);
		}
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 8);
		this.cellFormatting.setForeColorIndex((short) 9);
		this.testLogAccess.setValue(rowNum, 0, "Execution Duration" + executionTime, this.cellFormatting);
		this.testLogAccess.mergeCells(rowNum, rowNum, 0, 4);

		rowNum = this.testLogAccess.addRow();
		this.cellFormatting.centred = false;
		this.cellFormatting.setBackColorIndex((short) 9);
		this.cellFormatting.setForeColorIndex((short) 14);
		this.testLogAccess.setValue(rowNum, "Step_No", "Steps Passed", this.cellFormatting);
		this.testLogAccess.setValue(rowNum, "Step_Name", " " + nStepPassed, this.cellFormatting);
		this.cellFormatting.setForeColorIndex((short) 8);
		this.testLogAccess.setValue(rowNum, "Description", "", this.cellFormatting);
		this.cellFormatting.setForeColorIndex((short) 15);
		this.testLogAccess.setValue(rowNum, "Status", "Steps Failed", this.cellFormatting);
		this.testLogAccess.setValue(rowNum, "Step_Time", " : " + nStepFailed, this.cellFormatting);
		this.wrapUpTestLog();
	}

	private boolean processStatusColumn(final Status stepStatus) {
		boolean stepContainsScreeshot = false;
		switch (stepStatus) {
		case PASS:
			this.cellFormatting.setForeColorIndex((short) 14);
			stepContainsScreeshot = this.reportSettings.takeScreenshotPassedStep;
			break;
		case WARNING:
			this.cellFormatting.setForeColorIndex((short) 14);
			stepContainsScreeshot = this.reportSettings.takeScreenshotFailedStep;
			break;
		case FAIL:
			this.cellFormatting.setForeColorIndex((short) 14);
			stepContainsScreeshot = this.reportSettings.takeScreenshotFailedStep;
			break;
		case DONE:
			this.cellFormatting.setForeColorIndex((short) 14);
			stepContainsScreeshot = false;
			break;
		case SCREENSHOT:
			this.cellFormatting.setForeColorIndex((short) 14);
			stepContainsScreeshot = true;
			break;

		case DEBUG:
			this.cellFormatting.setForeColorIndex((short) 14);
			stepContainsScreeshot = false;
			break;
		}
		return stepContainsScreeshot;
	}

	

	private void wrapUpTestLog() {
		this.testLogAccess.autoFitContents(0, 4);
		this.testLogAccess.addOuterBorder(0, 4);
		this.testLogAccess.setDataSheetName("Cover_Page");
		this.testLogAccess.autoFitContents(0, 4);
		this.testLogAccess.addOuterBorder(0, 4);
	}

	public void initializeResultSummary() {
		this.resultSummaryAccess.createWorkBook();
		this.resultSummaryAccess.addsheet("Cover_Page");
		this.resultSummaryAccess.addsheet("Result_Summary");
		this.initializeResultSummaryColorPalette();
	}

	private void initializeResultSummaryColorPalette() {
		this.resultSummaryAccess.setCustomPaletteColor((short) 8, this.reportTheme.getHeadingBackColor());
		this.resultSummaryAccess.setCustomPaletteColor((short) 9, this.reportTheme.getHeadingForeColor());
		this.resultSummaryAccess.setCustomPaletteColor((short) 10, this.reportTheme.getSelectionBackColor());
		this.resultSummaryAccess.setCustomPaletteColor((short) 11, this.reportTheme.getSelectionForeColor());
		this.resultSummaryAccess.setCustomPaletteColor((short) 12, this.reportTheme.getContentBackColor());
		this.resultSummaryAccess.setCustomPaletteColor((short) 13, this.reportTheme.getContentForeColor());
		this.resultSummaryAccess.setCustomPaletteColor((short) 14, "#008000");
		this.resultSummaryAccess.setCustomPaletteColor((short) 15, "#FF0000");
	}

	@Override
	public void addResultSummaryHeading(final String heading) {
		this.resultSummaryAccess.setDataSheetName("Cover_Page");
		int rowNum = this.resultSummaryAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = this.resultSummaryAccess.addRow();
		}
		this.cellFormatting.setFontName("Copperplate Gothic Bold");
		this.cellFormatting.setFontSize((short) 12);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 8);
		this.cellFormatting.setForeColorIndex((short) 9);
		this.resultSummaryAccess.setValue(rowNum, 0, heading, this.cellFormatting);
		this.resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);

	}

	@Override
	public void addResultSummarySubHeading(final String subHeading, final String subHeading1, final String subHeading2,
			final String subHeading3) {
		this.resultSummaryAccess.setDataSheetName("Cover_Page");
		final int rowNum = this.resultSummaryAccess.addRow();
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = false;
		this.cellFormatting.setBackColorIndex((short) 9);
		this.cellFormatting.setForeColorIndex((short) 8);
		this.resultSummaryAccess.setValue(rowNum, 0, subHeading, this.cellFormatting);
		this.resultSummaryAccess.setValue(rowNum, 1, subHeading1, this.cellFormatting);
		this.resultSummaryAccess.setValue(rowNum, 2, "", this.cellFormatting);
		this.resultSummaryAccess.setValue(rowNum, 3, subHeading2, this.cellFormatting);
		this.resultSummaryAccess.setValue(rowNum, 4, subHeading3, this.cellFormatting);
	}

	@Override
	public void addResultSummaryTableHeading() {
		this.resultSummaryAccess.setDataSheetName("Result_Summary");
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = true;
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 8);
		this.cellFormatting.setForeColorIndex((short) 9);
		this.resultSummaryAccess.addColumn("Test_Senario", this.cellFormatting);
		this.resultSummaryAccess.addColumn("Test_case", this.cellFormatting);
		this.resultSummaryAccess.addColumn("Test_Description", this.cellFormatting);
		this.resultSummaryAccess.addColumn("Execution_Time", this.cellFormatting);
		this.resultSummaryAccess.addColumn("Test_Status", this.cellFormatting);
	}

	@Override
	public void updateResultSummary(final String senarioName, final String testcaseName, final String testcaseDes,
			final String executionTime, final String testStatus) {
		this.resultSummaryAccess.setDataSheetName("Result_Summary");
		final int rowNum = this.resultSummaryAccess.addRow();
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.setBackColorIndex((short) 12);
		this.cellFormatting.setForeColorIndex((short) 13);
		this.cellFormatting.bold = false;
		this.cellFormatting.centred = false;
		this.resultSummaryAccess.setValue(rowNum, "Test_Senario", senarioName, this.cellFormatting);
		final int columnNum = this.resultSummaryAccess.getColumnNum("Test_Case", 0);
		this.resultSummaryAccess.setValue(rowNum, columnNum, testcaseName, this.cellFormatting);
		if (reportSettings.linkTestLogsToSummary) {
			this.resultSummaryAccess.setHyperLink(rowNum, columnNum,
					String.valueOf(senarioName) + "_" + testcaseName + ".xls");
		}
		this.resultSummaryAccess.setValue(rowNum, "Test_Description", testcaseDes, this.cellFormatting);
		this.cellFormatting.centred = true;
		this.resultSummaryAccess.setValue(rowNum, "Execution_Time", executionTime, this.cellFormatting);
		this.cellFormatting.centred = true;
		if (testStatus.equalsIgnoreCase("Passed")) {
			this.cellFormatting.setForeColorIndex((short) 14);
		}
		if (testStatus.equalsIgnoreCase("Failed")) {
			this.cellFormatting.setForeColorIndex((short) 15);
		}
		this.resultSummaryAccess.setValue(rowNum, "Test_Status", testStatus, this.cellFormatting);
	}

	@Override
	public void addResultSummaryFooter(final String totalExecutionTime, final int nTestsPassed, final int nTestFailed) {
		this.resultSummaryAccess.setDataSheetName("Result_Summary");
		int rowNum = this.resultSummaryAccess.addRow();
		this.cellFormatting.setFontName("Verdana");
		this.cellFormatting.setFontSize((short) 10);
		this.cellFormatting.bold = false;
		this.cellFormatting.centred = false;
		this.cellFormatting.setBackColorIndex((short) 8);
		this.cellFormatting.setForeColorIndex((short) 9);
		this.resultSummaryAccess.setValue(rowNum, 0, "Total Duration : " + totalExecutionTime, this.cellFormatting);
		this.resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 0);
		rowNum = this.resultSummaryAccess.addRow();
		this.cellFormatting.centred = true;
		this.cellFormatting.setBackColorIndex((short) 9);
		this.cellFormatting.setForeColorIndex((short) 14);
		this.resultSummaryAccess.setValue(rowNum, "Test_Senario", "Test Passed", this.cellFormatting);
		this.resultSummaryAccess.setValue(rowNum, "Test_Case", ": " + nTestsPassed, this.cellFormatting);
		this.cellFormatting.setForeColorIndex((short) 8);
		this.resultSummaryAccess.setValue(rowNum, "Test_Description", "", this.cellFormatting);
		this.cellFormatting.setForeColorIndex((short) 15);
		this.resultSummaryAccess.setValue(rowNum, "Execution_Time", "Tests Failed", this.cellFormatting);
		this.resultSummaryAccess.setValue(rowNum, "Test_Case", ": " + nTestFailed, this.cellFormatting);
		this.wrapUpResultSummary();

	}

	private void wrapUpResultSummary() {
		this.resultSummaryAccess.autoFitContents(0, 4);
		this.resultSummaryAccess.addOuterBorder(0, 4);
		this.resultSummaryAccess.setDataSheetName("Cover_Page");
		this.resultSummaryAccess.autoFitContents(0, 4);
		this.resultSummaryAccess.addOuterBorder(0, 4);

	}

}
