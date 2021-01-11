package partners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

public class Home {
	
	public static String PARTNER_URL = "https://stpartners.remotemeeting.com";
	
	public static String LOGIN_URI = "/login";
	public static String LOGOUT_URI = "/login?logout";
	public static String DASHBOARD_URI = "/dashboard";
	public static String COMPANY_URI = "/company";
	public static String LICENSE_URI = "/license";
	public static String PAYMENT_URI = "/payment";
	public static String ANSWER_URI = "/answer";
	
	public static String DASHBOARD_BTN = "//li[@data-menu='dashboard']";
	public static String COMPANY_BTN = "//li[@data-menu='company']";
	public static String LICENSE_BTN = "//li[@data-menu='license']";
	
	public static String KO_PARTNER = "rsupkor@rsupport.com";
	public static String JA_PARTNER = "rsupjpn@rsupport.com";
	
	public static String PW = "111111";
	
	public static String AlertMsg = "최대 6개월까지 검색가능합니다.";
	
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
	/*
	@Test(priority = 1, enabled = true)
	public void selectLang() throws Exception {
		String failMsg = "";
		
		driver.get(PARTNER_URL+LOGIN_URI);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));
		
		if(!driver.findElement(By.xpath("//div[@class='language']")).isDisplayed()) {
			failMsg = "1.Language Box is not display";
		}
		
		driver.findElement(By.xpath("//div[@class='select-check up']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language-list show']")));
		
		if(!driver.findElement(By.xpath("//div[@class='language-list show']")).isDisplayed()) {
			failMsg = failMsg + "\n2.Language Select Box is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void selectKO() throws Exception {
		String failMsg = "";
		
		login(KO_PARTNER,PW);
		
		if(!driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class").contains("ko")) {
			failMsg = "Wrong Language [Expected]Korean [Actual]" + driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class");
		}
		
		logout();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void selectJA() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='select-check up']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language-list show']")));
		
		driver.findElement(By.xpath("//div[@class='language-list show']//li[2]")).click();
		
		login(KO_PARTNER,PW);
		
		if(!driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class").contains("ja")) {
			failMsg = "Wrong Language [Expected]Japan [Actual]" + driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class");
		}
		
		logout();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void selectEN() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='select-check up']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language-list show']")));
		
		driver.findElement(By.xpath("//div[@class='language-list show']//li[3]")).click();
		
		login(KO_PARTNER,PW);
		
		if(!driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class").contains("en")) {
			failMsg = "Wrong Language [Expected]English [Actual]" + driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class");
		}
		
		logout();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	*/
	@Test(priority = 5, enabled = true)
	public void loginKO() throws Exception {
		String failMsg = "";
		
		driver.get(PARTNER_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));
		
		if(!driver.getCurrentUrl().contentEquals(PARTNER_URL + LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + PARTNER_URL + LOGIN_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		login(KO_PARTNER,PW);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	/*
	@Test(priority = 6, enabled = true)
	public void Dashboard_goLicense() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//section[@class='wrap-notice']/div[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("라이선스 관리")) {
			failMsg = "1.Wrong Menu [Expected] 라이선스 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(PARTNER_URL + LICENSE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + PARTNER_URL + LICENSE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath("//button[@id='expiration']")).getAttribute("class").contains("active")) {
			failMsg = failMsg + "\n3.Wrong active Btn";
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.licenseStatus']/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n4.Wrong selected [Expected]정상 [Actual]"
							+ driver.findElement(By.xpath("//select[@id='search.licenseStatus']/option[2]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void Dashboard_goCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		driver.findElement(By.xpath("//section[@class='wrap-notice']/div[2]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("고객사 관리")) {
			failMsg = "1.Wrong Menu [Expected] 고객사 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(PARTNER_URL + COMPANY_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + PARTNER_URL + COMPANY_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.planType']/option[7]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong selected [Expected] 사용중 요금제 있음 [Actual]" 
							+ driver.findElement(By.xpath("//select[@id='search.planType']/option[7]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.unusedConferenceDayCondition']/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n4.Wrong selected [Expected] 최근 7일간 미사용 [Actual]"
							+ driver.findElement(By.xpath("//select[@id='search.unusedConferenceDayCondition']/option[2]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void Dashboard_goAnswer() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//section[@class='wrap-notice']/div[3]/div/span[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("답변 하기")) {
			failMsg = "1.Wrong Menu [Expected] 답변 하기 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(PARTNER_URL + ANSWER_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + PARTNER_URL + ANSWER_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.answer']/option[3]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong selected [Expected]미답변 [Actual]"
							+ driver.findElement(By.xpath("//select[@id='search.answer']/option[3]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 8, enabled = true)
	public void Dashboard_salesprice() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		if(!driver.findElement(By.xpath("//div[@id='sales-price']")).isDisplayed()) {
			failMsg = "Sales-price is not display";
		}
		
		int pricesum = 0;
		for(int i = 0; i < 3; i++) {
				
			List<WebElement> price = driver.findElements(By.xpath("//div[@class='item-price']"));
			String a = price.get(i).getText().replace(",", "");

			pricesum += Integer.parseInt(a);
			
		}
		
		if(Integer.parseInt(driver.findElement(By.xpath("//div[@class='total-price']")).getText().replace(",", "")) != pricesum) {
			failMsg = "Wrong price [Expected]" + driver.findElement(By.xpath("//div[@class='total-price']")).getText() + " [Actual]" + pricesum;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void Dashboard_goPayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='wrap-cards']/div[1]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("결제 관리")) {
			failMsg = "1.Wrong Menu [Expected] 결제 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(PARTNER_URL + PAYMENT_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + PARTNER_URL + PAYMENT_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void Dashboard_salesprice2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		if(!driver.findElement(By.xpath("//canvas[@id='new-license-doughtnut']")).isDisplayed()) {
			failMsg = "Right Chart is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void Dashboard_goLicense2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='wrap-cards']/div[2]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("라이선스 관리")) {
			failMsg = "1.Wrong Menu [Expected] 라이선스 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(PARTNER_URL + LICENSE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + PARTNER_URL + LICENSE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void Dashboard_salesprice3() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		if(!driver.findElement(By.xpath("//canvas[@id='yearly-sales-bar']")).isDisplayed()) {
			failMsg = "Bottom Chart is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	*/
	@Test(priority = 13, enabled = true)
	public void goCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(COMPANY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("고객사 관리")) {
			failMsg = "1.Wrong Menu [Expected] 고객사 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(PARTNER_URL + COMPANY_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + PARTNER_URL + COMPANY_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong selected [Expected]기간 [Actual]"
							+ driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//input[@id='startDate']")).getAttribute("disabled").contentEquals("true") ||
			!driver.findElement(By.xpath("//input[@id='endDate']")).getAttribute("disabled").contentEquals("true")) {
				failMsg = failMsg + "\n4.Date input is not disable";
			}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void searchCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//select[@id='date-condition']")).click();
		driver.findElement(By.xpath("//select[@id='date-condition']/option[2]")).click();
		
		driver.findElement(By.xpath("//input[@id='startDate']")).click();
		for(int i=0; i<6; i++ ) {
			driver.findElement(By.xpath("//div[@class='datepicker-days']//th[@class='prev']")).click();
		}
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String newday = Integer.toString(day);
		
		selectDate(newday);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		if(!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void searchCompany_leaved() throws Exception {
		
		driver.findElement(By.xpath(COMPANY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//button[@class='search-toggle-btn']")).click();
		driver.findElement(By.xpath("//select[@id='search.leaved']")).click();
		driver.findElement(By.xpath("//select[@id='search.leaved']/option[2]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		checkData(6,"탈퇴 처리중");
		
	}
	
	@Test(priority = 16, enabled = true)
	public void searchCompany_type() throws Exception {
		
		driver.findElement(By.xpath(COMPANY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//button[@class='search-toggle-btn']")).click();
		driver.findElement(By.xpath("//select[@id='search.issuedByType']")).click();
		driver.findElement(By.xpath("//select[@id='search.issuedByType']/option[2]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		checkData(3,"온라인");
		
	}
		
	
	public void selectDate(String date) {
		
	    WebElement eval = driver.findElement(By.xpath("//div[contains(@class,'datepicker-days')]/table/tbody"));
	    List<WebElement> alldates = eval.findElements(By.tagName("td"));
	    for(WebElement cell:alldates){
	         String day=cell.getText();
	            if (cell.getText().contentEquals(date)) {           
	                 driver.findElement(By.xpath("//div[contains(@class,'datepicker-days')]/table/tbody/tr/td[text()='"+day+"']")).click();
	                 break;
	                }
	            }
	 }
	
	public void checkData(int datanum, String data) throws Exception {
		if (driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr")).isEmpty()) {
			Exception e = new Exception("null data");
			throw e;

		} else {
			List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
			for (int i = 0; i < list.size(); i++) {
				String result = list.get(i).findElement(By.xpath(".//td[" + datanum + "]")).getText();
				if (!result.contentEquals(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + i + "[Data]" + result);
					throw e;
				}
			}
		}
	}
	
	
		

	
	
	/*
	@Test(priority = 30, enabled = true)
	public void logoutKO() throws Exception {
		String failMsg = "";
		
		logout();	
	}
	
	
	@Test(priority = 7, enabled = true)
	public void loginJA() throws Exception {
		String failMsg = "";
		
		login(JA_PARTNER,PW);
		
		logout();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	*/
		
	public void login(String ID, String PW) throws Exception {
		
		driver.findElement(By.xpath("//input[@id='j_username']")).sendKeys(ID);
		driver.findElement(By.xpath("//input[@id='j_password']")).sendKeys(PW);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		if(!driver.getCurrentUrl().contentEquals(PARTNER_URL + DASHBOARD_URI)) {
			Exception e = new Exception("Not Login");
			throw e;
		}
	}
	
	public void logout() throws Exception {
		driver.findElement(By.xpath("//div[@id='profile-info']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='wrap-more-profile']")));
		
		driver.findElement(By.xpath("//a[@class='btn-logout']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-box']")));
		
		if(!driver.getCurrentUrl().contentEquals(PARTNER_URL + LOGOUT_URI)) {
			Exception e = new Exception("Not Logout");
			throw e;
		}
	}
		

}
