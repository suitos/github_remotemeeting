package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/* Theme
 * 1. 테마관리 이동확인
 * 2. 테마관리 수정 클릭 > 테마 수정 화면 이동
 * 3. 테마수정 화면 기본값 옵션 확인. 이름란 비활성화, 프리뷰 이미지 기본 이미지 사용
 * 4. 테마수정 신규 선택하여 이미지 변경 저장 - 저장 팝업 확인. 변경 이미지 확인. 저장 후 언어 추가 탭 추가 확인
 */

public class Theme {
	public static String XPATH_THEME_LIST = "//div[@id='theme-list']/ul/li";
	public static String XPATH_THEME_RADIO_DEFAULT = "//div[@class='theme-type']//div[@class='form-item-wrap radio-wrap'][1]";
	public static String XPATH_THEME_RADIO_MODIFY = "//div[@class='theme-type']//div[@class='form-item-wrap radio-wrap'][2]";
	public static String XPATH_THEME_RADIO_CURRENTIMG = "//div[@class='form-set explanation-group image-preview-wrapper']//div[@class='radio-wrap disable-target'][1]";
	public static String XPATH_THEME_RADIO_NEWIMG = "//div[@class='form-set explanation-group image-preview-wrapper']//div[@class='radio-wrap disable-target'][2]";
	public static String XPATH_THEME_EDITVIEW_NAME = "//input[@id='theme-name-default']";
	
	public static String XPATH_THEME_EDITVIEW_TAB1 = "//ul[@id='tab-menu-handlebars']/li[1]";
	public static String XPATH_THEME_EDITVIEW_TAB2 = "//ul[@id='tab-menu-handlebars']/li[2]";
	public static String XPATH_THEME_EDITVIEW_IMG_PREVIEW1 = "//div[@class='preview preview-saved image-holder on']//img";
	public static String XPATH_THEME_EDITVIEW_IMG_PREVIEW2 = "//div[@class='preview preview-saved on image-holder']//img";
	
	public static String XPATH_THEME_EDITVIEW_IMG_INPUT1 = "//input[@id='channel-preview-default']";
	public static String XPATH_THEME_EDITVIEW_IMG_INPUT2 = "//input[@id='main-preview-default']";
	
	public static String XPATH_THEME_EDITVIEW_SAVE_BTN = "//button[@id='save-theme-default']";
	
	public static String URL_THEME = "/customer/company-theme-list";
	public static String URL_THEMEVIEW = "/customer/company-theme-view/";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);

	}
	
	@BeforeMethod
	public void setBrowser() {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() > 1) {
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));
		}
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_THEME)) {
			Users user = new Users();
			user.selectSideMenu(driver, 7, 2);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_LIST)));
		}
	}
	
	@Test(priority = 1, enabled = true)
	public void Theme_view() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		user.selectSideMenu(driver, 7, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_LIST)));
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_THEME)) {
			failMsg = failMsg + "\n1. no management theme view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void Theme_editview() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		List<WebElement> list = driver.findElements(By.xpath(XPATH_THEME_LIST));
		
		if(list.size() == 0) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			//수정 클릭
			list.get(0).findElement(By.xpath(".//a[@class='btn btn-modify']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='theme-handlebars']")));
			
			if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_THEMEVIEW)) {
				failMsg = failMsg + "\n2. not theme view. current url : " + driver.getCurrentUrl();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void Theme_editDefault() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		List<WebElement> list = driver.findElements(By.xpath(XPATH_THEME_LIST));
		
		if(list.size() == 0) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			//수정 클릭
			list.get(0).findElement(By.xpath(".//a[@class='btn btn-modify']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_RADIO_DEFAULT)));

			driver.findElement(By.xpath(XPATH_THEME_RADIO_DEFAULT)).click();
			Thread.sleep(500);
			
			if(driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_NAME)).isEnabled()) {
				failMsg = failMsg + "\n2. theme name input box is enabled(defalut mode)";
			}
			
			if(!driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW1)).getAttribute("src").contains("/storage/theme")) {
				failMsg = failMsg + "\n3. lounge defalut theme img : " 
						+ driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW1)).getAttribute("src");
			}
			if(!driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW2)).getAttribute("src").contains("/storage/theme")) {
				failMsg = failMsg + "\n4. box defalut theme img : " 
						+ driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW2)).getAttribute("src");
			}
					
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void Theme_editMod() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<WebElement> list = driver.findElements(By.xpath(XPATH_THEME_LIST));
		
		if(list.size() == 0) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			//수정 클릭
			list.get(0).findElement(By.xpath(".//a[@class='btn btn-modify']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_RADIO_DEFAULT)));

			driver.findElement(By.xpath(XPATH_THEME_RADIO_MODIFY)).click();
			Thread.sleep(500);
			
			if(!driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_NAME)).isEnabled()) {
				failMsg = failMsg + "\n2. theme name input box is disabled(modify mode)";
			}
			
			driver.findElement(By.xpath(XPATH_THEME_RADIO_NEWIMG)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='view-area preview preview-new preview-new-default on']")));
			
			String imgFime = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[3];
			driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_INPUT1)).sendKeys(imgFime);
			driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_INPUT2)).sendKeys(imgFime);
			
			//save
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_SAVE_BTN)));
			driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_SAVE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
			if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(GroupInfo.MSG_POPUP_SAVE)) {
				failMsg = failMsg + "\n3. saved popup msg [Expected]" + GroupInfo.MSG_POPUP_SAVE
						+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
			}
			
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW1)));
			
			if(!driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW1)).getAttribute("src").contains(CommonValues.MEETING_URL + "/storage/companyTheme/thumbnail/")) {
				failMsg = failMsg + "\n4. lounge modified theme img : " 
						+ driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW1)).getAttribute("src");
			}
			if(!driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW2)).getAttribute("src").contains(CommonValues.MEETING_URL + "/storage/companyTheme/")) {
				failMsg = failMsg + "\n5. box modified theme img : " 
						+ driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_IMG_PREVIEW2)).getAttribute("src");
			}
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_EDITVIEW_TAB2)));
			if(!driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_TAB2 + "//span")).getText().contentEquals("+ 언어 추가")) {
				failMsg = failMsg + "\n1. added tab name [Expected]+ 언어 추가 [Actual]"
						+ driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_TAB2 + "//span")).getText();
			}
			
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public void setDefault() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_THEME)) {
			Users user = new Users();
			user.selectSideMenu(driver, 7, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_LIST)));
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		List<WebElement> list = driver.findElements(By.xpath(XPATH_THEME_LIST));
		
		if(list.size() > 0) {
			//수정 클릭
			list.get(0).findElement(By.xpath(".//a[@class='btn btn-modify']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_THEME_RADIO_DEFAULT)));

			driver.findElement(By.xpath(XPATH_THEME_RADIO_DEFAULT)).click();
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_SAVE_BTN)));
			driver.findElement(By.xpath(XPATH_THEME_EDITVIEW_SAVE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			
		} 
	}
	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		setDefault();
		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}


