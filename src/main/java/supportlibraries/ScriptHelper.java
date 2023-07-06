package supportlibraries;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.openqa.selenium.WebDriver;

import com.framework.selenium.SeleniumReport;
import com.framework.support.AutomatorDataTable;

/**
 * Wrapper class for common framwork objects, to be used across the entire test
 * case and depentent libraries
 * 
 * @author sabarinathan
 *
 */

public class ScriptHelper {
	private final AutomatorDataTable dataTable;
	private final SeleniumReport report;
	private final WebDriver driver;

	/**
	 * Constructor to initialize all the objects wrapped by the {@link ScriptHelper}
	 * class
	 * 
	 * @param dataTable the {@link AutomatorDataTable} object
	 * @param report    the {@link SeleniumReport} object
	 * @param driver    The {@link WebDriver} object
	 */

	final protected ConcurrentMap<String, String> dynamicData = new ConcurrentHashMap<String, String>();

	public String getDynamicData(final String key) {
		return dynamicData.get(key);
	}

	public void setDynamicData(final String key, final String value) {
		dynamicData.put(key, value);
	}

	public ScriptHelper(AutomatorDataTable dataTable, SeleniumReport report, WebDriver driver) {
		this.dataTable = dataTable;
		this.report = report;
		this.driver = driver;
	}

	/**
	 * Function to get and return the corresponding objects
	 */
	public AutomatorDataTable getDataTable() {
		return dataTable;
	}

	public SeleniumReport getReport() {
		return report;
	}

	public WebDriver getDriver() {
		return driver;
	}

}
