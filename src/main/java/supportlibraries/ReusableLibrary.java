package supportlibraries;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.framework.selenium.SeleniumReport;
import com.framework.support.AutomatorDataTable;
import com.framework.support.FrameworkException;
import com.framework.support.FrameworkParameters;
import com.framework.support.Settings;

public abstract class ReusableLibrary {

	protected AutomatorDataTable dataTable;

	protected SeleniumReport report;

	protected WebDriver driver;

	protected ScriptHelper scriptHelper;

	protected Properties properties;

	protected FrameworkParameters frameworkParameters;
	
	
	public ReusableLibrary(ScriptHelper scriptHelper) {
		this.scriptHelper = scriptHelper;
		this.dataTable = scriptHelper.getDataTable();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getDriver();
		
		properties = Settings.getInstance();
		frameworkParameters = FrameworkParameters.getInstance();
		
	}

}
