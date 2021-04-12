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
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import admin.Connect;

/*
 * 1.로그인
 * 2.빠른 시작 선택 후 문서 공유 모드로 회의 시작
 * 3.이미지 파일 공유 후 로그 확인
 * 4.텍스트 파일 공유 후 로그 확인
 * 5.문서 공유 모드로 화면 공유 후 토스트 메세지 확인
 * 6.문서 공유 모드로 화면 공유 중지 후 토스트 메세지 확인
 * 7.문서 공유 모드 회의 나가기
 * 8.빠른 시작 선택 후 화면 공유 모드로 회의 시작
 * 9.이미지 파일 공유 후 로그 확인
 * 10.텍스트 파일 공유 후 로그 확인
 * 11.화면 공유 모드로 화면 공유 후 토스트 메세지 확인
 * 12.화면 공유 모드로 화면 공유 중지 후 토스트 메세지 확인
 * 13.화면 공유 모드 회의 나가기
 * 14.빠른 시작 선택 후 세미나 모드로 회의 시작
 * 15.이미지 파일 공유 후 로그 확인
 * 16.텍스트 파일 공유 후 로그 확인
 * 17.세미나 모드로 화면 공유 후 토스트 메세지 확인
 * 18.세미나 모드로 화면 공유 중지 후 토스트 메세지 확인
 * 19.세미나 모드 회의 나가기
 * 20.라운지에서 관리자 페이지 버튼 선택 후 대시보드 화면 확인
 */

public class P2PEnterprise_Mode {
	
	public String TOAST_SEMINARMODE = "";
	
	public static WebDriver driver;
	
	public String[] user1 = {CommonValues.ADMEMAIL, CommonValues.ADMINNICKNAME};
	public String[] user2 = {CommonValues.USERS[0], CommonValues.USERS[0].replace("@gmail.com", "")};
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		if(CommonValues.MEETING_URL.contentEquals(CommonValues.MEETING_URL_REAL6)) {
			user1[0] = "auto1@rsupport.com";
			user1[1] = "자동화1";
			user2[0] = "auto2@rsupport.com";
			user1[1] = "자동화2";
		} 
		
		//유저에 따라 토스트 메세지 다를수 있
		TOAST_SEMINARMODE =  user1[1] +"님이 사회자 권한을 선택하였습니다.";
		
		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
	}
	
	@Test(priority = 1)
	public void Login_Mode() throws Exception {
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(driver, user1[0], CommonValues.USERPW);
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
		
		Thread.sleep(500);
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline : " + timeline;
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
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
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
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STARTSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
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
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STOPSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void ExitDOCMode() throws Exception {
		Exit();
	}
	
	@Test(priority = 8, enabled = true)
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
		Thread.sleep(500);
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline : " + timeline;
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
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
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
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STARTSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
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
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STOPSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void ExitScreenMode() throws Exception {
		Exit();
	}
	
	@Test(priority = 14, enabled = true)
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
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
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
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
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
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STARTSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18)
	public void SeminarMode_StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loader-bi']")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));

		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STOPSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" +msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19)
	public void ExitSeminarMode() throws Exception {
		Exit();
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
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
			String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
			if (!msg.contentEquals(TOAST_SEMINARMODE)) {
				Exception e = new Exception("Wrong SendInvitation MSG [Expected]" + TOAST_SEMINARMODE + " [Actual]"
						+ msg);
				throw e;
			}
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITE)));
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();
	}
	
	public void ShareDocument(WebDriver driver, int filetype, int sharedDocCount, String N) throws InterruptedException {
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[filetype];
		
		switch(N) {
			case "DOCMODE":driver.findElement(By.xpath("//button/input[@type='file']")).sendKeys(addedfile);
			break;
			case "OTHERMODE" :driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
			break;
		}
		
		Thread.sleep(5000);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//div[@id='doc-tools']")), "class", "visible"));
        
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@id='doc-count']"), Integer.toString(sharedDocCount)));
		
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
