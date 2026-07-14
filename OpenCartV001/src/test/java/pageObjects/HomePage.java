package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

//Constructor for init
public class HomePage extends BasePage {
	public HomePage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

//locators	
	@FindBy(xpath = "//span[normalize-space()='My Account']") 
	WebElement MyAccLk;
	
	@FindBy(xpath = "//a[text()='Register']") 
	WebElement Reglk;
	
	@FindBy(xpath = "//a[text()='Login']") 
	WebElement Loginlk;
	
	@FindBy(xpath = "//input[@placeholder='Search']") 
	WebElement SearchBox;
	
	@FindBy(xpath = "//button[@class='btn btn-default btn-lg']") 
	WebElement Searchbtn;
	
	//Search results-iMac
	@FindBy(xpath = "//a[text()='iMac']") 
	WebElement SearchResult;
	
	//actions on the locators

	public void clickMyAcccount() {
		MyAccLk.click();
	}
	
	public void clickRegister() {
		Reglk.click();
	}
	
	public void clicklogin() {
		Loginlk.click();
	}
	
	public void setSearchdata(String searchstring) {
		SearchBox.sendKeys(searchstring);
	}
	
	public void clickSearchbtn() {
		Searchbtn.click();
	}
	
	public Boolean verifyASearchResultlink() {
		try { 
			return(SearchResult.isDisplayed());
		}catch (Exception e) {
			return false;
		}	
		}
}
