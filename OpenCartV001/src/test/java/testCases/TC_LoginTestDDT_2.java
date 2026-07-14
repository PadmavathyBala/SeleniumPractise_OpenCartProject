package testCases;

import java.time.Duration;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;


public class TC_LoginTestDDT_2 extends BaseClass {
	
	@Test (groups = {"Regression","Master"}, dataProvider = "LoginData", dataProviderClass = DataProviders.class)
	public void LoginTest(String uName,String Pwd,String res ) throws InterruptedException {
		HomePage hp = new HomePage(driver);
		LoginPage lp = new LoginPage(driver);
		MyAccountPage ma = new MyAccountPage(driver);
		hp.clickMyAcccount();
		hp.clicklogin();
		lp.setUserLoginEmail(uName);
		lp.setUserLoginPwd(Pwd);
		lp.clickloginBtn();
		Thread.sleep(Duration.ofSeconds(5));
		if (res.equalsIgnoreCase("Valid")) {
			
			if (ma.verifyAccRLoginConfirmationMsg()){
				ma.clicklogoutBtn();
				logReport.debug("Test Case Passed: "+this.getClass().getName());
				Assert.assertEquals(true, true);
			}
			else {logReport.error("Test Case failed: "+this.getClass().getName());
				Assert.assertEquals(false, true);	
		}
	}
		if (res.equalsIgnoreCase("Invalid")) {
			if (ma.verifyAccRLoginConfirmationMsg()) {
				ma.clicklogoutBtn();
				logReport.error("Test Case Failed: "+this.getClass().getName());
				Assert.assertEquals(false, true);
			}
			else {logReport.debug("Test Case Passed: "+this.getClass().getName());
				Assert.assertEquals(true, true);	
				}
			}
	}
}
	

