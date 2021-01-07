package mandatory;


import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class P2PFree {
	
	public static String XPATH_FREECREATE_HEADER = "//header[@class='title']";
	
	public static String XPATH_LEAVE_BTN = "//button[@id='btn-leave']";
	
	public static String MSG_FREECREATE_HEADER = "회원가입 없이, 시간 제한 없이\n" + "무료 화상회의 바로 시작";
	
	public static String MSG_NOTESTATE = "Free 버전에서는 기록이 저장되지 않습니다.";
	
	public static String TOOLTIP_AITAB = "관리자 페이지에서 AI기록 제한을 제한없음으로 변경해야 합니다.";
	
	public static String EXIT_HEADER = "RemoteMeeting Business는 더 다양한 협업 기능을 제공합니다.";
	
	public String roomID = "";
	
	public static WebDriver driver;
	public static WebDriver Attenddriver;

	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Attenddriver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver", Attenddriver);

	}
	
	@Test(priority = 1)
	public void selectFree() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_BTN)).click();
		
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_FREECREATE_DIALOG)));
		
		if(!driver.findElement(By.xpath(XPATH_FREECREATE_HEADER)).getText().contentEquals(MSG_FREECREATE_HEADER)) {
			failMsg = "1. popup subtitle [Expected]" + MSG_FREECREATE_HEADER 
					+ "[Actual]" + driver.findElement(By.xpath(XPATH_FREECREATE_HEADER)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2)
	public void makeFree() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).sendKeys(CommonValues.FREENICKNAME);
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)).click();
		
		if(comm.isAlertPresent(driver) == true) {
			TimeUnit.SECONDS.sleep(15);
			driver.findElement(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)).click();
		}
		
		comm.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='device-setting-notifications-box-wrapper']"), "style", "display: none;"));
		
		roomID = driver.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL , "");

		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = "1.Can't enter Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if(driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITE)).getAttribute("style").contains("block")) {
			failMsg = failMsg + "\n2.Can't dispay Invitation screen";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3)
	public void inviteFree() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_INVITE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITESUBMIT_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_SENDIVITATION) == false) {
			failMsg = "Wrong SendInvitation MSG [Expected]" + CommonValues.TOAST_SENDIVITATION
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if(!CommonValues.USERS[0].contains(driver.findElement(By.xpath(CommonValues.XPATH_SENTLIST_NAME)).getText())) {
			failMsg = "1.Wrong Sent list";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4)
	public void inviteFree2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SENTLIST_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_INVITELIST)));
		
		if(!CommonValues.USERS[0].contains((driver.findElement(By.xpath(CommonValues.XPATH_INVITELIST + "/strong")).getText()))) {
			failMsg = "1.Wrong Sent list name";
		}
		
		if(!CommonValues.USERS[0].contains((driver.findElement(By.xpath(CommonValues.XPATH_INVITELIST + "/span[1]")).getText()))) {
			failMsg = failMsg + "\n2.Wrong Sent list email";
		}
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_INVITELIST + "/span[2]")).getText().contentEquals("전송성공")) {
			failMsg = failMsg + "\n3.Failed to Sent email";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_INVITELISTCONFIRM_BTN)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 5)
	public void Note() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contentEquals("active") ) {
			failMsg = "Inactive Note";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)).sendKeys("AUTOTEST");
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_NOTESTATE)), MSG_NOTESTATE));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESTATE)).getText().contentEquals(MSG_NOTESTATE)) {
			failMsg = failMsg + "\n2.Wrong MSG [Expected]" + MSG_NOTESTATE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_NOTESTATE)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 6)
	public void Closenote() throws Exception {
		String failMsg = "";
		
		Actions actions = new Actions (driver);
		WebElement background = driver.findElement(By.id("local-video"));
		actions.doubleClick(background).perform ();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("float-note") ) {
			failMsg = "active Note";
		}
		
		if(driver.findElement(By.xpath("//aside[@id='meeting-note']//h2")).isDisplayed()) {
			failMsg = "\n2.Display Note";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7)
	public void Sharenote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("active") ) {
			failMsg = "Inactive Note";
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTESHARE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE)).isDisplayed() ) {
			failMsg = "Inactive ShareNote";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8)
	public void Sendnote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE + "//button")).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_NOTESHARE) == false) {
			failMsg = "Wrong SendNote MSG [Expected]" + CommonValues.TOAST_NOTESHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9)
	public void OverAITab() throws Exception {
	
		Actions action = new Actions(driver);
		WebElement aitab = driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li"));
		Thread.sleep(1000);
		action.moveToElement(aitab).build().perform();
		Thread.sleep(3000);
				
		Boolean isToolTipDisplayed = driver.findElement(By.xpath("//div[@class='tooltip-notice-option note-tooltip']")).isDisplayed();
		System.out.println("Is Tooltip displayed ? : " + isToolTipDisplayed);
		if (isToolTipDisplayed == true) {
			String tooltipText = driver.findElement(By.xpath("//div[@class='tooltip-notice-option note-tooltip']")).getText();
			System.out.println("Tooltip Text:- " + tooltipText);
			if(!tooltipText.contentEquals(TOOLTIP_AITAB)) {
				Exception e = new Exception("AITab tooltip text is wrong :" + tooltipText);
				throw e;
			}
		}
		else {
			Exception e = new Exception("AITab tooltip is not displayed");
			throw e;		
			}
	}
	
	@Test(priority = 10)
	public void SendESC_Note() throws Exception {
		String failMsg = "";
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).perform();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("float-note") ) {
			failMsg = "active Note" + driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class");
		}
		
		if(driver.findElement(By.xpath("//aside[@id='meeting-note']//h2")).isDisplayed() ) {
			failMsg = "\n2.Display Note";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11)
	public void CheckTimeline_Camera() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 카메라를 껐습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 카메라를 켰습니다.")) {
			failMsg = "\n 2.Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12)
	public void CheckTimeline_MIC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 마이크를 음소거했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 마이크 음소거를 해제했습니다.")) {
			failMsg = "\n2.Wrong Timeline";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13)
	public void SendEnter_Timeline() throws Exception {
		String failMsg = "";
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ENTER).perform();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE)).isDisplayed()) {
			failMsg = "Timeline is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14)
	public void SendTxt_Timeline() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)).sendKeys("TEST");
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINESEND_BTN)).click();
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		if(!timeline.contentEquals("TEST")) {
			failMsg = "Wrong Timeline";
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15)
	public void SendESC_Timeline() throws Exception {
		String failMsg = "";
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).perform();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE)).isDisplayed()) {
			failMsg = "Timeline is display";
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16)
	public void Setting() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Setting popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@class='button round gray close']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElements(By.xpath("//div[@class='content-wrap']")).isEmpty()) {
			failMsg = "\n2.Setting popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17)
	public void Attend() throws Exception {
		String failMsg = "";
		
		comm.attendMeeting(driver, Attenddriver);
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), "[FREEATTENDEE] 님이 참여했습니다."));
		
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		System.out.println(msg);
		if(!msg.contentEquals("["+CommonValues.ATTENDEEFREENICKNAME+"] 님이 참여했습니다.")) {
			failMsg = "Wrong Attend MSG [Expected] [FREEATTENDEE] 님이 참여했습니다." 
					+ " [Actual]" + msg;
		}
		
		if (!Attenddriver.getCurrentUrl().contentEquals((CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID))) {
			failMsg = "1.Attendee can't entered Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + Attenddriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 18)
	public void Refresh() throws Exception {
		String failMsg = "";
		
		String url = driver.getCurrentUrl();
		driver.navigate().refresh();
		
		comm.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pip-container']")));
		
		if (!driver.getCurrentUrl().contentEquals((url))) {
			failMsg = "1.Attendee can't entered Free Room [Expected]" + url
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19)
	public void ExitSelect() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Exit popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']/div/div[1]")).getText().contentEquals(EXIT_HEADER)) {
			failMsg = "\n2.Exit popup header is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20)
	public void Exit() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LEAVE_BTN)));
		
		driver.findElement(By.xpath(XPATH_LEAVE_BTN)).click();
		
		comm.waitForLoad(driver);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.KRHOME_URL)) {
			failMsg = "1.Can't leave Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.KRHOME_URL
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
		Attenddriver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
		
}
	


