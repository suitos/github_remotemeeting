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
 * 2.회사 정보 수정 선택 후 화면 이동
 * 3.이전 로고 이미지 확인 및 삭제
 * 4.서식에 맞지 않은 파일 설정 후 문구 확인
 * 5.올바른 파일 등록
 * 6.청구서 정보 확인(이미지 적용됬는지)
 * 7.신규등록 선택 후 팝업 확인
 * 8.케이스별 에러 메세지 확인
 * 9.해당 팝업 취소 클릭 후 닫힘 확인
 * 10.케이스별 팝업 및 기본값 확인
 * 11.미인증 계정 로그인 시도
 * 12.비밀번호 초기화 선택 후 팝업 확인 및 취소 클릭 후 닫힘 확인
 * 13.비밀번호 초기화
 * 14.인증 계정 로그인 시도
 * 15.비밀번호 변경 선택 후 팝업 확인
 * 16.비밀번호 변경 시 null값 입력 시 메세지 확인
 * 17.현재 비밀번호 틀리게 입력시 토스트 메세지 확인
 * 18.변경할 비밀번호와 비밀번호 확인값 다를 경우 문구 확인
 * 19.해당 팝업 취소 클릭 후 닫힘 확인
 * 20.비밀번호 변경
 * 21.사용자 삭제
 */

public class Mypage {
	
	public static String AlertMsg = "형식에 맞지 않는 파일을 선택하셨습니다.\n" + "(지원형식 : png, jpg, gif)";
	public static String AlertMsg2 = "사용 가능합니다.";
	public static String AlertMsg3 = "이메일이나 비밀번호를 확인해 주세요.";
	public static String AlertMsg4 = "비밀번호를 초기화 하시겠습니까? \n" + "초기화 비밀번호는 '111111' 입니다.";
	public static String AlertMsg5 = "비밀번호를 초기화 하였습니다.";
	public static String AlertMsg6 = "현재 비밀번호를 확인해 주세요.";
	public static String AlertMsg7 = "정말 삭제하시겠습니까?";
	
	private static String search_URL = "https://stpartners.remotemeeting.com/payment?search.dateCondition=ALL&search.penalty=false&search.planFor=&search.paymentStatus=S&search.paymentMethod=&search.partnerName=rsupkor&search.partnerId=000000005def4da1015def73bb580000&search.keywordCondition=COMPANY_NAME&search.keyword=&_search.total=on";
	
	public static WebDriver driver;
	public static WebDriver User_driver;

	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		driver = comm2.setDriver(driver, browsertype, "lang=ko_KR", true);
		User_driver = comm2.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", User_driver);
		
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
	public void goMypage() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.MYPAGE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "정보 수정"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']/h2")).getText().contentEquals("정보 수정")) {
			failMsg = "1.Wrong Menu [Expected] 정보 수정 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']/h2")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.MYPAGE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.MYPAGE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void checkPrevImage() throws Exception {
		String failMsg = "";
		
		if(driver.findElement(By.xpath("//div[@class='logo-contents']/img")).getAttribute("src") != null) {
			
			String savedsrc = driver.findElement(By.xpath("//div[@class='logo-contents']/img")).getAttribute("src");
			
			String filePath = mandatory.CommonValues.TESTFILE_PATH;
			if (System.getProperty("os.name").toLowerCase().contains("mac"))
				filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
			String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[4];

			driver.findElement(By.xpath("//input[@id='logoImage']")).sendKeys(addedfile);
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.attributeContains(By.xpath("//div/img"), "src", "data"));
			
			driver.findElement(By.xpath("//button[@class='btn btn-default btn-table return-logo-btn']")).click();
			
			wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@class='logo-contents']/img"), "src", savedsrc));
			
			if(!driver.findElement(By.xpath("//div[@class='logo-contents']/img")).getAttribute("src").contentEquals(savedsrc)) {
				failMsg = "Prev image is not display";
			}	
		}else {
			failMsg = "saved image is not exist";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void saveWrongPartnerLogoImage() throws Exception {
		String failMsg = "";
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[0];

		driver.findElement(By.xpath("//input[@id='logoImage']")).sendKeys(addedfile);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void savePartnerLogoImage() throws Exception {
		String failMsg = "";
		
		String savedsrc = driver.findElement(By.xpath("//div[@class='logo-contents']/img")).getAttribute("src");
		
		String filePath = mandatory.CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac"))
			filePath = mandatory.CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + mandatory.CommonValues.TESTFILE_LIST[5];

		driver.findElement(By.xpath("//input[@id='logoImage']")).sendKeys(addedfile);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div/img"), "src", "data"));
		
		driver.findElement(By.xpath("//button[@id='partner-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		if(driver.findElement(By.xpath("//div[@class='logo-contents']/img")).getAttribute("src").contentEquals(savedsrc)) {
			failMsg = "Don't saved image";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void checkBillsrc() throws Exception {
		String failMsg = "";
		
		String savedsrc = driver.findElement(By.xpath("//div[@class='logo-contents']/img")).getAttribute("src");
		
		driver.get(search_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("결제 관리")) {
			failMsg = "1.Wrong Menu [Expected] 결제 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		List <WebElement> billbtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-success partner-customer-bill']"));
		billbtn.get(0).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='modalBillBody']")));
		
		String bill_src = driver.findElement(By.xpath("//div[@class='modal-header']/img")).getAttribute("src");
		
		if(!bill_src.contentEquals(savedsrc)) {
			failMsg = failMsg + "\n2.Wrong src [Expected]" + savedsrc + " [Actual]" + bill_src;  
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void checkAdduserPopup() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.MYPAGE_URI);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "정보 수정"));

		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new user']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		if(!driver.findElement(By.xpath("//form[@id='user']")).isDisplayed()) {
			failMsg = "add user popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void checkAdduserErrorMsg() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='user-save']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBe(By.xpath("//span[@id='email-message']"), "이메일 중복확인을 해주세요."));

		if (!driver.findElement(By.xpath("//span[@id='email-message']")).getText().contentEquals("이메일 중복확인을 해주세요.")
				|| !driver.findElement(By.xpath("//span[@id='name-error-message']")).getText().contentEquals("필수 입력란입니다.")
				|| !driver.findElement(By.xpath("//span[@id='department-message']")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = "Error Msg is wrong";
		}
		
		driver.findElement(By.xpath("//button[@class='btn btn-default btn-overlap user-email-btn']")).click();
		wait.until(ExpectedConditions.textToBe(By.xpath("//span[@id='email-message']"), "필수 입력란입니다."));

		if (!driver.findElement(By.xpath("//span[@id='email-message']")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = failMsg + "\n2.Error Msg is wrong [Expected] 필수 입력란입니다. [Actual]" +  driver.findElement(By.xpath("//span[@id='email-message']")).getText();
		}
		
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys("aaaaa");
		driver.findElement(By.xpath("//button[@class='btn btn-default btn-overlap user-email-btn']")).click();
		
		wait.until(ExpectedConditions.textToBe(By.xpath("//span[@id='email-message']"), "올바른 이메일 형식으로 입력해주세요."));

		if (!driver.findElement(By.xpath("//span[@id='email-message']")).getText().contentEquals("올바른 이메일 형식으로 입력해주세요.")) {
			failMsg = failMsg + "\n3.Error Msg is wrong [Expected] 올바른 이메일 형식으로 입력해주세요. [Actual]" + driver.findElement(By.xpath("//span[@id='email-message']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void cancelAdduser() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		if(!driver.findElements(By.xpath("//form[@id='user']")).isEmpty()) {
			failMsg = "Don't cancel user add popup";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void checkAdduserAndcheckValue() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new user']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(CommonValues.USERS[3]);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@class='btn btn-default btn-overlap user-email-btn']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		if(!driver.findElement(By.xpath("//select[@id='roleId']/option")).getText().contentEquals("지사 관리자")) {
			failMsg = failMsg + "\n2. default value is wrong [Expected] 지사 관리자 [Actual]" + driver.findElement(By.xpath("//select[@id='roleId']/option")).getText();
		}
		
		driver.findElement(By.xpath("//div[2]/input[@name='name']")).sendKeys("test");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@name='department']")).sendKeys("testQA");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[4]//label/div")).click();
		
		driver.findElement(By.xpath("//button[@id='user-save']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		if(driver.findElements(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]")).isEmpty()) {
			failMsg = failMsg + "\n3.Don't saved user";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void checkDisabledUserLogin() throws Exception {
		String failMsg = "";
		
		User_driver.get(CommonValues_Partners.PARTNER_URL);

		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));

		if (!User_driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + User_driver.getCurrentUrl();
		}

		comm.login(User_driver, CommonValues.USERS[3], "111111");
		
		Alert alert = User_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg3)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg3 + " [Actual]" + alert_msg;
		}
		
		comm2.waitForLoad(User_driver);
		Thread.sleep(1000);
		
		if (!User_driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + User_driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void cancelResetPW() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg4)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg4 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void ResetPW() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		wait.until(ExpectedConditions.alertIsPresent());
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg5)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg5 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void checkabledUserLogin() throws Exception {
		
		driver.findElement(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		driver.findElement(By.xpath("//div[4]//label/div")).click();
		
		driver.findElement(By.xpath("//button[@id='user-save']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		comm.login(User_driver, CommonValues.USERS[3], "111111");
		
	}
	
	@Test(priority = 15, enabled = true)
	public void checkChangePWPopup() throws Exception {
		String failMsg = "";
		
		User_driver.findElement(By.xpath(CommonValues_Partners.MYPAGE_BTN)).click();

		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "정보 수정"));

		if (!User_driver.findElement(By.xpath("//div[@class='panel-header']/h2")).getText().contentEquals("정보 수정")) {
			failMsg = "1.Wrong Menu [Expected] 정보 수정 [Actual]"
					+ User_driver.findElement(By.xpath("//div[@class='panel-header']/h2")).getText();
		}
		
		User_driver.findElement(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr//button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='changeRequestVO']")));
		
		if(!User_driver.findElement(By.xpath("//h4[@class='modal-title']")).getText().contentEquals("비밀번호 변경")) {
			failMsg = "Wrong Popup [Expected] 비밀번호 변경 [Actual]" + User_driver.findElement(By.xpath("//h4[@class='modal-title']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 16, enabled = true)
	public void insertChangePW_null() throws Exception {
		String failMsg = "";
		
		User_driver.findElement(By.xpath("//button[@id='save']")).click();
		
		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.textToBe(By.xpath("//span[@id='oldPassword-message']"), "필수 입력란입니다."));
		
		if(!User_driver.findElement(By.xpath("//span[@id='oldPassword-message']")).getText().contentEquals("필수 입력란입니다.") ||
				!User_driver.findElement(By.xpath("//span[@id='newPassword-message']")).getText().contentEquals("필수 입력란입니다.") ||
				!User_driver.findElement(By.xpath("//span[@id='confirm-newPassword-message']")).getText().contentEquals("필수 입력란입니다.")) {
			failMsg = "Error Msg is wrong [Expected] 필수 입력란입니다.";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void insertChangePW_invalid() throws Exception {
		String failMsg = "";
		
		User_driver.findElement(By.xpath("//input[@id='oldPassword']")).sendKeys("222222");
		User_driver.findElement(By.xpath("//input[@id='newPassword']")).sendKeys("222222");
		User_driver.findElement(By.xpath("//input[@id='confirm-newPassword']")).sendKeys("222222");
		
		Thread.sleep(1000);
		User_driver.findElement(By.xpath("//button[@id='save']")).click();
		
		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = User_driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg6)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg6 + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void insertChangePW_wrong() throws Exception {
		String failMsg = "";
		
		User_driver.findElement(By.xpath("//input[@id='confirm-newPassword']")).sendKeys("222222");
		User_driver.findElement(By.xpath("//button[@id='save']")).click();
		
		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.textToBe(By.xpath("//span[@id='newPassword-message']"), "입력한 비밀번호와 재입력한 비밀번호가 일치하지 않습니다. 다시 확인해 주세요."));
		
		if(!User_driver.findElement(By.xpath("//span[@id='newPassword-message']")).getText().contentEquals("입력한 비밀번호와 재입력한 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.")) {
			failMsg = "Error Msg is wrong [Expected] 필수 입력란입니다. [Actual]" + User_driver.findElement(By.xpath("//span[@id='newPassword-message']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19, enabled = true)
	public void closeChangePWPopup() throws Exception {
		String failMsg = "";
		
		User_driver.findElement(By.xpath("//div[3]/button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='changeRequestVO']")));
		
		if(!User_driver.findElements(By.xpath("//form[@id='changeRequestVO']")).isEmpty()) {
			failMsg = "ChangePW Popup is not close";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void ChangePW() throws Exception {
		
		User_driver.findElement(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr//button")).click();
		
		WebDriverWait wait = new WebDriverWait(User_driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='changeRequestVO']")));
		
		User_driver.findElement(By.xpath("//input[@id='oldPassword']")).sendKeys("111111");
		User_driver.findElement(By.xpath("//input[@id='newPassword']")).sendKeys("222222");
		User_driver.findElement(By.xpath("//input[@id='confirm-newPassword']")).sendKeys("222222");
		
		User_driver.findElement(By.xpath("//button[@id='save']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//form[@id='changeRequestVO']")));
		
		comm.logout(User_driver);
		
		comm.login(User_driver, CommonValues.USERS[3], "222222");
		
	}
	
	@Test(priority = 21, enabled = true)
	public void deleteAdduser() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr//a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='user']")));
		
		driver.findElement(By.xpath("//button[@id='user-delete']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg7)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg7 + " [Actual]" + alert_msg;
		}
		
		comm2.waitForLoad(driver);
		Thread.sleep(1000);
		
		if(!driver.findElements(By.xpath("//td[contains(text(),'rmrsup5@gmail.com')]/ancestor::tr")).isEmpty()) {
			failMsg = failMsg + "Don't delete User";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		User_driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
}
