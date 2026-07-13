package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage{

	public MyAccountPage(WebDriver driver) {
		super(driver);
	}
	//Msg webelement
	@FindBy(xpath = "//h2[text()='My Account']") 
	WebElement MyAcctMsg;
	
	//Logout Button
	@FindBy(xpath = "//a[@class='list-group-item'][normalize-space()='Logout']")
	WebElement LogoutBtn;
	
	public Boolean verifyAccRLoginConfirmationMsg() {
		try { 
			return(MyAcctMsg.isDisplayed());
		}catch (Exception e) {
			return false;
		}	
		}
	public void clicklogoutBtn() {
		LogoutBtn.click();
	}
}
