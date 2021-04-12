package android;

import java.util.concurrent.TimeUnit;

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
 * 1.데모 배너 체크
 * 2.데모 배너 클릭 후 시작 팝업 확인
 * 3.데모 시작 팝업 닫기
 * 4.데모 시작
 * 5.25초 대기
 * 6.메뉴 체크
 * 7.비밀번호 잠금 클릭 후 토스트 메세지 확인
 * 8.녹화 클릭 후 토스트 메세지 확인
 * 9.사회자 모드 클릭 후 토스트 메세지 확인
 * 10.문서 공유 클릭 후 토스트 메세지 확인
 * 11.데모 종료 클릭 후 팝업 확인
 * 12.데모 종료
 * 13.데모 배너 닫기
 * 14.로그아웃
 * 15.재로그인
 * 16.데모 배너 re체크
 * 17.초기 화면 룸 화면 확인
 * 18.스케쥴 클릭
 * 19.라운지 내 빈 룸 클릭
 * 20.웹 로그인
 * 21.웹에서 미팅 개설
 * 22.랴운지 내 진행중인 룸 클릭
 * 23.웹 내 해당 미팅 잠금 설정
 * 24.잠긴 미팅 모바일 내에서 메세지 확인
 * 25.웹 내 해당 미팅 pw 설정
 * 26.pw 설정된 미팅 모바일 내에서 메세지 확인
 * 27.웹 미팅 종료
 * 28.라운지 하단 특허 텍스트 확인
 * 29.플로팅 버튼 클릭
 * 30.플로팅 버튼 닫기 클릭
 * 31.예약 추가 버튼 클릭
 * 32.회의 시작 버튼 클릭
 * 33.플로팅 버튼 이용하여 미팅 만들기
 * 34.웹에서 해당 미팅 확인
 */
public class Lounge {
	
	private static String MSG_LOCK = "잠겨있는 회의실 입니다. 참여할 수 없습니다.";
	private static String MSG_PW = "잠겨있는 회의실입니다.\n" + "비밀번호를 입력 후 입장하시기 바랍니다.";
	
	private static String TOAST_DOCSHARE = "라이브미팅 중에는 사용 할 수 없습니다. 라이브미팅 종료 후 회의를 개설해 이용해 보세요.";
	
	private static String TXT_DEMO = "AI데모를 통해 리모트미팅을 쉽게 알아보세요.";
	
	private static String TXT_DEMOPOPUP = "AI데모 ‘아루’와 함께 생생한 라이브미팅을 체험해보세요.";
	private static String TXT_DEMOPOPUP2 = "리모트미팅의 사용법과 기능을 쉽게 알아보세요.";
	
	private static String TXT_DEMOEXITPOPUP = "지금 회의실을 나가시면 처음부터 다시 시작하셔야 합니다. 그래도 나가시겠습니까?";

	public static AndroidDriver<AndroidElement> androidDriver = null;
	public static WebDriver chromeDriver;
	
	CommonAndroid commA = new CommonAndroid();
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0,true);

		chromeDriver = comm.setDriver(chromeDriver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", androidDriver);
		context.setAttribute("webDriver2", chromeDriver);
	
		commA.setServer(androidDriver);
		commA.emailLogin(androidDriver, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 1, enabled = true)
	public void checkDemoBanner() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_demo_popup")).isDisplayed()) {
			failMsg = "DemoBanner is not display";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_lounge_textView")).getText().contentEquals(TXT_DEMO)) {
			failMsg = "DemoBanner text is wrong [Expected]" + TXT_DEMO + " [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_lounge_textView")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void clickDemoBanner() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_demo_popup")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		if(!androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.TextView[1]")).getText().contentEquals(TXT_DEMOPOPUP)) {
			failMsg = failMsg + "\n2.Demo Popup text is wrong";
		}
		
		if(!androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.TextView[2]")).getText().contentEquals(TXT_DEMOPOPUP2)) {
			failMsg = failMsg + "\n3.Demo Popup text is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void closeDemoBannerPopup() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button1")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isEmpty()) {
			failMsg = "Demo start Popup is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void startDemo() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_demo_popup")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/pip_screen")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Wrong Activity [Expected]" + CommonAndroid.MEETING_ROOMACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
		
	}
	
	@Test(priority = 5, enabled = true)
	public void wait25s() throws Exception {
		
		TimeUnit.SECONDS.sleep(25);
	}
	
	@Test(priority = 6, enabled = true)
	public void checkMenuLoginDemo() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
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
	
	@Test(priority = 7, enabled = true)
	public void clickLockLoginDemo() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "비밀번호 잠금");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='비밀번호 잠금']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_DOCSHARE);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void clickRecordLoginDemo() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='녹화']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_DOCSHARE);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void clickModeratorModeLoginDemo() throws Exception {
		String failMsg = "";

		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "사회자 모드");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='사회자 모드']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_DOCSHARE);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void checkDOCShareMsg() throws Exception {
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "문서공유");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='문서공유']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_DOCSHARE);
		
	}

	@Test(priority = 11, enabled = true)
	public void clickExitLoginDemo() throws Exception {
		String failMsg = "";
		
		if(androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/menu_list")).isEmpty()) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();	
		}
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText().contentEquals(TXT_DEMOEXITPOPUP)) {
			failMsg = "Exit Popup text is wrong [Expected]" + TXT_DEMOEXITPOPUP + " [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button1")).click();
	
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isEmpty()) {
			failMsg = failMsg + "\n2.Exit Popup is still display";
		}
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = failMsg + "\n3.Don't maintain Activity [Expected]" + CommonAndroid.MEETING_ROOMACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void exitLoginDemo() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));

		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/home_ex_activity")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_HOMEACTIVITY)) {
			failMsg = "Don't intent Activity [Expected]" + CommonAndroid.MEETING_HOMEACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void closeDemoBanner() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_lounge_close")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/live_meeting_demo_popup")));

		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/live_meeting_demo_popup")).isEmpty()) {
			failMsg = "DemoBanner is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void logout() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[3]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SETTINGS"));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_logout")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)));

		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_LOGINACTIVITY)) {
			failMsg = "Don't logout [Expected]" + CommonAndroid.MEETING_LOGINACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}		
	}
	
	@Test(priority = 15, enabled = true)
	public void reLogin() throws Exception {
		
		commA.emailLogin(androidDriver, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 16, enabled = true)
	public void reCheckDemoBanner() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_demo_popup")).isDisplayed()) {
			failMsg = "DemoBanner is not display";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_lounge_textView")).getText().contentEquals(TXT_DEMO)) {
			failMsg = "DemoBanner text is wrong [Expected]" + TXT_DEMO + " [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_lounge_textView")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void checkRoomPage() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText().contentEquals("ROOM")) {
			failMsg = "Wrong Header [Expected] ROOM [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText();
		}
		
		if(!androidDriver.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).isSelected()) {
			failMsg = failMsg + "\n2.Don't selected Room Btn";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void clickSchedule() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SCHEDULE"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText().contentEquals("SCHEDULE")) {
			failMsg = "Wrong Header [Expected] SCHEDULE [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText();
		}
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "ROOM"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText().contentEquals("ROOM")) {
			failMsg = failMsg + "\n2.Wrong Header [Expected] ROOM [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19, enabled = true)
	public void clickEmptyRoom() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.RelativeLayout")).click();

		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_layout")).isDisplayed()) {
			failMsg = "Create Popup is not display";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/custom_text")).getText().contentEquals("회의 시작")) {
			failMsg = failMsg + "\n2.Wrong text [Expected] 회의 시작 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/custom_text")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void weblogin() throws Exception {
		
		chromeDriver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(chromeDriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(chromeDriver, CommonValues.USERS[0], CommonValues.USERPW);
		
	}
	
	@Test(priority = 21, enabled = true)
	public void createMeeting_Web() throws Exception {
		
		comm.createNormalMeeting(chromeDriver, "LoungeTest");
	}
	
	@Test(priority = 22, enabled = true)
	public void clickDoingRoom() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='LoungeTest']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_attendee_list_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_attendee_name")).getText().contentEquals("rmrsup1")) {
			failMsg = "Wrong attendee [Expected] rmrsup1 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_attendee_name")).getText();
		}
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/custom_text")).getText().contentEquals("회의 참여")) {
			failMsg = failMsg + "\n2.Wrong text [Expected] 회의 참여 [Actual]" +androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/custom_text")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, enabled = true)
	public void settingLock() throws Exception {
		
		chromeDriver.findElement(By.xpath("//button[@id='lock']")).click();
		
		WebDriverWait wait = new WebDriverWait(chromeDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='lock-drop-box']")));
		
		chromeDriver.findElement(By.xpath("//button[@class='on']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
	}
	
	@Test(priority = 24, enabled = true)
	public void checkLockMsg() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_lock_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/lock_description")).getText().contentEquals(MSG_LOCK)) {
			failMsg = "Wrong Msg [Expected" + MSG_LOCK + " [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/lock_description")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 25, enabled = true)
	public void settingPW() throws Exception {
	
		chromeDriver.findElement(By.xpath("//a[@id='set-password']")).click();
	
		WebDriverWait wait = new WebDriverWait(chromeDriver, 15);
	
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
	
	}
	
	@Test(priority = 26, enabled = true)
	public void checkPWMsg() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_lock_password_layout")));
		
		if (!androidDriver.findElement(By.xpath(
				"//android.widget.ScrollView/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.TextView"))
				.getText().contentEquals(MSG_PW)) {
			
			failMsg = "Wrong Msg [Expected]" + MSG_PW + " [Actual]" + androidDriver.findElement(By.xpath(
					"//android.widget.ScrollView/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.TextView"))
					.getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_lock_password_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 27, enabled = true)
	public void exitMeeting_Web() throws Exception {
		String failMsg = "";
		
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		comm.waitForLoad(chromeDriver);
		
		WebDriverWait wait = new WebDriverWait(chromeDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		
		if (!chromeDriver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = "1.Can't leave Room [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL
					+ " [Actual]" + chromeDriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	@Test(priority = 28, enabled = true)
	public void checkLoungeUnderText() throws Exception {
		String failMsg = "";
		
		commA.scrollToAnElementByText(androidDriver, "특허로 보호받고 있는 라운지 UX");
		
		String text = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_footer_text")).getText();
		
		if(!text.contentEquals("특허로 보호받고 있는 라운지 UX")) {
			failMsg = "Wrong text [Expected] 특허로 보호받고 있는 라운지 UX [Actual]" + text;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 29, enabled = true)
	public void clickFloatingBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/floating_button")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_add_reservation")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_accesscode_meeting")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_meeting")).isDisplayed()) {
			failMsg = "Btns are not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 30, enabled = true)
	public void closeFloatingBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/floating_menu_add_reservation")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/floating_menu_add_reservation")).isEmpty()
				|| !androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_accesscode_meeting")).isEmpty()
				|| !androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_meeting")).isEmpty()) {
			failMsg = "Btns are still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 31, enabled = true)
	public void clickAddReservationBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/floating_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_add_reservation")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_header")));
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText().contentEquals("예약 추가")) {
			failMsg = "Wrong Header [Expected] 예약 추가 [Actual]" + androidDriver.findElement(By.xpath("//android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_left_image_button")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_header")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 32, enabled = true)
	public void clickStartMeetingBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/floating_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_meeting")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isDisplayed()) {
			failMsg = "Create Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 33, enabled = true)
	public void createMeeting_FloatingBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_edit_text")).sendKeys("LoungeTest");
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Don't enter room [Expected]" + CommonAndroid.MEETING_ROOMACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 34, enabled = true)
	public void FindMeeting_Web() throws Exception {
		String failMsg = "";
		
		if(chromeDriver.findElements(By.xpath("//p[contains(text(), 'LoungeTest')]")).isEmpty()) {
			failMsg = "Can't create Meeting";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		androidDriver.quit();
		chromeDriver.quit();
	}
	
}
