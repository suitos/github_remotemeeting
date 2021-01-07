package mandatory;



import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.Alert;
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



public class P2PEnterprise {
	
	public static String XPATH_PROFILE_BTN = "//button[@id='btn-user']";
	public static String XPATH_LOUNGE_BTN = "//div[@class='header-item btn-lounge']";

	public static String XPATH_SCHEDULE_BTN = "//i[@class='rmicon-calendar']";
	public static String XPATH_HISTORY_BTN = "//i[@class='rmicon-history']";
	
	public static String HREF_PROFILE = "//a[@href='/ko/setting/profile']";
	
	public static String XPATH_SCREENSHOT_BTN = "//button[@id='screen-shot']";
	public static String XPATH_RECORD_BTN = "//button[@id='recording']";
	public static String XPATH_SPEAKRIGHT_BTN = "//button[@id='speak-right']";
	public static String XPATH_SPEAKLIST_BTN = "//button[@id='speak-list']";
	public static String XPATH_PIP_BTN = "//button[@id='pip-hide-btn']";
	
	public static String TOAST_BLOCK = "회의실이 접근 차단 상태로 변경되었습니다.";
	public static String TOAST_LOCK = "회의실이 잠금 상태로 변경되었습니다.";
	public static String TOAST_CHANGEPW = "비밀번호가 변경 되었습니다.";
	public static String TOAST_UNLOCK = "회의실 잠금이 해제 되었습니다.";
	public static String TOAST_TOGETHERNOTE = "다른 참여자들의 회의록을 펼쳤습니다.";
	public static String TOAST_SPEAKRIGHT = CommonValues.ADMINNICKNAME +"님이 사회자 권한을 선택하였습니다.";
	public static String TOAST_ALLMICOFF = "전체 마이크가 OFF 되었습니다.";
	public static String TOAST_ALLMICON = "전체 마이크가 ON 되었습니다.";
	public static String TOAST_PIPFIX = "주화면을 고정했습니다. 화면 고정을 해제하시려면 고정된 PIP를 한번 더 선택해주세요.";
	public static String TOAST_RECORD = "녹화가 시작되었습니다.";
	public static String TOAST_STOPRECORD = "녹화가 종료되었습니다.";
	public static String TOAST_SAVE = "일정이 저장되었습니다.";
	public static String TOAST_DELETE = "삭제가 완료되었습니다.";
	public static String TOAST_SUCCESS = "정상 처리 되었습니다.";
	
	public static String TOOLTIP_AI = "현재 AI 회의 기록이 OFF 상태입니다.\n" + "상단의 AI 버튼을 ON으로 해주세요.";
	
	public static String MSG_NOTESTATE = "모든 변경사항이 HISTORY에 저장됨.";
	
	public static String CONTENTWRAP_TXT = "녹화를 시작하시겠습니까? 녹화된 영상은 히스토리 메뉴에서 확인할 수 있습니다.";
	public static String CONTENTWRAP_TXT2 = "사회자 모드는 회의 참여자가 많을 경우 원활한 회의 진행을 위해 발언권을 조정하는 기능을 제공합니다.\n" + 
											"사회자 모드를 활성화하면 " +CommonValues.ADMINNICKNAME + "님이 회의 조정자가 됩니다.\n" + 
											"사회자 모드를 활성화 하시겠습니까?";
	private static String RoomTitle = "P2PEnterprise";
	
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
	public void Login() throws Exception {
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 2)
	public void Profile() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_PROFILE_BTN)).click();
		
		if(driver.findElements(By.xpath("//div[@class='header-item is-open']")).isEmpty()) {
			failMsg = "ProfileWindow is not open";
		}
		
		driver.findElement(By.xpath(HREF_PROFILE)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-inner-wrap']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.KRPROFILE_URL)) {
			failMsg = "Can't enter Profile [Expected]" + CommonValues.MEETING_URL + CommonValues.KRPROFILE_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3)
	public void Profile_Setting() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='image-type-upload']")));

		driver.findElement(By.xpath("//label[@for='image-type-upload']")).click();
		
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[4];
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys(addedfile);
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@class='file-name']"), CommonValues.TESTFILE_LIST[4]));
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.xpath("//div[@id='photo']"), "style", "profile-default-person.png")));
		
		driver.findElement(By.xpath("//button[@class='cola-btn size-sm type-full green']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		
		if(!alertText.contentEquals("정상적으로 등록되었습니다.")) {
			failMsg = "Alert is wrong [Expected] 정상적으로 등록되었습니다. [Actual]" + alertText;
		}
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//fieldset/div[2]/div[1]"), "class", "form-item-wrap original radio-wrap"));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4)
	public void GoLounge() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LOUNGE_BTN)));
		
		driver.findElement(By.xpath(XPATH_LOUNGE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='channel-wrap']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = "Can't enter lounge [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL 
					+" [Actual]" + driver.getCurrentUrl();
		}
		
		List <WebElement> roomlist = driver.findElements(By.xpath("//div[@class='channel-wrap']"));
		
		if(roomlist.size() != 99 && roomlist.isEmpty()) {
			failMsg = "Room list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5)
	public void QuickStart() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Quick start popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6)
	public void QuickStart2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys(RoomTitle);
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		
		comm.waitForLoad(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='device-setting-notifications-box-wrapper']"), "style", "display: none;"));
		
		roomID = driver.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL , "");

		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = "1.Can't enter Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7)
	public void Enterprise_Invite() throws Exception {
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
	
	@Test(priority = 8)
	public void Enterprise_Invite2() throws Exception {
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
	
	@Test(priority = 9)
	public void Enterprise_Attend() throws Exception {
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
	
	@Test(priority = 10)
	public void Enterprise_Refresh() throws Exception {
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
	
	@Test(priority = 11)
	public void LockRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='lock']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='lock-drop-box']")));
		
		if(!driver.findElement(By.xpath("//div[@id='lock-drop-box']")).isDisplayed()) {
			failMsg = "1.Lock drop box is not display";
		}
		
		driver.findElement(By.xpath("//button[@class='on']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_BLOCK) == false) {
			failMsg = failMsg + "\n2.Wrong Lock MSG [Expected]" + TOAST_BLOCK
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
			
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12)
	public void ChangeRoomPW() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@id='set-password']")).click();
		
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_LOCK) == false) {
			failMsg = failMsg + "\n2.Wrong Lock MSG [Expected]" + TOAST_LOCK
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		driver.findElement(By.xpath("//button[@id='reload-password']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_CHANGEPW) == false) {
			failMsg = failMsg + "Wrong ChangePW MSG [Expected]" + TOAST_CHANGEPW
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13)
	public void UnlockRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='off active']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_UNLOCK) == false) {
			failMsg = "Wrong Unlock MSG [Expected]" + TOAST_UNLOCK
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14)
	public void Record() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_RECORD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Recording popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']//p")).getText().contentEquals(CONTENTWRAP_TXT)) {
			failMsg = failMsg + "\n2.Wrong TXT [Expected]" + CONTENTWRAP_TXT2
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']")).getText();
		}
		
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_RECORD) == false) {
			failMsg = "Wrong Unlock MSG [Expected]" + TOAST_RECORD
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	
	@Test(priority = 16)
	public void Enterprise_note() throws Exception {
		String failMsg = "";
		
		Actions action = new Actions(driver);
		action.keyDown(Keys.SHIFT).sendKeys(Keys.ENTER).keyUp(Keys.SHIFT).perform();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contentEquals("active") ) {
			failMsg = "Inactive Note";
		}
		
		if(!driver.findElement(By.xpath("//aside[@id='meeting-note']//h2")).isDisplayed() ) {
			failMsg = "\n2.Don't Display Note";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17)
	public void TurnOn_NoteAI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='off']")).click();
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("data-automatic").contentEquals("on") ) {
			failMsg = "AI don't turn ON";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18)
	public void TurnOff_NoteAI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='on']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "AI OFF popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("data-automatic").contentEquals("off") ) {
			failMsg = failMsg + "\n2.AI don't turn OFF";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).isDisplayed()) {
			failMsg = failMsg + "\n3.AI tooltip is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).getText().contentEquals(TOOLTIP_AI)) {
			failMsg = failMsg + "\n4.AI tooltip text is wrong [Expected]" + TOOLTIP_AI 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19)
	public void NoteManual() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@data-menu='manual']")).click();
		
		if(!driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']")).getAttribute("data-selected").contentEquals("manual") ) {
			failMsg = failMsg + "\n2.Manual Tab inactive";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)).sendKeys("AUTOTEST");
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
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
	
	@Test(priority = 20)
	public void Enterprise_Sharenote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTESHARE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE)).isDisplayed() ) {
			failMsg = "Inactive ShareNote";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 21)
	public void Enterprise_Sendnote() throws Exception {
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
	
	@Test(priority = 22)
	public void Enterprise_Historynote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTEHISTORY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "History note popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@id='dialog-close']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23)
	public void Enterprise_Togethernote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btn-see-togather']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_TOGETHERNOTE) == false) {
			failMsg = "Wrong Toast MSG [Expected]" + TOAST_TOGETHERNOTE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 24)
	public void Enterprise_Closenote() throws Exception {
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
	
	@Test(priority = 25)
	public void Enterprise_CheckTimeline_Camera() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 카메라를 껐습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 카메라를 켰습니다.")) {
			failMsg = "\n 2.Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 26)
	public void Enterprise_CheckTimeline_MIC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 마이크를 음소거했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 마이크 음소거를 해제했습니다.")) {
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
	
	@Test(priority = 27)
	public void Enterprise_ShareDOC_jpg() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,4,1);
		
		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 28)
	public void Enterprise_ShareDOC_txt() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,8,2);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 29)
	public void Enterprise_StartShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STARTSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 30)
	public void Enterprise_StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STOPSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 31)
	public void Enterprise_SendTxt_Timeline() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
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
	
	@Test(priority = 32)
	public void Enterprise_SendESC_Timeline() throws Exception {
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
	
	@Test(priority = 33)
	public void Enterprise_SpeakRight() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "SpeakRight popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).getText().contains(CONTENTWRAP_TXT2)) {
			failMsg = "Wrong TXT [Expected]" + CONTENTWRAP_TXT2
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']")).getText();
		}
		
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_SPEAKRIGHT) == false) {
			failMsg = "Wrong Speakright MSG [Expected]" + TOAST_SPEAKRIGHT
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 34)
	public void Enterprise_SpeakRight2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='all-mic-off']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_ALLMICOFF) == false) {
			failMsg = "Wrong Speakright MSG [Expected]" + TOAST_ALLMICOFF
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		driver.findElement(By.xpath("//button[@id='all-mic-on']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_ALLMICON) == false) {
			failMsg = "Wrong Speakright MSG [Expected]" + TOAST_ALLMICON
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 35)
	public void Enterprise_SpeakList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SPEAKLIST_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='participants']")));
		
		List <WebElement> attendelist = driver.findElements(By.xpath("//span[@class='attendee-name']"));
		
		if (!attendelist.get(0).getText().contentEquals(CommonValues.ADMINNICKNAME)
				|| !attendelist.get(1).getText().contentEquals(CommonValues.ATTENDEEFREENICKNAME)) {
			failMsg = "Wrong SpeakList [Expected]" + CommonValues.ADMINNICKNAME + "," + CommonValues.ATTENDEEFREENICKNAME 
					+ " [Actual]" + attendelist.get(0).getText() + "," + attendelist.get(1).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 36)
	public void Enterprise_PIPExport() throws Exception {
		String failMsg = "";
		
		if(driver.findElement(By.xpath("//div[@id='local-video-pip']")).getAttribute("class").contentEquals("video-wrap")) {
			List <WebElement> pipexportbtn = driver.findElements(By.xpath("//button[@class='btn-pip-export']"));
			pipexportbtn.get(0).click();
			
			if(!driver.findElement(By.xpath("//div[@id='local-video-pip']")).getAttribute("class").contentEquals("video-wrap pip-exported")) {
				failMsg = "";
			}
		}else {
			failMsg = "";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 37)
	public void Enterprise_PIPView() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_PIP_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pip-inner-wrap']")));
		System.out.println(driver.findElement(By.xpath("//div[@id='pip-wrap']")).getAttribute("class"));
		if(!driver.findElement(By.xpath("//div[@id='pip-wrap']")).getAttribute("class").contentEquals("")) {
			failMsg = "1.Class is wrong";
		}
		
		if(!driver.findElement(By.xpath("//div[@id='pip-inner-wrap']")).isDisplayed()) {
			failMsg = failMsg + "\n2.Don't display PIP";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 38)
	public void Enterprise_PIPFix() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//i[@class='icon device-type pc']")).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_PIPFIX) == false) {
			failMsg = "Wrong SendInvitation MSG [Expected]" + TOAST_PIPFIX
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 39)
	public void Enterprise_Setting() throws Exception {
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
	
	@Test(priority = 40, enabled = false)
	public void Enterprise_Screenshot() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SCREENSHOT_BTN)).click();
		
	}
	
	@Test(priority = 45)
	public void StopRecord() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_RECORD_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, TOAST_STOPRECORD) == false) {
			failMsg = "Wrong Stop Record MSG [Expected]" + TOAST_STOPRECORD
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 46)
	public void Enterprise_PIPHide() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_PIP_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='pip-inner-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@id='pip-wrap']")).getAttribute("class").contentEquals("pip-hide")) {
			failMsg = "1.Class is wrong";
		}
		
		if(driver.findElement(By.xpath("//div[@id='pip-inner-wrap']")).isDisplayed()) {
			failMsg = failMsg + "\n2.Display PIP";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 47)
	public void Enterprise_Exit() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		comm.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = "1.Can't leave Room [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 48)
	public void Schedule() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SCHEDULE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='schedule']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.SCHEDULE_URL)) {
			failMsg = "1.Wrong URL [Expected]" + CommonValues.MEETING_URL + CommonValues.SCHEDULE_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 49)
	public void addSchedule() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btn-create']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Add Schedule popup is not display";
		}
		
		driver.findElement(By.xpath("//input[@id='title-input']")).sendKeys("Schedule Test");
		driver.findElement(By.xpath("//button[@class='button round green large']")).click();
		
		wait.until(ExpectedConditions.attributeToBe(By.xpath("//section[@class='card-wrap']/div"), "class", "schedule-card "));
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='toast-inner success']/span")), TOAST_SAVE));
		
		if (!driver.findElement(By.xpath("//div[1]/p[@class='title']")).getText().contentEquals("Schedule Test")
				|| !driver.findElement(By.xpath("//li/span[@class='name']")).getText().contentEquals(CommonValues.ADMINNICKNAME)) {

			failMsg = "\n2.Wrong Schedule [Expected]Schedule Test," + CommonValues.ADMINNICKNAME + " [Acual]"
					+ driver.findElement(By.xpath("//div[1]/p[@class='title']")).getText() + ","
					+ driver.findElement(By.xpath("//li/span[@class='name']")).getText();
		}
		
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		
		if(!msg.contentEquals(TOAST_SAVE)) {
			failMsg = failMsg + "\n3.Wrong Save MSG [Expected]" + TOAST_SAVE
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 50)
	public void deleteSchedule() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		driver.findElement(By.xpath("//div[@class='schedule-card ']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Confirm Schedule popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@class='button round red close large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='question-dialog']")));
		
		if(!driver.findElement(By.xpath("//section[@id='question-dialog']")).isDisplayed()) {
			failMsg = failMsg + "\n2.Delete Schedule popup is not display";
		}
		
		driver.findElement(By.xpath("//section/div/button[@class='button round green large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();

		if(!msg.contentEquals(TOAST_DELETE)) {
			failMsg = failMsg + "\n3.Wrong Delete MSG [Expected]" + TOAST_DELETE
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		}
		
		if(!driver.findElement(By.xpath("//section[@class='card-wrap']/div")).getAttribute("class").contentEquals("schedule-card empty-card")) {
			failMsg = failMsg + "\n4.not Empty Card";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 51)
	public void History() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_HISTORY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='history-list']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.HISTORY_URL)) {
			failMsg = "1.Wrong URL [Expected]" + CommonValues.MEETING_URL + CommonValues.HISTORY_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 52)
	public void viewHistory() throws Exception {
		String failMsg = "";
		
		List<WebElement> historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
		historylist.get(0).findElement(By.xpath(".//div[@class='left']/button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "History note popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 53)
	public void shareHistory() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath("//button[@data-btn='share-note']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[2]/div/div")));
		
		if(!driver.findElement(By.xpath("//section[2]/div/div")).isDisplayed()) {
			failMsg = "1.History share popup is not display";
		}
		
		driver.findElement(By.xpath("//input[@id='note-share-input']")).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath("//input[@id='note-share-input']")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//form[@id='contact-form']/div[2]/button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		
		if(!msg.contentEquals(TOAST_SUCCESS)) {
			failMsg = failMsg + "\n2.Wrong Success MSG [Expected]" + TOAST_SUCCESS
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		}
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).perform();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//section[2]/div/div")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='history-list']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 54)
	public void recordingHistory() throws Exception {
		String failMsg = "";
		
		List<WebElement> historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
		
		if(historylist.get(0).findElements(By.xpath(".//i[@class='play-btn-icon']")).isEmpty()) {
			for(int i=1; i < historylist.size(); i++) {
				historylist.get(i).findElement(By.xpath(".//i[@class='play-btn-icon']")).click();
				if(!historylist.get(i).findElements(By.xpath(".//i[@class='play-btn-icon']")).isEmpty()) {
					break;
				}
			}
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='video-player']")));
			
			if(!driver.findElement(By.xpath("//div[@class='video-player']")).isDisplayed()) {
				failMsg = "1.Record Start popup is not display";
			}
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

	public void ShareDocument(WebDriver driver, int i, int j) throws InterruptedException {
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[i];
		driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//div[@id='doc-tools']")), "class", "visible"));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@id='doc-count']"), Integer.toString(j)));
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
	}

}
