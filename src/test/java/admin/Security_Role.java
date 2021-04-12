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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/* Security_Role
 *
 */

public class Security_Role {
	public static String XPATH_SECURITY_ROLES = "//div[@class='wrapper-radio input-group width-max conference-access']/div[@class='form-item-wrap radio-wrap']";
	public static String XPATH_SECURITY_ROLES_GUIDE = "//div[@class='wrapper-radio input-group width-max conference-access']/div[@class='guide-text']";
	
	public static String XPATH_ROOM_NOTE_TAB = "//ul[@class='wrap-tab-menu']";
	
	public static String XPATH_SECURITY_AI_RADIO_ALLOW = "//div[@class='panel-inner panel-center']/div[9]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_AI_RADIO_NOTALLOW = "//div[@class='panel-inner panel-center']/div[9]//div[@class='wrapper-radio input-group width-max']/div[2]";

	public static String MSG_SECURITY_NOTPERMIT_GUEST = "로그인 사용자만 접속 가능한 회의입니다.\n"
			+ "로그인 후 재 접속 해주세요.\n"+ "[Code: 40233]";
	public static String MSG_SECURITY_NOTPERMIT_OTHER = "동일한 그룹 사용자만 접속 가능한 회의입니다.\n"+ "[Code: 40234)";
	public static String MSG_ROLE_GUIDE_LOGIN = "* 리모트미팅 계정을 갖고 있는 사용자만 회의 입장이 가능합니다. (Guest 참여 불가)";
	public static String MSG_ROLE_GUIDE_GROUP = "* 등록된 그룹 사용자만 회의 입장이 가능합니다.";
	public static String MSG_ROLE_GUIDE_GROUPWITHGUEST = "* 등록된 그룹 사용자만 회의 입장이 가능합니다.\n"
			+ "* Guest 참여 가능 선택 시, Guest가 회의에 참여 가능합니다. (다른 그룹 사용자 참여 불가)\n"
			+ "* 동일 그룹 사용자에게만 회의 히스토리가 공유 됩니다.";
	public static String MSG_NOTALLOW_AINOTE = "관리자 페이지에서 AI기록 제한을 제한없음으로 변경해야 합니다.";
	
	public static String[] SECURITY_ROLES = {"제한 없음", "로그인 사용자", "그룹 사용자", "그룹 사용자 + Guest"};
	
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

	}
	
	@Test(priority = 1, enabled = true)
	public void SecurityRole() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 설정
		Users user = new Users();
		user.selectSideMenu(driver, 5, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		//권한 radiobox 확인
		List<WebElement> roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		
		if(roles.size() != 4) {
			failMsg = failMsg + "\n2. role radio box is not 4. actual : " + roles.size();
		} else {
			int selected = 0;
			for (int i = 0; i < roles.size() ;  i++) {
				if(!roles.get(i).findElement(By.xpath("./label")).getText().contentEquals(SECURITY_ROLES[i])) {
					failMsg = failMsg + "\n3-" + i + ". role label [Expected]" + SECURITY_ROLES[i]
							 + " [Actual]" + roles.get(i).findElement(By.xpath("./label")).getText();
				}
				
				if(roles.get(i).findElement(By.xpath("./input")).isSelected()) selected ++;
			}
			
			if(selected != 1 ) {
				failMsg = failMsg + "\n5. selected radio box is not 1. [Actual]" + selected ;
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void SecurityRole_all() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//권한 radiobox 확인
		List<WebElement> roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		
		roles.get(0).click();
		Thread.sleep(500);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//새탭
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		String roomID = createMeetingRoom(driver, true);
		
		try {
			enterRoom(driver_guest, roomID, false, true);
		} catch (Exception e) {
			failMsg = failMsg + "\n1. fail to enter the room (guest) : " + e.getMessage();
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, dependsOnMethods = {"SecurityRole_all"}, alwaysRun = true, enabled = true)
	public void SecurityRole_loginUser() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//권한 radiobox 확인
		List<WebElement> roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		
		roles.get(1).click();
		Thread.sleep(500);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_ROLES_GUIDE)));
		if(!driver.findElement(By.xpath(XPATH_SECURITY_ROLES_GUIDE)).getText().contentEquals(MSG_ROLE_GUIDE_LOGIN)) {
			failMsg = failMsg + "\n0. role guide [Expected]" + MSG_ROLE_GUIDE_LOGIN
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_SECURITY_ROLES_GUIDE)).getText();
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//check radio
		roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		if (!roles.get(1).findElement(By.xpath("./input")).isSelected()) {
			failMsg = "\n1. role value is not selected.";
		}
		
		//새탭
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		String roomID = createMeetingRoom(driver, true);
		
		//guest
		try {
			String msg = enterRoom(driver_guest, roomID, false, false);
			if(msg.contentEquals(MSG_SECURITY_NOTPERMIT_GUEST)) {
				failMsg = failMsg + "\n2. enter room error warning (guest) [Expected]" + MSG_SECURITY_NOTPERMIT_GUEST
						 + " [Actual]" + msg;
			}
		} catch (Exception e) {
			failMsg = failMsg + "\n3. fail to enter the room (guest) : " + e.getMessage();
		}
		
		//login user
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		CommonValues comm = new CommonValues();
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			enterRoom(driver_guest, roomID, true, true);
		} catch (Exception e) {
			failMsg = failMsg + "\n4. fail to enter the room (login user) : " + e.getMessage();
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		comm.logout(driver_guest);
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, dependsOnMethods = {"SecurityRole_loginUser"}, alwaysRun = true, enabled = true)
	public void SecurityRole_group() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//권한 radiobox 확인
		List<WebElement> roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		
		roles.get(2).click();
		Thread.sleep(500);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_ROLES_GUIDE)));
		if(!driver.findElement(By.xpath(XPATH_SECURITY_ROLES_GUIDE)).getText().contentEquals(MSG_ROLE_GUIDE_GROUP)) {
			failMsg = failMsg + "\n0. role guide [Expected]" + MSG_ROLE_GUIDE_GROUP
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_SECURITY_ROLES_GUIDE)).getText();
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//check radio
		roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		if (!roles.get(2).findElement(By.xpath("./input")).isSelected()) {
			failMsg = "\n1. role value is not selected.";
		}
		
		//새탭
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		String roomID = createMeetingRoom(driver, true);
		
		//guest
		try {
			String msg = enterRoom(driver_guest, roomID, false, false);
			if(msg.contentEquals(MSG_SECURITY_NOTPERMIT_GUEST)) {
				failMsg = failMsg + "\n2. enter room error warning (guest) [Expected]" + MSG_SECURITY_NOTPERMIT_GUEST
						 + " [Actual]" + msg;
			}
		} catch (Exception e) {
			failMsg = failMsg + "\n3. fail to enter the room (guest) : " + e.getMessage();
		}
		
		//login user (other group)
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		CommonValues comm = new CommonValues();
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			String msg = enterRoom(driver_guest, roomID, true, false);
			if(msg.contentEquals(MSG_SECURITY_NOTPERMIT_OTHER)) {
				failMsg = failMsg + "\n4. enter room error warning (other group) [Expected]" + MSG_SECURITY_NOTPERMIT_OTHER
						 + " [Actual]" + msg;
			}

		} catch (Exception e) {
			failMsg = failMsg + "\n5. fail to enter the room (other group) : " + e.getMessage();
			takescreenshot(driver_guest, "room.png");
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		//login user (same group)
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		comm.login(driver_guest, CommonValues.USERS[0], CommonValues.USERPW);
		try {
			enterRoom(driver_guest, roomID, true, true);
		} catch (Exception e) {
			failMsg = failMsg + "\n6. fail to enter the room (same group) : " + e.getMessage();
		}
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
	
	@Test(priority = 5, dependsOnMethods = {"SecurityRole_group"}, alwaysRun = true, enabled = true)
	public void SecurityRole_groupWithg() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//권한 radiobox 확인
		List<WebElement> roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		
		roles.get(3).click();
		Thread.sleep(500);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_ROLES_GUIDE)));
		if(!driver.findElement(By.xpath(XPATH_SECURITY_ROLES_GUIDE)).getText().contentEquals(MSG_ROLE_GUIDE_GROUPWITHGUEST)) {
			failMsg = failMsg + "\n0. role guide [Expected]" + MSG_ROLE_GUIDE_GROUPWITHGUEST
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_SECURITY_ROLES_GUIDE)).getText();
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//check radio
		roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		if (!roles.get(3).findElement(By.xpath("./input")).isSelected()) {
			failMsg = "\n1. role value is not selected.";
		}
		
		//새탭
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		String roomID = createMeetingRoom(driver, true);
		
		//guest
		try {
			enterRoom(driver_guest, roomID, false, true);
		} catch (Exception e) {
			failMsg = failMsg + "\n3. fail to enter the room (guest) : " + e.getMessage();
		}
		
		//login user (other group)
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		CommonValues comm = new CommonValues();
		comm.login(driver_guest, CommonValues.ADM_ID, CommonValues.USERPW);
		try {
			String msg = enterRoom(driver_guest, roomID, true, false);
			if(msg.contentEquals(MSG_SECURITY_NOTPERMIT_OTHER)) {
				failMsg = failMsg + "\n4. enter room error warning (other group) [Expected]" + MSG_SECURITY_NOTPERMIT_OTHER
						 + " [Actual]" + msg;
			}

		} catch (Exception e) {
			failMsg = failMsg + "\n5. fail to enter the room (other group) : " + e.getMessage();
			takescreenshot(driver_guest, "room.png");
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);
		
		//login user (same group)
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.KRHOME_URL);
		comm.login(driver_guest, CommonValues.USERS[0], CommonValues.USERPW);
		try {
			enterRoom(driver_guest, roomID, true, true);
		} catch (Exception e) {
			failMsg = failMsg + "\n6. fail to enter the room (same group) : " + e.getMessage();
		}
		driver_guest.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		comm.logout(driver_guest);		
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		//원복
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SECURITY_ROLES)));
		roles = driver.findElements(By.xpath(XPATH_SECURITY_ROLES));
		roles.get(0).click();
		Thread.sleep(500);
	
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void SecurityAI_settingN() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SECURITY_AI_RADIO_NOTALLOW)));
		driver.findElement(By.xpath(XPATH_SECURITY_AI_RADIO_NOTALLOW)).click();
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		createMeetingRoom(driver, true);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)));
		
		//회의록 클릭
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)));
		if(!driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB)).getAttribute("data-selected").contentEquals("manual")) {
			failMsg = failMsg + "\n1.note tab defalut value is not manual [Actual]" 
					+ driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB)).getAttribute("data-selected");
		}
		Thread.sleep(500);
		//click ai & hover tooltip
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB + "/li[1]/a"));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB + "/li[1]//div[@class='tooltip-notice-option note-tooltip']")).isDisplayed()
			|| !driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB + "/li[1]//div[@class='tooltip-notice-option note-tooltip']")).getText().contentEquals(MSG_NOTALLOW_AINOTE)){
			failMsg = failMsg + "\n2. ai tab tooltib state isdispayed : "  + driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB + "./li[1]//div[@class='tooltip-notice-option note-tooltip']")).isDisplayed()
					+ ", tooltip [Expected]" + MSG_NOTALLOW_AINOTE 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB)).getAttribute("data-selected");
		}
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void SecurityAI_settingY() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Security.URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Security.XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SECURITY_AI_RADIO_ALLOW)));
		driver.findElement(By.xpath(XPATH_SECURITY_AI_RADIO_ALLOW)).click();
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(Security.XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		createMeetingRoom(driver, true);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)));
		
		//회의록 클릭
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)));
		if(!driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB)).getAttribute("data-selected").contentEquals("automatic")) {
			failMsg = failMsg + "\n1.note tab defalut value is not automatic [Actual]" 
					+ driver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB)).getAttribute("data-selected");
		}
		Thread.sleep(500);
	
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public String createMeetingRoom(WebDriver wd, boolean isAllow) throws Exception {
		String roomID = "";
		wd.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		WebDriverWait wait = new WebDriverWait(wd, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		//회의개설 시도
		wd.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)));
		wd.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys("test1");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)));
		wd.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		
		if(isAllow) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)));
			roomID = wd.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL, "");
		} else {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = wd.switchTo().alert();
			roomID = alert.getText();
			alert.accept();
		}
		return roomID;
	}
	
	public String enterRoom(WebDriver wd, String roomid, boolean islogin, boolean permit) {
		String msg = "";
		WebDriverWait wait = new WebDriverWait(wd, 20);
		wd.get(CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomid);
		
		if(!islogin) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)));
			wd.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).clear();
			wd.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).sendKeys("guest");
			wd.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).sendKeys(Keys.ENTER);
		} 
		
		if(permit) {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_NOTE_BTN)));
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='device-setting-notification-box']")));
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='device-setting-notification-box']")));
				wd.findElement(By.xpath("//div[@id='msg-box']//button")).click();;

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = wd.switchTo().alert();
			msg = alert.getText();
			alert.accept();
		}
		return msg;
	}
	
	public void takescreenshot(WebDriver e, String filename) throws IOException {
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


