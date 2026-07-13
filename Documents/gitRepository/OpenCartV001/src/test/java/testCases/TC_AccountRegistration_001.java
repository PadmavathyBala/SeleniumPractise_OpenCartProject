package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC_AccountRegistration_001 extends BaseClass{
	
	@Test(groups= {"Functional","Master"})
	public void TC_AccountRegistration_001_1() throws InterruptedException {
		HomePage HomePg = new HomePage(driver);
		logReport.info("Test -Execution started- "+this.getClass().getName());
		AccountRegistrationPage AccReg = new AccountRegistrationPage(driver);
		HomePg.clickMyAcccount();
		HomePg.clickRegister();
		Thread.sleep(3000);
		AccReg.setFirstName(RandString());
		AccReg.setLastName(RandString());
		AccReg.setTelephone(RandNum());
		String EmailAdd = RandString()+"@gmail.com";
		AccReg.setEmail(EmailAdd);
		String Pwd = RandPwd();
		AccReg.setPassword(Pwd);
		AccReg.setConfPassword(Pwd);
		AccReg.clickNewsletterCheckBox();
		AccReg.selPrivacyPolicyCheckBox();
		Thread.sleep(3000);
		AccReg.clickcontinue();
		Thread.sleep(3000);
		String AccountRegMsg = "Your Account Has Been Created!";
		String ActMessage = AccReg.verifyAccRegConfirmationMsg();
		if (AccountRegMsg.equals(ActMessage))
			{
			logReport.info("Test case passed");
			Assert.assertEquals(true, true);
		}
			else {
				logReport.error("Test case failed");
				Assert.assertEquals(false, true);
			}
		
	}
	}
	
	
