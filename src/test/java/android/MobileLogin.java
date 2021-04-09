package android;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import mandatory.CommonValues;

/* MobileLogin
 * 1. 로그인 화면 확인(구성)
 * 2. 이메일 로그인 화면 확인
 * 3. 이메일 로그인 화면 로그인 버튼 비활성화 확인
 */
public class MobileLogin {

	public static String ID_MAIN_SETTING_BTN = "com.rsupport.remotemeeting.application:id/login_main_setting_btn";
	public static String ID_MAIN_CREATE_FREE_CONFERENCE = "com.rsupport.remotemeeting.application:id/login_main_create_btn";
	public static String ID_MAIN_RM_LOGO = "com.rsupport.remotemeeting.application:id/login_main_logo";
	public static String ID_MAIN_AI_BANNER = "com.rsupport.remotemeeting.application:id/live_meeting_banner_string";
	public static String ID_LOGIN_BACK_BTN = "com.rsupport.remotemeeting.application:id/login_email_back_btn";
	public static String ID_LOGIN_ERROR = "com.rsupport.remotemeeting.application:id/login_email_error_tv";
	
	public static String ID_FINDPW_MESSAGE = "com.rsupport.remotemeeting.application:id/find_password_message";
	public static String ID_FINDPW_EMAIL = "com.rsupport.remotemeeting.application:id/find_password_email_id";
	public static String ID_FINDPW_BTN = "com.rsupport.remotemeeting.application:id/find_password_button";
	public static String ID_POPUP_TEXT = "com.rsupport.remotemeeting.application:id/popup_text";
	public static String ID_POPUP_OK = "com.rsupport.remotemeeting.application:id/popup_button2";
	
	public static String ID_ROOM_HEADER = "com.rsupport.remotemeeting.application:id/header_text";

	public static AndroidDriver<AndroidElement> androidDriver = null;
	
	CommonAndroid commA = new CommonAndroid();
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0, true);
		commA.setServer(androidDriver);
		
		context.setAttribute("webDriver", androidDriver);
		
	}
	
	@Test(priority = 1, enabled = true)
	public void loginView() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MAIN_SETTING_BTN)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MAIN_CREATE_FREE_CONFERENCE)));
		if(!androidDriver.findElement(By.id(ID_MAIN_CREATE_FREE_CONFERENCE)).getText().contentEquals("회의 체험하기")) {
			failMsg = failMsg + "\n1. free conference button label [Expected]" + "회의 체험하기" 
					+ " [Actual]" + androidDriver.findElement(By.id(ID_MAIN_CREATE_FREE_CONFERENCE)).getText();
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MAIN_RM_LOGO)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)));
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_MAIN_LOGIN_EMAIL_BTN)));
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_MAIN_LOGIN_EMAIL_TEXT)).getText().contentEquals("이메일 로그인")) {
			failMsg = failMsg + "\n2. login email button label [Expected]" + "이메일 로그인" 
					+ " [Actual]" + androidDriver.findElement(By.id(CommonAndroid.ID_MAIN_LOGIN_EMAIL_TEXT)).getText();
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_MAIN_LOGIN_GOOGLE_BTN)));
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_MAIN_LOGIN_GOOGLE_TEXT)).getText().contentEquals("구글로 로그인")) {
			failMsg = failMsg + "\n3. login google button label [Expected]" + "구글로 로그인" 
					+ " [Actual]" + androidDriver.findElement(By.id(CommonAndroid.ID_MAIN_LOGIN_GOOGLE_TEXT)).getText();
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MAIN_AI_BANNER)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void emailLoginView() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MAIN_SETTING_BTN)));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_MAIN_LOGIN_EMAIL_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_FINDPW)).getText().contentEquals("비밀번호 재설정")) {
			failMsg = failMsg + "\n1. reset pw label [Expected]비밀번호 재설정 [Actual]" 
					+ androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_FINDPW)).getText();
		}
		
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_EMAIL)).getText().contentEquals("이메일을 입력해 주세요.")) {
			failMsg = failMsg + "\n2. email placeholder [Expected]이메일을 입력해 주세요. [Actual]" 
					+ androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_EMAIL)).getText();
		}
		if(!androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).getText().contentEquals("비밀번호를 입력해 주세요.")) {
			failMsg = failMsg + "\n3. pw placeholder [Expected]비밀번호를 입력해 주세요. [Actual]" 
					+ androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).getText();
		}
		
		if(!androidDriver.isKeyboardShown()) {
			failMsg = failMsg + "\n4. keyboard is not shown.";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, dependsOnMethods = {"emailLoginView"}, alwaysRun = true,  enabled = true)
	public void emailLogin_submitButton() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		
		//이메일, pw 모두 빈상태에서 버튼 확인
		if(androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_SUBMIT)).isEnabled()) {
			failMsg = failMsg + "\n1. submit button is enabled(empty value)";
		}
		
		//비밀번호만 입력 후 버튼 확인
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).sendKeys("123456789");
		Thread.sleep(500);
		if(androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_SUBMIT)).isEnabled()) {
			failMsg = failMsg + "\n2. submit button is enabled(only pw)";
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, dependsOnMethods = {"emailLoginView"}, alwaysRun = true,  enabled = true)
	public void emailLogin_invalid() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		
		//이메일, pw 모두 빈상태에서 버튼 확인
		if(androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_SUBMIT)).isEnabled()) {
			failMsg = failMsg + "\n1. submit button is enabled(empty value)";
		}
		
		//비밀번호만 입력 후 버튼 확인
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_EMAIL)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_EMAIL)).sendKeys(CommonValues.USERS[0]);
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).sendKeys("123456789");

		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_SUBMIT)).click();;
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ID_LOGIN_ERROR)));
		String msg = androidDriver.findElement(By.id(ID_LOGIN_ERROR)).getText();
		String expectedmsg = "아이디 또는 비밀번호를 잘못 입력 하셨습니다.\n[Code : 40114]";
		if(!msg.contentEquals(expectedmsg)) {
			failMsg = failMsg + "\n1. login error msg [Expected]" + expectedmsg + " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, dependsOnMethods = {"emailLoginView"}, alwaysRun = true,  enabled = true)
	public void emailLogin_back() throws Exception {

		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_LOGIN_ERROR)));
		
		androidDriver.findElement(By.id(ID_LOGIN_BACK_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_ACCESSCODE_INPUT)));
		
	}
	
	@Test(priority = 11,  enabled = true)
	public void resetPW() throws Exception {

		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_MAIN_SETTING_BTN)));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_MAIN_LOGIN_EMAIL_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_FINDPW)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_FINDPW_MESSAGE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 12, dependsOnMethods = {"resetPW"},  enabled = true)
	public void resetPW_invalidEmail() throws Exception {

		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_FINDPW_MESSAGE)));
		androidDriver.findElement(By.id(ID_FINDPW_EMAIL)).clear();
		androidDriver.findElement(By.id(ID_FINDPW_EMAIL)).sendKeys("test9999@rsupport.com");
		
		androidDriver.findElement(By.id(ID_FINDPW_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_POPUP_TEXT)));
		String expectedmsg = "가입되어 있지 않은 이메일입니다.\n다시 확인해주세요.";
		if(!androidDriver.findElement(By.id(ID_POPUP_TEXT)).getText().contentEquals(expectedmsg)) {
			failMsg = failMsg + "\n1. popup msg [Expected]" + expectedmsg 
					+ " [Actual]" + androidDriver.findElement(By.id(ID_POPUP_TEXT)).getText();
		}
		androidDriver.findElement(By.id(ID_POPUP_OK)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_POPUP_TEXT)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 13, dependsOnMethods = {"resetPW"},  enabled = true)
	public void resetPW_validEmail() throws Exception {

		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_FINDPW_MESSAGE)));
		androidDriver.findElement(By.id(ID_FINDPW_EMAIL)).clear();
		androidDriver.findElement(By.id(ID_FINDPW_EMAIL)).sendKeys(CommonValues.USERS[0]);
		
		androidDriver.findElement(By.id(ID_FINDPW_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_POPUP_TEXT)));
		String expectedmsg = "가입하신 이메일로 비밀번호 재설정 메일을 전송 하였습니다.";
		if(!androidDriver.findElement(By.id(ID_POPUP_TEXT)).getText().contentEquals(expectedmsg)) {
			failMsg = failMsg + "\n1. popup msg [Expected]" + expectedmsg 
					+ " [Actual]" + androidDriver.findElement(By.id(ID_POPUP_TEXT)).getText();
		}
		androidDriver.findElement(By.id(ID_POPUP_OK)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(ID_POPUP_TEXT)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 20, enabled = true)
	public void emailLogin_valid() throws Exception {

		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		CommonAndroid commA = new CommonAndroid();
		if(androidDriver.currentActivity().contains(CommonAndroid.MEETING_LOGINACTIVITY) 
				&& commA.checkDisplay(androidDriver, By.id(ID_FINDPW_MESSAGE))) {
			androidDriver.findElement(By.id(ID_LOGIN_BACK_BTN)).click();
		}
		
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_EMAIL)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_EMAIL)).sendKeys(CommonValues.USERS[0]);
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).clear();
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_PW)).sendKeys(CommonValues.USERPW);

		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_LOGIN_SUBMIT)));
		androidDriver.findElement(By.id(CommonAndroid.ID_LOGIN_SUBMIT)).click();;
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_ROOM_HEADER)));
		
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
