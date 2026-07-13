package pageObjects;
import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class AccountRegistrationPage extends BasePage  {

	//Constructor for init
		public AccountRegistrationPage(WebDriver driver) {
			super(driver);
		}
	//locators	
		@FindBy(xpath = "//input[@id='input-firstname']") 
		WebElement TxtFirstName;
		
		@FindBy(xpath = "//input[@id='input-lastname']") 
		WebElement TxtLastName;
		
		@FindBy(xpath = "//input[@id='input-email']") 
		WebElement TxtEmail;
		
		@FindBy(xpath = "//input[@id='input-telephone']") 
		WebElement TxtTelephone;
		
		@FindBy(xpath = "//input[@id='input-password']") 
		WebElement TxtPassword;
		
		@FindBy(xpath = "//input[@id='input-confirm']") 
		WebElement TxtConfPassword;
		
		
		@FindBy(xpath = "//input[@value='0']") 
		WebElement ChkBxNewsletter;
		
		@FindBy(xpath = "//input[@name='agree']") 
		WebElement ChkbxPrivacyPolicy;
		
		@FindBy(xpath = "//input[@value='Continue']") 
		WebElement BtnContinue;
		
		@FindBy(xpath = "//h1[text()='Your Account Has Been Created!']") 
		WebElement MsgConfirmation;
		
		//actions on the locators
		public void setFirstName(String FirstName) {
			TxtFirstName.sendKeys(FirstName);
		}
		
		public void setLastName(String LastName) {
			TxtLastName.sendKeys(LastName);
		}
		public void setEmail(String Email) {
			TxtEmail.sendKeys(Email);
		}
		public void setTelephone(String Telephone) {
			TxtTelephone.sendKeys(Telephone);
		}
		public void setPassword(String Password) {
			TxtPassword.sendKeys(Password);
		}
		public void setConfPassword(String Password) {
			TxtConfPassword.sendKeys(Password);
		}
		
		public void clickNewsletterCheckBox() {
			
			ChkBxNewsletter.click();
		}
		public void selPrivacyPolicyCheckBox() {
			
			ChkbxPrivacyPolicy.click();
		}
		public void clickcontinue() {
			
			BtnContinue.click();
		}
		
		public String verifyAccRegConfirmationMsg() {
			try { 
				return MsgConfirmation.getText();
			}catch (NoSuchElementException e) {
				return e.getMessage();
			}	
			}
		}
		


