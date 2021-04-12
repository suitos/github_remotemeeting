package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.api.services.oauth2.Oauth2.Userinfo;

import mandatory.CommonValues;


/* PaymentHistory
* 1. 결제이력 - 결제이력 메뉴 확인. 사용중인 요금제 확인
 * 2. 결제이력 - 요금제 해약 버튼 클릭 - 취소 
 * 10. 결제이력 - 사용중인 요금제에서 사용자 지정 선택
 * 11. 결제이력 - 사용중인 요금제 - 사용자지정 - 이름순 정렬
 * 12. 결제이력 - 사용중인 요금제 - 사용자지정 - 부서순 정렬
 * 13. 결제이력 - 사용중인 요금제 - 유저 지정된 사용자 항목으로 이동
 * 14. 결제이력 - 사용중인 요금제 - 유저 지정된 사용자 항목 후 저장 - 저장된 값 확인
 * 20. ID 어드민 - 요금제에 지정되지 않은 유저 있을 경우 로그인 - 사용자 지정팝업 확인클릭
 * 21. ID 어드민 - 요금제에 지정되지 않은 유저 있을 경우 로그인 - 사용자 지정팝업 나중에 클릭
 * 30. 결제이력 - ID 요금제에서 추가 클릭 - 추가결제 화면 
 * 31. 결제이력 - 과거 결제이력 요금제의 청구서 버튼 노출 - 청구서 클릭시 청구서 팝업
 */

public class PaymentHistory {
	public static String XPATH_MODALDIALOG_BODY  = "//section[@class='modal-body']";
	public static String XPATH_MODALDIALOG_FOOTER_Y  = "//section[@class='modal-footer']/button[1]";
	public static String XPATH_MODALDIALOG_FOOTER_N  = "//section[@class='modal-footer']/button[2]";
	
	public static String XPATH_USERBOX_UNASSIGNLIST = "//ul[@id='unAssignedUserHandle']/li";
	public static String XPATH_USERBOX_ASSIGNLIST = "//ul[@id='assignedUserHandle']/li";
	public static String XPATH_USERBOX_SORTNAME_BTN = "//button[@id='sortByName']";
	public static String XPATH_USERBOX_SORTDEP_BTN = "//button[@id='sortByDepartment']";
	public static String XPATH_USERBOX_ADD_BTN = "//div[@class='wrap-add btn-edit']/button[@class='btn-add button']";
	public static String XPATH_USERBOX_DELETE_BTN = "//div[@class='wrap-add btn-edit']/button[@class='btn-delete button']";
	public static String XPATH_USERBOX_LOADING = "//div[@class='loading']";
	
	public static String XPATH_MODALDIALOG_TITLE  = "//div[@class='modal-content']//h4[@class='modal-title']";
	
	public static String XPATH_PAYMENT_CURRENT_LIST = "//section[@class='panel-section current-payment']//tbody[@class='searchable']/tr";
	public static String XPATH_PAYMENT_PAST_LIST = "//section[@class='panel-section past-payment']//tbody[@class='searchable']/tr";
	
	public static String MSG_PAYMENT_TITLE = "결제 이력";
	public static String MSG_PAYMENT_CANCEL_WARNING = "해약 하시면,\n"
			+ "다음 달부터 정기 결제가 되지 않아 사용 할 수 없습니다.\n"
			+ "해약하시겠습니까?\n"
			+ "* 해약 전까지의 사용한 금액이 결제 되어, 결제 예상 금액과 실제 청구 금액은 차이가 있을 수 있습니다.\n"
			+ "* 자세한 사항은 요금제 유의사항을 참고하세요.\n"
			+ "결제 예상 금액(부가세 포함) : KRW 0";
	public static String MSG_ADDUSERPOPUP_TITLE = "사용자 지정";
	public static String MSG_LOGINPOPUP_ID_ASSIGN = "User요금제에 사용자를 지정해주세요.\n"
			+ "사용자 지정을 완료하지 않은 라이선스가 있습니다.\n"
			+ "User 요금제는 지정된 사용자만 서비스를 이용할 수 있습니다.";
	public static String MSG_NOLICENSE_ADM = "라이선스에 할당되지 않은 계정입니다.\n"
			+ "요금제를 확인해주세요.\n"
			+ "[Code: 40602]";
	public static String MSG_NOLICENSE_USER = "라이선스에 할당되지 않은 계정입니다.\n"
			+ "관리자에게 문의해주세요.\n"
			+ "[Code: 40605]";
	public static String URL_PAYMENTLIST = "/customer/payment-list";
	public static String URL_LICENSEADD = "/customer/license-idAddForm";
	
	public static String ADM_NOID = "id1@rsupport.com"; //사용자 : rsrsup13
	public static String ADM_IDHISTORY = "dd@b.ocm"; //ID요금제 결제 이력 확인 테스트용
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
	public void paymentHistory() throws Exception {
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
		user.selectSideMenu(driver, 3, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText().contentEquals(MSG_PAYMENT_TITLE)) {
			failMsg = failMsg + "\n1. payment list pannel [Expected]" + MSG_PAYMENT_TITLE
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText();
		}
		
		List<WebElement> lic = driver.findElements(By.xpath("//section[@class='panel-section current-payment']//tr[@role='row']"));
		
		if(lic.size() >0 ) {
			if(!lic.get(0).findElement(By.xpath("./th[1]")).getText().contentEquals("요금제")) {
				failMsg = failMsg + "\n2. license column title 1. [Expected]" + "요금제"
						+ " [Actual]" + lic.get(0).findElement(By.xpath("./th[1]")).getText();
			}
			if(!lic.get(0).findElement(By.xpath("./th[2]")).getText().contentEquals("수량")) {
				failMsg = failMsg + "\n3. license column title 2. [Expected]" + "수량"
						+ " [Actual]" + lic.get(0).findElement(By.xpath("./th[2]")).getText();
			}
			if(!lic.get(0).findElement(By.xpath("./th[3]")).getText().contentEquals("시작일")) {
				failMsg = failMsg + "\n4. license column title 3. [Expected]" + "시작일"
						+ " [Actual]" + lic.get(0).findElement(By.xpath("./th[3]")).getText();
			}
			if(!lic.get(0).findElement(By.xpath("./th[4]")).getText().contentEquals("종료일")) {
				failMsg = failMsg + "\n5. license column title 4. [Expected]" + "종료일"
						+ " [Actual]" + lic.get(0).findElement(By.xpath("./th[4]")).getText();
			}
			
			
		} else {
			failMsg = failMsg + "\n6. payment license is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void payment_cancelN() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, DashBoard.BASIC_ADM, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() >0 ) {
			//해약 클릭
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_BODY)));
			
			if(!driver.findElement(By.xpath(XPATH_MODALDIALOG_BODY)).getText().contentEquals(MSG_PAYMENT_CANCEL_WARNING)) {
				failMsg = "\n1. license cancel warning msg [Expected]" + MSG_PAYMENT_CANCEL_WARNING
						 + " [Actual]" + driver.findElement(By.xpath(XPATH_MODALDIALOG_BODY)).getText();
			}
			
			driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_BODY)));
		} else {
			failMsg = failMsg + "\n1. payment license is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void payment_IDUser() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADM_ID, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 결제 관리
		Users user = new Users();
		user.selectSideMenu(driver, 3, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() >0 ) {
			//사용자지정 클릭
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			
			if(!driver.findElement(By.xpath(XPATH_MODALDIALOG_TITLE)).getText().contentEquals(MSG_ADDUSERPOPUP_TITLE)) {
				failMsg = "\n1. add user popup title [Expected]" + MSG_ADDUSERPOPUP_TITLE
						 + " [Actual]" + driver.findElement(By.xpath(XPATH_MODALDIALOG_TITLE)).getText();
			}
			
			driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		} else {
			failMsg = failMsg + "\n1. payment license is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, dependsOnMethods = {"payment_IDUser"}, alwaysRun = true, enabled = true)
	public void payment_UserNameSort() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_PAYMENTLIST)) {
			driver.get(CommonValues.MEETING_URL + URL_PAYMENTLIST);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		}
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() >0 ) {
			//사용자지정 클릭
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_USERBOX_LOADING)));
			
			String[][] userlist = getIDUsers(driver, XPATH_USERBOX_UNASSIGNLIST);
			
			//click sort name
			driver.findElement(By.xpath(XPATH_USERBOX_SORTNAME_BTN)).click();
			Thread.sleep(1000);
		
			Arrays.sort(userlist, new Comparator<String[]>() {

				@Override
				public int compare(String[] o1, String[] o2) {
					return o1[0].compareTo(o2[0]);
				}
			});
			
			String[][] sortedList = getIDUsers(driver, XPATH_USERBOX_UNASSIGNLIST);
			
			if(!Arrays.deepEquals(userlist, sortedList)) {
				failMsg = failMsg + "\n1. name sort fail.";
			}
		
		} else {
			failMsg = failMsg + "\n2. payment license is empty.";
		}
		
		driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, dependsOnMethods = {"payment_IDUser"}, alwaysRun = true, enabled = true)
	public void payment_UserDepSort() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_PAYMENTLIST)) {
			driver.get(CommonValues.MEETING_URL + URL_PAYMENTLIST);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		}
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() >0 ) {
			//사용자지정 클릭
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_USERBOX_LOADING)));
			
			String[][] userlist = getIDUsers(driver, XPATH_USERBOX_UNASSIGNLIST);
			
			//click sort dep
			driver.findElement(By.xpath(XPATH_USERBOX_SORTDEP_BTN)).click();
			Thread.sleep(1000);
			Arrays.sort(userlist, new Comparator<String[]>() {

				@Override
				public int compare(String[] o1, String[] o2) {
					return o1[2].compareTo(o2[2]);
				}
			});
	
			String[][] sortedList = getIDUsers(driver, XPATH_USERBOX_UNASSIGNLIST);

			if(!Arrays.deepEquals(userlist, sortedList)) {
				failMsg = failMsg + "\n1. dep sort fail.";
			}
		
		} else {
			failMsg = failMsg + "\n2. payment license is empty.";
		}
		
		driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 13, dependsOnMethods = {"payment_IDUser"}, alwaysRun = true, enabled = true)
	public void payment_IDmoveUser() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_PAYMENTLIST)) {
			driver.get(CommonValues.MEETING_URL + URL_PAYMENTLIST);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		}
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() >0 ) {
			//사용자지정 클릭
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_USERBOX_LOADING)));
			
			ArrayList<String> userlistUnassign = getIDUsersArray(driver, XPATH_USERBOX_UNASSIGNLIST);
			ArrayList<String> userlistAssign = getIDUsersArray(driver, XPATH_USERBOX_ASSIGNLIST);
			
			List<WebElement> list = driver.findElements(By.xpath(XPATH_USERBOX_ASSIGNLIST));
			if(list.size() > 0) {
				//할당된 ID중 1개 체크박스 클릭
				String user = list.get(1).findElement(By.xpath(".//div[@class='user-holder']")).getText();
				list.get(1).findElement(By.xpath(".//input[@class='checkbox']")).click();
				Thread.sleep(500);
				
				//해제 클릭
				driver.findElement(By.xpath(XPATH_USERBOX_DELETE_BTN)).click();
				Thread.sleep(1000);
				userlistAssign.remove(user);
				userlistUnassign.add(user);
				
				ArrayList<String> temp = getIDUsersArray(driver, XPATH_USERBOX_UNASSIGNLIST);
				if(!userlistUnassign.containsAll(temp)){
					failMsg = failMsg + "\n1-1. unassigned list(after delete user) [Expected]" + userlistUnassign.size()
					 + " [Actual]" + temp.size();
				}
				
				ArrayList<String> temp2 = getIDUsersArray(driver, XPATH_USERBOX_ASSIGNLIST);
				if(!userlistAssign.containsAll(temp2)){
					failMsg = failMsg + "\n1-2. assigned list(after delete user) [Expected]" + userlistAssign.size()
					 + " [Actual]" + userlistAssign.size();
				}
				
				//추가동작
				List<WebElement> allList = driver.findElements(By.xpath(XPATH_USERBOX_UNASSIGNLIST));
				user = allList.get(1).findElement(By.xpath(".//div[@class='user-holder']")).getText();
				allList.get(1).findElement(By.xpath(".//input[@class='checkbox']")).click();
				Thread.sleep(500);
				//추가 클릭
				driver.findElement(By.xpath(XPATH_USERBOX_ADD_BTN)).click();
				Thread.sleep(1000);
				userlistUnassign.remove(user);
				userlistAssign.add(user);
				
				temp = getIDUsersArray(driver, XPATH_USERBOX_UNASSIGNLIST);
				if(!userlistUnassign.containsAll(temp)){
					failMsg = failMsg + "\n2-1. unassigned list(after add user) [Expected]" + userlistUnassign.size()
					 + " [Actual]" + temp.size();

				}
				
				temp2 = getIDUsersArray(driver, XPATH_USERBOX_ASSIGNLIST);
				if(!userlistAssign.containsAll(temp2)){
					failMsg = failMsg + "\n2-2. assigned list(after add user) [Expected]" + userlistAssign.size()
					 + " [Actual]" + temp2.size();
				}

			}
		
			//click cancel
			driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		} else {
			failMsg = failMsg + "\n3. payment license is empty.";
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 14, dependsOnMethods = {"payment_IDUser"}, alwaysRun = true, enabled = true)
	public void payment_IDmoveUserSave() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_PAYMENTLIST)) {
			driver.get(CommonValues.MEETING_URL + URL_PAYMENTLIST);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		}
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() >0 ) {
			//사용자지정 클릭
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_USERBOX_LOADING)));
			
			ArrayList<String> userlistUnassign = getIDUsersArray(driver, XPATH_USERBOX_UNASSIGNLIST);
			ArrayList<String> userlistAssign = getIDUsersArray(driver, XPATH_USERBOX_ASSIGNLIST);
			
			List<WebElement> list = driver.findElements(By.xpath(XPATH_USERBOX_ASSIGNLIST));
			if(list.size() > 0) {
				//할당된 ID중 1개 체크박스 클릭
				String user = list.get(1).findElement(By.xpath(".//div[@class='user-holder']")).getText();
				list.get(1).findElement(By.xpath(".//input[@class='checkbox']")).click();
				Thread.sleep(500);
				
				//해제 클릭
				driver.findElement(By.xpath(XPATH_USERBOX_DELETE_BTN)).click();
				Thread.sleep(1000);
				userlistAssign.remove(user);
				userlistUnassign.add(user);
				
				//추가동작
				List<WebElement> allList = driver.findElements(By.xpath(XPATH_USERBOX_UNASSIGNLIST));
				user = allList.get(1).findElement(By.xpath(".//div[@class='user-holder']")).getText();
				allList.get(1).findElement(By.xpath(".//input[@class='checkbox']")).click();
				Thread.sleep(500);
				//추가 클릭
				driver.findElement(By.xpath(XPATH_USERBOX_ADD_BTN)).click();
				Thread.sleep(1000);
				userlistUnassign.remove(user);
				userlistAssign.add(user);
			}
			//click save
			driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_Y)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
			
			lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
			lic.get(0).findElement(By.xpath("./td[5]/button")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_USERBOX_LOADING)));

			ArrayList<String> temp = getIDUsersArray(driver, XPATH_USERBOX_ASSIGNLIST);
			if(!userlistAssign.containsAll(temp)) {
				failMsg = failMsg + "\n2. assigned list(after save) [Expected]" + userlistAssign.size()
				 + " [Actual]" + temp.size();
			}
			
			//click cancel
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_MODALDIALOG_FOOTER_N)));
			driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			
		} else {
			failMsg = failMsg + "\n2. payment license is empty.";
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void payment_IDassignPopupY() throws Exception {
		String failMsg = "";

		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_ADMIN_LOGIN_EMAIL)));
		
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(ADM_NOID);
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_PW)).sendKeys(DashBoard.ID_PW);
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_BTN)).click();
			
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_LOGINPOPUP_ID_ASSIGN)) {
			failMsg = failMsg + "\n1. popup msg [Expected]" +MSG_LOGINPOPUP_ID_ASSIGN
			 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		
		//click 사용자 지정하기
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		
		if(!driver.findElement(By.xpath(XPATH_MODALDIALOG_TITLE)).getText().contentEquals(MSG_ADDUSERPOPUP_TITLE)) {
			failMsg = "\n2. add user popup title [Expected]" + MSG_ADDUSERPOPUP_TITLE
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_MODALDIALOG_TITLE)).getText();
		}
		
		//click cancel
		driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 21, enabled = true)
	public void payment_IDassignPopupN() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_ADMIN_LOGIN_EMAIL)));
		
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_EMAIL)).clear();
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_EMAIL)).sendKeys(ADM_NOID);
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_PW)).clear();
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_PW)).sendKeys(DashBoard.ID_PW);
		driver.findElement(By.xpath(Connect.XPATH_ADMIN_LOGIN_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_LOGINPOPUP_ID_ASSIGN)) {
			failMsg = failMsg + "\n1. popup msg [Expected]" +MSG_LOGINPOPUP_ID_ASSIGN
			 + " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		
		//click 다음에
		driver.findElement(By.xpath(Connect.XPATH_MODAL_FOOTER_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + DashBoard.DASHBOARD_URI)) {
			failMsg = failMsg + "\n2. no dash board url. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 30, enabled = true)
	public void payment_addIDbutton() throws Exception {
		String failMsg = "";
		
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, ADM_IDHISTORY, DashBoard.ID_PW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
			//click 사용자 지정하기
			driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
			//click cancel
			driver.findElement(By.xpath(XPATH_MODALDIALOG_FOOTER_N)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_MODALDIALOG_TITLE)));
		} catch (Exception e) {
			// do not anything
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_PAYMENTLIST)) {
			driver.get(CommonValues.MEETING_URL + URL_PAYMENTLIST);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		}
		
		//추가 버튼 확인
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_CURRENT_LIST));
		if(lic.size() > 0 ) {
			if(lic.get(0).findElement(By.xpath("./td[5]/a")).getText().contentEquals("추가")) {
				lic.get(0).findElement(By.xpath("./td[5]/a")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text']")));
				
				if(!driver.findElement(By.xpath("//div[@class='panel-header']/div[1]")).getText().contentEquals("요금제 결제")) {
					failMsg = failMsg + "\n1. view title [Expected]" + "요금제 결제"
							 + " [Actual]" + driver.findElement(By.xpath("//div[@class='panel-header']/div[1]")).getText();
				}
			} else {
				failMsg = failMsg + "\n2. cannot find add id button.";
			}
			
		} else {
			failMsg = failMsg + "\n3. payment license is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 31, dependsOnMethods = {"payment_addIDbutton"}, alwaysRun = true,  enabled = true)
	public void payment_checkBill() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_PAYMENTLIST)) {
			driver.get(CommonValues.MEETING_URL + URL_PAYMENTLIST);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_PAYMENT_CURRENT_LIST)));
		}
		
		List<WebElement> lic = driver.findElements(By.xpath(XPATH_PAYMENT_PAST_LIST));
		boolean findbutton = false;
		if(lic.size() >0 ) {
			for (WebElement webElement : lic) {
				try {
					webElement.findElement(By.xpath("./td[9]/button")).click();;
					findbutton = true;
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-body bill']")));
					if(!driver.findElement(By.xpath("//div[@class='modal-body bill']/p")).getText().contentEquals("RemoteMeeting 청구서")) {
						failMsg = failMsg + "\n1. bill popup title [Expected]" + "RemoteMeeting 청구서"
								+ " [Actual]" + driver.findElement(By.xpath("//div[@class='modal-body bill']/p")).getText();
					}
					
					//check ui : todo
					
					// 닫기 클릭
					driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='modal-body bill']")));
					break;
				} catch (Exception e) {
					//do not anything
				}
			}
			
			if(!findbutton) {
				failMsg = failMsg + "\n2. cannot find bill button";
			}
			
		} else {
			failMsg = failMsg + "\n3. past license list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 40, enabled = true)
	public void payment_noLicAdm() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Connect connect = new Connect();
		connect.logoutAdmin(driver);

		CommonValues comm = new CommonValues();
		driver.get(CommonValues.MEETING_URL);
		comm.login(driver, DashBoard.ID_ADM, DashBoard.ID_PW);
		
		driver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		//회의개설 시도
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)));
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys("test1");
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		String msg = alert.getText();
		alert.accept();
		
		if(!msg.contentEquals(MSG_NOLICENSE_ADM)) {
			failMsg = failMsg + "\n1. license error msg [Expected]" + MSG_NOLICENSE_ADM
					+ " [Actual]" + msg;
		}
		
		driver.findElement(By.xpath("//button[@class='dialog-close']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 41, enabled = true)
	public void payment_noLicUser() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);

		CommonValues comm = new CommonValues();
		comm.logout(driver);
		driver.get(CommonValues.MEETING_URL);
		comm.login(driver, CommonValues.USER_NOLIC, CommonValues.USERPW);
		
		driver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		//회의개설 시도
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)));
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys("test1");
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		String msg = alert.getText();
		alert.accept();
		
		if(!msg.contentEquals(MSG_NOLICENSE_USER)) {
			failMsg = failMsg + "\n1. license error msg [Expected]" + MSG_NOLICENSE_USER
					+ " [Actual]" + msg;
		}
		
		driver.findElement(By.xpath("//button[@class='dialog-close']")).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public String[][] getIDUsers(WebDriver wd, String xpath){
		List<WebElement> userlist = wd.findElements(By.xpath(xpath));
		
		String[][] users = new String[userlist.size()-1][3];
		
		for (int i = 0; i < userlist.size()-1; i++) {
			String userinfo[] = userlist.get(i).findElement(By.xpath(".//div[@class='user-holder']")).getText().split(" ");
			
			users[i][0] = userinfo[0];
			users[i][1] = userinfo[1];
			if(userinfo.length == 3)
				users[i][2] = userinfo[2];
			else
				users[i][2] = "";
		}
		
		return users;
	}
	
	public ArrayList<String> getIDUsersArray(WebDriver wd, String xpath){
		List<WebElement> userlist = wd.findElements(By.xpath(xpath));
		
		ArrayList<String> users = new ArrayList<>();
		
		for (int i = 0; i < userlist.size()-1; i++) {
			if(!userlist.get(i).getAttribute("class").contains("no-data"))
				users.add(userlist.get(i).findElement(By.xpath(".//div[@class='user-holder']")).getText());
		}
		
		return users;
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


