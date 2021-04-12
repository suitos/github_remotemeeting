package admin;

import static org.testng.Assert.fail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

/* Stats
 * 1. 통계 화면 이동
 * 2. 통계 - 월별 리스트에서 더보기 선택
 * 3. 통계 - 월별 화면에서 연도 선택
 * 4. 통계 - 일별 탭으로 이동 - 일 더보기 선택
 * 5. 통계 - 일별 탭 날짜검색 확인
 */

public class Stats {
	public static String XPATH_STATS_TABS = "//ul[@class='nav nav-tabs']/li";
	public static String XPATH_STATS_MONDETAILLIST = "//div[@id='monthlyConferenceBody']//tbody/tr";
	public static String XPATH_STATS_DAYDETAILLIST = "//div[@id='dailyConferenceBody']//tbody/tr";
	public static String XPATH_STATS_SUBMITSEARCH_BTN = "//button[@id='submitSearch']";
	public static String XPATH_STATS_DATEPICKER_START = "//input[@class='form-control pull-right start-date']";
	public static String XPATH_STATS_DATEPICKER_END = "//input[@class='form-control pull-right end-date']";
	public static String XPATH_STATS_MONTHLYCANVAS = "//canvas[@id='monthlyCanvas']";
	public static String XPATH_STATS_YEARSELECT = "//select[@id='yearlySelect']";
	
	public static String MSG_STATS_DAYDETAIL_POPUP = "※ 사용량은 1분 단위로 사용량이 측정되며, 하나의 회의에 대한 사용 시간으로 계산됩니다.\n"
			+ "(예. 4인 참여 10분 20초간 회의시 11분으로 측정)";
	public static String URL_STAT = "/customer/stat";
	
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
	public void StatsView() throws Exception {
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
		user.selectSideMenu(driver, 4, 0);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_TABS)));
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_STAT)) {
			failMsg = failMsg + "\n1. not stat view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, dependsOnMethods = {"StatsView"}, alwaysRun = true, enabled = true)
	public void StatsView_More() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_STAT)) {
			user.selectSideMenu(driver, 4, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_TABS)));
		}
		
		List<WebElement> monthlyList = driver.findElements(By.xpath(XPATH_STATS_MONDETAILLIST));
		if(monthlyList.size() > 0 ) {
			monthlyList.get(0).findElement(By.xpath("./td[6]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_SUBMITSEARCH_BTN)));
			
			if(!driver.findElement(By.xpath(XPATH_STATS_TABS + "[2]")).getAttribute("class").contains("active")) {
				failMsg = failMsg + "\n1. dailyTab is not actived.";
			}
		
		} else {
			failMsg = failMsg + "\n2. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, dependsOnMethods = {"StatsView"}, alwaysRun = true, enabled = true)
	public void StatsView_MonthYear() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_STAT)) {
			user.selectSideMenu(driver, 4, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_TABS)));
		}
		
		if(!driver.findElement(By.xpath(XPATH_STATS_TABS + "[1]")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(XPATH_STATS_TABS + "[1]")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_MONTHLYCANVAS)));
		}
		
		LocalDateTime nowDateTime = LocalDateTime.now();
		int year = nowDateTime.getYear();
		if(!driver.findElement(By.xpath(XPATH_STATS_YEARSELECT + "/option[1]")).getAttribute("value").contentEquals(Integer.toString(year))) {
			failMsg = failMsg + "\n1. year selector defalut value [Expected]" + year
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_STATS_YEARSELECT + "/option[1]")).getAttribute("value");
		}
		
		//select this year -1
		driver.findElement(By.xpath(XPATH_STATS_YEARSELECT)).click();
		Thread.sleep(500);
		driver.findElement(By.xpath(XPATH_STATS_YEARSELECT + "/option[2]")).click();
		Thread.sleep(1000);
		
		String[] urlsplit = driver.getCurrentUrl().split("&");
		for (String string : urlsplit) {
			if(string.contains("monthlyDate")) {
				String selectedY = string.split("=")[1];
				if(Integer.toString(year).contentEquals(selectedY)) {
					failMsg = failMsg + "\n2. year selected value [Expected]" + (year-1)
							+ " [Actual]" + selectedY;
				} else {
					System.out.println("find str : " + string);
				}
				break;
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void StatsView_dailyTab() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		user.selectSideMenu(driver, 4, 0);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_TABS)));
		
		if(!driver.findElement(By.xpath(XPATH_STATS_TABS + "[2]")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(XPATH_STATS_TABS + "[2]")).click();
			Thread.sleep(1000);
		}
		
		List<WebElement> dayList = driver.findElements(By.xpath(XPATH_STATS_DAYDETAILLIST));
		if(dayList.size() > 0 ) {
			dayList.get(0).findElement(By.xpath("./td[6]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
			
			//popup msg
			if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY + "//div[@class='desc']")).getText()
					.contentEquals(MSG_STATS_DAYDETAIL_POPUP)) {
				failMsg = failMsg + "\n1. popup msg [Expected]" + MSG_STATS_DAYDETAIL_POPUP
						 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY + "//div[@class='desc']")).getText();
			}
			
			driver.findElement(By.xpath("//button[@class='close']")).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		} else {
			failMsg = failMsg + "\n2. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void StatsView_dailyTab_daySearch() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		user.selectSideMenu(driver, 4, 0);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_STATS_TABS)));
		
		if(!driver.findElement(By.xpath(XPATH_STATS_TABS + "[2]")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(XPATH_STATS_TABS + "[2]")).click();
			Thread.sleep(1000);
		}
		
		LocalDateTime nowDateTime = LocalDateTime.now();
		String startD = "";
		String endD = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		//click 30
		driver.findElement(By.xpath("//button[@id='month']")).click();
		Thread.sleep(500);
		startD = driver.findElement(By.xpath(XPATH_STATS_DATEPICKER_START)).getAttribute("data-valid");
		endD = driver.findElement(By.xpath(XPATH_STATS_DATEPICKER_END)).getAttribute("data-valid");
		LocalDateTime st = LocalDateTime.ofInstant(new Date(Long.parseLong(startD)).toInstant(), ZoneId.systemDefault());
		LocalDateTime et = LocalDateTime.ofInstant(new Date(Long.parseLong(endD)).toInstant(), ZoneId.systemDefault());

		if(!nowDateTime.minusDays(29).format(formatter).contentEquals(st.format(formatter))) {
			failMsg = failMsg + "\n1-1. start date(30day) [Expected]" + nowDateTime.minusDays(29).format(formatter)
					 + " [Actual]" + st.format(formatter);
		}
		if(!nowDateTime.format(formatter).contentEquals(et.format(formatter))) {
			failMsg = failMsg + "\n1-2. end date(30day) [Expected]" + nowDateTime.format(formatter)
					 + " [Actual]" + et.format(formatter);
		}
		
		//click 60
		driver.findElement(By.xpath("//button[@id='threeMonths']")).click();
		Thread.sleep(500);
		startD = driver.findElement(By.xpath(XPATH_STATS_DATEPICKER_START)).getAttribute("data-valid");
		endD = driver.findElement(By.xpath(XPATH_STATS_DATEPICKER_END)).getAttribute("data-valid");
		st = LocalDateTime.ofInstant(new Date(Long.parseLong(startD)).toInstant(), ZoneId.systemDefault());
		et = LocalDateTime.ofInstant(new Date(Long.parseLong(endD)).toInstant(), ZoneId.systemDefault());

		if(!nowDateTime.minusDays(59).format(formatter).contentEquals(st.format(formatter))) {
			failMsg = failMsg + "\n2-1. start date(60day) [Expected]" + nowDateTime.minusDays(59).format(formatter)
					 + " [Actual]" + st.format(formatter);
		}
		if(!nowDateTime.format(formatter).contentEquals(et.format(formatter))) {
			failMsg = failMsg + "\n2-2. end date(60day) [Expected]" + nowDateTime.format(formatter)
					 + " [Actual]" + et.format(formatter);
		}		
		
		//click 30
		driver.findElement(By.xpath("//button[@id='halfYear']")).click();
		Thread.sleep(500);
		startD = driver.findElement(By.xpath(XPATH_STATS_DATEPICKER_START)).getAttribute("data-valid");
		endD = driver.findElement(By.xpath(XPATH_STATS_DATEPICKER_END)).getAttribute("data-valid");
		st = LocalDateTime.ofInstant(new Date(Long.parseLong(startD)).toInstant(), ZoneId.systemDefault());
		et = LocalDateTime.ofInstant(new Date(Long.parseLong(endD)).toInstant(), ZoneId.systemDefault());

		if(!nowDateTime.minusDays(89).format(formatter).contentEquals(st.format(formatter))) {
			failMsg = failMsg + "\n3-1. start date(90day) [Expected]" + nowDateTime.minusDays(89).format(formatter)
					 + " [Actual]" + st.format(formatter);
		}
		if(!nowDateTime.format(formatter).contentEquals(et.format(formatter))) {
			failMsg = failMsg + "\n3-2. end date(90day) [Expected]" + nowDateTime.format(formatter)
					 + " [Actual]" + et.format(formatter);
		}			
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
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


