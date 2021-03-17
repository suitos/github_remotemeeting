package partners;

import static org.testng.Assert.fail;

import java.util.List;

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
 * 2.메일 발송 이력 화면 이동
 * 3.고객사명 조건 검색 확인
 * 4.고객 코드 조건 검색 확인
 * 5.발신자 이메일 조건 검색 확인
 * 6.수신자 이메일 조건 검색 확인
 * 7.메일 종류 조건 검색 확인
 * 8.성공 여부 조건 검색 확인
 */

public class Mailsend {
	
	public static WebDriver driver;
	
	private static String Companyname = "자동화테스트용";
	private static String Code = "2c908a9176926493017692bcee930000"; //자동화테스트용 고객코드
	private static String email = "rmrsupadm@gmail.com";
	
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
	public void goMailsend() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.MAILSEND_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "메일발송이력"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("메일발송이력")) {
			failMsg = "1.Wrong Menu [Expected] 메일발송이력 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.MAILSEND_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.MAILSEND_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void searchMailsend_companyname() throws Exception {
		
		searchdata(1,Companyname);
		checkData(1,Companyname);
	}
	
	@Test(priority = 4, enabled = true)
	public void searchMailsend_companycode() throws Exception {
		
		searchdata(2,Code);
		checkData(2,Code);
	}
	
	@Test(priority = 5, enabled = true)
	public void searchMailsend_Senderemail() throws Exception {
		
		searchdata(3,email);
		checkData(3,email);
	}
	
	@Test(priority = 6, enabled = true)
	public void searchMailsend_Receiveremail() throws Exception {
		
		searchdata(4,email);
		checkData(4,email);
	}
	
	@Test(priority = 7, enabled = true)
	public void searchMailsend_kind() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.MAILSEND_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "메일발송이력"));
		
		driver.findElement(By.xpath("//button[@class='search-toggle-btn']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));
		
		changeSearchcondition(1,2);
		checkData(5, "회의초대");
		
		changeSearchcondition(1,3);
		checkData(5, "회의록공유");
		
		changeSearchcondition(1,4);
		checkData(5, "유저등록");
		
		changeSearchcondition(1,5);
		checkData(5, "회원가입");

		changeSearchcondition(1,6);
		checkData(5, "패스워드 변경");
		
		changeSearchcondition(1,7);
		checkData(5, "탈퇴");
		
		changeSearchcondition(1,8);
		checkData(5, "메일 유효성 체크");
		
		changeSearchcondition(1,9);
		checkData(5, "예약 메일 발송");
	}
	
	@Test(priority = 8, enabled = true)
	public void searchMailsend_bool() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.MAILSEND_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "메일발송이력"));
		
		driver.findElement(By.xpath("//button[@class='search-toggle-btn']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));
		
		changeSearchcondition(2,2);
		checkData(6, "true");
		
		changeSearchcondition(2,3);
		checkData(6, "false");
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
	public void searchdata(int i, String data) {
		//1=CompanyName, 2=Code, 3=발신자Email, 4=수신자Email
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']/option[" + i +"]")).click();
		
		WebElement input = driver.findElement(By.xpath("//input[@id='search-keywordString']"));
		
		while(!input.getAttribute("value").isEmpty() || !input.getText().isEmpty()) {
			input.sendKeys(Keys.BACK_SPACE); }
		
		input.sendKeys(data);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		comm2.waitForLoad(driver);
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.get(0).findElement(By.xpath(".//td")).getText().contentEquals("데이터가 없습니다.")) {
			driver.findElement(By.xpath("//button[@data-term='-90']")).click();
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.attributeContains(By.xpath("//button[@id='halfYear']"), "class", "btn btn-days btn-term active"));
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			comm2.waitForLoad(driver);
		}
	}
	
	public void changeSearchcondition(int i, int j) {
		
		if(i == 1) {
			driver.findElement(By.xpath("//select[@id='search.mailFor']")).click();
			driver.findElement(By.xpath("//select[@id='search.mailFor']/option[" + j + "]")).click();		
		}
		else {
			driver.findElement(By.xpath("//select[@id='search.mailSendSuccessFlag']")).click();
			driver.findElement(By.xpath("//select[@id='search.mailSendSuccessFlag']/option[" + j + "]")).click();
		}
		
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		comm2.waitForLoad(driver);
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.get(0).findElement(By.xpath(".//td")).getText().contentEquals("데이터가 없습니다.")) {
			driver.findElement(By.xpath("//button[@data-term='-90']")).click();
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.attributeContains(By.xpath("//button[@id='halfYear']"), "class", "btn btn-days btn-term active"));
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			comm2.waitForLoad(driver);
		}

	}
	
	public void checkData(int i, String data) throws Exception {
		//1=Code, 2=CompanyName, 3=발신자Email, 4=수신자Email
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		String result;
		
		for (int j = 0; j < list.size(); j++) {

			switch (i) {
			case 1:
				result = list.get(j).findElement(By.xpath(".//td[2]")).getText();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 2:
				result = list.get(j).findElement(By.xpath(".//td[2]")).getText();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 3:
				
				result = list.get(j).findElement(By.xpath(".//td[4]")).getText();

				if (!result.contentEquals(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 4:
			
			result = list.get(j).findElement(By.xpath(".//td[5]")).getText();

			if (!result.contentEquals(data)) {
				Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
				throw e;
			}
			break;

			case 5:
			
			result = list.get(j).findElement(By.xpath(".//td[3]")).getText();

			if (!result.contentEquals(data)) {
				Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
				throw e;
			}
			break;
			
			case 6:
				
				result = list.get(j).findElement(By.xpath(".//td[6]")).getText();

				if (!result.contentEquals(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			}
		}
	}

}
