package partners;

import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * 1.파트너 로그인
 * 2.운영 관리 화면 이동
 * 3.일부 단어 입력 후 검색 시 해당 단어 포함된 전체 데이터 노출 확인
 * 4.공백 입력 후 검색 시 전체 데이터 노출 확인
 * 5.미등록된 단어 입력 후 검색 시 데이터 미노출 확인
 * 6.신규 등록 버튼 클릭 시 팝업 확인
 * 7.빈값 입력 시 메세지 확인
 * 8.중복된 값 입력 시 메세지 확인
 * 9.20자 넘는 경우 작성 불가 확인
 * 10.닫기 선택 시 닫힘 확인
 * 11.저장 시 최상단 노출 확인
 * 12.리스트 사이즈 최대 15개 확인 및 페이징 확인
 * 13.URI상세 팝업 확인
 * 14.빈값 입력  시 메세지 확인
 * 15.중복된 값 입력 시 메세지 확인
 * 16.저장 시 팝업 닫힘 확인
 * 17.저장한 단어 삭제하여 원복
 */

public class Setting {
	
	public static String AlertMsg = "정말 삭제하시겠습니까?";
	
	public static WebDriver driver;
	
	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		driver = comm2.setDriver(driver, browsertype, "lang=ko_KR", true);
		
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
	public void goSetting() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.BANWORD_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "URI 제외 단어 관리"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("URI 제외 단어 관리")) {
			failMsg = "1.Wrong Menu [Expected] URI 제외 단어 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.BANWORD_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.BANWORD_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void searchBanword() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("shit");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		String result = driver.findElement(By.xpath("//span[@class='result-info']")).getText().replaceAll("[^0-9]", "");
		int int_result = Integer.parseInt(result);
		
		List <WebElement> shitlist = driver.findElements(By.xpath("//td[contains(text(),'shit')]"));
		
		if(shitlist.size() != int_result) {
			failMsg = "Search is wrong [Expected size]" + shitlist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void searchBanword_empty() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.BANWORD_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "URI 제외 단어 관리"));
		
		String result = driver.findElement(By.xpath("//span[@class='result-info']")).getText().replaceAll("[^0-9]", "");
		int int_result = Integer.parseInt(result);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		String search_result = driver.findElement(By.xpath("//span[@class='result-info']")).getText().replaceAll("[^0-9]", "");
		int int_search_result = Integer.parseInt(search_result);
		
		if(int_result != int_search_result) {
			failMsg = "Search function is wrong when insert empty value [Expected]" + int_result + " [Actual]" + int_search_result;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void searchBanword_invalid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("shittestqa");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		String search_result = driver.findElement(By.xpath("//span[@class='result-info']")).getText().replaceAll("[^0-9]", "");
		int int_search_result = Integer.parseInt(search_result);
		
		if(int_search_result != 0) {
			failMsg = "Search data is wrong [Expected] 0 [Actual]" + int_search_result;
		}
		
		if(!driver.findElement(By.xpath("//tbody[@id='companyListWrapper']//td")).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = failMsg + "\n2.Search data is wrong [Expected] 데이터가 없습니다. [Actual]" + driver.findElement(By.xpath("//tbody[@id='companyListWrapper']//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void checkAddBanwordPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new ban-uri']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));

		if(!driver.findElement(By.xpath("//div[@id='popupBanUri']")).isDisplayed()) {
			failMsg = "Add Banword Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void addBanword_empty() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='ban-uri-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error-msg']")));

		if(!driver.findElement(By.xpath("//span[@class='error-msg']")).getText().contentEquals("URI에 제외 할 단어를 입력해주세요.")) {
			failMsg = "Error MSG is wrong [Expected] URI에 제외 할 단어를 입력해주세요. [Actual]" + driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void addBanword_duplicate() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys("shit");
		
		driver.findElement(By.xpath("//button[@id='ban-uri-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error-msg']")));
		
		if(!driver.findElement(By.xpath("//span[@class='error-msg']")).getText().contentEquals("이미 등록된 단어 입니다.")) {
			failMsg = "Error MSG is wrong [Expected] 이미 등록된 단어 입니다. [Actual]" + driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void addBanword_over20value() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys(Keys.BACK_SPACE); 
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys("123456789012345678901234567890");
		
		if(driver.findElement(By.xpath("//input[@id='ban-uri-input']")).getAttribute("data-valid").contentEquals("123456789012345678901234567890")) {
			failMsg = "Over 20 value [Expected] 12345678901234567890 [Actual]" + driver.findElement(By.xpath("//input[@id='ban-uri-input']")).getAttribute("data-valid");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void closeAddBanwordPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='cola-admin-close']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));

		if(driver.findElement(By.xpath("//div[@id='popupBanUri']")).isDisplayed()) {
			failMsg = "Add Banword Popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void saveBanword() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.BANWORD_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "URI 제외 단어 관리"));
		
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new ban-uri']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));

		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys("qatest");
		
		driver.findElement(By.xpath("//button[@id='ban-uri-save']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));

		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		List <WebElement> banwordlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(!banwordlist.get(0).findElement(By.xpath(".//td")).getText().contentEquals("qatest")) {
			failMsg = "Wrong saved Banword [Expected] qatest [Actual]" + banwordlist.get(0).findElement(By.xpath(".//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void checkListsizeAndPagingUI() throws Exception {
		String failMsg = "";
		
		List <WebElement> banwordlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(banwordlist.size() != 15) {
			failMsg = "size is wrong [Expected] 15 [Actual]" + banwordlist.size();
		}
		
		if(!driver.findElement(By.xpath("//div[@class='pagination']")).isDisplayed()) {
			failMsg = failMsg + "paging UI is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void checkInfoBanwordPopup() throws Exception {
		String failMsg = "";
		
		List <WebElement> banwordlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		banwordlist.get(0).findElement(By.xpath(".//td")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));

		if(!driver.findElement(By.xpath("//div[@id='popupBanUri']")).isDisplayed()) {
			failMsg = "Info Banword Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority = 14, enabled = true)
	public void InfoBanword_empty() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys(Keys.BACK_SPACE); 
		
		driver.findElement(By.xpath("//button[@id='ban-uri-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error-msg']")));

		if(!driver.findElement(By.xpath("//span[@class='error-msg']")).getText().contentEquals("URI에 제외 할 단어를 입력해주세요.")) {
			failMsg = "Error MSG is wrong [Expected] URI에 제외 할 단어를 입력해주세요. [Actual]" + driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void InfoBanword_duplicate() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys("shit");
		
		driver.findElement(By.xpath("//button[@id='ban-uri-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error-msg']")));
		
		if(!driver.findElement(By.xpath("//span[@class='error-msg']")).getText().contentEquals("이미 등록된 단어 입니다.")) {
			failMsg = "Error MSG is wrong [Expected] 이미 등록된 단어 입니다. [Actual]" + driver.findElement(By.xpath("//span[@class='error-msg']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void saveBanword2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys(Keys.BACK_SPACE); 
		
		driver.findElement(By.xpath("//input[@id='ban-uri-input']")).sendKeys("qatestreset");
		
		driver.findElement(By.xpath("//button[@id='ban-uri-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));

		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		List <WebElement> banwordlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(!banwordlist.get(0).findElement(By.xpath(".//td")).getText().contentEquals("qatestreset")) {
			failMsg = "Wrong saved Banword [Expected] qatest [Actual]" + banwordlist.get(0).findElement(By.xpath(".//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void deleteBanword() throws Exception {
		String failMsg = "";
		
		List <WebElement> banwordlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		banwordlist.get(0).findElement(By.xpath(".//td")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));
		
		driver.findElement(By.xpath("//button[@id='ban-uri-delete']")).click();

		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		System.out.println(alert_msg);
		alert.dismiss();
		
		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if(!driver.findElement(By.xpath("//div[@id='popupBanUri']")).isDisplayed()) {
			failMsg = "Info Banword Popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@id='ban-uri-delete']")).click();

		wait.until(ExpectedConditions.alertIsPresent());
		
		alert = driver.switchTo().alert();
		alert.accept();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='popupBanUri']")));
		comm2.waitForLoad(driver);
		
		if(!driver.findElements(By.xpath("//td[contains(text(),'qatestreset')]")).isEmpty()) {
			failMsg = "Don't deleted banword";
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
