package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

/* GroupInfo
 * 1. 
 */

public class GroupInfo2 {
	public static String XPATH_GROUPINFO_DELETEGROUP_PW = Connect.XPATH_MODAL_BODY +  "//input[@id='password']";
	public static String XPATH_GROUPINFO_DELETEGROUP_PW_EMPTYERROR = Connect.XPATH_MODAL_BODY +  "//div[@id='password-error']";
	public static String XPATH_GROUPINFO_DELETEGROUP_PW_MISSMATCHERROR = Connect.XPATH_MODAL_BODY +  "//div[@id='password-not-match']";
	
	public static String MSG_GROUPINFO_DELETEGROUP_POPUP = "그룹 삭제 및 회원 탈퇴하면 모든 데이터가 삭제됩니다.\n"
			+ "90일 이내에 관리자 및 사용자 계정과 동일한 이메일로\n"+ "재가입 할 수 없습니다.\n\n"+ "계속 하시겠습니까?";
	public static String MSG_GROUPINFO_DELETEGROUP_MISSMATCHPW = "비밀번호가 일치하지 않습니다.";
			
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
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
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + GroupInfo.URL_GROUPINFO)) {
			driver.get(CommonValues.MEETING_URL + GroupInfo.URL_GROUPINFO);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GroupInfo.XPATH_GROUPINFO_VIEW_SUBTITLE)));
		} else {
			driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).clear();
			driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).sendKeys(GroupInfo.GROUPNAME);
			driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).clear();
			driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).sendKeys("0100000000");
		}
	}
	
	@Test(priority = 1, enabled = true)
	public void GroupInfo_GroupDeletePopup() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPDELETE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPDELETE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getText().contentEquals(MSG_GROUPINFO_DELETEGROUP_POPUP)) {
			failMsg = failMsg + "\n1. delete group confirm popup msg [Expected]" + MSG_GROUPINFO_DELETEGROUP_POPUP
					 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getText();
		}
		
		if(!driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).getText().contentEquals("확인")) {
			failMsg = failMsg + "\n2. delete group confirm popup button1 [Expected]" + "확인"
					 + " [Actual]" + driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y));
		}
		if(!driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).getText().contentEquals("취소")) {
			failMsg = failMsg + "\n3. delete group confirm popup button2 [Expected]" + "취소"
					 + " [Actual]" + driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N));
		}
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void GroupInfo_GroupDelete_emptyPW() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPDELETE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPDELETE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW)).clear();
		
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW_EMPTYERROR)));
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW_EMPTYERROR)).getText().contentEquals(Users2.MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n1. group delete confirm popup - empty pw msg [Expected]" + Users2.MSG_MISS_ESSENTIAL
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW_EMPTYERROR)).getText();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void GroupInfo_GroupDelete_invalidPW() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPDELETE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPDELETE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW)).sendKeys("123123");
		
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW_MISSMATCHERROR)));
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW_MISSMATCHERROR)).getText().contentEquals(MSG_GROUPINFO_DELETEGROUP_MISSMATCHPW)) {
			failMsg = failMsg + "\n1. group delete confirm popup - invalid pw msg [Expected]" + MSG_GROUPINFO_DELETEGROUP_MISSMATCHPW
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_DELETEGROUP_PW_MISSMATCHERROR)).getText();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void GroupInfo_save() throws Exception {
		String failMsg = "";
		
		String name_mod = "modgroup";
		String phone_mod = "0101111111";
		String repname_mod = "test123";
		String cono_mod = "123456789";
		
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).sendKeys(name_mod);
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).sendKeys(phone_mod);
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_REPNAME)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_REPNAME)).sendKeys(repname_mod);
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_CONUMBER)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_CONUMBER)).sendKeys(cono_mod);
		
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if(!driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).getAttribute("value").contentEquals(name_mod)) {
			failMsg = failMsg + "\n1. saved group name [Expected]" + name_mod
					+ " [Actual]" + driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).getAttribute("value");
		}
		if(!driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).getAttribute("value").contentEquals(phone_mod)) {
			failMsg = failMsg + "\n2. saved phone [Expected]" + phone_mod
					+ " [Actual]" + driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).getAttribute("value");
		}
		if(!driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_REPNAME)).getAttribute("value").contentEquals(repname_mod)) {
			failMsg = failMsg + "\n3. saved representativeName [Expected]" + repname_mod
					+ " [Actual]" + driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_REPNAME)).getAttribute("value");
		}
		if(!driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_CONUMBER)).getAttribute("value").contentEquals(cono_mod)) {
			failMsg = failMsg + "\n4. saved corporateNumber [Expected]" + cono_mod
					+ " [Actual]" + driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_CONUMBER)).getAttribute("value");
		}
		
		//기본 데이터로 원복
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_GROUPNAME)).sendKeys(GroupInfo.GROUPNAME);
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_PHONE)).sendKeys("0100000000");
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_REPNAME)).clear();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_CONUMBER)).clear();
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		
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
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}


