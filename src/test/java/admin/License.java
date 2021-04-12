package admin;

import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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


/* License
 * 1. 요금제 화면 이동
 * 2. 베이직 요금제 결제화면 확인
 * 3. 베이직 요금제 결제 - 이미 사용중인 요금제가 있는 경우
 * 4. Id 요금제 사용중인 사용중인 관리자가 새로운 후불 요금제 신청 시도 - 해지후 신청 팝업 확인
 * 5. Free 사용중인 사용중인 관리자가 새로운 후불 요금제 신청 시도 - 취소
 * 10. 요금제 없는 관리자가 ID요금제(user 요금제) 신청 시도 화면 확인
 * 11. 요금제 없는 관리자가 ID요금제(user 요금제) 신청 화면 - 동의 없이 신청 시도
 * 12. 요금제 없는 관리자가 ID요금제(user 요금제) 신청 화면 - ID 3개로 신청 시도(최소 4개)
 * 13. 요금제 없는 관리자가 ID요금제(user 요금제) 신청 화면 - ID 갯수에 따른 사용료 증감 확인
 * 
 */

public class License {
	public static String XPATH_LICENSE_LIST = "//tbody[@id='companyListWrapper']/tr";
	public static String XPATH_LICENSE_APPLYLICENSE_BTN = "//button[@id='applyLicense']";
	public static String XPATH_LICENSE_ADDLICENSE_BTN = "//button[@id='addLicense']";
	public static String XPATH_LICENSE_INPUTBOX_TYPE = "//div[@class='panel-inner']/div[1]/input";
	public static String XPATH_LICENSE_INPUTBOX_DATE = "//div[@class='panel-inner']/div[2]/input";
	public static String XPATH_LICENSE_INPUTBOX_CHARGE = "//div[@class='panel-inner']/div[3]/input";
	public static String XPATH_LICENSE_INPUTBOX_DISCOUNT = "//div[@class='panel-inner']/div[4]/input";
	public static String XPATH_LICENSE_INPUTBOX_MINCHARGE = "//div[@class='panel-inner']/div[5]/input";
	public static String XPATH_LICENSE_INPUTBOX_PAYOPTION = "//div[@class='panel-inner']/div[6]/input";
	
	public static String XPATH_LICENSE_INPUTBOX_IDTYPE = "//div[@class='panel-inner']/div[1]/input[@class='form-control']";
	public static String XPATH_LICENSE_INPUTBOX_IDDATE = "//div[@class='panel-inner']/div[2]//input";
	public static String XPATH_LICENSE_INPUTBOX_IDPRICE = "//div[@class='panel-inner']/div[4]/input";
	public static String XPATH_LICENSE_INPUTBOX_IDPRICETOTAL = "//div[@id='sum-input']";
	public static String XPATH_LICENSE_AGREE_CHECKBOX = "//div[@class='checkbox-wrapper']//div";
	public static String XPATH_LICENSE_INPUTBOX_IDPAYOPTION = "//div[@class='panel-inner']/div[5]/input";
	
	public static String MSG_PANELHEADER_LICENSE = "요금제";
	public static String MSG_LICENSE_REGALREADY = "사용하시던 요금제의 만료일이 남았습니다.\n"
			+ "기간 만료 후, 구매가 가능합니다.";
	public static String MSG_LICENSE_FREE_REMAIN = "아직 무료체험 기간이 남아있습니다.\n"
			+ "계속 진행하시면 잔여 무료체험 기간은 사라집니다.";
	public static String MSG_NO_AGREEMENT = "정기 결제 이용에 동의해주세요.";
	public static String MSG_MINIMUN_ID = "선택하신 요금제 최소 구매 수량은 4개 입니다.";

	public static String URL_ADDLICENSE = "/customer/license-addForm";
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		context.setAttribute("webDriver", driver);

	}
	
	@Test(priority = 1, enabled = true)
	public void licenseView() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText().contentEquals(MSG_PANELHEADER_LICENSE)) {
			failMsg = failMsg + "\n1. license view panel [Expected]" + MSG_PANELHEADER_LICENSE
					 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void license_Basic() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, DashBoard.EXPIRED_ADM, DashBoard.ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_EMPTY)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//basic 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("BASIC")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
				
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)));
		
		//menu 확인
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)).getAttribute("value").contentEquals("BASIC") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)), "readonly")){
			failMsg = failMsg + "\n1. license type [Expected]BASIC [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)), "readonly");
		}
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DATE)).getAttribute("value").contentEquals("무약정 ") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DATE)), "readonly")){
			failMsg = failMsg + "\n2. license date [Expected]무약정 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DATE)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DATE)), "readonly");
		}
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).getAttribute("value").contentEquals("0원") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)), "readonly")){
			failMsg = failMsg + "\n3. license charge [Expected]0원 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)), "readonly");
		}
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DISCOUNT)).getAttribute("value").contentEquals("0원") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DISCOUNT)), "readonly")){
			failMsg = failMsg + "\n4. license discount [Expected]0원 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DISCOUNT)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_DISCOUNT)), "readonly");
		}
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_MINCHARGE)).getAttribute("value").contentEquals("200원") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_MINCHARGE)), "readonly")){
			failMsg = failMsg + "\n5. license charge per minute[Expected]200원 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_MINCHARGE)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_MINCHARGE)), "readonly");
		}
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_PAYOPTION)).getAttribute("value").contentEquals("매월 1일 신용카드 자동결제(후불)") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_PAYOPTION)), "readonly")){
			failMsg = failMsg + "\n6. license pay option [Expected]매월 1일 신용카드 자동결제(후불) [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_PAYOPTION)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_PAYOPTION)), "readonly");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = false)
	public void license_deferred() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, DashBoard.EXPIRED_ADM, DashBoard.ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_EMPTY)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//basic 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("BASIC")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
				
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)));
		
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void license_duplicated() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, DashBoard.ID_ADM, DashBoard.ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//basic 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("BASIC")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
				
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)));
		
		driver.findElement(By.xpath(XPATH_LICENSE_ADDLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_LICENSE_REGALREADY)) {
			failMsg = failMsg + "\n1. popup msg [Expected]" + MSG_LICENSE_REGALREADY 
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void license_FreeN() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//basic 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("BASIC")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
				
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_TYPE)));
		
		driver.findElement(By.xpath(XPATH_LICENSE_ADDLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_LICENSE_FREE_REMAIN)) {
			failMsg = failMsg + "\n1. popup msg [Expected]" + MSG_LICENSE_FREE_REMAIN 
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void license_AddlicenseID() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, DashBoard.EXPIRED_ADM, DashBoard.ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_EMPTY)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//user 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("User 요금제")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
				
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)));
		
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)).getAttribute("value").contentEquals("User 요금제")) {
			failMsg = failMsg + "\n1. license type [Expected]User 요금제 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)).getAttribute("value");
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String today = format1.format(cal.getTime());
		
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDDATE)).getAttribute("value").contentEquals(today)) {
			failMsg = failMsg + "\n1. license type [Expected]" + today + " [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDDATE)).getAttribute("value");
		}
		//수량 placeholder확인
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).getAttribute("placeholder").contentEquals("구매하려는 User 수량을 입력해주세요.")) {
			failMsg = failMsg + "\n1. license type [Expected]" + "구매하려는 User 수량을 입력해주세요." + " [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).getAttribute("placeholder");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, dependsOnMethods = {"license_AddlicenseID"}, alwaysRun = true, enabled = true)
	public void license_AddlicenseIDNoAgree() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		
		conn.loginAdmin(driver, DashBoard.EXPIRED_ADM, DashBoard.ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_EMPTY)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//user 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("User 요금제")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
				
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)));
		
		driver.findElement(By.xpath(XPATH_LICENSE_ADDLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_NO_AGREEMENT)) {
			failMsg = failMsg + "\n1. popup msg [Expeced]" + MSG_NO_AGREEMENT
					 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, dependsOnMethods = {"license_AddlicenseID"}, alwaysRun = true, enabled = true)
	public void license_AddlicenseIDMin() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_ADDLICENSE)) {
			//click 결제 관리
			Users user = new Users();
			user.selectSideMenu(driver, 3, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
			
			//user 요금제 자세히 보기 클릭
			List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
			
			if(list.size() > 0 ) {
				for (WebElement webElement : list) {
					if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("User 요금제")) {
						webElement.findElement(By.xpath("./td[7]/a")).click();
						break;
					}
					
				}
			}
			
			//신청 클릭
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
			driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)));
			
		}
		
		//ID 3개 입력(최소수량 4)
		driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).clear();
		driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).sendKeys("3");
		
		//click checkbox
		driver.findElement(By.xpath(XPATH_LICENSE_AGREE_CHECKBOX)).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath(XPATH_LICENSE_ADDLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_MINIMUN_ID)) {
			failMsg = failMsg + "\n1. popup msg [Expeced]" + MSG_MINIMUN_ID
					 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 13, dependsOnMethods = {"license_AddlicenseID"}, alwaysRun = true, enabled = true)
	public void license_AddlicenseIDCharge() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_ADDLICENSE)) {
			//click 결제 관리
			Users user = new Users();
			user.selectSideMenu(driver, 3, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
			
			//user 요금제 자세히 보기 클릭
			List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
			
			if(list.size() > 0 ) {
				for (WebElement webElement : list) {
					if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("User 요금제")) {
						webElement.findElement(By.xpath("./td[7]/a")).click();
						break;
					}
					
				}
			}
			
			//신청 클릭
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
			driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)));
			
		}
		
		//ID 0
		driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).clear();
		driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).sendKeys("0");
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPRICE)).getAttribute("value").contentEquals("0원")
				|| !driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPRICETOTAL)).getText().contentEquals("0원")) {
			failMsg = failMsg + "\n1. id price [Expected]0 [Actual]" + driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPRICE)).getAttribute("value");
		}
	
		//ID 0
		driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).clear();
		driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).sendKeys("3");
		Thread.sleep(1000);
		
		String price = driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPRICE)).getAttribute("value");
		String price2 = driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPRICETOTAL)).getText();
		price = price.replace(",", "");
		price = price.replace("원", "");
		price2 = price2.replace(",", "");
		price2 = price2.replace("원", "");
		if(Integer.valueOf(price) <= 0
				|| Integer.valueOf(price2) <= 0) {
			failMsg = failMsg + "\n1. id price [Expected]more than 0 [Actual]" + price;
		}

		//결제방법 확인
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPAYOPTION)).getAttribute("value").contentEquals("신용카드 결제(선불) ") 
				|| !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPAYOPTION)), "readonly")){
			failMsg = failMsg + "\n2. license pay option [Expected]신용카드 결제(선불)  [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPAYOPTION)).getAttribute("value")
					 + " input box is " + !findAttribute(driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDPAYOPTION)), "readonly");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	//기존수량 표시 업음
	@Test(priority = 20, enabled = false)
	public void license_IDtoID() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_LIST)));
		
		//user 요금제 자세히 보기 클릭
		List<WebElement> list = driver.findElements(By.xpath(XPATH_LICENSE_LIST));
		
		if(list.size() > 0 ) {
			for (WebElement webElement : list) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals("User 요금제")) {
					webElement.findElement(By.xpath("./td[7]/a")).click();
					break;
				}
			}
		}
		
		//신청 클릭
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)));
		driver.findElement(By.xpath(XPATH_LICENSE_APPLYLICENSE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)));
		
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)).getAttribute("value").contentEquals("User 요금제")) {
			failMsg = failMsg + "\n1. license type [Expected]User 요금제 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDTYPE)).getAttribute("value");
		}
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String today = format1.format(cal.getTime());
		
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDDATE)).getAttribute("value").contentEquals(today)) {
			failMsg = failMsg + "\n1. license type [Expected]" + today + " [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_IDDATE)).getAttribute("value");
		}
		//수량 placeholder확인 & 수정불가
		if(!driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).getAttribute("placeholder").contentEquals("구매하려는 User 수량을 입력해주세요.")) {
			failMsg = failMsg + "\n1. license type [Expected]" + "구매하려는 User 수량을 입력해주세요." + " [Actual]" 
					+ driver.findElement(By.xpath(XPATH_LICENSE_INPUTBOX_CHARGE)).getAttribute("placeholder");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public boolean findAttribute(WebElement wd, String value) {
		try {
			wd.getAttribute(value);
			return true;
		} catch (Exception e) {
			return false;
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


