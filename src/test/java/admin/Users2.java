package admin;

import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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


import mandatory.CommonValues;


/* Users
 * 1. 유저 신규 등록
 * 2. 신규 사용자 - 이메일 잘못된 포맷으로 입력
 * 3. 신규 사용자 - 중복 이메일 입력
 * 4. 신규 사용자 - 정상 이메일 입력
 * 5. 신규 사용자 - 필수 정보 누락후 등록 시도
 * 6. 신규 사용자 - 목록 클릭 
 * 10. 사용자 - 정상 유저 등록 
 * 11. 사용자 - 등록된 유저 삭제
 * 12. 사용자 - 삭제된 유저 이메일을 이용해 다시 등록 
 * 13. 사용자 - 등록된 유저의 정보 수정 - 삭제
 * 20. 사용자 - 검색 후 검색된 정보 확인
 * 21. 사용자 - 미인증 유저 정보 확인 - 미인증 유저 인증 메일 발송
 * 22. 사용자 - 사용자 정보 화면 이동
 * 23. 사용자 - 사용자 정보 화면에서 등록된 프로필 이미지 확인
 * 30. 사용자 정보 - 유저 권한 확인 후 변경
 * 31. 사용자 정보 - 목록 클릭
 * 32. 사용자 정보 - 비밀번호 재설정 N
 * 33. 사용자 정보 - 비밀번호 재설정 Y
 * 34. 본인계정 - 사용자 정보에 삭제 버튼 없음/권한 변경 불가 확인
 */

public class Users2 {

	public static String XPATH_MODAL_NOHEADER = "//div[@class='no-header']/div";
	
	public static String XPATH_ADDUSER_VIEW_TITLE = "//div[@class='panel-header']/h2";
	public static String XPATH_ADDUSER_VIEW_EMAIL = "//input[@id='username']";
	public static String XPATH_ADDUSER_VIEW_EMAIL_BTN = "//span[@class='input-group-btn']/button";
	public static String XPATH_ADDUSER_VIEW_EMAIL_ERROR = "//span[@id='username-error']";
	public static String XPATH_ADDUSER_VIEW_NAME = "//input[@id='name']";
	public static String XPATH_ADDUSER_VIEW_DEP = "//input[@id='department']";
	public static String XPATH_ADDUSER_VIEW_PHONE = "//input[@id='phone']";
	public static String XPATH_ADDUSER_VIEW_NAME_ERROR = "//span[@id='name-error']";
	public static String XPATH_ADDUSER_VIEW_DEP_ERROR = "//span[@id='department-error']";
	public static String XPATH_ADDUSER_SAVE_BTN = "//button[@type='submit']";
	public static String XPATH_ADDUSER_LIST_BTN = "//a[@class='btn btn-default spa-menu']";
	public static String XPATH_SENDATUHMAIL_BTN = "//button[@class='btn btn-primary btn-authorization']";
	public static String XPATH_USERVIEW_PROFILE = "//div[@class='profile-contain']";
	public static String XPATH_USERVIEW_EMAIL = "//input[@id='userEmail']";
	public static String XPATH_USERVIEW_ROLE = "//select[@id='roleId']";
	public static String XPATH_USERVIEW_ROLE_SELECTED = XPATH_USERVIEW_ROLE + "/option[@selected='selected']";
	public static String XPATH_USERVIEW_NAME = "//input[@id='name']";
	public static String XPATH_USERVIEW_DEP = "//input[@id='department']";
	public static String XPATH_USERVIEW_PHONE = "//input[@id='phone']";
	public static String XPATH_USERVIEW_JOB = "//input[@id='position']";
	public static String XPATH_USERVIEW_MOBILE = "//input[@id='mobile']";
	public static String XPATH_USERVIEW_MEMO = "//textarea[@id='memo']";
	
	public static String XPATH_ADMIN_LIST_SEARCHBOX = "//input[@id='search-keywordString']";
	public static String XPATH_ADMIN_LIST_SEARCH_BTN = "//span[@class='input-group-btn']/button[@type='submit']";
	
	public static String XPATH_USERVIEW_DELETE_BTN = "//div[@class='row box-footer center']/button[@id='removeModal']";
	public static String XPATH_USERVIEW_SAVE_BTN = "//div[@class='row box-footer center']/button[@type='submit']";
	public static String XPATH_USERVIEW_LIST_BTN = "//div[@class='row box-footer center']/a[@class='btn btn-default spa-menu']";
	public static String XPATH_USERVIEW_RESETPW_BTN = "//div[@class='row box-footer center']/button[@id='resetPasswordModal']";
	
	public static String XPATH_TOAST_CONTENTS = "//div[@id='toastPopup']//p[@class='alert-contents']";
	
	public static String MSG_ADDUSER_VIEW_TITLE = "신규등록";
	public static String MSG_ADDUSER_VIEW_EMAIL_INVALID = "형식에 맞게 입력해 주세요.";
	public static String MSG_ADDUSER_VIEW_EMAIL_DUPLICATED = "중복된 이메일입니다. 다시 입력해 주세요.";
	public static String MSG_TOAST_VALIDEMAIL = "사용 가능한 이메일입니다.";
	public static String MSG_MISS_ESSENTIAL = "필수 입력란입니다.";
	public static String MSG_ADDUSER_SUCCESS = "사용자 등록이 완료되었으며 인증 이메일을 발송하였습니다.\n"
			+ "인증 완료 후 서비스를 이용할 수 있습니다.";
	public static String MSG_TOAST_SENDATUHMAIL = "회사 등록 인증메일을 전송하였습니다.\n"
			+ "대량의 사용자를 선택한 경우 메일 발송까지 다소 시간이 걸릴 수 있습니다.";
	public static String MSG_POPUP_RESETPW = "사용자에게 비밀번호 재설정 메일이 전송됩니다.";
	public static String MSG_TOAST_SUCCESS = "정상적으로 처리되었습니다.";
	
	public static String URL_USERNEW = "/customer/user-new";
	public static String URL_USERVIEW = "/customer/user-view?";
	
	public static String USER_NEW = "rsrsup11@gmail.com";
	public static String DEP_NEW = "QA";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		context.setAttribute("webDriver", driver);
		
		driver.get(CommonValues.MEETING_URL + CommonValues.ADMIN_URL);

	}
	
	@Test(priority = 0, alwaysRun = true,  enabled = true)
	public void userMenu() throws Exception {
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 사용자 관리
		Users user = new Users();
		user.selectSideMenu(driver, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
	}

	@Test(priority = 1, enabled = true)
	public void user_addView() throws Exception {
		String failMsg = "";

		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		//신규등록 클릭
		driver.findElement(By.xpath(Users.XPATH_ADMIN_RIGHT_BTNS + "[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		
		if(!driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_TITLE)).getText().contentEquals(MSG_ADDUSER_VIEW_TITLE)) {
			failMsg = failMsg + "\n1. add user view title [Expected]" + MSG_ADDUSER_VIEW_TITLE 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_TITLE)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, dependsOnMethods = {"user_addView"}, alwaysRun = true, enabled = true)
	public void user_add_emailInvalid() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERNEW)) {
			driver.get(CommonValues.MEETING_URL + URL_USERNEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		}
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).sendKeys("invalid email");
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_ERROR)));
		if(!driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_ERROR)).getText().contentEquals(MSG_ADDUSER_VIEW_EMAIL_INVALID)) {
			failMsg = failMsg + "\n1. Email error msg(invalid form) [Expected]" + MSG_ADDUSER_VIEW_EMAIL_INVALID 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void user_add_emailDuplicate() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERNEW)) {
			driver.get(CommonValues.MEETING_URL + URL_USERNEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		}
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).sendKeys(CommonValues.ADMEMAIL);
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
		if(!driver.findElement(By.xpath(XPATH_MODAL_NOHEADER)).getAttribute("innerText").contentEquals(MSG_ADDUSER_VIEW_EMAIL_DUPLICATED)) {
			failMsg = failMsg + "\n1. Email error msg(duplicated) [Expected]" + MSG_ADDUSER_VIEW_EMAIL_DUPLICATED 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_MODAL_NOHEADER)).getText();
		}
		
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void user_add_emailVaid() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERNEW)) {
			driver.get(CommonValues.MEETING_URL + URL_USERNEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		}
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).sendKeys(USER_NEW);
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_TOAST_CONTENTS)));
		if(!driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getAttribute("innerText").contentEquals(MSG_TOAST_VALIDEMAIL)) {
			failMsg = failMsg + "\n1. Email error msg(duplicated) [Expected]" + MSG_TOAST_VALIDEMAIL 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getAttribute("innerText");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void user_add_missEssential() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERNEW)) {
			driver.get(CommonValues.MEETING_URL + URL_USERNEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		}
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP)).clear();
		
		driver.findElement(By.xpath(XPATH_ADDUSER_SAVE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_ERROR)));
		
		if(!driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_ERROR)).getAttribute("innerText").contentEquals(MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n1. miss essential value error(email) [Expected]" + MSG_MISS_ESSENTIAL 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_ERROR)).getAttribute("innerText");
		}
		
		if(!driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME_ERROR)).getAttribute("innerText").contentEquals(MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n2. miss essential value error(name) [Expected]" + MSG_MISS_ESSENTIAL 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME_ERROR)).getAttribute("innerText");
		}
		
		if(!driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP_ERROR)).getAttribute("innerText").contentEquals(MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n3. miss essential value error(department) [Expected]" + MSG_MISS_ESSENTIAL 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP_ERROR)).getAttribute("innerText");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void user_addview_toList() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERNEW)) {
			//click 사용자 관리
			driver.get(CommonValues.MEETING_URL + URL_USERNEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		}
		
		driver.findElement(By.xpath(XPATH_ADDUSER_LIST_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			failMsg = failMsg + "\n1. not userlist view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void user_addUserValid() throws Exception {
		String failMsg = "";

		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		//신규등록 클릭
		driver.findElement(By.xpath(Users.XPATH_ADMIN_RIGHT_BTNS + "[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).sendKeys(USER_NEW);
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_TOAST_CONTENTS)));
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME)).sendKeys("tester");
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP)).sendKeys(DEP_NEW);
		
		driver.findElement(By.xpath(XPATH_ADDUSER_SAVE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_ADDUSER_SUCCESS)) {
			failMsg = failMsg + "\n1. add user popup msg [Expected]" + MSG_ADDUSER_SUCCESS 
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText");
		}
		
		driver.findElement(By.xpath(Connect.XPATH_MODAL_FOOTER_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		
		Users users = new Users();
		String[][] listItem = users.getListItems(driver);
		
		if(listItem != null && listItem.length > 0) {
			if(!listItem[0][0].equals("tester")) {
				failMsg = failMsg + "\n1-1. added user name [Expected]tester [Actual]" + listItem[0][0];
			}
			if(!listItem[0][1].equals(USER_NEW)) {
				failMsg = failMsg + "\n1-2. added user email [Expected]" + USER_NEW + " [Actual]" + listItem[0][1];
			}
			if(!listItem[0][2].equals(DEP_NEW)) {
				failMsg = failMsg + "\n1-2. added user department [Expected]" + DEP_NEW + " [Actual]" + listItem[0][2];
			}
		} else {
			failMsg = failMsg + "\n2. user list error (list is empty or other error)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, dependsOnMethods = {"user_addUserValid"}, enabled = true)
	public void user_deleteUser() throws Exception {
		String failMsg = "";

		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		//delete user
		if(!deleteUser(driver, USER_NEW)) {
			failMsg = "\n1. fail to delete user username : " + USER_NEW;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, dependsOnMethods = {"user_deleteUser"}, enabled = true)
	public void user_addUser2() throws Exception {
		String failMsg = "";

		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		//신규등록 클릭
		driver.findElement(By.xpath(Users.XPATH_ADMIN_RIGHT_BTNS + "[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL)).sendKeys(USER_NEW);
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_EMAIL_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_TOAST_CONTENTS)));
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_NAME)).sendKeys("tester");
		
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP)).clear();
		driver.findElement(By.xpath(XPATH_ADDUSER_VIEW_DEP)).sendKeys(DEP_NEW);
		
		driver.findElement(By.xpath(XPATH_ADDUSER_SAVE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_ADDUSER_SUCCESS)) {
			failMsg = failMsg + "\n1. add user popup msg [Expected]" + MSG_ADDUSER_SUCCESS 
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText");
		}
		
		driver.findElement(By.xpath(Connect.XPATH_MODAL_FOOTER_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		
		Users users = new Users();
		String[][] listItem = users.getListItems(driver);
		
		if(listItem != null && listItem.length > 0) {
			if(!listItem[0][0].equals("tester")) {
				failMsg = failMsg + "\n1-1. added user name [Expected]tester [Actual]" + listItem[0][0];
			}
			if(!listItem[0][1].equals(USER_NEW)) {
				failMsg = failMsg + "\n1-2. added user email [Expected]" + USER_NEW + " [Actual]" + listItem[0][1];
			}
			if(!listItem[0][2].equals(DEP_NEW)) {
				failMsg = failMsg + "\n1-2. added user department [Expected]" + DEP_NEW + " [Actual]" + listItem[0][2];
			}
		} else {
			failMsg = failMsg + "\n2. user list error (list is empty or other error)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 13, dependsOnMethods = {"user_addUser2"}, enabled = true)
	public void user_edit() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		String userName = "name_edit";
		String userDep = "dep_edit";
		String userPhone = "0100000000";
		String userJob = "job_edit";
		String userMobile = "0100000000";
		String userMemo = "test!";
		
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(USER_NEW);
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath("./td[3]/a")).getText().contentEquals(USER_NEW)) {
					webElement.findElement(By.xpath("./td[3]/a")).click();
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_USERVIEW_DELETE_BTN)));
					break;
				}
			}
			
			driver.findElement(By.xpath(XPATH_USERVIEW_NAME)).clear();
			driver.findElement(By.xpath(XPATH_USERVIEW_NAME)).sendKeys(userName);
			driver.findElement(By.xpath(XPATH_USERVIEW_DEP)).clear();
			driver.findElement(By.xpath(XPATH_USERVIEW_DEP)).sendKeys(userDep);
			driver.findElement(By.xpath(XPATH_USERVIEW_PHONE)).clear();
			driver.findElement(By.xpath(XPATH_USERVIEW_PHONE)).sendKeys(userPhone);
			driver.findElement(By.xpath(XPATH_USERVIEW_JOB)).clear();
			driver.findElement(By.xpath(XPATH_USERVIEW_JOB)).sendKeys(userJob);
			driver.findElement(By.xpath(XPATH_USERVIEW_MOBILE)).clear();
			driver.findElement(By.xpath(XPATH_USERVIEW_MOBILE)).sendKeys(userMobile);
			driver.findElement(By.xpath(XPATH_USERVIEW_MEMO)).clear();
			driver.findElement(By.xpath(XPATH_USERVIEW_MEMO)).sendKeys(userMemo);
		
			//save
			driver.findElement(By.xpath(XPATH_USERVIEW_SAVE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			
			//refresh
			driver.navigate().refresh();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_USERVIEW_DELETE_BTN)));
			
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_NAME)).getAttribute("value").contentEquals(userName)) {
				failMsg = failMsg + "\n1. user name [Expected]" + userName 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_NAME)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_DEP)).getAttribute("value").contentEquals(userDep)) {
				failMsg = failMsg + "\n2. user department [Expected]" + userDep 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_DEP)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_PHONE)).getAttribute("value").contentEquals(userPhone)) {
				failMsg = failMsg + "\n3. user Phone [Expected]" + userPhone 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_PHONE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_JOB)).getAttribute("value").contentEquals(userJob)) {
				failMsg = failMsg + "\n4. user job [Expected]" + userJob 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_JOB)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_MOBILE)).getAttribute("value").contentEquals(userMobile)) {
				failMsg = failMsg + "\n5. user Mobile [Expected]" + userMobile 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_MOBILE)).getAttribute("value");
			}
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_MEMO)).getAttribute("value").contentEquals(userMemo)) {
				failMsg = failMsg + "\n6. user Memo [Expected]" + userMemo 
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_MEMO)).getAttribute("value");
			}
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		//user delete
		deleteUser(driver, USER_NEW);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}	
	
	@Test(priority = 20, enabled = true)
	public void user_search() throws Exception {
		String failMsg = "";

		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		String searchStr = "기업용관리자";
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(searchStr);
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		
		Thread.sleep(2000);
		
		String[][] searchedUser = user.getListItems(driver);
		
		for (int i = 0; i < searchedUser.length; i++) {
			if(!searchedUser[i][0].contains(searchStr) && searchedUser[i][1].contains(searchStr)) {
				failMsg = "\n1-" + i + ". searched item error. keyword : " + searchStr 
						+ ",  searched user info : " + searchedUser[i];
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 21, enabled = true)
	public void user_sendAuthMail() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			//인증상태 확인
			if(!listItem.get(0).findElement(By.xpath("./td[7]")).getText().contentEquals("미인증")) {
				failMsg = failMsg + "\n1. auth mode [Expeced]" + "미인증"
						+ " [Actual]" + listItem.get(0).findElement(By.xpath("./td[7]"));
			}
			
			listItem.get(0).findElement(By.xpath("./td[1]/input")).click();
			Thread.sleep(500);
			
			driver.findElement(By.xpath(XPATH_SENDATUHMAIL_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_TOAST_CONTENTS)));
			if(!driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_TOAST_SENDATUHMAIL)) {
				failMsg = failMsg + "\n2. send auth mail msg [Expeced]" + MSG_TOAST_SENDATUHMAIL
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getText();
			}
			
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 22, enabled = true)
	public void user_userView() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath("./td[3]/a")).getText().contentEquals(CommonValues.ADMEMAIL)) {
					webElement.findElement(By.xpath("./td[3]/a")).click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
					
					if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_USERVIEW)) {
						failMsg = "\n1. no user view. current url : " + driver.getCurrentUrl();
					} else {
						if(!driver.findElement(By.xpath(XPATH_USERVIEW_EMAIL)).getAttribute("value").contentEquals(CommonValues.ADMEMAIL)) {
							failMsg = "\n2. user email error [Expected" + CommonValues.ADMEMAIL 
									+ " [Actual]" + driver.findElement(By.xpath(XPATH_USERVIEW_EMAIL)).getAttribute("value");
						}
					}
					break;
				}
				
			}
			
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 23, dependsOnMethods = {"user_userView"}, enabled = true)
	public void user_userViewProfile() throws Exception {
		String failMsg = "";
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_USERVIEW)) {
			//click 사용자 관리
			failMsg = failMsg + "\n1. no user view";
		} else {
			String profile = driver.findElement(By.xpath(XPATH_USERVIEW_PROFILE)).getAttribute("style");
			
			if(!profile.contains(CommonValues.MEETING_URL + "/profileImages/")) {
				failMsg = failMsg + "\n2. profile img check : " + profile;
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 30, enabled = true)
	public void user_userRole() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
	
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(Connect.NOAUTH_ADM);
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath("./td[3]/a")).getText().contentEquals(Connect.NOAUTH_ADM)) {
					webElement.findElement(By.xpath("./td[3]/a")).click();
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_USERVIEW_DELETE_BTN)));
					break;
				}
			}
			
			driver.findElement(By.xpath(XPATH_USERVIEW_ROLE)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(XPATH_USERVIEW_ROLE + "/option[2]")).click();
			
			//save
			driver.findElement(By.xpath(XPATH_USERVIEW_SAVE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			driver.navigate().refresh();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			
			if(!driver.findElement(By.xpath(XPATH_USERVIEW_ROLE_SELECTED)).getText().contentEquals("사용자")) {
				failMsg = "\n1. user role is not changed [Expected]사용자 [Actual]" 
						+ driver.findElement(By.xpath(XPATH_USERVIEW_ROLE_SELECTED)).getText();
			}
			
			//reset
			driver.findElement(By.xpath(XPATH_USERVIEW_ROLE)).click();
			Thread.sleep(500);
			driver.findElement(By.xpath(XPATH_USERVIEW_ROLE + "/option[1]")).click();
			//save
			driver.findElement(By.xpath(XPATH_USERVIEW_SAVE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODAL_NOHEADER)));
			
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}	
	
	@Test(priority = 31, dependsOnMethods = {"user_userRole"}, alwaysRun = true, enabled = true)
	public void user_viewtoList() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_USERVIEW)) {
			//click 목록
			driver.findElement(By.xpath(XPATH_USERVIEW_LIST_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));

			if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
				failMsg = "\n1. not user list. current url : " + driver.getCurrentUrl();
			}
		} else {
			failMsg = failMsg + "\n3. not user view";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 32, enabled = true)
	public void user_resetPW_N() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(Connect.NOAUTH_ADM);
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath("./td[3]/a")).getText().contentEquals(Connect.NOAUTH_ADM)) {
					webElement.findElement(By.xpath("./td[3]/a")).click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
					
					//click reset pw
					driver.findElement(By.xpath(XPATH_USERVIEW_RESETPW_BTN)).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
					
					if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_POPUP_RESETPW)) {
						failMsg = "\n1. popup msg [Expeted]" + MSG_POPUP_RESETPW
								 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText");
					}
					
					driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
					
					try {
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_TOAST_CONTENTS)));
						if(driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_TOAST_SUCCESS)) {
							failMsg = "\n2.find reset pw success msg";
						}
						
					} catch (Exception e) {
						// do not anything
					}
					break;
				}
			}
			
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 33, enabled = true)
	public void user_resetPW_Y() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(Connect.NOAUTH_ADM);
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath("./td[3]/a")).getText().contentEquals(Connect.NOAUTH_ADM)) {
					webElement.findElement(By.xpath("./td[3]/a")).click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
					
					//click reset pw
					driver.findElement(By.xpath(XPATH_USERVIEW_RESETPW_BTN)).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
					
					if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_POPUP_RESETPW)) {
						failMsg = "\n1. popup msg [Expeted]" + MSG_POPUP_RESETPW
								 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText");
					}
					
					driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
					
					try {
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_TOAST_CONTENTS)));
						if(!driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_TOAST_SUCCESS)) {
							failMsg = "\n2.success toast msg [Expeted]" + MSG_TOAST_SUCCESS
									 + " [Actual]" + driver.findElement(By.xpath(XPATH_TOAST_CONTENTS)).getText();
						}
						
					} catch (Exception e) {
						failMsg = "\n3.cannot find toast.";
					}
					break;
				}
			}
			
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 34, enabled = true)
	public void user_MyView() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			//click 사용자 관리
			user.selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(CommonValues.ADMEMAIL);
		driver.findElement(By.xpath(XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath("./td[3]/a")).getText().contentEquals(CommonValues.ADMEMAIL)) {
					webElement.findElement(By.xpath("./td[3]/a")).click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADDUSER_VIEW_TITLE)));
					
					try {
						driver.findElement(By.xpath(XPATH_USERVIEW_DELETE_BTN));
						failMsg = "\n1. find delete button in my view";
					} catch (Exception e) {
						// do not anything
					}
					
					if(driver.findElement(By.xpath(XPATH_USERVIEW_ROLE)).isEnabled()) {
						failMsg = "\n2. role button is enabled in my view";
					}
					
					break;
				}
			}
			
		} else {
			failMsg = failMsg + "\n3. list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public boolean deleteUser(WebDriver wd, String email) {
		boolean isDeleted = false;
		
		WebDriverWait wait = new WebDriverWait(wd, 10);
		if(!wd.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + Users.URL_USERS)) {
			wd.get(CommonValues.MEETING_URL + Users.URL_USERS);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ADMIN_LIST_SEARCHBOX)));
		}
		
		List<WebElement> listItem = wd.findElements(By.xpath(Users.XPATH_ADMIN_VIEWLIST));
		String colform = "./td[%d]";
		if(listItem.size() >0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(String.format(colform, 3))).getText().contentEquals(email)) {
					webElement.findElement(By.xpath(String.format(colform, 3) + "/a")).click();
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_USERVIEW_DELETE_BTN)));
					
					wd.findElement(By.xpath(XPATH_USERVIEW_DELETE_BTN)).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
					wd.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='result-body modal-body']")));
					wd.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users.XPATH_ADMIN_VIEWLIST)));
					
					isDeleted = true;
					break;
				}
			}
			
		}
		
		return isDeleted;
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
