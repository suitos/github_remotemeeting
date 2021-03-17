package partners;

import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

/*
 * 1.파트너 로그인
 * 2.메인 파트너사 설정 선택
 * 3.파트너사 등록 선택 후 파트너사 검색 팝업 확인
 * 4.파트너사 검색 팝업 내 닫기 선택
 * 5.파트너사 등록 선택 후 팝업 내 존재하지 않는 파트너사명 입력 후 검색
 * 6.두글자 입력 후 조건에 맞는 파트너사 검색되는지 확인
 * 7.파트너사 검색,선택 후 팝업 확인 및 지정 취소
 * 8.지정 확인 선택 후 팝업 확인 및 목록 추가 확인
 * 9.파트너사 드래그 후 순서 변경 취소
 * 10.파트너사 드래그 후 순서 변경 확인
 * 11.파트너사 추가된 상태로 지정 해제 취소 선택
 * 12.파트너사 추가된 상태로 지정 해제 확인 선택
 */

public class BranchPartners {
	
	public static String AlertMsg = "지정 하시겠습니까?";
	public static String AlertMsg2 = "설정이 완료되었습니다.";
	public static String AlertMsg3 = "순서 변경을 하시겠습니까?";
	public static String AlertMsg4 = "순서 변경이 완료되었습니다.";
	public static String AlertMsg5 = "설정을 해제 하시겠습니까?";
	public static String AlertMsg6 = "설정이 해제 되었습니다.";
	
	public static WebDriver driver;

	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
	}
	
	@Test(priority = 1, enabled = true)
	public void loginPartners() throws Exception {
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
	public void goBranchPartners() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.BRANCHPARTNER_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "메인 파트너사 설정"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("메인 파트너사 설정")) {
			failMsg = "1.Wrong Menu [Expected] 메인 파트너사 설정 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.BRANCHPARTNER_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.BRANCHPARTNER_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void checkBranchPartnersPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btnSearchCompany']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='popupSearchCompany']")));
		
		if(!driver.findElement(By.xpath("//div[@id='popupSearchCompany']")).isDisplayed()) {
			failMsg = "popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void checkBranchPartnersPopupClose() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-default btn-xs']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='popupSearchCompany']")));
		
		if(driver.findElement(By.xpath("//div[@id='popupSearchCompany']")).isDisplayed()) {
			failMsg = "popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void insertPartnerName_invalid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btnSearchCompany']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='popupSearchCompany']")));
		
		driver.findElement(By.xpath("//input[@id='searchName']")).sendKeys("xx");
		
		driver.findElement(By.xpath("//button[@id='btnSearch']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='companySearchResult']")));
		
		if(!driver.findElement(By.xpath("//div[@id='companySearchResult']//td")).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = "Data search is wrong [Expected] 데이터가 없습니다. [Actual]" + driver.findElement(By.xpath("//div[@id='companySearchResult']//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void insertPartnerName_valid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='searchName']")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//input[@id='searchName']")).sendKeys(Keys.BACK_SPACE); 
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@id='searchName']")).sendKeys("자동");
		
		driver.findElement(By.xpath("//button[@id='btnSearch']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBe(By.xpath("//div[@id='companySearchResult']//td/a"), "자동화테스트용"));

		if(!driver.findElement(By.xpath("//div[@id='companySearchResult']//a")).getText().contentEquals("자동화테스트용")) {
			failMsg = "Data search is wrong [Expected] 자동화테스트용 [Actual]" + driver.findElement(By.xpath("//div[@id='companySearchResult']//a")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority = 7, enabled = true)
	public void checkBranchPartnersAlertandClose() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='companySearchResult']//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if(comm.isAlertPresent(driver) == true) {
			failMsg = failMsg + "\n2.Alert is exist";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void appointPartners() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='companySearchResult']//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		String alert_msg = alert.getText();
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		System.out.println(alert_msg);
		comm2.waitForLoad(driver);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void dragPartnersAndcancel() throws Exception {
		String failMsg = "";
		
		String Topartner = driver.findElement(By.xpath("//tbody/tr[3]/td[1]/a")).getText();
		
		Actions act = new Actions(driver);					
		act.dragAndDrop(driver.findElement(By.xpath("//tr[3]")), driver.findElement(By.xpath("//tr[2]"))).build().perform();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		String Frompartner = driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText();
		
		if(!Topartner.contentEquals(Frompartner)) {
			failMsg = "Don't drag partner [Expected]" + Topartner + " [Actual]" + Frompartner;
		}
		
		driver.findElement(By.xpath("//button[@id='sortable-save-btn']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		if (!alert_msg.contentEquals(AlertMsg3)) {
			failMsg = failMsg + "\n2.Alert msg is wrong [Expected]" + AlertMsg3 + " [Actual]" + alert_msg;
		}
		
		driver.findElement(By.xpath(CommonValues_Partners.BRANCHPARTNER_BTN)).click();

		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "메인 파트너사 설정"));

		if(!driver.findElement(By.xpath("//tbody/tr[3]/td[1]/a")).getText().contentEquals(Topartner)) {
			failMsg = failMsg + "\n3.Don't cancel sort partner [Expected]" + Topartner + " [Actual]" + driver.findElement(By.xpath("//tbody/tr[3]/td[1]/a")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void dragPartnersAndsave() throws Exception {
		String failMsg = "";
		
		String Topartner = driver.findElement(By.xpath("//tbody/tr[3]/td[1]/a")).getText();
		
		Actions act = new Actions(driver);					
		act.dragAndDrop(driver.findElement(By.xpath("//tr[3]")), driver.findElement(By.xpath("//tr[2]"))).build().perform();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		String Frompartner = driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText();
		
		if(!Topartner.contentEquals(Frompartner)) {
			failMsg = "Don't drag partner [Expected]" + Topartner + " [Actual]" + Frompartner;
		}
		
		driver.findElement(By.xpath("//button[@id='sortable-save-btn']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		String alert_msg = alert.getText();
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg4)) {
			failMsg = failMsg + "\n2.Alert msg is wrong [Expected]" + AlertMsg4 + " [Actual]" + alert_msg;
		}
		
		driver.findElement(By.xpath(CommonValues_Partners.BRANCHPARTNER_BTN)).click();

		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "메인 파트너사 설정"));

		if(!driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText().contentEquals(Topartner)) {
			failMsg = failMsg + "\n3.Don't save sort partner [Expected]" + Topartner + " [Actual]" + driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void cancelDeleteAppointPartners() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tr[2]//button[@class='btn btn-table btn-danger btn-delete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		if (!alert_msg.contentEquals(AlertMsg5)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg5 + " [Actual]" + alert_msg;
		}
		
		if(!driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText().contentEquals("자동화테스트용")) {
			failMsg = failMsg + "\n2.Don't cancel deleted partner [Expected] 자동화테스트용 [Actual]" + driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void deleteAppointPartners() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tr[2]//button[@class='btn btn-table btn-danger btn-delete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		String alert_msg = alert.getText();
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg6)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg6 + " [Actual]" + alert_msg;
		}
		
		comm2.waitForLoad(driver);
		
		List <WebElement> list = driver.findElements(By.xpath("//tbody/tr"));
		
		if(list.size() != 2) {
			failMsg = failMsg + "\n2.Don't deleted list [Expected size] 2 [Actual Size]" + list.size();
		}
		
		if(driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText().contentEquals("자동화테스트용")) {
			failMsg = failMsg + "\n2.Don't deleted partner [Actual]" + driver.findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText();
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
	
}
