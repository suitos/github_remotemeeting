package admin;

import static org.testng.Assert.fail;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import mandatory.CommonValues;


/* DashBoard
 * 1. 대시보드 월 사용량(미구현)
 * 2. 대시보드 요금제 - 없을때
 * 3. 대시보드 요금제 - Free 일때
 * 
 */

public class DashBoard {

	public static String XPATH_DASHBOARD_LICENSE_TITLE = "//article[@id='paymentUsage'][2]//div[@class='current-product']";
	public static String XPATH_DASHBOARD_LICENSE_DAY = "//article[@id='paymentUsage'][2]//div[@class='payment-data']//span[@class='amount']";
	public static String XPATH_DASHBOARD_LICENSE_PAYMETN = "//article[@id='paymentUsage'][2]//div[@class='wrap-payment-data table-type purchase-id']/label";
	public static String XPATH_DASHBOARD_LICENSE_ID_DATE = "//article[@id='paymentUsage'][2]//span[@class='br-responsive']";
	public static String XPATH_DASHBOARD_LICENSE_EMPTY = "//article[@id='paymentUsage'][2]//div[@class='no-content']";
	
	public static String XPATH_DASHBOARD_SAVINGCOST = "//div[@class='info-box cost']";
	public static String XPATH_DASHBOARD_SAVINGTRAVEL = "//div[@class='info-box distance']";
	public static String XPATH_DASHBOARD_SAVINGCARBON = "//div[@class='info-box carbon']";
	public static String XPATH_DASHBOARD_SUB_TITLE = "//label[@class='info-box-title']";
	public static String XPATH_DASHBOARD_SUB_NO = "//span[@class='info-box-number']";
	public static String XPATH_DASHBOARD_SUB_TOOLTIP = "//span[@class='cola-admin-admin_question']";
	public static String XPATH_DASHBOARD_SUB_TOOLTIP_MSG = "/div[@class='wrap-tooltip']";
	
	public static String MSG_ADMIN_PAYMENT_FREE = "FREE Trial";
	public static String MSG_ADMIN_PAYMENT_ID = "ID 요금제";
	public static String MSG_ADMIN_PAYMENT_BASIC = "BASIC";
	public static String MSG_ADMIN_PAYMENT_EMPTY = "사용중인 요금제가 없습니다.";
	public static String MSG_ADMIN_SAVINGCOST_TITLE = "절약비용";
	public static String MSG_ADMIN_SAVINGTRAVEL_TITLE = "절약거리";
	public static String MSG_ADMIN_SAVINGCARBON_TITLE = "탄소절감량";
	public static String MSG_ADMIN_SAVINGCOST_TOOLTIP = "절약비용은 화상회의 대신 오프라인 회의했을 때 예상되는 최소 비용입니다. 당신이 소재하고 있는 곳에서 실제 미팅이 열리는 지역으로 이동했을 때 발생하는 비용을 가상화합니다. 비용은 주로 연료와 숙식으로 이루어지는 여행경비입니다. 절약비용은 실제 경비와 차이가 있습니다.";
	public static String MSG_ADMIN_SAVINGTRAVEL_TOOLTIP = "절약거리는 오프라인으로 회의를 했을 경우 예상되는 이동거리 입니다. 도시단위 위치정보를 기반으로 미팅 개설자와 참여자의 직선거리 합계이며, 실제 도로상황 등이 반영되지 않은 수치입니다. 실제 거리보다 짧거나 다를 수 있습니다.";
	public static String MSG_ADMIN_SAVINGCARBON_TOOLTIP = "탄소절감량은 오프라인으로 회의를 했을 경우 예상되는 탄소배출량 입니다. 회의 참가 위치의 직선거리, 연비, 등을 반영한 수치이며, 실제 발생하는 탄소배출량보다 적거나 다를 수 있습니다.";
	
	
	public static String DASHBOARD_URI = "/customer/dashboard";
	
	public static String EXPIRED_ADM = "testhyeonkyeong3@gmail.com"; //사용요금제 없는 유저
	public static String ID_ADM = "id01@rsupport.com"; //id 요금제 사용 유저
	public static String BASIC_ADM = "tmdalstest06@gmail.com"; //id 요금제 사용 유저
	public static String ID_PW = "111111";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		context.setAttribute("webDriver", driver);

	}
	
	@Test(priority = 1, enabled = true)
	public void dashboardUseage() throws Exception {
		String failMsg = "";
		
		//admin logout
	
		driver.get(CommonValues.MEETING_URL);
		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 2, enabled = true)
	public void dashboardFree_License() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, Connect.FREE_ADM, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)).getText().contentEquals(MSG_ADMIN_PAYMENT_FREE)) {
			failMsg = failMsg + "\n1. payment data [Expected]" + MSG_ADMIN_PAYMENT_FREE 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)).getText();
		}
		
		String licenseDate = driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_DAY)).getText();
		if(Integer.parseInt(licenseDate) <= 0) {
			failMsg = failMsg + "\n2. Remining period [Expected]more than 0"
					+ " [Actual]" + licenseDate;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void dashboardExpired_License() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, EXPIRED_ADM, ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_EMPTY)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_EMPTY)).getText().contentEquals(MSG_ADMIN_PAYMENT_EMPTY)) {
			failMsg = failMsg + "\n1. payment data [Expected]" + MSG_ADMIN_PAYMENT_EMPTY 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_EMPTY)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void dashboardBasic_License() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, BASIC_ADM, ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)).getText().contentEquals(MSG_ADMIN_PAYMENT_BASIC)) {
			failMsg = failMsg + "\n1. payment data [Expected]" + MSG_ADMIN_PAYMENT_BASIC 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)).getText();
		}
		
		String licenseDate = driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_DAY)).getText();
		if(Integer.parseInt(licenseDate) < 0) {
			failMsg = failMsg + "\n2. Remining period [Expected]more than 0"
					+ " [Actual]" + licenseDate;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void dashboardID_License() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, ID_ADM, ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)).getText().contentEquals(MSG_ADMIN_PAYMENT_ID)) {
			failMsg = failMsg + "\n1. payment data [Expected]" + MSG_ADMIN_PAYMENT_ID 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)).getText();
		}
		
		String payment = driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_PAYMETN)).getText();
		if(!payment.contentEquals("구매 내역")) {
			failMsg = failMsg + "\n2. payment data [Expected]구매 내역" + MSG_ADMIN_PAYMENT_ID 
					+ " [Actual]" + payment;
		}
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_ID_DATE)).getText().contentEquals("2021.01.06 ~ 2099.12.31")) {
			failMsg = failMsg + "\n3. payment data [Expected]2021.01.06 ~ 2099.12.31" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_LICENSE_ID_DATE)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void dashboard_savingCost() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TITLE)).getText().contentEquals(MSG_ADMIN_SAVINGCOST_TITLE)) {
			failMsg = failMsg + "\n1. saving cost title [Expected]" + MSG_ADMIN_SAVINGCOST_TITLE 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TITLE)).getText();
		}
		
		String savingCost = driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_NO)).getText();
		if(Integer.parseInt(savingCost.replace(",", "")) <= 0) {
			failMsg = failMsg + "\n2. Saving Cost [Expected]more than 0"
					+ " [Actual]" + savingCost;
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, dependsOnMethods = {"dashboard_savingCost"}, alwaysRun = true, enabled = true)
	public void dashboard_savingCost_tooltip() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + DASHBOARD_URI)) {
			conn.logoutAdmin(driver);
			conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TOOLTIP));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText().contentEquals(MSG_ADMIN_SAVINGCOST_TOOLTIP)) {
			failMsg = failMsg + "\n1. saving cost tooltip msg(mouse hover) [Expected]" + MSG_ADMIN_SAVINGCOST_TOOLTIP 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText();
		}
		
		//타이틀 호버
		actions.moveToElement(driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TITLE))).perform();
		Thread.sleep(500);
		
		//? 클릭 후 다른 위치로 마우스 이동
		driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TOOLTIP)).click();
		actions.moveToElement(driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TITLE))).perform();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText().contentEquals(MSG_ADMIN_SAVINGCOST_TOOLTIP)) {
			failMsg = failMsg + "\n2. saving cost tooltip msg(click) [Expected]" + MSG_ADMIN_SAVINGCOST_TOOLTIP 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCOST + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText();
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void dashboard_savingTravel() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_NO)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TITLE)).getText().contentEquals(MSG_ADMIN_SAVINGTRAVEL_TITLE)) {
			failMsg = failMsg + "\n1. saving cost title [Expected]" + MSG_ADMIN_SAVINGTRAVEL_TITLE 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TITLE)).getText();
		}
		
		String savingCost = driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_NO)).getText();
		if(Integer.parseInt(savingCost.replace(",", "")) < 0) {
			failMsg = failMsg + "\n2. Saving Cost [Expected]more than 0"
					+ " [Actual]" + savingCost;
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 13, dependsOnMethods = {"dashboard_savingTravel"}, alwaysRun = true, enabled = true)
	public void dashboard_savingTravel_tooltip() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + DASHBOARD_URI)) {
			conn.logoutAdmin(driver);
			conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TOOLTIP));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText().contentEquals(MSG_ADMIN_SAVINGTRAVEL_TOOLTIP)) {
			failMsg = failMsg + "\n1. saving travel tooltip msg(mouse hover) [Expected]" + MSG_ADMIN_SAVINGTRAVEL_TOOLTIP 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText();
		}
		
		//타이틀 호버
		actions.moveToElement(driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TITLE))).perform();
		Thread.sleep(500);
		
		//? 클릭 후 다른 위치로 마우스 이동
		driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TOOLTIP)).click();
		actions.moveToElement(driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TITLE))).perform();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText().contentEquals(MSG_ADMIN_SAVINGTRAVEL_TOOLTIP)) {
			failMsg = failMsg + "\n2. saving travel tooltip msg(click) [Expected]" + MSG_ADMIN_SAVINGTRAVEL_TOOLTIP 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGTRAVEL + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText();
		}
				
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void dashboard_savingCarbon() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_NO)));
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TITLE)).getText().contentEquals(MSG_ADMIN_SAVINGCARBON_TITLE)) {
			failMsg = failMsg + "\n1. saving carbon title [Expected]" + MSG_ADMIN_SAVINGCARBON_TITLE 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TITLE)).getText();
		}
		
		String savingCost = driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_NO)).getText();
		if(Integer.parseInt(savingCost.replace(".", "")) < 0) {
			failMsg = failMsg + "\n2. Saving carbon [Expected]more than 0"
					+ " [Actual]" + savingCost;
		}
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 15, dependsOnMethods = {"dashboard_savingCarbon"}, alwaysRun = true, enabled = true)
	public void dashboard_savingCarbon_tooltip() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		//admin logout
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + DASHBOARD_URI)) {
			conn.logoutAdmin(driver);
			conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_LICENSE_TITLE)));
		
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TOOLTIP));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText().contentEquals(MSG_ADMIN_SAVINGCARBON_TOOLTIP)) {
			failMsg = failMsg + "\n1. saving carbon tooltip msg(mouse hover) [Expected]" + MSG_ADMIN_SAVINGCARBON_TOOLTIP 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText();
		}
		
		//타이틀 호버
		actions.moveToElement(driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TITLE))).perform();
		Thread.sleep(500);
		
		//? 클릭 후 다른 위치로 마우스 이동
		driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TOOLTIP)).click();
		actions.moveToElement(driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TITLE))).perform();
		Thread.sleep(500);
		
		if(!driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText().contentEquals(MSG_ADMIN_SAVINGCARBON_TOOLTIP)) {
			failMsg = failMsg + "\n2. saving carbon tooltip msg(click) [Expected]" + MSG_ADMIN_SAVINGCARBON_TOOLTIP 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DASHBOARD_SAVINGCARBON + XPATH_DASHBOARD_SUB_TOOLTIP_MSG)).getText();
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
