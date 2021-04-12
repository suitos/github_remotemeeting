package android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import android.CommonAndroid.scrollDirection;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import mandatory.CommonValues;

public class AttendeesList {


	public static String ID_ROOM_PIP_GRIDLAYOUT = "com.rsupport.remotemeeting.application:id/active_speaker_user_pip_gridlayout";
	public static String ID_ROOM_PIP_ADMINICON = "com.rsupport.remotemeeting.application:id/voice_admin_icon";
	
	public static String XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN = "//android.widget.TextView[@text='참여자 리스트']";
	public static String XPATH_ROOM_SIDEMENU_LEAVE_BTN = "//android.widget.TextView[@text='나가기']";

	public static String XPATH_ROOM_ATTENDEELIST = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup";
	public static String XPATH_ROOM_ATTENDEELIST_MIC = ".//android.widget.ImageButton[1]";
	public static String XPATH_ROOM_ATTENDEELIST_CAM = ".//android.widget.ImageButton[2]";
	public static String ID_ROOM_ATTENDEELIST_CROWN = "com.rsupport.remotemeeting.application:id/admin_icon";
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	
	public static List<String> usernames = null;
	
	public static WebDriver chromeDriver = null;
	
	CommonAndroid commA = new CommonAndroid();
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0, true);

		chromeDriver = comm.setDriver(chromeDriver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", chromeDriver);
		context.setAttribute("webDriver2", androidDriver);
		
		commA.setServer(androidDriver); 
		// TODO :  유저 바꿔야함 : ADMEMAIL
		commA.emailLogin(androidDriver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		chromeDriver.get(CommonValues.MEETING_URL);
		comm.login(chromeDriver, CommonValues.USERS[0], CommonValues.USERPW);
		
		usernames = new ArrayList<String>(); 
	}
	
	//1. android 혼자 입장. 참여자 리스트 보기 가능
	@Test(priority = 1, enabled = true)
	public void attendAlone() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_TAB)).isSelected()) {
			androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_TAB)).click();
		}
		
		List<AndroidElement> boxlist = androidDriver.findElements(By.id(CommonAndroid.ID_LOUNGE_ROOM_CARD));
		
		for (AndroidElement box : boxlist) {
			if(!findElement(box, true, CommonAndroid.ID_LOUNGE_ROOM_INFOBOX)) {
				box.click();
				break;
			}
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_INPUT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_INPUT)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_INPUT)).sendKeys("androidRoom");
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_INVITE_LAYOUT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_INVITECLOSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_PIP_SCREEN)));

		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		
		//참여자 리스트 클릭 - 참여자 리스트 보고 다시 나가기
		commA.scrollToAnElementByText(element, "참여자 리스트");
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		//룸 나가기
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//2. android 입장 - web 입장
	@Test(priority = 2, enabled = true)
	public void attendTow_List() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_CARD)));
		
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_TAB)).isSelected()) {
			androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_TAB)).click();
		}
		
		List<AndroidElement> boxlist = androidDriver.findElements(By.id(CommonAndroid.ID_LOUNGE_ROOM_CARD));
		
		for (AndroidElement box : boxlist) {
			if(!findElement(box, true, CommonAndroid.ID_LOUNGE_ROOM_INFOBOX)) {
				box.click();
				break;
			}
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_INPUT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_INPUT)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_INPUT)).sendKeys("androidRoom");
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_CREATEROOM_POPUP_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_INVITE_LAYOUT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_INVITECLOSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_PIP_SCREEN)));

		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		String code = androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE)).getText().replace(" ", "");

		//chrome 유저 참석
		CommonValues comm = new CommonValues();
		comm.attendRoomLoginUser(chromeDriver, code);
		
		usernames.add(CommonValues.ADMINNICKNAME);
		usernames.add("rmrsup1");
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		//참석한 유저 android에서 확인
		wait.until(ExpectedConditions.numberOfElementsToBe(By.id(MobileShare.ID_ROOM_PIP_USERNAME), 2));
		
		//android 참여자 리스트 확인
		if(!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			commA.scrollToAnElementByText(element, "나가기");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		}
		
		
		commA.scrollToAnElementByText(element, "참여자 리스트");
		
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		List<AndroidElement> list = androidDriver.findElements(By.xpath(XPATH_ROOM_ATTENDEELIST));
		if(list.size() != 2) {
			failMsg = failMsg + "\n1. attendees list size [Expected]2 [Actual]" + list.size();
		} else {
			List<String> sortedname = new ArrayList<String>();
			sortedname.addAll(usernames);
			Collections.sort(sortedname);
			
			if(!list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText().contentEquals(sortedname.get(0))) {
				failMsg = failMsg + "\n2. attendeesList nickname 1 [Expected]" + sortedname.get(0)
						+ " [Actual]" + list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText();
			}
			if(!list.get(1).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText().contentEquals(sortedname.get(1))) {
				failMsg = failMsg + "\n3. attendeesList nickname 2 [Expected]" + sortedname.get(1)
						+ " [Actual]" + list.get(1).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText();
			}
		}
		//참석자 리스트 나가기
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//3. 웹유저가 사회자 - android 유저가 참여자 리스트 확인
	@Test(priority = 3, dependsOnMethods = {"attendTow_List"}, alwaysRun = true, enabled = true)
	public void attendTow_MCmode_list() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		WebDriverWait wait_chrome = new WebDriverWait(chromeDriver, 10);

		// 웹유저 사회자 하기
		if(!chromeDriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).getAttribute("class").contains("active")) {
			chromeDriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).click();
			wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='speak-right-dialog']")));
			chromeDriver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
			
		}
		//android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
			commA.scrollToAnElementByText(element, "나가기");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		}
		
		// 사용자 리스트 클릭
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		commA.scrollToAnElementByText(element, "참여자 리스트");
		
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		List<AndroidElement> list = androidDriver.findElements(By.xpath(XPATH_ROOM_ATTENDEELIST));
		if(list.size() != 2) {
			failMsg = failMsg + "\n1. attendees list size [Expected]2 [Actual]" + list.size();
		} else {
			//첫유저는 사회자
			if(!list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText().contentEquals(usernames.get(1))) {
				failMsg = failMsg + "\n2. attendeesList nickname 1 [Expected]" + usernames.get(1)
						+ " [Actual]" + list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText();
			}
			//웹유저 사회자 아이콘 확인
			if(!findElement(list.get(0), true, ID_ROOM_ATTENDEELIST_CROWN)) {
				failMsg = failMsg + "\n3. cannot find crown icon. ";
			}
			
			if(!list.get(1).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText().contentEquals(usernames.get(0))) {
				failMsg = failMsg + "\n4. attendeesList nickname 2 [Expected]" + usernames.get(0)
						+ " [Actual]" + list.get(1).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText();
			}
			
		}

		// 참석자 리스트 나가기
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//4. 웹유저가 사회자 - android 유저가 참여자 리스트에서 카메라/마이크 아이콘 비활성화 확인
	@Test(priority = 4, dependsOnMethods = {"attendTow_List"}, alwaysRun = true, enabled = true)
	public void attendTow_MCmode1() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		WebDriverWait wait_chrome = new WebDriverWait(chromeDriver, 10);

		// 웹유저 사회자 하기
		if(!chromeDriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).getAttribute("class").contains("active")) {
			chromeDriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).click();
			wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='speak-right-dialog']")));
			chromeDriver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
			
		}
	
		//android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
			commA.scrollToAnElementByText(element, "나가기");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		}
		
		// 사용자 리스트 클릭
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		commA.scrollToAnElementByText(element, "참여자 리스트");
		
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		List<AndroidElement> list = androidDriver.findElements(By.xpath(XPATH_ROOM_ATTENDEELIST));
		if(list.size() != 2) {
			failMsg = failMsg + "\n1. attendees list size [Expected]2 [Actual]" + list.size();
		} else {
			//user1
			if(list.get(0).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_MIC)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n2. 1user's mic button is enabled.";
			}
			if(list.get(0).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_CAM)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n3. 1user's cam button is enabled.";
			}
			
			//user2
			if(list.get(1).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_MIC)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n4. 2user's mic button is enabled.";
			}
			if(list.get(1).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_CAM)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n4. 2user's cam button is enabled.";
			}
			
		}

		// 참석자 리스트 나가기
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//4. 웹유저가 사회자 - android 유저가 발언권 요청 하기 위해 확인
	@Test(priority = 5, dependsOnMethods = {"attendTow_List"}, alwaysRun = true, enabled = true)
	public void attendTow_reqVoice() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		WebDriverWait wait_chrome = new WebDriverWait(chromeDriver, 10);

		// 웹유저 사회자 하기
		if(!chromeDriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).getAttribute("class").contains("active")) {
			chromeDriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).click();
			wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='speak-right-dialog']")));
			chromeDriver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();
		}
		
		// 오디오 다끔
		chromeDriver.findElement(By.xpath("//div[@class='control-mic-all']//button[1]")).click();
		wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='control-select-box']")));
		chromeDriver.findElement(By.xpath("//li[@class='label mic select']")).click();
		chromeDriver.findElement(By.xpath("//button[@id='all-mic-off']")).click();
		
		wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		wait_chrome.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE)));
		}

		//위로 스크롤해보기
		commA.swipeElementAndroid(androidDriver, androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list")), scrollDirection.DOWN);

		//발언권 요청 버튼 확인
		if(!androidDriver.findElement(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView")).getText().contains("발언권요청")) {
			failMsg = failMsg + "\n1. cannot find button 발언권요청 [Actual]" 
					+ androidDriver.findElement(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView")).getText();
		} else {
			//발언권 요청 버튼 클릭
			androidDriver.findElement(By.xpath("//androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.TextView")).click();
			
			//웹유저 발언권 요청 토스트 확인
			wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
			String msg = chromeDriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
			String expected = usernames.get(0) + "님이 발언권을 요청하셨습니다.";
			if(!msg.contentEquals(expected)) {
				failMsg = failMsg + "\n2. toast msg [Expected]" + expected + " [Actual]" + msg;
			}
		}
		
		
		/*
		// android 사이드 메뉴 보이면 클릭
		if (commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEEXIT_BTN)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE)));
		}
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//10. android 유저가 사회자
	@Test(priority = 10, dependsOnMethods = {"attendTow_List"}, alwaysRun = true, enabled = true)
	public void attendTow_MCandroid() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
	
		//android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
			commA.scrollToAnElementByText(element, "나가기");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		}
		
		
		
		//android사회자 아니면 사회자 클릭
		List<MobileElement> userPip = androidDriver.findElement(By.id(ID_ROOM_PIP_GRIDLAYOUT)).findElements(By.xpath(".//android.view.ViewGroup"));
		for (MobileElement mobileElement : userPip) {
			if(mobileElement.findElement(By.id(MobileShare.ID_ROOM_PIP_USERNAME)).getText().contentEquals(CommonValues.ADMINNICKNAME)) {
				if(!findElement((AndroidElement)mobileElement, true, ID_ROOM_PIP_ADMINICON)) {
					
					AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
					commA.scrollToAnElementByText(element, "사회자 모드");
					androidDriver.findElement(By.xpath("//android.widget.TextView[@text='사회자 모드']")).click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_POPUP_BUTTON2)));
					androidDriver.findElement(By.id(CommonAndroid.ID_POPUP_BUTTON2)).click();
					break;
				}
			}
		}
		
	
		//android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
			commA.scrollToAnElementByText(element, "나가기");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_LEAVE_BTN)));
		}		
		
		
		// 사용자 리스트 클릭
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		commA.scrollToAnElementByText(element, "참여자 리스트");
		
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		List<AndroidElement> list = androidDriver.findElements(By.xpath(XPATH_ROOM_ATTENDEELIST));
		if(list.size() != 2) {
			failMsg = failMsg + "\n1. attendees list size [Expected]2 [Actual]" + list.size();
		} else {
			//user1
			if(!list.get(0).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_MIC)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n2. 1user's mic button is disabled.";
			}
			if(!list.get(0).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_CAM)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n3. 1user's cam button is disabled.";
			}
			//nickname : 사회자 먼저 확인
			if(!list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText().contentEquals(usernames.get(0))) {
				failMsg = failMsg + "\n4. attendeesList nickname 1 [Expected]" + usernames.get(0)
				+ " [Actual]" + list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText();
			}
			
			//user2
			if(!list.get(1).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_MIC)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n5. 2user's mic button is disabled.";
			}
			if(!list.get(1).findElement(By.xpath(XPATH_ROOM_ATTENDEELIST_CAM)).getAttribute("clickable").contains("true")) {
				failMsg = failMsg + "\n6. 2user's cam button is disabled.";
			}
			//nickname : 웹유저
			if(!list.get(1).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText().contentEquals(usernames.get(1))) {
				failMsg = failMsg + "\n7. attendeesList nickname 2 [Expected]" + usernames.get(1)
				+ " [Actual]" + list.get(0).findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_NICKNAME)).getText();
			}
			
		}

		// 참석자 리스트 나가기
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//11. 발언권 요청
	@Test(priority = 11, dependsOnMethods = {"attendTow_List"}, alwaysRun = true, enabled = true)
	public void attendTow_reqMIC() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		WebDriverWait wait_chrome = new WebDriverWait(chromeDriver, 10);

		//참석자 마이크 끄고
		if(chromeDriver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).isEnabled()) {
			chromeDriver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		}
		
		//발언권요청 클릭
		wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_REQUESTSPEAK_BTN)));
		chromeDriver.findElement(By.xpath(CommonValues.XPATH_REQUESTSPEAK_BTN)).click();
		Thread.sleep(1000);
		
		//pip에서 발언권 요청 아이콘 확인
		List<MobileElement> userPip = androidDriver.findElement(By.id(ID_ROOM_PIP_GRIDLAYOUT)).findElements(By.xpath(".//android.view.ViewGroup"));
		for (MobileElement mobileElement : userPip) {
			if(mobileElement.findElement(By.id(MobileShare.ID_ROOM_PIP_USERNAME)).getText().contentEquals(usernames.get(1))) {
				if(!findElement((AndroidElement)mobileElement, true, CommonAndroid.ID_ROOM_PIP_VOICEREQICON)) {
					failMsg = failMsg + "\n1. cannot find voice request icon in pip.";
				}
				break;
			}
		}
		
		// android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
			commA.scrollToAnElementByText(element, "참여자 리스트");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)));
		}
		
		//android 에서 발언권요청 뷰 확인
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));

		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_VOICEREQ_VIEW))) {
			failMsg = failMsg + "\n2. not displayed voice request view.";
		} else {
			// 요청 닉네임 확인
			List<AndroidElement> reqUsers = androidDriver.findElements(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_VOICEREQ_USERNAME));
			if (reqUsers.size() != 0) {
				if (!reqUsers.get(0).getText().contentEquals(usernames.get(1))) {
					failMsg = failMsg + "\n3. request user name [Expected]" + usernames.get(1) + " [Actual]"
							+ reqUsers.get(0).getText();
				}
			}
		}
		
		// 참석자 리스트 나가기
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//12. 발언권 요청 취소(웹유저), android사회자 발언권 확인
	@Test(priority = 12, dependsOnMethods = {"attendTow_List", "attendTow_reqMIC"}, alwaysRun = true, enabled = true)
	public void attendTow_reqCancel() throws Exception {
		
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		WebDriverWait wait_chrome = new WebDriverWait(chromeDriver, 10);

		//참석자 마이크 끄고
		if(chromeDriver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).isEnabled()) {
			chromeDriver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		}
		
		//발언권요청 취소 (한번더 클릭)
		wait_chrome.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_REQUESTSPEAK_BTN)));
		if(chromeDriver.findElement(By.xpath(CommonValues.XPATH_REQUESTSPEAK_BTN)).getAttribute("class").contains("active")) {
			chromeDriver.findElement(By.xpath(CommonValues.XPATH_REQUESTSPEAK_BTN)).click();
			Thread.sleep(1000);
		}
		
		
		//pip에서 발언권 요청 아이콘 없어짐
		List<MobileElement> userPip = androidDriver.findElement(By.id(ID_ROOM_PIP_GRIDLAYOUT)).findElements(By.xpath(".//android.view.ViewGroup"));
		for (MobileElement mobileElement : userPip) {
			if(mobileElement.findElement(By.id(MobileShare.ID_ROOM_PIP_USERNAME)).getText().contentEquals(usernames.get(1))) {
				if(findElement((AndroidElement)mobileElement, true, CommonAndroid.ID_ROOM_PIP_VOICEREQICON)) {
					failMsg = failMsg + "\n1. find voice request icon in pip.";
				}
				break;
			}
		}
		
		// android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
			commA.scrollToAnElementByText(element, "참여자 리스트");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)));
		}
		
		//android 에서 발언권요청 뷰 확인
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_ATTENDEELIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
		
		if(commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_VOICEREQ_VIEW))) {
			failMsg = failMsg + "\n2. displayed voice request view.";
		}
		// 참석자 리스트 나가기
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_ATTENDEELIST_LEAVE_ARROW)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ROOM_ATTENDEELIST)));
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	public static boolean findElement(AndroidElement el, boolean isID, String subel) {

		try {
			if(isID) {
				el.findElement(By.id(subel));
			} else {
				el.findElement(By.xpath(subel));
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		chromeDriver.quit();
		androidDriver.quit();
	}

}
