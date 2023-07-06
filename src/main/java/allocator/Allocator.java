package allocator;

import com.framework.selenium.Browser;
import com.framework.selenium.ResultSummaryManager;
import com.framework.selenium.SeleniumTestParameters;
import com.framework.support.ExcelDataAccess;
import com.framework.support.FrameworkParameters;
import com.framework.support.IterationOptions;
import com.framework.support.Settings;
import org.openqa.selenium.Platform;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * 1. Clean the browser by kill the task using RunTime
 * 2. Execute Driver Batch 
 * 		1. Set the relative Path from ResultSummaryManager.class
 * 		2. Set the Number of thread count to execute
 */

public class Allocator {

	private ResultSummaryManager resultSummaryManager = new ResultSummaryManager();
	Properties properties;
	private FrameworkParameters frameworkParameters = new FrameworkParameters().getInstance();

	public static void main(String[] args) {
		// killProcess("chrome");
		// killProcess("chromedriver");
		Allocator allocator = new Allocator();
		allocator.driverBatchExecution();
	}

	private void driverBatchExecution() {
		resultSummaryManager.setRelativePath();
		properties = Settings.getInstance();
		resultSummaryManager.initializeTestBatch(properties.getProperty("RunConfiguration"));

		int nThread = Integer.parseInt(properties.getProperty("NumberofThreads"));
		
		resultSummaryManager.initializeSummaryReport(nThread);
		resultSummaryManager.setUpErrorLog();
		executeTestBatch(nThread);
		resultSummaryManager.wrapUp(false);
		//resultSummaryManager.launchResultSummary();

	}

	private void executeTestBatch(int nThread) {
		List<SeleniumTestParameters> testInstancesToRun = getRunInfo(frameworkParameters.getRunConfiguration());
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThread);
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++) {
			ParallelRunner testRunner = new ParallelRunner(testInstancesToRun.get(currentTestInstance),
					resultSummaryManager);
			parallelExecutor.execute(testRunner);
			if (frameworkParameters.getStopExecution()) {
				break;
			}
		}
		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private List<SeleniumTestParameters> getRunInfo(String sheetName) {
		ExcelDataAccess runManagerAccess = new ExcelDataAccess(frameworkParameters.getReleativePath(), "Run Manager");
		runManagerAccess.setDataSheetName(sheetName);

		int nTestInstance = runManagerAccess.getLastRowNum();
		List<SeleniumTestParameters> testInstanceToRun = new ArrayList<SeleniumTestParameters>();
		for (int currentTestInstance = 1; currentTestInstance <= nTestInstance; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance, "Execute");
			
			if (executeFlag.equalsIgnoreCase("Yes")) {
				String currentScenario = runManagerAccess.getValue(currentTestInstance, "TestScenario");
				String currentTestCase = runManagerAccess.getValue(currentTestInstance, "TestCase");
				SeleniumTestParameters testParameter = new SeleniumTestParameters(currentScenario, currentTestCase);

				testParameter.setCurrentTestDescription(runManagerAccess.getValue(currentTestInstance, "Description"));

				String iterationMode = runManagerAccess.getValue(currentTestInstance, "IterationMode");
				if (!iterationMode.equals("")) {
					testParameter.setIterationMode(IterationOptions.valueOf(iterationMode));
				} else {
					testParameter.setIterationMode(IterationOptions.RunAllIteration);
				}
				String startIteration = runManagerAccess.getValue(currentTestInstance, "StartIteration");
				if (!startIteration.equals("")) {
					testParameter.setStartIteration(Integer.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(currentTestInstance, "EndIteration");
				if (!endIteration.equals("")) {
					testParameter.setEndIteration(Integer.parseInt(endIteration));
				}
				String browser = runManagerAccess.getValue(currentTestInstance, "Browser");
				if (!browser.equals("")) {
					testParameter.setBrowser(Browser.valueOf(browser));
				} else {
					testParameter.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
				}
				String browserVersion = runManagerAccess.getValue(currentTestInstance, "BrowserVersion");
				if (!browserVersion.equals("")) {
					testParameter.setBrowserVersion(browserVersion);
				}
				String platform = runManagerAccess.getValue(currentTestInstance, "Platform");
				if (!platform.equals("")) {
					testParameter.setPlatform(Platform.valueOf(platform));
				} else {
					testParameter.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));

				}
				testInstanceToRun.add(testParameter);
			}
		}

		return testInstanceToRun;
	}

	private static void killProcess(String serviceName) {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + serviceName + ".exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
