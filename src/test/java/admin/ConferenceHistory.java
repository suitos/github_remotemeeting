package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/* ConferenceHistory
 * 1. 회의내역 화면 이동 확인
 * 2. 회의내역 화면 리스트 컬럼 라벨 확인
 * 3. 회의 내역 검색 날짜 확인. 최대 1년전까지만 가능하다
 * 4. 회의 내역 검색 날짜 확인. 검색하려는 날짜는 최대 90일까지 가능. 90초과 검색시 토스트 발생
 * 5. 회의 내역 리스트 참여자 정보 버튼 클릭시 참여자 정보 팝업 뜸 확인
 */

public class ConferenceHistory {
	public static String XPATH_HISTORY_LIST = "//table[@id='retrieveTable']//tbody[@id='companyListWrapper']/tr";
	public static String XPATH_HISTORY_LIST_COLUMNS = "//table[@id='retrieveTable']//tr[@role='row']/th";
	public static String XPATH_HISTORY_SEARCH_DATESTART = "//input[@id='startDate']";
	public static String XPATH_HISTORY_SEARCH_DATEEND = "//input[@id='endDate']";
	public static String XPATH_HISTORY_SEARCH_BTN = "//button[@type='submit']";
	
	public static String MSG_HISTORY_MAX_SEARCHDATE = "검색은 최대 90일까지 가능합니다.";
	
	public static String URL_HISTORY = "/customer/conference-log";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);

	}
	
	@BeforeMethod
	public void setBrowser() {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() > 1) {
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));
		}
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_HISTORY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 7, 1);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_HISTORY_LIST)));
		}
	}
	
	@Test(priority = 1, enabled = true)
	public void History_view() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Users user = new Users();
		user.selectSideMenu(driver, 7, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_HISTORY_LIST)));
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_HISTORY)) {
			failMsg = failMsg + "\n1. no conference history view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void History_listLabel() throws Exception {
		String failMsg = "";

		List<WebElement> columns = driver.findElements(By.xpath(XPATH_HISTORY_LIST_COLUMNS));
		
		if(columns.size() == 0 ) {
			failMsg = failMsg + "\n1. list column size is 0";
		} else {
			String columnLabels[] = {"날짜", "시작시간", "사용시간", "시작시간(과금기준)", "사용시간(과금기준)", "방번호", "최대 참여자수", "개설자", "참여자 정보"};
			
			for (int i = 0; i < columns.size(); i++) {
				if(!columns.get(i).getText().contentEquals(columnLabels[i])) {
					failMsg = failMsg + "\n2-" + i + ". column label [Expected]" + columnLabels[i]
							+ " [Actual]" + columns.get(i).getText();
				}
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 3, enabled = true)
	public void History_searchDate() throws Exception {
		String failMsg = "";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].removeAttribute(\"readonly\");", driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)));
		
		LocalDateTime nowDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String lastyear = formatter.format(nowDateTime.minusDays(365));
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).clear();
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).sendKeys(lastyear);
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATEEND)).click();;
		Thread.sleep(500);
		if(!driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).getAttribute("value").contentEquals(lastyear)) {
			failMsg = failMsg + "\n1. searchable date : " + driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).getAttribute("value");
		}
		
		//1년 이상 날짜 설정 시도
		lastyear = formatter.format(nowDateTime.minusMonths(13));
		System.out.println("test date : " + lastyear);
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).clear();
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).sendKeys(lastyear);
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATEEND)).click();;
		Thread.sleep(500);
		if(driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).getAttribute("value").contentEquals(lastyear)) {
			failMsg = failMsg + "\n1. searchable date : " + driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void History_searchInvalidDate() throws Exception {
		String failMsg = "";

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].removeAttribute(\"readonly\");", driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)));
		
		//90일 초과 설정
		LocalDateTime nowDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String lastyear = formatter.format(nowDateTime.minusDays(91));
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).clear();
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).sendKeys(lastyear);
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATEEND)).click();
		
		//click search
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getAttribute("innerText").contentEquals(MSG_HISTORY_MAX_SEARCHDATE)) {
			failMsg = failMsg + "\n1. searchable date msg [Expected]" + MSG_HISTORY_MAX_SEARCHDATE 
					+ " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getAttribute("innerText");
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		lastyear = formatter.format(nowDateTime.minusDays(89));
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).clear();
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATESTART)).sendKeys(lastyear);
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_DATEEND)).click();
		//click search
		driver.findElement(By.xpath(XPATH_HISTORY_SEARCH_BTN)).click();
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
			failMsg = failMsg + "\n3. search fail, startdate : " + lastyear ;
		} catch (Exception e) {
			//do not anything : valid status
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void History_listViewBtn() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		List<WebElement> list = driver.findElements(By.xpath(XPATH_HISTORY_LIST));
		
		if(list.size() == 0) {
			failMsg = failMsg + "\n1. list size is 0";
		} else {
			list.get(0).findElement(By.xpath("./td[9]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
			
			if(!driver.findElement(By.xpath("//div[@class='modal-header']/h4")).getText().contentEquals("참여자 정보")) {
				failMsg = failMsg + "\n1. list view button popup title [Expected]참여자 정보 [Actual]" 
						+ driver.findElement(By.xpath("//div[@class='modal-header']/h4")).getText();
			}
			
			List<WebElement> userlist = driver.findElements(By.xpath(Connect.XPATH_MODAL_BODY + "//tbody[@id='participantsView']/tr"));
			boolean finduser = false;
			for (WebElement webElement : userlist) {
				if(webElement.findElement(By.xpath("./td[1]")).getText().contentEquals(CommonValues.ADMINNICKNAME)) {
					finduser = true;
				}
			}
			
			if(!finduser) {
				failMsg = failMsg + "\n2. cannot find user in list";
			}
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
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


