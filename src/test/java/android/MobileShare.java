package android;

import static org.testng.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import mandatory.CommonValues;
import mandatory.P2PFree_Share;

/* MobileShare - galaxy s9(10) 전용
 * 1. pc 회의생성 - 모바일 참석
 * 2. 모바일 문서공유 - 업로드된 문서 목록 비어있음 확인
 * 3. 모바일 문서공유 - 파일에서 불러오기 - 선택
 * 4. 모바일 문서공유 - 촬영하기 선택 - 카메라에서 촬영후 공유
 * 5. 모바일 문서공유 - 앨범에서 불러오기 - 앨범에서 선택후 공유
 * 6. 모바일 문서공유 - 팝업에서 취소
 * 7. 모바일 문서공유 - 업로드된 문서 목록 비어있지 않음 확인 후 취소
 * 8. 모바일 문서공유 - 업로드된 문서 목록 비어있지 않음 확인후 리스트 선택 공유
 * 
 * 11. 모바일 PIP 숨김 클릭
 * 12. 모바일 PIP 보이기 클릭
 * 14. 모바일 PIP 순서확인 및 이름체크
 * 15. 모바일 PIP 내화면 선택
 * 16. 모바일 PIP 상대방 선택
 * 
 * 21. 웹참석자가 문서공유 - 모바일 확인(토스트, 툴바)
 * 22. 문서공유중 pip 숨기기
 * 23. 문서공유중 pip 보이기
 * 30. 웹참석자가 문서공유 종료
 *  
 * 41. 모바일 회의록 공유 이메일 빈채로 공유 시도 - 토스트 확인
 * 42. 모바일 회의록 공유 잘못된 이메일 포맷으로 공유 시도 - 토스트 확인
 * 43. 모바일 회의록 공유 정상 이메일로 공유 - 토스트 확인
 */

public class MobileShare {
	
	
	
	public static String ID_ROOM_DOCSHARE_UPLOADLIST_BTN = "com.rsupport.remotemeeting.application:id/document_share_recent_list";
	public static String ID_ROOM_DOCSHARE_FILE_BTN = "com.rsupport.remotemeeting.application:id/document_share_file_list";
	public static String ID_ROOM_DOCSHARE_CAMERA_BTN = "com.rsupport.remotemeeting.application:id/document_share_take_picture";
	public static String ID_ROOM_DOCSHARE_ALBUM_BTN = "com.rsupport.remotemeeting.application:id/document_share_pick_gallery";
	public static String ID_ROOM_DOCSHARE_LISTEMPTY = "com.rsupport.remotemeeting.application:id/document_share_file_list_no_list";
	public static String ID_ROOM_DOCSHARE_LISTCANCEL_BTN = "com.rsupport.remotemeeting.application:id/document_share_recent_list_close";
	public static String ID_ROOM_DOCSHARE_SELECTCLOSE = "com.rsupport.remotemeeting.application:id/document_share_select_view_close";
	
	public static String ID_ROOM_PIP_BTN = "com.rsupport.remotemeeting.application:id/conference_pip_button";
	public static String ID_ROOM_NORMALPIP_LAYOUT = "com.rsupport.remotemeeting.application:id/normal_pip_layout";
	public static String ID_ROOM_PIP_SCREEN = "com.rsupport.remotemeeting.application:id/active_speaker_layout";
	public static String ID_ROOM_PIP_USERNAME = "com.rsupport.remotemeeting.application:id/conference_pip_user_name";
	
	public static String ID_ROOM_DRAWCUSOR_BTN = "com.rsupport.remotemeeting.application:id/drawing_cursor";
	public static String ID_ROOM_DOCMENU_CLOSE_BTN = "com.rsupport.remotemeeting.application:id/document_share_finish";
	
	public static String ID_ROOM_NOTE_SHARE_ICON = "com.rsupport.remotemeeting.application:id/minutes_share";
	public static String ID_ROOM_NOTE_SHARE_BTN = "com.rsupport.remotemeeting.application:id/minutes_share_send_button_layout";
	public static String ID_ROOM_NOTE_SHARE_EMAIL_INPUTBOX = "com.rsupport.remotemeeting.application:id/minutes_share_email_text";
	
	public static String XPATH_ROOM_SIDEMENU_DOC_BTN = "//android.widget.RelativeLayout[5]";
	public static String XPATH_ROOM_SIDEMENU_NOTE_BTN = "//android.widget.RelativeLayout[6]";
	
	public static String XPATH_DOC_GALLERY_BTN = "//android.widget.HorizontalScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]";
	
	public static String XPATH_GALLERY_IMAGELIST = "//androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout";
	public static String XPATH_GALLERY_IMAGELIST_LG = "//android.support.v7.widget.RecyclerView/android.widget.LinearLayout";
	
	public static String XPATH_ROOM_NOTE_TAB1 = "//androidx.appcompat.app.ActionBar.e[1]";
	public static String XPATH_ROOM_NOTE_TAB2 = "//androidx.appcompat.app.ActionBar.e[2]";

	public static String androidAttendee = "android";
	
	public static WebDriver chromeDriver = null;
	public static AndroidDriver<AndroidElement> androidDriver = null;

	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		
		CommonAndroid commA = new CommonAndroid();
		androidDriver = commA.setAndroidDriver(0,true);
		
		CommonValues comm = new CommonValues();
		comm.setDriverProperty("Chrome");

		chromeDriver = comm.setDriver(chromeDriver, "Chrome", "lang=ko_KR", true);
		context.setAttribute("webDriver", chromeDriver);
		context.setAttribute("webDriver2", androidDriver);
		
		commA.setServer(androidDriver);
	}
	
	@AfterMethod
	public void setBrowser() {
		/*
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			
			System.out.println("current Activity : " + androidDriver.currentActivity());
			Activity activity = new Activity(CommonAndroid.MEETING_PACKAGENAME, CommonAndroid.MEETING_ROOMACTIVITY);
			androidDriver.startActivity(activity);
		}
		*/	
	}
	
	@Test(priority = 1)
	public void attendMeeting() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)));
		
		chromeDriver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		CommonValues comm = new CommonValues();
		comm.login(chromeDriver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		comm.createNormalMeeting(chromeDriver, "test");
		String code = comm.findCode(chromeDriver).replace(" ", "");
		
		System.out.println("code : " + code);
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).click();
		androidDriver.findElement(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)).sendKeys(code);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_join_btn")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")));
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")).clear();
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/anonymous_nickname_edittext")).sendKeys(androidAttendee);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/nickname_ok_button")).click();
		
		Thread.sleep(1000);

	}
	
	@Test(priority = 2, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocListEmpty() throws Exception {
		String failMsg = "";
		
		System.out.println("current Activity(Room) : " + androidDriver.currentActivity());
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();


		// 목록에서 공유하기 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_UPLOADLIST_BTN)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_UPLOADLIST_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_LISTEMPTY)));

		if(!androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_LISTEMPTY)).findElement(By.xpath(".//android.widget.TextView")).getText().contentEquals("목록이 없습니다.")) {
			failMsg = "\n1. sharedoc list msg [Expected]목록이 없습니다. [Actual]" 
					+ androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_LISTEMPTY)).findElement(By.xpath(".//android.widget.TextView")).getText();
		}
		
		//click cancel
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_LISTCANCEL_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocFile() throws Exception {
		String failMsg = "";
		
		System.out.println("current Activity(Room) : " + androidDriver.currentActivity());
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 300);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();


		// 앨범에서 공유하기 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_FILE_BTN)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_FILE_BTN)).click();
		
		Thread.sleep(2000);
		System.out.println("current Activity(File) : " + androidDriver.currentActivity());
		
		if(androidDriver.currentActivity().contains(".picker.PickActivity")) {
			String listxpath = "";
			String scrollview = "//android.view.ViewGroup/android.support.v7.widget.RecyclerView";
			if(CommonAndroid.deviceLists.get(0)[2].contentEquals("lge")) {
				scrollview = "//android.view.ViewGroup/android.support.v7.widget.RecyclerView";
				listxpath = XPATH_GALLERY_IMAGELIST_LG;
			} else {
				listxpath = XPATH_GALLERY_IMAGELIST;
			}
		
			List<AndroidElement> imglist = null;
			boolean findFile = false;
			
			MobileElement element = androidDriver.findElement(By.xpath(scrollview));
			CommonAndroid commA = new CommonAndroid();

			while (!findFile) {
				imglist = androidDriver.findElements(By.xpath(listxpath));
				for (AndroidElement androidElement : imglist) {
					if(androidElement.isEnabled()) {
						androidElement.click();
						findFile = true;
						break;
					}					
				}
				if(!findFile) {
					commA.swipeElementAndroid(androidDriver, element, CommonAndroid.scrollDirection.UP, 200);
					Thread.sleep(500);
				}
			}

		} else {
			failMsg = "\n1. not file list app : " + androidDriver.currentActivity();
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		Thread.sleep(500);
		androidDriver.findElement(By.id(ID_ROOM_DOCMENU_CLOSE_BTN)).click();
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocCamera() throws Exception {
		String failMsg = "";
		
		System.out.println("current Activity(Room) : " + androidDriver.currentActivity());
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();


		// 앨범에서 공유하기 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_CAMERA_BTN)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_CAMERA_BTN)).click();
		
		Thread.sleep(2000);
		System.out.println("current Activity(Camera) : " + androidDriver.currentActivity());

		if(androidDriver.currentActivity().contains(".Camera")) {
			
			if(CommonAndroid.deviceLists.get(0)[2].contentEquals("lge")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.lge.camera:id/shutter_large_comp")));
				androidDriver.findElement(By.id("com.lge.camera:id/shutter_large_comp")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.lge.camera:id/done_button")));
				androidDriver.findElement(By.id("com.lge.camera:id/done_button")).click();
			} else {
				List<AndroidElement> buttons = androidDriver.findElements(By.xpath("//GLButton[@content-desc='NONE']"));
				if(buttons.size() ==2) {
					buttons.get(1).click();
					
					Thread.sleep(2000);
					androidDriver.findElement(By.xpath("//GLButton[@content-desc='NONE']")).click();
				}
			}
			
		} else {
			failMsg = "\n1. not camera app : " + androidDriver.currentActivity();
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		Thread.sleep(500);
		androidDriver.findElement(By.id(ID_ROOM_DOCMENU_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocAlbum() throws Exception {
		String failMsg = "";
		
		System.out.println("current Activity(Room) : " + androidDriver.currentActivity());
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 30);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();


		// 앨범에서 공유하기 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_ALBUM_BTN)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_ALBUM_BTN)).click();

		Thread.sleep(2000);
		if (androidDriver.currentActivity().contains(".picker.PickActivity")) {
			String listxpath = "";
			String scrollview = "//android.view.ViewGroup/android.support.v7.widget.RecyclerView";
			if(CommonAndroid.deviceLists.get(0)[2].contentEquals("lge")) {
				scrollview = "//android.view.ViewGroup/android.support.v7.widget.RecyclerView";
				listxpath = XPATH_GALLERY_IMAGELIST_LG;
			} else {
				listxpath = XPATH_GALLERY_IMAGELIST;
			}
		
			List<AndroidElement> imglist = null;
			boolean findFile = false;
			
			MobileElement element = androidDriver.findElement(By.xpath(scrollview));
			CommonAndroid commA = new CommonAndroid();

			while (!findFile) {
				imglist = androidDriver.findElements(By.xpath(listxpath));
				for (AndroidElement androidElement : imglist) {
					if(androidElement.isEnabled()) {
						androidElement.click();
						findFile = true;
						break;
					}					
				}
				if(!findFile) {
					commA.swipeElementAndroid(androidDriver, element, CommonAndroid.scrollDirection.UP, 200);
					Thread.sleep(500);
				}
			}

		} else {
			Thread.sleep(5000);
			System.out.println("current Activity(album) : " + androidDriver.currentActivity());

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DOC_GALLERY_BTN)));
			androidDriver.findElement(By.xpath(XPATH_DOC_GALLERY_BTN)).click();

			Thread.sleep(1000);
			System.out.println("current Activity(gallery) : " + androidDriver.currentActivity());
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		Thread.sleep(500);
		androidDriver.findElement(By.id(ID_ROOM_DOCMENU_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocCancel() throws Exception {
		String failMsg = "";
		
		System.out.println("current Activity(Room) : " + androidDriver.currentActivity());
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 30);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		
		//cancel
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_SELECTCLOSE)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_SELECTCLOSE)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 7, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocListCancel() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();


		// 앨범에서 공유하기 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_UPLOADLIST_BTN)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_UPLOADLIST_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_LISTCANCEL_BTN)));
		
		//list 확인
		String xpath_list = "//android.widget.LinearLayout/android.widget.LinearLayout";
		List<AndroidElement> list = androidDriver.findElements(By.xpath(xpath_list));
		if(list.size() > 1) {
			androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_LISTCANCEL_BTN)).click();
		} else {
			failMsg = failMsg + "\n1. cannot find doc list. doc list size : " + list.size();
			//click cancel
			androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_LISTCANCEL_BTN)).click();
		}
		
		Thread.sleep(5000);
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 8, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void shareDocList() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		// side menu btn
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();


		// 앨범에서 공유하기 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout[9]")));
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_DOC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_UPLOADLIST_BTN)));
		androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_UPLOADLIST_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DOCSHARE_LISTCANCEL_BTN)));
		
		//list 확인
		String xpath_list = "//android.widget.LinearLayout/android.widget.LinearLayout";
		List<AndroidElement> list = androidDriver.findElements(By.xpath(xpath_list));
		if(list.size() > 1) {
			list.get(0).click();
		} else {
			failMsg = failMsg + "\n1. cannot find doc list. doc list size : " + list.size();
			//click cancel
			androidDriver.findElement(By.id(ID_ROOM_DOCSHARE_LISTCANCEL_BTN)).click();
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		Thread.sleep(500);
		androidDriver.findElement(By.id(ID_ROOM_DOCMENU_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void hidePIP() throws Exception {
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));
		
		//click pip button
		androidDriver.findElement(By.id(ID_ROOM_PIP_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

	}
	
	@Test(priority = 12, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void showPIP() throws Exception {
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));
		
		//click pip button
		androidDriver.findElement(By.id(ID_ROOM_PIP_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

	}
	
	@Test(priority = 13, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void attendeesName() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

		AndroidElement piplayout = androidDriver.findElement(By.id(ID_ROOM_PIP_SCREEN));
		List<MobileElement> list = piplayout.findElements(By.id(ID_ROOM_PIP_USERNAME));
		
		if(list.size() != 2) {
			failMsg = failMsg + "\n1. user pip size error. current size : " + list.size();
		} else {
			List<String> users = new  ArrayList<String>(); 
			for (MobileElement mobileElement : list) {
				users.add(mobileElement.getText().replace("(나)", "").trim());
			}
			//순서 확인 위해 하나씩 확인
			if(!users.get(0).contentEquals(CommonValues.ADMINNICKNAME)) {
				failMsg = failMsg + "\n2. cannot find username : " + CommonValues.ADMINNICKNAME;
			}
			
			if(!users.get(1).contentEquals(androidAttendee)) {
				failMsg = failMsg + "\n3. cannot find username : " + androidAttendee;
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 14, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void pipSelect1() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

		AndroidElement pipview = androidDriver.findElement(By.id(ID_ROOM_PIP_SCREEN));
		List<MobileElement> list = pipview.findElements(By.xpath(".//android.widget.LinearLayout"));

		if(list.size() != 2) {
			failMsg = failMsg + "\n1. user pip size error. current size : " + list.size();
		} else {
			//click user
			list.get(1).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/conference_pip_pin_image")));
			
			if(!list.get(1).findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_pin_image")).isDisplayed()) {
				failMsg = failMsg + "\n2. cannot find pin icon  ";
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 15, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void pipSelect2() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

		AndroidElement pipview = androidDriver.findElement(By.id(ID_ROOM_PIP_SCREEN));
		List<MobileElement> list = pipview.findElements(By.xpath(".//android.widget.LinearLayout"));

		if(list.size() != 2) {
			failMsg = failMsg + "\n1. user pip size error. current size : " + list.size();
		} else {
			//click user
			list.get(0).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/conference_pip_pin_image")));
			if(!list.get(0).findElement(By.id("com.rsupport.remotemeeting.application:id/conference_pip_pin_image")).isDisplayed()) {
				failMsg = failMsg + "\n2. cannot find pin icon  ";
			}
			list.get(0).click();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 21, dependsOnMethods = {"attendMeeting"}, enabled = false)
	public void webDocShare() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

		//기본 문서툴 없음 확인
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		
		//웹에서 문서공유
		CommonValues comm = new CommonValues();
		comm.ShareDocument(chromeDriver, CommonValues.filetype.JPG);
			
		//toast 확인
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)));
		String msg = androidDriver.findElement(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)).getText();
		if(!msg.contains("공유했습니다.")) {
			failMsg = failMsg + "\n1. toast message. expected to include text : 공유했습니다. [Actual]" + msg;
		}
		//문서공유 뷰 확인(툴바로 확인)
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 22, dependsOnMethods = {"attendMeeting", "webDocShare"}, enabled = false)
	public void webDocShare_hidePIP() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));
		
		//click pip button
		androidDriver.findElement(By.id(ID_ROOM_PIP_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 23, dependsOnMethods = {"attendMeeting", "webDocShare"}, enabled = false)
	public void webDocShare_showPIP() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));
		
		//click pip button
		androidDriver.findElement(By.id(ID_ROOM_PIP_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_NORMALPIP_LAYOUT)));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 30, dependsOnMethods = {"attendMeeting", "webDocShare"}, enabled = false)
	public void webDocShare_close() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		
		
		//웹이 공유 종료
		chromeDriver.findElement(By.xpath(P2PFree_Share.XPATH_CLOSE_BTN)).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_ROOM_DRAWCUSOR_BTN)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 41, dependsOnMethods = {"attendMeeting"}, enabled = true)
	public void noteshare_empty() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)));
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_ROOM_SIDEMENU_NOTE_BTN)));
		
		androidDriver.findElement(By.xpath(XPATH_ROOM_SIDEMENU_NOTE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_NOTE_TAB2)));
		//수동기록 선택 확인
		if(!androidDriver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB2)).isSelected()) {
			androidDriver.findElement(By.xpath(XPATH_ROOM_NOTE_TAB2)).click();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_ROOM_NOTE_SHARE_ICON)));
		
		//회의록 공유 아이콘 클릭
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_ICON)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_ROOM_NOTE_SHARE_BTN)));
		
		//이메일 빈채로 공유 클릭
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_BTN)).click();
		
		//toast 확인
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)));
		String msg = androidDriver.findElement(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)).getText();
		String expectedmsg = "이메일을 입력하세요";
		if (!msg.contentEquals(expectedmsg)) {
			failMsg = failMsg + "\n1. toast message. [Expected]" + expectedmsg +  " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 42, dependsOnMethods = {"noteshare_empty"}, alwaysRun = true, enabled = true)
	public void noteshare_invalidemail() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_ROOM_NOTE_SHARE_ICON)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)));
		
		//회의록 공유 아이콘 클릭
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_ICON)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_ROOM_NOTE_SHARE_BTN)));
		
		//이메일 포맷 아닌 문자열 입력
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_EMAIL_INPUTBOX)).clear();
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_EMAIL_INPUTBOX)).sendKeys("invalid email");;
		
		//이메일 빈채로 공유 클릭
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_BTN)).click();
		
		//toast 확인
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)));
		String msg = androidDriver.findElement(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)).getText();
		String expectedmsg = "이메일 형식이 맞지 않습니다.";
		if (!msg.contentEquals(expectedmsg)) {
			failMsg = failMsg + "\n1. toast message. [Expected]" + expectedmsg +  " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 43, dependsOnMethods = {"noteshare_empty"}, alwaysRun = true, enabled = true)
	public void noteshare_validemail() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_ROOM_NOTE_SHARE_ICON)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)));
		
		//회의록 공유 아이콘 클릭
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_ICON)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_ROOM_NOTE_SHARE_BTN)));
		
		//정상 이메일 입력
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_EMAIL_INPUTBOX)).clear();
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_EMAIL_INPUTBOX)).sendKeys("sinhyekim@rsupport.com");;
		
		//이메일 빈채로 공유 클릭
		androidDriver.findElement(By.id(ID_ROOM_NOTE_SHARE_BTN)).click();
		
		//toast 확인
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)));
		String msg = androidDriver.findElement(By.xpath(CommonAndroid.XPATH_ANDROID_TOAST)).getText();
		String expectedmsg = "전송하였습니다";
		if (!msg.contentEquals(expectedmsg)) {
			failMsg = failMsg + "\n1. toast message. [Expected]" + expectedmsg +  " [Actual]" + msg;
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ROOM_NOTE_TAB2)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		chromeDriver.quit();
		androidDriver.quit();
	}
}


