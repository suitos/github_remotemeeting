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

/* AccessLog
 * 1. 접근내역 클릭 뷰 확인
 * 2. 접근 내역 화면 리스트 라벨 확인
 * 3. 접근 내역 - admin 로그인 로그
 * 4. 접근 내역 - 회의 시작 로그
 * 5. 접근 내역 - 회의 참여 로그
 * 6. 접근 내역 - 문서 공유 로그
 * 7. 접근 내역 - 화면 공유 로그
 * 8. 접근 내역 - 회의록 공유 로그
 * 9. 접근 내역 - 녹화 로그
 * 10. 접근 내역 - 보안옵션 변경 로그
 */

public class AccessLog {

	public static String XPATH_ACCESSLOG_LIST = "//tbody[@id='accessLogBody']/tr";
	
	public static String ACCESSLOGTYPE[] = {"관리자로그인", "회의시작", "회의참여(비회원)", "문서 공유", "화면 공유", "회의록 공유", "녹화 시작", "보안옵션변경"};
	
	public static String URL_ACCESSLOG = "/customer/security-accesslog";
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
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_ACCESSLOG)) {
			driver.get(CommonValues.MEETING_URL + URL_ACCESSLOG);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		}
	}
	
	@Test(priority = 1, enabled = true)
	public void AccessLog_view() throws Exception {
		String failMsg = "";
		
		//접근내역 클릭
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_ACCESSLOG)) {
			failMsg = failMsg + "\n1. not access log view. current url : " + driver.getCurrentUrl();
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void AccessLog_listLabel() throws Exception {
		String failMsg = "";
		
		//리스트 라벨 확인
		
		List<WebElement> labellist = driver.findElements(By.xpath("//table[@class='table  table-striped dataTable responsive-table']//tr/th"));
		
		String labels[] = {"날짜", "이메일", "이름", "접속 단말기", "행동", "ip", "접속위치"};
		
		for (int i = 0; i < labellist.size(); i++) {
			if(!labellist.get(i).getText().contentEquals(labels[i])) {
				failMsg = failMsg + "\n" + i + ". colum label [Expected]" + labels[i]
						 + " [Actual]" + labellist.get(i).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void AccessLog_login() throws Exception {
		String failMsg = "";
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[0])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[0];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void AccessLog_startMeeting() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"about:blank\");");
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[1])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[1];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void AccessLog_joinMeeting() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"about:blank\");");
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[2])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[2];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void AccessLog_shareDoc() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"about:blank\");");
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCSHARE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_DOCUPLOAD_BTN)));
		String filepath = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCUPLOAD_INPUT)).sendKeys(filepath);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_DOCCONTENT_VIEW)));
		
		Thread.sleep(1000);
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[3])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[3];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void AccessLog_shareScreen() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"about:blank\");");
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_SCREENSHARE_VIEWDESC)));

		Thread.sleep(1000);
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[4])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[4];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void AccessLog_shareNote() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"about:blank\");");
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)));
		//driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).clear();
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(DashBoard.ID_ADM);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//div[@id='note-share-wrap']//button[@type='submit']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));

		Thread.sleep(1000);
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[5])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[5];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void AccessLog_recording() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open(\"about:blank\");");
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-body']")));
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();

		Thread.sleep(1000);
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		Users user = new Users();
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[6])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[6];
				}
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void AccessLog_securityOption() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		//보안 화면 이동
		user.selectSideMenu(driver, 5, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		
		//보안 옵션 - ai 설정 변경 
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_UNSETAI)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_UNSETAI)).click();
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//다시 원복
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_UNSETAI)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SETAI)).click();
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		// 접근내역 화면 이동
		user.selectSideMenu(driver, 5, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ACCESSLOG_LIST)));
		
		//로그인 로그 확인
		List<WebElement> list = driver.findElements(By.xpath(XPATH_ACCESSLOG_LIST));
		if(list.size() <= 0 ) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			boolean findlog = false;
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[5]")).getText().contentEquals(ACCESSLOGTYPE[7])) {
					findlog = true;
					break;
				}
				if(!findlog) {
					failMsg = failMsg + "\n2. cannot find access log. logtype : " + ACCESSLOGTYPE[7];
				}
			}
		}
	
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


