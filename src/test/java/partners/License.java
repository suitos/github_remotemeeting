package partners;

import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

public class License {
	
	public static String ID_PREPAYMENT_BTN = "//button[@id='id-prepayment']";
	public static String CONFERENCE_PREPAYMENT_BTN = "//button[@id='conference-prepayment']";
	public static String WEIGHTED_BTN = "//button[@id='weighted-system']";
	public static String EXPIRATION_BTN = "//button[@id='expiration']";
	public static String NORMAL_BTN = "//button[@id='normal']";
	public static String ALL_BTN = "//button[@id='all']";
	
	public static String AlertMsg = "사용 승인을 처리하시겠습니까?\n" + "사용 승인 처리를 하면 라이선스가 활성화됩니다.";
	public static String AlertMsg2 = "형식에 맞지 않습니다. 확인 후 다시 입력해 주세요.";
	public static String AlertMsg3 = "수량을  확인 후 다시 입력해 주세요.";
	public static String AlertMsg4 = "결제 정보를 삭제하시겠습니까?";
	public static String AlertMsg5 = "결제 추가를 취소하시겠습니까?";
	
	public static WebDriver driver;
	public static WebDriver Partner_driver;
	
	private static String name = "license test";
	
	public String Code;
	public String Companyname;
	
	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Partner_driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver", Partner_driver);
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
	public void goLicense() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("라이선스 관리")) {
			failMsg = "1.Wrong Menu [Expected] 라이선스 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void search_IDprepayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(ID_PREPAYMENT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));

		if(!driver.findElement(By.xpath("//option[@value='I']")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='I']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//option[@value='3']")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='3']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void search_Conferenceprepayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CONFERENCE_PREPAYMENT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));

		if(!driver.findElement(By.xpath("//option[@value='R']")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='R']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//option[@value='3']")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='3']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void search_Weighted() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(WEIGHTED_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));

		if(!driver.findElement(By.xpath("//option[@value='W']")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='W']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//option[@value='3']")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='3']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void search_Expiration() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(EXPIRATION_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));
		
		driver.findElement(By.xpath("//input[@id='startDate']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		String st_today = driver.findElement(By.xpath("//td[contains(@class, 'active day')]")).getText();
		String st_today2 = driver.findElement(By.xpath("//div[1]//th[contains(@class, 'datepicker-switch')]")).getText();
		
		String startdate = st_today + " " + st_today2;
		System.out.println(startdate);
		
		String pattern = "d MMMMM yyyy";
		SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		System.out.println(today);
		
		if(!startdate.contentEquals(today)) {
			failMsg = "Start date is wrong [Expected]" + today + " [Actual]" + startdate;
		}
		
		driver.findElement(By.xpath("//input[@id='endDate']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		String end_today = driver.findElement(By.xpath("//td[contains(@class, 'active day')]")).getText();
		String end_today2 = driver.findElement(By.xpath("//div[1]//th[contains(@class, 'datepicker-switch')]")).getText();
		
		String enddate = end_today + " " + end_today2;
		System.out.println(enddate);
		
		String addDay = AddDate(today, 0, 0, 6);
		System.out.println(addDay);
		
		if(!enddate.contentEquals(addDay)) {
			failMsg = failMsg + "\n2.End date is Wrong [Expected]" + addDay + " [Actual]" + enddate;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void search_Normal() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(NORMAL_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));

		if(!driver.findElement(By.xpath("//option[@value='ALL']")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='ALL']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//option[@value='1']")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='1']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void search_All() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(ALL_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row']")));

		if(!driver.findElement(By.xpath("//option[@value='ALL']")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//option[@value='ALL']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void searchLicense_Code() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@class='btn btn-shortcut rapid-search-btn active']")));

		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.get(0).findElement(By.xpath(".//td")).getText().contentEquals("데이터가 없습니다.")) {
			driver.findElement(By.xpath(ALL_BTN)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row']")));
			
			list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
	
		}
		
		list.get(0).findElement(By.xpath(".//td[2]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		Companyname = driver.findElement(By.xpath("//input[@id='companyName']")).getAttribute("value");
		System.out.println(Code);
		System.out.println(Companyname);
		
		driver.navigate().back();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		searchdata(1, Code);
		Thread.sleep(1000);
		checkData(1, Code);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 10, enabled = true)
	public void searchLicense_Companyname() throws Exception {
		
		searchdata(2, Companyname);
		Thread.sleep(1000);
		checkData(2, Companyname);
	}
	
	@Test(priority = 11, enabled = true)
	public void searchLicense_Email() throws Exception {
		
		searchdata(3, mandatory.CommonValues.ADMEMAIL);
		Thread.sleep(1000);
		checkData(3, mandatory.CommonValues.ADMEMAIL);
	}
	
	@Test(priority = 12, enabled = true)
	public void excel() throws Exception {
		
		
	}
	
	@Test(priority= 13, enabled = true)
	public void pagingLicense() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));

		driver.findElement(By.xpath(ALL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row']")));

		List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		if (rows.size() != 30) {
			failMsg = "list rows [Expected]30 [Actual]" + rows.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void checkApproveBtn_Partner() throws Exception {
		String failMsg = "";
		
		Partner_driver.get(CommonValues_Partners.PARTNER_URL);
		
		WebDriverWait wait = new WebDriverWait(Partner_driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));

		if (!Partner_driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + Partner_driver.getCurrentUrl();
		}

		comm.login(Partner_driver, mandatory.CommonValues.PARTNERTEST_EMAIL, mandatory.CommonValues.USERPW);

		createLicense(Partner_driver);
		
		Partner_driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		wait.until(ExpectedConditions.textToBePresentInElement(Partner_driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		if(!Partner_driver.findElements(By.xpath("//button[@class='btn btn-table btn-success approve']")).isEmpty()) {
			failMsg = failMsg + "\n2.Approve btn is display in Partner account";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void checkApproveBtn_Branch() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		WebElement list = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]"));
		
		if(list.findElements(By.xpath("//button[@class='btn btn-table btn-success approve']")).isEmpty()) {
			failMsg = "Approve btn is not display in Branch account";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void clickFreetrialLicenseLink() throws Exception {
		String failMsg = "";
		
		WebElement Freetriallistlink = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[2]/td[4]/a"));
		
		Freetriallistlink.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//form[@id='license']//span")), "라이선스"));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']/div")).getText().contentEquals("라이선스 관리 > 상세정보")) {
			failMsg = "Don't go license info page when click List Link [Expected Contains]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI + " [Actual]" + driver.getCurrentUrl(); ;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void displayDatepicker() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='endDate']")));
		
		driver.findElement(By.xpath("//input[@id='startDate']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		if(!driver.findElement(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")).isDisplayed()) {
			failMsg = "startDate Datepicker is not display";
		}
		
		driver.findElement(By.xpath("//input[@id='endDate']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		if(!driver.findElement(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")).isDisplayed()) {
			failMsg = failMsg + "endDate Datepicker is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void wrongEnddate() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='endDate']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		driver.findElement(By.xpath("//div[1]//thead/tr[1]/th[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//tr[1]/td[(text()='1')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		Thread.sleep(1000);
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19, enabled = true)
	public void changeDate() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		WebElement list = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[2]"));
		
		String stdt = list.findElement(By.xpath(".//td[6]")).getText();
		String eddt = list.findElement(By.xpath(".//td[7]")).getText();
		
		WebElement Freetriallistlink = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[2]/td[4]/a"));
		
		Freetriallistlink.click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//form[@id='license']//span")), "라이선스"));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='endDate']")));
		
		driver.findElement(By.xpath("//input[@id='startDate']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		driver.findElement(By.xpath("//div[1]//thead/tr[1]/th[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//tr[1]/td[(text()='1')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@id='endDate']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		driver.findElement(By.xpath("//div[1]//thead/tr[1]/th[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//td[(text()='10')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		Thread.sleep(1000);
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		list = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[2]"));
		
		String stdt2 = list.findElement(By.xpath(".//td[6]")).getText();
		String eddt2 = list.findElement(By.xpath(".//td[7]")).getText();
		System.out.println(stdt);
		System.out.println(stdt2);
		System.out.println(eddt);
		System.out.println(eddt2);
		
		if(stdt.contentEquals(stdt2) || eddt.contentEquals(eddt2)) {
			failMsg = "Date don't update";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void clickUserPlanLicenseLink() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		WebElement list = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]"));
		
		list.findElement(By.xpath(".//td[4]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//form[@id='license']//span")), "라이선스"));
		
		if(!driver.findElement(By.xpath("//div[@class='panel-header']/div")).getText().contentEquals("라이선스 관리 > 상세정보")) {
			failMsg = "Don't go license info page when click List Link [Expected Contains]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LICENSE_URI + " [Actual]" + driver.getCurrentUrl(); ;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 21, enabled = true)
	public void wrongUseramount() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='amount']")).sendKeys("!!!");
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 22, enabled = true)
	public void wrongUseramount2() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='amount']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.CONTROL, "a");
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='amount']")).sendKeys("0");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg3)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg3 + " [Actual]" + alert_msg;
		}
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		if(!driver.findElement(By.xpath("//input[@id='amount']")).getAttribute("value").contentEquals("1")) {
			failMsg = "Amount value is wrong [Expected] 1 [Actual]" + driver.findElement(By.xpath("//input[@id='amount']")).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, enabled = true)
	public void clickPaymentadd() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table payment-add']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[1]//button[2]")));
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		List<WebElement> addlist = driver.findElements(By.xpath("//tbody/tr"));
		
		if(addlist.size() != 2) {
			failMsg = "List is not added [Expected] 2 [Actual]" + addlist.size();
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		System.out.println(today);
		
		if(!driver.findElement(By.xpath("//td[3]/input[@class='payment-update-datepicker']")).getAttribute("value").contentEquals(today)) {
			failMsg = failMsg + "\n3.Default Startdate Value is wrong [Expected]" + today + " [Actual]" + driver.findElement(By.xpath("//td[3]/input[@class='payment-update-datepicker']")).getAttribute("value");
		}
		
		if(!driver.findElement(By.xpath("//td[4]/input[@class='payment-update-datepicker']")).getAttribute("value").contentEquals(today)) {
			failMsg = failMsg + "\n4.Default Enddate Value is wrong [Expected]" + today + " [Actual]" + driver.findElement(By.xpath("//td[4]/input[@class='payment-update-datepicker']")).getAttribute("value");
		}
		
		if(!driver.findElement(By.xpath("//input[@class='payment-update-pay']")).getAttribute("value").contentEquals("")) {
			failMsg = failMsg + "\n5. Default Pay Value is wrong [Expected] null [Actual]" + driver.findElement(By.xpath("//input[@class='payment-update-pay']")).getAttribute("value");
		}
		
		if(!driver.findElement(By.xpath("//select[@class='payment-status']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n5. Default option is wrong";
		}
		
		if(!driver.findElement(By.xpath("//tr[1]//button[1]")).getText().contentEquals("저장하기")) {
			failMsg = failMsg + "\n6.Button is wrong [Expected] 저장하기 [Actual]" + driver.findElement(By.xpath("//tr[1]//button[1]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//tr[1]//button[2]")).getText().contentEquals("실행 취소")) {
			failMsg = failMsg + "\n7.Button is wrong [Expected] 실행 취소 [Actual]" + driver.findElement(By.xpath("//tr[1]//button[2]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 24, enabled = true)
	public void cancelPaymentadd() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tr[1]//button[2]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg5)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg5 + " [Actual]" + alert_msg;
		}
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		List<WebElement> addlist = driver.findElements(By.xpath("//tbody/tr"));
		
		if(addlist.size() != 1) {
			failMsg = failMsg + "\n2.List is not added [Expected] 1 [Actual]" + addlist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority = 25, enabled = true)
	public void insertPaymentvalue() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table payment-add']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[1]//button[2]")));
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[3]/input[@class='payment-update-datepicker']")));
		
		driver.findElement(By.xpath("//td[3]/input[@class='payment-update-datepicker']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		driver.findElement(By.xpath("//tr[1]/td[(text()='1')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//td[4]/input[@class='payment-update-datepicker']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		driver.findElement(By.xpath("//tr[1]/td[(text()='1')]")).click();
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//input[@class='payment-update-pay']")).sendKeys("10");
		
		if(!driver.findElement(By.xpath("//select[@class='payment-status']/option[1]")).getText().contentEquals("성공")) {
			failMsg = "1.Wrong option [Expected] 성공 [Actual]" + driver.findElement(By.xpath("//select[@class='payment-status']/option[1]")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@class='payment-status']/option[2]")).getText().contentEquals("미납")) {
			failMsg = failMsg + "\n2.Wrong option [Expected] 미납 [Actual]" + driver.findElement(By.xpath("//select[@class='payment-status']/option[2]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 26, enabled = true)
	public void savePaymentvalue() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tr[1]/td[9]/button[1]")).click();
		comm2.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBe(By.xpath("//tr[1]//button"), "수정하기"));
		
		if(!driver.findElement(By.xpath("//tr[1]//button")).getText().contentEquals("수정하기")) {
			failMsg = "Button text is wrong [Expected] 수정하기 [Actual]" + driver.findElement(By.xpath("//tr[1]//button")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority = 27, enabled = true)
	public void savePaymentvalue_date() throws Exception {
		String failMsg = "";

		String stdate = driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
		System.out.println(stdate);
		
		if(!stdate.substring(stdate.length()-2,stdate.length()).contentEquals("01")) {
			failMsg = failMsg + "\n2.Startdate value saved Wrong [Expected]" + stdate.substring(0,8) + "01 [Actual]" + stdate;
		}
		
		String eddate = driver.findElement(By.xpath("//tr[1]/td[4]")).getText();
		System.out.println(eddate);
		
		if(!eddate.substring(eddate.length()-2,eddate.length()).contentEquals("01")) {
			failMsg = failMsg + "\n3.Enddate value saved Wrong [Expected]" + eddate.substring(0,8) + "01 [Actual]" + eddate;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 28, enabled = true)
	public void savePaymentvalue_pay() throws Exception {
		String failMsg = "";
		
		String pay = driver.findElement(By.xpath("//tr[1]/td[7]")).getText();
		System.out.println(pay);
		
		if(!pay.contentEquals("KRW 11")) {
			failMsg = failMsg + "\n4.PAY value saved Wrong [Expected] KRW 11 [Actual]" + pay;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 29, enabled = true)
	public void updatePayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-default btn-table payment-update-btn']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='payment-update-datepicker']")));
		
		if(driver.findElements(By.xpath("//input[@class='payment-update-datepicker']")).isEmpty()) {
			failMsg = "1.Not active Pay date";
		}
		
		if(driver.findElements(By.xpath("//input[@class='payment-update-pay']")).isEmpty()) {
			failMsg = failMsg + "\n2.Not active Pay amount";
		}
		
		if(driver.findElements(By.xpath("//select[@class='payment-status']")).isEmpty()) {
			failMsg = failMsg + "\n3.Not active pay status";
		}
		
		driver.findElement(By.xpath("//td[4]/input[@class='payment-update-datepicker']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
		
		driver.findElement(By.xpath("//td[(text()='10')]")).click();
		Thread.sleep(1000);
		
		while(!driver.findElement(By.xpath("//input[@class='payment-update-pay']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@class='payment-update-pay']")).sendKeys(Keys.CONTROL, "a");
			driver.findElement(By.xpath("//input[@class='payment-update-pay']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@class='payment-update-pay']")).sendKeys("100");
		
		driver.findElement(By.xpath("//tr[1]/td[9]/button[1]")).click();
		comm2.waitForLoad(driver);
		
		wait.until(ExpectedConditions.textToBe(By.xpath("//tr[1]//button"), "수정하기"));
		wait.until(ExpectedConditions.textToBe(By.xpath("//tr[1]/td[7]"), "KRW 110"));
		
		String eddate = driver.findElement(By.xpath("//tr[1]/td[4]")).getText();
		System.out.println(eddate);
		
		if(!eddate.substring(eddate.length()-2,eddate.length()).contentEquals("10")) {
			failMsg = failMsg + "\n4.Enddate value updated Wrong [Expected]" + eddate.substring(0,8) + "10 [Actual]" + eddate;
		}
		
		String pay = driver.findElement(By.xpath("//tr[1]/td[7]")).getText();
		System.out.println(pay);
		
		if(!pay.contentEquals("KRW 110")) {
			failMsg = failMsg + "\n5.PAY value updated Wrong [Expected] KRW 110 [Actual]" + pay;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 30, enabled = true)
	public void removePayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tr[1]/td[1]//label/div")).click();
		
		if(!driver.findElement(By.xpath("//tr[1]/td[1]//input")).isSelected()) {
			failMsg = "Don't selected";
		}
		
		driver.findElement(By.xpath("//button[@class='btn btn-danger btn-table payment-delete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg4)) {
			failMsg = "\n2.Alert msg is wrong [Expected]" + AlertMsg4 + " [Actual]" + alert_msg;
		}
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		List<WebElement> addlist = driver.findElements(By.xpath("//tbody/tr"));
		
		if(addlist.size() != 1) {
			failMsg = failMsg + "\n3.List is not removed [Expected] 1 [Actual]" + addlist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 31, enabled = true)
	public void updateUseramount() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='amount']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.CONTROL, "a");
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='amount']")).sendKeys("30");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(3000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//input[@id='amount']"), "value", "30"));
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		WebElement list = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]"));
		
		if(!list.findElement(By.xpath(".//td[5]")).getText().contentEquals("30")) {
			failMsg = "Don't update UserAmount [Expected] 30 [Actual]" + list.findElement(By.xpath(".//td[5]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
		
	@Test(priority = 32, enabled = true)
	public void clickApproveBtn_Branch() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LICENSE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
		
		WebElement list = driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]"));
			
		list.findElement(By.xpath("//button[@class='btn btn-table btn-success approve']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		Thread.sleep(1000);
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]/td[8]")), "정상"));
		
		if(!driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]/td[8]")).getText().contentEquals("정상")) {
			failMsg = failMsg + "\n2.license is not normal [Expected] 정상 [Actual]" + driver.findElement(By.xpath("//tbody[@id='companyListWrapper']/tr[1]/td[8]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		Partner_driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	public String AddDate(String strDate, int year, int month, int day) throws Exception {
		
		String pattern = "d MMMMM yyyy";
		SimpleDateFormat dtFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
		Calendar cal = Calendar.getInstance();
		Date dt = dtFormat.parse(strDate);
		cal.setTime(dt);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		return dtFormat.format(cal.getTime());
	}
	
	public void searchdata(int i, String data) {
		//1=Code, 2=CompanyName, 3=Email, 4=CONTRACT_NO 
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']/option[" + i +"]")).click();
		
		WebElement input = driver.findElement(By.xpath("//input[@id='search-keywordString']"));
		
		while(!input.getAttribute("value").isEmpty() || !input.getText().isEmpty()) {
			input.sendKeys(Keys.BACK_SPACE); }
		
		
		input.sendKeys(data);
		
		if(i == 3) {
			driver.findElement(By.xpath("//select[@id='date-condition']")).click();
			driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).click();
			
		}
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		comm2.waitForLoad(driver);
		
	}
	
	public void checkData(int i, String data) throws Exception {
		//1=Code, 2=CompanyName, 3=Email
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		String result;
		
		for (int j = 0; j < list.size(); j++) {

			switch (i) {
			case 1:
				result = list.get(j).findElement(By.xpath(".//td[2]/a")).getText();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 2:
				result = list.get(j).findElement(By.xpath(".//td[2]/a")).getText();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 3:
				
				list.get(j).findElement(By.xpath(".//td[2]/a")).click();

				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
				
				Thread.sleep(1500);

				driver.findElement(By.xpath("//input[@id='searchKeyword']")).sendKeys(data);
				Thread.sleep(1000);
				driver.findElement(By.xpath("//button[@id='doSearch']")).click();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//tbody[@id='handleTableUserRow']//td[2]")), data));
				
				result = driver.findElement(By.xpath("//tbody[@id='handleTableUserRow']//td[2]")).getText();
				System.out.println(result);

				if (!result.contentEquals(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}

				driver.navigate().back();
				
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "라이선스 관리"));
				
				Thread.sleep(2000);
				searchdata(i, data);
				Thread.sleep(2000);

				list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));

				break;
				}
			}
		}
	
	public void createLicense(WebDriver driver) throws InterruptedException {
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANYADD_URI);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		driver.findElement(By.xpath("//input[@id='companyName']")).sendKeys(name);
		driver.findElement(By.xpath("//select[@id='businessType']")).click();
		driver.findElement(By.xpath("//select[@id='businessType']/option[2]")).click();
		
		driver.findElement(By.xpath("//select[@id='businessKind']")).click();
		driver.findElement(By.xpath("//select[@id='businessKind']/option[2]")).click();
		
		driver.findElement(By.xpath("//input[@id='phone']")).sendKeys("1111111");
		/*
		driver.findElement(By.xpath("//button[@id='partner-tree-btn']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@role='treeitem']")));
		
		driver.findElement(By.xpath("//li/i[@role='presentation']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='jstree-anchor']")));
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[contains(text(), '자동화테스트용')]")).click();
		*/
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		Thread.sleep(3000);
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		driver.findElement(By.xpath("//button[@data-type='I']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		Thread.sleep(3000);
		driver.findElement(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[2]//a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "라이선스 관리"));
		
		driver.findElement(By.xpath("//button[@id='license-demo-expired']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		
		alert.accept();
		Thread.sleep(1000);
		alert.accept();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@id='license-demo-expired']")));
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='offlinePurchaseStrategy.type']")));
		
	}
	
	
}
