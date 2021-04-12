package mandatory;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import admin.Connect;

public class P2PEnterprise_Admin {

	public static String HREF_PROFILE = "//a[@href='/customer/dashboard']";
	public static String ADMIN_URI = "/rmadmin";
	public static String DASHBOARD_URI = "/customer/dashboard";
	
	public static WebDriver driver;
	
	public String[] user1 = {CommonValues.ADMEMAIL, CommonValues.ADMINNICKNAME};
	public String[] user2 = {CommonValues.USERS[0], CommonValues.USERS[0].replace("@gmail.com", "")};
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		if(CommonValues.MEETING_URL.contentEquals(CommonValues.MEETING_URL_REAL6)) {
			user1[0] = "auto1@rsupport.com";
			user1[1] = "자동화1";
			user2[0] = "auto2@rsupport.com";
			user1[1] = "자동화2";
		} 
		
		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
	}
	
	@Test(priority = 1)
	public void Login_Mode() throws Exception {
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(driver, user1[0], CommonValues.USERPW);
	}

	@Test(priority = 2)
	public void GoAdmin() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(HREF_PROFILE)).click();
		
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(1));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + DASHBOARD_URI)) {
			failMsg = "Can't enter Admin Dashboard [Expected]" + CommonValues.MEETING_URL + DASHBOARD_URI
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_PROFILE_INFO_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_ADMIN_LOGOUT_BTN)));
		
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGOUT_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-box']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + ADMIN_URI)) {
			failMsg = "Can't enter Admin Login [Expected]" + CommonValues.MEETING_URL + ADMIN_URI
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
