package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/* Security3
 * 1. 
 */

public class Security3 {

	public static String MSG_WAITMAXMINUTE_ERROR_CHAR = "숫자만 입력 가능합니다.";
	public static String MSG_WAITMAXMINUTE_ERROR_MIN = "10분이상으로 설정해 주세요.";
	public static String MSG_WAITMAXMINUTE_ERROR_MAX = "720분 이하로 설정해 주세요.";
	public static String MSG_WAITMAXMINUTE_TIMEOVER = "참여자가 없어 회의가 자동 종료 됩니다.";
	
	public static WebDriver driver;
	public static WebDriver driver_guest;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		driver_guest = comm.setDriver(driver_guest, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", driver_guest);
		
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);

	}
	
	@BeforeMethod
	public void setBrowser() {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() > 1) {
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));
		}
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			driver.get(CommonValues.MEETING_URL + Security.URL_SECURITY);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
	}
	
	@Test(priority = 1, enabled = true)
	public void SecurityWaitMinute_char() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)));
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).clear();
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).sendKeys("한글");
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)));
		
		if(!driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)).getText().contentEquals(MSG_WAITMAXMINUTE_ERROR_CHAR)) {
			failMsg = "\n1.WAITMAXMINUTE warning tooltip [Expected]" + MSG_WAITMAXMINUTE_ERROR_CHAR
					 + " [Actual]" + driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void SecurityWaitMinute_min() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)));
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).clear();
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).sendKeys("9");
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)));
		
		if(!driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)).getText().contentEquals(MSG_WAITMAXMINUTE_ERROR_MIN)) {
			failMsg = "\n1.WAITMAXMINUTE warning tooltip [Expected]" + MSG_WAITMAXMINUTE_ERROR_MIN
					 + " [Actual]" + driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)).getText();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).clear();
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).sendKeys("730");
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)));
		
		if(!driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)).getText().contentEquals(MSG_WAITMAXMINUTE_ERROR_MAX)) {
			failMsg = "\n1.WAITMAXMINUTE warning tooltip [Expected]" + MSG_WAITMAXMINUTE_ERROR_MAX
					 + " [Actual]" + driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void SecurityWaitMinute_room() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)));
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).clear();
		driver.findElement(By.xpath(Security.XPATH_SECURITY_WAITMAXMINUTE)).sendKeys("10");
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		Security_Role sec = new Security_Role();
		driver.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		String roomID = "";
		try {
			roomID = sec.createMeetingRoom(driver, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n1. fail to create meeting room : " + e.getMessage();
		}
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();

		//10분 대기
		Thread.sleep(600000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_WAITMAXMINUTE_TIMEOVER)) {
			failMsg = failMsg + "\n2. time over popup msg [Expected]" + MSG_WAITMAXMINUTE_TIMEOVER
					 + " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		//바로 종료
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='gnb-lounge']//a[@data-name='HISTORY']")));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = failMsg + "\n3. url after leave. currenturl : " + driver.getCurrentUrl();
		}
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		driver_guest.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}


