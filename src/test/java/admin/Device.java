package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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


/* Device
 * 1. 장비관리 리스트 이동
 * 2. 장비관리 리스트 검색 
 * 3. 장비관리 리스트 모드 확인 - 카드/리스트
 * 10. 장비관리 - 신규등록 화면 확인
 * 11. 장비관리 신규등록 화면 필수값 누락 
 * 12. 장비관리 - 신규등록 잘못된 시리얼번호 유효성 확인
 * 13. 장비관리 - 신규등록 정상 시리얼번호 유효성 확인
 * 14. 장비관리 - 신규등록 룸 번호 할당
 * 15. 장비관리 - 신규등록 화면에서 목록으로 이동
 * 20. 장비관리 - 기존 등록된 시리얼 번호로 재등록 
 * 21. 장비관리 - 등록된 장비 룸 번호 수정
 * 30. 장비관리 - 새로운 시리얼 번호로 장비 등
 * 31. 장비관리 - 장비리스트 선택하여 장비정보로 이동
 * 32. 장비관리 - 장비정보 수정 - 이미 등록된 장비의 룸번호 중복 
 * 33. 장비관리 - 장비정보 화면에서 목록 이동
 * 34. 장비관리 - 장비정보 수정
 * 35. 장비관리 - 등록된 장비 삭제
 */

public class Device {
	public static String XPATH_RIGTHMENU_TOGGLE_BTN = "//div[@class='right-info']/button[@id='toggleType']";
	public static String XPATH_DEVICELIST_CARD = "//section[@class='wrap-list']//ul[@class='wrap-card']/li";
	public static String XPATH_DEVICELIST_LIST = "//section[@class='wrap-list']//tbody[@id='companyListWrapper']/tr";
	
	public static String XPATH_DEVICENEW_NAME = "//input[@id='equipmentName']";
	public static String XPATH_DEVICENEW_NAME_ERROR = "//span[@id='equipmentName-error']";
	public static String XPATH_DEVICENEW_SERIAL = "//input[@id='serialNumber']";
	public static String XPATH_DEVICENEW_SERIAL_ERROR = "//span[@id='serialNumber-error']";
	public static String XPATH_DEVICENEW_SERIAL_BTN = "//button[@id='validButton']";
	public static String XPATH_DEVICENEW_ROOMNO = "//input[@id='conferenceRoom.id']";
	
	public static String XPATH_DEVICEVIEW_ROOMNO = "//input[@name='conferenceRoom.id']";
	public static String XPATH_DEVICEVIEW_SAVE_BTN = "//div[@class='layer-right']/button[@id='modify-submit']";
	public static String XPATH_DEVICEVIEW_LIST_BTN = "//div[@class='layer-right']/button[1]";
	public static String XPATH_DEVICEVIEW_DELETE_BTN = "//div[@class='layer-left']/button[1]";
	public static String XPATH_DEVICEVIEW_NAME = "//input[@id='name']";
	public static String XPATH_DEVICEVIEW_PLACE = "//input[@id='place']";
	public static String XPATH_DEVICEVIEW_MEMO = "//textarea[@id='memo']";
	
	public static String MSG_PANELHEADER_DEVICE = "장비관리";
	public static String MSG_DEVICENEW_TITLE = "신규등록";
	public static String MSG_DEVICELISTBTN_CARD = "카드로 보기";
	public static String MSG_DEVICELISTBTN_LIST = "리스트로 보기";
	public static String MSG_TOAST_VALIDDEIVCE = "유효한 장비입니다.";
	public static String MSG_TOAST_INVALIDDEIVCE = "잘못된 시리얼번호입니다. 다시 확인 해주세요.";
	public static String MSG_TOAST_DEVICEROOM_DUPLICATE = "이(가) 동일한 회의실에 할당되어 있습니다.\n"
			+ "계속 진행하시겠습니까?";

	public static String URL_DEVICE = "/customer/box-list";
	public static String URL_DEVICENEW = "/customer/box-new";
	public static String URL_DEVICEVIEW = "/customer/box-view";
	
	public static String SERIALNUMBER_NEW = "D0E3-6FP1L-69CK-R7LP";
	public static String SERIALNUMBER_OLD = "C0B3-6JZ8X-43FY-WJ6A";
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		context.setAttribute("webDriver", driver);

	}
	
	@Test(priority = 1, enabled = true)
	public void deviceMenu() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 장비 관리
		Users user = new Users();
		user.selectSideMenu(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText().contentEquals(MSG_PANELHEADER_DEVICE)) {
			failMsg = failMsg + "\n1. device list pannel [Expected]" + MSG_PANELHEADER_DEVICE
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void device_search() throws Exception {
		String failMsg = "";

		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			user.selectSideMenu(driver, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys("invalid name");
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		
		if(listItem.size() > 0) {
			if(!listItem.get(0).getAttribute("class").contentEquals("no-data")) {
				failMsg = failMsg + "\n1. searched list item is not empty. searched item size : " + listItem.size();
			}
		}
		
		String name = "장비";
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCHBOX)).sendKeys(name);
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(!webElement.findElement(By.xpath(".//div[@class='inner-name']")).getText().contains(name)) {
					failMsg = failMsg + "\n2. searched item name [Expected]contains : " + name 
							+ "[Actual]" + webElement.findElement(By.xpath(".//div[@class='inner-name']")).getText();
				}
			}
		} else {
			failMsg = failMsg + "\n3. searched list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void device_listMode() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			user.selectSideMenu(driver, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		//clear
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCHBOX)).clear();
		driver.findElement(By.xpath(Users2.XPATH_ADMIN_LIST_SEARCH_BTN)).click();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(XPATH_DEVICELIST_CARD)).isDisplayed() 
				&& driver.findElement(By.xpath(XPATH_DEVICELIST_LIST)).isDisplayed()) {
			failMsg = failMsg + "\n1. list is not card style. ";
		}
		
		if(!driver.findElement(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)).getText().contentEquals(MSG_DEVICELISTBTN_LIST)) {
			failMsg = failMsg + "\n2. list mode button title [Expeted]" + MSG_DEVICELISTBTN_LIST
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)).getText();
		}
		
		//click list mode button (change list mode)
		driver.findElement(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)).click();
		Thread.sleep(1000);
		
		if(driver.findElement(By.xpath(XPATH_DEVICELIST_CARD)).isDisplayed() 
				&& !driver.findElement(By.xpath(XPATH_DEVICELIST_LIST)).isDisplayed()) {
			failMsg = failMsg + "\n3. list is not list style. ";
		}
		
		if(!driver.findElement(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)).getText().contentEquals(MSG_DEVICELISTBTN_CARD)) {
			failMsg = failMsg + "\n4. list mode button title [Expeted]" + MSG_DEVICELISTBTN_CARD
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)).getText();
		}
		
		//reset mode(card)
		driver.findElement(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)).click();
		Thread.sleep(1000);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =10, enabled = true)
	public void device_deviceNew() throws Exception {
		String failMsg = "";
		
		Users user = new Users();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			user.selectSideMenu(driver, 2);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		//click new device
		driver.findElement(By.xpath(Users.XPATH_ADMIN_RIGHT_BTNS + "[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		
		if(!driver.findElement(By.xpath(Users2.XPATH_ADDUSER_VIEW_TITLE)).getText().contentEquals(MSG_DEVICENEW_TITLE)) {
			failMsg = failMsg + "\n1. new device view title [Expeted]" + MSG_DEVICENEW_TITLE
					 + " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_ADDUSER_VIEW_TITLE)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =11, dependsOnMethods = {"device_deviceNew"}, enabled = true)
	public void device_newEssential() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICENEW)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICENEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		}
		
		//click save
		driver.findElement(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICENEW_NAME_ERROR)));
		
		if(!driver.findElement(By.xpath(XPATH_DEVICENEW_NAME_ERROR)).getText().contentEquals(Users2.MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n1. miss device name error [Expeted]" + Users2.MSG_MISS_ESSENTIAL
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICENEW_NAME_ERROR)).getText();
		}
		
		if(!driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL_ERROR)).getText().contentEquals(Users2.MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n2. miss device serial number error [Expeted]" + Users2.MSG_MISS_ESSENTIAL
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL_ERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =12, enabled = true)
	public void device_serialInvalid() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICENEW)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICENEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		}
		
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).clear();
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).sendKeys("invalidSerial");
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_TOAST_INVALIDDEIVCE)) {
			failMsg = failMsg + "\n1. invalid serial number error [Expeted]" +MSG_TOAST_INVALIDDEIVCE
					 + " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority =13, enabled = true)
	public void device_serialValid() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICENEW)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICENEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		}
		
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).clear();
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).sendKeys(SERIALNUMBER_NEW);
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText().contentEquals(MSG_TOAST_VALIDDEIVCE)) {
			failMsg = failMsg + "\n1. valid serial number toast msg [Expeted]" +MSG_TOAST_VALIDDEIVCE
					 + " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =14, enabled = true)
	public void device_roomNumber() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICENEW)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICENEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		}
		
		if(!driver.findElement(By.xpath(XPATH_DEVICENEW_ROOMNO)).getAttribute("value").contentEquals("2")) {
			failMsg = failMsg + "\n1. new device view room number [Expeted]2" 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICENEW_ROOMNO)).getAttribute("value");
		}
			
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =15, enabled = true)
	public void device_newToList() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICENEW)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICENEW);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		}
		
		//click 목록
		driver.findElement(By.xpath(Users2.XPATH_USERVIEW_LIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			failMsg = failMsg + "\n1. no device list view. current url : " + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =20, enabled = true)
	public void device_regOldSerial() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		//click new device
		driver.findElement(By.xpath(Users.XPATH_ADMIN_RIGHT_BTNS + "[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		
		driver.findElement(By.xpath(XPATH_DEVICENEW_NAME)).clear();
		driver.findElement(By.xpath(XPATH_DEVICENEW_NAME)).sendKeys("장비_OldSerial");
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).click();
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).sendKeys(SERIALNUMBER_OLD);
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		driver.findElement(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		boolean findDevice = false;
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_OLD)) {
					findDevice = true;
					break;
				}
			}
			if(!findDevice) {
				failMsg = failMsg + "\n1. cannot find device. device num : " + SERIALNUMBER_OLD;
			}
		} else {
			failMsg = failMsg + "\n2. searched list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority =21, enabled = true)
	public void device_editRoomNo() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		boolean findItem = false;
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_OLD)) {
					webElement.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
					findItem = true;
					break;
				}
			}
			
			if(findItem) {
				String serialNo = driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value");
				if(!serialNo.equals("2")) {
					failMsg = failMsg + "\n1. registed device room number [Expeted]2 [Actual]" + serialNo;
				}
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).clear();
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).sendKeys("1");
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
				driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
				
				driver.navigate().refresh();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
				
				if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value").contentEquals("1")) {
					failMsg = failMsg + "\n2. room number is not changed [Expeted]1 [Actual]" 
							+ driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value");
				}
			} else {
				failMsg = failMsg + "\n1. cannot find device. device num : " + SERIALNUMBER_OLD;
			}
		
		} else {
			failMsg = failMsg + "\n4. searched list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 30, enabled = true)
	public void device_regNew() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		//click new device
		driver.findElement(By.xpath(Users.XPATH_ADMIN_RIGHT_BTNS + "[2]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)));
		
		driver.findElement(By.xpath(XPATH_DEVICENEW_NAME)).clear();
		driver.findElement(By.xpath(XPATH_DEVICENEW_NAME)).sendKeys("NewSerial");
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).click();
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL)).sendKeys(SERIALNUMBER_NEW);
		driver.findElement(By.xpath(XPATH_DEVICENEW_SERIAL_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		driver.findElement(By.xpath(Users2.XPATH_ADDUSER_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		boolean findDevice = false;
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_NEW)) {
					findDevice = true;
					break;
				}
			}
			if(!findDevice) {
				failMsg = failMsg + "\n1. cannot find device. device num : " + SERIALNUMBER_NEW;
			}
		} else {
			failMsg = failMsg + "\n2. searched list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 31, enabled = true)
	public void device_listItemClick() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		boolean findDevice = false;
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_NEW)) {
					webElement.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
					findDevice = true;
					break;
				}
			}
			if(!findDevice) {
				failMsg = failMsg + "\n1. cannot find device. device num : " + SERIALNUMBER_NEW;
			} else {
				if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICEVIEW)) {
					failMsg = failMsg + "\n2. no device info view. current url : " + driver.getCurrentUrl();
				}
			}
		} else {
			failMsg = failMsg + "\n3. searched list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 32, dependsOnMethods = {"device_listItemClick"},  enabled = true)
	public void device_roomNoDuplicate() throws Exception {
		String failMsg = "";
		
		String roomNo = driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value");
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).clear();
		driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).sendKeys("1");
		driver.findElement(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText").contains(MSG_TOAST_DEVICEROOM_DUPLICATE)) {
			failMsg = failMsg + "\n1. popup msg(duplicated room No.) [Expeced]" + MSG_TOAST_DEVICEROOM_DUPLICATE
					 + " [Actual]" +driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText");
		}
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_N)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		
		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
		
		if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value").contentEquals(roomNo)) {
			failMsg = failMsg + "\n2. room No is Changed [Expeced]" + roomNo
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value");
		}
		
		driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).clear();
		driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).sendKeys("1");
		driver.findElement(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_MODAL_NOHEADER)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
		
		if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value").contentEquals("1")) {
			failMsg = failMsg + "\n3. room No is not Changed [Expeced]" + "1"
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 33, dependsOnMethods = {"device_listItemClick"},  enabled = true)
	public void device_viewToList() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICEVIEW)) {
			failMsg = failMsg + "\n1. no device info view. current url : " + driver.getCurrentUrl();
		} else {
			//click 목록
			driver.findElement(By.xpath(XPATH_DEVICEVIEW_LIST_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
			
			if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
				failMsg = failMsg + "\n2. no device list view. current url : " + driver.getCurrentUrl();
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 34, dependsOnMethods = {"device_listItemClick"},  enabled = true)
	public void device_edit() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		boolean findDevice = false;
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_NEW)) {
					webElement.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
					findDevice = true;
					break;
				}
			}
			if(!findDevice) {
				failMsg = failMsg + "\n1. cannot find device. device num : " + SERIALNUMBER_NEW;
			} else {
				String name = "name_edit";
				String place = "place_edit";
				String memo = "memo_edit";
				
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_NAME)).clear();
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_NAME)).sendKeys(name);
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_PLACE)).clear();
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_PLACE)).sendKeys(place);
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_MEMO)).clear();
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_MEMO)).sendKeys(memo);
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).clear();
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).sendKeys("10");
				
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)));
				driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
				
				driver.navigate().refresh();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
				
				if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_NAME)).getAttribute("value").contentEquals(name)) {
					failMsg = failMsg + "\n2.edited name [Expected]" + name 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICEVIEW_NAME)).getAttribute("value");
				}
				if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_PLACE)).getAttribute("value").contentEquals(place)) {
					failMsg = failMsg + "\n3.edited place [Expected]" + place 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICEVIEW_PLACE)).getAttribute("value");
				}
				if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_MEMO)).getAttribute("value").contentEquals(memo)) {
					failMsg = failMsg + "\n4.edited memo [Expected]" + memo 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICEVIEW_MEMO)).getAttribute("value");
				}
				if(!driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value").contentEquals("10")) {
					failMsg = failMsg + "\n5.edited room No. [Expected]" + "10" 
							+ " [Actual]" + driver.findElement(By.xpath(XPATH_DEVICEVIEW_ROOMNO)).getAttribute("value");
				}
			}
		} else {
			failMsg = failMsg + "\n3. searched list is empty.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 35, dependsOnMethods = {"device_listItemClick"},  enabled = true)
	public void device_delete() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_DEVICE)) {
			driver.get(CommonValues.MEETING_URL + URL_DEVICE);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
		}
		
		List<WebElement> listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
		boolean findDevice = false;
		if(listItem.size() > 0) {
			for (WebElement webElement : listItem) {
				if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_NEW)) {
					webElement.click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_DEVICEVIEW_SAVE_BTN)));
					findDevice = true;
					break;
				}
			}
			if(!findDevice) {
				failMsg = failMsg + "\n1. cannot find device. device num : " + SERIALNUMBER_NEW;
			} else {
				driver.findElement(By.xpath(XPATH_DEVICEVIEW_DELETE_BTN)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
				driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
				driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_RIGTHMENU_TOGGLE_BTN)));
				
				listItem = driver.findElements(By.xpath(XPATH_DEVICELIST_CARD));
				
				for (WebElement webElement : listItem) {
					if(webElement.findElement(By.xpath(".//div[@class='wrap-info']")).getText().contains(SERIALNUMBER_NEW)) {
						failMsg = failMsg + "\n2. fail to delete device. find device in list. device num : " + SERIALNUMBER_NEW;
						break;
					}
				}
			}
		} else {
			failMsg = failMsg + "\n3. searched list is empty.";
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
