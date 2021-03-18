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

public class Setting {

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
	public void goSetting() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[3]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SETTINGS"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText().contentEquals("SETTINGS")) {
			failMsg = "Wrong Header [Expected] SETTINGS [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void clickAbout() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/about")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
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
	
	@Test(priority = 3, enabled = true)
	public void clickBack() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_left_image_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SETTINGS"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/setting_activity")).isDisplayed()) {
			failMsg = "Don't intent Activity";
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
