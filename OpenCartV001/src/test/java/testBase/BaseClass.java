package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {
	public static WebDriver driver;
	public Logger logReport;
	public Properties p;
	//public TakesScreenshot ts;

	@SuppressWarnings("deprecation")
	@Parameters({"os","browser"})
	@BeforeClass(groups = {"Sanity","Master","Regression"})
	public void setUp(String os, String br ) throws IOException {
		logReport = LogManager.getLogger(this.getClass());
		FileReader f = new FileReader("./src//test//resources//config.properties");
		p = new Properties();
		p.load(f);
		String URL = p.getProperty("URL1");
		String env = p.getProperty("EXECUTION_ENV");
		
		switch (env.toLowerCase()) {
		case "local":
			// browser
			switch (br.toLowerCase()) {
			case "chrome":  
				driver = new ChromeDriver();
				logReport.debug("Browser Name: Chrome Browser");
				break;
			case "safari":
				driver = new  SafariDriver();
				logReport.debug("Browser Name: Safari Browser");
				break;
			default: logReport.debug("Invalid Browser");
				return;
			} break;
		case "remote":
			DesiredCapabilities cap = new DesiredCapabilities();
			switch (os.toLowerCase()){
			case ("windows"):
				cap.setPlatform(Platform.WINDOWS);
				break;
			case ("mac"):
				cap.setPlatform(Platform.MAC);
				break;
			case ("linux"):
				cap.setPlatform(Platform.LINUX);
				break;	
			default: logReport.debug("Invalid Platform");
			return;
			}
			switch (br.toLowerCase()) {
			case "chrome":  
				cap.setBrowserName("chrome");break;
			case "safari":
				cap.setBrowserName("safari");break;
			default: logReport.debug("Invalid Browser");
				return;
		}
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
		}
		driver.get(URL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
		
	@AfterClass(groups = {"Sanity","Master","Regression"})
	public void tearDown() {
	driver.quit();
	}
	
	public String RandString() {
		String RandomString = RandomStringUtils.randomAlphabetic(5);
		return RandomString;
		}
	public String RandNum() {
		String RandomNum = RandomStringUtils.randomNumeric(7);
		return RandomNum;
		}
	public String RandPwd() {
		String RandPwd = RandomStringUtils.randomAlphanumeric(8);
		return RandPwd;
		}
	public String captureScreenshot(String tname) {
		//Take screenshot of the whole page
	String Datetimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	TakesScreenshot ts = (TakesScreenshot) driver;
    File sourcefile = ts.getScreenshotAs(OutputType.FILE);
    File targetfile = new File(".//screenshots//"+this.getClass().getName()+Datetimestamp+".png");
    sourcefile.renameTo(targetfile);
    return targetfile.getAbsolutePath();
	}
}
