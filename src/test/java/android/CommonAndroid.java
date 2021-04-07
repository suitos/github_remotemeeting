package android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.clipboard.ClipboardContentType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import mandatory.CommonValues;

public class CommonAndroid {
	
	public static String APPIUM_SERVER_URL = "http://0.0.0.0:4723/wd/hub";
	
	// android
	public static String MEETING_PACKAGENAME = "com.rsupport.remotemeeting.application";
	public static String MEETING_STARTACTIVITY = ".ui.intro.IntroScreen";
	public static String MEETING_LOGINACTIVITY = ".ui.login.LoginActivity";
	public static String MEETING_ROOMACTIVITY = ".ui.ConferenceActivity";
	public static String MEETING_HOMEACTIVITY = ".ui.home.HomeActivity";

	// id, xpath
	public static String ID_TERMS_OK_BTN = "com.rsupport.remotemeeting.application:id/application_terms_ok";
	public static String ID_ACCESSCODE_INPUT = "com.rsupport.remotemeeting.application:id/access_code_text";
	public static String ID_MAIN_LOGO = "com.rsupport.remotemeeting.application:id/hidden_key_button";
    public static String ID_MAIN_LOGIN_EMAIL_BTN = "com.rsupport.remotemeeting.application:id/login_main_email_btn";
    public static String ID_MAIN_LOGIN_EMAIL_TEXT = "com.rsupport.remotemeeting.application:id/login_main_email_text";
    public static String ID_MAIN_LOGIN_GOOGLE_BTN = "com.rsupport.remotemeeting.application:id/login_main_google_btn";
    public static String ID_MAIN_LOGIN_GOOGLE_TEXT = "com.rsupport.remotemeeting.application:id/login_main_google_text";
    public static String ID_LOGIN_EMAIL = "com.rsupport.remotemeeting.application:id/login_email_id_et";
    public static String ID_LOGIN_PW = "com.rsupport.remotemeeting.application:id/login_email_password_et";
    public static String ID_LOGIN_SUBMIT = "com.rsupport.remotemeeting.application:id/login_email_submit_btn";
    public static String ID_LOGIN_FINDPW = "com.rsupport.remotemeeting.application:id/login_email_find_pwd_btn";
    
    
    //로그인유저 라운지
    public static String ID_LOUNGE_ROOM_TAB = "com.rsupport.remotemeeting.application:id/bottom_menu_room";
    public static String ID_LOUNGE_ROOM_CARD = "com.rsupport.remotemeeting.application:id/box_card_base";
	public static String ID_LOUNGE_ROOM_INFOBOX = "com.rsupport.remotemeeting.application:id/box_channel_info_layout";
	public static String ID_LOUNGE_CREATEROOM_POPUP_INPUT = "com.rsupport.remotemeeting.application:id/channel_info_create_title_edittext";
	public static String ID_LOUNGE_CREATEROOM_POPUP_BTN = "com.rsupport.remotemeeting.application:id/custom_text";
	public static String ID_LOUNGE_ROOM_INVITE_LAYOUT = "com.rsupport.remotemeeting.application:id/channel_info_invite_layout";
	public static String ID_LOUNGE_ROOM_INVITECLOSE_BTN = "com.rsupport.remotemeeting.application:id/channel_info_close";
	public static String ID_LOUNGE_ROOM_PIP_SCREEN = "com.rsupport.remotemeeting.application:id/pip_screen";
	public static String ID_ROOM_SIDEMENU_PINCODE = "com.rsupport.remotemeeting.application:id/pin_code";
	public static String ID_ROOM_ATTENDEELIST_NICKNAME = "com.rsupport.remotemeeting.application:id/participants_list_user_nickname";
	public static String ID_ROOM_ATTENDEELIST_LEAVE_ARROW = "com.rsupport.remotemeeting.application:id/header_left_image_button";
	public static String ID_ROOM_ATTENDEELIST_VOICEREQ_VIEW = "com.rsupport.remotemeeting.application:id/voice_request_list_view";
	public static String ID_ROOM_ATTENDEELIST_VOICEREQ_USERNAME = "com.rsupport.remotemeeting.application:id/request_user_name";
	public static String ID_ROOM_PIP_VOICEREQICON = "com.rsupport.remotemeeting.application:id/conference_pip_status_voice_request";
	
	
    public static String ID_POPUP_BUTTON1 = "com.rsupport.remotemeeting.application:id/popup_button1";
    public static String ID_POPUP_BUTTON2 = "com.rsupport.remotemeeting.application:id/popup_button2";
    
    public static String ID_PIP_VOICEADMIN_CROWN = "com.rsupport.remotemeeting.application:id/conference_pip_status_voice_admin";
	
    public static String ID_ROOM_SIDEMENU_BTN = "com.rsupport.remotemeeting.application:id/conference_menu";
    public static String ID_ROOM_SIDEEXIT_BTN = "com.rsupport.remotemeeting.application:id/menu_exit";
    
    public static String ID_ROOM_ACCESSCODE = "com.rsupport.remotemeeting.application:id/channel_info_invite_accessCode";
    
	public static String XPATH_ANDROID_TOAST = "//android.widget.Toast";
	
	public static ArrayList<String[]> deviceLists = null;
	
	public AndroidDriver<AndroidElement> setAndroidDriver(int devicenum, Boolean Permission) throws MalformedURLException, InterruptedException {

		ArrayList<String[]> devices = getConnectedDevice();
		
		String cmd = "adb am force-stop " + MEETING_PACKAGENAME;
		
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		}
		catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("deviceName", devices.get(devicenum)[0]);
		cap.setCapability("platformVersion", devices.get(devicenum)[1]); // 환경
		cap.setCapability("udid", devices.get(devicenum)[0]);
		cap.setCapability("automationName", "Appium");
		cap.setCapability("appPackage", MEETING_PACKAGENAME); 
		cap.setCapability("appActivity", MEETING_STARTACTIVITY); 
		cap.setCapability("newCommandTimeout", 10000); 
		//cap.setCapability("noReset", true); // 테스트전에 리셋할건지 여부(true/false)
		
		if (Permission == true) {
			cap.setCapability("autoGrantPermissions", true); // noReset이 true이면 동작하지 않음
		} else {
			cap.setCapability("autoGrantPermissions", false);
		}
		
		URL remoteUrl = new URL(APPIUM_SERVER_URL); // WebDriver Hub ip/port (앱피움에서 확인)
		
		AndroidDriver<AndroidElement> androidDriver  = new AndroidDriver<AndroidElement>(remoteUrl, cap);
		
		androidDriver.unlockDevice();
		
		return androidDriver;
	}
	
	public ArrayList<String[]> getConnectedDevice() {
		
		deviceLists = new ArrayList<String[]>();
		
		String command = "adb devices";
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				if(!s.contentEquals("List of devices attached") && !s.trim().isEmpty()) {
					String deviceName = s.split("\t")[0];
					
					String vercomm = "adb -s %s shell getprop ro.build.version.release;getprop ro.product.vendor.brand;getprop ro.product.model";
					Process p2 = Runtime.getRuntime().exec(String.format(vercomm, deviceName));
					p2.waitFor();
					
					BufferedReader versionInput = new BufferedReader(new InputStreamReader(p2.getInputStream()));
					
					String value ="";
					String[] info = new String[4];
					info[0] = deviceName;
					if((value = versionInput.readLine()) != null) info[1] = value;
					if((value = versionInput.readLine()) != null) info[2] = value;
					if((value = versionInput.readLine()) != null) info[3] = value;
					
					deviceLists.add(info);
					
					System.out.println(String.format("device info : %s, %s, %s, %s", info[0], info[1], info[2], info[3]));
				}
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return deviceLists;
	}
	
	public Boolean checkDisplay(AndroidDriver<AndroidElement> androidDriver, By by) {
		try {
			return androidDriver.findElement(by).isDisplayed();
		} catch (NoSuchElementException ignored) {
			return false;
		} catch (StaleElementReferenceException ignored) {
			return false;
		}
	}
	
	public void setServer(AndroidDriver<AndroidElement> androidDriver) throws InterruptedException {

		if(CommonValues.MEETING_URL.contains("st.")) {
			WebDriverWait wait = new WebDriverWait(androidDriver, 10);
			if(androidDriver.currentActivity().contains(MEETING_STARTACTIVITY)) {
				wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_TERMS_OK_BTN)));
				androidDriver.findElement(By.id(CommonAndroid.ID_TERMS_OK_BTN)).click();
			}
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)));
			} catch (Exception e) {
				if(checkDisplay(androidDriver, By.id(CommonAndroid.ID_ACCESSCODE_INPUT)) == false) {
					androidDriver.activateApp(CommonAndroid.MEETING_PACKAGENAME);
				}
			}
			
			Actions action = new Actions(androidDriver);
			
			//tab logo 10
			for (int i = 0; i < 10; i++) {
				//androidDriver.findElement(By.id(ID_MAIN_LOGO)).click();
				action.click(androidDriver.findElement(By.id(ID_MAIN_LOGO)));
				System.out.println("clicl logo : " + i);
			}
			action.perform();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/text1")));
			androidDriver.findElement(By.id("android:id/text1")).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.CheckedTextView")));
			List<AndroidElement> list = androidDriver.findElements(By.xpath("//android.widget.CheckedTextView"));
			
			for (AndroidElement androidElement : list) {
				if(androidElement.getText().contains("stapi")) {
					androidElement.click();
					break;
				}
			}
			
			androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_ok")).click();
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)));
			} catch (Exception e) {
					androidDriver.activateApp(CommonAndroid.MEETING_PACKAGENAME);
				
			}

			System.out.println("current Activity(Main) : " + androidDriver.currentActivity());
		} 
	}
	
	/**
	 * Performs swipe inside an element
	 *
	 * @param el  the element to swipe
	 * @param dir the direction of swipe
	 * @version java-client: 7.3.0
	 **/
	public enum scrollDirection {
		DOWN, UP, LEFT, RIGHT
	}
	
	public void swipeElementAndroid(AndroidDriver<AndroidElement> driver, MobileElement scrollview, scrollDirection dir) {
		swipeElementAndroid(driver, scrollview, dir, 0);
	}
	
	public void swipeElementAndroid(AndroidDriver<AndroidElement> driver, MobileElement scrollview, scrollDirection dir, int releaseTime) {
	    // Animation default time:
	    //  - Android: 300 ms
	    //  - iOS: 200 ms
	    // final value depends on your app and could be greater
	    final int ANIMATION_TIME = 200; // ms

	    final int PRESS_TIME = 200; // ms

	    int edgeBorder;
	    PointOption pointOptionStart, pointOptionEnd;

	    // init screen variables
	    Rectangle rect = scrollview.getRect();
	    // sometimes it is needed to configure edgeBorders
	    // you can also improve borders to have vertical/horizontal
	    // or left/right/up/down border variables
	    edgeBorder = 10;
	    
	    switch (dir) {
	        case DOWN: // from up to down
	            pointOptionStart = PointOption.point(rect.x + rect.width / 2,
	                    rect.y + edgeBorder);
	            pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
	                    rect.y + rect.height - edgeBorder);
	            break;
	        case UP: // from down to up
	            pointOptionStart = PointOption.point(rect.x + rect.width / 2,
	                    rect.y + rect.height - edgeBorder);
	            pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
	                    rect.y + edgeBorder);
	            break;
	        case LEFT: // from right to left
	            pointOptionStart = PointOption.point(rect.x + rect.width - edgeBorder,
	                    rect.y + rect.height / 2);
	            pointOptionEnd = PointOption.point(rect.x + edgeBorder,
	                    rect.y + rect.height / 2);
	            break;
	        case RIGHT: // from left to right
	            pointOptionStart = PointOption.point(rect.x + edgeBorder,
	                    rect.y + rect.height / 2);
	            pointOptionEnd = PointOption.point(rect.x + rect.width - edgeBorder,
	                    rect.y + rect.height / 2);
	            break;
	        default:
	            throw new IllegalArgumentException("swipeElementAndroid(): dir: '" + dir + "' NOT supported");
	    }

	    // execute swipe using TouchAction
	    try {
	    	if(releaseTime > 0) {
	    		 new TouchAction(driver)
	                .press(pointOptionStart)
	                // a bit more reliable when we add small wait
	                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
	                .moveTo(pointOptionEnd)
	                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(releaseTime)))
	                .release().perform();
	    	} else {
	    		 new TouchAction(driver)
	                .press(pointOptionStart)
	                // a bit more reliable when we add small wait
	                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
	                .moveTo(pointOptionEnd)
	                .release().perform();
	    	}
	       
	    } catch (Exception e) {
	        System.err.println("swipeElementAndroid(): TouchAction FAILED\n" + e.getMessage());
	        return;
	    }

	    // always allow swipe action to complete
	    try {
	        Thread.sleep(ANIMATION_TIME);
	    } catch (InterruptedException e) {
	        // ignore
	    }
	}
	
	public AndroidElement scrollToAnElementByText(AndroidDriver<AndroidElement> androidDriver, String text) {
		//텍스트 찾을때 까지 스크롤
		return androidDriver.findElement(MobileBy.AndroidUIAutomator(
		        "new UiScrollable(new UiSelector().scrollable(true))" +
		         ".scrollIntoView(new UiSelector().text(\""+text +"\"))"));
		
        //androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();

	}
	
	public AndroidElement scrollToAnElementByText(AndroidElement element, String text) {
		//엘리먼트 내에서 텍스트 찾을때 까지 스크롤
        return (AndroidElement) element.findElement(MobileBy.AndroidUIAutomator(
		        "new UiScrollable(new UiSelector().scrollable(true))" +
		         ".scrollIntoView(new UiSelector().text(\""+text +"\"))"));
        
	}

	public String findAppVersion(int devicenum) throws Exception {

		String cmd = "adb -s" + deviceLists.get(devicenum)[0] + " shell dumpsys package " + CommonAndroid.MEETING_PACKAGENAME + " |grep versionName";
		try {
			Process p = Runtime.getRuntime().exec(cmd);;
			String appVersion = new BufferedReader(new InputStreamReader(p.getInputStream())).readLine().trim();
			String subappVersion = appVersion.substring(appVersion.lastIndexOf("=")+1);
			
			System.out.println("appVersion = " + subappVersion);

			return subappVersion;

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * AndroidDriver.isKeyboardShown() 쓰세요.
	 **/
	public boolean isSoftKeyboadShown(String deviceName) throws Exception {

		String cmd = String.format("adb -s %s shell dumpsys input_method | grep mInputShown", deviceName);
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			String value = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine().trim();
			String[] values = value.split(" ");
			for (String string : values) {
				if(string.contains("mInputShown=")) {
					value = string;
					break;
				}
			}
			
			String isShown = value.replace("mInputShown=", "");
			
			System.out.println("keyboard : " + value);
			if(isShown.equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	public void CheckToastMsg(AndroidDriver<AndroidElement> androidDriver, String Toastmsg) throws Exception {
		
		WebDriverWait waitForToast = new WebDriverWait(androidDriver,15);
		waitForToast.until(ExpectedConditions.presenceOfElementLocated((By.xpath(XPATH_ANDROID_TOAST))));

		String toastMessage = androidDriver.findElement((By.xpath(XPATH_ANDROID_TOAST))).getText();
		
		System.out.println(toastMessage);
		
		if(!toastMessage.contentEquals(Toastmsg)) {
			Exception e = new Exception("Wrong ToastMsg [Expected]" + Toastmsg + " [Actual]" + toastMessage);
			throw e;
		}
		
		//waitForToast.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated((By.xpath(XPATH_ANDROID_TOAST)))));

		waitForToast.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ANDROID_TOAST)));
		waitForToast.until(ExpectedConditions.invisibilityOf(androidDriver.findElement(By.xpath(XPATH_ANDROID_TOAST))));

	}
	
	public String getClipboard(AndroidDriver<AndroidElement> androidDriver) {
		
		androidDriver.getClipboard(ClipboardContentType.PLAINTEXT); // get plaintext
		String clipboard = androidDriver.getClipboardText();
		
		return clipboard;
	}
	
	public String getInfo(AndroidDriver<AndroidElement> androidDriver, int i) {

		String clipboard = getClipboard(androidDriver);

		String[] lines = clipboard.split("\n");
		
		if (i == 2) {
			return lines[2];
		} else if (i == 4) {
			return lines[4].substring(lines[4].lastIndexOf(":")+1).trim();
		} else {
			return null;
		}

	}
	
	public String getSession(AndroidDriver<AndroidElement> androidDriver, String key) {
		
		Map<String, Object> caps = androidDriver.getSessionDetails();
		
		return (String) caps.get(key);
		
	}
	
	public void emailLogin(AndroidDriver<AndroidElement> androidDriver, String userid, String userpw) {
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		CommonAndroid commA = new CommonAndroid();
		if(androidDriver.currentActivity().contains(CommonAndroid.MEETING_LOGINACTIVITY) 
				&& commA.checkDisplay(androidDriver, By.id(MobileLogin.ID_FINDPW_MESSAGE))) {
			androidDriver.findElement(By.id(MobileLogin.ID_LOGIN_BACK_BTN)).click();
		} else if (commA.checkDisplay(androidDriver, By.id(ID_ACCESSCODE_INPUT))) {
			androidDriver.findElement(By.id(ID_MAIN_LOGIN_EMAIL_BTN)).click();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_LOGIN_EMAIL)));
		
		androidDriver.findElement(By.id(ID_LOGIN_EMAIL)).clear();
		androidDriver.findElement(By.id(ID_LOGIN_EMAIL)).sendKeys(userid);
		androidDriver.findElement(By.id(ID_LOGIN_PW)).clear();
		androidDriver.findElement(By.id(ID_LOGIN_PW)).sendKeys(userpw);

		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_SUBMIT)).click();;
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(MobileLogin.ID_ROOM_HEADER)));
		
		System.out.println("current activity" + androidDriver.currentActivity());
	}
	
	public String getAccessCode(AndroidDriver<AndroidElement> androidDriver) {
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		if(androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")).isEmpty()) {
			androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
			
			androidDriver.findElement(By.xpath("//android.widget.TextView[@text='초대']")).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
			
		} else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		}
		
		String code = androidDriver.findElement(By.id(ID_ROOM_ACCESSCODE)).getText();
		
		return code;
	}
}
