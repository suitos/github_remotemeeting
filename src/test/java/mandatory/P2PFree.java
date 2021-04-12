package mandatory;

import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
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

/*
 * 1.홈에서 회의 시작 선택
 * 2.닉네임 입력 후 회의 시작 선택, 입장 후 초대 팝업 확인
 * 3.이메일 입력 후 초대 메일 전송 후 토스트 메세지 확인
 * 4.초대 발송 내역 내 전송성공 문구 확인
 * 5.회의실 잠금 후 로그 확인
 * 6.비밀번호 설정하기 클릭 후 로그 확인 (disabled, 삭제예정)
 * 7.참여자 접근 차단하기 클릭 후 로그 확인
 * 8.회의실 잠금 off후 로그 확인
 * 9.회의록 아이콘 클릭
 * 10.화면 임의 위치 더블 클릭 후 회의록 닫힘 확인
 * 11.회의록 공유 선택
 * 12.회의록 전송 후 토스트 메세지 확인
 * 13.AI기록에 마우스 오버 후 툴팁 확인
 * 14.ESC키 입력 후 회의록 닫힘 확인
 * 15.카메라 ON/OFF 경우 로그 확인
 * 16.MIC ON/OFF 경우 로그 확인
 * 17.ENTER키 입력 후 타임라인 창 확인
 * 18.타임라인에 메시지 입력 후 전송 클릭 및 로그 확인
 * 19.ESC키 입력 후 타임라인 닫힘 확인
 * 20.환경설정 클릭 후 팝업 확인 및  취소 클릭 시 환경설정 팝업 닫힘 확인
 * 21.마이크 변경 후 환경설정 팝업 내 변경된 마이크 확인 
 * 22.스피커 변경 후 환경설정 팝업 내 변경된 스피커 확인
 * 23.참석자 참석 및 토스트 메세지 확인
 * 24.로컬 pip 내 이름 확인
 * 25.새로고침 후 룸 재입장 확인(pip로 확인)
 * 26.회의 나가기 선택 후 팝업 확인
 * 27.팝업 내 나가기 선택 후 회의 종료 확인
 */

public class P2PFree {
	
	public static String XPATH_FREECREATE_HEADER = "//header[@class='title']";
	
	public static String XPATH_LEAVE_BTN = "//button[@id='btn-leave']";
	public static String XPATH_DIALOG_HEADER = "//div[@class='dialog-header']";
	public static String XPATH_DIALOG_CLOSE_BTN = "//i[@class='icon close black']";
	
	public static String MSG_FREECREATE_HEADER = "회원 가입 없이 바로 회의 시작";
	
	public static String MSG_NOTESTATE = "Free 버전에서는 기록이 저장되지 않습니다.";
	
	public static String MSG_SETTING = "현재의 마이크, 카메라 설정이 적용됩니다.";
	
	public static String TOOLTIP_AITAB = "Free 버전 회의에서는 사용하실 수\n없습니다.";
	
	public static String EXIT_HEADER = "RemoteMeeting 회원은 더 다양한 기능을 사용할 수 있습니다.";
	
	
	public String roomID = "";
	
	public static WebDriver driver;
	public static WebDriver Attenddriver;

	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Attenddriver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", Attenddriver);

	}
	
	@Test(priority = 1)
	public void selectFree() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_BTN)).click();
		
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_FREECREATE_DIALOG)));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XPATH_FREECREATE_HEADER)));
		String headermsg = driver.findElement(By.xpath(XPATH_FREECREATE_HEADER)).getText();
		if(!headermsg.contentEquals(MSG_FREECREATE_HEADER)) {
			failMsg = "1. popup subtitle [Expected]" + MSG_FREECREATE_HEADER 
					+ "[Actual]" + headermsg;
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
		
		roomID = driver.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL , "");

		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = failMsg + "\n1.Can't enter Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITE)).isDisplayed()) {
			failMsg = failMsg + "\n2.Can't dispay Invitation screen";
		}
		
		if(!driver.findElement(By.xpath("//div[@title='FREE']")).getText().contentEquals("FREE")) {
			failMsg = failMsg + "\n3.Wrong invite title [Expected] FREE [Actual]" + driver.findElement(By.xpath("//div[@title='FREE']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, dependsOnMethods = {"makeFree"}, alwaysRun = true, enabled = true)
	public void inviteFree() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITE)).isDisplayed())
			driver.findElement(By.xpath(CommonValues.XPATH_INVITE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITESUBMIT_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals( CommonValues.TOAST_SENDIVITATION)) {
			failMsg = "Wrong SendInvitation MSG [Expected]" + CommonValues.TOAST_SENDIVITATION
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
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

	@Test(priority = 5, enabled = true)
	public void LockFreeRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath("//button[@id='lock']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DIALOG_HEADER)));
		if(!driver.findElement(By.xpath(XPATH_DIALOG_HEADER)).getText().contentEquals(EXIT_HEADER)) {
			failMsg = "\n1.Exit popup header is wrong [Expected]" + EXIT_HEADER 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DIALOG_HEADER)).getText();
		}
		driver.findElement(By.xpath(XPATH_DIALOG_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void Note() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("active") ) {
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
	
	@Test(priority = 10, dependsOnMethods = {"Note"}, enabled = true)
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
	
	@Test(priority = 11, dependsOnMethods = {"Note"}, enabled = true)
	public void Sharenote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("active") ) {
			failMsg = "Inactive Note";
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_NOTESHARE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE)).isDisplayed() ) {
			failMsg = "Inactive ShareNote";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, dependsOnMethods = {"Note"}, enabled = true)
	public void Sendnote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE + "//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_NOTESHARE)) {
			failMsg = "Wrong SendNote MSG [Expected]" + CommonValues.TOAST_NOTESHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, dependsOnMethods = {"Note"}, enabled = true)
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
		} else {
			Exception e = new Exception("AITab tooltip is not displayed");
			throw e;
		}
	}
	
	@Test(priority = 14, dependsOnMethods = {"Note"}, enabled = true)
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
	
	@Test(priority = 15, enabled = true)
	public void CheckTimeline_Camera() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		//toast 확인하도록 수정(타임라인 미확인)
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(P2PEnterprise.TOAST_CAMERA_OFF)) {
			failMsg = "\n1. camera off toast. [Expected]" + P2PEnterprise.TOAST_CAMERA_OFF
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//camera on
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(P2PEnterprise.TOAST_CAMERA_ON)) {
			failMsg = "\n2. camera on toast. [Expected]" + P2PEnterprise.TOAST_CAMERA_ON
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		/* 타임라인 체크 없음
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 카메라를 껐습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 카메라를 켰습니다.")) {
			failMsg = "\n 2.Wrong Timeline";
		}
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void CheckTimeline_MIC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		//toast 확인하도록 수정(타임라인 미확인)
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if (!msg.contentEquals(P2PEnterprise.TOAST_AUDIO_OFF)) {
			failMsg = "\n1. audio off toast. [Expected]" + P2PEnterprise.TOAST_AUDIO_OFF + " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		
		//on
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if (!msg.contentEquals(P2PEnterprise.TOAST_AUDIO_ON)) {
			failMsg = "\n1. audio on toast. [Expected]" + P2PEnterprise.TOAST_AUDIO_ON + " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		/* 타임라인 안찍음
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
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
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
	
	@Test(priority = 18, enabled = true)
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
	
	@Test(priority = 19, enabled = true)
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
	
	@Test(priority = 20, enabled = true)
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
	
	@Test(priority = 21, enabled = true)
	public void SettingMIC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")));
		//마이크 워닝 툴팁 떠있으면 없애기
		if(driver.findElement(By.xpath("//div[@class='device-title']/em")).isDisplayed())
			driver.findElement(By.xpath("//div[@class='device-title']/em")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='mic-list']//select")));
		driver.findElement(By.xpath("//div[@id='mic-list']//select")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='mic-list']//select/option[2]")));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='mic-list']//select/option[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")).click();
		
		comm.waitForLoad(driver);
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loader-bi']")));
		Thread.sleep(5000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@id='mic-list']//select/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "a";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 22, enabled = true)
	public void SettingSpeaker() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		driver.findElement(By.xpath("//select[@id='speakers']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='speakers']/option[2]")));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//select[@id='speakers']/option[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")).click();
		
		comm.waitForLoad(driver);
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loader-bi']")));
		Thread.sleep(5000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//select[@id='speakers']/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "a";
		}
		
		driver.findElement(By.xpath("//button[@class='button round gray close']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, enabled = true)
	public void Attend() throws Exception {
		String failMsg = "";
		
		comm.attendMeeting(driver, Attenddriver);
		
		/* 참석 토스트 없음
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		System.out.println(msg);
		if(!msg.contentEquals("["+CommonValues.ATTENDEEFREENICKNAME+"] 님이 참여했습니다.")) {
			failMsg = "Wrong Attend MSG [Expected] [FREEATTENDEE] 님이 참여했습니다." 
					+ " [Actual]" + msg;
		}
		*/
		//타임라인 열기
		if(!driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		Thread.sleep(1000);
		//기존 유저 참석자 입장 타임라인 확인 - 타임라인 마지막 메세지 가져옴
		String timeline = comm.checkTimeline(driver);
		
		String expectedmsg = "["+CommonValues.ATTENDEEFREENICKNAME+"] 님이 참여했습니다.";
		if(!timeline.contentEquals(expectedmsg)) {
			failMsg = "\n1. Wrong Timeline [Expcted]" + expectedmsg + " [Actual]" + timeline;
		}
		
		if (!Attenddriver.getCurrentUrl().contentEquals((CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID))) {
			failMsg = "2.Attendee can't entered Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + Attenddriver.getCurrentUrl();
		}
		
		//타임라인 닫기
		if(driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 22, dependsOnMethods = {"Attend"}, enabled = true)
	public void checkLocalPIPname() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='local-video-pip']//div[@class='pip-top']/span[4]")));
		String localpipname = driver.findElement(By.xpath("//div[@id='local-video-pip']//div[@class='pip-top']/span[4]")).getText();
		
		//pip 이름 확인
		if(!localpipname.contentEquals(CommonValues.FREENICKNAME)) {
			failMsg = "\n1.Wrong Local PIP Name [Expected]" + CommonValues.FREENICKNAME + " [Actual]" + localpipname;
		}
		
		//내 pip 유저 아이콘 확인
		try {
			driver.findElement(By.xpath("//div[@id='local-video-pip']//span[@class='overlap me']"));
		} catch (NoSuchElementException e) {
			failMsg = failMsg + "\n3. cannot find 'me'icon in my pip.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, dependsOnMethods = {"Attend"}, enabled = true)
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
	
	@Test(priority = 24, enabled = true)
	public void ExitSelect() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "1.Exit popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']/div/div[1]")).getText().contentEquals(EXIT_HEADER)) {
			failMsg = "\n2.Exit popup header is wrong [Expected]" + EXIT_HEADER 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']/div/div[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 25, enabled = true)
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
	


