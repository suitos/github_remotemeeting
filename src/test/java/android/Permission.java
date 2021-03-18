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

/*
 * 1.상세 안내 팝업 확인 후 앱 재실행 후 재노출 확인
 * 2.다시 보지 않기 선택 후 카메라 팝업 확인 및 재실행 후 미노출 확인
 * 3.권한 거부 후 팝업 확인
 * 4.설정 메뉴 확인
 * 5.앱 버전 확인
 * 6.라이선스 노출 확인
 */

public class Permission {
	
private static String MSG_DENY = "필수 권한이 허용되지 않아 앱을 실행할 수 없습니다.\n" + "앱의 권한에서 모두 허용으로 설정해주세요.";
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	
	CommonAndroid commA = new CommonAndroid();
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		
		androidDriver = commA.setAndroidDriver(0,false);
		
		context.setAttribute("webDriver", androidDriver);
		
	}
	
	@Test(priority = 1, enabled = true)
	public void checkPopup() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_TERMS_OK_BTN)));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isDisplayed()){
			failMsg = "Start Popup is not display";
		}
		
		androidDriver.closeApp();
		
		androidDriver.activateApp(CommonAndroid.MEETING_PACKAGENAME);
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isDisplayed()){
			failMsg = failMsg + "\n3.Start Popup is not still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void checkPopup2() throws Exception {
		String failMsg = "";
		
        androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/terms_checkbox")).click();
        Thread.sleep(1000);
        
        androidDriver.findElement(By.id(CommonAndroid.ID_TERMS_OK_BTN)).click();
        
        if(!androidDriver.findElement(By.id("com.android.permissioncontroller:id/grant_dialog")).isDisplayed()) {
        	failMsg = "1.Permission Popup is not display";
        }
    		
        androidDriver.closeApp();
		
		androidDriver.activateApp(CommonAndroid.MEETING_PACKAGENAME);
		
		//layout 미노출로 back 1회 후 reactive
		androidDriver.navigate().back();
		Thread.sleep(1000);
		
		androidDriver.activateApp(CommonAndroid.MEETING_PACKAGENAME);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.permissioncontroller:id/grant_singleton")));
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isEmpty()) {
			failMsg = failMsg + "\n2.Start Popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void denyPermission() throws Exception {
		String failMsg = "";
		
		//permission deny
		androidDriver.findElement(By.id("com.android.permissioncontroller:id/permission_deny_button")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.id("com.android.permissioncontroller:id/permission_deny_button")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.id("com.android.permissioncontroller:id/permission_deny_button")).click();
		Thread.sleep(2000);
		androidDriver.findElement(By.id("com.android.permissioncontroller:id/permission_deny_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_text")));
		
		String denymsg = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText();
		
		if(!denymsg.contentEquals(MSG_DENY)) {
			failMsg = "Msg is wrong [Expected]" + MSG_DENY + " [Actual]" + denymsg;
		}
		
		androidDriver = commA.setAndroidDriver(0,true);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void checkSettingUI() throws Exception {
		String failMsg = "";
		
		androidDriver = commA.setAndroidDriver(0,true);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(CommonAndroid.ID_TERMS_OK_BTN)));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_TERMS_OK_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/login_main_setting_btn")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/login_main_setting_btn")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/setting_header")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_network_text")).getText().contentEquals("모바일 네트워크 사용")
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_network_text")).isDisplayed()) {
			failMsg = "Wrong Menu [Expected] 모바일 네트워크 사용 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_network_text")).getText();
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_noise_text")).getText().contentEquals("노이즈 제거 사용")
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_noise_text")).isDisplayed()) {
			failMsg = failMsg + "\n2.Wrong Menu [Expected] 노이즈 제거 사용 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_noise_text")).getText();
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_about_text")).getText().contentEquals("About")
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_about_text")).isDisplayed()) {
			failMsg = "\n3.Wrong Menu [Expected] About [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_about_text")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void checkVersion() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/about")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/about_header_view")));
		
		String appVersion = commA.findAppVersion(0);
		
		String version = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/version")).getText();
		if(!version.substring(version.lastIndexOf("r")+1).trim().contentEquals(appVersion)) {
			failMsg = "Wrong version [Expected]" + appVersion + " [Actual]" + version;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void clickLicense() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/about_open_source_license")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/licence_header_view")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/license_textview")).isDisplayed()) {
			failMsg = "Licence Menu is not display";
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
