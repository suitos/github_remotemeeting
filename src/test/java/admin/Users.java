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


/* Users
 * 1. 사용자 관리 화면 이동
 * 2. 사용자 관리 - 엑셀 업로드 팝업 확인 / 엑셀양식 다운로드
 * 3. 엑셀파일 업로드에 추가
 */

public class Users {
	public static String XPATH_MODAL_FOOTER_BTN_Y  = "//div[@class='modal-footer']/button[1]";
	public static String XPATH_MODAL_FOOTER_BTN_N  = "//div[@class='modal-footer']/button[2]";

	public static String XPATH_ADMIN_SIDEBAR_MENU = "//ul[@class='sidebar-menu']/li";
	public static String XPATH_ADMIN_RIGHT_BTNS = "//div[@class='right-info']/button";
	public static String XPATH_ADMIN_VIEWLIST = "//tbody[@class='searchable']/tr";
	
	public static String XPATH_MODAL_DOWNLOAD_FORMFILE = Connect.XPATH_MODAL_BODY + "//a";
	public static String XPATH_MODAL_UPLOAD_FIELD = Connect.XPATH_MODAL_BODY + "//input[@id='uploadFile']";
	public static String XPATH_MODAL_UPLOAD_TXT = Connect.XPATH_MODAL_BODY + "//div[@class='file-drag-area attatched']";
	public static String XPATH_MODAL_UPLOAD_ERROR = Connect.XPATH_MODAL_BODY + "//span[@id='uploadFile-error']";
	
	public static String MSG_PANELHEADER_USERS = "사용자 관리";
	public static String MSG_POPUP_EXCELUPLOAD  = "엑셀을 업로드하여 다수의 사용자 정보를 한번에 입력할 수 있습니다.\n"
			+ "정해진 엑셀 양식에 입력하여 업로드 하세요.(지원하는 파일 양식 : xlsx)\n\n"
			+ "엑셀 양식 다운로드\n\n"
			+ "클릭 또는 드래그해서 파일을 선택해 주세요.";
	public static String[] EXCEL_USER_FORM = {"이름", "이메일", "부서", "직급/직책", "전화번호", "핸드폰"};
	public static String MSG_MODAL_UPLOAD_ERROR = "엑셀파일만 업로드 가능합니다.";
	
	public static String userExcelSample = "user-sampledata.xlsx";
	public static String URL_USERS = "/customer/user-list";
			
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
	public void userMenu() throws Exception {
		String failMsg = "";

		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoard.XPATH_DASHBOARD_LICENSE_TITLE)));
		
		//click 사용자 관리
		selectSideMenu(driver, 1);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_PANEL_HEADER)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_PANEL_HEADER)).getText().contentEquals(MSG_PANELHEADER_USERS)) {
			failMsg = "\n1. user management view title [Expected]" + MSG_PANELHEADER_USERS;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = false)
	public void user_excelPopup() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERS)) {
			//click 사용자 관리
			selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_PANEL_HEADER)));
		}
	
		//click excel upload btn
		driver.findElement(By.xpath(XPATH_ADMIN_RIGHT_BTNS + "[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText").contentEquals(MSG_POPUP_EXCELUPLOAD)) {
			failMsg = "\n1. excel upload popup msg [Expected]" + MSG_POPUP_EXCELUPLOAD 
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_BODY)).getAttribute("innerText");
		}
		
		//click download excel form
		driver.findElement(By.xpath(XPATH_MODAL_DOWNLOAD_FORMFILE)).click();
		Thread.sleep(5000);
		
		String formFilePath = getExcelpath("user-sample");
		
		String[][] excelData = readExcelFile(formFilePath);
		
		//check format
		if(excelData.length > 1 || excelData.length == 0) {
			failMsg = "\n1. form file row size [Expected]1 [Actual]" + excelData.length;
		} else {
			for (int i = 0; i < excelData[0].length; i++) {
				if(!excelData[0][i].contentEquals(EXCEL_USER_FORM[i])) {
					failMsg = "\n2-" + i +". excel file form [Expected]" + EXCEL_USER_FORM[i] 
							+ excelData[0][i];
				}
			}
		}
		
		deleteFile(formFilePath);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = false)
	public void user_excelAdd() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERS)) {
			//click 사용자 관리
			selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_PANEL_HEADER)));
		}
	
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		} catch (Exception e) {
			//click excel upload btn
			driver.findElement(By.xpath(XPATH_ADMIN_RIGHT_BTNS + "[1]")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		}
		
		driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_FIELD)).sendKeys(CommonValues.TESTFILE_PATH + userExcelSample);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_UPLOAD_TXT)));
		if(!driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_TXT)).getText().contentEquals(userExcelSample)) {
			failMsg = "\n1. uploaded file name [Expected]" + userExcelSample 
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_TXT)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = false)
	public void user_excelAddInvalid() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERS)) {
			//click 사용자 관리
			selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_PANEL_HEADER)));
		}
	
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		} catch (Exception e) {
			//click excel upload btn
			driver.findElement(By.xpath(XPATH_ADMIN_RIGHT_BTNS + "[1]")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		}
		
		// doc 파일 업로드 시도
		driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_FIELD)).sendKeys(CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0]);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_UPLOAD_TXT)));
		
		// 저장 클릭
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_UPLOAD_ERROR)));
			
			if(!driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_ERROR)).getText().contentEquals(MSG_MODAL_UPLOAD_ERROR)) {
				failMsg = failMsg + "\n1. invalid file upload error msg [Expected]" + XPATH_MODAL_UPLOAD_ERROR
						 + " [Actual]" + driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_ERROR)).getText();
			}
		} catch (Exception e) {
			failMsg = failMsg + "\n2. cannot find error msg.";
		}
		
		// 팝업 cancel
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER_BTN_N)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void user_excelCancel() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERS)) {
			//click 사용자 관리
			selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_PANEL_HEADER)));
		}
	
		//기존 리스트 유저 확인
		String[][] oldList = getListItems(driver);
		
		driver.findElement(By.xpath(XPATH_ADMIN_RIGHT_BTNS + "[1]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_FIELD)).sendKeys(CommonValues.TESTFILE_PATH + userExcelSample);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_UPLOAD_TXT)));
		
		//upload cancel
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER_BTN_N)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		String[][] newList = getListItems(driver);
		
		if(!Arrays.deepEquals(oldList, newList)) {
			failMsg = failMsg + "\n1.User list items diff";
			
			System.out.println("list is no equal");  
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void user_excelExport() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + URL_USERS)) {
			//click 사용자 관리
			selectSideMenu(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_PANEL_HEADER)));
		}
	
		//기존 리스트 유저 확인
		String[][] oldList = getListItems(driver);
		
		driver.findElement(By.xpath(XPATH_ADMIN_RIGHT_BTNS + "[1]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		driver.findElement(By.xpath(XPATH_MODAL_UPLOAD_FIELD)).sendKeys(CommonValues.TESTFILE_PATH + userExcelSample);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_MODAL_UPLOAD_TXT)));
		
		//upload cancel
		driver.findElement(By.xpath(XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_BODY)));
		
		String[][] newList = getListItems(driver);
		
		if(!Arrays.deepEquals(oldList, newList)) {
			failMsg = failMsg + "\n1.User list items diff";
			
			System.out.println("list is no equal");  
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	//사이드 메뉴 선택 사용자관리가 1번 이후 +1
	public void selectSideMenu(WebDriver wd, int num) {
		List<WebElement> menus = wd.findElements(By.xpath(XPATH_ADMIN_SIDEBAR_MENU));
	
		if(menus.size() > 0) {
			menus.get(num).click();
		}
	}	
	
	public String getExcelpath(String filename) {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		int num = 1;
		
		String home = System.getProperty("user.home");
		if (os.contains("windows")) {
			
			path = home + "\\Downloads\\" + filename + ".xlsx";
			File file = new File(path);

			if (!file.exists()) {
				while (true) {
					num++;
					path = home + "\\Downloads\\" + filename + " (" + num + ").xlsx";
					File file2 = new File(path);
					if (file2.exists())
						break;
					}
			} 
		} else {
			path = home + "/Downloads/" + filename + ".xlsx";
		}
		return path;
	}

	public void deleteFile(String filepath) throws Exception {
		
	    File file = new File(filepath);

	    try {
	        if (file.exists()) {
	            file.delete();
	            System.out.println("File is delete");
	        } else {
	            
	            System.out.println("File is not exist");  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
	private String[][] readExcelFile(String filepath) throws Exception {

		File file = new File(filepath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook testDataWorkBook = new XSSFWorkbook(inputStream);
		Sheet testDataSheet = testDataWorkBook.getSheetAt(0);

		int rowCount = testDataSheet.getLastRowNum();
		rowCount = (rowCount>0?rowCount:rowCount+1);
		int cells = testDataSheet.getRow(0).getPhysicalNumberOfCells();

		DataFormatter formatter = new DataFormatter();

		String[][] data = new String[rowCount][cells];
		
		//첫번째 행 포함 가져오기.
		for (int i = 0; i <  rowCount; i++) {
			Row row = testDataSheet.getRow(i);
			for (int j = 0; j < cells; j++) {
				
				Cell cell = row.getCell(j);
				String a = formatter.formatCellValue(cell);

				data[i][j] = a;
			}
		}
		testDataWorkBook.close();
		return data;
	}
	
	public String[][] getListItems(WebDriver wd){
		
		List<WebElement> listItem = wd.findElements(By.xpath(XPATH_ADMIN_VIEWLIST));
		
		int rows = listItem.size();
		int cols = 3; //필수값인 3개 만 확인
		if(rows> 0 ) {
			String[][] items = new String[rows][cols];
			
			String colform = "./td[%d]";
			
			for (int i = 0; i < rows; i++) {
				
				for (int j = 0; j < cols; j++) {
					//1부터 시작, 체크박스 제외 +2 확인
					items[i][j] = listItem.get(i).findElement(By.xpath(String.format(colform, j+2))).getText();
					
					System.out.println("row test : " + items[i][j]);  
				}
			}
			
			return items;
			
		} else {
			return null;
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
