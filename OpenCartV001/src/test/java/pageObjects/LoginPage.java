package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{
	public LoginPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

//locators	
	@FindBy(xpath = "//input[@id='input-email']") 
	WebElement TxtLoginEmail;
	
	@FindBy(xpath = "//input[@id='input-password']") 
	WebElement TxtLoginPwd;
	
	@FindBy(xpath = "//input[@type='submit']") 
	WebElement LoginBtn;
	
	
	//actions on the locators
	//Enter user information
	public void setUserLoginEmail(String UserEmail) {
		TxtLoginEmail.sendKeys(UserEmail);
	}
	
	public void setUserLoginPwd(String UserPwd) {
		TxtLoginPwd.sendKeys(UserPwd);
	}
	
	public void clickloginBtn() {
		LoginBtn.click();
	}
	

	
}

