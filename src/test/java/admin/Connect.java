package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import mandatory.CommonValues;
import mandatory.Free;


/* Connect
 * 1. 브랜드 사이트 일본어상태에서 로그인 > 어드민 접속
 * 2. 브랜드 사이트 영어상태에서 로그인 > 어드민 접속
 * 3. 브랜드 사이트 한국어상태에서 로그인 > 어드민 접속
 * 
 * 5. 어드민 로그인 화면 확인 - 입력없이 로그인 시도 시 에러 확인
 * 6. 비밀번호 찾기 이동 - 이메일 빈채로 확인
 * 7. 비밀번호 찾기 이동 - 형식에 맞지 않는 값 입력 - 정상 값 입력
 * 
 * 10. 정상 이메일 + 잘못된 비밀번호 로그인 시도
 * 11. 인증되지 않은 이메일로 로그인시도
 * 12. 정상 계정 로그인
 * 
 * 20. 유저 없는 관리자 로그인 - 사용자 등록 클릭
 * 21. 유저 없는 관리자 로그인 - 사용자 등록 취소
 * 
 * 30. 파트너 아이디로 로그인
 * 31. 로그인 - 프로필 정보 확인 - 로그아웃
 */

public class Connect {
	public static String XPATH_DASHBOARD_USAGE_TITLE = "//article[@id='compareUsage']/div[@class='panel-title']";
	public static String XPATH_DASHBOARD_COUNT_TITLE  = "//article[@id='compareCount']/div[@class='panel-title']";
	
	public static String XPATH_MODAL_BODY  = "//div[@class='modal-body']";
	public static String XPATH_MODAL_RESULTBODY  = "//div[@class='result-body modal-body']";
	public static String XPATH_MODAL_FOOTER_Y  = "//div[@class='modal-footer']/a[1]";
	public static String XPATH_MODAL_FOOTER_N  = "//div[@class='modal-footer']/a[2]";
	public static String XPATH_PANEL_HEADER  = "//div[@class='panel-header']";
	
	public static String XPATH_ADMIN_PROFILE_INFO_BTN  = "//div[@id='profile-info']/div[@id='user-image-mini']";
	public static String XPATH_ADMIN_LOGOUT_BTN = "//a[@class='btn-logout']";
	
	public static String XPATH_ADMIN_LOGIN_EMAIL = "//input[@id='j_username']";
	public static String XPATH_ADMIN_LOGIN_PW = "//input[@id='j_password']";
	public static String XPATH_ADMIN_LOGIN_BTN = "//button[@type='submit']";
	public static String XPATH_ADMIN_FINDPW_BTN = "//a[@id='passwordReset']";
	
	public static String XPATH_ADMIN_FINDPW_FORM = "//form[@id='password-find-form']";
	public static String XPATH_ADMIN_FINDPW_EMAIL = "//input[@id='email']";
	public static String XPATH_ADMIN_FINDPW_CONFIRM = XPATH_ADMIN_FINDPW_FORM +"//button[@type='submit']";
	public static String XPATH_ADMIN_FINDPW_EMAIL_ERROR = "//em[@id='email-error']";
	
	public static String XPATH_ADMIN_LOGIN_EMAIL_ERROR= "//span[@id='j_username-error']";
	public static String XPATH_ADMIN_LOGIN_PW_ERROR = "//span[@id='j_password-error']";
	public static String XPATH_ADMIN_LOGIN_MISS_ERROR = "//div[@id='notMatching']";
	
	public static String MSG_DASHBOARD_TITLES_KO = "사용량, 회의 개수";
	public static String MSG_DASHBOARD_TITLES_JA = "利用量, 会議数";
	public static String MSG_DASHBOARD_TITLES_EN = "Usage Statistics, Meetings";
	
	public static String MSG_LOGIN_EMAIL_ERROR = "이메일을 입력해 주세요.";
	public static String MSG_LOGIN_PW_ERROR = "비밀번호를 입력해 주세요.";
	public static String MSG_LOGIN_MISSMATCH_ERROR = "아이디 또는 비밀번호를 잘못 입력 하셨습니다.";
	public static String MSG_FINDPW_EMAIL_ERROR = "필수 입력사항입니다.";
	public static String MSG_FINDPW_EMAIL_ERROR2 = "이메일이 형식에 맞지 않습니다.";
	public static String MSG_FINDPW_EMAIL_NOAUTH_ERROR = "전송된 이메일에서 인증을 완료해 주세요.";
	public static String MSG_ADMIN_NOUSER_ADM_LOGIN_ERROR = "함께 사용할 그룹 사용자를 등록하세요.";
	public static String MSG_ADMIN_PANEL_HEADER_USER = "사용자 관리";
	public static String MSG_ADMIN_LOGIN_LICENSE_ERROR = "라이선스가 만료되었습니다!\n" + "RemoteMeeting을 이용해 주셔서 감사합니다.\n" + "그룹 관리자에게 문의해 주시기 바랍니다.";
	
	public static String NOAUTH_ADM = "rsrsup10@gmail.com"; //미인증 유저
	public static String NOUSER_ADM = "rsrsup9@gmail.com"; //사용자없는 유저
	public static String FREE_ADM = "rmrsupadm@gmail.com"; //Free사용고객사 유저
	
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
	public void connectJA() throws Exception {
		String failMsg = "";
		
		//admin logout
		logoutAdmin(driver);
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)).click();
		
		//한국어 설정
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FOOTER_LANG_JA)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_JA)).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		//로그인
		CommonValues comm = new CommonValues();
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		//관리자 페이지 클릭
		driver.findElement(By.xpath(CommonValues.XPATH_HEADER_ADMIN_BTN)).click();
		
		//새탭 확인
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() == 2) {
			driver.switchTo().window(tabs.get(0));
			//close 1 tab
			driver.close();
			//switch room tab
			driver.switchTo().window(tabs.get(1));
		} 
		
		String dashboard = String.format("%s, %s", driver.findElement(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)).getText(), 
				driver.findElement(By.xpath(XPATH_DASHBOARD_COUNT_TITLE)).getText());
		
		if(!dashboard.contentEquals(MSG_DASHBOARD_TITLES_JA)) {
			failMsg = "1. dashboard title [Expected]" + MSG_DASHBOARD_TITLES_JA + " [Actual]" + dashboard;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void connectEN() throws Exception {
		String failMsg = "";
		
		//admin logout
		logoutAdmin(driver);
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)).click();
		
		//한국어 설정
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FOOTER_LANG_EN)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_EN)).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		//로그인
		CommonValues comm = new CommonValues();
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		//관리자 페이지 클릭
		driver.findElement(By.xpath(CommonValues.XPATH_HEADER_ADMIN_BTN)).click();
		
		//새탭 확인
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() == 2) {
			driver.switchTo().window(tabs.get(0));
			//close 1 tab
			driver.close();
			//switch room tab
			driver.switchTo().window(tabs.get(1));
		} 
		
		String dashboard = String.format("%s, %s", driver.findElement(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)).getText(), 
				driver.findElement(By.xpath(XPATH_DASHBOARD_COUNT_TITLE)).getText());
		
		if(!dashboard.contentEquals(MSG_DASHBOARD_TITLES_EN)) {
			failMsg = "1. dashboard title [Expected]" + MSG_DASHBOARD_TITLES_EN + " [Actual]" + dashboard;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void connectKO() throws Exception {
		String failMsg = "";
		
		logoutAdmin(driver);
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_BTN)).click();
		
		//한국어 설정
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FOOTER_LANG_KO)));
		driver.findElement(By.xpath(CommonValues.XPATH_FOOTER_LANG_KO)).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Free.XPATH_AIDEMO_BTN)));
		
		//로그인
		CommonValues comm = new CommonValues();
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		//관리자 페이지 클릭
		driver.findElement(By.xpath(CommonValues.XPATH_HEADER_ADMIN_BTN)).click();
		
		//새탭 확인
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() == 2) {
			driver.switchTo().window(tabs.get(0));
			//close 1 tab
			driver.close();
			//switch room tab
			driver.switchTo().window(tabs.get(1));
		} 
		
		String dashboard = String.format("%s, %s", driver.findElement(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)).getText(), 
				driver.findElement(By.xpath(XPATH_DASHBOARD_COUNT_TITLE)).getText());
		
		if(!dashboard.contentEquals(MSG_DASHBOARD_TITLES_KO)) {
			failMsg = "1. dashboard title [Expected]" + MSG_DASHBOARD_TITLES_KO + " [Actual]" + dashboard;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void loginEmpty() throws Exception {
		String failMsg = "";
		
		logoutAdmin(driver);
		
		driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)));
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_EMAIL_ERROR)));
			
			if(!driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL_ERROR)).getText().contentEquals(MSG_LOGIN_EMAIL_ERROR)) {
				failMsg = failMsg + "\n1. login email error msg. [Expected]" + MSG_LOGIN_EMAIL_ERROR
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL_ERROR)).getText();
			}
			if(!driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW_ERROR)).getText().contentEquals(MSG_LOGIN_PW_ERROR)) {
				failMsg = failMsg + "\n2. login email error msg. [Expected]" + MSG_LOGIN_EMAIL_ERROR
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW_ERROR)).getText();
			}
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n3. cannot find error msg";
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 6, enabled = true)
	public void findPW() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_FINDPW_FORM)));
		
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_CONFIRM)).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)));
			if(!driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)).getText().contentEquals(MSG_FINDPW_EMAIL_ERROR)) {
				failMsg = failMsg + "\n1. find pw email error msg. [Expected]" + MSG_FINDPW_EMAIL_ERROR
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)).getText();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n2. cannot find error msg";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 7, dependsOnMethods = {"findPW"}, alwaysRun = true, enabled = true)
	public void findPW_worngEmail() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_FINDPW_FORM)));
		
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL)).sendKeys("worng email");
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_CONFIRM)).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)));
			if(!driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)).getText().contentEquals(MSG_FINDPW_EMAIL_ERROR2)) {
				failMsg = failMsg + "\n1. find pw email error msg. [Expected]" + MSG_FINDPW_EMAIL_ERROR2
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)).getText();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n2. cannot find error msg";
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_FINDPW_EMAIL)).sendKeys(CommonValues.ADMEMAIL);
		
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_ADMIN_FINDPW_EMAIL_ERROR)));		
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n3. not removed error msg";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void login_worngPW() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(CommonValues.ADMEMAIL);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).click();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys("wrongPW");
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)));
			if(!driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)).getText().contentEquals(MSG_LOGIN_MISSMATCH_ERROR)) {
				failMsg = failMsg + "\n1. login error msg.(miss match) [Expected]" + MSG_LOGIN_MISSMATCH_ERROR
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)).getText();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n2. cannot find error msg";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	
	@Test(priority = 11, enabled = true)
	public void login_noauth() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		} else {
			driver.navigate().refresh();
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(NOAUTH_ADM);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)));
			if(!driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)).getText().contentEquals(MSG_FINDPW_EMAIL_NOAUTH_ERROR)) {
				failMsg = failMsg + "\n1. login error msg.(noauth user) [Expected]" + MSG_FINDPW_EMAIL_NOAUTH_ERROR
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)).getText();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n2. cannot find error msg";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void login_valid() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(CommonValues.ADMEMAIL);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)));
		
		String dashboard = String.format("%s, %s", driver.findElement(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)).getText(), 
				driver.findElement(By.xpath(XPATH_DASHBOARD_COUNT_TITLE)).getText());
		
		if(!dashboard.contentEquals(MSG_DASHBOARD_TITLES_KO)) {
			failMsg = "1. dashboard title [Expected]" + MSG_DASHBOARD_TITLES_KO + " [Actual]" + dashboard;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void login_nouser() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(NOUSER_ADM);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_BODY)));

		if(!driver.findElement(By.xpath(XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_ADMIN_NOUSER_ADM_LOGIN_ERROR)) {
			failMsg = failMsg + "1. nouser adm login msg [Expected]" + MSG_ADMIN_NOUSER_ADM_LOGIN_ERROR
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_MODAL_BODY)).getAttribute("innerText");
		}
		
		//click 사용자등록
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER_Y)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PANEL_HEADER)));
		
		if(!driver.findElement(By.xpath(XPATH_PANEL_HEADER)).getAttribute("innerText").contentEquals(MSG_ADMIN_PANEL_HEADER_USER)) {
			failMsg = failMsg + "\n2. nouser adm. not user view [Actual]" + driver.findElement(By.xpath(XPATH_PANEL_HEADER)).getAttribute("innerText");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 21, enabled = true)
	public void login_nouser2() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(NOUSER_ADM);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_BODY)));

		if(!driver.findElement(By.xpath(XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_ADMIN_NOUSER_ADM_LOGIN_ERROR)) {
			failMsg = failMsg + "1. nouser adm login msg [Expected]" + MSG_ADMIN_NOUSER_ADM_LOGIN_ERROR
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_MODAL_BODY)).getAttribute("innerText");
		}
		
		//click 사용자등록
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER_N)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)));
		
		String dashboard = String.format("%s, %s", driver.findElement(By.xpath(XPATH_DASHBOARD_USAGE_TITLE)).getText(), 
				driver.findElement(By.xpath(XPATH_DASHBOARD_COUNT_TITLE)).getText());
		
		if(!dashboard.contentEquals(MSG_DASHBOARD_TITLES_KO)) {
			failMsg = "\n2. nouser adm. not dashboard view";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 30, enabled = true)
	public void login_partner() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(CommonValues.PARTNERKR_EMAIL);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys("111111");
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)));
			if(!driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)).getText().contentEquals(MSG_ADMIN_LOGIN_LICENSE_ERROR)) {
				failMsg = failMsg + "\n1. login error msg.(license error) [Expected]" + MSG_ADMIN_LOGIN_LICENSE_ERROR
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_MISS_ERROR)).getText();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n2. cannot find error msg";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 31, enabled = true)
	public void loginProfile() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			logoutAdmin(driver);
			driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(CommonValues.ADMEMAIL);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		String xpath_prifile = "//ul[@id='more-profile']/li";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_PROFILE_INFO_BTN)));
			
			driver.findElement(By.xpath(XPATH_ADMIN_PROFILE_INFO_BTN)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGOUT_BTN)));
			
			if(!driver.findElement(By.xpath(xpath_prifile + "[1]")).getText().contentEquals("자동화테스트용")) {
				failMsg = failMsg + "\n1. profile customer [Expected]자동화테스트용" 
						+ " [Actual]" + driver.findElement(By.xpath(xpath_prifile + "[1]")).getText();
			}
			if(!driver.findElement(By.xpath(xpath_prifile + "[2]")).getText().contentEquals("QA")) {
				failMsg = failMsg + "\n1. profile department [Expected]QA" 
						+ " [Actual]" + driver.findElement(By.xpath(xpath_prifile + "[2]")).getText();
			}
			if(!driver.findElement(By.xpath(xpath_prifile + "[3]")).getText().contentEquals("자동화기업용관리자")) {
				failMsg = failMsg + "\n1. profile user name [Expected]자동화기업용관리자" 
						+ " [Actual]" + driver.findElement(By.xpath(xpath_prifile + "[3]")).getText();
			}
			
			driver.findElement(By.xpath(XPATH_ADMIN_LOGOUT_BTN)).click();

		} catch (Exception e) {
			// TODO: handle exception
			failMsg = failMsg + "\n2. cannot find profile menu";
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}	

	public void logoutAdmin(WebDriver wd) {
		
		if(!wd.getCurrentUrl().contains(CommonValues.MEETING_URL)) {
			wd.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);
		}
		
		try {
			WebDriverWait wait = new WebDriverWait(wd, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_ADMIN_PROFILE_INFO_BTN)));
			wd.findElement(By.xpath(XPATH_ADMIN_PROFILE_INFO_BTN)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGOUT_BTN)));
			wd.findElement(By.xpath(XPATH_ADMIN_LOGOUT_BTN)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)));
			
		} catch (Exception e) {
			// TODO: handle exception. do not anything...
			System.out.println("cannot logout. error : " + e.getMessage());
		}
	}
	
	public void loginAdmin(WebDriver wd, String user, String pw) {
		WebDriverWait wait = new WebDriverWait(wd, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)));
		
		wd.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).clear();
		wd.findElement(By.xpath(XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(user);
		wd.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).clear();
		wd.findElement(By.xpath(XPATH_ADMIN_LOGIN_PW)).sendKeys(pw);
		wd.findElement(By.xpath(XPATH_ADMIN_LOGIN_BTN)).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_PROFILE_INFO_BTN)));
		} catch (Exception e) {
			System.out.println("login error" + e.getMessage());
			
			if(isElementPresent_wd(wd, By.xpath(XPATH_MODAL_FOOTER_Y))) {
				wd.findElement(By.xpath(XPATH_MODAL_FOOTER_Y)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_PROFILE_INFO_BTN)));
			}
		}

	}
	
	private boolean isElementPresent_wd(WebDriver wd, By by) {
		try {
			wd.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
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
