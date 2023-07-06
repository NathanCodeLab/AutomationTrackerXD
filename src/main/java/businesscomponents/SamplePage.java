package businesscomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.support.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class SamplePage extends ReusableLibrary{

	public SamplePage(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	private static final String DATAPROVIDER = "DataProvider";
	String data1= dataTable.getData(DATAPROVIDER, "Data1");
	String data2= dataTable.getData(DATAPROVIDER, "Data2");
	String data3= dataTable.getData(DATAPROVIDER, "Data3");
	String data4= dataTable.getData(DATAPROVIDER, "Data4");
	
	public void sampleMethod() throws InterruptedException {
		driver.get("https://www.google.com/");
		WebElement findElement = driver.findElement(By.name("q"));
		findElement.sendKeys(data1+data2+data3+data4);
	//	Thread.sleep(5000);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.FAIL);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.FAIL);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.FAIL);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.FAIL);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.FAIL);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.FAIL);
		report.updateTestLog("Sample Google Login", "Launch and entered into Google", Status.PASS);
		
	}

}
