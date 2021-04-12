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

/* Security2
 * 1. 회의 생성 가능시간 - 현재시간 기준 가능하도록 설정후 확인 -> 불가능한 시간으로 설정후 확인
 * 2. 회의 생성 가능시간 - 제한없음 설정 후 확인
 * 3. 문서 공유 기능 제한 - 전체 제한 설정 후 사용할 수 없음 확인
 * 4. 문서 공유 기능 제한 - 외부인 참여시 제한 : 게스트 참여 룸에서 사용할 수 없음 확인
 * 5. 문서 공유 기능 제한 - 제한 없음 : 게스트 참여 룸에서 사용가능 확인
 * 6. 화면 공유 기능 제한 - 전체 제한 설정 후 사용할 수 없음 확인
 * 7. 화면 공유 기능 제한 - 외부인 참여시 제한 : 게스트 참여 룸에서 사용할 수 없음 확인
 * 8. 화면 공유 기능 제한 - 제한 없음 : 게스트 참여 룸에서 사용가능 확인
 * 9. 녹화 기능 제한 - 전체 제한 설정 후 사용할 수 없음 확인
 * 10. 녹화 기능 제한 - 외부인 참여시 제한 : 로그인 유저참여 룸에서 사용할 수 없음 확인
 * 11. 녹화 기능 제한 - 제한 없음 : 로그인 유저참여 참여 룸에서 사용가능 확인
 * 12. 녹화 다운로드 제한 - 전체 제한 설정 후 사용할 수 없음 확인(히스토리에서 확인)
 * 13. 녹화 다운로드 제한 - 제한없음 설정 후 다운로드 가능 확인(히스토리에서 확인)
 * 14. 회의록 공유 기능 제한 - 전체 제한 설정 후 룸에서 회의록 공유 사용할수 없음 확인
 * 15. 회의록 공유 기능 제한 - 전체 제한 설정 후 히스토리에서 해당 회의 회의록 공유 할수 없음 확인
 * 16. 회의록 공유 기능 제한 - 외부인 참여시 제한 : 회의실에 외부인 참여시 회의록 공유 기능 사용할 수 없음 확인
 * 17. 회의록 공유 기능 제한 - 제한 없음 : 회의실에서 회의록 공유 기능 사용 가능 확인(어드민, 타그룹유저)
 */

public class Security2 {
	public static String XPATH_SECURITY_MEETING_TIME_START = "//select[@id='startHour']";
	public static String XPATH_SECURITY_MEETING_TIME_END = "//select[@id='endHour']";
	
	public static String XPATH_SECURITY_DOC_UNLIMIT= "//select[@id='endHour']";
	
	public static String MSG_SECURITY_NOTALLOWED_TIME = "회의 가능시간이 아닙니다.\n"
			+ "그룹 관리자에게 문의하세요.\n"+ "[Code: 40220]";
	public static String MSG_SECURITY_NOTALLOWEDFUNC = "사용 권한이 없습니다.\n"+ "그룹 관리자에게 문의하세요.";
	public static String MSG_SECURITY_NOTALLOWEDFUNC_GUEST = "회의 주최자의 그룹에서 사용 제한 설정된 기능입니다.";
	public static String MSG_SECURITY_REC_USERLIMIT = "외부인(타그룹, 비회원)의 참여로 사용 권한이 제한되었습니다.\n"+ "그룹 관리자에게 문의하세요.";
	public static String MSG_SECURITY_REC_CONFIRM = "녹화를 시작하시겠습니까? 녹화된 영상은 히스토리 메뉴에서 확인할 수 있습니다.";
	public static String MSG_SECURITY_NOTESHARE_TITLE = "회의록 공유";
	
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
	public void SecurityTime_limit() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_TIMELIMIT + "/input")));
		//driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_TIMELIMIT + "/input")).sendKeys(Keys.ENTER);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_MEETING_TIME_START)));
		
		LocalDateTime nowDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
		int hour = Integer.parseInt(nowDateTime.format(formatter));

		int startT = 0;
		int endT = 0;
		
		//allow time set 현재시간 기준 가능하도록 설정
		if(hour >= 23) {
			startT = hour - 1;
			endT = 0;
		} else {
			startT = hour;
			endT = hour + 1;
		}
		
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_START)).click();
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_START + "/option[" + (startT + 1) + "]")).click();
		
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_END)).click();
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_END + "/option[" + (endT + 1) + "]")).click();
		
		//save
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//check create seminar(admin)
		Security_Role sec = new Security_Role();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		CommonValues comm = new CommonValues();
		comm.login(driver_guest, CommonValues.ADMEMAIL, CommonValues.USERPW);
		try {
			sec.createMeetingRoom(driver_guest, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n1. fail to create meeting room : " + e.getMessage();
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		// allow time set 현재시간 기준 불가능하도록 설정
		if (hour >= 1) {
			startT = hour - 1;
			endT = hour;
		} else {
			startT = hour + 1;
			endT = hour + 2;
		}
		
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_START)).click();
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_START + "/option[" + (startT + 1) + "]")).click();
		
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_END)).click();
		driver.findElement(By.xpath(XPATH_SECURITY_MEETING_TIME_END + "/option[" + (endT + 1) + "]")).click();
		
		//save
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		comm.login(driver_guest, CommonValues.ADMEMAIL, CommonValues.USERPW);
		try {
			String msg = sec.createMeetingRoom(driver_guest, false);
			
			if(!msg.contentEquals(MSG_SECURITY_NOTALLOWED_TIME)) {
				failMsg = failMsg + "\n2. not allowed to create meeting msg [Expected]" + MSG_SECURITY_NOTALLOWED_TIME
						 + " [Actual]" + msg;
			}

		} catch (Exception e) {
			failMsg = failMsg + "\n3. fail to create meeting room : " + e.getMessage();
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void SecurityTime_unlimit() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}

		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_TIMEUNLIMIT + "/input")));
		Thread.sleep(500);
		
		//save
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//check create seminar(admin)
		Security_Role sec = new Security_Role();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		CommonValues comm = new CommonValues();
		comm.login(driver_guest, CommonValues.ADMEMAIL, CommonValues.USERPW);
		try {
			sec.createMeetingRoom(driver_guest, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n1. fail to create meeting room : " + e.getMessage();
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void SecurityDoc_limitAll() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_DOCLIMITALL)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//check create seminar(admin)
		Security_Role sec = new Security_Role();
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		CommonValues comm = new CommonValues();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		comm.login(driver_guest, CommonValues.ADMEMAIL, CommonValues.USERPW);
		try {
			sec.createMeetingRoom(driver_guest, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n1. fail to create meeting room : " + e.getMessage();
		}
		driver_guest.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();
		driver_guest.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCSHARE_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver_guest.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void SecurityDoc_limitUser() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_DOCLIMITUSER)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCSHARE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='doc-upload-btn']")));
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		driver_guest.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCSHARE_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC_GUEST)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC_GUEST
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver_guest.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void SecurityDoc_unlimit() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_DOCUNLIMIT)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCSHARE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='doc-upload-btn']")));
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		driver_guest.findElement(By.xpath(CommonValues.XPATH_ROOM_DOCSHARE_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='doc-upload-btn']")));
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void SecurityScreen_limitAll() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SCREENLIMITALL)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void SecurityScreen_limitUser() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SCREENLIMITUSER)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		driver_guest.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC_GUEST)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC_GUEST
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver_guest.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void SecurityScreen_unlimit() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SCREENUNLIMIT)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		driver_guest.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		driver_guest.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void SecurityRec_limitAll() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_RECLIMITALL)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		try {
			sec.enterRoom(driver_guest, roomID, false, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void SecurityRec_limitUser() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_RECLIMITUSER)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		CommonValues comm = new CommonValues();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			sec.enterRoom(driver_guest, roomID, true, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_REC_USERLIMIT)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_REC_USERLIMIT
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		driver_guest.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC_GUEST)) {
			failMsg = failMsg + "\n1. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC_GUEST
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver_guest.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void SecurityRec_unlimit() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_RECUNLIMIT)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		CommonValues comm = new CommonValues();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			sec.enterRoom(driver_guest, roomID, true, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-body']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-body']//p")).getText().contentEquals(MSG_SECURITY_REC_CONFIRM)) {
			failMsg = failMsg + "\n3. confirm popup msg [Expected]" + MSG_SECURITY_REC_CONFIRM
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-body']//p")).getText();
		}
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		
		wait_guest.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_RECORDING_BTN)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-body']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-body']//p")).getText().contentEquals(MSG_SECURITY_REC_CONFIRM)) {
			failMsg = failMsg + "\n4. confirm popup msg [Expected]" + MSG_SECURITY_REC_CONFIRM
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-body']//p")).getText();
		}
		driver_guest.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	
	@Test(priority = 12, enabled = true)
	public void SecurityRecDown_limit() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_RECDOIWNLOADLIMIT)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		CommonValues comm = new CommonValues();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			sec.enterRoom(driver_guest, roomID, true, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-body']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-body']//p")).getText().contentEquals(MSG_SECURITY_REC_CONFIRM)) {
			failMsg = failMsg + "\n3. confirm popup msg [Expected]" + MSG_SECURITY_REC_CONFIRM
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-body']//p")).getText();
		}
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORDING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		Thread.sleep(1000);
		
		//둘다 나가기
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='gnb-lounge']//a[@data-name='HISTORY']")));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='gnb-lounge']//a[@data-name='HISTORY']")));
		
		Thread.sleep(10000);
		
		//history click (admin)
		driver.findElement(By.xpath("//ul[@id='gnb-lounge']//a[@data-name='HISTORY']")).click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		List<WebElement> historyList = driver.findElements(By.xpath("//div[@id='content']/ul/li"));
		if(historyList.size() == 0 ) {
			failMsg = failMsg + "\n4. history list is empty";
		} else {
			historyList.get(0).findElement(By.xpath(".//a[@class='video-recordings detail-wrapper']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='video-player']")));
			try {
				driver.findElement(By.xpath("//div[@id='record-download']")).click();
				failMsg = failMsg + "\n5. clickable button (admin user)";
			} catch (Exception e) {
				// do not anything
			}
			
		}
		
		//history click (other group user)
		driver_guest.findElement(By.xpath("//ul[@id='gnb-lounge']//a[@data-name='HISTORY']")).click();
		Thread.sleep(1000);
		wait_guest.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		historyList = driver_guest.findElements(By.xpath("//div[@id='content']/ul/li"));
		if(historyList.size() == 0 ) {
			failMsg = failMsg + "\n6. history list is empty";
		} else {
			historyList.get(0).findElement(By.xpath(".//a[@class='video-recordings detail-wrapper']")).click();
			wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='video-player']")));
			try {
				driver_guest.findElement(By.xpath("//div[@id='record-download']")).click();
				failMsg = failMsg + "\n7. clickable button (admin user)";
			} catch (Exception e) {
				// do not anything
			}
			
		}

		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 13, dependsOnMethods = {"SecurityRecDown_limit"}, alwaysRun = true, enabled = true)
	public void SecurityRecDown_Allow() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_RECDOIWNLOAD)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		
		//history click (admin)
		driver.get(CommonValues.MEETING_URL + CommonValues.HISTORY_URL);
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		List<WebElement> historyList = driver.findElements(By.xpath("//div[@id='content']/ul/li"));
		if(historyList.size() == 0 ) {
			failMsg = failMsg + "\n4. history list is empty";
		} else {
			historyList.get(0).findElement(By.xpath(".//a[@class='video-recordings detail-wrapper']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='video-player']")));
			try {
				driver.findElement(By.xpath("//div[@id='record-download']")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='dialog-body']")));
				driver.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
			} catch (Exception e) {
				failMsg = failMsg + "\n5. cannot download video file.";
			}
			
		}
		
		//history click (other group user)
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		if(!driver_guest.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.HISTORY_URL)) {
			driver_guest.findElement(By.xpath("//ul[@id='gnb-lounge']//a[@data-name='HISTORY']")).click();
		} else {
			driver_guest.navigate().refresh();
		}
		Thread.sleep(1000);
		wait_guest.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		historyList = driver_guest.findElements(By.xpath("//div[@id='content']/ul/li"));
		if(historyList.size() == 0 ) {
			failMsg = failMsg + "\n6. history list is empty";
		} else {
			historyList.get(0).findElement(By.xpath(".//a[@class='video-recordings detail-wrapper']")).click();
			wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='video-player']")));
			try {
				driver_guest.findElement(By.xpath("//div[@id='record-download']")).click();
				wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='dialog-body']")));
				driver_guest.findElement(By.xpath("//div[@class='button-box']/button[2]")).click();
			} catch (Exception e) {
				failMsg = failMsg + "\n7. cannot download video file.";
			}
			
		}
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		CommonValues comm = new CommonValues();
		comm.logout(driver_guest);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void SecurityNoteShare_limitAll() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SHARENOTELIMITALL)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
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
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC)) {
			failMsg = failMsg + "\n3. share note title [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void SecurityNoteShare_limitAll_history() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SHARENOTELIMITALL)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));

		driver.get(CommonValues.MEETING_URL + CommonValues.HISTORY_URL);
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		List<WebElement> historyList = driver.findElements(By.xpath("//div[@id='content']/ul/li"));
		if(historyList.size() == 0 ) {
			failMsg = failMsg + "\n2. history list is empty";
		} else {
			historyList.get(0).findElement(By.xpath(".//div[@class='left']/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-btn='share-note']")));
			driver.findElement(By.xpath("//button[@data-btn='share-note']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='note-share-input']")));
			driver.findElement(By.xpath("//input[@id='note-share-input']")).sendKeys(DashBoard.ID_ADM);
			driver.findElement(By.xpath("//input[@id='note-share-input']")).sendKeys(Keys.ENTER);
			driver.findElement(By.xpath("//section[@id='note-share']//button[@type='submit']")).click();
			
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			if(!alert.getText().contains(MSG_SECURITY_NOTALLOWEDFUNC)) {
				failMsg = "\n3. alert msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC
						 + " [Actual]" + alert.getText();
			}
			alert.accept();
		}
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void SecurityNoteShare_limitUser() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SHARENOTELIMITUSER)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		//혼자있을때 공유 기능 확인
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)));
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTESHARE_TITLE)) {
			failMsg = failMsg + "\n2. not allowed function msg [Expected]" + MSG_SECURITY_NOTESHARE_TITLE
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='dialog-close']")).click();
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		CommonValues comm = new CommonValues();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			sec.enterRoom(driver_guest, roomID, true, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n3. fail to enter the room (guest)" + e.getMessage();
		}
		
		//admin 확인
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)));
		js.executeScript("arguments[0].click();",driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)));
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_REC_USERLIMIT)) {
			failMsg = failMsg + "\n4. not allowed function msg [Expected]" + MSG_SECURITY_REC_USERLIMIT
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();

		//다른 유저 확인
		driver_guest.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		JavascriptExecutor js2 = (JavascriptExecutor) driver_guest;
		js2.executeScript("arguments[0].click();", driver_guest.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")));
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)));
		js2.executeScript("arguments[0].click();", driver_guest.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)));
		
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTALLOWEDFUNC_GUEST)) {
			failMsg = failMsg + "\n5. not allowed function msg [Expected]" + MSG_SECURITY_NOTALLOWEDFUNC_GUEST
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver_guest.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void SecurityNoteShare_unlimit() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath(Security.XPATH_SECURITY_RADIO_SHARENOTEUNLIMIT)).click();
		Thread.sleep(500);
		
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//create meeting room(admin)
		
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
		//혼자있을때 공유 기능 확인X
		
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		CommonValues comm = new CommonValues();
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			sec.enterRoom(driver_guest, roomID, true, true);

		} catch (Exception e) {
			failMsg = failMsg + "\n2. fail to enter the room (guest)" + e.getMessage();
		}
		
		//admin 확인
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)));
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTESHARE_TITLE)) {
			failMsg = failMsg + "\n2. note share popup title [Expected]" + MSG_SECURITY_NOTESHARE_TITLE
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver.findElement(By.xpath("//button[@id='dialog-close']")).click();
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();

		//다른 유저 확인
		driver_guest.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		JavascriptExecutor js2 = (JavascriptExecutor) driver_guest;
		js2.executeScript("arguments[0].click();", driver_guest.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")));
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)));
		js2.executeScript("arguments[0].click();", driver_guest.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)));
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		if(!driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_SECURITY_NOTESHARE_TITLE)) {
			failMsg = failMsg + "\n3. note share popup title [Expected]" + MSG_SECURITY_NOTESHARE_TITLE
					+ " [Actual]" + driver_guest.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		driver_guest.findElement(By.xpath("//button[@id='dialog-close']")).click();
		driver_guest.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
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


