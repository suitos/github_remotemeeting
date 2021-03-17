package partners;

import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
 * 2.고객사 검색
 * 3.요금제 확인
 * 4.접속 페이지 URI 수정 후 복사
 * 5.접속 페이지 내 URI 확인
 * 6.접속 페이지 URI 숫자만 사용 불가능한지 확인
 */

public class Function {
	
	public static String AlertMsg = "접속 주소가 복사되었습니다.";
	
	public String connectPageURL;
	public String URI = "test123";
	
	public static WebDriver driver;
	public static WebDriver Con_driver;

	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Con_driver = comm.setDriver(Con_driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", Con_driver);
		
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
	public void searchCompanyforFunction() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.COMPANY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "고객사 관리"));
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys("자동화테스트용.function");
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//td[2]/a")), "자동화테스트용.function"));
		
		driver.findElement(By.xpath("//td[2]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
	}
	
	@Test(priority = 3, enabled = true)
	public void checkPlanforFunction() throws Exception {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		String Enddate = driver.findElement(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[1]/td[2]")).getText();
		
		if(today.contentEquals(Enddate)) {
			driver.findElement(By.xpath("//tbody[@id='handleTableLicenseRow']/tr[1]//a")).click();
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "라이선스 관리 > 상세정보"));
			
			driver.findElement(By.xpath("//input[@id='endDate']")).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
			
			driver.findElement(By.xpath("//div[1]//th[(@class='next')]")).click();
			
			driver.findElement(By.xpath("//td[(text()='10')]")).click();
			Thread.sleep(1000);
			
			driver.findElement(By.xpath("//button[@id='license-save']")).click();
			
			driver.navigate().back();
			
			wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));	
		}
		
	}
	
	@Test(priority = 4, enabled = true)
	public void copyconnectPageURL() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//i[@id='clipboard']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		connectPageURL = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		System.out.println(connectPageURL);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void connectPageURL() throws Exception {
		String failMsg = "";
		
		Con_driver.get(connectPageURL);
		
		WebDriverWait wait = new WebDriverWait(Con_driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-box dark-mode']")));
		
		Con_driver.findElement(By.xpath("//input[@id='email']")).sendKeys(CommonValues.USERS[4]);
		Con_driver.findElement(By.xpath("//input[@id='password']")).sendKeys(CommonValues.USERPW);
		
		Con_driver.findElement(By.xpath("//button[@class='btn btn-m btn-primary wide']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='gnb-admin']")));
		
		if(!Con_driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = "Wrong Url [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL + " [Actual]" + Con_driver.getCurrentUrl();
		}
		
		Con_driver.findElement(By.xpath("//div[@id='gnb-admin']/a")).click();
		
		ArrayList<String> tabs = new ArrayList<String> (Con_driver.getWindowHandles());
		Con_driver.close();
		Con_driver.switchTo().window(tabs.get(1));
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/customer/view']")));
		
		Con_driver.findElement(By.xpath("//a[@href='/customer/view']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(Con_driver.findElement(By.xpath("//div[@class='panel-header']")), "그룹정보"));
		
		if(!Con_driver.findElement(By.xpath("//input[@id='customAdress']")).getAttribute("value").contentEquals(URI)) {
			failMsg = failMsg + "\n2.Wrong URI in admin page [Expected]" + URI + " [Actual]" + Con_driver.findElement(By.xpath("//input[@id='customAdress']")).getAttribute("value");
		}
		
		driver.findElement(By.xpath("//input[@id='customAdress']")).sendKeys("1234");
		
		driver.findElement(By.xpath("//button[@id='adressOverlap']")).click();
		
		WebDriverWait wait2 = new WebDriverWait(driver, 10);
		wait2.until(ExpectedConditions.alertIsPresent());
		
		driver.switchTo().alert().accept();
		
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		comm2.waitForLoad(driver);
		
		Con_driver.navigate().refresh();
		
		comm2.waitForLoad(Con_driver);
		
		if(!Con_driver.findElement(By.xpath("//input[@id='customAdress']")).getAttribute("value").contentEquals(URI + "1234")) {
			failMsg = failMsg + "\n3.Don't changed";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void resetconnectPageURL() throws Exception {
		String failMsg = "";
		
		while(!driver.findElement(By.xpath("//input[@id='customAdress']")).getAttribute("data-before").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='customAdress']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='customAdress']")).sendKeys("111111");
		
		if(!driver.findElement(By.xpath("//button[@id='adressOverlap']")).getAttribute("disabled").contentEquals("true")) {
			failMsg = "Can insert only num";
		}
		
		while(!driver.findElement(By.xpath("//input[@id='customAdress']")).getAttribute("data-before").isEmpty()) {
			driver.findElement(By.xpath("//input[@id='customAdress']")).sendKeys(Keys.BACK_SPACE);
		}
		
		driver.findElement(By.xpath("//input[@id='customAdress']")).sendKeys(URI);
		
		driver.findElement(By.xpath("//button[@id='adressOverlap']")).click();
		
		WebDriverWait wait2 = new WebDriverWait(driver, 10);
		wait2.until(ExpectedConditions.alertIsPresent());
		
		driver.switchTo().alert().accept();
		
		driver.findElement(By.xpath("//button[@id='company-save']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		Con_driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
}
