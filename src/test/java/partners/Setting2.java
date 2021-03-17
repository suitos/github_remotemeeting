package partners;

import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.Alert;
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
 * 1.파트너 로그인
 * 2.영업 관리 화면 이동
 * 3.검색 인풋박스 placeholder 확인
 * 4.텍스트 입력 후 텍스트 노출 확인(검색 후 value값으로 확인)
 * 5.검색 결과 null일 경우 문구 확인
 * 6.고객사명 조건 검색
 * 7.고객 코드 조건 검색
 * 8.이메일 조건 검색
 * 9.리스트 사이즈 최대 30개 확인 및 페이징 확인
 * 10.
 * 11.
 * 12.발송 실패 이메일 관리 이동
 * 13.이메일 저장 및 삭제 확인
 */

public class Setting2 {
	
	public static WebDriver driver;
	
	private static String Code = "2c908a917749a5ff0177657b204600e2";
	private static String Email = "rmrsup6@gmail.com";
	
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
	public void goSetting2() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.SALESLIST_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "영업 관리"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("영업 관리")) {
			failMsg = "1.Wrong Menu [Expected] 영업 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.SALESLIST_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.SALESLIST_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void checkPlaceholder() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("placeholder").contentEquals("검색어를 입력하세요.")) {
			failMsg = "Placeholder is wrong [Expected] 검색어를 입력하세요. [Actual]" + driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("placeholder");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void checkValue() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("qwerty123");
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//tbody[@id='salesListWrapper']//td")), "데이터가 없습니다."));

		if(!driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value").contentEquals("qwerty123")) {
			failMsg = "Value is wrong [Expected] qwerty123 [Actual]" + driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void checkNullData() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath("//tbody[@id='salesListWrapper']//td")).getText().contentEquals("데이터가 없습니다.")) {
			failMsg = "Null data is wrong [Expected]데이터가 없습니다. [Actual]" + driver.findElement(By.xpath("//tbody[@id='salesListWrapper']//td")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void searchSetting2_Companyname() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.SALESLIST_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "영업 관리"));

		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='salesListWrapper']/tr"));
		
		String Companyname = list.get(0).findElement(By.xpath(".//td[1]")).getText().toLowerCase();
		
		searchData(1,Companyname);
		
		checkData(Companyname);
	}
	
	@Test(priority = 7, enabled = true)
	public void searchSetting2_Code() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.SALESLIST_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "영업 관리"));

		searchData(2,Code);
		
		checkData("자동화테스트용.function");
		
	}
	
	@Test(priority = 8, enabled = true)
	public void searchSetting2_Email() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.SALESLIST_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "영업 관리"));

		searchData(3,Email);
		
		checkData("자동화테스트용.function");
		
	}
	
	@Test(priority = 9, enabled = true)
	public void checkListsizeAndPagingUI2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.SALESLIST_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "영업 관리"));

		List <WebElement> list = driver.findElements(By.xpath("//tbody[@id='salesListWrapper']/tr"));
		
		if(list.size() != 30) {
			failMsg = "size is wrong [Expected] 30 [Actual]" + list.size();
		}
		
		if(!driver.findElement(By.xpath("//div[@class='pagination']")).isDisplayed()) {
			failMsg = failMsg + "paging UI is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void goSetting3() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.INVALIDEMAIL_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "발송 실패 이메일 관리"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("발송 실패 이메일 관리")) {
			failMsg = "1.Wrong Menu [Expected] 발송 실패 이메일 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.INVALIDEMAIL_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.INVALIDEMAIL_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void saveEmail() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new invalid-email']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='validEmail']")));
		
		driver.findElement(By.xpath("//input[@id='valid-email']")).sendKeys(CommonValues.USERS[0]);
		
		driver.findElement(By.xpath("//button[@id='valid-email-save']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='validEmail']")));
		comm2.waitForLoad(driver);
		
		List <WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(!list.get(0).findElement(By.xpath(".//td[1]")).getText().contentEquals(CommonValues.USERS[0])) {
			failMsg = "Don't save Email";
		}
		
		list.get(0).findElement(By.xpath(".//td[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='validEmail']")));
		
		driver.findElement(By.xpath("//button[@id='valid-email-delete']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		driver.switchTo().alert().accept();;
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='validEmail']")));
		comm2.waitForLoad(driver);
		
		list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.get(0).findElement(By.xpath(".//td[1]")).getText().contentEquals(CommonValues.USERS[0])) {
			failMsg = "Don't delete Email";
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

	private void searchData(int i, String data) {
		//1=고객사명, 2=고객코드, 3=이메일
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']")).click();
		
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']/option[" + i + "]")).click();
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys(data);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		comm2.waitForLoad(driver);
	}
	
	private void checkData(String data) throws Exception {
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='salesListWrapper']/tr"));
		
		for(int i = 0; i<list.size(); i++) {
			String result = list.get(i).findElement(By.xpath(".//td[1]")).getText().toLowerCase();
			
			if(!result.contains(data)) {
				Exception e = new Exception("Wrong result [Expcted]" + data + " [Actual]" + result);
				throw e;
			}
		}
	}
	
	
}
