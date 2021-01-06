package mandatory;

import static org.testng.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * 1.AI데모 팝업 확인
 * 2.AI데모 실행,  AI데모 룸 꽉찻을 경우 예외처리
 * 3.데모 로그 확인
 * 4.나가기 버튼 클릭 후 팝업 명 확인
 * 5.AI데모 나가기 
 */

public class Free {
	public static String XPATH_AIDEMO_BTN = "//button[@class='chatbot-btn chatbot-popup']";
	public static String XPATH_POPUP_DIALOG = "//section[@id='create-live-meeting-dialog']";
	public static String XPATH_POPUP_SUBDESC = "//section[@id='create-live-meeting-dialog']//p[@classname='sub-desc']";
	
	public static String XPATH_AIDEMO_BTNBOX = "//button[@id='create-live-meeting']";
	
	public static String XPATH_EXITDEMO_BTN = "//button[@id='exit']";
	
	public static String MSG_FULLDEMO = "모든 아루가 데모를 진행하고 있습니다.\n" + "잠시 후 다시 시도해주세요.";
	public static String MSG_DEMOCHAT = "WebRTC란?\n" + "Web Real-Time Communication의 약자";
	public static String MSG_EXITDEMO = "지금 회의실을 나가시면 처음부터 다시 시작하셔야 합니다.\n" + "그래도 나가시겠습니까?";
	public static String MSG_AIDEMO_SUBDESC = "리모트미팅의 사용법과 기능을 쉽게 알아보세요.";
	
	public static WebDriver driver;

	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);

	}
	
	@Test(priority = 1)
	public void demoselet() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_AIDEMO_BTN)));
		
		driver.findElement(By.xpath(XPATH_AIDEMO_BTN)).click();
		
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(XPATH_POPUP_DIALOG)));
		
		if(!driver.findElement(By.xpath(XPATH_POPUP_SUBDESC)).getAttribute("innerText").contentEquals(MSG_AIDEMO_SUBDESC)) {
			failMsg = "1. popup subtitle [Expected]" + MSG_AIDEMO_SUBDESC 
					+ "[Actual]" + driver.findElement(By.xpath(XPATH_POPUP_SUBDESC)).getAttribute("innerText");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2)
	public void Enterdemo() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_AIDEMO_BTNBOX)).click();
		Thread.sleep(1000);

		if (isAlertPresent(driver) == true) {
			Alert alert = driver.switchTo().alert();
			String alert_msg = alert.getText();
			alert.accept();
			Thread.sleep(1000);
			System.out.println(alert_msg);
			if (!alert_msg.contentEquals(MSG_FULLDEMO)) {
				failMsg = "1.Full AI DEMO ROOM";

			}
		} else {

			waitForLoad(driver);

			if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
				failMsg = "1.Can't enter Demo Room";
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3)
	public void checkDemochat() throws Exception {
		String failMsg = "";
		
		TimeUnit.MINUTES.sleep(1);
		
		driver.findElement(By.xpath("//i[@class='icon logger']")).click();
		Thread.sleep(2000);
		
		List<WebElement> Demomenu = driver.findElements(By.xpath("//button[@class='bot-script']"));
		Demomenu.get(2).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//i[@class='icon logger']")).click();
		Thread.sleep(2000);
		
		List<WebElement> Demochat = driver.findElements(By.xpath("//li[@class='chat']/div/p"));
		System.out.println(Demochat.size());
		if(!Demochat.get(Demochat.size() -1).getText().contentEquals(MSG_DEMOCHAT)) {
			failMsg = "Demo chat is wrong [Expected]" + MSG_DEMOCHAT + " [Actual]" + Demochat.get(Demochat.size() -1).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4)
	public void checkExitdemoMSG() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_EXITDEMO_BTN)).click();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='dialog-header']")), MSG_EXITDEMO));
		
		if(!driver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals(MSG_EXITDEMO)) {
			failMsg = "1.Wrong Exit MSG";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5)
	public void Exitdemo() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btn-leave']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_AIDEMO_BTN)));
		
		System.out.println(driver.getCurrentUrl());
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.KRHOME_URL) ) {
			failMsg = "1.Can't leave Demo Room";
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
	
	public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
                WebDriverWait wait = new WebDriverWait(driver, 30);
                wait.until(pageLoadCondition);
	}
        
	public boolean isAlertPresent(WebDriver wd) {
	    boolean foundAlert = false;
	    WebDriverWait wait = new WebDriverWait(wd, 5);
	    try {
	        wait.until(ExpectedConditions.alertIsPresent());
	        foundAlert = true;
	    } catch (TimeoutException TO) {
	        foundAlert = false;
	    }
	    return foundAlert;
	}
}
