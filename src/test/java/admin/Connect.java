package admin;

import static org.testng.Assert.fail;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import mandatory.CommonValues;
import mandatory.Free;


public class Connect {
	public static String XPATH_DASHBOARD_USAGE_TITLE = "//article[@id='compareUsage']/div[@class='panel-title']";
	public static String XPATH_DASHBOARD_COUNT_TITLE  = "//article[@id='compareCount']/div[@class='panel-title']";
	
	public static String MSG_DASHBOARD_TITLES = "사용량, 회의 개수";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=en_US", true);
		context.setAttribute("webDriver", driver);

	}
	
	@Test(priority = 1, enabled = true)
	public void connectKO() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)).click();
		
		//한국어 설정
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FOOTER_LANG_KO)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_KO)).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		//로그인
		CommonValues comm = new CommonValues();
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		//관리자 페이지 클릭
		driver.findElement(By.xpath(CommonValues.XPATH_HEADER_ADMIN_BTN)).click();
		
		//새탭 확인
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() == 2) {
			driver.switchTo().window(tabs.get(0));
			//close 1 tab
			driver.close();
			//switch room tab
			driver.switchTo().window(tabs.get(1));
		} 
		
		String dashboard = String.format("%s, %s", driver.findElement(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)).getText(), 
				driver.findElement(By.xpath(XPATH_DASHBOARD_COUNT_TITLE)).getText());
		
		if(!dashboard.contentEquals(MSG_DASHBOARD_TITLES)) {
			failMsg = "1. dashboard title [Expected]" + MSG_DASHBOARD_TITLES + " [Actual]" + dashboard;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
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
}
