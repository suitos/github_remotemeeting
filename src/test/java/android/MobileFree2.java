package android;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import mandatory.CommonValues;

/*
 * Free룸 개설 - 모바일 유료 참석자 및  웹 참석 후 문서공유 시나리오(모바일 로그인 계정 : rmrsupadm@gmail.com)
 * 1.Free버전 회의 개설
 * 2.코드 가져오기
 * 3.참석 코드란에 빈값 넣기
 * 4.참석 코드란에 6자리 미만 값 넣기
 * 5.잘못된 코드 넣기
 * 6.해당 팝업창 닫기
 * 7.참석하기
 * 8.참석 토스트 메세지 및 pip확인
 * 9.타이머 노출되는지 확인
 * 10.백그라운드 클릭 시 메뉴 닫힘 확인
 * 11.유료참석자가 비밀번호 잠금 클릭
 * 12.유료참석자가 녹화 클릭
 * 13.유료참석자가 모드 변경 클릭 
 * 14.유료참석자가 사회자 모드 클릭
 * 15.유료참석자 캠 끄기
 * 16.유료참석자 캠 켜기
 * 17.유료참석자 마이크 끄기
 * 18.유료참석자 마이크 켜기
 * 19.유료참석자 나가기
 * 20.웹참석자 참석
 * 21.웹참석자 문서 공유 시작
 * 22.문서 공유 종료
 * 23.Free룸 개설자 문서 공유 시작 
 */

public class MobileFree2 {
	
	private static String NICKNAME;
	private static String code;
	private static String src;
	
	private static String Wrongcode = "111111";
	
	private static String TOAST_EMPTYCODE = "6자리 접속코드를 입력 하세요.";
	private static String TOAST_WRONGCODE = "접속 코드가 일치하지 않습니다. 다시 확인 해주세요.";
	//private static String TOAST_ATTEND = " 님이 참여했습니다.";
	private static String TOAST_IMPOSSIBLE = "Free 버전 회의에서는 사용하실 수 없습니다.";
	private static String TOAST_CAMOFF = "카메라가 꺼졌습니다. 상대방에게 영상이 보여지지 않습니다.";
	private static String TOAST_CAMON = "카메라가 켜졌습니다.";
	private static String TOAST_MICOFF = "마이크가 꺼졌습니다. 상대방에게 소리가 들리지 않습니다.";
	private static String TOAST_MICON = "마이크가 켜졌습니다.";
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	public static AndroidDriver<AndroidElement> androidDriver2 = null;

	public static WebDriver chromeDriver;
	
	CommonAndroid commA = new CommonAndroid();
	CommonValues comm = new CommonValues();
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0,true);
		androidDriver2 = commA.setAndroidDriver(1,true);
		
		comm.setDriverProperty("Chrome_test");

		chromeDriver = comm.setDriver(chromeDriver, "Chrome_test", "lang=ko_KR", true);
		
		context.setAttribute("webDriver", androidDriver);
		context.setAttribute("webDriver2", androidDriver2);
		context.setAttribute("webDriver3", chromeDriver);
		
		commA.setServer(androidDriver);
		commA.setServer(androidDriver2);
		
		commA.emailLogin(androidDriver2, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 1, enabled = true)
	public void createFree() throws Exception {
		String failMsg = "";
		
		NICKNAME = "Free Nickname";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/login_main_create_btn")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_create_btn")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/demo_conference_nickname_edittext")).sendKeys(NICKNAME);
		
		Thread.sleep(1000);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Don't enter room [Expected]" + CommonAndroid.MEETING_ROOMACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void getCode() throws Exception {
		
		code = commA.getAccessCode(androidDriver);
		System.out.println(code);
	}
	
	@Test(priority = 3, enabled = true)
	public void AttendPayaccount_Emptycode() throws Exception {
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_accesscode_meeting")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_accesscode_meeting")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/accesscode_input_area")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_ok_button")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_EMPTYCODE);
			
	}
	
	@Test(priority = 4, enabled = true)
	public void AttendPayaccount_Undercode() throws Exception {
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_code_text")).sendKeys("11111");
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_ok_button")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_EMPTYCODE);
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_code_text")).clear();
		
	}
	
	@Test(priority = 5, enabled = true)
	public void AttendPayaccount_Wrongcode() throws Exception {
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_code_text")).sendKeys(Wrongcode);
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_ok_button")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_WRONGCODE);
		
	}
	
	@Test(priority = 6, enabled = true)
	public void AttendPayaccount_ClosePopup() throws Exception {
		String failMsg = "";
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_cancel_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/accesscode_cardView")));
		
		if(!androidDriver2.findElements(By.id("com.rsupport.remotemeeting.application:id/accesscode_cardView")).isEmpty()) {
			failMsg = "Popup is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void Attend_Payaccount() throws Exception {
		String failMsg = "";
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_accesscode_meeting")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_menu_start_accesscode_meeting")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/accesscode_input_area")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_code_text")).sendKeys(code);
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/access_ok_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_cardView")));
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/connect_screen")));
		
		if(!androidDriver2.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Don't intent Activity [Expected]" + CommonAndroid.MEETING_ROOMACTIVITY + " [Actual]" + androidDriver2.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void checkAttend() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")).isEmpty()) {
			failMsg = "Invite Popup is not display when Attend another attendee";
		}
		
		//commA.CheckToastMsg(androidDriver, CommonValues.ADMINNICKNAME + TOAST_ATTEND);
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/active_speaker_user_pip_gridlayout")).isDisplayed()) {
			failMsg = failMsg + "\n2.PIP is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void checkTimer() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_timer")).isDisplayed()) {
			failMsg = "Timer is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void clickBackground() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_other_area")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/menu_list")).isEmpty()) {
			failMsg = "Menu is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void clickLock_PayAcount() throws Exception {
		
		androidDriver2.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='비밀번호 잠금']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_IMPOSSIBLE);
	}
	
	@Test(priority = 12, enabled = true)
	public void clickRecord_PayAcount() throws Exception {
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='녹화']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_IMPOSSIBLE);
	}
	
	@Test(priority = 13, enabled = true)
	public void clickMode_PayAcount() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "모드 변경");
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='모드 변경']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_cardView")));
		
		if(!androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_cardView")).isDisplayed()) {
			failMsg = "Popup is not display when click Mode";
		}
		
		List <AndroidElement> menu = androidDriver2.findElements(By.id("com.rsupport.remotemeeting.application:id/select_item_text"));
		
		String [] a= new String[menu.size()];
		
		for(int i = 0; i < menu.size(); i++) {
			a[i] = menu.get(i).getText();
			
			if (i == 0) {
				if (!a[i].contentEquals("주화자 모드")) {
					failMsg = "Wrong Menu [Expected] 주화자 모드 [Actual]" + a[i];
				}
			} else if (i == 1) {
				if (!a[i].contentEquals("분할 모드")) {
					failMsg = "Wrong Menu [Expected] 분할 모드 [Actual]" + a[i];
				}
			} else {
				if (!a[i].contentEquals("강조 모드")) {
					failMsg = "Wrong Menu [Expected] 강조 모드 [Actual]" + a[i];
				}
			}
		}
		
		androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/select_popup_cancel_btn")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_cardView")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void clickModeratorMode_PayAcount() throws Exception {
		
		AndroidElement element = androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "사회자 모드");
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='사회자 모드']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_IMPOSSIBLE);

	}
	
	@Test(priority = 15, enabled = true)
	public void clickCamOff_PayAcount() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "카메라");
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='카메라']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_CAMOFF);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/conference_pip_novideo_bg")));
		
		if(!androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_novideo_bg")).isDisplayed()
				|| !androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_profile_circle")).isDisplayed()) {
			failMsg = "Camera not off and not display default img in attendee";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_novideo_bg")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_profile_circle")).isDisplayed()) {
			failMsg = "Camera not off and not display default img in creator";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void clickCamOn_PayAcount() throws Exception {
		String failMsg = "";
		
		Thread.sleep(1500);
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='카메라']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_CAMON);
		
		if(!androidDriver2.findElements(By.id("com.rsupport.remotemeeting.application:id/conference_pip_novideo_bg")).isEmpty()
				|| !androidDriver2.findElements(By.id("com.rsupport.remotemeeting.application:id/conference_pip_profile_img")).isEmpty()) {
			failMsg = "Camera not on and still display default img in attendee";
		}
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/conference_pip_novideo_bg")).isEmpty()
				|| !androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/conference_pip_profile_img")).isEmpty()) {
			failMsg = "Camera not on and still display default img in creator";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void clickMicOff_PayAcount() throws Exception {
		String failMsg = "";
		
		Thread.sleep(1500);
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='마이크']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_MICOFF);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/conference_pip_status_mute")));
		
		if(!androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_status_mute")).isDisplayed()) {
			failMsg = "MicOff img not display in attendee";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_status_mute")).isDisplayed()) {
			failMsg = "MicOff img not display in creator";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void clickMicOn_PayAcount() throws Exception {
		String failMsg = "";
		
		Thread.sleep(1500);

		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='마이크']")).click();
		
		commA.CheckToastMsg(androidDriver2, TOAST_MICON);
		
		if(!androidDriver2.findElements(By.id("com.rsupport.remotemeeting.application:id/conference_pip_status_mute")).isEmpty()) {
			failMsg = "MicOff img still display in attendee";
		}
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/conference_pip_status_mute")).isEmpty()) {
			failMsg = "MicOff img still display in creator";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority = 19, enabled = true)
	public void clickExit_PayAcount() throws Exception {
		String failMsg = "";
		
		Thread.sleep(1500);

		AndroidElement element = androidDriver2.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		androidDriver2.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver2, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/home_ex_activity")));
		
		if(!androidDriver2.currentActivity().contentEquals(CommonAndroid.MEETING_HOMEACTIVITY)) {
			failMsg = "Don't leave room [Expected]" + CommonAndroid.MEETING_HOMEACTIVITY + " [Actual]" + androidDriver2.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void AttendWeb() throws Exception {
		String failMsg = "";
	
		NICKNAME = "ChromeAttendee";
		
		chromeDriver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait2 = new WebDriverWait(chromeDriver, 20);
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_FREECREATEATTEND_BTN)).click();
		Thread.sleep(2000);
		
		for (int i = 0; i < code.length(); i++) {
			chromeDriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(code.substring(i, i+1));
	          Thread.sleep(1000);
		}
	          
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_FREECREATEATTEND_BTN)).click();
		
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)));
		
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).sendKeys(NICKNAME);
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)).click();
		
		comm.waitForLoad(chromeDriver);
		
		wait2.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//section[@id='conference-wrap']")));
		wait2.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait2.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='device-setting-notifications-box-wrapper']"), "style", "display: none;"));
		
		
		if(!chromeDriver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = "Wrong URL [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + " [Actual]" + chromeDriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 21, enabled = true)
	public void ShareDoc_Web() throws Exception {
		String failMsg = "";
		
		ShareDocument(chromeDriver,4,1);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.Image")));
		
        src = chromeDriver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
        System.out.println(src);
        
		if(!src.contains(androidDriver.findElement(By.xpath("//android.widget.Image")).getText())) {
			failMsg = "Wrong src";
		}
		
		if(!androidDriver.findElement(By.xpath("//android.widget.Image")).isDisplayed()) {
			failMsg = failMsg + "\n2.Image is not display" + src;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 22, enabled = true)
	public void FinishShareDoc_Web() throws Exception {

		chromeDriver.findElement(By.xpath("//button[@id='doc-close']")).click();
		
		WebDriverWait wait = new WebDriverWait(chromeDriver, 20);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//article[@id='document-content']/img")));
		
        WebDriverWait wait2 = new WebDriverWait(androidDriver, 20);
        wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//android.widget.Image")));
		
	}
	
	@Test(priority = 23, enabled = true)
	public void ShareDoc_Mobile() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		androidDriver.findElement(By.xpath("//android.widget.RelativeLayout[6]/android.widget.ImageView")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_cardView")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/document_share_recent_list")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/document_share_list_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/document_share_recent_list_file_text")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.Image")));
		
		WebDriverWait wait2 = new WebDriverWait(chromeDriver, 20);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//article[@id='document-content']/img")));
		
		src = chromeDriver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		System.out.println(src);
		
		if(!src.contains(androidDriver.findElement(By.xpath("//android.widget.Image")).getText())) {
			failMsg = "Wrong src";
		}
		
		if(!androidDriver.findElement(By.xpath("//android.widget.Image")).isDisplayed()) {
			failMsg = failMsg + "\n2.Image is not display";
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
