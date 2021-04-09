package android;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import mandatory.CommonValues;

/*
 * Free룸 개설 - 로그인x 참석자 시나리오
 * 1.Free버전 회의 개설 및 닉네임 설정 팝업 확인
 * 2.회의 생성 시 닉네임 설정 팝업란 빈값 입력 후 토스트 메세지 확인
 * 3.Free룸 개설 후 초대 팝업 확인
 * 4.초대 팝업 닫힘 확인 및 룸 정보 복사
 * 5.메뉴 확인
 * 6.비밀번호 잠금 클릭 후 팝업 확인
 * 7.사회자 모드 클릭 후 팝업 확인
 * 8.녹화 버튼 클릭 후 결재 유도 팝업 확인
 * 9.수동기록 탭 문구 확인
 * 10.회의록 클릭 후 AI기록 클릭 후 토스트 메세지 확인
 * 11.접속 코드 입력란 클릭 시 키보드 올라오는지 확인
 * 12.회의 참여 시 빈값,6자리 미만 값 입력 후 화살표 비활성화 확인
 * 13.회의 참여 시 닉네임 설정 팝업란 빈값 입력 후 토스트 메세지 확인
 * 14.해당 팝업 내 참여 취소 버튼 클릭 후 닫힘 확인
 * 15.모바일로 회의 참석
 * 16.모바일 참석자 메뉴 확인
 * 17.웹으로 최대 동시 참여 인원 확인
 * 18.개설자 기준 나가기 버튼 클릭 후 결재 유도 팝업 확인 후 로그인 화면 이동 확인
 * 19.참석자 기준 퇴장 토스트 메세지 확인 후 룸 화면 유지 확인
 */

public class MobileFree {
	
	private static String MSG_FREEPOPUP = "회의에서 사용할 닉네임을 입력하시기 바랍니다.(필수)";
	
	private static String TOAST_NICKNAME = "회의에서 사용 할 닉네임을 입력하세요.";
	//private static String TOAST_AI = "관리자 페이지에서 AI기록 제한을 제한없음으로 변경해야 합니다.";
	private static String TOAST_AI = "Free 버전 회의에서는 사용하실 수 없습니다.";
	
	private static String ALERT_EXCESS = "회의 최대 인원수를 초과하여 참여할 수 없습니다.\n" + "[Code: 40226]";
	
	private static String NICKNAME;
	
	private static String url;
	private static String code;
	
	public static WebDriver chromeDriver;
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	public static AndroidDriver<AndroidElement> androidDriver2 = null;

	CommonAndroid commA = new CommonAndroid();
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0,true);
		androidDriver2 = commA.setAndroidDriver(1,true);
		
		comm.setDriverProperty(browsertype);

		chromeDriver = comm.setDriver(chromeDriver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", chromeDriver);
		context.setAttribute("webDriver2", androidDriver);
		context.setAttribute("webDriver3", androidDriver2);
		
		commA.setServer(androidDriver);
		commA.setServer(androidDriver2);
		
	}
	
	@Test(priority = 1, enabled = true)
	public void experienceMeeting() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/login_main_create_btn")));
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_create_btn")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if (!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/demo_conference_description2")).getText().contentEquals(MSG_FREEPOPUP)) {
			failMsg = "Wrong Popup";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void checkToastMsg_Emptynickname() throws Exception {
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_NICKNAME);
	
	}
	
	@Test(priority = 3, enabled = true)
	public void createFree() throws Exception {
		String failMsg = "";
		
		NICKNAME = "Free Nickname";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/demo_conference_nickname_edittext")).sendKeys(NICKNAME);
		
		Thread.sleep(2000);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Don't enter room";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_demo_text")).getText().contentEquals("FREE")) {
			failMsg = "Wrong Text [Expected] FREE [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_demo_text")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void checkClipboard() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_other_link_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/contentPanel")));
		
		if(CommonAndroid.deviceLists.get(0)[2].contentEquals("google")) {
			androidDriver.findElement(By.id("android:id/chooser_copy_button")).click();
			
		} else if(CommonAndroid.deviceLists.get(0)[2].contentEquals("samsung")) {
			androidDriver.findElement(By.xpath("//android.widget.TextView[@text='복사']")).click();
		} 
		
		Thread.sleep(1000);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		url = commA.getInfo(androidDriver, 2);
		code = commA.getInfo(androidDriver, 4);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")).isEmpty()) {
			failMsg = "Invite Popup is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	/*
	@Test(priority = 5, enabled = true)
	public void moveChrome() throws Exception {
		
		androidDriver2.pressKey(new KeyEvent(AndroidKey.HOME));
		
		System.out.println(url);
		Thread.sleep(2000);
		
		androidDriver2.startActivity(new Activity("com.android.chrome", "com.google.android.apps.chrome.Main"));
		Thread.sleep(1000);
		androidDriver2.findElement(By.id("com.android.chrome:id/url_bar")).click();
		Thread.sleep(1000);
		androidDriver2.findElement(By.id("com.android.chrome:id/url_bar")).sendKeys(url);
		androidDriver2.pressKey(new KeyEvent(AndroidKey.ENTER));
		Thread.sleep(1000);
		androidDriver2.pressKey(new KeyEvent(AndroidKey.ENTER));
		
	}
	*/
	@Test(priority = 5, enabled = true)
	public void checkMenu() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		for (int i = 0; i < 13; i++) {
			String Menu[] = {"비밀번호 잠금", "초대", "카메라 전환", "카메라", "마이크", "문서공유", "회의록", "타임라인", "녹화", "모드 변경", "사회자 모드", "참여자 리스트", "나가기" };
			
			commA.scrollToAnElementByText(element, Menu[i]);
			
			if (!androidDriver.findElement(By.xpath("//android.widget.TextView[@text='"+ Menu[i] + "']")).getText().contentEquals(Menu[i])) {
				failMsg = "Wrong Menu and not display [Expected]" + Menu[i] + " [Actual]" + androidDriver.findElement(By.xpath("//android.widget.TextView[@text='"+ Menu[i] + "']")).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void clickLockFree() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "비밀번호 잠금");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='비밀번호 잠금']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void clickModeratorModeFree() throws Exception {
		String failMsg = "";

		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "사회자 모드");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='사회자 모드']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void clickRecordFree() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='녹화']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void checkManualTab() throws Exception {
		String failMsg = "";
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='회의록']")).click();
		
		Thread.sleep(2000);
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/minutes_notify_message")).getText().contentEquals("보기 기능만 제공합니다.")) {
			failMsg = "Wrong Text [Expected] 보기 기능만 제공합니다. [Actaul]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/minutes_notify_message")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 10, enabled = true)
	public void checkAITab() throws Exception {
		
		androidDriver.findElement(By.xpath("//androidx.appcompat.app.ActionBar.e[@content-desc='AI기록']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_AI);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/title_header_arrow")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/pip_screen")));	
	}
	
	
	@Test(priority = 11, enabled = true)
	public void checkKeyboardshown() throws Exception {
		String failMsg = "";
		
		androidDriver2.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).click();
		
		Thread.sleep(1000);
		
		boolean isKeyboardShown = androidDriver2.isKeyboardShown();
		
		if(isKeyboardShown == false) {
			failMsg = "Keyboard is not shown";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void checkAttendBtn() throws Exception {
		String failMsg = "";
		
		androidDriver2.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).clear();
		
		if(androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_join_btn")).isEnabled()) {
			failMsg = "AttendBtn is Enabled when don't insert code";
		}
		
		androidDriver2.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).click();
		androidDriver2.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).sendKeys("11111");
		
		if(androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_join_btn")).isEnabled()) {
			failMsg = "AttendBtn is Enabled when don't insert code";
		}
		
		androidDriver2.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).clear();
		androidDriver2.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).sendKeys(code);
		
		if(!androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_join_btn")).isEnabled()) {
			failMsg = "AttendBtn is Enabled when don't insert code";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void checkAttendNickname() throws Exception {
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_join_btn")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")).clear();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_NICKNAME);
		
	}
	
	@Test(priority = 14, enabled = true)
	public void closeAttendPopup() throws Exception {
		String failMsg = "";
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_cancel_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")));
		
		if(!androidDriver2.findElements(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isEmpty()) {
			failMsg = "Don't close insert Nickname Popup";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void AttendMobile() throws Exception {
		String failMsg = "";
		
		NICKNAME = "AndroidAttendee";
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_join_btn")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")).clear();
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")).sendKeys(NICKNAME);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/pip_screen")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/active_speaker_user_pip_gridlayout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/active_speaker_user_pip_gridlayout")).isDisplayed()) {
			failMsg = "Under PIP is not display in Creater";
		}
		
		if(!androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/active_speaker_user_pip_gridlayout")).isDisplayed()) {
			failMsg = "Under PIP is not display in Attendee";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void checkAttendeeMenu() throws Exception {
		String failMsg = "";
		
		androidDriver2.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		for (int i = 0; i < 13; i++) {
			String Menu[] = {"비밀번호 잠금", "초대", "카메라 전환", "카메라", "마이크", "문서공유", "회의록", "타임라인", "녹화", "모드 변경", "사회자 모드", "참여자 리스트", "나가기" };
			
			commA.scrollToAnElementByText(element, Menu[i]);
			
			if (!androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='"+ Menu[i] + "']")).getText().contentEquals(Menu[i])) {
				failMsg = "Wrong Menu and not display [Expected]" + Menu[i] + " [Actual]" + androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='"+ Menu[i] + "']")).getText();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void AttendWeb() throws Exception {
		String failMsg = "";
	
		NICKNAME = "ChromeAttendee";
		
		chromeDriver.get(url);
		
		WebDriverWait wait2 = new WebDriverWait(chromeDriver, 20);
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)));
		
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).sendKeys(NICKNAME);
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)).click();
		
		wait2.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = chromeDriver.switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		
		if(!alertText.contentEquals(ALERT_EXCESS)) {
			failMsg = "Alert is wrong [Expected]" + ALERT_EXCESS + " [Actual]" + alertText;
		}
		
		comm.waitForLoad(chromeDriver);

		if(!chromeDriver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL+CommonValues.KRHOME_URL)) {
			failMsg = "Wrong URL [Expected]" + CommonValues.MEETING_URL+CommonValues.KRHOME_URL + " [Actual]" + chromeDriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void clickExitFree() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/enterprise_version_info_layout")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_LOGINACTIVITY)) {
			failMsg = "Don't intent Activity [Expected]" + CommonAndroid.MEETING_LOGINACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19, enabled = true)
	public void checkExitFree() throws Exception {
		String failMsg = "";
		
		NICKNAME = "Free Nickname";
		
		//commA.CheckToastMsg(androidDriver2, NICKNAME+TOAST_EXIT);
		
		if(!androidDriver2.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Don't maintain Activity [Expected]" + CommonAndroid.MEETING_ROOMACTIVITY + " [Actual]" + androidDriver2.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		androidDriver.quit();
		androidDriver2.quit();
		chromeDriver.quit();
	}
}
