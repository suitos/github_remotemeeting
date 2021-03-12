package partners;

import static org.testng.Assert.fail;

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

import mandatory.CommonValues;

/*
 * 1.언어 선택 창 노출 확인
 * 2.한국어 선택
 * 3.일본어 선택
 * 4.영어 선택
 * 5.로그인 페이지 이동 확인
 * 6.라이선스 만료 예정 선택 후 화면 이동 및 검색 조건 확인
 * 7.미사용 고객사 선택 후 화면 이동 및 검색 조건 확인
 * 8.답변 대기중 문의 선택 후 리스트 및 화면 이동 확인
 * 9.매출 UI 확인
 * 10.결제관리 선택 후 화면 이동 확인
 * 11.라이선스 등록수 차트 DISPLAY
 * 12.라이선스 관리 선택 후 화면 이동 확인
 * 13.월단위 매출 현황 차트 DISPLAY
 * 14.로그아웃
 */

public class Home {
	
	public static WebDriver driver;
	
	CommonValues_Partners comm = new CommonValues_Partners();

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
	public void selectLang() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL+CommonValues_Partners.LOGIN_URI);
		
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
		
		comm.login(driver,CommonValues_Partners.KO_PARTNER,CommonValues_Partners.PW);
		
		if(!driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class").contains("ko")) {
			failMsg = "Wrong Language [Expected]Korean [Actual]" + driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class");
		}
		
		comm.logout(driver);
		
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
		
		comm.login(driver,CommonValues_Partners.KO_PARTNER,CommonValues_Partners.PW);
		
		if(!driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class").contains("ja")) {
			failMsg = "Wrong Language [Expected]Japan [Actual]" + driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class");
		}
		
		comm.logout(driver);
		
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
		
		comm.login(driver, CommonValues_Partners.KO_PARTNER, CommonValues_Partners.PW);
		
		if(!driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class").contains("en")) {
			failMsg = "Wrong Language [Expected]English [Actual]" + driver.findElement(By.xpath("//body[@id='partnerAdmin']")).getAttribute("class");
		}
		
		comm.logout(driver);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void loginKO() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		comm.login(driver, CommonValues_Partners.KO_PARTNER, CommonValues_Partners.PW);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void Dashboard_goLicense() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//section[@class='wrap-notice']/div[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("라이선스 관리")) {
			failMsg = "1.Wrong Menu [Expected] 라이선스 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI + " [Actual]" + driver.getCurrentUrl();
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
		
		driver.findElement(By.xpath(CommonValues_Partners.DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		driver.findElement(By.xpath("//section[@class='wrap-notice']/div[2]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("고객사 관리")) {
			failMsg = "1.Wrong Menu [Expected] 고객사 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + " [Actual]" + driver.getCurrentUrl();
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
		
		driver.findElement(By.xpath(CommonValues_Partners.DASHBOARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//section[@class='wrap-notice']/div[3]/div/span[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("답변 하기")) {
			failMsg = "1.Wrong Menu [Expected] 답변 하기 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI + " [Actual]" + driver.getCurrentUrl();
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
	
	@Test(priority = 9, enabled = true)
	public void Dashboard_salesprice() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.DASHBOARD_BTN)).click();
		
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
	
	@Test(priority = 10, enabled = true)
	public void Dashboard_goPayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='wrap-cards']/div[1]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("결제 관리")) {
			failMsg = "1.Wrong Menu [Expected] 결제 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void Dashboard_salesprice2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.DASHBOARD_BTN)).click();
		
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
	
	@Test(priority = 12, enabled = true)
	public void Dashboard_goLicense2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='wrap-cards']/div[2]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("라이선스 관리")) {
			failMsg = "1.Wrong Menu [Expected] 라이선스 관리 [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void Dashboard_salesprice3() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.DASHBOARD_BTN)).click();
		
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

	
	@Test(priority = 14, enabled = true)
	public void logoutKO() throws Exception {
		
		comm.logout(driver);	
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
