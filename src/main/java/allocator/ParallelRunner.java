package allocator;

import java.util.Date;

import com.framework.selenium.ResultSummaryManager;
import com.framework.selenium.SeleniumTestParameters;
import com.framework.support.FrameworkParameters;
import com.framework.support.Util;

import supportlibraries.DriverScript;

public class ParallelRunner implements Runnable {
	/**
	 * Function to facilitate parallel execution of test scripts
	 * 
	 * @author sabarinathan
	 * 
	 */
	private final SeleniumTestParameters testParameters;
	private final ResultSummaryManager resultSummaryManager;

	/**
	 * 
	 * Constructor to initialize the details to the test case to be executed
	 * 
	 * @param testParameters       The {@link SeleniumTestParameters} objects
	 *                             (passed from the {@link Allocator}
	 * @param resultSummaryManager The {@link ResultSummaryManager} object (passed
	 *                             from the {@link Allocator}
	 */

	public ParallelRunner(SeleniumTestParameters testParameters, ResultSummaryManager resultSummaryManager) {
		super();
		this.testParameters = testParameters;
		this.resultSummaryManager = resultSummaryManager;
	}

	@Override
	public void run() {
		Date startTime = Util.getCurrentTime();
		String testStatus = invokeTestScript();
		Date endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		resultSummaryManager.updateResultSummary(testParameters.getCurrentScenario(),
				testParameters.getCurrentTestCase(), testParameters.getCurrentTestDescription(), executionTime,
				testStatus);
		System.out.println("Execution Done");
	}

	private String invokeTestScript() {
		String testStatus;
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		
		if (frameworkParameters.getStopExecution()) {
			testStatus = "Aborted";
		} else {
			DriverScript driverScript = new DriverScript(this.testParameters);
			driverScript.setTestExecutedInUnitTestFramework(false);
			driverScript.driverTestExecution();
			testStatus = driverScript.getTestStatus();
		}

		return testStatus;
	}

}
