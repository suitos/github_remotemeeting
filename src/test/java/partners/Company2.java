package partners;

import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/*
 * 1.파트너 로그인
 * 2.고객사 관리 화면 이동
 * 3.해당 고객사 검색 후 상세정보 진입
 * 4.고객 지원 정보 데이터 없을 경우 확인
 * 5.신규 등록 클릭 후 팝업 확인
 * 6.필수 입력 값 빈값 입력 후 저장
 * 7.작성자,문의사항 글자 수 초과 후 저장
 * 8.팝업 닫힘 확인
 * 9.고객 지원 등록 확인 
 * 10.수정 가능한지 확인 및 셀렉 박스 모두 선택 가능한지 확인
 * 11.파트너사 계정에서 보기 권한만 가능한지 확인
 * 12.삭제 확인
 */

public class Company2 {
	
	public static String AlertMsg = "정말 삭제하시겠습니까?";
	
	public static WebDriver driver;
	public static WebDriver Login_driver;

	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Login_driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
	
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", Login_driver);

	}

	@Test(priority = 1, enabled = true)
	public void loginCompany() throws Exception {
		String failMsg = "";

		driver.get(CommonValues_Partners.PARTNER_URL);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));

		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + driver.getCurrentUrl();
		}

		comm.login(driver, CommonValues_Partners.KO_PARTNER, CommonValues_Partners.PW);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 2, enabled = true)
	public void goCompany2() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

	}
	
	@Test(priority = 3, enabled = true)
	public void searchCompany2() throws Exception {
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("자동화테스트용.function");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		comm2.waitForLoad(driver);
		
		driver.findElement(By.xpath("//td[2]/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
	}
	
	@Test(priority = 4, enabled = true)
	public void RegistSupport_null() throws Exception {
		String failMsg = "";

		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td")).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = "Text is wrong when data is null [Expected] 데이터가 없습니다. [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void clickRegistSupportPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table add-customer-support']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		if(!driver.findElement(By.xpath("//form[@id='customerSupport']")).isDisplayed()) {
			failMsg = "RegistSupport Popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//label[@style='float: right;']")).getText().contentEquals("가입 방식 : 파트너사")) {
			failMsg = "Wrong text [Expected] 가입 방식 : 파트너사 [Actual]" + driver.findElement(By.xpath("//label[@style='float: right;']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void checkRegistSupportPopup_empty() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath("//span[@id='authorName-message']")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = "";
		}
		
		if(!driver.findElement(By.xpath("//span[@id='content-message']")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = "";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void checkRegistSupportPopup_max() throws Exception {
		String failMsg = "";
		
		for(int i = 0; i<6; i++) {
			driver.findElement(By.xpath("//input[@id='authorName']")).sendKeys("1234567890");
		}
		
		for(int i = 0; i<31; i++) {
			driver.findElement(By.xpath("//textarea[@id='content']")).sendKeys("1234567890");
		}
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath("//span[@id='authorName-message']")).getText().contentEquals("최대 50자 입력가능")) {
			failMsg = "";
		}
		
		if(!driver.findElement(By.xpath("//span[@id='content-message']")).getText().contentEquals("최대 300자 입력가능")) {
			failMsg = "";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void closeRegistSupportPopup() throws Exception {
		String failMsg = "";
	
		driver.findElement(By.xpath("//button[@class='close']")).click();
	
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
	
		if(!driver.findElements(By.xpath("//form[@id='customerSupport']")).isEmpty()) {
			failMsg = "RegistSupport Popup is still display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void saveRegistSupportPopup() throws Exception {
		String failMsg = "";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table add-customer-support']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		driver.findElement(By.xpath("//input[@id='authorName']")).sendKeys("test");
		driver.findElement(By.xpath("//textarea[@id='content']")).sendKeys("context test");
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);

		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[1]")), "내부"));

		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[1]")).getText().contentEquals("내부")) {
			failMsg = "Don't save data [Expected] 내부 [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[1]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText().contentEquals("계약")) {
			failMsg = failMsg + "\n2.Don't save data [Expected] 계약 [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).getText().contentEquals("context test")) {
			failMsg = failMsg + "\n3.Don't save data [Expected] context text [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[4]/div")).getText().contentEquals("test")) {
			failMsg = failMsg + "\n4.Don't save data [Expected] test [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[4]/div")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[5]")).getText().contentEquals(today)) {
			failMsg = failMsg + "\n5.Don't save data [Expected]" + today +"[Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[5]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//section[@id='wrapCustomerSupportList']//span[2]")).getAttribute("data-count").contentEquals("1")) {
			failMsg = failMsg + "\n6.Wrong Count [Expected]1 [Actual]" + driver.findElement(By.xpath("//section[@id='wrapCustomerSupportList']//span[2]")).getAttribute("data-count");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void updateRegistSupportPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		driver.findElement(By.xpath("//select[@id='inOut']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//select[@id='inOut']/option[2]")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//select[@id='supportType']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//select[@id='supportType']/option[2]")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//input[@id='authorName']")).sendKeys("test");
		driver.findElement(By.xpath("//textarea[@id='content']")).sendKeys("context test");
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[1]")), "외부"));

		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[1]")).getText().contentEquals("외부")) {
			failMsg = "Don't update data [Expected] 외부 [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[1]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText().contentEquals("장애")) {
			failMsg = failMsg + "\n2.Don't update data [Expected] 장애 [Actual]";
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).getText().contentEquals("context testcontext test")) {
			failMsg = failMsg + "\n3.Don't update data [Expected] context testcontext test [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[4]/div")).getText().contentEquals("testtest")) {
			failMsg = failMsg + "\n4.Don't update data [Expected] testtest [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[4]/div")).getText();
		}
		
		driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		driver.findElement(By.xpath("//select[@id='supportType']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//select[@id='supportType']/option[3]")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText().contentEquals("일반")) {
			failMsg = failMsg + "\n5.Don't update data [Expected] 일반 [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText();
		}
		
		driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		driver.findElement(By.xpath("//select[@id='supportType']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//select[@id='supportType']/option[4]")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText().contentEquals("결제")) {
			failMsg = failMsg + "\n6.Don't update data [Expected] 결제 [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText();
		}
		
		driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		driver.findElement(By.xpath("//select[@id='supportType']")).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//select[@id='supportType']/option[5]")).click();
		Thread.sleep(500);
		
		driver.findElement(By.xpath("//button[@id='customer-support-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText().contentEquals("기타")) {
			failMsg = failMsg + "\n7.Don't update data [Expected] 기타 [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[2]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void checkRegistSupportPopup_Partner() throws Exception {
		String failMsg = "";
		
		Login_driver.get(CommonValues_Partners.PARTNER_URL);

		WebDriverWait wait = new WebDriverWait(Login_driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));

		if (!Login_driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + driver.getCurrentUrl();
		}

		comm.login(Login_driver, CommonValues.PARTNERTEST_EMAIL, CommonValues.USERPW);

		Login_driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		Login_driver.findElement(By.xpath("//td[2]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		Login_driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		if(!Login_driver.findElement(By.xpath("//select[@id='inOut']")).getAttribute("disabled").contentEquals("true")) {
			failMsg = "Don't disabled";
		}
		
		if(!Login_driver.findElement(By.xpath("//select[@id='supportType']")).getAttribute("disabled").contentEquals("true")) {
			failMsg = failMsg + "\n2.Don't disabled";
		}
		
		if(!Login_driver.findElement(By.xpath("//input[@id='authorName']")).getAttribute("disabled").contentEquals("true")) {
			failMsg = failMsg + "\n3.Don't disabled";
		}
		
		if(!Login_driver.findElement(By.xpath("//textarea[@id='content']")).getAttribute("disabled").contentEquals("true")) {
			failMsg = failMsg + "\n4.Don't disabled";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void deleteRegistSupportPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td[3]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='customerSupport']")));
		
		driver.findElement(By.xpath("//button[@id='customer-support-delete']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		driver.findElement(By.xpath("//button[@id='customer-support-delete']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		driver.switchTo().alert().accept();
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		if(!driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td")).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = "Text is wrong when data is null [Expected] 데이터가 없습니다. [Actual]" + driver.findElement(By.xpath("//tbody[@id='handleTableCustomerSupportRow']//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		Login_driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
}
