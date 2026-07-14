package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import testBase.BaseClass;

public class TC_SearchProduct_001 extends BaseClass{
	
	@Test(groups= {"Functional","Master"})
	public void TC_SearchProduct_001_1() throws InterruptedException {
		HomePage HomePg = new HomePage(driver);
		logReport.info("Test -Execution started- "+this.getClass().getName());
		HomePg.setSearchdata("iMac");
		HomePg.clickSearchbtn();
		if (HomePg.verifyASearchResultlink())
			{
			logReport.info("Test case passed"+this.getClass().getName());
			Assert.assertEquals(true, true);
		}
			else {
				logReport.error("Test case failed"+this.getClass().getName());
				Assert.assertEquals(false, true);
			}
	}
	}
	
	
