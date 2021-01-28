package partners;

import static org.testng.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

import com.google.gdata.model.atompub.Control;

import mandatory.CommonValues;

public class Company {
	
	public static String TOGGLE_BTN = "//button[@class='search-toggle-btn']";
	public static String SEARCH_BTN = "//button[@type='submit']";
	
	public static String AlertMsg = "최대 6개월까지 검색가능합니다.";
	public static String AlertMsg2 = "탈퇴 처리를 하시겠습니까?\n" + "탈퇴 처리중 상태로 변경되며 90일 이후에 완전 삭제됩니다.";
	public static String AlertMsg3 = "탈퇴 처리가 완료되었습니다.";
	public static String AlertMsg4 = "탈퇴를 취소 하시겠습니까?\n" + "정상 고객사 상태로 변경됩니다.";
	public static String AlertMsg5 = "탈퇴 취소 처리가 완료되었습니다.";
	public static String AlertMsg6 = "날짜 선택이 잘못되었습니다. 다시 선택해 주세요.";
	public static String AlertMsg7 = "이용자 수는 1명 이상 1000명 이하로 선택해주세요.";
	public static String AlertMsg8 = "무료체험을 해약하시겠습니까? 해약하면 남아있는 무료 기간은 소멸됩니다.";
	public static String AlertMsg9 = "해약 처리 되었습니다.";
	public static String AlertMsg10 = "사용 가능합니다.";
	public static String AlertMsg11 = "비밀번호를 초기화 하시겠습니까? \n" + "초기화 비밀번호는 '111111' 입니다.";
	public static String AlertMsg12 = "비밀번호를 초기화 하였습니다.";
	public static String AlertMsg13 = "전송된 이메일에서 인증을 완료해 주세요. [Code: 40832]";
	public static String AlertMsg14 = "지정 가능한 수량을 초과했습니다.";
	
	public static String ErrorMsg = "필수 입력란입니다.";
	public static String ErrorMsg2 = "최대 회의 시간은 10분이상 720분 이하로 설정해 주세요.";
	public static String ErrorMsg3 = "10분이상으로 설정해 주세요.";
	public static String ErrorMsg4 = "720분 이하로 설정해 주세요.";
	public static String Tooltip = "요금제 이용 중인 그룹만 사용 가능한 URL 설정 기능입니다.";
	public static String Msg = "형식에 맞지 않는 파일을 선택하셨습니다. (지원형식 : png, jpg, gif)";
	
	public String Code;
	
	private String CompanyName = "autotest";
	
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
		context.setAttribute("webDriver", Login_driver);

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
	public void goCompany() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("고객사 관리")) {
			failMsg = "1.Wrong Menu [Expected] 고객사 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}

		if (!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + " [Actual]"
					+ driver.getCurrentUrl();
		}

		if (!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected")
				.contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong selected [Expected]기간 [Actual]"
					+ driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getText();
		}

		if (!driver.findElement(By.xpath("//input[@id='startDate']")).getAttribute("disabled").contentEquals("true")
				|| !driver.findElement(By.xpath("//input[@id='endDate']")).getAttribute("disabled")
						.contentEquals("true")) {
			failMsg = failMsg + "\n4.Date input is not disable";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 3, enabled = true)
	public void searchCompany() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath("//select[@id='date-condition']")).click();
		driver.findElement(By.xpath("//select[@id='date-condition']/option[2]")).click();

		driver.findElement(By.xpath("//input[@id='startDate']")).click();
		
		for (int i = 0; i < 6; i++) {
			driver.findElement(By.xpath("//div[@class='datepicker-days']//th[@class='prev']")).click();
		}
		
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String newday = Integer.toString(day);

		selectDate(newday);

		driver.findElement(By.xpath(SEARCH_BTN)).click();

		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 4, enabled = true)
	public void searchCompany_leaved() throws Exception {

		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		driver.findElement(By.xpath(TOGGLE_BTN)).click();
		driver.findElement(By.xpath("//select[@id='search.leaved']")).click();
		driver.findElement(By.xpath("//select[@id='search.leaved']/option[2]")).click();
		driver.findElement(By.xpath(SEARCH_BTN)).click();

		checkData(6, "탈퇴 처리중");

	}

	@Test(priority = 5, enabled = true)
	public void searchCompany_issuetype() throws Exception {

		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		driver.findElement(By.xpath(TOGGLE_BTN)).click();
		driver.findElement(By.xpath("//select[@id='search.issuedByType']")).click();
		driver.findElement(By.xpath("//select[@id='search.issuedByType']/option[2]")).click();
		driver.findElement(By.xpath(SEARCH_BTN)).click();

		checkData(3, "온라인");

	}

	@Test(priority = 6, enabled = true)
	public void searchCompany_plantype() throws Exception {

		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		driver.findElement(By.xpath(TOGGLE_BTN)).click();
		driver.findElement(By.xpath("//select[@id='search.planType']")).click();
		driver.findElement(By.xpath("//select[@id='search.planType']/option[3]")).click();
		driver.findElement(By.xpath(SEARCH_BTN)).click();

		checkData(5, "종량 요금제");
	}
	
	@Test(priority = 7, enabled = true)
	public void searchCompany_partner() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		driver.findElement(By.xpath(TOGGLE_BTN)).click();
		driver.findElement(By.xpath("//button[@id='partner-tree-btn']")).click();
		driver.findElement(By.xpath("//i[@class='jstree-icon jstree-ocl']")).click();
		
		List<WebElement> treelist = driver.findElements(By.xpath("//li[@role='treeitem']//a"));
		
		treelist.get(1).click();
		String partnername = treelist.get(1).getText();
		System.out.println(partnername);
		
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		driver.findElement(By.xpath(TOGGLE_BTN)).click();
		
		if(!driver.findElement(By.xpath("//input[@id='partnerName']")).getAttribute("value").contentEquals(partnername)) {
			failMsg = "Can't search Parter. [Expected]" + partnername 
					+ " [Actual]" + driver.findElement(By.xpath("//input[@id='partnerName']")).getAttribute("value");
		}
		
		checkData(4, partnername);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void searchCompany_companyname() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));

		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("자동화");
		
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		
		checkData(2, "자동화");
		
	}
	
	@Test(priority = 9, enabled = true)
	public void searchCompany_companycode() throws Exception {

		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys(CommonValues_Partners.TESTACCOUNT_COMPANYCODE);
		
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']/option[1]")).click();
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		
		checkData(2, "자동화테스트용");
		
	}
	
	@Test(priority = 10, enabled = true)
	public void searchCompany_companyemail() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys(mandatory.CommonValues.ADMEMAIL);
		
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']/option[3]")).click();
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		
		checkData(2, "자동화테스트용");
		
	}
	
	@Test(priority = 11, enabled = true)
	public void searchCompany_notleaved() throws Exception {

		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("자동화");
		
		driver.findElement(By.xpath(TOGGLE_BTN)).click();
		driver.findElement(By.xpath("//select[@id='search.leaved']")).click();
		driver.findElement(By.xpath("//select[@id='search.leaved']/option[3]")).click();
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		
		checkData(6, "정상 고객사");
		
	}
	
	@Test(priority = 12, enabled = true)
	public void searchCompany_checked() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		if(!driver.findElement(By.xpath("//input[@id='search.total1']")).isSelected()) {
			failMsg = "CheckBox is not selected";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 13, enabled = true)
	public void pagingCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		String N2 = driver.findElement(By.xpath("//span[@class='result-info']")).getText().substring(2,5);
		int Realcount = Integer.parseInt(N2);
		System.out.println(N2);
		System.out.println(Realcount);
		
		List<WebElement> paging = driver.findElements(By.xpath("//div[@class='pagination']/li"));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@class='pagination']/li")));
		
		paging.get(paging.size()-1).findElement(By.xpath(".//i")).click();
		
		List<WebElement> paging2 = driver.findElements(By.xpath("//div[@class='pagination']/li"));
		
		String lastP = paging2.get(paging2.size()-1).findElement(By.xpath("./a")).getText();
		System.out.println(lastP);
		int lastPnum = Integer.parseInt(lastP);

		// 페이지 수 확인
		if (lastPnum != (int) Math.ceil((double) Realcount / 30)) {
			failMsg = "1. list paging error. paging count [Expected]" + (int) Math.ceil((double) Realcount / 30)
					+ " [Actual]" + lastPnum;
		}
		Thread.sleep(3000);
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		if (rows.size() != 30) {
			failMsg = failMsg + "\n 2-1. list rows [Expected]30 [Actual]" + rows.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 16, enabled = true)
	public void addCompanyURL() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table add-new']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='layer-right custom-breadcrumb']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANYADD_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANYADD_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 17, enabled = true)
	public void addCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		if(!driver.findElement(By.xpath("//div[@id='companyName']//span[@class='error-msg']")).getText().contentEquals(ErrorMsg) ||
				!driver.findElement(By.xpath("//div[2]/div[1]/label/span[2]")).getText().contentEquals(ErrorMsg) ||
				!driver.findElement(By.xpath("//div[@id='phone']//span[3]")).getText().contentEquals(ErrorMsg)) {
			failMsg = "1.Wrong ErrorMsg";
		}
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANYADD_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANYADD_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 18, enabled = true)
	public void addCompany2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@id='companyName']")).sendKeys(CompanyName);
		driver.findElement(By.xpath("//select[@id='businessType']")).click();
		driver.findElement(By.xpath("//select[@id='businessType']/option[2]")).click();
		
		driver.findElement(By.xpath("//select[@id='businessKind']")).click();
		driver.findElement(By.xpath("//select[@id='businessKind']/option[2]")).click();
		
		driver.findElement(By.xpath("//input[@id='phone']")).sendKeys("1111111");
		
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		Thread.sleep(3000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 19, enabled = true)
	public void addCompany3() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='panel-header']")));
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys(CompanyName);
		
		driver.findElement(By.xpath(SEARCH_BTN)).click();
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		list.get(0).findElement(By.xpath(".//a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 20, enabled = true)
	public void reviseCompany() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='phone']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='phone']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='phone']")).sendKeys("2222222");
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		Thread.sleep(3000);
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code);
		
		if(!driver.findElement(By.xpath("//input[@id='phone']")).getAttribute("value").contentEquals("2222222")) {
			failMsg = "Don't revise company";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 21, enabled = true)
	public void tooltipCompany() throws Exception {
		
		Actions action = new Actions(driver);
		WebElement tooltip = driver.findElement(By.xpath("//div[@class='panel-title-wrap']//button"));
		Thread.sleep(1000);
		action.moveToElement(tooltip).build().perform();
		Thread.sleep(3000);
				
		Boolean isToolTipDisplayed = driver.findElement(By.xpath("//div[@class='tooltip-inner']/b")).isDisplayed();
		System.out.println("Is Tooltip displayed ? : " + isToolTipDisplayed);
		if (isToolTipDisplayed == true) {
			String tooltipText = driver.findElement(By.xpath("//div[@class='tooltip-inner']/b")).getText();
			System.out.println("Tooltip Text:- " + tooltipText);
			if(!tooltipText.contentEquals(Tooltip)) {
				Exception e = new Exception("AITab tooltip text is wrong :" + tooltipText);
				throw e;
			}
		}
		else {
			Exception e = new Exception("AITab tooltip is not displayed");
			throw e;		
			}
	}
	
	@Test(priority= 22, enabled = true)
	public void uploadCompany() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath("//div[@id='wrapperLogoImage']/div/div[1]/div[3]")).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='file-upload-wrapper file-inner admin selected-new']")));

		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[3];

		driver.findElement(By.xpath("//input[@id='uploadLogoLounge']")).sendKeys(addedfile);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='logoLoungeWrapper']/div/span")), "파일명:image"));

		driver.findElement(By.xpath("//input[@id='uploadLogoAdmin']")).sendKeys(addedfile);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='logoAdminWrapper']/div/span")), "파일명:image"));

		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='logoAdminWrapper']/div/span")), "파일명:image")));

		if (!driver.findElement(By.xpath("//div[1]/div/div/div/div[1]/img")).getAttribute("src")
				.contains(CommonValues_Partners.PARTNER_URL)
				|| !driver.findElement(By.xpath("//div[2]/div[1]/div/div[1]/div/div/div//img")).getAttribute("src")
						.contains(CommonValues_Partners.PARTNER_URL)) {
			failMsg = "Don't upload image";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 23, enabled = true)
	public void uploadCompany2() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath("//div[@id='wrapperLogoImage']/div/div[1]/div[3]")).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='file-upload-wrapper file-inner admin selected-new']")));

		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[0];

		driver.findElement(By.xpath("//input[@id='uploadLogoLounge']")).sendKeys(addedfile);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-content']")));
		
		if(!driver.findElement(By.xpath("//div[@class='result-body modal-body']")).getText().contentEquals(Msg)) {
			failMsg = "Wrong Msg [Expected]" + Msg + " [Actual]" + driver.findElement(By.xpath("//div[@class='result-body modal-body']")).getText();
		}
		
		driver.findElement(By.xpath("//button[@id='modalCallback']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='modal-content']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 24, enabled = true)
	public void usageStatusCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='usageStatus']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));
		
		if(!driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value").contentEquals(Code)) {
			failMsg = "Wrong Filtering";
		}
		
		if (!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCELOGSTAT_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCELOGSTAT_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 25, enabled = true)
	public void meetingHistoryCompany() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		driver.findElement(By.xpath("//button[@id='meetingHistory']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));
		
		if(!driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value").contentEquals(Code)) {
			failMsg = "Wrong Filtering";
		}
		
		if (!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 26, enabled = true)
	public void paymentCompany() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		driver.findElement(By.xpath("//button[@id='billingManagement']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));
		
		if(!driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value").contentEquals(Code)) {
			failMsg = "Wrong Filtering";
		}
		
		if (!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 27, enabled = true)
	public void DefaultmeetingTime() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		if(!driver.findElement(By.xpath("//input[@id='maxDuration']")).getAttribute("value").contentEquals("720")) {
			failMsg = "Default meeting time option is wrong [Expected] 720 [Actual]" + driver.findElement(By.xpath("//input[@id='maxDuration']")).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 28, enabled = true)
	public void MinmeetingTime() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='maxDuration']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='maxDuration']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='maxDuration']")).sendKeys("9");
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")),ErrorMsg2));
		
		if(!driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")).getText().contentEquals(ErrorMsg2)) {
			failMsg = "Wrong Msg [Expected]" + ErrorMsg2 + " [Actual]" + driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 29, enabled = true)
	public void MaxmeetingTime() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='maxDuration']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='maxDuration']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='maxDuration']")).sendKeys("1000");
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")),ErrorMsg2));
		
		if(!driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")).getText().contentEquals(ErrorMsg2)) {
			failMsg = "Wrong Msg [Expected]" + ErrorMsg2 + " [Actual]" + driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 30, enabled = true)
	public void DefaultwaitingTime() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).getAttribute("value").contentEquals("10")) {
			failMsg = "Default meeting waiting option is wrong [Expected] 10 [Actual]" + driver.findElement(By.xpath("//input[@id='maxDuration']")).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 31, enabled = true)
	public void MinwaitingTime() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).sendKeys("9");
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='conferenceWatingMaxMinutes']/div/span")),ErrorMsg3));
		
		if(!driver.findElement(By.xpath("//div[@id='conferenceWatingMaxMinutes']/div/span")).getText().contentEquals(ErrorMsg3)) {
			failMsg = "Wrong Msg [Expected]" + ErrorMsg3 + " [Actual]" + driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 32, enabled = true)
	public void MaxwaitingTime() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='conferenceWatingMaxMinutes']")).sendKeys("1000");
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@id='conferenceWatingMaxMinutes']/div/span")),ErrorMsg4));
		
		if(!driver.findElement(By.xpath("//div[@id='conferenceWatingMaxMinutes']/div/span")).getText().contentEquals(ErrorMsg4)) {
			failMsg = "Wrong Msg [Expected]" + ErrorMsg4 + " [Actual]" + driver.findElement(By.xpath("//div[@id='conferenceMaxMinute']/div/span")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 33, enabled = true)
	public void leaved() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='company-leaved']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		if(!driver.findElement(By.xpath("//button[@id='company-leaved']")).isDisplayed()) {
			failMsg = "Cancel Button is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 34, enabled = true)
	public void leaved2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='company-leaved']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		System.out.println(alert_msg);
		Thread.sleep(1000);
		
		String alert_msg2= alert.getText();
		alert.accept();
		System.out.println(alert_msg2);
		
		if(!alert_msg2.contentEquals(AlertMsg3)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg3 + " [Actual]" + alert_msg2;
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='company-leaved-cancel']")));
		
		if(!driver.findElement(By.xpath("//button[@id='company-leaved-cancel']")).isDisplayed()) {
			failMsg = "Cancel Button is not display";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 35, enabled = true)
	public void cancelleaved() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='company-leaved-cancel']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		System.out.println(alert_msg);
		Thread.sleep(1000);
		
		if(!alert_msg.contentEquals(AlertMsg4)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg4 + " [Actual]" + alert_msg;
		}
		
		if(!driver.findElement(By.xpath("//button[@id='company-leaved-cancel']")).isDisplayed()) {
			failMsg = "Cancel leaved Button is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 36, enabled = true)
	public void cancelleaved2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='company-leaved-cancel']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		System.out.println(alert_msg);
		Thread.sleep(1000);
		
		String alert_msg2= alert.getText();
		alert.accept();
		System.out.println(alert_msg2);
		
		if(!alert_msg2.contentEquals(AlertMsg5)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg5 + " [Actual]" + alert_msg2;
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='company-leaved']")));
		
		if(!driver.findElement(By.xpath("//button[@id='company-leaved']")).isDisplayed()) {
			failMsg = "leaved Button is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority= 37, enabled = true)
	public void golistCompany() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@class='btn btn-default spa-menu']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "고객사 관리"));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority= 38, enabled = true)
	public void checkplan() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		
		String addDay = AddDate(today, 0, 0, 14);
		
		List <WebElement> td = driver.findElements(By.xpath("//tbody[@id='handleTableLicenseRow']//td"));

		if (!td.get(0).findElement(By.xpath(".//a")).getText().contentEquals(today)) {
			failMsg = "Start date is wrong [Expectd]" + today + " [Actual]"
					+ td.get(0).findElement(By.xpath(".//a")).getText();
		}

		if (!td.get(1).getText().contentEquals(addDay)) {
			failMsg = failMsg + "\n2.End date is wrong [Expectd]" + addDay + "[Actual]" + td.get(1).getText();
		}

		if (!td.get(2).getText().contentEquals("FREE Trial")) {
			failMsg = failMsg + "\n3.Plan is wrong [Expected] Free Trial [Actual]" + td.get(2).getText();
		}

		if (!td.get(3).getText().contentEquals("-")) {
			failMsg = failMsg + "\n4.Amount is wrong [Expected] - [Actual]" + td.get(3).getText();
		}

		if (!td.get(4).getText().contentEquals("정상")) {
			failMsg = failMsg + "\n5.Status is wrong [Expected] 정상 [Actual]" + td.get(4).getText();
		}

		if (!td.get(5).getText().contentEquals("X")) {
			failMsg = failMsg + "\n6.Status2 is wrong [Expected] X [Actual]" + td.get(5).getText();
		}

		if (!td.get(6).getText().contentEquals("-")) {
			failMsg = failMsg + "\n7.Status3 is wrong [Expected] - [Actual]" + td.get(6).getText();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 39, enabled = true)
	public void checkFreetrialPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@data-type='FREE']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if(!driver.findElement(By.xpath("//form[@id='license']")).isDisplayed()) {
			failMsg = "Free Trial Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 40, enabled = true)
	public void closeFreetrialPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//form[@id='license']/div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if(!driver.findElements(By.xpath("//form[@id='license']")).isEmpty()) {
			failMsg = "Free Trial Popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 41, enabled = true)
	public void reviseFreetrialDate() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@data-type='FREE']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		driver.findElement(By.xpath("//input[@id='startDate']")).click();
		
		driver.findElement(By.xpath("//div[@class='datepicker-days']//th[@class='next']")).click();
		
		selectDate("25");
		
		driver.findElement(By.xpath("//button[@id='license-free-save']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg6)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg6 + " [Actual]" + alert_msg;
		}
		
		driver.findElement(By.xpath("//form[@id='license']/div[3]/button[1]")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 42, enabled = true)
	public void clickPlanStartDate() throws Exception {
		String failMsg = "";
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//tbody[@id='handleTableLicenseRow']//td/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "라이선스 관리"));
		
		if (!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + CommonValues_Partners.LICENSE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + CommonValues_Partners.LICENSE_URI + "/" + Code + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 43, enabled = true)
	public void checkUserPlanPopup() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@data-type='I']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if(!driver.findElement(By.xpath("//form[@id='license']")).isDisplayed()) {
			failMsg = "User Plan Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 44, enabled = true)
	public void closeUserPlanPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//form[@id='license']/div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if(!driver.findElements(By.xpath("//form[@id='license']")).isEmpty()) {
			failMsg = "User Plan Popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 45, enabled = true)
	public void selectUserPlanYear() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@data-type='I']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		driver.findElement(By.xpath("//select[@id='year-select']")).click();
		driver.findElement(By.xpath("//select[@id='year-select']/option[2]")).click();
		
		if(!driver.findElement(By.xpath("//select[@id='year-select']/option[2]")).getText().contentEquals("2 년")) {
			failMsg = "Year option is wrong + [Expected] 2년  [Actual]" + driver.findElement(By.xpath("//select[@id='year-select']/option[2]")).getText();
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//select[@id='year-select']")).click();
		driver.findElement(By.xpath("//select[@id='year-select']/option[1]")).click();
		
		if(!driver.findElement(By.xpath("//select[@id='year-select']/option[1]")).getText().contentEquals("1 년 ")) {
			failMsg = failMsg + "\n2.Year option is wrong + [Expected] 1년  [Actual]" + driver.findElement(By.xpath("//select[@id='year-select']/option[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority= 46, enabled = true)
	public void selectUserPlanMonth() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//select[@id='month-paid-select']")).click();
		driver.findElement(By.xpath("//select[@id='month-paid-select']/option[2]")).click();
		
		List<WebElement> option = driver.findElements(By.xpath("//select[@id='monthly-select']/option"));
		
		if(option.size() != 11) {
			failMsg = "Month option size is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 47, enabled = true)
	public void selectUserPlanAmountMax() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='amount']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.CONTROL, "a");
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='amount']")).sendKeys("1001");
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg7)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg7 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 48, enabled = true)
	public void saveUserPlan() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='amount']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='amount']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='amount']")).sendKeys("2");
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		Thread.sleep(3000);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		
		List <WebElement> td = driver.findElements(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[1]/td"));
		
		if (!td.get(2).getText().contentEquals("User 요금제")) {
			failMsg = "Plan is wrong [Expected] User 요금제 [Actual]" + td.get(2).getText();
		}

		if (!td.get(3).getText().contentEquals("2")) {
			failMsg = failMsg + "\n2.Amount is wrong [Expected] 2 [Actual]" + td.get(3).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 49, enabled = true)
	public void checkRoomPlanPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@data-type='R']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if(!driver.findElement(By.xpath("//form[@id='license']")).isDisplayed()) {
			failMsg = "Room Plan Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 50, enabled = true)
	public void closeRoomPlanPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//form[@id='license']/div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		if(!driver.findElements(By.xpath("//form[@id='license']")).isEmpty()) {
			failMsg = "Room Plan Popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 51, enabled = true)
	public void selectRoomPlanYear() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@data-type='R']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='license']")));
		
		driver.findElement(By.xpath("//select[@id='year-select']")).click();
		driver.findElement(By.xpath("//select[@id='year-select']/option[2]")).click();
		
		if(!driver.findElement(By.xpath("//select[@id='year-select']/option[2]")).getText().contentEquals("2 년")) {
			failMsg = "Year option is wrong + [Expected] 2년  [Actual]" + driver.findElement(By.xpath("//select[@id='year-select']/option[2]")).getText();
		}
		
		driver.findElement(By.xpath("//select[@id='year-select']")).click();
		driver.findElement(By.xpath("//select[@id='year-select']/option[1]")).click();
		
		if(!driver.findElement(By.xpath("//select[@id='year-select']/option[1]")).getText().contentEquals("1 년 ")) {
			failMsg = failMsg + "\n2.Year option is wrong + [Expected] 1년  [Actual]" + driver.findElement(By.xpath("//select[@id='year-select']/option[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 52, enabled = true)
	public void selectRoomPlanMonth() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//select[@id='month-paid-select']")).click();
		driver.findElement(By.xpath("//select[@id='month-paid-select']/option[2]")).click();
		
		List<WebElement> option = driver.findElements(By.xpath("//select[@id='monthly-select']/option"));
		
		if(option.size() != 11) {
			failMsg = "Month option size is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 53, enabled = true)
	public void saveRoomPlan() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//select[@id='plan-select-box']")).click();
		driver.findElement(By.xpath("//select[@id='plan-select-box']/option[2]")).click();
		
		while(!driver.findElement(By.xpath("//input[@id='roomCount']")).getAttribute("value").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='roomCount']")).sendKeys(Keys.CONTROL, "a");
			driver.findElement(By.xpath("//input[@id='roomCount']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='roomCount']")).sendKeys("10");
		
		driver.findElement(By.xpath("//button[@id='license-save']")).click();
		
		Thread.sleep(3000);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		
		List <WebElement> td = driver.findElements(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[1]/td"));
		
		if (!td.get(2).getText().contentEquals("회의실 요금제 - 5")) {
			failMsg = "Plan is wrong [Expected] 회의실 요금제 - 5 [Actual]" + td.get(2).getText();
		}

		if (!td.get(3).getText().contentEquals("15")) {
			failMsg = failMsg + "\n2.Amount is wrong [Expected] 15 [Actual]" + td.get(3).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	/*	
	@Test(priority= 54, enabled = true)
	public void expiredPlan() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[3]//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "라이선스 관리"));
		
		if (!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + CommonValues_Partners.LICENSE_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + CommonValues_Partners.LICENSE_URI + "/" + Code + " [Actual]" + driver.getCurrentUrl();
		}
		
		driver.findElement(By.xpath("//button[@id='license-demo-expired']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg8)) {
			failMsg = failMsg + "\n2.Alert msg is wrong [Expected]" + AlertMsg8 + " [Actual]" + alert_msg;
		}
		
		String alert_msg2= alert.getText();
		alert.accept();
		System.out.println(alert_msg2);
		
		if(!alert_msg2.contentEquals(AlertMsg9)) {
			failMsg = failMsg + "\n3.Alert msg is wrong [Expected]" + AlertMsg9 + " [Actual]" + alert_msg2;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	*/ 	
	@Test(priority= 56, enabled = true)
	public void userList() throws Exception {
		String failMsg = "";
		
		List <WebElement> userlisttitle = driver.findElements(By.xpath("//table[@id='userList']//th"));
		
		if(!userlisttitle.get(0).getText().contentEquals("이름")) {
			failMsg = "Title is wrong [Expected] 이름 [Actual]" + userlisttitle.get(0).getText(); 
		}
		
		if(!userlisttitle.get(1).getText().contentEquals("이메일")) {
			failMsg = failMsg + "\n2.Title is wrong [Expected] 이메일 [Actual]" + userlisttitle.get(1).getText(); 
		}
		
		if(!userlisttitle.get(2).getText().contentEquals("부서")) {
			failMsg = failMsg + "\n3.Title is wrong [Expected] 부서 [Actual]" + userlisttitle.get(2).getText(); 
		}
		
		if(!userlisttitle.get(3).getText().contentEquals("권한")) {
			failMsg = failMsg + "\n3.Title is wrong [Expected] 권한 [Actual]" + userlisttitle.get(2).getText(); 
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 57, enabled = true)
	public void checkCreateDemouserPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary add-new btn-table demo-user']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-content panel panel-primary']")));
		
		if(!driver.findElement(By.xpath("//div[@class='modal-content panel panel-primary']")).isDisplayed()) {
			failMsg = "Create Demouser popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 58, enabled = true)
	public void closeCreateDemouserPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[2]//div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='modal-content panel panel-primary']")));
		
		
		if(!driver.findElements(By.xpath("//div[@class='modal-content panel panel-primary']")).isEmpty()) {
			failMsg = "Create Demouser popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 59, enabled = true)
	public void createDemouser() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary add-new btn-table demo-user']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-content panel panel-primary']")));
		
		driver.findElement(By.xpath("//button[@id='demo-user-create-btn']")).click();
		
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		
		List<WebElement> demouserlist = driver.findElements(By.xpath("//tbody[@id='handleTableUserRow']/tr"));
		
		if(demouserlist.size() != 4) {
			failMsg = "Demo users is not 4";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 60, enabled = true)
	public void checkDemouserBtn() throws Exception {
		String failMsg = "";
		
		if(!driver.findElements(By.xpath("//button[@class='btn btn-primary add-new btn-table demo-user']")).isEmpty()) {
			failMsg = "Create Demouser Btn is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 61, enabled = true)
	public void importuserList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table excel']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-content']")));
		
		if(!driver.findElement(By.xpath("//div[@class='modal-content']")).isDisplayed()) {
			failMsg = "Import user Popup is not display";
		}
		
		driver.findElement(By.xpath("//section/div/a")).click();
		//download excel
		
		TimeUnit.SECONDS.sleep(5);
		
		String filepath = Excelpath("user-sample");
		
		if(existExcelFile(filepath) == false) {
			failMsg = failMsg + "\n2.user-sample file is not exist";
		}
		else {
			deleteExcelFile(filepath);
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 63, enabled = true)
	public void uploadWrongFile() throws Exception {
		String failMsg = "";
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath("//input[@name='uploadFile']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='file-drag-area attatched']")), mandatory.CommonValues.TESTFILE_LIST[0]));
		
		driver.findElement(By.xpath("//form[@id='formUserList']//div[2]/button[1]")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//span[@id='uploadFile-error']")), "엑셀파일만 업로드 가능합니다."));
		
		if(!driver.findElement(By.xpath("//span[@id='uploadFile-error']")).getText().contentEquals("엑셀파일만 업로드 가능합니다.")) {
			failMsg = "Wrong Error Msg [Expected] 엑셀파일만 업로드 가능합니다. [Actual]" + driver.findElement(By.xpath("//span[@id='uploadFile-error']")).getText();
		}
		
		driver.findElement(By.xpath("//section/div[2]/button[2]")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='modal-content']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority= 64, enabled = true)
	public void uploadWronguserList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table excel']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-content']")));
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues_Partners.TESTFILE_LIST[1];
		driver.findElement(By.xpath("//input[@name='uploadFile']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='file-drag-area attatched']")), CommonValues_Partners.TESTFILE_LIST[1]));
		
		driver.findElement(By.xpath("//form[@id='formUserList']//div[2]/button[1]")).click();
		
		Thread.sleep(2000);
		
		if(!driver.findElement(By.xpath("//section[2]//p")).getText().contentEquals("오류로 인해 아래 리스트의 사용자 등록이 실패하였습니다.")) {
			failMsg = "Wrong msg [Expected] 오류로 인해 아래 리스트의 사용자 등록이 실패하였습니다. [Actual]" + driver.findElement(By.xpath("//section[2]//p")).getText();
		}
		
		driver.findElement(By.xpath("//div//section[2]/div[2]/button")).click();
		
		Thread.sleep(3000);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 65, enabled = true)
	public void uploaduserList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table excel']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-content']")));
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues_Partners.TESTFILE_LIST[0];
		driver.findElement(By.xpath("//input[@name='uploadFile']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='file-drag-area attatched']")), CommonValues_Partners.TESTFILE_LIST[0]));
		
		if(!driver.findElements(By.xpath("//span[@id='uploadFile-error']")).isEmpty()) {
			failMsg = "Error Msg is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='file-drag-area attatched']")).getText().contentEquals(CommonValues_Partners.TESTFILE_LIST[0])) {
			failMsg = failMsg + "\n2.Don't upload excel file";
		}
		
		driver.findElement(By.xpath("//form[@id='formUserList']//div[2]/button[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//section[@class='result-success']"))));
		
		driver.findElement(By.xpath("//div[@id='importExcel']//section[2]//button")).click();
		
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		
		List<WebElement> userlist = driver.findElements(By.xpath("//tbody[@id='handleTableUserRow']/tr"));
		
		if(!userlist.get(0).findElement(By.xpath(".//td[1]")).getText().contentEquals("rmrsup3")) {
			failMsg = failMsg + "\n3. Wrong upload user name [Expected] rmrsup3 [Actual]" + userlist.get(0).findElement(By.xpath(".//td[1]")).getText();
		}
		
		if(!userlist.get(0).findElement(By.xpath(".//td[2]")).getText().contentEquals(mandatory.CommonValues.USERS[1])) {
			failMsg = failMsg + "\n4. Wrong upload user email [Expected]" + mandatory.CommonValues.USERS[1] + " [Actual]" + userlist.get(0).findElement(By.xpath(".//td[1]")).getText();
		}
		
		if(!userlist.get(1).findElement(By.xpath(".//td[1]")).getText().contentEquals("rmrsup4")) {
			failMsg = failMsg + "\n5. Wrong upload user name [Expected] rmrsup4 [Actual]" + userlist.get(1).findElement(By.xpath(".//td[1]")).getText();
		}
		
		if(!userlist.get(1).findElement(By.xpath(".//td[2]")).getText().contentEquals(mandatory.CommonValues.USERS[2])) {
			failMsg = failMsg + "\n6. Wrong upload user email [Expected]" + mandatory.CommonValues.USERS[2] + " [Actual]" + userlist.get(1).findElement(By.xpath(".//td[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 66, enabled = true)
	public void checkNewuserListPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary add-new btn-table user']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		if(!driver.findElement(By.xpath("//form[@id='user']")).isDisplayed()) {
			failMsg = "New userList Popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 66, enabled = true)
	public void insertNewuserList_invalid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//form[@id='user']//div//div[1]//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//*[@id='email-message']")), "필수 입력란입니다."));
		
		if(!driver.findElement(By.xpath("//*[@id='email-message']")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = "1.Wrong error Msg [Expected] 필수 입력란입니다. [Actual]" + driver.findElement(By.xpath("//*[@id='email-message']")).getText();
		}
		
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("123");
		
		driver.findElement(By.xpath("//form[@id='user']//div//div[1]//button")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//*[@id='email-message']")), "올바른 이메일 형식으로 입력해주세요."));
		
		if(!driver.findElement(By.xpath("//*[@id='email-message']")).getText().contentEquals("올바른 이메일 형식으로 입력해주세요.")) {
			failMsg = failMsg + "\n2.Wrong error Msg [Expected] 올바른 이메일 형식으로 입력해주세요. [Actual]" + driver.findElement(By.xpath("//*[@id='email-message']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 67, enabled = true)
	public void insertNewuserList_duplicate() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(Keys.BACK_SPACE);
		
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(mandatory.CommonValues.USERS[1]);
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//form[@id='user']//div//div[1]//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//*[@id='email-message']")), "이미 등록된 이메일이 있습니다. 다시 입력해 주세요."));
		
		if(!driver.findElement(By.xpath("//*[@id='email-message']")).getText().contentEquals("이미 등록된 이메일이 있습니다. 다시 입력해 주세요.")) {
			failMsg = "Wrong error Msg [Expected] 이미 등록된 이메일이 있습니다. 다시 입력해 주세요. [Actual]" + driver.findElement(By.xpath("//*[@id='email-message']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 68, enabled = true)
	public void closeNewuserListPopup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//form[@id='user']/div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		if(!driver.findElements(By.xpath("//form[@id='user']")).isEmpty()) {
			failMsg = "New User List Popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 69, enabled = true)
	public void insertNewuserList_valid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary add-new btn-table user']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(mandatory.CommonValues.ADMEMAIL2);
		
		driver.findElement(By.xpath("//form[@id='user']//div//div[1]//button")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg10)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg10 + " [Actual]" + alert_msg;
		}
		
		driver.findElement(By.xpath("//select[@id='roleId']")).click();
		driver.findElement(By.xpath("//select[@id='roleId']/option[1]")).click();
		
		driver.findElement(By.xpath("//form[@id='user']//div[1]/div[2]/input")).sendKeys("rmrsup2");
		driver.findElement(By.xpath("//form[@id='user']//div[2]/div[1]/input")).sendKeys("QA");
		
		driver.findElement(By.xpath("//button[@id='user-save']")).click();
		
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		
		List<WebElement> userlist = driver.findElements(By.xpath("//tbody[@id='handleTableUserRow']/tr"));
		
		if(!userlist.get(0).findElement(By.xpath(".//td[1]")).getText().contentEquals("rmrsup2")) {
			failMsg = "1.Wrong upload user name [Expected] rmrsup2 [Actual]" + userlist.get(0).findElement(By.xpath(".//td[1]")).getText();
		}
		
		if(!userlist.get(0).findElement(By.xpath(".//td[2]")).getText().contentEquals(mandatory.CommonValues.ADMEMAIL2)) {
			failMsg = failMsg + "\n2. Wrong upload user email [Expected]" + mandatory.CommonValues.ADMEMAIL2 + " [Actual]" + userlist.get(0).findElement(By.xpath(".//td[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 70, enabled = true)
	public void checkAdminLogin() throws Exception {
		String failMsg = "";
		
		Login_driver.get(mandatory.CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(Login_driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm2.login(Login_driver, mandatory.CommonValues.ADMEMAIL2, "111111");
		
		if(Login_driver.findElements(By.xpath("//div[@id='gnb-admin']/a")).isEmpty()) {
			failMsg = "admin Btn is not display";
		}
	
		Login_driver.findElement(By.xpath("//button[@id='btn-user']")).click();
		Login_driver.findElement(By.xpath("//ul[@class='dropdown']/li[2]")).click();
		//logout
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 71, enabled = true)
	public void checkActiveBtn() throws Exception {
		String failMsg = "";
		
		List<WebElement> userlist = driver.findElements(By.xpath("//tbody[@id='handleTableUserRow']/tr"));
		
		userlist.get(1).findElement(By.xpath(".//td[1]/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		if(!driver.findElement(By.xpath("//form[@id='user']")).isDisplayed()) {
			failMsg = "User Popup is not display";
		}
		
		driver.findElement(By.xpath("//div[@class='fake-checkbox']")).click();
		
		driver.findElement(By.xpath("//button[@id='user-save']")).click();
		
		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='wrapLicenseList']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 72, enabled = true)
	public void checkPWReset() throws Exception {
		String failMsg = "";
		
		List<WebElement> userlist2 = driver.findElements(By.xpath("//tbody[@id='handleTableUserRow']/tr"));
		userlist2.get(1).findElement(By.xpath(".//td[5]/button")).click();
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		if (!alert_msg.contentEquals(AlertMsg11)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg11 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 73, enabled = true)
	public void checkPWReset2() throws Exception {
		String failMsg = "";
		
		List<WebElement> userlist2 = driver.findElements(By.xpath("//tbody[@id='handleTableUserRow']/tr"));
		userlist2.get(1).findElement(By.xpath(".//td[5]/button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		System.out.println(alert_msg);
		
		Thread.sleep(3000);
		
		String alert_msg2= alert.getText();
		alert.accept();
		System.out.println(alert_msg2);
		
		if (!alert_msg2.contentEquals(AlertMsg12)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg12 + " [Actual]" + alert_msg2;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority= 74, enabled = true)
	public void checkActiveUserLogin() throws Exception {
		String failMsg = "";
		
		comm2.login(Login_driver, mandatory.CommonValues.USERS[1], "111111");
		
		if(!Login_driver.findElements(By.xpath("//div[@id='gnb-admin']/a")).isEmpty()) {
			failMsg = failMsg + "\n2.admin Btn is display";
		}
		
		Login_driver.findElement(By.xpath("//button[@id='btn-user']")).click();
		Login_driver.findElement(By.xpath("//ul[@class='dropdown']/li[2]")).click();
		//logout
		WebDriverWait wait2 = new WebDriverWait(Login_driver, 10);
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 75, enabled = true)
	public void checkInactiveUserLogin() throws Exception {
		String failMsg = "";
		
		comm2.login(Login_driver, mandatory.CommonValues.USERS[2], "111111");
	
		Alert alert = Login_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		System.out.println(alert_msg);
		
		if (!alert_msg.contentEquals(AlertMsg13)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg13 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 76, enabled = true)
	public void settingUserPlan() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[2]/td[1]/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "라이선스 관리"));
		
		List<WebElement> checkbox = driver.findElements(By.xpath("//div[@class='checkbox']//input"));
		
		checkbox.get(0).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//span[@id='selected-amount']")), "1"));
		
		if(!checkbox.get(0).isSelected()) {
			failMsg = "Don't select checkbox";
		}
		
		if(!driver.findElement(By.xpath("//span[@id='selected-amount']")).getText().contentEquals("1")) {
			failMsg = failMsg + "Select amount is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 77, enabled = true)
	public void settingUserPlanAll() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='license-check-all']/input")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//span[@id='selected-amount']")), "7"));
		
		if(!driver.findElement(By.xpath("//span[@id='selected-amount']")).getText().contentEquals("7")) {
			failMsg = failMsg + "Select amount is wrong";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 78, enabled = true)
	public void checkUserSaveMsg() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='license-user-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		System.out.println(alert_msg);
		
		if(!alert_msg.contentEquals(AlertMsg14)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg14 + " [Actual]" + alert_msg;
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

	public String AddDate(String strDate, int year, int month, int day) throws Exception {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy.MM.dd");
		Calendar cal = Calendar.getInstance();
		Date dt = dtFormat.parse(strDate);
		cal.setTime(dt);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		return dtFormat.format(cal.getTime());
	}
		
	public void selectDate(String date) {
		
	    WebElement eval = driver.findElement(By.xpath("//div[contains(@class,'datepicker-days')]/table/tbody"));
	    List<WebElement> alldates = eval.findElements(By.tagName("td"));
	    for(WebElement cell:alldates){
	         String day = cell.getText();
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
				
				if (datanum == 2) {
					if (!result.contains(data)) {
						Exception e = new Exception("Wrong data [RowNum]" + i + "[Data]" + result);
						throw e;
					}
				} else {
					if (!result.contentEquals(data)) {
						Exception e = new Exception("Wrong data [RowNum]" + i + "[Data]" + result);
						throw e;
					}
			}}
		}
	}
	
	public String Excelpath(String filename) {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		int num = 1;
		
		String home = System.getProperty("user.home");
		if (os.contains("windows")) {
			
			path = home + "\\Downloads\\" + filename + ".xlsx";
			File file = new File(path);

			if (!file.exists()) {
				while (true) {
					num++;
					path = home + "\\Downloads\\" + filename + " (" + num + ").xlsx";
					File file2 = new File(path);
					if (file2.exists())
						break;
					}
			} 
		} else {
			path = home + "/Downloads/" + filename + ".xlsx";
		}
		return path;
	}
	
	public boolean existExcelFile(String filepath) {
		
		File file = new File(filepath);

		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void deleteExcelFile(String filepath) throws Exception {
		
	    File file = new File(filepath);

	    try {
	        if (file.exists()) {
	            file.delete();
	            System.out.println("delete file : " + filepath);
	        } else {
	            System.out.println("File is not exist");  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
}
