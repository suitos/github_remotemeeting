package mandatory;


import static org.testng.Assert.fail;

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


public class P2PFree_Share {
	
	public static String XPATH_DOC_BTN = "//button[@id='doc-share']";
	public static String XPATH_DOCUPLOAD_BTN = "//a[@id='doc-upload-btn']";
	
	public static String XPATH_CURSOR_BTN = "//button[@id='draw-hide']";
	public static String XPATH_POINTER_BTN = "//button[@id='draw-pointer']";
	public static String XPATH_DRAW_BTN = "//button[@id='draw-pen']";
	public static String XPATH_ERASER_BTN = "//button[@id='draw-eraser']";
	public static String XPATH_REMOVEALL_BTN = "//button[@id='draw-removeall']";
	public static String XPATH_CLOSE_BTN = "//button[@id='doc-close']";
	
	public String roomID = "";
	
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
	public void CreateFree() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.createFreeMeeting(driver, CommonValues.FREENICKNAME);
		
		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = "1.Can't enter Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 2)
	public void ShareDOC() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_DOC_BTN)));
		
		driver.findElement(By.xpath(XPATH_DOC_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(XPATH_DOCUPLOAD_BTN))));
		
		if(!driver.findElement(By.xpath(XPATH_DOCUPLOAD_BTN)).isDisplayed()) {
			failMsg = "1.DOC input box is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3)
	public void ShareDOC_docx0() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,0);
		
		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[0] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 4)
	public void ShareDOC_xlsx1() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,1);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[1] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5)
	public void ShareDOC_hwp2() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,2);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[2] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6)
	public void ShareDOC_png3() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,3);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[3] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7)
	public void ShareDOC_jpg4() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,4);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8)
	public void ShareDOC_gif5() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,5);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[5] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9)
	public void ShareDOC_pdf6() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,6);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[6] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10)
	public void ShareDOC_pptx7() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,7);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[7] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11)
	public void ShareDOC_txt8() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,8);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12)
	public void checkPointer() throws Exception {
		String failMsg = "";
		
		if (driver.findElement(By.xpath(XPATH_CURSOR_BTN)).getAttribute("class").contentEquals("button active")) {

			driver.findElement(By.xpath(XPATH_POINTER_BTN)).click();

			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath(XPATH_POINTER_BTN)), "class",
					"active"));

			if (!driver.findElement(By.xpath(XPATH_POINTER_BTN)).getAttribute("class").contentEquals("button active")) {
				failMsg = "Pointer is not active"
						+ driver.findElement(By.xpath(XPATH_POINTER_BTN)).getAttribute("class");

			}
		} else {
			failMsg = "Cursor is not default active";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13)
	public void checkDraw() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_DRAW_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath(XPATH_DRAW_BTN)), "class", "button active"));
		
		if(!driver.findElement(By.xpath(XPATH_DRAW_BTN)).getAttribute("class").contentEquals("button active")) {
			failMsg = "Drawer is not active";	
		}
		if(driver.findElement(By.xpath(XPATH_ERASER_BTN)).getAttribute("disabled") != null) {
			failMsg = "\nEraser is disabled";
		}
		if(driver.findElement(By.xpath(XPATH_REMOVEALL_BTN)).getAttribute("disabled") != null) {
			failMsg = "\nRemoveall is disabled";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14)
	public void closeShareDOC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_CLOSE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//div[@id='doc-tools']")), "class", "visible")));
        
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
        String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		if(!timeline.contentEquals(CommonValues.FREENICKNAME + "님이 문서공유를 종료했습니다.")) {
			failMsg = "Wrong Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15)
	public void StartShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), CommonValues.TOAST_STARTSCREENSHARE));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
	
		if(!msg.contentEquals(CommonValues.TOAST_STARTSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16)
	public void StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), CommonValues.TOAST_STOPSCREENSHARE));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
	
		if(!msg.contentEquals(CommonValues.TOAST_STOPSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + msg;
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
	
	public void ShareDocument(WebDriver driver, int i) throws InterruptedException {
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[i];
		driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//div[@id='doc-tools']")), "class", "visible"));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@id='doc-count']"), Integer.toString(i+1)));
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//li[" +(i+1)+ "]/div/p/b")), "title", CommonValues.TESTFILE_LIST[i]));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[" +(i+1)+ "]/div/p/b")));	
	}

}
