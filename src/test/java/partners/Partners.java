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
 * 2.파트너사 관리 화면 이동
 * 3.검색 기본값 확인
 * 4.데이터 있는 문구 조건 검색 
 * 5.데이터 없는 문구 조건 검색 
 * 6.파트너사 목록 확인
 * 7.파트너사 등록 선택
 * 8.파트너사명 미입력 후 저장 
 * 9.상위 파트너사로 등록 확인
 * 10.상위 파트너사 선택 확인
 * 11.파트너사 로고 형식에 맞지 않은 파일 등록
 * 12.파트너사 로고 형식에 맞는 파일 등록
 * 13.파트너사 등록
 * 14.파트너사 상세정보로 이동
 * 15.파트너사 삭제
 */

public class Partners {
	
	public static String SEARCH_BTN = "//button[@type='submit']";
	
	public static String AlertMsg = "형식에 맞지 않는 파일을 선택하셨습니다.\n" + "(지원형식 : png, jpg, gif)";
	
	public String Code;
	
	private String PartnerName = "autotest-partner";
	
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
	public void goPartners() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.PARTNER_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("파트너사 관리")) {
			failMsg = "1.Wrong Menu [Expected] 파트너사 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void searchPartners_default() throws Exception {
		String failMsg = "";
		
		String result = driver.findElement(By.xpath("//span[@class='result-info']")).getText();
		
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		
		Thread.sleep(3000);
		comm2.waitForLoad(driver);
		
		if(!result.contentEquals(driver.findElement(By.xpath("//span[@class='result-info']")).getText())) {
			failMsg = "Wrong Search Result [Expected]" + result + " [Actual]" + driver.findElement(By.xpath("//span[@class='result-info']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void searchPartners_exist() throws Exception {
		
		searchdata("테스트");
		Thread.sleep(3000);
		checkData("테스트");

	}
	
	@Test(priority = 5, enabled = true)
	public void searchPartners_null() throws Exception {
		String failMsg = "";
		
		searchdata("qwe!23");
		
		Thread.sleep(3000);
		String result = driver.findElement(By.xpath("//td[1]")).getText();
		
		if(!result.contentEquals("데이터가 없습니다.")) {
			failMsg = "Wrong result [Expected] 데이터가 없습니다. [Actual]" + result;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void checksearchPartnersTitle() throws Exception {
		String failMsg = "";
		
		List<WebElement> th = driver.findElements(By.xpath("//tr[@role='row']/th"));
		
		if(!th.get(0).getText().contentEquals("파트너사명")) {
			failMsg = "Column title is wrong [Expected] 파트너사명 [Actual]" + th.get(0).getText();
		}
		
		if(!th.get(1).getText().contentEquals("상위 파트너사")) {
			failMsg = "Column title is wrong [Expected] 상위 파트너사 [Actual]" + th.get(1).getText();
		}
		
		if(!th.get(2).getText().contentEquals("전화번호")) {
			failMsg = "Column title is wrong [Expected] 전화번호 [Actual]" + th.get(2).getText();
		}
		
		if(!th.get(3).getText().contentEquals("등록일")) {
			failMsg = "Column title is wrong [Expected] 등록일 [Actual]" + th.get(3).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void goAddPartners() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/div")), "파트너사 관리 > 상세정보"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']/div")).getText().contentEquals("파트너사 관리 > 상세정보")) {
			failMsg = "1.Wrong Menu [Expected] 파트너사 관리 > 상세정보 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']/div")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNERADD_URI )) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNERADD_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void addPartners_empty() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='partner-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//span[3]")), "필수 입력란입니다."));
		
		if(!driver.findElement(By.xpath("//span[3]")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = "Error Msg is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void checkUpperPartner() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath("//input[@id='partnerName']")).getAttribute("value").contentEquals("rsupkor")) {
			failMsg = "Upper Partner is wrong [Expected] rsupkor [Actual]" + driver.findElement(By.xpath("//input[@id='partnerName']")).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void checkPartners() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='partner-tree-btn']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@role='treeitem']")));
		
		driver.findElement(By.xpath("//li/i[@role='presentation']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='jstree-anchor']")));
		
		List<WebElement> partnerlist = driver.findElements(By.xpath("//li[@role='treeitem']"));
		
		String partnerID = partnerlist.get(2).getAttribute("id");
		partnerlist.get(2).findElement(By.xpath(".//a")).click();
		
		wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//input[@id='partnerId']")), "value",partnerID));
		
		if(!driver.findElement(By.xpath("//input[@id='partnerId']")).getAttribute("value").contentEquals(partnerID)) {
			failMsg = "Can't select Partner";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void insertWrongfile() throws Exception {
		String failMsg = "";
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath("//input[@id='logoImage']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		System.out.println(alert_msg);
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void insertImagefile() throws Exception {
		String failMsg = "";
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[4];
		driver.findElement(By.xpath("//input[@id='logoImage']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='logo-contents']/img")));
		
		if(!driver.findElement(By.xpath("//div[@class='logo-contents']/img")).isDisplayed()) {
			failMsg = "Image is not insert";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void addPartners() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNERADD_URI);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/div")), "파트너사 관리 > 상세정보"));
		
		driver.findElement(By.xpath("//input[@id='companyName']")).sendKeys(PartnerName);
		
		driver.findElement(By.xpath("//button[@id='partner-save']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[3]/label")), "고객코드"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI + "/" + Code)) {
			failMsg = "Wrong Url [Expected]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI + "/" + Code + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void goPartnersInfo() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "파트너사 관리"));
		
		searchdata(PartnerName);
		Thread.sleep(3000);
		
		checkData(PartnerName);
		
		driver.findElement(By.xpath("//tr//a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[3]/label")), "고객코드"));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI + "/" + Code)) {
			failMsg = "Wrong Url [Expected]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PARTNER_URI + "/" + Code + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void deletePartners() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='partner-delete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		Thread.sleep(1000);
		alert.accept();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "파트너사 관리"));
		
		searchdata(PartnerName);
		
		Thread.sleep(3000);
		String result = driver.findElement(By.xpath("//td[1]")).getText();
		
		if(!result.contentEquals("데이터가 없습니다.")) {
			failMsg = "Wrong result [Expected] 데이터가 없습니다. [Actual]" + result;
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
		
	public void searchdata(String data) {
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys(data);
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		comm2.waitForLoad(driver);
		
	}
	
	public void checkData(String data) throws Exception {
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		for (int i = 0; i < list.size(); i++) {

			String result = list.get(i).findElement(By.xpath(".//td[1]")).getText();

			if (!result.contains(data)) {
				Exception e = new Exception("Wrong data [RowNum]" + i + "[Data]" + result);
				throw e;
			}
		}
	}
	
		

}
