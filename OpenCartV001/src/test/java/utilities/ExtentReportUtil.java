package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportUtil implements ITestListener{
	ExtentSparkReporter Spark;
	ExtentReports Extent;
	ExtentTest test;
	String FileName_;
	
	public void onStart(ITestContext context) {
		String Datetimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		FileName_="./reports/Report"+this.getClass().getName()+Datetimestamp+".html";
		//Create Spark object. This is to create a report file, Set report title and report Name. 
		//Create a file and pass the filename as a parameter
		Spark = new ExtentSparkReporter(FileName_);
		Spark.config().setDocumentTitle("Automation Report");
		Spark.config().setReportName("Functional Testing");
		Spark.config().setTheme(Theme.DARK);                          // DARK / STANDARD
	    Spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
	    Spark.config().setEncoding("UTF-8");

		//Create Extent object. This will aid in attaching the report generated from 
		//ExtentSparkReporter, and add details regarding test execution 
		//like environment, tester name and so on
		Extent = new ExtentReports();
		Extent.attachReporter(Spark);
		
		Extent.setSystemInfo("Environment","macOS");
		Extent.setSystemInfo("Tester","Padma");
		Extent.setSystemInfo("Cycle","DryRun");
		Extent.setSystemInfo("Date",LocalDate.now().toString());
		Extent.setSystemInfo("Browser Name","Chrome");
		
	}
	//Capturing Screenshot for both passed and failed test cases
public void onTestSuccess(ITestResult result) {
		test = Extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.PASS, "Test Case "+result.getName()+" Passed");
		String screenshotfilepath = new BaseClass().captureScreenshot(result.getName().toString());
		test.addScreenCaptureFromPath(screenshotfilepath);

}
public void onTestFailure(ITestResult result) {
	test = Extent.createTest(result.getTestClass().getName());
	test.log(Status.FAIL, "Test Case "+result.getThrowable()+" Failed");
	String screenshotfilepath = new BaseClass().captureScreenshot(result.getName().toString());
	test.addScreenCaptureFromPath(screenshotfilepath);
}
		
public void onTestSkipped(ITestResult result) {
	test = Extent.createTest(result.getTestClass().getName());
	test.log(Status.SKIP, "Test Case "+result.getName()+" Skipped");
}	
public void onFinish(ITestContext context) {
	Extent.flush();
//To open the report automatically after execution
	File FileNameObj = new File(FileName_);
	try {
		
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (FileNameObj.exists()) {
                desktop.browse(FileNameObj.toURI());
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
	
}
	}


	

	


