package admin;

import static org.testng.Assert.fail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Alert;
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

/* Security
 * 1. 보안 화면 이동
 * 2. 보안 - ip입력없이 추가시도
 * 3. 보안 - IP 추가 / 삭제
 * 4. 보안 - IP 추가 후 동일 IP 추가 시도
 * 5. 보안 - IP 선택하지 않고 삭제 클릭
 * 10. 보안 - IP 설정(유효하지 않은 IP) - 유저 rm 로그인 시도
 * 11. 보안 - IP 설정(유효하지 않은 IP) - 어드민 rm 로그인 후 회의 생성 시도
 * 12. 보안 - IP 설정 해제 후 유저로 rm 로그인
 */

public class Security {
	
	public static String XPATH_SECURITY_SECURITYOPTION_PANEL = "//div[@class='panel-title ip-config']";
	public static String XPATH_SECURITY_IPADDRESS_RADIO_USABLE = "//div[@class='wrap-ipaddress wrapper-radio']/div[1]/label";
	public static String XPATH_SECURITY_IPADDRESS_RADIO_UNUSABLE = "//div[@class='wrap-ipaddress wrapper-radio']/div[2]/label";
	public static String XPATH_SECURITY_ADDIP_BTN = "//button[@id='addIp']";
	public static String XPATH_SECURITY_REMOVEIP_BTN = "//button[@id='removeIP']";
	public static String XPATH_SECURITY_IPSTART = "//div[@class='wrap-ip start-wrap']/input";
	public static String XPATH_SECURITY_IPEND = "//div[@class='wrap-ip end-wrap']/input";
	public static String XPATH_SECURITY_ADDEDIPLIST = "//select[@id='selectIp']/option";
	public static String XPATH_SECURITY_SAVE_BTN = "//button[@id='btnSave']";
	
	public static String XPATH_SECURITY_RADIO_TIMEUNLIMIT = "//div[@class='panel-inner panel-center']/div[3]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_TIMELIMIT = "//div[@class='panel-inner panel-center']/div[3]//div[@class='wrapper-radio input-group width-max']/div[2]";
	
	public static String XPATH_SECURITY_RADIO_DOCUNLIMIT = "//div[@class='panel-inner panel-center']/div[4]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_DOCLIMITALL = "//div[@class='panel-inner panel-center']/div[4]//div[@class='wrapper-radio input-group width-max']/div[2]";
	public static String XPATH_SECURITY_RADIO_DOCLIMITUSER = "//div[@class='panel-inner panel-center']/div[4]//div[@class='wrapper-radio input-group width-max']/div[3]";
	
	public static String XPATH_SECURITY_RADIO_SCREENUNLIMIT = "//div[@class='panel-inner panel-center']/div[5]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_SCREENLIMITALL = "//div[@class='panel-inner panel-center']/div[5]//div[@class='wrapper-radio input-group width-max']/div[2]";
	public static String XPATH_SECURITY_RADIO_SCREENLIMITUSER = "//div[@class='panel-inner panel-center']/div[5]//div[@class='wrapper-radio input-group width-max']/div[3]";

	public static String XPATH_SECURITY_RADIO_RECUNLIMIT = "//div[@class='panel-inner panel-center']/div[6]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_RECLIMITALL = "//div[@class='panel-inner panel-center']/div[6]//div[@class='wrapper-radio input-group width-max']/div[2]";
	public static String XPATH_SECURITY_RADIO_RECLIMITUSER = "//div[@class='panel-inner panel-center']/div[6]//div[@class='wrapper-radio input-group width-max']/div[3]";
	
	public static String XPATH_SECURITY_RADIO_RECDOIWNLOAD = "//div[@class='panel-inner panel-center']/div[7]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_RECDOIWNLOADLIMIT = "//div[@class='panel-inner panel-center']/div[7]//div[@class='wrapper-radio input-group width-max']/div[2]";
	
	public static String XPATH_SECURITY_RADIO_SHARENOTEUNLIMIT = "//div[@class='panel-inner panel-center']/div[8]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_SHARENOTELIMITALL = "//div[@class='panel-inner panel-center']/div[8]//div[@class='wrapper-radio input-group width-max']/div[2]";
	public static String XPATH_SECURITY_RADIO_SHARENOTELIMITUSER = "//div[@class='panel-inner panel-center']/div[8]//div[@class='wrapper-radio input-group width-max']/div[3]";
	
	public static String XPATH_SECURITY_RADIO_SETAI = "//div[@class='panel-inner panel-center']/div[9]//div[@class='wrapper-radio input-group width-max']/div[1]";
	public static String XPATH_SECURITY_RADIO_UNSETAI = "//div[@class='panel-inner panel-center']/div[9]//div[@class='wrapper-radio input-group width-max']/div[2]";
	
	public static String XPATH_SECURITY_WAITMAXMINUTE = "//input[@id='conferenceWatingMaxMinutes']";
	public static String XPATH_SECURITY_WAITMAXMINUTE_ERROR = "//span[@id='conferenceMaxMinuteLabel-error']";
	
	public static String MSG_SECURITY_EMPTYIP = "접근을 허용할 IP주소를 등록해주세요.";
	public static String MSG_SECURITY_DUPLICATEDIP = "이미 추가된 IP주소 입니다.";
	public static String MSG_SECURITY_NOSELECTIP = "IP주소를 선택해주세요.";
	public static String MSG_SECURITY_NOTALLOWEDIP = "접근 권한이 없는 IP에서 접속하였습니다.\n그룹 관리자에게 문의해 주시기 바랍니다. [Code: 40715]";
	public static String MSG_SECURITY_NOTALLOWEDIP_ADIMIN = "접근 권한이 없는 IP에서 접속하였습니다.\n관리자 페이지의 보안 설정에서 허용 공인IP 설정을 확인해주세요.\n[Code: 40221]";
	public static String MSG_SECURITY_IP_TOOLTIP = "공인IP란? 전세계에서 유일한 IP주소로 각 나라의 기관으로부터 할당받은 IP주소 입니다. PC 설정에서 볼 수 있는 IP주소와 다를 수 있습니다.";
	public static String URL_SECURITY = "/customer/security-settings";

	public static WebDriver driver;
	public static WebDriver driver_guest;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		driver_guest = comm.setDriver(driver_guest, browsertype, "lang=ko_KR", true);
		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", driver_guest);

	}
	
	@Test(priority = 1, enabled = true)
	public void SecurityView() throws Exception {
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
		user.selectSideMenu(driver, 5, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			failMsg = failMsg + "\n1. no security view. current url : " + driver.getCurrentUrl();
		}
		
		//tooltip 확인
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath("//span[@class='cola-admin-admin_question']"));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		if(!driver.findElement(By.xpath("//div[@class='tooltip fade bottom in']")).getText().contentEquals(MSG_SECURITY_IP_TOOLTIP)) {
			failMsg = failMsg + "\n2. ip setting tooltip [Expected]" + MSG_SECURITY_IP_TOOLTIP
					 + " [Actial]" + driver.findElement(By.xpath("//div[@class='tooltip fade bottom in']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void SecurityView_addIP_empty() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//click ip check
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_USABLE)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_ADDIP_BTN)));
		driver.findElement(By.xpath(XPATH_SECURITY_ADDIP_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_SECURITY_EMPTYIP)) {
			failMsg = failMsg + "\n1. toast msg [Expected]" + MSG_SECURITY_EMPTYIP
					 + " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void SecurityView_addIP_addRemove() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//click ip check
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_USABLE)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_ADDIP_BTN)));
		
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[4]")).sendKeys("1");
		
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[4]")).sendKeys("1");
		
		driver.findElement(By.xpath(XPATH_SECURITY_ADDIP_BTN)).click();
		Thread.sleep(500);
		List<WebElement> iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
		if(iplist.size() == 1) {
			if(!iplist.get(0).getText().contentEquals("1.1.1.1~1.1.1.1")) {
				failMsg = failMsg + "\n1. added ip [Expected]1.1.1.1~1.1.1.1 [Actual]" + iplist.get(0).getText();
			}
			
			//추가한 ip 삭제
			iplist.get(0).click();
			driver.findElement(By.xpath(XPATH_SECURITY_REMOVEIP_BTN)).click();
			Thread.sleep(500);
			iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
			if(iplist.size() != 0) {
				failMsg = failMsg + "\n2. ip is not removed. expected count0, actual count : " + iplist.size();
			}
		} else {
			failMsg = failMsg + "\n3. added ip list error expected 1 but " + iplist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void SecurityView_addDuplicated() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		//click ip check
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_USABLE)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_ADDIP_BTN)));
		
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[4]")).sendKeys("1");
		
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[4]")).sendKeys("1");
		
		driver.findElement(By.xpath(XPATH_SECURITY_ADDIP_BTN)).click();
		Thread.sleep(500);
	
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[4]")).sendKeys("1");
		
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[4]")).sendKeys("1");
		
		driver.findElement(By.xpath(XPATH_SECURITY_ADDIP_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_SECURITY_DUPLICATEDIP)) {
			failMsg = failMsg + "\n1. toast msg [Expected]" + MSG_SECURITY_DUPLICATEDIP
					 + " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
	
		List<WebElement> iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
		if(iplist.size() != 1) {
			failMsg = failMsg + "\n2. ip count1, actual count : " + iplist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void SecurityView_removeNoOne() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		List<WebElement> iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
		
		if (iplist.size() != 0) {
			for (int i = iplist.size()-1 ; i == 0  ; i--) {
				iplist.get(i).click();
				driver.findElement(By.xpath(XPATH_SECURITY_REMOVEIP_BTN)).click();
				Thread.sleep(500);
			}
		} 
		
		driver.findElement(By.xpath(XPATH_SECURITY_REMOVEIP_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_SECURITY_NOSELECTIP)) {
			failMsg = failMsg + "\n1. toast msg [Expected]" + MSG_SECURITY_NOSELECTIP
					 + " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
	
		if(iplist.size() != 1) {
			failMsg = failMsg + "\n2. ip count1, actual count : " + iplist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void SecurityView_setIPUser() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		// click ip check
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_USABLE)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_ADDIP_BTN)));

		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[4]")).sendKeys("1");

		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[1]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[2]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[3]")).sendKeys("1");
		driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[4]")).sendKeys("1");

		driver.findElement(By.xpath(XPATH_SECURITY_ADDIP_BTN)).click();
		Thread.sleep(500);
		
		//save 1.1.1.1~1.1.1.1
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//try user login
		driver_guest.get(CommonValues.MEETING_URL);
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		wait_guest.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_EMAIL)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_EMAIL)).sendKeys(CommonValues.USERS[0]);
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_SUBMIT)).click();
		try {
			wait_guest.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver_guest.switchTo().alert();
			if(!alert.getText().contentEquals(MSG_SECURITY_NOTALLOWEDIP)) {
				failMsg = "\n1. alert msg [Expected]" + MSG_SECURITY_NOTALLOWEDIP
						 + " [Actual]" + alert.getText();
			}
			alert.accept();
		} catch (Exception e) {
			failMsg = "\n2. error msg : " + e.getMessage();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, dependsOnMethods = {"SecurityView_setIPUser"}, alwaysRun = true, enabled = true)
	public void SecurityView_setIPAdmin() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		// click ip check
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_USABLE)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_ADDIP_BTN)));
		
		List<WebElement> iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
		boolean ipcheck = false;
		for (WebElement webElement : iplist) {
			if (webElement.getText().contentEquals("1.1.1.1~1.1.1.1")) {
				ipcheck = true;
			}
		}
		if(!ipcheck) {
			driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[1]")).sendKeys("1");
			driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[2]")).sendKeys("1");
			driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[3]")).sendKeys("1");
			driver.findElement(By.xpath(XPATH_SECURITY_IPSTART + "[4]")).sendKeys("1");

			driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[1]")).sendKeys("1");
			driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[2]")).sendKeys("1");
			driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[3]")).sendKeys("1");
			driver.findElement(By.xpath(XPATH_SECURITY_IPEND + "[4]")).sendKeys("1");

			driver.findElement(By.xpath(XPATH_SECURITY_ADDIP_BTN)).click();
			Thread.sleep(500);
		}
		//save 1.1.1.1~1.1.1.1
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//try admin login
		driver_guest.get(CommonValues.MEETING_URL);
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		wait_guest.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_EMAIL)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_EMAIL)).sendKeys(CommonValues.ADMEMAIL);
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_SUBMIT)).click();
		CommonValues comm = new CommonValues();
		comm.waitForLoad(driver_guest);
		wait_guest.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		//회의개설 시도
		driver_guest.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys("test1");
		driver_guest.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		try {
			wait_guest.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver_guest.switchTo().alert();
			if(!alert.getText().contentEquals(MSG_SECURITY_NOTALLOWEDIP_ADIMIN)) {
				failMsg = "\n1. alert msg [Expected]" + MSG_SECURITY_NOTALLOWEDIP_ADIMIN
						 + " [Actual]" + alert.getText();
			}
			alert.accept();
		} catch (Exception e) {
			failMsg = "\n2. error msg : " + e.getMessage();
		}

		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, dependsOnMethods = {"SecurityView_setIPAdmin"}, alwaysRun = true, enabled = true)
	public void SecurityView_unSetIP() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_SECURITY)) {
			Users user = new Users();
			user.selectSideMenu(driver, 5, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_SECURITY_SECURITYOPTION_PANEL)));
		}
		
		// click ip check
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_USABLE)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SECURITY_ADDIP_BTN)));
		
		List<WebElement> iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
		while (iplist.size() > 0) {
			if(iplist.size() >= 2) {
				iplist.get(1).click();
				driver.findElement(By.xpath(XPATH_SECURITY_REMOVEIP_BTN)).click();
				Thread.sleep(500);
				iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
			} if(iplist.size() == 1) {
				if(iplist.get(0).getAttribute("class").contentEquals("before-remove")) {
					break;
				} else {
					iplist.get(0).click();
					driver.findElement(By.xpath(XPATH_SECURITY_REMOVEIP_BTN)).click();
					Thread.sleep(500);
					iplist = driver.findElements(By.xpath(XPATH_SECURITY_ADDEDIPLIST));
				}
			}
		}
		
		//사용안함 클릭
		driver.findElement(By.xpath(XPATH_SECURITY_IPADDRESS_RADIO_UNUSABLE)).click();
		//save
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_SECURITY_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_SECURITY_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		//try user login (after logout)
		WebDriverWait wait_guest = new WebDriverWait(driver_guest, 10);
		driver_guest.get(CommonValues.MEETING_URL);
		CommonValues comm = new CommonValues();
		try {
			wait_guest.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)));
		} catch (Exception e) {
			comm.logout(driver_guest);
		}
		
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)).click();
		wait_guest.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_HOME_LOGIN_EMAIL)));
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_EMAIL)).sendKeys(CommonValues.USERS[0]);
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_PW)).sendKeys(CommonValues.USERPW);
		driver_guest.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_SUBMIT)).click();
		
		comm.waitForLoad(driver_guest);
		wait_guest.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		driver_guest.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}


