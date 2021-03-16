package mandatory;

import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * 1.홈에서 회의 시작 선택
 * 2.닉네임 입력 후 회의 시작 선택
 * 3.참석자 참석 및 토스트 메세지 확인
 * 4.16분 대기 및 회의 최대 시간 경과 팝업 확인
 * 5.팝업 내 확인 클릭 후 비즈니스 팝업 확인
 * 6.비즈니스 팝업 내 나가기 클릭 후 회의 종료 확인
 */

public class P2PFree2 {
	
	public static String XPATH_FREECREATE_HEADER = "//header[@class='title']";
	
	public static String MSG_FREECREATE_HEADER = "회원 가입 없이 바로 회의 시작";
	
	public static String MSG_SETTING = "현재의 마이크, 카메라 설정이 적용됩니다.";
	
	public static String XPATH_LEAVE_BTN = "//button[@id='btn-leave']";
	
	public static String MSG_15EXCEED = "한 회의는 최대 15분까지 사용할 수 있습니다.";
	
	public static String EXIT_HEADER = "RemoteMeeting Business는 더 다양한 협업 기능을 제공합니다.";
	
	public String roomID = "";
	
	public static WebDriver driver;
	public static WebDriver Attenddriver;

	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Attenddriver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", Attenddriver);

	}
	
	@Test(priority = 1)
	public void selectFree2() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_BTN)).click();
		
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_FREECREATE_DIALOG)));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XPATH_FREECREATE_HEADER)));
		
		String headermsg = driver.findElement(By.xpath(XPATH_FREECREATE_HEADER)).getText();
		
		if(!headermsg.contentEquals(MSG_FREECREATE_HEADER)) {
			failMsg = "1. popup subtitle [Expected]" + MSG_FREECREATE_HEADER 
					+ "[Actual]" + headermsg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2)
	public void makeFree2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATE_INPUT)).sendKeys(CommonValues.FREENICKNAME);
		driver.findElement(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)).click();
		
		if(comm.isAlertPresent(driver) == true) {
			TimeUnit.SECONDS.sleep(15);
			driver.findElement(By.xpath(CommonValues.XPATH_FREECREATESUBMIT_BTN)).click();
		}
		
		comm.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='device-setting-notifications-box-wrapper']"), "style", "display: block;"));
		
		if(!driver.findElement(By.xpath("//div[@class='notification-message']/div")).getText().contentEquals(MSG_SETTING)) {
			failMsg = "Wrong Msg [Expected]" + MSG_SETTING + " [Actual]" + driver.findElement(By.xpath("//div[@class='notification-message']/div")).getText();
 		}
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='device-setting-notifications-box-wrapper']"), "style", "display: none;"));
		
		roomID = driver.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL , "");

		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = failMsg + "\n2.Can't enter Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITE)).isDisplayed()) {
			failMsg = failMsg + "\n3.Can't dispay Invitation screen";
		}
		
		if(!driver.findElement(By.xpath("//div[@title='FREE']")).getText().contentEquals("FREE")) {
			failMsg = failMsg + "\n4.Wrong invite title [Expected] FREE [Actual]" + driver.findElement(By.xpath("//div[@title='FREE']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void Attend2() throws Exception {
		String failMsg = "";
		
		comm.attendMeeting(driver, Attenddriver);
		
		/* 입장 toast 안뜨도록 수정됨(21.03.02)
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		System.out.println(msg);
		if(!msg.contentEquals("["+CommonValues.ATTENDEEFREENICKNAME+"] 님이 참여했습니다.")) {
			failMsg = "Wrong Attend MSG [Expected] [FREEATTENDEE] 님이 참여했습니다." 
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		*/
		if (!Attenddriver.getCurrentUrl().contentEquals((CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID))) {
			failMsg = "1.Attendee can't entered Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + Attenddriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 4, enabled = true)
	public void Wait15M() throws Exception {
		String failMsg = "";
		
		TimeUnit.MINUTES.sleep(16);
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Exit Popup is not display in Creator";
		}
		
		if(!Attenddriver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = failMsg + "\n2.Exit Popup is not display in Attendee";
		}
		
		if(!driver.findElement(By.xpath("//p[@id='expired-time-text']")).getText().contentEquals(MSG_15EXCEED)) {
			failMsg = failMsg + "\n3.Popup text is wrong in Creator[Expcted]" + MSG_15EXCEED + " [Actual]" + driver.findElement(By.xpath("//p[@id='expired-time-text']")).getText();
		}
		
		if(!Attenddriver.findElement(By.xpath("//p[@id='expired-time-text']")).getText().contentEquals(MSG_15EXCEED)) {
			failMsg = failMsg + "\n4.Popup text is wrong in Attendee[Expcted]" + MSG_15EXCEED + " [Actual]" + Attenddriver.findElement(By.xpath("//p[@id='expired-time-text']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 5, enabled = true)
	public void ClickConfirm() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='demo-business-info']")));
		
		if(!driver.findElement(By.xpath("//div[@id='demo-business-info']")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']/div/div[1]")).getText().contentEquals(P2PFree.EXIT_HEADER)) {
			failMsg = "\n2.Exit popup header is wrong. [Expected]" + P2PFree.EXIT_HEADER
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']/div/div[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 6, enabled = true)
	public void Exit2() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LEAVE_BTN)));
		
		driver.findElement(By.xpath(XPATH_LEAVE_BTN)).click();
		
		comm.waitForLoad(driver);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.KRHOME_URL)) {
			failMsg = "1.Can't leave Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.KRHOME_URL
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
		Attenddriver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
}
