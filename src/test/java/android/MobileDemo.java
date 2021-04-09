package android;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/*
 * 1.하단 데모 배너 확인
 * 2.데모 팝업 확인
 * 3.데모 팝업 닫기 확인
 * 4.데모 시작
 * 5.pip 이름 확인
 * 6.20초 대기
 * 7.메뉴 체크
 * 8.그룹 가입 클릭 시 토스트 메세지 확인
 * 9.잠금 클릭 시 팝업 확인
 * 10.녹화 클릭 시 팝업 확인
 * 11.사회자 모드 클릭 시 팝업 확인
 * 12.문서 공유 클릭 시 토스트 메세지 확인
 * 13.데모 종료 클릭 시 팝업 확인
 * 14.데모 종료
 */

public class MobileDemo {
	
	private static String TXT_DEMO = "AI데모 ‘아루’와 함께\n" + "생생한 라이브미팅을 체험해보세요.";
	private static String TXT_DEMOPOPUP = "AI데모 ‘아루’와 함께 생생한 라이브미팅을 체험해보세요.리모트미팅의 사용법과 기능을 쉽게 알아보세요.";
	private static String TXT_EXIT = "지금 회의실을 나가시면 처음부터 다시 시작하셔야 합니다. 그래도 나가시겠습니까?";
	
	private static String TOAST_JOINGROUP = "로그인된 회원에게만 제공되는 기능입니다. 로그인을 하시거나 무료체험에 가입해 보세요.";
	private static String TOAST_DOCSHARE = "라이브미팅 중에는 사용 할 수 없습니다. 라이브미팅 종료 후 회의를 개설해 이용해 보세요.";
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	
	CommonAndroid commA = new CommonAndroid();
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0,true);
		
		context.setAttribute("webDriver", androidDriver);
		
		commA.setServer(androidDriver);
		
	}
	
	@Test(priority = 1, enabled = true)
	public void checkDemoBanner() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_live_meeting_banner")).isDisplayed()) {
			failMsg = "Demo Banner is not display";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_banner_string")).getText().contentEquals(TXT_DEMO)) {
			failMsg = "Demo txt is wrong [Expected]" + TXT_DEMO + " [Acutal]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_banner_string")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void checkDemoBannerPopup() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_banner_string")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		String DemoPopup = androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.TextView[1]")).getText() + androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.TextView[2]")).getText();
		
		if(!DemoPopup.contentEquals(TXT_DEMOPOPUP)) {
			failMsg = "Demo Popup txt is wrong [Expected]" + TXT_DEMOPOPUP + " [Actual]" + DemoPopup;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
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
			failMsg = "Demo Popup is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void startDemo() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/live_meeting_banner_string")).click();
		
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
	public void checkPIPDemo() throws Exception {
		String failMsg = "";
			
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/conference_pip_user_name")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_user_name")).getText().contentEquals("Guest")) {
			failMsg = "Pip is wrong [Expected] Guest [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_user_name")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void wait20s() throws Exception {
		
		TimeUnit.SECONDS.sleep(60);
	}
	
	@Test(priority = 7, enabled = true)
	public void checkMenuDemo() throws Exception {
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
	
	@Test(priority = 8, enabled = true)
	public void checkGroupjoinMsg() throws Exception {
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		commA.scrollToAnElementByText(element, "초대");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='초대']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_group_join_button")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_JOINGROUP);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
	}
	
	@Test(priority = 9, enabled = true)
	public void clickLockDemo() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "비밀번호 잠금");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='비밀번호 잠금']")).click();
		
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
	
	@Test(priority = 10, enabled = true)
	public void clickRecordDemo() throws Exception {
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
	
	@Test(priority = 11, enabled = true)
	public void clickModeratorModeDemo() throws Exception {
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
	
	@Test(priority = 12, enabled = true)
	public void checkDOCShareMsg() throws Exception {
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "문서공유");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='문서공유']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_DOCSHARE);
		
	}
	
	@Test(priority = 13, enabled = true)
	public void closeExitPopup() throws Exception {
		String failMsg = "";
		
		if(androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/menu_list")).isEmpty()) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();	
		}
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text='나가기']")));
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText().contentEquals(TXT_EXIT)) {
			failMsg = "Wrong Txt [Expected]" + TXT_EXIT + " [Actual]" +androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button1")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isEmpty()) {
			failMsg = failMsg + "\n2.Exit Popup is not closed";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void ExitDemo() throws Exception {
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
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/login_main_rootview")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_LOGINACTIVITY)) {
			failMsg = "Don't intent Activity [Expected]" + CommonAndroid.MEETING_LOGINACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		androidDriver.quit();	
	}
}
