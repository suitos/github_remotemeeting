package mandatory;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class P2PEnterprise_Mode {
	
	public static String TOAST_SEMINARMODE = CommonValues.ADMINNICKNAME +"님이 사회자 권한을 선택하였습니다.";
	
	public static String HREF_PROFILE = "//a[@href='/customer/dashboard']";
	
	public static String ADMIN_URI = "/rmadmin";
	public static String DASHBOARD_URI = "/customer/dashboard";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
	}
	
	@Test(priority = 1)
	public void Login_Mode() throws Exception {
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 2)
	public void QuickStart_DOCMode() throws Exception {

		ChooseMode(1);
	}
	
	@Test(priority = 3)
	public void DOCMode_Sharejpg() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,4,1,"DOCMODE");
		
		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4)
	public void DOCMode_Sharetxt() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,8,2,"DOCMODE");

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5)
	public void DOCMode_StartShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STARTSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6)
	public void DOCMode_StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STOPSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7)
	public void ExitDOCMode() throws Exception {
		Exit();
	}
	
	@Test(priority = 8)
	public void QuickStart_ScreenMode() throws Exception {

		ChooseMode(2);
	}
	
	@Test(priority = 9)
	public void ScreenMode_Sharejpg() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,4,1,"OTHERMODE");
		
		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10)
	public void ScreenMode_Sharetxt() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,8,2,"OTHERMODE");

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11)
	public void ScreenMode_StartShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STARTSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12)
	public void ScreenMode_StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STOPSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13)
	public void ExitScreenMode() throws Exception {
		Exit();
	}
	
	@Test(priority = 14)
	public void QuickStart_SeminarMode() throws Exception {
		
		ChooseMode(3);
	}
	
	@Test(priority = 15)
	public void SeminarMode_Sharejpg() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,4,1,"OTHERMODE");
		
		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16)
	public void SeminarMode_Sharetxt() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,8,2,"OTHERMODE");

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.ADMINNICKNAME + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17)
	public void SeminarMode_StartShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STARTSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18)
	public void SeminarMode_StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		if(comm.GetAndCheckToastMsg(driver, CommonValues.TOAST_STOPSCREENSHARE) == false) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19)
	public void ExitSeminarMode() throws Exception {
		Exit();
	}
	
	@Test(priority = 20)
	public void GoAdmin() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(HREF_PROFILE)).click();
		
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.close();
		driver.switchTo().window(tabs.get(1));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + DASHBOARD_URI)) {
			failMsg = "Can't enter Admin Dashboard [Expected]" + CommonValues.MEETING_URL + DASHBOARD_URI
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath("//div[@id='header-username']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='wrap-more-profile']")));
		
		driver.findElement(By.xpath("//div[@id='wrap-logout']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-box']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + ADMIN_URI)) {
			failMsg = "Can't enter Admin Login [Expected]" + CommonValues.MEETING_URL + ADMIN_URI
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	public void ChooseMode(int i) throws Exception { 
		//1=문서공유 2=화면공유 3=세미나모드
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		List<WebElement> Mode = driver.findElements(By.xpath("//div[@class='form-item-wrap radio-wrap']"));
		
		Mode.get(i).click();
		String Title = Mode.get(i).findElement(By.xpath(".//span[@class='icon-text']")).getText();
		System.out.println(Title);
		
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys(Title);
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		//wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='device-setting-notifications-box-wrapper']"), "style", "display: none;"));
		
		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			Exception e = new Exception("Wrong URL :" + driver.getCurrentUrl());
			throw e;
		}
		if (i != 3) {
			if (!driver.findElement(By.xpath("//div[@id='starter-wrap']/h2")).getText().contains(Title)) {
				Exception e = new Exception(
						"Wrong Mode :" + driver.findElement(By.xpath("//div[@id='starter-wrap']/h2")).getText());
				throw e;
			}
		} else {
			if (comm.GetAndCheckToastMsg(driver, TOAST_SEMINARMODE) == false) {
				Exception e = new Exception("Wrong SendInvitation MSG [Expected]" + TOAST_SEMINARMODE + " [Actual]"
						+ driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText());
				throw e;
			}
		}
	}
	
	public void ShareDocument(WebDriver driver, int i, int j, String N) throws InterruptedException {
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[i];
		
		switch(N) {
			case "DOCMODE":driver.findElement(By.xpath("//button/input[@type='file']")).sendKeys(addedfile);
			break;
			case "OTHERMODE" :driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
			break;
		}
		
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//div[@id='doc-tools']")), "class", "visible"));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@id='doc-count']"), Integer.toString(j)));
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
	}
	
	public void Exit() throws Exception {
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		comm.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			Exception e = new Exception("1.Can't leave Room [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL
					+ " [Actual]" + driver.getCurrentUrl());
			throw e;
		}
	}
	

}
