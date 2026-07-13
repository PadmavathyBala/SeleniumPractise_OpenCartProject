package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;


public class TC_LoginTest_001 extends BaseClass {
	
	@Test(groups={"Sanity","Master"})
	public void LoginTest() {
		HomePage hp = new HomePage(driver);
		LoginPage lp = new LoginPage(driver);
		MyAccountPage ma = new MyAccountPage(driver);
		hp.clickMyAcccount();
		hp.clicklogin();
		lp.setUserLoginEmail(p.getProperty("UserName"));
		lp.setUserLoginPwd(p.getProperty("Password"));
		lp.clickloginBtn();
		if (ma.verifyAccRLoginConfirmationMsg()){
			logReport.debug(this.getClass().getName()+": Passed");
		} else {
			logReport.debug(this.getClass().getName()+": Failed");
		}
		
		Assert.assertEquals(ma.verifyAccRLoginConfirmationMsg(),true);
		
	}
	

}
