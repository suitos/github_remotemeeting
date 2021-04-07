package android;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import mandatory.CommonValues;

/*
 * 1.방제 입력 없이 회의 개설 시도
 * 2.회의 개설
 * 3.pip not display 확인
 * 4.외부 앱 공유 팝업 열리는지 확인
 * 5.메뉴 열리는지 확인
 * 
 * 
 * 
 * 
 * 
 * 10.메뉴 확인
 * 11.카메라 끌 경우 카메라전환 버튼 비활성화 확인
 * 12.타이머 대기중 확인
 * 13.잠금 및 패스워드 길이 확인
 * 14.잠금 해제 확인
 * 15.초대창 확인
 * 
 * 
 * 
 * 
 * 
 * 20.그룹 클릭
 * 21.최근 목록탭 날짜 내림차순 확인
 * 22.최근목록 없을 경우 확인
 * 23.부서 및 전체 주소록 확인
 * 24.초대 레이아웃 닫기
 * 25.사회자 모드,참여자 리스트 enabled 확인
 * 
 * 
 * 
 * 
 * 
 * 30.
 * 31.카메라 전환 클릭
 * 32.카메라 off
 * 33.카메라 on
 * 34.마이크 off
 * 35.마이크 on
 * 
 * 
 * 
 * 
 * 
 * 40.회의록 확인
 * 41.녹화 클릭
 * 42.메뉴 닫기
 * 43.참여자 리스트 내 x버튼 확인
 * 44.x버튼 클릭
 * 45.회의 나가기
 */

public class AloneMode {
	
	private static final String TOAST_EMPTYTITLE = "회의 제목을 입력하세요.";
	private static final String TOAST_CAMOFF = "카메라가 꺼졌습니다. 상대방에게 영상이 보여지지 않습니다.";
	private static final String TOAST_CAMON = "카메라가 켜졌습니다.";
	private static final String TOAST_LOCK = "회의실이 잠금 상태로 변경되었습니다.";
	private static final String TOAST_UNLOCK = "회의실 잠금이 해제 되었습니다.";
	private static final String TOAST_CAMCHANGE = "카메라가 전환되었습니다.";
	private static final String TOAST_MICOFF = "마이크가 꺼졌습니다. 상대방에게 소리가 들리지 않습니다.";
	private static final String TOAST_MICON = "마이크가 켜졌습니다.";
	private static final String TOAST_RECORD = "다른 사용자가 참여한 후 녹화를 시작 할 수 있습니다.";
	
	private static String TITLE;
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	
	CommonAndroid commA = new CommonAndroid();
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		 
		androidDriver = commA.setAndroidDriver(0,true);
		
		context.setAttribute("webDriver", androidDriver);
	
		commA.setServer(androidDriver);
		commA.emailLogin(androidDriver, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 1, enabled = true)
	public void createMeeting_emptyTitle() throws Exception {
	
		List <AndroidElement> empty = androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/box_channel_add_layout"));
		empty.get(0).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_layout")));

		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_button")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_EMPTYTITLE);
	}
	
	@Test(priority = 2, enabled = true)
	public void createMeeting() throws Exception {
		String failMsg = "";
		
		TITLE = "AloneMode Test";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_title_edittext")).sendKeys(TITLE);
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_create_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")).isDisplayed()) {
			failMsg = "Don't display invite layout";
		}
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = failMsg + "Don't enter room";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
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
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void checkDisplay() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/pip_screen")).isDisplayed()) {
			failMsg = "Main display not display";
		}
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/active_speaker_layout")).isEmpty()) {
			failMsg = failMsg + "PIP is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void checkMenulist() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list")).isDisplayed()) {
			failMsg = "Menu list is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void checkMenu() throws Exception {
		String failMsg = "";
		
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
	
	@Test(priority = 11, enabled = true)
	public void checkCamerachangeBtn() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		
		commA.scrollToAnElementByText(element, "카메라");
	
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='카메라']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_CAMOFF);

		//android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE)));
		}
		
		commA.scrollToAnElementByText(element, "카메라 전환");
		
		if(!androidDriver.findElement(By.xpath("//android.widget.TextView[@text='카메라 전환']/..")).getAttribute("enabled").contentEquals("false")) {
			failMsg = "CameraChange Btn is enabled!";
		}
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='카메라']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void checkTimer() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_timer")).getText().contentEquals("대기중")) {
			failMsg = "Timer text is wrong [Expected]대기중 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/conference_timer")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void checkLockandPWLength() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		
		commA.scrollToAnElementByText(element, "비밀번호 잠금");
	
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='비밀번호 잠금']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_LOCK);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_locked_layout")));
		
		if(androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_locked_layout_password")).getText().length() != 8) {
			failMsg = "PW length is not 8 [PW]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_locked_layout_password")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void checkUnlock() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		commA.scrollToAnElementByText(element, "잠금 해제");
	
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='잠금 해제']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_UNLOCK);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_locked_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/menu_locked_layout")).isEmpty()) {
			failMsg = "Don't unlock meeting";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void checkInvite() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		commA.scrollToAnElementByText(element, "초대");
	
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='초대']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_accessCode")).getText().length() != 7) {
			failMsg = "AccessCode length is not 6 [Code]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_accessCode")).getText();
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_channel_message")).getText().contentEquals("상대방에게 회의 링크를 공유하여 초대하세요.")) {
			failMsg = failMsg + "Wrong Text [Expected] 상대방에게 회의 링크를 공유하여 초대하세요. [Actual]" +  androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_channel_message")).getText(); 
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}	
	
	@Test(priority = 20, enabled = true)
	public void clickGroup() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_group_join_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/conference_invite_fragment")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/title_header_title_string")).getText().contentEquals("그룹 주소록")) {
			failMsg = "Wrong Header [Expected] 그룹 주소록 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/title_header_title_string")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 21, enabled = true)
	public void checkDateDESC() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text")).isEmpty()) {
			
			List <AndroidElement> index = androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text"));
			
			int a[] = new int[index.size()];
			
			if(index.size() > 1) {
				
				for(int i = 0; i < index.size(); i++) {
					
					a[i] = Integer.parseInt(index.get(i).getText().replace(".", "")); 
				}
				
				for(int j =0; j < index.size()-1; j++) {
					if(a[j] < a[j+1]) {						
						failMsg = "Date is not desc";
					}
				}				
			} else {
				throw new SkipException("This test is skip");
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 22, enabled = true)
	public void checkRecentlylist() throws Exception {
		String failMsg = "";
		
		if(androidDriver.findElement(By.xpath("//androidx.appcompat.app.ActionBar.e[@content-desc='전체']")).isSelected()) {
			androidDriver.findElement(By.xpath("//androidx.appcompat.app.ActionBar.e[@content-desc='최근 목록']")).click();
			
			WebDriverWait wait = new WebDriverWait(androidDriver, 20);
	        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//androidx.viewpager.widget.ViewPager//android.widget.TextView"), "최근목록이 없습니다"));
			
			if(!androidDriver.findElement(By.xpath("//androidx.viewpager.widget.ViewPager//android.widget.TextView")).getText().contentEquals("최근목록이 없습니다")) {
				failMsg = "Wrong text [Expected] 최근목록이 없습니다 [Actual]" + androidDriver.findElement(By.xpath("//androidx.viewpager.widget.ViewPager//android.widget.TextView")).getText();
			}
		} else {
			
			throw new SkipException("This test is skip");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, enabled = true)
	public void checkAddressandDepartment() throws Exception {
		String failMsg = "";
		
		if(androidDriver.findElement(By.xpath("//androidx.appcompat.app.ActionBar.e[@content-desc='최근 목록']")).isSelected()) {
			androidDriver.findElement(By.xpath("//androidx.appcompat.app.ActionBar.e[@content-desc='전체']")).click();
			
			WebDriverWait wait = new WebDriverWait(androidDriver, 20);
	        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text"), "QA"));
			
	        if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text")).getText().contentEquals("QA")) {
	        	failMsg = "Wrong department [Expected] QA [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text")).getText();
	        }
	        
	        if(androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/invite_list_contacts_item")).size() != 2) {
	        	failMsg = failMsg + "Wrong Address size";
	        }
	        
		} else {
			WebDriverWait wait = new WebDriverWait(androidDriver, 20);
	        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text"), "QA"));
			
	        if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text")).getText().contentEquals("QA")) {
	        	failMsg = "Wrong department [Expected] QA [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text")).getText();
	        }
	        
	        if(androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/invite_list_contacts_item")).size() != 2) {
	        	failMsg = failMsg + "Wrong department [Expected] QA [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/invite_list_index_text")).getText();
	        }
		}
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/invite_select_count")).isEmpty()) {
			failMsg = failMsg + "Confirm Btn is wrong";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/title_header_arrow")).click();
		Thread.sleep(1000);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 24, enabled = true)
	public void closeInvitelayout() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")).isEmpty()) {
			failMsg = "Invite Popup is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 25, enabled = true)
	public void checkMenuenabled() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
	      
		// android 초대 팝업 떠있으면 닫기
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_INVITE_LAYOUT)));
			androidDriver.findElement(By.id(CommonAndroid.ID_LOUNGE_ROOM_INVITECLOSE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOUNGE_ROOM_PIP_SCREEN)));
		} catch (Exception e) {
			System.out.println("wait error : " + e);
		}

		// android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE)));
		}
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "사회자 모드");
		
		if(androidDriver.findElement(By.xpath("//android.widget.TextView[@text='사회자 모드']/../android.widget.ImageView")).getAttribute("enabled").contentEquals("false")) {
			failMsg = failMsg + "\n1. 사회자 모드 : enabled false";
		}
		
		commA.scrollToAnElementByText(element, "참여자 리스트");
		
		if(androidDriver.findElement(By.xpath("//android.widget.TextView[@text='참여자 리스트']/..")).getAttribute("enabled").contentEquals("false")) {
			failMsg = failMsg + "\n2. 참여자 리스트 : enabled false.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 30, enabled = false)
	public void a30() throws Exception {
		
	}
	
	@Test(priority = 31, enabled = true)
	public void clickCamerachange() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		
		// android 사이드 메뉴 보이지 않으면 클릭
		if (!commA.checkDisplay(androidDriver, By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE))) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ROOM_SIDEMENU_PINCODE)));
		}
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));
		
		commA.scrollToAnElementByText(element, "카메라 전환");
	
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='카메라 전환']")).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(CommonAndroid.XPATH_ANDROID_TOAST))));

		String toastMessage = androidDriver.findElement((By.xpath(CommonAndroid.XPATH_ANDROID_TOAST))).getText();
		
		System.out.println(toastMessage);
		
		if(!toastMessage.contentEquals(TOAST_CAMCHANGE)) {
			failMsg = "Wrong ToastMsg [Expected]" + TOAST_CAMCHANGE + " [Actual]" + toastMessage;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 32, enabled = true)
	public void clickCamOff_Alone() throws Exception {
		String failMsg = "";
		
		Thread.sleep(3000);

		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "카메라");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='카메라']")).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(CommonAndroid.XPATH_ANDROID_TOAST))));

		String toastMessage = androidDriver.findElement((By.xpath(CommonAndroid.XPATH_ANDROID_TOAST))).getText();
		
		System.out.println(toastMessage);
		
		if(!toastMessage.contentEquals(TOAST_CAMOFF)) {
			failMsg = "Wrong ToastMsg [Expected]" + TOAST_CAMOFF + " [Actual]" + toastMessage;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 33, enabled = true)
	public void clickCamOn_Alone() throws Exception {
		
		Thread.sleep(3000);
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='카메라']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_CAMON);
		
	}
	
	@Test(priority = 34, enabled = true)
	public void clickMicOff_Alone() throws Exception {
		
		Thread.sleep(3000);
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='마이크']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_MICOFF);
		
	}
	
	@Test(priority = 35, enabled = true)
	public void clickMicOn_Alone() throws Exception {
		
		Thread.sleep(3000);

		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='마이크']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_MICON);
		
	}
	
	@Test(priority = 40, enabled = true)
	public void clickLog_Alone() throws Exception {
		String failMsg = "";
	
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='회의록']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/title_header_title_string"), "회의록"));
		
		androidDriver.findElement(By.xpath("//androidx.appcompat.app.ActionBar.e[@content-desc='수동기록']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/minutes_notify_message"), "보기 기능만 제공합니다."));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/minutes_notify_message")).getText().contentEquals("보기 기능만 제공합니다.")) {
			failMsg = "Wrong Text [Expected] 보기 기능만 제공합니다. [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/minutes_notify_message")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/title_header_arrow")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 41, enabled = true)
	public void clickRecord_Alone() throws Exception {
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='녹화']")).click();
		
		commA.CheckToastMsg(androidDriver, TOAST_RECORD);
		
	}
	
	@Test(priority = 42, enabled = true)
	public void closeMenu() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEEXIT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/menu_list")).isEmpty()) {
			failMsg = "Menu is not closed";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 43, enabled = true)
	public void checkXBtn() throws Exception {
		String failMsg = "";
	
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "참여자 리스트");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='참여자 리스트']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/participants_list_search_edit_text")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/participants_list_search_edit_text")).sendKeys("T");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/participants_list_clean_search_text")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/participants_list_clean_search_text")).isDisplayed()) {
			failMsg = "Don't display X Btn";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 44, enabled = true)
	public void clickXBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/participants_list_clean_search_text")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/participants_list_search_edit_text"), "이름 검색 필터"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/participants_list_search_edit_text")).getText().contentEquals("이름 검색 필터")) {
			failMsg = "Don't delete text [Expected] 이름 검색 필터 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/participants_list_search_edit_text")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_left_image_button")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 45, enabled = true)
	public void clickExit_Alone() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/home_ex_activity")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_HOMEACTIVITY)) {
			failMsg = "Don't leave room [Expected]" + CommonAndroid.MEETING_HOMEACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		androidDriver.quit();	
	}

}

