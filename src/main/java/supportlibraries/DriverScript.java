package supportlibraries;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.framework.selenium.Browser;
import com.framework.selenium.ExecutionMode;
import com.framework.selenium.SeleniumReport;
import com.framework.selenium.SeleniumTestParameters;
import com.framework.selenium.WebDriverFactory;
import com.framework.support.AutomatorDataTable;
import com.framework.support.ExcelDataAccess;
import com.framework.support.FrameworkException;
import com.framework.support.FrameworkParameters;
import com.framework.support.IterationOptions;
import com.framework.support.OnError;
import com.framework.support.ReportSettings;
import com.framework.support.ReportTheme;
import com.framework.support.ReportThemeFactory;
import com.framework.support.Settings;
import com.framework.support.Status;
import com.framework.support.TimeStamp;
import com.framework.support.Util;
import com.framework.support.ReportThemeFactory.Theme;

public class DriverScript {

	private List<String> businessFlowData;
	private int currentIteration, currentSubIteration;
	private Date startTime, endTime;

	private AutomatorDataTable dataTable;
	private ReportSettings reportSettings;
	private SeleniumReport report;
	private WebDriver driver;
	private ScriptHelper scriptHelper;

	private Properties properties;
	private ExecutionMode executionMode;
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private Boolean testExecutedInUnitTestFramework = true;
	private Boolean linkScreenshotsToTestLog = true;
	private String testStatus;
	private final SeleniumTestParameters testParameters;
	private static String reportPath;

	/****
	 * 
	 * Function to indicate whether the Test is executing in JUnit/TestNG or Not
	 * 
	 * @param testExecutedInUnitTestFrameWork Boolean variable indicate whether the
	 *                                        test is executed in j/tNG
	 */

	public void setTestExecutedInUnitTestFramework(Boolean testExecutedInUnitTestFramework) {
		this.testExecutedInUnitTestFramework = testExecutedInUnitTestFramework;
	}

	/****
	 * Function to configure the linking of screenshot to the corresponding test log
	 * indicates whether screenshots should be linked to the corresponding test log
	 * 
	 */
	public void setLinkScreenshotsToTestLog(Boolean linkScreenshotsToTestLog) {
		this.linkScreenshotsToTestLog = linkScreenshotsToTestLog;
	}

	public String getTestStatus() {
		return this.testStatus;
	}

	public DriverScript(SeleniumTestParameters testParameters) {
		this.testParameters = testParameters;
	}

	/**
	 * Function to Execute the given test case
	 * 
	 */
	public void driverTestExecution() {
		startUp();
		System.out.println("After StartUp");
		initializeTestIteration();
		System.out.println("Test Iteration Initiated");
		initializeWebDriver();
		initializeTestReport();
		initializeDataTable();
		initializeTestScript();

		executeTestIterations();

		quitDriver();
		wrapUp();

	}

	private void startUp() {
		startTime = Util.getCurrentTime();
		properties = Settings.getInstance();
		setDefaultTestParameters();
	}

	private void setDefaultTestParameters() {
		if (testParameters.getIterationMode() == null) {
			testParameters.setIterationMode(IterationOptions.RunAllIteration);
		}
		if (System.getProperty("Browser") != null) {
			testParameters.setBrowser(Browser.valueOf(System.getProperty("Browser")));
		} else {
			if (testParameters.getBrowser() == null) {
				testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
			}
		}
		if (System.getProperty("BrowserVersion") != null) {
			testParameters.setBrowserVersion(System.getProperty("BrowserVersion"));
		}
		if (System.getProperty("Platform") != null) {
			testParameters.setPlatform(Platform.valueOf(System.getProperty("Platform")));
		} else {
			if (testParameters.getPlatform() == null) {
				testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));
			}
		}
	}

	private void initializeTestIteration() {
		switch (testParameters.getIterationMode()) {
		case RunAllIteration:
			String datatablePath = frameworkParameters.getReleativePath() + Util.getFileSeparator() + "Datatables";
			ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, testParameters.getCurrentScenario());
			testDataAccess.setDataSheetName(properties.getProperty("DefaultDataSheet"));

			int startRowNum = testDataAccess.getRowNum(testParameters.getCurrentTestCase(), 0);
			int nTestcaseRow = testDataAccess.getRowCount(testParameters.getCurrentTestCase(), 0, startRowNum);
			int nSubIteraton = testDataAccess.getRowCount("1", 1, startRowNum); // atlease will have one iteration in
			int nIteration = nTestcaseRow / nSubIteraton;
			testParameters.setEndIteration(nIteration);
			currentIteration = 1;
			break;

		case RunOneIterationOnly:
			currentIteration = 1;
			break;

		case RunRangeOfIteration:
			if (testParameters.getStartIteration() > testParameters.getEndIteration()) {
				throw new FrameworkException("Error", "StartIteration cannot be greater than EndIteration");
			}
			currentIteration = testParameters.getStartIteration();
			break;
		default:
			throw new FrameworkException("Unhandled Iteration Mode!");
		}
	}

	private void initializeWebDriver() {
		executionMode = ExecutionMode.valueOf(properties.getProperty("ExecutionMode"));
		try {
			switch (executionMode) {
			case Local:
				driver = WebDriverFactory.getDriver(testParameters.getBrowser());
				break;
			case Remote:
				driver = WebDriverFactory.getDriver(testParameters.getBrowser(), properties.getProperty("RemoteURL"));
				break;
			case Grid:
				driver = WebDriverFactory.getDriver(testParameters.getBrowser(), testParameters.getBrowserVersion(),
						testParameters.getPlatform(), properties.getProperty("RemoteURL"));
				break;
			case Perfecto:
				// ImobileDevice device =
				// WebDriverFactory.getDriver(testParameters.getPerfectoDeviceId());
				break;

			default:
				throw new FrameworkException("Unhandled Exception Mode!");
			}
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.getMessage();
			System.out.println("I am trying to first catch to invoke driver");
			try {
				Thread.sleep(3000);
				WebDriverFactory.getDriver(testParameters.getBrowser(), testParameters.getBrowserVersion(),
						testParameters.getPlatform(), properties.getProperty("RemoteURL"));
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
				System.out.println("I am trying in second catch to invoke driver");
			}
		}
	}

	private void initializeTestReport() {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory
				.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));
		report = new SeleniumReport(reportSettings, reportTheme);
		report.initialize();
		report.setDriver(driver);
		report.initializeTestLog();
		createTestLogHeader();

	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}
		reportSettings = new ReportSettings(reportPath,
				testParameters.getCurrentScenario() + "_" + testParameters.getCurrentTestCase());
		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setLogLevel(Integer.parseInt(properties.getProperty("LogLevel")));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.generateExcelReport = Boolean.parseBoolean(properties.getProperty("ExcelReport"));
		reportSettings.generateHTMLReport = Boolean.parseBoolean(properties.getProperty("HtmlReport"));
		reportSettings.generateExtendsReport = Boolean.parseBoolean(properties.getProperty("ExtendsReport"));
		reportSettings.takeScreenshotFailedStep = Boolean
				.parseBoolean(properties.getProperty("TakesScreenshotFailedStep"));
		reportSettings.takeScreenshotPassedStep = Boolean
				.parseBoolean(properties.getProperty("TakesScreenshotPassedStep"));
		reportSettings.consoidateScreenshotsInWordDoc = Boolean
				.parseBoolean(properties.getProperty("ConsolidateScreenshotInWordDoc"));
		if (testParameters.getBrowser().equals(Browser.HtmlUnit)) {
			reportSettings.linkScreenshotToTestLog = false;
		} else {
			reportSettings.linkScreenshotToTestLog = this.linkScreenshotsToTestLog;
		}

	}

	private void createTestLogHeader() {
		report.addTestLogHeading(reportSettings.getProjectName() + " - " + reportSettings.getReportName()
				+ " Automation Execution Results");

		report.addTestLogSubHeading("Date & Time ",
				" : " + Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")), "Iteration Mode ",
				" : " + testParameters.getIterationMode());
		report.addTestLogSubHeading("Start Iteration ", " : " + testParameters.getStartIteration(), "End Iteration ",
				" : " + testParameters.getEndIteration());

		switch (executionMode) {
		case Local:
			report.addTestLogSubHeading("Browser", " : " + testParameters.getBrowser(), "Executed on ",
					" : " + "Local Machine");
			break;
		case Remote:
			report.addTestLogSubHeading("Browser", " : " + testParameters.getBrowser(), "Executed on ",
					" : " + properties.getProperty("RemoteURL"));
			break;
		case Grid:
			String browserVersion = testParameters.getBrowserVersion();
			if (browserVersion == null) {
				browserVersion = "Not Specified";
			}
			report.addTestLogSubHeading("Browser", " : " + testParameters.getBrowser(), "Version ",
					" : " + browserVersion);

			report.addTestLogSubHeading("Platform", " : " + testParameters.getPlatform().toString(), "Executed on ",
					" : " + "Grid @" + properties.getProperty("RemoteURL"));
			break;
		case Perfecto:
			/*
			 * report.addTestLogSubHeading("Device ID", " : " +
			 * testParameters.getPerfectoDeviceId(), "Executed on ", " : " +
			 * "Perfecto MobileCloud");
			 * 
			 * report.addTestLogSubHeading("Perfecto Host", " : " +
			 * properties.getProperty("PerfectoHost"), "Perfecto User", " : " +
			 * properties.getProperty("PerfectoUser"));
			 */
			break;
		default:
			throw new FrameworkException("Unhandled Execution Mode!");
		}
		report.addTestLogTableHeading();

	}

	public static void consolidatedReportPath() {
		String consolidatedReportPath;
		consolidatedReportPath = reportPath + Util.getFileSeparator() + "Consolidated HTML Results";
		System.out.println(consolidatedReportPath);
	}

	private void initializeDataTable() {
		String datatablePath = frameworkParameters.getReleativePath() + Util.getFileSeparator() + "Datatables";
		String runTimeDatatablePath;
		String runTimePath;
		String consolidatedReportPath;

		Boolean includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));

		if (includeTestDataInReport) {
			runTimeDatatablePath = reportPath + Util.getFileSeparator() + "Datatables";
			runTimePath = reportPath + Util.getFileSeparator() + "HTML Results" + "\\*.html";
			consolidatedReportPath = reportPath + Util.getFileSeparator() + "Consolidated HTML Results";
			File runTimeDatatable = new File(
					runTimeDatatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");
			System.out.println("ReportPath");
			System.out.println(runTimePath);
			System.out.println(consolidatedReportPath);
			File consolidatedPath = new File(consolidatedReportPath);
			if (!consolidatedPath.exists()) {
				if (consolidatedPath.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create Directorys");

				}
			}
			try {
				final String WHITESPACE = " ";
				File batFile = new File("D:\\Telly\\Batch\\CustomReport.bat");
				FileOutputStream fos = new FileOutputStream(batFile);
				DataOutputStream dos = new DataOutputStream(fos);
				StringBuilder builder = new StringBuilder();
				builder.append("xcopy");
				builder.append(WHITESPACE);
				builder.append("/y");
				builder.append(WHITESPACE);
				builder.append("\"");
				builder.append(WHITESPACE);
				builder.append("\"");
				builder.append("D:\\Telly\\ReportsCustom\\Selenium\\");

				String resultString = builder.toString();
				System.out.println(resultString);
				dos.writeBytes(resultString);
				dos.write('\n');
				String reString = new StringBuilder().append("Exit").toString();
				dos.writeBytes(reString);

			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
			if (!runTimeDatatable.exists()) {
				File datatable = new File(
						datatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");
				try {
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e) {

					e.printStackTrace();
					throw new FrameworkException("Error in creating run-time datatable : copying failed....");

				}
			}
			File runTimeCommontable = new File(runTimeDatatablePath + Util.getFileSeparator() + "Common Testdata.xls");
			if (!runTimeCommontable.exists()) {
				File commonDatatable = new File(datatablePath + Util.getFileSeparator() + "Common Testdata.xls");

				try {
					FileUtils.copyFile(commonDatatable, runTimeCommontable);
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException("Error in creating run-time datatable : copying failed....");

				}
			}
		} 
		else {
			runTimeDatatablePath = datatablePath;
		}
		dataTable = new AutomatorDataTable(runTimeDatatablePath, testParameters.getCurrentScenario());
		dataTable.setDataReferenceIdentifier(properties.getProperty("DataReferenceIdentifer"));

	}

	private void initializeTestScript() {
		scriptHelper = new ScriptHelper(dataTable, report, driver);
		businessFlowData = getBusinessFlow();
	}

	private List<String> getBusinessFlow() {
		ExcelDataAccess businessFlowAccess = new ExcelDataAccess(
				frameworkParameters.getReleativePath() + Util.getFileSeparator() + "Datatables",
				testParameters.getCurrentScenario());
		businessFlowAccess.setDataSheetName("Business_Flow");
		int rowNum = businessFlowAccess.getRowNum(testParameters.getCurrentTestCase(), 0);
		if (rowNum == -1) {
			throw new FrameworkException("The Test case \"" + this.testParameters.getCurrentTestCase() + "\""
					+ "is not found in the Business flow sheet!");
		}
		String dataValue;
		List<String> businessFlowData = new ArrayList<String>();
		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if (dataValue.equals("")) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}
		if (businessFlowData.isEmpty()) {
			throw new FrameworkException("No business flow found against the test case \""
					+ this.testParameters.getCurrentTestCase() + "\"");

		}
		return businessFlowData;
	}

	private void executeTestIterations() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.addTestLogSection("Iteration: " + Integer.toString(currentIteration));

			try {
				executeTestcase(businessFlowData);
			} catch (FrameworkException fx) {
				exceptionHander(fx, fx.errorName);
			} catch (Exception e) {
				exceptionHander(e, "error");
			}
			currentIteration++;
		}
	}

	private void exceptionHander(Exception ex, String exceptionName) {
		// error Reporting

		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null) {
			exceptionDescription = ex.toString();
		}
		if (ex.getCause() != null) {
			report.updateTestLog(exceptionName, exceptionDescription + "<b> Caused by: </b>" + ex.getCause(),
					Status.FAIL);
		} else {
			report.updateTestLog(exceptionName, exceptionDescription, Status.FAIL);

		}
		ex.printStackTrace();

		// Error Responce
		if (frameworkParameters.getStopExecution()) {
			report.updateTestLog("Automator Info", "Test Execution terminated by user! All subsequent tests aborted...",
					Status.DONE);
			currentIteration = testParameters.getEndIteration();
		} else {
			OnError onError = OnError.valueOf(properties.getProperty("OnError"));
			switch (onError) {
			case NextIteration:

				report.updateTestLog("Automator Info",
						"Test case iteration terminated by user! Proceeding to next Iteration (if applicable)...",
						Status.DONE);
				break;
			case NextTestcase:

				report.updateTestLog("Automator Info",
						"Test case terminated by user! Proceeding to next Iteration (if applicable)...", Status.DONE);
				break;
			case Stop:

				report.updateTestLog("Automator Info",
						"Test Execution terminated by user! All subsequent tests Aborted...", Status.DONE);
				currentIteration = testParameters.getEndIteration();
				break;
			}
		}
	}

	private void executeTestcase(List<String> businessFlowData)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		HashMap<String, Integer> keywordDirectory = new HashMap<String, Integer>();
		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum).split(",");
			String currentKeyword = currentFlowData[0];
			int nKeywordIteration;
			if (currentFlowData.length > 1) {
				nKeywordIteration = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIteration = 1;
			}
			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIteration; currentKeywordIteration++) {
				if (keywordDirectory.containsKey(currentKeyword)) {
					keywordDirectory.put(currentKeyword, keywordDirectory.get(currentKeyword) + 1);

				} else {
					keywordDirectory.put(currentKeyword, 1);

				}
				currentSubIteration = keywordDirectory.get(currentKeyword);
				dataTable.setCurrentRow(testParameters.getCurrentTestCase(), currentIteration, currentSubIteration);
				if (currentSubIteration > 1) {
					report.addTestLogSubSection(currentKeyword + "(Sub-Iteration : " + currentSubIteration + ")");
				} else {
					report.addTestLogSubSection(currentKeyword);
				}
				invokeBusinessComponent(currentKeyword);
			}
		}

	}

	private void invokeBusinessComponent(String currentKeyword)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		File[] packageDirectories = {
				new File(frameworkParameters.getReleativePath() + Util.getFileSeparator() + "target"
						+ Util.getFileSeparator() + "classes" + Util.getFileSeparator() + "businesscomponents")
				,
				new File(frameworkParameters.getReleativePath() + Util.getFileSeparator() + "target"
						+ Util.getFileSeparator() + "classes" + Util.getFileSeparator() + "componentgroups") 
		};
	

		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();
			
			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();
				// we only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// remove the .class extension to get the class name
					String classname = fileName.substring(0, fileName.length() - CLASS_FILE_EXTENSION.length());
					Class<?> reusableComponents = Class.forName(packageName + "." + classname);
					Method executeComponent;
					try {
						System.out.println("Keyword " + currentKeyword);
						currentKeyword = currentKeyword.substring(0, 1).toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException e) {
						// if the method is not found in this class. search the next class
						continue;

					}
					isMethodFound = true;
					Constructor<?> ctor = reusableComponents.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);
					executeComponent.invoke(businessComponent, (Object[]) null);

					break;

				}

			}
		}
		if (!isMethodFound) {
			throw new FrameworkException("Keyword " + currentKeyword + " is not found within any class "
					+ "inside the businesscomponents package");

		}
	}

	private void quitDriver() {
		driver.quit();

		/*
		 * if (executionMode.equals(ExecutionMode.Perfecto)) { device.close(); }
		 */
	}

	private void wrapUp() {
		endTime = Util.getCurrentTime();
		closeTestReport();

		testStatus = report.getTestStatus();

		if (testExecutedInUnitTestFramework && testStatus.equalsIgnoreCase("Failed")) {
			Assert.fail(report.getFailureDescription());
		}
	}

	private void closeTestReport() {
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.addTestLogFooter(executionTime);
		if (reportSettings.consoidateScreenshotsInWordDoc) {
			report.consolidateScreenshotsinWordDoc();
		}
	}

}
